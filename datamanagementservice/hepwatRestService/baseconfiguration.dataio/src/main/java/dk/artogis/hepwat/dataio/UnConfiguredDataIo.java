package dk.artogis.hepwat.dataio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// import java.time.ZonedDateTime;
import java.util.*;

public class UnConfiguredDataIo implements Serializable
{
    @JsonIgnore
    private static String tableName = "sensor_object_nodes";
    private Integer id ;
    private Integer dataSourceId;
    private String sensorObjectId;
    private String sensorObjectNodeId;
    private String name;
    private String description;
    private String sensorObjectDescription;
    private String sensorObjectName;
    private String dataSourceName;
    @JsonIgnore
    private String dataType;
    private String nodeType;
    @JsonIgnore
    private String nodeDomain;
    @JsonIgnore
    private Boolean readable = true;
    @JsonIgnore
    private Boolean writeable = false;
    private Integer interval = 1000;
    @JsonIgnore
    private String updatedString;
    private String unit;
    private Timestamp lastRun;
    private String status;
    private Boolean sensorLog;
    private Boolean configured;
    @JsonIgnore
    private LocalDateTime updated;
    @JsonIgnore
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private String lastValue;

    private String nameAlias;

    private static String f_id = "sensor_object_node_key";
    private static String f_name = "name";
    private static String f_unit = "unit";
    private static String f_description = "description";
    private static String f_sensorObjectId = "sensor_object_id";
    private static String f_sensorObjectNodeId = "sensor_object_node_id";
    private static String f_dataSourceId = "datasource_id";
    private static String f_nodeType = "nodetype";
    private static String f_dataType = "datatype";
    private static String f_nodeDomain  = "nodedomain";
    private static String f_readable = "readable";
    private static String f_writeable = "writeable";
    private static String f_interval = "interval";
    private static String f_updated = "updated";
    private static String f_lastRun = "lastrun";
    private static String f_status = "status";
    private static String f_sensorLog = "sensor_log";
    private static String f_configured = "configured";

    private static String f_sensorObjectDescription = "sensor_object_description";
    private static String f_sensorObjectName = "sensor_object_name";
    private static String f_dataSourceName = "datasource_name";

    private static String f_lastValue = "lastValue";

    private static String f_nameAlias = "name_alias";

