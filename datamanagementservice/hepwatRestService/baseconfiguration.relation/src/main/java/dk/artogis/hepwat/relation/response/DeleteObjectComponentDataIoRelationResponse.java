package dk.artogis.hepwat.relation.response;

import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteObjectComponentDataIoRelationResponse extends Status
{

    public DeleteObjectComponentDataIoRelationResponse()
    {}
    public DeleteObjectComponentDataIoRelationResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = ObjectComponentDataIoRelation.Delete(id, connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "ObjectComponentDataIoRelation deleted";
                this.JsonObject = "{ \"objectComponentDataIoRelationId\": " + id + " }";
            }
            else this.Message = "could not delete item with id: " + id.toString();
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in deleting baseconfiguration.ObjectComponentDataIoRelation");
        }
        finally {
            connection.close();
        }
    }

}
