//package server;
//
//import dk.artogis.hepwat.measurement.MeasurementTemplateType;
//import dk.artogis.hepwat.measurement.response.InsertMeasurementTemplateTypeResponse;
//import dk.artogis.hepwat.measurement.response.MeasurementTemplateTypeResponse;
//import dk.artogis.hepwat.measurement.response.MeasurementTemplateTypesResponse;
//import dk.artogis.hepwat.measurement.response.UpdateMeasurementTemplateTypeResponse;
//import dk.artogis.hepwat.common.database.PostGreSQLConnection;
//
//import javax.servlet.http.HttpServlet;
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//
//@Path("/measurementtemplatetype")
//public class EndPointMeasurementTemplateType extends HttpServlet{
//
//    /**
//     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
//     * to the client as "text/plain" media type.
//     *
//     * @return String that will be returned as a text/plain response.
//     */
//
//    @GET
//    @Path("{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getMeasurementTemplateType(@PathParam("id") Integer id) {
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        MeasurementTemplateTypeResponse measurementTemplateTypeResponse = new MeasurementTemplateTypeResponse(id, connection);
//        Response response = null;
//        if (measurementTemplateTypeResponse != null) {
//            measurementTemplateTypeResponse.Version = Config.version;
//            if (measurementTemplateTypeResponse.Success)
//                response = Response.ok(measurementTemplateTypeResponse).build();
//            else response = Response.serverError().entity(measurementTemplateTypeResponse.Message + " " +measurementTemplateTypeResponse).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        return response;
//    }
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getMeasurementTemplateTypes() {
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        MeasurementTemplateTypesResponse measurementTemplateTypesResponse = new MeasurementTemplateTypesResponse(connection);
//        Response response = null;
//        if (measurementTemplateTypesResponse!= null) {
//            measurementTemplateTypesResponse.Version = Config.version;
//            if (measurementTemplateTypesResponse.Success)
//                response = Response.ok(measurementTemplateTypesResponse).build();
//            else response = Response.serverError().entity(measurementTemplateTypesResponse.Message + " " + measurementTemplateTypesResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        return response;
//    }
//    @POST
//    @Consumes("application/json")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addMeasurementTemplateType(final MeasurementTemplateType measurementTemplateType) {
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        InsertMeasurementTemplateTypeResponse insertMeasurementTemplateTypeResponse = new InsertMeasurementTemplateTypeResponse(measurementTemplateType, connection);
//        Response response = null;
//        if (insertMeasurementTemplateTypeResponse != null) {
//            insertMeasurementTemplateTypeResponse.Version = Config.version;
//            if (insertMeasurementTemplateTypeResponse.Success)
//                response = Response.ok(insertMeasurementTemplateTypeResponse).build();
//            else response = Response.serverError().entity(insertMeasurementTemplateTypeResponse.Message  + " " + insertMeasurementTemplateTypeResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        return response;
//    }
//    @PUT
//    @Consumes("application/json")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response updateMeasurementTemplateType(final MeasurementTemplateType measurementTemplateType) {
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        UpdateMeasurementTemplateTypeResponse updateMeasurementTemplateTypeResponse = new UpdateMeasurementTemplateTypeResponse(measurementTemplateType, connection);
//        Response response = null;
//        if (updateMeasurementTemplateTypeResponse != null) {
//            updateMeasurementTemplateTypeResponse.Version = Config.version;
//            if (updateMeasurementTemplateTypeResponse.Success)
//                response = Response.ok(updateMeasurementTemplateTypeResponse).build();
//            else response = Response.serverError().entity(updateMeasurementTemplateTypeResponse.Message +" " + updateMeasurementTemplateTypeResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        return response;
//    }
//}