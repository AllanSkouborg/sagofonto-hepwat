package testEndPoints;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dk.artogis.hepwat.services.configuration.Configuration;
import dk.artogis.hepwat.services.configuration.response.ConfigurationResponse;
import dk.artogis.hepwat.services.configuration.response.ConfigurationsResponse;
import dk.artogis.hepwat.services.configuration.response.InsertConfigurationResponse;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TestServicesConfiguration {
    public static void main(String[] args) {
//        addNewConfigurationWithId();
//        addNewConfiguration();
//        getConfigurationFromId();
//        updateConfiguration();
        getConfigurations();
        deleteConfiguration();
    }


    private static void getConfigurationFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer serviceId = 2;

        Response response = target.path("rest").
                path("servicesconfiguration/"+ serviceId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ConfigurationResponse obtr = gson.fromJson(output, ConfigurationResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getConfigurations() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("servicesconfiguration/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        ConfigurationsResponse configurationsResponse = null;
        try {
            configurationsResponse = mapper.readValue(output, ConfigurationsResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void addNewConfigurationWithId()
    {
        Configuration configuration = new Configuration();
        configuration.setServiceId(1);
        configuration.setName("aggregation_service_1");
        configuration.setType("streaming aggregation average");
        configuration.setDescription("aggregation 2 minutes");

        Integer[] aggTypes = {1};
        configuration.setAggregationTypes(aggTypes);
        configuration.setSeektoEnd(false);
        configuration.setAutoOffSet("earliest");
        configuration.setTopicGroupId("aggregation_service_1_a");
        configuration.setClientId("aggregation_service_1_a");
        Integer[] calculationtypes = {2,3};
        configuration.setCalculationTypes(calculationtypes);
        configuration.setKafkaBroker("192.168.16:9092");
        configuration.setMongoDbDataStoreId(1);
        configuration.setOutputTopic("aggregated_ouput_a");
        configuration.setStateTopic("aggregated_state_a");

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("servicesconfiguration/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(configuration, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        InsertConfigurationResponse insertConfigurationResponse = null;
        try {
            insertConfigurationResponse = mapper.readValue(output, InsertConfigurationResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response +  " output: " +  output);
    }
    private static void addNewConfiguration()
    {
        Configuration configuration = new Configuration();
        //configuration.setServiceId(3);
        configuration.setName("aggregation_service_2");
        configuration.setType("streaming aggregation average");
        configuration.setDescription("aggregation 60 minutes");

        Integer[] aggTypes = {1};
        configuration.setAggregationTypes(aggTypes);
        configuration.setSeektoEnd(false);
        configuration.setAutoOffSet("earliest");
        configuration.setTopicGroupId("aggregation_service_2_a");
        configuration.setClientId("aggregation_service_2_a");
        Integer[] calculationtypes = {0};
        configuration.setCalculationTypes(calculationtypes);
        configuration.setKafkaBroker("192.168.16:9092");
        configuration.setMongoDbDataStoreId(1);
        configuration.setOutputTopic("aggregated_ouput_a");
        configuration.setStateTopic("aggregated_state_a");

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        Invocation.Builder invocationBuilder =  target.path("rest").path("servicesconfiguration/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(configuration, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        InsertConfigurationResponse insertConfigurationResponse = null;
        try {
            insertConfigurationResponse = mapper.readValue(output, InsertConfigurationResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response +  " output: " +  output);
    }

    private static void updateConfiguration()
    {
        Configuration configuration = new Configuration();
        configuration.setServiceId(1);
        configuration.setName("aggregation_service_2");
        configuration.setType("streaming aggregation average");
        configuration.setDescription("aggregation 60 minutes");

        Integer[] aggTypes = {1};
        configuration.setAggregationTypes(aggTypes);
        configuration.setSeektoEnd(false);
        configuration.setAutoOffSet("earliest");
        configuration.setTopicGroupId("aggregation_service_2_a");
        configuration.setClientId("aggregation_service_2_a");
        Integer[] calculationtypes = {0};
        configuration.setCalculationTypes(calculationtypes);
        configuration.setKafkaBroker("192.168.16:9092");
        configuration.setMongoDbDataStoreId(1);
        configuration.setOutputTopic("aggregated_ouput_ab");
        configuration.setStateTopic("aggregated_state_a");


        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("servicesconfiguration/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(configuration, MediaType.APPLICATION_JSON));

        System.out.println(response);
    }
    private static void deleteConfiguration()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        Integer serviceId = 1;

        Response response = target.path("rest").
                path("servicesconfiguration/" + serviceId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
