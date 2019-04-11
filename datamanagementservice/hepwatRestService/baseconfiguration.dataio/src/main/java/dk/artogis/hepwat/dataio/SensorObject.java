package dk.artogis.hepwat.dataio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.client.model.geojson.Geometry;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SensorObject implements Serializable
{
    @JsonIgnore
    public static String tableName = "sensor_object";
    private String id;
    private Integer datasourceId;
    private String name;
    private String description;
    @JsonIgnore
    private LocalDateTime updated;
    private String location;
    private String ogrGeometry;
    @JsonIgnore
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private Boolean configured;
    private String nameAlias;

    public static String f_id = "sensor_object_id";
    private static String f_datasourceId = "datasource_id";
    private static String f_name = "name";
    public static String f_description = "description";
    private static String f_updated = "updated";
    private static String f_location = "location";
    private static String f_ogrGeometry = "ogr_geometry";
    private static String f_configured = "configured";
    private static String f_nameAlias = "name_alias";

    //region getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDataSourceId() {
        return datasourceId;
    }

    public void setDataSourceId(Integer datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOGRGeometry() {
        return ogrGeometry;
    }

    public void setOGRGeometry(String ogrGeometry) {
        this.ogrGeometry = ogrGeometry;
    }

    public Boolean getConfigured() {
        return configured;
    }

    public void setConfigured(Boolean configured) {
        this.configured = configured;
    }

    public String getNameAlias() {
        return nameAlias;
    }

    public void setNameAlias(String nameAlias) {
        this.nameAlias = nameAlias;
    }

//endregion getters and setters

    public SensorObject(String id, Integer datasourceId, String name, String description,
                        LocalDateTime updated, String location, String ogrGeometry, Boolean configured)
    {
        this.id = id;
        this.datasourceId = datasourceId;
        this.name = name;
        this.description = description;
        this.updated = updated;
        this.location = location;
        this.ogrGeometry = ogrGeometry;
        this.configured = configured;
        this.nameAlias = nameAlias;
    }
    public SensorObject()
    {

    }
    public SensorObject(ResultSet rs)
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

        insertFields = getFields();
        insertValues = getValuesParameters();
/*
        if ((this.authenticationType != null) && (!this.authenticationType.isEmpty()))
        {
            this.updated = LocalDateTime.parse(authenticationType, dateTimeFormatter);
        }
*/
        parameters = new HashMap();
        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_datasourceId, this.datasourceId);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_description, this.description);
        parameters.put("@"+f_updated, this.updated);
        parameters.put("@"+f_location, this.location);
        parameters.put("@"+f_ogrGeometry, this.ogrGeometry);
        parameters.put("@"+f_configured, this.configured);
        parameters.put("@"+f_nameAlias, this.nameAlias);

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
        String[] updateFields = getUpdateFields();
        String[] updateValues = getUpdateValuesParameters();
        Map parameters = null;
        parameters = new HashMap();

        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_datasourceId, this.datasourceId);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_description, this.description);
        parameters.put("@"+f_updated, this.updated);
        parameters.put("@"+f_location, this.location);
        parameters.put("@"+f_ogrGeometry, this.ogrGeometry);
        parameters.put("@"+f_configured, this.configured);
        parameters.put("@"+f_nameAlias, this.nameAlias);

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
    public static Status UpdateConfigured(String id, Integer datasourceId, Boolean configured, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        String[] updateFields = getUpdateConfiguredFields();
        String[] updateValues = getUpdateConfiguredValuesParameters();
        Map parameters = null;
        parameters = new HashMap();

        parameters.put("@"+f_id, id);
        parameters.put("@"+f_datasourceId, datasourceId);
        parameters.put("@"+f_configured, configured);

        SqlCriteria[] criterias = new SqlCriteria[2];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_id;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_datasourceId;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_datasourceId;
        criterias[1] = criteria1;

        Integer result = connection.update(tableName, updateFields, updateValues, criterias, parameters);

        if (result == 1 )
            status.Success = true;
        else {
            status.Message = "no rows updated, or only part of records updated";
            status.Error = connection.Status.Error;
        }
        return status;
    }

    public static Status UpdateAlias(String id, Integer datasourceId, String nameAlias, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        String[] updateFields = getUpdateAliasFields();
        String[] updateValues = getUpdateAliasValuesParameters();
        Map parameters = null;
        parameters = new HashMap();

        parameters.put("@"+f_id, id);
        parameters.put("@"+f_datasourceId, datasourceId);
        parameters.put("@"+f_nameAlias, nameAlias);

        SqlCriteria[] criterias = new SqlCriteria[2];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_id;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_datasourceId;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_datasourceId;
        criterias[1] = criteria1;

        Integer result = connection.update(tableName, updateFields, updateValues, criterias, parameters);
        System.out.println("sensorobject update alias result: " + result);

        if (result == 1 )
            status.Success = true;
        else {
            status.Message = "no rows updated, or only part of records updated";
            status.Error = connection.Status.Error;
        }
        return status;
    }

    public static SensorObject GetSensorObject(String id, Integer datasourceId, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[2];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_id;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_datasourceId;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_datasourceId;
        criterias[1] = criteria1;

        Map parameters = new HashMap();
        parameters.put("@"+f_id, id);
        parameters.put("@"+f_datasourceId, datasourceId);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<SensorObject> sensorObjects = GetSensorObjectsFromResultset(rs);
        if (sensorObjects.size() >= 1) {
            SensorObject sensorObject = (SensorObject) (sensorObjects.toArray())[0];
            return sensorObject;
        }
        return  null;
    }
    private static String[] getUpdateConfiguredFields() {
        return new String[]{ f_configured};
    }
    private static String[] getUpdateConfiguredValuesParameters() {
        return new String[]{ "@"+f_configured};
    }

    private static String[] getUpdateAliasFields() {
        return new String[]{ f_nameAlias};
    }
    private static String[] getUpdateAliasValuesParameters() {
        return new String[]{ "@"+f_nameAlias};
    }

    private static String[] getUpdateFields() {
        String[] fields = getFields();
        List<String> fieldsList = new ArrayList<String>();
        fieldsList.addAll(Arrays.asList(fields));
        fieldsList.remove(f_id);
        String[] fieldsArray = null;
        if (fieldsList != null ) {
            fieldsArray = new String[fieldsList.size()];
            fieldsArray = (String[]) fieldsList.toArray(fieldsArray);
        }
        return fieldsArray;
    }

    private static String[] getUpdateValuesParameters() {
        String[] valueParameters = getValuesParameters();
        List<String> valueParametersList = new ArrayList<String>();
        valueParametersList.addAll(Arrays.asList(valueParameters));
        valueParametersList.remove("@"+ f_id);

        String[] valueParametersArray = null;
        if (valueParameters != null ) {
            valueParametersArray = new String[valueParametersList.size()];
            valueParametersArray = (String[]) valueParametersList.toArray(valueParametersArray);
        }
        return valueParametersArray;
    }

    private static String[] getFields() {
        return new String[]{f_id, f_datasourceId, f_name, f_description, f_updated, f_location, f_ogrGeometry, f_configured, f_nameAlias};
    }

    private static String[] getValuesParameters() {
        String[] fields = getFields();
        List<String> fieldsList = Arrays.asList(fields);
        List<String> valueParameters = new ArrayList<String>();
        for (String field : fieldsList)
        {
            valueParameters.add("@"+ field);
        }
        String[] valueParametersArray = null;
        if (valueParameters != null ) {
            valueParametersArray = new String[valueParameters.size()];
            valueParametersArray = (String[]) valueParameters.toArray(valueParametersArray);
        }
        return  valueParametersArray;
    }
    public static List<SensorObject> GetSensorObjects(Connection connection ) throws Exception
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
        List<SensorObject> sensorObjects = null;
        try {
            sensorObjects = GetSensorObjectsFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  sensorObjects;
    }

    public static List<SensorObject> GetSensorObjectsFromResultset(ResultSet resultSet) throws Exception
    {
        List<SensorObject> SensorObjects = new ArrayList<SensorObject>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                SensorObject SensorObject = new SensorObject();
                SensorObject.id = resultSet.getString(f_id);
                SensorObject.datasourceId = resultSet.getInt(f_datasourceId);
                SensorObject.name = resultSet.getString(f_name);
                SensorObject.description = resultSet.getString(f_description);
                Timestamp time = resultSet.getTimestamp(f_updated);
                if (time != null)
                    SensorObject.updated = time.toLocalDateTime();
                SensorObject.location = resultSet.getString(f_location);
                SensorObject.ogrGeometry = resultSet.getString(f_ogrGeometry);
                SensorObject.configured =  resultSet.getBoolean(f_configured);
                SensorObject.nameAlias =  resultSet.getString(f_nameAlias);
                SensorObjects.add(SensorObject);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return SensorObjects;
    }

}
