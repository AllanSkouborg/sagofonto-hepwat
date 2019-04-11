package dk.artogis.hepwat.measurement.response;

import dk.artogis.hepwat.measurement.MeasurementTemplate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementTemplateResponse extends Status implements Serializable
{
    @JsonDeserialize(as= MeasurementTemplate.class)
    public MeasurementTemplate measurementTemplate;

    public MeasurementTemplateResponse()
    {
    }
    public MeasurementTemplateResponse(UUID id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            measurementTemplate = MeasurementTemplate.GetMeasurementTemplate(id,connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.measurement template");
        }
        finally {
            connection.close();
        }

    }
    public MeasurementTemplateResponse(Integer id, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            measurementTemplate = MeasurementTemplate.GetMeasurementTemplateById(id,connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.measurement template");
        }
        finally {
            connection.close();
        }
    }
}
