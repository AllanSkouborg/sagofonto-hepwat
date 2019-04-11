package app;


import configuration.DeviceConfigurations;
import configuration.ServiceConfiguration;
import mongodb.*;


import org.apache.log4j.*;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

public class application {

    private static String DefaultLoggingActivatedString = "On";
    private static String DefaultLoggingPathString = "/tmp/hepwat/logs/";
    private static String DefaultServiceIdString = "0";
    private static String DefaultServiceUrlString = "http://localhost:8080/hepwatRestService/rest/";
    private static String DefaultLoggingLevelString = "Info";
    private static String DefaultLoggingSizeString = "10MB";


    private static Integer serviceId = 0;
    private static URI serviceUri;

    private ServiceConfiguration serviceConfiguration;
    private static DeviceConfigurations deviceConfigurations;
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        //TODO: Remove sysem out print
        System.out.println("Connector initializing...");

        String loggingOn = DefaultLoggingActivatedString;
        String loggingPath = DefaultLoggingPathString;
        String serviceUrl = DefaultServiceIdString;
        String serviceIdString = DefaultServiceUrlString;
        String loggingLevel = DefaultLoggingLevelString;
        String loggingSize = DefaultLoggingSizeString;

        Logger logger = Logger.getLogger(application.class);
        logger.info("starting");
        try {
            if (args.length > 0) {
                serviceUrl = args[0];
                serviceUri = UriBuilder.fromUri(serviceUrl).build();
            }
            if (args.length > 1) {
                serviceIdString = args[1];
                try {
                    serviceId = Integer.parseInt(serviceIdString);
                } catch (Exception ex) {
                    throw new Exception("could not parse serviceId from startup arguments");
                }
            }
            if (args.length > 2) {
                loggingPath = args[2];
                if (!loggingPath.endsWith("/"))
                    loggingPath = loggingPath + "/";
            }
            if (args.length > 3) {
                loggingOn = args[3];
            }
            if (args.length > 4) {
                loggingLevel = args[4];
            }
            if (args.length > 5) {
                loggingSize = args[5];
            }


            if (new String(loggingOn.toLowerCase()).equals(DefaultLoggingActivatedString.toLowerCase())) {
                String fileName = "serviceId_" + serviceIdString;
                try {
                    PatternLayout patternLayout = new PatternLayout();
                    patternLayout.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
                    RollingFileAppender rolingFileAppender = new RollingFileAppender(patternLayout, loggingPath + "Monogoconsumer_" + fileName + ".log");
                    rolingFileAppender.setMaxFileSize(loggingSize);
                    rolingFileAppender.setMaxBackupIndex(2);
                    logger.addAppender(rolingFileAppender);
                    if (loggingLevel.toLowerCase().equals(DefaultLoggingLevelString.toLowerCase()))
                        logger.setLevel(Level.INFO);
                    else logger.setLevel(Level.TRACE);
                    if (logger.isInfoEnabled())
                        logger.info("logger settings added");
                } catch (Exception ex) {
                    System.out.println("Could not add logger settings");
                    logger.error("Could not add logger settings");
                    throw new Exception("Could not add logger settings");
                }
            } else {
                logger.setLevel(Level.OFF);
            }
            if (logger.isInfoEnabled()) {
                logger.info("Configured rest service url :" + serviceUrl);
                logger.info("Configured service id: " + serviceIdString);
            }

            if (args.length > 3) {
               application application = new application(serviceUri, serviceId, logger);
            }
            else {
                System.out.println("Settings file argument missing");
                if (logger.isInfoEnabled()) {
                    logger.info("Settings file argument missing");
                }
            }
        }
        catch (Exception ex) {
            System.out.println("could not execute on the given service settings " + ex.getMessage());
            logger.error("could not execute on the given service settings: " + ex.getMessage());
        }
    }


    private  void startTimer(Logger logger) {
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("Task performed on: " + new Date() + "n" +
                        "Thread's name: " + Thread.currentThread().getName());
                DeviceConfigurations localDeviceConfigurations = new DeviceConfigurations(serviceUri);
                if (localDeviceConfigurations != null) {
                    if (logger.isInfoEnabled())
                        logger.info("got device configuration");
                    if (localDeviceConfigurations.configurations != null) {
                        if (logger.isInfoEnabled())
                            logger.info("configuration contains list :" + localDeviceConfigurations.configurations.size());
                        lock.lock();
                        try {
                            deviceConfigurations.configurations = localDeviceConfigurations.configurations;
                        } catch (Exception ex) {
                            logger.error("error in replacing configuration ");
                        } finally {
                            lock.unlock();
                        }
                    }
                }

            }
        };
        Timer timer = new Timer("Timer");

        long delay = serviceConfiguration.getConfiguration().getRecycleDataInterval();
        long periode = serviceConfiguration.getConfiguration().getRecycleDataInterval();
        timer.schedule(task, delay, periode);

    }

    public application(URI serviceUri, Integer serviceId, Logger logger)
    {
        if (logger.isInfoEnabled())
            logger.info("going to start service configuration");

        serviceConfiguration = new ServiceConfiguration(serviceUri, serviceId, logger);
        if ((serviceConfiguration != null) && (serviceConfiguration.getConfiguration() != null) && (serviceConfiguration.getProcessingConfigs() != null) && (serviceConfiguration.getMongoDbDataStore() != null)) {
            if (logger.isInfoEnabled())
                logger.info("service configuration ok");
            deviceConfigurations = new DeviceConfigurations(serviceUri);

            if (logger.isInfoEnabled())
                logger.info("configuration ok, going to start consumer");
            Consumer consumer = null;
            try {
                consumer = new Consumer(
                        serviceConfiguration.getConfiguration(),
                        serviceConfiguration.getProcessingConfigs(), serviceConfiguration.getMongoDbDataStore(), deviceConfigurations, logger);
            }
            catch(Exception ex)
            {
                logger.error("cold not create consumer, exits " + ex.getMessage());
                return;
            }
            if (logger.isInfoEnabled()) {
                logger.info("Connector initialized and ready for running ");
            }
            System.out.println("Connector initialized and ready for running ");

            if (consumer != null) {
                startTimer(logger);
                consumer.start(lock);
            }

        }
        else {
            System.out.println("Data from rest service not provided");
            if (logger.isInfoEnabled()) {
                logger.info("Data from rest service not provided");
            }
        }
    }
}
