package dk.artogis.hepwat.calculation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.calculation.AggregationCalculationType;
import dk.artogis.hepwat.calculation.AggregationType;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregationCalculationTypeResponse extends Status implements Serializable
{
    @JsonDeserialize(as=AggregationCalculationTypeResponse.class)
    public AggregationCalculationType aggregationCalculationType;

    public AggregationCalculationTypeResponse()
    {
    }
    public AggregationCalculationTypeResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            aggregationCalculationType = AggregationCalculationType.GetAggregationCalculationType(id,connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving aggregation calculation type");
        }
        finally {
            connection.close();
        }
    }
}
