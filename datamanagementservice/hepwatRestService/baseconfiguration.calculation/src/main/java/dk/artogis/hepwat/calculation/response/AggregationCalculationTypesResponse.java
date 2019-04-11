package dk.artogis.hepwat.calculation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.calculation.AggregationCalculationType;
import dk.artogis.hepwat.calculation.AggregationType;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregationCalculationTypesResponse extends Status
{
    public AggregationCalculationType[] aggregationCalculationTypes;
    public AggregationCalculationTypesResponse()
    {
    }
    public AggregationCalculationTypesResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<AggregationCalculationType> aggregationCalculationTypeList =  AggregationCalculationType.GetAggregationCalculationTypes(connection );
            if (aggregationCalculationTypeList != null ) {
                aggregationCalculationTypes = new AggregationCalculationType[aggregationCalculationTypeList.size()];
                aggregationCalculationTypes = (AggregationCalculationType[]) aggregationCalculationTypeList.toArray(aggregationCalculationTypes);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving aggregation calculation types");
        }
        finally {
            connection.close();
        }
    }

}
