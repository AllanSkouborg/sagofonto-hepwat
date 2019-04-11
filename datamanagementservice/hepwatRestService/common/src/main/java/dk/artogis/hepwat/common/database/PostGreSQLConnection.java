package dk.artogis.hepwat.common.database;



import dk.artogis.hepwat.common.utility.Status;

import java.sql.Connection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import org.apache.log4j.Logger;


public class PostGreSQLConnection extends dk.artogis.hepwat.common.database.Connection {

        private Connection conn;
        private String host;
        private String dbName;
        private String user;
        private String pass;
        private Logger logger = Logger.getLogger(PostGreSQLConnection.class);

        //we don't like this constructor
        protected PostGreSQLConnection() {}

        public PostGreSQLConnection(String host, String dbName, String user, String pass) {
            this.host = host;
            this.dbName = dbName;
            this.user = user;
            this.pass = pass;
        }
        public PostGreSQLConnection(String server, String port, String dbName, String schema, String user, String pass) {
            this.host = "jdbc:postgresql://" + server+ ":"+ port + "/";
            this.dbName = dbName;
            this.user = user;
            this.pass = pass;
            this._schema = schema;
        }

        public boolean connect() throws SQLException, ClassNotFoundException {

            Logger logger = Logger.getLogger(PostGreSQLConnection.class);
            if(logger.isTraceEnabled()) {
                logger.trace("Entering connect");
            }
            Status = new Status();
            if (host.isEmpty() || dbName.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                throw new SQLException("Database credentials missing");
            }

            Class.forName("org.postgresql.Driver");
            String url = this.host + this.dbName;

            if ((_schema != null) && (!_schema.isEmpty()))
                url = url + "?searchpath=" + _schema;

            this.conn = DriverManager.getConnection(
                    url,
                    this.user, this.pass);

            if(logger.isTraceEnabled())
                logger.trace("Leaving connect");
            return true;
        }

        public boolean close()
        {
            try {
                if (conn != null)
                    conn.close();

            }
            catch (Exception ex)
            {
                logger.error("Error on close connection");
                return  false;
            }
            return  true;
        }
        public int insert(String table, Map values) throws SQLException {
            if(logger.isTraceEnabled()) {
                logger.trace("Entering insert");
            }

            Status = new Status();
            StringBuilder columns = new StringBuilder();
            StringBuilder vals = new StringBuilder();

            for (Object col : values.keySet()) {
                columns.append(col).append(",");

                if (values.get(col) instanceof String) {
                    vals.append("'").append(values.get(col)).append("', ");
                }
                else vals.append(values.get(col)).append(",");
            }

            columns.setLength(columns.length()-1);
            vals.setLength(vals.length()-1);

            String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table,
                    columns.toString(), vals.toString());
            Integer result = this.conn.createStatement().executeUpdate(query);

