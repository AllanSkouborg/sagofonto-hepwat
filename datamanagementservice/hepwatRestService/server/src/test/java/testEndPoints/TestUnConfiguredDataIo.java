package testEndPoints;

import dk.artogis.hepwat.dataio.UnConfiguredDataIo;
import dk.artogis.hepwat.dataio.response.InsertUnConfiguredDataIoResponse;
import dk.artogis.hepwat.dataio.response.UnConfiguredDataIoResponse;
import dk.artogis.hepwat.dataio.response.UnConfiguredDataIosResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TestUnConfiguredDataIo {
    public static void main(String[] args) {
        addNewUnConfiguredDataIo();
//        getUnConfiguredDataIoFromId();
//        updateUnConfiguredDataIo();
//        getUnConfiguredDataIos();
    }


    private static void getUnConfiguredDataIoFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer id = 2003;

        Response response = target.path("rest").
                path("unconfigureddataio/"+ id.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();
        UnConfiguredDataIoResponse obtr = gson.fromJson(output, UnConfiguredDataIoResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getUnConfiguredDataIos() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("unconfigureddataio/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        UnConfiguredDataIosResponse unConfiguredDataIosResponse = null;
        try {
            unConfiguredDataIosResponse = mapper.readValue(output, UnConfiguredDataIosResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void addNewUnConfiguredDataIo()
    {
        UnConfiguredDataIo unConfiguredDataIo = new UnConfiguredDataIo();
        unConfiguredDataIo.setId(300010);
        unConfiguredDataIo.setName("Test2");
        unConfiguredDataIo.setUnit("meter. pr. sek");
        unConfiguredDataIo.setDataSourceId(2002);
        unConfiguredDataIo.setInterval(200);
        unConfiguredDataIo.setNodeDomain("");
        unConfiguredDataIo.setDescription("test maaler");
        unConfiguredDataIo.setSensorObjectId("Flowmåler3");
        unConfiguredDataIo.setSensorObjectNodeId("flow3");
        unConfiguredDataIo.setReadable(true);
        unConfiguredDataIo.setWriteable(false);
        unConfiguredDataIo.setDataType("Double");
        unConfiguredDataIo.setNodeType("AnalogItem");
        unConfiguredDataIo.setUpdatedString("15-02-2018 11:17:55");

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("unconfigureddataio/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(unConfiguredDataIo, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        InsertUnConfiguredDataIoResponse insertUnConfiguredDataIoResponse = null;
        try {
            insertUnConfiguredDataIoResponse = mapper.readValue(output, InsertUnConfiguredDataIoResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response);
    }

    private static void updateUnConfiguredDataIo()
    {
        UnConfiguredDataIo unConfiguredDataIo = new UnConfiguredDataIo();
        unConfiguredDataIo.setId(300001);
        unConfiguredDataIo.setName("Test");
        unConfiguredDataIo.setUnit("meter. pr. sek");
        unConfiguredDataIo.setDataSourceId(2001);
        unConfiguredDataIo.setInterval(200);
        unConfiguredDataIo.setNodeDomain("");
        unConfiguredDataIo.setDescription("test maaleren");
        unConfiguredDataIo.setSensorObjectId("");
        unConfiguredDataIo.setSensorObjectNodeId("");
        unConfiguredDataIo.setReadable(true);
        unConfiguredDataIo.setWriteable(false);
        unConfiguredDataIo.setDataType("Double");
        unConfiguredDataIo.setNodeType("AnalogItem");
        unConfiguredDataIo.setUpdatedString("15-02-2018 11:17");

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("unconfigureddataio/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(unConfiguredDataIo, MediaType.APPLICATION_JSON));

        System.out.println(response);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
