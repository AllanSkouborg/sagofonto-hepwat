package testCommon;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.common.database.SqlCriteria;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class TestPostGreSQLConnection {

    public static void main(String[] args) {

//        insertTests();
//        selectTests();
//        updateTests();
        deleteTests();
    }

    static void insertTests() {
        PostGreSQLConnection conn = new PostGreSQLConnection(
                TestDBConnection.HOST,
                TestDBConnection.DB_NAME,
                TestDBConnection.USERNAME,
                TestDBConnection.PASSWORD);
        try {
            if (conn.connect()) {
                System.out.println("DB connected");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (conn != null) {

            Test3(conn);
            Test4(conn);
            Test5(conn);
            Test6(conn);
            Test7(conn);
        }
    }

    static void selectTests() {
        PostGreSQLConnection conn = new PostGreSQLConnection(
                TestDBConnection.HOST,
                TestDBConnection.DB_NAME,
                TestDBConnection.USERNAME,
                TestDBConnection.PASSWORD);
        try {
            if (conn.connect()) {
                System.out.println("DB connected");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (conn != null) {

            Test10(conn);
            Test11(conn);
        }
    }

    static void updateTests() {
        PostGreSQLConnection conn = new PostGreSQLConnection(
                TestDBConnection.HOST,
                TestDBConnection.DB_NAME,
                TestDBConnection.USERNAME,
                TestDBConnection.PASSWORD);
        try {
            if (conn.connect()) {
                System.out.println("DB connected");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (conn != null) {

            Test12(conn);
            Test13(conn);
        }
    }


    static void deleteTests() {
        PostGreSQLConnection conn = new PostGreSQLConnection(
                TestDBConnection.HOST,
                TestDBConnection.DB_NAME,
                TestDBConnection.USERNAME,
                TestDBConnection.PASSWORD);
        try {
            if (conn.connect()) {
                System.out.println("DB connected");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (conn != null) {
            Test14(conn);
        }
    }

    static void Test1() {
        PostGreSQLConnection client = new PostGreSQLConnection(
                TestDBConnection.HOST,
                TestDBConnection.DB_NAME,
                TestDBConnection.USERNAME,
                TestDBConnection.PASSWORD);

        try {
            if (client.connect()) {
                System.out.println("DB connected");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


        try {
            ResultSet rs = client.execQuery("SELECT * FROM people");

            while (rs.next()) {

                System.out.printf("%d\t%s\t%s\t%d\n",
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4));
            }
        } catch (Exception ex) {


        }

        try {
            Map vals = new HashMap();

            vals.put("id", 5);
            vals.put("name", "Aldox");
            vals.put("surname", "Ziflaj");
            vals.put("age", 16);
            if (client.insert("people", vals) == 1) {
                System.out.println("Record added");
            }
        } catch (Exception ex) {

        }

    }


    static void Test3(PostGreSQLConnection conn) {
        System.out.println("Test 3");
        try {
            String tableName = "people";
            String[] insertFields = new String[]{"id", "name"};
            String[] insertValues = new String[]{"@id", "@name"};
            Map parameters = new HashMap();
            parameters.put("@id", 9);
            parameters.put("@name", "Ramsey");
            Integer rowsAffected = conn.insert(tableName, insertFields, insertValues, parameters, false, null);
            System.out.printf("%d\t\n", rowsAffected);
        } catch (Exception ex) {
            System.out.print("Error in insert");
        }
    }

    static void Test4(PostGreSQLConnection conn) {
        System.out.println("Test 4");

        try {
            String tableName = "people";
            String[] insertFields = new String[]{"name", "id"};
            String[] insertValues = new String[]{"@name", "@id"};
            Map parameters = new HashMap();
            parameters.put("@id", 10);
            parameters.put("@name", "Ramseys");
            Integer rowsAffected = conn.insert(tableName, insertFields, insertValues, parameters, false, null);
            System.out.printf("%d\t\n", rowsAffected);
        } catch (Exception ex) {
            System.out.print("Error in insert");
        }
    }

    static void Test5(PostGreSQLConnection conn) {
        System.out.println("Test 5");

        try {
            String tableName = "people";
            String[] insertFields = new String[]{"name", "id"};
            String[] insertValues = new String[]{"@name", "@id"};
            Map parameters = new HashMap();
            parameters.put("@name", "Ramseysse");
            parameters.put("@id", 11);

            Integer rowsAffected = conn.insert(tableName, insertFields, insertValues, parameters, false, null);
            System.out.printf("%d\t\n", rowsAffected);
        } catch (Exception ex) {
            System.out.print("Error in insert");
        }
    }

    static void Test6(PostGreSQLConnection conn) {
        System.out.println("Test 6");

        try {
            String tableName = "people";
            String[] insertFields = new String[]{"id", "name"};
            String[] insertValues = new String[]{"@id", "@name"};
            Map parameters = new HashMap();
            parameters.put("@name", "Ramseyssesa");
            parameters.put("@id", 12);

            Integer rowsAffected = conn.insert(tableName, insertFields, insertValues, parameters, false, null);
            System.out.printf("%d\t\n", rowsAffected);
        } catch (Exception ex) {
            System.out.print("Error in insert");
        }
    }

    static void Test7(PostGreSQLConnection conn) {
        System.out.println("Test 7");

        try {
            String tableName = "people";
            String[] insertFields = new String[]{"id", "name", "surname", "age"};
            String[] insertValues = new String[]{"@id", "@name", "@surname", "@age"};
            Map parameters = new HashMap();
            parameters.put("@name", "Ramseyssesana");
            parameters.put("@id", 13);
            parameters.put("@age", 27);
            parameters.put("@surname", "Ramseyssesananans");
            Integer rowsAffected = conn.insert(tableName, insertFields, insertValues, parameters, false, null);
            System.out.printf("%d\t\n", rowsAffected);
        } catch (Exception ex) {
            System.out.print("Error in insert");
        }
    }

    static void Test10(PostGreSQLConnection conn) {
        System.out.println("Test 10");
        //public ResultSet select(String[] tableNames, String[] fieldNames, SqlCriteria[] criterias, Map<String, Object> parameters, Map<String, String> orderBy, int limit)
        try {
            String[] tableNames = new String[]{"people"};

            String[] fields = new String[]{"id", "name"};

            SqlCriteria aCriteria = new SqlCriteria();
            aCriteria.FieldName = "name";
            aCriteria.CompareOperator = "=";
            aCriteria.FieldValue = "@name";
            SqlCriteria[] criterias = new SqlCriteria[1];
            criterias[0] = aCriteria;

            Map parameters = new HashMap();
            parameters.put("@name", "Ramsey");

            ResultSet rs = conn.select(tableNames, fields, criterias, parameters, null, 0);
            while (rs.next()) {
                System.out.printf("%d\t%s\t\n",
                        rs.getInt(1),
                        rs.getString(2));
//                        rs.getString(3),
//                        rs.getInt(4));
            }
        } catch (Exception ex) {
        }
    }

    static void Test11(PostGreSQLConnection conn) {
        System.out.println("Test 11");
        //public ResultSet select(String[] tableNames, String[] fieldNames, SqlCriteria[] criterias, Map<String, Object> parameters, Map<String, String> orderBy, int limit)
        try {
            String[] tableNames = new String[]{"people"};

            String[] fields = new String[]{"id", "name", "age"};

            SqlCriteria[] criterias = new SqlCriteria[2];
            SqlCriteria criteria1 = new SqlCriteria();
            criteria1.FieldName = "name";
            criteria1.CompareOperator = "=";
            criteria1.FieldValue = "@name";
            criteria1.CriteriaOperand = "AND";
            criterias[0] = criteria1;
            SqlCriteria criteria2 = new SqlCriteria();
            criteria2.FieldName = "age";
            criteria2.CompareOperator = "=";
            criteria2.FieldValue = "@age";
            criterias[1] = criteria2;
            Map parameters = new HashMap();
            parameters.put("@name", "Ramseyssesana ");
            parameters.put("@age", 27);

            ResultSet rs = conn.select(tableNames, fields, criterias, parameters, null, 0);
            while (rs.next()) {
                System.out.printf("%d\t%s\t%d\t\n",
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3));
            }
        } catch (Exception ex) {
        }

    }

    static void Test12(PostGreSQLConnection conn) {
        System.out.println("Test 12");

        try {
            String tableName = "people";
            String[] updateFields = new String[]{"surname", "age"};
            String[] updateValues = new String[]{"@surname", "@age"};
            Map parameters = new HashMap();

            parameters.put("@age", 26);
            parameters.put("@surname", "Rasmussen");
            parameters.put("@name", "Ramsey");

            SqlCriteria[] criterias = new SqlCriteria[1];
            SqlCriteria criteria1 = new SqlCriteria();
            criteria1.FieldName = "name";
            criteria1.CompareOperator = "=";
            criteria1.FieldValue = "@name";

            criterias[0] = criteria1;
            // int update(String tableName, String[] updateFields, String[] updateValues, SqlCriteria[] criterias, Map<String, Object> parameters)
            Integer rowsAffected = conn.update(tableName, updateFields, updateValues, criterias, parameters);
            System.out.printf("%d\t\n", rowsAffected);
        }
        catch (Exception ex) {
            System.out.print("Error in insert");
        }

    }
    static void Test13(PostGreSQLConnection conn) {
        System.out.println("Test 13");

        try {
            String tableName = "people";
            String[] updateFields = new String[]{"age", "name"};
            String[] updateValues = new String[]{"@age", "@name", };
            Map parameters = new HashMap();

            parameters.put("@age", 25);
            parameters.put("@name", "Rasmus");
            parameters.put("@id", 10);

            SqlCriteria[] criterias = new SqlCriteria[1];
            SqlCriteria criteria1 = new SqlCriteria();
            criteria1.FieldName = "id";
            criteria1.CompareOperator = "=";
            criteria1.FieldValue = "@id";

            criterias[0] = criteria1;
            // int update(String tableName, String[] updateFields, String[] updateValues, SqlCriteria[] criterias, Map<String, Object> parameters)
            Integer rowsAffected = conn.update(tableName, updateFields, updateValues, criterias, parameters);
            System.out.printf("%d\t\n", rowsAffected);
        }
        catch (Exception ex) {
            System.out.print("Error in insert");
        }

    }

    static void Test14(PostGreSQLConnection conn) {
        System.out.println("Test 14");

        try {
            String tableName = "people";

            Map parameters = new HashMap();
            parameters.put("@id", 11);

            SqlCriteria[] criterias = new SqlCriteria[1];
            SqlCriteria criteria1 = new SqlCriteria();
            criteria1.FieldName = "id";
            criteria1.CompareOperator = "=";
            criteria1.FieldValue = "@id";

            criterias[0] = criteria1;
            // int delete(String tableName, SqlCriteria[] criterias, Map<String, Object> parameters)
            Integer rowsAffected = conn.delete(tableName,  criterias, parameters);
            System.out.printf("%d\t\n", rowsAffected);
        }
        catch (Exception ex) {
            System.out.print("Error in insert");
        }

    }

}









