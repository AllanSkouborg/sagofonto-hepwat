package dk.artogis.hepwat.common.schema;

import java.util.LinkedHashMap;
import java.util.Map;

public interface IDataSchema {


     static String f_id = "_id";
     static String f_value = "value";
     static String f_deviceId = "hepwatDeviceId";
     static String f_calcType = "calctype";
     static String f_aggType = "aggtype";
    static String f_dataType = "datatype";

     static String f_timeStamp = "timestamp";
     Map<Integer, Field> fields = new LinkedHashMap<>();

    public String primaryIndex  = f_id;

    public static String timeIndex = f_timeStamp;

    public static String deviceIdIndex = f_deviceId;

    public static String valueField = f_value;

    public static String aggregationTypeField = f_aggType;

    public static String calculationTypeField = f_calcType;

    public static String dataTypeField = f_dataType;

    public Map<Integer, Field> getFields();


    public static String getPrimaryIndex() {
        return primaryIndex;
    }

    public static String getTimeIndex() {
        return timeIndex;
    }

    public static String getDeviceIdIndex() {
        return deviceIdIndex;
    }

    public static String getValueField() {
        return valueField;
    }

    public static String getAggregationTypeField() {
        return aggregationTypeField;
    }

    public static String getCalculationTypeField() {
        return calculationTypeField;
    }

    public static String getDataTypeField() {
        return dataTypeField;
    }

}
