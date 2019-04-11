package dk.artogis.hepwat.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;
//import dk.artogis.hepwat.datastore.DataStore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.log4j.Logger;


public class Dashboard {
    // IntelliJ checkin test

    @JsonIgnore
    private static String tableName = "config_dashboard";
    private Integer id ;
    private String title;
    private String cards;

    @JsonIgnore
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static String f_id = "id";
    private static String f_title = "title";
    private static String f_cards = "cards";

    private static String f_componentTableName = "dashboard_table_name" ;

    //region getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }


    //endregion getters and setters
    public Dashboard(Integer  id, String title, String cards)
    {
        this.id = id;
        this.title = title;
        this.cards = cards;
    }

    public Dashboard()
    {

    }

    public Dashboard(ResultSet rs)
    {
        //TODO : fill out if nessecary
    }

    public Status Insert(Connection connection )
    {
        //System.out.println("Dashboard.Insert - start 1");

        Status status = new Status();
        status.Success = false;
        String[] insertFields = null;
        String[] insertValues = null;
        Map parameters = null;

        //System.out.println("Dashboard.Insert - start 2");

        insertFields = getInsertFields();
        insertValues = getInsertValuesParameters();
        parameters = new HashMap();

        //System.out.println("Dashboard.Insert - start 3");
        //System.out.println("Dashboard.Insert - start 3, insertFields: " + insertFields.toString());
        for(int i = 0; i < insertFields.length; i++) {
            //System.out.println(insertFields[i]);
        }
        //System.out.println("Dashboard.Insert - start 3, updateValues: " + insertValues);
        for(int i = 0; i < insertValues.length; i++) {
            //System.out.println(insertValues[i]);
        }

        parameters.put("@"+f_title, this.title);
        parameters.put("@"+f_cards, this.cards);

        //System.out.println("Dashboard.Insert - start 4");

        Integer result = connection.insert(tableName, insertFields, insertValues, parameters, false, null);

        if (result == 1 ) {
            //System.out.println("Dashboard.Insert - start 5");
            status.Success = true;
        }
        else {
            status.Message = "no rows updated, or only part of records updated";
        }
        return status;
    }


    public Status Update(Connection connection)
    {
        System.out.println("Dashboard.Update - start");
        Status status = new Status();
        status.Success = false;
        String[] updateFields = getFields();
        String[] updateValues = getValuesParameters();
        Map parameters = null;

        System.out.println("Dashboard.Update - start 1");

        updateFields = getUpdateFields();
        updateValues = getUpdateValuesParameters();
        parameters = new HashMap();

        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_title, this.title);
        parameters.put("@"+f_cards, this.cards);

        System.out.println("Dashboard.Update - start 2");

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_id;
        criterias[0] = criteria1;

        System.out.println("Dashboard.Update - start 3");
        System.out.println("Dashboard.Update - start 3, updateFields: " + updateFields.toString());
        for(int i = 0; i < updateFields.length; i++) {
            System.out.println(updateFields[i]);
        }
        System.out.println("Dashboard.Update - start 3, updateValues: " + updateValues);
        for(int i = 0; i < updateValues.length; i++) {
            System.out.println(updateValues[i]);
        }

        Integer result = connection.update(tableName, updateFields, updateValues, criterias, parameters);

        System.out.println("Dashboard.Update - start 4");

        if (result == 1 ) {
            System.out.println("Dashboard.Update - start 5");
            status.Success = true;

        }
        else {
            status.Message = "no rows updated, or only part of records updated";
        }

        //System.out.println("Dashboard.Update - end, status JSON: " + status.JsonObject.toString());

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


    public static Dashboard GetDashboard(Integer id, Connection connection ) throws Exception
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
            throw new Exception("error in finding resultset");
        }
        List<Dashboard> dashboards = GetDashboardsFromResultset(rs);
        if (dashboards.size() >= 1) {
            Dashboard sl = (Dashboard) (dashboards.toArray())[0];
            return sl;
        }
        return  null;
    }

    private static String[] getUpdateFields() {
        return new String[]{f_title, f_cards};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{"@"+f_title, "@"+ f_cards};
    }

    private static String[] getFields() {
        return new String[]{f_id, f_title, f_cards};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_title, "@"+f_cards};
    }


    private static String[] getInsertFields() {
        return new String[]{f_title, f_cards};
    }

    private static String[] getInsertValuesParameters() {
        return new String[]{"@"+f_title, "@"+f_cards};
    }
    public static List<Dashboard> GetDashboards(Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};
        System.out.println("GetDashboards - tableName[0] " + tableNames[0]);

        String[] fields = getFields();

        for(int i = 0; i < fields.length; i++) {
            System.out.println("GetDashboards - field " + (i + 1) + ": " + fields[i]);
        }
        Map parameters = new HashMap();
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, null, null, null, 0);
            System.out.println("GetDashboards - result set: " + rs);
        }
        catch (Exception ex) {
            throw new Exception("error in finding resultset");
        }

        List<Dashboard> dashboards = null;

        try {
            dashboards = GetDashboardsFromResultset(rs);
        }
        catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

        return  dashboards;
    }

    public static List<Dashboard> GetDashboardsFromResultset(ResultSet resultSet) throws Exception {
        List<Dashboard> dashboards = new ArrayList<Dashboard>();
        if (resultSet == null) {
            throw new Exception("resultset null");
        }

        try
        {
            while(resultSet.next()) {
                Dashboard dashboard = new Dashboard();
                dashboard.id = resultSet.getInt(f_id);
                dashboard.title = resultSet.getString(f_title);
                dashboard.cards = resultSet.getString(f_cards);

                dashboards.add(dashboard);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return dashboards;
    }



}
