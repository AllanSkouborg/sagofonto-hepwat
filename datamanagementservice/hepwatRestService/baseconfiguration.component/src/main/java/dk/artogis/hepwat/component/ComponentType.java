package dk.artogis.hepwat.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.datastore.DataStore;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
TEST KOMMENTAR 02
 */

public class ComponentType implements Serializable
{
    @JsonIgnore
    private static String tableName = "config_component_type";
    private UUID id ;
    private String name;
    private UUID datastoreId;

    private Integer type ;
    @JsonIgnore
    private LocalDateTime editDateTime;
    private String editDate;
    private DataStore dataStore;
    private String wfs;
    private String wfsLayer;
    private String fieldId;
    private String fieldName;
    private String fieldDescription;
    private String componentTableName;
    private Integer zOrder;

    @JsonIgnore
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static String f_id = "id";
    private static String f_name = "name";
    private static String f_type = "type";
    private static String f_dataStoreId = "datastore_id";
    private static String f_editTime = "edittime";
    private static String f_wfs = "wfs";
    private static String f_wfsLayer = "wfs_layer";
    private static String f_field_id = "field_id";
    private static String f_field_name = "field_name";
    private static String f_field_description = "field_description";
    private static String f_z_order = "z_order";

    private static String f_componentTableName = "component_table_name" ;

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

    public String getComponentTableName() {
        return componentTableName;
    }

    public void setComponentTableName(String componentTableName) {
        this.componentTableName = componentTableName;
    }
    public String getWfs() {
        return wfs;
    }

    public void setWfs(String wfs) {
        this.wfs = wfs;
    }