    //region getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSensorObjectNodeId() {
        return sensorObjectNodeId;
    }

    public void setSensorObjectNodeId(String sensorObjectNodeId) {
        this.sensorObjectNodeId = sensorObjectNodeId;
    }

    public String getSensorObjectId() {
        return sensorObjectId;
    }

    public void setSensorObjectId(String sensorObjectId) {
        this.sensorObjectId = sensorObjectId;
    }

    public Integer getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getNodeDomain() {
        return nodeDomain;
    }

    public void setNodeDomain(String nodeDomain) {
        this.nodeDomain = nodeDomain;
    }

    public Boolean getReadable() {
        return readable;
    }

    public void setReadable(Boolean readable) {
        this.readable = readable;
    }

    public Boolean getWriteable() {
        return writeable;
    }

    public void setWriteable(Boolean writeable) {
        this.writeable = writeable;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getUpdatedString() {
        return updatedString;
    }

    public void setUpdatedString(String updatedString) {
        this.updatedString = updatedString;
    }

    public Timestamp getLastRun() {
        return lastRun;
    }

    public void setLastRun(Timestamp lastRun) {
        this.lastRun = lastRun;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getSensorLog() {
        return sensorLog;
    }

    public void setSensorLog(Boolean sensorLog) {
        this.sensorLog = sensorLog;
    }

    public Boolean getConfigured() {
        return configured;
    }

    public void setConfigured(Boolean configured) {
        this.configured = configured;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public String getSensorObjectDescription() {
        return sensorObjectDescription;
    }

    public void setSensorObjectDescription(String sensorObjectDescription) {
        this.sensorObjectDescription = sensorObjectDescription;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getLastValue() {
        return lastValue;
    }

    public void setLastValue(String lastValue) {
        this.lastValue = lastValue;
    }

    public String getNameAlias() {
        return nameAlias;
    }

    public void setNameAlias(String nameAlias) {
        this.nameAlias = nameAlias;
    }

    public String getSensorObjectName() {
        return sensorObjectName;
    }

    public void setSensorObjectName(String sensorObjectName) {
        this.sensorObjectName = sensorObjectName;
    }

    //endregion getters and setters

    public UnConfiguredDataIo(Integer  id, String name, String description, String unit, Integer dataSourceId,
                              String sensorObjectId, String sensorObjectNodeId, String nodeType, String dataType,
                              String nodeDomain, Boolean writeable, Boolean readable, Integer interval,
                              LocalDateTime updated, Timestamp lastRun, String status, Boolean sensorLog,
                              Boolean configured, String lastValue)

    {
        this.id = id;
        this.name = name;
        this.sensorObjectId = sensorObjectId;
        this.description = description;
        this.unit = unit;
        this.dataSourceId = dataSourceId;
        this.sensorObjectNodeId = sensorObjectNodeId;
        this.nodeType = nodeType;
        this.dataType = dataType;
        this.nodeDomain = nodeDomain;
        this.readable = readable;
        this.writeable = writeable;
        this.interval = interval;
        this.updated = updated;
        this.lastRun = lastRun;
        this.status = status;
        this.sensorLog = sensorLog;
        this.configured = configured;

        this.lastValue = lastValue;

        //this.nameAlias = nameAlias;
    }
    public UnConfiguredDataIo()
    {

    }
    public UnConfiguredDataIo(ResultSet rs)
    {
        //TODO : fill out if nessecary
    }
    //not to be used, the data should only be inserted in the robots
    public Status Insert(Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        String[] insertFields = getFields();
        String[] insertValues = getValuesParameters();
        Map parameters = null;

        insertFields = getFields();
        insertValues = getValuesParameters();

        if ((this.updatedString != null) && (!this.updatedString.isEmpty()))
        {
            this.updated = LocalDateTime.parse(updatedString, dateTimeFormatter);
        }

        parameters = new HashMap();
        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_unit, this.unit);
        parameters.put("@"+f_dataSourceId, this.dataSourceId);
        parameters.put("@"+f_description, this.description);
        parameters.put("@"+f_nodeType, this.nodeType);
        parameters.put("@"+f_sensorObjectId, this.sensorObjectId);
        parameters.put("@"+f_sensorObjectNodeId, this.sensorObjectNodeId);
        parameters.put("@"+f_dataType, this.dataType);
        parameters.put("@"+f_readable, this.readable);
        parameters.put("@"+f_writeable, this.writeable);
        parameters.put("@"+f_interval, this.interval);
        parameters.put("@"+f_updated, this.updated);
        parameters.put("@"+f_lastRun, this.lastRun);
        parameters.put("@"+f_status, this.status);
        parameters.put("@"+f_sensorLog, this.sensorLog);
        parameters.put("@"+f_nodeDomain, this.nodeDomain);
        parameters.put("@"+f_configured, this.configured);

        Integer result = connection.insert(tableName, insertFields, insertValues, parameters, false, null);

        if (result == 1 )
            status.Success = true;
        else {
            status.Message = "no rows inserted";
            status.Error = connection.Status.Error;
        }
        return status;
    }
    public static Status UpdateConfigured(Integer id, Boolean configured, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        String[] updateFields = getUpdateConfiguredFields();
        String[] updateValues = getUpdateConfiguredValuesParameters();
        Map parameters = null;
        parameters = new HashMap();

        parameters.put("@"+f_id, id);
        parameters.put("@"+f_configured, configured);

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

    // Update name alias
    public static Status UpdateNameAlias(Integer id, String nameAlias, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        String[] updateFields = getUpdateNameAliasFields();
        String[] updateValues = getUpdateNameAliasValuesParameters();
        Map parameters = null;
        parameters = new HashMap();

        parameters.put("@"+f_id, id);
        parameters.put("@"+f_nameAlias, nameAlias);

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

    //not to be used, the data should only be updated in the robots
    public Status Update(Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        String[] updateFields = getUpdateFields();
        String[] updateValues = getUpdateValuesParameters();
        Map parameters = null;
        parameters = new HashMap();

        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_unit, this.unit);
        parameters.put("@"+f_dataSourceId, this.dataSourceId);
        parameters.put("@"+f_description, this.description);
        parameters.put("@"+f_nodeType, this.nodeType);
        parameters.put("@"+f_sensorObjectId, this.sensorObjectId);
        parameters.put("@"+f_sensorObjectNodeId, this.sensorObjectNodeId);
        parameters.put("@"+f_dataType, this.dataType);
        parameters.put("@"+f_readable, this.readable);
        parameters.put("@"+f_writeable, this.writeable);
        parameters.put("@"+f_interval, this.interval);
        parameters.put("@"+f_updated, this.updated);
        parameters.put("@"+f_lastRun, this.lastRun);
        parameters.put("@"+f_status, this.status);
        parameters.put("@"+f_sensorLog, this.sensorLog);
        parameters.put("@"+f_nodeDomain, this.nodeDomain);
        parameters.put("@"+f_configured, this.configured);



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

    // not used, use instead GetUnConfiguredDataIoWithSensorObjectData
    public static UnConfiguredDataIo GetUnConfiguredDataIo(Integer id, Connection connection ) throws Exception
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
        List<UnConfiguredDataIo> configuredDataIos = GetUnConfiguredDataIosFromResultset(rs);
        if (configuredDataIos.size() >= 1) {
            UnConfiguredDataIo configuredDataIo = (UnConfiguredDataIo) (configuredDataIos.toArray())[0];
            return configuredDataIo;
        }
        return  null;
    }

    public static UnConfiguredDataIo GetUnConfiguredDataIoWithSensorObjectData(Integer id, Connection connection ) throws Exception
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

            String selectString = buildSelectString(id);
            rs = connection.SelectByString(selectString, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<UnConfiguredDataIo> configuredDataIos = GetUnConfiguredDataIosFromResultset(rs);
        if (configuredDataIos.size() >= 1) {
            UnConfiguredDataIo configuredDataIo = (UnConfiguredDataIo) (configuredDataIos.toArray())[0];
            return configuredDataIo;
        }
        return  null;
    }



    private static String buildSelectString(Integer id)
    {
        StringBuilder stringBuilder = buildSelectStringFields();

        if (id > -1)
        {
            String whereClause = " WHERE dataIO."+f_id+ " = " + id.toString();
            stringBuilder.append(whereClause);
        }

        return stringBuilder.toString();
    }

    private static StringBuilder buildSelectStringFields() {
        StringBuilder stringBuilder = new StringBuilder("SELECT dataIO.*, so.description AS "+ f_sensorObjectDescription + ", so.name AS "+ f_sensorObjectName + ", ds.name AS " + f_dataSourceName + " FROM " + tableName +" AS dataIO " );
        stringBuilder.append( " LEFT JOIN " + SensorObject.tableName + " AS so ON dataIO."+ f_sensorObjectId + " = so." + SensorObject.f_id);
        stringBuilder.append( " LEFT JOIN " + DataSource.tableName + " AS ds ON dataIO."+ f_dataSourceId + " = ds." + DataSource.f_id);
        return stringBuilder;
    }

    private static String buildArraySelectString(String[] queryIdsArray)
    {
        StringBuilder stringBuilder = buildSelectStringFields();
        String whereClause = " WHERE dataIO."+f_id+ " in (" + String.join(",", queryIdsArray) + ")";

        stringBuilder.append(whereClause);

        return stringBuilder.toString();
    }


    private static String[] getUpdateConfiguredFields() {
        return new String[]{ f_configured};
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
    private static String[] getUpdateConfiguredValuesParameters() {
        return new String[]{ "@"+f_configured};
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

    // Name alias fields
    private static String[] getUpdateNameAliasFields() {
        return new String[]{ f_nameAlias};
    }
    private static String[] getUpdateNameAliasValuesParameters() {
        return new String[]{ "@"+f_nameAlias };
    }

    private static String[] getFields() {
        return new String[]{f_id, f_name, f_description, f_unit, f_sensorObjectId, f_sensorObjectNodeId, f_nodeDomain,
                f_dataSourceId, f_nodeType, f_dataType, f_readable, f_writeable, f_interval, f_updated,
                f_lastRun, f_status, f_sensorLog, f_configured, f_lastValue, f_nameAlias};
    }

    private static String[] getFieldsWithSensorObjectData() {
        return new String[]{f_id, f_name, f_description, f_unit, f_sensorObjectId, f_sensorObjectNodeId, f_nodeDomain,
                f_dataSourceId, f_nodeType, f_dataType, f_readable, f_writeable, f_interval, f_updated,
                f_lastRun, f_status, f_sensorLog, f_configured, f_sensorObjectDescription, f_sensorObjectName, f_dataSourceName, f_lastValue};
    }


    private static String[] getValuesParametersWithSensorObjectData() {
        String[] fields = getFieldsWithSensorObjectData();
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

        //return new String[]{"@"+f_id, "@"+f_name, "@"+f_description, "@"+f_unit, "@"+f_sensorObjectId, "@"+f_sensorObjectNodeId, "@"+f_nodeDomain, "@"+f_dataSourceId, "@"+f_nodeType, "@"+f_dataType, "@"+f_readable, "@"+f_writeable, "@"+f_interval, "@"+f_updated };
    }

    public static List<UnConfiguredDataIo> GetUnConfiguredDataIosWithSensorData(Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        Map parameters = new HashMap();
        ResultSet rs = null;

        try {
            String selectString = buildSelectString(-1);
            rs = connection.SelectByString(selectString, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<UnConfiguredDataIo> configuredDataIos = null;
        try {
            configuredDataIos = GetUnConfiguredDataIosFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  configuredDataIos;
    }

    public static List<UnConfiguredDataIo> GetUnConfiguredDataIosFromArrayWithSensorData(String[] queryIdsArray, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        Map parameters = new HashMap();
        ResultSet rs = null;

        try {
            String selectString = buildArraySelectString(queryIdsArray);
            rs = connection.SelectByString(selectString, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<UnConfiguredDataIo> configuredDataIos = null;
        try {
            configuredDataIos = GetUnConfiguredDataIosFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  configuredDataIos;
    }

    // not used, use instead GetUnConfiguredDataIosWithSensorData
    public static List<UnConfiguredDataIo> GetUnConfiguredDataIos(Connection connection ) throws Exception
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
        List<UnConfiguredDataIo> configuredDataIos = null;
        try {
            configuredDataIos = GetUnConfiguredDataIosFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  configuredDataIos;
    }

    public static List<UnConfiguredDataIo> GetUnConfiguredDataIosFromResultset(ResultSet resultSet) throws Exception
    {
        List<UnConfiguredDataIo> unConfiguredDataIos = new ArrayList<UnConfiguredDataIo>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                UnConfiguredDataIo unConfiguredDataIo = new UnConfiguredDataIo();
                unConfiguredDataIo.id = resultSet.getInt(f_id);
                unConfiguredDataIo.name = resultSet.getString(f_name);
                unConfiguredDataIo.unit = resultSet.getString(f_unit);
                unConfiguredDataIo.description = resultSet.getString(f_description);
                unConfiguredDataIo.interval = resultSet.getInt(f_interval);
                unConfiguredDataIo.dataSourceId = resultSet.getInt(f_dataSourceId);
                unConfiguredDataIo.sensorObjectId = resultSet.getString(f_sensorObjectId);
                unConfiguredDataIo.sensorObjectNodeId = resultSet.getString(f_sensorObjectNodeId);
                unConfiguredDataIo.dataType = resultSet.getString(f_dataType);
                unConfiguredDataIo.nodeType = resultSet.getString(f_nodeType);
                unConfiguredDataIo.nodeDomain = resultSet.getString(f_nodeDomain);
                unConfiguredDataIo.readable = resultSet.getBoolean(f_readable);
                unConfiguredDataIo.writeable = resultSet.getBoolean(f_writeable);
                unConfiguredDataIo.lastRun = resultSet.getTimestamp(f_lastRun);
                unConfiguredDataIo.status = resultSet.getString(f_status);
                unConfiguredDataIo.sensorLog = resultSet.getBoolean(f_sensorLog);
                unConfiguredDataIo.configured = resultSet.getBoolean(f_configured);
                unConfiguredDataIo.sensorObjectDescription = resultSet.getString(f_sensorObjectDescription);
                unConfiguredDataIo.dataSourceName = resultSet.getString(f_dataSourceName);
                unConfiguredDataIo.sensorObjectName = resultSet.getString(f_sensorObjectName);
                Timestamp time = resultSet.getTimestamp(f_updated);
                if (time != null)
                    unConfiguredDataIo.updated = time.toLocalDateTime();
                unConfiguredDataIos.add(unConfiguredDataIo);

                unConfiguredDataIo.lastValue = resultSet.getString(f_lastValue);

                unConfiguredDataIo.nameAlias = resultSet.getString(f_nameAlias);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return unConfiguredDataIos;
    }

}
