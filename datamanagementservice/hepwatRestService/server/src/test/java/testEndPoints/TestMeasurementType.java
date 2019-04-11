package testEndPoints;

import dk.artogis.hepwat.measurement.MeasurementType;
import dk.artogis.hepwat.measurement.response.InsertMeasurementTemplateResponse;
import dk.artogis.hepwat.measurement.response.MeasurementTemplateTypeResponse;
import dk.artogis.hepwat.measurement.response.MeasurementTemplateTypesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dk.artogis.hepwat.measurement.MeasurementType;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TestMeasurementType {
    public static void main(String[] args) {

//       addNewMeasurementType();
//        getMeasurementTypeFromId();
//        updateMeasurementType();
//        getMeasurementTypes();
        deleteMeasurementType();
    }


    private static void getMeasurementTypeFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer id = 1;

        Response response = target.path("rest").
                path("measurementtype/"+ id.toString()).queryParam("language", "da").
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();
        MeasurementTemplateTypeResponse measurementTemplateTypeResponse = gson.fromJson(output, MeasurementTemplateTypeResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getMeasurementTypes() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("measurementtype/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        MeasurementTemplateTypesResponse measurementTemplateTypesResponse = null;
        try {
            measurementTemplateTypesResponse = mapper.readValue(output, MeasurementTemplateTypesResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void addNewMeasurementType()
    {
        MeasurementType measurementType = new MeasurementType();
        //measurementType.setId(1);
        measurementType.setName("Test Vand måling");
        measurementType.setIsBatteryStatus(false);
        measurementType.setIsSignalStrength(true);
        measurementType.setLanguage("da");


        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("measurementtype").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(measurementType, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        InsertMeasurementTemplateResponse insertMeasurementTemplateResponse = null;
        try {
            insertMeasurementTemplateResponse = mapper.readValue(output, InsertMeasurementTemplateResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response + " output : " + output);
    }

    private static void updateMeasurementType()
    {
        MeasurementType measurementType = new MeasurementType();
        measurementType.setId(29);
        measurementType.setName("Test Test vand måling");
        measurementType.setIsBatteryStatus(false);
        measurementType.setIsSignalStrength(true);
        measurementType.setLanguage("da");

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("measurementtype").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(measurementType, MediaType.APPLICATION_JSON));

        System.out.println(response);
    }
    private static void deleteMeasurementType()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        Integer measurementtypeId = 30;

        Response response = target.path("rest").
                path("measurementtype/" + measurementtypeId.toString()).queryParam("language", "da").
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
