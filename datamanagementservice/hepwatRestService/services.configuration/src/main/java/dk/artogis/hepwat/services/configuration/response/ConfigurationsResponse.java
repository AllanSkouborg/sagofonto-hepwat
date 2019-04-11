package dk.artogis.hepwat.services.configuration.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.services.configuration.Configuration;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationsResponse extends Status
{
    public Configuration[] configurations;
    public ConfigurationsResponse()
    {
    }
    public ConfigurationsResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<Configuration> configurationList =  Configuration.GetConfigurations(connection );
            if (configurationList != null ) {
                configurations = new Configuration[configurationList.size()];
                configurations = (Configuration[]) configurationList.toArray(configurations);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving service configurations");
        }
        finally {
            connection.close();
        }
    }

}
