package dk.artogis.hepwat.measurement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MeasurementTemplateType implements Serializable
{
    @JsonIgnore
    private static String tableName = "config_measurement_template_type";
    private Integer id ;
    private String name;
    private String searchText;

    private static String f_id = "id";
    private static String f_name = "name";
    private static String f_searchText = "searchtext";

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

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    //endregion getters and setters
    public MeasurementTemplateType(UUID  id, Integer templateType, String measurementAlias, String measurementName, Integer measurementType)
    {

    }
    public MeasurementTemplateType()
    {

    }
    public MeasurementTemplateType(ResultSet rs)
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
        String[] identitycolumns = new String[]{f_id};

        //parameters.put("@"+f_id, this.id);

        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_searchText, this.searchText);

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
        String[] updateFields = null;
        String[] updateValues = null;
        Map parameters = null;

        updateFields = getUpdateFields();
        updateValues = getUpdateValuesParameters();
        parameters = new HashMap();

        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_searchText, this.searchText);

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

    public static MeasurementTemplateType GetMeasurementTemplateType(Integer id, Connection connection ) throws Exception
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
        List<MeasurementTemplateType> measurementTemplateTypes = GetMeasurementTemplateTypesFromResultset(rs);
        if (measurementTemplateTypes.size() >= 1) {
            MeasurementTemplateType measurementTemplate = (MeasurementTemplateType) (measurementTemplateTypes.toArray())[0];
            return measurementTemplate;
        }
        return  null;
    }

    private static String[] getUpdateFields() {
        return new String[]{f_name, f_searchText};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{"@"+f_name, "@"+f_searchText};
    }

    private static String[] getFields() {
        return new String[]{f_id, f_name, f_searchText};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name, "@"+f_searchText};
    }
    public static List<MeasurementTemplateType> GetMeasurementTemplateTypes(Connection connection ) throws Exception
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
        List<MeasurementTemplateType> measurementTemplates = null;
        try {
            measurementTemplates = GetMeasurementTemplateTypesFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  measurementTemplates;
    }

    public static List<MeasurementTemplateType> GetMeasurementTemplateTypesFromResultset(ResultSet resultSet) throws Exception
    {
        List<MeasurementTemplateType> measurementTemplateTypes = new ArrayList<MeasurementTemplateType>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                MeasurementTemplateType measurementTemplateType = new MeasurementTemplateType();
                measurementTemplateType.id = resultSet.getInt(f_id);
                measurementTemplateType.name = resultSet.getString(f_name);
                measurementTemplateType.searchText = resultSet.getString(f_searchText);
                measurementTemplateTypes.add(measurementTemplateType);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return measurementTemplateTypes;
    }


}
