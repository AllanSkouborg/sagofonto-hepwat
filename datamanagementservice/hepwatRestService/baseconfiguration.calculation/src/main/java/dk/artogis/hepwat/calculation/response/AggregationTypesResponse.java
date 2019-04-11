package dk.artogis.hepwat.calculation.response;

import dk.artogis.hepwat.calculation.AggregationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregationTypesResponse extends Status
{
    public AggregationType[] aggregationTypes;
    public AggregationTypesResponse()
    {
    }
    public AggregationTypesResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<AggregationType> aggregationTypeList =  AggregationType.GetAggregationTypes(connection );
            if (aggregationTypeList != null ) {
                aggregationTypes = new AggregationType[aggregationTypeList.size()];
                aggregationTypes = (AggregationType[]) aggregationTypeList.toArray(aggregationTypes);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving aggregation types");
        }
        finally {
            connection.close();
        }
    }

}
