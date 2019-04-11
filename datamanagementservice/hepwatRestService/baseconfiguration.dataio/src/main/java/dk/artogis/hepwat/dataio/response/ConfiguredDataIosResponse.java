package dk.artogis.hepwat.dataio.response;

import dk.artogis.hepwat.dataio.ConfiguredDataIo;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfiguredDataIosResponse extends Status
{
    public ConfiguredDataIo[] configuredDataIos;
    public ConfiguredDataIosResponse()
    {
    }
    public ConfiguredDataIosResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<ConfiguredDataIo> configuredDataIoList =  ConfiguredDataIo.GetConfiguredDataIos(connection );
            if (configuredDataIoList != null ) {
                configuredDataIos = new ConfiguredDataIo[configuredDataIoList.size()];
                configuredDataIos = (ConfiguredDataIo[]) configuredDataIoList.toArray(configuredDataIos);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving configured dataIo");
        }
        finally {
            connection.close();
        }
    }

}
