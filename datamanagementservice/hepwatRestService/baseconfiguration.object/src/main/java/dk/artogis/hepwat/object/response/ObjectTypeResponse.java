package dk.artogis.hepwat.object.response;

import dk.artogis.hepwat.object.ObjectType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectTypeResponse extends Status implements Serializable
{
    @JsonDeserialize(as= ObjectType.class)
    public ObjectType objectType;

    public ObjectTypeResponse()
    {
    }
    public ObjectTypeResponse(UUID id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            objectType = ObjectType.GetObjectType(id,connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving objecttype");

        }
        finally {
            connection.close();
        }
    }
    public ObjectTypeResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            objectType = ObjectType.GetObjectType(id,connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving objecttype");

        }
        finally {
            connection.close();
        }
    }
}
