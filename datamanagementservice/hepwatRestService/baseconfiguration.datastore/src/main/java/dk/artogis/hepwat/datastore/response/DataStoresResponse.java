package dk.artogis.hepwat.datastore.response;

import dk.artogis.hepwat.datastore.DataStore;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataStoresResponse extends Status
{
    public DataStore[] dataStores;

    public DataStoresResponse()
    {}
    public DataStoresResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            dataStores = (DataStore[])DataStore.GetDataStores(connection ).toArray(new DataStore[0]);
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving datastores");
        }
        finally {
            connection.close();
        }
    }

}
