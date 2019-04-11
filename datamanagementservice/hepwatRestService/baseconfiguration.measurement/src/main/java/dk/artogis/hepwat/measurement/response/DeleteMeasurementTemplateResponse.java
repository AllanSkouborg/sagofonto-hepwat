package dk.artogis.hepwat.measurement.response;

import dk.artogis.hepwat.measurement.MeasurementTemplate;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteMeasurementTemplateResponse extends Status
{

    public DeleteMeasurementTemplateResponse()
    {}
    public DeleteMeasurementTemplateResponse(UUID id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = MeasurementTemplate.Delete(id, connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "MeasurementTemplate deleted";
                this.JsonObject = "{ \"measurementTemplateId\": " + id + " }";
            }
            else this.Message = "could not delete item with id: " + id.toString();
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in deleting baseconfiguration.MeasurementTemplate");
        }
        finally {
            connection.close();
        }
    }

}
