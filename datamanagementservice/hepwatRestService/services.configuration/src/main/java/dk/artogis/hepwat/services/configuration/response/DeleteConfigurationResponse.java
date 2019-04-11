package dk.artogis.hepwat.services.configuration.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.services.configuration.Configuration;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteConfigurationResponse extends Status
{

    public DeleteConfigurationResponse()
    {}
    public DeleteConfigurationResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = Configuration.Delete(id, connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "configuration deleted";
                this.JsonObject = "{ \"configurationId\": " + id + " }";
            }
            else this.Message = "could not delete item with id: " + id.toString();
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in deleting configuration");
        }
        finally {
            connection.close();
        }
    }

}
