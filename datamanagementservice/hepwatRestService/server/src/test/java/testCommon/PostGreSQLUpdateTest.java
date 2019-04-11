package testCommon;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;


public class PostGreSQLUpdateTest {

    public static PostGreSQLConnection conn = null;
    public String tableName = "people";
    public String[] updateFields = null;
    public String[] updateValues = null;
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
    public void updateOneRowOn2ParametersOnCriteriaString() throws  Exception
    {

        tableName = "people";
        updateFields = new String[]{"surname", "age"};
        updateValues = new String[]{"@surname", "@age"};
        Map parameters = new HashMap();

        parameters.put("@age", 26);
        parameters.put("@surname", "Ramseyssesnananan");
        parameters.put("@name", "Ramsey");

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = "name";
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@name";

        criterias[0] = criteria1;

        assertEquals("should update one row",1,  conn.update(tableName, updateFields, updateValues, criterias, parameters));
    }

    @Test
    public void upeateOneRowOn2ParametersOnCriteriaInteger() throws  Exception
    {
        String tableName = "people";
        String[] updateFields = new String[]{"age", "name"};
        String[] updateValues = new String[]{"@age", "@name" };
        Map parameters = new HashMap();

        parameters.put("@age", 25);
        parameters.put("@name", "Rasmus");
        parameters.put("@id", 1);

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = "id";
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@id";

        criterias[0] = criteria1;

        assertEquals("should update one row",1, conn.update(tableName, updateFields, updateValues, criterias, parameters));
    }

    @AfterClass
    public static void CloseDBConnection()
    {
        conn.close();
    }
}
