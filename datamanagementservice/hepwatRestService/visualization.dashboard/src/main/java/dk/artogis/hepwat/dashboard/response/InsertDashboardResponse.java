package dk.artogis.hepwat.dashboard.response;

import dk.artogis.hepwat.dashboard.Dashboard;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertDashboardResponse extends Status
{

    public InsertDashboardResponse(){}

    public InsertDashboardResponse(Dashboard dashboard, Connection connection)
    {
        Logger logger = Logger.getLogger(InsertDashboardResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering InsertDashboardResponse");
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = dashboard.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"dashboardId\": "  + dashboard.getId()  +  " }";
        }
        catch (Exception ex)
        {
            logger.error("error in inserting dashboard : " + ex.getMessage());
            this.Message = ex.getMessage();
            this.Error = status.Error;
            //System.out.println("Error in inserting dashboard");
        }
        finally {
            connection.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving finally InsertDashboardResponse");
        }
    }
}
