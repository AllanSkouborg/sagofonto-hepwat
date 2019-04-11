package dk.artogis.hepwat.streamingStatusService.app;

import dk.artogis.hepwat.streamingStatusService.configuration.*;
import dk.artogis.hepwat.streamingStatusService.streamingprocess.Calculation;
import org.apache.log4j.*;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;


public class application {

    static final String InputTopic = "raw-test-1";
    static final String KafkaCluster = "192.168.1.16:9092";
    static final String AggMinutesString = "1";
    static final String LoggingActivatedString = "On";
    static final String LoggingPathString = "/tmp/hewpwat/logs/";
    static final String OutputTopicIdString = "output-test-filter";
    static final String CalculationTypeString = "1";

    static Logger logger = Logger.getLogger(application.class);
    private static String  DefaultLoggingActivatedString = "On";
    private static String DefaultLoggingPathString = "/tmp/hepwat/logs/";
    private static String DefaultServiceIdString = "0";
    private static String DefaultServiceUrlString = "http://localhost:8080/hepwatRestService/rest/";
    private static String  DefaultLoggingLevelString = "Info";
    private static String  DefaultLoggingSizeString = "10MB";

    private static Integer serviceId = 0 ;
    private static URI serviceUri;

    private static ServiceConfiguration serviceConfiguration;
    private DeviceConfigurations deviceConfigurations;
    private StatusTypes statusTypes;

    private static Calculation calculation;
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(final String[] args) throws Exception {

        System.out.println("Connector initializing...");

        String loggingOn =  DefaultLoggingActivatedString;
        String loggingPath =  DefaultLoggingPathString;
        String serviceUrl =  DefaultServiceIdString;
        String serviceIdString = DefaultServiceUrlString;
        String loggingLevel= DefaultLoggingLevelString;
        String loggingSize = DefaultLoggingSizeString;

        Logger logger = Logger.getLogger(application.class);
        logger.info("starting");

        try
        {
            if(args.length > 0) {
                serviceUrl = args[0];
                serviceUri = UriBuilder.fromUri(serviceUrl).build();
            }
            if(args.length > 1) {
                serviceIdString = args[1];
                try {
                    serviceId = Integer.parseInt(serviceIdString);
                }
                catch (Exception ex)
                {
                    throw new Exception("could not parse serviceId from startup arguments");
                }
            }
            if(args.length > 2) {
                loggingPath = args[2];
                if (!loggingPath.endsWith("/"))
                    loggingPath = loggingPath + "/";
            }
            if(args.length > 3) {
                loggingOn = args[3];
            }
            if(args.length > 4) {
                loggingLevel = args[4];
            }
            if(args.length > 5) {
                loggingSize = args[5];
            }

            if (new String(loggingOn.toLowerCase()).equals(DefaultLoggingActivatedString.toLowerCase()))
            {
                String fileName = "serviceId_" + serviceIdString;
                try {
                    PatternLayout patternLayout = new PatternLayout();
                    patternLayout.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
                    RollingFileAppender rolingFileAppender = new RollingFileAppender(patternLayout, loggingPath + "StreamingStatus_"+ fileName + ".log");
                    rolingFileAppender.setMaxFileSize(loggingSize);
                    rolingFileAppender.setMaxBackupIndex(2);
                    logger.addAppender(rolingFileAppender);
                    if (loggingLevel.toLowerCase().equals(DefaultLoggingLevelString.toLowerCase()))
                        logger.setLevel(Level.INFO);
                    else logger.setLevel(Level.TRACE);
                    if (logger.isInfoEnabled())
                        logger.info("logger settings added");
                }
                catch (Exception ex)
                {
                    System.out.println("Could not add logger settings");
                    logger.error("Could not add logger settings");
                    throw new Exception("Could not add logger settings");
                }
            }
            else
            {
                logger.setLevel(Level.OFF);
            }
            if(logger.isInfoEnabled()) {
                logger.info("Configured rest service url :" + serviceUrl);
                logger.info("Configured service id: " + serviceIdString);
            }

            if(args.length > 3) {
                application application = new application(serviceUri, serviceId,  logger);
            }
            else {
                System.out.println("Settings file argument missing");
                logger.error("Settings file argument missing");
            }
        }
        catch (Exception ex)
        {
            System.out.println("could not execute on the given service settings " + ex.getMessage());
            logger.error("could not execute on the given service settings: " + ex.getMessage());
        }
    }

    private void startTimer(Logger logger)
    {
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("Task performed on: " + new Date() + "n" +
                        "Thread's name: " + Thread.currentThread().getName());
                DeviceConfigurations localDeviceConfigurations = new DeviceConfigurations(serviceUri, serviceConfiguration.calcType, logger);
                if (localDeviceConfigurations != null) {
                    if (logger.isInfoEnabled())
                        logger.info("got device configuration");
                    if (localDeviceConfigurations.configurations != null) {
                        if (logger.isInfoEnabled())
                            logger.info("device configuration formulas not empty");
                        lock.lock();
                        try {
                            deviceConfigurations.configurations = localDeviceConfigurations.configurations;
                        } catch (Exception ex) {
                            logger.error("error in replacing device configuration");
                        } finally {
                            lock.unlock();
                        }
                    }
                }

            }
        };
        Timer timer = new Timer("Timer");

        long delay = serviceConfiguration.getConfiguration().getRecycleDataInterval();//120000L;
        long periode = serviceConfiguration.getConfiguration().getRecycleDataInterval();//120000L;
        timer.schedule(task, delay, periode);

    }
    public application(URI  serviceUri, Integer serviceId, Logger logger)
    {
        if (logger.isInfoEnabled())
            logger.info("going to start service configuration");

        serviceConfiguration = new ServiceConfiguration(serviceUri, serviceId, logger);
        if ((serviceConfiguration != null) && (serviceConfiguration.isOk())) {
            if (logger.isInfoEnabled())
                logger.info("configuration ok, going to get statustypes ");
            statusTypes = new StatusTypes(serviceUri);
            if (statusTypes != null) {
                if (logger.isInfoEnabled())
                    logger.info("statustypes ok, going to get devicecalculations ");
                deviceConfigurations = new DeviceConfigurations(serviceUri, serviceConfiguration.calcType, logger);
                if (deviceConfigurations != null) {
                    if (logger.isInfoEnabled())
                        logger.info("Streaming process initialized and ready for running ");
                    System.out.println("Streaming process  initialized and ready for running ");
                    calculation = new Calculation(serviceConfiguration, deviceConfigurations, statusTypes, logger);
                    if ((calculation != null) && (calculation.getOk() == true)){
                        startTimer(logger);
                        calculation.run(lock);
                    } else logger.error("calculation process not properly instantiated");
                } else logger.error("deviceCalculations not found");
            }else logger.error("statustypes not found");

        } else {
            //System.out.println("Data from rest service not provided");
            logger.error("Data from rest service not provided");
        }
    }

}
