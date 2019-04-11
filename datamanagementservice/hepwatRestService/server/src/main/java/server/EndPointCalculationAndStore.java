package server;

import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.calculation.response.*;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

@Path("/calculationandstore")
public class EndPointCalculationAndStore extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET //TODO: skal måske ændres
    @Path("/{dataioid}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCalculationAndStore( @PathParam("dataioid") Integer dataioid, @QueryParam("calculation") Integer calculation, @QueryParam("templatetype") Integer templatetype) {
        Logger logger = Logger.getLogger(EndPointCalculationAndStore.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getCalculationAndStore");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        CalculationAndStoreResponse calculationAndStoreResponse = new CalculationAndStoreResponse(calculation, dataioid, templatetype, connection);
        Response response = null;
        if (calculationAndStoreResponse != null) {
            calculationAndStoreResponse.Version = Config.version;
            if (calculationAndStoreResponse.Success)
                response = Response.ok(calculationAndStoreResponse).build();
            else response = Response.serverError().entity(calculationAndStoreResponse.Message + " " +calculationAndStoreResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getCalculationAndStore");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCalculationsAndStores() {
        Logger logger = Logger.getLogger(EndPointCalculationAndStore.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getCalculationsAndStores");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        CalculationsAndStoresResponse calculationsAndStoresResponse = new CalculationsAndStoresResponse(connection);
        Response response = null;
        if (calculationsAndStoresResponse!= null) {
            calculationsAndStoresResponse.Version = Config.version;
            if (calculationsAndStoresResponse.Success)
                response = Response.ok(calculationsAndStoresResponse).build();
            else response = Response.serverError().entity(calculationsAndStoresResponse.Message + " " + calculationsAndStoresResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getCalculationsAndStores");
        return response;
    }
    @GET
    @Path("/templateid/{templateid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCalculationsAndStoresByTemplateId( @PathParam("templateid") Integer templateid) {
        Logger logger = Logger.getLogger(EndPointCalculationAndStore.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getCalculationsAndStoresByTemplateId");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        CalculationsAndStoresResponse calculationsAndStoresResponse = new CalculationsAndStoresResponse(templateid, connection);
        Response response = null;
        if (calculationsAndStoresResponse!= null) {
            calculationsAndStoresResponse.Version = Config.version;
            if (calculationsAndStoresResponse.Success)
                response = Response.ok(calculationsAndStoresResponse).build();
            else response = Response.serverError().entity(calculationsAndStoresResponse.Message + " " + calculationsAndStoresResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getCalculationsAndStoresByTemplateId");
        return response;
    }
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCalculationAndStore(final CalculationAndStore calculationAndStore) {
        Logger logger = Logger.getLogger(EndPointCalculationAndStore.class);
        if(logger.isInfoEnabled())
            logger.info("Entering addCalculationAndStore");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertCalculationAndStoreResponse insertCalculationAndStoreResponse = new InsertCalculationAndStoreResponse(calculationAndStore, connection);
        Response response = null;
        if (insertCalculationAndStoreResponse != null) {
            insertCalculationAndStoreResponse.Version = Config.version;
            if (insertCalculationAndStoreResponse.Success)
                response = Response.ok(insertCalculationAndStoreResponse).build();
            else response = Response.serverError().entity(insertCalculationAndStoreResponse.Message  + " " + insertCalculationAndStoreResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving addCalculationAndStore");
        return response;
    }
    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCalculationAndStore(final CalculationAndStore calculationAndStore) {
        Logger logger = Logger.getLogger(EndPointCalculationAndStore.class);
        if(logger.isInfoEnabled())
            logger.info("Entering updateCalculationAndStore");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateCalculationandStoreResponse updateCalculationandStoreResponse = new UpdateCalculationandStoreResponse(calculationAndStore, connection);
        Response response = null;
        if (updateCalculationandStoreResponse != null) {
            updateCalculationandStoreResponse.Version = Config.version;
            if (updateCalculationandStoreResponse.Success)
                response = Response.ok(updateCalculationandStoreResponse).build();
            else response = Response.serverError().entity(updateCalculationandStoreResponse.Message +" " + updateCalculationandStoreResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateCalculationAndStore");
        return response;
    }

    @DELETE //TODO: skal måske ændres
    @Path("/{dataioid}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCalculationAndStore( @PathParam("dataioid") Integer dataioid, @QueryParam("templatetype") Integer templatetype, @QueryParam("calculation") Integer calculation) {
        Logger logger = Logger.getLogger(EndPointCalculationAndStore.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteCalculationAndStore");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteCalculationAndStoreResponse deleteCalculationAndStoreResponse = new DeleteCalculationAndStoreResponse( dataioid, templatetype, calculation, connection);
        Response response = null;
        if (deleteCalculationAndStoreResponse != null) {
            deleteCalculationAndStoreResponse.Version = Config.version;
            if (deleteCalculationAndStoreResponse.Success)
                response = Response.ok(deleteCalculationAndStoreResponse).build();
            else response = Response.serverError().entity(deleteCalculationAndStoreResponse.Message + " " +deleteCalculationAndStoreResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteCalculationAndStore");
        return response;
    }

}