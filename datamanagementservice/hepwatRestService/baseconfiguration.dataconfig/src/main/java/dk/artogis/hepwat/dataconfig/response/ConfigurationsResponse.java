package dk.artogis.hepwat.dataconfig.response;

import dk.artogis.hepwat.dataconfig.Configuration;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.List;
import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationsResponse extends Status
{
    public Configuration[] configurations;
    public ConfigurationsResponse()
    {
    }

    // Get all
    public ConfigurationsResponse(Connection connection,Boolean includeRelations, Boolean addTypeInfo)
    {
        Logger logger = Logger.getLogger(ConfigurationResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering ConfigurationsResponse");
        this.Success = false;

        try {
            connection.connect();
            List<Configuration> configurationList =  Configuration.GetConfigurations(connection, includeRelations, addTypeInfo );
            if (configurationList != null ) {
                configurations = new Configuration[configurationList.size()];
                configurations = (Configuration[]) configurationList.toArray(configurations);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            logger.error("Error in retrieving configurations : " + ex.getMessage());
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving configureations ");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving ConfigurationsResponse");
        }
    }

    // Get array of ID's
    public ConfigurationsResponse(String idArrayString, Connection connection,Boolean includeRelations, Boolean addTypeInfo)
    {
        Logger logger = Logger.getLogger(ConfigurationResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering ConfigurationsResponse - array of ID's: " + idArrayString);
        this.Success = false;

        try {
            connection.connect();
            List<Configuration> configurationList =  Configuration.GetConfigurationsFromArray(idArrayString, connection, includeRelations, addTypeInfo );
            if (configurationList != null ) {
                configurations = new Configuration[configurationList.size()];
                configurations = (Configuration[]) configurationList.toArray(configurations);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            logger.error("Error in retrieving configurations : " + ex.getMessage());
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving configureations ");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving ConfigurationsResponse");
        }
    }

    public ConfigurationsResponse(Connection connection,Boolean includeRelations, Boolean addTypeInfo, Integer calculationType)
    {
        Logger logger = Logger.getLogger(ConfigurationResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering ConfigurationsResponse calculation type");

        this.Success = false;

        try {
            connection.connect();
            List<Configuration> configurationList =  Configuration.GetConfigurations(connection, includeRelations, addTypeInfo, calculationType );
            if (configurationList != null ) {
                configurations = new Configuration[configurationList.size()];
                configurations = (Configuration[]) configurationList.toArray(configurations);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            logger.error("Error in retrieving configurations : " + ex.getMessage());
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving configureations ");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving ConfigurationsResponse");
        }
    }

}
