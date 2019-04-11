package dk.artogis.hepwat.dataconfig.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataconfig.Configuration;
import dk.artogis.hepwat.dataconfig.ConfigurationTemplate;

import java.io.Serializable;
import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationTemplateResponse extends Status implements Serializable
{
    @JsonDeserialize(as= Configuration.class)
    public ConfigurationTemplate configurationTemplate;

    public ConfigurationTemplateResponse()
    {
    }
    public ConfigurationTemplateResponse(Integer id, Connection connection)
    {
        Logger logger = Logger.getLogger(ConfigurationTemplateResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering ConfigurationTemplateResponse");
        this.Success = false;

        try {
            connection.connect();
            configurationTemplate = ConfigurationTemplate.GetConfigurationTemplate(id,connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            logger.error("Error in retrieving configuration template : " + ex.getMessage());
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.dataconfig configurationTemplate");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving ConfigurationTemplateResponse");
        }
    }

}
