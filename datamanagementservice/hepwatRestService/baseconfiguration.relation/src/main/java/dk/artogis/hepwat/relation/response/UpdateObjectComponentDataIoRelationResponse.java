package dk.artogis.hepwat.relation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateObjectComponentDataIoRelationResponse extends Status
{

    public UpdateObjectComponentDataIoRelationResponse(){}
    public UpdateObjectComponentDataIoRelationResponse(ObjectComponentDataIoRelation objectComponenetDataIoRelation, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = objectComponenetDataIoRelation.Update(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in retrieving baseconfiguration.objectcomponentdataiorelation");
        }
        finally {
            connection.close();
        }
    }
}
