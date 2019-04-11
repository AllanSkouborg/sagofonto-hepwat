package dk.artogis.hepwat.calculation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.calculation.AggregationAndStore;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregationAndStoreResponse extends Status implements Serializable
{
    @JsonDeserialize(as=AggregationAndStore.class)
    public AggregationAndStore aggregationAndStore;

    public AggregationAndStoreResponse()
    {
    }
    public AggregationAndStoreResponse(Integer calculation, Integer dataIoId, Integer templatetype, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            aggregationAndStore = AggregationAndStore.GetAggregationAndStore(calculation, dataIoId, templatetype, connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.calculation, aggregation and store");
        }
        finally {
            connection.close();
        }
    }

}
