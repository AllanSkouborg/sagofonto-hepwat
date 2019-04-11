package dk.artogis.hepwat.onlineservice;


import dk.artogis.hepwat.onlineservice.configuration.AggregationAndCalculations;
import dk.artogis.hepwat.onlineservice.configuration.DeviceCalculations;
import dk.artogis.hepwat.onlineservice.configuration.DeviceConfigurations;
import dk.artogis.hepwat.onlineservice.configuration.ServiceConfiguration;

import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.jar.Attributes;
import java.util.jar.Manifest;


@WebListener
public class ConfigService implements ServletContextListener {

    private static URI serviceUri;
    static Logger logger = null;
    public  static ServiceConfiguration serviceConfiguration;

    @Context
    ServletContext context;
    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Perform action during application's startup

        context = event.getServletContext();
        Config.hepwatRestService = context.getInitParameter("hepwatRestService");
        Config.serviceId = Integer.parseInt(context.getInitParameter("serviceId"));

        logger = Logger.getLogger(ConfigService.class);
        // initalize  logging
        if(logger.isInfoEnabled()) {
            logger.info("Configuration is read from web.xml: ");
        }

        Config.version = getVersion(context);
        if(logger.isInfoEnabled()) {
            logger.info("Software version : " + Config.version);
        }

        serviceUri = UriBuilder.fromUri(Config.hepwatRestService).build();
        serviceConfiguration = new ServiceConfiguration(serviceUri, Config.serviceId, logger );

        if (Config.serviceId > 0) {
            if ((serviceConfiguration != null) && (serviceConfiguration.isOk())) {
                if (logger.isInfoEnabled()) {
                    logger.info("Online service initialized and ready for running ");
                    System.out.println("Online service initialized and ready for running ");
                }
            } else {
                System.out.println("Data from rest service not provided");
                logger.error("Data from rest service not provided");
            }
        }
        else {
            System.out.println("Settings file argument missing");
            logger.error("Settings file argument missing");
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