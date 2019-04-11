package dk.artogis.hepwat.calculationService.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.calculation.AggregationAndStore;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.dataconfig.Configuration;
import dk.artogis.hepwat.dataconfig.ConfigurationFormula;
import dk.artogis.hepwat.dataconfig.ConfigurationFormula;
import dk.artogis.hepwat.dataconfig.response.ConfigurationFormulasResponse;
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

public class DeviceConfigurationFormulas {

    public ArrayList<ConfigurationFormula> configurationFormulas;
    Integer calcType = 0;

    public DeviceConfigurationFormulas(URI baseUri, Integer calcType, Logger logger)
    {
        this.calcType = calcType;
        Client client = ClientBuilder.newClient();

        WebTarget target = client.target(baseUri);
        ConfigurationFormulasResponse configurationFormulasResponse = null;

        try {

            target = target.path("rest").
                    path("configurationformula/"+calcType.toString() );

            Response response = target.
                    request().
                    accept(MediaType.APPLICATION_JSON).get();

            String output = response.readEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();


            configurationFormulasResponse = mapper.readValue(output, ConfigurationFormulasResponse.class);
            //System.out.println(response.toString() + " output: " + output);
            if (logger.isTraceEnabled())
                logger.trace(response.toString() + " output: " + output);
        }
        catch (Exception ex)
        {
            logger.error("could not get deviceconfiguration formulas");
        }
        if ((configurationFormulasResponse != null) && (configurationFormulasResponse.configurationFormulas != null)){

            this.configurationFormulas = Lists.newArrayList(configurationFormulasResponse.configurationFormulas);
        }
    }

    public boolean HasCalculation(Integer hepwatDeviceId, Integer calculationType)
    {
        if (configurationFormulas != null) {
            for (ConfigurationFormula configurationFormula : configurationFormulas) {
                if (configurationFormula.getId().equals(hepwatDeviceId)) {
                    String formula = configurationFormula.getFormula();
                    if ((formula != null) && (!formula.isEmpty()))
                        return true;
                }
            }
        }
        return false;
    }
    public String GetFormula(Integer hepwatDeviceId)
    {
        if (configurationFormulas != null) {
            for (ConfigurationFormula configurationFormula : configurationFormulas)
            {
                if (configurationFormula.getId().equals(hepwatDeviceId)) {
                    String formula = configurationFormula.getFormula();
                    return formula;
                }
            }
        }
        return null;
    }
}
