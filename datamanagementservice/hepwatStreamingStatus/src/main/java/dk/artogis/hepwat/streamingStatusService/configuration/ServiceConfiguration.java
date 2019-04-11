package dk.artogis.hepwat.streamingStatusService.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.services.configuration.Configuration;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;
import dk.artogis.hepwat.services.configuration.response.ConfigurationResponse;
import dk.artogis.hepwat.services.configuration.response.ProcessingConfigsResponse;
import org.apache.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;


public class ServiceConfiguration {

    public URI baseUri ;

    public String inputTopic;
    public String outputTopic;
    public Integer aggregationTypeId;
    public Integer calcType;
    public Boolean skipFilter = false;

    public Configuration getConfiguration() {
        return configuration;
    }

    public ProcessingConfig[] getProcessingConfigs() {
        return processingConfigs;
    }

    public boolean isOk() {
        return isOk;
    }

    private Configuration configuration = null;

    private ProcessingConfig[] processingConfigs = null;


    private boolean isOk = false;
    private Integer serviceId;
    private  static Logger logger = null;

    public ServiceConfiguration(URI baseUri, Integer serviceId, Logger instantiatedLogger)
    {

        logger = instantiatedLogger;
        if(logger.isInfoEnabled())
            logger.info("entered service configuration instantiation");

        this.baseUri = baseUri;
        this.serviceId = serviceId;
        try
        {
            getServiceConfiguration();
            getProcessingConfigsFromService();
           // getAggregationTypes();
            if ((this.configuration != null)&& (this.processingConfigs != null)) {
                ArrayList<String> topics =  ProcessingConfig.getTopicsForAggTypesAndCalcTypes(configuration.getAggregationTypes(),configuration.getCalculationTypes(), processingConfigs);
                inputTopic = configuration.getInputTopic();
                outputTopic = configuration.getOutputTopic();
                aggregationTypeId = configuration.getServiceAggType();
                skipFilter = configuration.getSkipFilter();
                calcType = configuration.getServiceCalcType();

                isOk = true;
            }
        }
        catch (Exception ex)
        {
            logger.error("Could not get configuration: " + ex.getMessage());
        }
    }

    public void getServiceConfiguration() throws Exception
    {
        Client client = ClientBuilder.newClient();


        ConfigurationResponse configurationResponse = null;
        try {

            WebTarget target = client.target(baseUri);

            target = target.path("rest").
                    path("servicesconfiguration/" + serviceId.toString() );

            Response response = target.
                    request().
                    accept(MediaType.APPLICATION_JSON).get();
            if(logger.isInfoEnabled())
                logger.info("get request executed");
            String output = response.readEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();


            configurationResponse = mapper.readValue(output, ConfigurationResponse.class);
        }
        catch (Exception ex)
        {
            throw new Exception("could not get service config" + ex.getMessage());
        }
        if ((configurationResponse != null) && (configurationResponse.configuration != null)){

            this.configuration = configurationResponse.configuration;
            logger.info("ServiceConfiguration: service config retrieved");
        }
    }

    public boolean isInConfigurationCalculationTypes(Integer actCalcType)
    {
        for (Integer calcType : configuration.getCalculationTypes())
        {
            if (calcType.equals(actCalcType))
                return true;
        }
        return  false;
    }

    public void getProcessingConfigsFromService() throws Exception
    {
        Client client = ClientBuilder.newClient();


        ProcessingConfigsResponse processingConfigsResponse = null;

        try {
            WebTarget target = client.target(baseUri);

            target = target.path("rest").
                    path("processingconfig/");

            Response response = target.
                    request().
                    accept(MediaType.APPLICATION_JSON).get();

            String output = response.readEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();

            processingConfigsResponse = mapper.readValue(output, ProcessingConfigsResponse.class);
        }
        catch (Exception ex)
        {
            throw new Exception("could not get processing config" + ex.getMessage());
        }
        if ((processingConfigsResponse != null) && (processingConfigsResponse.processingConfigs != null)){

            this.processingConfigs = processingConfigsResponse.processingConfigs;
            logger.info("ServiceConfiguration: processing configs retrieved");
        }

    }

}
