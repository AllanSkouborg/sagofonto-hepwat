package dk.artogis.hepwat.measurement.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.measurement.MeasurementTemplate;
import dk.artogis.hepwat.measurement.MeasurementType;
import dk.artogis.hepwat.measurement.Unit;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitResponse extends Status implements Serializable
{
    @JsonDeserialize(as= UnitResponse.class)
    public Unit unit;

    public UnitResponse()
    {
    }
    public UnitResponse(Integer id, String language, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            unit = Unit.GetUnit(id, language, connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving baseconfiguration unit");
        }
        finally {
            connection.close();
        }
    }
}
