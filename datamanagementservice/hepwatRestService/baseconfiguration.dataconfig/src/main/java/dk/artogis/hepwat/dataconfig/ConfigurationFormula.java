package dk.artogis.hepwat.dataconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.schema.RawDataSchema;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataio.ConfiguredDataIo;
import dk.artogis.hepwat.object.FeatureObject;
import dk.artogis.hepwat.object.KeyDescription;
import dk.artogis.hepwat.object.ObjectType;
import dk.artogis.hepwat.object.Field;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationFormula  implements Serializable
{
    public static String measurementValuePlaceholder = "{{sensor_output_value}}";
    public static String temporaryMeasurementValuePlaceholder = "measurementValue";
    public static String objectValueStartMark = "{{";
    public static String objectValueEndMark = "}}";

    private Integer id;
    private String formula;


    //region getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }


//endregion getters and setters

    public ConfigurationFormula(){}
    public ConfigurationFormula(Integer id, String formula)
    {
        this.formula = formula;
        this.id = id;
    }
    public ConfigurationFormula(ResultSet rs)
    {
        //TODO : fill out if nessecary
    }

    public static List<ConfigurationFormula> getConfigurationFormulas(Connection connection, Integer calculationType, Integer dataioid)
    {
        List<ConfigurationFormula> configurationFormulas = new ArrayList<ConfigurationFormula>();
        try {
            List<CalculationAndStore> calculationAndStores = CalculationAndStore.GetCalculationAndStoresByCalculationType(calculationType, connection);
            for (CalculationAndStore calculationAndStore : calculationAndStores)
            {
                if ((calculationAndStore.getFormula() != null ) && (!calculationAndStore.getFormula().isEmpty()) )
                {
                    if (((dataioid != null) && dataioid.equals(calculationAndStore.getDataIoId()) ) || dataioid == null) {
                        ConfigurationFormula configurationFormula = new ConfigurationFormula(calculationAndStore.getDataIoId(), calculationAndStore.getFormula());
                        Status status = calculate(connection, configurationFormula.id, configurationFormula.formula, "1"); //test with measurementvalue =1
                        if (status.Success) {
                            configurationFormula.formula = status.Message; // formula replaced with object values
                            configurationFormulas.add(configurationFormula);
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            //TODO: Logging
        }

        return  configurationFormulas;
    }

    public static Status calculate(Connection connection, Integer id, String formula , String measurementValue)
    {
        Status status = new Status();
        status.Success = false;
        try {
            // get list with fields
            List<String> fieldNames = getFieldNames(formula);

            if (fieldNames.size() > 0) {
                // new test formula " Math.sqrt({{sensor_output_value}}) + {{DanDas:bundkote}}"
                List<ObjectComponentDataIoRelation> relations = ObjectComponentDataIoRelation.GetDataIoRelations(id, connection, true);
                //find active object relation
                for (ObjectComponentDataIoRelation relation : relations) {
                    if ((relation.getObjectKeys() != null) && (relation.getEndTime() == null)) {
                        List<Field> fields = FeatureObject.getFields(connection, fieldNames, relation.getObjectType(), relation.getObjectKeys());
                        String result = null;
                        String formulaReplacedWithObjectValues = null;
                        try {
                            formulaReplacedWithObjectValues = replaceFields(formula, fieldNames, fields);
                            result = calculate(formulaReplacedWithObjectValues, measurementValue);
                        }
                        catch (Exception ex)
                        {
                            status.Message = "could not replace values or calculate formula : " +ex.getMessage();
                        }
                        finally {

                            if ((result != null) && (!result.isEmpty())) {
                                status.Success = true;
                                status.Message = formulaReplacedWithObjectValues;
                                status.JsonObject = "{ \"result\": " + result + " }";
                            } else {
                                status.Success = false;
                                status.JsonObject = "{ \"result\": \"null\" }";
                            }
                        }
                    }
                }
            }
            else { // formula simple with out
                String result= null;
                try {
                    result = calculate(formula, measurementValue);
                }
                catch (Exception ex)
                {
                    status.Message = "Formula could not be used for calculation : " + ex.getMessage();
                }
                finally {


                    if ((result != null) && (!result.isEmpty())) {
                        status.Success = true;
                        status.Message = formula;
                        status.JsonObject = "{ \"result\": " + result + " }";
                    } else {
                        status.Success = false;
                        status.JsonObject = "{ \"result\": \"null\" }";
                    }
                }
            }
        }
        catch(Exception ex)
        {
                status.Error = ex.getMessage();
        }

        return status;
    }



    public static List<String>  getFieldNames( String formula)
    {
        Status status = new Status();
        status.Success = false;
        List<String> objectFields = new ArrayList<String>();

        // new test formula " Math.sqrt({{sensor_output_value}}) + {{DanDas:bundkote}}"

        String tempFormula =  formula;
        while (tempFormula.contains(measurementValuePlaceholder))
            tempFormula = tempFormula.replace(measurementValuePlaceholder, temporaryMeasurementValuePlaceholder );

        Integer startIndex = 0;
        while (tempFormula.indexOf(objectValueStartMark, startIndex) > 0)
        {
            Integer objectFieldStartIndex = tempFormula.indexOf(objectValueStartMark,startIndex);
            Integer objectFieldEndIndex = tempFormula.indexOf(objectValueEndMark, startIndex);
            String objectfield = tempFormula.substring(objectFieldStartIndex+objectValueStartMark.length(), objectFieldEndIndex);

            objectFields.add(objectfield);

            startIndex = objectFieldEndIndex+objectValueEndMark.length();
        }

        return  objectFields;
    }

    private static String calculate(String formulatWithFieldValues,  String measurementValue)
    {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");

        String scriptFormula = formulatWithFieldValues.replace(measurementValuePlaceholder, measurementValue);
        double valueScript = 0;
        String valueScriptString = null;
        Object valueScriptObject = null;
        try {
            //sumValueScript = ((double)nashorn.eval(scriptFormula));
            valueScriptObject = nashorn.eval(scriptFormula);
            if (valueScriptObject != null)
            {
                if (valueScriptObject.getClass() == Integer.class)
                    valueScriptString = Integer.toString((Integer) valueScriptObject);
                else if (valueScriptObject.getClass() == Double.class)
                    valueScriptString = Double.toString((Double) valueScriptObject);
                else if (valueScriptObject.getClass() == Float.class)
                    valueScriptString = Float.toString((Float) valueScriptObject);
            }
        } catch (Exception ex) {
            System.out.println("error sm");
        }
        return valueScriptString;
    }

    private static String replaceFields(String formula, List<String> fieldNames, List<Field> fields)
    {
        String replacedFormula =  formula;

//        while (replacedFormula.contains(measurementValuePlaceholder))
//            replacedFormula = replacedFormula.replace(measurementValuePlaceholder, temporaryMeasurementValuePlaceholder);
//

        for (Field field : fields)
        {
            while (replacedFormula.contains(field.name))
            {
                replacedFormula = replacedFormula.replace(objectValueStartMark + field.name + objectValueEndMark, field.value);
            }
        }

//        while (replacedFormula.contains(temporaryMeasurementValuePlaceholder)) {
//            replacedFormula = replacedFormula.replace(temporaryMeasurementValuePlaceholder, measurementValuePlaceholder);
//        }
        return replacedFormula;
    }

}
