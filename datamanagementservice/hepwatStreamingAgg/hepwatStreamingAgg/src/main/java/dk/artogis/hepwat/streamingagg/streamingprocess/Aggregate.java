package dk.artogis.hepwat.streamingagg.streamingprocess;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.artogis.hepwat.calculation.AggregationCalculationType;
import dk.artogis.hepwat.common.schema.AggregatedDataSchema;
import dk.artogis.hepwat.calculation.AggregationType;
import dk.artogis.hepwat.streamingagg.configuration.DeviceConfigurations;
import org.apache.log4j.Logger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
class Aggregate {


    static JsonNode newValue(String measurementKey, JsonNode measurement, JsonNode sum,  Integer aggType, Integer aggregationCalculation, Logger logger, DeviceConfigurations deviceConfigurations, Lock lock)
    {
        if(logger.isTraceEnabled()) {
            logger.trace("Received measurement: " + measurement.toString());
            logger.trace("state result received: " + sum.toString());
        }
        Integer hepwatDeviceId = 0;
        Integer actCalcType = 0;
        try {
            hepwatDeviceId = measurement.get(AggregatedDataSchema.f_deviceId).asInt();

            if (measurement.get(AggregatedDataSchema.f_calcType) != null)
                actCalcType = measurement.get(AggregatedDataSchema.f_calcType).asInt();
        }
        catch (Exception ex)
        {
            logger.error("could not get device id or calc type from measurement");
        }
        Double scaleToUint = 1.0;
        lock.lock();
        try {
            scaleToUint = deviceConfigurations.GetScaleToUnit(hepwatDeviceId, actCalcType);
        }
        catch (Exception ex)
        {
            logger.error("could not get scaleToUnit");
        }
        finally {
            lock.unlock();
        }
        if (aggregationCalculation == AggregationCalculationType.TypeCalcAverage)
            return  newAverage(measurementKey, measurement, sum, aggType, logger, scaleToUint);
        else  if (aggregationCalculation == AggregationCalculationType.TypeCalcSum)
            return newSum(measurementKey, measurement, sum, aggType, logger, scaleToUint);
        else if (aggregationCalculation == AggregationCalculationType.TypeCalcDeltaSum)
           return newDeltaSum(measurementKey, measurement, sum, aggType, logger, scaleToUint);
        else return  null;

    }

