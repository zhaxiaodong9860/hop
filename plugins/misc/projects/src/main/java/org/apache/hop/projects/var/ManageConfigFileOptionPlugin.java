package org.apache.hop.projects.var;

import org.apache.commons.lang.StringUtils;
import org.apache.hop.core.config.DescribedVariable;
import org.apache.hop.core.config.DescribedVariablesConfigFile;
import org.apache.hop.core.config.plugin.ConfigPlugin;
import org.apache.hop.core.config.plugin.IConfigOptions;
import org.apache.hop.core.exception.HopException;
import org.apache.hop.core.logging.ILogChannel;
import org.apache.hop.core.variables.IVariables;
import org.apache.hop.metadata.api.IHopMetadataProvider;
import picocli.CommandLine;

import java.io.File;

@ConfigPlugin(
  id = "ManageConfigFileOptionPlugin",
  description = "Allows command line editing of configuration files"
)
public class ManageConfigFileOptionPlugin implements IConfigOptions {

  @CommandLine.Option( names = { "-cfg", "--config-file" }, description = "Specify the configuration JSON file to manage" )
  private String configFile = null;

  @CommandLine.Option( names = { "-cfv", "--config-file-set-variables" }, description = "A list of variable=value combinations separated by a comma", split = "," )
  private String[] configSetVariables;

  @CommandLine.Option( names = { "-cfd", "--config-file-describe-variables" }, description = "A list of variable=description combinations separated by a comma", split = "," )
  private String[] configDescribeVariables;

  @Override public boolean handleOption( ILogChannel log, IHopMetadataProvider metadataProvider, IVariables variables ) throws HopException {

    String realConfigFile = variables.environmentSubstitute( configFile );
    if ( StringUtils.isEmpty( realConfigFile ) ) {
      return false;
    }

    try {
      boolean changed = false;
      DescribedVariablesConfigFile variablesConfigFile = new DescribedVariablesConfigFile( realConfigFile );
      if ( new File( realConfigFile ).exists() ) {
        variablesConfigFile.readFromFile();
      }

      // Set variable values
      //
      if ( configSetVariables != null && configSetVariables.length > 0 ) {
        for ( String varValue : configSetVariables ) {
          int equalsIndex = varValue.indexOf( "=" );
          if ( equalsIndex > 0 ) {
            String variableName = varValue.substring( 0, equalsIndex );
            String variableValue = varValue.substring( equalsIndex + 1 );
            DescribedVariable describedVariable = variablesConfigFile.findDescribedVariable( variableName );
            if ( describedVariable == null ) {
              describedVariable = new DescribedVariable( variableName, variableValue, "" );
            } else {
              describedVariable.setValue( variableValue );
            }
            variablesConfigFile.setDescribedVariable( describedVariable );
            changed = true;
          }
        }
      }

      // Set variable descriptions
      //
      if ( configDescribeVariables != null && configDescribeVariables.length > 0 ) {
        for ( String varDesc : configDescribeVariables ) {
          int equalsIndex = varDesc.indexOf( "=" );
          if ( equalsIndex > 0 ) {
            String variableName = varDesc.substring( 0, equalsIndex );
            String variableDescription = varDesc.substring( equalsIndex + 1 );
            DescribedVariable describedVariable = variablesConfigFile.findDescribedVariable( variableName );
            if ( describedVariable == null ) {
              describedVariable = new DescribedVariable( variableName, null, variableDescription );
            } else {
              describedVariable.setDescription( variableDescription );
            }
            variablesConfigFile.setDescribedVariable( describedVariable );
            changed = true;
          }
        }
      }

      if (changed) {
        variablesConfigFile.saveToFile();
        log.logBasic("Configuration file '"+configFile+"' was modified.");
      }

      return changed;
    } catch ( Exception e ) {
      throw new HopException( "Error managing a configuration file", e );
    }
  }


  /**
   * Gets configFile
   *
   * @return value of configFile
   */
  public String getConfigFile() {
    return configFile;
  }

  /**
   * @param configFile The configFile to set
   */
  public void setConfigFile( String configFile ) {
    this.configFile = configFile;
  }

  /**
   * Gets configSetVariables
   *
   * @return value of configSetVariables
   */
  public String[] getConfigSetVariables() {
    return configSetVariables;
  }

  /**
   * @param configSetVariables The configSetVariables to set
   */
  public void setConfigSetVariables( String[] configSetVariables ) {
    this.configSetVariables = configSetVariables;
  }

  /**
   * Gets configDescribeVariables
   *
   * @return value of configDescribeVariables
   */
  public String[] getConfigDescribeVariables() {
    return configDescribeVariables;
  }

  /**
   * @param configDescribeVariables The configDescribeVariables to set
   */
  public void setConfigDescribeVariables( String[] configDescribeVariables ) {
    this.configDescribeVariables = configDescribeVariables;
  }
}
