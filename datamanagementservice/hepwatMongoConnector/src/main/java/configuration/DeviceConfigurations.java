package configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.calculation.AggregationAndStore;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.dataconfig.Configuration;
import dk.artogis.hepwat.dataconfig.response.ConfigurationsResponse;
import jersey.repackaged.com.google.common.collect.Lists;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;

public class DeviceConfigurations {

    public ArrayList<Configuration> configurations;
    Integer aggregationType = 0;

    public DeviceConfigurations(URI baseUri)
    {
        Client client = ClientBuilder.newClient();
        ConfigurationsResponse configurationsResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        try {

            WebTarget target = client.target(baseUri);
            target = target.path("rest").
                    path("dataconfiguration/" );

            Response response = target.
                    request().
                    accept(MediaType.APPLICATION_JSON).get();

            String output = response.readEntity(String.class);

            //TODO: remove system.out
            System.out.println(response.toString() + " output: " + output);
            configurationsResponse = mapper.readValue(output, ConfigurationsResponse.class);
        }
        catch (Exception ex)
        {

        }
        if ((configurationsResponse != null) && (configurationsResponse.configurations != null)){

            this.configurations = Lists.newArrayList(configurationsResponse.configurations);
        }

    }


    public boolean store(Integer hepwatDeviceId, Integer calculationType, Integer aggregationType,Integer dataType)
    {
        if (configurations != null) {
            for (Configuration configuration : configurations) {
                if (configuration.getId().equals(hepwatDeviceId)) {
                    CalculationAndStore[] calculationAndStores = configuration.getCalculationAndStores();
                    for (CalculationAndStore calculationAndStore : calculationAndStores) {
                        if (calculationAndStore.getCalculation() == calculationType) {
                            AggregationAndStore[] aggregationAndStores = calculationAndStore.getAggregationAndStores();
                            for (AggregationAndStore aggregationAndStore : aggregationAndStores) {
                                if (aggregationAndStore.getAggregationType() == aggregationType)
                                    // TODO: bruge denne når REST service er implementeret: if (aggregationAndStore.getDataType() == dataType)
                                        return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
