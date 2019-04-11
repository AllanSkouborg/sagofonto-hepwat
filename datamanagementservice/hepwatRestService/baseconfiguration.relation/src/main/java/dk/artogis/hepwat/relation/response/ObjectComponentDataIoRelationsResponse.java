package dk.artogis.hepwat.relation.response;

import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectComponentDataIoRelationsResponse extends Status
{
    public ObjectComponentDataIoRelation[] objectComponentDataIoRelations;

    ObjectComponentDataIoRelationsResponse() {}

    public ObjectComponentDataIoRelationsResponse(Connection  connection)
    {
        this.Success = false;
        try {
            connection.connect();
            objectComponentDataIoRelations = (ObjectComponentDataIoRelation[])ObjectComponentDataIoRelation.GetObjectComponentDataIoRelations(connection ).toArray(new ObjectComponentDataIoRelation[0]);
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving objectcomponentdataiorelation");
        }
        finally {
            connection.close();
        }
    }

}
