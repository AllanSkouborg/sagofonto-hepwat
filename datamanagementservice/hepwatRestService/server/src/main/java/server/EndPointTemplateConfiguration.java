package server;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.dataconfig.Configuration;
import dk.artogis.hepwat.dataconfig.ConfigurationTemplate;
import dk.artogis.hepwat.dataconfig.response.*;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import org.apache.log4j.Logger;

//import services.configuration.response.*;


@Path("/templateconfiguration")
public class EndPointTemplateConfiguration extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfigurationTemplate(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointTemplateConfiguration.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getConfigurationTemplate");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ConfigurationTemplateResponse configurationTemplateResponse = new ConfigurationTemplateResponse(id, connection);
        Response response = null;
        if (configurationTemplateResponse != null) {
            configurationTemplateResponse.Version = Config.version;
            if (configurationTemplateResponse.Success)
                response = Response.ok(configurationTemplateResponse).build();
            else response = Response.serverError().entity(configurationTemplateResponse.Message + " " +configurationTemplateResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getConfigurationTemplate");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfigurationTemplates() {
        Logger logger = Logger.getLogger(EndPointTemplateConfiguration.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getConfigurationTemplates");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ConfigurationTemplatesResponse configurationTemplatesResponse = new ConfigurationTemplatesResponse(connection);
        Response response = null;
        if (configurationTemplatesResponse!= null) {
            configurationTemplatesResponse.Version = Config.version;
            if (configurationTemplatesResponse.Success)
                response = Response.ok(configurationTemplatesResponse).build();
            else response = Response.serverError().entity(configurationTemplatesResponse.Message + " " + configurationTemplatesResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getConfigurationTemplates");
        return response;
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addConfigurationTemplate(final ConfigurationTemplate configurationTemplate) {
        Logger logger = Logger.getLogger(EndPointTemplateConfiguration.class);
        if(logger.isInfoEnabled())
            logger.info("Entering addConfigurationTemplate");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertConfigurationTemplateResponse insertConfigurationTemplateResponse = new InsertConfigurationTemplateResponse(configurationTemplate, connection);
        Response response = null;
        if (insertConfigurationTemplateResponse != null) {
            insertConfigurationTemplateResponse.Version = Config.version;
            if (insertConfigurationTemplateResponse.Success)
                response = Response.ok(insertConfigurationTemplateResponse).build();
            else response = Response.serverError().entity(insertConfigurationTemplateResponse.Message  + " " + insertConfigurationTemplateResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving addConfigurationTemplate");
        return response;
    }
    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateConfigurationTemplate(final ConfigurationTemplate configurationTemplate) {
        Logger logger = Logger.getLogger(EndPointTemplateConfiguration.class);
        if(logger.isInfoEnabled())
            logger.info("Entering updateConfigurationTemplate");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateConfigurationTemplateResponse updateConfigurationTemplateResponse = new UpdateConfigurationTemplateResponse(configurationTemplate, connection);
        Response response = null;
        if (updateConfigurationTemplateResponse != null) {
            updateConfigurationTemplateResponse.Version = Config.version;
            if (updateConfigurationTemplateResponse.Success)
                response = Response.ok(updateConfigurationTemplateResponse).build();
            else response = Response.serverError().entity(updateConfigurationTemplateResponse.Message +" " + updateConfigurationTemplateResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateConfigurationTemplate");
        return response;
    }
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteConfigurationTemplate(@PathParam("id") UUID id) {
        Logger logger = Logger.getLogger(EndPointTemplateConfiguration.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteConfigurationTemplate");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteConfigurationTemplateResponse deleteConfigurationTemplateResponse = new DeleteConfigurationTemplateResponse(id, connection);
        Response response = null;
        if (deleteConfigurationTemplateResponse != null) {
            deleteConfigurationTemplateResponse.Version = Config.version;
            if (deleteConfigurationTemplateResponse.Success)
                response = Response.ok(deleteConfigurationTemplateResponse).build();
            else response = Response.serverError().entity(deleteConfigurationTemplateResponse.Message + " " + deleteConfigurationTemplateResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteConfigurationTemplate");
        return response;
    }
    @DELETE
    @Path("/templatetype/{templatetype}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteConfigurationTemplateByTemplateType(@PathParam("templatetype") Integer templateType) {
        Logger logger = Logger.getLogger(EndPointTemplateConfiguration.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteConfigurationTemplateByTemplateType");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteConfigurationTemplateResponse deleteConfigurationTemplateResponse = new DeleteConfigurationTemplateResponse(templateType, connection);
        Response response = null;
        if (deleteConfigurationTemplateResponse != null) {
            deleteConfigurationTemplateResponse.Version = Config.version;
            if (deleteConfigurationTemplateResponse.Success)
                response = Response.ok(deleteConfigurationTemplateResponse).build();
            else response = Response.serverError().entity(deleteConfigurationTemplateResponse.Message + " " + deleteConfigurationTemplateResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteConfigurationTemplateByTemplateType");
        return response;
    }
}
