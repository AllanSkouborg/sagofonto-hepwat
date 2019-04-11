package dk.artogis.hepwat.measurement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Unit implements Serializable
{
    @JsonIgnore
    private static String tableName = "config_unit";
    private Integer id ;
    private String name;
    private String description;
    private String language;

    private static String f_id = "id";
    private static String f_name = "name";
    private static String f_description = "description";
    private static String f_language = "language";

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    //endregion getters and setters
    public Unit(Integer id, String name, String description, String language)
    {
        this.id =id;
        this.name = name;
        this.description = description;
        this.language = language;
    }
    public Unit()
    {

    }
    public Unit(ResultSet rs)
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
            this.id =  result;
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
    public static Unit GetUnit(Integer id, String language, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();
        Map parameters = new HashMap();
        SqlCriteria[] criterias = null;
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@f_id";
        parameters.put("@f_id", id);
        criterias = new SqlCriteria[1];
        if ((language != null) && (!language.isEmpty())) {
            criteria1.CriteriaOperand = "AND";
            criterias = new SqlCriteria[2];
            SqlCriteria criteria2 = new SqlCriteria();
            criteria2.FieldName = f_language;
            criteria2.CompareOperator = "=";
            criteria2.FieldValue = "@" + f_language;
            criterias[1] = criteria2;
            parameters.put("@"+f_language, language);
        }
        criterias[0] = criteria1;

        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<Unit> unitList = GetUnitFromResultset(rs);
        if (unitList.size() >= 1) {
            Unit units = (Unit) (unitList.toArray())[0];
            return units;
        }
        return  null;
    }

    private static String[] getUpdateFields() {
        return new String[]{f_name, f_description, f_language};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{"@"+f_name, "@"+f_description, "@"+f_language};
    }

    private static String[] getFields() {
        return new String[]{f_id, f_name, f_description, f_language};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name, "@"+f_description, "@"+f_language};
    }
    public static List<Unit> GetUnits(Connection connection, String language ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();
        SqlCriteria[] criterias = null;
        Map parameters = null;
        if ((language != null) && (!language.isEmpty())) {
            SqlCriteria aCriteria = new SqlCriteria();
            parameters = new HashMap();
            aCriteria.FieldName = f_language;
            aCriteria.CompareOperator = "=";
            aCriteria.FieldValue = "@" + f_language;
            criterias = new SqlCriteria[1];
            criterias[0] = aCriteria;

            parameters.put("@" + f_language, language);
        }
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<Unit> measurementTypeList = null;
        try {
            measurementTypeList = GetUnitFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  measurementTypeList;
    }

    public static List<Unit> GetUnitFromResultset(ResultSet resultSet) throws Exception
    {
        List<Unit> unitList = new ArrayList<Unit>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                Unit unit = new Unit();
                unit.id = resultSet.getInt(f_id);
                unit.name = resultSet.getString(f_name);
                unit.description = resultSet.getString(f_description);
                unit.language = resultSet.getString(f_language);
                unitList.add(unit);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return unitList;
    }


}
