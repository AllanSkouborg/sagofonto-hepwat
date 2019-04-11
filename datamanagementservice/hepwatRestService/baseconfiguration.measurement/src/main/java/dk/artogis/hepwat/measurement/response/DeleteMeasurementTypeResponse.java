package dk.artogis.hepwat.measurement.response;


import dk.artogis.hepwat.measurement.MeasurementType;
import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteMeasurementTypeResponse extends Status
{

    public DeleteMeasurementTypeResponse()
    {}
    public DeleteMeasurementTypeResponse(Integer id,String language, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = MeasurementType.Delete(id, language, connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "MeasurementType deleted";
                this.JsonObject = "{ \"measurementTypeId\": " + id + "\"language\": " + language +" }";
            }
            else this.Message = "could not delete item with id: " + id.toString() + "and language : " + language;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in deleting baseconfiguration.MeasurementType");
        }
        finally {
            connection.close();
        }
    }

}
