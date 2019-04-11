package dk.artogis.hepwat.onlineservice.clientmessage;

public class Subscription {

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

    public Integer getDatatype() {
        return datatype;
    }

    public void setDatatype(Integer datatype) {
        this.datatype = datatype;
    }

    public String getString()
    {
        String subscriptionText = "";
        subscriptionText = getHepwatDeviceId().toString() +  " aggtype: " + getAggtype().toString() + " calctype: " + getCalctype().toString() + " datatype: " + getDatatype().toString() ;

        return subscriptionText;
    }
}
