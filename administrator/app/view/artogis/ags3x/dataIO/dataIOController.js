Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dataIO.dataIOController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xDataIOConfig',

    requires: [
       'AGS3xIoTAdmin.util.events',
       'AGS3xIoTAdmin.util.util',
       'AGS3xIoTAdmin.store.artogis.ags3x.toc.layerTree',
       'AGS3xIoTAdmin.view.artogis.ags3x.mapSearch.mapSearchController',
       'AGS3xIoTAdmin.view.artogis.ags3x.map.mapController',
       'AGS3xIoTAdmin.view.artogis.ags3x.toc.tocController'
    ],

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelDataHeaderTitle: 'Data',
    fieldLabelUnconfiguredTitle: 'Ikke-konfigureret data',
    fieldLabelButtonRefreshData: 'Genopfrisk data',
    fieldLabelUnconfiguredSource: 'DataRobot',
    fieldLabelUnconfiguredSensorId: 'Måler-ID',
    fieldLabelUnconfiguredName: 'Målernavn',
    fieldLabelUnconfiguredSensorName: 'Navn',
    fieldLabelUnconfiguredNodeType: 'Sensortype',
    fieldLabelUnconfiguredDescription: 'Beskrivelse',
    fieldLabelUnconfiguredSensorObjectDescription: 'Sensorobjekt beskrivelse',
    fieldLabelUnconfiguredId: 'Sensor-ID',
    fieldLabelUnconfiguredUnit: 'Enhed',

    fieldLabelCalculationsCalculation: 'Beregning',
    fieldLabelCalculationsFormula: 'Formular',
    fieldLabelCalculationsType: 'Type',
    fieldLabelCalculationsData: 'Data',
    fieldLabelCalculationsUnit: 'Enhed',
    fieldLabelCalculationsStore: 'Gem',
    fieldLabelCalculationsAverageTwoMinutes: 'Gennemsnit 2 min.',
    fieldLabelCalculationsAverageSixtyMinutes: 'Gennemsnit 60 min.',
    fieldLabelCalculationsSumSixtyMinutes: 'Sum 60 min.',
    fieldLabelScaleToUnit: 'Enhedsskalering',
    fieldLabelCalculationsOpenModel: 'Åbn beregningsmodel',
    fieldLabelCalculationsOpenFormulaEditor: 'Åbn formulareditor',
    fieldLabelCalculationsAggregationAndStore: 'Aggregering og lagring',
    fieldLabelCalculationsWindowTitle: 'Beregninger',

    fieldLabelConfiguredTitle: 'Konfigureret data',
    fieldLabelConfiguredDataId: 'Data ID',
    fieldLabelConfiguredSensorId: 'Sensor ID',
    fieldLabelConfiguredName: 'Navn',
    fieldLabelConfiguredDescription: 'Beskrivelse',
    fieldLabelConfiguredAlias: 'Alias',
    fieldLabelConfiguredMeasurementName: 'Målingsbeskrivelse',

    fieldLabelObjectCompType: 'Type',
    fieldLabelObjectCompName: 'Navn',
    fieldLabelObjectCompDescription: 'Beskrivelse',
    fieldLabelObjectCompId: 'ID',

    errorTextConfiguredDataNotSaved: 'Konfigureret data kunne ikke gemmes.',
    
    textMessageConfiguredDataSaved: 'Konfigureret data er blevet gemt.',

    fieldLabelCalculation: 'Udregning',
    fieldLabelAggregate: 'Aggregering',
    fieldLabelUnit: 'Enhed',
    fieldLabelInterval: 'Interval',
    fieldLabelButtonCalculationAccept: 'Acceptér',
    fieldLabelButtonCalculationCancel: 'Fortryd',

    fieldLabelResetDataIOQuestion: 'Vil du nulstille den valgte sensor data?',
    fieldLabelResetDataIOYes: 'Ja',
    fieldLabelResetDataIONo: 'Nej',

    fieldLabelDataLoading: 'Henter data... vent venligst',

    baseUrl: '',
    map: null,
    mapSearchTool: null,
    selectedObject: null,
    calculationsInfoWindow: null,

    activeFilters: null,

    openResultListPanel: function () {
        this.getViewModel().set('activeTab', 1);
    },

    // load selected component or object location for display in editor
    getObjectKeys: function (data) {
        var self = this;

        console.log('dataIOController.getObjectKeys - data: ', data);
        console.log('dataIOController.getObjectKeys - data.featureDescription: ' + data.featureDescription);

        data.description = (data.featureDescription != null) ? data.featureDescription : 'N/A';

        var storeData = [];
        storeData.push(data);

        console.log('dataIOController.getObjectKeys - storeData: ', storeData);

        var componentOrObjectStore = this.getViewModel().getStore('componentOrObjectStore');
        componentOrObjectStore.setData(storeData);

        Ext.ComponentQuery.query('#maskObjectsAndComponents')[0].hide();

        self.toggleSaveButton();

    },

    toggleSaveButton: function () {
        var self = this;
        console.log('dataIOController.toggleSaveButton - Toggling save button...');

        var configuredDataStore = self.getViewModel().getStore('configuredDataStore');
        var calculationDatastore = self.getViewModel().getStore('calculationDataStore');
        var componentOrObjectStore = self.getViewModel().getStore('componentOrObjectStore');

        console.log('dataIOController.toggleSaveButton - configuredDataStore data: ', configuredDataStore.data.items);
        console.log('dataIOController.toggleSaveButton - calculationDatastore data: ', calculationDatastore.data.items);
        console.log('dataIOController.toggleSaveButton - componentOrObjectStore data: ', componentOrObjectStore.data.items);

        if (calculationDatastore.data.items.length >= 4 && componentOrObjectStore.data.items.length == 1) {
            Ext.ComponentQuery.query('#dataIOSaveButton')[0].setDisabled(false);
            console.log('dataIOController.toggleSaveButton - enable save button');
        }
        else {
            Ext.ComponentQuery.query('#dataIOSaveButton')[0].setDisabled(true);
            console.log('dataIOController.toggleSaveButton - disable save button');
        }
    },
    performEditorSave: function () {
        var self = this;

        var configuredDataStore = self.getViewModel().getStore('configuredDataStore');
        console.log('dataIOController.performEditorSave - configuredDataStore: ', configuredDataStore);
        var configuredItems = configuredDataStore.data.items;

        if (configuredItems.length === 0) {
            return;
        }

        var configuredItem = configuredItems[0];
        console.log('dataIOController.performEditorSave - configuredItem: ', configuredItem);

        self.saveConfigurationData(configuredItem);

    },
    saveConfigurationData: function (configuredItem) {
        var self = this;

        console.log('dataIOController.saveConfigurationData - data to save: ', configuredItem);

        
        var param = null;

        var configuredDataStore = self.getViewModel().getStore('configuredDataStore');
        var configuredDataEntity = configuredDataStore.data.items[0].data;

        var calculationDataStore = self.getViewModel().getStore('calculationDataStore');
        var componentOrObjectStore = self.getViewModel().getStore('componentOrObjectStore');
        var measurementTemplateType = self.getViewModel().getStore('measurementTemplateType');
        
        console.log('dataIOController.saveConfigurationData - configuredDataStore: ', configuredDataStore);

        var configuredValue = configuredItem.data.configured;
        console.log('dataIOController.saveConfigurationData - nodeType: ', configuredItem.data.nodeType);
        console.log('dataIOController.saveConfigurationData - configuredValue: ', configuredValue);

        console.log('dataIOController.saveConfigurationData - calculationDataStore: ', calculationDataStore);
        var calculationItems = calculationDataStore.data.items;

        console.log('dataIOController.saveConfigurationData - componentOrObjectStore: ', componentOrObjectStore);
        var selectedObject = componentOrObjectStore.data.items[0].data;
        console.log('dataIOController.saveConfigurationData - selectedObject: ', selectedObject);
        
        if (calculationItems.length > 0) {
            var calculationAndStores = [];

            var objectComponentDataIoRelation;

            if (!configuredDataEntity.relation) {
                console.log('dataIOController.saveConfigurationData - selectedObject - A');

                objectComponentDataIoRelation = {
                    "objectKeys": selectedObject.objectKeys,
                    "objectType": selectedObject.objectType,
                    "componentType": selectedObject.componentType,
                    "componentKey": selectedObject.componentKey,
                    "relationType": selectedObject.relationType,
                    "dataIoType": selectedObject.dataIoType,
                    "dataIoKey": selectedObject.dataIoKey
                }
            }
            else {
                console.log('dataIOController.saveConfigurationData - selectedObject - B');
                delete configuredDataEntity.relation.createTimeString;
                delete configuredDataEntity.relation.endTimeString;

                objectComponentDataIoRelation = configuredDataEntity.relation;

            }

            console.log('dataIOController.saveConfigurationData - objectComponentDataIoRelation: ', objectComponentDataIoRelation);

            console.log('dataIOController.saveConfigurationData - calculationItems: ', calculationItems);

            for (var c = 0; c < calculationItems.length; c++) {
                var calculationItem = calculationItems[c];
                calculationAndStores.push({
                    "calculation": calculationItem.data.calculation,
                    "formula": calculationItem.data.formula,
                    "unit": calculationItem.data.unit,
                    "store": calculationItem.data.store,
                    "calculationType": calculationItem.data.calculationType,
                    "aggregationAndStores": calculationItem.data.aggregationAndStores
                });
            }

            var sensorObjectUrl = self.baseUrl + 'sensorobject/' + configuredItem.data.dataSourceId + '?sensorobjectid=' + configuredItem.data.sensorObjectId;
            console.log('dataIOController.saveConfigurationData - sensorObjectUrl: ', sensorObjectUrl);

            Ext.Ajax.request({
                url: sensorObjectUrl,
                method: 'GET',
                scope: this,
                callback: function (options, success, response) {
                    if (success) {

                        try {

                            var sensorObjectResult = Ext.decode(response.responseText);
                            console.log('dataIOController.saveConfigurationData - get sensor object success, sensorObjectResult: ', sensorObjectResult);

                            param = {
                                "sensorObjectName": configuredItem.data.sensorObjectName,
                                "sensorObjectAlias": (sensorObjectResult.sensorObject.nameAlias != null) ? sensorObjectResult.sensorObject.nameAlias : configuredItem.data.sensorObjectName,

                                "dataSourceId": configuredItem.data.dataSourceId,
                                "sensorObjectDescription": configuredItem.data.sensorObjectDescription,
                                "unit": configuredItem.data.unit,

                                "templateType": configuredItem.data.templateType,
                                "measurementType": AGS3xIoTAdmin.util.util.getMeasurementTypeFromTemplate(configuredItem.data.templateType),
                                "id": configuredItem.data.dataId,
                                "description": configuredItem.data.description,
                                "calculationAndStores": calculationAndStores,
                                "dataSourceName": configuredItem.data.dataSourceName,
                                "sensorObjectId": configuredItem.data.sensorObjectId,
                                "sensorObjectNodeId": configuredItem.data.sensorObjectNodeId,
                                "relation": objectComponentDataIoRelation,
                                "isBatteryStatus": null,

                                "sensorObjectNodeName": configuredItem.data.sensorObjectNodeName,
                            }

                            console.log('dataIOController.saveConfigurationData - final params: ', param);

                            var url = self.baseUrl + 'dataconfiguration';

                            // create new item...
                            if (configuredValue == false || !configuredValue) {
                                console.log('dataIOController.saveConfigurationData - creating new item');

                                // Add loading mask
                                var loadingMask = new Ext.LoadMask(
                                    {
                                        msg: self.fieldLabelDataLoading,
                                        id: 'saveConfigurationDataMask',
                                        target: Ext.ComponentQuery.query('#ags3xDataIOConfig')[0]
                                    }
                                );
                                loadingMask.show();

                                Ext.Ajax.request({
                                    url: url,
                                    method: 'POST',
                                    jsonData: Ext.JSON.encode(param),
                                    scope: this,
                                    callback: function (options, success, response) {
                                        if (success) {
                                            console.log('dataIOController.saveConfigurationData - successful save!');
                                            console.log('dataIOController.saveConfigurationData - response: ', response);
                                            this.getViewModel().set('connectionObjectValue', '');
                                            this.getViewModel().set('saveButtonDisabled', true);
                                            this.selectedObject = null;

                                            AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.textMessageConfiguredDataSaved, 'INFO');

                                            self.resetConfigurationEditor();

                                            self.refreshUnconfiguredData();

                                            Ext.ComponentQuery.query('#saveConfigurationDataMask')[0].destroy();
                                        }
                                        else {
                                            AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.errorTextConfiguredDataNotSaved, 'ERROR');
                                            console.log('dataIOController.saveConfigurationData - response: ', response);
                                            Ext.ComponentQuery.query('#saveConfigurationDataMask')[0].destroy();
                                        }
                                    }
                                }, this);
                            }

                                // ... or update existing item
                            else if (configuredValue == true) {

                                console.log('dataIOController.saveConfigurationData - updating existing item');

                                // Add loading mask
                                var loadingMask = new Ext.LoadMask(
                                    {
                                        msg: self.fieldLabelDataLoading,
                                        id: 'saveConfigurationDataMask',
                                        target: Ext.ComponentQuery.query('#ags3xDataIOConfig')[0]
                                    }
                                );
                                loadingMask.show();

                                Ext.Ajax.request({
                                    url: url,
                                    method: 'PUT',
                                    jsonData: Ext.JSON.encode(param),
                                    scope: this,
                                    callback: function (options, success, response) {
                                        if (success) {
                                            console.log('dataIOController.saveConfigurationData - successful save!');
                                            console.log('dataIOController.saveConfigurationData - response: ', response);
                                            this.getViewModel().set('connectionObjectValue', '');
                                            this.getViewModel().set('saveButtonDisabled', true);
                                            this.selectedObject = null;
                                            AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.textMessageConfiguredDataSaved, 'INFO');

                                            self.resetConfigurationEditor();

                                            self.refreshUnconfiguredData();

                                            Ext.ComponentQuery.query('#saveConfigurationDataMask')[0].destroy();
                                        }
                                        else {
                                            AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.errorTextConfiguredDataNotSaved, 'ERROR');
                                            Ext.ComponentQuery.query('#saveConfigurationDataMask')[0].destroy();
                                        }
                                    }
                                }, this);
                            }
                            else {
                                console.log('dataIOController.saveConfigurationData - "configuredValue" - no value');
                                Ext.ComponentQuery.query('#saveConfigurationDataMask')[0].destroy();
                            }
                        }
                        catch (exception) {
                            console.log('dataIOController.saveConfigurationData - get sensor object success, EXCEPTION: ', exception);
                        }

                    } // get sensor object success
                    else {
                        console.log('dataIOController.saveConfigurationData - get sensor object success, FAILURE: ', response);
                    }
                }
            });
        }

    },
    resetConfigurationEditor: function () {
        var self = this;

        var selection = Ext.ComponentQuery.query('#dataGridUnconfigured')[0].getSelection();
        if (Ext.ComponentQuery.query('#comboxTemplateOptions').length > 0) {
            if (selection.length > 0) {
                Ext.ComponentQuery.query('#comboxTemplateOptions')[0].setDisabled(false);
            }
            else {
                Ext.ComponentQuery.query('#comboxTemplateOptions')[0].setDisabled(true);
            }
        }

        console.log('dataIOController.resetConfigurationEditor - selection: ', selection);

        var calculationDataStore = self.getViewModel().getStore('calculationDataStore');
        calculationDataStore.setData([]);

        Ext.ComponentQuery.query('#comboxTemplateOptions')[0].setValue(null);
        Ext.ComponentQuery.query('#maskCalculations')[0].show();

        // reset panelConfiguredData
        var configuredDataStore = self.getViewModel().getStore('configuredDataStore');

        if (configuredDataStore.data.items.length > 0) {
            console.log('dataIOController.resetConfigurationEditor - configuredDataStore: ', configuredDataStore.data.items[0].data);
            var data = configuredDataStore.data.items[0].data;
            data.alias, data.measurementName, data.measurementType, data.templateType, data.unit, data.relation = null;
        }
        else {
            configuredDataStore.setData([]);
        }

        // reset panelObjectComponent
        var componentOrObjectStore = self.getViewModel().getStore('componentOrObjectStore');
        componentOrObjectStore.setData([]);
        Ext.ComponentQuery.query('#maskObjectsAndComponents')[0].show();

        self.toggleSaveButton();

        self.localizePointOnMap(null, null);
    },
    // load and populate layer tree nodes
    getNodes: function (url, layerTypeSent, supportLayersConfig) {
        var self = this;

        console.log('dataIOController.getNodes - url: ', url);
        console.log('dataIOController.getNodes - layerTypeSent: ', layerTypeSent);

        if (layerTypeSent == 'componentType' || layerTypeSent == 'objectType') {
            Ext.Ajax.request({
                scope: this,
                url: url,
                method: 'GET',
                contentType: 'application/json',
                callback: function (options, success, response) {
                    if (success) {
                        try {
                            var result = Ext.decode(response.responseText);
                            console.log('dataIOController.getNodes - result: ', result);
                            var items = [];
                            var parentNode = {};
                            var layerType;

                            if (layerTypeSent == 'componentType') {
                                layerType = 'ComponentType';
                                items = result.componentTypes;
                                parentNode.leaf = false;
                                parentNode.name = 'Component layer';
                                parentNode.children = [];
                            }
                            else if (layerTypeSent == 'objectType'){
                                layerType = 'ObjectType';
                                items = result.objectTypes;
                                parentNode.leaf = false;
                                parentNode.name = 'Object layer';
                                parentNode.children = [];
                                console.log('dataIOController.getNodes - objectTypes items: ', items);
                            }

                            for (o = 0; o < items.length; o++) {
                                var item = items[o];
                                console.log('\ndataIOController.getNodes - item: ', item);

                                var layerId = AGS3xIoTAdmin.util.util.generateUUID();
                                console.log('dataIOController.getNodes - layerId: ', layerId);

                                //var lyrInfo = self.getLayerInfoFromUrl(item.wfs);
                                var lyrInfo = {
                                    layerUrl: item.wfs,
                                    layerName: item.wfsLayer
                                };
                                console.log('dataIOController.getNodes - lyrInfo: ', lyrInfo);

                                if (lyrInfo.layerUrl != null && lyrInfo.layerName != null ) {
                                    var legendNodeUrl = lyrInfo.layerUrl;
                                    legendNodeUrl += ((legendNodeUrl.indexOf('?') < 0) ? "?" : "&") + "service=" +
                                    "WMS" + "&version=" +
                                    "1.1.1" + "&request=GetLegendGraphic&FORMAT=image/png&transparent=true&layer=" +
                                    lyrInfo.layerName;

                                    console.log('dataIOController.getNodes - lyrInfo.layerName: ', lyrInfo.layerName);

                                    var node = {
                                        checked: true,
                                        name: item.name,
                                        iconCls: 'treenode-no-icon',
                                        data: { text: item.name, layerId: layerId, layerType: layerType },
                                        leaf: false,
                                        children: [{
                                            iconCls: 'treenode-no-icon',
                                            name: '<table class="legend_table"><tr><td><img src=' + legendNodeUrl + ' /></td></tr></table>',
                                            leaf: true,
                                            checked: null
                                        }]
                                    }

                                    var keyDescriptions = [];
                                    if (item.hasOwnProperty('keyDescriptions')) {
                                        for (var k = 0; k < item.keyDescriptions.length; k++) {
                                            var keyDescription = item.keyDescriptions[k];
                                            keyDescriptions.push({ field: keyDescription.field, type: keyDescription.type, value: null });
                                        }
                                    }
                                    else {
                                        keyDescriptions.push({ field: 'id', type: 'integer', value: null });
                                    }

                                    var layerInfo = {
                                        itemId: item.id,
                                        layerId: layerId,
                                        layerType: layerType,
                                        isBasemapLayer: false,
                                        tocName: item.name,
                                        keyDescriptions: keyDescriptions,
                                        zIndex: item.zOrder
                                    };
                                    console.log('dataIOController.getNodes - layerUrl to add to map: ', lyrInfo.layerUrl);
                                    console.log('dataIOController.getNodes - layerName to add to map: ', lyrInfo.layerName);
                                    console.log('dataIOController.getNodes - layerInfo to add to map: ', layerInfo);

                                    AGS3xIoTAdmin.util.events.fireEvent('addLayers', { layerUrl: lyrInfo.layerUrl, layerName: lyrInfo.layerName, layerInfo: layerInfo });

                                    parentNode.children.push(node);
                                }
                            }

                            AGS3xIoTAdmin.util.events.fireEvent('addTocItem', parentNode);
                        }
                        catch (error) {
                            console.log('dataIOController.getNodes - error: ', error);
                        }
                    }
                }
            }, this);
        }
        else if (layerTypeSent == 'supportType') {
            console.log('dataIOController.getNodes - supportType, supportLayerItem: ', supportLayerItem);

            // Init parent node
            var parentNode = {};
            parentNode.leaf = false;
            parentNode.name = 'Support layer';
            parentNode.children = [];

            for (var i = 0; i < supportLayersConfig.length; i++) {
                var supportLayerItem = supportLayersConfig[i];

                // add children nodes
                var legendNodeUrl = '';

                var node = {
                    checked: true,
                    name: supportLayerItem.name,
                    iconCls: 'treenode-no-icon',
                    data: { text: supportLayerItem.name, layerId: supportLayerItem.layerId, layerType: supportLayerItem.layers },
                    leaf: false,
                    children: [{
                        iconCls: 'treenode-no-icon',
                        name: '<table class="legend_table"><tr><td><img src=' + supportLayerItem.legendNodeUrl + ' /></td></tr></table>',
                        leaf: true,
                        checked: null
                    }]
                }

                parentNode.children.push(node);

                AGS3xIoTAdmin.util.events.fireEvent(
                    'addLayers',
                    {
                        layerUrl: supportLayerItem.url,
                        layerName: supportLayerItem.layers,
                        layerInfo: supportLayerItem,
                        layerType: 'supportType'
                    }
                );

            }

            AGS3xIoTAdmin.util.events.fireEvent('addTocItem', parentNode);

        }
        else {
            console.log('dataIOController.getNodes - no layerTypeSent value');
        }
    },
    getUnconfiguredDataIO: function () {
        var self = this;

        var measurementTypesDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.measurementTypesStore');
        var measurementTypesData = [];

        for (var i = 0; i < measurementTypesDataStore.data.items.length; i++) {
            measurementTypesData.push(measurementTypesDataStore.data.items[i].data);
        }
        
        console.log('dataIOController.getUnconfiguredDataIO - measurementTypesData: ', measurementTypesData);

        var unconfiguredDataUrl = self.baseUrl + 'unconfigureddataio';

        Ext.Ajax.request({
            scope: this,
            url: unconfiguredDataUrl,
            measurementTypesData: measurementTypesData,
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dataIOController.getUnconfiguredDataIO - result: ', result);
                        var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');

                        var data = [];

                        for (var i = 0; i < result.unConfiguredDataIos.length; i++) {
                            var item = result.unConfiguredDataIos[i];
                            var record = {
                                dataSourceId: item.dataSourceId,
                                sensorId: item.sensorObjectId,
                                sensorObjectName: item.sensorObjectName,
                                sensorObjectAlias: AGS3xIoTAdmin.util.util.getDeviceAlias(item.sensorObjectId, item.sensorObjectName),
                                sensorObjectNodeId: item.sensorObjectNodeId,
                                dataSourceName: item.dataSourceName,
                                name: item.name,
                                description: item.description,
                                sensorObjectDescription: item.sensorObjectDescription,
                                dataId: item.id,
                                unit: item.unit,
                                configured: item.configured,
                                nodeType: item.nodeType,
                                nodeTypeString: AGS3xIoTAdmin.util.util.getMeasurementTypeNameFromId(item.nodeType, options.measurementTypesData)
                            };

                            data.push(record);
                        }

                        console.log('dataIOController.getUnconfiguredDataIO - data: ', data);

                        unconfiguredDataStore.setData(data);
                        console.log('dataIOController.getUnconfiguredDataIO - unconfiguredDataStore: ', unconfiguredDataStore);

                        var comboxConfiguredFilterValue = Ext.ComponentQuery.query('#comboxConfiguredFilter')[0].getValue();
                        console.log('dataIOController.getUnconfiguredDataIO - comboxConfiguredFilterValue: ', comboxConfiguredFilterValue);

                        self.configurationSelected(comboxConfiguredFilterValue);

                        var filters = unconfiguredDataStore.getFilters();
                        console.log('dataIOController.getUnconfiguredDataIO - filters: ', filters);

                        if (Ext.ComponentQuery.query('#dataGridUnconfigured').length > 0) {
                            /*
                            var dataGridUnconfiguredPlugin = Ext.ComponentQuery.query('#dataGridUnconfigured')[0].getPlugin('filterplugin');
                            console.log('dataIOController.getUnconfiguredDataIO - dataGridUnconfiguredPlugin: ', dataGridUnconfiguredPlugin);

                            dataGridUnconfiguredPlugin.clearFilters();
                            dataGridUnconfiguredPlugin.addFilters(self.activeFilters);
                            */

                            console.log('dataIOController.getUnconfiguredDataIO - activeFilters: ', self.activeFilters);

                            if (self.activeFilters != null && self.activeFilters.length > 0) {
                                //dataGridUnconfiguredPlugin.clearFilters();
                                
                                var gridStore = Ext.ComponentQuery.query('#dataGridUnconfigured')[0].getStore();
                                var gridStoreLength = gridStore.data.items.length;

                                gridStore.filterBy(function (record) {
                                    return true;
                                })

                                gridStore.filterBy(function (record) {
                                    var result = true;
                                    //console.log('dataIOController.getUnconfiguredDataIO - filterBy, record: ', record);
                                    //console.log('dataIOController.getUnconfiguredDataIO - filterBy, self.activeFilters: ', self.activeFilters);
                                    var recordData = record.data;

                                    for (var f = 0; f < self.activeFilters.length; f++) {
                                        var filter = self.activeFilters[f];
                                        var filterProperty = filter.config.property;
                                        var filterValue = filter._value.toLowerCase();

                                        if (filterValue != null && filterValue != '') {
                                            var recordValue = recordData[filterProperty].toLowerCase();
                                           // console.log('dataIOController.getUnconfiguredDataIO - filterBy, recordValue: ', recordValue);
                                            if (recordValue.indexOf(filterValue) == -1) {
                                                result = false;
                                                break;
                                            }
                                            else {
                                                //console.log('dataIOController.getUnconfiguredDataIO - HIT (filtervalue "' + filterValue + '"): ', recordValue);
                                            }
                                        }
                                    }

                                    if (result == true) {
                                        console.log('dataIOController.getUnconfiguredDataIO - filterBy, TRUE: ', record);
                                    }
                                    else if( result == false){
                                        console.log('dataIOController.getUnconfiguredDataIO - filterBy, FALSE: ', record);
                                    }

                                    
                                    return result;
                                });

                                /*
                                console.log('dataIOController.getUnconfiguredDataIO - gridStore: ', gridStore);

                                //console.log('dataIOController.getUnconfiguredDataIO - gridStore filters: ', gridStore.filters);

                                //gridStore.clearFilter();

                                var tempArray = [];

                                for (var t = 0; t < gridStoreLength; t++) {
                                    tempArray.push(gridStore.data.items[t].data);
                                }

                                for (var f = 0; f < self.activeFilters.length; f++) {
                                    var filter = self.activeFilters[f];
                                    var filterProperty = filter.config.property;
                                    var filterValue = filter.config.value.toLowerCase();

                                    var resultArray = [];

                                    for (var x = 0; x < tempArray.length; x++) {
                                        var itemValue = tempArray[x][filterProperty].toLowerCase();
                                        console.log('dataIOController.getUnconfiguredDataIO - itemValue: ' + itemValue + ', target: ' + filterValue);

                                        if (itemValue.indexOf(filterValue) > -1) {
                                            resultArray.push(tempArray[x]);
                                        }
                                    }

                                    tempArray = resultArray;
                                }

                                console.log('dataIOController.getUnconfiguredDataIO - tempArray: ', tempArray);
                                gridStore.loadData(tempArray);*/

                                /*
                                var stringFilterArray = [];

                                for (var f = 0; f < self.activeFilters.length; f++) {
                                    var filter = self.activeFilters[f];

                                    var stringFilter = new Ext.util.Filter({
                                        filterFn: function (item) {
                                            console.log('dataIOController.getUnconfiguredDataIO - filtering item: ', item);
                                            console.log('dataIOController.getUnconfiguredDataIO - filter._property: ', filter._property);
                                            var stringToSearch = item.data[filter._property].toLowerCase();
                                            console.log('dataIOController.getUnconfiguredDataIO - stringToSearch: ', stringToSearch);
                                            var stringToFind = filter._filterValue.toLowerCase();
                                            console.log('dataIOController.getUnconfiguredDataIO - stringToFind: ', stringToFind);
                                            return stringToSearch.indexOf(stringToFind) > -1;
                                        }
                                    });

                                    stringFilterArray.push(stringFilter);
                                }

                                gridStore.filter(stringFilterArray);
                                */

                                /*
                                for (var f = 0; f < self.activeFilters.length; f++) {
                                    var filter = self.activeFilters[f];

                                    var stringFilter = new Ext.util.Filter({
                                        filterFn: function (item) {
                                            console.log('dataIOController.getUnconfiguredDataIO - filtering item: ', item);
                                            console.log('dataIOController.getUnconfiguredDataIO - filter._property: ', filter._property);
                                            var stringToSearch = item.data[filter._property].toLowerCase();
                                            var stringToFind = filter._filterValue.toLowerCase();
                                            return stringToSearch.indexOf(stringToFind) > -1;
                                        }
                                    });

                                    gridStore.filter(stringFilter);
                                }
                                */

                                //console.log('dataIOController.getUnconfiguredDataIO - filterArray: ', filterArray);
                                //gridStore.filter(stringFilter);
                               
                            }
                        }

                        // scroll to top
                        document.getElementById('dataGridUnconfigured').getElementsByClassName('x-grid-view')[0].scrollTop = 0;
                    }
                    catch (ex) {
                        console.log('dataIOController.getUnconfiguredDataIO - exception: ', ex);
                    }
                }
            }
        });
    },
    refreshUnconfiguredData: function() {
        var self = this;
        self.getUnconfiguredDataIO();
    },
    // when data entry is selected, populate editor with corresponding data, configured or unconfigured
    dataEntrySelected: function (record) {
        var self = this;

        self.resetConfigurationEditor();

        console.log('dataIOController.dataEntrySelected - record: ', record);

        var configuredDataStore = self.getViewModel().getStore('configuredDataStore');
        console.log('dataIOController.dataEntrySelected - configuredDataStore: ', configuredDataStore);

        var data; 

        if (record.data.configured == false) {
            console.log('dataIOController.dataEntrySelected - sensor is not configured');

            Ext.ComponentQuery.query('#buttonResetDataIO')[0].setDisabled(true);
            Ext.ComponentQuery.query('#dataIOClearButton')[0].setDisabled(false);

            document.getElementById('featureAccept').disabled = false;
            document.getElementById('acceptComponent').disabled = false;

            data = [
                {
                    'dataId': record.data.dataId,
                    'sensorId': record.data.sensorId,
                    'sensorObjectNodeId': record.data.sensorObjectNodeId,
                    'sensorObjectNodeName': record.data.name,
                    'name': record.data.dataSourceName,
                    'description': record.data.description,
                    'sensorObjectName': record.data.sensorObjectName,
                    'sensorObjectAlias': (record.data.sensorObjectAlias != null && record.data.sensorObjectAlias.length > 0) ? record.data.sensorObjectAlias : record.data.sensorObjectName,
                    'measurementType': record.data.nodeType,
                    'templateType': null,
                    'unit': record.data.unit,
                    'configured': false,

                    'dataSourceId':  record.data.dataSourceId,
                    'sensorObjectDescription': record.data.sensorObjectDescription,
                    'dataSourceName': record.data.dataSourceName,
                    'sensorObjectId': record.data.sensorId,

                    'isBatteryStatus': null
                }
            ]

            console.log('dataIOController.dataEntrySelected - data: ', data);

            var calculationData = self.getViewModel().get('calculationDataStore');
            console.log('dataIOController.dataEntrySelected - calculationData: ', calculationData);

            if (calculationData.data.length > 0) {
                data[0].measurementName = calculationData.data.items[0].data.templateName;
                data[0].templateType = calculationData.data.items[0].data.templateType;
            }

            console.log('dataIOController.dataEntrySelected - data to save to configuredDataStore: ', data);

            configuredDataStore.setData(data);

            self.localizePointOnMap(null, null);

        }
        // set data from configured Data IO
        else {

            // Add loading mask
            var loadingMask = new Ext.LoadMask(
                {
                    msg: self.fieldLabelDataLoading,
                    id: 'loadConfigurationDataMask',
                    target: Ext.ComponentQuery.query('#ags3xDataIOConfig')[0]
                }
            );
            loadingMask.show();

            Ext.ComponentQuery.query('#buttonResetDataIO')[0].setDisabled(false);
            Ext.ComponentQuery.query('#dataIOClearButton')[0].setDisabled(true);

            document.getElementById('featureAccept').disabled = true;
            document.getElementById('acceptComponent').disabled = true;

            console.log('dataIOController.dataEntrySelected - sensor is already configured');

            var url = self.baseUrl + 'dataconfiguration/' + record.data.dataId;
            console.log('dataIOController.dataEntrySelected - url: ', url);

            Ext.Ajax.request({
                scope: this,
                url: url,
                method: 'GET',
                contentType: 'application/json',
                callback: function (options, success, response) {
                    if (success) {
                        try {
                            var result = Ext.decode(response.responseText);
                            console.log('dataIOController.dataEntrySelected - result: ', result);

                            // Load calculations
                            var calculationDataStore = self.getViewModel().getStore('calculationDataStore');
                            console.log('dataIOController.dataEntrySelected - result.configuration.calculationAndStores: ', result.configuration.calculationAndStores);
                            calculationDataStore.setData(result.configuration.calculationAndStores);
                            calculationDataStore.sort([{
                                property: 'calculation',
                                direction: 'ASC'
                            }]);

                            var measurementType = result.configuration.measurementType;
                            var templateType = result.configuration.templateType;
                            var isBatteryStatus = null;
                            var measurementName = AGS3xIoTAdmin.util.util.getMeasurementNameFromTemplateType(templateType, self.getViewModel());
                            var measurementAlias = AGS3xIoTAdmin.util.util.getMeasurementAliasFromTemplateType(templateType, self.getViewModel());

                            dataToTransfer = [
                                {
                                    'dataId': record.data.dataId,
                                    'sensorId': record.data.sensorId,
                                    'sensorObjectNodeId': record.data.sensorObjectNodeId,
                                    'sensorObjectNodeName': record.data.name,
                                    'name': record.data.dataSourceName,

                                    'description': record.data.description,
                                    'alias': measurementAlias,
                                    'measurementName': measurementName,
                                    'measurementType': measurementType,
                                    'templateType': templateType,
                                    'unit': record.data.unit,
                                    'configured': true,

                                    'dataSourceId': record.data.dataSourceId,
                                    'sensorObjectDescription': record.data.sensorObjectDescription,
                                    'dataSourceName': record.data.dataSourceName,
                                    'sensorObjectId': record.data.sensorId,
                                    'sensorObjectName': record.data.sensorObjectName,
                                    'sensorObjectAlias': (record.data.sensorObjectAlias != null && record.data.sensorObjectAlias.length > 0) ? record.data.sensorObjectAlias : record.data.sensorObjectName,
                                    'isBatteryStatus': isBatteryStatus
                                }
                            ]
                            
                            console.log('dataIOController.dataEntrySelected - dataToTransfer: ', dataToTransfer);

                            configuredDataStore.setData(dataToTransfer);

                            Ext.ComponentQuery.query('#maskCalculations')[0].hide();

                            console.log('dataIOController.dataEntrySelected - objectcomponentdatarelation URL: ', self.baseUrl + 'objectcomponentdatarelation/dataio/' + record.data.dataId);

                            var relationUrl = self.baseUrl + 'objectcomponentdatarelation/dataio/' + record.data.dataId;

                            console.log('dataIOController.dataEntrySelected - relationUrl: ', relationUrl);

                            console.log('dataIOController.dataEntrySelected - isBatteryStatus: ', isBatteryStatus);

                            // Load component or object
                            Ext.Ajax.request({
                                scope: this,
                                url: relationUrl,
                                method: 'GET',
                                contentType: 'application/json',
                                callback: function (options, success, response) {
                                    if (success) {
                                        try {
                                            var result = Ext.decode(response.responseText);
                                            var relationResult = result.objectComponenetDataIoRelations[0];

                                            console.log('dataIOController.dataEntrySelected - relation load result: ', result);

                                            if (result.objectComponenetDataIoRelations.length > 0 ) {
                                                var objectComponenetDataIoRelations = result.objectComponenetDataIoRelations[0];

                                                // add relation data to dataToTransfer object
                                                dataToTransfer[0].relation = objectComponenetDataIoRelations;

                                                // Handle object relation
                                                if (objectComponenetDataIoRelations.hasOwnProperty('objectKeys') && objectComponenetDataIoRelations.objectKeys != null) {
                                                    console.log('dataIOController.dataEntrySelected - relation is an object');

                                                    var objectId = Number(objectComponenetDataIoRelations.objectKeys[0].value);
                                                    var objectTypeId = Number(objectComponenetDataIoRelations.objectType);

                                                    console.log('dataIOController.dataEntrySelected - objectId: ', objectId);
                                                    console.log('dataIOController.dataEntrySelected - objectTypeId: ', objectTypeId);

                                                    Ext.Ajax.request({
                                                        scope: this,
                                                        url: self.baseUrl + 'objecttype',
                                                        method: 'GET',
                                                        contentType: 'application/json',
                                                        callback: function (options, success, response) {
                                                            if (success) {
                                                                try {
                                                                    var result = Ext.decode(response.responseText);
                                                                    console.log('dataIOController.dataEntrySelected - object type load result: ', result);
                                                                    for (var i = 0; i < result.objectTypes.length; i++) {
                                                                        var objectType = result.objectTypes[i];
                                                                        
                                                                        if (objectType.type == objectTypeId ) {
                                                                            console.log('dataIOController.dataEntrySelected - (HIT) objectType: ', objectType); var fieldIdTagName = objectType.fieldId;
                                                                            var fieldNameTagName = objectType.fieldName;
                                                                            var fieldDescriptionTagName = objectType.fieldDescription;
                                                                            console.log('dataIOController.dataEntrySelected - fieldIdTagName: ', fieldIdTagName);
                                                                            console.log('dataIOController.dataEntrySelected - fieldNameTagName: ', fieldNameTagName);
                                                                            console.log('dataIOController.dataEntrySelected - fieldDescriptionTagName: ', fieldDescriptionTagName);

                                                                            var wfsSchemaUrl = objectType.wfs + '?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName=' + objectType.wfsLayer;

                                                                            // Get geometry tag name
                                                                            Ext.Ajax.request({
                                                                                scope: this,
                                                                                url: wfsSchemaUrl,
                                                                                method: 'GET',
                                                                                objectType: objectType,
                                                                                contentType: 'text/xml',
                                                                                callback: function (options, success, response) {
                                                                                    if (success) {
                                                                                        try {
                                                                                            var xmlSchemaResult = null;
                                                                                            if (window.DOMParser) {
                                                                                                // code for modern browsers
                                                                                                var parser = new DOMParser();
                                                                                                xmlSchemaResult = parser.parseFromString(response.responseText, "text/xml");
                                                                                            } else {
                                                                                                // code for old IE browsers
                                                                                                var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                                                                                xmlDoc.async = false;
                                                                                                xmlSchemaResult = xmlDoc.loadXML(response.responseText);
                                                                                            }

                                                                                            var schemaChildNodes;
                                                                                            if (navigator.userAgent.indexOf('Edge') >= 0) {
                                                                                                schemaChildNodes = xmlSchemaResult.getElementsByTagName('element')
                                                                                            }
                                                                                            else {
                                                                                                schemaChildNodes = xmlSchemaResult.getElementsByTagName('xsd:element')
                                                                                            }

                                                                                            console.log('dataIOController.dataEntrySelected - schemaChildNodes: ', schemaChildNodes);

                                                                                            console.log('dataIOController.dataEntrySelected - geometryElementName, looking for value GeometryPropertyType');
                                                                                            var geometryElementName;
                                                                                            for (var i = 0; i < schemaChildNodes.length; i++) {
                                                                                                var childNode = schemaChildNodes[i];
                                                                                                var typeNoPrefix = (childNode.getAttribute('type').indexOf(':') > -1) ? childNode.getAttribute('type').split(':')[1] : childNode.getAttribute('type');
                                                                                                console.log('dataIOController.dataEntrySelected - typeNoPrefix: ', typeNoPrefix);
                                                                                                if (typeNoPrefix.trim() == 'GeometryPropertyType') {
                                                                                                    geometryElementName = childNode.getAttribute('name');
                                                                                                    console.log('dataIOController.dataEntrySelected - geometryElementName (HIT): ', geometryElementName);
                                                                                                    break;
                                                                                                }
                                                                                            }

                                                                                            console.log('dataIOController.dataEntrySelected - xmlSchemaResult: ', xmlSchemaResult);

                                                                                            var wfsQueryUrl = options.objectType.wfs;
                                                                                            console.log('dataIOController.dataEntrySelected - wfsQueryUrl: ', wfsQueryUrl);

                                                                                            var queryXmlData = '<wfs:GetFeature service="WFS" version="1.0.0" ' +
                                                                                              'xmlns:wfs="http://www.opengis.net/wfs" ' +
                                                                                              'xmlns:ogc="http://www.opengis.net/ogc" ' +
                                                                                            '>' +
                                                                                            '<wfs:Query typeName="' + options.objectType.wfsLayer + '">' +
                                                                                              '<ogc:Filter>' +
                                                                                                '<ogc:PropertyIsEqualTo>' +
                                                                                                  '<ogc:PropertyName>' + fieldIdTagName + '</ogc:PropertyName>' +
                                                                                                  '<ogc:Value>' + objectId + '</ogc:Value>' +
                                                                                                '</ogc:PropertyIsEqualTo>' +
                                                                                              '</ogc:Filter>' +
                                                                                            '</wfs:Query>' +
                                                                                           '</wfs:GetFeature>';

                                                                                            console.log('dataIOController.dataEntrySelected - queryXmlData: ', queryXmlData);

                                                                                            // Locate object on map
                                                                                            Ext.Ajax.request({
                                                                                                scope: this,
                                                                                                url: wfsQueryUrl,
                                                                                                method: 'POST',
                                                                                                xmlData: queryXmlData,
                                                                                                geometryElementName: geometryElementName,
                                                                                                contentType: 'text/xml',
                                                                                                callback: function (options, success, response) {
                                                                                                    if (success) {
                                                                                                        try {
                                                                                                            var xmlResult = null;
                                                                                                            if (window.DOMParser) {
                                                                                                                // code for modern browsers
                                                                                                                var parser = new DOMParser();
                                                                                                                xmlResult = parser.parseFromString(response.responseText, "text/xml");
                                                                                                            } else {
                                                                                                                // code for old IE browsers
                                                                                                                var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                                                                                                xmlDoc.async = false;
                                                                                                                xmlResult = xmlDoc.loadXML(response.responseText);
                                                                                                            }

                                                                                                            console.log('dataIOController.dataEntrySelected - wfs XML result for object ' + objectId  + ': ', xmlResult);
                                                                                            
                                                                                                            var childNodes = xmlResult.getElementsByTagName('*');
                                                                                                            console.log('dataIOController.dataEntrySelected - childNodes: ', childNodes);

                                                                                                            console.log('dataIOController.dataEntrySelected - description, looking for: ', fieldDescriptionTagName);
                                                                                                            var description;
                                                                                                            for (var i = 0; i < childNodes.length; i++ ) {
                                                                                                                var childNode = childNodes[i];
                                                                                                                var tagNameNoPrefix = (childNode.tagName.indexOf(':') > -1) ? childNode.tagName.split(':')[1] : childNode.tagName;
                                                                                                                console.log('dataIOController.dataEntrySelected - tagNameNoPrefix: ', tagNameNoPrefix);
                                                                                                                console.log('dataIOController.dataEntrySelected - nodeValue: ', childNode.childNodes[0].nodeValue);
                                                                                                                if (fieldDescriptionTagName.trim() == tagNameNoPrefix.trim()) {
                                                                                                                    description = childNode.childNodes[0].nodeValue;
                                                                                                                    break;
                                                                                                                }
                                                                                                            }

                                                                                                            var name;
                                                                                                            console.log('dataIOController.dataEntrySelected - name, looking for: ', fieldNameTagName);
                                                                                                            for (var i = 0; i < childNodes.length; i++) {
                                                                                                                var childNode = childNodes[i];
                                                                                                                var tagNameNoPrefix = (childNode.tagName.indexOf(':') > -1) ? childNode.tagName.split(':')[1] : childNode.tagName;
                                                                                                                console.log('dataIOController.dataEntrySelected - tagNameNoPrefix: ', tagNameNoPrefix);
                                                                                                                console.log('dataIOController.dataEntrySelected - nodeValue: ', childNode.childNodes[0].nodeValue);
                                                                                                                if (fieldNameTagName.trim() == tagNameNoPrefix.trim()) {
                                                                                                                    name = childNode.childNodes[0].nodeValue;
                                                                                                                    break;
                                                                                                                }
                                                                                                            }

                                                                                                            // get location for map localization
                                                                                                            var location;
                                                                                                            var latitude;
                                                                                                            var longitude;

                                                                                                            console.log('dataIOController.dataEntrySelected - looking for geometry under tag: ', options.geometryElementName);

                                                                                                            // Look for geometry tag
                                                                                                            for (var i = 0; i < childNodes.length; i++) {
                                                                                                                var childNode = childNodes[i];
                                                                                                                var tagNameNoPrefix = (childNode.tagName.indexOf(':') > -1) ? childNode.tagName.split(':')[1] : childNode.tagName;
                                                                                                                console.log('dataIOController.dataEntrySelected - tagNameNoPrefix: ', tagNameNoPrefix);
                                                                                                                if (tagNameNoPrefix.trim() == options.geometryElementName) {
                                                                                                                    console.log('dataIOController.dataEntrySelected - geometry tag HIT');

                                                                                                                    var geometryChildNode = childNode.childNodes[0];
                                                                                                                    console.log('dataIOController.dataEntrySelected - geometryChildNode: ', geometryChildNode);

                                                                                                                    var geometryChildNodeTagNameNoPrefix = (geometryChildNode.tagName.indexOf(':') > -1) ? geometryChildNode.tagName.split(':')[1] : geometryChildNode.tagName;
                                                                                                                    console.log('dataIOController.dataEntrySelected - geometryChildNodeTagNameNoPrefix: ', geometryChildNodeTagNameNoPrefix);

                                                                                                                    if (geometryChildNodeTagNameNoPrefix == 'Point') {
                                                                                                                        var pointNode = geometryChildNode.childNodes[0];
                                                                                                                        console.log('dataIOController.dataEntrySelected - pointNode: ', pointNode);
                                                                                                                        var location = pointNode.childNodes[0].nodeValue;
                                                                                                                        console.log('dataIOController.dataEntrySelected - location: ', location);

                                                                                                                        if (location.indexOf(',') > -1) {
                                                                                                                            latitude = location.split(',')[1].trim();
                                                                                                                            longitude = location.split(',')[0].trim();
                                                                                                                        }
                                                                                                                        else {
                                                                                                                            latitude = location.split(' ')[1].trim();
                                                                                                                            longitude = location.split(' ')[0].trim();
                                                                                                                        }
                                                                                                                    }

                                                                                                                    break;
                                                                                                                }
                                                                                                            }

                                                                                                            console.log('dataIOController.dataEntrySelected - (component) location: ', location);

                                                                                                            var componentOrObjectData = [{
                                                                                                                type: 'ObjectType',
                                                                                                                tempId: objectId,
                                                                                                                name: name,
                                                                                                                description: description,
                                                                                                                relation: relationResult
                                                                                                            }];

                                                                                                            var componentOrObjectStore = self.getViewModel().getStore('componentOrObjectStore');

                                                                                                            componentOrObjectStore.setData(componentOrObjectData);

                                                                                                            // unmask and toggle save button
                                                                                                            Ext.ComponentQuery.query('#maskObjectsAndComponents')[0].hide();
                                                                                                            Ext.ComponentQuery.query('#maskCalculations')[0].hide();

                                                                                                            Ext.ComponentQuery.query('#comboxTemplateOptionsHeader')[0].setDisabled(true);

                                                                                                            self.toggleSaveButton();

                                                                                                            self.localizePointOnMap(latitude, longitude);

                                                                                                            loadingMask.destroy();

                                                                                                        }
                                                                                                        catch (ex) {
                                                                                                            console.log('dataIOController.dataEntrySelected - wfsQueryUrl load, EXCEPTION: ', ex);
                                                                                                            loadingMask.destroy();
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            });

                                                                                        }
                                                                                        catch (ex) {
                                                                                            console.log('dataIOController.dataEntrySelected - wfsChemaUrl load, EXCEPTION: ', ex);
                                                                                            loadingMask.destroy();
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                                catch (ex) {
                                                                    console.log('dataIOController.dataEntrySelected - object type load, EXCEPTION: ', ex);
                                                                    loadingMask.destroy();
                                                                }
                                                            }
                                                        }
                                                    });
                                                }

                                                // Handle component relation
                                                else {
                                                    console.log('dataIOController.dataEntrySelected - relation is a component');

                                                    var componentKey = objectComponenetDataIoRelations.componentKey;
                                                    var componentTypeId = objectComponenetDataIoRelations.componentType;

                                                    console.log('dataIOController.dataEntrySelected - componentKey: ', componentKey);
                                                    console.log('dataIOController.dataEntrySelected - componentTypeId: ', componentTypeId);

                                                    console.log('dataIOController.dataEntrySelected - dataToTransfer: ', dataToTransfer);
                                                    
                                                    var componentTypeUrl = self.baseUrl + 'componenttype/type/' + componentTypeId;
                                                    console.log('dataIOController.dataEntrySelected - componentTypeUrl: ', componentTypeUrl);

                                                    Ext.Ajax.request({
                                                        scope: this,
                                                        url: componentTypeUrl,
                                                        method: 'GET',
                                                        contentType: 'application/json',
                                                        callback: function (options, success, response) {
                                                            if (success) {
                                                                try {
                                                                    var result = Ext.decode(response.responseText);
                                                                    console.log('dataIOController.dataEntrySelected - component type ' + componentTypeId + ' load result: ', result);
                                                                    var componentType = result.componentType;

                                                                    var wfs = componentType.wfs + '?service=WFS&version=1.1.0&request=GetFeature&typeNames=' + componentType.wfsLayer;
                                                                    var fieldIdTagName = componentType.fieldId;
                                                                    var fieldDescriptionTagName = componentType.fieldDescription;
                                                                    var componentName = componentType.name;
                                                                    console.log('dataIOController.dataEntrySelected - componentName: ', componentName);

                                                                    var configuredDataStore = self.getViewModel().getStore('configuredDataStore');
                                                                    console.log('dataIOController.dataEntrySelected - check A - configuredDataStore: ', configuredDataStore);

                                                                    var wfsSchemaUrl = componentType.wfs + '?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName=' + componentType.wfsLayer;

                                                                    // Get geometry tag name
                                                                    Ext.Ajax.request({
                                                                        scope: this,
                                                                        url: wfsSchemaUrl,
                                                                        method: 'GET',
                                                                        componentType: componentType,
                                                                        contentType: 'text/xml',
                                                                        callback: function (options, success, response) {
                                                                            if (success) {
                                                                                try {
                                                                                    var xmlSchemaResult = null;
                                                                                    if (window.DOMParser) {
                                                                                        // code for modern browsers
                                                                                        var parser = new DOMParser();
                                                                                        xmlSchemaResult = parser.parseFromString(response.responseText, "text/xml");
                                                                                    } else {
                                                                                        // code for old IE browsers
                                                                                        var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                                                                        xmlDoc.async = false;
                                                                                        xmlSchemaResult = xmlDoc.loadXML(response.responseText);
                                                                                    }

                                                                                    var schemaChildNodes;
                                                                                    if (navigator.userAgent.indexOf('Edge') >= 0) {
                                                                                        schemaChildNodes = xmlResult.getElementsByTagName('element');
                                                                                    }
                                                                                    else {
                                                                                        schemaChildNodes = xmlResult.getElementsByTagName('xsd:element');
                                                                                    }


                                                                                    console.log('dataIOController.dataEntrySelected - schemaChildNodes: ', schemaChildNodes);

                                                                                    console.log('dataIOController.dataEntrySelected - geometryElementName, looking for value GeometryPropertyType');
                                                                                    var geometryElementName;
                                                                                    for (var i = 0; i < schemaChildNodes.length; i++) {
                                                                                        var childNode = schemaChildNodes[i];
                                                                                        var typeNoPrefix = (childNode.getAttribute('type').indexOf(':') > -1) ? childNode.getAttribute('type').split(':')[1] : childNode.getAttribute('type');
                                                                                        console.log('dataIOController.dataEntrySelected - typeNoPrefix: ', typeNoPrefix);
                                                                                        if (typeNoPrefix.trim() == 'GeometryPropertyType') {
                                                                                            geometryElementName = childNode.getAttribute('name');
                                                                                            console.log('dataIOController.dataEntrySelected - geometryElementName (HIT): ', geometryElementName);
                                                                                            break;
                                                                                        }
                                                                                    }

                                                                                    console.log('dataIOController.dataEntrySelected - xmlSchemaResult: ', xmlSchemaResult);

                                                                                    var wfsQueryUrl = options.componentType.wfs;
                                                                                    console.log('dataIOController.dataEntrySelected - wfsQueryUrl: ', wfsQueryUrl);

                                                                                    var queryXmlData = '<wfs:GetFeature service="WFS" version="1.0.0" ' +
                                                                                              'xmlns:wfs="http://www.opengis.net/wfs" ' +
                                                                                              'xmlns:ogc="http://www.opengis.net/ogc" ' +
                                                                                            '>' +
                                                                                            '<wfs:Query typeName="' + options.componentType.wfsLayer + '">' +
                                                                                              '<ogc:Filter>' +
                                                                                                '<ogc:PropertyIsEqualTo>' +
                                                                                                  '<ogc:PropertyName>' + fieldIdTagName + '</ogc:PropertyName>' +
                                                                                                  '<ogc:Value>' + componentKey + '</ogc:Value>' +
                                                                                                '</ogc:PropertyIsEqualTo>' +
                                                                                              '</ogc:Filter>' +
                                                                                            '</wfs:Query>' +
                                                                                           '</wfs:GetFeature>';

                                                                                    console.log('dataIOController.dataEntrySelected - queryXmlData: ', queryXmlData);

                                                                                    // Locate object on map
                                                                                    Ext.Ajax.request({
                                                                                        scope: this,
                                                                                        url: wfsQueryUrl,
                                                                                        method: 'POST',
                                                                                        xmlData: queryXmlData,
                                                                                        geometryElementName: geometryElementName,
                                                                                        contentType: 'text/xml',
                                                                                        callback: function (options, success, response) {
                                                                                            if (success) {
                                                                                                try {
                                                                                                    var xmlResult = null;
                                                                                                    if (window.DOMParser) {
                                                                                                        // code for modern browsers
                                                                                                        var parser = new DOMParser();
                                                                                                        xmlResult = parser.parseFromString(response.responseText, "text/xml");
                                                                                                    } else {
                                                                                                        // code for old IE browsers
                                                                                                        var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                                                                                        xmlDoc.async = false;
                                                                                                        xmlResult = xmlDoc.loadXML(response.responseText);
                                                                                                    }

                                                                                                    console.log('dataIOController.dataEntrySelected - wfs XML result for component ' + componentKey  + ': ', xmlResult);

                                                                                                    var childNodes = xmlResult.getElementsByTagName('*');
                                                                                                    console.log('dataIOController.dataEntrySelected - childNodes: ', childNodes);

                                                                                                    console.log('dataIOController.dataEntrySelected - description, looking for: ', fieldDescriptionTagName);

                                                                                                    var description;

                                                                                                    for (var i = 0; i < childNodes.length; i++) {
                                                                                                        var childNode = childNodes[i];
                                                                                                        var tagNameNoPrefix = (childNode.tagName.indexOf(':') > -1) ? childNode.tagName.split(':')[1] : childNode.tagName;
                                                                                                        console.log('dataIOController.dataEntrySelected - tagNameNoPrefix: ', tagNameNoPrefix);
                                                                                                        console.log('dataIOController.dataEntrySelected - nodeValue: ', childNode.childNodes[0].nodeValue);
                                                                                                        if (fieldDescriptionTagName.trim() == tagNameNoPrefix.trim()) {
                                                                                                            description = childNode.childNodes[0].nodeValue;
                                                                                                            break;
                                                                                                        }
                                                                                                    }

                                                                                                    // get location for map localization
                                                                                                    var location;
                                                                                                    var latitude;
                                                                                                    var longitude;

                                                                                                    console.log('dataIOController.dataEntrySelected - looking for geometry under tag: ', options.geometryElementName);

                                                                                                    // Look for geometry tag
                                                                                                    for (var i = 0; i < childNodes.length; i++) {
                                                                                                        var childNode = childNodes[i];
                                                                                                        var tagNameNoPrefix = (childNode.tagName.indexOf(':') > -1) ? childNode.tagName.split(':')[1] : childNode.tagName;
                                                                                                        console.log('dataIOController.dataEntrySelected - tagNameNoPrefix: ', tagNameNoPrefix);
                                                                                                        if (tagNameNoPrefix.trim() == options.geometryElementName) {
                                                                                                            console.log('dataIOController.dataEntrySelected - geometry tag HIT');

                                                                                                            var geometryChildNode = childNode.childNodes[0];
                                                                                                            console.log('dataIOController.dataEntrySelected - geometryChildNode: ', geometryChildNode);

                                                                                                            var geometryChildNodeTagNameNoPrefix = (geometryChildNode.tagName.indexOf(':') > -1) ? geometryChildNode.tagName.split(':')[1] : geometryChildNode.tagName;
                                                                                                            console.log('dataIOController.dataEntrySelected - geometryChildNodeTagNameNoPrefix: ', geometryChildNodeTagNameNoPrefix);

                                                                                                            if (geometryChildNodeTagNameNoPrefix == 'Point') {
                                                                                                                var pointNode = geometryChildNode.childNodes[0];
                                                                                                                console.log('dataIOController.dataEntrySelected - pointNode: ', pointNode);
                                                                                                                var location = pointNode.childNodes[0].nodeValue;
                                                                                                                console.log('dataIOController.dataEntrySelected - location: ', location);

                                                                                                                if (location.indexOf(',') > -1) {
                                                                                                                    latitude = location.split(',')[1].trim();
                                                                                                                    longitude = location.split(',')[0].trim();
                                                                                                                }
                                                                                                                else {
                                                                                                                    latitude = location.split(' ')[1].trim();
                                                                                                                    longitude = location.split(' ')[0].trim();
                                                                                                                }
                                                                                                            }

                                                                                                            break;
                                                                                                        }
                                                                                                    }

                                                                                                    console.log('dataIOController.dataEntrySelected - (component) location: ', location);

                                                                                    
                                                                                                    var componentOrObjectData = [{
                                                                                                        type: 'ComponentType',
                                                                                                        tempId: componentTypeId + '-' + componentKey, // include componentType??
                                                                                                        name: componentName,
                                                                                                        description: description
                                                                                                    }];

                                                                                                    var componentOrObjectStore = self.getViewModel().getStore('componentOrObjectStore');

                                                                                                    componentOrObjectStore.setData(componentOrObjectData);

                                                                                    
                                                                                                    // unmask and toggle save button
                                                                                                    Ext.ComponentQuery.query('#maskObjectsAndComponents')[0].hide();
                                                                                                    Ext.ComponentQuery.query('#maskCalculations')[0].hide();
                                                                                                    self.toggleSaveButton();

                                                                                                    Ext.ComponentQuery.query('#comboxTemplateOptionsHeader')[0].setDisabled(true);

                                                                                    
                                                                                                    self.localizePointOnMap(latitude, longitude);

                                                                                                    loadingMask.destroy();
                                                                                    
                                                                                                }
                                                                                                catch (ex) {
                                                                                                    console.log('dataIOController.dataEntrySelected - wfsQueryUrl load, EXCEPTION: ', ex);
                                                                                                    loadingMask.destroy();
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                                catch (ex) {
                                                                                    console.log('dataIOController.dataEntrySelected - wfsSchemaUrl load, EXCEPTION: ', ex);
                                                                                    loadingMask.destroy();
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                                    
                                                                }
                                                                catch (ex) {
                                                                    console.log('dataIOController.dataEntrySelected - load component data, EXCEPTION: ', ex);
                                                                    loadingMask.destroy();
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                            else {
                                                console.log('dataIOController.dataEntrySelected - empty result');
                                                loadingMask.destroy();
                                            }
                                        }
                                        catch(ex) {
                                            console.log('dataIOController.dataEntrySelected - relation load, EXCEPTION: ', ex);
                                            loadingMask.destroy();
                                        }
                                    }
                                }
                            });
                        }
                        catch (ex) {
                            console.log('dataIOController.dataEntrySelected - calculation load, EXCEPTION: ', ex);
                            loadingMask.destroy();
                        }
                    }
                }
            });
        }
        
        if (Ext.ComponentQuery.query('#buttonSelectComponentOfDataIO').length > 0) {
            Ext.ComponentQuery.query('#buttonSelectComponentOfDataIO')[0].setDisabled(false);
        }
        self.toggleSaveButton();
    },
    dataEntryUnselected: function() {
        var self = this;
        var store = self.getViewModel().getStore('configuredDataStore');
        var data = []
        store.setData(data);

        Ext.ComponentQuery.query('#buttonResetDataIO')[0].setDisabled(true);

        self.toggleSaveButton();

        self.resetConfigurationEditor();
    },
    templateSelected: function (selectionData) {
        var self = this;

        // Add loading mask
        Ext.ComponentQuery.query('#maskCalculations')[0].show();

        var templateType = selectionData.value;
        var measurementAlias = AGS3xIoTAdmin.util.util.getMeasurementAliasFromTemplateType(templateType, self.getViewModel());
        var measurementName = selectionData.rawValue;

        console.log('dataIOController.templateSelected - templateType: ', templateType);
        console.log('dataIOController.templateSelected - measurementName: ', measurementName);

        // load template data 
        if (templateType) {

            var configuredDataStore = self.getViewModel().getStore('configuredDataStore');
            console.log('dataIOController.templateSelected - configuredDataStore before template selection:', configuredDataStore);

            if (configuredDataStore.data.length == 1) {
                console.log('dataIOController.templateSelected - existing record will be edited');
                var selectedRecordEntry = configuredDataStore.data.items[0].data;
                console.log('dataIOController.templateSelected - selectedRecordEntry: ', selectedRecordEntry);
                console.log('dataIOController.templateSelected - selectedRecordEntry.relation: ', selectedRecordEntry.relation);
                var data = [];
                data[0] = selectedRecordEntry;
                data[0].alias = measurementAlias;
                data[0].measurementName = measurementName;
                data[0].templateType = templateType;

                console.log('dataIOController.templateSelected - data: ', data);

                configuredDataStore.setData(data);

            }
            else {
                console.log('dataIOController.templateSelected - no existing record, template measurement placeholder');

                var data = [
                    {
                        measurementName: measurementName,
                        templateType: templateType
                    }
                ];
                configuredDataStore.setData(data);
            }

            console.log('dataIOController.templateSelected - configuredDataStore after template selection: ', configuredDataStore);

            // load calculation data
            var url = self.baseUrl + 'calculationandstore/templateid/' + templateType;

            console.log('dataIOController.templateSelected - calculation URL: ', url);

            Ext.Ajax.request({
                scope: this,
                url: url,
                method: 'GET',
                contentType: 'application/json',
                callback: function (options, success, response) {
                    if (success) {
                        try {
                            var result = Ext.decode(response.responseText);
                            console.log('dataIOController.templateSelected -  fetch calculationandstore/templateid, result: ', result);
                            var calculations = result.calculationAndStores;
                            var data = [];
                            for (var c = 0; c < calculations.length; c++) {
                                var item = calculations[c];
                                var record = {
                                    calculation: item.calculation,
                                    calculationType: item.calculationType,
                                    formula: item.formula,
                                    unit: item.unit,
                                    store: item.store,
                                    aggregationAndStores: item.aggregationAndStores,
                                    dataIoId: item.dataIoId,
                                    templateType: templateType,
                                    templateName: measurementName
                                };
                                data[Number(item.calculation)] = record;
                            }

                            console.log('dataIOController.templateSelected - calculationandstore/templateid - data: ', data);

                            var store = this.getViewModel().getStore('calculationDataStore');
                            store.setData(data)

                            Ext.ComponentQuery.query('#comboxTemplateOptions')[0].setDisabled(false);
                            Ext.ComponentQuery.query('#comboxTemplateOptionsHeader')[0].setDisabled(false);
                            Ext.ComponentQuery.query('#comboxTemplateOptionsHeader')[0].setValue(templateType);

                            self.toggleSaveButton();
                        }
                        catch(exception) {
                            console.log('dataIOController.templateSelected - load calculationandstore, EXCEPTION: ', exception);
                        }
                    }
                }
            }, this);

            // unmask
            Ext.ComponentQuery.query('#maskCalculations')[0].hide();
        }
        else {
            // mask
            Ext.ComponentQuery.query('#maskCalculations')[0].show();
        } 
    },
    openCalculationAggregate: function (index, dataSource) {
        var self = this;

        console.log('dataIOController.openCalculationAggregate - index: ', index);
        
        var aggData = dataSource.data.items[index].data.aggregationAndStores;

        console.log('dataIOController.openCalculationAggregate - data: ', aggData);

        var subData = new Array(5);

        for (var i = 0; i < aggData.length; i++ ) {
            var object = {};
            object.aggregationCalculation = aggData[i].aggregationCalculation;
            object.aggregate = aggData[i].aggregate;
            object.unit = aggData[i].unit;
            object.scaleToUnit = aggData[i].scaleToUnit;
            object.aggregationInterval = aggData[i].aggregationInterval;

            object.statusOn = aggData[i].statusOn;
            object.max = aggData[i].max;
            object.high = aggData[i].high;
            object.low = aggData[i].low;
            object.min = aggData[i].min;

            object.aggregationType = aggData[i].aggregationType;

            subData[aggData[i].aggregationType] = object;
        }

        var selectedCalculationsStore = Ext.create('Ext.data.Store', {
            storeId: 'selectedCalculationsStore',
            fields: [
                'aggregationCalculation',
                'aggregate',
                'unit',
                'scaleToUnit',
                'aggregationInterval',
                'status',
                'max',
                'high',
                'low',
                'min'
            ],
            data: subData
        });

        self.calculationsInfoWindow = new Ext.Window({
            id: 'calculationsInfoWindow',
            header: {
                title: self.fieldLabelCalculationsWindowTitle,
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important'
                }
            },
            closable: true,
            width: 780,
            height: 242,
            x: ((window.innerWidth - 780) / 2),
            y: ((window.innerHeight - 242) / 2),
            border: false,
            frame: false,
            modal: true,
            maskClickAction: 'destroy',
            renderTo: document.body,
            style: {
                'text-align': 'center',
                'border': '0 !important',
                'border-radius': '0px !important'
            },
            bodyStyle: {
                'background': '#ffffff !important'
            },
            layout: 'border',
            items: [
                {
                    xtype: 'panel',
                    id: 'calculationsInfoPanel',
                    region: 'center',
                    width: 520,
                    border: false,
                    style: {
                        'text-align': 'center',
                        'border': '0 !important',
                        'margin-top': '10px',
                        'padding-left': '10px',
                        'padding-right': '10px'
                    },
                    items: [
                        {
                            xtype: 'gridpanel',
                            id: 'calculationsGridPanel',
                            multiSelect: false,
                            frame: true,
                            allowDeselect: true,
                            store: Ext.data.StoreManager.lookup('selectedCalculationsStore'),
                            plugins: {
                                ptype: 'rowediting',
                                clicksToEdit: 1
                            },
                            columns: [
                                {
                                    text: self.fieldLabelCalculation,
                                    dataIndex: 'aggregationCalculation',
                                    flex: 1
                                },
                                {
                                    text: self.fieldLabelAggregate,
                                    dataIndex: 'aggregate',
                                    width: 90,
                                    renderer: function (value) {
                                        console.log('Rendering: ', value);
                                        switch (value) {
                                            case true:
                                                return 'Yes';
                                            case false:
                                                return 'No';
                                        }
                                    },
                                    editor: {
                                        xtype: 'combobox',
                                        store: self.getViewModel().getStore('boolTypes'),
                                        displayField: 'name',
                                        valueField: 'type',
                                        queryMode: 'local',
                                        editable: false
                                    }
                                },
                                {
                                    text: self.fieldLabelUnit,
                                    dataIndex: 'unit',
                                    width: 75,
                                    editor: {
                                        xtype: 'combobox',
                                        store: self.getViewModel().getStore('unitTypesStore'),
                                        displayField: 'name',
                                        valueField: 'id',
                                        queryMode: 'local',
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item" title="{description.da}">{name}</li>',
                                            '</tpl></ul>'
                                        )
                                    }
                                },
                                {
                                    text: self.fieldLabelScaleToUnit,
                                    dataIndex: 'scaleToUnit',
                                    width: 120,
                                    editor: {
                                        xtype: 'textfield',
                                        maskRe: /[0-9.-]/
                                    }
                                },
                                {
                                    text: self.fieldLabelInterval,
                                    dataIndex: 'aggregationInterval',
                                    width: 75
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'gridpanel',
                    id: 'statusGridPanel',
                    region: 'east',
                    width: 260,
                    style: {
                        'margin-top': '10px',
                        'margin-right': '10px'
                    },
                    bodyStyle: {
                        'overflow': 'hidden !important'
                    },
                    multiSelect: false,
                    frame: true,
                    allowDeselect: true,
                    store: Ext.data.StoreManager.lookup('selectedCalculationsStore'),
                    plugins: {
                        ptype: 'rowediting',
                        clicksToEdit: 1,
                        listeners: {
                            edit: function (editor, context, options) {
                                console.log('dataIOController.edit - editor: ', editor);
                                console.log('dataIOController.edit - context: ', context);
                                console.log('dataIOController.edit - options: ', options);

                                // convert strings with commas to strings with punctuations for number conversion

                                self.toggleStatusMonitoring(editor, context, options);
                            },
                            beforeedit: function (editor, context, options) {
                                console.log('dataIOController.beforeedit - editor: ', editor);
                                console.log('dataIOController.beforeedit - context: ', context);
                                console.log('dataIOController.beforeedit - options: ', options);

                                
                            }
                        }
                    },
                    columns: [
                        {
                            text: 'Status',
                            dataIndex: 'statusOn',
                            width: 60,
                            sortable: false,
                            cls: 'formula-header-extra',
                            tdCls: 'open-status-button',
                            renderer: function (value, b, c, d, e) {
                                console.log('Value: ', value);
                                if (value == true) {
                                    return '<div class="fa fa-bell" title="Statusmonitorering er aktiveret"></div>'
                                }
                                else {
                                    return '<div class="fa fa-bell-slash-o" title="Statusmonitorering er ikke aktiveret"></div>'
                                }
                            },
                            listeners: {
                                click: function (component, b, c, d, e) {
                                    console.log('click - open formular editor: ', component);
                                    console.log('Row: ', component.eventPosition.rowIdx);
                                }
                            }
                        },
                        {
                            text: 'Max',
                            dataIndex: 'max',
                            width: 50,
                            editor: {
                                xtype: 'textfield',
                                maskRe: /[0-9.,]/
                            }
                        },
                        {
                            text: 'High',
                            dataIndex: 'high',
                            width: 50,
                            editor: {
                                xtype: 'textfield',
                                maskRe: /[0-9.,]/
                            }
                        },
                        {
                            text: 'Low',
                            dataIndex: 'low',
                            width: 50,
                            editor: {
                                xtype: 'textfield',
                                maskRe: /[0-9.,]/
                            }
                        },
                        {
                            text: 'Min',
                            dataIndex: 'min',
                            width: 50,
                            editor: {
                                xtype: 'textfield',
                                maskRe: /[0-9.,]/
                            }
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    height: 40,
                    padding: '5px 10px 10px 10px',
                    region: 'south',
                    bodyStyle: {
                        'background': 'transparent !important'
                    },
                    items: [
                        {
                            xtype: 'button',
                            text: self.fieldLabelButtonCalculationAccept,
                            handler: function () {
                                self.performAggregationUpdate(index, dataSource, subData)
                            },
                            width: 80,
                            style: {
                                'float': 'right',
                                'margin-left': '10px'
                            }
                        },
                        {
                            xtype: 'button',
                            text: self.fieldLabelButtonCalculationCancel,
                            handler: function () {
                                self.cancelAggregationUpdate()
                            },
                            width: 80,
                            style: {
                                'float': 'right',
                                'margin-left': '10px'
                            }
                        }
                    ]
                }
            ]
        }).show();
    },
    toggleStatusMonitoring: function (editor, context, options) {
        var self = this;

        var index = context.rowIdx;

        console.log('dataIOController.toggleStatusMonitoring - editor: ', editor);
        console.log('dataIOController.toggleStatusMonitoring - context: ', context);
        console.log('dataIOController.toggleStatusMonitoring - options: ', options);

        var storeData = context.store.data.items[index].data;
        console.log('dataIOController.toggleStatusMonitoring - storeData: ', storeData);

        var statusIconElement = document.getElementById('statusGridPanel').getElementsByClassName('open-status-button')[index].getElementsByClassName('fa')[0];
        console.log('dataIOController.toggleStatusMonitoring - statusIconElement: ', statusIconElement);

        if (
            (storeData.max != null && storeData.max.length > 0) ||
            (storeData.high != null && storeData.high.length > 0) ||
            (storeData.low != null && storeData.low.length > 0) ||
            (storeData.min != null && storeData.min.length > 0)
            ) {
            console.log('dataIOController.toggleStatusMonitoring - turn status on');

            storeData.statusOn = true;

            statusIconElement.className = 'fa fa-bell';
        }
        else {
            console.log('dataIOController.toggleStatusMonitoring - turn status off');

            storeData.statusOn = false;

            statusIconElement.className = 'fa fa-bell-slash-o';
        }        
    },
    performAggregationUpdate: function (index, dataSource, subData) {
        var self = this;

        console.log('dataIOController.performAggregationUpdate - index: ', index);
        console.log('dataIOController.performAggregationUpdate - dataSource: ', dataSource);
        console.log('dataIOController.performAggregationUpdate - subData: ', subData);

        var calculationDataStore = self.getViewModel().getStore('calculationDataStore');

        var aggregationAndStores = calculationDataStore.data.items[index].data.aggregationAndStores;

        console.log('dataIOController.performAggregationUpdate - calculationDataStore: ', calculationDataStore);
        console.log('dataIOController.performAggregationUpdate - aggregationAndStores at index ' + index + ': ', aggregationAndStores);

        var overallStatusOn = false;

        for (var i = 0; i < aggregationAndStores.length; i++) {
            var aggregationAndStore = aggregationAndStores[i];
            console.log('dataIOController.performAggregationUpdate - for loop, aggregationAndStore: ', aggregationAndStore);

            for (var s = 0; s < subData.length; s++) {
                var subDataRecord = subData[s];

                if (aggregationAndStore.aggregationType == subDataRecord.aggregationType) {
                    console.log('dataIOController.performAggregationUpdate - check HIT... aggregationAndStore.aggregationType: ', aggregationAndStore.aggregationType, 'subDataRecord.aggregationType: ', subDataRecord.aggregationType);
                    aggregationAndStore.aggregate = subDataRecord.aggregate;
                    aggregationAndStore.scaleToUnit = Number(subDataRecord.scaleToUnit);
                    aggregationAndStore.aggregationInterval = subDataRecord.aggregationInterval;
                    aggregationAndStore.unit = subDataRecord.unit;

                    // TBD - add to web service, database, etc
                    aggregationAndStore.statusOn = subDataRecord.statusOn;

                    // replace commas with punctuation
                    if (subDataRecord.max != undefined && subDataRecord.max != null && isNaN(subDataRecord.max) == true) {
                        console.log('max: ', subDataRecord.max);
                        subDataRecord.max = (subDataRecord.max.indexOf(',') > -1) ? subDataRecord.max.replace(',', '.') : subDataRecord.max;
                    }
                    if (subDataRecord.high != undefined && subDataRecord.high != null && isNaN(subDataRecord.high) == true) {
                        console.log('high: ', subDataRecord.high);
                        subDataRecord.high = (subDataRecord.high.indexOf(',') > -1) ? subDataRecord.high.replace(',', '.') : subDataRecord.high;
                    }
                    if (subDataRecord.low != undefined && subDataRecord.low != null && isNaN(subDataRecord.low) == true) {
                        console.log('low: ', subDataRecord.low);
                        subDataRecord.low = (subDataRecord.low.indexOf(',') > -1) ? subDataRecord.low.replace(',', '.') : subDataRecord.low;
                    }
                    if (subDataRecord.min != undefined && subDataRecord.min != null && isNaN(subDataRecord.min) == true) {
                        console.log('min: ', subDataRecord.min);
                        subDataRecord.min = (subDataRecord.min.indexOf(',') > -1) ? subDataRecord.min.replace(',', '.') : subDataRecord.min;
                    }

                    aggregationAndStore.max = (subDataRecord.max == undefined || subDataRecord.max == '') ? null : Number(subDataRecord.max);
                    aggregationAndStore.high = (subDataRecord.high == undefined || subDataRecord.high == '') ? null : Number(subDataRecord.high);
                    aggregationAndStore.low = (subDataRecord.low == undefined || subDataRecord.low == '') ? null : Number(subDataRecord.low);
                    aggregationAndStore.min = (subDataRecord.min == undefined || subDataRecord.min == '') ? null : Number(subDataRecord.min);

                    if (subDataRecord.statusOn == true) {
                        overallStatusOn = true;
                    }

                    break;
                }
            }

            var statusIconElement = document.getElementById('gridpanelCalculation').getElementsByTagName('table')[index].getElementsByClassName('open-status-button')[0].getElementsByClassName('fa')[0];
            if (overallStatusOn == true) {
                statusIconElement.className = 'fa fa-bell'
            }
            else {
                statusIconElement.className = 'fa fa-bell-slash-o'
            }
            
        }

        console.log('dataIOController.performAggregationUpdate - complete, data after: ', aggregationAndStores);

        self.calculationsInfoWindow.destroy();
    },
    cancelAggregationUpdate: function () {
        var self = this;
        console.log('dataIOController.cancelAggregationUpdate');

        self.calculationsInfoWindow.destroy();
    },
    configurationSelected: function (value) {
        var self = this;

        self.resetConfigurationEditor();

        console.log('dataIOController.configurationSelected - value: ', value);

        var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');
        var unconfiguredDataLength = unconfiguredDataStore.data.items.length;

        var filteredDataArray = [];

        for (var i = 0; i < unconfiguredDataLength; i++) {
            var item = unconfiguredDataStore.data.items[i].data;
            
            if( value == 1 ) {
                filteredDataArray.push(item);
            }
            else {
                if(value == 2) {
                    if (item.hasOwnProperty('configured') && item.configured == true) {
                        filteredDataArray.push(item);
                    }
                }
                else if(value == 3) {
                    if (item.hasOwnProperty('configured') && item.configured == false) {
                        filteredDataArray.push(item);
                    }
                }
            }
        }

        console.log('dataIOController.configurationSelected - filteredDataArray: ', filteredDataArray);

        var tempStore = Ext.create('Ext.data.Store', {
            data: filteredDataArray
        });

        Ext.ComponentQuery.query('#dataGridUnconfigured')[0].setStore(tempStore);

        document.getElementById('dataIOCount').innerHTML = '(' + filteredDataArray.length + ')';
    },
    localizePointOnMap: function (latitude, longitude) {
        var self = this;
        console.log('dataIOController.localizePointOnMap - latitude: ', latitude);
        console.log('dataIOController.localizePointOnMap - longitude: ', longitude);

        var coordinate = null;
        if(latitude && longitude) { 
            coordinate = [Number(longitude), Number(latitude)];
        }

        self.fireEvent('setLocationEvent', this, coordinate);
    },
    openFormulaEditor: function (index, dataSource, component) {
        var self = this;

        console.log('dataIOController.openFormularEditor - index: ', index);
        console.log('dataIOController.openFormularEditor - dataSource: ', dataSource);
        console.log('dataIOController.openFormularEditor - component: ', component);

        var dataIOVariables = self.getViewModel().getStore('dataIOVariables');
        var componentOrObjectStore = self.getViewModel().getStore('componentOrObjectStore');
        var componentOrObjectItem;
        var wfs;
        var objectId;
        var domElement = document.getElementById(component.id).getElementsByClassName('x-grid-item')[index].getElementsByTagName('td')[1].getElementsByTagName('div')[0];; // TBD

        if (componentOrObjectStore.data.items.length > 0) {
            componentOrObjectItem = componentOrObjectStore.data.items[0].data;
            console.log('dataIOController.openFormularEditor - componentOrObjectItem to load: ', componentOrObjectItem);

            if (componentOrObjectItem.type == 'ComponentType') {

                var componentType = Number(componentOrObjectItem.tempId.split('-')[0]);
                var componentKey = Number(componentOrObjectItem.tempId.split('-')[1]);

                Ext.Ajax.request({
                    scope: self,
                    url: self.baseUrl + 'objectcomponentdatarelation/component/' + componentKey + '?componenttype=' + componentType,
                    method: 'GET',
                    contentType: 'application/json',
                    callback: function (options, success, response) {
                        if (success) {
                            try {
                                var result = Ext.decode(response.responseText);
                                console.log('dataIOController.openFormularEditor - load component relation, result: ', result);

                                var relations = result.objectComponenetDataIoRelations;

                                for (var i = 0; i < relations.length; i++) {
                                    var relation = relations[i];
                                    if (relation.endTimeString == null && relation.relationType == 2) {
                                        console.log('dataIOController.openFormularEditor - relation to process: ', relation);
                                        objectId = relation.objectKeys[0].value;
                                        var objectType = relation.objectType;
                                        var objectStore = self.getViewModel().getStore('objectTypesStore');

                                        for (var i = 0; i < objectStore.data.items.length; i++) {
                                            var object = objectStore.data.items[i].data;
                                            if (object.type == objectType) {
                                                console.log('dataIOController.openFormularEditor - object: ', object);
                                                wfs = object.wfs + '?service=WFS&version=1.1.0&request=GetFeature&typeNames=' + object.wfsLayer;
                                                console.log('dataIOController.openFormularEditor - wfs to process: ', wfs);
                                                break;
                                            }
                                        }

                                        break;
                                    }
                                }

                                var url = wfs + '&featureID=' + objectId;

                                Ext.Ajax.request({
                                    scope: self,
                                    url: url,
                                    method: 'GET',
                                    contentType: 'application/json',
                                    callback: function (options, success, response) {
                                        if (success) {
                                            try {
                                                var xmlResult = null;
                                                if (window.DOMParser) {
                                                    // code for modern browsers
                                                    var parser = new DOMParser();
                                                    xmlResult = parser.parseFromString(response.responseText, "text/xml");
                                                } else {
                                                    // code for old IE browsers
                                                    var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                                    xmlDoc.async = false;
                                                    xmlResult = xmlDoc.loadXML(response.responseText);
                                                }

                                                console.log('dataIOController.openFormularEditor - wfs XML result: ', xmlResult);

                                                // define feature types as string or number
                                                var featureTypeDescriptionUrl = AGS3xIoTAdmin.util.util.getFeatureTypeDescriptionUrl(wfs); // http://hwd-win2016-01:8090/assens/DanDas/wfs?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName=dandasknudegis_ra

                                                Ext.Ajax.request({
                                                    scope: self,
                                                    url: featureTypeDescriptionUrl,
                                                    method: 'GET',
                                                    contentType: 'application/json',
                                                    xmlResult: xmlResult,
                                                    callback: function (options, success, response) {
                                                        if (success) {
                                                            try {
                                                                var featuresXmlResult = null;
                                                                if (window.DOMParser) {
                                                                    // code for modern browsers
                                                                    var parser = new DOMParser();
                                                                    featuresXmlResult = parser.parseFromString(response.responseText, "text/xml");
                                                                } else {
                                                                    // code for old IE browsers
                                                                    var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                                                    xmlDoc.async = false;
                                                                    featuresXmlResult = xmlDoc.loadXML(response.responseText);
                                                                }

                                                                console.log('dataIOController.openFormularEditor - featureTypeDescription XML result: ', featuresXmlResult);

                                                                var featureDescriptionTags;
                                                                if (navigator.userAgent.indexOf('Edge') >= 0) {
                                                                    featureDescriptionTags = xmlResult.getElementsByTagName('element');
                                                                }
                                                                else {
                                                                    featureDescriptionTags = xmlResult.getElementsByTagName('xsd:element');
                                                                }

                                                                console.log('dataIOController.openFormularEditor - featureDescriptionTags: ', featureDescriptionTags);

                                                                var data = [];

                                                                console.log('dataIOController.openFormularEditor - options.xmlResult: ', options.xmlResult);
                                                                var tags = options.xmlResult.getElementsByTagName('*');
                                                                console.log('dataIOController.openFormularEditor - tags: ', tags);
                                                                console.log('dataIOController.openFormularEditor - tags.length: ', tags.length);

                                                                for (var i = 0; i < tags.length; i++) {
                                                                    if (AGS3xIoTAdmin.util.util.checkTagIsNumber(tags[i].nodeName, featureDescriptionTags) == true) {
                                                                        var tagObject = { 'name': tags[i].nodeName }
                                                                        data.push(tagObject);
                                                                    }
                                                                }

                                                                dataIOVariables.setData(data);

                                                                

                                                                self.openFormulaEditorInfoWindow(index, dataSource, domElement, data);

                                                            }
                                                            catch (exception) {

                                                            }
                                                        }
                                                    }
                                                });

                                            }
                                            catch (exception) {
                                                console.log('dataIOController.openFormularEditor - load feature data, EXCEPTION: ', exception);
                                            }
                                        }
                                    }
                                });

                            }
                            catch(exception) {
                                console.log('dataIOController.openFormularEditor - load component relation, EXCEPTION: ', exception);
                            }
                        }
                    }
                });

            }
            else if (componentOrObjectItem.type == 'ObjectType') {

                var objectType = (componentOrObjectItem.relation) ? componentOrObjectItem.relation.objectType : componentOrObjectItem.objectType;
                var objectStore = self.getViewModel().getStore('objectTypesStore');

                for (var i = 0; i < objectStore.data.items.length; i++) {
                    var object = objectStore.data.items[i].data;
                    if (object.type == objectType) {
                        console.log('dataIOController.openFormularEditor - object: ', object);
                        wfs = object.wfs + '?service=WFS&version=1.1.0&request=GetFeature&typeNames=' + object.wfsLayer;
                        objectId = componentOrObjectItem.tempId;
                        break;
                    }
                }

                console.log('dataIOController.openFormularEditor - wfs to load: ', wfs);

                var url = wfs + '&featureID=' + objectId;

                Ext.Ajax.request({
                    scope: self,
                    url: url,
                    method: 'GET',
                    contentType: 'application/json',
                    callback: function (options, success, response) {
                        if (success) {
                            try {
                                var xmlResult = null;
                                if (window.DOMParser) {
                                    // code for modern browsers
                                    var parser = new DOMParser();
                                    xmlResult = parser.parseFromString(response.responseText, "text/xml");
                                } else {
                                    // code for old IE browsers
                                    var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                    xmlDoc.async = false;
                                    xmlResult = xmlDoc.loadXML(response.responseText);
                                }

                                console.log('dataIOController.openFormularEditor - wfs XML result: ', xmlResult);

                                // define feature types as string or number
                                var featureTypeDescriptionUrl = AGS3xIoTAdmin.util.util.getFeatureTypeDescriptionUrl(wfs); // http://hwd-win2016-01:8090/assens/DanDas/wfs?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName=dandasknudegis_ra

                                Ext.Ajax.request({
                                    scope: self,
                                    url: featureTypeDescriptionUrl,
                                    method: 'GET',
                                    contentType: 'application/json',
                                    xmlResult: xmlResult,
                                    callback: function (options, success, response) {
                                        if (success) {
                                            try {
                                                var featuresXmlResult = null;
                                                if (window.DOMParser) {
                                                    // code for modern browsers
                                                    var parser = new DOMParser();
                                                    featuresXmlResult = parser.parseFromString(response.responseText, "text/xml");
                                                } else {
                                                    // code for old IE browsers
                                                    var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                                    xmlDoc.async = false;
                                                    featuresXmlResult = xmlDoc.loadXML(response.responseText);
                                                }

                                                console.log('dataIOController.openFormularEditor - featureTypeDescription XML result: ', featuresXmlResult);

                                                var featureDescriptionTags;
                                                if (navigator.userAgent.indexOf('Edge') >= 0) {
                                                    featureDescriptionTags = xmlResult.getElementsByTagName('element');
                                                }
                                                else {
                                                    featureDescriptionTags = xmlResult.getElementsByTagName('xsd:element');
                                                }

                                                console.log('dataIOController.openFormularEditor - featureDescriptionTags: ', featureDescriptionTags);

                                                var data = [];

                                                console.log('dataIOController.openFormularEditor - options.xmlResult: ', options.xmlResult);
                                                var tags = options.xmlResult.getElementsByTagName('*');
                                                console.log('dataIOController.openFormularEditor - tags: ', tags);
                                                console.log('dataIOController.openFormularEditor - tags.length: ', tags.length);

                                                for (var i = 0; i < tags.length; i++) {
                                                    if (AGS3xIoTAdmin.util.util.checkTagIsNumber(tags[i].nodeName, featureDescriptionTags) == true) {
                                                        var tagObject = { 'name': tags[i].nodeName }
                                                        data.push(tagObject);
                                                    }
                                                }

                                                dataIOVariables.setData(data);

                                                self.openFormulaEditorInfoWindow(index, dataSource, domElement, data);

                                            }
                                            catch(exception) {
                                                console.log('dataIOController.openFormularEditor - load feature description, EXCEPTION: ', exception);
                                            }
                                        }
                                    }
                                });
                                

                            }
                            catch (exception) {
                                console.log('dataIOController.openFormularEditor - load feature data, EXCEPTION: ', exception);
                            }
                        }
                    }
                });
            }

        }
        else {
            self.openFormulaEditorInfoWindow(index, dataSource, null, null);
        }
    },
    openFormulaEditorInfoWindow: function (index, dataSource, domElement, objectDataVariables) {
        var self = this;

        console.log('dataIOController.openFormulaEditorInfoWindow - index: ', index);
        console.log('dataIOController.openFormulaEditorInfoWindow - dataSource: ', dataSource);
        console.log('dataIOController.openFormulaEditorInfoWindow - domElement: ', domElement);
        console.log('dataIOController.openFormulaEditorInfoWindow - objectDataVariables: ', objectDataVariables);

        var dataId;

        if( Ext.ComponentQuery.query('#dataGridUnconfigured').length > 0) {
            var selectedDataIO = Ext.ComponentQuery.query('#dataGridUnconfigured')[0].getSelectionModel().getSelection()[0].data;
            console.log('dataIOController.openFormulaEditorInfoWindow - selectedDataIO: ', selectedDataIO);
            dataId = selectedDataIO.dataId;
        }

        var transferData = {
            formulaString: dataSource.data.items[index].data.formula,
            dataTarget: {
                object: dataSource.data.items[index].data,
                key: 'formula'
            },
            domTarget: domElement,
            objectDataVariables: objectDataVariables,
            dataId: dataId
        }

        console.log('dataIOController.openFormulaEditorInfoWindow - transferData: ', transferData);

        self.formulaEditorInfoWindow = new Ext.Window({
            id: 'formulaEditorWindow',
            header: {
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important',
                    'text-align': 'center !important'
                },
                bodyStyle: {
                    'text-align': 'center !important'
                }
            },
            closable: true,
            width: 800,
            height: 454,
            x: ((window.innerWidth - 800) / 2),
            y: ((window.innerHeight - 450) / 2),
            padding: '0 0 20px 0',
            border: false,
            frame: false,
            modal: true,
            maskClickAction: 'destroy',
            renderTo: document.body,
            style: {
                'text-align': 'center',
                'border': '0 !important',
                'border-radius': '0px !important'
            },
            items: [
                {
                    xtype: 'ags3xFormulaEditor',
                    transferData: transferData
                }
            ]
        }).show();
    },
    openStatusEditorInfoWindow: function (index, dataSource, domElement, objectDataVariables) {
        var self = this;

        console.log('dataIOController.openStatusEditorInfoWindow - index: ', index);
        console.log('dataIOController.openStatusEditorInfoWindow - dataSource: ', dataSource);
        console.log('dataIOController.openStatusEditorInfoWindow - domElement: ', domElement);
        console.log('dataIOController.openStatusEditorInfoWindow - objectDataVariables: ', objectDataVariables);

        var dataId;

        if (Ext.ComponentQuery.query('#dataGridUnconfigured').length > 0) {
            var selectedDataIO = Ext.ComponentQuery.query('#dataGridUnconfigured')[0].getSelectionModel().getSelection()[0].data;
            console.log('dataIOController.openStatusEditorInfoWindow - selectedDataIO: ', selectedDataIO);
            dataId = selectedDataIO.dataId;
        }

        var transferData = {
            formulaString: dataSource.data.items[index].data.formula,
            dataTarget: {
                object: dataSource.data.items[index].data,
                key: 'formula'
            },
            domTarget: domElement,
            objectDataVariables: objectDataVariables,
            dataId: dataId
        }

        console.log('dataIOController.openStatusEditorInfoWindow - transferData: ', transferData);

        self.formulaEditorInfoWindow = new Ext.Window({
            id: 'statusEditorWindow',
            header: {
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important',
                    'text-align': 'center !important'
                },
                bodyStyle: {
                    'text-align': 'center !important'
                }
            },
            closable: true,
            width: 800,
            height: 454,
            x: ((window.innerWidth - 800) / 2),
            y: ((window.innerHeight - 450) / 2),
            padding: '0 0 20px 0',
            border: false,
            frame: false,
            modal: true,
            maskClickAction: 'destroy',
            renderTo: document.body,
            style: {
                'text-align': 'center',
                'border': '0 !important',
                'border-radius': '0px !important'
            },
            items: [
                {
                    xtype: 'ags3xStatusEditor',
                    transferData: transferData
                }
            ]
        }).show();
    },
    activateMapLocalization: function(record) {
        var self = this;

        console.log('dataIOController.activateMapLocalization - record: ', record);

        if(record.data.type == 'ComponentType') {
            var componentType = Number(record.data.tempId.split('-')[0]);
            var componentId = Number(record.data.tempId.split('-')[1]);

            console.log('dataIOController.activateMapLocalization - get location of component ' + componentId + ', type ' + componentType);

            Ext.Ajax.request({
                scope: self,
                url: self.baseUrl + 'componenttype/type/' + componentType,
                method: 'GET',
                contentType: 'application/json',
                callback: function (options, success, response) {
                    if (success) {
                        try {
                            var result = Ext.decode(response.responseText);
                            console.log('dataIOController.activateMapLocalization - component type result: ', result);

                            var componentType = result.componentType;
                            var wfs = componentType.wfs + '?service=WFS&version=1.1.0&request=GetFeature&typeNames=' + componentType.wfsLayer;

                            console.log('dataIOController.activateMapLocalization - component type ' + componentType + ' wfs: ', wfs);

                            var wfsUrl = wfs + '&featureId=' + componentId;
                            console.log('dataIOController.activateMapLocalization - wfs URL: ', wfsUrl);

                            Ext.Ajax.request({
                                scope: this,
                                url: wfsUrl,
                                method: 'GET',
                                contentType: 'text/xml',
                                callback: function (options, success, response) {
                                    if (success) {
                                        try {
                                            var xmlResult = null;
                                            if (window.DOMParser) {
                                                // code for modern browsers
                                                var parser = new DOMParser();
                                                xmlResult = parser.parseFromString(response.responseText, "text/xml");
                                            } else {
                                                // code for old IE browsers
                                                var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                                xmlDoc.async = false;
                                                xmlResult = xmlDoc.loadXML(response.responseText);
                                            }

                                            console.log('dataIOController.activateMapLocalization - wfs XML result for component ' + componentId + ': ', xmlResult);

                                            // get location for map localization
                                            var location;
                                            var latitude;
                                            var longitude;

                                            var posElement;
                                            if (navigator.userAgent.indexOf('Edge') >= 0) {
                                                posElement = xmlResult.getElementsByTagName('pos');
                                            }
                                            else {
                                                posElement = xmlResult.getElementsByTagName('gml:pos');
                                            }

                                            if (posElement.length > 0) {
                                                console.log('dataIOController.activateMapLocalization - gml:pos exists: ', posElement[0]);
                                                location = posElement[0].childNodes[0].nodeValue;
                                                latitude = location.split(' ')[1];
                                                longitude = location.split(' ')[0];
                                            }
                                            else {
                                                //location = xmlResult.getElementsByTagName('DanDas:knudetype')[0].childNodes[0].nodeValue;
                                            }
                                            console.log('dataIOController.activateMapLocalization - (component) location: ', location);

                                            self.localizePointOnMap(Number(latitude), Number(longitude));
                                        }
                                        catch (exception) {
                                            console.log('dataIOController.activateMapLocalization - load WFS XML data, EXCEPTION: ', exception);
                                        }
                                    }
                                }
                            });
                        }
                        catch (exception) {
                            console.log('dataIOController.activateMapLocalization - load component type data, EXCEPTION: ', exception);
                        }
                    }
                }
            });
        }
        else if (record.data.type == 'ObjectType') {
            var objectId = Number(record.data.tempId);

            Ext.Ajax.request({
                scope: self,
                url: self.baseUrl + 'objecttype/type/' + record.data.relation.objectType,
                method: 'GET',
                contentType: 'application/json',
                callback: function (options, success, response) {
                    if (success) {
                        try {
                            var result = Ext.decode(response.responseText);
                            console.log('dataIOController.activateMapLocalization - object type result: ', result);

                            var wfs = result.objectType.wfs + '?service=WFS&version=1.1.0&request=GetFeature&typeNames=' + result.objectType.wfsLayer;
                            console.log('dataIOController.activateMapLocalization - object type wfs: ', wfs);

                            var wfsUrl = wfs + '&featureId=' + record.data.tempId;

                            Ext.Ajax.request({
                                scope: this,
                                url: wfsUrl,
                                method: 'GET',
                                contentType: 'text/xml',
                                callback: function (options, success, response) {
                                    if (success) {
                                        try {
                                            var xmlResult = null;
                                            if (window.DOMParser) {
                                                // code for modern browsers
                                                var parser = new DOMParser();
                                                xmlResult = parser.parseFromString(response.responseText, "text/xml");
                                            } else {
                                                // code for old IE browsers
                                                var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                                xmlDoc.async = false;
                                                xmlResult = xmlDoc.loadXML(response.responseText);
                                            }

                                            console.log('dataIOController.activateMapLocalization - wfs XML result for object ' + record.data.tempId + ': ', xmlResult);

                                            var childNodes = xmlResult.getElementsByTagName('*');

                                            // get location for map localization
                                            var location;
                                            var latitude;
                                            var longitude;

                                            for (var i = 0; i < childNodes.length; i++) {
                                                var childNode = childNodes[i];
                                                var tagNameNoPrefix = (childNode.tagName.indexOf(':') > -1) ? childNode.tagName.split(':')[1] : childNode.tagName;
                                                console.log('dataIOController.dataEntrySelected - tagNameNoPrefix: ', tagNameNoPrefix);
                                                console.log('dataIOController.dataEntrySelected - nodeValue: ', childNode.childNodes[0].nodeValue);
                                                if (tagNameNoPrefix.trim() == 'pos') {
                                                    location = childNode.childNodes[0].nodeValue;
                                                    latitude = location.split(' ')[1];
                                                    longitude = location.split(' ')[0];

                                                    break;
                                                }
                                            }

                                            console.log('dataIOController.activateMapLocalization - (object) location: ', location);

                                            self.localizePointOnMap(Number(latitude), Number(longitude));
                                        }
                                        catch (exception) {
                                            console.log('dataIOController.activateMapLocalization - load WFS XML data, exception: ', exception);
                                        }
                                    }
                                }
                            });
                        }
                        catch (exception) {
                            console.log('dataIOController.activateMapLocalization - load object type data, EXCEPTION: ', exception);
                        }
                    }
                }
            });
        }
        else {
            console.log('dataIOController.activateMapLocalization - no determinable type');
        }
    },
    resetDataIO: function () {
        var self = this;

        self.resetDataIOWindow = new Ext.Window({
            id: 'resetDataIOWindow',
            header: {
                title: self.fieldLabelResetDataIOQuestion,
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important',
                    'text-align': 'center !important'
                },
                bodyStyle: {
                    'text-align': 'center !important'
                }
            },
            closable: true,
            width: 300,
            height: 72,
            x: ((window.innerWidth - 300) / 2),
            y: ((window.innerHeight - 70) / 2),
            border: false,
            frame: false,
            modal: true,
            maskClickAction: 'destroy',
            renderTo: document.body,
            style: {
                'text-align': 'center',
                'border': '0 !important',
                'border-radius': '0px !important'
            },
            items: [
                {
                    xtype: 'panel',
                    id: 'resetDataIOWindowButtonsHolder',
                    items: [
                        {
                            xtype: 'button',
                            text: self.fieldLabelResetDataIOYes,
                            style: {
                                'float': 'left',
                                'margin-left': '10px',
                                'width': '130px'
                            },
                            handler: function () {
                                console.log('Perform reset Data IO');
                                self.performResetDataIO();
                            }
                        },
                        {
                            xtype: 'button',
                            text: self.fieldLabelResetDataIONo,
                            style: {
                                'float': 'right',
                                'margin-right': '10px',
                                'width': '130px'
                            },
                            handler: function () {
                                console.log('Cancel reset Data IO');
                                self.resetDataIOWindow.destroy();
                            }
                        }
                    ]
                }
            ]
        }).show();
    },
    performResetDataIO: function () {
        var self = this;

        // set end date for relation, set configured to false, delete configuration
        var gridPanel = Ext.ComponentQuery.query('#dataGridUnconfigured')[0];
        var selection = gridPanel.getSelection()[0].data;

        // dataId
        var dataId = selection.dataId;

        console.log('dataIOController.resetDataIO - gridPanel: ', gridPanel);
        console.log('dataIOController.resetDataIO - selection: ', selection);
        console.log('dataIOController.resetDataIO - dataId: ', dataId);

        var relationUrl = self.baseUrl + 'objectcomponentdatarelation/dataio/' + dataId;

        var loadingMask = new Ext.LoadMask(
            {
                msg: self.fieldLabelDataLoading,
                id: 'resetDataIOMask',
                target: Ext.ComponentQuery.query('#ags3xDataIOConfig')[0]
            }
        );

        loadingMask.show();

        var deleteConfigurationUrl = self.baseUrl + 'dataconfiguration/' + dataId

        Ext.Ajax.request({
            scope: this,
            url: deleteConfigurationUrl,
            method: 'DELETE',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);

                        console.log('dataIOController.resetDataIO - delete configuration, result: ', result);

                        if(result['Success'] == true) {
                            Ext.ComponentQuery.query('#buttonResetDataIO')[0].setDisabled(true);
                            Ext.ComponentQuery.query('#resetDataIOMask')[0].destroy();
                            self.resetDataIOWindow.destroy();

                            self.resetConfigurationEditor();

                            // Find record in unconfigureddataio store and set configured to false... then update?
                            var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');
                            console.log('dataIOController.resetDataIO - unconfiguredDataStore: ', unconfiguredDataStore);

                            var filters = unconfiguredDataStore.getFilters();
                            console.log('dataIOController.resetDataIO - filters: ', filters);

                            var updatedRecord = unconfiguredDataStore.findRecord('dataId', dataId);
                            updatedRecord.data.configured = false;
                            console.log('dataIOController.resetDataIO - updatedRecord: ', updatedRecord);

                            var dataGridUnconfigured = Ext.ComponentQuery.query('#dataGridUnconfigured')[0];
                            var plugins = dataGridUnconfigured.getPlugins();
                            console.log('dataIOController.resetDataIO - plugins: ', plugins);

                            //var configuredDataStore = self.getViewModel().getStore('configuredDataStore');
                            //configuredDataStore.setData([]);

                            var selection = dataGridUnconfigured.getSelection(updatedRecord);
                            var internalId = selection[0].internalId;
                            console.log('dataIOController.resetDataIO - selection: ', selection);
                            console.log('dataIOController.resetDataIO - internalId: ', internalId);

                            var configuredRow = document.getElementById('dataGridUnconfigured').querySelectorAll('[data-recordid="' + internalId + '"]')[0].getElementsByClassName('configured-true')[0];
                            configuredRow.className = 'configured-false x-grid-row';

                            var iconCell = configuredRow.getElementsByClassName('configured-status-content')[0];
                            iconCell.className = 'fa fa-close configured-status-content';

                            gridPanel.setSelection(null);
                        }
                    }
                    catch (exception) {
                        console.log('dataIOController.resetDataIO - delete configuration, exception: ', exception);
                        Ext.ComponentQuery.query('#resetDataIOMask')[0].destroy();
                        self.resetDataIOWindow.destroy();
                    }
                }
                else {
                    console.log('dataIOController.resetDataIO - failure, response', response);
                    Ext.ComponentQuery.query('#resetDataIOMask')[0].destroy();
                    self.resetDataIOWindow.destroy();
                }
            }
        });
    },
    cancelComponentMove: function () {
        var self = this;

        console.log('dataIOController.cancelComponentMove');

        self.fireEvent('cancelComponentMovement', this);
    },


    //////////////////////////////
    // SPECIAL                  //
    //////////////////////////////

    performSpecialAction: function () {
        var self = this;

        // 1 - get test sensors
        Ext.Ajax.request({
            scope: this,
            url: self.baseUrl + 'unconfigureddataio',
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);

                        //console.log('dataIOController.performSpecialAction - unconfigureddataio, result: ', result);

                        var testSensorsArray = [];

                        for (var i = 0; i < result.unConfiguredDataIos.length; i++) {
                            var sensor = result.unConfiguredDataIos[i];

                            if (sensor.dataSourceId == 1011 && sensor.configured == false) {
                                testSensorsArray.push(sensor);
                            }
                        }

                        console.log('dataIOController.performSpecialAction - unconfigureddataio, testSensorsArray: ', testSensorsArray);

                        // 2 - get objects, layers: DanDas:dandasknudegis_pst, 
                        // http://hwd-win2016-01:8090/assens/DanDas/wfs?service=WFS&version=1.1.0&request=GetFeature&typeNames=DanDas:dandasknudegis_pst

                        Ext.Ajax.request({
                            scope: this,
                            url: 'http://hwd-win2016-01:8090/assens/DanDas/wfs?service=WFS&version=1.1.0&request=GetFeature&typeNames=DanDas:dandasknudegis_pst',
                            method: 'GET',
                            contentType: 'text/xml',
                            testSensorsArray: testSensorsArray,
                            callback: function (options, success, response) {
                                if (success) {
                                    try {
                                        var xmlResult = null;
                                        if (window.DOMParser) {
                                            // code for modern browsers
                                            var parser = new DOMParser();
                                            xmlResult = parser.parseFromString(response.responseText, "text/xml");
                                        } else {
                                            // code for old IE browsers
                                            var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                            xmlDoc.async = false;
                                            xmlResult = xmlDoc.loadXML(response.responseText);
                                        }

                                        console.log('dataIOController.performSpecialAction - WFS GetFeature, xmlResult: ', xmlResult);

                                        var features = xmlResult.getElementsByTagName('DanDas:dandasknudegis_pst');
                                        console.log('dataIOController.performSpecialAction - features length: ', features.length);

                                        for (var f = 0; f < options.testSensorsArray.length; f++) {
                                            var feature = features[f];
                                            var sensor = options.testSensorsArray[f]
                                            console.log('\ndataIOController.performSpecialAction - feature (' + f + '): ', feature);
                                            console.log('dataIOController.performSpecialAction - sensor (' + f + '): ', sensor);

                                            var ogcFid = feature.getElementsByTagName('DanDas:ogc_fid')[0].childNodes[0].nodeValue;
                                            console.log('dataIOController.performSpecialAction - ogcFid (' + f + '): ', ogcFid);

                                            var seconds = Number(sensor.name.split('_')[1].replace('s', ''));
                                            console.log('dataIOController.performSpecialAction - seconds (' + f + '): ', seconds);

                                            var max = Math.floor((seconds / 10) * 10);
                                            var high = Math.floor((seconds / 10) * 9);
                                            var low = Math.floor((seconds / 10) * 2);
                                            var min = Math.floor((seconds / 10) * 1);

                                            console.log('dataIOController.performSpecialAction - max (' + f + '): ', max);
                                            console.log('dataIOController.performSpecialAction - high (' + f + '): ', high);
                                            console.log('dataIOController.performSpecialAction - low (' + f + '): ', low);
                                            console.log('dataIOController.performSpecialAction - min (' + f + '): ', min);

                                            var calculationAndStores = [
                                                {
                                                    calculation: 0,
                                                    formula: "{{sensor_output_value}} * 1",
                                                    aggregationAndStores: [
                                                        {
                                                            aggregationType: 0,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: true,
                                                            max: max,
                                                            high: high,
                                                            low: low,
                                                            min: min
                                                        },
                                                        {
                                                            aggregationType: 1,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 2,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 3,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 4,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        }
                                                    ]
                                                },
                                                {
                                                    calculation: 1,
                                                    formula: "{{sensor_output_value}} * 1",
                                                    aggregationAndStores: [
                                                        {
                                                            aggregationType: 0,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 1,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 2,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 3,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 4,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        }
                                                    ]
                                                },
                                                {
                                                    calculation: 2,
                                                    formula: "{{sensor_output_value}} * 1",
                                                    aggregationAndStores: [
                                                        {
                                                            aggregationType: 0,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 1,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 2,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 3,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 4,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        }
                                                    ]
                                                },
                                                {
                                                    calculation: 3,
                                                    formula: "{{sensor_output_value}} * 1",
                                                    aggregationAndStores: [
                                                        {
                                                            aggregationType: 0,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 1,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 2,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 3,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        },
                                                        {
                                                            aggregationType: 4,
                                                            unit: "sek",
                                                            store: true,
                                                            aggregate: true,
                                                            scaleToUnit: 1,
                                                            statusOn: false
                                                        }
                                                    ]
                                                }
                                            ]

                                            var objectComponentDataIoRelation = {
                                                "objectKeys": [
                                                    {
                                                        field: "ogc_fid",
                                                        type: "integer",
                                                        value: ogcFid
                                                    }
                                                ],
                                                "objectType": 13,
                                                "componentType": null,
                                                "componentKey": null,
                                                "relationType": 1,
                                                "dataIoType": 17,
                                                "dataIoKey": sensor.id
                                            }

                                            console.log('dataIOController.performSpecialAction - objectComponentDataIoRelation (' + f + '): ', objectComponentDataIoRelation);

                                            param = {
                                                "sensorObjectName": sensor.sensorObjectName,
                                                "sensorObjectAlias": sensor.sensorObjectName,
                                                "dataSourceId": sensor.dataSourceId,
                                                "sensorObjectDescription": sensor.sensorObjectDescription,
                                                "unit": sensor.unit,
                                                "templateType": 25,
                                                "measurementType": 17,
                                                "id": sensor.id,
                                                "description": sensor.description,
                                                "calculationAndStores": calculationAndStores,
                                                "dataSourceName": sensor.dataSourceName,
                                                "sensorObjectId": sensor.sensorObjectId,
                                                "sensorObjectNodeId": sensor.sensorObjectNodeId,
                                                "sensorObjectNodeName": sensor.sensorObjectNodeName,
                                                "relation": objectComponentDataIoRelation,
                                                "isBatteryStatus": null
                                            }

                                            console.log('dataIOController.performSpecialAction - final params (' + f + '): ', param);

                                            var url = self.baseUrl + 'dataconfiguration';

                                            // create new item...
                                            console.log('dataIOController.performSpecialAction - creating new item');

                                            
                                            Ext.Ajax.request({
                                                url: url,
                                                method: 'POST',
                                                jsonData: Ext.JSON.encode(param),
                                                scope: this,
                                                callback: function (options, success, response) {
                                                    if (success) {
                                                        console.log('dataIOController.performSpecialAction - successful save!');
                                                        console.log('dataIOController.performSpecialAction - response: ', response);
                                                    }
                                                    else {
                                                        AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.errorTextConfiguredDataNotSaved, 'ERROR');
                                                        console.log('dataIOController.performSpecialAction - response: ', response);
                                                    }
                                                }
                                            }, this);
                                            
                                        }
                                    }
                                    catch (exception) {
                                        console.log('dataIOController.performSpecialAction - WFS GetFeature, exception: ', exception);
                                    }
                                }
                            }
                        });
                        
                    }
                    catch(exception) {
                        console.log('dataIOController.performSpecialAction - unconfigureddataio, exception: ', exception);
                    }
                }
            }
        });
    },
    updateDeviceAlias: function (editor, context, eOpts) {
        var self = this;

        console.log('dataIOController.updateDeviceAlias - editor: ', editor);
        console.log('dataIOController.updateDeviceAlias - context: ', context);
        console.log('dataIOController.updateDeviceAlias - eOpts: ', eOpts);

        var dataSourceId = context.record.data.dataSourceId;
        var sensorObjectId = context.record.data.sensorId;
        var originalName = context.record.data.sensorObjectName;
        var newNameAlias = context.newValues.sensorObjectAlias;
        console.log('dataIOController.updateDeviceAlias - newNameAlias: ', newNameAlias);

        var params = {
            nameAlias: newNameAlias
        }

        var sensorObjectUpdateUrl = self.baseUrl + 'sensorobject/' + dataSourceId + '/alias?sensorobjectid=' + sensorObjectId;
        console.log('dataIOController.updateDeviceAlias - sensorObjectUpdateUrl: ', sensorObjectUpdateUrl);

        Ext.Ajax.request({
            scope: self,
            url: sensorObjectUpdateUrl,
            method: 'PUT',
            jsonData: Ext.JSON.encode(params),
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dataIOController.updateDeviceAlias - update result: ', result);

                        self.updateConfiguredDataIOAliases(sensorObjectId, newNameAlias, originalName);
                    }
                    catch (exception) {
                        console.log('dataIOController.updateDeviceAlias - EXCEPTION: ', exception);
                    }
                }
                else {
                    console.log('dataIOController.updateDeviceAlias - failure, response: ', response);
                }
            }
        });
    },
    updateConfiguredDataIOAliases: function(sensorObjectId, newNameAlias, originalName) {
        var self = this;

        console.log('dataIOController.updateConfiguredDataIOAliases - sensorObjectId: ', sensorObjectId);
        console.log('dataIOController.updateConfiguredDataIOAliases - newNameAlias: ', newNameAlias);
        console.log('dataIOController.updateConfiguredDataIOAliases - originalName: ', originalName);

        var devicesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.devicesStore');
        console.log('dataIOController.updateConfiguredDataIOAliases - devicesStore, before: ', devicesStore);

        var devicesStoreLength = devicesStore.data.items.length;

        for (var i = 0; i < devicesStoreLength; i++) {
            var deviceItem = devicesStore.data.items[i].data;
            if (deviceItem.deviceId == sensorObjectId) {
                console.log('dataIOController.updateConfiguredDataIOAliases - deviceItem (index ' + i + '): ', deviceItem);
                deviceItem.deviceAlias = newNameAlias;
                break;
            }
        }

        console.log('dataIOController.updateConfiguredDataIOAliases - devicesStore, after: ', devicesStore);

        var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');
        var unconfiguredDataStoreLength = unconfiguredDataStore.data.items.length;

        console.log('dataIOController.updateConfiguredDataIOAliases - unconfiguredDataStore: ', unconfiguredDataStore);

        for (var i = 0; i < unconfiguredDataStoreLength; i++) {
            var unconfiguredDataItem = unconfiguredDataStore.data.items[i].data;
            if (unconfiguredDataItem.sensorId == sensorObjectId) {
                console.log('dataIOController.updateConfiguredDataIOAliases - unconfiguredDataItem (index ' + i + '): ', unconfiguredDataItem);
                unconfiguredDataItem.sensorObjectAlias = newNameAlias;
            }
        }

        Ext.ComponentQuery.query('#dataGridUnconfigured')[0].reconfigure(unconfiguredDataStore);

        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'dataconfiguration',
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dataIOController.updateConfiguredDataIOAliases - get result: ', result);

                        var configurations = result.configurations;

                        for (var i = 0; i < configurations.length; i++) {
                            var configuration = configurations[i];

                            if (configuration.sensorObjectId == sensorObjectId) {
                                console.log('dataIOController.updateConfiguredDataIOAliases - HIT: ', configuration);

                                configuration.sensorObjectAlias = newNameAlias;
                                configuration.sensorObjectName = originalName;

                                Ext.Ajax.request({
                                    scope: self,
                                    url: self.baseUrl + 'dataconfiguration',
                                    method: 'PUT',
                                    jsonData: Ext.JSON.encode(configuration),
                                    contentType: 'application/json',
                                    callback: function (options, success, response) {
                                        if (success) {
                                            try {
                                                var result = Ext.decode(response.responseText);
                                                console.log('dataIOController.updateConfiguredDataIOAliases - update result: ', result);

                                                // Update records data
                                                var unconfiguredDataStore = self.getViewModel().getStore('unconfiguredDataStore');
                                                var length = unconfiguredDataStore.data.items.length;
                                                for (var i = 0; i < length; i++) {
                                                    var dataItem = unconfiguredDataStore.data.items[i].data;
                                                    
                                                    if (sensorObjectId == dataItem.sensorId) {
                                                        console.log('dataIOController.updateConfiguredDataIOAliases - dataItem: ', dataItem);
                                                        dataItem.sensorObjectAlias = newNameAlias;

                                                        Ext.ComponentQuery.query('#dataGridUnconfigured')[0].reconfigure();
                                                    }
                                                }
                                            }
                                            catch (exception) {
                                                console.log('dataIOController.updateConfiguredDataIOAliases - update, EXCEPTION: ', exception);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                    catch (exception) {
                        console.log('dataIOController.updateConfiguredDataIOAliases - EXCEPTION: ', exception);
                    }
                }
                else {
                    console.log('dataIOController.updateConfiguredDataIOAliases - failure, response: ', response);
                }
            }
        });
    },
    processFilterChange: function(filters) {
        var self = this;

        if (filters.length == 1 && filters[0].config.hasOwnProperty('property') == false) {
            console.log('\ndataIOController.processFilterChange - no valid filter sent, probably uncofigured data refresh?', filters);
        }
        else {
            console.log('\ndataIOController.processFilterChange - filters: ', filters);

            var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');
            console.log('dataIOController.processFilterChange - unconfiguredDataStore: ', unconfiguredDataStore);

            //self.activeFilters = filters;
            var sentFiltersArray = [];
            for (var s = 0; s < filters.length; s++) {
                if (sentFiltersArray.indexOf(filters[s].config.property) == -1) {
                    sentFiltersArray.push(filters[s].config.property);
                }
            }

            var existingFiltersArray = [];
            var existingFilters = [];
            var finalFilters = [];
        
            if(self.activeFilters != null) {
                existingFilters = self.activeFilters;
            }

            var existingFiltersLength = existingFilters.length;

            finalFilters = existingFilters;

            /*
            for (var x = 0; x < existingFilters.length; x++) {
                console.log('dataIOController.processFilterChange - existingFilter: ', existingFilters[x]);

                if (existingFiltersArray.indexOf(existingFilters[x]._property) == -1) {
                    existingFiltersArray.push(existingFilters[x]._property);
                    finalFilters.push(existingFilters[x]);
                }
            }
            */

            console.log('dataIOController.processFilterChange - existing properties array (existingFiltersArray): ', existingFiltersArray);

            for (var i = 0; i < filters.length; i++) {
                var filterCandidate = filters[i];
                console.log('dataIOController.processFilterChange - filterCandidate: ', filterCandidate);

                if (filterCandidate.config.hasOwnProperty('property') == true) {
                    console.log('dataIOController.processFilterChange - filterCandidate has property: ', filterCandidate.config.property);

                    filterCandidate.config.value = filterCandidate._value;

                    if (filterCandidate.config.hasOwnProperty('operator') == false) {
                        filterCandidate.config.operator = 'like';
                    }

                    console.log('dataIOController.processFilterChange - checking existing filters: ', existingFilters);

                    // Give priority to filters with isGridFilter property
                    var duplicateDetected = false;
                    for (var x = 0; x < existingFiltersLength; x++) {
                        var existingFilter = existingFilters[x];
                        console.log('dataIOController.processFilterChange - comparing filter: ', filterCandidate);
                        console.log('dataIOController.processFilterChange - ... to existing filter: ', existingFilter);

                        if (existingFilter._property == filterCandidate.config.property) {
                            duplicateDetected = true;

                            existingFilters[x] = filterCandidate;
                        }
                    }

                    if (duplicateDetected == false) {
                        finalFilters.push(filterCandidate);
                    }
                }
                else {
                    console.log('dataIOController.processFilterChange - filterCandidate does not have the config property');
                }
            }

            console.log('dataIOController.processFilterChange - finalFilters: ', finalFilters);

        
            self.activeFilters = [];

            // Only add filters with values
            for (var f = 0; f < finalFilters.length; f++) {
                if (finalFilters[f].config.value != null && finalFilters[f].config.value != '') {
                    // Remove filters that were not sent with the latest filterchange event
                    console.log('dataIOController.processFilterChange - sentFiltersArray: ', sentFiltersArray);

                    self.activeFilters.push(finalFilters[f]);

                    /*if (sentFiltersArray.indexOf(finalFilters[f].config.property) > -1) {
                        self.activeFilters.push(finalFilters[f]);
                    }*/
                
                }
            }

        
        

            console.log('dataIOController.processFilterChange - self.activeFilters: ', self.activeFilters);
       

            var gridStore = Ext.ComponentQuery.query('#dataGridUnconfigured')[0].getStore();
            var gridStoreLength = gridStore.data.items.length;
        }
    },

    /**
     * Called when the view is created
     */
    init: function () {
        var self = this;

        self.baseUrl = AGS3xIoTAdmin.systemData.serviceUrl;      

        // UNCONFIGURED DATA
        var unconfiguredGridColumns = [
            {
                text: '',
                dataIndex: 'configured',
                id: 'colConfigured',
                width: 60,
                cls: 'header-configured-status',
                html: '<div style="height: 28px;width: 60px;position: absolute;top: 0px;left: 0px;" title="' + this.fieldLabelSensorsListStatus + '"></div>',
                renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                    switch (value) {
                        case true:
                            return '<div class="fa fa-check configured-status-content" data-statusvalue="' + value + '" title="' + record.data.statusTooltipText + '"></div>';
                        case false:
                            return '<div class="fa fa-close configured-status-content" data-statusvalue="' + value + '" title="' + record.data.statusTooltipText + '"></div>';
                        
                        default:
                            return '';
                    }
                }
            },
            { text: self.fieldLabelUnconfiguredSource, dataIndex: 'dataSourceName', width: 175, filter: { type: 'string' } },
            {
                text: self.fieldLabelUnconfiguredName,
                dataIndex: 'sensorObjectAlias',
                flex: 1,
                filter: {
                    type: 'string'
                },
                editor: {
                    xtype: 'textfield'
                }
            },
            { text: self.fieldLabelUnconfiguredSensorName, dataIndex: 'name', width: 160, filter: { type: 'string' } },
            { text: self.fieldLabelUnconfiguredSensorId, dataIndex: 'sensorId', width: 80, filter: { type: 'string' } },
            { text: self.fieldLabelUnconfiguredId, dataIndex: 'dataId', width: 100, filter: { type: 'string' } },
            { text: self.fieldLabelUnconfiguredNodeType, dataIndex: 'nodeTypeString', width: 155, filter: { type: 'string' } },
            { text: self.fieldLabelUnconfiguredUnit, dataIndex: 'unit', width: 60, filter: { type: 'string' } }
        ]
        self.getViewModel().set('unconfiguredGridColumns', unconfiguredGridColumns);

        // CONFIGURED DATA
        var configuredGridColumns = [
            { text: self.fieldLabelConfiguredDataId, dataIndex: 'dataId' },
            { text: self.fieldLabelConfiguredSensorId, dataIndex: 'sensorId', flex: 1 },
            { text: self.fieldLabelConfiguredName, dataIndex: 'name', editor: 'textfield', flex: 1 },
            { text: self.fieldLabelConfiguredDescription, dataIndex: 'description', editor: 'textfield', flex: 1 },
            { text: self.fieldLabelConfiguredMeasurementName, dataIndex: 'measurementName', flex: 1 }
        ]
        self.getViewModel().set('configuredGridColumns', configuredGridColumns);

        // CALCULATION DATA
        var calculationGridColumns = [
            { text: self.fieldLabelCalculationsType, dataIndex: 'calculationType', width: 75, sortable: false },
            { text: self.fieldLabelCalculationsFormula, dataIndex: 'formula', editor: 'textfield', flex: 1 },
            {
                text: '',
                width: 35,
                sortable: false,
                cls: 'formula-header-extra',
                tdCls: 'open-formula-button',
                renderer: function (value, b, c, d, e) {
                    return '<div class="fa fa-edit" title="' + self.fieldLabelCalculationsOpenFormulaEditor + '"></div>'
                },
                listeners: {
                    click: function (component, b, c, d, e) {
                        console.log('click - open formular editor: ', component);
                        console.log('Row: ', component.eventPosition.rowIdx);
                        var index = Number(component.eventPosition.rowIdx);
                        self.openFormulaEditor(index, component.dataSource, component);
                    }
                }
            },
            {
                text: 'Status',
                width: 60,
                sortable: false,
                cls: 'formula-header-extra',
                tdCls: 'open-status-button',
                renderer: function (value, element, record, index, recordCount) {
                    console.log('bell test - value: ', value);
                    console.log('bell test - element: ', element);
                    console.log('bell test - record: ', record);
                    console.log('bell test - index: ', index);
                    console.log('bell test - recordCount: ', recordCount);

                    var statusOn = false;

                    for (var i = 0; i < record.data.aggregationAndStores.length; i++ ) {
                        var aggAndStore = record.data.aggregationAndStores[i];

                        if (aggAndStore.statusOn == true) {
                            statusOn = true;
                            break;
                        }
                    }

                    if (statusOn == false) {
                        return '<div class="fa fa-bell-slash-o" title="Status"></div>'
                    }
                    else {
                        return '<div class="fa fa-bell" title="Status"></div>'
                    }
                    
                },
                listeners: {
                    click: function (component, b, c, d, e) {
                        console.log('click - open formular editor: ', component);
                        console.log('Row: ', component.eventPosition.rowIdx);
                        var index = Number(component.eventPosition.rowIdx);
                        self.openCalculationAggregate(index, component.dataSource);
                    }
                }
            },
            {
                text: self.fieldLabelCalculationsAggregationAndStore,
                width: 160,
                sortable: false,
                titleText: self.fieldLabelCalculationsOpenModel,
                renderer: function (value, b, c, d, e) {
                    console.log('dataIOController - fieldLabelCalculationsAggregationAndStore.renderer - value: ', value);
                    console.log('dataIOController - fieldLabelCalculationsAggregationAndStore.renderer - b: ', b);
                    console.log('dataIOController - fieldLabelCalculationsAggregationAndStore.renderer - c: ', c);
                    console.log('dataIOController - fieldLabelCalculationsAggregationAndStore.renderer - d: ', d);
                    console.log('dataIOController - fieldLabelCalculationsAggregationAndStore.renderer - e: ', e);

                    return '<a class="open-calculation-model" href="#">' + self.fieldLabelCalculationsOpenModel + '</a>'

                },
                tdCls: 'open-calculation-link',
                listeners: {
                    click: function (component, b, c, d, e) {
                        console.log('click - component: ', component);
                        console.log('Row: ', component.eventPosition.rowIdx);
                        var index = Number(component.eventPosition.rowIdx);
                        self.openCalculationAggregate(index, component.dataSource);
                    }
                }
            }       
        ]

        self.getViewModel().set('calculationGridColumns', calculationGridColumns);

        var componentOrObjectColumns = [
            { text: self.fieldLabelObjectCompType, dataIndex: 'type', width: 130 },
            { text: self.fieldLabelObjectCompName, dataIndex: 'name', width: 150 },
            { text: self.fieldLabelObjectCompDescription, dataIndex: 'description', flex: 1 },
            { text: self.fieldLabelObjectCompId, dataIndex: 'tempId' }
        ];
        self.getViewModel().set('componentOrObjectColumns', componentOrObjectColumns);

        // get object type nodes
        var objectTypeUrl = self.baseUrl + 'objecttype';
        self.getNodes(objectTypeUrl, 'objectType');

        // get component type nodes
        var componentTypeUrl = this.baseUrl + 'componenttype';
        self.getNodes(componentTypeUrl, 'componentType');

        // get "other type" nodes
        Ext.Ajax.request({
            url: AGS3xIoTAdmin.systemData.serviceUrl + "supportlayer",
            method: 'GET',
            success: function (response) {
                var settings = Ext.decode(response.responseText);
                console.log('settings - support layer test: ', settings);

                var layers = settings.supportLayers;
                var supportLayersConfig = [];

                for (var i = 0; i < layers.length; i++) {
                    var layer = layers[i];

                    var layerObject = {};

                    layerObject.fullUrl = layer.wfs;
                    layerObject.url = layer.wfs.split('?')[0];
                    layerObject.name = layer.name;
                    layerObject.version = AGS3xIoTAdmin.util.util.getUrlParameterByName('version', layer.wfs);
                    layerObject.layers = AGS3xIoTAdmin.util.util.getUrlParameterByName('layers', layer.wfs);
                    layerObject.layerId = AGS3xIoTAdmin.util.util.generateUUID();
                    layerObject.tiled = true;
                    layerObject.bbox = AGS3xIoTAdmin.util.util.getUrlParameterByName('bbox', layer.wfs);
                    layerObject.srs = AGS3xIoTAdmin.util.util.getUrlParameterByName('srs', layer.wfs);
                    layerObject.format = AGS3xIoTAdmin.util.util.getUrlParameterByName('format', layer.wfs);
                    layerObject.zIndex = layer.zOrder;

                    supportLayersConfig.push(layerObject);
                }

                self.getNodes(null, 'supportType', supportLayersConfig);
            },
            failure: function (response) {
                console.log('settings - support layer test - FAILURE: ', response);
            }
        });

        // TEMPORARY TRANSLATION FIX
        var language = AGS3xIoTAdmin.util.util.getUrlParameterByName('locale', window.location.href);
        console.log('dataIOController.init - language: ', language);
        switch(language) {
            case 'en':
                self.fieldLabelCalculationsOpenModel = 'Open calculation model';
                self.fieldLabelCalculationsOpenFormulaEditor = 'Open formula editor';
                break;
            default:
                break;
        }

        // EVENT LISTENERS
        AGS3xIoTAdmin.util.events.on('getObjectKeys', self.getObjectKeys, self);
        AGS3xIoTAdmin.util.events.on('openResultListPanel', self.openResultListPanel, self);
        AGS3xIoTAdmin.util.events.on('dataIODeviceAliasChanged', self.updateDeviceAlias, self);
        AGS3xIoTAdmin.util.events.on('configurationSelected', self.configurationSelected, self);
        AGS3xIoTAdmin.util.events.on('resetDataIO', self.resetDataIO, self);
        AGS3xIoTAdmin.util.events.on('dataEntrySelected', self.dataEntrySelected, self);
        AGS3xIoTAdmin.util.events.on('dataEntryUnselected', self.dataEntryUnselected, self);
        AGS3xIoTAdmin.util.events.on('dataIOTemplateSelected', self.templateSelected, self);
        AGS3xIoTAdmin.util.events.on('activateMapLocalization', self.activateMapLocalization, self);
        AGS3xIoTAdmin.util.events.on('processFilterChange', self.processFilterChange, self);
        
        // TEST
        var stateStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.stateStore');
        console.log('dataIOController.init - stateStore: ', stateStore);
    }
});
