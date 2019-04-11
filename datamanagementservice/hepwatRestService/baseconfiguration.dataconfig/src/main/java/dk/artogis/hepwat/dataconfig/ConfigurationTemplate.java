package dk.artogis.hepwat.dataconfig;

import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataio.ConfiguredDataIo;
import dk.artogis.hepwat.measurement.MeasurementTemplate;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConfigurationTemplate extends MeasurementTemplate
{

    private CalculationAndStore[] calculationAndStores;

    //region getters and setters

    public CalculationAndStore[] getCalculationAndStores() {
        return calculationAndStores;
    }

    public void setCalculationAndStores(CalculationAndStore[] calculationAndStores) {
        this.calculationAndStores = calculationAndStores;
    }


//endregion getters and setters

    public ConfigurationTemplate(){}
    public ConfigurationTemplate(MeasurementTemplate measurementTemplate)
    {
        this.setId(measurementTemplate.getId());
        this.setTemplateType(measurementTemplate.getTemplateType());
        this.setMeasurementAlias(measurementTemplate.getMeasurementAlias());
        this.setMeasurementName(measurementTemplate.getMeasurementName());
        this.setMeasurementType(measurementTemplate.getMeasurementType());
        this.setTemplateType(measurementTemplate.getTemplateType());
 ;
    }
    public ConfigurationTemplate(ResultSet rs)
    {
        //TODO : fill out if nessecary
    }
    public Status Insert(Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        try {
            status = super.Insert(connection);
            if (status.Success  == true) {
                MeasurementTemplate insertedMeasurementTemplate = MeasurementTemplate.GetMeasurementTemplate(super.getId(), connection);
                if (insertedMeasurementTemplate != null) {
                    if (calculationAndStores != null) {
                        for (CalculationAndStore cs : calculationAndStores) {
                            cs.setTemplateType(insertedMeasurementTemplate.getTemplateType());
                            cs.setDataIoId(-1);
                            status = cs.Insert(connection);
                            if (status.Success == false)
                                break;
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            status.Error = ex.getMessage();
        }
        return status;
    }

    public static Status  DeleteFromTemplateId( Integer templateType, Connection connection)
    {
        Status status = new Status();
        status.Success = false;

        status = CalculationAndStore.Delete(-1, templateType, -1, connection);
        if (status.Success)
        {
            MeasurementTemplate.DeleteTemplateType(templateType, connection);
        }

        return  status;
    }
    public static Status Delete(UUID id, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        try {
            MeasurementTemplate measurementTemplateToDelete = MeasurementTemplate.GetMeasurementTemplate(id, connection);
            if (measurementTemplateToDelete != null) {

                status = CalculationAndStore.Delete(-1, measurementTemplateToDelete.getTemplateType(), -1, connection);
                if (status.Success) {
                    MeasurementTemplate.Delete(id, connection);
                }
            }
        }
        catch (Exception ex)
        {
            status.Message = "error in deleting measurementtemplate : " + ex.getMessage();
        }
        return  status;
    }
    public Status Update(Connection connection)
    {
        Status status = new Status();
        status.Success = false;

        try {
            MeasurementTemplate measurementTemplateToUpdate = MeasurementTemplate.GetMeasurementTemplate(super.getId(), connection);
            if (measurementTemplateToUpdate != null) {
                if (calculationAndStores != null) {
                    for (CalculationAndStore cs : calculationAndStores) {
                        cs.setTemplateType(measurementTemplateToUpdate.getTemplateType());
                        cs.setDataIoId(-1);
                        status = cs.Update(connection);
                        if (status.Success == false)
                            break;
                    }
                    if (status.Success == true) {
                        status = super.Update(connection);

                    }
                }
            }
        }
        catch (Exception ex)
        {
            status.Error = ex.getMessage();
        }

        return status;
    }


    public static ConfigurationTemplate GetConfigurationTemplate(Integer id, Connection connection ) throws Exception
    {

        MeasurementTemplate measurementTemplate = MeasurementTemplate.GetMeasurementTemplateById(id, connection);
        ConfigurationTemplate configuration = new ConfigurationTemplate(measurementTemplate);
        List<CalculationAndStore> calculationAndStoresList =  CalculationAndStore.GetCalculationAndStoresByTemplateId(id, connection);

        if (calculationAndStoresList != null ) {
            configuration.calculationAndStores = new CalculationAndStore[calculationAndStoresList.size()];
            configuration.calculationAndStores = (CalculationAndStore[]) calculationAndStoresList.toArray(configuration.calculationAndStores);
        }

        return  configuration;
    }


    public static List<ConfigurationTemplate> GetConfigurationTemplates(Connection connection ) throws Exception
    {

        List<ConfigurationTemplate> configurationTemplates = new ArrayList<ConfigurationTemplate>();
        List<MeasurementTemplate> measurementTemplates = MeasurementTemplate.GetMeasurementTemplates( connection);

        for (MeasurementTemplate measurementTemplate: measurementTemplates)
        {
            List<CalculationAndStore> calculationAndStoresList =  CalculationAndStore.GetCalculationAndStoresByTemplateId(measurementTemplate.getTemplateType(), connection);
            ConfigurationTemplate configurationTemplate = new ConfigurationTemplate(measurementTemplate);
            if (calculationAndStoresList != null ) {
                configurationTemplate.calculationAndStores = new CalculationAndStore[calculationAndStoresList.size()];
                configurationTemplate.calculationAndStores = (CalculationAndStore[]) calculationAndStoresList.toArray(configurationTemplate.calculationAndStores);
            }

            configurationTemplates.add(configurationTemplate);
        }

        return  configurationTemplates;
    }




}
