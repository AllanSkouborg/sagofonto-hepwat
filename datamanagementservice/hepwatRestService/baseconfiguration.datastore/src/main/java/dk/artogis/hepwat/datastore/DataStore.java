package dk.artogis.hepwat.datastore;

import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DataStore {

    private static String tableName = "config_data_store";

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String name;
    private String server;
    private String port;
    private String database;
    private String schema;
    private String user;
    private String password;

    private static String f_id = "id";
    private static String f_name = "name";
    private static String f_server = "server";
    private static String f_database = "database";
    private static String f_schema = "schema";
    private static String f_port = "port";
    private static String f_user = "user";
    private static String f_password = "passw";
    private static String f_updated = "updated";


    public DataStore( UUID  id, String name, String server, String port, String database, String schema, String user, String password )
    {
        this.id = id;
        this.database = database;
        this.name = name;
        this.server = server;
        this.schema = schema;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public DataStore()
    {

    }
    public DataStore(UUID id, Connection connection)
    {


    }

    public DataStore(ResultSet rs)
    {


    }
    public Status Insert(Connection connection)
    {
        String[] insertFields = getFields();
        String[] insertValues = getValuesParameters();
        Map parameters = null;

        insertFields = getFields();
        insertValues = getValuesParameters();
        parameters = new HashMap();

        if (this.id == null)
            this.id = UUID.randomUUID();

        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_server, this.server);
        parameters.put("@"+f_port, this.port);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+f_schema, this.schema);
        parameters.put("@"+f_user, this.user);
        parameters.put("@"+f_password, this.password);
        parameters.put("@"+f_database, this.database);

        Integer result = connection.insert(tableName, insertFields, insertValues, parameters, false, null);

        Status status = new Status();
        if (result == 1 )
            status.Success = true;
        else {
            status.Success = false;
            status.Message = "no rows updated";
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
        parameters.put("@"+f_database, this.database);
        parameters.put("@"+f_server, this.server);
        parameters.put("@"+f_schema, this.schema);
        parameters.put("@"+f_user, this.user);
        parameters.put("@"+f_password, this.password);
        parameters.put("@"+f_port, this.port);

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_id;
        criterias[0] = criteria1;

        Integer result = connection.update(tableName, updateFields, updateValues, criterias, parameters);

        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows updated, or only part of records updated";
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

    public static DataStore GetDataStore( UUID id, Connection connection ) throws Exception
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
        List<DataStore> dataStores = GetDataStoresFromResultset(rs);
        if (dataStores.size() >= 1)
            return (DataStore) (dataStores.toArray())[0];
        return  null;
    }

    private static String[] getFields() {
        return new String[]{f_id, f_name, f_server, f_port, f_schema, f_database, f_user, f_password};
    }
    private static String[] getValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name, "@"+f_server, "@"+f_port, "@"+f_schema, "@"+f_database, "@"+f_user, "@"+f_password};
    }

    private static String[] getUpdateFields() {
        return new String[]{f_id, f_name, f_server, f_port, f_schema, f_database, f_user, f_password};
    }
    private static String[] getUpdateValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name, "@"+f_server, "@"+f_port, "@"+f_schema, "@"+f_database, "@"+f_user, "@"+f_password};
    }
    public static List<DataStore> GetDataStores(Connection connection ) throws  Exception
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
        List<DataStore> dataStores = null;
        try {
            dataStores = GetDataStoresFromResultset(rs);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage());
        }

        return  dataStores;
    }

    public static List<DataStore> GetDataStoresFromResultset(ResultSet resultSet)
    {
        List<DataStore> dataStores = new ArrayList<DataStore>();
        try
        {
            while(resultSet.next()) {
                DataStore ds = new DataStore();
                ds.id = resultSet.getObject(f_id, UUID.class);
                ds.name = resultSet.getString(f_name);
                ds.server = resultSet.getString(f_server);
                ds.port = resultSet.getString(f_port);
                ds.user = resultSet.getString(f_user);
                ds.password = resultSet.getString(f_password);
                ds.schema = resultSet.getString(f_schema);
                ds.database = resultSet.getString(f_database);
                dataStores.add(ds);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            return  dataStores;
        }
        return dataStores;
    }
}
