package dk.artogis.hepwat.measurement.response;

import dk.artogis.hepwat.measurement.MeasurementTemplate;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateMeasurementTemplateResponse extends Status
{

    public UpdateMeasurementTemplateResponse() {
    }
    public UpdateMeasurementTemplateResponse(MeasurementTemplate measurementTemplate, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = measurementTemplate.Update(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in updateing baseconfiguration.measurement template");
        }
        finally {
            connection.close();
        }
    }
}
