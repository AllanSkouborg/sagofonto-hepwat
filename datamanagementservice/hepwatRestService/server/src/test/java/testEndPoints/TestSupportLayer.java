//package testEndPoints;
//
//import dk.artogis.hepwat.supportlayer.SupportLayer;
//import dk.artogis.hepwat.component.response.ComponentTypeResponse;
//import dk.artogis.hepwat.component.response.ComponentTypesResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.glassfish.jersey.client.ClientConfig;
//
//import javax.ws.rs.client.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.UriBuilder;
//import java.net.URI;
//import java.util.UUID;
//
//
//public class TestSupportLayer {
//
//    private static void addNewSupportLayer()
//    {
//        SupportLayer supportLayer = new SupportLayer();
//        //obt.setId(UUID.fromString("4a077c1a-0b28-11e8-ba89-0ed5f89f718b"));
//        supportLayer.setName("Motors");
//        supportLayer.setWfs("http://test2");
//        ClientConfig clientConfig = new ClientConfig();
//        Client client = ClientBuilder.newClient(clientConfig);
//        WebTarget target = client.target(getBaseURI());
//
//        Invocation.Builder invocationBuilder =  target.path("rest").path("supportlayer/").request(MediaType.APPLICATION_JSON);
//        Response response = invocationBuilder.post(Entity.entity(supportLayer, MediaType.APPLICATION_JSON));
//        String output = response.readEntity(String.class);
//        System.out.println(response +  " output: " +  output);
//    }
//
//    private static URI getBaseURI() {
//        return UriBuilder.fromUri("http://localhost:8080/").build();
//    }
//
//}
