package dk.artogis.hepwat.onlineservice.subscription;

import com.fasterxml.jackson.databind.JsonNode;
import dk.artogis.hepwat.common.schema.AggregatedDataSchema;
import dk.artogis.hepwat.common.schema.IDataSchema;
import dk.artogis.hepwat.onlineservice.clientmessage.Subscription;

import java.util.ArrayList;

public class DataSubscriptions {
    ArrayList<DataSubscription>  dataSubscriptions;

    public ArrayList<DataSubscription> getDataSubscriptions() {
        return dataSubscriptions;
    }

    public void setDataSubscriptions(ArrayList<DataSubscription> dataSubscriptions) {
        this.dataSubscriptions = dataSubscriptions;
    }

    public DataSubscriptions(Subscription[] subscriptions)
    {
        dataSubscriptions = new ArrayList<>();
        for (Subscription subscription :subscriptions) {
            DataSubscription dataSubscription = new DataSubscription(subscription);
            dataSubscriptions.add(dataSubscription);
        }

    }

    public boolean subscriptionExists( Integer key, JsonNode recordContent)
    {
        Integer aggType = 0;
        Integer calcType = 0;
        Integer dataType = 0;

        if (recordContent.get(IDataSchema.f_calcType) != null && recordContent.get(IDataSchema.f_calcType).isNumber())
            calcType =  recordContent.get(IDataSchema.f_calcType).asInt();

        if (recordContent.get(IDataSchema.f_aggType) != null && recordContent.get(IDataSchema.f_aggType).isNumber())
            aggType =  recordContent.get(IDataSchema.f_aggType).asInt();

        if (recordContent.get(IDataSchema.f_dataType) != null && recordContent.get(IDataSchema.f_dataType).isNumber())
            dataType =  recordContent.get(IDataSchema.f_dataType).asInt();

        for (DataSubscription dataSubscription : dataSubscriptions) {
            if (dataSubscription.hepwatDeviceId.intValue() == key.intValue())
                if (dataSubscription.calctype.intValue() == calcType.intValue())
                    if (dataSubscription.aggtype.intValue() == aggType.intValue())
                        if (dataSubscription.datatype.intValue() == dataType.intValue())
                            return  true;
        }

        return  false;
    }
}

