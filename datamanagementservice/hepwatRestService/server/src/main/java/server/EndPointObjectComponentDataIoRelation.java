package server;

import dk.artogis.hepwat.relation.*;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.relation.response.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import org.apache.log4j.Logger;

@Path("/objectcomponentdatarelation")
public class EndPointObjectComponentDataIoRelation {

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

//    @GET
//    @Path("/relationid/{relationid}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getObjectComponentDataIoRelationByRelationId(@PathParam("relationid") UUID id) {
//        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
//        if(logger.isInfoEnabled())
//            logger.info("Entering getObjectComponentDataIoRelationByRelationId");
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        ObjectComponentDataIoRelationResponse objectComponentDataIoRelationResponse = new ObjectComponentDataIoRelationResponse(id, connection);
//        Response response = null;
//        if (objectComponentDataIoRelationResponse != null) {
//            objectComponentDataIoRelationResponse.Version = Config.version;
//            if (objectComponentDataIoRelationResponse.Success)
//                response = Response.ok(objectComponentDataIoRelationResponse).build();
//            else response = Response.serverError().entity(objectComponentDataIoRelationResponse.Message + " " + objectComponentDataIoRelationResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        if(logger.isInfoEnabled())
//            logger.info("Leaving getObjectComponentDataIoRelationByRelationId");
//        return response;
//    }
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectComponentDataIoRelation(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getObjectComponentDataIoRelation");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ObjectComponentDataIoRelationResponse objectComponentDataIoRelationResponse = new ObjectComponentDataIoRelationResponse(id, connection);
        Response response = null;
        if (objectComponentDataIoRelationResponse != null) {
            objectComponentDataIoRelationResponse.Version = Config.version;
            if (objectComponentDataIoRelationResponse.Success)
                response = Response.ok(objectComponentDataIoRelationResponse).build();
            else response = Response.serverError().entity(objectComponentDataIoRelationResponse.Message + " " + objectComponentDataIoRelationResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getObjectComponentDataIoRelation");
        return response;
    }
    @GET
    @Path("/component/{componentid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComponentRelations(@PathParam("componentid") Integer componentid, @QueryParam("componenttype") Integer componenttype) {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getComponentRelations");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ComponentRelationsResponse componentRelationsResponse = new ComponentRelationsResponse(componentid, componenttype, connection);
        Response response = null;
        if (componentRelationsResponse != null) {
            componentRelationsResponse.Version = Config.version;
            if (componentRelationsResponse.Success)
                response = Response.ok(componentRelationsResponse).build();
            else response = Response.serverError().entity(componentRelationsResponse.Message + " " + componentRelationsResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getComponentRelations");
        return response;
    }
    @GET
    @Path("/dataio/{dataioid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataIoRelations(@PathParam("dataioid") Integer id) {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getDataIoRelations");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DataIoRelationsResponse dataIoRelationsResponse = new DataIoRelationsResponse(id, connection);
        Response response = null;
        if (dataIoRelationsResponse != null) {
            dataIoRelationsResponse.Version = Config.version;
            if (dataIoRelationsResponse.Success)
                response = Response.ok(dataIoRelationsResponse).build();
            else response = Response.serverError().entity(dataIoRelationsResponse.Message + " " + dataIoRelationsResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getDataIoRelations");
        return response;
    }
    @GET
    @Path("/object/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectRelations(@PathParam("value") String value, @QueryParam("field") String field, @QueryParam("fieldtype") String fieldtype,  @QueryParam("objecttype") Integer objecttype) {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getObjectRelations");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ObjectRelationsResponse objectRelationsResponse = new ObjectRelationsResponse(value, field, fieldtype, objecttype, connection);
        Response response = null;
        if (objectRelationsResponse != null) {
            objectRelationsResponse.Version = Config.version;
            if (objectRelationsResponse.Success)
                response = Response.ok(objectRelationsResponse).build();
            else response = Response.serverError().entity(objectRelationsResponse.Message + " " + objectRelationsResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getObjectRelations");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectComponentDataIoRelations() {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getObjectComponentDataIoRelations");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ObjectComponentDataIoRelationsResponse objectComponentDataIoRelationsResponse = new ObjectComponentDataIoRelationsResponse(connection);
        Response response = null;
        if (objectComponentDataIoRelationsResponse!= null) {
            objectComponentDataIoRelationsResponse.Version = Config.version;
            if (objectComponentDataIoRelationsResponse.Success)
                response = Response.ok(objectComponentDataIoRelationsResponse).build();
            else response = Response.serverError().entity(objectComponentDataIoRelationsResponse.Message + " " + objectComponentDataIoRelationsResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getObjectComponentDataIoRelations");
        return response;
    }
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCObjectComponentDataIoRelation(final ObjectComponentDataIoRelation objectComponenetDataIoRelation) {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering addCObjectComponentDataIoRelation");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertObjectComponentDataIoRelastionResponse insertObjectComponentDataIoRelastionResponse = new InsertObjectComponentDataIoRelastionResponse(objectComponenetDataIoRelation, connection);
        Response response = null;
        if (insertObjectComponentDataIoRelastionResponse != null) {
            insertObjectComponentDataIoRelastionResponse.Version = Config.version;
            if (insertObjectComponentDataIoRelastionResponse.Success)
                response = Response.ok(insertObjectComponentDataIoRelastionResponse).build();
            else response = Response.serverError().entity(insertObjectComponentDataIoRelastionResponse.Message + " " + insertObjectComponentDataIoRelastionResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving addCObjectComponentDataIoRelation");
        return response;
    }
    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateObjectcompoentDataIORelation(final ObjectComponentDataIoRelation objectComponenetDataIoRelation) {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering updateObjectcompoentDataIORelation");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateObjectComponentDataIoRelationResponse updateObjectComponentDataIoRelationResponse = new UpdateObjectComponentDataIoRelationResponse(objectComponenetDataIoRelation, connection);
        Response response = null;
        if (updateObjectComponentDataIoRelationResponse != null) {
            updateObjectComponentDataIoRelationResponse.Version = Config.version;
            if (updateObjectComponentDataIoRelationResponse.Success)
                response = Response.ok(updateObjectComponentDataIoRelationResponse).build();
            else response = Response.serverError().entity(updateObjectComponentDataIoRelationResponse.Message + " " + updateObjectComponentDataIoRelationResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateObjectcompoentDataIORelation");
        return response;
    }
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response  updateObjectcompoentDataIORelationEndTime(final TimeUpdate timeUpdate, @PathParam("id")UUID id) {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering updateObjectcompoentDataIORelationEndTime");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateObjectComponentDataIoRelationEndtimeResponse updateObjectComponentDataIoRelationEndtimeResponse = new UpdateObjectComponentDataIoRelationEndtimeResponse(id, timeUpdate.time, connection);
        Response response = null;
        if (updateObjectComponentDataIoRelationEndtimeResponse != null) {
            updateObjectComponentDataIoRelationEndtimeResponse.Version = Config.version;
            if (updateObjectComponentDataIoRelationEndtimeResponse.Success)
                response = Response.ok(updateObjectComponentDataIoRelationEndtimeResponse).build();
            else response = Response.serverError().entity(updateObjectComponentDataIoRelationEndtimeResponse.Message + " " + updateObjectComponentDataIoRelationEndtimeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateObjectcompoentDataIORelationEndTime");
        return response;
    }
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteObjectComponentDataIoRelation(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteObjectComponentDataIoRelation");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteObjectComponentDataIoRelationResponse deleteObjectComponentDataIoRelationResponse = new DeleteObjectComponentDataIoRelationResponse(id, connection);
        Response response = null;
        if (deleteObjectComponentDataIoRelationResponse != null) {
            deleteObjectComponentDataIoRelationResponse.Version = Config.version;
            if (deleteObjectComponentDataIoRelationResponse.Success)
                response = Response.ok(deleteObjectComponentDataIoRelationResponse).build();
            else response = Response.serverError().entity(deleteObjectComponentDataIoRelationResponse.Message + " " + deleteObjectComponentDataIoRelationResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteObjectComponentDataIoRelation");
        return response;
    }
    @DELETE
    @Path("/component/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteComponentRelations(@PathParam("id") Integer id, @QueryParam("componenttype") Integer componenttype) {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteComponentRelations");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteComponentRelationsResponse deleteComponentRelationsResponse = new DeleteComponentRelationsResponse(id, componenttype, connection);
        Response response = null;
        if (deleteComponentRelationsResponse != null) {
            deleteComponentRelationsResponse.Version = Config.version;
            if (deleteComponentRelationsResponse.Success)
                response = Response.ok(deleteComponentRelationsResponse).build();
            else response = Response.serverError().entity(deleteComponentRelationsResponse.Message + " " + deleteComponentRelationsResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();

        if(logger.isInfoEnabled())
            logger.info("Leaving deleteComponentRelations");
        return response;
    }
    @DELETE
    @Path("/dataio/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDataIoRelations(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteDataIoRelations");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteDataIoRelationsResponse deleteDataIoRelationsResponse = new DeleteDataIoRelationsResponse(id, connection);
        Response response = null;
        if (deleteDataIoRelationsResponse != null) {
            deleteDataIoRelationsResponse.Version = Config.version;
            if (deleteDataIoRelationsResponse.Success)
                response = Response.ok(deleteDataIoRelationsResponse).build();
            else response = Response.serverError().entity(deleteDataIoRelationsResponse.Message + " " + deleteDataIoRelationsResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteDataIoRelations");
        return response;
    }
    @DELETE
    @Path("/object/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteObjectRelations(@PathParam("value") String value, @QueryParam("field") String field, @QueryParam("fieldtype") String fieldtype, @QueryParam("objecttype")Integer objecttype) {
        Logger logger = Logger.getLogger(EndPointObjectComponentDataIoRelation.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteObjectRelations");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteObjectRelationsResponse deleteObjectRelationsResponse = new DeleteObjectRelationsResponse(value, field , fieldtype, objecttype, connection);
        Response response = null;
        if (deleteObjectRelationsResponse != null) {
            deleteObjectRelationsResponse.Version = Config.version;
            if (deleteObjectRelationsResponse.Success)
                response = Response.ok(deleteObjectRelationsResponse).build();
            else response = Response.serverError().entity(deleteObjectRelationsResponse.Message + " " + deleteObjectRelationsResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteObjectRelations");
        return response;
    }
}