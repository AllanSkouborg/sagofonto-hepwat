package dk.artogis.hepwat.streamingStatusService.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.calculation.AggregationAndStore;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.dataconfig.Configuration;
import dk.artogis.hepwat.dataconfig.response.ConfigurationsResponse;
import jersey.repackaged.com.google.common.collect.Lists;
import org.apache.log4j.Logger;

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

    public DeviceConfigurations(URI baseUri, Integer aggType, Logger logger)
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
            //System.out.println(response.toString() + " output: " + output);
            if (logger.isTraceEnabled())
                logger.trace(response.toString() + " output: " + output);
        }
        catch (Exception ex)
        {
            logger.error("error in getting configuration");
        }
        if ((configurationsResponse != null) && (configurationsResponse.configurations != null)){

            this.configurations = Lists.newArrayList(configurationsResponse.configurations);

        }

    }


    public boolean HasStatusSetting(Integer hepwatDeviceId, Integer calculationType, Integer aggregationType)
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
                                    if ((aggregationAndStore.getStatusOn() != null) && (aggregationAndStore.getStatusOn() == true))
                                        return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public AggregationAndStore GetStatusSetting(Integer hepwatDeviceId, Integer calculationType, Integer aggregationType)
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
                                    return aggregationAndStore;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
