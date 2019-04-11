package dk.artogis.hepwat.calculation.response;

import dk.artogis.hepwat.calculation.AggregationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteAggregationTypeResponse extends Status
{

    public DeleteAggregationTypeResponse()
    {}
    public DeleteAggregationTypeResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = AggregationType.Delete(id, connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "AggregationType deleted";
                this.JsonObject = "{ \"aggregationTypeId\": " + id + " }";
            }
            else this.Message = "could not delete item with id: " + id.toString();
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in deleting baseconfiguration.AggregationType");
        }
        finally {
            connection.close();
        }
    }

}
