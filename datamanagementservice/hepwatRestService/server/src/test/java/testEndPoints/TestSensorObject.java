package testEndPoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dk.artogis.hepwat.dataio.SensorObject;
import dk.artogis.hepwat.dataio.response.SensorObjectResponse;
import dk.artogis.hepwat.dataio.response.SensorObjectsResponse;
import dk.artogis.hepwat.object.KeyDescription;
import dk.artogis.hepwat.object.ObjectType;
import dk.artogis.hepwat.object.response.InsertObjectTypeResponse;
import dk.artogis.hepwat.object.response.ObjectTypeResponse;
import dk.artogis.hepwat.object.response.ObjectTypesResponse;
import jdk.nashorn.internal.runtime.URIUtils;
import org.apache.tomcat.util.buf.UriUtil;
import org.glassfish.jersey.client.ClientConfig;
import server.ConfiguredState;
import server.TimeUpdate;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestSensorObject {
    public static void main(String[] args) {



       updateSensorObject();
//        getSensorObjects();
        getSensorObjectFromId();
    }


    private static void getSensorObjectFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        String sensorObjectId = "ns=2;s=1:CS.TT.413@Cases";
        Integer datasourceId = 1003;
        String urlencodedSensorObjectId = "";
        try {
            urlencodedSensorObjectId = URLEncoder.encode(sensorObjectId, "UTF-8");
        }
        catch (Exception ex)
        {

        }
        Response response = target.path("rest").
                path("sensorobject/"+ datasourceId.toString()).queryParam("sensorobjectid", urlencodedSensorObjectId).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();
        SensorObjectResponse sensorObjectResponse = gson.fromJson(output, SensorObjectResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getSensorObjects() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("sensorobject/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        SensorObjectsResponse sensorObjectsResponse = null;
        try {
            sensorObjectsResponse = mapper.readValue(output, SensorObjectsResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }


    private static void updateSensorObject()
    {
        String sensorObjectId = "ns=2;s=1:CS.TT.413@Cases";
        String urlencodedSensorObjectId = "";
        try {
            urlencodedSensorObjectId = URLEncoder.encode(sensorObjectId, "UTF-8");
        }
        catch (Exception ex)
        {

        }
        Integer datasourceId = 1003;
        ConfiguredState configuredState = new ConfiguredState();
        configuredState.configured = true;
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("sensorobject/"+ datasourceId.toString()).queryParam("sensorobjectid", urlencodedSensorObjectId).request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(configuredState, MediaType.APPLICATION_JSON));

        System.out.println(response);
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
