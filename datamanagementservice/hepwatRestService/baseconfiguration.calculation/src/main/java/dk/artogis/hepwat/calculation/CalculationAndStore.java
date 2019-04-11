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
public class CalculationAndStore implements Serializable
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
    private static String tableName = "config_calculation_and_store";
    private Integer calculation ;
    @JsonIgnore
    private Integer dataIoId;
    private String formula;
    private String calculationType;

    @JsonIgnore
    private Integer templateType;
    private AggregationAndStore[] aggregationAndStores;

    private static String f_calculation = "calculation";
    private static String f_dataIoId = "data_io_id";
    private static String f_formula = "formula";

    private static String f_templateType = "template_type";

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

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public AggregationAndStore[] getAggregationAndStores() {
        return aggregationAndStores;
    }

    public void setAggregationAndStores(AggregationAndStore[] aggregationAndStores) {
        this.aggregationAndStores = aggregationAndStores;
    }

    public String getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }

    //endregion getters and setters
    public CalculationAndStore(Integer calculation, Integer dataIoId, String formula, String unit, Boolean store, Integer templateType  )
    {
        this.calculation = calculation;
        this.dataIoId = dataIoId;
        this.formula = formula;
        this.templateType = templateType;
    }
    public CalculationAndStore()
    {

    }
    public CalculationAndStore(ResultSet rs)
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
        parameters.put("@"+f_formula, this.formula);
        parameters.put("@" +f_templateType, this.templateType);

        // Insert AggregationAndStore

        if (aggregationAndStores != null)
        {
            for (AggregationAndStore aggregationAndStore : aggregationAndStores)
            {
                aggregationAndStore.setTemplateType(this.templateType);
                aggregationAndStore.setDataIoId(this.dataIoId);
                aggregationAndStore.setCalculation(this.calculation);
                status = aggregationAndStore.Insert(connection);
                if (status.Success == false)
                    break;
            }
        }
        if (status.Success) {//
            Integer result = connection.insert(tableName, insertFields, insertValues, parameters, false, null);

            if (result == 1)
                status.Success = true;
            else {
                status.Message = "no rows inserted";
                status.Error = connection.Status.Error;
            }
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
        parameters.put("@"+f_formula, this.formula);;
        parameters.put("@" +f_templateType, this.templateType);

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


        if (aggregationAndStores != null)
        {
            for (AggregationAndStore aggregationAndStore : aggregationAndStores)
            {
                aggregationAndStore.setTemplateType(this.templateType);
                aggregationAndStore.setDataIoId(this.dataIoId);
                aggregationAndStore.setCalculation(this.calculation);
                status = aggregationAndStore.Update(connection);
                if (status.Success == false)
                    break;
            }
        }

        Integer result = -1;
        if (status.Success)
            result = connection.update(tableName, updateFields, updateValues, criterias, parameters);

        if (result == 1 )
            status.Success = true;
        else {
            status.Message = "no rows updated, or only part of records updated";
            status.Error = connection.Status.Error;
        }
        return status;
    }

    public static Status Delete(Integer dataIoId, Integer templateType, Integer calculation, Connection connection) {

        Status status = AggregationAndStore.Delete(dataIoId, templateType, calculation, connection);
        if (status.Success)
            status = CalculationAndStore.DeleteRow(dataIoId, templateType, calculation, connection);
        return  status;
    }

    public static Status DeleteRow(Integer dataIoId, Integer templateType, Integer calculation, Connection connection)
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
            if ((templateType  > 0 )  || (calculation > 0  ))
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

    public static CalculationAndStore GetCalculationAndStore(Integer calculation, Integer dataIoId, Integer templatetype, Connection connection ) throws Exception
    {
        List <CalculationType> calculationTypes = null;

        try{
            calculationTypes = CalculationType.GetCalculationTypes(connection);
        }
        catch (Exception ex)
        {
            throw new Exception ("could not get calculationTypes");
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
        List<CalculationAndStore> calculationAndStores = GetCalculationsAndStoresFromResultset(rs, calculationTypes);
        if (calculationAndStores.size() >= 1) {
            CalculationAndStore calculationAndStore = (CalculationAndStore) (calculationAndStores.toArray())[0];
            return calculationAndStore;
        }
        return  null;
    }

    public static List< CalculationAndStore> GetCalculationAndStores( Integer dataIoId, Connection connection, Boolean addTypeInfo, Boolean includeAggregationAndStores ) throws Exception
    {

        List <CalculationType> calculationTypes = null;
        if (addTypeInfo) {
            try {
                calculationTypes = CalculationType.GetCalculationTypes(connection);
            } catch (Exception ex) {
                throw new Exception("could not get calculationTypes");
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
        List<CalculationAndStore> calculationAndStores = GetCalculationsAndStoresFromResultset(rs, calculationTypes);
        // get aggregation and stores
        if (includeAggregationAndStores) {
            for (CalculationAndStore calculationAndStore : calculationAndStores) {
                List<AggregationAndStore> aggregationAndStoresList = AggregationAndStore.GetAggregationAndStores(dataIoId, calculationAndStore.calculation, connection, addTypeInfo);
                if (aggregationAndStoresList != null) {
                    AggregationAndStore[] aggregationAndStores = new AggregationAndStore[aggregationAndStoresList.size()];
                    aggregationAndStores = (AggregationAndStore[]) aggregationAndStoresList.toArray(aggregationAndStores);
                    calculationAndStore.setAggregationAndStores(aggregationAndStores);
                }
            }
        }
        return  calculationAndStores;
    }
    public static List< CalculationAndStore> GetCalculationAndStoresByTemplateId( Integer templatetype, Connection connection ) throws Exception
    {
        List <CalculationType> calculationTypes = null;

        try{
            calculationTypes = CalculationType.GetCalculationTypes(connection);
        }
        catch (Exception ex)
        {
            throw new Exception ("could not get calculationTypes");
        }

        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[2];
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
        criterias[1] = criteria2;

        Integer dataIoId = -1;

        Map parameters = new HashMap();
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
        List<CalculationAndStore> calculationAndStores = GetCalculationsAndStoresFromResultset(rs, calculationTypes);

        // get aggregation and stores
        for (CalculationAndStore calculationAndStore : calculationAndStores)
        {
            List<AggregationAndStore> aggregationAndStoresList = AggregationAndStore.GetAggregationAndStoresBytTemplateId(templatetype, calculationAndStore.calculation, connection);
            if (aggregationAndStoresList != null ) {
                AggregationAndStore[] aggregationAndStores = new AggregationAndStore[aggregationAndStoresList.size()];
                aggregationAndStores = (AggregationAndStore[]) aggregationAndStoresList.toArray(aggregationAndStores);
                calculationAndStore.setAggregationAndStores(aggregationAndStores);
            }
        }
        return  calculationAndStores;
    }
    public static List< CalculationAndStore> GetCalculationAndStoresByCalculationType( Integer calculationType, Connection connection ) throws Exception
    {

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
        criteria2.CompareOperator = "<>";
        criteria2.FieldValue = "@" + f_dataIoId;
        criterias[1] = criteria2;

        Integer dataIoId = -1;

        Map parameters = new HashMap();
        parameters.put("@"+f_calculation, calculationType);
        parameters.put("@"+f_dataIoId, dataIoId);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<CalculationAndStore> calculationAndStores = GetCalculationsAndStoresFromResultset(rs, null);


        return  calculationAndStores;
    }
    private static String[] getUpdateFields() {
        return new String[]{ f_formula, f_templateType};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{ "@"+f_formula,  "@"+f_templateType};
    }

    private static String[] getFields() {
        return new String[]{f_calculation, f_dataIoId, f_formula, f_templateType};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_calculation, "@"+f_dataIoId, "@"+f_formula, "@"+f_templateType};
    }

    public static List<CalculationAndStore> GetCalculationAndStores(Connection connection ) throws Exception
    {
        List <CalculationType> calculationTypes = null;

        try{
            calculationTypes = CalculationType.GetCalculationTypes(connection);
        }
        catch (Exception ex)
        {
            throw new Exception ("could not get calculationTypes");
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

        List<CalculationAndStore> calculationAndStores = null;
        try {
            calculationAndStores = GetCalculationsAndStoresFromResultset(rs, calculationTypes);

            // get aggregation and stores
            for (CalculationAndStore calculationAndStore : calculationAndStores) {
                List<AggregationAndStore> aggregationAndStoresList = AggregationAndStore.GetAggregationAndStoresBytTemplateId(calculationAndStore.templateType, calculationAndStore.calculation, connection);
                if (aggregationAndStoresList != null) {
                    AggregationAndStore[] aggregationAndStores = new AggregationAndStore[aggregationAndStoresList.size()];
                    aggregationAndStores = (AggregationAndStore[]) aggregationAndStoresList.toArray(aggregationAndStores);
                    calculationAndStore.setAggregationAndStores(aggregationAndStores);
                }
            }
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }



        return  calculationAndStores;
    }

    public static List<CalculationAndStore> GetCalculationsAndStoresFromResultset(ResultSet resultSet, List <CalculationType> calculationTypes ) throws Exception
    {
        List<CalculationAndStore> calculationAndStores = new ArrayList<CalculationAndStore>();
        if (resultSet == null)
            throw new Exception ("resultset null");

        try
        {
            while(resultSet.next()) {
                CalculationAndStore calculationAndStore = new CalculationAndStore();
                calculationAndStore.calculation = resultSet.getInt(f_calculation);
                calculationAndStore.dataIoId = resultSet.getInt(f_dataIoId);
                calculationAndStore.formula = resultSet.getString(f_formula);
                calculationAndStore.templateType= resultSet.getInt(f_templateType);
                if (calculationTypes != null)
                    calculationAndStore.calculationType = CalculationType.GetCalculationTypeText(calculationTypes, calculationAndStore.calculation);
                calculationAndStores.add(calculationAndStore);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return calculationAndStores;
    }

    public Boolean hasAggType(Integer aggType)
    {
        //TODO: remove method from this class
        Boolean found = false;
        return true;

    }

}
