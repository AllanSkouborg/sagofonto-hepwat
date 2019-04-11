package dk.artogis.hepwat.calculation.response;

import dk.artogis.hepwat.calculation.AggregationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregationTypeResponse extends Status implements Serializable
{
    @JsonDeserialize(as=AggregationTypeResponse.class)
    public AggregationType aggregationType;

    public AggregationTypeResponse()
    {
    }
    public AggregationTypeResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            aggregationType = AggregationType.GetAggregationType(id,connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving aggregation type");
        }
        finally {
            connection.close();
        }
    }
}
