package dk.artogis.hepwat.relation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteDataIoRelationsResponse extends Status
{

    public DeleteDataIoRelationsResponse()
    {}
    public DeleteDataIoRelationsResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = ObjectComponentDataIoRelation.DeleteDataIoRelation(id, connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "DataIo Relations deleted";
                this.JsonObject = "{ \"dataIo\": " + id + " }";
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
