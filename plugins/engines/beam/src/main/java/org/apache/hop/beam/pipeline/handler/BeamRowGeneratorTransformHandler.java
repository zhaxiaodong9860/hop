/*! ******************************************************************************
 *
 * Hop : The Hop Orchestration Platform
 *
 * http://www.project-hop.org
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.apache.hop.beam.pipeline.handler;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.Read;
import org.apache.beam.sdk.io.synthetic.SyntheticBoundedSource;
import org.apache.beam.sdk.io.synthetic.SyntheticSourceOptions;
import org.apache.beam.sdk.io.synthetic.SyntheticUnboundedSource;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.hop.beam.core.HopRow;
import org.apache.hop.beam.core.fn.StaticHopRowFn;
import org.apache.hop.beam.core.util.JsonRowMeta;
import org.apache.hop.beam.engines.IBeamPipelineEngineRunConfiguration;
import org.apache.hop.core.Const;
import org.apache.hop.core.ICheckResult;
import org.apache.hop.core.RowMetaAndData;
import org.apache.hop.core.exception.HopException;
import org.apache.hop.core.logging.ILogChannel;
import org.apache.hop.core.row.IRowMeta;
import org.apache.hop.metadata.api.IHopMetadataProvider;
import org.apache.hop.pipeline.PipelineMeta;
import org.apache.hop.pipeline.transform.TransformMeta;
import org.apache.hop.pipeline.transforms.rowgenerator.RowGenerator;
import org.apache.hop.pipeline.transforms.rowgenerator.RowGeneratorMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeamRowGeneratorTransformHandler extends BeamBaseTransformHandler implements IBeamTransformHandler {

  public BeamRowGeneratorTransformHandler( IBeamPipelineEngineRunConfiguration runConfiguration, IHopMetadataProvider metadataProvider, PipelineMeta pipelineMeta, List<String> transformPluginClasses, List<String> xpPluginClasses ) {
    super( runConfiguration, false, false, metadataProvider, pipelineMeta, transformPluginClasses, xpPluginClasses );
  }

  public boolean isInput() {
    return true;
  }

  public boolean isOutput() {
    return false;
  }

  @Override public void handleTransform( ILogChannel log, TransformMeta transformMeta, Map<String, PCollection<HopRow>> stepCollectionMap,
                                         Pipeline pipeline, IRowMeta rowMeta, List<TransformMeta> previousSteps,
                                         PCollection<HopRow> input ) throws HopException {

    // Don't simply case but serialize/de-serialize the metadata to prevent classloader exceptions
    //
    RowGeneratorMeta meta = new RowGeneratorMeta();
    loadTransformMetadata(meta, transformMeta, metadataProvider, pipelineMeta);

    List<ICheckResult> remarks = new ArrayList<>();
    final RowMetaAndData rowMetaAndData = RowGenerator.buildRow( meta, remarks, "" );
    if (!remarks.isEmpty()) {
      String message = "There are "+remarks.size()+" remarks concerning the generated rows:"+Const.CR;
      for (ICheckResult remark : remarks) {
        message+=remark.getText()+Const.CR;
      }
      throw new HopException(message);
    }

    String rowMetaJson = JsonRowMeta.toJson(rowMetaAndData.getRowMeta());
    String rowDataXml;
    try {
      rowDataXml = rowMetaAndData.getRowMeta().getDataXml( rowMetaAndData.getData() );
    } catch ( IOException e ) {
      throw new HopException("Error encoding row as XML", e);
    }


    PCollection<HopRow> afterInput;

    if (meta.isNeverEnding()) {
      SyntheticSourceOptions options = new SyntheticSourceOptions();
      options.numRecords = Long.MAX_VALUE;
      long intervalMs = Const.toLong( pipelineMeta.environmentSubstitute( meta.getIntervalInMs() ), -1L);
      if (intervalMs<0) {
        throw new HopException("The interval in milliseconds is expected to be >= 0, not '"+meta.getIntervalInMs()+"'");
      }

      // The processing time delay is a random value between 0 and intervalMs so we need to double this to get the same speed on average.
      //
      options.nextProcessingTimeDelay( intervalMs * 2 );
      options.forceNumInitialBundles = transformMeta.getCopies();

      SyntheticUnboundedSource unboundedSource = new SyntheticUnboundedSource( options );
      Read.Unbounded<KV<byte[], byte[]>> unboundedReader = Read.from( unboundedSource );
      PCollection<KV<byte[], byte[]>> sourceInput = pipeline.apply( unboundedReader );
      String currentTimeField = pipelineMeta.environmentSubstitute( meta.getRowTimeField() );
      int currentTimeFieldIndex = rowMeta.indexOfValue( currentTimeField );
      String previousTimeField = pipelineMeta.environmentSubstitute( meta.getLastTimeField() );
      int previousTimeFieldIndex = rowMeta.indexOfValue( previousTimeField );

      afterInput = sourceInput.apply( ParDo.of(new StaticHopRowFn(
        transformMeta.getName(),
        rowMetaJson,
        rowDataXml,
        true,
        currentTimeFieldIndex,
        previousTimeFieldIndex,
        transformPluginClasses,
        xpPluginClasses
      )));

    } else {
      // A fixed number of records
      //
      SyntheticSourceOptions options = new SyntheticSourceOptions();

      options.numRecords = Const.toLong( pipelineMeta.environmentSubstitute( meta.getRowLimit() ), -1L);
      if (options.numRecords<0) {
        throw new HopException("Please specify a valid number of records to generate, not '"+meta.getRowLimit()+"'");
      }
      options.nextProcessingTimeDelay( 0 );
      options.forceNumInitialBundles = transformMeta.getCopies();
      SyntheticBoundedSource boundedSource = new SyntheticBoundedSource(options);
      Read.Bounded<KV<byte[], byte[]>> boundedReader = Read.from( boundedSource );
      PCollection<KV<byte[], byte[]>> sourceInput = pipeline.apply( boundedReader );

      afterInput = sourceInput.apply( ParDo.of(new StaticHopRowFn(
        transformMeta.getName(),
        rowMetaJson,
        rowDataXml,
        false,
        -1,
        -1,
        transformPluginClasses,
        xpPluginClasses
      )));

    }


    stepCollectionMap.put( transformMeta.getName(), afterInput );
    log.logBasic( "Handled transform (ROW GENERATOR) : " + transformMeta.getName() );
  }



}
