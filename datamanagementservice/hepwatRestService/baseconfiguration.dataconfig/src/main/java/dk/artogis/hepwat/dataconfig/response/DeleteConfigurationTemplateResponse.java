package dk.artogis.hepwat.dataconfig.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataconfig.ConfigurationTemplate;

import java.util.UUID;
import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteConfigurationTemplateResponse extends Status
{

    public DeleteConfigurationTemplateResponse(){}

    public DeleteConfigurationTemplateResponse(UUID id, Connection connection)
    {
        Logger logger = Logger.getLogger(DeleteConfigurationTemplateResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering DeleteConfigurationTemplateResponse");
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = ConfigurationTemplate.Delete(id, connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"configurationId\": \""  + id  +  "\" }";
        }
        catch (Exception ex)
        {
            logger.error("Error in deleting configuration template : " + ex.getMessage());
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in deleting configurationtemplate");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving DeleteConfigurationTemplateResponse");
        }
    }

    public DeleteConfigurationTemplateResponse(Integer templateType, Connection connection)
    {
        Logger logger = Logger.getLogger(ConfigurationResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering DeleteConfigurationTemplateResponse by templatetype");
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = ConfigurationTemplate.DeleteFromTemplateId(templateType, connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"templatetype\": "  + templateType  +  " }";
        }
        catch (Exception ex)
        {
            logger.error("Error in deleting configuration template by templatetype : " + ex.getMessage());
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in deleting configurationtemplate");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving DeleteConfigurationTemplateResponse by templatetype");
        }
    }
}
