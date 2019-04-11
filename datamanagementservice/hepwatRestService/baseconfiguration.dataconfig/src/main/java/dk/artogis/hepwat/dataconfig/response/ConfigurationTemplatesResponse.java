package dk.artogis.hepwat.dataconfig.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataconfig.Configuration;
import dk.artogis.hepwat.dataconfig.ConfigurationTemplate;

import java.util.List;
import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationTemplatesResponse extends Status
{
    public ConfigurationTemplate[] configurationTemplates;
    public ConfigurationTemplatesResponse()
    {
    }
    public ConfigurationTemplatesResponse(Connection connection)
    {
        Logger logger = Logger.getLogger(ConfigurationTemplatesResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering ConfigurationTemplatesResponse");
        this.Success = false;

        try {
            connection.connect();
            List<ConfigurationTemplate> configurationTemplatesList =  ConfigurationTemplate.GetConfigurationTemplates(connection );
            if (configurationTemplatesList != null ) {
                configurationTemplates = new ConfigurationTemplate[configurationTemplatesList.size()];
                configurationTemplates = (ConfigurationTemplate[]) configurationTemplatesList.toArray(configurationTemplates);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            logger.error("Error in retrieving configuration templates : " + ex.getMessage());
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving configuration templates ");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving ConfigurationTemplatesResponse");
        }
    }

}
