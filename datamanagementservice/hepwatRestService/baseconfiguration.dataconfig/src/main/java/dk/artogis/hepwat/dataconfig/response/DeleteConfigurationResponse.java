package dk.artogis.hepwat.dataconfig.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataconfig.Configuration;
import org.apache.log4j.Logger;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteConfigurationResponse extends Status
{

    public DeleteConfigurationResponse(){}

    public DeleteConfigurationResponse(Integer id, Connection connection)
    {
        Logger logger = Logger.getLogger(DeleteConfigurationResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering DeleteConfigurationResponse");
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = Configuration.Delete(id, connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"configurationId\": " + id + " }";
        }
        catch (Exception ex)
        {
            logger.error("Error in deleting configuration : " + ex.getMessage());
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in inserting configuration");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving DeleteConfigurationResponse");
        }
    }
}
