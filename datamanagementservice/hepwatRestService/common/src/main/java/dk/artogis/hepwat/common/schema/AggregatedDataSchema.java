package dk.artogis.hepwat.common.schema;

import java.util.*;

public class AggregatedDataSchema implements  IDataSchema{


    public static String f_endTime = "endtime";
    public static String f_count = "count";
    public static String f_sum = "sum";
    public static String f_lastValue = "lastValue";
    public static String f_timeStamp = "timestamp";

    private Map<Integer, Field> fields = new LinkedHashMap<>();

    public static String timeIndex = f_endTime;

    public Map<Integer, Field> getFields() {
        return fields;
    }

    public AggregatedDataSchema()
    {
        Field field1 = new Field();
        field1.name = f_deviceId;
        field1.type = FieldType.INTEGER;
        fields.put(1, field1);

        Field field2 = new Field();
        field2.name = f_value;
        field2.type = FieldType.DOUBLE;
        fields.put(2, field2);

        Field field3 = new Field();
        field3.name = f_sum;
        field3.type = FieldType.DOUBLE;
        fields.put(3, field3);

        Field field4 = new Field();
        field4.name = f_calcType;
        field4.type = FieldType.INTEGER;
        fields.put(4, field4);

        Field field5 = new Field();
        field5.name = f_aggType;
        field5.type = FieldType.INTEGER;
        fields.put(5, field5);

        Field field6 = new Field();
        field6.name = f_count;
        field6.type = FieldType.INTEGER;
        fields.put(6, field6);

        Field field7 = new Field();
        field7.name = f_endTime;
        field7.type = FieldType.LONG;
        fields.put(7, field7);

        Field field8 = new Field();
        field8.name = f_timeStamp;
        field8.type = FieldType.LONG;
        fields.put(8, field8);

        Field field9 = new Field();
        field9.name = f_lastValue;
        field9.type = FieldType.DOUBLE;
        fields.put(9, field9);

        Field field10 = new Field();
        field10.name = f_dataType;
        field10.type = FieldType.INTEGER;
        fields.put(10, field10);

    }
}
