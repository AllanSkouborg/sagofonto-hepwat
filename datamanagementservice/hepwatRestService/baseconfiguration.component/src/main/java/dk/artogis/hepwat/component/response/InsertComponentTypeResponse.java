package dk.artogis.hepwat.component.response;

import dk.artogis.hepwat.component.ComponentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertComponentTypeResponse extends Status
{
    public ComponentType componentType;

    InsertComponentTypeResponse(){}

    public InsertComponentTypeResponse(ComponentType componentType, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = componentType.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"componentTypeId\": "  + componentType.getId() + " }";
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in retrieving baseconfiguration.datastore");
        }
        finally {
            connection.close();
        }
    }
}
