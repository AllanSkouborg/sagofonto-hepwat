package dk.artogis.hepwat.onlineservice.startdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.data.DataRecord;

public class OnlineDataRecord {
    private Integer hepwatDeviceId;
    private Double value;
    private Long timestamp;
    private Integer aggtype = 0;
    private Integer calctype = 0;
    private Integer datatype = 0;

    public Integer getHepwatDeviceId() {
        return hepwatDeviceId;
    }

    public void setHepwatDeviceId(Integer hepwatDeviceId) {
        this.hepwatDeviceId = hepwatDeviceId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getAggtype() {
        return aggtype;
    }

    public void setAggtype(Integer aggtype) {
        this.aggtype = aggtype;
    }

    public Integer getCalctype() {
        return calctype;
    }

    public void setCalctype(Integer calctype) {
        this.calctype = calctype;
    }

    public Integer getDatatype() {
        return datatype;
    }

    public void setDatatype(Integer datatype) {
        this.datatype = datatype;
    }

    public OnlineDataRecord(DataRecord dataRecord)
    {
        this.hepwatDeviceId = dataRecord.getId();
        this.timestamp = dataRecord.getTimestamp();
        this.value = dataRecord.getValue();
        this.aggtype = 0;
        this.calctype = 0;
        this.datatype = 0;
    }
    public OnlineDataRecord(DataRecord dataRecord, Integer aggtype, Integer calctype, Integer datatype)
    {
        this.hepwatDeviceId = dataRecord.getId();
        this.timestamp = dataRecord.getTimestamp();
        this.value = dataRecord.getValue();
        this.aggtype = aggtype;
        this.calctype = calctype;
        this.datatype = datatype;
    }
    public JsonNode ToJson()
    {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode newJsonNode = mapper.convertValue(this, JsonNode.class);
        return  newJsonNode;
    }
}
