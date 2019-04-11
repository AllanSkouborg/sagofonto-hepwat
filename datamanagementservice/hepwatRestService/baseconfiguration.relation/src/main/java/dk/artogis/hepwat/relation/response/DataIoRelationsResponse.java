package dk.artogis.hepwat.relation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataIoRelationsResponse extends Status
{
    public ObjectComponentDataIoRelation[] objectComponenetDataIoRelations;

    DataIoRelationsResponse() {}


    public DataIoRelationsResponse(Integer dataIoKey, Connection  connection)
    {
        this.Success = false;
        try {
            connection.connect();
            objectComponenetDataIoRelations = (ObjectComponentDataIoRelation[])ObjectComponentDataIoRelation.GetDataIoRelations(dataIoKey, connection, false ).toArray(new ObjectComponentDataIoRelation[0]);
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
