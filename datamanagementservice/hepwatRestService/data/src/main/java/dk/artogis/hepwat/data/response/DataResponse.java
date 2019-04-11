package dk.artogis.hepwat.data.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.common.database.MongoDbConnection;
import dk.artogis.hepwat.common.database.PostGreSQLConnection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.data.config.DataStore;
import org.apache.log4j.Logger;

import dk.artogis.hepwat.data.DataRecord;
import dk.artogis.hepwat.services.configuration.ProcessingConfig;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse extends Status implements Serializable
{

    public DataRecord[] dataRecords;
    public DataResponse()
    {
    }

    public DataResponse(Integer id, ZonedDateTime startTime, ZonedDateTime endTime, Integer aggtype, Integer calctype, Integer datatype, Integer limit, Long lastTimeStamp, PostGreSQLConnection connection)
    {
        this.Success = false;
        Logger logger = Logger.getLogger(DataResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering dataResponse");
        try {
            connection.connect();
            ProcessingConfig processingConfig = ProcessingConfig.GetProcessingConfig(aggtype, calctype, datatype, connection);
            if (processingConfig == null)
                throw  new Exception("no processingconfig found");
            if(logger.isTraceEnabled())
                logger.trace("Processing config found ");
            DataStore dataStore = DataStore.GetDataStore(processingConfig.getStore(), connection);
            if (dataStore == null)
                throw  new Exception("no Mongo db configured");
            MongoDbConnection mdbconnection = MongoDbConnection.getMongoDbConnection(dataStore.getServer(), dataStore.getPort(), dataStore.getDatabase(), null, null);
            if (mdbconnection == null)
                throw  new Exception("no Mongo db connection available on configured server");
            //mdbconnection.connect();
            if(logger.isInfoEnabled())
                logger.info("Mongo connection established");
            List<DataRecord> dataRecordList =  DataRecord.GetDataRecords(id, startTime, endTime, aggtype, calctype, datatype, limit, lastTimeStamp, mdbconnection, processingConfig.getCollection() );
            if (dataRecordList != null ) {
                dataRecords = new DataRecord[dataRecordList.size()];
                dataRecords = (DataRecord[]) dataRecordList.toArray(dataRecords);
            }
            this.Success = true;

        }
        catch (Exception ex)
        {
            logger.error("Error in retrieving data records : " + ex.getMessage());
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving data records");
        }
        finally {
            if(logger.isTraceEnabled())
                logger.trace("Leaving dataResponse");
            connection.close();
        }

    }
    public DataResponse(Integer id, Integer aggtype, Integer calctype, Integer datatype,  PostGreSQLConnection connection)
    {
        this.Success = false;
        Logger logger = Logger.getLogger(DataResponse.class);
        if(logger.isTraceEnabled())
            logger.trace("Entering dataResponse");
        try {
            connection.connect();
            ProcessingConfig processingConfig = ProcessingConfig.GetProcessingConfig(aggtype, calctype, datatype, connection);
            if (processingConfig == null)
                throw  new Exception("no processingconfig found");
            if(logger.isTraceEnabled())
                logger.trace("Processing config found ");
            DataStore dataStore = DataStore.GetDataStore(processingConfig.getStore(), connection);
            if (dataStore == null)
                throw  new Exception("no Mongo db configured");
            MongoDbConnection mdbconnection = MongoDbConnection.getMongoDbConnection(dataStore.getServer(), dataStore.getPort(), dataStore.getDatabase(), null, null);
            if (mdbconnection == null)
                throw  new Exception("no Mongo db connection available on configured server");
            //mdbconnection.connect();
            if(logger.isInfoEnabled())
                logger.info("Mongo connection established");
            List<DataRecord> dataRecordList =  DataRecord.GetLastDataRecord(id,  aggtype, calctype, datatype, mdbconnection, processingConfig.getCollection() );
            if (dataRecordList != null ) {
                dataRecords = new DataRecord[dataRecordList.size()];
                dataRecords = (DataRecord[]) dataRecordList.toArray(dataRecords);
            }
            this.Success = true;

        }
        catch (Exception ex)
        {
            logger.error("Error in retrieving data records : " + ex.getMessage());
            this.Message = ex.getMessage();
            System.out.print("Error in retrieving data records");
        }
        finally {
            if(logger.isTraceEnabled())
                logger.trace("Leaving dataResponse");
            connection.close();
        }

    }
}
