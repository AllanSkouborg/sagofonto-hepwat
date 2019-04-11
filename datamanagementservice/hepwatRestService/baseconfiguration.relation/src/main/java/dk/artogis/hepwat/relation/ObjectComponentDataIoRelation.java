package dk.artogis.hepwat.relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.object.KeyDescription;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ObjectComponentDataIoRelation implements Serializable
{
    @JsonIgnore
    public static Integer TypeNone = 0;
    @JsonIgnore
    public static Integer TypeObjectDataIo = 1;
    @JsonIgnore
    public static Integer TypeObjectComponent = 2;
    @JsonIgnore
    public static Integer TypeComponentDataIo = 3;


    @JsonIgnore
    private static String tableName = "config_object_component_data_io_relation";
    private Integer id ;
    private UUID relationId;
    private Integer relationType ;
    @JsonIgnore
    private LocalDateTime createTime;
    private String createTimeString;
    @JsonIgnore
    private LocalDateTime endTime;
    private String endTimeString;

    @JsonIgnore
    private String objectKeyString;
    private KeyDescription[] objectKeys;
    private Integer objectType;

    private Integer componentType;
    private Integer componentKey;
    private Integer dataIoKey;
    private Integer dataIoType;

    @JsonIgnore
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static String f_id = "id";
    private static String f_relationId = "relation_id";
    private static String f_relationType = "relation_type";
    private static String f_objectType = "object_type";
    private static String f_objectKey = "object_key";
    private static String f_componentType = "component_type";
    private static String f_componentKey = "component_key";
    private static String f_dataIoType = "data_io_type";
    private static String f_dataIoKey = "data_io_key";
    private static String f_createTime = "createtime";
    private static String f_endTime = "endtime";


    //region getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getRelationId() {
        return relationId;
    }

    public void setRelationId(UUID relationId) {
        this.relationId = relationId;
    }

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeString() {
        return createTimeString;
    }

    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = createTimeString;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    public String getObjectKeyString() {
        return objectKeyString;
    }

    public void setObjectKeyString(String objectKeyString) {
        this.objectKeyString = objectKeyString;
    }

    public KeyDescription[] getObjectKeys() {
        return objectKeys;
    }

    public void setObjectKeys(KeyDescription[] objectKeys) {
        this.objectKeys = objectKeys;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public Integer getComponentType() {
        return componentType;
    }

    public void setComponentType(Integer componentType) {
        this.componentType = componentType;
    }

    public Integer getComponentKey() {
        return componentKey;
    }

    public void setComponentKey(Integer componentKey) {
        this.componentKey = componentKey;
    }

    public Integer getDataIoKey() {
        return dataIoKey;
    }

    public void setDataIoKey(Integer dataIoKey) {
        this.dataIoKey = dataIoKey;
    }

    public Integer getDataIoType() {
        return dataIoType;
    }

    public void setDataIoType(Integer dataIoType) {
        this.dataIoType = dataIoType;
    }


    //endregion getters and setters

    public ObjectComponentDataIoRelation(Integer  id, UUID relationId, Integer objectType, String objectKeyString, KeyDescription[] objectKeys, Integer relationType, Integer componentType, Integer componentKey, Integer dataIoKey, Integer dataIoType, String createTimeString, String endTimeString)
    {
        if (createTimeString == null) {
            createTime = LocalDateTime.now();
        }
        else createTime = LocalDateTime.parse(createTimeString, dateTimeFormatter);
        if (endTimeString == null) {
            endTime = null;
        }
        else endTime = LocalDateTime.parse(endTimeString, dateTimeFormatter);

        this.id = id;
        this.relationId = relationId;
        this.objectType = objectType;
        this.objectKeyString = objectKeyString;
        this.objectKeys = objectKeys;
        this.relationType = relationType;
        this.componentKey = componentKey;
        this.componentType = componentType;
        this.dataIoKey = dataIoKey;
        this.dataIoType = dataIoType;
    }
    public ObjectComponentDataIoRelation()
    {
        createTime = LocalDateTime.now();
        createTimeString = createTime.format(dateTimeFormatter);
    }
    public ObjectComponentDataIoRelation(ResultSet rs)
    {
        //TODO : fill out if nessecary
    }
    public Status Insert(Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        String[] insertFields = null;
        String[] insertValues = null;
        Map parameters = null;

        insertFields = getInsertFields();
        insertValues = getInsertValuesParameters();
        parameters = new HashMap();

        if (objectKeys != null) {
            if (!ObjectKeysToJson()) return status;
        }
        relationType = ObjectComponentDataIoRelation.TypeNone;
        if (objectKeys != null)
        {
            if (componentKey != null)
                relationType = ObjectComponentDataIoRelation.TypeObjectComponent;
            else if  (dataIoKey != null)
                relationType = ObjectComponentDataIoRelation.TypeObjectDataIo;
        }
        else if ((componentKey != null) && (dataIoKey != null))
            relationType = ObjectComponentDataIoRelation.TypeComponentDataIo;



        if (this.relationId == null)
            this.setRelationId(UUID.randomUUID());
        parameters.put("@" + f_relationId, this.relationId);
        parameters.put("@"+f_relationType, this.relationType);
        parameters.put("@"+f_objectKey, this.objectKeyString);
        parameters.put("@"+f_objectType, this.objectType);
        parameters.put("@"+f_componentKey, this.componentKey);
        parameters.put("@"+f_componentType, this.componentType);
        parameters.put("@"+f_dataIoKey, this.dataIoKey);
        parameters.put("@"+f_dataIoType, this.dataIoType);


        LocalDateTime createTime = LocalDateTime.parse(createTimeString, dateTimeFormatter);
        parameters.put("@"+f_createTime, createTime);

        LocalDateTime endTime = null;
        if (endTimeString != null) {
            endTime = LocalDateTime.parse(endTimeString, dateTimeFormatter);
        }
        parameters.put("@" + f_endTime, endTime);

        String[] identitycolumns = new String[]{f_id};

        Integer result = connection.insert(tableName, insertFields, insertValues, parameters, true, identitycolumns);

        if (result >= 0 ) {
            status.Success = true;
            this.id = result;
        }
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

        updateFields = getUpdateFields();
        updateValues = getUpdateValuesParameters();
        parameters = new HashMap();

        if (objectKeys != null) {
            if (!ObjectKeysToJson()) return status;
        }

        relationType = ObjectComponentDataIoRelation.TypeNone;
        if (objectKeys != null)
        {
            if (componentKey != null)
                relationType = ObjectComponentDataIoRelation.TypeObjectComponent;
            else if  (dataIoKey != null)
                relationType = ObjectComponentDataIoRelation.TypeObjectDataIo;
        }
        else if ((componentKey != null) && (dataIoKey != null))
            relationType = ObjectComponentDataIoRelation.TypeComponentDataIo;


        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_relationId, this.relationId);
        parameters.put("@"+f_relationType, this.relationType);
        parameters.put("@"+f_objectKey, this.objectKeyString);
        parameters.put("@"+f_objectType, this.objectType);
        parameters.put("@"+f_componentKey, this.componentKey);
        parameters.put("@"+f_componentType, this.componentType);
        parameters.put("@"+f_dataIoKey, this.dataIoKey);
        parameters.put("@"+f_dataIoType, this.dataIoType);

//        LocalDateTime createTime = LocalDateTime.parse(createTimeString, dateTimeFormatter);
//        parameters.put("@"+f_createTime, createTime);

        LocalDateTime endTime = null;
        if ((endTimeString != null) && (!endTimeString.isEmpty()))
            endTime = LocalDateTime.parse(endTimeString, dateTimeFormatter);
        parameters.put("@"+f_endTime, endTime);

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
    public static Status UpdateEndTime(UUID id, String endTimeString, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        String[] updateFields = new String[]{f_endTime};
        String[] updateValues =   new String[]{"@"+f_endTime};

        Map parameters = null;

        parameters = new HashMap();

        parameters.put("@"+f_relationId, id);
        LocalDateTime endTime = LocalDateTime.parse(endTimeString, dateTimeFormatter);
        parameters.put("@"+f_endTime, endTime);


        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_relationId;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_relationId;
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
    public static Status DeleteDataIoRelation(Integer dataIoId, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        Map parameters = new HashMap();
        parameters.put("@"+f_dataIoKey, dataIoId);

        SqlCriteria[] criterias = new SqlCriteria[2];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_dataIoKey;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@"+f_dataIoKey;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_endTime;
        criteria2.CompareOperator = "is";
        criteria2.FieldValue = "null";
        criterias[1] = criteria2;

        Integer result = connection.delete(tableName, criterias, parameters);
        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows deleted";
        return status;
    }
    public static Status DeleteComponentRelation(Integer componentId, Integer componentType,  Connection connection)
    {
        Status status = new Status();
        status.Success = false;

        SqlCriteria[] criterias = new SqlCriteria[2];

        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_componentKey;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_componentKey;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_componentType;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_componentType;
        criterias[1] = criteria2;

        Map parameters = new HashMap();
        parameters.put("@" + f_componentKey, componentId);
        parameters.put("@" + f_componentType, componentType);
        ResultSet rs = null;

        Integer result = connection.delete(tableName, criterias, parameters);
        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows deleted";
        return status;
    }

    public static Status DeleteObjectRelation(KeyDescription keyDescription, Integer objectType,  Connection connection)
    {
        Status status = new Status();
        status.Success = false;

        SqlCriteria[] criterias = new SqlCriteria[2];

        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_objectKey;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_objectKey;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;

        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_objectType;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_objectType;
        criterias[1] = criteria2;

        Map parameters = new HashMap();
        parameters.put("@" + f_objectKey, keyDescription.toJson());
        parameters.put("@" + f_objectType, objectType);

        Integer result = connection.delete(tableName, criterias, parameters);
        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows deleted";
        return status;
    }
    private boolean ObjectKeysToJson() {

        ObjectMapper mapper = new ObjectMapper();
        try {
            this.objectKeyString = mapper.writeValueAsString(objectKeys);
        }
        catch (Exception ex)
        {
            //TODO : Logging
            return false;
        }
        return true;
    }

    public static ObjectComponentDataIoRelation GetObjectComponentDataIoRelation(UUID relationId, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria aCriteria = new SqlCriteria();
        aCriteria.FieldName = f_relationId;
        aCriteria.CompareOperator = "=";
        aCriteria.FieldValue = "@" + f_relationId;
        SqlCriteria[] criterias = new SqlCriteria[1];
        criterias[0] = aCriteria;

        Map parameters = new HashMap();
        parameters.put("@" +f_relationId, relationId);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<ObjectComponentDataIoRelation> objectComponentDataIoRelations = GetObjectComponentDataIoRelationsFromResultset(rs);
        if (objectComponentDataIoRelations.size() >= 1) {
            ObjectComponentDataIoRelation objectComponenetDataIoRelation = (ObjectComponentDataIoRelation) (objectComponentDataIoRelations.toArray())[0];

            return objectComponenetDataIoRelation;
        }
        return  null;
    }
    public static ObjectComponentDataIoRelation GetObjectComponentDataIoRelation(Integer id, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria aCriteria = new SqlCriteria();
        aCriteria.FieldName = f_id;
        aCriteria.CompareOperator = "=";
        aCriteria.FieldValue = "@" + f_id;
        SqlCriteria[] criterias = new SqlCriteria[1];
        criterias[0] = aCriteria;

        Map parameters = new HashMap();
        parameters.put("@" + f_id, id);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<ObjectComponentDataIoRelation> objectComponentDataIoRelations = GetObjectComponentDataIoRelationsFromResultset(rs);
        if (objectComponentDataIoRelations.size() >= 1) {
            ObjectComponentDataIoRelation objectComponentDataIoRelation = (ObjectComponentDataIoRelation) (objectComponentDataIoRelations.toArray())[0];

            return objectComponentDataIoRelation;
        }
        return  null;
    }


    private static String[] getUpdateFields() {
        return new String[]{f_relationId,  f_objectType, f_objectKey, f_relationType, f_componentType, f_componentKey, f_dataIoKey,  f_dataIoType, f_endTime};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{"@"+f_relationId,"@"+f_objectType, "@"+f_objectKey, "@"+f_relationType, "@"+f_componentType, "@"+f_componentKey, "@"+f_dataIoKey, "@"+f_dataIoType,  "@"+f_endTime};
    }


    private static String[] getInsertFields() {
        return new String[]{f_relationId,  f_objectType, f_objectKey, f_relationType, f_componentType, f_componentKey, f_dataIoKey,  f_dataIoType,  f_createTime, f_endTime};
    }

    private static String[] getInsertValuesParameters() {
        return new String[]{"@"+f_relationId,"@"+f_objectType, "@"+f_objectKey, "@"+f_relationType, "@"+f_componentType, "@"+f_componentKey, "@"+f_dataIoKey, "@"+f_dataIoType, "@"+ f_createTime, "@"+f_endTime};
    }


    private static String[] getFields() {
        return new String[]{f_id, f_relationId, f_objectType, f_objectKey,  f_relationType, f_componentType, f_componentKey,  f_dataIoKey, f_dataIoType, f_createTime, f_endTime};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_relationId, "@"+f_objectType, "@"+f_objectKey, "@"+f_relationType,  "@"+f_componentType, "@"+f_componentKey, "@"+f_dataIoKey, "@"+f_dataIoType,  "@"+f_createTime, "@"+f_endTime};
    }

    public static List<ObjectComponentDataIoRelation> GetObjectComponentDataIoRelations(Connection connection ) throws Exception
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
        List<ObjectComponentDataIoRelation> objectComponentDataIoRelations = null;
        try {
            objectComponentDataIoRelations = GetObjectComponentDataIoRelationsFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  objectComponentDataIoRelations;
    }

    public static List<ObjectComponentDataIoRelation> GetComponentRelations(Integer componentKey, Integer componentType, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[2];

        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_componentKey;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_componentKey;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_componentType;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_componentType;
        criterias[1] = criteria2;

        Map parameters = new HashMap();
        parameters.put("@" + f_componentKey, componentKey);
        parameters.put("@" + f_componentType, componentType);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<ObjectComponentDataIoRelation> objectComponentDataIoRelations = null;
        try {
            objectComponentDataIoRelations = GetObjectComponentDataIoRelationsFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  objectComponentDataIoRelations;
    }

    public static List<ObjectComponentDataIoRelation> GetDataIoRelations(Integer dataIoKey, Connection connection, boolean includeComponentRelations ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria aCriteria = new SqlCriteria();
        aCriteria.FieldName = f_dataIoKey;
        aCriteria.CompareOperator = "=";
        aCriteria.FieldValue = "@" + f_dataIoKey;
        SqlCriteria[] criterias = new SqlCriteria[1];
        criterias[0] = aCriteria;

        Map parameters = new HashMap();
        parameters.put("@" + f_dataIoKey, dataIoKey);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<ObjectComponentDataIoRelation> objectComponentDataIoRelations = null;
        try {
            objectComponentDataIoRelations = GetObjectComponentDataIoRelationsFromResultset(rs);
            if(includeComponentRelations) {
                if (objectComponentDataIoRelations.size() == 1) {
                    ObjectComponentDataIoRelation relation = objectComponentDataIoRelations.get(0);
                    if (relation.getRelationType() == TypeComponentDataIo) {  // Get relation to object
                        List<ObjectComponentDataIoRelation> compoenentRelations = GetComponentRelations(relation.componentKey, relation.componentType, connection);
                        for (ObjectComponentDataIoRelation componetRelation : compoenentRelations)
                            objectComponentDataIoRelations.add(componetRelation);

                    }
                } else throw new Exception("more than one relation to dataio");
            }
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  objectComponentDataIoRelations;
    }
    public static List<ObjectComponentDataIoRelation> GetObjectRelations(KeyDescription keyDescription, Integer objecttype, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[2];

        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_objectKey;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_objectKey;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;

        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_objectType;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_objectType;
        criterias[1] = criteria2;

        Map parameters = new HashMap();
        parameters.put("@" + f_objectKey, keyDescription.toJson());
        parameters.put("@" + f_objectType, objecttype);

        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<ObjectComponentDataIoRelation> objectComponentDataIoRelations = null;
        try {
            objectComponentDataIoRelations = GetObjectComponentDataIoRelationsFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  objectComponentDataIoRelations;
    }



    public static List<ObjectComponentDataIoRelation> GetObjectComponentDataIoRelationsFromResultset(ResultSet resultSet) throws Exception
    {
        List<ObjectComponentDataIoRelation> objectComponentDataIoRelations = new ArrayList<ObjectComponentDataIoRelation>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                ObjectComponentDataIoRelation objectComponenetDataIoRelation = new ObjectComponentDataIoRelation();
                objectComponenetDataIoRelation.relationId = resultSet.getObject(f_relationId, UUID.class);
                objectComponenetDataIoRelation.id = resultSet.getInt(f_id);
                if (resultSet.getObject(f_objectType) != null)
                    objectComponenetDataIoRelation.objectType = resultSet.getInt(f_objectType);
                if (resultSet.getObject(f_objectKey) != null)
                objectComponenetDataIoRelation.objectKeyString = resultSet.getString(f_objectKey);

                ObjectMapper objectMapper = new ObjectMapper();
                if (objectComponenetDataIoRelation.objectKeyString != null)
                {
                    try {
                        objectComponenetDataIoRelation.objectKeys =  objectMapper.readValue(objectComponenetDataIoRelation.objectKeyString, KeyDescription[].class);
                    }
                    catch (Exception ex)
                    {
                        //TODO: Logging
                        throw new Exception("could not retrieve json from key description" );
                    }
                }
                objectComponenetDataIoRelation.relationType = resultSet.getInt(f_relationType);
                if (resultSet.getObject(f_componentType) != null)
                    objectComponenetDataIoRelation.componentType = resultSet.getInt(f_componentType);
                if (resultSet.getObject(f_componentKey) != null)
                    objectComponenetDataIoRelation.componentKey = resultSet.getInt(f_componentKey);
                if (resultSet.getObject(f_dataIoType) != null)
                    objectComponenetDataIoRelation.dataIoType = resultSet.getInt(f_dataIoType);
                if (resultSet.getObject(f_dataIoKey) != null)
                    objectComponenetDataIoRelation.dataIoKey = resultSet.getInt(f_dataIoKey);

                Timestamp ctime = resultSet.getTimestamp(f_createTime);
                if (ctime != null) {
                    objectComponenetDataIoRelation.createTime = ctime.toLocalDateTime();
                    objectComponenetDataIoRelation.createTimeString = objectComponenetDataIoRelation.createTime.format(dateTimeFormatter);
                }
                Timestamp etime = resultSet.getTimestamp(f_endTime);
                if (etime != null) {
                    objectComponenetDataIoRelation.endTime = etime.toLocalDateTime();
                    objectComponenetDataIoRelation.endTimeString = objectComponenetDataIoRelation.endTime.format(dateTimeFormatter);
                }
                objectComponentDataIoRelations.add(objectComponenetDataIoRelation);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return objectComponentDataIoRelations;
    }


}
