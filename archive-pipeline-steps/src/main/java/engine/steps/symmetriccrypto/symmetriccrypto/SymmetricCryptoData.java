/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
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

package org.apache.hop.pipeline.steps.symmetriccrypto.symmetriccrypto;

import org.apache.hop.core.row.RowMetaInterface;
import org.apache.hop.pipeline.step.BaseStepData;
import org.apache.hop.pipeline.step.StepDataInterface;
import org.apache.hop.pipeline.steps.symmetriccrypto.symmetricalgorithm.SymmetricCrypto;
import org.apache.hop.pipeline.steps.symmetriccrypto.symmetricalgorithm.SymmetricCryptoMeta;

/**
 * Symmetric algorithm Executes a SymmetricCrypto on the values in the input stream. Selected calculated values can
 * then be put on the output stream.
 *
 * @author Samatar
 * @since 5-apr-2003
 */
public class SymmetricCryptoData extends BaseStepData implements StepDataInterface {
  public RowMetaInterface outputRowMeta;
  public int indexOfSecretkeyField;
  public int indexOfMessage;
  public SymmetricCryptoMeta CryptMeta;
  public SymmetricCrypto Crypt;

  public SymmetricCryptoData() {
    super();
    indexOfSecretkeyField = -1;
    indexOfMessage = -1;
  }

}