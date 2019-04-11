package dk.artogis.hepwat.services.configuration.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.services.configuration.Configuration;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertConfigurationResponse extends Status
{
    public Configuration configuration;

    public InsertConfigurationResponse(){}

    public InsertConfigurationResponse(Configuration configuration, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = configuration.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"configurationServiceId\": "  + configuration.getServiceId() + " }";
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in inserting service configuration");
        }
        finally {
            connection.close();
        }
    }
}
