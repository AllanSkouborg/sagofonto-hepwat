package dk.artogis.hepwat.services.configuration.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessingConfigsResponse extends Status
{
    public ProcessingConfig[] processingConfigs;
    public ProcessingConfigsResponse()
    {
    }
    public ProcessingConfigsResponse(Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<ProcessingConfig> processingConfigList =  ProcessingConfig.GetProcessingConfigs(connection );
            if (processingConfigList != null ) {
                processingConfigs = new ProcessingConfig[processingConfigList.size()];
                processingConfigs = (ProcessingConfig[]) processingConfigList.toArray(processingConfigs);
            }
            this.Success = true;
        }
        catch (Exception ex)
        {
            //TODO: Logging
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving service processing configs");
        }
        finally {
            connection.close();
        }
    }
    public ProcessingConfigsResponse( Integer aggType,  Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<ProcessingConfig> processingConfigList = ProcessingConfig.GetProcessingConfigAllCalcTypes(aggType,  connection );
            if (processingConfigList != null ) {
                processingConfigs = new ProcessingConfig[processingConfigList.size()];
                processingConfigs = (ProcessingConfig[]) processingConfigList.toArray(processingConfigs);
            }
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
    public ProcessingConfigsResponse( Integer calctype, boolean allAggTypes, Connection connection)
    {
        this.Success = false;

        try {
            connection.connect();
            List<ProcessingConfig> processingConfigList = ProcessingConfig.GetProcessingConfigAllAggTypes(calctype,  connection );
            if (processingConfigList != null ) {
                processingConfigs = new ProcessingConfig[processingConfigList.size()];
                processingConfigs = (ProcessingConfig[]) processingConfigList.toArray(processingConfigs);
            }
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
