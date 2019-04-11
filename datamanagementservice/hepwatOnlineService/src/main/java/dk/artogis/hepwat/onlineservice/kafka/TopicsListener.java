package dk.artogis.hepwat.onlineservice.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import dk.artogis.hepwat.onlineservice.subscription.DataSubscriptions;
import dk.artogis.hepwat.onlineservice.taskhandling.OnlineWebSocketSession;
import org.apache.log4j.Logger;

import java.util.Date;

public class TopicsListener extends Thread implements IJsonConsumerListener
{
    private static Logger logger = Logger.getLogger(TopicsListener.class);

    private static  String _sessionId = "";
    private JsonConsumer _consumer = null;

    private boolean _listening = false;
    public boolean isListening() {
        return _listening;
    }
    private OnlineWebSocketSession _onlineWebSocketSession = null;
    private DataSubscriptions _dataSubscriptions;

    public TopicsListener(DataSubscriptions dataSubscriptions, OnlineWebSocketSession onlineWebSocketSession) {

        if (logger.isTraceEnabled()) logger.trace("Start - TopicsListener in thread");
        _onlineWebSocketSession = onlineWebSocketSession;
        _sessionId =  _onlineWebSocketSession.get_sessionId();
        _dataSubscriptions = dataSubscriptions;
        if (logger.isTraceEnabled()) logger.trace("End - TopicsListener, session id: " + _sessionId.toString());
    }
    @Override
    public void run() {
        if (logger.isTraceEnabled()) logger.trace("Start - run:");
        super.run();
        startListening();
        if (logger.isTraceEnabled()) logger.trace("End - run:");
    }

    private void startListening() {

        if (logger.isTraceEnabled()) logger.trace("Start - startListening:");
        if(!_listening) {
            if (_consumer == null) {
                _consumer = new JsonConsumer( _dataSubscriptions, _sessionId);
                _consumer.addListener(this);
            }
            if (_consumer.isReady()) {
                _listening = true;
                _consumer.consume();
            }
        }
        else {
            if (logger.isTraceEnabled()) logger.trace("startListening: already listening : " + _sessionId);
        }

        if (logger.isTraceEnabled()) logger.trace("End - startListening: " + _sessionId);
    }

    public void stopListening() {
        if (logger.isTraceEnabled()) logger.trace("Start - stopListening: + _clientId.toString()");
        _listening = false;
        _consumer.stop();
        _consumer.removeListener(this);
        _onlineWebSocketSession.sendText(
                "{ \"success\": true, \"thread session\": \"" + _sessionId +
                         "\", \"message\": \"Listening stopped\" }");
        _onlineWebSocketSession.closeSession();
        if (logger.isTraceEnabled()) logger.trace("End - stopListening: " + _sessionId);
    }

    public void messageReceived(String Key, JsonNode message) {
        if (logger.isTraceEnabled()) logger.trace("Start - messageReceived: " + _sessionId);
        if(_listening) {
            Date lastRead = new Date();

            String responseStr = message.toString() ;

            boolean isOpen = _onlineWebSocketSession.sendText( responseStr);
            if (!isOpen)
                stopListening();
        }
        if (logger.isTraceEnabled()) logger.trace("End - messageReceived: " + _sessionId);
    }
}
