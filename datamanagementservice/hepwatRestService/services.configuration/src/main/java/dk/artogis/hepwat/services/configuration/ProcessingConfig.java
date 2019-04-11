package dk.artogis.hepwat.services.configuration;

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

public class ProcessingConfig implements Serializable
{
    @JsonIgnore
    private static String tableName = "services_processing_config";
    private Integer aggType ;
    private Integer calcType ;
    private String topic;
    private String collection;
    private Integer store;
    private Integer dataType;

    private static String f_aggType = "agg_type";
    private static String f_calcType = "calc_type";
    private static String f_topic = "topic";
    private static String f_collection = "collection";
    private static String f_store = "store";
    private static String f_dataType = "data_type";

    //region getters and setters


    public Integer getAggType() {
        return aggType;
    }

    public void setAggType(Integer aggType) {
        this.aggType = aggType;
    }

    public Integer getCalcType() {
        return calcType;
    }

    public void setCalcType(Integer calcType) {
        this.calcType = calcType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    //endregion getters and setters
    public ProcessingConfig(Integer  id, String name, Integer minutes)
    {

    }
    public ProcessingConfig()
    {

    }
    public ProcessingConfig(ResultSet rs)
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

        parameters.put("@"+f_aggType, this.aggType);
        parameters.put("@"+f_calcType, this.calcType);
        parameters.put("@"+f_topic, this.topic);
        parameters.put("@"+f_collection, this.collection);
        parameters.put("@"+f_store, this.store);
        parameters.put("@"+ f_dataType, this.dataType);

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

        parameters.put("@"+f_aggType, this.aggType);
        parameters.put("@"+f_calcType, this.calcType);
        parameters.put("@"+f_topic, this.topic);
        parameters.put("@"+f_collection, this.collection);
        parameters.put("@"+f_store, this.store);
        parameters.put("@"+ f_dataType, this.dataType);

        SqlCriteria[] criterias = new SqlCriteria[3];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_aggType;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_aggType;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_calcType;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_calcType;
        criteria2.CriteriaOperand = "AND";
        criterias[1] = criteria2;
        SqlCriteria criteria3 = new SqlCriteria();
        criteria3.FieldName = f_dataType;
        criteria3.CompareOperator = "=";
        criteria3.FieldValue = "@" + f_dataType;
        criterias[2] = criteria3;

        Integer result = connection.update(tableName, updateFields, updateValues, criterias, parameters);

        if (result == 1 )
            status.Success = true;
        else {
            status.Message = "no rows updated, or only part of records updated";
            status.Error = connection.Status.Error;
        }
        return status;
    }

    public static Status Delete(Integer aggType, Integer calcType, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        Map parameters = new HashMap();
        parameters.put("@"+f_aggType , aggType );
        parameters.put("@"+f_calcType , calcType );


        SqlCriteria[] criterias = new SqlCriteria[3];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_aggType;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_aggType;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_calcType;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_calcType;
        criteria2.CriteriaOperand = "AND";
        criterias[1] = criteria2;
        SqlCriteria criteria3 = new SqlCriteria();
        criteria3.FieldName = f_dataType;
        criteria3.CompareOperator = "=";
        criteria3.FieldValue = "@" + f_dataType;
        criterias[2] = criteria3;

        Integer result = connection.delete(tableName, criterias, parameters);
        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows deleted";
        return status;
    }
    public static ProcessingConfig GetProcessingConfig(Integer aggType, Integer calcType, Integer dataType, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[3];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_aggType;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_aggType;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_calcType;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_calcType;
        criteria2.CriteriaOperand = "AND";
        criterias[1] = criteria2;
        SqlCriteria criteria3 = new SqlCriteria();
        criteria3.FieldName = f_dataType;
        criteria3.CompareOperator = "=";
        criteria3.FieldValue = "@" + f_dataType;
        criterias[2] = criteria3;

        Map parameters = new HashMap();
        parameters.put("@"+f_aggType,aggType);
        parameters.put("@"+f_calcType, calcType);
        parameters.put("@"+f_dataType, dataType);


        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<ProcessingConfig> processingConfigs = GetProcessingConfigsFromResultset(rs);
        if (processingConfigs.size() >= 1) {
            ProcessingConfig processingConfig = (ProcessingConfig) (processingConfigs.toArray())[0];
            return processingConfig;
        }
        return  null;
    }

    public static List<ProcessingConfig> GetProcessingConfigAllAggTypes(Integer calcType, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_calcType;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_calcType;
        criterias[0] = criteria1;

        Map parameters = new HashMap();
        parameters.put("@"+f_calcType, calcType);


        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<ProcessingConfig> processingConfigs = GetProcessingConfigsFromResultset(rs);
        return processingConfigs;
    }

