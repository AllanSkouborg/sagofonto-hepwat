package testEndPoints;

import dk.artogis.hepwat.measurement.MeasurementTemplate;
import dk.artogis.hepwat.measurement.response.InsertMeasurementTemplateResponse;
import dk.artogis.hepwat.measurement.response.MeasurementTemplateResponse;
import dk.artogis.hepwat.measurement.response.MeasurementTemplatesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.UUID;

public class TestMeasurementTemplate {
    public static void main(String[] args) {
        //addNewMeasurementTemplateWithId();
        addNewMeasurementTemplate();
        //getMeasurementTemplateFromId();
        //updateMeasurementTemplate();
        //getMeasurementTemplates();
        //deleteMeasurementTemplate();
    }


    private static void getMeasurementTemplateFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        UUID objecttypeID = UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b");

        Response response = target.path("rest").
                path("measurementtemplate/"+ objecttypeID.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();
        MeasurementTemplateResponse obtr = gson.fromJson(output, MeasurementTemplateResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getMeasurementTemplates() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("measurementtemplate/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        MeasurementTemplatesResponse measurementTemplatesResponse = null;
        try {
            measurementTemplatesResponse = mapper.readValue(output, MeasurementTemplatesResponse.class);
        }
        catch (Exception ex)
        {

        }
        //ObjectTypesResponse obtr = gson.fromJson(output, ObjectTypesResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void addNewMeasurementTemplate()
    {
        MeasurementTemplate measurementTemplate = new MeasurementTemplate();
        //measurementTemplate.setId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b"));
        measurementTemplate.setMeasurementAlias("Alias er nyt");
        measurementTemplate.setMeasurementType(2);
        measurementTemplate.setMeasurementName("Masse måling");
        //measurementTemplate.setTemplateType(2);

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("measurementtemplate/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(measurementTemplate, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        InsertMeasurementTemplateResponse insertMeasurementTemplateResponse = null;
        try {
            insertMeasurementTemplateResponse = mapper.readValue(output, InsertMeasurementTemplateResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response +  " output: " +  output);
    }
    private static void addNewMeasurementTemplateWithId()
    {
        MeasurementTemplate measurementTemplate = new MeasurementTemplate();
        measurementTemplate.setId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718d"));
        measurementTemplate.setMeasurementAlias("Alias");
        measurementTemplate.setMeasurementType(3);
        measurementTemplate.setMeasurementName("Flow måling");
        measurementTemplate.setTemplateType(2);

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("measurementtemplate/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(measurementTemplate, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        InsertMeasurementTemplateResponse insertMeasurementTemplateResponse = null;
        try {
            insertMeasurementTemplateResponse = mapper.readValue(output, InsertMeasurementTemplateResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response +  " output: " +  output);
    }
    private static void updateMeasurementTemplate()
    {
        MeasurementTemplate measurementTemplate = new MeasurementTemplate();
        measurementTemplate.setId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b"));
        measurementTemplate.setMeasurementAlias("AliasFor Måling");
        measurementTemplate.setMeasurementType(3);
        measurementTemplate.setMeasurementName("Energi måling");
        measurementTemplate.setTemplateType(2);

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("measurementtemplate/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(measurementTemplate, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        System.out.println(response +  " output: " +  output);
    }
    private static void deleteMeasurementTemplate()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        UUID measurementTemplateId = UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718c");

        Response response = target.path("rest").
                path("measurementtemplate/" + measurementTemplateId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
