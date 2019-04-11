package server;


import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.measurement.MeasurementType;
import dk.artogis.hepwat.measurement.response.*;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

@Path("/measurementtype")
public class EndPointMeasurementType extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeasurementType(@PathParam("id") Integer id,@QueryParam("language") String language) {
        Logger logger = Logger.getLogger(EndPointMeasurementType.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getMeasurementType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        MeasurementTypeResponse measurementTypeResponse = new MeasurementTypeResponse(id, language, connection);
        Response response = null;
        if (measurementTypeResponse != null) {
            measurementTypeResponse.Version = Config.version;
            if (measurementTypeResponse.Success)
                response = Response.ok(measurementTypeResponse).build();
            else response = Response.serverError().entity(measurementTypeResponse.Message + " " +measurementTypeResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getMeasurementType");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeasurementTypes() {
        Logger logger = Logger.getLogger(EndPointMeasurementType.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getMeasurementTypes");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        MeasurementTypesResponse measurementTypesResponse = new MeasurementTypesResponse(connection);
        Response response = null;
        if (measurementTypesResponse!= null) {
            measurementTypesResponse.Version = Config.version;
            if (measurementTypesResponse.Success)
                response = Response.ok(measurementTypesResponse).build();
            else response = Response.serverError().entity(measurementTypesResponse.Message + " " + measurementTypesResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getMeasurementTypes");
        return response;
    }
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMeasurementType(final MeasurementType measurementType) {
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertMeasurementTypeResponse insertMeasurementTypeResponse = new InsertMeasurementTypeResponse(measurementType, connection);
        Response response = null;
        if (insertMeasurementTypeResponse != null) {
            insertMeasurementTypeResponse.Version = Config.version;
            if (insertMeasurementTypeResponse.Success)
                response = Response.ok(insertMeasurementTypeResponse).build();
            else response = Response.serverError().entity(insertMeasurementTypeResponse.Message  + " " + insertMeasurementTypeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        return response;
    }

    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMeasurementType(final MeasurementType measurementType) {
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateMeasurementTypeResponse updateMeasurementTypeResponse = new UpdateMeasurementTypeResponse(measurementType, connection);
        Response response = null;
        if (updateMeasurementTypeResponse != null) {
            updateMeasurementTypeResponse.Version = Config.version;
            if (updateMeasurementTypeResponse.Success)
                response = Response.ok(updateMeasurementTypeResponse).build();
            else response = Response.serverError().entity(updateMeasurementTypeResponse.Message +" " + updateMeasurementTypeResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        return response;
    }
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteObjectType(@PathParam("id") Integer id, @QueryParam("language") String language) {
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteMeasurementTypeResponse deleteMeasurementTypeResponse = new DeleteMeasurementTypeResponse(id, language, connection);
        Response response = null;
        if (deleteMeasurementTypeResponse != null) {
            deleteMeasurementTypeResponse.Version = Config.version;
            if (deleteMeasurementTypeResponse.Success)
                response = Response.ok(deleteMeasurementTypeResponse).build();
            else response = Response.serverError().entity(deleteMeasurementTypeResponse.Message + " " + deleteMeasurementTypeResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        return response;
    }
}