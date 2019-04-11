package dk.artogis.hepwat.streamingStatusService.streamingprocess;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.artogis.hepwat.calculation.AggregationAndStore;
import dk.artogis.hepwat.common.schema.StatusRawDataSchema;
import dk.artogis.hepwat.common.schema.AggregatedDataSchema;
import dk.artogis.hepwat.common.schema.RawDataSchema;
import dk.artogis.hepwat.streamingStatusService.configuration.DeviceConfigurations;
import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import java.util.concurrent.locks.ReentrantLock;

public class Calculate {


    public static JsonNode newValue(JsonNode measurement, Integer calcType, DeviceConfigurations deviceConfigurations, ReentrantLock lock, Double MaxValue, Double HighValue, Double LowValue, Double MinValue, Double NoValue, Logger logger)
    {
        Integer IsStatus = 1;

        Double MinAlarm = MinValue;
        Double LowAlarm = LowValue;
        Double MaxAlarm = MaxValue;
        Double HighAlarm = HighValue;
        Double NoStatus = NoValue;

        if(logger.isInfoEnabled()){
            logger.info("received value: " + measurement.toString());
        }

        Double Max = null;
        Double High = null;
        Double Low = null;
        Double Min = null;

        ObjectNode newValue = JsonNodeFactory.instance.objectNode();

        Integer hepwatDeviceId = 0;
        try{
            hepwatDeviceId = measurement.get(RawDataSchema.f_deviceId).asInt();
            Integer actCalcType = 0;
            if (measurement.get(RawDataSchema.f_calcType) != null)
                actCalcType = measurement.get(AggregatedDataSchema.f_calcType).asInt();

            Integer actAggType = 0;
            if (measurement.get(RawDataSchema.f_aggType) != null)
                actAggType = measurement.get(AggregatedDataSchema.f_aggType).asInt();

            lock.lock();
            try {
                AggregationAndStore aggregationAndStore = deviceConfigurations.GetStatusSetting(hepwatDeviceId, actCalcType, actAggType);
                if (aggregationAndStore != null)
                {
                    Max = aggregationAndStore.getMax();
                    Min = aggregationAndStore.getMin();
                    High = aggregationAndStore.getHigh();
                    Low = aggregationAndStore.getLow();
                }
            }
            catch (Exception ex)
            {
                logger.error("could not get status limits");
            }
            finally {
                lock.unlock();
            }
            Integer sequence = CalculateSequenceNumber(Max, High, Low, Min);

            Double value = measurement.get(RawDataSchema.f_value).asDouble();
            Double statusValue = getaAlarm(Low, Min, High, Max,  MinAlarm, LowAlarm, HighAlarm, MaxAlarm, NoStatus, sequence, value);


            newValue.put(RawDataSchema.f_deviceId, hepwatDeviceId);
            newValue.put(RawDataSchema.f_aggType, actAggType);
            newValue.put(RawDataSchema.f_calcType, calcType);
            newValue.put(RawDataSchema.f_value, statusValue);
            newValue.put(StatusRawDataSchema.f_dataType, IsStatus);

            newValue.put(RawDataSchema.f_timeStamp, measurement.get(RawDataSchema.f_timeStamp).asLong());

            if(logger.isInfoEnabled()){
                logger.info("Calculated newValue: " + newValue.toString());
            }

            Long actTime = System.currentTimeMillis();
            Long actMeasurementTime = measurement.get("timestamp").asLong();
            if (actTime - actMeasurementTime > 300000)
                logger.error("Data seems to be delayed, acttime: " + actTime.toString() + "actMeasurementTime: " + actMeasurementTime.toString() );
        }
        catch (Exception ex)
        {
            logger.error("something went wrong in the calculation ");
            return null;
        }
        return newValue;


    }

