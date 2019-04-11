package dk.artogis.hepwat.calculation.response;

import dk.artogis.hepwat.calculation.AggregationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertAggregationTypeResponse extends Status
{

    public InsertAggregationTypeResponse(){}

    public InsertAggregationTypeResponse(AggregationType aggregationType, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = aggregationType.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"aggregationTypeId\": "  + aggregationType.getId() + " }";
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in insert of aggregation type");
        }
        finally {
            connection.close();
        }
    }
}
