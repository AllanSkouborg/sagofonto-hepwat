package dk.artogis.hepwat.object.response;

import dk.artogis.hepwat.object.ObjectType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateObjectTypeResponse extends Status
{
    public ObjectType objectType;
    public UpdateObjectTypeResponse() {
    }
    public UpdateObjectTypeResponse(ObjectType objectType,Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = objectType.Update(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updateing objecttype");
        }
        finally {
            connection.close();
        }
    }
}
