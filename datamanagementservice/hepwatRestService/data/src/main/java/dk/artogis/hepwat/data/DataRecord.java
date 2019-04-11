package dk.artogis.hepwat.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import dk.artogis.hepwat.calculation.AggregationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import dk.artogis.hepwat.common.database.MongoDbConnection;
import dk.artogis.hepwat.common.schema.AggregatedDataSchema;
import dk.artogis.hepwat.common.schema.RawDataSchema;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.io.IOException;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataRecord implements Serializable {

    private static String GREATER_THAN_OR_EQUAL_TO = "$gte";
    private static String GREATER_THAN = "$gt";
    private static String LESS_THAN_OR_EQUAL_TO = "$lte";

    private Integer id;
    private Double value;
    private Long timestamp;
    @JsonIgnore
    private String sTimestamp;

    private static String f_id = "hepwatDeviceId";
    private static String f_time = "endtime";
    private static String   f_value = "value";
    private static String   f_primaryIndex  = "_id";
    private static String   f_aggType  = "aggtype";
    private static String   f_calcType  = "calctype";

    //region getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getsTimestamp() {
        return sTimestamp;
    }

    public void setsTimestamp(String sTimestamp) {
        this.sTimestamp = sTimestamp;
    }

    //endregion getters and setters

    public DataRecord()
    {}

    public static List GetDataRecords(Integer id, ZonedDateTime startTime, ZonedDateTime endTime, Integer aggtype, Integer calctype, Integer datatype, Integer limit, Long lastTimeStamp, MongoDbConnection connection, String datastore) throws Exception
    {
        Logger logger = Logger.getLogger(DataRecord.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering GetDataRecord");

        List<DataRecord> dataRecordList = null;
        MongoDatabase db = null;
        MongoCollection<Document> collection = null;
        try {
            db = connection.getDatabase();

            collection = db.getCollection(datastore);
            Long lStartTime = startTime.toEpochSecond() * 1000;
            Long lEndTime = endTime.toEpochSecond() * 1000;


            if (aggtype == AggregationType.TypeNone)
                dataRecordList = getRawRecords(id, collection, lStartTime, lEndTime, aggtype, calctype, datatype, limit, lastTimeStamp);
            else
                dataRecordList = getAggregatedRecords(id, collection, lStartTime, lEndTime, aggtype, calctype, datatype, limit, lastTimeStamp);

        }
        catch (Exception ex)
        {
            logger.error("error in connection to Mongo or in getting data");
        }
        finally {
            //TODO: anything to close ? //MKB 05-09-2018 : noting found yet.
        }
        if(logger.isTraceEnabled())
            logger.trace("Leaving GetDataRecord");
        return dataRecordList;
    }

    public static List GetLastDataRecord(Integer id, Integer aggtype, Integer calctype, Integer datatype,  MongoDbConnection connection, String datastore) throws Exception
    {
        Logger logger = Logger.getLogger(DataRecord.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering GetLastDataRecord");

        List<DataRecord> dataRecordList = null;
        MongoDatabase db = null;
        MongoCollection<Document> collection = null;
        try {
            db = connection.getDatabase();

            collection = db.getCollection(datastore);

            if (aggtype == AggregationType.TypeNone)
                dataRecordList = getRawRecord(id, collection, aggtype, calctype, datatype);
            else
                dataRecordList = getAggregatedRecord(id, collection, aggtype, calctype, datatype);

        }
        catch (Exception ex)
        {
            logger.error("error in connection to Mongo or in getting data");
        }
        finally {
            //TODO: anything to close ? //MKB 05-09-2018 : noting found yet.
        }
        if(logger.isTraceEnabled())
            logger.trace("Leaving GetDataRecord");
        return dataRecordList;
    }

    private static List getRawRecords(Integer id, MongoCollection<Document> collection, Long lStartTime, Long lEndTime, Integer aggType, Integer calcType, Integer dataType, Integer limit, Long lastTimeStamp) {

        Logger logger = Logger.getLogger(DataRecord.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering getRawRecords");

        f_time = RawDataSchema.timeIndex;
        f_value = RawDataSchema.valueField;
        f_id = RawDataSchema.deviceIdIndex;

        FindIterable<Document> queryResul  = collection.find(new Document(f_id, id).append(f_aggType,aggType).append(f_calcType,calcType).append(f_time, new Document(GREATER_THAN_OR_EQUAL_TO, lStartTime).append(LESS_THAN_OR_EQUAL_TO,lEndTime))).limit(limit).sort(new Document(f_time, 1));
        if(logger.isTraceEnabled())
            logger.trace("query returned");
        List<DataRecord> records = new ArrayList<DataRecord>();
        boolean first = true;
        MongoCursor<Document> cursor = queryResul.iterator();
        try {
            while (cursor.hasNext()) {
                if ((first)&&(logger.isTraceEnabled()))
                {
                    logger.trace("getting first record");
                    first = false;
                }
                Document doc = cursor.next();
                DataRecord dataRecord =  new DataRecord();
                dataRecord.id = id;
                dataRecord.timestamp = doc.getLong(f_time);
                try {
                    dataRecord.value = doc.getDouble(f_value);
                }
                catch (Exception ex)
                {
                    dataRecord.value =  ((double)doc.getInteger(f_value));
                }
                records.add(dataRecord);
            }
        }
        catch (Exception ex)
        {
            System.out.println("error in reading bson");
        }
        finally {
           cursor.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving getRawRecords");
        }

        return  records;
    }

    private static List getRawRecord(Integer id, MongoCollection<Document> collection, Integer aggType, Integer calcType, Integer dataType) {

        Logger logger = Logger.getLogger(DataRecord.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering getRawRecords");

        f_time = RawDataSchema.timeIndex;
        f_value = RawDataSchema.valueField;
        f_id = RawDataSchema.deviceIdIndex;

        FindIterable<Document> queryResul  = collection.find(new Document(f_id, id).append(f_aggType,aggType).append(f_calcType,calcType)).limit(1).sort(new Document(f_time, -1));
        if(logger.isTraceEnabled())
            logger.trace("query returned");
        List<DataRecord> records = new ArrayList<DataRecord>();
        boolean first = true;
        MongoCursor<Document> cursor = queryResul.iterator();
        try {
            while (cursor.hasNext()) {
                if ((first)&&(logger.isTraceEnabled()))
                {
                    logger.trace("getting first record");
                    first = false;
                }
                Document doc = cursor.next();
                DataRecord dataRecord =  new DataRecord();
                dataRecord.id = id;
                dataRecord.timestamp = doc.getLong(f_time);
                try {
                    dataRecord.value = doc.getDouble(f_value);
                }
                catch (Exception ex)
                {
                    dataRecord.value =  ((double)doc.getInteger(f_value));
                }
                records.add(dataRecord);
            }
        }
        catch (Exception ex)
        {
            System.out.println("error in reading bson");
        }
        finally {
            cursor.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving getRawRecords");
        }

        return  records;
    }

    private static List getAggregatedRecords(Integer id, MongoCollection<Document> collection, Long lStartTime, Long lEndTime, Integer aggType, Integer calcType,  Integer dataType, Integer  limit, Long lastTimeStamp ) throws Exception{

        Logger logger = Logger.getLogger(DataRecord.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering getAggregatedRecords");
        FindIterable<Document> queryResul = null;

        String startTimeOperator = GREATER_THAN_OR_EQUAL_TO;
        if (lastTimeStamp != null)
        {
            startTimeOperator = GREATER_THAN;
            lStartTime = lastTimeStamp;
        }

        f_time = AggregatedDataSchema.timeIndex;
        f_value = AggregatedDataSchema.valueField;
        f_id = AggregatedDataSchema.deviceIdIndex;
        f_primaryIndex = AggregatedDataSchema.primaryIndex;
        f_aggType = AggregatedDataSchema.aggregationTypeField;
        f_calcType = AggregatedDataSchema.calculationTypeField;

        queryResul  = collection.find(new Document(f_id, id).append(f_aggType,aggType).append(f_calcType,calcType).append(f_time, new Document(startTimeOperator, lStartTime).append(LESS_THAN_OR_EQUAL_TO,lEndTime))).limit(limit).sort(new Document(f_time, 1));
        if(logger.isTraceEnabled())
            logger.trace("query returned");

        List<DataRecord> records = new ArrayList<DataRecord>();
        boolean first = true;
        MongoCursor<Document> cursor = queryResul.iterator();
        try {
            while (cursor.hasNext()) {
                if ((first)&&(logger.isTraceEnabled()))
                {
                    logger.trace("getting first record");
                    first = false;
                }
                Document doc = cursor.next();
                DataRecord dataRecord =  new DataRecord();
                dataRecord.id =  id;
                try {
                    dataRecord.timestamp = doc.getLong("endtime");
                }
                catch (Exception ex)
                {
                    throw new Exception("could not unpack data");
                }
                try {
                    dataRecord.value = doc.getDouble(f_value);
                }
                catch (Exception ex)
                {
                    dataRecord.value =  ((double)doc.getInteger(f_value));
                }
                records.add(dataRecord);
            }
        }
        catch (Exception ex)
        {
            System.out.println("error in reading bson");
            throw new Exception( "error parsing data");
        }
        finally {
            cursor.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving getAggregatedRecords");
        }

        return  records;
    }
    private static List getAggregatedRecord(Integer id, MongoCollection<Document> collection,  Integer aggType, Integer calcType,  Integer dataType) throws Exception{

        Logger logger = Logger.getLogger(DataRecord.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering getAggregatedRecords");
        FindIterable<Document> queryResul = null;

        f_time = AggregatedDataSchema.timeIndex;
        f_value = AggregatedDataSchema.valueField;
        f_id = AggregatedDataSchema.deviceIdIndex;
        f_primaryIndex = AggregatedDataSchema.primaryIndex;
        f_aggType = AggregatedDataSchema.aggregationTypeField;
        f_calcType = AggregatedDataSchema.calculationTypeField;

        queryResul  = collection.find(new Document(f_id, id).append(f_aggType,aggType).append(f_calcType,calcType)).limit(1).sort(new Document(f_time, -1));
        if(logger.isTraceEnabled())
            logger.trace("query returned");

        List<DataRecord> records = new ArrayList<DataRecord>();
        boolean first = true;
        MongoCursor<Document> cursor = queryResul.iterator();
        try {
            while (cursor.hasNext()) {
                if ((first)&&(logger.isTraceEnabled()))
                {
                    logger.trace("getting first record");
                    first = false;
                }
                Document doc = cursor.next();
                DataRecord dataRecord =  new DataRecord();
                dataRecord.id =  id;
                try {
                    dataRecord.timestamp = doc.getLong("endtime");
                }
                catch (Exception ex)
                {
                    throw new Exception("could not unpack data");
                }
                try {
                    dataRecord.value = doc.getDouble(f_value);
                }
                catch (Exception ex)
                {
                    dataRecord.value =  ((double)doc.getInteger(f_value));
                }
                records.add(dataRecord);
            }
        }
        catch (Exception ex)
        {
            System.out.println("error in reading bson");
            throw new Exception( "error parsing data");
        }
        finally {
            cursor.close();
            if(logger.isTraceEnabled())
                logger.trace("Leaving getAggregatedRecords");
        }

        return  records;
    }
    public JsonNode ToJson()
    {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode newJsonNode = mapper.convertValue(this, JsonNode.class);
        return  newJsonNode;
    }
}
