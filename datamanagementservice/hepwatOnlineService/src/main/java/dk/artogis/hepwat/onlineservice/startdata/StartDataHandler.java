package dk.artogis.hepwat.onlineservice.startdata;

import com.sun.jndi.toolkit.url.Uri;
import dk.artogis.hepwat.onlineservice.Config;
import dk.artogis.hepwat.onlineservice.subscription.DataSubscription;
import dk.artogis.hepwat.onlineservice.subscription.DataSubscriptions;
import dk.artogis.hepwat.onlineservice.taskhandling.OnlineWebSocketSession;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class StartDataHandler {

    private DataSubscriptions dataSubscriptions = null;
    private OnlineWebSocketSession onlineWebSocketSession = null;
    private URI serviceUri = null;
    public  StartDataHandler ( DataSubscriptions dataSubscriptions, OnlineWebSocketSession onlineWebSocketSession)
    {
        this.serviceUri = UriBuilder.fromUri(Config.hepwatRestService).build();
        this.onlineWebSocketSession = onlineWebSocketSession;
        this.dataSubscriptions = dataSubscriptions;


    }
    public void getStartData()
    {

        for (DataSubscription dataSubscription : dataSubscriptions.getDataSubscriptions()) {
            ActualDataRecord actualDataRecord = new ActualDataRecord(serviceUri, dataSubscription.getHepwatDeviceId(), dataSubscription.getAggtype(), dataSubscription.getCalctype(), dataSubscription.getDataType());
            String actualRecordText = actualDataRecord.getLastRecordText();
            if (actualRecordText != null)
            {
                onlineWebSocketSession.sendText(actualRecordText);
            }

        }
    }

}
