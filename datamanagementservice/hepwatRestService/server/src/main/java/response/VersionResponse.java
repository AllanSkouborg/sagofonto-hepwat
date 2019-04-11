package response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.measurement.Unit;
import server.Config;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VersionResponse extends Status implements Serializable
{


    public VersionResponse()
    {
        this.Success = false;

        try {
            this.Version = Config.version;
            this.Success = true;
        }
        catch (Exception ex)
        {
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving version");
        }
    }
}
