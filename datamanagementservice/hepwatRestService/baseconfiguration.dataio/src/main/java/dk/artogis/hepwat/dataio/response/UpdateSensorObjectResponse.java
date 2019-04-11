package dk.artogis.hepwat.dataio.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataio.SensorObject;
import dk.artogis.hepwat.dataio.UnConfiguredDataIo;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateSensorObjectResponse extends Status
{
    public UpdateSensorObjectResponse() {
    }
    public UpdateSensorObjectResponse(String sensorObjectId, Integer datasourceId, Boolean configured, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = SensorObject.UpdateConfigured(sensorObjectId, datasourceId, configured, connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updating sernsorobject");
        }
        finally {
            connection.close();
        }
    }

    public UpdateSensorObjectResponse(String sensorObjectId, Integer datasourceId, String nameAlias, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = SensorObject.UpdateAlias(sensorObjectId, datasourceId, nameAlias, connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updating sernsorobject");
        }
        finally {
            connection.close();
        }
    }
}
