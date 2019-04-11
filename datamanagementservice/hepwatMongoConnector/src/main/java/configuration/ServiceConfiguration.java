package configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.data.config.DataStore;
import dk.artogis.hepwat.data.response.DataStoreResponse;
import org.apache.log4j.Logger;
import dk.artogis.hepwat.services.configuration.Configuration;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;
import dk.artogis.hepwat.services.configuration.response.ConfigurationResponse;
import dk.artogis.hepwat.services.configuration.response.ProcessingConfigsResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;


public class ServiceConfiguration {

    URI baseUri ;

    public Configuration getConfiguration() {
        return configuration;
    }

    public ProcessingConfig[] getProcessingConfigs() {
        return processingConfigs;
    }

    public DataStore getMongoDbDataStore() {
        return mongoDbDataStore;
    }

    public boolean isOk() {
        return isOk;
    }

    private Configuration configuration = null;

    private ProcessingConfig[] processingConfigs = null;

    private DataStore mongoDbDataStore = null;

    private boolean isOk = false;

    private  static Logger logger = null;

    public ServiceConfiguration(URI baseUri, Integer serviceId, Logger instantiatedLogger)
    {
        this.baseUri = baseUri;
        Client client = ClientBuilder.newClient();
        logger = instantiatedLogger;
        if(logger.isInfoEnabled())
            logger.info("entered service configuration");

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
            ConfigurationResponse configurationResponse = null;

            configurationResponse = mapper.readValue(output, ConfigurationResponse.class);
            if ((configurationResponse != null) && (configurationResponse.configuration != null)){

                this.configuration = configurationResponse.configuration;
                logger.info("ServiceConfiguration: configuration retrieved");
                getProcessingConfigsFromService();
                getMongoDBDataStore(configuration.getMongoDbDataStoreId());
                if ((this.processingConfigs != null) && (this.mongoDbDataStore != null))
                    isOk = true;
            }
        }
        catch (Exception ex)
        {
            logger.error("Could not get configuraiton: " + ex.getMessage());
        }
    }


    public void getProcessingConfigsFromService() throws Exception
    {
        Client client = ClientBuilder.newClient();
        ObjectMapper mapper = new ObjectMapper();
        ProcessingConfigsResponse processingConfigsResponse = null;

        try {
            WebTarget target = client.target(baseUri);

            target = target.path("rest").
                    path("processingconfig/");

            Response response = target.
                    request().
                    accept(MediaType.APPLICATION_JSON).get();

            String output = response.readEntity(String.class);

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
    public void getMongoDBDataStore(Integer mongoDbId) throws Exception
    {
        Client client = ClientBuilder.newClient();
        ObjectMapper mapper = new ObjectMapper();
        DataStoreResponse dataStoreResponse = null;

        try {

            WebTarget target = client.target(baseUri);

            target = target.path("rest").
                    path("bigdatastore/"+ mongoDbId.toString());

            Response response = target.
                    request().
                    accept(MediaType.APPLICATION_JSON).get();

            String output = response.readEntity(String.class);
            dataStoreResponse = mapper.readValue(output, DataStoreResponse.class);
        }
        catch (Exception ex)
        {
            throw new Exception("could not get bigdata store: " + ex.getMessage());
        }
        if ((dataStoreResponse != null) && (dataStoreResponse.dataStore != null)){

            this.mongoDbDataStore = dataStoreResponse.dataStore;
            if (logger.isInfoEnabled())
            logger.info("ServiceConfiguration: mongoDbDataStore retrieved");
        }

    }

}
