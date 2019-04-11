package testCommon;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestPostgreSQLConnectionMain {

    public static void main(String[] args) {



//        Result result = JUnitCore.runClasses(PostGreSQLInsertTest.class);
//        for (Failure failure : result.getFailures()) {
//            System.out.println(failure.toString());
//        }
//
//        System.out.println("Run count insert : " + result.getRunCount()) ;
//        System.out.println("Errors insert : " + result.getFailureCount());
//
//
//        result = JUnitCore.runClasses(PostGreSQLSelectTest.class);
//        for (Failure failure : result.getFailures()) {
//            System.out.println(failure.toString());
//        }
//
//        System.out.println("Run count select: " + result.getRunCount()) ;
//        System.out.println("Errors select: " + result.getFailureCount());
//
//
//
//        result = JUnitCore.runClasses(PostGreSQLUpdateTest.class);
//        for (Failure failure : result.getFailures()) {
//            System.out.println(failure.toString());
//        }
//
//        System.out.println("Run count update: " + result.getRunCount()) ;
//        System.out.println("Errors update: " + result.getFailureCount());
//
//
//        result = JUnitCore.runClasses(PostGreSQLUpdateTest.class);
//        for (Failure failure : result.getFailures()) {
//            System.out.println(failure.toString());
//        }
//
//        System.out.println("Run count update: " + result.getRunCount()) ;
//        System.out.println("Errors update: " + result.getFailureCount());
//
//        result = JUnitCore.runClasses(PostGreSQLDeleteTest.class);
//        for (Failure failure : result.getFailures()) {
//            System.out.println(failure.toString());
//        }
//
//        System.out.println("Run count delete: " + result.getRunCount()) ;
//        System.out.println("Errors delete: " + result.getFailureCount());

        Result result = JUnitCore.runClasses(PostGreSQLInsertDifferentTypesTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println("Run count insert different types: " + result.getRunCount()) ;
        System.out.println("Errors different types: " + result.getFailureCount());

        result = JUnitCore.runClasses(PostGreSQLSelectDifferentTypesTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println("Run count select different types: " + result.getRunCount()) ;
        System.out.println("Errors select different types: " + result.getFailureCount());
    }
}
