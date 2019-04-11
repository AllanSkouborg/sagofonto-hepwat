package testEndPoints;

import dk.artogis.hepwat.datastore.DataStore;
import dk.artogis.hepwat.datastore.response.DataStoreResponse;
import dk.artogis.hepwat.datastore.response.DataStoresResponse;
import com.google.gson.Gson;
import dk.artogis.hepwat.datastore.response.DataStoresResponse;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.UUID;

public class TestDataStore {
    public static void main(String[] args) {
        addNewDataStoreWithId();
        addNewDataStore();
        getDataStoreFromId();
        updateDataStore();
        getDataStores();
        deleteDataStore();
    }


    private static void getDataStoreFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        UUID dataSoreID = UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718c");

        Response response = target.path("rest").
                path("datastore/" + dataSoreID.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();
        DataStoreResponse obtr = gson.fromJson(output, DataStoreResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getDataStores() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        UUID dataSoreID = UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718c");

        Response response = target.path("rest").
                path("datastore/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        DataStoresResponse obtr = gson.fromJson(output, DataStoresResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void addNewDataStoreWithId()
    {
        DataStore dataStore = new DataStore();
        dataStore.setId(UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718f"));
        dataStore.setName("DanVand");
        dataStore.setDatabase("hepwat");
        dataStore.setServer("hepwatserver");
        dataStore.setPort("5432");
        dataStore.setDatabase("DanVand");
        dataStore.setSchema("public");
        dataStore.setUser("postgres");
        dataStore.setPassword("Slotsgade22");
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("datastore/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(dataStore, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static void addNewDataStore()
    {
        DataStore dataStore = new DataStore();
        //dataStore.setId(UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718c"));
        dataStore.setName("DanVande");
        dataStore.setDatabase("hepwat");
        dataStore.setServer("hepwatserver");
        dataStore.setPort("5432");
        dataStore.setDatabase("DanVand");
        dataStore.setSchema("public");
        dataStore.setUser("postgres");
        dataStore.setPassword("Slotsgade22");
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("datastore/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(dataStore, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static void updateDataStore()
    {

        DataStore dataStore = new DataStore();
        dataStore.setId(UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718c"));
        dataStore.setName("DanVande");
        dataStore.setDatabase("hepwate");
        dataStore.setPort("5431");
        dataStore.setDatabase("DanVande");
        dataStore.setSchema("publice");
        dataStore.setUser("postgrese");
        dataStore.setPassword("Slotsgade22e");
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("datastore/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(dataStore, MediaType.APPLICATION_JSON));

        System.out.println(response);
    }
    private static void deleteDataStore()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        UUID dataStoreId = UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718e");

        Response response = target.path("rest").
                path("datastore/" + dataStoreId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
