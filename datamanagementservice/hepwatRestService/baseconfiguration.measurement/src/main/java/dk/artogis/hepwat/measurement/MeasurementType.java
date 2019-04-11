package dk.artogis.hepwat.measurement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MeasurementType implements Serializable
{
    @JsonIgnore
    private static String tableName = "config_measurement_type";
    private Integer id ;
    private String name;
    private boolean isSignalStrength;
    private boolean isBatteryStatus;
    private String language;

    private static String f_id = "id";
    private static String f_name = "name";
    private static String f_isSignalStrength = "is_signal_strength";
    private static String f_isBatteryStatus = "is_battery_status";
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

    public boolean getIsSignalStrength() {
        return isSignalStrength;
    }

    public void setIsSignalStrength(boolean isSignalStrength) {
        this.isSignalStrength = isSignalStrength;
    }

    public boolean getIsBatteryStatus() {
        return isBatteryStatus;
    }

    public void setIsBatteryStatus(boolean isBatteryStatus) {
        this.isBatteryStatus = isBatteryStatus;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

//endregion getters and setters

    public MeasurementType()
    {

    }
    public MeasurementType(ResultSet rs)
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

        //parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_isSignalStrength, this.isSignalStrength);
        parameters.put("@"+f_isBatteryStatus, this.isBatteryStatus);
        parameters.put("@" + f_language, this.language);

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
        parameters.put("@"+f_isSignalStrength, this.isSignalStrength);
        parameters.put("@"+f_isBatteryStatus, this.isBatteryStatus);
        parameters.put("@"+f_language, this.language);

        SqlCriteria[] criterias = new SqlCriteria[2];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_id;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_language;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_language;
        criterias[1] = criteria2;

        Integer result = connection.update(tableName, updateFields, updateValues, criterias, parameters);

        if (result == 1 )
            status.Success = true;
        else {
            status.Message = "no rows updated, or only part of records updated";
            status.Error = connection.Status.Error;
        }
        return status;
    }
    public static Status Delete(Integer id, String language, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        Map parameters = new HashMap();
        parameters.put("@"+f_id, id);
        parameters.put("@"+f_language, language);

        SqlCriteria[] criterias = new SqlCriteria[2];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_id;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_language;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_language;
        criterias[1] = criteria2;

        criterias[0] = criteria1;

        Integer result = connection.delete(tableName, criterias, parameters);
        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows deleted";
        return status;
    }
    public static MeasurementType GetMeasurementType(Integer id, String language, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria[] criterias = new SqlCriteria[2];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_id;
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = f_language;
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@" + f_language;
        criterias[1] = criteria2;

        Map parameters = new HashMap();
        parameters.put("@" + f_id, id);
        parameters.put("@" + f_language, language);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset " + connection.Status.Error);
        }
        List<MeasurementType> measurementTypesList = GetMeasurementTypesFromResultset(rs);
        if (measurementTypesList.size() >= 1) {
            MeasurementType measurementTypes = (MeasurementType) (measurementTypesList.toArray())[0];
            return measurementTypes;
        }
        return  null;
    }

    private static String[] getInsertFields() {
        return new String[]{f_name, f_isSignalStrength, f_isBatteryStatus, f_language};
    }

    private static String[] getInsertValuesParameters() {
        return new String[]{"@"+f_name, "@"+f_isSignalStrength, "@"+f_isBatteryStatus, "@" + f_language};
    }
    private static String[] getUpdateFields() {
        return new String[]{f_name, f_isSignalStrength, f_isBatteryStatus};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{"@"+f_name, "@"+f_isSignalStrength, "@"+f_isBatteryStatus};
    }
    private static String[] getFields() {
        return new String[]{f_id, f_name, f_isSignalStrength, f_isBatteryStatus, f_language};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name, "@"+f_isSignalStrength, "@"+f_isBatteryStatus, "@" + f_language};
    }
    public static List<MeasurementType> GetMeasurementTypes(Connection connection ) throws Exception
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
        List<MeasurementType> measurementTypeList = null;
        try {
            measurementTypeList = GetMeasurementTypesFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  measurementTypeList;
    }

    public static List<MeasurementType> GetMeasurementTypesFromResultset(ResultSet resultSet) throws Exception
    {
        List<MeasurementType> measurementTypes = new ArrayList<MeasurementType>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                MeasurementType measurementType = new MeasurementType();
                measurementType.id = resultSet.getInt(f_id);
                measurementType.name = resultSet.getString(f_name);
                measurementType.isSignalStrength = resultSet.getBoolean(f_isSignalStrength);
                measurementType.isBatteryStatus = resultSet.getBoolean(f_isBatteryStatus);
                measurementType.language = resultSet.getString(f_language);
                measurementTypes.add(measurementType);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return measurementTypes;
    }


}
