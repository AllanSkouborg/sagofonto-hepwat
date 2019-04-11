package server;

import dk.artogis.hepwat.dataio.DataSource;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.dataio.response.DataSourceResponse;
import dk.artogis.hepwat.dataio.response.DataSourcesResponse;
import dk.artogis.hepwat.dataio.response.InsertDataSourceResponse;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

//test
@Path("/datasource")
public class EndPointDataSource extends HttpServlet{

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataSource(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointDataSource.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getDataSource");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DataSourceResponse dataSourceResponse = new DataSourceResponse(id, connection);
        Response response = null;
        if (dataSourceResponse != null) {
            dataSourceResponse.Version = Config.version;
            if (dataSourceResponse.Success)
                response = Response.ok(dataSourceResponse).build();
            else response = Response.serverError().entity(dataSourceResponse.Message + " " + dataSourceResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getDataSource");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataSources() {
        Logger logger = Logger.getLogger(EndPointDataSource.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getDataSources");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DataSourcesResponse dataSourcesResponse = new DataSourcesResponse(connection);
        Response response = null;
        if (dataSourcesResponse!= null) {
            dataSourcesResponse.Version = Config.version;
            if (dataSourcesResponse.Success)
                response = Response.ok(dataSourcesResponse).build();
            else response = Response.serverError().entity(dataSourcesResponse.Message + " " + dataSourcesResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getDataSources");
        return response;
    }
//    @POST
//    @Consumes("application/json")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addDataSource(final DataSource dataSource) {
//        Logger logger = Logger.getLogger(EndPointDataSource.class);
//        if(logger.isInfoEnabled())
//            logger.info("Entering addDataSource");
//        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
//        InsertDataSourceResponse insertDataSourceResponse = new InsertDataSourceResponse(dataSource, connection);
//        Response response = null;
//        if (insertDataSourceResponse != null) {
//            insertDataSourceResponse.Version = Config.version;
//            if (insertDataSourceResponse.Success)
//                response = Response.ok(insertDataSourceResponse).build();
//            else response = Response.serverError().entity(insertDataSourceResponse.Message  + " " + insertDataSourceResponse.Error).build();
//        }
//        else response = Response.serverError().entity("could not retrieve response").build();
//        if(logger.isInfoEnabled())
//            logger.info("Leaving addDataSource");
//        return response;
//    }

}