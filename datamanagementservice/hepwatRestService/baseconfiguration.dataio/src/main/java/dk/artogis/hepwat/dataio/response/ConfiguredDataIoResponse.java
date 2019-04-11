package dk.artogis.hepwat.dataio.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataio.ConfiguredDataIo;


import java.io.Serializable;
//import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfiguredDataIoResponse extends Status implements Serializable
{
    @JsonDeserialize(as=ConfiguredDataIoResponse.class)
    public ConfiguredDataIo configuredDataIo;

    public ConfiguredDataIoResponse()
    {
    }
    public ConfiguredDataIoResponse(Integer id, Connection connection)
    {
//        Logger logger = Logger.getLogger(ConfiguredDataIoResponse.class);
//        if(logger.isTraceEnabled())
//            logger.trace("Entering InsertConfigurationResponse");
        this.Success = false;

        try {
            connection.connect();
            configuredDataIo = ConfiguredDataIo.GetConfiguredDataIo(id,connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.datastore");
        }
        finally {
            connection.close();
        }
    }

}
