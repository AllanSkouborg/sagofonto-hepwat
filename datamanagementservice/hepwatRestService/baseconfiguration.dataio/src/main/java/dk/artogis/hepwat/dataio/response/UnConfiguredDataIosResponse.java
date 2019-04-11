package dk.artogis.hepwat.dataio.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataio.UnConfiguredDataIo;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnConfiguredDataIosResponse extends Status
{
    public UnConfiguredDataIo[] unConfiguredDataIos;
    public UnConfiguredDataIosResponse()
    {
    }

    // Return all data IO's
    public UnConfiguredDataIosResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<UnConfiguredDataIo> configuredDataIoList =  UnConfiguredDataIo.GetUnConfiguredDataIosWithSensorData(connection );
            if (configuredDataIoList != null ) {
                unConfiguredDataIos = new UnConfiguredDataIo[configuredDataIoList.size()];
                unConfiguredDataIos = (UnConfiguredDataIo[]) configuredDataIoList.toArray(unConfiguredDataIos);
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

    // Return data IO's defined in comma-separated string
    public UnConfiguredDataIosResponse(String[] queryIdsArray, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<UnConfiguredDataIo> configuredDataIoList =  UnConfiguredDataIo.GetUnConfiguredDataIosFromArrayWithSensorData(queryIdsArray, connection );
            if (configuredDataIoList != null ) {
                unConfiguredDataIos = new UnConfiguredDataIo[configuredDataIoList.size()];
                unConfiguredDataIos = (UnConfiguredDataIo[]) configuredDataIoList.toArray(unConfiguredDataIos);
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
