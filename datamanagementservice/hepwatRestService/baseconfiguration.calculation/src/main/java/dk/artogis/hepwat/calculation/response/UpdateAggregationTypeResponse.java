package dk.artogis.hepwat.calculation.response;

import dk.artogis.hepwat.calculation.AggregationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateAggregationTypeResponse extends Status
{

    public UpdateAggregationTypeResponse() {
    }
    public UpdateAggregationTypeResponse(AggregationType aggregationType, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = aggregationType.Update(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updateing aggregation type");
        }
        finally {
            connection.close();
        }
    }
}
