package server;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.services.configuration.Configuration;
import dk.artogis.hepwat.services.configuration.response.*;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

@Path("/servicesconfiguration")
public class EndPointServicesConfiguration extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfiguration(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointServicesConfiguration.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getConfiguration");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ConfigurationResponse configurationResponse = new ConfigurationResponse(id, connection);
        Response response = null;
        if (configurationResponse != null) {
            configurationResponse.Version = Config.version;
            if (configurationResponse.Success)
                response = Response.ok(configurationResponse).build();
            else response = Response.serverError().entity(configurationResponse.Message + " " +configurationResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getConfiguration");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfigurations() {
        Logger logger = Logger.getLogger(EndPointServicesConfiguration.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getConfigurations");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ConfigurationsResponse configurationsResponse = new ConfigurationsResponse(connection);
        Response response = null;
        if (configurationsResponse!= null) {
            configurationsResponse.Version = Config.version;
            if (configurationsResponse.Success)
                response = Response.ok(configurationsResponse).build();
            else response = Response.serverError().entity(configurationsResponse.Message + " " + configurationsResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getConfigurations");
        return response;
    }
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addConfiguration(final Configuration configuration) {
        Logger logger = Logger.getLogger(EndPointServicesConfiguration.class);
        if(logger.isInfoEnabled())
            logger.info("Entering addConfiguration");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertConfigurationResponse insertConfigurationResponse = new InsertConfigurationResponse(configuration, connection);
        Response response = null;
        if (insertConfigurationResponse != null) {
            insertConfigurationResponse.Version = Config.version;
            if (insertConfigurationResponse.Success)
                response = Response.ok(insertConfigurationResponse).build();
            else response = Response.serverError().entity(insertConfigurationResponse.Message  + " " + insertConfigurationResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving addConfiguration");
        return response;
    }

    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateConfiguration(final Configuration configuration) {
        Logger logger = Logger.getLogger(EndPointServicesConfiguration.class);
        if(logger.isInfoEnabled())
            logger.info("Entering updateConfiguration");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateConfigurationResponse updateConfigurationResponse = new UpdateConfigurationResponse(configuration, connection);
        Response response = null;
        if (updateConfigurationResponse != null) {
            updateConfigurationResponse.Version = Config.version;
            if (updateConfigurationResponse.Success)
                response = Response.ok(updateConfigurationResponse).build();
            else response = Response.serverError().entity(updateConfigurationResponse.Message +" " + updateConfigurationResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateConfiguration");
        return response;
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteConfiguration(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointServicesConfiguration.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteConfiguration");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteConfigurationResponse deleteConfigurationResponse = new DeleteConfigurationResponse(id, connection);
        Response response = null;
        if (deleteConfigurationResponse != null) {
            deleteConfigurationResponse.Version = Config.version;
            if (deleteConfigurationResponse.Success)
                response = Response.ok(deleteConfigurationResponse).build();
            else response = Response.serverError().entity(deleteConfigurationResponse.Message + " " +deleteConfigurationResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteConfiguration");
        return response;
    }

}