package dk.artogis.hepwat.calculation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.calculation.AggregationAndStore;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregationsAndStoresResponse extends Status
{
    public AggregationAndStore[] aggregationAndStores;
    public AggregationsAndStoresResponse()
    {
    }
    public AggregationsAndStoresResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<AggregationAndStore> aggregationAndStoresList =  AggregationAndStore.GetAggretationAndStores(connection );
            if (aggregationAndStoresList != null ) {
                aggregationAndStores = new AggregationAndStore[aggregationAndStoresList.size()];
                aggregationAndStores = (AggregationAndStore[]) aggregationAndStoresList.toArray(aggregationAndStores);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.calculation, aggregations and store");
        }
        finally {
            connection.close();
        }
    }
    public AggregationsAndStoresResponse(Integer templateId, Integer calculation, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<AggregationAndStore> aggregationAndStoresList =  AggregationAndStore.GetAggregationAndStoresBytTemplateId(templateId, calculation, connection );
            if (aggregationAndStoresList != null ) {
                aggregationAndStores = new AggregationAndStore[aggregationAndStoresList.size()];
                aggregationAndStores = (AggregationAndStore[]) aggregationAndStoresList.toArray(aggregationAndStores);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.calculation, aggregations and stores");
        }
        finally {
            connection.close();
        }

    }
}
