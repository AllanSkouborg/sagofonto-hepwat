package testCommon;

import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostGreSQLInsertDifferentTypesTest {

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
        String execueCmd = "DROP TABLE if exists public.customers ;\n" +
                "\n" +
                "CREATE TABLE public.customers\n" +
                "(\n" +
                "  id integer NOT NULL,\n" +
                "  name character(20),\n" +
                "  surname character(20),\n" +
                "  age integer,\n" +
                "  createdate date,\n" +
                "  lastbuydatetime timestamp without time zone,\n" +
                "  lastbuytime time without time zone,\n" +
                "  total double precision,\n" +
                "  lastbuy double precision,\n" +
                "  vactionperiod interval,\n" +
                "  isadult boolean,\n" +
                "  uniqueid uuid,\n" +
                "  CONSTRAINT customers_pkey PRIMARY KEY (id)\n" +
                ")\n" +
                "WITH (\n" +
                "  OIDS=FALSE\n" +
                ");\n" +
                "ALTER TABLE public.customers\n" +
                "  OWNER TO postgres;";

        conn.DoExecute(execueCmd);
    }

    @Test
    public void insertFieldsInRightOrderParametersRightOrder()
    {
        tableName = "customers";
        insertFields = new String[]{"id", "name", "surname", "age", "createdate", "lastbuydatetime", "lastbuytime", "total", "lastbuy", "vactionperiod", "isadult", "uniqueid"};
        insertValues = new String[]{"@id", "@name","@surname", "@age", "@createdate", "@lastbuydatetime", "@lastbuytime", "@total", "@lastbuy", "@vactionperiod", "@isadult", "@uniqueid"};
        parameters = new HashMap();
        Boolean isAdult = true;
        Double  total = 2.22;
        Float lastBuy = 1.115f;
        UUID uniqueId = UUID.fromString("994c8502-5afe-44a9-9702-67d094bf5b09");
        String createDateString = "14-01-2018";
        String lastbuydatetimeString = "2016-11-09 10:30";
        String lastbouytimeString = "10:30";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
        LocalDateTime lastbuydatetime = LocalDateTime.parse(lastbuydatetimeString, dateTimeFormatter);
        LocalDate createdate = LocalDate.parse(createDateString, dateFormatter);
        LocalTime lastbuytime = LocalTime.parse(lastbouytimeString, timeFormatter );

        parameters.put("@id", 2);
        parameters.put("@name", "Ramsey");
        parameters.put("@surname", "Ramseyson");
        parameters.put("@age", 17);
        parameters.put("@createdate", createdate);
        parameters.put("@lastbuydatetime",lastbuydatetime);
        parameters.put("@lastbuytime", lastbuytime);
        parameters.put("@total", total);
        parameters.put("@lastbuy", lastBuy);
        parameters.put("@vactionperiod", null);
        parameters.put("@isadult", isAdult );
        parameters.put("@uniqueid", uniqueId);

        assertEquals(1, conn.insert(tableName, insertFields, insertValues, parameters, false, null), "should insert one row" );
    }
    @Test
    public void insertFieldsInRightOrderParametersRightOrderNotAllFields()
    {
        tableName = "customers";
        insertFields = new String[]{"id", "name", "surname", "age", "createdate",  "lastbuytime", "total", "lastbuy",  "isadult", "uniqueid"};
        insertValues = new String[]{"@id", "@name","@surname", "@age", "@createdate",  "@lastbuytime", "@total", "@lastbuy",  "@isadult", "@uniqueid"};
        parameters = new HashMap();
        Boolean isAdult = false;
        Double  total = 2.22;
        Float lastBuy = 1.115f;
        UUID uniqueId = UUID.fromString("994c8502-5afe-44a9-9702-67d094bf5b09");
        String createDateString = "17-01-2018";
        String lastbuydatetimeString = "2017-11-09 10:30";
        String lastbouytimeString = "11:30";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
        LocalDateTime lastbuydatetime = LocalDateTime.parse(lastbuydatetimeString, dateTimeFormatter);
        LocalDate createdate = LocalDate.parse(createDateString, dateFormatter);
        LocalTime lastbuytime = LocalTime.parse(lastbouytimeString, timeFormatter );

        parameters.put("@id", 3);
        parameters.put("@name", "Bo ");
        parameters.put("@surname", "Boson");
        parameters.put("@age", 15);
        parameters.put("@createdate", createdate);
        parameters.put("@lastbuytime", lastbuytime);
        parameters.put("@total", total);
        parameters.put("@lastbuy", lastBuy);
        parameters.put("@isadult", isAdult );
        parameters.put("@uniqueid", uniqueId);

        assertEquals(1, conn.insert(tableName, insertFields, insertValues, parameters, false, null), "should insert one row" );
    }

    @AfterClass
    public static void CloseDBConnection()
    {
        conn.close();
    }

}
