package dk.artogis.hepwat.calculation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.calculation.AggregationAndStore;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateAggregationAndStoreResponse extends Status
{

    public UpdateAggregationAndStoreResponse() {
    }
    public UpdateAggregationAndStoreResponse(AggregationAndStore aggregationAndStore, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = aggregationAndStore.Update(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updating baseconfiguration.calculation, aggregation and store");
        }
        finally {
            connection.close();
        }
    }

}
