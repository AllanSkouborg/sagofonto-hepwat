package server;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.data.response.DataStoreResponse;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

@Path("/bigdatastore")
public class EndPointBigDataStore extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataStore(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointBigDataStore.class);
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

}