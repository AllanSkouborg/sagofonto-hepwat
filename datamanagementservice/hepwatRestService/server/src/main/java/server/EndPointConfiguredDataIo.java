package server;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.dataio.ConfiguredDataIo;
import dk.artogis.hepwat.dataio.response.ConfiguredDataIoResponse;
import dk.artogis.hepwat.dataio.response.ConfiguredDataIosResponse;
import dk.artogis.hepwat.dataio.response.InsertConfiguredDataIoResponse;
import dk.artogis.hepwat.dataio.response.UpdateConfiguredDataIoResponse;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

//@Path("/configureddataio")
//public class EndPointConfiguredDataIo extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
//
//    @GET
//    @Path("{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getConfiguredDataIo(@PathParam("id") Integer id) {
//        Logger logger = Logger.getLogger(EndPointConfiguredDataIo.class);
//        if(logger.isInfoEnabled())
//            logger.info("Entering getConfiguredDataIo");
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        ConfiguredDataIoResponse configuredDataIoResponse = new ConfiguredDataIoResponse(id, connection);
//        Response response = null;
//        if (configuredDataIoResponse != null) {
//            configuredDataIoResponse.Version = Config.version;
//            if (configuredDataIoResponse.Success)
//                response = Response.ok(configuredDataIoResponse).build();
//            else response = Response.serverError().entity(configuredDataIoResponse.Message + " " +configuredDataIoResponse).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        if(logger.isInfoEnabled())
//            logger.info("Leaving getConfiguredDataIo");
//        return response;
//    }
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getConfiguredDataIos() {
//        Logger logger = Logger.getLogger(EndPointConfiguredDataIo.class);
//        if(logger.isInfoEnabled())
//            logger.info("Entering getConfiguredDataIos");
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        ConfiguredDataIosResponse configuredDataIosResponse = new ConfiguredDataIosResponse(connection);
//        Response response = null;
//        if (configuredDataIosResponse!= null) {
//            configuredDataIosResponse.Version = Config.version;
//            if (configuredDataIosResponse.Success)
//                response = Response.ok(configuredDataIosResponse).build();
//            else response = Response.serverError().entity(configuredDataIosResponse.Message + " " + configuredDataIosResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        if(logger.isInfoEnabled())
//            logger.info("Leaving getConfiguredDataIos");
//        return response;
//    }
//    @POST
//    @Consumes("application/json")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addConfiguredDataIo(final ConfiguredDataIo configuredDataIo) {
//        Logger logger = Logger.getLogger(EndPointConfiguredDataIo.class);
//        if(logger.isInfoEnabled())
//            logger.info("Entering addConfiguredDataIo");
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        InsertConfiguredDataIoResponse insertConfiguredDataIoResponse = new InsertConfiguredDataIoResponse(configuredDataIo, connection);
//        Response response = null;
//        if (insertConfiguredDataIoResponse != null) {
//            insertConfiguredDataIoResponse.Version = Config.version;
//            if (insertConfiguredDataIoResponse.Success)
//                response = Response.ok(insertConfiguredDataIoResponse).build();
//            else response = Response.serverError().entity(insertConfiguredDataIoResponse.Message  + " " + insertConfiguredDataIoResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        if(logger.isInfoEnabled())
//            logger.info("Leaving addConfiguredDataIo");
//        return response;
//    }
//    @PUT
//    @Consumes("application/json")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response updateConfiguredDataIo(final ConfiguredDataIo configuredDataIo) {
//        Logger logger = Logger.getLogger(EndPointConfiguredDataIo.class);
//        if(logger.isInfoEnabled())
//            logger.info("Entering updateConfiguredDataIo");
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        UpdateConfiguredDataIoResponse updateConfiguredDataIoResponse = new UpdateConfiguredDataIoResponse(configuredDataIo, connection);
//        Response response = null;
//        if (updateConfiguredDataIoResponse != null) {
//            updateConfiguredDataIoResponse.Version = Config.version;
//            if (updateConfiguredDataIoResponse.Success)
//                response = Response.ok(updateConfiguredDataIoResponse).build();
//            else response = Response.serverError().entity(updateConfiguredDataIoResponse.Message +" " + updateConfiguredDataIoResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        if(logger.isInfoEnabled())
//            logger.info("Leaving updateConfiguredDataIo");
//        return response;
//    }
//}