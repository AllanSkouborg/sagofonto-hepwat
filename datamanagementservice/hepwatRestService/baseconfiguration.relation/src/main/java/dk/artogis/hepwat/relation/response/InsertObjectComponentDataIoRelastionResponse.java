package dk.artogis.hepwat.relation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertObjectComponentDataIoRelastionResponse extends Status
{
    InsertObjectComponentDataIoRelastionResponse(){}

    public InsertObjectComponentDataIoRelastionResponse(ObjectComponentDataIoRelation objectComponentDataIoRelation, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = objectComponentDataIoRelation.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"objectComponentDataIoRelationId\": "  + objectComponentDataIoRelation.getId() + " , " + "\"objectComponentDataIoRelationRelationId\": "  + objectComponentDataIoRelation.getRelationId() +  " }";
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in inserting baseconfiguration.objectcomponentdataiorelation");
        }
        finally {
            connection.close();
        }

    }
}
