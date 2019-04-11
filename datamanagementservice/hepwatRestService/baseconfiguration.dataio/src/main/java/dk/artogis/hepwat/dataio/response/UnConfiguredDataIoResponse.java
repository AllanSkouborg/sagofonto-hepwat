package dk.artogis.hepwat.dataio.response;

import dk.artogis.hepwat.dataio.UnConfiguredDataIo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnConfiguredDataIoResponse extends Status implements Serializable
{
    @JsonDeserialize(as=UnConfiguredDataIo.class)
    public UnConfiguredDataIo unConfiguredDataIo;

    public UnConfiguredDataIoResponse()
    {
    }
    public UnConfiguredDataIoResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            unConfiguredDataIo = UnConfiguredDataIo.GetUnConfiguredDataIoWithSensorObjectData(id,connection );
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
