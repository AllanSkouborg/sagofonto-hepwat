package dk.artogis.hepwat.object;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DoubleNode;

import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.datastore.DataStore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class FeatureObject {




    public static List<Field> GetFeatureObjectFields( Connection connection, String tableName, List<String> fieldNames, KeyDescription[] keyDescriptions ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        //String[] fieldNameArray = (String[]) fieldNames.toArray();
        List<String> fieldNamesWithoutPrefix = new ArrayList<>();
        List<Field>  fields = new ArrayList<Field>();
        for (String fieldName : fieldNames) {
            String fieldNameParts[] = fieldName.split(":");
            String fieldNameWithoutPrefix = null;
            if (fieldNameParts.length > 1)
                fieldNameWithoutPrefix = fieldNameParts[1];
            else
                fieldNameWithoutPrefix = fieldNameParts[0];
            Field field = new Field();
            field.nameWithOutPrefix = fieldNameWithoutPrefix;
            field.name = fieldName;
            fields.add(field);
            fieldNamesWithoutPrefix.add(fieldNameWithoutPrefix);
        }
        String[] fieldNameArray = new String[fieldNamesWithoutPrefix.size()] ;
        fieldNameArray = (String[])fieldNamesWithoutPrefix.toArray(fieldNameArray);


        Map parameters = new HashMap();
        SqlCriteria[] criterias = new SqlCriteria[keyDescriptions.length];
        Integer index = 0;
        for (KeyDescription keyDescription : keyDescriptions ) {
            SqlCriteria aCriteria = new SqlCriteria();
            aCriteria.FieldName = keyDescription.field;
            aCriteria.CompareOperator = "=";
            aCriteria.FieldValue = "@" + keyDescription.field;
            if ((keyDescriptions.length > 1) && (index == keyDescriptions.length -1))
                aCriteria.CriteriaOperand = "AND";
            criterias[index] = aCriteria;
            // check type of key
            if (keyDescription.type.toLowerCase().equals("integer"))
                parameters.put("@"+keyDescription.field, Integer.parseInt(keyDescription.value));
            else
                parameters.put("@"+keyDescription.field, keyDescription.value);
            index++;
        }


        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fieldNameArray, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        fields = GetFeatureObjectFromResultset(rs,fields);
        return  fields;
    }

    public static List<Field> GetFeatureObjectFromResultset(ResultSet resultSet,  List<Field> fields) throws Exception
    {
        if (resultSet == null)
            throw new Exception ("resultset null");
        try {   //https://www.postgresql.org/docs/9.1/static/datatype-numeric.html
            while (resultSet.next()) {

                for (Integer index = 1; index <= resultSet.getMetaData().getColumnCount(); index++) {
                    String columnName = resultSet.getMetaData().getColumnName(index);
                    for (Field field : fields) {
                        if (field.nameWithOutPrefix.equals(columnName)) {
                            String value = null;
                            try {
                                Integer type = resultSet.getMetaData().getColumnType(index);

                                switch (type) {
                                    case -5:
                                        value = resultSet.getBigDecimal(index).toPlainString(); // bigint
                                        break;
                                    case 2:
                                        value = Double.toString(resultSet.getDouble(index)); // decimal
                                        break;
                                    case 4:
                                        value = Integer.toString(resultSet.getInt(index)); //integer
                                        break;
                                    case 5:
                                        value = Integer.toString(resultSet.getInt(index)); // small int
                                        break;
                                    case 7:
                                        value = Float.toString(resultSet.getFloat(index)); //float
                                        break;
                                    case 8:
                                        value = Double.toString(resultSet.getDouble(index)); //double
                                        break;
                                    default: value = null;
                                        break;
                                }
                            }
                            catch (Exception ex) {
                                //TODO: Logging single field missed

                            }
                            finally {
                                field.value = value;
                            }
                        }
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return fields;
    }

    public static List<Field> getFields(Connection connection, List<String> fieldNames, Integer objectTypeId, KeyDescription[] keyDescriptions)
    {
        List<Field> fields = new ArrayList<Field>();
        Connection featureObjectDataStoreConnection = null;
        try {
            ObjectType objectType = ObjectType.GetObjectType(objectTypeId, connection);

            DataStore dataStore = DataStore.GetDataStore(objectType.getDatastoreId(), connection);

            featureObjectDataStoreConnection = new PostGreSQLConnection(dataStore.getServer(), dataStore.getPort(), dataStore.getDatabase(), dataStore.getSchema(), dataStore.getUser(), dataStore.getPassword());
            featureObjectDataStoreConnection.connect();

            //PostGreSQLConnection connection = new PostGreSQLConnection(Config.server, Config.port, Config.database, Config.schema, Config.user, Config.password);
            fields = FeatureObject.GetFeatureObjectFields(featureObjectDataStoreConnection, objectType.getObjectTableName(), fieldNames, keyDescriptions);

        }
        catch (Exception ex)
        {
            //TODO: logging
        }
        finally {
            if(featureObjectDataStoreConnection !=  null)
                featureObjectDataStoreConnection.close();
        }
        return  fields;
    }
}
