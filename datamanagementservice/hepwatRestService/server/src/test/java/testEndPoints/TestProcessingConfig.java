package testEndPoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.glassfish.jersey.client.ClientConfig;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;
import dk.artogis.hepwat.services.configuration.response.InsertProcessingConfigResponse;
import dk.artogis.hepwat.services.configuration.response.ProcessingConfigResponse;
import dk.artogis.hepwat.services.configuration.response.ProcessingConfigsResponse;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TestProcessingConfig {
    public static void main(String[] args) {
        // getProcessingConfig();
        // getProcessingConfigsAllAggTypes();
        // getProcessingConfigsAllCalcTypes();
        // addNewProcessingConfig();
        // updateProcessingConfig();
        deleteProcessingConfig();
    }


    private static void getProcessingConfig() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer aggType = 0;
        Integer calcType = 1;

        Response response = target.path("rest").
                path("processingconfig/"+ aggType.toString()).queryParam("calctype", calcType).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ProcessingConfigResponse processingConfigResponse = gson.fromJson(output, ProcessingConfigResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getProcessingConfigsAllAggTypes() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer calctype = 1;

        Response response = target.path("rest").
                path("processingconfig/aggtypes/" + calctype.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        ProcessingConfigsResponse processingConfigsResponse = null;
        try {
            processingConfigsResponse = mapper.readValue(output, ProcessingConfigsResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getProcessingConfigsAllCalcTypes() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer aggtype = 1;

        Response response = target.path("rest").
                path("processingconfig/calctypes/" + aggtype.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        ProcessingConfigsResponse processingConfigsResponse = null;
        try {
            processingConfigsResponse = mapper.readValue(output, ProcessingConfigsResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void addNewProcessingConfig()
    {
        ProcessingConfig processingConfig = new ProcessingConfig();
        processingConfig.setAggType(3);
        processingConfig.setCalcType(3);
        processingConfig.setTopic("calc-test-1-output-aggregated");
        processingConfig.setCollection("agg_use");
        processingConfig.setStore(1);

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("processingconfig/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(processingConfig, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        InsertProcessingConfigResponse insertProcessingConfigResponse = null;
        try {
            insertProcessingConfigResponse = mapper.readValue(output, InsertProcessingConfigResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response + " output: " + output);
    }

    private static void updateProcessingConfig()
    {
        ProcessingConfig processingConfig = new ProcessingConfig();
        processingConfig.setAggType(3);
        processingConfig.setCalcType(3);
        processingConfig.setTopic("calc-test-1-output-aggregated");
        processingConfig.setCollection("agg_uses");
        processingConfig.setStore(1);

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("processingconfig/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(processingConfig, MediaType.APPLICATION_JSON));

        String output = response.readEntity(String.class);
        System.out.println(response + " output: " + output);
    }
    private static void deleteProcessingConfig()
    {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        Integer aggType = 3;
        Integer calcType = 3;

        Response response = target.path("rest").
                path("processingconfig/" + aggType.toString()).queryParam("calctype", calcType).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
