package dk.artogis.hepwat.calculation.response;

import dk.artogis.hepwat.calculation.CalculationAndStore;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateCalculationandStoreResponse extends Status
{

    public UpdateCalculationandStoreResponse() {
    }
    public UpdateCalculationandStoreResponse(CalculationAndStore calculationAndStore, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = calculationAndStore.Update(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updating baseconfiguration.calculation and store");
        }
        finally {
            connection.close();
        }
    }

}
