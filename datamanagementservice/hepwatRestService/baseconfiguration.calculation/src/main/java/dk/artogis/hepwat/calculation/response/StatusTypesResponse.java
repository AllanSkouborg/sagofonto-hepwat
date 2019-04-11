package dk.artogis.hepwat.calculation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.calculation.AggregationType;
import dk.artogis.hepwat.calculation.StatusType;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusTypesResponse extends Status
{
    public StatusType[] statusTypes;
    public StatusTypesResponse()
    {
    }
    public StatusTypesResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<StatusType> statusTypeList =  StatusType.GetStatusTypes(connection );
            if (statusTypeList != null ) {
                statusTypes = new StatusType[statusTypeList.size()];
                statusTypes = (StatusType[]) statusTypeList.toArray(statusTypes);
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
