package dk.artogis.hepwat.dataio.response;

import dk.artogis.hepwat.dataio.SensorObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorObjectResponse extends Status implements Serializable
{
    @JsonDeserialize(as=SensorObject.class)
    public SensorObject sensorObject;

    public SensorObjectResponse()
    {
    }
    public SensorObjectResponse(String id, Integer datasourceId , Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            sensorObject = SensorObject.GetSensorObject(id, datasourceId, connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving unconfigured dataIo");
        }
        finally {
            connection.close();
        }
    }

}
