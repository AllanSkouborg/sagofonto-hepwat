package dk.artogis.hepwat.common.database;


import com.mongodb.MongoClient;
import dk.artogis.hepwat.common.utility.Status;

import org.apache.log4j.Logger;

public  class MongoDbConnection extends MongoClient  {

        private MongoClient mongoClient;
        private static MongoDbConnection mongoDbConnection_instance = null;
        public Status status;
        private static Integer mongoPort = 27017;
        private String server;
        private String port;
        private String database;
        private String userName;
        private String password;


        private MongoDbConnection(String server, String port, String dbName,  String user, String pass) {
            super(server, Integer.valueOf(port));

            this.server = server;
            this.database = dbName;
            this.port = port;
            this.userName = user;
            this.password = pass;
        }

        public static MongoDbConnection getMongoDbConnection(String server, String port, String dbName,  String user, String pass){
            Logger logger = Logger.getLogger(PostGreSQLConnection.class);

            if (mongoDbConnection_instance == null) {
                if (logger.isInfoEnabled())
                    logger.info("going to create mongo connection");
                mongoDbConnection_instance = new MongoDbConnection(server, port, dbName, user, pass);
            }
            if (logger.isTraceEnabled())
                logger.trace("going to return mongodb connection instance");
            return mongoDbConnection_instance;
        }


        public com.mongodb.client.MongoDatabase getDatabase()
        {
            return super.getDatabase(database);
        }


}
