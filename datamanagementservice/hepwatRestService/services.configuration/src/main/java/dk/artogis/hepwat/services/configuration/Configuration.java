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

public class Configuration implements Serializable
{
    @JsonIgnore
    private static String tableName = "services_configuration";
    private Integer serviceId ;
    private String description;
    private String name;
    private String type ;
    private String kafkaBroker;
    private String clientId;
    private String topicGroupId;
    private Boolean seektoEnd;
    private String autoOffSet;
    private Integer[] calculationTypes;
    private String calculationTypesString;
    private Integer[] aggregationTypes;
    private String aggregationTypesString;
    private String outputTopic;
    private String stateTopic;
    private Integer mongoDbDataStoreId;
    private Integer serviceCalcType;
    private Integer serviceAggType;
    private String inputTopic;
    private String stateDir;
    private Integer commitInterval;
    @JsonIgnore
    private Integer aggregationInterval;
    private Boolean skipFilter;
    private Integer recycleDataInterval;

    //region fieldnames
    private static String f_serviceId = "service_id";
    private static String f_name = "name";
    private static String f_type = "type";
    private static String f_description = "description";
    private static String f_kafkaBroker = "kafka_broker";
    private static String f_clientId = "client_id";
    private static String f_topicGroupId = "topic_group_id";
    private static String f_seekToEnd ="seek_to_end";
    private static String f_autoOffSet = "auto_offset";
    private static String f_calculationTypesString = "calculation_types";
    private static String f_aggregationTypesString = "aggregation_types";
    private static String f_ouputTopic = "output_topic";
    private static String f_stateTopic = "state_topic";
    private static String f_mongoDbDataStoreId = "mongodb_datastore_id";
    private static String f_serviceCalcType = "service_calctype";
    private static String f_serviceAggType = "service_aggtype";
    private static String f_inputTopic = "input_topic";
    private static String f_stateDir = "state_dir";
    private static String f_commitInterval = "commit_interval";
    private static String f_aggretationInterval = "aggregation_interval";
    private static String f_skipFilter = "skip_filter";
    private static String f_recycleDataInterval = "recycle_data_interval";

    //endregion fieldnames

    //region getters and setters

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKafkaBroker() {
        return kafkaBroker;
    }

