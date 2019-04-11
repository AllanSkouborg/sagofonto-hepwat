package server;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.datastore.DataStore;
import dk.artogis.hepwat.datastore.response.*;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import org.apache.log4j.Logger;

@Path("/datastore")
public class EndPointDataStore extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataStore(@PathParam("id") UUID id) {
        Logger logger = Logger.getLogger(EndPointDataStore.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getDataStore");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DataStoreResponse dataStoreResponse = new DataStoreResponse(id, connection);
        Response response = null;
        if (dataStoreResponse != null) {
            dataStoreResponse.Version = Config.version;
            if (dataStoreResponse.Success)
                response = Response.ok(dataStoreResponse).build();
            else response = Response.serverError().entity(dataStoreResponse.Message + " " + dataStoreResponse.Error).build();
        }
        else response =  Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getDataStore");
        return response;

    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataStores() {
        Logger logger = Logger.getLogger(EndPointDataStore.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getDataStores");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DataStoresResponse dataStoresResponse = new DataStoresResponse(connection);
        Response response = null;
        if (dataStoresResponse != null) {
            dataStoresResponse.Version = Config.version;
            if (dataStoresResponse.Success)
                response = Response.ok(dataStoresResponse).build();
            else response = Response.serverError().entity(dataStoresResponse.Message + " " +dataStoresResponse.Error).build();
        }
        else response =  Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getDataStores");
        return response;
    }
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDataStore(final DataStore dataStore) {
        Logger logger = Logger.getLogger(EndPointDataStore.class);
        if(logger.isInfoEnabled())
            logger.info("Entering addDataStore");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertDataStoreResponse insertDataStoreResponse = new InsertDataStoreResponse(dataStore, connection);
        Response response = null;
        if (insertDataStoreResponse != null) {
            insertDataStoreResponse.Version = Config.version;
            if (insertDataStoreResponse.Success)
                response = Response.ok(insertDataStoreResponse).build();
            else response = Response.serverError().entity(insertDataStoreResponse.Message + " " + insertDataStoreResponse.Error).build();
        }
        else response =  Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving addDataStore");
        return response;
    }
    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDataStore(final DataStore dataStore) {
        Logger logger = Logger.getLogger(EndPointDataStore.class);
        if(logger.isInfoEnabled())
            logger.info("Entering updateDataStore");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateDataStoreResponse updateDataStoreResponse = new UpdateDataStoreResponse(dataStore, connection);
        Response response = null;
        if (updateDataStoreResponse !=  null) {
            updateDataStoreResponse.Version = Config.version;
            if (updateDataStoreResponse.Success)
                response = Response.ok(updateDataStoreResponse).build();
            else response = Response.serverError().entity(updateDataStoreResponse.Message + " " + updateDataStoreResponse.Error).build();
        }
        else  response =  Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateDataStore");
        return response;
    }
    @DELETE
    @Path("{id}")
    @Consumes("application/json")
    public Response deleteDataStore(@PathParam("id") UUID id) {
        Logger logger = Logger.getLogger(EndPointDataStore.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteDataStore");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteDataStoreResponse deleteDataStoreResponse = new DeleteDataStoreResponse(id, connection);
        Response response = null;
        if (deleteDataStoreResponse !=  null) {
            deleteDataStoreResponse.Version = Config.version;
            if (deleteDataStoreResponse.Success)
                response = Response.ok(deleteDataStoreResponse).build();
            else response = Response.serverError().entity(deleteDataStoreResponse.Message + " " + deleteDataStoreResponse.Error).build();
        }
        else  response =  Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteDataStore");
        return response;
    }
}