package server;

import dk.artogis.hepwat.dataio.UnConfiguredDataIo;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.dataio.response.InsertUnConfiguredDataIoResponse;
import dk.artogis.hepwat.dataio.response.UnConfiguredDataIoResponse;
import dk.artogis.hepwat.dataio.response.UnConfiguredDataIosResponse;
import dk.artogis.hepwat.dataio.response.UpdateUnConfiguredDataIoResponse;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

@Path("/unconfigureddataio")
public class EndPointUnConfiguredDataIo extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnConfiguredDataIo(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointUnConfiguredDataIo.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getUnConfiguredDataIo");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UnConfiguredDataIoResponse unConfiguredDataIoResponse = new UnConfiguredDataIoResponse(id, connection);
        Response response = null;
        if (unConfiguredDataIoResponse != null) {
            unConfiguredDataIoResponse.Version = Config.version;
            if (unConfiguredDataIoResponse.Success)
                response = Response.ok(unConfiguredDataIoResponse).build();
            else response = Response.serverError().entity(unConfiguredDataIoResponse.Message + " " +unConfiguredDataIoResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getUnConfiguredDataIo");
        return response;
    }
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUnConfiguredDataIo( final ConfiguredState configuredState, @PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointUnConfiguredDataIo.class);
        if(logger.isInfoEnabled())
            logger.info("Entering updateUnConfiguredDataIo");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateUnConfiguredDataIoResponse updateUConfiguredDataIoResponse = new UpdateUnConfiguredDataIoResponse(id, configuredState.configured, connection);
        Response response = null;
        if (updateUConfiguredDataIoResponse != null) {
            updateUConfiguredDataIoResponse.Version = Config.version;
            if (updateUConfiguredDataIoResponse.Success)
                response = Response.ok(updateUConfiguredDataIoResponse).build();
            else response = Response.serverError().entity(updateUConfiguredDataIoResponse.Message + " " +updateUConfiguredDataIoResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateUnConfiguredDataIo");
        return response;
    }

    // Update name_alias
    @PUT
    @Path("{id}/alias")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUnConfiguredDataIoNameAlias( final NameAliasState nameAliasState, @PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointUnConfiguredDataIo.class);
        if(logger.isInfoEnabled()) {
            logger.info("Entering updateUnConfiguredDataIo");
        }

        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateUnConfiguredDataIoResponse updateUConfiguredDataIoResponse = new UpdateUnConfiguredDataIoResponse(id, nameAliasState.nameAlias, connection);
        Response response = null;

        if (updateUConfiguredDataIoResponse != null) {
            updateUConfiguredDataIoResponse.Version = Config.version;
            if (updateUConfiguredDataIoResponse.Success) {
                response = Response.ok(updateUConfiguredDataIoResponse).build();
            }
            else {
                response = Response.serverError().entity(updateUConfiguredDataIoResponse.Message + " " +updateUConfiguredDataIoResponse).build();
            }
        }
        else {
            response = Response.serverError().entity("could not retrieve response").build();
        }
        if(logger.isInfoEnabled()) {
            logger.info("Leaving updateUnConfiguredDataIo");
        }
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnConfiguredDataIos( @Context UriInfo info) {
        Logger logger = Logger.getLogger(EndPointUnConfiguredDataIo.class);
        if(logger.isInfoEnabled()) {
            logger.info("Entering getUnConfiguredDataIos");
        }

        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        Response response = null;

        String queryIdsString = info.getQueryParameters().getFirst("ids");
        if (queryIdsString != null) {
            String[] queryIdsStringArray = queryIdsString.split(",");
            UnConfiguredDataIosResponse unConfiguredDataIosResponse = new UnConfiguredDataIosResponse(queryIdsStringArray, connection);
            if (unConfiguredDataIosResponse != null) {
                unConfiguredDataIosResponse.Version = Config.version;
                if (unConfiguredDataIosResponse.Success)
                    response = Response.ok(unConfiguredDataIosResponse).build();
                else
                    response = Response.serverError().entity(unConfiguredDataIosResponse.Message + " " + unConfiguredDataIosResponse.Error).build();
            } else {
                response = Response.serverError().entity("could not retrieve response").build();
            }
        }
        // No ID array defined
        else {
            UnConfiguredDataIosResponse unConfiguredDataIosResponse = new UnConfiguredDataIosResponse(connection);

            if (unConfiguredDataIosResponse != null) {
                unConfiguredDataIosResponse.Version = Config.version;
                if (unConfiguredDataIosResponse.Success)
                    response = Response.ok(unConfiguredDataIosResponse).build();
                else
                    response = Response.serverError().entity(unConfiguredDataIosResponse.Message + " " + unConfiguredDataIosResponse.Error).build();
            } else {
                response = Response.serverError().entity("could not retrieve response").build();
            }
        }

        if(logger.isInfoEnabled()) {
            logger.info("Leaving getUnConfiguredDataIos");
        }
        return response;
    }
/*    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUnConfiguredDataIo(final UnConfiguredDataIo unConfiguredDataIo) {
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertUnConfiguredDataIoResponse insertUnConfiguredDataIoResponse = new InsertUnConfiguredDataIoResponse(unConfiguredDataIo, connection);
        Response response = null;
        if (insertUnConfiguredDataIoResponse != null) {
            insertUnConfiguredDataIoResponse.Version = Config.version;
            if (insertUnConfiguredDataIoResponse.Success)
                response = Response.ok(insertUnConfiguredDataIoResponse).build();
            else response = Response.serverError().entity(insertUnConfiguredDataIoResponse.Message  + " " + insertUnConfiguredDataIoResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        return response;
    }*/
/*    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUnConfiguredDataIo(final UnConfiguredDataIo unConfiguredDataIo) {
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateUnConfiguredDataIoResponse updateUnConfiguredDataIoResponse = new UpdateUnConfiguredDataIoResponse(unConfiguredDataIo, connection);
        Response response = null;
        if (updateUnConfiguredDataIoResponse != null) {
            updateUnConfiguredDataIoResponse.Version = Config.version;
            if (updateUnConfiguredDataIoResponse.Success)
                response = Response.ok(updateUnConfiguredDataIoResponse).build();
            else response = Response.serverError().entity(updateUnConfiguredDataIoResponse.Message +" " + updateUnConfiguredDataIoResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        return response;
    }*/
}