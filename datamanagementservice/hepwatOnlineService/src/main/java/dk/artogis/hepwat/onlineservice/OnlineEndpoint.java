package dk.artogis.hepwat.onlineservice;
import dk.artogis.hepwat.onlineservice.coders.MessageDecoder;
import dk.artogis.hepwat.onlineservice.coders.MessageEncoder;
import dk.artogis.hepwat.onlineservice.clientmessage.SubscribtionMessage;
import dk.artogis.hepwat.onlineservice.clientmessage.JoinMessage;
import dk.artogis.hepwat.onlineservice.clientmessage.Message;
import dk.artogis.hepwat.onlineservice.clientmessage.Subscription;
import dk.artogis.hepwat.onlineservice.subscription.DataSubscriptions;
import dk.artogis.hepwat.onlineservice.taskhandling.OnlineSession;
import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@ServerEndpoint(value = "/data", decoders = MessageDecoder.class, encoders = MessageEncoder.class )
public class OnlineEndpoint {
    RemoteEndpoint.Basic remoteEndpointBasic;
    //Session session ;
    Logger logger = Logger.getLogger(OnlineEndpoint.class);

    private  ExecutorService executor = Executors.newCachedThreadPool();//Executors.newFixedThreadPool(10);
    private  ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;

    @OnOpen
    public void onOpen(Session session)
    {

        try {
            remoteEndpointBasic = session.getBasicRemote();
            remoteEndpointBasic.sendText(Config.version);

            logger.info("session id : " + session.getId() + " ");
        }
        catch (Exception ex)
        {
            logger.error("error in sending version info: " + ex.getMessage());
        }
    }

    @OnError
    public void  onError(Session session, Throwable t)
    {
        logger.error("session id : " + session.getId() + " ");
        logger.error("session id : " + session.getId() + " " + t.getMessage() + " ");
    }

    @OnMessage
    public void onMessage(Session session, Message message) {

        logger.info("session id: " + session.getId());
        if (message instanceof JoinMessage) {
            processMessage((JoinMessage) message);
        } else if (message instanceof SubscribtionMessage) {
            logger.info("message : " + ((SubscribtionMessage) message).getString());
            processMessage((SubscribtionMessage) message, session);
        } else {
            logger.error("Unknown message type" );
        }
    }

@OnClose
public void onClose(Session session)
{
    try {
        logger.info("Closing session : " + session.getId() );
        CloseOnlineSession(session);
    }
    catch (Exception ex)
    {
        logger.info("Closing session error: " + ex.getMessage() );
    }
}

    private void processMessage(JoinMessage message) {
        // for future use
    }
    private void processMessage(SubscribtionMessage message, Session websocketSession) {

        Subscription[] subscriptions = message.getSubscriptions();
        for (Subscription subscription: subscriptions) {
           if  (logger.isInfoEnabled())
               logger.info( subscription.getHepwatDeviceId().toString());
        }
        DataSubscriptions dataSubscriptions = new DataSubscriptions(message.getSubscriptions());

        CreateOnlineSession(dataSubscriptions, websocketSession);
    }

    private void CreateOnlineSession(DataSubscriptions dataSubscriptions, Session session)
    {
        if (logger.isInfoEnabled()) logger.info(String.format("Start - CreateOnlineSession: %s",  " Session: " + session.getId()));
        if (logger.isInfoEnabled()) logger.info("before starting new thread. Pool active threads : " + pool.getActiveCount());

        try {
            OnlineSession onlineSession = new OnlineSession(dataSubscriptions, session);
            if (onlineSession != null)
                executor.submit(onlineSession);
        }
        catch (Exception ex)
        {
            logger.error("error in creating new online session: " + ex.getMessage());
        }
        if (logger.isInfoEnabled()) logger.info("started new thread. Pool active threads : " + pool.getActiveCount());

    }
    private void CloseOnlineSession(Session session)
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
