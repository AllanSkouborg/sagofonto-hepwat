package dk.artogis.hepwat.measurement.response;

import dk.artogis.hepwat.measurement.MeasurementTemplateType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertMeasurementTemplateTypeResponse extends Status
{


    public InsertMeasurementTemplateTypeResponse(){}

    public InsertMeasurementTemplateTypeResponse(MeasurementTemplateType measurementTemplateType, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = measurementTemplateType.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"measurementTemplateTypeId\": "  + measurementTemplateType.getId() + " }";
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in insert of measurementtemplate type");
        }
        finally {
            connection.close();
        }
    }
}
