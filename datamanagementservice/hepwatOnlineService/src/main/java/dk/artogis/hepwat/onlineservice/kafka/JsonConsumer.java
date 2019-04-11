package dk.artogis.hepwat.onlineservice.kafka;

/**
 * Created by clifford.jakobsen on 22-05-2017. modified to Json by Margit
 * Inspired by https://github.com/omkreddy/kafka-examples/blob/master/producer/src/main/java/kafka/examples/consumer/BasicConsumerExample.java and https://gist.github.com/yaroncon
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.scenario.Settings;
import dk.artogis.hepwat.onlineservice.ConfigService;
import dk.artogis.hepwat.onlineservice.subscription.DataSubscription;
import dk.artogis.hepwat.onlineservice.subscription.DataSubscriptions;

import dk.artogis.hepwat.services.configuration.Configuration;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

//import org.codehaus.jackson.JsonNode;

public class JsonConsumer {

    private static Logger logger = Logger.getLogger(JsonConsumer.class);


    private String _topicName = null;
    private String  _clientId;
    private boolean _isConsuming = false;
    private KafkaConsumer<String, JsonNode> _consumer = null;
    private ArrayList<String> _topics;
    private ArrayList<Integer> _deviceIds ;

    public boolean isReady() {
        return (ConfigService.serviceConfiguration.getConfiguration() != null );
    }

   DataSubscriptions _dataSubscriptions;


    public JsonConsumer(DataSubscriptions dataSubscriptions, String clientId) {

        //if (logger.isTraceEnabled()) logger.trace("Start - JsonConsumer");
        if (logger.isInfoEnabled()) logger.info("Start - JsonConsumer: " + clientId.toString());
        _clientId = clientId;
        _topics = new ArrayList<String>();
        _deviceIds = new ArrayList<Integer>();
        _dataSubscriptions = dataSubscriptions;
        ArrayList<DataSubscription> dataSubscriptionsList = dataSubscriptions.getDataSubscriptions();
        ProcessingConfig[]processingconfigConfigs =  ConfigService.serviceConfiguration.getProcessingConfigs();

        for(DataSubscription dataSubscription : dataSubscriptionsList)
        {
            Integer aggType = dataSubscription.getAggtype();
            Integer calcType = dataSubscription.getCalctype();
            Integer dataType = dataSubscription.getDataType();
            String topic = getTopic(aggType, calcType, dataType, processingconfigConfigs);
            if ((topic != null) && !topic.isEmpty())
            {
                if (! _topics.contains(topic ) )
                    _topics.add(topic);
            }
            Integer deviceId = dataSubscription.getHepwatDeviceId();
            if (!_deviceIds.contains(deviceId))
                _deviceIds.add(deviceId);
        }

        if (logger.isTraceEnabled()) logger.trace("End - JsonConsumer");
    }
    private String getTopic(Integer aggType, Integer calcType, Integer dataType, ProcessingConfig[] processingConfigs)
    {
        for (ProcessingConfig processingConfig: processingConfigs) {
            if ((processingConfig.getAggType() == aggType) && (processingConfig.getCalcType() == calcType) && (processingConfig.getDataType() == dataType))
                return processingConfig.getTopic();
        }
        return  null;
    }

    public void consume()
    {
        if (logger.isTraceEnabled()) logger.trace("Start - consume");

        if(!isReady()) {
            if (logger.isInfoEnabled()) logger.info("consume: Consumer is not ready to start");
            return;
        }

        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ConfigService.serviceConfiguration.getConfiguration().getKafkaBroker());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, ConfigService.serviceConfiguration.getConfiguration().getTopicGroupId() + "-" + _clientId);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, ConfigService.serviceConfiguration.getConfiguration().getAutoOffSet());
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, ConfigService.serviceConfiguration.getConfiguration().getClientId()+ "-" + _clientId);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.connect.json.JsonDeserializer");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        Thread.currentThread().setContextClassLoader(null);
        _consumer = new KafkaConsumer<String, JsonNode>(properties);

        ArrayList<TopicPartition> topicPartitions = new ArrayList<>();
        for (String topic: _topics) {
            topicPartitions.add(new TopicPartition(topic, 0));
        }

        _consumer.assign(topicPartitions);
        _isConsuming = true;

        ConsumerRecords<String, JsonNode> records;
        boolean seekToend = true;
        if(seekToend) {
            _consumer.seekToEnd(topicPartitions);
        }

        try {
            while (_isConsuming) {
                records = _consumer.poll(10000);
                for (ConsumerRecord<String, JsonNode> record : records) {
                        for (IJsonConsumerListener listener : _listeners) {
                            Integer key = -1;
                            if (record.key() != null) {
                                if (record.key().contains("@")) {
                                    String keyAndCalcString = record.key();
                                    keyAndCalcString = keyAndCalcString.substring(1, keyAndCalcString.length());
                                    String[] keyAndCalcParts = keyAndCalcString.split("@");
                                    if (keyAndCalcParts.length > 0) {
                                        String[] keyParts = keyAndCalcParts[0].split(" ");
                                        if (keyParts.length > 0)
                                            key = new Integer(keyParts[0]);
                                    }
                                } else key = new Integer(record.key());
                                if (_deviceIds.contains(key)) {
                                    if (_dataSubscriptions.subscriptionExists(key, record.value())) {
                                        JsonNode jsonNode = record.value();
                                        listener.messageReceived(record.key(), jsonNode);
                                    }
                                }
                            }
                        }
                        if (logger.isTraceEnabled()) logger.trace(String.format("consume: received message topic =%s, partition =%s, offset = %d, message = %s", record.topic(), record.partition(), record.offset(), record.value().toString()));
                }
                try {
                    _consumer.commitSync();
                } catch (CommitFailedException e) {
                    logger.error("consume: Consumer could not commit offsets", e);
                }
            }
        }
        finally {
            if (logger.isInfoEnabled()) logger.info("Disconnecting from Kafka consumer: " + _clientId);
            _isConsuming = false;
            _consumer.close();
            if (logger.isInfoEnabled()) logger.info("Disconnected");
        }

        if (logger.isTraceEnabled()) logger.trace("End - consume");
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
