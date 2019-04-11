package dk.artogis.hepwat.dataio.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataio.DataSource;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourcesResponse extends Status
{
    public DataSource[] dataSources;
    public DataSourcesResponse()
    {
    }
    public DataSourcesResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<DataSource> dataSourceList =  DataSource.GetDataSources(connection );
            if (dataSourceList != null ) {
                dataSources = new DataSource[dataSourceList.size()];
                dataSources = (DataSource[]) dataSourceList.toArray(dataSources);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving un-configured dataIo");
        }
        finally {
            connection.close();
        }
    }

}
