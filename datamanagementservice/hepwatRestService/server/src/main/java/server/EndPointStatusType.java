package server;


import dk.artogis.hepwat.calculation.response.StatusTypesResponse;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/statustype")
public class EndPointStatusType extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatusTypes() {
        Logger logger = Logger.getLogger(EndPointStatusType.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getStatusTypes");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        StatusTypesResponse statusTypesResponse = new StatusTypesResponse(connection);
        Response response = null;
        if (statusTypesResponse!= null) {
            statusTypesResponse.Version = Config.version;
            if (statusTypesResponse.Success)
                response = Response.ok(statusTypesResponse).build();
            else response = Response.serverError().entity(statusTypesResponse.Message + " " + statusTypesResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getStatusTypes");
        return response;
    }

}