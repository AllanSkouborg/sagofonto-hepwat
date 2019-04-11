package dk.artogis.hepwat.services.configuration.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.services.configuration.Configuration;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertProcessingConfigResponse extends Status
{
    public Configuration configuration;

    public InsertProcessingConfigResponse(){}

    public InsertProcessingConfigResponse(ProcessingConfig processingConfig, Connection connection)
    {
        this.Success = false;
        Status status = new Status();

        try {
            connection.connect();
            status = processingConfig.Insert(connection );
            this.Message = status.Message;
            this.Error = status.Error;
            this.Success = status.Success;
            this.JsonObject = "{ \"processingconfigAggtype\": "  + processingConfig.getAggType() + " }";
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            this.Error = status.Error;
            System.out.print("Error in inserting processingconfig");
        }
        finally {
            connection.close();
        }
    }
}
