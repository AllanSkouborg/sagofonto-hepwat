package server;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.component.ComponentType;
import dk.artogis.hepwat.component.response.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import org.apache.log4j.Logger;

@Path("/componenttype")
public class EndPointComponenttype {

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComponentType(@PathParam("id") UUID id) {
        Logger logger = Logger.getLogger(EndPointComponenttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getComponentType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ComponentTypeResponse componentTypeResponse = new ComponentTypeResponse(id, connection);
        Response response = null;
        if (componentTypeResponse != null) {
            componentTypeResponse.Version = Config.version;
            if (componentTypeResponse.Success)
                response = Response.ok(componentTypeResponse).build();
            else response = Response.serverError().entity(componentTypeResponse.Message + " " + componentTypeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getComponentType");
        return response;
    }
    @GET
    @Path("/type/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComponentTypeByType(@PathParam("type") Integer type) {
        Logger logger = Logger.getLogger(EndPointComponenttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getComponentTypeByType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ComponentTypeResponse componentTypeResponse = new ComponentTypeResponse(type, connection);
        Response response = null;
        if (componentTypeResponse != null) {
            componentTypeResponse.Version = Config.version;
            if (componentTypeResponse.Success)
                response = Response.ok(componentTypeResponse).build();
            else response = Response.serverError().entity(componentTypeResponse.Message + " " + componentTypeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getComponentTypeByType");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComponentTypes() {
        Logger logger = Logger.getLogger(EndPointComponenttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getComponentTypes");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ComponentTypesResponse componentTypesResponse = new ComponentTypesResponse(connection);
        Response response = null;
        if (componentTypesResponse!= null) {
            componentTypesResponse.Version = Config.version;
            if (componentTypesResponse.Success)
                response = Response.ok(componentTypesResponse).build();
            else response = Response.serverError().entity(componentTypesResponse.Message + " " + componentTypesResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getComponentTypes");
        return response;
    }
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComponentType(final ComponentType componentType) {
        Logger logger = Logger.getLogger(EndPointComponenttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering addComponentType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertComponentTypeResponse insertComponentTypeResponse = new InsertComponentTypeResponse(componentType, connection);
        Response response = null;
        if (insertComponentTypeResponse != null) {
            insertComponentTypeResponse.Version = Config.version;
            if (insertComponentTypeResponse.Success)
                response = Response.ok(insertComponentTypeResponse).build();
            else response = Response.serverError().entity(insertComponentTypeResponse.Message + " " + insertComponentTypeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving addComponentType");
        return response;
    }

    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateComponentType(final ComponentType componentType) {
        Logger logger = Logger.getLogger(EndPointComponenttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering updateComponentType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateComponentTypeResponse updateComponentTypeResponse = new UpdateComponentTypeResponse(componentType, connection);
        Response response = null;
        if (updateComponentTypeResponse != null) {
            updateComponentTypeResponse.Version = Config.version;
            if (updateComponentTypeResponse.Success)
                response = Response.ok(updateComponentTypeResponse).build();
            else response = Response.serverError().entity(updateComponentTypeResponse.Message + " " + updateComponentTypeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateComponentType");
        return response;
    }
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteComponentType(@PathParam("id") UUID id) {
        Logger logger = Logger.getLogger(EndPointComponenttype.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteComponentType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteComponentTypeResponse deleteComponentTypeResponse = new DeleteComponentTypeResponse(id, connection);
        Response response = null;
        if (deleteComponentTypeResponse != null) {
            deleteComponentTypeResponse.Version = Config.version;
            if (deleteComponentTypeResponse.Success)
                response = Response.ok(deleteComponentTypeResponse).build();
            else response = Response.serverError().entity(deleteComponentTypeResponse.Message + " " + deleteComponentTypeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteComponentType");
        return response;
    }
}