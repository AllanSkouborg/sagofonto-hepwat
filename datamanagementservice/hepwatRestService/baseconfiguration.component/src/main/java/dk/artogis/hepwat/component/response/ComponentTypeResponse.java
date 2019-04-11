package dk.artogis.hepwat.component.response;

import dk.artogis.hepwat.component.ComponentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComponentTypeResponse extends Status
{
    public ComponentType componentType;

    public ComponentTypeResponse(){}

    public ComponentTypeResponse(UUID id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            componentType = ComponentType.GetComponentType(id, connection);
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
    public ComponentTypeResponse(Integer type, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            componentType = ComponentType.GetComponentType(type, connection);
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
