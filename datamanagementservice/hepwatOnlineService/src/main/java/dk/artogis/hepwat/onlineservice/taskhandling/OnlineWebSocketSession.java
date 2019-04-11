package dk.artogis.hepwat.onlineservice.taskhandling;

import org.apache.log4j.Logger;

import javax.websocket.Session;
import java.io.IOException;

public class OnlineWebSocketSession {
    private Session _session;

    public String get_sessionId() {
        return _sessionId;
    }
    private String _sessionId = "";
    Logger logger = Logger.getLogger(OnlineWebSocketSession.class);

    public OnlineWebSocketSession(Session session) {

        _session = session;
        _sessionId = session.getId();

    }

    public boolean sendText(String message)
    {
        try {
            if(_session.isOpen()) {
                    _session.getBasicRemote().sendText(message);
            }
            else {
                return false;
            }
        }
        catch (IOException ex) {
            return  false;
        }
        return true;
    }

    public void closeSession()
    {
        try {

            if (_session != null) {
                sendText("Closing session, due to error" );
                _session.close();
            }
        }
        catch (Exception ex)
        {
            logger.error("error in closing session: " + ex.getMessage());
        }
    }

}
