package dk.artogis.hepwat.relation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;

import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateObjectComponentDataIoRelationEndtimeResponse extends Status
{
    public UpdateObjectComponentDataIoRelationEndtimeResponse() {
    }
    public UpdateObjectComponentDataIoRelationEndtimeResponse(UUID id, String endtime, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = ObjectComponentDataIoRelation.UpdateEndTime(id, endtime, connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updateing objectcomponentdataiorelation endtime");
        }
        finally {
            connection.close();
        }
    }
}
