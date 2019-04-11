package testEndPoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dk.artogis.hepwat.calculation.AggregationAndStore;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.dataconfig.Configuration;
import dk.artogis.hepwat.dataconfig.ConfigurationTemplate;
import dk.artogis.hepwat.dataconfig.response.ConfigurationResponse;
import dk.artogis.hepwat.dataconfig.response.ConfigurationTemplateResponse;
import dk.artogis.hepwat.dataconfig.response.ConfigurationsResponse;
import dk.artogis.hepwat.dataconfig.response.InsertConfigurationTemplateResponse;
import dk.artogis.hepwat.object.KeyDescription;
import dk.artogis.hepwat.object.response.InsertObjectTypeResponse;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.UUID;

public class TestTemplateConfiguration {
    public static void main(String[] args) {

        //addNewTemplataConfiguration();
        //getTemplateConfigurationFromId();
        //updateTemplateConfiguration();
        getTemplateConfigurations();
        //deleteTemplateConfiguration();
//        deleteTemplateConfigurationFromTemplateType();
}

    private static void getTemplateConfigurationFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer id = 1;

        Response response = target.path("rest").
                path("templateconfiguration/" + id.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

       String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ConfigurationTemplateResponse obtr = gson.fromJson(output, ConfigurationTemplateResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getTemplateConfigurations() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("templateconfiguration/").
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        ConfigurationsResponse configurationsResponse = null;
        try {
            configurationsResponse = mapper.readValue(output, ConfigurationsResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void addNewTemplataConfiguration()
    {
        Integer dataIoId = 16;

        ConfigurationTemplate configurationTemplate = new ConfigurationTemplate();

        configurationTemplate.setMeasurementType(2);
        configurationTemplate.setMeasurementAlias("fl");
        configurationTemplate.setMeasurementName("Flow");

        CalculationAndStore calculationAndStore1 = new CalculationAndStore();
        calculationAndStore1.setFormula("");
        calculationAndStore1.setCalculation(0);
        CalculationAndStore calculationAndStore2 = new CalculationAndStore();
        calculationAndStore2.setFormula("/100");
        calculationAndStore2.setCalculation(1);
        CalculationAndStore[] calculationAndStores = new CalculationAndStore[2];
        calculationAndStores[0] = calculationAndStore1;
        calculationAndStores[1] = calculationAndStore2;


        for (CalculationAndStore calculationAndStore : calculationAndStores) {
            AggregationAndStore[] aggregationAndStores = new AggregationAndStore[2];
            AggregationAndStore aggregationAndStore1 = new AggregationAndStore();
            if (calculationAndStore.getCalculation() == 1)
                aggregationAndStore1.setUnit("m");
            else
                aggregationAndStore1.setUnit("cm");
            aggregationAndStore1.setAggregate(true);
            aggregationAndStore1.setAggregationType(0);
            aggregationAndStore1.setStore(true);
            aggregationAndStores[0] = aggregationAndStore1;
            AggregationAndStore aggregationAndStore2 = new AggregationAndStore();
            if (calculationAndStore.getCalculation() == 1)
                aggregationAndStore2.setUnit("m");
            else
                aggregationAndStore2.setUnit("cm");
            aggregationAndStore2.setAggregate(true);
            aggregationAndStore2.setAggregationType(1);
            aggregationAndStore2.setStore(true);
            aggregationAndStores[1] = aggregationAndStore2;
            calculationAndStore.setAggregationAndStores(aggregationAndStores);
        }
        configurationTemplate.setCalculationAndStores(calculationAndStores);



        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("templateconfiguration/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(configurationTemplate, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        InsertConfigurationTemplateResponse insertObjtypes = null;
        try {
            insertObjtypes = mapper.readValue(output, InsertConfigurationTemplateResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response+  " output: " +  output);
    }
    private static void updateTemplateConfiguration()
    {
        ConfigurationTemplate configurationTemplate = new ConfigurationTemplate();

        configurationTemplate.setId(UUID.fromString("02132b0e-0b09-4b1a-8a69-dafacdbf3336"));
        configurationTemplate.setMeasurementType(7);
        configurationTemplate.setMeasurementAlias("Dis");
        configurationTemplate.setMeasurementName("Afstand m");

        CalculationAndStore calculationAndStore1 = new CalculationAndStore();
        calculationAndStore1.setFormula("");
        calculationAndStore1.setCalculation(0);
        CalculationAndStore calculationAndStore2 = new CalculationAndStore();
        calculationAndStore2.setFormula("/100");
        calculationAndStore2.setCalculation(1);
        CalculationAndStore[] calculationAndStores = new CalculationAndStore[2];
        calculationAndStores[0] = calculationAndStore1;
        calculationAndStores[1] = calculationAndStore2;


        for (CalculationAndStore calculationAndStore : calculationAndStores) {
            AggregationAndStore[] aggregationAndStores = new AggregationAndStore[2];
            AggregationAndStore aggregationAndStore1 = new AggregationAndStore();
            if (calculationAndStore.getCalculation() == 1)
                aggregationAndStore1.setUnit("m");
            else
                aggregationAndStore1.setUnit("cm");
            aggregationAndStore1.setAggregate(true);
            aggregationAndStore1.setAggregationType(0);
            aggregationAndStore1.setStore(true);
            aggregationAndStores[0] = aggregationAndStore1;
            AggregationAndStore aggregationAndStore2 = new AggregationAndStore();
            if (calculationAndStore.getCalculation() == 1)
                aggregationAndStore2.setUnit("m");
            else
                aggregationAndStore2.setUnit("cm");
            aggregationAndStore2.setAggregate(true);
            aggregationAndStore2.setAggregationType(1);
            aggregationAndStore2.setStore(true);
            aggregationAndStores[1] = aggregationAndStore2;
            calculationAndStore.setAggregationAndStores(aggregationAndStores);
        }
        configurationTemplate.setCalculationAndStores(calculationAndStores);



        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("templateconfiguration/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(configurationTemplate, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }

    private static void deleteTemplateConfiguration()
    {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        UUID templateId = UUID.fromString("7ad8ba5b-76f6-4ca9-9311-b28907f0ac41"); //templatetype 7

        Response response = target.path("rest").
                path("templateconfiguration/" + templateId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static void deleteTemplateConfigurationFromTemplateType()
    {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        Integer templatetype = 13;

        Response response = target.path("rest").
                path("templateconfiguration/" +"templatetype/" + templatetype.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
