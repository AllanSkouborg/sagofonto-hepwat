package dk.artogis.hepwat.measurement.response;

import dk.artogis.hepwat.measurement.MeasurementTemplateType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementTemplateTypesResponse extends Status
{
    public MeasurementTemplateType[] measurementTemplateTypes;
    public MeasurementTemplateTypesResponse()
    {
    }
    public MeasurementTemplateTypesResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<MeasurementTemplateType> measurementTemplateTypesList =  MeasurementTemplateType.GetMeasurementTemplateTypes(connection );
            if (measurementTemplateTypesList != null ) {
                measurementTemplateTypes = new MeasurementTemplateType[measurementTemplateTypesList.size()];
                measurementTemplateTypes = (MeasurementTemplateType[]) measurementTemplateTypesList.toArray(measurementTemplateTypes);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.measurement template types");
        }
        finally {
            connection.close();
        }
    }

}
