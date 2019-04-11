package mongodb;

//import com.mongodb.MongoClient;
import configuration.DeviceConfigurations;
import dk.artogis.hepwat.calculation.AggregationType;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;
import dk.artogis.hepwat.calculation.response.DataType;
import dk.artogis.hepwat.common.database.MongoDbConnection;
import dk.artogis.hepwat.common.schema.*;

import dk.artogis.hepwat.data.config.DataStore;
import org.apache.log4j.Logger;
import org.bson.Document;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by clifford.jakobsen on 02-06-2017.
 */
public class Sink {

    //private mongodb.Settings _mongodbSettings;
    private MongoDatabase _db;
    private MongoCollection<Document>  _documents;
    private boolean _useUpsert = false;
    MongoDbConnection _connection = null;
    Integer[] _aggTypes = null;
    Integer[] _calcTypes = null;
    Boolean _skipFilter = false;
    ProcessingConfig[] _processingConfigs;
    private RawDataSchema _rawSchema = new RawDataSchema();
    private AggregatedDataSchema _aggSchema = new AggregatedDataSchema();
    private StatusRawDataSchema _statusRawSchema = new StatusRawDataSchema();
    private StatusAggregatedDataSchema _statusAggSchema = new StatusAggregatedDataSchema();
    Map<String, MongoCollection<Document> >  _documentCollectionList = new HashMap<>();
    DeviceConfigurations _deviceConfigurations ;

    static Logger logger = null;


    public Sink(DataStore mongoDbDataStore, Integer[] aggTypes, Integer[] calculationTypes, ProcessingConfig[] processingConfigs, DeviceConfigurations deviceConfigurations, Boolean skipFilter, Logger instantiatedLogger)
    {
        logger = instantiatedLogger;
        if(logger.isInfoEnabled())
            logger.info("Entering constructor");
        _calcTypes = calculationTypes;
        _aggTypes = aggTypes;
        _deviceConfigurations = deviceConfigurations;
        _skipFilter = skipFilter;
        _processingConfigs = processingConfigs;
        _connection = MongoDbConnection.getMongoDbConnection(mongoDbDataStore.getServer(), mongoDbDataStore.getPort(), mongoDbDataStore.getDatabase(), null, null);

        try {
            _db = _connection.getDatabase();
        }
        catch (Exception ex)
        {
            logger.error("Could not connecto to MongoDb: " + ex.getMessage());
            return;
        }
        new RawDataSchema();
        IDataSchema schema;
        MongoCollection<Document>  documents;
        String primaryIndex = null;
        String timeIndex = null;
        String deviceIdIndex = null;
        for (ProcessingConfig processingConfig : processingConfigs) {
            String collectionName = processingConfig.getCollection();
            if(logger.isInfoEnabled())
                logger.info("Cheking indexes  for collection : " + collectionName);
            if (!_documentCollectionList.keySet().contains(collectionName)) {
                _documentCollectionList.put(collectionName, _db.getCollection(collectionName));
                if (processingConfig.getAggType() == AggregationType.TypeNone) {
                    if(logger.isInfoEnabled())
                        logger.info("For collection: " + collectionName +  " Schema is raw");
                    schema = (IDataSchema) _rawSchema;
                    primaryIndex = RawDataSchema.primaryIndex;
                    timeIndex = RawDataSchema.timeIndex;
                    deviceIdIndex = RawDataSchema.deviceIdIndex;
                }
                else
                {
                    if(logger.isInfoEnabled())
                        logger.info("For collection: " + collectionName +  " Schema is agg");
                    schema = (IDataSchema) _aggSchema;
                    primaryIndex = AggregatedDataSchema.primaryIndex;
                    timeIndex = AggregatedDataSchema.timeIndex;
                    deviceIdIndex = AggregatedDataSchema.deviceIdIndex;
                }
                documents = _documentCollectionList.get(collectionName);
                Boolean foundTimeIndex = false;
                Boolean foundPrimaryIndex = false;
                Boolean foundDeviceIdIndex = false;
                for (Document index : documents.listIndexes()) {
                    if ( index.get("name").toString().contains(primaryIndex))
                        foundPrimaryIndex = true;
                    if ( index.get("name").toString().contains(deviceIdIndex))
                        foundDeviceIdIndex = true;
                    if ( index.get("name").toString().contains(timeIndex))
                        foundTimeIndex = true;
                }
                if (!foundTimeIndex) {
                    for(Field field: schema.getFields().values()) {
                        if (new String(field.name).equals(timeIndex)) {
                            documents.createIndex(Indexes.descending(timeIndex));
                            if(logger.isInfoEnabled())
                                logger.info("For collection: " + collectionName +  "Index : " + timeIndex + " created");
                            break;
                        }
                    }
                }
                if (!foundPrimaryIndex) {
                    for(Field field: schema.getFields().values()) {
                        if (new String(field.name).equals(primaryIndex)) {
                            documents.createIndex(Indexes.descending(primaryIndex));
                            if(logger.isInfoEnabled())
                                logger.info("For collection: " + collectionName +  "Index : " + primaryIndex + " created");
                            break;
                        }
                    }
                }
                if (!foundDeviceIdIndex) {
                    for(Field field: schema.getFields().values()) {
                        if (new String(field.name).equals(deviceIdIndex)) {
                            documents.createIndex(Indexes.descending(deviceIdIndex));
                            if(logger.isInfoEnabled())
                                logger.info("For collection: " + collectionName +  "Index : " + deviceIdIndex + " created");
                            break;
                        }
                    }
                }
            }
        }
        if(logger.isInfoEnabled())
            logger.info("Leaving constructor");
    }

