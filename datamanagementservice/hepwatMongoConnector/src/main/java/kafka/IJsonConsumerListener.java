package kafka;


import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.locks.ReentrantLock;


public interface IJsonConsumerListener {
    void messageReceived(String key, JsonNode message, ReentrantLock lock);
}
