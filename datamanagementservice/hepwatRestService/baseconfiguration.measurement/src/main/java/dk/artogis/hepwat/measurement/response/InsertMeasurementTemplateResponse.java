package dk.artogis.hepwat.measurement.response;

import dk.artogis.hepwat.measurement.MeasurementTemplate;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertMeasurementTemplateResponse extends Status
{


    public InsertMeasurementTemplateResponse(){}

    public InsertMeasurementTemplateResponse(MeasurementTemplate measurementTemplate, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = measurementTemplate.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"measurementTemplateId\": "  + measurementTemplate.getId() + " }";
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in insert of measurementtemplate");
        }
        finally {
            connection.close();
        }
    }
}
