package testEndPoints;

import com.google.gson.Gson;
import dk.artogis.hepwat.data.response.DataResponse;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TestData {
    public static void main(String[] args) {

        getDataFromId();
        //getAggDataFromId();
    }


    private static void getDataFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        System.out.println("start");
        WebTarget target = client.target(getBaseURI());
        Integer dataId = 8120;
//        String starttime = "13-11-2017 19:18:04";
//        String endtime = "13-11-2017 19:25:15";
        String starttime = "10-09-2018 08:15:00 GMT+0200";
        String endtime = "10-09-2018 10:15:00 GMT+0200";

        Response response = target.path("rest").
                path("data").queryParam("id", dataId).queryParam("starttime", starttime).queryParam("endtime", endtime).queryParam("aggtype",0).queryParam("calctype",0).queryParam("limit", 1000).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        System.out.println(response.toString() +  " output: " +  output);
        Gson gson = new Gson();
        DataResponse obtr = gson.fromJson(output, DataResponse.class);

    }
    private static void getAggDataFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Integer dataId = 1932;
        String starttime = "18-11-2017 14:40:04";
        String endtime = "18-11-2017 15:40:04";
        Response response = target.path("rest").
                path("data").queryParam("id", dataId).queryParam("starttime", starttime).queryParam("endtime", endtime).queryParam("aggtype",1).
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        System.out.println(response.toString() +  " output: " +  output);
        Gson gson = new Gson();
        DataResponse obtr = gson.fromJson(output, DataResponse.class);

    }


private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }
}
