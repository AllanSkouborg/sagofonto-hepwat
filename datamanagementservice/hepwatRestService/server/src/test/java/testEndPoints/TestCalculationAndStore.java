package testEndPoints;

import dk.artogis.hepwat.calculation.AggregationAndStore;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.calculation.response.*;
import dk.artogis.hepwat.calculation.response.InsertCalculationAndStoreResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.calculation.response.CalculationAndStoreResponse;
import dk.artogis.hepwat.calculation.response.CalculationsAndStoresResponse;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TestCalculationAndStore {
    public static void main(String[] args) {
        addNewCalculationAndStore();
 //       getCalculationAndStoreFromId();
       // updateCalculationAndStore();
//       getCalculationAndStores();
      //  deleteCalculationAndStore();

//        for (Integer calcType =  0; calcType< 4 ; calcType ++) {
//            addNewCalculationAndStoreWithMultipleAggregationAndStores(calcType, -1, 4, "°C", "*2", 4);
//        }
    }


    private static void getCalculationAndStoreFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer calculation = 2;
        Integer dataioid = 7855;
        Integer templatetype = 9;
        target = target.path("rest").
                path("calculationandstore/" + dataioid.toString());
        target = target.queryParam("calculation", calculation);
        //target = target.Param("dataioid", dataioid);
        target = target.queryParam("templatetype", templatetype);
        Response response = target.
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();

        try {
            CalculationAndStoreResponse obtr = gson.fromJson(output, CalculationAndStoreResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getCalculationAndStores() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("calculationandstore/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();
        CalculationsAndStoresResponse calculationsAndStoresResponse = null;
        try {
            calculationsAndStoresResponse = mapper.readValue(output, CalculationsAndStoresResponse.class);
        }
        catch (Exception ex)
        {

        }

        System.out.println(response.toString() +  " output: " +  output);
    }
    private static void addNewCalculationAndStore()
    {
        CalculationAndStore calculationAndStore = new CalculationAndStore();
       // calculationAndStore.setStore(true);
        calculationAndStore.setCalculation(1);
        calculationAndStore.setDataIoId(1005);
        calculationAndStore.setFormula("*2");
        calculationAndStore.setTemplateType(99);
        //calculationAndStore.setUnit("kWh");

        AggregationAndStore aggregationAndStore = new AggregationAndStore();
        //aggregationAndStore.setCalculation(1);
        //aggregationAndStore.setDataIoId(1005);
        //aggregationAndStore.setTemplateType(1);
        aggregationAndStore.setAggregationType(0);
        aggregationAndStore.setStore(true);
        aggregationAndStore.setAggregate(true);
        aggregationAndStore.setUnit("kwh");
        aggregationAndStore.setScaleToUnit(2);
        AggregationAndStore[] aggregationAndStores = new AggregationAndStore[1];
        aggregationAndStores[0] = aggregationAndStore;
        calculationAndStore.setAggregationAndStores(aggregationAndStores);

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("calculationandstore/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(calculationAndStore, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        InsertCalculationAndStoreResponse insertCalculationAndStoreResponse = null;
        try {
            insertCalculationAndStoreResponse = mapper.readValue(output, InsertCalculationAndStoreResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response);
    }

    private static void addNewCalculationAndStoreWithMultipleAggregationAndStores(Integer calcType,  Integer dataIo, Integer template, String unittext, String formulaText, Integer aggTypesUsed  )
    {
        Integer calculationType = calcType;
        Integer dataIoId = dataIo;
        Integer templateType = template;
        String unit = unittext;
        String formula = formulaText;


        CalculationAndStore calculationAndStore = new CalculationAndStore();
        //calculationAndStore.setStore(true);
        calculationAndStore.setCalculation(calculationType);
        calculationAndStore.setDataIoId(dataIoId);
        calculationAndStore.setFormula(formula);
        calculationAndStore.setTemplateType(templateType);

        ///calculationAndStore.setUnit(unit);

        AggregationAndStore[] aggregationAndStores = new AggregationAndStore[aggTypesUsed];
        for (Integer aggTypes = 0 ; aggTypes < aggTypesUsed; aggTypes++) {

            AggregationAndStore aggregationAndStore = new AggregationAndStore();
            aggregationAndStore.setCalculation(calculationType);
            aggregationAndStore.setDataIoId(dataIoId);
            aggregationAndStore.setTemplateType(templateType);
            aggregationAndStore.setAggregationType(aggTypes);
            aggregationAndStore.setStore(true);
            aggregationAndStore.setAggregate(true);
            aggregationAndStore.setUnit(unit);
            //aggregationAndStore.
            aggregationAndStores[aggTypes] = aggregationAndStore;
        }

        calculationAndStore.setAggregationAndStores(aggregationAndStores);

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("calculationandstore/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(calculationAndStore, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        InsertCalculationAndStoreResponse insertCalculationAndStoreResponse = null;
        try {
            insertCalculationAndStoreResponse = mapper.readValue(output, InsertCalculationAndStoreResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response);
    }

    private static void updateCalculationAndStore()
    {
        CalculationAndStore calculationAndStore = new CalculationAndStore();

        calculationAndStore.setCalculation(1);
        calculationAndStore.setDataIoId(1000);
        calculationAndStore.setFormula("*2");
        calculationAndStore.setTemplateType(1);


        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("calculationandstore/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(calculationAndStore, MediaType.APPLICATION_JSON));

        System.out.println(response);
    }
    private static void deleteCalculationAndStore()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        Integer calculationAndStoreId = -1;
        Integer calculation = -1;
        Integer templatetype = 1;

        Response response = target.path("rest")
                .queryParam("templatetype", templatetype)
                .queryParam("calculation", calculation)
                .path("calculationandstore/" + calculationAndStoreId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
