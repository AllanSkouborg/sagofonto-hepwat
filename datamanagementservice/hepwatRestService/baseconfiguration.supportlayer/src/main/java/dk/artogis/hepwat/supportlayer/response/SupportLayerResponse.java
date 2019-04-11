package dk.artogis.hepwat.supportlayer.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.supportlayer.SupportLayer;

import java.util.UUID;

import org.apache.log4j.Logger;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupportLayerResponse extends Status
{
    public SupportLayer supportLayer;

    public SupportLayerResponse(){}

    public SupportLayerResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            supportLayer = SupportLayer.GetSupportLayer(id, connection);
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            //System.out.print("Error in retrieving baseconfiguration.supportlayer");
        }
        finally {
            connection.close();
        }
    }
}