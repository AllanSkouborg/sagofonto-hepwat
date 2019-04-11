package testEndPoints;

import dk.artogis.hepwat.calculation.AggregationType;
import dk.artogis.hepwat.calculation.response.*;
import dk.artogis.hepwat.calculation.response.InsertAggregationTypeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dk.artogis.hepwat.calculation.response.AggregationTypeResponse;
import dk.artogis.hepwat.calculation.response.AggregationTypesResponse;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TestAggregationType {
    public static void main(String[] args) {
        addNewAggregationType();
        getAggregationTypeFromId();
        updateAggregationType();
        getAggregationTypes();
        deleteAggregationType();
    }


    private static void getAggregationTypeFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer id = 1;

        Response response = target.path("rest").
                path("aggregationtype/"+ id.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();
        AggregationTypeResponse aggregationTypeResponse = gson.fromJson(output, AggregationTypeResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getAggregationTypes() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("aggregationtype/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        AggregationTypesResponse aggregationTypesResponse = null;
        try {
            aggregationTypesResponse = mapper.readValue(output, AggregationTypesResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void addNewAggregationType()
    {
        AggregationType aggregationType = new AggregationType();
       // aggregationType.setId(4);
        aggregationType.setName("Sum");
        aggregationType.setMinutes(8);


        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("aggregationtype/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(aggregationType, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        InsertAggregationTypeResponse insertAggregationTypeResponse = null;
        try {
            insertAggregationTypeResponse = mapper.readValue(output, InsertAggregationTypeResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response + " output: " + output);
    }

    private static void updateAggregationType()
    {
        AggregationType aggregationType = new AggregationType();
        aggregationType.setId(4);
        aggregationType.setName("Average");
        aggregationType.setMinutes(4);

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("aggregationtype/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(aggregationType, MediaType.APPLICATION_JSON));

        String output = response.readEntity(String.class);
        System.out.println(response + " output: " + output);
    }
    private static void deleteAggregationType()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        Integer aggregationtypeId = 3;

        Response response = target.path("rest").
                path("aggregationtype/" + aggregationtypeId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
