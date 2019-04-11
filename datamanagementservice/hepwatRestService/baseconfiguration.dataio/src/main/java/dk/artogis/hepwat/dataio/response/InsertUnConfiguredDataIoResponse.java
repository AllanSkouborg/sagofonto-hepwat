package dk.artogis.hepwat.dataio.response;

import dk.artogis.hepwat.dataio.UnConfiguredDataIo;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertUnConfiguredDataIoResponse extends Status
{

    public InsertUnConfiguredDataIoResponse(){}

    public InsertUnConfiguredDataIoResponse(UnConfiguredDataIo unConfiguredDataIo, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = unConfiguredDataIo.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in inserting un-configured DataIo");
        }
        finally {
            connection.close();
        }
    }
}
