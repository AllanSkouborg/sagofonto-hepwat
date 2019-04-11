package server;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.dataconfig.Configuration;
import dk.artogis.hepwat.dataconfig.ConfigurationFormula;
import dk.artogis.hepwat.dataconfig.response.ConfigurationFormulaValidateResponse;
import dk.artogis.hepwat.dataconfig.response.ConfigurationFormulasResponse;
import dk.artogis.hepwat.object.ObjectType;
import dk.artogis.hepwat.object.response.InsertObjectTypeResponse;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

@Path("/configurationformula")
public class EndPointConfigurationFormula extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */


    @GET
    @Path("{calculationtype}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFormula(@PathParam("calculationtype") Integer calculationtype, @QueryParam("dataioid") Integer dataioid ) {
        Logger logger = Logger.getLogger(EndPointConfigurationFormula.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getFormula");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ConfigurationFormulasResponse configurationFormulaResponse = new ConfigurationFormulasResponse(connection, calculationtype, dataioid);
        Response response = null;
        if (configurationFormulaResponse!= null) {
            configurationFormulaResponse.Version = Config.version;
            if (configurationFormulaResponse.Success)
                response = Response.ok(configurationFormulaResponse).build();
            else response = Response.serverError().entity(configurationFormulaResponse.Message + " " + configurationFormulaResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getFormula");
        return response;
    }
    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateConfigurationFormula(final ConfigurationFormula configurationFormula, @QueryParam("measurementvalue")String measurementvalue) {
        Logger logger = Logger.getLogger(EndPointConfigurationFormula.class);
        if(logger.isInfoEnabled())
            logger.info("Entering validateConfigurationFormula");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        ConfigurationFormulaValidateResponse configurationFormulaValidateResponse = new ConfigurationFormulaValidateResponse( connection, configurationFormula, measurementvalue);
        Response response = null;
        if (configurationFormulaValidateResponse != null) {
            configurationFormulaValidateResponse.Version = Config.version;
            response = Response.ok(configurationFormulaValidateResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving validateConfigurationFormula");
        return response;
    }
}