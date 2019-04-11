package dk.artogis.hepwat.common.schema;

import java.util.LinkedHashMap;
import java.util.Map;

public class RawDataSchema implements  IDataSchema{

    private Map<Integer, Field> fields = new LinkedHashMap<>();

    public Map<Integer, Field> getFields() {
        return fields;
    }


    public RawDataSchema()
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
        field3.name = f_timeStamp;
        field3.type = FieldType.LONG;
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
        field6.name = f_dataType;
        field6.type = FieldType.INTEGER;
        fields.put(6, field6);

    }


}
