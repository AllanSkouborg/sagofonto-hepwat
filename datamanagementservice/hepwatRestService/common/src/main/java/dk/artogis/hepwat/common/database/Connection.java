package dk.artogis.hepwat.common.database;

import dk.artogis.hepwat.common.utility.Status;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class Connection {

    public Status Status;

    public Connection()
    {


    }
    /// <summary>
    /// Use as prefix to table or field names, if you already have formatted manually or by using FormatTableName or FormatFieldName
    /// </summary>
    public final String NoFormat = "#NOFORMAT#";


    protected String _server = "";
    protected String _port = "";
    protected String _database = "";
    protected String _schema = "";
    protected String _userName = "";
    protected String _password = "";
    protected Status _status = null;



    public ResultSet select(String tableName, String fieldName, SqlCriteria[] criterias, Map<String, Object> parameters, Map<String, String> orderBy, int limit)
    {
        return select(
                new String[] { tableName },
                new String[] { fieldName },
                criterias,
                parameters,
                orderBy,
                limit
        );
    }

    public ResultSet select(String tableName, String[] fieldNames, SqlCriteria[] criterias, Map<String, Object> parameters, Map<String, String> orderBy, int limit)
    {
        return select(
                new String[] { tableName },
                fieldNames,
                criterias,
                parameters,
                orderBy,
                limit
        );
    }

    public ResultSet select(List<String> tableNames, List<String> fieldNames, List<SqlCriteria> criterias, Map<String, Object> parameters, Map<String, String> orderBy, int limit)
    {
        return select(
                (String[]) tableNames.toArray(),
                (String[])fieldNames.toArray(),
                criterias != null ? (SqlCriteria[]) criterias.toArray() : null,
                parameters,
                orderBy,
                limit
        );
    }

    public abstract ResultSet select(String[] tableNames, String[] fieldNames, SqlCriteria[] criterias, Map<String, Object> parameters, Map<String, String> orderBy, int limit);


    public abstract ResultSet SelectByString(String selectCmd, Integer limit );

    public int insert(String tableName, List<String> insertFields, List<String> insertValues, Map<String, Object> parameters , boolean identityInsert , String[] identityColumns)
    {
        return insert(
                tableName,
                (String[])insertFields.toArray(),
                (String[])insertValues.toArray(),
                parameters,
                identityInsert,
                identityColumns
        );
    }

    public abstract int insert(String tableName, String[] insertFields, String[] insertValues, Map<String, Object> parameters, boolean identityInsert , String[] identityColumns);

    public int update(String tableName, List<String> updateFields, List<String> updateValues, List<SqlCriteria> criterias , Map<String, Object> parameters )
    {
        return update(
                tableName,
                (String[])updateFields.toArray(),
                (String[])updateValues.toArray(),
                criterias != null ? (SqlCriteria[])criterias.toArray() : null,
                parameters
        );
    }

    public abstract int update(String tableName, String[] updateFields, String[] updateValues, SqlCriteria[] criterias , Map<String, Object> parameters );

    public int delete(String tableName, List<SqlCriteria> criterias , Map<String, Object> parameters )
    {
        return delete(
                tableName,
                criterias != null ? (SqlCriteria[])criterias.toArray() : null,
                parameters
        );
    }

    public abstract boolean connect() throws SQLException, ClassNotFoundException;

    public abstract int delete(String tableName, SqlCriteria[] criterias , Map<String, Object> parameters );

    public abstract String FormatTableName(String tableName);

    public abstract String FormatFieldName(String fieldName);

    public abstract String SpatialTypeGeomFromText(String WKT, int srId);

    public abstract String FormatBooleanValue(Boolean boolValue);

    public abstract boolean close();

}
