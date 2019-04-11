package dk.artogis.hepwat.onlineservice.startdata;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.data.*;
import dk.artogis.hepwat.data.DataRecord;
import dk.artogis.hepwat.data.response.DataResponse;

import org.apache.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;


public class ActualDataRecord {

    public URI baseUri ;

    private Integer serviceId;
    private  static Logger logger = null;
    private DataRecord[] dataRecords = null;
    private Integer aggtype=0;
    private Integer calctype = 0;
    private Integer datatype = 0;


    public ActualDataRecord(URI baseUri, Integer id, Integer aggtype, Integer cacltype, Integer datatype)
    {

        logger = Logger.getLogger(ActualDataRecord.class);
        if(logger.isInfoEnabled())
            logger.info("entered ActualRecord instantiation");

        this.aggtype = aggtype;
        this.calctype = cacltype;
        this.datatype = datatype;
        this.baseUri = baseUri;
        this.serviceId = serviceId;
        try
        {
            getLastDataRecord(id, aggtype, cacltype, datatype);
        }
        catch (Exception ex)
        {
            logger.error("Could not get configuration: " + ex.getMessage());
        }
    }

    public void getLastDataRecord(Integer id, Integer aggtype, Integer calctype, Integer datatype) throws Exception
    {
        Client client = ClientBuilder.newClient();


        DataResponse dataResponse = null;
        try {

            WebTarget target = client.target(baseUri);

            target = target.path("rest").
                    path("data/last/" + id.toString() ).queryParam("aggtype", aggtype).queryParam("calctype", calctype).queryParam("datatype", datatype);

            Response response = target.
                    request().
                    accept(MediaType.APPLICATION_JSON).get();
            if(logger.isInfoEnabled())
                logger.info("get request executed");
            String output = response.readEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();
            System.out.print(output);

            dataResponse = mapper.readValue(output, DataResponse.class);
        }
        catch (Exception ex)
        {
            throw new Exception("could not get data" + ex.getMessage());
        }
        if ((dataResponse != null) && (dataResponse.dataRecords != null)){

            this.dataRecords = dataResponse.dataRecords;
            logger.info("ServiceConfiguration: service config retrieved");
        }
    }
    public String getLastRecordText()
    {
        String dataString = null;
        //if more than one record, only use the first (sorted descending by time)
        if (dataRecords.length > 0)
        {
            DataRecord dataRecord = dataRecords[0];
            OnlineDataRecord onlineDataRecord = new OnlineDataRecord(dataRecord,aggtype, calctype, datatype);
            JsonNode jsonNode = onlineDataRecord.ToJson();
            dataString = jsonNode.toString();
        }
        return dataString;
    }


}
