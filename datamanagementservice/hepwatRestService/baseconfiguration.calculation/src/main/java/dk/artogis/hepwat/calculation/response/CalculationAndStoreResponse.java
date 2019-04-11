package dk.artogis.hepwat.calculation.response;

import dk.artogis.hepwat.calculation.CalculationAndStore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalculationAndStoreResponse extends Status implements Serializable
{
    @JsonDeserialize(as=CalculationAndStore.class)
    public CalculationAndStore calculationAndStore;

    public CalculationAndStoreResponse()
    {
    }
    public CalculationAndStoreResponse(Integer calculation, Integer dataIoId, Integer templatetype, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            calculationAndStore = CalculationAndStore.GetCalculationAndStore(calculation, dataIoId, templatetype, connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.calculation and store");
        }
        finally {
            connection.close();
        }
    }

}
