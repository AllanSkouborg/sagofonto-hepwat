package dk.artogis.hepwat.relation.response;

import dk.artogis.hepwat.object.KeyDescription;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectRelationsResponse extends Status
{
    public ObjectComponentDataIoRelation[] objectComponentDataIoRelations;

    ObjectRelationsResponse() {}

    public ObjectRelationsResponse(String value, String field, String type , Integer objecttype,  Connection  connection)
    {
        this.Success = false;
        try {
            connection.connect();
            KeyDescription keyDescription = new KeyDescription();
            keyDescription.field = field;
            keyDescription.value = value;
            keyDescription.type = type;
            objectComponentDataIoRelations = (ObjectComponentDataIoRelation[])ObjectComponentDataIoRelation.GetObjectRelations(keyDescription, objecttype, connection).toArray(new ObjectComponentDataIoRelation[0]);
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving datastores");
        }
        finally {
            connection.close();
        }
    }
}
