package dk.artogis.hepwat.onlineservice.taskhandling;

import dk.artogis.hepwat.onlineservice.kafka.TopicsListener;
import dk.artogis.hepwat.onlineservice.startdata.StartDataHandler;
import dk.artogis.hepwat.onlineservice.subscription.DataSubscriptions;

import org.apache.log4j.Logger;

import javax.websocket.Session;


public class OnlineSession implements Runnable{
        private static Logger logger = Logger.getLogger(OnlineSession.class);
        private OnlineWebSocketSession _onlineWebSocketSession = null;
        private TopicsListener _topicsListener = null;
        private StartDataHandler _startDataHandler = null;

    public OnlineSession(DataSubscriptions dataSubscriptions, Session session ) {

        if (logger.isTraceEnabled()) logger.trace(String.format("Start - OnlineSession with session number: %s", session.getId()));
        _onlineWebSocketSession = new OnlineWebSocketSession(session);
        _startDataHandler = new StartDataHandler(dataSubscriptions, _onlineWebSocketSession);
        _topicsListener = new TopicsListener(dataSubscriptions, _onlineWebSocketSession);
    }
    public void run()
    {
        try {
            _startDataHandler.getStartData();
            _topicsListener.run();
        }
        catch (Exception ex)
        {
            logger.error("error in getting start data or error in running topicslistener");
            _onlineWebSocketSession.closeSession();
        }
        if (logger.isTraceEnabled()) logger.trace("Run ended");
    }


    public static void closeSession(String sessionId) {
        if (logger.isTraceEnabled()) logger.trace(String.format("Start - closeSession name: %s", sessionId));

    }
}
