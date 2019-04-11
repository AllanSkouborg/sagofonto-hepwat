package dk.artogis.hepwat.dataconfig.response;

import dk.artogis.hepwat.dataconfig.Configuration;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertConfigurationResponse extends Status
{

    public InsertConfigurationResponse(){}

    public InsertConfigurationResponse(Configuration configuration, Connection connection)
    {
        Logger logger = Logger.getLogger(InsertConfigurationResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering InsertConfigurationResponse");
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = configuration.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"configurationId\": "  + configuration.getId()  +  " }";
        }
        catch (Exception ex)
        {
            logger.error("error in inserting configuration : " + ex.getMessage());
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in inserting configuration");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving finally InsertConfigurationResponse");
        }
    }
}
