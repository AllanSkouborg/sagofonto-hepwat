package dk.artogis.hepwat.dashboard.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dashboard.Dashboard;

import java.util.UUID;

import org.apache.log4j.Logger;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardResponse extends Status
{
    public Dashboard dashboard;

    public DashboardResponse(){}

    public DashboardResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            dashboard = Dashboard.GetDashboard(id, connection);
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            //System.out.print("Error in retrieving baseconfiguration.dashboard");
        }
        finally {
            connection.close();
        }
    }
}