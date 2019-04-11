package server;


import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;


@WebListener
public class ConfigService implements ServletContextListener {

    static Logger logger = null;

    @Context
    ServletContext context;
    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Perform action during application's startup

        context = event.getServletContext();
        Config.database = context.getInitParameter("Database");
        Config.server = context.getInitParameter("DatabaseServer");
        Config.port = context.getInitParameter("Port");
        Config.schema = context.getInitParameter("Schema");
        Config.user = context.getInitParameter("User");
        Config.password = context.getInitParameter("Password");
        Config.version = context.getInitParameter("Version");
        Config.mongodb = context.getInitParameter("Mongodb");

        logger = Logger.getLogger(ConfigService.class);
        // initalize  logging
        if(logger.isInfoEnabled()) {
            logger.info("Configuration is read from web.xml: ");
        }
        Config.version = getVersion(context);
        if(logger.isInfoEnabled()) {
            logger.info("Software version : " + Config.version);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Perform action during application's shutdown
    }

    private String getVersion(ServletContext context) {

        String revision = "unknown";
        InputStream is = context.getResourceAsStream("/META-INF/MANIFEST.MF");
        Manifest manifest = new Manifest();
        try {
            manifest.read(is);
        } catch (IOException e) {
            // error handling
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // error handling
            }
        }
        Attributes attr = manifest.getMainAttributes();
        String implRevision = attr.getValue("Build-Timestamp");
        String implVersion = attr.getValue("Implementation-Version");

        if (implRevision == null || implVersion == null)
            revision = "unknown";
        else
            revision = implVersion + " / " + implRevision;
        return  revision;
    }

}