    public void setWfsLayer(String wfsLayer) {
        this.wfsLayer = wfsLayer;
    }
    public String getWfsLayer() {
        return wfsLayer;
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
    public ComponentType(UUID  id, String name, UUID dataStoreId, Integer type, String componentTableName, String wfs, String wfsLayer)
    {
        editDateTime = LocalDateTime.now();
        editDate = editDateTime.format(dateTimeFormatter);
        this.id = id;
        this.name = name;
        this.datastoreId = dataStoreId;
        this.componentTableName = componentTableName;
        this.wfs = wfs;
        this.type = type;
        this.wfsLayer = wfsLayer;
    }
    public ComponentType()
    {
        editDateTime = LocalDateTime.now();
        editDate = editDateTime.format(dateTimeFormatter);
    }
    public ComponentType(ResultSet rs)
    {
        //TODO : fill out if nessecary
    }
    public Status Insert(Connection connection )
    {
        Status status = new Status();
        status.Success = false;
        String[] insertFields = null;
        String[] insertValues = null;
        Map parameters = null;

        insertFields = getInsertFields();
        insertValues = getInsertValuesParameters();
        parameters = new HashMap();

        if (this.id == null)
            this.id = UUID.randomUUID();
        parameters.put("@"+f_id, this.id);

        parameters.put("@"+f_wfs, this.wfs);
        parameters.put("@"+f_wfsLayer, this.wfsLayer);
        parameters.put("@"+f_componentTableName, this.componentTableName);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_dataStoreId, this.datastoreId);
        parameters.put("@"+f_field_id, this.fieldId);
        parameters.put("@"+f_field_name, this.fieldName);
        parameters.put("@"+f_field_description, fieldDescription);
        parameters.put("@"+ f_z_order, this.zOrder);

        //parameters.put("@"+f_type, this.type);
        LocalDateTime editDateTime = LocalDateTime.parse(editDate, dateTimeFormatter);

        parameters.put("@"+f_editTime, editDateTime);

        Integer result = connection.insert(tableName, insertFields, insertValues, parameters, false, null);

        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows updated, or only part of records updated";
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

        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_wfs, this.wfs);
        parameters.put("@"+f_wfsLayer, this.wfsLayer);
        parameters.put("@"+f_componentTableName, this.componentTableName);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_dataStoreId, this.datastoreId);
        parameters.put("@"+f_field_id, this.fieldId);
        parameters.put("@"+f_field_name, this.fieldName);
        parameters.put("@"+f_field_description, fieldDescription);
        parameters.put("@"+ f_z_order, this.zOrder);
        //parameters.put("@"+f_type, this.type);
        LocalDateTime editDateTime = LocalDateTime.parse(editDate, dateTimeFormatter);
        parameters.put("@"+f_editTime, editDateTime);

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_id;
        criterias[0] = criteria1;

        Integer result = connection.update(tableName, updateFields, updateValues, criterias, parameters);

        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows updated, or only part of records updated";
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

    public static ComponentType GetComponentType(UUID id, Connection connection ) throws Exception
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
            throw new Exception("error in finding resultset");
        }
        List<ComponentType> objectTypes = GetComponentTypesFromResultset(rs);
        if (objectTypes.size() >= 1) {
            ComponentType ot = (ComponentType) (objectTypes.toArray())[0];
            ot.dataStore = DataStore.GetDataStore(ot.datastoreId, connection);
            return ot;
        }
        return  null;
    }

    public static ComponentType GetComponentType(Integer type, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria aCriteria = new SqlCriteria();
        aCriteria.FieldName = f_type;
        aCriteria.CompareOperator = "=";
        aCriteria.FieldValue = "@" + f_type;
        SqlCriteria[] criterias = new SqlCriteria[1];
        criterias[0] = aCriteria;

        Map parameters = new HashMap();
        parameters.put("@"+f_type, type);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<ComponentType> objectTypes = GetComponentTypesFromResultset(rs);
        if (objectTypes.size() >= 1) {
            ComponentType ot = (ComponentType) (objectTypes.toArray())[0];
            ot.dataStore = DataStore.GetDataStore(ot.datastoreId, connection);
            return ot;
        }
        return  null;
    }
    private static String[] getUpdateFields() {
        return new String[]{f_name, f_dataStoreId, f_editTime, f_componentTableName, f_wfs, f_wfsLayer, f_field_id, f_field_name, f_field_description, f_z_order};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{"@"+f_name, "@"+f_dataStoreId, "@"+f_editTime, "@"+f_componentTableName, "@"+f_wfs, "@"+f_wfsLayer, "@"+f_field_id, "@"+f_field_name, "@"+f_field_description, "@"+ f_z_order};
    }

    private static String[] getFields() {
        return new String[]{f_id, f_name, f_type, f_dataStoreId, f_editTime, f_componentTableName, f_wfs, f_wfsLayer, f_field_id, f_field_name, f_field_description, f_z_order};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name, "@"+f_type, "@"+f_dataStoreId, "@"+f_editTime, "@"+f_componentTableName, "@"+f_wfs, "@"+f_wfsLayer, "@"+f_field_id, "@"+f_field_name, "@"+f_field_description, "@"+ f_z_order};
    }


    private static String[] getInsertFields() {
        return new String[]{f_id, f_name, f_dataStoreId, f_editTime, f_componentTableName, f_wfs, f_wfsLayer,f_field_id, f_field_name, f_field_description, f_z_order};
    }

    private static String[] getInsertValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name,  "@"+f_dataStoreId, "@"+f_editTime, "@"+f_componentTableName, "@"+f_wfs, "@"+f_wfsLayer,"@"+f_field_id, "@"+f_field_name, "@"+f_field_description, "@"+ f_z_order};
    }
    public static List<ComponentType> GetComponentTypes(Connection connection ) throws Exception
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
        List<ComponentType> objectTypes = null;
        try {
             objectTypes = GetComponentTypesFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage());
        }

        return  objectTypes;
    }

    public static List<ComponentType> GetComponentTypesFromResultset(ResultSet resultSet) throws Exception
    {
        List<ComponentType> componentTypes = new ArrayList<ComponentType>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                ComponentType componentType = new ComponentType();
                componentType.id = resultSet.getObject(f_id, UUID.class);
                componentType.name = resultSet.getString(f_name);
                componentType.wfs = resultSet.getString(f_wfs);
                componentType.wfsLayer = resultSet.getString(f_wfsLayer);
                componentType.type = resultSet.getInt(f_type);
                componentType.componentTableName = resultSet.getString(f_componentTableName);
                componentType.datastoreId = resultSet.getObject(f_dataStoreId, UUID.class);
                componentType.editDateTime = resultSet.getTimestamp(f_editTime).toLocalDateTime();
                componentType.fieldId = resultSet.getString(f_field_id);
                componentType.fieldName = resultSet.getString(f_field_name);
                componentType.fieldDescription = resultSet.getString(f_field_description);
                componentType.zOrder = resultSet.getInt(f_z_order);

                componentTypes.add(componentType);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return componentTypes;
    }


}
