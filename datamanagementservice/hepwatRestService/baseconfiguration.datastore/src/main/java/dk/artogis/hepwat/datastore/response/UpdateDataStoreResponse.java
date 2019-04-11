package dk.artogis.hepwat.datastore.response;

import dk.artogis.hepwat.datastore.DataStore;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateDataStoreResponse extends Status
{
    public DataStore dataStore;

    public UpdateDataStoreResponse() {
    }

    public UpdateDataStoreResponse(DataStore dataStore, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = dataStore.Update(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in retrieving baseconfiguration.datastore");
        }
        finally {
            connection.close();
        }
    }
}
