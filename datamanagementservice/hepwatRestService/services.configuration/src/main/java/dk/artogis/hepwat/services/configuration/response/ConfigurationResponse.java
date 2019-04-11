package dk.artogis.hepwat.services.configuration.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.services.configuration.Configuration;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationResponse extends Status implements Serializable
{
    @JsonDeserialize(as= Configuration.class)
    public Configuration configuration;

    public ConfigurationResponse()
    {
    }
    public ConfigurationResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            configuration = Configuration.GetConfiguration(id,connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving service configuration");

        }
        finally {
            connection.close();
        }
    }

}
