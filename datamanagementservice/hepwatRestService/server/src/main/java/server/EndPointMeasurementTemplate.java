package server;

import dk.artogis.hepwat.measurement.MeasurementTemplate;
import dk.artogis.hepwat.measurement.response.*;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import org.apache.log4j.Logger;

@Path("/measurementtemplate")
public class EndPointMeasurementTemplate extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeasurementTemplate(@PathParam("id") UUID id) {
        Logger logger = Logger.getLogger(EndPointMeasurementTemplate.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getMeasurementTemplate");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        MeasurementTemplateResponse measurementTemplateResponse = new MeasurementTemplateResponse(id, connection);
        Response response = null;
        if (measurementTemplateResponse != null) {
            measurementTemplateResponse.Version = Config.version;
            if (measurementTemplateResponse.Success)
                response = Response.ok(measurementTemplateResponse).build();
            else response = Response.serverError().entity(measurementTemplateResponse.Message + " " +measurementTemplateResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getMeasurementTemplate");
        return response;
    }
//    @GET
//    @Path("/type/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getMeasurementTemplateByType(@PathParam("id") Integer id) {
//        Logger logger = Logger.getLogger(EndPointMeasurementTemplate.class);
//        if(logger.isInfoEnabled())
//            logger.info("Entering getMeasurementTemplateByType");
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        MeasurementTemplateResponse measurementTemplateResponse = new MeasurementTemplateResponse(id, connection);
//        Response response = null;
//        if (measurementTemplateResponse != null) {
//            measurementTemplateResponse.Version = Config.version;
//            if (measurementTemplateResponse.Success)
//                response = Response.ok(measurementTemplateResponse).build();
//            else response = Response.serverError().entity(measurementTemplateResponse.Message + " " +measurementTemplateResponse).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        if(logger.isInfoEnabled())
//            logger.info("Leaving getMeasurementTemplateByType");
//        return response;
//    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeasurementTemplates() {
        Logger logger = Logger.getLogger(EndPointMeasurementTemplate.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getMeasurementTemplates");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        MeasurementTemplatesResponse measurementTemplatesResponse = new MeasurementTemplatesResponse(connection);
        Response response = null;
        if (measurementTemplatesResponse!= null) {
            measurementTemplatesResponse.Version = Config.version;
            if (measurementTemplatesResponse.Success)
                response = Response.ok(measurementTemplatesResponse).build();
            else response = Response.serverError().entity(measurementTemplatesResponse.Message + " " + measurementTemplatesResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getMeasurementTemplates");
        return response;
    }
//    @POST
//    @Consumes("application/json")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addMeasurementTemplate(final MeasurementTemplate measurementTemplate) {
//        Logger logger = Logger.getLogger(EndPointMeasurementTemplate.class);
//        if(logger.isInfoEnabled())
//            logger.info("Entering addMeasurementTemplate");
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        InsertMeasurementTemplateResponse insertMeasurementTemplateResponse = new InsertMeasurementTemplateResponse(measurementTemplate, connection);
//        Response response = null;
//        if (insertMeasurementTemplateResponse != null) {
//            insertMeasurementTemplateResponse.Version = Config.version;
//            if (insertMeasurementTemplateResponse.Success)
//                response = Response.ok(insertMeasurementTemplateResponse).build();
//            else response = Response.serverError().entity(insertMeasurementTemplateResponse.Message  + " " + insertMeasurementTemplateResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        if(logger.isInfoEnabled())
//            logger.info("Leaving addMeasurementTemplate");
//        return response;
//    }
//    @PUT
//    @Consumes("application/json")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response updateMeasurementTemplate(final MeasurementTemplate measurementTemplate) {
//        Logger logger = Logger.getLogger(EndPointMeasurementTemplate.class);
//        if(logger.isInfoEnabled())
//            logger.info("Entering updateMeasurementTemplate");
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        UpdateMeasurementTemplateResponse updateMeasurementTemplateResponse = new UpdateMeasurementTemplateResponse(measurementTemplate, connection);
//        Response response = null;
//        if (updateMeasurementTemplateResponse != null) {
//            updateMeasurementTemplateResponse.Version = Config.version;
//            if (updateMeasurementTemplateResponse.Success)
//                response = Response.ok(updateMeasurementTemplateResponse).build();
//            else response = Response.serverError().entity(updateMeasurementTemplateResponse.Message +" " + updateMeasurementTemplateResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        if(logger.isInfoEnabled())
//            logger.info("Leaving updateMeasurementTemplate");
//        return response;
//    }
//    @DELETE
//    @Path("{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response deleteMeasurementTemplate(@PathParam("id") UUID id) {
//        Logger logger = Logger.getLogger(EndPointMeasurementTemplate.class);
//        if(logger.isInfoEnabled())
//            logger.info("Entering deleteMeasurementTemplate");
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        DeleteMeasurementTemplateResponse deleteMeasurementTemplateResponse = new DeleteMeasurementTemplateResponse(id, connection);
//        Response response = null;
//        if (deleteMeasurementTemplateResponse != null) {
//            deleteMeasurementTemplateResponse.Version = Config.version;
//            if (deleteMeasurementTemplateResponse.Success)
//                response = Response.ok(deleteMeasurementTemplateResponse).build();
//            else response = Response.serverError().entity(deleteMeasurementTemplateResponse.Message + " " + deleteMeasurementTemplateResponse).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        if(logger.isInfoEnabled())
//            logger.info("Leaving deleteMeasurementTemplate");
//        return response;
//    }
}