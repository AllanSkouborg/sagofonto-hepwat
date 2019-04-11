package dk.artogis.hepwat.streamingagg.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.artogis.hepwat.calculation.AggregationCalculationType;
import dk.artogis.hepwat.calculation.AggregationType;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.calculation.response.AggregationCalculationTypesResponse;
import dk.artogis.hepwat.calculation.response.AggregationTypeResponse;
import dk.artogis.hepwat.calculation.response.AggregationTypesResponse;
import dk.artogis.hepwat.calculation.response.CalculationsAndStoresResponse;
import jersey.repackaged.com.google.common.collect.Lists;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;

public class AggregationAndCalculations {

    private ArrayList<AggregationType> aggregationTypes;
    private Integer aggregationType = 0;
    private URI baseUri= null;

    public Integer aggregationCalculationType;
    public Integer aggregationInterval;
    public Boolean isOk;

    public AggregationAndCalculations(URI baseUri, Integer aggregationType)
    {
        this.baseUri = baseUri;
        this.aggregationType = aggregationType;

        try {
            GetAggregationtypes();
        }
        catch (Exception ex)
        {

        }

        if ((this.aggregationTypes != null))
        {
            for (AggregationType aggType : aggregationTypes)
            {
                if (aggType.getId() == aggregationType)
                {
                    aggregationCalculationType = aggType.getAggregationCalculationType();
                    aggregationInterval = aggType.getMinutes();
                    isOk = true;
                }
            }

        }
    }

    private void GetAggregationtypes() {

        Client client = ClientBuilder.newClient();


        AggregationTypesResponse aggregationTypesResponse = null;

        try {
            WebTarget target = client.target(baseUri);

            target = target.path("rest").
                    path("aggregationtype/" );

            Response response = target.
                    request().
                    accept(MediaType.APPLICATION_JSON).get();

            String output = response.readEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();

            aggregationTypesResponse = mapper.readValue(output, AggregationTypesResponse.class);

            //TODO: remove system.out
            System.out.println(response.toString() + " output: " + output);
        }
        catch (Exception ex)
        {

        }
        if ((aggregationTypesResponse != null) && (aggregationTypesResponse.aggregationTypes != null)){

            this.aggregationTypes = Lists.newArrayList(aggregationTypesResponse.aggregationTypes);

        }
        else
        {
            this.aggregationTypes  = new ArrayList<AggregationType>();
        }


    }



}
