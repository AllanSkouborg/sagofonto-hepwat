package dk.artogis.hepwat.streamingStatusService.streamingprocess;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import dk.artogis.hepwat.calculation.StatusType;
import dk.artogis.hepwat.streamingStatusService.configuration.DeviceConfigurations;
import dk.artogis.hepwat.streamingStatusService.configuration.ServiceConfiguration;
import dk.artogis.hepwat.common.schema.AggregatedDataSchema;
import dk.artogis.hepwat.common.schema.Field;
import dk.artogis.hepwat.common.schema.FieldType;
import dk.artogis.hepwat.common.schema.RawDataSchema;
import dk.artogis.hepwat.streamingStatusService.configuration.StatusTypes;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.metrics.Stat;
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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Calculation {

    final Integer TypeNone = 0;
    final Integer TypeMax = 1;
    final Integer TypeHigh =  2;
    final Integer TypeLow =  3;
    final Integer TypeMin =  4;

    final Properties streamsConfiguration = new Properties();
    ObjectNode initialMeasurementsSum = null;
    //private Serde<JsonNode> jsonSerde
    private final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
    private final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
    private final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);
    private ServiceConfiguration serviceConfiguration = null;
    private KafkaStreams streams;
    private AggregatedDataSchema aggregatedDataSchema = null;
    private Logger logger = null;
    private String[] calculationFields = {RawDataSchema.f_deviceId, RawDataSchema.f_aggType, RawDataSchema.f_timeStamp,  RawDataSchema.f_calcType, RawDataSchema.f_value};
    private DeviceConfigurations deviceConfigurations;
    private StatusTypes statusTypes ;

    private Double Max;
    private Double High;
    private Double Low;
    private Double Min;
    private Double None;

    public Boolean getOk() {
        return isOk;
    }

    private Boolean isOk = false;

    public KafkaStreams getStreams() {
        return streams;
    }

    public Calculation(ServiceConfiguration serviceConfiguration, DeviceConfigurations deviceConfigurations, StatusTypes statusTypes, Logger instantiatedLogger)
    {
        logger = instantiatedLogger;
        this.serviceConfiguration = serviceConfiguration;
        this.deviceConfigurations = deviceConfigurations;
        this.statusTypes = statusTypes;
        List<String> calculationsFieldsList = new ArrayList<>(Arrays.asList(calculationFields));
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, serviceConfiguration.getConfiguration().getTopicGroupId());

        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, serviceConfiguration.getConfiguration().getClientId());
        // Where to find Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, serviceConfiguration.getConfiguration().getKafkaBroker());
        streamsConfiguration.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, TimeStampExtractor.class);
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, serviceConfiguration.getConfiguration().getAutoOffSet());
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, serviceConfiguration.getConfiguration().getCommitInterval());
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, serviceConfiguration.getConfiguration().getStateDir());
        streamsConfiguration.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG,"exactly_once" );
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
            if (calculationsFieldsList.contains(field.name)) {
                if (field.type == FieldType.INTEGER)
                    initialMeasurementsSum.put(field.name, 0);
                else if (field.type == FieldType.DOUBLE)
                    initialMeasurementsSum.put(field.name, 0.0);
                else if (field.type == FieldType.LONG)
                    initialMeasurementsSum.put(field.name, 0L);
            }
        }

        Max = statusTypes.getStatusTypeValue(TypeMax);
        High = statusTypes.getStatusTypeValue(TypeHigh);
        Low = statusTypes.getStatusTypeValue(TypeLow);
        Min = statusTypes.getStatusTypeValue(TypeMin);
        None = statusTypes.getStatusTypeValue(TypeNone);
        if ((Max != null)&& (High != null ) && (Low != null) && (Min != null) && (None != null))
            isOk = true;

     }

    public void run(ReentrantLock lock)
    //public void run()
    {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");

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
                measurementValueNode = value.get(RawDataSchema.f_value);
                deviceIdValueNode = value.get(RawDataSchema.f_deviceId);
                timestampValueNode = value.get(RawDataSchema.f_timeStamp);
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

            boolean hasCalculation = false;
            try {
                Integer id = 0;
                try {
                    id = value.get(RawDataSchema.f_deviceId).asInt();
                }
                catch (Exception ex)
                {
                    return  false;
                }

                lock.lock();
                try {
                    Integer calctype = 0;
                    Integer aggtype = 0;
                    try {
                        calctype = value.get(RawDataSchema.f_calcType).asInt();
                        calctype = value.get(RawDataSchema.f_calcType).asInt();
                    }
                    catch (Exception ex)
                    {
                        calctype = 0;
                    }
                    if ((deviceConfigurations.HasStatusSetting(id, calctype, aggtype)) && (serviceConfiguration.isInConfigurationCalculationTypes(calctype))) {
                        hasCalculation = true;
                    }
                }
                catch (Exception ex)
                {
                    logger.error("could not get configuration for device");
                    throw  new Exception("could not get deviceConfiguration calcType");
                }
                finally {
                    lock.unlock();
                }
            } catch (Exception ex) {
                return false;
            }
            return  hasCalculation;
        }
        );



        KStream<String, JsonNode> calculatedStream = filteredStream.mapValues( measurement -> Calculate.newValue( measurement, serviceConfiguration.calcType, deviceConfigurations, lock,  Max, High, Low, Min, None, logger) );

        calculatedStream.to(Serdes.String(), jsonSerde, serviceConfiguration.outputTopic );

        KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);
        streams.cleanUp();
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }


}
