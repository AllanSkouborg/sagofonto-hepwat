package server;


import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.measurement.response.UnitResponse;
import dk.artogis.hepwat.measurement.response.UnitsResponse;
import org.apache.log4j.Logger;
import response.VersionResponse;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/version")
public class EndPointVersion extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVersion() {
        Logger logger = Logger.getLogger(EndPointVersion.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getUnits");
        VersionResponse versionResponse = new VersionResponse();
        Response response = null;
        if (versionResponse!= null) {
            if (versionResponse.Success)
                response = Response.ok(versionResponse).build();
            else response = Response.serverError().entity(versionResponse.Message + " " + versionResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getVersion");
        return response;
    }

}