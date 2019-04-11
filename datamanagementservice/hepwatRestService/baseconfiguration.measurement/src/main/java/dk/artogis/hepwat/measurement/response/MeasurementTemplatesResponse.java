package dk.artogis.hepwat.measurement.response;

import dk.artogis.hepwat.measurement.MeasurementTemplate;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementTemplatesResponse extends Status
{
    public MeasurementTemplate[] measurementTemplates;
    public MeasurementTemplatesResponse()
    {
    }
    public MeasurementTemplatesResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<MeasurementTemplate> measurementTemplatesList =  MeasurementTemplate.GetMeasurementTemplates(connection );
            if (measurementTemplatesList != null ) {
                measurementTemplates = new MeasurementTemplate[measurementTemplatesList.size()];
                measurementTemplates = (MeasurementTemplate[]) measurementTemplatesList.toArray(measurementTemplates);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving objecttypes");
        }
        finally {
            connection.close();
        }
    }

}
