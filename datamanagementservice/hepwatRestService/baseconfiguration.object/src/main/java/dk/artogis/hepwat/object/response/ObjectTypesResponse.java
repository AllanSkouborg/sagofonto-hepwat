package dk.artogis.hepwat.object.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.object.ObjectType;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectTypesResponse extends Status
{
    public ObjectType[] objectTypes;
    public ObjectTypesResponse()
    {
    }
    public ObjectTypesResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<ObjectType> objectTypesList =  ObjectType.GetObjectTypes(connection );
            if (objectTypesList != null ) {
                objectTypes = new ObjectType[objectTypesList.size()];
                objectTypes = (ObjectType[]) objectTypesList.toArray(objectTypes);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving objecttypes");
        }
        finally {
            connection.close();
        }
    }

}
