package server;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;
import dk.artogis.hepwat.services.configuration.response.*;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
//import services.configuration.response.ConfigurationResponse;


@Path("/processingconfig")
public class EndPointProcessingConfig extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("/calctypes/{aggtype}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfiguration(@PathParam("aggtype") Integer aggtype) {
        Logger logger = Logger.getLogger(EndPointProcessingConfig.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getConfiguration");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);

        ProcessingConfigsResponse processingConfigsResponse = new ProcessingConfigsResponse( aggtype, connection);
        Response response = null;
        if (processingConfigsResponse != null) {
            processingConfigsResponse.Version = Config.version;
            if (processingConfigsResponse.Success)
                response = Response.ok(processingConfigsResponse).build();
            else response = Response.serverError().entity(processingConfigsResponse.Message + " " + processingConfigsResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getConfiguration");
        return response;

    }

    @GET
    @Path("/aggtypes/{calctype}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfigurationsForCalctype(@PathParam("calctype") Integer calctype) {
        Logger logger = Logger.getLogger(EndPointProcessingConfig.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getConfigurationsForCalctype");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ProcessingConfigsResponse processingConfigsResponse = new ProcessingConfigsResponse( calctype, true, connection);
        Response response = null;
        if (processingConfigsResponse != null) {
            processingConfigsResponse.Version = Config.version;
            if (processingConfigsResponse.Success)
                response = Response.ok(processingConfigsResponse).build();
            else response = Response.serverError().entity(processingConfigsResponse.Message + " " +processingConfigsResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getConfigurationsForCalctype");
        return response;
    }
    @GET
    @Path("{aggtype}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfigurationsForAggType(@PathParam("aggtype") Integer aggtype, @QueryParam("calctype") Integer calctype, @QueryParam("datatype") Integer datatype) {
        Logger logger = Logger.getLogger(EndPointProcessingConfig.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getConfigurationsForAggType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ProcessingConfigResponse processingConfigResponse = new ProcessingConfigResponse(aggtype, calctype, datatype, connection);
        Response response = null;
        if (processingConfigResponse != null) {
            processingConfigResponse.Version = Config.version;
            if (processingConfigResponse.Success)
                response = Response.ok(processingConfigResponse).build();
            else response = Response.serverError().entity(processingConfigResponse.Message + " " +processingConfigResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getConfigurationsForAggType");
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfigurations() {
        Logger logger = Logger.getLogger(EndPointProcessingConfig.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getConfigurations");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ProcessingConfigsResponse processingConfigsResponse = new ProcessingConfigsResponse(connection);
        Response response = null;
        if (processingConfigsResponse!= null) {
            processingConfigsResponse.Version = Config.version;
            if (processingConfigsResponse.Success)
                response = Response.ok(processingConfigsResponse).build();
            else response = Response.serverError().entity(processingConfigsResponse.Message + " " + processingConfigsResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getConfigurations");
        return response;
    }
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addConfiguration(final ProcessingConfig processingConfig) {
        Logger logger = Logger.getLogger(EndPointProcessingConfig.class);
        if(logger.isInfoEnabled())
            logger.info("Entering addConfiguration");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertProcessingConfigResponse insertProcessingConfigResponse = new InsertProcessingConfigResponse(processingConfig, connection);
        Response response = null;
        if (insertProcessingConfigResponse != null) {
            insertProcessingConfigResponse.Version = Config.version;
            if (insertProcessingConfigResponse.Success)
                response = Response.ok(insertProcessingConfigResponse).build();
            else response = Response.serverError().entity(insertProcessingConfigResponse.Message  + " " + insertProcessingConfigResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving addConfiguration");
        return response;
    }

    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateConfiguration(final ProcessingConfig processingConfig) {
        Logger logger = Logger.getLogger(EndPointProcessingConfig.class);
        if(logger.isInfoEnabled())
            logger.info("Entering updateConfiguration");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateProcessingConfigResponse updateProcessingConfigResponse = new UpdateProcessingConfigResponse(processingConfig, connection);
        Response response = null;
        if (updateProcessingConfigResponse != null) {
            updateProcessingConfigResponse.Version = Config.version;
            if (updateProcessingConfigResponse.Success)
                response = Response.ok(updateProcessingConfigResponse).build();
            else response = Response.serverError().entity(updateProcessingConfigResponse.Message +" " + updateProcessingConfigResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateConfiguration");
        return response;
    }

    @DELETE
    @Path("{aggtype}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteConfiguration(@PathParam("aggtype") Integer aggtype, @QueryParam("calctype") Integer calctype) {
        Logger logger = Logger.getLogger(EndPointProcessingConfig.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteConfiguration");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteProcessingConfigResponse deleteProcessingConfigResponse = new DeleteProcessingConfigResponse(aggtype, calctype, connection);
        Response response = null;
        if (deleteProcessingConfigResponse != null) {
            deleteProcessingConfigResponse.Version = Config.version;
            if (deleteProcessingConfigResponse.Success)
                response = Response.ok(deleteProcessingConfigResponse).build();
            else response = Response.serverError().entity(deleteProcessingConfigResponse.Message + " " +deleteProcessingConfigResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteConfiguration");
        return response;
    }

}