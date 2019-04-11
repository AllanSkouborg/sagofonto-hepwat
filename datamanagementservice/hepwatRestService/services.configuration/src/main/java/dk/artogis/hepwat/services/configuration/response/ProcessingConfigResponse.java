package dk.artogis.hepwat.services.configuration.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessingConfigResponse extends Status implements Serializable
{
    @JsonDeserialize(as=ProcessingConfig.class)
    public ProcessingConfig processingConfig;

    public ProcessingConfigResponse()
    {
    }
    public ProcessingConfigResponse( Integer aggType, Integer calcType, Integer dataType, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            processingConfig = ProcessingConfig.GetProcessingConfig(aggType, calcType, dataType, connection );
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving services processing config");
        }
        finally {
            connection.close();
        }
    }

}
