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

package org.apache.hop.core.plugins;

import org.apache.hop.core.Const;
import org.apache.hop.core.annotations.PluginDialog;
import org.apache.hop.core.exception.HopPluginException;
import org.apache.hop.workflow.action.IActionDialog;

import java.net.URL;
import java.util.List;

/**
 * This class represents the action dialog fragment type.
 */
@PluginMainClassType( IActionDialog.class )
@PluginAnnotationType( PluginDialog.class )
public class ActionDialogFragmentType extends BaseFragmentType<PluginDialog> implements IPluginType<PluginDialog> {

  private static ActionDialogFragmentType actionDialogFragmentType;

  protected ActionDialogFragmentType() {
    super( PluginDialog.class, "JOBENTRYDIALOG", "Plugin Workflow Entry Dialog", ActionPluginType.class );
  }

  public static ActionDialogFragmentType getInstance() {
    if ( actionDialogFragmentType == null ) {
      actionDialogFragmentType = new ActionDialogFragmentType();
    }
    return actionDialogFragmentType;
  }

  @Override
  protected String extractID( PluginDialog annotation ) {
    return annotation.id();
  }

  @Override
  protected String extractImageFile( PluginDialog annotation ) {
    return annotation.image();
  }

  @Override
  protected String extractDocumentationUrl( PluginDialog annotation ) {
    return Const.getDocUrl( annotation.documentationUrl() );
  }

  @Override
  protected String extractCasesUrl( PluginDialog annotation ) {
    return annotation.casesUrl();
  }

  @Override
  protected String extractForumUrl( PluginDialog annotation ) {
    return annotation.forumUrl();
  }

  @Override
  protected String extractSuggestion( PluginDialog annotation ) {
    return null;
  }

  @Override
  public void handlePluginAnnotation( Class<?> clazz, PluginDialog annotation,
                                      List<String> libraries, boolean nativePluginType, URL pluginFolder ) throws HopPluginException {
    if ( ( (PluginDialog) annotation ).pluginType() == PluginDialog.PluginType.ACTION ) {
      super.handlePluginAnnotation( clazz, annotation, libraries, nativePluginType, pluginFolder );
    }
  }
}
