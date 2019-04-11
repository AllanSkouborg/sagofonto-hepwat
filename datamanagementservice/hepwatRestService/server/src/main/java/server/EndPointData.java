package server;

import dk.artogis.hepwat.calculation.response.DataType;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.data.response.DataResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Path("/data")
public class EndPointData extends HttpServlet{
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss' GMT'X");
    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getData(@QueryParam("id") Integer id, @QueryParam("starttime") String starttime, @QueryParam("endtime") String endtime, @QueryParam("aggtype") Integer aggtype, @QueryParam("calctype") Integer calctype, @QueryParam("datatype") Integer datatype, @QueryParam("limit") Integer limit, @QueryParam("lasttimestamp") Long lasttimestamp) {
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        Response response = null;
        ZonedDateTime zStartTime = null;
        ZonedDateTime zEndTime = null;
        Logger logger = Logger.getLogger(EndPointData.class);
        if(logger.isInfoEnabled()) {
            logger.info("Start executing request for data ");
        }
        try {
            zStartTime = ZonedDateTime.parse(starttime, dateTimeFormatter);
            zEndTime = ZonedDateTime.parse(endtime, dateTimeFormatter);
        }
        catch (Exception ex)
        {
            response = Response.serverError().entity("parameters wrong format, not dates").build();
            return response;
        }
        try {
            if (aggtype == null)
                throw new Exception("aggtype is null, must be integer");
            if (calctype == null)
                throw new Exception("calctype is null, must be integer");
            if (limit == null)
                throw new Exception("limit is null, must be integer");
            if (datatype == null)
                datatype = DataType.TypeData;
            if (lasttimestamp != null)
            {
                ZonedDateTime zlastTimeStamp = ZonedDateTime.parse(starttime, dateTimeFormatter);
            }
        }
        catch (Exception ex)
        {
            response = Response.serverError().entity("parameters wrong " + ex.getMessage() ).build();
            return response;
        }
        DataResponse dataResponse = new DataResponse(id, zStartTime, zEndTime, aggtype, calctype, datatype, limit, lasttimestamp, connection);

        if (dataResponse != null) {
            dataResponse.Version = Config.version;
            if (dataResponse.Success)
                response = Response.ok(dataResponse).build();
            else response = Response.serverError().entity(dataResponse.Message + " " + dataResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();

        if(logger.isInfoEnabled()) {
            logger.info("End executing request for data ");
        }

        return response;
    }
    @GET
    @Path("/last/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLastData(@PathParam("id") Integer id, @QueryParam("aggtype") Integer aggtype, @QueryParam("calctype") Integer calctype, @QueryParam("datatype") Integer datatype ) {
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        Response response = null;
        Logger logger = Logger.getLogger(EndPointData.class);
        if(logger.isInfoEnabled()) {
            logger.info("Start executing request for data ");
        }
        try {
            if (aggtype == null)
                throw new Exception("aggtype is null, must be integer");
            if (calctype == null)
                throw new Exception("calctype is null, must be integer");
            if (datatype == null)
                datatype = DataType.TypeData;
        }
        catch (Exception ex)
        {
            response = Response.serverError().entity("parameters wrong " + ex.getMessage() ).build();
            return response;
        }
        DataResponse dataResponse = new DataResponse(id,  aggtype, calctype, datatype, connection);

        if (dataResponse != null) {
            dataResponse.Version = Config.version;
            if (dataResponse.Success)
                response = Response.ok(dataResponse).build();
            else response = Response.serverError().entity(dataResponse.Message + " " + dataResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();

        if(logger.isInfoEnabled()) {
            logger.info("End executing request for data ");
        }

        return response;
    }
}