package dk.artogis.hepwat.measurement.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.measurement.MeasurementType;
import dk.artogis.hepwat.measurement.Unit;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitsResponse extends Status
{
    public Unit[] units;
    public UnitsResponse()
    {
    }
    public UnitsResponse(Connection connection, String language)
    {
        this.Success = false;

        try {
            connection.connect();
            List<Unit> unitList =  Unit.GetUnits(connection, language );
            if (unitList != null ) {
                units = new Unit[unitList.size()];
                units = (Unit[]) unitList.toArray(units);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration units");
        }
        finally {
            connection.close();
        }
    }

}
