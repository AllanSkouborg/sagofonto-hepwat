package server;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.supportlayer.SupportLayer;

import dk.artogis.hepwat.supportlayer.response.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import org.apache.log4j.Logger;

@Path("/supportlayer")
public class EndPointSupportLayer {

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupportLayer(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointSupportLayer.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getSupportLayer");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        SupportLayerResponse supportLayerResponse = new SupportLayerResponse(id, connection);
        Response response = null;
        if (supportLayerResponse != null) {
            supportLayerResponse.Version = Config.version;
            if (supportLayerResponse.Success) {
                response = Response.ok(supportLayerResponse).build();
            }
            else {
                response = Response.serverError().entity(supportLayerResponse.Message + " " + supportLayerResponse.Error).build();
            }
        }
        else {
            response = Response.serverError().entity("could not retrieve response").build();
        }
        if(logger.isInfoEnabled()) {
            logger.info("Leaving getSupportLayer");
        }

        return response;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupportLayers() {
        Logger logger = Logger.getLogger(EndPointComponenttype.class);
        if(logger.isInfoEnabled()) {
            logger.info("Entering getSupportLayers");
        }

        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        SupportLayersResponse supportLayersResponse = new SupportLayersResponse(connection);
        Response response = null;

        if (supportLayersResponse!= null) {
            supportLayersResponse.Version = Config.version;
            if (supportLayersResponse.Success) {
                response = Response.ok(supportLayersResponse).build();
            }
            else {
                response = Response.serverError().entity(supportLayersResponse.Message + " " + supportLayersResponse.Error).build();
            }
        }
        else {
            response = Response.serverError().entity("could not retrieve response").build();
        }

        if(logger.isInfoEnabled()) {
            logger.info("Leaving getSupportLayers");
        }

        return response;
    }

}