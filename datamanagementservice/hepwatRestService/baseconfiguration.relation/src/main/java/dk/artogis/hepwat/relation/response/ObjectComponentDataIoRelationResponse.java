package dk.artogis.hepwat.relation.response;

import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectComponentDataIoRelationResponse extends Status
{
    public ObjectComponentDataIoRelation objectComponentDataIoRelation;

    public ObjectComponentDataIoRelationResponse(){}

    public ObjectComponentDataIoRelationResponse(UUID id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            objectComponentDataIoRelation = ObjectComponentDataIoRelation.GetObjectComponentDataIoRelation(id, connection);
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.datastore");
        }
        finally {
            connection.close();
        }
    }
    public ObjectComponentDataIoRelationResponse(Integer relationId, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            objectComponentDataIoRelation = ObjectComponentDataIoRelation.GetObjectComponentDataIoRelation(relationId, connection);
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.datastore");
        }
        finally {
            connection.close();
        }
    }
}
