package dk.artogis.hepwat.component.response;

import dk.artogis.hepwat.component.ComponentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComponentTypesResponse extends Status
{
    public ComponentType[] componentTypes;

    ComponentTypesResponse() {}

    public ComponentTypesResponse(Connection  connection)
    {
        this.Success = false;
        try {
            connection.connect();
            componentTypes = (ComponentType[])ComponentType.GetComponentTypes(connection ).toArray(new ComponentType[0]);
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
