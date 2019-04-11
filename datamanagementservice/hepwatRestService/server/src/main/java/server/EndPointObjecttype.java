package server;

import dk.artogis.hepwat.object.response.*;
import dk.artogis.hepwat.object.ObjectType;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;


@Path("/objecttype")
public class EndPointObjecttype  extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectType(@PathParam("id") UUID id) {
        Logger logger = Logger.getLogger(EndPointObjecttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getObjectType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ObjectTypeResponse objectTypeResponse = new ObjectTypeResponse(id, connection);
        Response response = null;
        if (objectTypeResponse != null) {
            objectTypeResponse.Version = Config.version;
            if (objectTypeResponse.Success)
                response = Response.ok(objectTypeResponse).build();
            else response = Response.serverError().entity(objectTypeResponse.Message + " " +objectTypeResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getObjectType");
        return response;
    }
    @GET
    @Path("/type/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectTypeByType(@PathParam("type") Integer id) {
        Logger logger = Logger.getLogger(EndPointObjecttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getObjectTypeByType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ObjectTypeResponse objectTypeResponse = new ObjectTypeResponse(id, connection);
        Response response = null;
        if (objectTypeResponse != null) {
            objectTypeResponse.Version = Config.version;
            if (objectTypeResponse.Success)
                response = Response.ok(objectTypeResponse).build();
            else response = Response.serverError().entity(objectTypeResponse.Message + " " +objectTypeResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getObjectTypeByType");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectTypes() {
        Logger logger = Logger.getLogger(EndPointObjecttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getObjectTypeByType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ObjectTypesResponse objectTypesResponse = new ObjectTypesResponse(connection);
        Response response = null;
        if (objectTypesResponse!= null) {
            objectTypesResponse.Version = Config.version;
            if (objectTypesResponse.Success)
                response = Response.ok(objectTypesResponse).build();
            else response = Response.serverError().entity(objectTypesResponse.Message + " " + objectTypesResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getObjectTypeByType");
        return response;
    }
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addObjectType(final ObjectType objectType) {
        Logger logger = Logger.getLogger(EndPointObjecttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering addObjectType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertObjectTypeResponse insertObjectTypeResponse = new InsertObjectTypeResponse(objectType, connection);
        Response response = null;
        if (insertObjectTypeResponse != null) {
            insertObjectTypeResponse.Version = Config.version;
            if (insertObjectTypeResponse.Success)
                response = Response.ok(insertObjectTypeResponse).build();
            else response = Response.serverError().entity(insertObjectTypeResponse.Message  + " " + insertObjectTypeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving addObjectType");
        return response;
    }

    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateObjectType(final ObjectType objectType) {
        Logger logger = Logger.getLogger(EndPointObjecttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering updateObjectType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateObjectTypeResponse updateObjectTypeResponse = new UpdateObjectTypeResponse(objectType, connection);
        Response response = null;
        if (updateObjectTypeResponse != null) {
            updateObjectTypeResponse.Version = Config.version;
            if (updateObjectTypeResponse.Success)
                response = Response.ok(updateObjectTypeResponse).build();
            else response = Response.serverError().entity(updateObjectTypeResponse.Message +" " + updateObjectTypeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateObjectType");
        return response;
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteObjectType(@PathParam("id") UUID id) {
        Logger logger = Logger.getLogger(EndPointObjecttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteObjectType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteObjectTypeResponse deleteObjectTypeResponse = new DeleteObjectTypeResponse(id, connection);
        Response response = null;
        if (deleteObjectTypeResponse != null) {
            deleteObjectTypeResponse.Version = Config.version;
            if (deleteObjectTypeResponse.Success)
                response = Response.ok(deleteObjectTypeResponse).build();
            else response = Response.serverError().entity(deleteObjectTypeResponse.Message + " " +deleteObjectTypeResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteObjectType");
        return response;
    }

}