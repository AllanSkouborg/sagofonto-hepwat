package dk.artogis.hepwat.dataconfig.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataconfig.ConfigurationTemplate;
import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateConfigurationTemplateResponse extends Status
{

    public UpdateConfigurationTemplateResponse(){}

    public UpdateConfigurationTemplateResponse(ConfigurationTemplate configurationTemplate, Connection connection)
    {
        Logger logger = Logger.getLogger(UpdateConfigurationTemplateResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering UpdateConfigurationTemplateResponse");
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = configurationTemplate.Update(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"configurationId\": \""  +configurationTemplate.getId()  +  "\" }";
        }
        catch (Exception ex)
        {
            logger.error("Error in updating configuration template : " + ex.getMessage());
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updating configurationtemplate");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving UpdateConfigurationTemplateResponse");
        }
    }
}
