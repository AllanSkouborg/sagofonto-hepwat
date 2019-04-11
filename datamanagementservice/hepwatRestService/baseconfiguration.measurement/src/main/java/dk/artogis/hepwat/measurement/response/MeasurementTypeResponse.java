package dk.artogis.hepwat.measurement.response;

import dk.artogis.hepwat.measurement.MeasurementType;
import dk.artogis.hepwat.measurement.MeasurementTemplate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementTypeResponse extends Status implements Serializable
{
    @JsonDeserialize(as= MeasurementTemplate.class)
    public MeasurementType measurementType;

    public MeasurementTypeResponse()
    {
    }
    public MeasurementTypeResponse(Integer id, String language, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            measurementType = MeasurementType.GetMeasurementType(id, language, connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration.measurement type");
        }
        finally {
            connection.close();
        }
    }
}
