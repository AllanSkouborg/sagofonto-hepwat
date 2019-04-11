package dk.artogis.hepwat.calculationService.streamingprocess;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.artogis.hepwat.calculationService.configuration.DeviceConfigurationFormulas;

import dk.artogis.hepwat.common.schema.AggregatedDataSchema;
import dk.artogis.hepwat.common.schema.RawDataSchema;
import dk.artogis.hepwat.dataconfig.ConfigurationFormula;
import org.apache.log4j.Logger;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Properties;

import java.util.concurrent.locks.ReentrantLock;

public class Calculate {


    public static JsonNode newValue(JsonNode measurement, Integer calcType, DeviceConfigurationFormulas deviceConfigurationFormulas, ScriptEngine nashorn, ReentrantLock lock, Logger logger)
    {
        if(logger.isInfoEnabled()){
            logger.info("received value: " + measurement.toString());
        }

        ObjectNode newValue = JsonNodeFactory.instance.objectNode();
        String measurementValuePlaceholder =  ConfigurationFormula.measurementValuePlaceholder; //"{{sensor_output_value}}"
        Integer hepwatDeviceId = 0;
        try{
            hepwatDeviceId = measurement.get(RawDataSchema.f_deviceId).asInt();
            Integer actCalcType = 0;
            if (measurement.get(RawDataSchema.f_calcType) != null)
                actCalcType = measurement.get(AggregatedDataSchema.f_calcType).asInt();
            String scriptFormula = "{{sensor_output_value}} * 1"; //Default
            lock.lock();
            try {
                scriptFormula = deviceConfigurationFormulas.GetFormula(hepwatDeviceId);
            }
            catch (Exception ex)
            {
                logger.error("could not get deviceFromula");
                throw  new Exception("could not get deviceFromula");
            }
            finally {
                lock.unlock();
            }
            Double value = measurement.get(RawDataSchema.f_value).asDouble();
            if(logger.isInfoEnabled()){
                logger.info("Formula: " + scriptFormula.toString());
            }
            while (scriptFormula.contains(measurementValuePlaceholder))
                scriptFormula = scriptFormula.replace(measurementValuePlaceholder, value.toString());

            newValue.put(RawDataSchema.f_deviceId, hepwatDeviceId);
            newValue.put(RawDataSchema.f_aggType, 0);
            newValue.put(RawDataSchema.f_calcType, calcType);

            newValue.put(RawDataSchema.f_timeStamp, measurement.get(RawDataSchema.f_timeStamp).asLong());
            double sumValueScript = 0;
            try {
                sumValueScript = (Double) nashorn.eval(scriptFormula);
                newValue.put(RawDataSchema.f_value, sumValueScript);
            } catch (Exception ex) {
                System.out.println("error sum");
            }

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
            logger.error("something went wrong in the calculation newAverage");
            return null;
        }
        return newValue;


    }



}
