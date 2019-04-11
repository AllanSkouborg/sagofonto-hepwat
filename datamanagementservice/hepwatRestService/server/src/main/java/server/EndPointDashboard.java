package server;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.dashboard.response.*;
import dk.artogis.hepwat.dashboard.Dashboard;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;



@Path("/dashboard")
public class EndPointDashboard extends HttpServlet {

    /**
     * Method handling HTTP GET requests. The returned baseconfiguration.object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashboard(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointDashboard.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getDashboard");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DashboardResponse dashboardResponse = new DashboardResponse(id, connection);
        Response response = null;
        if (dashboardResponse != null) {
            dashboardResponse.Version = Config.version;
            if (dashboardResponse.Success)
                response = Response.ok(dashboardResponse).build();
            else response = Response.serverError().entity(dashboardResponse.Message + " " +dashboardResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getDashboard");
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashboards() {
        Logger logger = Logger.getLogger(EndPointDashboard.class);
        if(logger.isInfoEnabled())
            logger.info("Entering getDashboards");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DashboardsResponse dashboardsResponse = new DashboardsResponse(connection);
        Response response = null;
        if (dashboardsResponse!= null) {
            dashboardsResponse.Version = Config.version;
            if (dashboardsResponse.Success) {
                //System.out.println("dashboardsResponse: " + dashboardsResponse.Message);
                response = Response.ok(dashboardsResponse).build();
            }
            else {
                response = Response.serverError().entity(dashboardsResponse.Message + " " + dashboardsResponse.Error).build();
            }
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving getDashboards");
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addConfiguration(final Dashboard dashboard) {
        Logger logger = Logger.getLogger(EndPointDashboard.class);
        if(logger.isInfoEnabled())
            logger.info("Entering addDashboard");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        InsertDashboardResponse insertDashboardResponse = new InsertDashboardResponse(dashboard, connection);
        Response response = null;
        if (insertDashboardResponse != null) {
            insertDashboardResponse.Version = Config.version;
            if (insertDashboardResponse.Success)
                response = Response.ok(insertDashboardResponse).build();
            else response = Response.serverError().entity(insertDashboardResponse.Message  + " " + insertDashboardResponse.Error).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving addDashboard");
        return response;
    }

    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDashboard(final Dashboard dashboard) {

        Logger logger = Logger.getLogger(EndPointDashboard.class);
        if(logger.isInfoEnabled()) {
            logger.info("Entering updateDashboard");
        }

        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        UpdateDashboardResponse updateDashboardResponse = new UpdateDashboardResponse(dashboard, connection);
        Response response = null;
        if (updateDashboardResponse != null) {
            updateDashboardResponse.Version = Config.version;
            if (updateDashboardResponse.Success) {
                response = Response.ok(updateDashboardResponse).build();
            }
            else {
                response = Response.serverError().entity(updateDashboardResponse.Message +" " + updateDashboardResponse.Error).build();
            }
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving updateDashboard");
        return response;
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDashboard(@PathParam("id") Integer id) {
        Logger logger = Logger.getLogger(EndPointDashboard.class);
        if(logger.isInfoEnabled())
            logger.info("Entering deleteDashboard");
        PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
        DeleteDashboardResponse deleteDashboardResponse = new DeleteDashboardResponse(id, connection);
        Response response = null;
        if (deleteDashboardResponse != null) {
            deleteDashboardResponse.Version = Config.version;
            if (deleteDashboardResponse.Success)
                response = Response.ok(deleteDashboardResponse).build();
            else response = Response.serverError().entity(deleteDashboardResponse.Message + " " +deleteDashboardResponse).build();
        }
        else response = Response.serverError().entity("could not retrieve response").build();
        if(logger.isInfoEnabled())
            logger.info("Leaving deleteDashboard");
        return response;
    }
}