package dk.artogis.hepwat.dataio.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataio.SensorObject;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorObjectsResponse extends Status
{
    public SensorObject[] sensorObjects;
    public SensorObjectsResponse()
    {
    }
    public SensorObjectsResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<SensorObject> sensorObjectList =  SensorObject.GetSensorObjects(connection );
            if (sensorObjectList != null ) {
                sensorObjects = new SensorObject[sensorObjectList.size()];
                sensorObjects = (SensorObject[]) sensorObjectList.toArray(sensorObjects);
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
