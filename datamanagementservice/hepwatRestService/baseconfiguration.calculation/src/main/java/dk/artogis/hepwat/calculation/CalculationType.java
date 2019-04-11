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

public class CalculationType implements Serializable
{
    @JsonIgnore
    public static Integer TypeNone = 0;
    public static Integer TypeCalc1 = 1;
    public static Integer TypeCalc2 = 2;
    public static Integer TypeCalc3 = 3;
    public static Integer TypeCalc4 = 4;

    @JsonIgnore
    private static String tableName = "config_calculationtype";
    private Integer id ;
    private String name;


    private static String f_id = "type";
    private static String f_name = "name";

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

    //endregion getters and setters
    public CalculationType(Integer  id, String name, Integer minutes)
    {

    }
    public CalculationType()
    {

    }
    public CalculationType(ResultSet rs)
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

        //parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_name, this.name);


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

    public static CalculationType GetAggregationType(Integer id, Connection connection ) throws Exception
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
        List<CalculationType> aggregationTypes = GetCalculationTypesFromResultset(rs);
        if (aggregationTypes.size() >= 1) {
            CalculationType aggregationType = (CalculationType) (aggregationTypes.toArray())[0];
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
        return new String[]{f_name};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{"@"+f_name};
    }

    private static String[] getFields() {
        return new String[]{f_id, f_name};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name};
    }

    public static List<CalculationType> GetCalculationTypes(Connection connection ) throws Exception
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
        List<CalculationType> aggregationTypes = null;
        try {
            aggregationTypes = GetCalculationTypesFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  aggregationTypes;
    }

    public static List<CalculationType> GetCalculationTypesFromResultset(ResultSet resultSet) throws Exception
    {
        List<CalculationType> aggregationTypes = new ArrayList<CalculationType>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                CalculationType aggregationType = new CalculationType();
                aggregationType.id = resultSet.getInt(f_id);
                aggregationType.name = resultSet.getString(f_name);
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
    public static String GetCalculationTypeText(List<CalculationType> calculationTypes, Integer calculationTypeId)
    {
        for (CalculationType calculationType : calculationTypes)
        {
            if (calculationType.id == calculationTypeId)
                return calculationType.name;
        }
        return  "Unknown";
    }

}
