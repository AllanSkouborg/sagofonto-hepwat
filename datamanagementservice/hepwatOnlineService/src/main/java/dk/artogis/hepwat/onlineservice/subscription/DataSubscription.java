package dk.artogis.hepwat.onlineservice.subscription;

import dk.artogis.hepwat.onlineservice.clientmessage.Subscription;

public class DataSubscription {

    Integer hepwatDeviceId;
    Integer calctype;
    Integer aggtype;
    Integer datatype;

    public Integer getHepwatDeviceId() {
        return hepwatDeviceId;
    }

    public void setHepwatDeviceId(Integer hepwatDeviceId) {
        this.hepwatDeviceId = hepwatDeviceId;
    }

    public Integer getCalctype() {
        return calctype;
    }

    public void setCalctype(Integer calctype) {
        this.calctype = calctype;
    }

    public Integer getAggtype() {
        return aggtype;
    }

    public void setAggtype(Integer aggtype) {
        this.aggtype = aggtype;
    }

    public Integer getDataType() {
        return datatype;
    }

    public void setDataType(Integer dataType) {
        this.datatype = dataType;
    }

    public DataSubscription(Subscription subscription)
    {
        this.aggtype = subscription.getAggtype();
        this.calctype = subscription.getCalctype();
        this.hepwatDeviceId = subscription.getHepwatDeviceId();
        this.datatype = subscription.getDatatype();
    }

}
