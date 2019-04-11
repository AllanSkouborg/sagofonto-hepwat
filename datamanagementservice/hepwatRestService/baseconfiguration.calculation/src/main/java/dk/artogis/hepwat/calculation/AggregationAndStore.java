package dk.artogis.hepwat.calculation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregationAndStore implements Serializable
{
    @JsonIgnore
    public static Integer TypeNone = 0;
    @JsonIgnore
    public static Integer TypeCalcPreProcess = 1;
    @JsonIgnore
    public static Integer TypeUse = 2;
    @JsonIgnore
    public static Integer TypeQuality = 3;

    private static Integer MAX_NO_CALCULATIONS = 4;
    @JsonIgnore
    private static String tableName = "config_aggregation_and_store";
    @JsonIgnore
    private Integer calculation ;
    @JsonIgnore
    private Integer dataIoId;

    private Integer aggregationType;
    private String unit;
    private Boolean store;
    private Boolean aggregate;
    private String aggregationCalculation;
    private Integer aggregationInterval;
    private double scaleToUnit;

    private Boolean statusOn;
    private Double max;
    private Double high;
    private Double low;
    private Double min;

    @JsonIgnore
    private Integer templateType;


    private static String f_calculation = "calculation";
    private static String f_dataIoId = "data_io_id";
    private static String f_aggregationType = "aggregation_type";
    private static String f_unit = "unit";
    private static String f_store = "store";
    private static String f_templateType = "template_type";
    private static String f_aggregate = "aggregate";
    private static String f_scaleToUnit = "scale_to_unit";

    private static String f_statusOn = "status_on";
    private static String f_max = "max";
    private static String f_high = "high";
    private static String f_low = "low";
    private static String f_min = "min";

    //region getters and setters
    public Integer getCalculation() {
        return calculation;
    }

    public void setCalculation(Integer calculation) {
        this.calculation = calculation;
    }

    public Integer getDataIoId() {
        return dataIoId;
    }

    public void setDataIoId(Integer dataIoId) {
        this.dataIoId = dataIoId;
    }

    public Integer getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(Integer aggregationType) {
        this.aggregationType = aggregationType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getStore() {
        return store;
    }

    public void setStore(Boolean store) {
        this.store = store;
    }


    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public Boolean getAggregate() {
        return aggregate;
    }

    public void setAggregate(Boolean aggregate) {
        this.aggregate = aggregate;
    }

    public String getAggregationCalculation() {
        return aggregationCalculation;
    }

    public void setAggregationCalculation(String aggregationCalculation) {
        this.aggregationCalculation = aggregationCalculation;
    }

    public Integer getAggregationInterval() {
        return aggregationInterval;
    }

    public void setAggregationInterval(Integer aggregationInterval) {
        this.aggregationInterval = aggregationInterval;
    }

    public double getScaleToUnit() {
        return scaleToUnit;
    }

    public void setScaleToUnit(double scaleToUnit) {
        this.scaleToUnit = scaleToUnit;
    }

    public Boolean getStatusOn() { return statusOn; }
    public void setStatusOn(Boolean statusOn) { this.statusOn = statusOn; }

    public Double getMax() { return max; }
    public void setMax(Double max) { this.max = max; }

    public Double getHigh() { return high; }
    public void setHigh(Double high) { this.high = high; }

    public Double getLow() { return low; }
    public void setLow(Double low) { this.low = low; }

    public Double getMin() { return min; }
    public void setMin(Double min) { this.min = min; }

    //endregion getters and setters
    public AggregationAndStore(Integer calculation, Integer dataIoId, Integer aggregationType, String unit, Boolean store, Integer templateType , Boolean aggregate, Boolean statusOn, Double max, Double high, Double low, Double min ) {
        this.calculation = calculation;
        this.dataIoId = dataIoId;
        this.aggregationType = aggregationType;
        this.store = store;
        this.unit = unit;
        this.templateType = templateType;
        this.aggregate = aggregate;

        this.statusOn = statusOn;
        this.max = max;
        this.high = high;
        this.low = low;
    }

    public AggregationAndStore()
    {

    }
    public AggregationAndStore(ResultSet rs)
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

        parameters.put("@"+f_calculation, this.calculation);
        parameters.put("@"+f_dataIoId, this.dataIoId);
        parameters.put("@"+f_aggregationType, this.aggregationType );
        parameters.put("@"+f_store, this.store);
        parameters.put("@"+f_unit, this.unit);
        parameters.put("@" +f_templateType, this.templateType);
        parameters.put("@" +f_aggregate, this.aggregate);
        parameters.put("@" +f_scaleToUnit, this.scaleToUnit);

        parameters.put("@" +f_statusOn, this.statusOn);
        parameters.put("@" +f_max, this.max);
        parameters.put("@" +f_high, this.high);
        parameters.put("@" +f_low, this.low);
        parameters.put("@" +f_min, this.min);

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

        parameters.put("@"+f_calculation, this.calculation);
        parameters.put("@"+f_dataIoId, this.dataIoId);
        parameters.put("@"+f_aggregationType, this.aggregationType );
        parameters.put("@"+f_store, this.store);
        parameters.put("@"+f_unit, this.unit);
        parameters.put("@"+f_templateType, this.templateType);
        parameters.put("@" +f_aggregate, this.aggregate);
        parameters.put("@" +f_scaleToUnit, this.scaleToUnit);

        parameters.put("@" +f_statusOn, this.statusOn);
        parameters.put("@" +f_max, this.max);
        parameters.put("@" +f_high, this.high);
        parameters.put("@" +f_low, this.low);
        parameters.put("@" +f_min, this.min);

        SqlCriteria[] criterias = new SqlCriteria[4];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_calculation;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_calculation;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_dataIoId;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_dataIoId;
        criteria2.CriteriaOperand = "AND";
        criterias[1] = criteria2;
        SqlCriteria criteria3 = new SqlCriteria();
        criteria3.FieldName = f_templateType;
        criteria3.CompareOperator = "=";
        criteria3.FieldValue = "@" + f_templateType;
        criteria3.CriteriaOperand = "AND";
        criterias[2] = criteria3;
        SqlCriteria criteria4 = new SqlCriteria();
        criteria4.FieldName = f_aggregationType;
        criteria4.CompareOperator = "=";
        criteria4.FieldValue = "@" + f_aggregationType;
        //criteria4.CriteriaOperand = "AND";
        criterias[3] = criteria4;

        Integer result = connection.update(tableName, updateFields, updateValues, criterias, parameters);

        if (result == 1 )
            status.Success = true;
        else {
            status.Message = "no rows updated, or only part of records updated";
            status.Error = connection.Status.Error;
        }
        return status;
    }

    public static Status Delete(Integer dataIoId, Integer templateType, Integer calculation, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        Map parameters = new HashMap();

        SqlCriteria[] criterias = null;
        SqlCriteria criteriaCalc = null;
        SqlCriteria criteriaTemplate = null;
        SqlCriteria criteriaDataIo = null;
        Integer criteriaCounter = 0 ;
        if (dataIoId  > 0 ) {
            criteriaDataIo = new SqlCriteria();
            criteriaCounter++;
            parameters.put("@"+f_dataIoId, dataIoId);
            criteriaDataIo.FieldName = f_dataIoId;
            criteriaDataIo.CompareOperator = "=";
            criteriaDataIo.FieldValue = "@"+f_dataIoId;
            if ((templateType  > 0 )  || (calculation > 0  ) )
                criteriaDataIo.CriteriaOperand = "AND";
        }
        if (templateType  > 0 ) {
            criteriaTemplate = new SqlCriteria();
            criteriaCounter++;
            parameters.put("@"+f_templateType, templateType);
            criteriaTemplate.FieldName = f_templateType;
            criteriaTemplate.CompareOperator = "=";
            criteriaTemplate.FieldValue = "@"+f_templateType;
            if (calculation > 0 && calculation <= MAX_NO_CALCULATIONS)
                criteriaTemplate.CriteriaOperand = "AND";
        }
        if (calculation > 0 && calculation <= MAX_NO_CALCULATIONS) {
            criteriaCalc = new SqlCriteria();
            criteriaCounter++;
            parameters.put("@"+f_calculation, calculation);
            criteriaCalc.FieldName = f_calculation;
            criteriaCalc.CompareOperator = "=";
            criteriaCalc.FieldValue = "@" + f_calculation;
        }
        criterias = new SqlCriteria[criteriaCounter];
        if (dataIoId  > 0 ) {
            criterias[0] = criteriaDataIo;
            if (templateType  > 0) {
                criterias[1] = criteriaTemplate;
                if ((calculation > 0 && calculation <= MAX_NO_CALCULATIONS))
                    criterias[2] = criteriaCalc;
            }
            else if ((calculation > 0 && calculation <= MAX_NO_CALCULATIONS))
                criterias[1] = criteriaCalc;
        }
        else  if (templateType  > 0 ) {
            criterias[0] = criteriaTemplate;
            if ((calculation > 0 && calculation <= MAX_NO_CALCULATIONS))
                criterias[1] = criteriaCalc;
        }
        else criterias [0] = criteriaCalc;


        Integer result = connection.delete(tableName, criterias, parameters);
        if (result >= 0)
            status.Success = true;
        else
            status.Message = "no rows deleted";
        return status;
    }

    public static AggregationAndStore GetAggregationAndStore(Integer calculation, Integer dataIoId, Integer templatetype, Connection connection ) throws Exception
    {
        List<AggregationType> aggregationTypeList = null;
         try{
             aggregationTypeList = AggregationType.GetAggregationTypes(connection);
         }
        catch (Exception ex)
        {
            throw new Exception("could not get aggregation types or aggregation calculation types");
        }

        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[3];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_calculation;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_calculation;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_dataIoId;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_dataIoId;
        criteria2.CriteriaOperand = "AND";
        criterias[1] = criteria2;
        SqlCriteria criteria3 = new SqlCriteria();
        criteria3.FieldName = f_templateType;
        criteria3.CompareOperator = "=";
        criteria3.FieldValue = "@" + f_templateType;
        criterias[2] = criteria3;

        Map parameters = new HashMap();
        parameters.put("@"+f_calculation, calculation);
        parameters.put("@"+f_templateType, templatetype);
        parameters.put("@"+f_dataIoId, dataIoId);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<AggregationAndStore> calculationAndStores = GetAggregationsAndStoresFromResultset(rs, aggregationTypeList);
        if (calculationAndStores.size() >= 1) {
            AggregationAndStore calculationAndStore = (AggregationAndStore) (calculationAndStores.toArray())[0];
            return calculationAndStore;
        }
        return  null;
    }

    public static List<AggregationAndStore> GetAggregationAndStores(Integer dataIoId, Connection connection ,Boolean addTypeInfo) throws Exception
    {
        List<AggregationType> aggregationTypeList = null;
        if (addTypeInfo) {
            try {
                aggregationTypeList = AggregationType.GetAggregationTypes(connection);
            } catch (Exception ex) {
                throw new Exception("could not get aggregation types ");
            }
        }
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_dataIoId;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_dataIoId;
        criterias[0] = criteria1;

        Map parameters = new HashMap();
        parameters.put("@"+f_dataIoId, dataIoId);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<AggregationAndStore> calculationAndStores = GetAggregationsAndStoresFromResultset(rs, aggregationTypeList);

        return  calculationAndStores;
    }
    public static List<AggregationAndStore> GetAggregationAndStores(Integer dataIoId, Integer calculation, Connection connection, Boolean addTypeInfo ) throws Exception
    {
        List<AggregationType> aggregationTypeList = null;
        if(addTypeInfo) {
            try {
                aggregationTypeList = AggregationType.GetAggregationTypes(connection);
            } catch (Exception ex) {
                throw new Exception("could not get aggregation types ");
            }
        }
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[2];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_calculation;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_calculation;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_dataIoId;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_dataIoId;
        criterias[1] = criteria2;

        Map parameters = new HashMap();
        parameters.put("@"+f_calculation, calculation);
        parameters.put("@"+f_dataIoId, dataIoId);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<AggregationAndStore> calculationAndStores = GetAggregationsAndStoresFromResultset(rs, aggregationTypeList);

        return  calculationAndStores;
    }
    public static List<AggregationAndStore> GetAggregationAndStoresBytTemplateId(Integer templatetype, Integer calculation,  Connection connection ) throws Exception
    {
        List<AggregationType> aggregationTypeList = null;
        try{
            aggregationTypeList = AggregationType.GetAggregationTypes(connection);
        }
        catch (Exception ex)
        {
            throw new Exception("could not get aggregation types ");
        }

        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[3];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_templateType;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_templateType;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_dataIoId;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_dataIoId;
        criteria2.CriteriaOperand = "AND";
        criterias[1] = criteria2;
        SqlCriteria criteria3 = new SqlCriteria();
        criteria3.FieldName = f_calculation;
        criteria3.CompareOperator = "=";
        criteria3.FieldValue = "@" + f_calculation;
        criterias[2] = criteria3;

        Integer dataIoId = -1;

        Map parameters = new HashMap();
        parameters.put("@"+f_templateType, templatetype);
        parameters.put("@"+f_dataIoId, dataIoId);
        parameters.put("@"+f_calculation, calculation);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<AggregationAndStore> calculationAndStores = GetAggregationsAndStoresFromResultset(rs, aggregationTypeList);

        return  calculationAndStores;
    }
    private static String[] getUpdateFields() {
        return new String[]{  f_unit, f_store,  f_aggregate, f_scaleToUnit, f_statusOn, f_max, f_high, f_low, f_min};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{ "@"+f_unit, "@"+f_store,  "@"+f_aggregate, "@"+f_scaleToUnit, "@"+f_statusOn, "@"+f_max, "@"+f_high, "@"+f_low, "@"+f_min };
    }

    private static String[] getFields() {
        return new String[]{f_calculation, f_dataIoId, f_aggregationType , f_unit, f_store, f_templateType, f_aggregate, f_scaleToUnit, f_statusOn, f_max, f_high, f_low, f_min};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_calculation, "@"+f_dataIoId, "@"+f_aggregationType , "@"+f_unit, "@"+f_store, "@"+f_templateType, "@"+f_aggregate, "@"+f_scaleToUnit, "@"+f_statusOn, "@"+f_max, "@"+f_high, "@"+f_low, "@"+f_min};
    }

    public static List<AggregationAndStore> GetAggretationAndStores(Connection connection ) throws Exception
    {
        List<AggregationType> aggregationTypeList = null;
        try{
            aggregationTypeList = AggregationType.GetAggregationTypes(connection);
        }
        catch (Exception ex)
        {
            throw new Exception("could not get aggregation types ");
        }

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
        List<AggregationAndStore> calculationAndStores = null;
        try {
            calculationAndStores = GetAggregationsAndStoresFromResultset(rs, aggregationTypeList);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  calculationAndStores;
    }

    public static List<AggregationAndStore> GetAggregationsAndStoresFromResultset(ResultSet resultSet, List<AggregationType> aggregationTypeList) throws Exception
    {
        List<AggregationAndStore> aggregationAndStores = new ArrayList<AggregationAndStore>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                AggregationAndStore aggregationAndStore = new AggregationAndStore();
                aggregationAndStore.calculation = resultSet.getInt(f_calculation);
                aggregationAndStore.dataIoId = resultSet.getInt(f_dataIoId);
                aggregationAndStore.aggregationType = resultSet.getInt(f_aggregationType);
                aggregationAndStore.unit = resultSet.getString(f_unit);
                aggregationAndStore.store = resultSet.getBoolean(f_store);
                aggregationAndStore.aggregate = resultSet.getBoolean(f_aggregate);
                aggregationAndStore.templateType= resultSet.getInt(f_templateType);
                aggregationAndStore.scaleToUnit =  resultSet.getDouble(f_scaleToUnit);

                aggregationAndStore.statusOn = resultSet.getBoolean(f_statusOn);
                aggregationAndStore.max = (resultSet.getObject(f_max) == null) ? null : resultSet.getDouble(f_max);
                aggregationAndStore.high = (resultSet.getObject(f_high) == null) ? null : resultSet.getDouble(f_high);
                aggregationAndStore.low = (resultSet.getObject(f_low) == null) ? null : resultSet.getDouble(f_low);
                aggregationAndStore.min = (resultSet.getObject(f_min) == null) ? null : resultSet.getDouble(f_min);

                if (aggregationTypeList != null) {
                    AggregationType aggType = AggregationType.GetAggregationType(aggregationTypeList, aggregationAndStore.aggregationType);
                    aggregationAndStore.aggregationCalculation = aggType.getName();
                    aggregationAndStore.aggregationInterval = aggType.getMinutes();
                }
                aggregationAndStores.add(aggregationAndStore);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return aggregationAndStores;
    }



}
