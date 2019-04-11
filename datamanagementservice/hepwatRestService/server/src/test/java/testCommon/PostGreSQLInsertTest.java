package testCommon;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostGreSQLInsertTest {

    public static PostGreSQLConnection conn = null;
    public String tableName = "people";
    public String[] insertFields = null;
    public String[] insertValues = null;
    public Map parameters = null;

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
        String execueCmd = "DROP TABLE  if exists public.people;\n" +
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

        conn.DoExecute(execueCmd);
    }

    @Test
    public void insertTwoFieldsInRightOrderParametersRightOrder()
    {
        tableName = "people";
        insertFields = new String[]{"id", "name"};
        insertValues = new String[]{"@id", "@name"};
        parameters = new HashMap();
        parameters.put("@id", 9);
        parameters.put("@name", "Ramsey");

        assertEquals(1, conn.insert(tableName, insertFields, insertValues, parameters, false, null), "should insert one row" );
    }

    @Test
    public void insertTwoFieldsInWrongOrderParametersRightOrder()
    {
        tableName = "people";
        insertFields = new String[]{"name", "id"};
        insertValues = new String[]{"@name", "@id"};
        Map parameters = new HashMap();
        parameters.put("@id", 10);
        parameters.put("@name", "Ramseys");

        assertEquals(1, conn.insert(tableName, insertFields, insertValues, parameters, false, null), "should insert one row" );
    }

    @Test
    public void insertTwoFieldsInWrongOrderParametersWrongOrder()
    {
        String tableName = "people";
        String[] insertFields = new String[]{"name", "id"};
        String[] insertValues = new String[]{"@name", "@id"};
        Map parameters = new HashMap();
        parameters.put("@name", "Ramseysse");
        parameters.put("@id", 11);

        assertEquals(1, conn.insert(tableName, insertFields, insertValues, parameters, false, null), "should insert one row" );
    }
    @Test
    public void insertTwoFieldsInRightOrderParametersWrongOrder()
    {
        tableName = "people";
        insertFields = new String[]{"id", "name"};
        insertValues = new String[]{"@id", "@name"};
        Map parameters = new HashMap();
        parameters.put("@name", "Ramseyssesa");
        parameters.put("@id", 12);

        assertEquals(1, conn.insert(tableName, insertFields, insertValues, parameters, false, null),"should insert one row");
    }
    @Test
    public void insertFourFieldsWrongOrderParametersWrongOrder()
    {
        tableName = "people";
        insertFields = new String[]{"id", "name", "surname", "age"};
        insertValues = new String[]{"@id", "@name", "@surname", "@age"};
        Map parameters = new HashMap();
        parameters.put("@name", "Ramseyssesana");
        parameters.put("@id", 13);
        parameters.put("@age", 27);
        parameters.put("@surname", "Ramseyssesananans");
        assertEquals(1,conn.insert(tableName, insertFields, insertValues, parameters, false, null),"should insert one row") ;
    }
    @AfterClass
    public static void CloseDBConnection()
    {
        conn.close();
    }

}
