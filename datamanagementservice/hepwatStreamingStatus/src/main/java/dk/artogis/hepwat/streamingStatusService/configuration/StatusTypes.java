package dk.artogis.hepwat.streamingStatusService.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.calculation.StatusType;
import dk.artogis.hepwat.calculation.response.StatusTypesResponse;
import jersey.repackaged.com.google.common.collect.Lists;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;

public class StatusTypes {

    public ArrayList<StatusType> statusTypesList;

    private URI baseUri= null;

    public Integer aggregationCalculationType;
    public Integer aggregationInterval;
    public Boolean isOk;

    public StatusTypes(URI baseUri)
    {
        this.baseUri = baseUri;
        GetStatustypes();
    }

    private void GetStatustypes() {

        Client client = ClientBuilder.newClient();


        StatusTypesResponse statusTypesResponse = null;

        try {
            WebTarget target = client.target(baseUri);

            target = target.path("rest").
                    path("statustype/" );

            Response response = target.
                    request().
                    accept(MediaType.APPLICATION_JSON).get();

            String output = response.readEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();

            statusTypesResponse = mapper.readValue(output, StatusTypesResponse.class);

            //TODO: remove system.out
            System.out.println(response.toString() + " output: " + output);
        }
        catch (Exception ex)
        {

        }
        if ((statusTypesResponse != null) && (statusTypesResponse.statusTypes != null)){

            this.statusTypesList = Lists.newArrayList(statusTypesResponse.statusTypes);

        }
        else
        {
            this.statusTypesList = new ArrayList<StatusType>();
        }


    }

    public Double getStatusTypeValue(Integer type)
    {
        for (StatusType statusType : statusTypesList)
        {
            if (statusType.getId().equals(type))
                return  statusType.getValue();
        }
        return null;
    }


}
