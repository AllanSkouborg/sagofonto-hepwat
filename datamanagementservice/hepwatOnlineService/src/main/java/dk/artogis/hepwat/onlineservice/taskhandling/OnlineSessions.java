package dk.artogis.hepwat.onlineservice.taskhandling;

import dk.artogis.hepwat.onlineservice.subscription.DataSubscriptions;

import org.apache.log4j.Logger;

import javax.websocket.Session;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class OnlineSessions {
    private static Logger logger = Logger.getLogger(OnlineSessions.class);
    private static ExecutorService executor = Executors.newCachedThreadPool();//Executors.newFixedThreadPool(10);
    private static ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
    private static Integer nextId = 0;


    private OnlineSessions() {

    }


    public static void createKafkaSession(DataSubscriptions dataSubscriptions, Session session) {
        nextId ++;
        if (logger.isInfoEnabled()) logger.info(String.format("Start - CreateKafkaSession: %s",  " Session: " + session.getId()));
        if (logger.isInfoEnabled()) logger.info("before starting new thread. Pool active threads : " + pool.getActiveCount());

        OnlineSession onlineSession = new OnlineSession(dataSubscriptions,  session);
        executor.submit(onlineSession);
        if (logger.isInfoEnabled()) logger.info("started new thread. Pool active threads : " + pool.getActiveCount());
    }

    public static void closeKafkaSession(Session session)
    {
        if (logger.isInfoEnabled()) logger.info("closeKafkaSession - begin");
        try {
            if ((session != null) && (session.isOpen()))
                session.close();
            if (logger.isInfoEnabled()) logger.info("closed Kafka session. Pool active threads : " + pool.getActiveCount());
        }
        catch (Exception ex)
        {
            if (logger.isInfoEnabled()) logger.info("Exception in Kafka session. Pool active threads : " + pool.getActiveCount() + " " + ex.getMessage());
        }
    }

}
