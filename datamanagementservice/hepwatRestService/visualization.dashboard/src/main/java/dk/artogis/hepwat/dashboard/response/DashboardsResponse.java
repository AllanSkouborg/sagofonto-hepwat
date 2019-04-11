package dk.artogis.hepwat.dashboard.response;

import dk.artogis.hepwat.dashboard.Dashboard;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardsResponse extends Status
{
    public Dashboard[] dashboards;

    DashboardsResponse() {}

    public DashboardsResponse(Connection  connection)
    {
        this.Success = false;
        try {
            connection.connect();
            dashboards = (Dashboard[])Dashboard.GetDashboards(connection ).toArray(new Dashboard[0]);
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            //System.out.println("Error in retrieving support layer data: ");
            //System.out.println(ex.getMessage());
        }
        finally {
            connection.close();
        }
    }

}