package dk.artogis.hepwat.dataconfig.response;

import dk.artogis.hepwat.dataconfig.Configuration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import org.apache.log4j.Logger;

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
        Logger logger = Logger.getLogger(ConfigurationResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering ConfigurationResponse");
        this.Success = false;

        try {
            connection.connect();
            configuration = Configuration.GetConfiguration(id,connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            logger.error("Error in retrieving configuration : " + ex.getMessage());
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving configuration");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving finally  ConfigurationResponse");
        }
    }

}
