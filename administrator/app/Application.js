/**
 * The main application class. An instance of this class is created by app.js when it
 * calls Ext.application(). This is the ideal place to handle application launch and
 * initialization details.
 */
Ext.define('AGS3xIoTAdmin.Application', {

    extend: 'Ext.app.Application',
    
    name: 'AGS3xIoTAdmin',

    requires: [
         'AGS3xIoTAdmin.model.deviceModel',
         'AGS3xIoTAdmin.model.objectTypeModel',
         'AGS3xIoTAdmin.model.measurementTypeModel',
         'AGS3xIoTAdmin.model.unitModel',
         'AGS3xIoTAdmin.model.dashboardModel',
         'AGS3xIoTAdmin.model.unconfiguredDataModel',
         'AGS3xIoTAdmin.model.robotModel',
         'AGS3xIoTAdmin.model.measurementTemplateModel',
         'AGS3xIoTAdmin.model.aggregationTypeModel',
         'AGS3xIoTAdmin.model.calculationTypeModel',
         'AGS3xIoTAdmin.model.componentTypeModel',
         'AGS3xIoTAdmin.model.configuredDataModel',
         'AGS3xIoTAdmin.model.templateConfigurationModel',
         'AGS3xIoTAdmin.model.boolTypeModel'
    ],

    stores: [
        // TODO: add global / shared stores here
        'AGS3xIoTAdmin.state.stateStore',
        'AGS3xIoTAdmin.state.objectTypesStore',
        'AGS3xIoTAdmin.state.robotsStore',
        'AGS3xIoTAdmin.state.devicesStore',
        'AGS3xIoTAdmin.state.unconfiguredDataStore',
        'AGS3xIoTAdmin.state.measurementTypesStore',
        'AGS3xIoTAdmin.state.unitsStore',
        'AGS3xIoTAdmin.state.dashboardsStore',
        'AGS3xIoTAdmin.state.measurementTemplatesStore',
        'AGS3xIoTAdmin.state.aggregationTypesStore',
        'AGS3xIoTAdmin.state.calculationTypesStore',
        'AGS3xIoTAdmin.state.componentTypesStore',
        'AGS3xIoTAdmin.state.configuredDataStore',
        'AGS3xIoTAdmin.state.templateConfigurationsStore',
        'AGS3xIoTAdmin.state.boolTypesStore'
    ],

    // text content - default is Danish (da), for translations, see locale folder
    textApplicationUpdateAvailableTitle: 'Applikationsopdatering',
    textApplicationUpdateAvailable: 'Der er en opdatering tilgængelig. Genindlæs siden?',

    boolTypeYes: 'Ja',
    boolTypeNo: 'Nej',

    startPageCommand: null,

    robotsLoaded: false,
    devicesLoaded: false,
    objectTypesLoaded: false,
    unconfiguredDataLoaded: false,
    measurementTypesLoaded: false,
    measurementTemplatesLoaded: false,
    dashboardsLoaded: false,
    unitsLoaded: false,
    calculationTypesLoaded: false,
    aggregationTypesLoaded: false,
    componentTypesLoaded: false,
    configuredDataLoaded: false,
    templateConfigurationsLoaded: false,
    boolTypesLoaded: false,

    launch: function () {
        var self = this;

        if (Ext.util.LocalStorage.supported) {
            Ext.state.Manager.setProvider(new Ext.state.LocalStorageProvider());
        }
        else {
            Ext.state.Manager.setProvider(new Ext.state.CookieProvider({
                expires: new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * 1) //1 day
            }));
        }

        AGS3xIoTAdmin.util.events.on('applicationLoadStartData', self.loadStartData, self);

        Ext.create('AGS3xIoTAdmin.view.artogis.ags3x.configuration.settings').load();

        console.log('Application.launch...');
    },
    onAppUpdate: function () {
        Ext.Msg.confirm(this.textApplicationUpdateAvailableTitle, this.textApplicationUpdateAvailable,
            function (choice) {
                if (choice === 'yes') {
                    window.location.reload();
                }
            }
        );
    },
    loadStartData: function(startPageCommand) {
        var self = this;

        console.log('Application.loadStartData - startPageCommand: ', startPageCommand);

        self.startPageCommand = startPageCommand;

        self.applyProxiesToStores();

        self.loadDevices();
        self.loadUnits();
        self.loadObjectTypes();
        self.loadDashboards();
        self.loadRobots();
        self.loadUnconfiguredDataWithMeasurements();
        self.loadMeasurementTemplates();
        self.loadCalculationTypes();
        self.loadAggregationTypes();
        self.loadComponentTypes();
        self.loadConfiguredData();
        self.loadTemplateConfigurations();

        self.loadBoolTypes();
    },
    applyProxiesToStores: function() {
        var self = this;

        self.baseUrl = AGS3xIoTAdmin.systemData.serviceUrl;
        self.layersCapabilitiesUrl = AGS3xIoTAdmin.systemData.mapConfiguration.layersCapabilitiesUrl;
        self.descripeFeatureTypeUrl = AGS3xIoTAdmin.systemData.mapConfiguration.descripeFeatureTypeUrl;

        // Units store
        var unitsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unitsStore');
        unitsStore.setModel('AGS3xIoTAdmin.model.unitModel');
        unitsStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'unit',
                reader: 'json'
            })
        );

        // Devices store
        var devicesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.devicesStore');
        devicesStore.setModel('AGS3xIoTAdmin.model.deviceModel');
        devicesStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'sensorobject',
                reader: 'json'
            })
        );

        // Object types store
        var objectTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.objectTypesStore');
        objectTypesStore.setModel('AGS3xIoTAdmin.model.objectTypeModel');
        objectTypesStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'objecttype',
                reader: 'json'
            })
        );

        // Measurement types store
        var measurementTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.measurementTypesStore');
        measurementTypesStore.setModel('AGS3xIoTAdmin.model.measurementTypeModel');
        measurementTypesStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'measurementtype',
                reader: 'json'
            })
        );
        var measurementTypesStoreSorters = measurementTypesStore.getSorters();
        measurementTypesStoreSorters.add('name');

        // Unconfigured Data IO's store
        var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');
        unconfiguredDataStore.setModel('AGS3xIoTAdmin.model.unconfiguredDataModel');
        unconfiguredDataStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'unconfigureddataio',
                reader: 'json'
            })
        );

        // Dashboards store
        var dashboardsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.dashboardsStore');
        dashboardsStore.setModel('AGS3xIoTAdmin.model.dashboardModel');
        dashboardsStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'dashboard',
                reader: 'json'
            })
        );

        // Robots store
        var robotsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.robotsStore');
        robotsStore.setModel('AGS3xIoTAdmin.model.robotModel');
        robotsStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'datasource',
                reader: 'json'
            })
        );

        // Measurement templates store
        var measurementTemplatesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.measurementTemplatesStore');
        measurementTemplatesStore.setModel('AGS3xIoTAdmin.model.measurementTemplateModel');
        measurementTemplatesStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'measurementtemplate',
                reader: 'json'
            })
        );

        // Calculation types store
        var calculationTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.calculationTypesStore');
        calculationTypesStore.setModel('AGS3xIoTAdmin.model.calculationTypeModel');
        calculationTypesStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'calculationtype',
                reader: 'json'
            })
        );

        // Aggregation types store
        var aggregationTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.aggregationTypesStore');
        aggregationTypesStore.setModel('AGS3xIoTAdmin.model.aggregationTypeModel');
        aggregationTypesStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'aggregationtype',
                reader: 'json'
            })
        );

        // Component types store
        var componentTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.componentTypesStore');
        componentTypesStore.setModel('AGS3xIoTAdmin.model.componentTypeModel');
        componentTypesStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'componenttype',
                reader: 'json'
            })
        );

        // Configured data store
        var configuredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.configuredDataStore');
        configuredDataStore.setModel('AGS3xIoTAdmin.model.configuredDataModel');
        configuredDataStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'dataconfiguration',
                reader: 'json'
            })
        );

        // Template configurations store
        var templateConfigurationsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.templateConfigurationsStore');
        templateConfigurationsStore.setModel('AGS3xIoTAdmin.model.templateConfigurationModel');
        templateConfigurationsStore.setProxy(
            new Ext.data.proxy.Ajax({
                url: self.baseUrl + 'templateconfiguration',
                reader: 'json'
            })
        );

        // Boolean types store
        var boolTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.boolTypesStore');
        boolTypesStore.setModel('AGS3xIoTAdmin.model.boolTypeModel');
    },
    loadUnits: function() {
        var self = this;

        var unitsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unitsStore');

        unitsStore.load(function (records, operation, success) {
            if (success) {
                try {
                    console.log('Application.loadUnits - records: ', records);
                    console.log('Application.loadUnits - operation: ', operation);

                    if (records.length > 0) {

                        var records = records[0].data.units;
                        var length = records.length;

                        var dataArray = [];

                        for (var i = 0; i < length; i++) {
                            var unitObject = records[i];

                            dataArray.push(unitObject);
                        }

                        unitsStore.loadData(dataArray);

                        console.log('Application.loadUnits - unitsStorem, after: ', unitsStore);

                        self.unitsLoaded = true;
                        self.checkDataReadiness();
                    }
                }
                catch (exception) {
                    console.log('Application.loadUnits - EXCEPTION: ', exception);
                }
            }
            else {
                console.log('Application.loadUnits - failure, operation: ', operation);
            }
        });
    },

    loadRobots: function() {
        var self = this;

        var robotsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.robotsStore');

        robotsStore.load(function (records, operation, success) {
            if (success) {
                try {
                    console.log('Application.loadRobots - records: ', records);
                    console.log('Application.loadRobots - records.length: ', records.length);
                    console.log('Application.loadRobots - operation: ', operation);

                    if (records.length > 0) {

                        var records = records[0].data.dataSources;
                        var length = records.length;

                        var dataArray = [];

                        for (var i = 0; i < length; i++) {
                            var robotObject = records[i];

                            dataArray.push(robotObject);
                        }

                        robotsStore.loadData(dataArray);

                        console.log('Application.loadRobots - robotsStore, after: ', robotsStore);

                        self.robotsLoaded = true;
                        self.checkDataReadiness();
                    }
                }
                catch (exception) {
                    console.log('Application.loadRobots - EXCEPTION: ', exception);
                }
            }
            else {
                console.log('Application.loadRobots - failure, operation: ', operation);
            }
        });
    },

    loadObjectTypes: function() {
        var self = this;

        console.log('Application.loadObjectTypes - self.layersCapabilitiesUrl: ', self.layersCapabilitiesUrl);

        Ext.Ajax.request({
            scope: this,
            url: self.layersCapabilitiesUrl,
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

                        console.log('Application.loadObjectTypes - xmlResult: ', xmlResult);
                        var layerNodes = xmlResult.getElementsByTagName('Layer');

                        var scaleHintsObject = {};

                        for (var i = 0; i < layerNodes.length; i++) {
                            var layerNode = layerNodes[i];
                            if (layerNode.getAttribute('queryable') == 1 && layerNode.getElementsByTagName('ScaleHint').length > 0) {
                                var wfsLayerName = layerNode.getElementsByTagName('Name')[0].childNodes[0].nodeValue;
                                var scaleHintNode = layerNode.getElementsByTagName('ScaleHint')[0];
                                var scaleHintMinValue = Number(scaleHintNode.getAttribute('min'));
                                var scaleHintMaxValue = Number(scaleHintNode.getAttribute('max'));

                                scaleHintsObject[wfsLayerName] = {};
                                scaleHintsObject[wfsLayerName]['scaleHintMin'] = scaleHintMinValue;
                                scaleHintsObject[wfsLayerName]['scaleHintMax'] = scaleHintMaxValue;
                            }
                        }

                        console.log('Application.loadObjectTypes - scaleHintsObject: ', scaleHintsObject);

                        var objectTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.objectTypesStore');

                        objectTypesStore.load(function (records, operation, success) {
                            if (success) {
                                try {
                                    console.log('Application.loadObjectTypes - records: ', records);
                                    console.log('Application.loadObjectTypes - operation: ', operation);

                                    if (records.length > 0) {

                                        var records = records[0].data.objectTypes;
                                        var length = records.length;

                                        var dataArray = [];

                                        for (var i = 0; i < length; i++) {
                                            var objectItem = records[i];

                                            if (scaleHintsObject.hasOwnProperty(objectItem.wfsLayer)) {
                                                objectItem['scaleHintMin'] = scaleHintsObject[objectItem.wfsLayer]['scaleHintMin'];
                                                objectItem['scaleHintMax'] = scaleHintsObject[objectItem.wfsLayer]['scaleHintMax'];
                                            }

                                            dataArray.push(objectItem);
                                        }

                                        objectTypesStore.loadData(dataArray);

                                        console.log('Application.loadObjectTypes - objectTypesStore, after: ', objectTypesStore);

                                        self.addGeometryDataToObjectTypes();
                                    }
                                }
                                catch (exception) {
                                    console.log('Application.loadObjectTypes - EXCEPTION: ', exception);
                                }
                            }
                            else {
                                console.log('Application.loadObjectTypes - failure, operation: ', operation);
                            }
                        });

                    }
                    catch(exception) {
                        console.log('Application.loadObjectTypes - get capabilities, exception: ', exception);
                    }
                }
                else {
                    console.log('Application.loadObjectTypes - get capabilities, failure: ', response.responseText);
                }
            }
        });

        
    },

    addGeometryDataToObjectTypes: function() {
        var self = this;

        var objectTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.objectTypesStore');
        var objectTypesStoreLength = objectTypesStore.data.items.length;
        console.log('Application.addGeometryDataToObjectTypes - objectTypesStore: ', objectTypesStore);
        console.log('Application.addGeometryDataToObjectTypes - objectTypesStoreLength: ', objectTypesStoreLength);

        var dataArray = [];

        var counter = 0;

        for(var i = 0; i < objectTypesStoreLength; i++) {
            var objectType = objectTypesStore.data.items[i].data;
            
            // Get gml:GeometryPropertyType tag name
            var descripeFeatureTypeUrl = self.descripeFeatureTypeUrl + '&typeName=' + objectType.wfsLayer
            console.log('Application.addGeometryDataToObjectTypes - descripeFeatureTypeUrl: ', descripeFeatureTypeUrl);

            Ext.Ajax.request({
                scope: this,
                url: descripeFeatureTypeUrl,
                method: 'GET',
                contentType: 'text/xml',
                objectType: objectType,
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

                            console.log('Application.addGeometryDataToObjectTypes - (' + options.objectType.wfsLayer + ') xmlResult: ', xmlResult);
                            console.log('Application.addGeometryDataToObjectTypes - options.objectType.wfsLayer: ', options.objectType.wfsLayer);

                            if (options.objectType.wfsLayer == null || options.objectType.wfsLayer == '' || options.objectType.wfsLayer == undefined) {
                                counter++;
                            }
                            else {
                                var elementNodes;
                                console.log('Application.addGeometryDataToObjectTypes - navigator.userAgent: ', navigator.userAgent);

                                //  || 
                                if (navigator.userAgent.indexOf('Edge') >= 0 || navigator.userAgent.indexOf("MSIE ") > 0) {
                                    console.log('Application.addGeometryDataToObjectTypes - Edge or IE <= 10');
                                    elementNodes = xmlResult.getElementsByTagName('element');
                                }
                                else if (!!navigator.userAgent.match(/Trident.*rv\:11\./)) {
                                    console.log('Application.addGeometryDataToObjectTypes - IE 11');
                                    elementNodes = xmlResult.getElementsByTagName('xsd:element');
                                }
                                else {
                                    elementNodes = xmlResult.getElementsByTagName('xsd:element');
                                }

                                console.log('Application.addGeometryDataToObjectTypes - elementNodes: ', elementNodes);

                                for (var x = 0; x < elementNodes.length; x++) {
                                    var elementNode = elementNodes[x];
                                    console.log('Application.addGeometryDataToObjectTypes - type: ', elementNode.getAttribute('type'));
                                    if(elementNode.getAttribute('type') == 'gml:GeometryPropertyType') {
                                        options.objectType['geometryTagName'] = elementNode.getAttribute('name');
                                        dataArray.push(options.objectType);

                                        counter++;
                                        console.log('Application.addGeometryDataToObjectTypes - (i = ' + counter + ') geometryTagName: ', options.objectType['geometryTagName']);
                                        console.log('Application.addGeometryDataToObjectTypes - objectType: ', options.objectType);
                                        break;
                                    }

                                    console.log('Application.addGeometryDataToObjectTypes - missing geometryTagName, options.objectType: ', options.objectType);
                                }
                            }

                            
                            if (counter == objectTypesStoreLength) {
                                console.log('Application.addGeometryDataToObjectTypes - DONE');

                                objectTypesStore.loadData(dataArray);

                                console.log('Application.addGeometryDataToObjectTypes - objectTypesStore, after: ', objectTypesStore);

                                self.objectTypesLoaded = true;
                                self.checkDataReadiness();
                            }

                            
                        }
                        catch(exception) {
                            console.log('Application.addGeometryDataToObjectTypes - exception: ', exception);
                            console.log('Application.addGeometryDataToObjectTypes - exception, options.objectType: ', options.objectType);
                            console.log('Application.addGeometryDataToObjectTypes - exception, response: ', response);
                        }
                    }
                    else {
                        console.log('Application.addGeometryDataToObjectTypes - failure: ', response.responseText);
                    }
                }
            })
        }
    },

    loadDevices: function () {
        var self = this;

        var devicesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.devicesStore');

        devicesStore.load(function (records, operation, success) {
            if (success) {
                try {
                    console.log('Application.loadDevices - records: ', records);
                    console.log('Application.loadDevices - operation: ', operation);

                    if (records.length > 0) {

                        var records = records[0].data.sensorObjects;
                        var length = records.length;

                        var dataArray = [];

                        for (var i = 0; i < length; i++) {
                            var sensorObject = records[i];

                            var sensorObjectData = {
                                dataSourceId: sensorObject.dataSourceId + '',
                                deviceName: sensorObject.name,
                                deviceAlias: (sensorObject.nameAlias != null && sensorObject.nameAlias.length > 0) ? sensorObject.nameAlias : sensorObject.name,
                                deviceId: sensorObject.id,
                                description: sensorObject.description,
                                location: sensorObject.location,
                                geometry: sensorObject.ogrgeometry
                            }

                            dataArray.push(sensorObjectData);
                        }

                        devicesStore.loadData(dataArray);

                        self.devicesLoaded = true;
                        self.checkDataReadiness();
                    }
                }
                catch(exception) {
                    console.log('Application.loadDevices - EXCEPTION: ', exception);
                }
            }
            else {
                console.log('Application.loadDevices - failure, operation: ', operation);
            }
        });
    },
    loadUnconfiguredDataWithMeasurements: function() {
        var self = this;

        var measurementTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.measurementTypesStore');

        // 1 - Load measurement types (used for setting node types of data IO's below
        measurementTypesStore.load(function (measurementTypesRecords, operation, success) {
            if (success) {
                try {
                    console.log('Application.loadUnconfiguredDataWithMeasurements (measurements): ', measurementTypesRecords);
                    console.log('Application.loadUnconfiguredDataWithMeasurements (measurements) - operation: ', operation);

                    if (measurementTypesRecords.length > 0) {
                        var measurementTypesItems = measurementTypesRecords[0].data.measurementTypes;
                        var length = measurementTypesItems.length;

                        var language = (AGS3xIoTAdmin.util.util.getUrlParameterByName('locale')) ? AGS3xIoTAdmin.util.util.getUrlParameterByName('locale') : 'da';

                        var measurementTypesData = [];

                        for (var i = 0; i < length; i++) {
                            var measurementType = measurementTypesItems[i];

                            if (measurementType.language == language) {
                                if (measurementType.isSignalStrength == true || measurementType.isBatteryStatus == true) {
                                    measurementTypesData.unshift(measurementType);
                                }
                                else {
                                    measurementTypesData.push(measurementType);
                                }
                            }
                        }

                        measurementTypesStore.loadData(measurementTypesData);

                        self.measurementTypesLoaded = true;

                        // 2 - load data IO's
                        var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');
                        
                        unconfiguredDataStore.load(function (unconfiguredRecords, operation, success) {
                            if (success) {
                                try {
                                    console.log('Application.loadUnconfiguredDataWithMeasurements (unconfigured data): ', unconfiguredRecords);
                                    console.log('Application.loadUnconfiguredDataWithMeasurements (unconfigured data) - operation: ', operation);

                                    if (unconfiguredRecords.length > 0) {
                                        var unconfiguredItems = unconfiguredRecords[0].data.unConfiguredDataIos;
                                        var unconfiguredItemsLength = unconfiguredItems.length;

                                        var unconfiguredItemsData = [];

                                        for (var u = 0; u < unconfiguredItemsLength; u++) {
                                            var unconfiguredItem = unconfiguredItems[u];

                                            var unconfiguredItemObject = {
                                                dataSourceId: unconfiguredItem.dataSourceId,
                                                sensorId: unconfiguredItem.sensorObjectId,
                                                sensorObjectName: unconfiguredItem.sensorObjectName,
                                                sensorObjectAlias: AGS3xIoTAdmin.util.util.getDeviceAlias(unconfiguredItem.sensorObjectId, unconfiguredItem.sensorObjectName),
                                                sensorObjectNodeId: unconfiguredItem.sensorObjectNodeId,
                                                sensorObjectNodeName: unconfiguredItem.name,
                                                dataSourceName: unconfiguredItem.dataSourceName,
                                                name: unconfiguredItem.name,
                                                description: unconfiguredItem.description,
                                                sensorObjectDescription: unconfiguredItem.sensorObjectDescription,
                                                dataId: unconfiguredItem.id,
                                                unit: unconfiguredItem.unit,
                                                configured: unconfiguredItem.configured,
                                                nodeType: unconfiguredItem.nodeType,
                                                nodeTypeString: AGS3xIoTAdmin.util.util.getMeasurementTypeNameFromId(unconfiguredItem.nodeType, measurementTypesData)
                                            };

                                            unconfiguredItemsData.push(unconfiguredItemObject);
                                        }

                                        unconfiguredDataStore.loadData(unconfiguredItemsData);
                                        console.log('Application.loadUnconfiguredDataWithMeasurements (unconfigured data) - unconfiguredDataStore, after: ', unconfiguredDataStore);

                                        self.unconfiguredDataLoaded = true;
                                        self.checkDataReadiness();
                                    }
                                }
                                catch (exception) {
                                    console.log('Application.loadUnconfiguredDataWithMeasurements (unconfigured data) - EXCEPTION: ', exception);
                                }
                            }
                            else {
                                console.log('Application.loadUnconfiguredDataWithMeasurements (unconfigured data) - failure, operation: ', operation);
                            }
                        });
                    }
                }
                catch (exception) {
                    console.log('Application.loadUnconfiguredDataWithMeasurements (measurements) - EXCEPTION: ', exception);
                }
            }
            else {
                console.log('Application.loadUnconfiguredDataWithMeasurements (measurements) - failure, operation: ', operation);
            }
        });
    },
    loadDashboards: function() {
        var self = this;

        var dashboardsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.dashboardsStore');

        dashboardsStore.load(function (records, operation, success) {
            if (success) {
                try {
                    console.log('Application.loadDashboards - records: ', records);
                    console.log('Application.loadDashboards - operation: ', operation);

                    if (records.length > 0) {

                        var records = records[0].data.dashboards;
                        var length = records.length;

                        var dataArray = [];

                        for (var i = 0; i < length; i++) {
                            var dashboardObject = records[i];
                            dashboardObject.dashboardId = dashboardObject.id;

                            dataArray.push(dashboardObject);
                        }

                        dashboardsStore.loadData(dataArray);

                        self.dashboardsLoaded = true;
                        self.checkDataReadiness();
                    }
                }
                catch (exception) {
                    console.log('Application.loadDashboards - EXCEPTION: ', exception);
                }
            }
            else {
                console.log('Application.loadDashboards - failure, operation: ', operation);
            }
        });
    },

    loadMeasurementTemplates: function() {
        var self = this;

        var measurementTemplatesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.measurementTemplatesStore');

        measurementTemplatesStore.load(function (records, operation, success) {
            if (success) {
                try {
                    console.log('Application.loadMeasurementTemplates - records: ', records);
                    console.log('Application.loadMeasurementTemplates - operation: ', operation);

                    if (records.length > 0) {

                        var records = records[0].data.measurementTemplates;
                        var length = records.length;

                        var dataArray = [];

                        for (var i = 0; i < length; i++) {
                            var measurementTemplateObject = records[i];

                            measurementTemplateObject.measurementGuid = measurementTemplateObject.id;

                            dataArray.push(measurementTemplateObject);
                        }

                        measurementTemplatesStore.loadData(dataArray);
                        measurementTemplatesStore.sort('measurementName', 'ASC')

                        self.measurementTemplatesLoaded = true;
                        self.checkDataReadiness();
                    }
                }
                catch (exception) {
                    console.log('Application.loadMeasurementTemplates - EXCEPTION: ', exception);
                }
            }
            else {
                console.log('Application.loadMeasurementTemplates - failure, operation: ', operation);
            }
        });

    },

    loadCalculationTypes: function() {
        var self = this;

        var calculationTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.calculationTypesStore');

        calculationTypesStore.load(function (records, operation, success) {
            if (success) {
                try {
                    console.log('Application.loadCalculationTypes - records: ', records);
                    console.log('Application.loadCalculationTypes - operation: ', operation);

                    if (records.length > 0) {

                        var records = records[0].data.calculationTypes;
                        var length = records.length;

                        var dataArray = [];

                        for (var i = 0; i < length; i++) {
                            var calculationTypeObject = records[i];

                            dataArray.push(calculationTypeObject);
                        }

                        calculationTypesStore.loadData(dataArray);
                        calculationTypesStore.sort('id', 'ASC');

                        self.calculationTypesLoaded = true;
                        self.checkDataReadiness();
                    }
                }
                catch (exception) {
                    console.log('Application.loadCalculationTypes - EXCEPTION: ', exception);
                }
            }
            else {
                console.log('Application.loadCalculationTypes - failure, operation: ', operation);
            }
        });
    },

    loadAggregationTypes: function () {
        var self = this;

        var aggregationTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.aggregationTypesStore');

        aggregationTypesStore.load(function (records, operation, success) {
            if (success) {
                try {
                    console.log('Application.loadAggregationTypes - records: ', records);
                    console.log('Application.loadAggregationTypes - operation: ', operation);

                    if (records.length > 0) {

                        var records = records[0].data.aggregationTypes;
                        var length = records.length;

                        var dataArray = [];

                        for (var i = 0; i < length; i++) {
                            var aggregationTypeObject = records[i];

                            dataArray.push(aggregationTypeObject);
                        }

                        aggregationTypesStore.loadData(dataArray);
                        aggregationTypesStore.sort('id', 'ASC');

                        self.aggregationTypesLoaded = true;
                        self.checkDataReadiness();
                    }
                }
                catch (exception) {
                    console.log('Application.loadAggregationTypes - EXCEPTION: ', exception);
                }
            }
            else {
                console.log('Application.loadAggregationTypes - failure, operation: ', operation);
            }
        });
    },

    loadComponentTypes: function() {
        var self = this;

        var componentTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.componentTypesStore');

        componentTypesStore.load(function (records, operation, success) {
            if (success) {
                try {
                    console.log('Application.loadComponentTypes - records: ', records);
                    console.log('Application.loadComponentTypes - operation: ', operation);

                    if (records.length > 0) {

                        var records = records[0].data.componentTypes;
                        var length = records.length;

                        var dataArray = [];

                        for (var i = 0; i < length; i++) {
                            var componentTypeObject = records[i];

                            dataArray.push(componentTypeObject);
                        }

                        componentTypesStore.loadData(dataArray);
                        componentTypesStore.sort('name', 'ASC');

                        self.componentTypesLoaded = true;
                        self.checkDataReadiness();
                    }
                }
                catch (exception) {
                    console.log('Application.loadComponentTypes - EXCEPTION: ', exception);
                }
            }
            else {
                console.log('Application.loadComponentTypes - failure, operation: ', operation);
            }
        });
    },

    loadConfiguredData: function () {
        var self = this;

        var configuredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.configuredDataStore');

        configuredDataStore.load(function (records, operation, success) {
            if (success) {
                try {
                    console.log('Application.loadConfiguredData - records: ', records);
                    console.log('Application.loadConfiguredData - operation: ', operation);

                    if (records.length > 0) {

                        var records = records[0].data.configurations;
                        var length = records.length;

                        var dataArray = [];

                        for (var i = 0; i < length; i++) {
                            var configuredDataObject = records[i];

                            dataArray.push(configuredDataObject);
                        }

                        configuredDataStore.loadData(dataArray);

                        self.configuredDataLoaded = true;
                        self.checkDataReadiness();
                    }
                }
                catch (exception) {
                    console.log('Application.loadConfiguredData - EXCEPTION: ', exception);
                    console.log('Application.loadConfiguredData - EXCEPTION, records: ', records);
                }
            }
            else {
                console.log('Application.loadConfiguredData - failure, operation: ', operation);
            }
        });

    },

    loadTemplateConfigurations: function() {
        var self = this;

        var templateConfigurationsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.templateConfigurationsStore');

        templateConfigurationsStore.load(function (records, operation, success) {
            if (success) {
                try {
                    console.log('Application.loadTemplateConfigurations - records: ', records);
                    console.log('Application.loadTemplateConfigurations - operation: ', operation);

                    if (records.length > 0) {

                        var records = records[0].data.configurationTemplates;
                        var length = records.length;

                        var dataArray = [];

                        for (var i = 0; i < length; i++) {
                            var configuredTemplateObject = records[i];

                            dataArray.push(configuredTemplateObject);
                        }

                        templateConfigurationsStore.loadData(dataArray);
                        templateConfigurationsStore.sort('measurementName');

                        self.templateConfigurationsLoaded = true;
                        self.checkDataReadiness();
                    }
                }
                catch (exception) {
                    console.log('Application.loadTemplateConfigurations - EXCEPTION: ', exception);
                }
            }
            else {
                console.log('Application.loadTemplateConfigurations - failure, operation: ', operation);
            }
        });
    },

    loadBoolTypes: function() {
        var self = this;

        var boolTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.boolTypesStore');

        var dataArray = [];

        dataArray.push({ type: true, name: self.boolTypeYes });
        dataArray.push({ type: false, name: self.boolTypeNo });

        boolTypesStore.loadData(dataArray);

        console.log('Application.loadBoolTypes - boolTypesStore: ', boolTypesStore);

        self.boolTypesLoaded = true;
        self.checkDataReadiness();
    },

    checkDataReadiness: function() {
        var self = this;

        var allDataLoaded = true;

        if (self.robotsLoaded == false) { allDataLoaded = false; }
        if (self.devicesLoaded == false) { allDataLoaded = false; }
        if (self.objectTypesLoaded == false) { allDataLoaded = false; }
        if (self.unconfiguredDataLoaded == false) { allDataLoaded = false; }
        if (self.measurementTypesLoaded == false) { allDataLoaded = false; }
        if (self.dashboardsLoaded == false) { allDataLoaded = false; }
        if (self.unitsLoaded == false) { allDataLoaded = false; }
        if (self.measurementTemplatesLoaded == false) { allDataLoaded = false; }
        if (self.calculationTypesLoaded == false) { allDataLoaded = false; }
        if (self.aggregationTypesLoaded == false) { allDataLoaded = false; }
        if (self.componentTypesLoaded == false) { allDataLoaded = false; }
        if (self.configuredDataLoaded == false) { allDataLoaded = false; }
        if (self.templateConfigurationsLoaded == false) { allDataLoaded = false; }
        if (self.boolTypesLoaded == false) { allDataLoaded = false; }

        console.log('Application.checkDataReadiness - robotsLoaded: ' + self.robotsLoaded);
        console.log('Application.checkDataReadiness - devicesLoaded: ' + self.devicesLoaded);
        console.log('Application.checkDataReadiness - objectTypesLoaded: ' + self.objectTypesLoaded);
        console.log('Application.checkDataReadiness - unconfiguredDataLoaded: ' + self.unconfiguredDataLoaded);
        console.log('Application.checkDataReadiness - measurementTypesLoaded: ' + self.measurementTypesLoaded);
        console.log('Application.checkDataReadiness - dashboardsLoaded: ' + self.dashboardsLoaded);
        console.log('Application.checkDataReadiness - unitsLoaded: ' + self.unitsLoaded);
        console.log('Application.checkDataReadiness - measurementTemplatesLoaded: ' + self.measurementTemplatesLoaded);
        console.log('Application.checkDataReadiness - calculationTypesLoaded: ' + self.calculationTypesLoaded);
        console.log('Application.checkDataReadiness - aggregationTypesLoaded: ' + self.aggregationTypesLoaded);
        console.log('Application.checkDataReadiness - componentTypesLoaded: ' + self.componentTypesLoaded);
        console.log('Application.checkDataReadiness - configuredDataLoaded: ' + self.configuredDataLoaded);
        console.log('Application.checkDataReadiness - templateConfigurationsLoaded: ' + self.templateConfigurationsLoaded);
        console.log('Application.checkDataReadiness - boolTypesLoaded: ' + self.boolTypesLoaded);

        if (allDataLoaded == true) {
            if (Ext.ComponentQuery.query('#maskInitData').length > 0) {
                Ext.ComponentQuery.query('#maskInitData')[0].destroy();
            }

            console.log('Application.checkDataReadiness - startPageCommand: ', self.startPageCommand);
            AGS3xIoTAdmin.util.events.fireEvent(self.startPageCommand, null);
            // AGS3xIoTAdmin.systemData
        }
    },
    init: function () {
        var self = this;

        console.log('Application.init - enabling/disabling console.log');

        var locationOrigin = window.location.origin;

        if (locationOrigin.indexOf('localhost') == -1) {
            Ext.Ajax.request({
                async: false,
                url: 'settings.json',
                method: 'GET',
                success: function (response) {
                    try {
                        var settings = Ext.decode(response.responseText);
                        
                        if (settings.debuggingActive == false) {
                            window['console']['log'] = function () { };
                        }
                    }
                    catch (exception) {
                        console.log('Application.init');
                    }
                }
            });
        }
        else {
            console.log('Application.init - running on localhost');
        }

    }
});
