package testCommon;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;

public class PostGreSQLSelectTest {

    public static PostGreSQLConnection conn = null;
    public String tableName = "people";
    public String[] insertFields = null;
    public String[] insertValues = null;
    public Map parameters = null;
    String[] tableNames = null;
    SqlCriteria[] criterias = null;

    @BeforeClass
    static public void OpenDatabase()
    {
        conn = new PostGreSQLConnection(
                TestDBConnection.HOST,
                TestDBConnection.DB_NAME,
                TestDBConnection.USERNAME,
                TestDBConnection.PASSWORD);
        try {
            if (conn.connect()) {
                System.out.println("DB connected");
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("");
        }

    }

    @Before
    public void createFreshTable()
    {
        //TODO: Drop and Create table
        String createCmd = "DROP TABLE  if exists public.people;\n" +
                "\n" +
                "CREATE TABLE public.people\n" +
                "(\n" +
                "  id integer NOT NULL,\n" +
                "  name character(20),\n" +
                "  surname character(20),\n" +
                "  age integer,\n" +
                "  CONSTRAINT people_pkey PRIMARY KEY (id)\n" +
                ")\n" +
                "WITH (\n" +
                "  OIDS=FALSE\n" +
                ");\n" +
                "ALTER TABLE public.people\n" +
                "  OWNER TO postgres;\n";

        conn.DoExecute(createCmd);

        String insertCmd = "INSERT INTO public.people(id, name, surname, age)" +
                " VALUES (1,'Ramsey' , 'Ramseyssesana', 27);" ;

        conn.DoExecute(insertCmd);
    }


    @Test
    public void selectOneRowOn2Parameters() throws  Exception
    {
        tableNames = new String[]{"people"};

        String[] fields = new String[]{"id", "name"};

        SqlCriteria aCriteria = new SqlCriteria();
        aCriteria.FieldName = "name";
        aCriteria.CompareOperator = "=";
        aCriteria.FieldValue = "@name";
         criterias = new SqlCriteria[1];
        criterias[0] = aCriteria;

        parameters = new HashMap();
        parameters.put("@name", "Ramsey");

        boolean hasRow = false;
        try {
            ResultSet rs = conn.select(tableNames, fields, criterias, parameters, null, 0);
            hasRow = rs.next();
        }
        catch (SQLException ex)
        {
            throw new Exception("no resultset found");
        }
        assertTrue("should return resultset with one row", hasRow );
    }

    @Test
    public void selectOneRow3FieldsOn2Parameters() throws  Exception
    {
        tableNames = new String[]{"people"};

        String[] fields = new String[]{"id", "surname", "age"};

        criterias = new SqlCriteria[2];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = "surname";
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@surname";
        criteria1.CriteriaOperand = "AND";
        criterias[0] = criteria1;
        SqlCriteria criteria2 = new SqlCriteria();
        criteria2.FieldName = "age";
        criteria2.CompareOperator = "=";
        criteria2.FieldValue = "@age";
        criterias[1] = criteria2;

        parameters = new HashMap();
        parameters.put("@surname", "Ramseyssesana");
        parameters.put("@age", 27);

        boolean hasRow = false;
        try {
            ResultSet rs = conn.select(tableNames, fields, criterias, parameters, null, 0);
            hasRow = rs.next();
        }
        catch (SQLException ex)
        {
            throw new Exception("no resultset found");
        }
        assertTrue("should return resultset with one row", hasRow );
    }

    @AfterClass
    public static void CloseDBConnection()
    {
        conn.close();
    }
}
