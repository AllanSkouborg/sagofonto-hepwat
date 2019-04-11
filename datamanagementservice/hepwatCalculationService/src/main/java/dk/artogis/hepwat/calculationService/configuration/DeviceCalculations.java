package dk.artogis.hepwat.calculationService.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.calculation.response.CalculationsAndStoresResponse;
import jersey.repackaged.com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;

public class DeviceCalculations {

    ArrayList<CalculationAndStore> calculationsAndStores;
    Integer calculationType = 0;

    public DeviceCalculations(URI baseUri, Integer calculationType,Logger logger)
    {
        this.calculationType = calculationType;
        Client client = ClientBuilder.newClient();


        CalculationsAndStoresResponse calculationsAndStoresResponse = null;

        try {
            WebTarget target = client.target(baseUri);

            target = target.path("rest").
                    path("calculationandstore/" );

            Response response = target.
                    request().
                    accept(MediaType.APPLICATION_JSON).get();

            String output = response.readEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();

            calculationsAndStoresResponse = mapper.readValue(output, CalculationsAndStoresResponse.class);
            //System.out.println(response.toString() + " output: " + output);
            if (logger.isTraceEnabled())
                logger.trace(response.toString() + " output: " + output);
        }
        catch (Exception ex)
        {

        }
        if ((calculationsAndStoresResponse != null) && (calculationsAndStoresResponse.calculationAndStores != null)){

            this.calculationsAndStores = Lists.newArrayList(calculationsAndStoresResponse.calculationAndStores);

        }
        else
        {
            calculationsAndStores = new ArrayList<CalculationAndStore>();
        }


    }

    public boolean HasCalc(Integer hepwatDeviceId)
    {
        for(CalculationAndStore item : calculationsAndStores)
        {
            if(item.getDataIoId().equals(hepwatDeviceId))
            {
                if (item.getCalculation().equals(calculationType))
                    if ((item.getFormula() != null) && (!item.getFormula().isEmpty()))
                        return true;
            }
        }
        return false;
    }
    public boolean HasAggregation(Integer hepwatDeviceId, Integer calculationType, Integer aggType)
    {
        for(CalculationAndStore item : calculationsAndStores)
        {
            if(item.getDataIoId().equals(hepwatDeviceId))
            {
                if (item.hasAggType(aggType) && (item.getCalculation() == calculationType))
                    return true;
            }
        }
        return false;
    }
}
