package dk.artogis.hepwat.onlineservice.configuration;

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

    ArrayList<Configuration> configurations;
    Integer aggregationType = 0;

    public DeviceConfigurations(URI baseUri,   Integer aggType)
    {
        this.aggregationType = aggType;
        Client client = ClientBuilder.newClient();

        WebTarget target = client.target(baseUri);
        ConfigurationsResponse configurationsResponse = null;

        try {

            target = target.path("rest").
                    path("dataconfiguration/" );

            Response response = target.
                    request().
                    accept(MediaType.APPLICATION_JSON).get();

            String output = response.readEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();


            configurationsResponse = mapper.readValue(output, ConfigurationsResponse.class);
            //TODO: remove system.out
            System.out.println(response.toString() + " output: " + output);
        }
        catch (Exception ex)
        {

        }
        if ((configurationsResponse != null) && (configurationsResponse.configurations != null)){

            this.configurations = Lists.newArrayList(configurationsResponse.configurations);

        }
        else
        {
            this.configurations = new ArrayList<Configuration>();
        }


    }


    public boolean HasAggregation(Integer hepwatDeviceId, Integer calculationType)
    {
        for (Configuration configuration : configurations)
        {
            if (configuration.getId().equals(hepwatDeviceId))
            {
                CalculationAndStore[] calculationAndStores = configuration.getCalculationAndStores();
                for(CalculationAndStore calculationAndStore : calculationAndStores)
                {
                    if (calculationAndStore.getCalculation() == calculationType)
                    {
                        AggregationAndStore[] aggregationAndStores = calculationAndStore.getAggregationAndStores();
                        for (AggregationAndStore aggregationAndStore : aggregationAndStores)
                        {
                            if (aggregationAndStore.getAggregationType() == aggregationType)
                                return  true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