    public void setKafkaBroker(String kafkaBroker) {
        this.kafkaBroker = kafkaBroker;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTopicGroupId() {
        return topicGroupId;
    }

    public void setTopicGroupId(String topicGroupId) {
        this.topicGroupId = topicGroupId;
    }

    public Boolean getSeektoEnd() {
        return seektoEnd;
    }

    public void setSeektoEnd(Boolean seektoEnd) {
        this.seektoEnd = seektoEnd;
    }

    public String getAutoOffSet() {
        return autoOffSet;
    }

    public void setAutoOffSet(String autoOffSet) {
        this.autoOffSet = autoOffSet;
    }


    public Integer[] getCalculationTypes() {
        if ((this.calculationTypesString != null) && (!this.calculationTypesString.isEmpty()))
        {
            try {
                String[] calculationTypesStringParts = this.calculationTypesString.split(",");
                if ((calculationTypesStringParts != null) && (calculationTypesStringParts.length > 0)) {
                    ArrayList<Integer> calculationTypeArrayList = new ArrayList<>();
                    for (String calculationTypeString : calculationTypesStringParts) {
                        calculationTypeArrayList.add(Integer.parseInt(calculationTypeString));
                    }
                    this.calculationTypes = new Integer[calculationTypeArrayList.size()];
                    this.calculationTypes = calculationTypeArrayList.toArray( this.calculationTypes);
                }
            }
            catch (Exception ex)
            {
                //TODO: Logging
            }
        }



        return this.calculationTypes;
    }

    public void setCalculationTypes(Integer[] calculationTypes) {
        this.calculationTypes = calculationTypes;
    }

    public String getCalculationTypesString() {
        return calculationTypesString;
    }

    public void setCalculationTypesString(String calculationTypesString) {
        this.calculationTypesString = calculationTypesString;
    }

    public Integer[] getAggregationTypes() {

        if ((this.aggregationTypesString != null) && (!this.aggregationTypesString.isEmpty()))
        {
            try {
                String[] aggregationTypesStringParts = this.aggregationTypesString.split(",");
                if ((aggregationTypesStringParts != null) && (aggregationTypesStringParts.length > 0)) {
                    ArrayList<Integer> aggregationTypeArrayList = new ArrayList<>();
                    for (String aggregationTypeString : aggregationTypesStringParts) {
                        aggregationTypeArrayList.add(Integer.parseInt(aggregationTypeString));
                    }
                    this.aggregationTypes = new Integer[aggregationTypeArrayList.size()];
                    this.aggregationTypes = aggregationTypeArrayList.toArray( this.aggregationTypes);
                }
            }
            catch (Exception ex)
            {
                //TODO: Logging
            }
        }
        return this.aggregationTypes;
    }

    public void setAggregationTypes(Integer[] aggregationTypes) {
        this.aggregationTypes = aggregationTypes;
    }

    public String getAggregationTypesString() {
        return aggregationTypesString;
    }

    public void setAggregationTypesString(String aggregationTypesString) {
        this.aggregationTypesString = aggregationTypesString;
    }

    public String getOutputTopic() {
        return outputTopic;
    }

    public void setOutputTopic(String outputTopic) {
        this.outputTopic = outputTopic;
    }

    public String getStateTopic() {
        return stateTopic;
    }

    public void setStateTopic(String stateTopic) {
        this.stateTopic = stateTopic;
    }

    public Integer getMongoDbDataStoreId() {
        return mongoDbDataStoreId;
    }

    public void setMongoDbDataStoreId(Integer mongoDbDataStoreId) {
        this.mongoDbDataStoreId = mongoDbDataStoreId;
    }

    public Integer getServiceCalcType() {
        return serviceCalcType;
    }

    public void setServiceCalcType(Integer serviceCalcType) {
        this.serviceCalcType = serviceCalcType;
    }

    public Integer getServiceAggType() {
        return serviceAggType;
    }

    public void setServiceAggType(Integer serviceAggType) {
        this.serviceAggType = serviceAggType;
    }

    public String getInputTopic() {
        return inputTopic;
    }

    public void setInputTopic(String inputTopic) {
        this.inputTopic = inputTopic;
    }

    public String getStateDir() {
        return stateDir;
    }

    public void setStateDir(String stateDir) {
        this.stateDir = stateDir;
    }

    public Integer getCommitInterval() {
        return commitInterval;
    }

    public void setCommitInterval(Integer commitInterval) {
        this.commitInterval = commitInterval;
    }

    public Integer getAggregationInterval() {
        return aggregationInterval;
    }

    public void setAggregationInterval(Integer aggregationInterval) {
        this.aggregationInterval = aggregationInterval;
    }

    public Boolean getSkipFilter() {
        return skipFilter;
    }

    public void setSkipFilter(Boolean skipFilter) {
        this.skipFilter = skipFilter;
    }

    public Integer getRecycleDataInterval() {
        return recycleDataInterval;
    }

    public void setRecycleDataInterval(Integer recycleDataInterval) {
        this.recycleDataInterval = recycleDataInterval;
    }
//endregion getters and setters


    public Configuration(Integer  serviceId, String name,  Integer type)
    {
        this.serviceId = serviceId;
        this.name = name;
    }
    public Configuration()
    {}
    public Configuration(ResultSet rs)
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

        insertFields = getUpdateFields();
        insertValues = getUpdateValuesParameters();

        parameters = new HashMap();

        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_description, this.description);
        parameters.put("@"+f_type, this.type);
        parameters.put("@"+f_kafkaBroker, this.kafkaBroker);
        parameters.put("@"+f_clientId, this.clientId);
        parameters.put("@"+f_topicGroupId, this.topicGroupId);
        parameters.put("@"+f_seekToEnd, this.seektoEnd);
        parameters.put("@"+f_autoOffSet, this.autoOffSet);
        parameters.put("@"+f_calculationTypesString, this.calculationTypesString);
        parameters.put("@"+f_aggregationTypesString, this.aggregationTypesString);
        parameters.put("@"+f_ouputTopic, this.outputTopic);
        parameters.put("@"+f_stateTopic, this.stateTopic);
        parameters.put("@"+f_mongoDbDataStoreId, this.mongoDbDataStoreId);
        parameters.put("@"+f_inputTopic, this.inputTopic);
        parameters.put("@"+f_stateDir, this.stateDir);
        parameters.put("@"+f_aggretationInterval, this.aggregationInterval);
        parameters.put("@"+f_commitInterval, this.commitInterval);
        parameters.put("@"+f_serviceAggType, this.serviceAggType);
        parameters.put("@"+ f_serviceCalcType, this.serviceCalcType);
        parameters.put("@"+ f_skipFilter, this.skipFilter);
        parameters.put("@"+ f_recycleDataInterval, this.recycleDataInterval);

