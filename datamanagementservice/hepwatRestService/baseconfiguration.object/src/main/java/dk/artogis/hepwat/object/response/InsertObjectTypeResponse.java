package dk.artogis.hepwat.object.response;

import dk.artogis.hepwat.object.ObjectType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertObjectTypeResponse extends Status
{
    public ObjectType objectType;

    public InsertObjectTypeResponse(){}

    public InsertObjectTypeResponse(ObjectType objectType,Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = objectType.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"objectTypeId\": "  + objectType.getId() + " }";
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in inserting objecttype");
        }
        finally {
            connection.close();
        }
    }
}
