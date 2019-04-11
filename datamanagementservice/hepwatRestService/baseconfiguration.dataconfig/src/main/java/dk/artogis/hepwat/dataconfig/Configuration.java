package dk.artogis.hepwat.dataconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.artogis.hepwat.calculation.CalculationAndStore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.utility.Status;
import dk.artogis.hepwat.dataio.ConfiguredDataIo;
import dk.artogis.hepwat.dataio.UnConfiguredDataIo;
import dk.artogis.hepwat.relation.ObjectComponentDataIoRelation;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Configuration extends ConfiguredDataIo
{

    private CalculationAndStore[] calculationAndStores;
    private ObjectComponentDataIoRelation relation;

    //region getters and setters

    public CalculationAndStore[] getCalculationAndStores() {
        return calculationAndStores;
    }

    public void setCalculationAndStores(CalculationAndStore[] calculationAndStores) {
        this.calculationAndStores = calculationAndStores;
    }

    public ObjectComponentDataIoRelation getRelation() {
        return relation;
    }

    public void setRelation(ObjectComponentDataIoRelation relation) {
        this.relation = relation;
    }

//endregion getters and setters

    public Configuration(){}
    public Configuration(ConfiguredDataIo configuredDataIo)
    {
        //this.setAlias(configuredDataIo.getAlias());
        //this.setName(configuredDataIo.getName());

        this.setSensorObjectAlias(configuredDataIo.getSensorObjectAlias());
        this.setSensorObjectName(configuredDataIo.getSensorObjectName());

        this.setId(configuredDataIo.getId());
        this.setMeasurementType(configuredDataIo.getMeasurementType());
        this.setTemplateType(configuredDataIo.getTemplateType());
        this.setUnit(configuredDataIo.getUnit());
        this.setDescription(configuredDataIo.getDescription());
        this.setSensorObjectId(configuredDataIo.getSensorObjectId());
        this.setSensorObjectNodeId(configuredDataIo.getSensorObjectNodeId());
        this.setSensorObjectDescription(configuredDataIo.getSensorObjectDescription());
        this.setDataSourceId(configuredDataIo.getDataSourceId());
        this.setDataSourceName(configuredDataIo.getDataSourceName());
        this.setSensorObjectNodeName(configuredDataIo.getSensorObjectNodeName());
        this.setIsBatteryStatus(configuredDataIo.getIsBatteryStatus());
    }
    public Configuration(ResultSet rs)
    {
        //TODO : fill out if nessecary
    }
    public Status Insert(Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        try {

            if (calculationAndStores != null) {
                for (CalculationAndStore cs : calculationAndStores) {
                    //
                    cs.setDataIoId(this.getId());
                    cs.setTemplateType(this.getTemplateType());
                    //
                    status = cs.Insert(connection);
                    if (status.Success == false)
                        break;
                }
                if (status.Success  == true) {
                    status = super.Insert(connection);
                    if ((status.Success  == true) && (this.relation != null)) {
                        relation.setDataIoType(this.getMeasurementType());
                        relation.setDataIoKey(this.getId());
                        if (relation.getComponentKey() != null)
                            relation.setRelationType(ObjectComponentDataIoRelation.TypeComponentDataIo); //TODO: set relation type to "RelationType
                        else
                            relation.setRelationType(ObjectComponentDataIoRelation.TypeObjectDataIo);
                        status = this.relation.Insert(connection);
                        if (status.Success)
                        {
                            UnConfiguredDataIo.UpdateConfigured(this.getId(), true, connection);
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

    public static Status  Delete(Integer id, Connection connection)
    {
        Status status = new Status();
        status.Success = false;

        status = CalculationAndStore.Delete(id, -1, -1, connection);
        if (status.Success)
        {
            status = ConfiguredDataIo.Delete(id, connection);
            if (status.Success) {
                status = ObjectComponentDataIoRelation.DeleteDataIoRelation(id, connection);
                if (status.Success)
                    UnConfiguredDataIo.UpdateConfigured(id, false, connection);
            }
        }

        return  status;
    }

    public Status Update(Connection connection)
    {
        Status status = new Status();
        status.Success = false;

        try {

            if (calculationAndStores != null) {
                for (CalculationAndStore cs : calculationAndStores) {
                    cs.setDataIoId(this.getId());
                    cs.setTemplateType(this.getTemplateType());

                    status = cs.Update(connection);
                    if (status.Success == false)
                        break;
                }
                if (status.Success  == true) {
                    status = super.Update(connection);
                    if ((status.Success  == true) && (this.relation != null)) {
                        relation.setDataIoType(this.getMeasurementType());
                        relation.setDataIoKey(this.getId());
                        if (relation.getComponentKey() != null)
                            relation.setRelationType(ObjectComponentDataIoRelation.TypeComponentDataIo); //TODO: set relation type to "RelationType
                        else
                            relation.setRelationType(ObjectComponentDataIoRelation.TypeObjectDataIo);

                        status = this.relation.Update(connection);
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


    public static Configuration GetConfiguration(Integer id, Connection connection ) throws Exception
    {

        ConfiguredDataIo configuredDataIo = ConfiguredDataIo.GetConfiguredDataIo(id, connection);
        Configuration configuration = new Configuration(configuredDataIo);
        List<CalculationAndStore> calculationAndStoresList =  CalculationAndStore.GetCalculationAndStores(id, connection, true, true);

        if (calculationAndStoresList != null ) {
            configuration.calculationAndStores = new CalculationAndStore[calculationAndStoresList.size()];
            configuration.calculationAndStores = (CalculationAndStore[]) calculationAndStoresList.toArray(configuration.calculationAndStores);
        }
        ObjectComponentDataIoRelation objectComponentDataIoRelation = ObjectComponentDataIoRelation.GetObjectComponentDataIoRelation(id, connection);
        configuration.relation = objectComponentDataIoRelation;
        return  configuration;
    }

    // Get all
    public static List<Configuration> GetConfigurations(Connection connection,Boolean includeRelations, Boolean addTypeInfo ) throws Exception
    {
        //System.out.println("GetConfigurations - A");

        List<Configuration> configurations = new ArrayList<Configuration>();
        List<ConfiguredDataIo> configuredDataIos = ConfiguredDataIo.GetConfiguredDataIos( connection);

        for (ConfiguredDataIo configuredDataIo: configuredDataIos)
        {
            List<CalculationAndStore> calculationAndStoresList =  CalculationAndStore.GetCalculationAndStores(configuredDataIo.getId(), connection, addTypeInfo, true);
            Configuration configuration = new Configuration(configuredDataIo);
            if (calculationAndStoresList != null ) {
                configuration.calculationAndStores = new CalculationAndStore[calculationAndStoresList.size()];
                configuration.calculationAndStores = (CalculationAndStore[]) calculationAndStoresList.toArray(configuration.calculationAndStores);
            }
            if (includeRelations) {
                ObjectComponentDataIoRelation objectComponentDataIoRelation = ObjectComponentDataIoRelation.GetObjectComponentDataIoRelation(configuredDataIo.getId(), connection);
                configuration.relation = objectComponentDataIoRelation;
                configurations.add(configuration);
            }
            configurations.add(configuration);
        }

        return  configurations;
    }

    // Get array of ID's
    public static List<Configuration> GetConfigurationsFromArray(String idArrayString, Connection connection,Boolean includeRelations, Boolean addTypeInfo ) throws Exception
    {
        //System.out.println("GetConfigurations - with array");

        List<Configuration> configurations = new ArrayList<Configuration>();
        List<ConfiguredDataIo> configuredDataIos = ConfiguredDataIo.GetConfiguredDataIosFromArray(idArrayString, connection);

        for (ConfiguredDataIo configuredDataIo: configuredDataIos)
        {
            List<CalculationAndStore> calculationAndStoresList =  CalculationAndStore.GetCalculationAndStores(configuredDataIo.getId(), connection, addTypeInfo, true);
            Configuration configuration = new Configuration(configuredDataIo);
            if (calculationAndStoresList != null ) {
                configuration.calculationAndStores = new CalculationAndStore[calculationAndStoresList.size()];
                configuration.calculationAndStores = (CalculationAndStore[]) calculationAndStoresList.toArray(configuration.calculationAndStores);
            }
            if (includeRelations) {
                ObjectComponentDataIoRelation objectComponentDataIoRelation = ObjectComponentDataIoRelation.GetObjectComponentDataIoRelation(configuredDataIo.getId(), connection);
                configuration.relation = objectComponentDataIoRelation;
                configurations.add(configuration);
            }
            configurations.add(configuration);
        }

        return  configurations;
    }

    public static List<Configuration> GetConfigurations(Connection connection,Boolean includeRelations, Boolean addTypeInfo, Integer calculationType ) throws Exception
    {
        //System.out.println("GetConfigurations - B");

        List<Configuration> configurations = new ArrayList<Configuration>();
        List<ConfiguredDataIo> configuredDataIos = ConfiguredDataIo.GetConfiguredDataIos( connection);

        for (ConfiguredDataIo configuredDataIo: configuredDataIos)
        {
            Boolean hasCalculation = false;
            List<CalculationAndStore> calculationAndStoresList =  CalculationAndStore.GetCalculationAndStores(configuredDataIo.getId(), connection, addTypeInfo, false);
            Configuration configuration = new Configuration(configuredDataIo);
            if (calculationAndStoresList != null ) {
                CalculationAndStore[] calculationAndStoresForActCalcType = new CalculationAndStore[1];
                for (CalculationAndStore calculationAndStore: calculationAndStoresList)
                {
                    if (calculationAndStore.getCalculation() == calculationType)
                    {
                        if (calculationAndStore.getFormula() != null) {
                            hasCalculation = true;
                            calculationAndStoresForActCalcType[0] = calculationAndStore;
                            configuration.calculationAndStores = calculationAndStoresForActCalcType;
                            break;
                        }
                    }
                }
            }
            if ((hasCalculation) && (includeRelations)) {
                ObjectComponentDataIoRelation objectComponentDataIoRelation = ObjectComponentDataIoRelation.GetObjectComponentDataIoRelation(configuredDataIo.getId(), connection);
                configuration.relation = objectComponentDataIoRelation;
                configurations.add(configuration);
            }
            if (hasCalculation)
                configurations.add(configuration);
        }

        return  configurations;
    }



}
