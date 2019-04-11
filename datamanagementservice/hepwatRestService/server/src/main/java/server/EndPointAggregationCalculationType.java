package server;

import dk.artogis.hepwat.calculation.AggregationCalculationType;
import dk.artogis.hepwat.calculation.AggregationType;
import dk.artogis.hepwat.calculation.response.*;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;


@Path("/aggregationcalculationtype")
public class EndPointAggregationCalculationType extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAggregationCalculationType(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointAggregationCalculationType.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getAggregationCalculationType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        AggregationCalculationTypeResponse aggregationCalculationTypeResponse = new AggregationCalculationTypeResponse(id, connection);
        Response response = null;
        if (aggregationCalculationTypeResponse != null) {
            aggregationCalculationTypeResponse.Version = Config.version;
            if (aggregationCalculationTypeResponse.Success)
                response = Response.ok(aggregationCalculationTypeResponse).build();
            else response = Response.serverError().entity(aggregationCalculationTypeResponse.Message + " " +aggregationCalculationTypeResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getAggregationCalculationType");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAggregationCalculationTypes() {
        Logger logger = Logger.getLogger(EndPointAggregationCalculationType.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getAggregationCalculationTypes");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        AggregationCalculationTypesResponse aggregationCalculationTypesResponse = new AggregationCalculationTypesResponse(connection);
        Response response = null;
        if (aggregationCalculationTypesResponse!= null) {
            aggregationCalculationTypesResponse.Version = Config.version;
            if (aggregationCalculationTypesResponse.Success)
                response = Response.ok(aggregationCalculationTypesResponse).build();
            else response = Response.serverError().entity(aggregationCalculationTypesResponse.Message + " " + aggregationCalculationTypesResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getAggregationCalculationTypes");
        return response;
    }
//    @PUT
//    @Consumes("application/json")
//    public Response addAggregationType(final AggregationType aggregationType) {
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        InsertAggregationTypeResponse insertAggregationTypeResponse = new InsertAggregationTypeResponse(aggregationType, connection);
//        Response response = null;
//        if (insertAggregationTypeResponse != null) {
//            insertAggregationTypeResponse.Version = Config.version;
//            if (insertAggregationTypeResponse.Success)
//                response = Response.ok(insertAggregationTypeResponse).build();
//            else response = Response.serverError().entity(insertAggregationTypeResponse.Message  + " " + insertAggregationTypeResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        return response;
//    }
//    @POST
//    @Consumes("application/json")
//    public Response updateAggregationType(final AggregationType aggregationType) {
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        UpdateAggregationTypeResponse updateAggregationTypeResponse = new UpdateAggregationTypeResponse(aggregationType, connection);
//        Response response = null;
//        if (updateAggregationTypeResponse != null) {
//            updateAggregationTypeResponse.Version = Config.version;
//            if (updateAggregationTypeResponse.Success)
//                response = Response.ok(updateAggregationTypeResponse).build();
//            else response = Response.serverError().entity(updateAggregationTypeResponse.Message +" " + updateAggregationTypeResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        return response;
//    }
//    @DELETE
//    @Path("{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response deleteObjectType(@PathParam("id") Integer id) {
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        DeleteAggregationTypeResponse deleteAggregationTypeResponse = new DeleteAggregationTypeResponse(id, connection);
//        Response response = null;
//        if (deleteAggregationTypeResponse != null) {
//            deleteAggregationTypeResponse.Version = Config.version;
//            if (deleteAggregationTypeResponse.Success)
//                response = Response.ok(deleteAggregationTypeResponse).build();
//            else response = Response.serverError().entity(deleteAggregationTypeResponse.Message + " " + deleteAggregationTypeResponse).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        return response;
//    }
}