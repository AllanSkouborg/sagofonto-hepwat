package dk.artogis.hepwat.dataconfig.response;

import dk.artogis.hepwat.dataconfig.Configuration;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateConfigurationResponse extends Status
{
    public UpdateConfigurationResponse() {
    }
    public UpdateConfigurationResponse(Configuration configuration, Connection connection)
    {
        Logger logger = Logger.getLogger(UpdateConfigurationResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering UpdateConfigurationResponse");
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = configuration.Update(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"configurationId\": "  + configuration.getId()  +  " }";
        }
        catch (Exception ex)
        {
            logger.error("Error in updating configuration : " + ex.getMessage());
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updating configuration");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving UpdateConfigurationResponse");
        }
    }
}
