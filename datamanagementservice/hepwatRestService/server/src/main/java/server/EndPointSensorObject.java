package server;

import dk.artogis.hepwat.dataio.SensorObject;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.dataio.response.SensorObjectResponse;
import dk.artogis.hepwat.dataio.response.SensorObjectsResponse;
import dk.artogis.hepwat.dataio.response.UpdateSensorObjectResponse;
import dk.artogis.hepwat.dataio.response.UpdateUnConfiguredDataIoResponse;
//import dk.artogis.hepwat.dataio.response.InsertSensorObjectResponse;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

@Path("/sensorobject")
public class EndPointSensorObject extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{datasourceid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorObject(@QueryParam("sensorobjectid") String sensorObjectId, @PathParam("datasourceid")Integer datasourceId) {
        Logger logger = Logger.getLogger(EndPointSensorObject.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getSensorObject");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        SensorObjectResponse sensorObjectResponse = new SensorObjectResponse(sensorObjectId, datasourceId,  connection);
        Response response = null;
        if (sensorObjectResponse != null) {
            sensorObjectResponse.Version = Config.version;
            if (sensorObjectResponse.Success)
                response = Response.ok(sensorObjectResponse).build();
            else response = Response.serverError().entity(sensorObjectResponse.Message + " " + sensorObjectResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getSensorObject");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorObjects() {
        Logger logger = Logger.getLogger(EndPointSensorObject.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getSensorObjects");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        SensorObjectsResponse sensorObjectsResponse = new SensorObjectsResponse(connection);
        Response response = null;
        if (sensorObjectsResponse!= null) {
            sensorObjectsResponse.Version = Config.version;
            if (sensorObjectsResponse.Success)
                response = Response.ok(sensorObjectsResponse).build();
            else response = Response.serverError().entity(sensorObjectsResponse.Message + " " + sensorObjectsResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getSensorObjects");
        return response;
    }

    @PUT
    @Path("{datasourceid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUnConfiguredSensorObject( final ConfiguredState configuredState, @QueryParam("sensorobjectid") String sensorObjectId, @PathParam("datasourceid")Integer datasourceId) {
        Logger logger = Logger.getLogger(EndPointSensorObject.class);
        if(logger.isInfoEnabled()) {
            logger.info("Entering updateUnConfiguredSensorObject");
        }



        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);

        UpdateSensorObjectResponse updateSensorObjectResponse = new UpdateSensorObjectResponse(sensorObjectId, datasourceId, configuredState.configured, connection);
        Response response = null;

        if (updateSensorObjectResponse != null) {
            updateSensorObjectResponse.Version = Config.version;
            if (updateSensorObjectResponse.Success) {
                response = Response.ok(updateSensorObjectResponse).build();
            }
            else {
                response = Response.serverError().entity(updateSensorObjectResponse.Message + " " + updateSensorObjectResponse).build();
            }
        }
        else {
            response = Response.serverError().entity("could not retrieve response").build();
        }
        if(logger.isInfoEnabled()) {
            logger.info("Leaving updateUnConfiguredSensorObject");
        }
        return response;
    }

    @PUT
    @Path("{datasourceid}/alias")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSensorObjectNameAlias( final NameAliasState nameAliasState, @QueryParam("sensorobjectid") String sensorObjectId, @PathParam("datasourceid")Integer datasourceId) {
        Logger logger = Logger.getLogger(EndPointSensorObject.class);
        if(logger.isInfoEnabled()) {
            logger.info("Entering updateUnConfiguredSensorObject");
        }



        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);

        UpdateSensorObjectResponse updateSensorObjectResponse = new UpdateSensorObjectResponse(sensorObjectId, datasourceId, nameAliasState.nameAlias, connection);
        Response response = null;

        if (updateSensorObjectResponse != null) {
            updateSensorObjectResponse.Version = Config.version;
            if (updateSensorObjectResponse.Success) {
                response = Response.ok(updateSensorObjectResponse).build();
            }
            else {
                response = Response.serverError().entity(updateSensorObjectResponse.Message + " " + updateSensorObjectResponse).build();
            }
        }
        else {
            response = Response.serverError().entity("could not retrieve response").build();
        }
        if(logger.isInfoEnabled()) {
            logger.info("Leaving updateUnConfiguredSensorObject");
        }
        return response;
    }


/* //endpoint is closed as data should only be inserted from robot
    @PUT
    @Consumes("application/json")
    public Response addSensorObject(final SensorObject sensorObject) {
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertSensorObjectResponse insertSensorObjectResponse = new InsertSensorObjectResponse(sensorObject, connection);
        Response response = null;
        if (insertSensorObjectResponse != null) {
            insertSensorObjectResponse.Version = Config.version;
            if (insertSensorObjectResponse.Success)
                response = Response.ok(insertSensorObjectResponse).build();
            else response = Response.serverError().entity(insertSensorObjectResponse.Message  + " " + insertSensorObjectResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        return response;
    }
*/



}