    public void sink(String key, Map<String, Object> fieldNamesAndValues, ReentrantLock lock)
    {
        if(logger.isTraceEnabled())
            logger.trace("sink : entered");
        boolean first = true;
        Document newDoc  = null;
        Integer aggType = AggregationType.TypeNone;
        Integer calcType = CalculationAndStore.TypeNone;
        Integer dataType = DataType.TypeData;
        Integer hepwatDeviceId = 0;
        IDataSchema schema= null;
        try {

            if (fieldNamesAndValues == null)
                throw new Exception("message is null");
            if (fieldNamesAndValues.keySet() == null)
                throw new Exception("message keyset is null");
            if (fieldNamesAndValues.keySet().contains(IDataSchema.aggregationTypeField))
                aggType = (Integer) fieldNamesAndValues.get(IDataSchema.aggregationTypeField);
            if (fieldNamesAndValues.keySet().contains(IDataSchema.calculationTypeField))
                calcType = (Integer) fieldNamesAndValues.get(IDataSchema.calculationTypeField);
            if (fieldNamesAndValues.keySet().contains(IDataSchema.dataTypeField))
                dataType = (Integer) fieldNamesAndValues.get(IDataSchema.dataTypeField);
            if (fieldNamesAndValues.keySet().contains(IDataSchema.f_deviceId))
                hepwatDeviceId = (Integer) fieldNamesAndValues.get(IDataSchema.f_deviceId);

            if (dataType == DataType.TypeStatus) {
                if (aggType == AggregationType.TypeNone) {
                    schema = _statusRawSchema;
                    _useUpsert = false;
                } else {
                    _useUpsert = true;
                    schema = _statusAggSchema;
                }
            }
            else
            {
                if (aggType == AggregationType.TypeNone) {
                    schema = _rawSchema;
                    _useUpsert = false;
                } else {
                    _useUpsert = true;
                    schema = _aggSchema;
                }
            }
            Boolean storeData = true;
            Boolean error = false;
            if (!_skipFilter) {
                try {
                    lock.lock();
                    storeData = _deviceConfigurations.store(hepwatDeviceId, calcType, aggType, dataType);

                } catch (Exception ex) {
                    logger.error("Could not get deviceconfiguration, store information");
                    storeData = false;
                    error = true;
                } finally {
                    lock.unlock();
                    if (error)
                        return;
                }
            }

            if(!storeData)
            {
                logger.trace("record for hepwatDeviceId: " + hepwatDeviceId.toString() +  " is not stored, due to configuration");
                return;
            }
            ProcessingConfig processingConfig = ProcessingConfig.getProcessingConfig(aggType, calcType, dataType,  _processingConfigs);

            if (processingConfig == null)
                throw new Exception("could not find processing config for the data");

            for (Field field : schema.getFields().values()) {
                if (first) {
                    if (fieldNamesAndValues.containsKey(field.name)) {
                        Object value = fieldNamesAndValues.get(field.name);
                        if (value instanceof String)
                            value = value.toString();
                        newDoc = new Document(field.name, value);
                        first = false;
                    }
                    else
                    {  // add fileds for calctype and agg type
                        newDoc = new Document(field.name, 0);
                        first = false;
                    }
                } else {
                    if (newDoc != null) {
                        if (fieldNamesAndValues.containsKey(field.name)) {
                            Object value = fieldNamesAndValues.get(field.name);
                            if (value instanceof String)
                                value = value.toString();
                            newDoc.append(field.name, value);
                        }
                        else
                        {  // add fileds for calctype and agg type if not present
                            newDoc.append(field.name, 0);
                        }
                    }
                }
            }

            if (newDoc == null)
                throw new Exception("could not create document for data");

            if (fieldNamesAndValues.containsKey(IDataSchema.aggregationTypeField)) {
                if (fieldNamesAndValues.get(IDataSchema.aggregationTypeField) != AggregationType.TypeNone) {
                    if (logger.isTraceEnabled())
                        logger.trace("sink : aggregatede data detected");
                    String[] keyParts = key.split("/");
                    if (keyParts.length > 1) {
                        Long endTime = Long.parseLong(keyParts[1].substring(0, keyParts[1].length() - 5)); // removing "]"
                        newDoc.append("endtime", endTime);
                    }
                }
            }
            _documents = _documentCollectionList.get(processingConfig.getCollection());
            if (_documents == null)
                throw new Exception("Could not get documnet collection for actual data");
            if (_useUpsert) {
                _documents.replaceOne(eq("_id", key), newDoc, new UpdateOptions().upsert(true).bypassDocumentValidation(true));
                if (logger.isTraceEnabled())
                    logger.trace("sink: document upserted");
            } else {
                _documents.insertOne(newDoc);
                if (logger.isTraceEnabled())
                    logger.trace("sink : document inserted");
            }

        }
        catch (Exception ex)
        {
            logger.error("sink: could not sink message" + ex.getMessage());
        }
        finally {
            if(logger.isTraceEnabled())
                logger.trace("sink : leaving");
        }

    }
    public void stop()
    {
        if (_connection != null)
            _connection.close();
    }

    public boolean hasAggType(Integer actAggType)
    {
        for (Integer aggType : _aggTypes)
        {
            if (aggType == actAggType)
                return true;
        }
        return  false;
    }


}
