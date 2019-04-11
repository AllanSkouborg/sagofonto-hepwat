package dk.artogis.hepwat.services.configuration.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.services.configuration.Configuration;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateConfigurationResponse extends Status
{

    public UpdateConfigurationResponse() {
    }
    public UpdateConfigurationResponse(Configuration configuration, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = configuration.Update(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updateing configuration");
        }
        finally {
            connection.close();
        }
    }
}
