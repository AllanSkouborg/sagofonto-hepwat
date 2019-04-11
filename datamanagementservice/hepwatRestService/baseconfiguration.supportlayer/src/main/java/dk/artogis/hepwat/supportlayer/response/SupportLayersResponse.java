package dk.artogis.hepwat.supportlayer.response;

import dk.artogis.hepwat.supportlayer.SupportLayer;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupportLayersResponse extends Status
{
    public SupportLayer[] supportLayers;

    SupportLayersResponse() {}

    public SupportLayersResponse(Connection  connection)
    {
        this.Success = false;
        try {
            connection.connect();
            supportLayers = (SupportLayer[])SupportLayer.GetSupportLayers(connection ).toArray(new SupportLayer[0]);
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            //System.out.println("Error in retrieving support layer data: ");
            //System.out.println(ex.getMessage());
        }
        finally {
            connection.close();
        }
    }

}