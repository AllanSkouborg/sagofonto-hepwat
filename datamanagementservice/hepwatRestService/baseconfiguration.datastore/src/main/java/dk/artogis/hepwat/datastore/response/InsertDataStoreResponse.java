package dk.artogis.hepwat.datastore.response;

import dk.artogis.hepwat.datastore.DataStore;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertDataStoreResponse extends Status
{
    public DataStore dataStore;

    public InsertDataStoreResponse()
    {}
    public InsertDataStoreResponse(DataStore dataStore, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = dataStore.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"dataStoreId\": "  + dataStore.getId()  +  " }";
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in inserting baseconfiguration.datastore");
        }
        finally {
            connection.close();
        }
    }
}
