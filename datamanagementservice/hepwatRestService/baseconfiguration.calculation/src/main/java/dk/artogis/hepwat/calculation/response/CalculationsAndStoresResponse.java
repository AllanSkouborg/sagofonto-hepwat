package dk.artogis.hepwat.calculation.response;

import dk.artogis.hepwat.calculation.CalculationAndStore;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalculationsAndStoresResponse extends Status
{
    public CalculationAndStore[] calculationAndStores;
    public CalculationsAndStoresResponse()
    {
    }
    public CalculationsAndStoresResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<CalculationAndStore> calculationAndStoresList =  CalculationAndStore.GetCalculationAndStores(connection );
            if (calculationAndStoresList != null ) {
                calculationAndStores = new CalculationAndStore[calculationAndStoresList.size()];
                calculationAndStores = (CalculationAndStore[]) calculationAndStoresList.toArray(calculationAndStores);
            }
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
    public CalculationsAndStoresResponse(Integer templateId, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<CalculationAndStore> calculationAndStoresList =  CalculationAndStore.GetCalculationAndStoresByTemplateId(templateId, connection );
            if (calculationAndStoresList != null ) {
                calculationAndStores = new CalculationAndStore[calculationAndStoresList.size()];
                calculationAndStores = (CalculationAndStore[]) calculationAndStoresList.toArray(calculationAndStores);
            }
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
