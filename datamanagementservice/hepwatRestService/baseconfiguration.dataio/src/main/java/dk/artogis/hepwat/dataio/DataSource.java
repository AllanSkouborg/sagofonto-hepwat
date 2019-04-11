package dk.artogis.hepwat.dataio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataSource implements Serializable
{
    @JsonIgnore
    public static String tableName = "datasource";
    private Integer id;
    private String name;
    private String url;
    private String authenticationType;
    private String description;
    private String username;
    private String password;
    @JsonIgnore
    private LocalDateTime updated;
    private String dashboardUrl;
    private Boolean robotStarted = true;
    private Boolean notificationOn = false;
    private Integer availability;
    @JsonIgnore
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static String f_id = "datasource_id";
    public static String f_name = "name";
    private static String f_url  = "url";
    private static String f_authenticationType = "authentication_type";
    private static String f_description = "description";
    private static String f_username = "username";
    private static String f_password = "password";
    private static String f_updated = "updated";
    private static String f_dashboardUrl = "dashboard_url";
    private static String f_robotStarted = "robot_started";
    private static String f_notificationOn = "notification_on";
    private static String f_availability = "availability";

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRobotStarted() {
        return robotStarted;
    }

    public void setRobotStarted(Boolean robotStarted) {
        this.robotStarted = robotStarted;
    }

    public Boolean getNotificationOn() {
        return notificationOn;
    }

    public void setNotificationOn(Boolean notificationOn) {
        this.notificationOn = notificationOn;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }


    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }


    //endregion getters and setters

    public DataSource(Integer id, String name, String description, String url, String authenticationType,
                      String username, String password, Boolean notificationOn, String dashboardUrl,
                      Boolean robotStarted, Integer availability, LocalDateTime updated)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.username = username;
        this.password = password;
        this.url = url;
        this.authenticationType = authenticationType;
        this.robotStarted = robotStarted;
        this.notificationOn = notificationOn;
        this.dashboardUrl = dashboardUrl;
        this.availability = availability;
        this.updated = updated;
    }
    public DataSource()
    {

    }
    public DataSource(ResultSet rs)
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

        if ((this.authenticationType != null) && (!this.authenticationType.isEmpty()))
        {
            this.updated = LocalDateTime.parse(authenticationType, dateTimeFormatter);
        }

        parameters = new HashMap();
        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_url, this.url);
        parameters.put("@"+f_authenticationType, this.authenticationType);
        parameters.put("@"+f_description, this.description);
        parameters.put("@"+f_username, this.username);
        parameters.put("@"+f_password, this.password);
        parameters.put("@"+f_dashboardUrl, this.dashboardUrl);
        parameters.put("@"+f_robotStarted, this.robotStarted);
        parameters.put("@"+f_notificationOn, this.notificationOn);
        parameters.put("@"+f_availability, this.availability);
        parameters.put("@"+f_updated, this.updated);

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
        String[] updateFields = getUpdateFields();
        String[] updateValues = getUpdateValuesParameters();
        Map parameters = null;
        parameters = new HashMap();

        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_url, this.url);
        parameters.put("@"+f_authenticationType, this.authenticationType);
        parameters.put("@"+f_description, this.description);
        parameters.put("@"+f_username, this.username);
        parameters.put("@"+f_password, this.password);
        parameters.put("@"+f_dashboardUrl, this.dashboardUrl);
        parameters.put("@"+f_robotStarted, this.robotStarted);
        parameters.put("@"+f_notificationOn, this.notificationOn);
        parameters.put("@"+f_availability, this.availability);
        parameters.put("@"+f_updated, this.updated);

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


    public static DataSource GetDataSource(Integer id, Connection connection ) throws Exception
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
        List<DataSource> dataSources = GetDataSourcesFromResultset(rs);
        if (dataSources.size() >= 1) {
            DataSource dataSource = (DataSource) (dataSources.toArray())[0];
            return dataSource;
        }
        return  null;
    }

    private static String[] getUpdateFields() {
        String[] fields = getFields();
        List<String> fieldsList = new ArrayList<String>();
        fieldsList.addAll(Arrays.asList(fields));
        fieldsList.remove(f_id);
        String[] fieldsArray = null;
        if (fieldsList != null ) {
            fieldsArray = new String[fieldsList.size()];
            fieldsArray = (String[]) fieldsList.toArray(fieldsArray);
        }
        return fieldsArray;
    }

    private static String[] getUpdateValuesParameters() {
        String[] valueParameters = getValuesParameters();
        List<String> valueParametersList = new ArrayList<String>();
        valueParametersList.addAll(Arrays.asList(valueParameters));
        valueParametersList.remove("@"+ f_id);

        String[] valueParametersArray = null;
        if (valueParameters != null ) {
            valueParametersArray = new String[valueParametersList.size()];
            valueParametersArray = (String[]) valueParametersList.toArray(valueParametersArray);
        }
        return valueParametersArray;
    }

    private static String[] getFields() {
        return new String[]{f_id, f_name, f_url, f_authenticationType, f_description,  f_username, f_password,
                f_updated, f_dashboardUrl, f_robotStarted, f_notificationOn, f_availability};
    }

    private static String[] getValuesParameters() {
        String[] fields = getFields();
        List<String> fieldsList = Arrays.asList(fields);
        List<String> valueParameters = new ArrayList<String>();
        for (String field : fieldsList)
        {
            valueParameters.add("@"+ field);
        }
        String[] valueParametersArray = null;
        if (valueParameters != null ) {
            valueParametersArray = new String[valueParameters.size()];
            valueParametersArray = (String[]) valueParameters.toArray(valueParametersArray);
        }
        return  valueParametersArray;
    }
    public static List<DataSource> GetDataSources(Connection connection ) throws Exception
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
        List<DataSource> dataSources = null;
        try {
            dataSources = GetDataSourcesFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " " + connection.Status.Error);
        }

        return  dataSources;
    }

    public static List<DataSource> GetDataSourcesFromResultset(ResultSet resultSet) throws Exception
    {
        List<DataSource> DataSources = new ArrayList<DataSource>();
        if (resultSet == null)
            throw new Exception ("resultset null");
        try
        {
            while(resultSet.next()) {
                DataSource DataSource = new DataSource();
                DataSource.id = resultSet.getInt(f_id);
                DataSource.name = resultSet.getString(f_name);
                DataSource.url = resultSet.getString(f_url);
                DataSource.authenticationType = resultSet.getString(f_authenticationType);
                DataSource.description = resultSet.getString(f_description);
                DataSource.username = resultSet.getString(f_username);
                DataSource.password = resultSet.getString(f_password);
                DataSource.dashboardUrl = resultSet.getString(f_dashboardUrl);
                DataSource.robotStarted = resultSet.getBoolean(f_robotStarted);
                DataSource.notificationOn = resultSet.getBoolean(f_notificationOn);
                DataSource.availability = resultSet.getInt(f_availability);
                Timestamp time = resultSet.getTimestamp(f_updated);
                if (time != null)
                    DataSource.updated = time.toLocalDateTime();
                DataSources.add(DataSource);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return DataSources;
    }

}