        String[] identitycolumns = new String[]{f_serviceId};
        Integer result = connection.insert(tableName, insertFields, insertValues, parameters, true, null);


        if (result == 1 ) {
            status.Success = true;
            this.serviceId = result;
        }
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

        parameters.put("@"+f_serviceId, this.serviceId);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_description, this.description);
        parameters.put("@"+f_type, this.type);
        parameters.put("@"+f_kafkaBroker, this.kafkaBroker);
        parameters.put("@"+f_clientId, this.clientId);
        parameters.put("@"+f_topicGroupId, this.topicGroupId);
        parameters.put("@"+f_seekToEnd, this.seektoEnd);
        parameters.put("@"+f_autoOffSet, this.autoOffSet);
        parameters.put("@"+f_calculationTypesString, this.calculationTypesString);
        parameters.put("@"+f_aggregationTypesString, this.aggregationTypesString);
        parameters.put("@"+f_ouputTopic, this.outputTopic);
        parameters.put("@"+f_stateTopic, this.stateTopic);
        parameters.put("@"+f_mongoDbDataStoreId, this.mongoDbDataStoreId);
        parameters.put("@"+f_inputTopic, this.inputTopic);
        parameters.put("@"+f_stateDir, this.stateDir);
        parameters.put("@"+f_aggretationInterval, this.aggregationInterval);
        parameters.put("@"+f_commitInterval, this.commitInterval);
        parameters.put("@"+f_serviceAggType, this.serviceAggType);
        parameters.put("@"+ f_serviceCalcType, this.serviceCalcType);
        parameters.put("@"+ f_skipFilter, this.skipFilter);
        parameters.put("@"+ f_recycleDataInterval, this.recycleDataInterval);

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_serviceId;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_serviceId;
        criterias[0] = criteria1;

        Integer result = connection.update(tableName, updateFields, updateValues, criterias, parameters);

        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows updated, or only part of records updated";
        return status;
    }

    public static Status Delete(Integer serviceId, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        Map parameters = new HashMap();
        parameters.put("@"+f_serviceId, serviceId);

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_serviceId;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@"+f_serviceId;

        criterias[0] = criteria1;

        Integer result = connection.delete(tableName, criterias, parameters);
        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows deleted";
        return status;
    }

    public static Configuration GetConfiguration(Integer serviceId, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria aCriteria = new SqlCriteria();
        aCriteria.FieldName = f_serviceId;
        aCriteria.CompareOperator = "=";
        aCriteria.FieldValue = "@f_serviceId";
        SqlCriteria[] criterias = new SqlCriteria[1];
        criterias[0] = aCriteria;

        Map parameters = new HashMap();
        parameters.put("@f_serviceId", serviceId);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<Configuration> configurations = GetConfigurationsFromResultset(rs);
        if (configurations.size() >= 1) {
            Configuration configuration = (Configuration) (configurations.toArray())[0];
            return configuration;
        }
        return  null;
    }

    private static String[] getUpdateFields() {
        return new String[]{f_description, f_name, f_type, f_kafkaBroker, f_clientId, f_topicGroupId, f_seekToEnd, f_autoOffSet, f_calculationTypesString, f_aggregationTypesString, f_ouputTopic, f_stateTopic, f_mongoDbDataStoreId, f_inputTopic, f_stateDir, f_aggretationInterval, f_commitInterval, f_serviceAggType, f_serviceCalcType, f_skipFilter, f_recycleDataInterval };
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{ "@"+f_description, "@"+f_name, "@"+f_type, "@"+f_kafkaBroker, "@"+f_clientId, "@"+f_topicGroupId, "@"+f_seekToEnd, "@"+f_autoOffSet,  "@"+f_calculationTypesString, "@"+f_aggregationTypesString, "@"+f_ouputTopic, "@"+f_stateTopic, "@"+f_mongoDbDataStoreId, "@"+f_inputTopic, "@"+f_stateDir, "@"+f_aggretationInterval, "@"+f_commitInterval, "@"+f_serviceAggType, "@"+f_serviceCalcType, "@"+f_skipFilter , "@"+f_recycleDataInterval};
    }

    private static String[] getFields() {
        return new String[]{f_serviceId, f_description, f_name, f_type, f_kafkaBroker, f_clientId, f_topicGroupId, f_seekToEnd, f_autoOffSet, f_calculationTypesString, f_aggregationTypesString, f_ouputTopic, f_stateTopic, f_mongoDbDataStoreId,f_inputTopic, f_stateDir, f_aggretationInterval, f_commitInterval, f_serviceAggType, f_serviceCalcType, f_skipFilter , f_recycleDataInterval };
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_serviceId, "@"+f_description, "@"+f_name, "@"+f_type, "@"+f_kafkaBroker, "@"+f_clientId, "@"+f_topicGroupId, "@"+f_seekToEnd, "@"+f_autoOffSet,  "@"+f_calculationTypesString, "@"+f_aggregationTypesString, "@"+f_ouputTopic, "@"+f_stateTopic, "@"+f_mongoDbDataStoreId, "@"+f_inputTopic, "@"+f_stateDir, "@"+f_aggretationInterval, "@"+f_commitInterval, "@"+f_serviceAggType, "@"+f_serviceCalcType, "@"+f_skipFilter, "@"+f_recycleDataInterval};
    }
    public static List<Configuration> GetConfigurations(Connection connection ) throws Exception
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
        List<Configuration> configurations = null;
        try {
            configurations = GetConfigurationsFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage());
        }

        return  configurations;
    }

    public static List<Configuration> GetConfigurationsFromResultset(ResultSet resultSet) throws Exception
    {
        List<Configuration> configurations = new ArrayList<Configuration>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                Configuration configuration = new Configuration();
                configuration.serviceId = resultSet.getInt(f_serviceId);
                configuration.description = resultSet.getString(f_description);
                configuration.name = resultSet.getString(f_name);
                configuration.type = resultSet.getString(f_description);
                configuration.kafkaBroker = resultSet.getString(f_kafkaBroker);
                configuration.clientId = resultSet.getString(f_clientId);
                configuration.topicGroupId = resultSet.getString(f_topicGroupId);
                configuration.seektoEnd = resultSet.getBoolean(f_seekToEnd);
                configuration.autoOffSet = resultSet.getString(f_autoOffSet);
                configuration.calculationTypesString = resultSet.getString(f_calculationTypesString);
                configuration.aggregationTypesString = resultSet.getString(f_aggregationTypesString);
                configuration.outputTopic = resultSet.getString(f_ouputTopic);
                configuration.stateTopic = resultSet.getString(f_stateTopic);
                configuration.mongoDbDataStoreId = resultSet.getInt(f_mongoDbDataStoreId);
                configuration.inputTopic = resultSet.getString(f_inputTopic);
                configuration.stateDir =resultSet.getString(f_stateDir);
                configuration.commitInterval = resultSet.getInt(f_commitInterval);
                configuration.aggregationInterval = resultSet.getInt(f_aggretationInterval);
                configuration.serviceAggType = resultSet.getInt(f_serviceAggType);
                configuration.serviceCalcType = resultSet.getInt(f_serviceCalcType);
                configuration.skipFilter = resultSet.getBoolean(f_skipFilter);
                configuration.recycleDataInterval = resultSet.getInt(f_recycleDataInterval);

                //unpack calculationtypes
                if ((configuration.calculationTypesString != null) && (!configuration.calculationTypesString.isEmpty()))
                {
                    try {
                        String[] calculationTypesStringParts = configuration.calculationTypesString.split(",");
                        if ((calculationTypesStringParts != null) && (calculationTypesStringParts.length > 0)) {
                            ArrayList<Integer> calculationTypeArrayList = new ArrayList<>();
                            for (String calculationTypeString : calculationTypesStringParts) {
                                calculationTypeArrayList.add(Integer.parseInt(calculationTypeString));
                            }
                            configuration.calculationTypes = new Integer[calculationTypeArrayList.size()];
                            configuration.calculationTypes = calculationTypeArrayList.toArray( configuration.calculationTypes);
                        }
                    }
                    catch (Exception ex)
                    {
                        //TODO: Logging
                    }
                }
                if ((configuration.aggregationTypesString != null) && (!configuration.aggregationTypesString.isEmpty()))
                {
                    try {
                        String[] aggregationTypesParts = configuration.aggregationTypesString.split(",");
                        if ((aggregationTypesParts != null) && (aggregationTypesParts.length > 0)) {
                            ArrayList<Integer> aggregationTypeArrayList = new ArrayList<>();
                            for (String calculationTypeString : aggregationTypesParts) {
                                aggregationTypeArrayList.add(Integer.parseInt(calculationTypeString));
                            }
                            configuration.aggregationTypes = new Integer[aggregationTypeArrayList.size()];
                            configuration.aggregationTypes = aggregationTypeArrayList.toArray( configuration.aggregationTypes);
                        }
                    }
                    catch (Exception ex)
                    {
                        //TODO: Logging
                    }
                }

                configurations.add(configuration);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return configurations;
    }


}
