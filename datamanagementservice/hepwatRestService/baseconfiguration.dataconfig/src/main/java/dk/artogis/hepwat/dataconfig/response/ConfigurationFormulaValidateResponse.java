package dk.artogis.hepwat.dataconfig.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataconfig.ConfigurationFormula;

import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationFormulaValidateResponse extends Status implements Serializable
{

    public ConfigurationFormulaValidateResponse()
    {
    }
    public ConfigurationFormulaValidateResponse(Connection connection,ConfigurationFormula configurationFormula, String measurementValue)
    {
        Logger logger = Logger.getLogger(ConfigurationFormulaValidateResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering ConfigurationFormulaValidateResponse");
        this.Success = false;

        try {
            connection.connect();
            if ((measurementValue == null) || (measurementValue.isEmpty()))
                measurementValue = "1.0";
            Status status = ConfigurationFormula.calculate(connection, configurationFormula.getId(), configurationFormula.getFormula(), measurementValue);
            this.Message = status.Message;
            this.Success = status.Success;
            this.JsonObject = status.JsonObject;
        }
        catch (Exception ex)
        {
            logger.error("Error in validating formula : " + ex.getMessage());
            this.Message = ex.getMessage();
            System.out.print("Error in validating configurationformula");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving ConfigurationFormulaValidateResponse");
        }
    }

}
