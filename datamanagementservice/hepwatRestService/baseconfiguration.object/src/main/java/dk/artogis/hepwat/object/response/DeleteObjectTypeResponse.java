package dk.artogis.hepwat.object.response;

import dk.artogis.hepwat.object.ObjectType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteObjectTypeResponse extends Status
{

    public DeleteObjectTypeResponse()
    {}
    public DeleteObjectTypeResponse(UUID id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = ObjectType.Delete(id, connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "objecttype deleted";
                this.JsonObject = "{ \"objectTypeId\": " + id + " }";
            }
            else this.Message = "could not delete item with id: " + id.toString();
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in deleting baseconfiguration.objecttype");
        }
        finally {
            connection.close();
        }
    }

}
