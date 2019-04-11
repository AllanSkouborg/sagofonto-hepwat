package dk.artogis.hepwat.dashboard.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dashboard.Dashboard;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteDashboardResponse extends Status
{

    public DeleteDashboardResponse()
    {}
    public DeleteDashboardResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = Dashboard.Delete(id, connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "dashboard deleted";
                this.JsonObject = "{ \"dashboardId\": " + id + " }";
            }
            else {
                this.Message = "could not delete item with id: " + id.toString();
            }
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            //System.out.print("Error in deleting baseconfiguration.dashboard");
        }
        finally {
            connection.close();
        }
    }

}