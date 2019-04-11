package dk.artogis.hepwat.datastore.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.datastore.DataStore;

import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteDataStoreResponse extends Status
{

    public DeleteDataStoreResponse()
    {}
    public DeleteDataStoreResponse(UUID id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = DataStore.Delete(id, connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "datastore deleted";
                this.JsonObject = "{ \"dataStoreId\": " + id + " }";
            }
            else
                this.Message = "could not delete item with id: " + id.toString();
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in deleting baseconfiguration.datastore");
        }
        finally {
            connection.close();
        }
    }

}
