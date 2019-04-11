package dk.artogis.hepwat.dataio.response;

import dk.artogis.hepwat.dataio.ConfiguredDataIo;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateConfiguredDataIoResponse extends Status
{
    public UpdateConfiguredDataIoResponse() {
    }
    public UpdateConfiguredDataIoResponse(ConfiguredDataIo configuredDataIo, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = configuredDataIo.Update(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updateing configured baseconfiguration.dataio");
        }
        finally {
            connection.close();
        }
    }
}
