package dk.artogis.hepwat.common.database;

import dk.artogis.hepwat.common.utility.Encryption;

public class ConnectionParameters {

    public ConnectionType ConnectionType;

    public String getServer() {
        return Server;
    }

    public void setServer(String server) {
        Server = server;
    }

    public String Server ;

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String Port ;

    public String getDatabase() {
        return Database;
    }

    public void setDatabase(String database) {
        Database = database;
    }

    public String Database ;

    public String getSchema() {
        return Schema;
    }

    public void setSchema(String schema) {
        Schema = schema;
    }

    public String Schema ;


    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String Username;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String Password ;


    public boolean isPasswordEncrypted() {
        if(!Password.isEmpty())
        {
            Encryption encryption = new Encryption(Username, Password);
            return encryption.PasswordEncrypted();
        }
        return false;
    }


}
