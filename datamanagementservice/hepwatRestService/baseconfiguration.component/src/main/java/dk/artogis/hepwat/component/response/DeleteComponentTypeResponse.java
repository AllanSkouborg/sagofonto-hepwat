package dk.artogis.hepwat.component.response;

import dk.artogis.hepwat.component.ComponentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteComponentTypeResponse extends Status
{

    public DeleteComponentTypeResponse()
    {}
    public DeleteComponentTypeResponse(UUID id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = ComponentType.Delete(id, connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "componenttype deleted";
                this.JsonObject = "{ \"componentTypeId\": " + id + " }";
            }
            else this.Message = "could not delete item with id: " + id.toString();
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in deleting baseconfiguration.componenttype");
        }
        finally {
            connection.close();
        }
    }

}
