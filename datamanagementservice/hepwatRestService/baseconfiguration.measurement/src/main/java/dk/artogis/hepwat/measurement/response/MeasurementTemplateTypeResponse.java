package dk.artogis.hepwat.measurement.response;

import dk.artogis.hepwat.measurement.MeasurementTemplateType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementTemplateTypeResponse extends Status implements Serializable
{
    @JsonDeserialize(as= MeasurementTemplateType.class)
    public MeasurementTemplateType measurementTemplateType;

    public MeasurementTemplateTypeResponse()
    {
    }
    public MeasurementTemplateTypeResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            measurementTemplateType = MeasurementTemplateType.GetMeasurementTemplateType(id,connection );
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.measurement template type");

        }
        finally {
            connection.close();
        }
        this.Success = true;
    }
}