    public static List<ProcessingConfig> GetProcessingConfigAllCalcTypes(Integer aggType, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_aggType;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_aggType;
        criterias[0] = criteria1;

        Map parameters = new HashMap();
        parameters.put("@"+f_aggType,aggType);

        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<ProcessingConfig> processingConfigs = GetProcessingConfigsFromResultset(rs);

        return processingConfigs;

    }
    private static String[] getUpdateFields() {
        return new String[]{f_topic, f_collection, f_store};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{ "@"+f_topic, "@"+f_collection, "@"+f_store};
    }

    private static String[] getFields() {
        return new String[]{f_aggType, f_calcType, f_topic, f_collection, f_store, f_dataType};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_aggType, "@"+f_calcType, "@"+f_topic, "@"+f_collection, "@"+f_store, "@"+ f_dataType};
    }

    public static List<ProcessingConfig> GetProcessingConfigs(Connection connection ) throws Exception
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
        List<ProcessingConfig> processingConfigs = null;
        try {
            processingConfigs = GetProcessingConfigsFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  processingConfigs;
    }

    public static List<ProcessingConfig> GetProcessingConfigsFromResultset(ResultSet resultSet) throws Exception
    {
        List<ProcessingConfig> processingConfigList = new ArrayList<ProcessingConfig>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                ProcessingConfig processingConfig = new ProcessingConfig();
                processingConfig.aggType = resultSet.getInt(f_aggType);
                processingConfig.calcType = resultSet.getInt(f_calcType);
                processingConfig.topic = resultSet.getString(f_topic);
                processingConfig.collection = resultSet.getString(f_collection);
                processingConfig.store = resultSet.getInt(f_store);
                processingConfig.dataType = resultSet.getInt(f_dataType);
                processingConfigList.add(processingConfig);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return processingConfigList;
    }

    public static ArrayList<String> getTopicsForAggType(Integer aggType, ProcessingConfig[] processingConfigs)
    {
        ArrayList<String> topics = new ArrayList<>();
        for (ProcessingConfig processingConfig : processingConfigs)
        {
            String topic = processingConfig.getTopic();
            if ((processingConfig.getAggType() == aggType) && (!topics.contains(topic)))
                topics.add(topic);
        }

        return  topics;
    }
    public static ArrayList<String> getTopicsForAggTypeAndCalcType(Integer aggType, Integer calcType, ProcessingConfig[] processingConfigs)
    {
        ArrayList<String> topics = new ArrayList<>();
        for (ProcessingConfig processingConfig : processingConfigs)
        {
            if ((processingConfig.getAggType() == aggType)&& (processingConfig.getCalcType() == calcType))
                topics.add(processingConfig.getTopic());
        }

        return  topics;
    }
    public static ArrayList<String> getTopicsForAggTypesAndCalcTypes(Integer[] aggTypes, Integer[] calcTypes, ProcessingConfig[] processingConfigs)
    {
        ArrayList<String> topics = new ArrayList<>();
        for (ProcessingConfig processingConfig : processingConfigs)
        {
            if ((isInAggTypes(aggTypes, processingConfig.aggType))&& (isInCalcTypes(calcTypes, processingConfig.calcType)))
                if (!(topics.contains(processingConfig.getTopic())))
                    topics.add(processingConfig.getTopic());
        }

        return  topics;
    }

    public static ProcessingConfig getProcessingConfig(Integer aggType, Integer calcType, Integer dataType, ProcessingConfig[] processingConfigs)
    {

        for (ProcessingConfig processingConfig : processingConfigs)
        {
            if ((processingConfig.getAggType() == aggType)&& (processingConfig.getCalcType() == calcType)&& (processingConfig.getDataType() == dataType))
                return processingConfig;
        }

        return  null;
    }
    public static ProcessingConfig[] getProcessingConfigs(Integer[] aggTypes, Integer[] calcTypes, ProcessingConfig[] processingConfigs)
    {
        ArrayList<ProcessingConfig> processingConfigArrayList = new ArrayList<>();
        for (ProcessingConfig processingConfig : processingConfigs)
        {
            if (( isInAggTypes(aggTypes, processingConfig.aggType ) ) && (isInCalcTypes(calcTypes, processingConfig.calcType)))
                processingConfigArrayList.add(processingConfig);
        }

        return  ( ProcessingConfig[] )processingConfigArrayList.toArray();
    }
    private static boolean isInAggTypes( Integer[] aggTypes, Integer actAggType )
    {
        for (Integer aggType : aggTypes ) {
            if (aggType == actAggType)
                return true;
        }
        return false;
    }
    private static boolean isInCalcTypes( Integer[] calcTypes, Integer actCalcType )
    {
        for (Integer calcType : calcTypes ) {
            if (calcType == actCalcType)
                return true;
        }
        return false;
    }
}
