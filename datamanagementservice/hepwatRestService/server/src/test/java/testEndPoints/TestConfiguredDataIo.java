package testEndPoints;

import dk.artogis.hepwat.dataio.ConfiguredDataIo;
import dk.artogis.hepwat.dataio.response.ConfiguredDataIoResponse;
import dk.artogis.hepwat.dataio.response.ConfiguredDataIosResponse;
import dk.artogis.hepwat.object.response.InsertObjectTypeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TestConfiguredDataIo {
    public static void main(String[] args) {
        addNewConfiguredDataIo();
        getConfiguredDataIoFromId();
        updateConfiguredDataIo();
        getConfiguredDataIos();
    }


    private static void getConfiguredDataIoFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer id = 1;

        Response response = target.path("rest").
                path("configureddataio/"+ id.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ConfiguredDataIoResponse obtr = gson.fromJson(output, ConfiguredDataIoResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getConfiguredDataIos() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("configureddataio/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        ConfiguredDataIosResponse configuredDataIosResponse = null;
        try {
            configuredDataIosResponse = mapper.readValue(output, ConfiguredDataIosResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void addNewConfiguredDataIo()
    {
        ConfiguredDataIo configuredDataIo = new ConfiguredDataIo();
        configuredDataIo.setId(3);

        //configuredDataIo.setName("KWH mo");
        //configuredDataIo.setAlias("Energi motor");

        configuredDataIo.setSensorObjectName("KWH mo");
        configuredDataIo.setSensorObjectAlias("Energi motor");

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("configureddataio/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(configuredDataIo, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        InsertObjectTypeResponse insertObjtypes = null;
        try {
            insertObjtypes = mapper.readValue(output, InsertObjectTypeResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response);
    }

    private static void updateConfiguredDataIo()
    {
        ConfiguredDataIo configuredDataIo = new ConfiguredDataIo();
        configuredDataIo.setId(1);

        //configuredDataIo.setName("flow mo");
        //configuredDataIo.setAlias("Flow motorere");

        configuredDataIo.setSensorObjectName("flow mo");
        configuredDataIo.setSensorObjectAlias("Flow motorere");

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("configureddataio/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(configuredDataIo, MediaType.APPLICATION_JSON));

        System.out.println(response);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
