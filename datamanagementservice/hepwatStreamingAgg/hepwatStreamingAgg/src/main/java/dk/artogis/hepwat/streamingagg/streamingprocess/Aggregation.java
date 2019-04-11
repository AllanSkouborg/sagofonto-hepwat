package dk.artogis.hepwat.streamingagg.streamingprocess;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import dk.artogis.hepwat.streamingagg.configuration.DeviceConfigurations;
import dk.artogis.hepwat.streamingagg.configuration.ServiceConfiguration;
import dk.artogis.hepwat.common.schema.*;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Aggregation  {

    final Properties streamsConfiguration = new Properties();
    ObjectNode initialMeasurementsSum = null;
    //private Serde<JsonNode> jsonSerde
    private final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
    private final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
    private final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);
    private ServiceConfiguration serviceConfiguration = null;
    private KafkaStreams streams;
    private Integer aggregationInterval = 10;
    private AggregatedDataSchema aggregatedDataSchema = null;
    private Logger logger = null;
    private String[] aggregationFields = {AggregatedDataSchema.f_deviceId, AggregatedDataSchema.f_aggType, AggregatedDataSchema.f_timeStamp, AggregatedDataSchema.f_count, AggregatedDataSchema.f_sum, AggregatedDataSchema.f_calcType, AggregatedDataSchema.f_value, AggregatedDataSchema.f_lastValue};

    private DeviceConfigurations deviceConfigurations;
    private Integer aggregationCalculationType;

    public KafkaStreams getStreams() {
        return streams;
    }

    public Aggregation(ServiceConfiguration serviceConfiguration, DeviceConfigurations deviceConfigurations, Integer aggregationCalculationType, Integer aggregationInterval,  Logger instantiatedLogger)
    {
        logger = instantiatedLogger;
        this.serviceConfiguration = serviceConfiguration;
        this.deviceConfigurations = deviceConfigurations;
        this.aggregationCalculationType = aggregationCalculationType;
        List<String> aggregationFieldsList = new ArrayList<>(Arrays.asList(aggregationFields));
        this.aggregationInterval = aggregationInterval;
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, serviceConfiguration.getConfiguration().getTopicGroupId());

        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, serviceConfiguration.getConfiguration().getClientId());
        // Where to find Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, serviceConfiguration.getConfiguration().getKafkaBroker());
        streamsConfiguration.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, TimeStampExtractor.class);
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, serviceConfiguration.getConfiguration().getAutoOffSet());
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, serviceConfiguration.getConfiguration().getCommitInterval());
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, serviceConfiguration.getConfiguration().getStateDir());
        // json Serde
        final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
        final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
        Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);

        // setup initial sum
        initialMeasurementsSum = JsonNodeFactory.instance.objectNode();
        aggregatedDataSchema = new AggregatedDataSchema();

        Map<Integer, Field> fields = null;

        fields = aggregatedDataSchema.getFields();

        for (Field field : fields.values())
        {
            if (aggregationFieldsList.contains(field.name)) {
                if (field.type == FieldType.INTEGER)
                    initialMeasurementsSum.put(field.name, 0);
                else if (field.type == FieldType.DOUBLE)
                    initialMeasurementsSum.put(field.name, 0.0);
                else if (field.type == FieldType.LONG)
                    initialMeasurementsSum.put(field.name, 0L);
            }
        }
    }

    public void run(ReentrantLock lock)
    //public void run()
    {
        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, JsonNode> rawStream = builder.stream(Serdes.String(), jsonSerde, serviceConfiguration.inputTopic);
        //filter if measurement has to be aggregated
        KStream<String , JsonNode> filteredStream = rawStream.filter((key, value) -> {

            if (key == null || value == null) {
                if (key == null)
                    logger.error("key is null, record is filtered out");
                if (value == null)
                    logger.error("Key: " + key +" The value is null, record is filtered out");
                return false;
            }

            Object measurementValueNode = null ;
            Object timestampValueNode = null ;
            Object deviceIdValueNode = null;
            try {
                measurementValueNode = value.get(IDataSchema.f_value);
                deviceIdValueNode = value.get(IDataSchema.f_deviceId);
                timestampValueNode = value.get(IDataSchema.f_timeStamp);
            }
            catch (Exception ex)
            {
                logger.error("Exception while getting basic data: for key : " + key.toString()+ " : execption : " + ex.getMessage());
            }
            finally {
                if (((measurementValueNode == null)||( measurementValueNode.equals(NullNode.getInstance()))) ||
                        ((deviceIdValueNode == null)||( deviceIdValueNode.equals(NullNode.getInstance())) ||
                                ((timestampValueNode == null)||( timestampValueNode.equals(NullNode.getInstance()))) ) ) {
                    logger.error("BASIC DATA IS MISSING");
                    logger.error("key : " + key.toString() + " value: " + value.toString());
                    return false;
                }
            }
            try{
                if (timestampValueNode instanceof LongNode)
                {
                   Long timestamp = ((LongNode) timestampValueNode).longValue();
                   if (!((timestamp > 0) && (timestamp < System.currentTimeMillis() +  86400000) ))
                   {
                       logger.error("key : " + key.toString() + " timestamp is out of range value: " + timestampValueNode.toString());
                       return false;
                   }
                }
                else
                {
                    logger.error("key : " + key.toString() + " timestamp is not correct datatype (Long) timestamp has value: " + timestampValueNode.toString());
                    return false;
                }
                if (measurementValueNode instanceof DoubleNode) {
                    Double measumentValue = ((DoubleNode) measurementValueNode).doubleValue();
                    if (!((measumentValue > Double.NEGATIVE_INFINITY) && (measumentValue < Double.POSITIVE_INFINITY))) {
                        logger.error("key : " + key.toString() + " measurementValue is out of range measurementValue has value: " + measurementValueNode.toString());
                        return false;
                    }
                }
                else if (measurementValueNode instanceof LongNode) {
                    Long measumentValue = ((LongNode) measurementValueNode).longValue();
                    if (!((measumentValue > Long.MIN_VALUE) && (measumentValue < Long.MAX_VALUE))) {
                        logger.error("key : " + key.toString() + " measurementValue is out of range, measurementValue has value: " + measurementValueNode.toString());
                        return false;
                    }
                }
                else if (measurementValueNode instanceof IntNode) {
                    Integer measumentValue = ((IntNode) measurementValueNode).intValue();
                    if (!((measumentValue > Integer.MIN_VALUE) && (measumentValue < Integer.MAX_VALUE))) {
                        logger.error("key : " + key.toString() + " measurementValue is out of range, measurementValue has value: " + measurementValueNode.toString());
                        return false;
                    }
                }
                else
                {
                    logger.error("key : " + key.toString() + " measurementValue is not correct datatype (Double or Long, or Integer) measurementValue has value: " + measurementValueNode.toString() );
                    return false;
                }
                if (deviceIdValueNode instanceof IntNode)
                {
                   Integer deviceId = ((IntNode)deviceIdValueNode).intValue();
                   if(! ((deviceId > 0 )&& (deviceId< Integer.MAX_VALUE)))
                   {
                       logger.error("key : " + key.toString() + " deviceId is out of range deviceId has value: " + deviceIdValueNode.toString());
                       return false;
                   }
                }
                else
                {
                    logger.error("key : " + key.toString() + " deviceId is not correct datatype (Integer) deviceId hase value: " + deviceIdValueNode.toString());
                    return false;
                }

            }
            catch (Exception ex)
            {
                logger.error("error on trying to unpack data, one or more nodes is not correct types");
                return false;
            }
            if (serviceConfiguration.skipFilter)
                        return true;
            boolean hasAggregation = false;
            try {
                Integer id = 0;
                try {
                    id = value.get(IDataSchema.f_deviceId).asInt();
                }
                catch (Exception ex)
                {
                    return  false;
                }

                lock.lock();
                try {
                    Integer calctype = 0;
                    try {
                        calctype = value.get(IDataSchema.f_calcType).asInt();
                    }
                    catch (Exception ex)
                    {
                        calctype = 0;
                    }
                    if (deviceConfigurations.HasAggregation(id,calctype))
                        hasAggregation = true;
                }
                catch (Exception ex)
                {
                    logger.trace("error in cheking configuraiton for device");
                    throw  new Exception("could not get deviceConfiguration aggType");
                }
                finally {
                    lock.unlock();
                }
            } catch (Exception ex) {
                return false;
            }
            return  hasAggregation;
        }
        );
        //original
        // KGroupedStream<String, JsonNode> groupedMeasurements = filteredStream.groupByKey(Serdes.String(), jsonSerde);

        KeyValueMapper<String, JsonNode, String > calcSelector = new KeyValueMapper<String, JsonNode, String>() {
            @Override
            public String apply(String s, JsonNode jsonNode) {
                Object calctypeObject = jsonNode.get(AggregatedDataSchema.f_calcType);
                String calctype = "0";
                if (calctypeObject != null)
                    calctype = calctypeObject.toString();
                return jsonNode == null ? s : s + " " + calctype;
            }
        };
         KGroupedStream<String, JsonNode> groupedMeasurements = filteredStream.groupBy(calcSelector, Serdes.String(), jsonSerde);


        KTable<Windowed<String>, JsonNode> timeWindowedAggregatedStream = groupedMeasurements.aggregate(
        () ->  initialMeasurementsSum, /* initializer */
        (aggKey, measurement, sum) -> Aggregate.newValue(aggKey, measurement, sum, serviceConfiguration.aggregationTypeId, aggregationCalculationType, logger, deviceConfigurations, lock ), /* adder */
        TimeWindows.of(TimeUnit.MINUTES.toMillis(aggregationInterval)), /* time-based window */
        jsonSerde, /* serde for aggregate value */
        serviceConfiguration.getConfiguration().getStateTopic() + aggregationInterval.toString() + "-minutes-" + serviceConfiguration.inputTopic + "-" + serviceConfiguration.outputTopic  /* state store name */);

        KStream<String, JsonNode> timeAggregatedStream = timeWindowedAggregatedStream.toStream((windowedDevice, aggregatedData)-> windowedDevice.toString()+ "," + serviceConfiguration.aggregationTypeId.toString()+ ","+ aggregatedData.get("calctype").asInt());

        timeAggregatedStream.to(Serdes.String(), jsonSerde, serviceConfiguration.outputTopic );

        KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);
        streams.cleanUp();
//        streams.setUncaughtExceptionHandler((Thread thread, Throwable throwable) -> {
//            logger.error("uncaought error in stream ");
//        });
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }


}
