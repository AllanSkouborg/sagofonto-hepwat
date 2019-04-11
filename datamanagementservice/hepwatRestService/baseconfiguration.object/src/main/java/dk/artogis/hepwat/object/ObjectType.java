package dk.artogis.hepwat.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.datastore.DataStore;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ObjectType implements Serializable
{
    @JsonIgnore
    private static String tableName = "config_object_type";
    private UUID id ;
    private String name;
    private UUID datastoreId;

    private Integer type ;
    @JsonIgnore
    private LocalDateTime editDateTime;
    private String editDate;
    private DataStore dataStore;
    @JsonIgnore
    private String keyDescription;
    private KeyDescription[] keyDescriptions;
    private String wfs;
    private String wfsLayer;
    private String fieldId;
    private String fieldName;
    private String fieldDescription;
    private Integer zOrder;

    private String objectTableName;
    @JsonIgnore
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static String f_id = "id";
    private static String f_name = "name";
    private static String f_type = "type";
    private static String f_dataStoreId = "datastore_id";
    private static String f_editTime = "edittime";
    private static String f_keyDescription = "key_description";
    private static String f_wfs = "wfs";
    private static String f_wfsLayer = "wfs_layer";
    private static String f_field_id = "field_id";
    private static String f_field_name = "field_name";
    private static String f_field_description = "field_description";
    private static String f_z_order = "z_order";

    private static String f_objectTableName = "object_table_name";

    //region getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public UUID getDatastoreId() {
        return datastoreId;
    }

    public void setDatastoreId(UUID datastoreId) {
        this.datastoreId = datastoreId;
    }


    public DataStore getDataStore() {
        return dataStore;
    }

    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public LocalDateTime getEditDateTime() {

        return editDateTime;
    }

    public void setEditDateTime(LocalDateTime editDateTime) {
        this.editDateTime = editDateTime;
    }

    public String getEditDate() {
        if (editDateTime != null)
            editDate = editDateTime.format(dateTimeFormatter);
        else  editDate = "";
        return editDate;
    }

    public void setEditDate(String editDate) {
        this.editDate = editDate;
    }

    public String getKeyDesciption() {
        return keyDescription;
    }

    public void setKeyDesciption(String keyDesciption) {
        this.keyDescription = keyDesciption;
    }

    public String getWfs() {
        return wfs;
    }

    public void setWfs(String wfs) {
        this.wfs = wfs;
    }

    public KeyDescription[] getKeyDescriptions() {
        return keyDescriptions;
    }

    public void setKeyDescriptions(KeyDescription[] keyDescriptions) {
        this.keyDescriptions = keyDescriptions;
    }

    public String getObjectTableName() {
        return objectTableName;
    }

    public void setObjectTableName(String objectTableName) {
        this.objectTableName = objectTableName;
    }

    public String getWfsLayer() {
        return wfsLayer;
    }

    public void setWfsLayer(String wfsLayer) {
        this.wfsLayer = wfsLayer;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public Integer getzOrder() {
        return zOrder;
    }

    public void setzOrder(Integer zOrder) {
        this.zOrder = zOrder;
    }

    //endregion getters and setters
    public ObjectType( UUID  id, String name, UUID dataStoreId, Integer type, String keyDesciption, String wfs, String wfsLayer, String objectTableName)
    {
        editDateTime = LocalDateTime.now();
        editDate = editDateTime.format(dateTimeFormatter);
        this.id = id;
        this.name = name;
        this.datastoreId = dataStoreId;
        this.keyDescription = keyDesciption;
        this.wfs = wfs;
        this.type = type;
        this.objectTableName = objectTableName;
        this.wfsLayer = wfsLayer;
    }
    public ObjectType()
    {
        editDateTime = LocalDateTime.now();
        editDate = editDateTime.format(dateTimeFormatter);
    }
    public ObjectType(ResultSet rs)
    {
        //TODO : fill out if nessecary
    }
    public Status Insert(Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        String[] insertFields = getFields();
        String[] insertValues = getValuesParameters();
        Map parameters = null;

        insertFields = getInsertFields();
        insertValues = getInsertValuesParameters();
        parameters = new HashMap();

        if (keyDescriptions != null) {
            if (!KeyDescriptionsToJson()) return status;
        }

        if (this.id == null)
            this.id = UUID.randomUUID();
        parameters.put("@"+f_id, this.id);

        parameters.put("@"+f_wfs, this.wfs);
        parameters.put("@"+f_keyDescription, this.keyDescription);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_dataStoreId, this.datastoreId);
        //parameters.put("@"+f_type, this.type);
        LocalDateTime editDateTime = LocalDateTime.parse(editDate, dateTimeFormatter);

        parameters.put("@"+f_editTime, editDateTime);
        parameters.put("@"+f_objectTableName, this.objectTableName);
        parameters.put("@"+f_wfsLayer, this.wfsLayer);
        parameters.put("@"+f_field_id, this.fieldId);
        parameters.put("@"+f_field_name, this.fieldName);
        parameters.put("@"+f_field_description, this.fieldDescription);
        parameters.put("@"+ f_z_order, this.zOrder);

        Integer result = connection.insert(tableName, insertFields, insertValues, parameters, false, null);

        if (result == 1 )
            status.Success = true;
        else {
            status.Message = "no rows inserted";
            status.Error = connection.Status.Error;
        }
        return status;
    }
    public Status Update(Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        String[] updateFields = getFields();
        String[] updateValues = getValuesParameters();
        Map parameters = null;

        updateFields = getUpdateFields();
        updateValues = getUpdateValuesParameters();
        parameters = new HashMap();

        if (keyDescriptions != null) {
            if (!KeyDescriptionsToJson()) return status;
        }
        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_wfs, this.wfs);
        parameters.put("@"+f_keyDescription, this.keyDescription);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_dataStoreId, this.datastoreId);
        //parameters.put("@"+f_type, this.type);
        LocalDateTime editDateTime = LocalDateTime.parse(editDate, dateTimeFormatter);
        parameters.put("@"+f_editTime, editDateTime);
        parameters.put("@"+f_objectTableName, this.objectTableName);
        parameters.put("@"+f_wfsLayer, this.wfsLayer);
        parameters.put("@"+f_field_id, this.fieldId);
        parameters.put("@"+f_field_name, this.fieldName);
        parameters.put("@"+f_field_description, fieldDescription);
        parameters.put("@"+ f_z_order, this.zOrder);

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_id;
        criterias[0] = criteria1;

        Integer result = connection.update(tableName, updateFields, updateValues, criterias, parameters);

        if (result == 1 )
            status.Success = true;
        else {
            status.Message = "no rows updated, or only part of records updated";
            status.Error = connection.Status.Error;
        }
        return status;
    }
    public static Status Delete(UUID id, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        Map parameters = new HashMap();
        parameters.put("@"+f_id, id);

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@"+f_id;

        criterias[0] = criteria1;

        Integer result = connection.delete(tableName, criterias, parameters);
        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows deleted";
        return status;
    }
    private boolean KeyDescriptionsToJson() {

        ObjectMapper mapper = new ObjectMapper();
        try {
            this.keyDescription = mapper.writeValueAsString(keyDescriptions);
        }
        catch (Exception ex)
        {
            //TODO : Logging
            return false;
        }
        return true;
    }

    public static ObjectType GetObjectType( UUID id, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria aCriteria = new SqlCriteria();
        aCriteria.FieldName = f_id;
        aCriteria.CompareOperator = "=";
        aCriteria.FieldValue = "@f_id";
        SqlCriteria[] criterias = new SqlCriteria[1];
        criterias[0] = aCriteria;

        Map parameters = new HashMap();
        parameters.put("@f_id", id);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<ObjectType> objectTypes = GetObjectTypesFromResultset(rs);
        if (objectTypes.size() >= 1) {
            ObjectType ot = (ObjectType) (objectTypes.toArray())[0];
            ot.dataStore = DataStore.GetDataStore(ot.datastoreId, connection);
            return ot;
        }
        return  null;
    }
    public static ObjectType GetObjectType( Integer id, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria aCriteria = new SqlCriteria();
        aCriteria.FieldName = f_type;
        aCriteria.CompareOperator = "=";
        aCriteria.FieldValue = "@f_type";
        SqlCriteria[] criterias = new SqlCriteria[1];
        criterias[0] = aCriteria;

        Map parameters = new HashMap();
        parameters.put("@f_type", id);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<ObjectType> objectTypes = GetObjectTypesFromResultset(rs);
        if (objectTypes.size() >= 1) {
            ObjectType ot = (ObjectType) (objectTypes.toArray())[0];
            ot.dataStore = DataStore.GetDataStore(ot.datastoreId, connection);
            return ot;
        }
        return  null;
    }
    private static String[] getInsertFields() {
        return new String[]{f_id, f_name, f_dataStoreId, f_editTime, f_keyDescription, f_wfs, f_objectTableName, f_wfsLayer, f_field_id, f_field_name, f_field_description, f_z_order};
    }

    private static String[] getInsertValuesParameters() {
        return new String[]{"@"+f_id,"@"+f_name, "@"+f_dataStoreId, "@"+f_editTime, "@"+f_keyDescription, "@"+f_wfs, "@"+f_objectTableName, "@"+f_wfsLayer, "@"+f_field_id, "@"+f_field_name, "@"+f_field_description, "@"+ f_z_order};
    }

    private static String[] getUpdateFields() {
        return new String[]{f_name,  f_dataStoreId, f_editTime, f_keyDescription, f_wfs, f_objectTableName, f_wfsLayer,  f_field_id, f_field_name, f_field_description, f_z_order};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{"@"+f_name, "@"+f_dataStoreId, "@"+f_editTime, "@"+f_keyDescription, "@"+f_wfs, "@"+f_objectTableName, "@"+f_wfsLayer,  "@"+f_field_id, "@"+f_field_name, "@"+f_field_description, "@"+ f_z_order};
    }


    private static String[] getFields() {
        return new String[]{f_id, f_name, f_type, f_dataStoreId, f_editTime, f_keyDescription, f_wfs, f_objectTableName, f_wfsLayer, f_field_id, f_field_name, f_field_description, f_z_order};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name, "@"+f_type, "@"+f_dataStoreId, "@"+f_editTime, "@"+f_keyDescription, "@"+ f_wfs, "@"+f_objectTableName, "@"+f_wfsLayer, "@"+f_field_id, "@"+f_field_name, "@"+f_field_description, "@"+ f_z_order};
    }
    public static List<ObjectType> GetObjectTypes(Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        Map parameters = new HashMap();
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, null, null, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<ObjectType> objectTypes = null;
        try {
             objectTypes = GetObjectTypesFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  objectTypes;
    }

    public static List<ObjectType> GetObjectTypesFromResultset(ResultSet resultSet) throws Exception
    {
        List<ObjectType> objectTypes = new ArrayList<ObjectType>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                ObjectType ot = new ObjectType();
                ot.id = resultSet.getObject(f_id, UUID.class);
                ot.name = resultSet.getString(f_name);
                ot.wfs = resultSet.getString(f_wfs);
                ot.type = resultSet.getInt(f_type);
                ot.keyDescription = resultSet.getString(f_keyDescription);
                ot.objectTableName = resultSet.getString(f_objectTableName);
                ot.wfsLayer = resultSet.getString(f_wfsLayer);
                ot.fieldId = resultSet.getString(f_field_id);
                ot.fieldName = resultSet.getString(f_field_name);
                ot.fieldDescription = resultSet.getString(f_field_description);
                ot.zOrder = resultSet.getInt(f_z_order);

                ObjectMapper objectMapper = new ObjectMapper();
                if (ot.keyDescription != null)
                {
                    try {
                        ot.keyDescriptions =  objectMapper.readValue(ot.keyDescription, KeyDescription[].class);
                    }
                    catch (Exception ex)
                    {
                        //TODO: Logging
                        throw new Exception("could not retrieve json from key description" );
                    }
                }
                ot.datastoreId = resultSet.getObject(f_dataStoreId, UUID.class);
                Timestamp time = resultSet.getTimestamp(f_editTime);
                if (time != null)
                    ot.editDateTime = time.toLocalDateTime();
                objectTypes.add(ot);

            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return objectTypes;
    }


}
