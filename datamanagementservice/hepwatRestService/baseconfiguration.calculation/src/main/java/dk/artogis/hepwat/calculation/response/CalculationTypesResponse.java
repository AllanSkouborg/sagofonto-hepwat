package dk.artogis.hepwat.calculation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.calculation.AggregationType;
import dk.artogis.hepwat.calculation.CalculationType;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalculationTypesResponse extends Status
{
    public CalculationType[] calculationTypes;
    public CalculationTypesResponse()
    {
    }
    public CalculationTypesResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<CalculationType> calculationTypeList =  CalculationType.GetCalculationTypes(connection );
            if (calculationTypeList != null ) {
                calculationTypes = new CalculationType[calculationTypeList.size()];
                calculationTypes = (CalculationType[]) calculationTypeList.toArray(calculationTypes);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving aggregation types");
        }
        finally {
            connection.close();
        }
    }

}
