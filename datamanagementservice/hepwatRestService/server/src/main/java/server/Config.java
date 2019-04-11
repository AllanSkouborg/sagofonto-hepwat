package server;

import org.apache.log4j.Logger;

public class Config {
    public static String database ;
    public static String server;
    public static String port;
    public static String user;
    public static String password;
    public static String schema;
    public static String version;
    public static String mongodb;
    public static String logPath;
    public static String logOn;
    public static String logSize;

    public static Integer getIntMongodb()
    {
        Integer intMongodb = 1;

        try
        {
            intMongodb = Integer.parseInt(mongodb);
        }
        catch (Exception ex)
        {
            Logger logger = Logger.getLogger(Config.class);

            logger.info("error in converting mongodb id " + ex.getMessage());
        }
        return  intMongodb;
    }
}
