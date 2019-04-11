package dk.artogis.hepwat.calculation.response;

import dk.artogis.hepwat.calculation.CalculationAndStore;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteCalculationAndStoreResponse extends Status
{

    public DeleteCalculationAndStoreResponse(){}

    public DeleteCalculationAndStoreResponse(Integer dataIoId, Integer templateType, Integer calculation, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = CalculationAndStore.Delete(dataIoId, templateType, calculation, connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"dataIoId\": "  + dataIoId + " , " + "\"templateType\": "  + templateType +  " , " + "\"calculation\": "  + calculation + " }";
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in delete of baseconfiguration.calculation and store");
        }
        finally {
            connection.close();
        }
    }
}
