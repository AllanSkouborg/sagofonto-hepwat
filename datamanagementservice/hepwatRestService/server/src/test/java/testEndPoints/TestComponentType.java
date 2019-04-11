package testEndPoints;

import dk.artogis.hepwat.component.ComponentType;
import dk.artogis.hepwat.component.response.ComponentTypeResponse;
import dk.artogis.hepwat.component.response.ComponentTypesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.UUID;

public class TestComponentType {
    public static void main(String[] args) {
//        addNewComponentTypeWithId();
//        addNewComponentType();
//        getComponentTypeFromId();
//        getComponentTypeFromType();
//        updateComponentType();
//        getComponentTypes();
        deleteComponentType();
    }

    private static void addNewComponentTypeWithId()
    {
        ComponentType obt = new ComponentType();
        obt.setId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718f"));
        obt.setName("Energy Meters");
        obt.setWfs(null);
        obt.setDatastoreId(UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718b"));
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("componenttype/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(obt, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        System.out.println(response +  " output: " +  output);
    }
    private static void addNewComponentType()
    {
        ComponentType componentType = new ComponentType();
        //obt.setId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b"));
        componentType.setName("Motors");
        componentType.setWfs("http://test2");
        componentType.setDatastoreId(UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718b"));
        componentType.setFieldDescription("testDesc");
        componentType.setFieldId("testId");
        componentType.setFieldName("testName");
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("componenttype/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(componentType, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        System.out.println(response +  " output: " +  output);
    }

    private static void getComponentTypeFromType() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer componenttype = 1;

        Response response = target.path("rest").
                path("componenttype/type/"+ componenttype.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        ComponentTypeResponse componentTypeResponse;
        ObjectMapper mapper = new ObjectMapper();
        try {
            componentTypeResponse = mapper.readValue(output, ComponentTypeResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void getComponentTypeFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        UUID componenttypeID = UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b");

        Response response = target.path("rest").
                path("componenttype/"+ componenttypeID.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        ComponentTypeResponse componentTypeResponse;
        ObjectMapper mapper = new ObjectMapper();
        try {
            componentTypeResponse = mapper.readValue(output, ComponentTypeResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getComponentTypes() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("componenttype/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        ComponentTypesResponse componentTypesResponse = null;
        try {
            componentTypesResponse = mapper.readValue(output, ComponentTypesResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }


    private static void updateComponentType()
    {
        ComponentType componentType = new ComponentType();
        componentType.setId(UUID.fromString("11ea7576-4236-467a-bc58-b5d5cb2d1787"));
        componentType.setName("EnergiMeters");
        componentType.setWfs("http://test22");
        componentType.setComponentTableName("component_energy_meter");
        componentType.setDatastoreId(UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718b"));
        componentType.setFieldDescription("testDesce");
        componentType.setFieldId("testIde");
        componentType.setFieldName("testNamee");

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("componenttype/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(componentType, MediaType.APPLICATION_JSON));

        System.out.println(response);
    }
    private static void deleteComponentType()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        UUID componentTypeId = UUID.fromString("11ea7576-4236-467a-bc58-b5d5cb2d1787");

        Response response = target.path("rest").
                path("componenttype/" + componentTypeId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
