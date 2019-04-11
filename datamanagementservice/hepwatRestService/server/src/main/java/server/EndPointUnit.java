package server;


import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.measurement.MeasurementType;
import dk.artogis.hepwat.measurement.response.*;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

@Path("/unit")
public class EndPointUnit extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnit(@PathParam("id") Integer id, @QueryParam("language") String language) {
        Logger logger = Logger.getLogger(EndPointUnit.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getUnit");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UnitResponse unitResponse = new UnitResponse(id, language, connection);
        Response response = null;
        if (unitResponse != null) {
            unitResponse.Version = Config.version;
            if (unitResponse.Success)
                response = Response.ok(unitResponse).build();
            else response = Response.serverError().entity(unitResponse.Message + " " +unitResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getUnit");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnits(@QueryParam("language") String language) {
        Logger logger = Logger.getLogger(EndPointUnit.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getUnits");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UnitsResponse unitsResponse = new UnitsResponse(connection, language);
        Response response = null;
        if (unitsResponse!= null) {
            unitsResponse.Version = Config.version;
            if (unitsResponse.Success)
                response = Response.ok(unitsResponse).build();
            else response = Response.serverError().entity(unitsResponse.Message + " " + unitsResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getUnits");
        return response;
    }

}