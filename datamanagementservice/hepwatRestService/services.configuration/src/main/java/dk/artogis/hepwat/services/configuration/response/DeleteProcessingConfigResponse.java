package dk.artogis.hepwat.services.configuration.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteProcessingConfigResponse extends Status
{

    public DeleteProcessingConfigResponse()
    {}
    public DeleteProcessingConfigResponse(Integer aggType, Integer calcType, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            Status status = ProcessingConfig.Delete(aggType, calcType,  connection);
            if (status.Success) {
                this.Success = true;
                this.Message = "processingconfig deleted";
                this.JsonObject = "{ \"processingconfigAggType\": " + aggType + "  }";
            }
            else this.Message = "could not delete item with aggType: " + aggType.toString();
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in deleting configuration");
        }
        finally {
            connection.close();
        }
    }

}
