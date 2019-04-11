package kafka;

/**
 * Created by clifford.jakobsen on 22-05-2017. modified to Json by Margit
 * Inspired by https://github.com/omkreddy/kafka-examples/blob/master/producer/src/main/java/kafka/examples/consumer/BasicConsumerExample.java and https://gist.github.com/yaroncon
 */

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;
import dk.artogis.hepwat.services.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

//import org.codehaus.jackson.JsonNode;

public class JsonConsumer {

    private static Logger logger = null;

    private ArrayList<String> _topicNames = null;
    private  Configuration _configuration = null;
    private boolean _isConsuming = false;
    private KafkaConsumer<String, JsonNode> _consumer = null;

    public JsonConsumer(Configuration configuration, ArrayList<String> topicNames, Logger instantiatedLogger)
    {
        logger = instantiatedLogger;
        if (logger.isTraceEnabled()) logger.trace("Start - JsonConsumer");
        _configuration = configuration;
        _topicNames = topicNames;

        if (logger.isTraceEnabled()) logger.trace("End - JsonConsumer");
    }
    public void consume(ReentrantLock lock)
    {
        if (logger.isTraceEnabled()) logger.trace("Start - consume");

        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, _configuration.getKafkaBroker());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, _configuration.getTopicGroupId());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, _configuration.getAutoOffSet());
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, _configuration.getClientId());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.connect.json.JsonDeserializer");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        _consumer = new KafkaConsumer<String, JsonNode>(properties);
        _consumer.subscribe(_topicNames);

        _isConsuming = true;

        ConsumerRecords<String, JsonNode> records;


        try {
            while (_isConsuming) {
                records = _consumer.poll(10000);
                for (ConsumerRecord<String, JsonNode> record : records) {
                    for (IJsonConsumerListener listener : _listeners) {
                        JsonNode jsonNode = record.value();
                        listener.messageReceived( record.key(), jsonNode, lock);
                    }
                    if (logger.isTraceEnabled()) logger.trace(String.format("consume: received message topic =%s, partition =%s, offset = %d, message = %s", record.topic(), record.partition(), record.offset(), record.value().toString()));
                }
                try {
                    _consumer.commitSync();
                }
                catch (Exception e) {
                    logger.error("consume: Consumer could not commit offsets", e);
                }
            }
        }
        finally {
            if (logger.isInfoEnabled()) logger.info("Disconnecting from Kafka consumer");
            _isConsuming = false;
            _consumer.close();
            if (logger.isInfoEnabled()) logger.info("Disconnected");
        }

    }

    public void stop() {
        if (logger.isTraceEnabled()) logger.trace("Start - stop");
        if(_isConsuming) {
            _isConsuming = false;
        }
        if (logger.isTraceEnabled()) logger.trace("End - stop");
    }

    @Override
    protected void finalize() throws Throwable {
        stop();
        super.finalize();
    }

    private List<IJsonConsumerListener> _listeners = new CopyOnWriteArrayList<IJsonConsumerListener>();

    public void addListener(IJsonConsumerListener listener) {
        if (logger.isTraceEnabled()) logger.trace("Start - addListener");
        _listeners.add(listener);
        if (logger.isTraceEnabled()) logger.trace("End - addListener");
    }

    public void removeListener(IJsonConsumerListener listener) {
        if (logger.isTraceEnabled()) logger.trace("Start - removeListener");
        _listeners.remove(listener);
        if (logger.isTraceEnabled()) logger.trace("End - removeListener");
    }
}
