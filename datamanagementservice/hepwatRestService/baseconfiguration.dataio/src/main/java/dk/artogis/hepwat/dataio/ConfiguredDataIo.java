package dk.artogis.hepwat.dataio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfiguredDataIo implements Serializable
{
    @JsonIgnore
    private static String tableName = "config_data_io";
    private Integer id ;
    //private String name;
    //private String alias;

    private String sensorObjectName;
    private String sensorObjectAlias;

    private String unit;
    private Integer measurementType;
    private Integer templateType;
    private String sensorObjectId;
    private String description;
    private String sensorObjectDescription;
    private String dataSourceName;
    private Integer dataSourceId;
    private String sensorObjectNodeId;
    private String sensorObjectNodeName;
    private Boolean isBatteryStatus;


    private static String f_id = "id";
    //private static String f_name = "name";
    //private static String f_alias = "alias";

    private static String f_sensorObjectName = "sensor_object_name";
    private static String f_sensorObjectAlias = "sensor_object_alias";

    private static String f_unit = "unit";
    private static String f_measurementType = "measurement_type";
    private static String f_templateType = "template_type";
    private static String f_description = "description";

    private static String f_sensorObjectId = "sensor_object_id";
    private static String f_sensorObjectNodeId = "sensor_object_node_id";
    private static String f_dataSourceId = "datasource_id";

    private static String f_sensorObjectDescription = "sensor_object_description";
    private static String f_dataSourceName = "datasource_name";
    private static String f_sensorObjectNodeName = "sensor_object_node_name";
    private static String f_isBatteryStatus = "is_battery_status";

    //region getters and setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /*
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    */

    public String getSensorObjectName() { return sensorObjectName; }
    public void setSensorObjectName(String sensorObjectName) {
        this.sensorObjectName = sensorObjectName;
    }
    public String getSensorObjectAlias() {
        return sensorObjectAlias;
    }
    public void setSensorObjectAlias(String sensorObjectAlias) {
        this.sensorObjectAlias = sensorObjectAlias;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(Integer measurementType) {
        this.measurementType = measurementType;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsBatteryStatus() {
        return isBatteryStatus;
    }
    public void setIsBatteryStatus(Boolean isBatteryStatus) {
        this.isBatteryStatus = isBatteryStatus;
    }

    public String getSensorObjectId() {
        return sensorObjectId;
    }

    public void setSensorObjectId(String sensorObjectId) {
        this.sensorObjectId = sensorObjectId;
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

    public Integer getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getSensorObjectNodeId() {
        return sensorObjectNodeId;
    }

    public void setSensorObjectNodeId(String sensorObjectNodeId) {
        this.sensorObjectNodeId = sensorObjectNodeId;
    }

    public String getSensorObjectNodeName() {
        return sensorObjectNodeName;
    }

    public void setSensorObjectNodeName(String sensorObjectNodeName) {
        this.sensorObjectNodeName = sensorObjectNodeName;
    }

    //endregion getters and setters
    public ConfiguredDataIo(Integer  id, String name, String alias, String unit, Integer measurementType,  Integer templateType, String sensorObjectId, String description, String sensorObjectNodeId, String sensorObjectDescription, String dataSourceName, Integer dataSourceId, String sensorObjectNodeName,   Boolean isBatteryStatus)
    {
        this.id = id;
        this.measurementType = measurementType;

        //this.name = name;
        //this.alias = alias;

        this.sensorObjectName = sensorObjectName;
        this.sensorObjectAlias = sensorObjectAlias;

        this.unit = unit;
        this.templateType = templateType;
        this.sensorObjectId = sensorObjectId;
        this.description = description;
        this.dataSourceId = dataSourceId;
        this.dataSourceName = dataSourceName;
        this.sensorObjectDescription = sensorObjectDescription;
        this.sensorObjectNodeId = sensorObjectNodeId;
        this.sensorObjectNodeName = sensorObjectNodeName;
        this.isBatteryStatus = isBatteryStatus;
    }

    public ConfiguredDataIo()
    {
    }

    public ConfiguredDataIo(ResultSet rs)
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
        parameters = new HashMap();

        parameters.put("@"+f_id, this.id);

        //parameters.put("@"+f_name, this.name);
        //parameters.put("@"+f_alias, this.alias);

        parameters.put("@"+f_sensorObjectName, this.sensorObjectName);
        parameters.put("@"+f_sensorObjectAlias, this.sensorObjectAlias);
        parameters.put("@"+f_unit, this.unit);
        parameters.put("@"+f_measurementType, this.measurementType);
        parameters.put("@"+f_templateType, this.templateType);
        parameters.put("@"+f_sensorObjectId, this.sensorObjectId);
        parameters.put("@"+f_description, this.description);
        parameters.put("@"+f_dataSourceId, this.dataSourceId);
        parameters.put("@"+f_dataSourceName, this.dataSourceName);
        parameters.put("@"+f_sensorObjectNodeId, this.sensorObjectNodeId);
        parameters.put("@"+f_sensorObjectDescription, this.sensorObjectDescription);
        parameters.put("@"+f_sensorObjectNodeName, this.sensorObjectNodeName);
        parameters.put("@"+f_isBatteryStatus, this.isBatteryStatus);

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

        //parameters.put("@"+f_name, this.name);
        //parameters.put("@"+f_alias, this.alias);

        parameters.put("@"+f_sensorObjectName, this.sensorObjectName);
        parameters.put("@"+f_sensorObjectAlias, this.sensorObjectAlias);

        parameters.put("@"+f_unit, this.unit);
        parameters.put("@"+f_measurementType, this.measurementType);
        parameters.put("@"+f_templateType, this.templateType);
        parameters.put("@"+f_sensorObjectId, this.sensorObjectId);
        parameters.put("@"+f_description, this.description);
        parameters.put("@"+f_dataSourceId, this.dataSourceId);
        parameters.put("@"+f_dataSourceName, this.dataSourceName);
        parameters.put("@"+f_sensorObjectNodeId, this.sensorObjectNodeId);
        parameters.put("@"+f_sensorObjectDescription, this.sensorObjectDescription);
        parameters.put("@"+f_sensorObjectNodeName, this.sensorObjectNodeName);
        parameters.put("@"+f_isBatteryStatus, this.isBatteryStatus);

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

    public static Status Delete(Integer id, Connection connection)
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

    public static ConfiguredDataIo GetConfiguredDataIo(Integer id, Connection connection ) throws Exception
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
        List<ConfiguredDataIo> configuredDataIos = GetConfiguredDataIosFromResultset(rs);
        if (configuredDataIos.size() >= 1) {
            ConfiguredDataIo configuredDataIo = (ConfiguredDataIo) (configuredDataIos.toArray())[0];
            return configuredDataIo;
        }
        return  null;
    }

    private static String[] getUpdateFields() {
        //return new String[]{ f_name, f_alias, f_unit, f_measurementType, f_templateType, f_sensorObjectId, f_description, f_dataSourceId, f_dataSourceName, f_sensorObjectNodeId, f_sensorObjectDescription, f_isBatteryStatus};
        return new String[]{ f_sensorObjectName, f_sensorObjectAlias, f_unit, f_measurementType, f_templateType, f_sensorObjectId, f_description, f_dataSourceId, f_dataSourceName, f_sensorObjectNodeId, f_sensorObjectDescription, f_sensorObjectNodeName, f_isBatteryStatus};
    }

    private static String[] getUpdateValuesParameters() {
        //return new String[]{ "@"+f_name, "@"+f_alias, "@"+f_unit, "@"+f_measurementType, "@"+f_templateType, "@"+f_sensorObjectId, "@" +f_description, "@" + f_dataSourceId, "@"+f_dataSourceName, "@" + f_sensorObjectNodeId, "@" + f_sensorObjectDescription, "@" + f_isBatteryStatus};
        return new String[]{ "@"+f_sensorObjectName, "@"+f_sensorObjectAlias, "@"+f_unit, "@"+f_measurementType, "@"+f_templateType, "@"+f_sensorObjectId, "@" +f_description, "@" + f_dataSourceId, "@"+f_dataSourceName, "@" + f_sensorObjectNodeId, "@" + f_sensorObjectDescription, "@" + f_sensorObjectNodeName, "@" + f_isBatteryStatus};
    }

    private static String[] getFields() {
        //return new String[]{f_id, f_name, f_alias, f_unit, f_measurementType, f_templateType, f_sensorObjectId, f_description, f_dataSourceId, f_dataSourceName, f_sensorObjectNodeId, f_sensorObjectDescription, f_isBatteryStatus};
        return new String[]{f_id, f_sensorObjectName, f_sensorObjectAlias, f_unit, f_measurementType, f_templateType, f_sensorObjectId, f_description, f_dataSourceId, f_dataSourceName, f_sensorObjectNodeId, f_sensorObjectDescription, f_sensorObjectNodeName, f_isBatteryStatus};
    }

    private static String[] getValuesParameters() {
        //return new String[]{"@"+f_id, "@"+f_name, "@"+f_alias, "@"+f_unit, "@"+f_measurementType, "@"+f_templateType,  "@"+f_sensorObjectId, "@"+f_description,"@" + f_dataSourceId, "@"+f_dataSourceName, "@" + f_sensorObjectNodeId, "@" + f_sensorObjectDescription, "@" + f_isBatteryStatus};
        return new String[]{"@"+f_id, "@"+f_sensorObjectName, "@"+f_sensorObjectAlias, "@"+f_unit, "@"+f_measurementType, "@"+f_templateType,  "@"+f_sensorObjectId, "@"+f_description,"@" + f_dataSourceId, "@"+f_dataSourceName, "@" + f_sensorObjectNodeId, "@" + f_sensorObjectDescription, "@" + f_sensorObjectNodeName,"@" + f_isBatteryStatus };
    }

    // Get all
    public static List<ConfiguredDataIo> GetConfiguredDataIos(Connection connection ) throws Exception
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
        List<ConfiguredDataIo> configuredDataIos = null;
        try {
            configuredDataIos = GetConfiguredDataIosFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  configuredDataIos;
    }

    // Get array of ID's
    public static List<ConfiguredDataIo> GetConfiguredDataIosFromArray(String idArrayString, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        Map parameters = new HashMap();
        ResultSet rs = null;

        try {
            String selectString = buildArraySelectString(idArrayString);
            rs = connection.SelectByString(selectString, 0);

            //rs = connection.select(tableNames, fields, null, null, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<ConfiguredDataIo> configuredDataIos = null;
        try {
            configuredDataIos = GetConfiguredDataIosFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  configuredDataIos;
    }

    private static String buildArraySelectString(String idArrayString)
    {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM " + tableName + " WHERE " + f_id +  " in (" + idArrayString + ")" );
        //System.out.println("buildArraySelectString - string: " + stringBuilder.toString());

        return stringBuilder.toString();
    }

    public static List<ConfiguredDataIo> GetConfiguredDataIosFromResultset(ResultSet resultSet) throws Exception
    {
        List<ConfiguredDataIo> configuredDataIos = new ArrayList<ConfiguredDataIo>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                ConfiguredDataIo configuredDataIo = new ConfiguredDataIo();
                configuredDataIo.id = resultSet.getInt(f_id);

                //configuredDataIo.name = resultSet.getString(f_name);
                //configuredDataIo.alias = resultSet.getString(f_alias);

                configuredDataIo.sensorObjectName = resultSet.getString(f_sensorObjectName);
                configuredDataIo.sensorObjectAlias = resultSet.getString(f_sensorObjectAlias);

                configuredDataIo.unit = resultSet.getString(f_unit);
                configuredDataIo.measurementType = resultSet.getInt(f_measurementType);
                configuredDataIo.templateType = resultSet.getInt(f_templateType);
                configuredDataIo.sensorObjectId = resultSet.getString(f_sensorObjectId);
                configuredDataIo.description = resultSet.getString(f_description);
                configuredDataIo.dataSourceName = resultSet.getString(f_dataSourceName);
                configuredDataIo.sensorObjectDescription = resultSet.getString(f_sensorObjectDescription);
                configuredDataIo.sensorObjectNodeId = resultSet.getString(f_sensorObjectNodeId);
                configuredDataIo.dataSourceId = resultSet.getInt(f_dataSourceId);
                try
                {
                configuredDataIo.sensorObjectNodeName = resultSet.getString(f_sensorObjectNodeName);
                }
                catch (SQLException ex)
                {
                    configuredDataIo.sensorObjectNodeName ="";
                }
                configuredDataIo.isBatteryStatus = resultSet.getBoolean(f_isBatteryStatus);
                //System.out.println("isBatteryStatus: " + resultSet.getBoolean(f_isBatteryStatus));
                configuredDataIos.add(configuredDataIo);

            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return configuredDataIos;
    }

}
