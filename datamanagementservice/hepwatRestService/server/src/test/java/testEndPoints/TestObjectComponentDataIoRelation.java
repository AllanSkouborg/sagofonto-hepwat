package testEndPoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.object.KeyDescription;
import dk.artogis.hepwat.relation.response.*;
import org.glassfish.jersey.client.ClientConfig;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;
import server.TimeUpdate;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestObjectComponentDataIoRelation {
    public static void main(String[] args) {
//        addNewObjectComponentDataIoRelationWithId();
//        addNewObjectComponentDataIoRelation();
// addNewObjectComponentRelation();
//        getObjectComponentDataIoRelationFromId();
//        getObjectComponentDataIoRelationFromRelationId();
//        updateObjectComponentDataIoRelation();
//        getObjectComponentDataIoRelations();
//        deleteObjectComponentDataIoRelation();


//        getComponentRelationFromId();
//        getObjectRelationFromId();
//          getDataIoRelationFromId();
  //      updateObjectComponentDataIoRelationEndtime();
//        deleteComponentRelations();
        deleteDataIoRelation();
//        deleteObjectRelations();
    }


    private static void addNewObjectComponentRelation()
    {
        ObjectComponentDataIoRelation objectComponentDataIoRelation = new ObjectComponentDataIoRelation();
        //objectComponentDataIoRelation.setRelationId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b"));
        objectComponentDataIoRelation.setObjectKeyString("[{\"field\": \"id\", \"type\":\"integer\" }]");
        KeyDescription kd = new KeyDescription();
        kd.field = "id";
        kd.type = "integer";
        kd.value = "39";
        List<KeyDescription> kds = new ArrayList<KeyDescription>();
        kds.add(kd);
        objectComponentDataIoRelation.setObjectKeys(kds.toArray(new KeyDescription[0]));
        objectComponentDataIoRelation.setObjectType(1);
        objectComponentDataIoRelation.setComponentKey(11002);
        objectComponentDataIoRelation.setComponentType(2);

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("objectcomponentdatarelation/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(objectComponentDataIoRelation, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        System.out.println(response +  " output: " +  output);
    }



    private static void addNewObjectComponentDataIoRelation()
    {
        ObjectComponentDataIoRelation objectComponentDataIoRelation = new ObjectComponentDataIoRelation();
        //objectComponentDataIoRelation.setRelationId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b"));
        objectComponentDataIoRelation.setObjectKeyString("[{\"field\": \"id\", \"type\":\"integer\" }]");
        KeyDescription kd = new KeyDescription();
        kd.field = "id";
        kd.type = "integer";
        kd.value = null;
        List<KeyDescription> kds = new ArrayList<KeyDescription>();
        kds.add(kd);
        objectComponentDataIoRelation.setObjectKeys(kds.toArray(new KeyDescription[0]));
        objectComponentDataIoRelation.setObjectType(1);
        objectComponentDataIoRelation.setComponentKey(11002);
        objectComponentDataIoRelation.setComponentType(2);
        objectComponentDataIoRelation.setDataIoKey(1113);
        objectComponentDataIoRelation.setDataIoType(3);
        //objectComponentDataIoRelation.setId(26);
        objectComponentDataIoRelation.setRelationType(5);
        objectComponentDataIoRelation.setEndTimeString("01-10-2018 12:55:30");
        objectComponentDataIoRelation.setCreateTimeString("01-01-2018 12:55:15");

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("objectcomponentdatarelation/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(objectComponentDataIoRelation, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        System.out.println(response +  " output: " +  output);
    }
    private static void addNewObjectComponentDataIoRelationWithId()
    {
        ObjectComponentDataIoRelation objectComponenetDataIoRelation = new ObjectComponentDataIoRelation();
        objectComponenetDataIoRelation.setRelationId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718c"));
        objectComponenetDataIoRelation.setObjectKeyString("[{\"field\": \"id\", \"type\":\"integer\" }]");
        KeyDescription kd = new KeyDescription();
        kd.field = "id";
        kd.type = "integer";
        kd.value = null;
        List<KeyDescription> kds = new ArrayList<KeyDescription>();
        kds.add(kd);
        objectComponenetDataIoRelation.setObjectKeys(kds.toArray(new KeyDescription[0]));
        objectComponenetDataIoRelation.setObjectType(1);
        objectComponenetDataIoRelation.setComponentKey(11001);
        objectComponenetDataIoRelation.setComponentType(2);
        objectComponenetDataIoRelation.setDataIoKey(1112);
        objectComponenetDataIoRelation.setDataIoType(3);
        //objectComponenetDataIoRelation.setId(26);
        objectComponenetDataIoRelation.setRelationType(5);
        objectComponenetDataIoRelation.setEndTimeString("01-10-2018 12:55:30");
        objectComponenetDataIoRelation.setCreateTimeString("01-01-2018 12:55:15");

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("objectcomponentdatarelation/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(objectComponenetDataIoRelation, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);

        System.out.println(response +  " output: " +  output);
    }
    private static void getObjectComponentDataIoRelationFromRelationId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        UUID relationID = UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b");

        Response response = target.path("rest").
                path("objectcomponentdatarelation/"+ "relationid/" + relationID.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        ObjectComponentDataIoRelationResponse objectComponentDataIoRelationResponse;
        ObjectMapper mapper = new ObjectMapper();
        try {
            objectComponentDataIoRelationResponse = mapper.readValue(output, ObjectComponentDataIoRelationResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void getObjectComponentDataIoRelationFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer id = 100001;

        Response response = target.path("rest").
                path("objectcomponentdatarelation/"+ id.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        ObjectComponentDataIoRelationResponse objectComponentDataIoRelationResponse;
        ObjectMapper mapper = new ObjectMapper();
        try {
            objectComponentDataIoRelationResponse = mapper.readValue(output, ObjectComponentDataIoRelationResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void getComponentRelationFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer id = 1;
        Integer componenttype = 1;

        Response response = target.path("rest").queryParam("componenttype",componenttype ).
                path("objectcomponentdatarelation/component/"+ id.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        ComponentRelationsResponse componentRelationsResponse;
        ObjectMapper mapper = new ObjectMapper();
        try {
            componentRelationsResponse = mapper.readValue(output, ComponentRelationsResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void getDataIoRelationFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer id = 100616;

        Response response = target.path("rest").
                path("objectcomponentdatarelation/dataio/"+ id.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        DataIoRelationsResponse dataIoRelationsResponse;
        ObjectMapper mapper = new ObjectMapper();
        try {
            dataIoRelationsResponse = mapper.readValue(output, DataIoRelationsResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void getObjectRelationFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        String value = "1138";
        String field = "id";
        String fieldtype = "integer";
        Integer objecttype = 1;

        Response response = target.path("rest").queryParam("field", field).queryParam("fieldtype", fieldtype).queryParam("objecttype",objecttype).
                path("objectcomponentdatarelation/object/"+ value).
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        ObjectRelationsResponse objectRelationsResponse;
        ObjectMapper mapper = new ObjectMapper();
        try {
            objectRelationsResponse = mapper.readValue(output, ObjectRelationsResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void getObjectComponentDataIoRelations() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("objectcomponentdatarelation/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        ObjectComponentDataIoRelationsResponse objectComponentDataIoRelationsResponse = null;
        try {
            objectComponentDataIoRelationsResponse = mapper.readValue(output, ObjectComponentDataIoRelationsResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }


    private static void updateObjectComponentDataIoRelation()
    {
        ObjectComponentDataIoRelation objectComponenetDataIoRelation = new ObjectComponentDataIoRelation();
        objectComponenetDataIoRelation.setRelationId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b"));
        objectComponenetDataIoRelation.setObjectKeyString("[{\"field\": \"id\", \"type\":\"integer\" }]");
        objectComponenetDataIoRelation.setObjectType(85);
        objectComponenetDataIoRelation.setComponentKey(11001);
        objectComponenetDataIoRelation.setComponentType(2);
        objectComponenetDataIoRelation.setDataIoKey(1112);
        objectComponenetDataIoRelation.setDataIoType(3);
        objectComponenetDataIoRelation.setId(1);
        objectComponenetDataIoRelation.setRelationType(5);

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("objectcomponentdatarelation/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(objectComponenetDataIoRelation, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }

    private static void updateObjectComponentDataIoRelationEndtime()
    {

        UUID id = UUID.fromString("86859939-7ae5-43cf-bb08-48a4ad00538b");
        TimeUpdate timeUpdate = new TimeUpdate();
        timeUpdate.time = "23-07-2018 12:02:00";

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest"). path("objectcomponentdatarelation/"+ id.toString()).request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(timeUpdate, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static void deleteObjectComponentDataIoRelation()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        Integer objectComponentDataIoRelationId = 3;

        Response response = target.path("rest").
                path("objectcomponentdatarelation/" + objectComponentDataIoRelationId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static void deleteObjectRelations()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        String value = "33";
        String field = "id";
        String fieldtype = "integer";
        Integer objectType = 3;


        Response response = target.path("rest").queryParam("fieldtype", fieldtype).queryParam("field", field).queryParam("objecttype", objectType).
                path("objectcomponentdatarelation/object/" + value).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static void deleteComponentRelations()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        Integer componentId = 11001;
        Integer componenttype = 4;

        Response response = target.path("rest").queryParam("componenttype", componenttype).
                path("objectcomponentdatarelation/component/" + componentId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static void deleteDataIoRelation()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        Integer dataIoId = 8002;

        Response response = target.path("rest").
                path("objectcomponentdatarelation/dataio/" + dataIoId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
