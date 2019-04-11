package testEndPoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dk.artogis.hepwat.calculation.AggregationAndStore;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.dataconfig.Configuration;
import dk.artogis.hepwat.dataconfig.ConfigurationTemplate;
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

public class AddTemplateConfigurationsForTest {
    //for test with internal IGSS  and internal robots
//    public static void main(String[] args)
//    {
//        addNewConfiguration(-1, 1, "KWh", "*2",4, 5,"Energy", "Energi måling", 1 );
//        addNewConfiguration(-1, 2, "m³/h", "*2",4, 5,"Flow", "Flow måling", 2 );
//        addNewConfiguration(-1, 3, "%", "*2",4, 5,"Percent", "Procent", 3 );
//        addNewConfiguration(-1, 4, "°C", "*2",4, 5,"Temperature", "Temperatur", 4 );
//        addNewConfiguration(-1, 5, "m/s", "*2",4, 5,"Velosity", "Hastighed", 5 );
//        addNewConfiguration(-1, 6, "m³", "*2",4, 5,"Volume", "Rumfang", 6 );
//        addNewConfiguration(-1, 7, "m", "*2",4, 5,"Distance", "Afstand m", 7 );
//        addNewConfiguration(-1, 8, "mVs", "*2",4, 5,"Pressure", "Tryk mVs", 8 );
//        addNewConfiguration(-1, 9, "MW", "*2",4, 5,"Power", "Effekt", 9 );
//        addNewConfiguration(-1, 10, "cm", "*2",4, 5,"Distance", "Afstand cm", 7 );
//        addNewConfiguration(-1, 11, "Rpm", "*2",4, 5,"Rotation", "Rotation", 10 );
//        addNewConfiguration(-1, 12, "pH", "*2",4, 5,"Acidity", "Surhedsgrad", 11 );
//        addNewConfiguration(-1, 13, "mS", "*2",4, 5,"Conductivity", "Ledningsevne", 12 );
//        addNewConfiguration(-1, 14, "Hz", "*2",4, 5,"Frequency", "Frekvens", 13 );
//        addNewConfiguration(-1, 15, "mBar", "*2",4, 5,"Pressure", "Tryk mBar", 8 );
//        addNewConfiguration(-1, 16, "NTU", "*2",4, 5,"Heat transmission", "Varme transmission", 14 );
//        addNewConfiguration(-1, 17, "mJ/cm2", "*2",4, 5,"Energy per. unit area", "Energi pr. fladeenhed", 15 );
//        addNewConfiguration(-1, 18, "Bar", "*2",4, 5,"Pressure", "Tryk Bar", 8 );
//
//    }
    //use in Assens
    public static void main(String[] args)
    {

        addNewConfiguration(-1,  "kWh", "{{sensor_output_value}} * 1",4, 5,"E", "Energi måling", 1 );
        addNewConfiguration(-1,  "m³/h", "{{sensor_output_value}} * 1",4, 5,"Q", "Flow måling m³/h", 2 );
        addNewConfiguration(-1,  "%", "{{sensor_output_value}} * 1",4, 5,"Percent", "Procent", 3 );
        addNewConfiguration(-1,  "°C", "{{sensor_output_value}} * 1",4, 5,"T", "Temperatur", 4 );
        addNewConfiguration(-1,  "m/s", "{{sensor_output_value}} * 1",4, 5,"v", "Hastighed", 5 );
        addNewConfiguration(-1,  "m³", "{{sensor_output_value}} * 1",4, 5,"V", "Rumfang", 6 );
        addNewConfiguration(-1,  "m", "{{sensor_output_value}} * 1",4, 5,"s", "Afstand m", 7 );
        addNewConfiguration(-1,  "mVs", "{{sensor_output_value}} * 1",4, 5,"p", "Tryk mVs", 8 );
        addNewConfiguration(-1,  "MW", "{{sensor_output_value}} * 1",4, 5,"P", "Effekt", 9 );
        addNewConfiguration(-1,  "cm", "{{sensor_output_value}} * 1",4, 5,"s", "Afstand cm", 7 );
        addNewConfiguration(-1,  "Rpm", "{{sensor_output_value}} * 1",4, 5,"n", "Rotation", 10 );
        addNewConfiguration(-1,  "pH", "{{sensor_output_value}} * 1",4, 5,"Acidity", "Surhedsgrad", 11 );
        addNewConfiguration(-1,  "mS", "{{sensor_output_value}} * 1",4, 5,"σ", "Ledningsevne", 12 );
        addNewConfiguration(-1,  "Hz", "{{sensor_output_value}} * 1",4, 5,"f", "Frekvens", 13 );
        addNewConfiguration(-1,  "mBar", "{{sensor_output_value}} * 1",4, 5,"p", "Tryk mBar", 8 );
        addNewConfiguration(-1,  "NTU", "{{sensor_output_value}} * 1",4, 5,"Heat transmission", "Varme transmission", 14 );
        addNewConfiguration(-1,  "mJ/cm2", "{{sensor_output_value}} * 1",4, 5,"Energy per. unit area", "Energi pr. fladeenhed", 15 );
        addNewConfiguration(-1,  "Bar", "{{sensor_output_value}} * 1",4, 5,"p", "Tryk Bar", 8 );

        addNewConfiguration(-1,  "", "{{sensor_output_value}} * 1",4, 5,"Ingen", "Ingen", 16 );
        addNewConfiguration(-1,  "/ kl:", "{{sensor_output_value}} * 1",4, 5,"t", "Tid kl. ", 17 );
        addNewConfiguration(-1,  ":", "{{sensor_output_value}} * 1",4, 5,"t", "Tid :", 17 );
        addNewConfiguration(-1,  "-", "{{sensor_output_value}} * 1",4, 5,"t", "Tid -", 17 );
        addNewConfiguration(-1,  "timer", "{{sensor_output_value}} * 1",4, 5,"t", "Tid timer", 17 );
        addNewConfiguration(-1,  "min.", "{{sensor_output_value}} * 1",4, 5,"t", "Tid min", 17 );
        addNewConfiguration(-1,  "sek.", "{{sensor_output_value}} * 1",4, 5,"t", "Tid sek", 17 );
        addNewConfiguration(-1,  "ppm", "{{sensor_output_value}} * 1",4, 5,"C", "Koncentration", 18 );
        addNewConfiguration(-1,  "m³/t", "{{sensor_output_value}} * 1",4, 5,"Q", "Flow måling m³/t", 2 );
        addNewConfiguration(-1,  "l/h", "{{sensor_output_value}} * 1",4, 5,"Q", "Flow måling l/h", 2 );

        addNewConfiguration(-1,  "g/l", "{{sensor_output_value}} * 1",4, 5,"ρ", "Masse koncentration g/l", 19 );
        addNewConfiguration(-1,  "mg/l", "{{sensor_output_value}} * 1",4, 5,"ρ", "Masse koncentration mg/l", 19 );
        addNewConfiguration(-1,  "A", "{{sensor_output_value}} * 1",4, 5,"I", "Strøm", 20 );
        addNewConfiguration(-1,  "mV", "{{sensor_output_value}} * 1",4, 5,"U", "Spænding", 21 );
        addNewConfiguration(-1,  "mm", "{{sensor_output_value}} * 1",4, 5,"s", "Afstand mm", 7 );
        addNewConfiguration(-1,  "°", "{{sensor_output_value}} * 1",4, 5,"Wind direction", "Vind retning", 22 );

    }
    private static void getConfigurationFromId() {
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

    private static void getConfigurations() {
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

    private static void addNewConfiguration(Integer dataIo,  String unittext, String formulaText, Integer calculations, Integer aggTypesUsed, String measurementAlias , String measurementNameText, Integer measurement  )
    {

        Integer dataIoId = dataIo;
        String unit = unittext;
        String formula = formulaText;
        String alias =measurementAlias;
        String measurementName = measurementNameText;
        Integer measurementtype=measurement;

        ConfigurationTemplate configurationTemplate = new ConfigurationTemplate();
        //configurationTemplate.setId(UUID.randomUUID());

        configurationTemplate.setMeasurementAlias(alias);
        configurationTemplate.setMeasurementType(measurementtype);
        configurationTemplate.setMeasurementName(measurementName);
        //configurationTemplate.setTemplateType(templateType);

        CalculationAndStore[] calculationAndStores = new CalculationAndStore[calculations];

        for (Integer calculationNumber  = 0 ; calculationNumber < calculations ; calculationNumber++) {
            CalculationAndStore calculationAndStore = new CalculationAndStore();

            calculationAndStore.setFormula(formula);
            calculationAndStore.setCalculation(calculationNumber);
            calculationAndStores[calculationNumber] = calculationAndStore;

            AggregationAndStore[] aggregationAndStores = new AggregationAndStore[aggTypesUsed];
            for (Integer aggTypes = 0 ; aggTypes < aggTypesUsed; aggTypes++) {

                AggregationAndStore aggregationAndStore = new AggregationAndStore();
                aggregationAndStore.setAggregationType(aggTypes);
                aggregationAndStore.setStore(true);
                aggregationAndStore.setAggregate(true);
                aggregationAndStore.setUnit(unit);
                aggregationAndStores[aggTypes] = aggregationAndStore;
            }

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
        InsertConfigurationTemplateResponse insertConfigurationTemplateResponse = null;
        try {
            insertConfigurationTemplateResponse = mapper.readValue(output, InsertConfigurationTemplateResponse.class);
        }
        catch (Exception ex)
        {

        }
        System.out.println(response+  " output: " +  output);
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/").build();
    }

}
