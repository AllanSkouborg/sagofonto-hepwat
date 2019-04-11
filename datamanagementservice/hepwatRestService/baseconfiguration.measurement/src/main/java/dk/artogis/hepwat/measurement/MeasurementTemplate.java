package dk.artogis.hepwat.measurement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MeasurementTemplate implements Serializable
{
    @JsonIgnore
    private static String tableName = "config_measurement_template";
    private UUID id ;
    private Integer templateType ;
    private String measurementAlias;
    private String measurementName;
    private Integer measurementType ;


    private static String f_id = "id";
    private static String f_templatetype = "template_type";
    private static String f_measurementAlias = "measurement_alias";
    private static String f_measurementName = "measurement_name";
    private static String f_measurementType = "measurement_type";


    //region getters and setters


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getMeasurementAlias() {
        return measurementAlias;
    }

    public void setMeasurementAlias(String measurementAlias) {
        this.measurementAlias = measurementAlias;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
    }

    public Integer getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(Integer measurementType) {
        this.measurementType = measurementType;
    }

    //endregion getters and setters
    public MeasurementTemplate(UUID  id, Integer templateType, String measurementAlias,  String measurementName, Integer measurementType)
    {

    }
    public MeasurementTemplate()
    {

    }
    public MeasurementTemplate(ResultSet rs)
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

        if (this.id == null)
            this.id = UUID.randomUUID();
        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_measurementAlias, this.measurementAlias);
        parameters.put("@"+f_measurementName, this.measurementName);
        parameters.put("@"+f_measurementType, this.measurementType);


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
        String[] updateFields = null;
        String[] updateValues = null;
        Map parameters = null;

        updateFields = getUpdateFields();
        updateValues = getUpdateValuesParameters();
        parameters = new HashMap();

        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_measurementAlias, this.measurementAlias);
        parameters.put("@"+f_measurementName, this.measurementName);
        parameters.put("@"+f_measurementType, this.measurementType);


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
    public static Status Delete(UUID id, Connection connection)
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
    public static Status DeleteTemplateType(Integer id, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        Map parameters = new HashMap();
        parameters.put("@"+f_templatetype, id);

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_templatetype;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@"+f_templatetype;

        criterias[0] = criteria1;

        Integer result = connection.delete(tableName, criterias, parameters);
        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows deleted";
        return status;
    }

    public static MeasurementTemplate GetMeasurementTemplate(UUID id, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getSelectFields();

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
        List<MeasurementTemplate> measurementTemplates = GetMeasurementTemplatesFromResultset(rs);
        if (measurementTemplates.size() >= 1) {
            MeasurementTemplate measurementTemplate = (MeasurementTemplate) (measurementTemplates.toArray())[0];
            return measurementTemplate;
        }
        return  null;
    }
    public static MeasurementTemplate GetMeasurementTemplateById(Integer templateId, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getSelectFields();

        SqlCriteria aCriteria = new SqlCriteria();
        aCriteria.FieldName = f_templatetype;
        aCriteria.CompareOperator = "=";
        aCriteria.FieldValue = "@"+ f_templatetype;

        SqlCriteria[] criterias = new SqlCriteria[1];
        criterias[0] = aCriteria;

        Map parameters = new HashMap();
        parameters.put("@"+f_templatetype, templateId);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<MeasurementTemplate> measurementTemplates = GetMeasurementTemplatesFromResultset(rs);
        if (measurementTemplates.size() >= 1) {
            MeasurementTemplate measurementTemplate = (MeasurementTemplate) (measurementTemplates.toArray())[0];
            return measurementTemplate;
        }
        return  null;
    }
    private static String[] getUpdateFields() {
        return new String[]{ f_measurementAlias, f_measurementName, f_measurementType};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{ "@"+f_measurementAlias, "@"+f_measurementName, "@"+f_measurementType};
    }

    private static String[] getInsertFields() {
        return new String[]{f_id, f_measurementAlias, f_measurementName, f_measurementType};
    }

    private static String[] getInsertValuesParameters() {
        return new String[]{"@"+f_id,  "@"+f_measurementAlias, "@"+f_measurementName, "@"+f_measurementType};
    }
    private static String[] getSelectFields() {
        return new String[]{f_id, f_measurementAlias, f_measurementName, f_measurementType, f_templatetype};
    }

    public static List<MeasurementTemplate> GetMeasurementTemplates(Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getSelectFields();

        Map parameters = new HashMap();
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, null, null, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<MeasurementTemplate> measurementTemplates = null;
        try {
            measurementTemplates = GetMeasurementTemplatesFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  measurementTemplates;
    }

    public static List<MeasurementTemplate> GetMeasurementTemplatesFromResultset(ResultSet resultSet) throws Exception
    {
        List<MeasurementTemplate> measurementTemplates = new ArrayList<MeasurementTemplate>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                MeasurementTemplate measurementTemplate = new MeasurementTemplate();
                measurementTemplate.id = resultSet.getObject(f_id, UUID.class);
                measurementTemplate.measurementAlias = resultSet.getString(f_measurementAlias);
                measurementTemplate.measurementName = resultSet.getString(f_measurementName);
                measurementTemplate.measurementType = resultSet.getInt(f_measurementType);
                measurementTemplate.templateType = resultSet.getInt(f_templatetype);
                measurementTemplates.add(measurementTemplate);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return measurementTemplates;
    }


}