            if(logger.isTraceEnabled()) {
                logger.trace("Leaving insert");
            }
            return result;
        }

         public ResultSet execQuery(String query) throws SQLException {

            return this.conn.createStatement().executeQuery(query);
        }
    public boolean DoExecute(String executeCmd)
    {
        if(logger.isTraceEnabled()) {
            logger.trace("Entering DoExecute");
        }
        boolean resultOk = false;
        Status = new Status();
        Connection connection = this.conn;
        PreparedStatement  statement = null;
        try
        {
            if (logger.isTraceEnabled())
                logger.trace("executeCmd : " + executeCmd);
            statement = connection.prepareStatement(executeCmd);
            resultOk = statement.execute();
            Status.Success = true;
        }
        catch (Exception ex)
        {
            logger.error("error in DoExecute : " + ex.getMessage());
            Status.Success = false;
            Status.Error = ex.getMessage();
        }
        if(logger.isTraceEnabled()) {
            logger.trace("Leaving DoExecute");
        }
        return resultOk;
    }
    @Override
    public int update(String tableName, String[] updateFields, String[] updateValues, SqlCriteria[] criterias, Map<String, Object> parameters) {

        if(logger.isTraceEnabled()) {
            logger.trace("Entering update");
        }
        StringBuilder sbExecuteCmd = new StringBuilder();
        Boolean first = true;
        Status = new Status();

        sbExecuteCmd.append(String.format("UPDATE %s SET ", FormatTableName(tableName)));

        first = true;
        for (int c = 0; c < updateFields.length; c++)
        {
            if (updateValues.length > c)
            {
                String updateField = updateFields[c];
                String updateValue = updateValues[c];

                if (updateField != null && !updateField.isEmpty())
                {
                    if (!first) sbExecuteCmd.append(",");
                    sbExecuteCmd.append(String.format(" %s=%s", FormatFieldName(updateField), updateValue));
                    first = false;
                }
            }
        }
        sbExecuteCmd.append(" ");

        if (criterias != null && criterias.length > 0)
        {
            sbExecuteCmd.append("WHERE ");
            first = true;
            for (int c = 0; c < criterias.length; c++)
            {
                String criteriaField = criterias[c].FieldName;
                String criteriaValue = criterias[c].FieldValue;
                String compareOperator = criterias[c].CompareOperator;

                if (criteriaField != null && !criteriaField.isEmpty())
                {
                    if (!first) sbExecuteCmd.append(String.format(" %s", criterias[c - 1].CriteriaOperand));
                    if (compareOperator != null && compareOperator.isEmpty()) compareOperator = "=";
                    sbExecuteCmd.append(String.format(" %s %s %s", FormatFieldName(criteriaField), compareOperator, criteriaValue));
                    first = false;
                }

            }
            sbExecuteCmd.append(" ");
        }


        if(logger.isTraceEnabled()) {
            logger.trace("sbExecuteCmd : " + sbExecuteCmd.toString());
            logger.trace("Leaving update");
        }
        return DoExecute(sbExecuteCmd.toString(), parameters);
    }





    @Override
    public int delete(String tableName, SqlCriteria[] criterias, Map<String, Object> parameters) {

        if(logger.isTraceEnabled()) {
            logger.trace("Entering delete");
        }

        StringBuilder sbExecuteCmd = new StringBuilder();
        boolean first = true;
        Status = new Status();
        sbExecuteCmd.append(String.format("DELETE FROM %s ", FormatTableName(tableName)));

        if (criterias != null && criterias.length > 0)
        {
            sbExecuteCmd.append("WHERE ");
            first = true;
            for (int c = 0; c < criterias.length; c++)
            {
                String criteriaField = criterias[c].FieldName;
                String criteriaValue = criterias[c].FieldValue;
                String compareOperator = criterias[c].CompareOperator;

                if (criteriaField != null && !criteriaField.isEmpty())
                {
                    if (!first) sbExecuteCmd.append(String.format(" %s", criterias[c - 1].CriteriaOperand));
                    if (compareOperator!= null && compareOperator.isEmpty()) compareOperator = "=";
                    sbExecuteCmd.append(String.format(" %s %s %s", FormatFieldName(criteriaField), compareOperator, criteriaValue));
                    first = false;
                }

            }
            sbExecuteCmd.append(" ");
        }

        if (logger.isTraceEnabled())
            logger.trace("sbExecuteCmd : " + sbExecuteCmd.toString());
        Integer result = DoExecute(sbExecuteCmd.toString(), parameters);

        if(logger.isTraceEnabled()) {
            logger.trace("Leaving delete");
        }
        return result;
    }


    @Override
    public ResultSet select(String[] tableNames, String[] fieldNames, SqlCriteria[] criterias, Map<String, Object> parameters, Map<String, String> orderBy, int limit) {

        if(logger.isTraceEnabled()) {
            logger.trace("Entering select");
        }

        StringBuilder selectCmd = new StringBuilder();
        boolean first = true;
        Status = new Status();
        selectCmd.append("SELECT ");
        first = true;
        for (Integer f = 0; f < fieldNames.length; f++ )
        {
            String fieldName = fieldNames[f];
            if (fieldName != null && !fieldName.isEmpty())
            {
                if (!first) selectCmd.append(",");
                selectCmd.append(FormatFieldName(fieldName));
                first = false;
            }
        }
        selectCmd.append(" ");

        selectCmd.append("FROM ");
        first = true;
        for (Integer t = 0; t < tableNames.length; t++ )
        {
            String tableName = tableNames[t];
            if (tableName != null && !tableName.isEmpty())
            {
                if (!first) selectCmd.append(",");
                selectCmd.append(FormatTableName(tableName));
                first = false;
            }
        }
        selectCmd.append(" ");

        if (criterias != null && criterias.length > 0)
        {
            selectCmd.append("WHERE ");
            first = true;
            for (int c = 0; c < criterias.length; c++)
            {
                String criteriaField = criterias[c].FieldName;
                String criteriaValue = criterias[c].FieldValue;
                String compareOperator = criterias[c].CompareOperator;

                if (criteriaField != null && !criteriaField.isEmpty())
                {
                    if (!first) selectCmd.append(String.format(" %s", criterias[c-1].CriteriaOperand));
                    if (compareOperator != null && compareOperator.isEmpty()) compareOperator = "=";
                    selectCmd.append(String.format(" %s %s %s", FormatFieldName(criteriaField), compareOperator, criteriaValue));
                    first = false;
                }

            }
            selectCmd.append(" ");
        }

        if (orderBy != null && orderBy.size() > 0)
        {
            selectCmd.append(" ORDER BY");
            first = true;
            for (String orderByFieldName : orderBy.keySet()) {
                if (orderByFieldName != null && !orderByFieldName.isEmpty())
                {
                    if (!first) selectCmd.append(",");
                    String orderByDir =  orderBy.get(orderByFieldName);//orderBy[orderByFieldName];
                    if (orderByDir != null && !orderByDir.isEmpty()) orderByDir = String.format(" %s", orderByDir); else orderByDir = "";
                    selectCmd.append(FormatFieldName(orderByFieldName) + orderByDir);
                    first = false;
                }
            }
        }

        if (limit > 0) selectCmd.append(String.format(" LIMIT %d ", limit));

        String  selectCommand = selectCmd.toString();

        if (logger.isTraceEnabled())
            logger.trace("selectCommand : " + selectCommand);

        ResultSet resultSet = DoSelect(selectCommand, parameters);

        if(logger.isTraceEnabled()) {
            logger.trace("Leaving select");
        }
        return  resultSet;

    }

    @Override
    public ResultSet SelectByString(String selectCmd, Integer limit )
    {
        if (limit > 0) selectCmd += " LIMIT " + limit.toString();
        return DoSelect(selectCmd, null);
    }


    protected  int DoExecute(String executeCmd, Map<String, Object> parameters )
    {

        if(logger.isTraceEnabled()) {
            logger.trace("Entering DoExecute with paramters");
        }

        int rowsaffected = -1;

        Status = new Status();
        Connection connection = this.conn;
        PreparedStatement  statement = null;
        List<OrderedParameter> orderedParameters = new ArrayList<OrderedParameter>();

        try
        {
            if(logger.isTraceEnabled())
                logger.trace("executeCmd before parse parameters : " + executeCmd);
            executeCmd = ParseParameters(executeCmd, parameters,orderedParameters);
            if(logger.isTraceEnabled())
                logger.trace("executeCmd after parse parameters: " + executeCmd);
            statement = connection.prepareStatement(executeCmd);
            if (!SetParameters(executeCmd, parameters, orderedParameters, statement))
                throw new Exception("Could not set parameters in prepared statement");
            Status.Success = true;
        }
        catch (Exception ex)
        {
            Status.Success = false;
            Status.Error = ex.getMessage();
            logger.error("error in parsing parameters or setting parameters : " + ex.getMessage());
            return rowsaffected;
        }

        // carry out update
        try
        {
            rowsaffected = statement.executeUpdate();
            Status.Success = true;
        }
        catch (Exception ex)
        {
            logger.error("error in DoExecute with parameters : " + ex.getMessage());
            Status.Success = false;
            Status.Error = ex.getMessage();
        }

        if(logger.isTraceEnabled()) {
            logger.trace("Leaving DoExecute with paramters");
        }

        return rowsaffected;
    }

    protected  int DoExecuteWithResult(String executeCmd, Map<String, Object> parameters, String identityColumn )
    {
        if(logger.isTraceEnabled()) {
            logger.trace("Entering DoExecuteWithResult");
        }

        int newId = -1;

        Status = new Status();
        Connection connection = this.conn;
        PreparedStatement  statement = null;
        List<OrderedParameter> orderedParameters = new ArrayList<OrderedParameter>();

        try
        {
            if(logger.isTraceEnabled())
                logger.trace("executeCmd before parse parameters : " + executeCmd);
            executeCmd = ParseParameters(executeCmd, parameters,orderedParameters);
            if(logger.isTraceEnabled())
                logger.trace("executeCmd after parse parameters : " + executeCmd);
            statement = connection.prepareStatement(executeCmd);
            if (!SetParameters(executeCmd, parameters, orderedParameters, statement))
                throw new Exception("Could not set parameters in prepared statement");
            Status.Success = true;
        }
        catch (Exception ex)
        {
            Status.Success = false;
            Status.Error = ex.getMessage();
            logger.error("error in parsing parameters : " + ex.getMessage());
            return newId;
        }

        // carry out update
        try
        {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                newId = resultSet.getInt(identityColumn);
            Status.Success = true;
        }
        catch (Exception ex)
        {
            logger.error("error in executing statement : " + ex.getMessage());
            Status.Success = false;
            Status.Error = ex.getMessage();
        }

        if(logger.isTraceEnabled()) {
            logger.trace("Leaving DoExecuteWithResult");
        }
        return newId;
    }
    private String ParseParameters (String executeCmd, Map<String, Object> parameters,  List<OrderedParameter> orderedParameters )
    {
        if(logger.isTraceEnabled()) {
            logger.trace("Entering ParseParameters");
        }
        Integer numberOfParameters = 0;
        String executeCmdTmp = executeCmd;
        if (parameters != null && (!parameters.isEmpty())) {
            while (executeCmdTmp.contains("@")) {
                for (String key : parameters.keySet()) {
                    if (executeCmdTmp.contains(key)) {
                        Integer position = executeCmdTmp.indexOf(key);
                        executeCmdTmp = executeCmdTmp.replaceFirst( key , "#"+key.substring(1,key.length()));
                        orderedParameters.add( new OrderedParameter(key, position) );
                        numberOfParameters++;
                        if (numberOfParameters > 1000)
                            throw new ArithmeticException("Too many parameters");
                    }
                }
            }
            numberOfParameters = 0;
            while (executeCmd.contains("@")) {
                for (String key : parameters.keySet()) {
                    if (executeCmd.contains(key)) {
                        if ((parameters.get(key) instanceof Timestamp)  )
                            executeCmd = executeCmd.replaceFirst( key , "CAST (? AS time)");
                        else
                            executeCmd = executeCmd.replaceFirst( key , "?");
                        numberOfParameters++;
                        if (numberOfParameters > 1000)
                            throw new ArithmeticException("Too many parameters");
                    }
                }
            }
        }
        if(logger.isTraceEnabled()) {
            logger.trace("Leaving ParseParameters");
            logger.trace("executeCmd : " + executeCmd);
        }
        return  executeCmd;
    }

    private boolean SetParameters (String executeCmd, Map<String, Object> parameters, List<OrderedParameter> orderedParameters, PreparedStatement statement)
    {
        if(logger.isTraceEnabled()) {
            logger.trace("Entering SetParameters");
        }
        try {
            /// replace values from parameters
            if ((orderedParameters != null) &&(!orderedParameters.isEmpty())) {
                Collections.sort(orderedParameters);
                for (Integer parameterIndex = 0 ; parameterIndex < orderedParameters.size(); parameterIndex++) {
                    Object value = parameters.get(orderedParameters.get(parameterIndex).key);
                    if (value == null)
                        statement.setNull(parameterIndex + 1, Types.NULL);
                    if (value instanceof String)
                        if (value != null)
                            statement.setString(parameterIndex + 1, (String) value);
                        else statement.setNull(parameterIndex + 1, Types.NVARCHAR);
                    else if (value instanceof Integer)
                        if (value != null)
                            statement.setInt(parameterIndex + 1, (Integer) value);
                        else statement.setNull(parameterIndex + 1, Types.INTEGER );
                    else if (value instanceof Boolean) {
                        if (value != null)
                            statement.setBoolean(parameterIndex + 1,(Boolean) value );
                        else statement.setNull(parameterIndex + 1, Types.BIT);
                    }
                    else if (value instanceof LocalTime)
                        if (value != null)
                            statement.setObject(parameterIndex + 1, value);
                        else statement.setNull(parameterIndex + 1, Types.TIME);
                    else if (value instanceof LocalDate)
                        if (value !=  null)
                            statement.setObject(parameterIndex + 1, value);
                        else statement.setNull(parameterIndex + 1, Types.DATE);
                    else if (value instanceof LocalDateTime)
                        if (value != null)
                            statement.setObject(parameterIndex + 1, value);
                        else statement.setNull(parameterIndex + 1, Types.TIMESTAMP_WITH_TIMEZONE);
                    else if (value instanceof Float)
                        if (value != null)
                            statement.setFloat(parameterIndex + 1, ((Float)value));
                        else statement.setNull(parameterIndex + 1, Types.FLOAT );
                    else if (value instanceof Double)
                        if (value != null)
                            statement.setDouble(parameterIndex + 1, ((Double)value));
                        else statement.setNull(parameterIndex + 1, Types.DOUBLE);
                    else if (value instanceof UUID)
                        if (value != null)
                            statement.setObject(parameterIndex+1, value);
                        else statement.setNull(parameterIndex + 1, Types.NULL);

                }
            }
        }
        catch (Exception ex)
        {
            Status.Success = false;
            Status.Error = ex.getMessage();
            logger.error("error in setting parameters : " + ex.getMessage());
            return false;
        }
        if(logger.isTraceEnabled()) {
            logger.trace("Leaving SetParameters");
        }
            return true;
    }



    private ResultSet DoSelect(String selectCmd, Map<String, Object> parameters ) {

        if(logger.isTraceEnabled()) {
            logger.trace("Entering DoSelect");
        }
        ResultSet dsRet = null;

        String selectCmdBeforeReplaceKey = selectCmd;
        Connection connection = this.conn;
        PreparedStatement  statement = null;
        List<OrderedParameter> orderedParameters = new ArrayList<OrderedParameter>();
        Integer numberOfUsedParameters = 0;
        try
        {
            if(logger.isTraceEnabled())
                logger.trace("executeCmd before parse parameters : " + selectCmd);
            selectCmd = ParseParameters(selectCmd, parameters, orderedParameters);
            if(logger.isTraceEnabled())
                logger.trace("executeCmd after parse parameters : " + selectCmd);
            statement = connection.prepareStatement(selectCmd);
            if (!SetParameters(selectCmd, parameters, orderedParameters, statement))
                throw new Exception("Could not set parameters in prepared statement for selectstatemetn");
            Status.Success = true;
        }
        catch (Exception ex)
        {
            Status.Success = false;
            Status.Error = ex.getMessage();
            logger.error("error in parsing or setting parameters : " + ex.getMessage());
            if(logger.isInfoEnabled())
                logger.info("selectCmd : "+ selectCmd);
            return dsRet;
        }

        try
        {
            dsRet = statement.executeQuery();
            Status.Success = true;
        }
        catch (Exception ex)
        {
            dsRet = null;
            Status.Success = false;
            Status.Error = ex.getMessage();
        }
        if(logger.isTraceEnabled()) {
            logger.trace("Leaving DoSelect");
        }
        return dsRet;
    }

    @Override
    public int insert(String tableName, String[] insertFields, String[] insertValues, Map<String, Object> parameters, boolean identityInsert, String[] identityColumns) {

        if(logger.isTraceEnabled()) {
            logger.trace("Entering insert");
        }
        Status = new Status();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        boolean first = true;
        for (int c = 0; c < insertFields.length; c++)
        {
            String insertField = insertFields[c];
            if ((insertField != null)&& (!insertField.isEmpty()))
            {
                if (!first) columns.append(",");
                columns.append(FormatFieldName(insertField));
                first = false;
            }
        }

        first = true;
        for (int v = 0; v < insertValues.length; v++)
        {
            String insertValue = insertValues[v];
            if ((insertValue != null)&& (!insertValue.isEmpty()))
            {
                if (!first) values.append(",");
                values.append(insertValue);
                first = false;
            }
        }

        String query = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName,
                columns.toString(), values.toString());

        int result = -1;
        if (identityInsert && identityColumns != null && identityColumns.length == 1 ) //can only handle one identity column
        {
//            for (int i = 0; i < identityColumns.length; i++)
//            {
//                String colName = identityColumns[i];
//                if(colName != null && !colName.isEmpty())
//                    query = query + (", select setval(pg_get_serial_sequence('" + FormatTableName(tableName) + "', '" + colName + "'), (select max(" + FormatFieldName(colName) + ") from " + FormatTableName(tableName) + "));");
//            }

            for (int i = 0; i < identityColumns.length; i++)
            {
                String colName = identityColumns[i];
                if(colName != null && !colName.isEmpty())
                    query = query + " RETURNING " + colName ;
            }

            try {
                if (logger.isTraceEnabled())
                    logger.trace("query : " + query );
                result = DoExecuteWithResult(query, parameters, identityColumns[0]);
            }
            catch (Exception ex)
            {
                logger.error("error in executing insert with result : " + ex.getMessage());
                if (logger.isInfoEnabled())
                    logger.info("query : " + query );
                Status.Error = ex.getMessage();
            }
        }
        else
        {
            try {
                result = DoExecute(query, parameters);
            }
            catch (Exception ex)
            {
                logger.error("error in executing insert : " + ex.getMessage());
                if (logger.isInfoEnabled())
                    logger.info("query : " + query );
                Status.Error = ex.getMessage();
            }
        }
        if(logger.isTraceEnabled()) {
            logger.trace("Leaving insert");
        }
        return result;

    }

    @Override
    public  String FormatTableName(String tableName)
    {
        StringBuilder sb = new StringBuilder();

        if ( _schema != null &&  !_schema.isEmpty())
        {
            sb.append("\"" + _schema + "\".");
        }
        if ( tableName != null && !tableName.isEmpty())
        {
            sb.append("\"" + tableName + "\"");
        }

        return sb.toString().replace("\"\"", "\"");
    }
    @Override
    public  String FormatFieldName(String fieldName)
    {
        StringBuilder sb = new StringBuilder();

        if (fieldName != null && !fieldName.isEmpty())
        {
            if (fieldName.startsWith(NoFormat)) return fieldName.substring(NoFormat.length());
            if (fieldName.equals("*")) return fieldName;
            if (fieldName.contains("."))
            {
                String[] tkns = fieldName.split(".");
                boolean first = true;
                for ( Integer stringPartIndex = 0 ; stringPartIndex < tkns.length ; stringPartIndex ++)
                {
                    String tkn = tkns[stringPartIndex];
                    if (tkn != null && !tkn.isEmpty())
                    {
                        if (first)
                            sb.append("\"" + tkn + "\"");
                        else
                            sb.append(".\"" + tkn + "\"");
                        first = false;
                    }
                }
            }
            else
                sb.append("\"" + fieldName + "\"");
        }

        return sb.toString();
    }
    @Override
    public  String FormatBooleanValue(Boolean boolValue)
    {
        if (boolValue.booleanValue())
            return "true";
        else
            return "false";
    }
    @Override
    public  String SpatialTypeGeomFromText(String WKT, int srId )
    {
        return String.format("public.ST_GeomFromText('%s', %d)", WKT, srId);
    }
}
