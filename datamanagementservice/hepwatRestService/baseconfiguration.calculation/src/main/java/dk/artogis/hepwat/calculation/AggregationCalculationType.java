package dk.artogis.hepwat.calculation;

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

public class AggregationCalculationType implements Serializable
{
    @JsonIgnore
    public static Integer TypeNone = 0;
    public static Integer TypeCalcAverage = 1;
    public static Integer TypeCalcSum = 2;
    public static Integer TypeCalcDeltaSum = 3;
    public static Integer TypeCalcTest = 4;

    @JsonIgnore
    private static String tableName = "config_aggregation_calculation_type";
    private Integer id ;
    private String name;
    private String formula;

    private static String f_id = "type";
    private static String f_name = "name";
    private static String f_formula = "formula";
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

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    //endregion getters and setters
    public AggregationCalculationType(Integer  id, String name, Integer minutes)
    {

    }
    public AggregationCalculationType()
    {

    }
    public AggregationCalculationType(ResultSet rs)
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

        insertFields = getUpdateFields();
        insertValues = getUpdateValuesParameters();
        parameters = new HashMap();

        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_formula, this.formula);

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
        String[] updateFields = getFields();
        String[] updateValues = getValuesParameters();
        Map parameters = null;

        updateFields = getUpdateFields();
        updateValues = getUpdateValuesParameters();
        parameters = new HashMap();

        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_formula, this.formula);

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

    public static AggregationCalculationType GetAggregationCalculationType(Integer id, Connection connection ) throws Exception
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
        List<AggregationCalculationType> aggregationTypes = GetAggregationCalculationTypesFromResultset(rs);
        if (aggregationTypes.size() >= 1) {
            AggregationCalculationType aggregationType = (AggregationCalculationType) (aggregationTypes.toArray())[0];
            return aggregationType;
        }
        return  null;
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
    private static String[] getUpdateFields() {
        return new String[]{f_name, f_formula};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{"@"+f_name, "@"+f_formula};
    }

    private static String[] getFields() {
        return new String[]{f_id, f_name, f_formula};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name, "@"+f_formula};
    }
    public static List<AggregationCalculationType> GetAggregationCalculationTypes(Connection connection ) throws Exception
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
        List<AggregationCalculationType> aggregationTypes = null;
        try {
            aggregationTypes = GetAggregationCalculationTypesFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  aggregationTypes;
    }

    public static List<AggregationCalculationType> GetAggregationCalculationTypesFromResultset(ResultSet resultSet) throws Exception
    {
        List<AggregationCalculationType> aggregationTypes = new ArrayList<AggregationCalculationType>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                AggregationCalculationType aggregationType = new AggregationCalculationType();
                aggregationType.id = resultSet.getInt(f_id);
                aggregationType.name = resultSet.getString(f_name);
                aggregationType.formula = resultSet.getString(f_formula);
                aggregationTypes.add(aggregationType);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return aggregationTypes;
    }


}
