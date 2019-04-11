package dk.artogis.hepwat.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.data.config.DataStore;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataStoreResponse extends Status
{
    public DataStore dataStore;

    public DataStoreResponse()
    {}
    public DataStoreResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            dataStore = DataStore.GetDataStore(id, connection);
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.datastore");
        }
        finally {
            connection.close();
        }
    }

}