    private static JsonNode newAverage(String measurementKey, JsonNode measurement, JsonNode sum,  Integer aggType, Logger logger, Double scaleToUnit) {


        ObjectNode newSum = JsonNodeFactory.instance.objectNode();
        Integer hepwatDeviceId = 0;
        try{
            hepwatDeviceId = measurement.get(AggregatedDataSchema.f_deviceId).asInt();
            Integer actCalcType = 0;
            if (measurement.get(AggregatedDataSchema.f_calcType) != null)
                actCalcType = measurement.get(AggregatedDataSchema.f_calcType).asInt();

            newSum.put(AggregatedDataSchema.f_deviceId, hepwatDeviceId);
            double sumValue = measurement.get(AggregatedDataSchema.f_value).asDouble() + sum.get(AggregatedDataSchema.f_sum).asDouble();
            newSum.put(AggregatedDataSchema.f_sum, sumValue);
            int count = sum.get(AggregatedDataSchema.f_count).asInt() + 1;
            newSum.put(AggregatedDataSchema.f_count, count);
            double average = (sumValue / count)  * scaleToUnit;
            newSum.put(AggregatedDataSchema.f_aggType, aggType);
            newSum.put(AggregatedDataSchema.f_calcType, actCalcType);
            newSum.put(AggregatedDataSchema.f_value, average);
            newSum.put(AggregatedDataSchema.f_timeStamp, measurement.get(AggregatedDataSchema.f_timeStamp).asLong());
            newSum.put(AggregatedDataSchema.f_lastValue, measurement.get(AggregatedDataSchema.f_value));

            if (logger.isInfoEnabled()) {
                logger.info("Calculated newSum: " + newSum.toString());
            }

            Long actTime = System.currentTimeMillis();
            Long actMeasurementTime = measurement.get("timestamp").asLong();
            if (actTime - actMeasurementTime > 300000)
                logger.error("Data seems to be delayed, acttime: " + actTime.toString() + "  actMeasurementTime: " + actMeasurementTime.toString());
        }
        catch (Exception ex)
        {
            logger.error("something went wrong in the calculation newAverage");
            return null;
        }
        return newSum;
    }
    private static JsonNode newSum(String measurementKey, JsonNode measurement, JsonNode sum, Integer aggType, Logger logger, Double scaleToUnit) {

        ObjectNode newSum = JsonNodeFactory.instance.objectNode();

        Integer hepwatDeviceId = 0;
        try{
            hepwatDeviceId = measurement.get(AggregatedDataSchema.f_deviceId).asInt();
            Integer actCalcType = 0;
            if (measurement.get(AggregatedDataSchema.f_calcType) != null)
                actCalcType = measurement.get(AggregatedDataSchema.f_calcType).asInt();


            newSum.put(AggregatedDataSchema.f_deviceId, hepwatDeviceId);
            double sumValue = (measurement.get(AggregatedDataSchema.f_value).asDouble() +  sum.get(AggregatedDataSchema.f_sum).asDouble()) * scaleToUnit;
            newSum.put(AggregatedDataSchema.f_sum, sumValue);
            int count = sum.get(AggregatedDataSchema.f_count).asInt() + 1;
            newSum.put(AggregatedDataSchema.f_count, count);
            newSum.put(AggregatedDataSchema.f_aggType, aggType );
            newSum.put(AggregatedDataSchema.f_calcType, actCalcType);
            newSum.put(AggregatedDataSchema.f_value, sumValue);
            newSum.put(AggregatedDataSchema.f_timeStamp, measurement.get(AggregatedDataSchema.f_timeStamp).asLong());
            newSum.put(AggregatedDataSchema.f_lastValue, measurement.get(AggregatedDataSchema.f_value));

            if(logger.isInfoEnabled()){
                logger.info("Calculated newSum: " + newSum.toString());
            }

            Long actTime = System.currentTimeMillis();
            Long actMeasurementTime = measurement.get("timestamp").asLong();
            if (actTime - actMeasurementTime > 300000)
                logger.error("Data seems to be delayed, acttime: " + actTime.toString() + " actMeasurementTime: " + actMeasurementTime.toString() );
        }
        catch (Exception ex)
        {
            logger.error("something went wrong in the calculation newAverage");
            return null;
        }
        return newSum;
    }
    private static JsonNode newDeltaSum(String measurementKey, JsonNode measurement, JsonNode sum,  Integer aggType,  Logger logger, Double scaleToUnit) {


        ObjectNode newSum = JsonNodeFactory.instance.objectNode();

        try{
            Integer hepwatDeviceId = 0;
            hepwatDeviceId = measurement.get(AggregatedDataSchema.f_deviceId).asInt();
            Integer actCalcType = 0;
            if (measurement.get(AggregatedDataSchema.f_calcType) != null)
                actCalcType = measurement.get(AggregatedDataSchema.f_calcType).asInt();

            Integer  sumDeviceId = sum.get(AggregatedDataSchema.f_deviceId).asInt();

            newSum.put(AggregatedDataSchema.f_deviceId, hepwatDeviceId);
            double sumLastValue = sum.get(AggregatedDataSchema.f_lastValue).asDouble();
            double sumValue = sum.get(AggregatedDataSchema.f_value).asDouble();
            double measurementValue =  measurement.get(AggregatedDataSchema.f_value).asDouble();
            double deltaValue = 0;
            if (sumDeviceId != 0) //new time window
                deltaValue =  measurementValue - sumLastValue; //starting on new use period and new use sum
            double newSumValue = (sumValue + deltaValue) * scaleToUnit;

            newSum.put(AggregatedDataSchema.f_sum, deltaValue);
            int count = sum.get(AggregatedDataSchema.f_count).asInt() + 1;
            newSum.put(AggregatedDataSchema.f_count, count);
            newSum.put(AggregatedDataSchema.f_aggType, aggType );
            newSum.put(AggregatedDataSchema.f_calcType, actCalcType);
            newSum.put(AggregatedDataSchema.f_value, newSumValue);
            newSum.put(AggregatedDataSchema.f_lastValue, measurementValue);
            newSum.put(AggregatedDataSchema.f_timeStamp, measurement.get(AggregatedDataSchema.f_timeStamp).asLong());

            if(logger.isInfoEnabled()){
                logger.info("Received measurement: " + measurement.toString());
                logger.info("Received sum: " + sum.toString());
                logger.info("Calculated newSum: " + newSum.toString());
            }

            Long actTime = System.currentTimeMillis();
            Long actMeasurementTime = measurement.get("timestamp").asLong();
            if (actTime - actMeasurementTime > 300000)
                logger.error("Data seems to be delayed, acttime: " + actTime.toString() + " actMeasurementTime: " + actMeasurementTime.toString() );
        }
        catch (Exception ex)
        {
            logger.error("something went wrong in the calculation newAverage");
            return null;
        }

        return newSum;
    }
}
