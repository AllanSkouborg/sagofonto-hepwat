package dk.artogis.hepwat.measurement.response;

import dk.artogis.hepwat.measurement.MeasurementType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementTypesResponse extends Status
{
    public MeasurementType[] measurementTypes;
    public MeasurementTypesResponse()
    {
    }
    public MeasurementTypesResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<MeasurementType> measurementTypeList =  MeasurementType.GetMeasurementTypes(connection );
            if (measurementTypeList != null ) {
                measurementTypes = new MeasurementType[measurementTypeList.size()];
                measurementTypes = (MeasurementType[]) measurementTypeList.toArray(measurementTypes);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.measurement types");
        }
        finally {
            connection.close();
        }
    }

}
