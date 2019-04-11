package server;

import dk.artogis.hepwat.calculation.AggregationType;
import dk.artogis.hepwat.calculation.response.*;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

@Path("/aggregationtype")
public class EndPointAggregationType extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAggregationType(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointAggregationType.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getAggregationType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        AggregationTypeResponse aggregationTypeResponse = new AggregationTypeResponse(id, connection);
        Response response = null;
        if (aggregationTypeResponse != null) {
            aggregationTypeResponse.Version = Config.version;
            if (aggregationTypeResponse.Success)
                response = Response.ok(aggregationTypeResponse).build();
            else response = Response.serverError().entity(aggregationTypeResponse.Message + " " +aggregationTypeResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getAggregationType");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAggregationTypes() {
        Logger logger = Logger.getLogger(EndPointAggregationType.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getAggregationTypes");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        AggregationTypesResponse aggregationTypesResponse = new AggregationTypesResponse(connection);
        Response response = null;
        if (aggregationTypesResponse!= null) {
            aggregationTypesResponse.Version = Config.version;
            if (aggregationTypesResponse.Success)
                response = Response.ok(aggregationTypesResponse).build();
            else response = Response.serverError().entity(aggregationTypesResponse.Message + " " + aggregationTypesResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getAggregationTypes");
        return response;
    }
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAggregationType(final AggregationType aggregationType) {
        Logger logger = Logger.getLogger(EndPointAggregationType.class);
        if(logger.isInfoEnabled())
            logger.info("Entering addAggregationType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertAggregationTypeResponse insertAggregationTypeResponse = new InsertAggregationTypeResponse(aggregationType, connection);
        Response response = null;
        if (insertAggregationTypeResponse != null) {
            insertAggregationTypeResponse.Version = Config.version;
            if (insertAggregationTypeResponse.Success)
                response = Response.ok(insertAggregationTypeResponse).build();
            else response = Response.serverError().entity(insertAggregationTypeResponse.Message  + " " + insertAggregationTypeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving addAggregationType");
        return response;
    }
    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAggregationType(final AggregationType aggregationType) {
        Logger logger = Logger.getLogger(EndPointAggregationType.class);
        if(logger.isInfoEnabled())
            logger.info("Entering updateAggregationType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateAggregationTypeResponse updateAggregationTypeResponse = new UpdateAggregationTypeResponse(aggregationType, connection);
        Response response = null;
        if (updateAggregationTypeResponse != null) {
            updateAggregationTypeResponse.Version = Config.version;
            if (updateAggregationTypeResponse.Success)
                response = Response.ok(updateAggregationTypeResponse).build();
            else response = Response.serverError().entity(updateAggregationTypeResponse.Message +" " + updateAggregationTypeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateAggregationType");
        return response;
    }
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletetAggregationType(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointAggregationType.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deletetAggregationType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteAggregationTypeResponse deleteAggregationTypeResponse = new DeleteAggregationTypeResponse(id, connection);
        Response response = null;
        if (deleteAggregationTypeResponse != null) {
            deleteAggregationTypeResponse.Version = Config.version;
            if (deleteAggregationTypeResponse.Success)
                response = Response.ok(deleteAggregationTypeResponse).build();
            else response = Response.serverError().entity(deleteAggregationTypeResponse.Message + " " + deleteAggregationTypeResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deletetAggregationType");
        return response;
    }
}