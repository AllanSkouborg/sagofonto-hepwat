package dk.artogis.hepwat.dataconfig.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataconfig.Configuration;
import dk.artogis.hepwat.dataconfig.ConfigurationFormula;

import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationFormulasResponse extends Status implements Serializable
{
    public ConfigurationFormula[] configurationFormulas;

    public ConfigurationFormulasResponse()
    {
    }
    public ConfigurationFormulasResponse(Connection connection, Integer calculationType, Integer dataioid)
    {
        Logger logger = Logger.getLogger(ConfigurationFormulasResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering ConfigurationFormulasResponse");
        this.Success = false;

        try {
            connection.connect();
            List <ConfigurationFormula> configurationFormulasList = ConfigurationFormula.getConfigurationFormulas(connection, calculationType , dataioid );

            if (configurationFormulasList != null ) {
                configurationFormulas = new ConfigurationFormula[configurationFormulasList.size()];
                configurationFormulas = (ConfigurationFormula[]) configurationFormulasList.toArray(configurationFormulas);
                this.Success = true;
            }
        }
        catch (Exception ex)
        {
            logger.error("Error in getting configuration formulas : " + ex.getMessage());
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration configurationformulas");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving ConfigurationFormulasResponse");
        }
    }

}
