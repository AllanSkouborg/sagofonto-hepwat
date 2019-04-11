package testEndPoints;

import dk.artogis.hepwat.object.KeyDescription;
import dk.artogis.hepwat.object.ObjectType;
import dk.artogis.hepwat.object.response.*;
import dk.artogis.hepwat.object.response.ObjectTypesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dk.artogis.hepwat.object.response.InsertObjectTypeResponse;
import dk.artogis.hepwat.object.response.ObjectTypeResponse;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestObjecttype {
    public static void main(String[] args) {
//        addNewObjectTypeWithId();
        //addNewObjectType();
//        getObjectTypeFromId();
 //       getObjectTypeFromType();
//        updateObjectType();
//        getObjectTypes();
        deleteObjedtType();
    }


    private static void getObjectTypeFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        UUID objecttypeID = UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b");

        Response response = target.path("rest").
                path("objecttype/"+ objecttypeID.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectTypeResponse obtr = gson.fromJson(output, ObjectTypeResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void getObjectTypeFromType() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Integer type = 2;
        Response response = target.path("rest").
                path("objecttype/type/"+ type.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectTypeResponse obtr = gson.fromJson(output, ObjectTypeResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void getObjectTypes() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("objecttype/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        ObjectTypesResponse objtypes = null;
        try {
            objtypes = mapper.readValue(output, ObjectTypesResponse.class);
        }
        catch (Exception ex)
        {

        }
        //ObjectTypesResponse obtr = gson.fromJson(output, ObjectTypesResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void addNewObjectTypeWithId()
    {
        ObjectType obt = new ObjectType();
        obt.setId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718f"));
        obt.setName("Ledninger");
        obt.setWfs(null);
        obt.setDatastoreId(UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718b"));
        obt.setKeyDesciption("[{\"field\": \"id\", \"type\":\"integer\" }]");
        obt.setObjectTableName("LedningerView");

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("objecttype/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(obt, MediaType.APPLICATION_JSON));
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
        System.out.println(response +  " output: " +  output);
    }
    private static void addNewObjectType()
    {
        ObjectType obt = new ObjectType();
       // obt.setId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b"));
        obt.setName("Ledninger");
        obt.setWfs(null);
        obt.setDatastoreId(UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718b"));
        obt.setKeyDesciption("[{\"field\": \"id\", \"type\":\"integer\" }]");
        obt.setObjectTableName("LedningerView");
//        DataStore ds = new DataStore();
//        ds.setId(4);
//        ds.setName("Danvande");
//        ds.setDatabase("Vande");
//        obt.setDataStore(ds);
        obt.setFieldId("dandas:id");
        obt.setFieldName("dandas:name");
        obt.setFieldDescription( "dandas:desc");
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        //forsøg manuel json
//        String json = "{\"id\": 1}";

        //forsøg Gson virker ok, ikke testet med dato pg dateformatter og nesteed
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();//new Gson();
//        String objtJson = gson.toJson(obt, ObjectType.class);
//                String response = target.path("rest").
//                path("objecttype/update").request(MediaType.APPLICATION_JSON)
//                .accept(MediaType.TEXT_PLAIN_TYPE)
//                .post(Entity.json(objtJson), String.class);

        //forsøg entity  og virker ok, ikke testet med dato pg dateformatter og nesteed
//        String response = target.path("rest").
//                path("objecttype/update").request(MediaType.APPLICATION_JSON)
//                .accept(MediaType.TEXT_PLAIN_TYPE)
//                .post(Entity.entity(obt, MediaType.APPLICATION_JSON_TYPE), String.class);

        //forsøg invocation builder, for at få svar tilbage som response
        Invocation.Builder invocationBuilder =  target.path("rest").path("objecttype/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(obt, MediaType.APPLICATION_JSON));
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
        System.out.println(response +  " output: " +  output);
    }

    private static void updateObjectType()
    {
        ObjectType obt = new ObjectType();
        obt.setId(UUID.fromString("ccbb03bb-7100-4ed1-90b7-0305000514ba"));
        obt.setName("Ledningers");
        obt.setWfs(null);
        obt.setFieldId("dandas:ide");
        obt.setFieldName("dandas:namee");
        obt.setFieldDescription( "dandas:desce");
        obt.setDatastoreId(UUID.fromString("36f711aa-0a7c-11e8-ba89-0ed5f89f718b"));
        KeyDescription kd = new KeyDescription();
        kd.field = "id";
        kd.type = "integer";
        kd.value = null;
        List<KeyDescription> kds = new ArrayList<KeyDescription>();
        kds.add(kd);
        obt.setKeyDescriptions(kds.toArray(new KeyDescription[0]));
        obt.setObjectTableName("LedningerView");
        //obt.setKeyDesciption("");
//        DataStore ds = new DataStore();
//        ds.setId(4);
//        ds.setName("Danvandene");
//        ds.setDatabase("Vande");
//        obt.setDataStore(ds);
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("objecttype/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(obt, MediaType.APPLICATION_JSON));

        System.out.println(response);
    }
    private static void deleteObjedtType()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        UUID objectTypeId = UUID.fromString("ccbb03bb-7100-4ed1-90b7-0305000514ba");

        Response response = target.path("rest").
                path("objecttype/" + objectTypeId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
