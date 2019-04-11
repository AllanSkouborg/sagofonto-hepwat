package dk.artogis.hepwat.dashboard.response;

import dk.artogis.hepwat.dashboard.Dashboard;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateDashboardResponse extends Status
{
    public UpdateDashboardResponse() {
    }
    public UpdateDashboardResponse(Dashboard dashboard, Connection connection)
    {
        //System.out.println("UpdateDashboardResponse - dashboard: " + dashboard.getId());
        Logger logger = Logger.getLogger(UpdateDashboardResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering UpdateDashboardResponse");
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = dashboard.Update(connection );
            //System.out.println("UpdateDashboardResponse - status: " + status);
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"dashboardId\": "  + dashboard.getId()  +  " }";

        }
        catch (Exception ex)
        {
            logger.error("Error in updating dashboard : " + ex.getMessage());
            this.Message = ex.getMessage();
            this.Error = status.Error;
            //System.out.print("Error in updating dashboard, message: " + ex.getMessage());
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving UpdateDashboardResponse");
        }
    }
}