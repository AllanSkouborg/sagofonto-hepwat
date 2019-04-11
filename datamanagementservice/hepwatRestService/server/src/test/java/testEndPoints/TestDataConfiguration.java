package testEndPoints;

import dk.artogis.hepwat.calculation.AggregationAndStore;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dk.artogis.hepwat.dataconfig.Configuration;
import dk.artogis.hepwat.dataconfig.response.ConfigurationResponse;
import dk.artogis.hepwat.dataconfig.response.ConfigurationsResponse;
import dk.artogis.hepwat.object.KeyDescription;
import dk.artogis.hepwat.object.response.InsertObjectTypeResponse;
import org.glassfish.jersey.client.ClientConfig;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.UUID;

public class TestDataConfiguration {
    public static void main(String[] args) {

//        addNewConfiguration();
//        getConfigurationFromId();
      updateConfiguration();
 //       getConfigurations();
 //       deleteDataConfiguration();
}


    private static void getConfigurationFromId() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());
        Integer id = 7855;

        Response response = target.path("rest").
//                queryParam("addTypeInfo", true).
//                queryParam("includeRelations", true).
                path("dataconfiguration/" + id.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        Gson gson = new Gson();
        ConfigurationResponse obtr = gson.fromJson(output, ConfigurationResponse.class);
        System.out.println(response.toString() +  " output: " +  output);
    }

    private static void getConfigurations() {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget target = client.target(getBaseURI());

        Response response = target.path("rest").
                path("dataconfiguration/").
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

    private static void addNewConfiguration()
    {
        Integer dataIoId = 908126;

        Configuration configuration = new Configuration();
        configuration.setId(dataIoId);

        //configuration.setName("Level PST");
        //configuration.setAlias("Level");

        configuration.setSensorObjectName("Level PST");
        configuration.setSensorObjectAlias("Level");

        configuration.setMeasurementType(3);
        configuration.setSensorObjectId("0000");
        configuration.setDescription("distance_maxbotix_raw");
        configuration.setTemplateType(3);
        configuration.setDataSourceName("testdatasource");
        configuration.setDataSourceId(100);
        configuration.setSensorObjectDescription("datasourceDescription");
        configuration.setSensorObjectNodeId("sensor_object_node_id");

        CalculationAndStore calculationAndStore1 = new CalculationAndStore();
        calculationAndStore1.setFormula("");
        calculationAndStore1.setCalculation(0);

        CalculationAndStore calculationAndStore2 = new CalculationAndStore();
        calculationAndStore2.setFormula("/100");
        calculationAndStore2.setCalculation(1);
        CalculationAndStore[] calculationAndStores = new CalculationAndStore[2];
        calculationAndStores[0] = calculationAndStore1;
        calculationAndStores[1] = calculationAndStore2;
        configuration.setCalculationAndStores(calculationAndStores);

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
            aggregationAndStore1.setScaleToUnit(1);
            aggregationAndStores[0] = aggregationAndStore1;
            AggregationAndStore aggregationAndStore2 = new AggregationAndStore();
            if (calculationAndStore.getCalculation() == 1)
                aggregationAndStore2.setUnit("m");
            else
                aggregationAndStore2.setUnit("cm");
            aggregationAndStore2.setAggregate(true);
            aggregationAndStore2.setAggregationType(1);
            aggregationAndStore2.setStore(true);
            aggregationAndStore2.setScaleToUnit(2.2);
            aggregationAndStores[1] = aggregationAndStore2;
            calculationAndStore.setAggregationAndStores(aggregationAndStores);
        }


        ObjectComponentDataIoRelation relation = new ObjectComponentDataIoRelation();
//        relation.setComponentKey(1);
//        relation.setComponentType(1);
        KeyDescription keyDescription = new KeyDescription();
        keyDescription.type = "integer";
        keyDescription.field = "id";
        keyDescription.value="38";
        KeyDescription[] keyDescriptions = new KeyDescription[1];
        keyDescriptions[0] = keyDescription;
        relation.setObjectKeys(keyDescriptions);
        relation.setObjectType(1);
        configuration.setRelation(relation);


        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("dataconfiguration/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(configuration, MediaType.APPLICATION_JSON));
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
        System.out.println(response+  " output: " +  output);
    }
    private static void updateConfiguration()
    {
        Integer dataIoId = 908126;
        Configuration configuration = new Configuration();
        configuration.setId(dataIoId);

        //configuration.setName("Level PST");
        //configuration.setAlias("Level22");

        configuration.setSensorObjectName("Level PST");
        configuration.setSensorObjectAlias("Level22");

        configuration.setMeasurementType(3);
        configuration.setSensorObjectId("0000");
        configuration.setDescription("distance_maxbotix_raw");
        configuration.setTemplateType(3);
        configuration.setSensorObjectId("0000");
        configuration.setDescription("distance_maxbotix_raw");
        configuration.setTemplateType(3);
        configuration.setDataSourceName("testdatasource_ny");
        configuration.setDataSourceId(101);
        configuration.setSensorObjectDescription("datasourceDescriptionny");
        configuration.setSensorObjectNodeId("sensor_object_node_id_ny");


        CalculationAndStore calculationAndStore1 = new CalculationAndStore();
        calculationAndStore1.setFormula("*66");
        calculationAndStore1.setCalculation(0);
        CalculationAndStore calculationAndStore2 = new CalculationAndStore();
        calculationAndStore2.setFormula("/1000");
        calculationAndStore2.setCalculation(1);
        CalculationAndStore[] calculationAndStores = new CalculationAndStore[2];
        calculationAndStores[0] = calculationAndStore1;
        calculationAndStores[1] = calculationAndStore2;
        configuration.setCalculationAndStores(calculationAndStores);

        for (CalculationAndStore calculationAndStore : calculationAndStores) {
            AggregationAndStore[] aggregationAndStores = new AggregationAndStore[2];
            AggregationAndStore aggregationAndStore1 = new AggregationAndStore();
            if (calculationAndStore.getCalculation() == 1)
                aggregationAndStore1.setUnit("m");
            else
                aggregationAndStore1.setUnit("cm");
            aggregationAndStore1.setAggregate(true);
            aggregationAndStore1.setAggregationType(0);
            aggregationAndStore1.setStore(false);
            aggregationAndStores[0] = aggregationAndStore1;
            aggregationAndStore1.setScaleToUnit(0.5);
            AggregationAndStore aggregationAndStore2 = new AggregationAndStore();
            if (calculationAndStore.getCalculation() == 1)
                aggregationAndStore2.setUnit("ms");
            else
                aggregationAndStore2.setUnit("cms");
            aggregationAndStore2.setAggregate(false);
            aggregationAndStore2.setAggregationType(1);
            aggregationAndStore2.setStore(true);
            aggregationAndStore2.setScaleToUnit(3.3);
            aggregationAndStores[1] = aggregationAndStore2;
            calculationAndStore.setAggregationAndStores(aggregationAndStores);
        }

        ObjectComponentDataIoRelation relation = new ObjectComponentDataIoRelation();
        KeyDescription keyDescription = new KeyDescription();
        keyDescription.type = "integer";
        keyDescription.field = "id";
        keyDescription.value="38";
        KeyDescription[] keyDescriptions = new KeyDescription[1];
        keyDescriptions[0] = keyDescription;
        relation.setObjectKeys(keyDescriptions);
        relation.setObjectType(1);
        relation.setRelationId( UUID.fromString("f6c54a9d-1055-449b-b821-b3d83d342ac9"));
        relation.setId(70);
        configuration.setRelation(relation);

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());

        Invocation.Builder invocationBuilder =  target.path("rest").path("dataconfiguration/").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(configuration, MediaType.APPLICATION_JSON));
        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }

    private static void deleteDataConfiguration()
    {

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(getBaseURI());
        Integer dataConfigurationId = 8120;

        Response response = target.path("rest").
                path("dataconfiguration/" + dataConfigurationId.toString()).
                request().
                accept(MediaType.APPLICATION_JSON).delete();


        String output = response.readEntity(String.class);
        System.out.println(response+  " output: " +  output);
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