    private static Double getaAlarm(Double low, Double min, Double high,  Double max, Double minAlarm, Double lowAlarm, Double highAlarm, Double maxAlarm, Double noStatus, Integer sequence, Double value) {
        Double statusValue = noStatus;
        switch (sequence)
        {
            case 0 : statusValue = noStatus;  // redundandt
            break;
            case 1 : {
                if (value <= min)
                    statusValue = minAlarm;
            }
            break;
            case 2 : {
                if (value <= low)
                    statusValue = lowAlarm;
            }
            break;
            case 3 : {
                if (value <= low)
                    statusValue = lowAlarm;
                if (value <= min)
                    statusValue = minAlarm;
            }
            break;
            case 4 : {
                if (value >= high)
                    statusValue = highAlarm;
            }
            break;
            case 5 : {
                if (value >= high)
                    statusValue = highAlarm;
                if (value <= min)
                    statusValue = minAlarm;
            }
            break;
            case 6 : {
                if (value >= high)
                    statusValue = highAlarm;
                if (value <= low)
                    statusValue = lowAlarm;
            }
            break;
            case 7 : {
                if (value >= high)
                    statusValue = highAlarm;
                if (value <= low)
                    statusValue = lowAlarm;
                if (value <= min)
                    statusValue = minAlarm;
            }
            break;
            case 8 : {
                if (value >= max)
                    statusValue = maxAlarm;
            }
            break;
            case 9 : {
                if (value >= max)
                    statusValue = maxAlarm;
                if (value <= min)
                    statusValue = minAlarm;
            }
            break;
            case 10 : {
                if (value >= max)
                    statusValue = maxAlarm;
                if (value <= low)
                    statusValue = lowAlarm;
            }
            break;
            case 11 : {
                if (value >= max)
                    statusValue = maxAlarm;
                if (value <= low)
                    statusValue = lowAlarm;
                if (value <= min)
                    statusValue = minAlarm;
            }
            break;
            case 12 : {
                if (value >= high)
                    statusValue = highAlarm;
                if (value >= max)
                    statusValue = maxAlarm;
            }
            break;
            case 13 : {
                if (value >= high)
                    statusValue = highAlarm;
                if (value >= max)
                    statusValue = maxAlarm;
                if (value <= min)
                    statusValue = minAlarm;
            }
            break;
            case 14 : {
                if (value >= high)
                    statusValue = highAlarm;
                if (value >= max)
                    statusValue = maxAlarm;
                if (value <= low)
                    statusValue = lowAlarm;
            }
            break;
            case 15 : {
                if (value >= high)
                    statusValue = highAlarm;
                if (value >= max)
                    statusValue = maxAlarm;
                if (value <= low)
                    statusValue = lowAlarm;
                if (value <= min)
                    statusValue = minAlarm;
            }
            break;
        }
        return statusValue;
    }


    private static Integer CalculateSequenceNumber(Double max, Double high, Double low, Double min) {
        Integer sequence = -1;
        if ((max == null )&& (high == null) && (low == null) && (min == null))
            sequence = 0;
        else if ((max == null )&& (high == null) && (low == null) && (min != null))
            sequence = 1;
        else if ((max == null )&& (high == null) && (low != null) && (min == null))
            sequence = 2;
        else if ((max == null )&& (high == null) && (low != null) && (min != null))
            sequence = 3;
        else if ((max == null )&& (high != null) && (low == null) && (min == null))
            sequence = 4;
        else if ((max == null )&& (high != null) && (low == null) && (min != null))
            sequence = 5;
        else if ((max == null )&& (high != null) && (low != null) && (min == null))
            sequence = 6;
        else if ((max == null )&& (high != null) && (low != null) && (min != null))
            sequence = 7;
        else if ((max != null )&& (high == null) && (low == null) && (min == null))
            sequence = 8;
        else if ((max != null )&& (high == null) && (low == null) && (min != null))
            sequence = 9;
        else if ((max != null )&& (high == null) && (low != null) && (min == null))
            sequence = 10;
        else if ((max != null )&& (high == null) && (low != null) && (min != null))
            sequence = 11;
        else if ((max != null )&& (high != null) && (low == null) && (min == null))
            sequence = 12;
        else if ((max != null )&& (high != null) && (low == null) && (min != null))
            sequence = 13;
        else if ((max != null )&& (high != null) && (low != null) && (min == null))
            sequence = 14;
        else if ((max != null )&& (high != null) && (low != null) && (min != null))
            sequence = 15;
        return sequence;
    }


}
