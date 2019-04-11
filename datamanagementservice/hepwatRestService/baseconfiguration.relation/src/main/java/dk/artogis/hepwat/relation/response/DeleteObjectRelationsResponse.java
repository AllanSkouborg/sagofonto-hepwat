package dk.artogis.hepwat.relation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.object.KeyDescription;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteObjectRelationsResponse extends Status
{

    public DeleteObjectRelationsResponse()
    {}
    public DeleteObjectRelationsResponse(String value, String field, String fieldtype , Integer objectType, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            KeyDescription keyDescription = new KeyDescription();
            keyDescription.field = field;
            keyDescription.value = value;
            keyDescription.type = fieldtype;
            Status status = ObjectComponentDataIoRelation.DeleteObjectRelation(keyDescription, objectType, connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "ObjectRelations deleted";
                this.JsonObject = "{ \"ObjectId value\": " + value + " , \"ObjectId type\": " + fieldtype + " , \"ObjectId field\": " + field+  " }";
            }
            else this.Message = "could not delete item with id: " + value.toString();
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
