package dk.artogis.hepwat.relation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteComponentRelationsResponse extends Status
{

    public DeleteComponentRelationsResponse()
    {}
    public DeleteComponentRelationsResponse(Integer id, Integer componentType, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = ObjectComponentDataIoRelation.DeleteComponentRelation(id, componentType,  connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "ComponeneRelations deleted";
                this.JsonObject = "{ \"componentId\": " + id + " }";
            }
            else this.Message = "could not delete item with id: " + id.toString();
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in deleting baseconfiguration.ObjectComponenetDataIoRelation");
        }
        finally {
            connection.close();
        }
    }

}
