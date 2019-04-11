package dk.artogis.hepwat.onlineservice.kafka;


import com.fasterxml.jackson.databind.JsonNode;


public interface IJsonConsumerListener {
    void messageReceived(String key, JsonNode message);
}
