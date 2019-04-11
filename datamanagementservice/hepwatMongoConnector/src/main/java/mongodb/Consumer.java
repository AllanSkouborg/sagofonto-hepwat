package mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import configuration.DeviceConfigurations;
import dk.artogis.hepwat.data.config.DataStore;
import kafka.IJsonConsumerListener;
import kafka.JsonConsumer;
import org.apache.log4j.Logger;
import dk.artogis.hepwat.services.configuration.Configuration;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer implements IJsonConsumerListener {


    private Sink _mongodbSink = null;


    private JsonConsumer _consumer = null;

    private boolean _running = false;
    public boolean isRunning() {
        return _running;
    }

    private Integer _numberOfMessagesConsumed = 0;
    public Integer getNumberOfMessagesConsumed() {
        return _numberOfMessagesConsumed;
    }

    private Integer _numberOfMessagesSinked = 0;
    public Integer getNumberOfMessagesSinked() {
        return _numberOfMessagesSinked;
    }


    static Logger logger = null;

    public Consumer(Configuration configuration, ProcessingConfig[] processingConfigs, DataStore dataStore, DeviceConfigurations deviceConfigurations, Logger instantiatedLogger) throws Exception {

        logger = instantiatedLogger;
        if(logger.isInfoEnabled())
            logger.info("Consumer instantiation started" );

        ArrayList<String> topics =  ProcessingConfig.getTopicsForAggTypesAndCalcTypes(configuration.getAggregationTypes(),configuration.getCalculationTypes(), processingConfigs);

        if(logger.isInfoEnabled())
            logger.info("topics found" );

        if (topics == null)
            throw new Exception("topics list are null");

        if (topics.size() == 0)
            throw new Exception("topics list contains no elements");

        _consumer = new JsonConsumer(configuration, topics, instantiatedLogger);
        if (_consumer == null)
            throw new Exception ("could not initialize consumer");

        _mongodbSink = new Sink(dataStore, configuration.getAggregationTypes(), configuration.getCalculationTypes(), processingConfigs, deviceConfigurations, configuration.getSkipFilter(), instantiatedLogger);
        if (_mongodbSink == null)
            throw new Exception ("could not initialize sink");

        if(logger.isInfoEnabled())
            logger.info("Consumer instantiated." );
    }

    public void start(ReentrantLock lock) {
        // on startup
        if(_running) {
            if(logger.isInfoEnabled())
                    logger.info("Mongodb sink is already running." );
            System.out.println("Mongodb sink is already running.");
            return;
        }
        _consumer.addListener(this);
        _running = true;
        _consumer.consume(lock);
    }


    @Override
    public void messageReceived(String key, JsonNode message, ReentrantLock lock) {
        if(logger.isTraceEnabled())
            logger.trace("Entering messageReceived");

        Map<String, Object> fieldNamesAndValues = new HashMap<String, Object>();


        if (message == null) {
            logger.error("record value is null, record is not stored");
            return;
        }
        if (key == null) {
            logger.error("key is null, record is not stored");
            return;
        }
        for(Iterator<String> fieldIterator = message.fieldNames(); fieldIterator.hasNext();) {
            String fieldName = fieldIterator.next();
            JsonNode value = message.get(fieldName);
            Object objValue = null;
            if (value != null) {
                if (value.isNumber())
                    objValue = value.numberValue();
                if (value.isTextual())
                    objValue = value.textValue();
            }
            fieldNamesAndValues.put(fieldName, objValue);
        }

        try {
            _mongodbSink.sink(key, fieldNamesAndValues, lock);
            _numberOfMessagesSinked++;
            if(logger.isInfoEnabled())
                logger.info("Consumer : Messages sinked : " + _numberOfMessagesSinked.toString());
            if(logger.isInfoEnabled()){
                if (key != null)
                    logger.info("sinked a record : " + key  + message.toString() );
                else
                    logger.info("sinked a record : " + message.toString() );
            }
        }
        finally {
            _numberOfMessagesConsumed++;
            if(logger.isInfoEnabled())
                logger.info("Consumer : Messages consumed : " + _numberOfMessagesConsumed.toString());
        }
    }

    public void stop() {
        // on shutdown
        _running = false;
        _consumer.stop();
        _mongodbSink.stop();
    }
}
