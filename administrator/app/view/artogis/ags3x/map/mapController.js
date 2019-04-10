Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.map.mapController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xMap',

    requires: [
        'AGS3xIoTAdmin.util.events'
    ],

    // text content - default is Danish (da), for translations, see translation.js file
    fieldLabelMapInfoWindowOf: 'af',
    fieldLabelMapInfoWindowTitle: 'Objekter og komponenter',
    fieldLabelMapInfoWindowType: 'Type',
    fieldLabelMapInfoWindowName: 'Navn',
    fieldLabelMapInfoWindowDescription: 'Beskrivelse',
    fieldLabelMapInfoWindowId: 'ID',
    fieldLabelMapInfoWindowButtonClose: 'Luk',
    fieldLabelMapInfoWindowButtonAccept: 'Acceptér',
    fieldLabelMapInfoWindowTitleObject: 'Kortobjekt',
    fieldLabelMapInfoWindowTitleObjects: 'Kortobjekter',
    fieldLabelMapInfoWindowShowObjectComponents: 'Vis objektets komponenter',
    fieldLabelMapInfoWindowAcceptMoveComponent: 'Gennemfør flytning',
    fieldLabelMapInfoWindowCancelMoveComponent: 'Afbryd flytning',
    fieldLabelMapInfoWindowComponentsList: 'Komponentliste',
    fieldLabelMapInfoWindowDetachComponent: 'Afkobl komponent',
    fieldLabelMapInfoWindowMoveComponent: 'Flyt komponent',
    fieldLabelMapInfoWindowNewComponent: 'Opret komponent',
    fieldLabelMapInfoWindowNewCancel: 'Fortryd',
    fieldLabelMapInfoWindowNewSelectIO: 'Vælg Data IO\'ens komponent',
    fieldLabelMapInfoWindowNewRobotName: 'DataRobot',
    fieldLabelMapInfoWindowNewDeviceName: 'Målernavn',
    fieldLabelMapInfoWindowNewDeviceID: 'Måler ID',
    fieldLabelMapInfoWindowApply: 'Anvend',
    fieldLabelMapInfoWindowCancel: 'Fortryd',
    fieldLabelMapInfoWindowComponentType: 'Komponenttype',
    fieldLabelMapInfoWindowChooseComponentType: 'Vælg en komponenttype...',
    fieldLabelMapInfoWindowNewComponentName: 'Navn',
    fieldLabelMapInfoWindowNewComponentDescription: 'Beskrivelse',
    fieldLabelMapInfoWindowNewComponentCreate: 'Opret',
    fieldLabelMapInfoWindowNewComponentAccept: 'Opret ny komponent',

    textErrorMessageNoComponentName: 'Venligst indtast et beskrivende navn for komponenten',
    textErrorMessageNoComponentNameTitle: 'Fejl - Ny komponent',

    textMessageLoadingBaseMapLayer: 'mapController.createWMTSLayer - Henter grundkortlag. Vent venligst.',
    textMessageNoComponentsAttached: 'Ingen komponenter tilknyttet objektet',
    textMessageComponentDetachedSuccess: 'Komponent er blevet frakoblet objektet',

    fieldLabelNewComponentWindowTitle: 'Opret ny komponent fra måler',

    baseUrl: null, // fetched from settings.json
    map: null,
    overlay: null,
    markerOverlay: null,

    // Flags for movement process
    componentMovementActive: false,
    movingComponentType: null,
    movingComponentKey: null,
    movingComponentRelationId: null,

    // GeoExt
    formatWFS: null,

    // Default Danish projection: ESPG: 25832
    getProjection: function (proj) {
        proj4.defs(proj.name, proj.definition);
        return new ol.proj.Projection({
            code: proj.name,
            extent: [
                proj.envelope.minX,
                proj.envelope.minY,
                proj.envelope.maxX,
                proj.envelope.maxY
            ]
        });
    },

    showHideLayer: function (data) {
        console.log('mapController.showHideLayer - data: ', data);
        console.log('mapController.showHideLayer - layerId to find: ', data.layerId);

        var mapLayers = this.map.getLayers();

        mapLayers.forEach(function (layer) {
            var layerInfo = layer.get("layerInfo");

            if (layerInfo && layerInfo.layerId === data.layerId) {
                console.log('mapController.showHideLayer - HIT!');
                console.log('mapController.showHideLayer - layerInfo: ', layerInfo);
                layer.setVisible(data.visible);
            }
        }, this);
    },

    addLayers: function (data) {
        var self = this;

        console.log('mapController.addLayers - data: ', data);
        console.log('mapController.addLayers - url: ', data.layerUrl);
        console.log('mapController.addLayers - layerName: ', data.layerName);

        var objectType = AGS3xIoTAdmin.util.util.getObjectTypeFromLayerName(data.layerName);
        console.log('mapController.addLayers - objectType: ', objectType);
        console.log('mapController.addLayers - AGS3xIoTAdmin.systemData.showLayersOnStart (2): ', AGS3xIoTAdmin.systemData.showLayersOnStart);

        var wmsSource;

        wmsSource = new ol.source.TileWMS({
            url: data.layerUrl,
            isBackgroundLayer: false,
            params: {
                'LAYERS': data.layerName,
                buffer: 256
            }
        });

        var wmsLayer;

        wmsLayer = new ol.layer.Tile({
            layerInfo: data.layerInfo,
            source: wmsSource,
            zIndex: data.layerInfo.zIndex,
            layerName: data.layerName,
            layerUrl: data.layerUrl,
            scaleHintMin: (objectType != null && objectType.hasOwnProperty('scaleHintMin')) ? objectType.scaleHintMin : null,
            scaleHintMax: (objectType != null && objectType.hasOwnProperty('scaleHintMax')) ? objectType.scaleHintMax : null,

            visible: (AGS3xIoTAdmin.systemData.hasOwnProperty('showLayersOnStart') && AGS3xIoTAdmin.systemData.showLayersOnStart == false) ? false : true
        });

        self.map.addLayer(wmsLayer);
    },

    // Get and add base map layer
    createWMTSLayer: function (projection, layerInfo) {
        var self = this;
        console.log('mapController.createWMTSLayer - start...');
        console.log('mapController.createWMTSLayer - projection: ', projection);
        console.log('mapController.createWMTSLayer - layerInfo: ', layerInfo);

        var params = {};

        var layerName = '';

        Ext.getBody().mask(self.textMessageLoadingBaseMapLayer);

        self.getCapabilitiesXML(
            function (response) {

                var baseMapLayer = null;
                var parser = new ol.format.WMTSCapabilities();
                var result = parser.read(response.responseText);
                var mapConfig = AGS3xIoTAdmin.systemData.mapConfiguration;
                var mapServiceUrl = mapConfig.baseMapLayer.capabilitiesUrl;
                layerName = mapConfig.baseMapLayer.layerName;

                var _layers = [];
                for (var i = 0; i < result.Contents.Layer.length; i++) {
                    if (result.Contents.Layer[i].Identifier == layerName) {

                        params.layer = layerName;
                        console.log('mapController.createWMTSLayer - layerName: ', layerName);
                        params.projection = projection;

                        console.log('mapController.createWMTSLayer - result: ', result);
                        console.log('mapController.createWMTSLayer - params: ', params);

                        var options = ol.source.WMTS.optionsFromCapabilities(result, params);
                        console.log('mapController.createWMTSLayer - ol.source.WMTS: ', ol.source.WMTS);
                        console.log('mapController.createWMTSLayer - Options: ', options);
                        if (options && options.urls.length) {
                            options.urls[0] = mapServiceUrl;
                        }

                        if (options) {
                            baseMapLayer = new ol.layer.Tile({
                                opacity: 1,
                                source: new ol.source.WMTS(options),
                                layerInfo: layerInfo
                            })

                            console.log('mapController.createWMTSLayer - baseMapLayer: ', baseMapLayer);

                            self.map.getLayers().insertAt(0, baseMapLayer);

                            //force refreshing the map
                            self.map.updateSize();
                        }
                        Ext.getBody().unmask();
                        break;
                    }
                }
            },

            function (error) {
                Ext.getBody().unmask();
                console.log(error);
            }
        );
    },

    getCapabilitiesXML: function (successCallback, failureCallback) {
        var mapConfig = AGS3xIoTAdmin.systemData.mapConfiguration;
        var capabilitiesUrl = mapConfig.baseMapLayer.capabilitiesUrl;
        var endpointVersion = mapConfig.baseMapLayer.endpointVersion;
        var endpointType = mapConfig.baseMapLayer.endpointType;

        var firstParamSep = "?";
        if (capabilitiesUrl.indexOf('?') > -1) firstParamSep = '&';

        if (Ext.String.endsWith(capabilitiesUrl, '/', true)) capabilitiesUrl = capabilitiesUrl.substr(0, capabilitiesUrl.length - 1);

        capabilitiesUrl += firstParamSep + 'request=GetCapabilities&version=' + endpointVersion + '&service=' + endpointType;

        console.log('mapController.getCapabilitiesXML - capabilitiesUrl: ', capabilitiesUrl);

        Ext.Ajax.request({
            url: capabilitiesUrl,
            method: 'GET',
            cors: true,
            successCallback: successCallback,
            failureCallback: failureCallback,
            endpointType: endpointType,
            success: function (response, opts) {
                if (response.request.options.successCallback) {
                    console.log('mapController.getCapabilitiesXML - response: ', response);

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

                    console.log('mapController.getCapabilitiesXML - response XML: ', xmlResult);

                    response.request.options.successCallback(response, opts);
                }
            },
            failure: function (response) {
                if (response.request.options.failureCallback) {
                    response.request.options.failureCallback(response);
                }
            }
        });
    },

    getResolutionFromScale: function (map, scale) {
        var units = map.getView().getProjection().getUnits();
        var dpi = 25.4 / 0.28;
        var mpu = ol.proj.METERS_PER_UNIT[units];
        var resolution = scale / mpu / 39.37 / dpi;
        console.log('mapController.getResolutionFromScale - scale ' + scale + ' to ' + resolution);
        return parseFloat(resolution);
    },

    onAfterRenderer: function (ct, eOpts) {
        console.log('mapController.onAfterRenderer...');
        var mapConfig = AGS3xIoTAdmin.systemData.mapConfiguration;

        console.log('mapController.onAfterRenderer - mapConfig: ', mapConfig)

        var extent = mapConfig.initialExtent;
        var projectionName = mapConfig.projection.name;
        var definition = mapConfig.projection.definition;
        var envelope = mapConfig.projection.envelope;

        console.log('mapController.onAfterRenderer - extent: ', extent);
        console.log('mapController.onAfterRenderer - projectionName: ', projectionName);
        console.log('mapController.onAfterRenderer - definition: ', definition);
        console.log('mapController.onAfterRenderer - envelope: ', envelope)

        var projection = this.getProjection({ name: projectionName, definition: definition, envelope: envelope });

        console.log('mapController.onAfterRenderer - projection: ', projection);

        var scales = mapConfig.scaleList;
        var resolutions = [];
        for (var r = scales.length - 1; r >= 0; r--) {
            resolutions.push(this.getResolutionFromScale(this.map, scales[r]));
        }

        console.log('mapController.onAfterRenderer - scales: ', scales);

        this.map.setTarget(this.view.el.dom);
        this.map.updateSize();

        this.map.setView(new ol.View({
            center: ol.extent.getCenter(extent),
            projection: projection,
            extent: extent,
            resolutions: resolutions
        }));

        this.map.getView().fit(extent, this.map.getSize(), {});

        var layerId = AGS3xIoTAdmin.util.util.generateUUID();
        var layerInfo = { layerId: layerId, layerType: 'BasemapLayer', isBasemapLayer: true, tocName: null, keyDescriptions: null };

        this.createWMTSLayer(projection, layerInfo);
    },
    handleClickResultData: function (mapEvent, clickResultData) {
        var self = this;
        console.log('mapController.handleClickResultData - Handling click result - mapEvent: ', mapEvent);
        console.log('mapcontroller.handleClickResultData - Handling click result - data: ', clickResultData);
        console.log('mapcontroller.handleClickResultData - self.map: ', self.map);

        document.getElementById('componentsContent').style.display = 'none';
        document.getElementById('featureContent').style.display = 'block';

        if (clickResultData.length > 0) {

            var componentOrObjectMultipleStore = this.getViewModel().getStore('componentOrObjectMultipleStore');
            componentOrObjectMultipleStore.setData(clickResultData);
            console.log('mapcontroller.handleClickResultData - componentOrObjectMultipleStore: ', componentOrObjectMultipleStore);
        
            var size = self.map.getSize();
            var centerPoint = [(size[0] / 2) - 150, (size[1] / 2)];
            self.map.getView().centerOn(mapEvent.coordinate, size, centerPoint);

            for( var i = 0; i < clickResultData.length; i++ ) {
                var item = clickResultData[i];
                var tempId = 'N/A';
                if( item.objectKeys != null ) {
                    if (item.objectKeys[0].value != null ) {
                        tempId = item.objectKeys[0].value;
                    }
                }
                else {
                    if (item.componentKey != null) {
                        tempId = item.componentType + '-' + item.componentKey;
                    }
                }

                item.tempId = tempId;
            }

            if (Ext.ComponentQuery.query('#idMapFeatureWindow').length == 1) {
                Ext.ComponentQuery.query('#idMapFeatureWindow')[0].destroy();
            }

            if (Ext.ComponentQuery.query('#idMapFeatureWindow').length == 0) {

                var coordinate = mapEvent.coordinate;

                // Create and append the options
                var selectList = document.getElementById('idMapFeatureCombobox');
                selectList.innerHTML = '';

                var count = 0;

                for (var i = 0; i < clickResultData.length; i++) {
                    var option = document.createElement("option");
                    console.log('mapcontroller.handleClickResultData - option data: ', clickResultData[i]);
                    var type = (clickResultData[i].objectType != null) ? 'object' : 'component';
                    option.value = clickResultData[i].tempId;
                    option.text = clickResultData[i].featureDescription + ' (' + type + ')';

                    if (clickResultData[i].objectType != null) {
                        count++;
                        selectList.appendChild(option);
                    }
                }

                var featureCount = document.getElementById('featureCountSpan');
                var featureTotal = document.getElementById('featureTotalSpan');
                featureCount.innerHTML = '1';
                featureTotal.innerHTML = count;
                

                // Set initial selected data
                document.getElementById('ol-popup-table-1').innerHTML = clickResultData[0].type;
                document.getElementById('ol-popup-table-2').innerHTML = clickResultData[0].name;
                document.getElementById('ol-popup-table-3').innerHTML = clickResultData[0].featureDescription;
                document.getElementById('ol-popup-table-4').innerHTML = clickResultData[0].tempId;

                var componentOrObjectMultipleStore = self.getViewModel().getStore('componentOrObjectMultipleStore');
                console.log('mapcontroller.handleClickResultData - componentOrObjectMultipleStore: ', componentOrObjectMultipleStore);

                var selectedObjectStore = self.getViewModel().getStore('selectedObjectStore');
                var dataToStore = [{
                    type: clickResultData[0].type,
                    temp_id: clickResultData[0].tempId,
                    name: clickResultData[0].name,
                    description: clickResultData[0].featureDescription
                }];
                console.log('mapcontroller.handleClickResultData - dataToStore: ', dataToStore);

                selectedObjectStore.setData(dataToStore);

                console.log('mapcontroller.handleClickResultData - selectedObjectStore: ', selectedObjectStore);

                // if component movement is active, hide "show components" button
                if (self.componentMovementActive == true) {
                    document.getElementById('featureShowComponents').style.display = 'none';
                    document.getElementById('featureAccept').style.display = 'none';
                    document.getElementById('movementAccept').style.display = 'block';
                    document.getElementById('movementCancel').style.display = 'block';
                }
                else {
                    document.getElementById('featureShowComponents').style.display = 'block';
                    document.getElementById('featureAccept').style.display = 'block';
                    document.getElementById('movementAccept').style.display = 'none';
                    document.getElementById('movementCancel').style.display = 'none';
                }

                var firstObjectCoordinates = clickResultData[0].geometry.coordinates;

                self.overlay.setPosition(firstObjectCoordinates);
                var size = self.map.getSize();
                var centerPoint = [(size[0] / 2), (size[1] / 2)];
                self.map.getView().centerOn(firstObjectCoordinates, size, centerPoint);
  
            }
            
        }
        // no features
        else {
            console.log('mapController.handleClickResultData - no feature selected from click');
        }
    },
    
    openNewComponentWindow: function (objectId, objectTypeId, mapEvent) {
        console.log('mapController.openNewComponentWindow - objectId: ' + objectId + ', objectTypeId: ' + objectTypeId + ', mapEvent: ' + mapEvent);

        var mapWidth = mapEvent.b.target.i.clientWidth;
        var mapHeight = mapEvent.b.target.i.clientHeight;
        var width = 300;
        var height = 290;
        var x = mapEvent.pixel[0];
        var y = mapEvent.pixel[1];
        var left = ((mapWidth - width) / 2) + 90;
        var top = ((mapHeight - height) / 2) + 100;

        var componentTypesStore = this.getViewModel().getStore('componentTypesStore');

        // make parent window un-closable
        var parentWindowHeader = Ext.ComponentQuery.query('#idMapFeatureComponentsWindow')[0].getHeader();
        var headerElement = document.getElementById(parentWindowHeader.id);
        var closeElement = headerElement.getElementsByClassName('x-tool')[0];
        closeElement.style.display = 'none';

        Ext.create('Ext.panel.Panel', {
            renderTo: Ext.get('mapId'),
            id: 'idMapFeatureNewComponentsWindow',
            height: height,
            width: width,
            style: {
                'position': 'absolute',
                'top': top + 'px',
                'left': left + 'px',
                'box-shadow': 'rgba(0, 0, 0, 0.2) 0px 1px 2px 0px'
            },
            float: true,
            closable: true,
            header: {
                title: self.fieldLabelMapInfoWindowNewComponent,
                style: {
                    'box-shadow': 'none !important'
                }
            },
            listeners: {
                close: function () {
                    // make parent window un-closable
                    var parentWindowHeader = Ext.ComponentQuery.query('#idMapFeatureComponentsWindow')[0].getHeader();
                    var headerElement = document.getElementById(parentWindowHeader.id);
                    var closeElement = headerElement.getElementsByClassName('x-tool')[0];
                    closeElement.style.display = 'block';
                }
            },
            items: [
                {
                    xtype: 'combobox',
                    id: 'idMapFeatureNewComponentCombobox',
                    valueField: 'id',
                    displayField: 'name',
                    store: componentTypesStore,
                    editable: false,
                    queryMode: 'local',
                    emptyText: self.fieldLabelMapInfoWindowChooseComponentType,
                    width: 280,
                    margin: 10,
                    tpl: Ext.create('Ext.XTemplate',
                        '<ul class="x-list-plain"><tpl for=".">',
                            '<li role="option" class="x-boundlist-item">{name}</li>',
                        '</tpl></ul>'
                    ),
                    listeners: {
                        change: function (a, b, c, d) {
                        }
                    }
                },
                {
                    xtype: 'panel',
                    width: 280,
                    height: 165,
                    margin: 10,
                    items: [
                        {
                            xtype: 'textfield',
                            fieldLabel: self.fieldLabelMapInfoWindowNewComponentName,
                            name: 'name'
                        },
                        {
                            xtype: 'textarea',
                            fieldLabel: self.fieldLabelMapInfoWindowNewComponentDescription,
                            name: 'description'
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    items: [
                        {
                            xtype: 'button',
                            text: self.fieldLabelMapInfoWindowNewComponentCreate,
                            disabled: true,
                            margin: '0 10px 0 0',
                            style: {
                                'float': 'right'
                            }
                        }
                    ]
                }
            ]
        });

    },
    saveMapFeatureSelection: function (type, id, componentId, componentDescription) {
        var self = this;

        console.log('mapController.saveMapFeatureSelection - type: ', type);
        console.log('mapController.saveMapFeatureSelection - id: ', id);
        console.log('mapController.saveMapFeatureSelection - componentId: ', componentId);
        console.log('mapController.saveMapFeatureSelection - componentDescription: ', componentDescription);

        var idToSave = null;
        if( type === 'subcomponent' ) {
            idToSave = componentId + '-' + id;
            console.log('mapController.saveMapFeatureSelection - component, idToSave: ', idToSave);

            // 1 - get component type data
            Ext.Ajax.request({
                url: self.baseUrl + 'componenttype',
                method: 'GET',
                callback: function (options, success, response) {
                    if (success) {
                        var result = Ext.JSON.decode(response.responseText);
                        console.log('mapController.saveMapFeatureSelection - GET componenttypes, success, response: ', response);
                        console.log('mapController.saveMapFeatureSelection - GET componenttypes, success, result: ', result);

                        // 2 - combine with component data and save
                        var dataToSend = {};
                        var componentTypes = result.componentTypes;
                        for (var i = 0; i < componentTypes.length; i++ ) {
                            var componentType = componentTypes[i];
                            console.log('mapController.saveMapFeatureSelection - componentType: ', componentType);
                            if (componentType.type == componentId) {
                                console.log('mapController.saveMapFeatureSelection - component HIT - type: ', componentType.type);
                                dataToSend.type = 'ComponentType';
                                dataToSend.componentType = componentId;
                                dataToSend.componentKey = id;
                                dataToSend.tempId = idToSave;
                                dataToSend.name = componentType.name;
                                dataToSend.featureDescription = componentDescription

                                break;
                            }
                        }

                        console.log('mapController.saveMapFeatureSelection - dataToSend: ', dataToSend);

                        self.overlay.setPosition(undefined);

                        // send data to dataIOController.getObjectKeys
                        AGS3xIoTAdmin.util.events.fireEvent('getObjectKeys', dataToSend);
                    }
                },
                failure: function (response) {
                    console.log('mapController.saveMapFeatureSelection - GET componenttypes, failure, response: ', response);
                }
            });

            
        }
        else {
            idToSave = id;
            console.log('mapController.saveMapFeatureSelection - object, idToSave: ', idToSave);

            var componentOrObjectMultipleStore = self.getViewModel().getStore('componentOrObjectMultipleStore');
            console.log('mapController.saveMapFeatureSelection - componentOrObjectMultipleStore: ', componentOrObjectMultipleStore);

            var dataToSend;
            for (var i = 0; i < componentOrObjectMultipleStore.data.items.length; i++) {
                var item = componentOrObjectMultipleStore.data.items[i].data;
                console.log('Item: ', item);
                var tempId = item.tempId;
                if (tempId == idToSave) {
                    dataToSend = item;
                    break;
                }
            }

            console.log('mapController.saveMapFeatureSelection - dataToSend: ', dataToSend);
            AGS3xIoTAdmin.util.events.fireEvent('getObjectKeys', dataToSend);
        }

    },

    // GEOEXT TEST
    createNewComponent: function (deviceId, newComponentName) {
        var self = this;

        console.log('mapController.createNewComponent - deviceId: ', deviceId);

        var dataSourceId;
        var devicesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.devicesStore');
        console.log('mapController.createNewComponent - devicesStore: ', devicesStore);

        for (var i = 0; i < devicesStore.data.items.length; i++ ) {
            var device = devicesStore.data.items[i].data;
            console.log('mapController.createNewComponent - device: ', device);
            if (device.deviceId == deviceId) {
                dataSourceId = Number(device.dataSourceId);
                break;
            }
        }
        
        // get object item data
        var selectedObjectId = Number(document.getElementById('idMapFeatureCombobox').value);
        var componentOrObjectMultipleStore = self.getViewModel().getStore('componentOrObjectMultipleStore');
        console.log('mapcontroller.createNewComponent - componentOrObjectMultipleStore: ', componentOrObjectMultipleStore);

        var objectItem = null;

        for (var c = 0; c < componentOrObjectMultipleStore.data.items.length; c++ ) {
            var listObjectItem = componentOrObjectMultipleStore.data.items[c].data;
            if (listObjectItem.tempId == selectedObjectId) {
                objectItem = listObjectItem;
                break;
            }
        }

        console.log('mapcontroller.createNewComponent - objectItem: ', objectItem);

        var objectInfoUrl = objectItem.wfs + '?service=WFS&version=1.1.0&request=GetFeature&typeNames=' + objectItem.wfsLayer + '&featureID=' + objectItem.tempId;
        console.log('mapcontroller.createNewComponent - objectInfoUrl: ', objectInfoUrl);

        // Get object info from WFS
        Ext.Ajax.request({
            url: objectInfoUrl,
            method: 'GET',
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
                        console.log('mapController.createNewComponent - object WFS GET result: ', xmlResult);

                        var xCoordinate;
                        var yCoordinate;

                        // use gml:pos, if available
                        var posElement;
                        if (navigator.userAgent.indexOf('Edge') >= 0) {
                            posElement = xmlResult.getElementsByTagName('pos');
                        }
                        else {
                            posElement = xmlResult.getElementsByTagName('gml:pos');
                        }


                        if (posElement.length > 0) {
                            var position = posElement[0].childNodes[0].nodeValue
                            xCoordinate = position.split(' ')[0];
                            yCoordinate = position.split(' ')[1];
                        }
                        

                        console.log('mapController.createNewComponent - xkoordinat: ', xCoordinate);
                        console.log('mapController.createNewComponent - ykoordinat: ', yCoordinate);

                        var now = new Date();

                        var timeString =
                            now.getFullYear() + '-' +
                            ('0' + (now.getMonth() + 1)).slice(-2) + '-' +
                            ('0' + now.getDate()).slice(-2) + 'T' +
                            ('0' + now.getHours()).slice(-2) + ':' +
                            ('0' + now.getMinutes()).slice(-2) + ':' +
                            ('0' + now.getSeconds()).slice(-2) + 'Z' +
                            '+01';
                        console.log('mapController.createNewComponent - timeString: ', timeString);

                        // Create new component
                        if (xCoordinate != null && yCoordinate != null) {
                            var componentTypeId = Ext.ComponentQuery.query('#newComponentType')[0].lastSelection[0].data.type;
                            console.log('mapcontroller.createNewComponent - componentTypeId: ', componentTypeId);

                            // get component type data object
                            var componentTypeObject = AGS3xIoTAdmin.util.util.getComponentObject(componentTypeId, self.getViewModel());

                            console.log('mapController.createNewComponent - componentTypeObject: ', componentTypeObject);

                            var descriptTagName = componentTypeObject.fieldDescription;
                            var creationDataObject = {
                                component_type_id: componentTypeId,
                                createdtime: timeString, // TIMESTAMP WITH TIME ZONE '2004-10-19 10:23:54+01'
                                ogr_geometry: new ol.geom.Point([xCoordinate, yCoordinate]),
                                sensor_object_id: deviceId,
                                datasource_id: dataSourceId
                            }

                            // temp

                            creationDataObject[descriptTagName] = newComponentName;

                            var feature = new ol.Feature(creationDataObject);

                            console.log('mapController.createNewComponent - feature: ', feature);

                            var optionsFormatGML = new ol.format.GML({
                                featureNS: 'Hepwat',
                                featureType: componentTypeObject.componentTableName,
                                srsName: 'EPSG:25832'
                            });
                            console.log('mapController.createNewComponent - optionsFormatGML: ', optionsFormatGML);

                            var node = self.formatWFS.writeTransaction([feature], null, null, optionsFormatGML);
                            console.log('mapcontroller.createNewComponent - node: ', node);

                            var xmlNode = new XMLSerializer().serializeToString(node);
                            console.log('mapController.createNewComponent - xmlNode: ', xmlNode);

                            // get component type data
                            var transactionUrl = componentTypeObject.wfs + '?service=wfs&request=Transaction&version=1.1.0';
                            console.log('mapController.createNewComponent - transactionUrl: ', transactionUrl);
                            
                            Ext.Ajax.request({
                                url: transactionUrl,
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'text/xml'
                                },
                                xmlData: xmlNode,
                                callback: function (options, success, response) {
 
                                    if (success) {
                                        console.log('mapController.createNewComponent - transaction success response: ', response.responseText);
                                        var xmlCreationResult = null;
                                        if (window.DOMParser) {
                                            // code for modern browsers
                                            var parser = new DOMParser();
                                            xmlCreationResult = parser.parseFromString(response.responseText, 'text/xml');
                                        } else {
                                            // code for old IE browsers
                                            var xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
                                            xmlDoc.async = false;
                                            xmlCreationResult = xmlDoc.loadXML(response.responseText);
                                        }

                                        var featureIdElement;
                                        if (navigator.userAgent.indexOf('Edge') >= 0) {
                                            featureIdElement = xmlCreationResult.getElementsByTagName('FeatureId')[0];
                                        }
                                        else {
                                            featureIdElement = xmlCreationResult.getElementsByTagName('ogc:FeatureId')[0];
                                        }

                                        var newFeatureId = featureIdElement.getAttribute('fid').split('.').pop();

                                        var relationData = {
                                            'objectKeys': [
                                                {
                                                    'field': objectItem.objectKeys[0].field,
                                                    'type': 'integer',
                                                    'value': Number(objectItem.tempId)
                                                }
                                            ],
                                            'objectType': Number(objectItem.objectType),
                                            'componentType': Number(componentTypeId),
                                            'componentKey': Number(newFeatureId)
                                        }

                                        console.log('mapController.createNewComponent - componentOrObjectMultipleStore: ', componentOrObjectMultipleStore);

                                        // Create relation between new component and parent object
                                        Ext.Ajax.request({
                                            url: self.baseUrl + 'objectcomponentdatarelation',
                                            method: 'POST',
                                            jsonData: Ext.JSON.encode(relationData),
                                            callback: function (options, success, response) {
                                                if (success) {
                                                    var result = Ext.JSON.decode(response.responseText);
                                                    console.log('mapController.createNewComponent - relation setting success result: ', result);
                                                    self.loadObjectComponents(objectItem.tempId, objectItem.objectKeys[0].field, Number(objectItem.objectType));

                                                    document.getElementById('componentsContent').style.display = 'block';

                                                    var newComponentItem = {
                                                        componentKey: Number(newFeatureId),
                                                        componentType: componentTypeObject.type,
                                                        featureDescription: newComponentName,
                                                        name: componentTypeObject.name,
                                                        objectKeys: null,
                                                        objectType: null,
                                                        tempId: Number(componentTypeId) + '-' + Number(newFeatureId),
                                                        type: "ComponentType",
                                                        wfs: componentTypeObject.wfs,
                                                        wfsLayer: componentTypeObject.wfsLayer
                                                    }

                                                    console.log('mapController.createNewComponent - componentOrObjectMultipleStore, before: ', componentOrObjectMultipleStore);
                                                    componentOrObjectMultipleStore.add(newComponentItem);
                                                    console.log('mapController.createNewComponent - componentOrObjectMultipleStore, after: ', componentOrObjectMultipleStore);                                                        
                                                    console.log('mapController.createNewComponent - newComponentItem: ', newComponentItem);
                                                }
                                                else {
                                                    console.log('mapController.createNewComponent - relation setting failures response: ', response);
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        console.log('mapController.createNewComponent - transaction failure response: ', response.responseText);
                                    }
                                }
                            });

                        }

                    }
                    catch (ex) {
                        console.log(ex);
                    }
                }
            }
        });
 
    },
    loadObjectComponents: function (objectId, idField, objectType) {
        var self = this;

        var componentsTable = document.getElementById("componentsListTable");
        componentsTable.innerHTML = '';

        document.getElementById('acceptComponent').disabled = true;
        document.getElementById('detachComponent').disabled = true;

        var url = self.baseUrl + 'objectcomponentdatarelation/object/' + objectId + '?field=' + idField + '&fieldtype=integer&objecttype=' + objectType;

        console.log('mapController.loadObjectComponents - objectId: ', objectId);
        console.log('mapController.loadObjectComponents - url: ', url);
        
        Ext.Ajax.request({
            url: url,
            method: 'GET',
            callback: function (options, success, response) {
                if (success) {
                    var result = Ext.JSON.decode(response.responseText);
                    console.log('mapcontroller.loadObjectComponents - result: ', result);

                    var componentTypesList = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.componentTypesStore');
                    console.log('mapcontroller.loadObjectComponents - componentTypesList: ', componentTypesList);

                    // no components - add info to window box
                    if (result.objectComponentDataIoRelations.length == 0) {
                        console.log('mapcontroller.loadObjectComponents - no components - add info to window box');
                        var row = componentsTable.insertRow(0);
                        var cell = document.createElement('td');
                        cell.innerHTML = '<div style="text-align: center; color: #a0a0a0;">' + self.textMessageNoComponentsAttached + '</div>';
                        row.appendChild(cell);
                    }
                    // components attached - add to list
                    else {
                        for (var i = 0; i < result.objectComponentDataIoRelations.length; i++) {
                            var relationItem = result.objectComponentDataIoRelations[i];

                            if (relationItem.relationType == 2 && !relationItem.endTimeString) {
                                console.log('mapcontroller.loadObjectComponents - relationItem: ', relationItem);

                                for (var c = 0; c < componentTypesList.data.items.length; c++ ) {
                                    var componentTemplate = componentTypesList.data.items[c].data;

                                    if (componentTemplate.type == relationItem.componentType) {
                                        console.log('mapcontroller.loadObjectComponents - COMPONENT HIT, componentTemplate: ', componentTemplate);
                                        console.log('mapcontroller.loadObjectComponents - url: ', componentTemplate.wfs + '&featureId=' + relationItem.componentKey);

                                        var fieldDescriptionTagName = componentTemplate.fieldDescription;

                                        Ext.Ajax.request({
                                            url: componentTemplate.wfs + '?service=WFS&version=1.1.0&request=GetFeature&typeNames=' + componentTemplate .wfsLayer + '&featureId=' + relationItem.componentKey,
                                            relationId: relationItem.id,
                                            componentKey: relationItem.componentKey,
                                            componentType: relationItem.componentType,
                                            fieldDescriptionTagName: fieldDescriptionTagName,
                                            method: 'GET',
                                            callback: function (options, success, response) {
                                                if (success) {
                                                    console.log('mapController.loadObjectComponents - XML success response: ', response.responseText);
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


                                                    console.log('mapController.loadObjectComponents - fieldDescription tag name: ', options.fieldDescriptionTagName);

                                                    
                                                    var componentDescription;
                                                    var sensorId;
                                                    
                                                    var featureMembers = xmlResult.getElementsByTagName('*');

                                                    for (var f = 0; f < featureMembers.length; f++) {
                                                        var feature = featureMembers[f];
                                                        console.log('mapController.loadObjectComponents - component feature: ', feature);
                                                        
                                                        var tagName = (feature.tagName.indexOf(':') == -1) ? feature.tagName : feature.tagName.split(':')[1];
                                                        console.log('mapController.loadObjectComponents - component feature tag name: ', tagName);
                                                        if (tagName == options.fieldDescriptionTagName) {
                                                            console.log('mapController.loadObjectComponents - component feature, HIT');
                                                            console.log('mapController.loadObjectComponents - component nodeValue: ', feature.childNodes[0].nodeValue);
                                                            componentDescription = feature.childNodes[0].nodeValue;
                                                        }

                                                        if(tagName == 'sensor_object_id') {
                                                            sensorId = feature.childNodes[0].nodeValue;
                                                        }
                                                    }

                                                    console.log('mapController.loadObjectComponents single component - componentDescription: ', componentDescription);
                                                    console.log('mapController.loadObjectComponents single component - sensorId: ', sensorId);

                                                    var numberOfRows = document.getElementById("componentsListTable").rows.length;
                                                    console.log(document.getElementById("componentsListTable").rows.length);
                                                    var row = componentsTable.insertRow(numberOfRows);

                                                    row.setAttribute('data-componentkey', options.componentKey);
                                                    row.setAttribute('data-componenttype', options.componentType);
                                                    row.setAttribute('data-relationid', options.relationId);

                                                    row.addEventListener('click', function (event) {
                                                        console.log('mapController.loadObjectComponents - component clicked, event: ', event);
                                                        console.log('mapController.loadObjectComponents - parentNode: ', event.target.parentNode);

                                                        if (document.getElementsByClassName('component-selected').length > 0) {
                                                            document.getElementsByClassName('component-selected')[0].classList.remove('component-selected');
                                                        }

                                                        event.target.parentNode.classList.toggle('component-selected');

                                                        var dataIOSelection = Ext.ComponentQuery.query('#dataGridUnconfigured')[0].getSelection();
                                                        console.log('mapController.loadObjectComponents - component clicked, dataIOSelection: ', dataIOSelection);
                                                        
                                                        if (dataIOSelection.length == 0) {
                                                            document.getElementById('acceptComponent').disabled = false;
                                                        }
                                                        else {
                                                            if (dataIOSelection[0].data.configured == true) {
                                                                document.getElementById('acceptComponent').disabled = true;
                                                            }
                                                            else {
                                                                document.getElementById('acceptComponent').disabled = false;
                                                            }
                                                        }

                                                        document.getElementById('detachComponent').disabled = false;
                                                        document.getElementById('moveComponent').disabled = false;
                                                    });

                                                    var cell = document.createElement('td');
                                                    cell.innerHTML = componentDescription + ' - ' + sensorId;
                                                
                                                    row.appendChild(cell);

                                                    if (self.newComponentWindow) {
                                                        self.newComponentWindow.destroy();
                                                    }
                                                }
                                            }
                                        });

                                        break;
                                    }
                                }
                            
                            }
                        }
                        
                    }
                }
                else {
                    console.log('mapcontroller.loadObjectComponents - failures response: ', response);
                }
            }
        });
    },
    componentOrObjectSelected: function (value) {
        var self = this;

        console.log('mapController.componentOrObjectSelected - value: ', value);

        var componentOrObjectMultipleStore = self.getViewModel().getStore('componentOrObjectMultipleStore');
        console.log('mapController.componentOrObjectSelected - Select event, componentOrObjectMultipleStore: ', componentOrObjectMultipleStore);
        var optionsList = componentOrObjectMultipleStore.data.items;
        for (var i = 0; i < optionsList.length; i++ ) {
            var option = optionsList[i].data;
            if (value == option.tempId) {
                console.log('mapController.componentOrObjectSelected - HIT - option: ', option);
                console.log('mapController.componentOrObjectSelected - HIT - option index: ', i);
                document.getElementById('featureCountSpan').innerHTML = '' + (i + 1);

                // Set initial selected data
                document.getElementById('ol-popup-table-1').innerHTML = option.type;
                document.getElementById('ol-popup-table-2').innerHTML = option.name;
                document.getElementById('ol-popup-table-3').innerHTML = option.featureDescription;
                document.getElementById('ol-popup-table-4').innerHTML = option.tempId;

                // disable "new component" button, if first type is ComponentType
                if (option.type === 'ComponentType') {
                    document.getElementById('featureShowComponents').disabled = true;
                }
                else {
                    // save selected object
                    var componentOrObjectMultipleStore = self.getViewModel().getStore('componentOrObjectMultipleStore');
                    console.log('mapcontroller.componentOrObjectSelected - componentOrObjectMultipleStore: ', componentOrObjectMultipleStore);

                    var selectedObjectStore = this.getViewModel().getStore('selectedObjectStore');
                    var dataToStore = [{
                        type: option.type,
                        temp_id: option.tempId,
                        name: option.name,
                        description: option.featureDescription
                    }];
                    selectedObjectStore.setData(dataToStore);

                    console.log('mapcontroller.componentOrObjectSelected - selectedObjectStore: ', selectedObjectStore);

                    document.getElementById('featureShowComponents').disabled = false;

                    var objectCoordinates = option.geometry.coordinates;
                    self.overlay.setPosition(objectCoordinates);
                    var size = self.map.getSize();
                    var centerPoint = [(size[0] / 2), (size[1] / 2)];
                    self.map.getView().centerOn(objectCoordinates, size, centerPoint);

                    // if component list is visible
                    if (document.getElementById('componentsContent').style.display == 'block') {
                        console.log('mapcontroller.componentOrObjectSelected - componentsContent is active');

                        self.loadObjectComponents(option.objectKeys[0].value, option.objectKeys[0].field, option.objectType);
                    }
                }

                break;
            }
        }
    },
    detachComponent: function(relationId) {
        var self = this;

        console.log('mapController.detachComponent - relationId: ', relationId);

        Ext.Ajax.request({
            url: self.baseUrl + 'objectcomponentdatarelation/' + relationId,
            method: 'GET',
            callback: function (options, success, response) {
                if (success) {
                    var result = Ext.JSON.decode(response.responseText);
                    console.log('mapcontroller.detachComponent - get relation, success result: ', result);

                    var objectComponentDataIoRelation = result.objectComponentDataIoRelation;

                    var now = new Date();

                    var timeString =
                        ('0' + now.getDate()).slice(-2) + '-' +
                        ('0' + (now.getMonth() + 1)).slice(-2) + '-' +
                        now.getFullYear() + ' ' +
                        ('0' + now.getHours()).slice(-2) + ':' +
                        ('0' + now.getMinutes()).slice(-2) + ':' +
                        ('0' + now.getSeconds()).slice(-2)
                        ;
                    console.log('mapcontroller.detachComponent - timeString: ', timeString);

                    objectComponentDataIoRelation.endTimeString = timeString;
                    delete objectComponentDataIoRelation['createTimeString'];

                    console.log('mapcontroller.detachComponent - objectComponenetDataIoRelation to save: ', objectComponentDataIoRelation);

                    // detach component from object, meaning set end date
                    Ext.Ajax.request({
                        url: self.baseUrl + 'objectcomponentdatarelation',
                        method: 'PUT',
                        jsonData: Ext.JSON.encode(objectComponentDataIoRelation),
                        callback: function (options, success, response) {
                            if (success) {
                                var result = Ext.JSON.decode(response.responseText);
                                console.log('mapcontroller.detachComponent - detachment of component, success result: ', result);

                                // reset editor
                                var elementToRemove = document.querySelectorAll('[data-relationid="' + relationId + '"]')[0];
                                elementToRemove.parentNode.removeChild(elementToRemove);
                                document.getElementById('acceptComponent').disabled = true;
                                document.getElementById('detachComponent').disabled = true;
                                document.getElementById('moveComponent').disabled = true;

                                AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.textMessageComponentDetachedSuccess, 'INFO');
                            }
                            else {
                                console.log('mapcontroller.detachComponent - detachment of component, failure result: ', response);
                            }
                        }
                    });

                }
                else {
                    console.log('mapcontroller.detachComponent - get relation, failure result: ', response);
                }
            }
        });

    },
    startComponentMovement: function (componentType, componentKey, relationId) {
        var self = this;
        self.componentMovementActive = true;
        self.movingComponentType = componentType;
        self.movingComponentKey = componentKey;
        self.movingComponentRelationId = relationId;
        console.log('mapController.startComponentMovement - init movement of component; ' + self.movingComponentType + '-' + self.movingComponentKey + ', relation ID: ' + self.movingComponentRelationId);

        Ext.ComponentQuery.query('#buttonToolbarCancelComponentMove')[0].setHidden(false);

        self.overlay.setPosition(undefined);

    },
    performComponentMovement: function (objectId) {
        var self = this;
        console.log('mapController.performComponentMovement - performing movement of component ' + self.movingComponentType + '-' + self.movingComponentKey + ' to object ' + objectId);

        var tempMovingComponentType = self.movingComponentType;
        var tempComponentKey = self.movingComponentKey;
        var componentObject = AGS3xIoTAdmin.util.util.getComponentObject(tempMovingComponentType, null);

        // 1 - get existing relation
        Ext.Ajax.request({
            url: self.baseUrl + 'objectcomponentdatarelation/' + self.movingComponentRelationId,
            method: 'GET',
            callback: function (options, success, response) {
                if (success) {
                    var result = Ext.JSON.decode(response.responseText);
                    console.log('mapcontroller.performComponentMovement - GET, result: ', result);

                    var objectComponentDataIoRelation = result.objectComponentDataIoRelation;

                    var now = new Date();

                    var timeString =
                        ('0' + now.getDate()).slice(-2) + '-' +
                        ('0' + (now.getMonth() + 1)).slice(-2) + '-' +
                        now.getFullYear() + ' ' +
                        ('0' + now.getHours()).slice(-2) + ':' +
                        ('0' + now.getMinutes()).slice(-2) + ':' +
                        ('0' + now.getSeconds()).slice(-2)

                    if (objectComponentDataIoRelation.createTimeString) {
                        delete objectComponentDataIoRelation.createTimeString;
                    }
                    objectComponentDataIoRelation.endTimeString = timeString;

                    console.log('mapcontroller.performComponentMovement - objectComponentDataIoRelation with end time: ', objectComponentDataIoRelation);

                    // 2 - set end time for old relation
                    Ext.Ajax.request({
                        url: self.baseUrl + 'objectcomponentdatarelation',
                        method: 'PUT',
                        jsonData: Ext.JSON.encode(objectComponentDataIoRelation),
                        callback: function (options, success, response) {
                            if (success) {
                                var result = Ext.JSON.decode(response.responseText);
                                console.log('mapcontroller.performComponentMovement - set endtime for old relation, result: ', result);

                                // 3 - create new relation
                                var selectedObjectStore = self.getViewModel().getStore('selectedObjectStore');
                                console.log('mapcontroller.performComponentMovement - selectedObjectStore: ', selectedObjectStore);
                                var objectData = selectedObjectStore.data.items[0].data;
                                console.log('mapcontroller.performComponentMovement - objectData: ', objectData);

                                console.log('mapcontroller.performComponentMovement - componentMovementActive: ', self.componentMovementActive);
                                console.log('mapcontroller.performComponentMovement - movingComponentType: ', self.movingComponentType);
                                console.log('mapcontroller.performComponentMovement - movingComponentKey: ', self.movingComponentKey);
                                console.log('mapcontroller.performComponentMovement - movingComponentRelationId: ', self.movingComponentRelationId);

                                var newComponentRelationData = {}

                                var componentOrObjectMultipleStore = self.getViewModel().getStore('componentOrObjectMultipleStore');
                                for (var i = 0; i < componentOrObjectMultipleStore.data.items.length; i++ ) {
                                    var objectItem = componentOrObjectMultipleStore.data.items[i].data;
                                    var objectItemKeys = objectItem.objectKeys[0];

                                    if (Number(objectItem.tempId) == Number(objectId)) {
                                        newComponentRelationData = {
                                            "objectKeys": [
                                                {
                                                    "field": objectItem.objectKeys[0].field,
                                                    "type": "integer",
                                                    "value": Number(objectItem.tempId)
                                            }
                                            ],
                                            "objectType": Number(objectItem.objectType),
                                            "componentType": Number(self.movingComponentType),
                                            "componentKey": Number(self.movingComponentKey)
                                        }

                                        console.log('mapcontroller.performComponentMovement - HIT - data: ', newComponentRelationData);

                                        break;
                                    }
                                }

                                // create data relation
                                Ext.Ajax.request({
                                    url: self.baseUrl + 'objectcomponentdatarelation',
                                    method: 'POST',
                                    jsonData: Ext.JSON.encode(newComponentRelationData),
                                    callback: function (options, success, response) {
                                        if (success) {
                                            var result = Ext.JSON.decode(response.responseText);
                                            console.log('mapcontroller.performComponentMovement - create new relation, result: ', result);

                                            self.componentMovementActive = false;
                                            self.movingComponentType = null;
                                            self.movingComponentKey = null;
                                            self.movingComponentRelationId = null;

                                            document.getElementById('moveComponent').disabled = true;
                                            Ext.ComponentQuery.query('#buttonToolbarCancelComponentMove')[0].setHidden(true);

                                            var componentOrObjectMultipleStore = self.getViewModel().getStore('componentOrObjectMultipleStore');
                                            console.log('mapController.performComponentMovement - componentOrObjectMultipleStore: ', componentOrObjectMultipleStore);

                                            var targetObjectItem;

                                            for (var i = 0; i < componentOrObjectMultipleStore.data.items.length; i++) {
                                                var objectItem = componentOrObjectMultipleStore.data.items[i].data;
                                                var objectItemKeys = objectItem.objectKeys[0];

                                                if (Number(objectItem.tempId) == Number(objectId)) {
                                                    targetObjectItem = objectItem;
                                                    break;
                                                }
                                            }

                                            console.log('mapController.performComponentMovement - objectId: ', objectId);
                                            console.log('mapController.performComponentMovement - targetObjectItem: ', targetObjectItem);
                                            console.log('mapController.performComponentMovement - tempMovingComponentType: ', tempMovingComponentType);
                                            console.log('mapController.performComponentMovement - tempComponentKey: ', tempComponentKey);
                                            console.log('mapController.performComponentMovement - componentObject: ', componentObject);

                                            var objectInfoUrl = targetObjectItem.wfs + '?service=WFS&version=1.1.0&request=GetFeature&typeNames=' + targetObjectItem.wfsLayer + '&featureID=' + targetObjectItem.tempId;

                                            Ext.Ajax.request({
                                                url: objectInfoUrl,
                                                method: 'GET',
                                                headers: {
                                                    'Content-Type': 'text/xml'
                                                },
                                                callback: function (options, success, response) {

                                                    if (success) {
                                                        console.log('mapController.performComponentMovement - object XML info success response: ', response.responseText);
                                                        var xmlInfoResult = null;
                                                        if (window.DOMParser) {
                                                            // code for modern browsers
                                                            var parser = new DOMParser();
                                                            xmlInfoResult = parser.parseFromString(response.responseText, 'text/xml');
                                                        } else {
                                                            // code for old IE browsers
                                                            var xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
                                                            xmlDoc.async = false;
                                                            xmlInfoResult = xmlDoc.loadXML(response.responseText);
                                                        }

                                                        console.log('mapController.performComponentMovement - object XML xmlInfoResult: ', xmlInfoResult);

                                                        var featureMembers = xmlInfoResult.getElementsByTagName('*');

                                                        var objectPosition;

                                                        for (var f = 0; f < featureMembers.length; f++) {
                                                            var feature = featureMembers[f];
                                                            console.log('mapController.performComponentMovement - object feature: ', feature);

                                                            var tagName = (feature.tagName.indexOf(':') == -1) ? feature.tagName : feature.tagName.split(':')[1];
                                                            console.log('mapController.performComponentMovement - object feature tag name: ', tagName);

                                                            if (tagName == 'pos') {
                                                                console.log('mapController.performComponentMovement - object gml:pos, HIT');
                                                                console.log('mapController.performComponentMovement - gml:pos value: ', feature.childNodes[0].nodeValue);
                                                                objectPosition = feature.childNodes[0].nodeValue;

                                                                break;
                                                            }
                                                        }

                                                        console.log('mapController.performComponentMovement - objectPosition: ', objectPosition);

                                                        var transactionUrl = componentObject.wfs + '?service=wfs&request=Transaction&version=1.1.0';
                                                        console.log('mapController.performComponentMovement - Update transactionUrl: ', transactionUrl);

                                                        var updateDataDoc =
                                                            '<?xml version="1.0" ?>' +
                                                            '<Transaction ' +
                                                                'version="1.1.0" ' +
                                                                'service="WFS" ' +
                                                                'xmlns:ogc="http://www.opengis.net/ogc" ' +
                                                                'xmlns:gml="http://www.opengis.net/gml" ' +
                                                                'xmlns:wfs="http://www.opengis.net/wfs" ' +
                                                            '>' +
                                                                '<Update typeName="' + componentObject.wfsLayer + '">' +
                                                                    '<Property>' +
                                                                        '<Name>ogr_geometry</Name>' +
                                                                        '<Value>' +
                                                                            '<gml:Point xmlns="http://www.opengis.net/gml" srsName="EPSG:25832">' +
                                                                                '<gml:pos>' + objectPosition + '</gml:pos>' +
                                                                            '</gml:Point>' +
                                                                        '</Value>' +
                                                                    '</Property>' +
                                                                    '<Filter>' +
                                                                        '<FeatureId fid="' + componentObject.componentTableName + '.' + tempComponentKey + '" />' +
                                                                    '</Filter>' +
                                                                '</Update>' +
                                                            '</Transaction>';

                                                        Ext.Ajax.request({
                                                            url: transactionUrl,
                                                            method: 'POST',
                                                            headers: {
                                                                'Content-Type': 'text/xml'
                                                            },
                                                            xmlData: updateDataDoc,
                                                            callback: function (options, success, response) {

                                                                if (success) {
                                                                    console.log('mapController.performComponentMovement - transaction success response: ', response.responseText);
                                                                    var xmlCreationResult = null;
                                                                    if (window.DOMParser) {
                                                                        // code for modern browsers
                                                                        var parser = new DOMParser();
                                                                        xmlCreationResult = parser.parseFromString(response.responseText, 'text/xml');
                                                                    } else {
                                                                        // code for old IE browsers
                                                                        var xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
                                                                        xmlDoc.async = false;
                                                                        xmlCreationResult = xmlDoc.loadXML(response.responseText);
                                                                    }

                                                                }
                                                            }
                                                        }); // transaction
                                                        
                                                    }
                                                }
                                            }); // objectInfo

                                        }
                                    }
                                }); // objectcomponentdatarelation - POST

                            }
                        }
                    }); // objectcomponentdatarelation - PUT
                }
            }
        });

        self.overlay.setPosition(undefined);

    },
    cancelComponentMovement: function () {
        var self = this;

        // deactivate movement process
        self.componentMovementActive = false;
        self.movingComponentType = null;
        self.movingComponentKey = null;
        self.movingComponentRelationId = null;

        document.getElementById('moveComponent').disabled = true;
        Ext.ComponentQuery.query('#buttonToolbarCancelComponentMove')[0].setHidden(true);

        self.overlay.setPosition(undefined);
    },
    selectDataIODeviceId: function () {
        var self = this;
        console.log('mapController.selectDataIODeviceId');

        var selectionSensorId = Ext.ComponentQuery.query('#dataGridUnconfigured')[0].getSelection()[0].data.sensorId;
        console.log('mapController.selectDataIODeviceId - selectionSensorId: ', selectionSensorId);

        // get the sensor object with selectionSensorId
        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'sensorobject',
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dataIOController.loadDevicesList - result: ', result);

                        var sensorObjectStoreItems = result.sensorObjects;

                        for (var i = 0; i < sensorObjectStoreItems.length; i++) {
                            var sensorObject = sensorObjectStoreItems[i];
                            console.log('mapController.selectDataIODeviceId - sensorObject: ', sensorObject);
                            elementSensorId = sensorObject.id;

                            console.log('mapController.selectDataIODeviceId - elementSensorId: ', elementSensorId + ' vs. ' + selectionSensorId);

                            if (elementSensorId == selectionSensorId) {
                                console.log('mapController.selectDataIODeviceId - HIT');

                                if (document.getElementsByClassName('devices-row x-grid-item-selected').length > 0) {
                                    document.getElementsByClassName('devices-row x-grid-item-selected')[0].classList.remove('x-grid-item-selected');
                                }

                                var devicesRows = document.getElementsByClassName('devices-row');
                                console.log('mapController.selectDataIODeviceId - devicesRows length: ', devicesRows.length);

                                self.createNewComponent(sensorObject.id, sensorObject.name);

                                break;
                            }
                        }
                    }
                    catch (exception) {
                        console.log('mapController.selectDataIODeviceId - exception: ', exception);
                    }
                }
            }
        });

    },
    centerMapOnCustomLocation: function (controller, coordinate) {
        console.log('mapController.centerMapOnCustomLocation - coordinate: ', coordinate);
        var self = this;

        
        if (document.getElementById('marker')) {
            var existingElement = document.getElementById('marker');
            existingElement.parentNode.removeChild(existingElement);
        }

        
        if (coordinate) {
            var size = self.map.getSize();
            //var centerPoint = [(size[0] / 2) - 150, (size[1] / 2)];
            var centerPoint = [(size[0] / 2), (size[1] / 2)];
            self.map.getView().centerOn(coordinate, size, centerPoint);

            var pos = new ol.proj.fromLonLat(coordinate);

            var markerElement = document.createElement('div');
            markerElement.setAttribute("id", "marker");
            //markerElement.setAttribute("style", "width: 40px;height: 40px;border: 1px solid #000000;border-radius: 20px;background-color: #FFFF00;opacity: 0.5;");
            markerElement.setAttribute("style", "width: 24px;height: 35px;background: url('./resources/artogis/ags3x/images/icons/location_marker_24.png') no-repeat;");

            // marker
            self.markerOverlay = new ol.Overlay({
                id: 'markerOverlay',
                position: coordinate,
                positioning: 'center-center',
                offset: [0,-16],
                element: markerElement,
                stopEvent: false
            });
            console.log('mapController.centerMapOnCustomLocation - marker: ', self.markerOverlay);
            self.map.addOverlay(self.markerOverlay);
        }
        
    },
    listen: {
        controller: {
            'ags3xDataIOConfig': {
                setLocationEvent: 'centerMapOnCustomLocation',
                cancelComponentMovement: 'cancelComponentMovement'
            }

        }
    },

    /**
    * Called when the view is created
    */
    init: function () {
        console.log('mapController.init - start...');
        this.map = this.view.getMap();
        var self = this;

        self.baseUrl = AGS3xIoTAdmin.systemData.serviceUrl;
        self.buffer = AGS3xIoTAdmin.systemData.mapConfiguration.buffer;
        self.showBufferCircle = AGS3xIoTAdmin.systemData.mapConfiguration.showBufferCircle;
        console.log('mapController.init - buffer: ', self.buffer);

        // TEST GEOEXT
        self.formatWFS = new ol.format.WFS();

        AGS3xIoTAdmin.map = this.map;

        AGS3xIoTAdmin.util.events.on('addLayers', this.addLayers, this);
        AGS3xIoTAdmin.util.events.on('showHideLayer', this.showHideLayer, this);

        var mapLayers = this.map.getLayers();

        console.log('mapController.init - mapLayers: ', mapLayers);

        // ADD OVERLAY FOR POPUP
        var popupElement = document.createElement('div');
        popupElement.setAttribute('id', 'ol-popup');
        popupElement.innerHTML =
            '<div class="ol-popup" style="display: block;">' +
                '<a class="ol-popup-closer" href="#"></a>' +
                '<h4 class="ol-popup-layerName">' + self.fieldLabelMapInfoWindowTitle + '</h4>' +
                '<table style="width:100%;">' +
                    '<tr>' +
                        '<td style="width:300px">' +
                            '<select id="idMapFeatureCombobox" style="width:100%;margin-left:-3px;"></select>' +
                         '</td>' +
                         '<td style="text-align:right;">' +
                            '<a id="prevRecord" class="ol-popup-prevRecord" href="#"></a>' +
                         '</td>' +
                         '<td style="text-align:right;">' +
                            '<a id="nextRecord" class="ol-popup-nextRecord" href="#"></a>' +
                         '</td>' +
                      '</tr>' +
                '</table>' +
                '<h4 class="ol-popup-featureCount"><span id="featureCountSpan">0</span> ' + self.fieldLabelMapInfoWindowOf + ' <span id="featureTotalSpan">0</span></h4>' + 
                '<hr>' +
                '<div class="ol-popup-content" style="max-height:100%;">' +
                    '<div id="featureContent">' +
                        '<table id="featureAttributesTable" class="ol-popup-table">' +
                            '<tr>' +
                                '<td>' + self.fieldLabelMapInfoWindowType  + '</td>' +
                                '<td class="ol-popup-table-td" id="ol-popup-table-1"></td>' +
                             '</tr>' +
                             '<tr>' +
                                '<td>' + self.fieldLabelMapInfoWindowName  + '</td>' +
                                '<td class="ol-popup-table-td" id="ol-popup-table-2"></td>' +
                             '</tr>' +
                             '<tr>' +
                                '<td>' + self.fieldLabelMapInfoWindowDescription  + '</td>' +
                                '<td class="ol-popup-table-td" id="ol-popup-table-3"></td>' +
                             '</tr>' +
                             '<tr>' +
                                '<td>' + self.fieldLabelMapInfoWindowId + '</td>' +
                                '<td class="ol-popup-table-td" id="ol-popup-table-4"></td>' +
                             '</tr>' +
                        '</table>' +
                        '<div style="margin-top: 10px;">' +
                            '<button id="featureAccept" class="custom-button float-right margin-left-5">' + self.fieldLabelMapInfoWindowApply + '</button>' +
                            '<button id="featureShowComponents" class="custom-button float-right margin-left-5">' + self.fieldLabelMapInfoWindowShowObjectComponents + '</button>' + 
                            '<button id="movementAccept" class="custom-button float-right margin-left-5">' + self.fieldLabelMapInfoWindowAcceptMoveComponent  + '</button>' +
                            '<button id="movementCancel" class="custom-button float-right margin-left-5">' + self.fieldLabelMapInfoWindowCancelMoveComponent  + '</button>' +
                        '</div>' +
                    '</div>' +

                    '<div id="componentsContent" style="display:none;">' +
                        '<div id="componentsList" style="height:100px;width:100%;overflow-y:scroll;overflow-x:hidden;">' +
                            '<table id="componentsListTable" class="ol-popup-table"></table>' +
                        '</div>' +

                        '<div style="width:100%;height:24px;margin-top: 10px;">' +

                            '<button id="detachComponent" class="custom-button float-right margin-left-5" disabled>' + self.fieldLabelMapInfoWindowDetachComponent + '</button>' +
                            '<button id="moveComponent" class="custom-button float-right margin-left-5" disabled>' + self.fieldLabelMapInfoWindowMoveComponent + '</button>' +
                            
                        '</div>' +

                        '<div style="width:100%;height:24px;margin-top: 10px;">' +

                            '<button id="acceptComponent" class="custom-button float-right margin-left-5" disabled>' + self.fieldLabelMapInfoWindowApply + '</button>' +
                            '<button id="cancelComponent" class="custom-button float-right margin-left-5">' + self.fieldLabelMapInfoWindowCancel + '</button>' +
                            '<button id="newComponent" class="custom-button float-right margin-left-5">' + self.fieldLabelMapInfoWindowNewComponent + '</button>' +

                        '</div>' +
                    '</div>' +
                '</div>' +
            '</div>';

        document.body.appendChild(popupElement);

        
        // next record
        document.getElementById('nextRecord').addEventListener('click', function (event) {
            var count = Number(document.getElementById('featureCountSpan').innerHTML);
            var total = Number(document.getElementById('featureTotalSpan').innerHTML);

            if (count == total) {
                count = 1;
            }
            else {
                count++;
            }

            console.log('Next - count: ', (count - 1));
            var select = document.getElementById('idMapFeatureCombobox');

            var optionsList = select.getElementsByTagName('option');
            for (var i = 0; i < optionsList.length; i++ ) {
                var option = optionsList[i];
                option.removeAttribute('selected');
                console.log('option: ', option);
            }

            select.getElementsByTagName('option')[count - 1].setAttribute('selected', 'selected');

            document.getElementById('featureCountSpan').innerHTML = count;

            document.getElementById('moveComponent').disabled = true;
            document.getElementById('detachComponent').disabled = true;

            self.componentOrObjectSelected(select.getElementsByTagName('option')[count - 1].value);


        });

        // previous record
        document.getElementById('prevRecord').addEventListener('click', function (event) {
            var count = Number(document.getElementById('featureCountSpan').innerHTML);
            var total = Number(document.getElementById('featureTotalSpan').innerHTML);

            if (count == 1) {
                count = total;
            }
            else {
                count--;
            }

            console.log('Prev - count: ', (count - 1));
            var select = document.getElementById('idMapFeatureCombobox');

            var optionsList = select.getElementsByTagName('option');
            for (var i = 0; i < optionsList.length; i++) {
                var option = optionsList[i];
                option.removeAttribute('selected');
                console.log('option: ', option);
            }

            select.getElementsByTagName('option')[count - 1].setAttribute('selected', 'selected');

            document.getElementById('featureCountSpan').innerHTML = count;

            document.getElementById('moveComponent').disabled = true;
            document.getElementById('detachComponent').disabled = true;

            self.componentOrObjectSelected(select.getElementsByTagName('option')[count - 1].value);

        });

        // accept selected object or component as the final input for Data IO config
        document.getElementById('featureAccept').addEventListener('click', function (event) {
            console.log('Map click event: ', event);
            var value = document.getElementById('idMapFeatureCombobox').value; // ??
            self.saveMapFeatureSelection('feature', value, null, null);
            self.overlay.setPosition(undefined);
        });

        // get list of components connected to object
        document.getElementById('featureShowComponents').addEventListener('click', function (event) {
            console.log('featureShowComponents - click event: ', event);
            console.log('featureShowComponents - click event - value from select: ', document.getElementById('idMapFeatureCombobox').value);

            var activeObjectIdValue = Number(document.getElementById('idMapFeatureCombobox').value);

            document.getElementById('featureContent').style.display = 'none';
            document.getElementById('componentsContent').style.display = 'block';

            // if object does not have both an ID and a WFS link, don't activate the "create new component" button
            var componentOrObjectMultipleStore = self.getViewModel().getStore('componentOrObjectMultipleStore');
            console.log('featureShowComponents - componentOrObjectMultipleStore: ', componentOrObjectMultipleStore);

            for (var i = 0; i < componentOrObjectMultipleStore.data.items.length; i++ ) {
                var item = componentOrObjectMultipleStore.data.items[i].data;
                if (Number(item.tempId) == activeObjectIdValue) {
                    console.log('HIT - item: ', item);
                    console.log('featureShowComponents - componentOrObjectMultipleStore.data.items[0].data: ', componentOrObjectMultipleStore.data.items[0].data);

                    // objectId, idField, objectType
                    self.loadObjectComponents(activeObjectIdValue, item.objectKeys[0].field, item.objectType);
                    break;
                }
            }
        });

        // accept movement of component
        document.getElementById('movementAccept').addEventListener('click', function (event) {
            console.log('Map click event - movementAccept: ', event);
            var value = document.getElementById('idMapFeatureCombobox').value; // ??
            self.performComponentMovement(value);
        });

        // cancel movement of component
        document.getElementById('movementCancel').addEventListener('click', function (event) {
            console.log('Map click event - movementCancel: ', event);
            self.cancelComponentMovement();
        });

        // change selected object or component
        document.getElementById('idMapFeatureCombobox').addEventListener('change', function (event) {
            console.log('Select event: ', event);

            self.componentOrObjectSelected(event.target.value);
            
        });

        // start creation of new component (connected to the specific selected object)
        document.getElementById('newComponent').addEventListener('click', function (event) {
            console.log('Map click event - newComponent: ', event);

            var devicesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.devicesStore');
            var devicesStoreLength = devicesStore.data.items.length;
            for (var i = 0; i < devicesStoreLength; i++) {
                var deviceItem = devicesStore.data.items[i].data;
                deviceItem.dataSourceName = AGS3xIoTAdmin.util.util.getDataSourceNameFromId(deviceItem.dataSourceId);
            }

            console.log('Map click event - newComponent, devicesStore: ', devicesStore);

            if( Ext.ComponentQuery.query('#existingDevicesGridPanel').length > 0 ) {
                Ext.ComponentQuery.query('#existingDevicesGridPanel')[0].destroy();
            }

            if (document.getElementsByClassName('devices-row x-grid-item-selected').length > 0) {
                document.getElementsByClassName('devices-row x-grid-item-selected')[0].classList.remove('x-grid-item-selected');
            }

            var dataIOSelection = Ext.ComponentQuery.query('#dataGridUnconfigured')[0].getSelection();

            self.newComponentWindow = new Ext.Window({
                id: 'newComponentWindow',
                header: {
                    title: self.fieldLabelNewComponentWindowTitle,
                    style: {
                        'text-align': 'center',
                        'border': '0 !important',
                        'border-radius': '0px !important',
                        'background': '#ffffff !important'
                    }
                },
                closable: true,
                width: 800,
                height: 700,
                x: ((window.innerWidth - 800) / 2),
                y: 100,
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
                items: [
                    {
                        xtype: 'panel',
                        id: 'newComponentToolBarHolder',
                        items: [
                            {
                                xtype: 'combobox',
                                id: 'newComponentType',
                                valueField: 'type',
                                displayField: 'name',
                                store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.componentTypesStore'),
                                editable: false,
                                queryMode: 'local',
                                emptyText: self.fieldLabelMapInfoWindowChooseComponentType,
                                width: 280,
                                margin: 10,
                                tpl: Ext.create('Ext.XTemplate',
                                    '<ul class="x-list-plain"><tpl for=".">',
                                        '<li role="option" class="x-boundlist-item">{name}</li>',
                                    '</tpl></ul>'
                                ),
                                style: {
                                    'float': 'left'
                                },
                                listeners: {
                                    change: function (component, b, c, d) {

                                        console.log('mapController.newComponent - change, component: ', component);

                                        var selection = Ext.ComponentQuery.query('#existingDevicesGridPanel')[0].getSelection();

                                        if (selection.length > 0) {
                                            Ext.ComponentQuery.query('#acceptNewComponent')[0].setDisabled(false);
                                        }
                                        else {
                                            Ext.ComponentQuery.query('#acceptNewComponent')[0].setDisabled(true);
                                        }
                                    }
                                }
                            },
                            {
                                xtype: 'button',
                                id: 'buttonSelectComponentOfDataIO',
                                text: self.fieldLabelMapInfoWindowNewSelectIO,
                                disabled: (dataIOSelection.length > 0) ? false : true,
                                style: {
                                    'float': 'right',
                                    'margin-right': '10px',
                                    'margin-top': '10px'
                                },
                                handler: function (component) {
                                    console.log('mapController.newComponent - buttonSelectComponentOfDataIO clicked, component: ', component);
                                    console.log('mapController.newComponent - buttonSelectComponentOfDataIO clicked, data IO selection: ', dataIOSelection[0]);

                                    var targetGridPanel = Ext.ComponentQuery.query('#existingDevicesGridPanel')[0];

                                    var sensorId = dataIOSelection[0].data.sensorId;

                                    var gridDataItems = targetGridPanel.getStore().data.items;
                                    console.log('mapController.newComponent - buttonSelectComponentOfDataIO clicked, gridDataItems: ', gridDataItems);

                                    for (var i = 0; i < gridDataItems.length; i++) {
                                        var recordData = gridDataItems[i].data;

                                        if (recordData.deviceId == sensorId) {
                                            var selectionModel = targetGridPanel.getSelectionModel();
                                            console.log('mapController.newComponent - buttonSelectComponentOfDataIO clicked, index: ', i);
                                            console.log('mapController.newComponent - buttonSelectComponentOfDataIO clicked, selectionModel: ', selectionModel);

                                            selectionModel.select(i);

                                            var selectedRecordInternalId = selectionModel.selected.items[0].internalId;
                                            console.log('mapController.newComponent - buttonSelectComponentOfDataIO clicked, selectedRecordInternalId: ', selectedRecordInternalId);

                                            var firstRow = document.getElementById('existingDevicesGridPanel').getElementsByTagName('table')[0];
                                            console.log('mapController.newComponent - buttonSelectComponentOfDataIO clicked, firstRow: ', firstRow);

                                            var firstRowHeight = firstRow.offsetHeight;

                                            console.log('mapController.newComponent - buttonSelectComponentOfDataIO clicked, firstRowHeight: ', firstRowHeight);

                                            targetGridPanel.scrollTo(0, (i * firstRowHeight));

                                            var componentTypeValue = Ext.ComponentQuery.query('#newComponentType')[0].getValue();
                                            if (componentTypeValue != null) {
                                                Ext.ComponentQuery.query('#acceptNewComponent')[0].setDisabled(false);
                                            }
                                            else {
                                                Ext.ComponentQuery.query('#acceptNewComponent')[0].setDisabled(true);
                                            }

                                            break;
                                        }
                                    }

                                    
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'gridpanel',
                        id: 'existingDevicesGridPanel',
                        store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.devicesStore'),
                        height: 580,
                        multiSelect: false,
                        frame: true,
                        allowDeselect: true,
                        selectable: true,
                        plugins: 'gridfilters',
                        columns: [
                            
                            { text: self.fieldLabelMapInfoWindowNewRobotName, dataIndex: 'dataSourceName', flex: 1, filter: { type: 'string' } },
                            { text: self.fieldLabelMapInfoWindowNewDeviceName, dataIndex: 'deviceName', flex: 1, filter: { type: 'string' } },
                            { text: self.fieldLabelMapInfoWindowNewDeviceID, dataIndex: 'deviceId', flex: 1, filter: { type: 'string' } }
                        ],
                        listeners: {
                            itemclick: function(component, record, item, index, e, eOpts) {
                                console.log('mapController.newCoponent - itemclick, component: ', component);
                                console.log('mapController.newCoponent - itemclick, record: ', record);
                                console.log('mapController.newCoponent - itemclick, item: ', item);

                                var componentTypeValue = Ext.ComponentQuery.query('#newComponentType')[0].getValue();
                                console.log('mapController.newCoponent - itemclick, componentTypeValue: ', componentTypeValue);

                                var selection = Ext.ComponentQuery.query('#existingDevicesGridPanel')[0].getSelection();

                                if (componentTypeValue != null && selection.length > 0) {
                                    Ext.ComponentQuery.query('#acceptNewComponent')[0].setDisabled(false);
                                }
                                else {
                                    Ext.ComponentQuery.query('#acceptNewComponent')[0].setDisabled(true);
                                }
                            }
                        }
                    },
                    {
                        xtype: 'panel',
                        id: 'newComponentButtonHolder',
                        margin: '10px 0 0 0',
                        items: [
                            {
                                xtype: 'button',
                                text: self.fieldLabelMapInfoWindowNewComponent,
                                disabled: true,
                                id: 'acceptNewComponent',
                                style: {
                                    'float': 'right',
                                    'margin-right': '10px'
                                },
                                handler: function () {
                                    var selectedRecord = Ext.ComponentQuery.query('#existingDevicesGridPanel')[0].getSelection()[0].data;
                                    console.log('mapController.newComponent - acceptNewComponent, selectedRecord: ', selectedRecord);

                                    var deviceId = selectedRecord.deviceId;
                                    var deviceName = selectedRecord.deviceName;

                                    self.createNewComponent(deviceId, deviceName);
                                }
                            },
                            {
                                xtype: 'button',
                                text: self.fieldLabelMapInfoWindowNewCancel,
                                id: 'cancelNewComponent',
                                style: {
                                    'float': 'right',
                                    'margin-right': '10px'
                                },
                                handler: function () {
                                    self.newComponentWindow.destroy();
                                }
                            }
                        ]
                    }
                ]
            }).show();
        });

        // detach component from object
        document.getElementById('detachComponent').addEventListener('click', function (event) {
            console.log('Map click event - detachComponent: ', event);
            console.log('document.getElementsByClassName(\'component-selected\')[0]: ', document.getElementsByClassName('component-selected')[0]);
            var relationId = document.getElementsByClassName('component-selected')[0].getAttribute('data-relationid');
            self.detachComponent(Number(relationId));
        });

        // detach component from object
        document.getElementById('moveComponent').addEventListener('click', function (event) {
            console.log('Map click event - moveComponent: ', event);
            console.log('document.getElementsByClassName(\'component-selected\')[0]: ', document.getElementsByClassName('component-selected')[0]);
            var componentType = document.getElementsByClassName('component-selected')[0].getAttribute('data-componenttype');
            var componentKey = document.getElementsByClassName('component-selected')[0].getAttribute('data-componentkey');
            var relationId = document.getElementsByClassName('component-selected')[0].getAttribute('data-relationid');

            self.startComponentMovement(componentType, componentKey, relationId);
        });

        // accept selection of component into the editor
        document.getElementById('acceptComponent').addEventListener('click', function (event) {
            console.log('Map click event - acceptComponent: ', event);
            console.log('document.getElementsByClassName(\'component-selected\')[0]: ', document.getElementsByClassName('component-selected')[0]);

            var componentKey = document.getElementsByClassName('component-selected')[0].getAttribute('data-componentkey');
            var componentType = document.getElementsByClassName('component-selected')[0].getAttribute('data-componenttype');
            var componentDescription = document.getElementsByClassName('component-selected')[0].getElementsByTagName('td')[0].innerHTML;

            self.saveMapFeatureSelection('subcomponent', Number(componentKey), Number(componentType), componentDescription);
        });

        // cancel selection of component into the editor
        document.getElementById('cancelComponent').addEventListener('click', function (event) {
            console.log('Map click event - cancelComponent: ', event);
            document.getElementById('componentsContent').style.display = 'none';
            document.getElementById('featureContent').style.display = 'block';
            document.getElementById('moveComponent').disabled = true;
        });      

        var closer = document.getElementsByClassName('ol-popup-closer')[0];

        closer.onclick = function () {
            self.overlay.setPosition(undefined);
            closer.blur();
            return false;
        };

        self.overlay = new ol.Overlay({
            element: popupElement,
            autoPan: true,
            autoPanAnimation: {
                duration: 250
            }
        });

        this.map.addOverlay(self.overlay);

        // ADD INTERACTION
        this.pointerDownEvent = this.map.on('singleclick', function (evt) {

            console.log('mapController:mapclick - evt: ', evt);
            console.log('mapController:mapclick - mapLayers: ', mapLayers);

            // TEST - linear ring
            var coordinate = evt.coordinate;
            console.log('mapController:mapclick - coordinate: ', coordinate);

            var view = self.map.getView();
            console.log('mapController:mapclick - view: ', view);

            var currentResolution = view.getResolution();
            console.log('mapController:mapclick - currentResolution: ', currentResolution);

            var radius = currentResolution * self.buffer;
            console.log('mapController:mapclick - radius: ', radius);

            var circle = new ol.geom.Circle(coordinate, radius);
            var polygonCircle = new ol.geom.Polygon.fromCircle(circle, 32);
            console.log('mapController:mapclick - polygonCircle: ', polygonCircle);

            var linearRing = polygonCircle.getLinearRing(0);
            console.log('mapController:mapclick - linearRing: ', linearRing);

            var linearRingCoordinates = linearRing.getCoordinates();
            console.log('mapController:mapclick - linearRingCoordinates: ', linearRingCoordinates);
            var linearRingCoordinatesString = linearRingCoordinates.join(' ') + '';
            console.log('mapController:mapclick - linearRingCoordinatesString: ', linearRingCoordinatesString);

            if(self.showBufferCircle == true) {
                var feature = new ol.Feature({
                    geometry: polygonCircle
                });

                var vectorSource = new ol.source.Vector({
                    features: [feature]
                });

                var vectorLayer = new ol.layer.Vector({
                    source: vectorSource
                });

                self.map.addLayer(vectorLayer);
            }

            if (evt.dragging) {
                return;
            }

            var mapLayersToHandleLength = mapLayers.getLength();
            var mapLayersToHandleCount = 0;
            var selectedObjectsArray = [];

            mapLayers.forEach(function (layer, index, array) {
                var layerInfo = layer.get('layerInfo');
                var layerName = layer.get('layerName');
                var layerUrl = layer.get('layerUrl');
                var layerScaleHintMin = layer.get('scaleHintMin');
                var layerScaleHintMax = layer.get('scaleHintMax');

                if (layerInfo && layerInfo.hasOwnProperty('layerType') && layerInfo.layerType == 'ObjectType' && !layerInfo.isBasemapLayer) {

                    // TBD - check if zoom level allows for layer fetch
                    var layerMinResolution = (layerScaleHintMin != null) ? self.getResolutionFromScale(self.map, layerScaleHintMin) : null;
                    var layerMaxResolution = (layerScaleHintMax != null) ? self.getResolutionFromScale(self.map, layerScaleHintMax) : null;

                    console.log('mapController:mapclick - (resolution) currentResolution: ', currentResolution);
                    console.log('mapController:mapclick - (resolution) layerMinResolution: ', layerMinResolution);
                    console.log('mapController:mapclick - (resolution) layerMaxResolution: ', layerMaxResolution);

                    console.log('mapController:mapclick - layer info: ', layerInfo);
                    console.log('mapController:mapclick - layerName: ', layerName);
                    console.log('mapController:mapclick - layerUrl: ', layerUrl);

                    var layerIsVisible = true;

                    if (layerMaxResolution != null && currentResolution > layerMaxResolution) {
                        layerIsVisible = false;
                    }

                    if (layerUrl && layerIsVisible == true) {

                        var queryUrl = layerUrl;
                        console.log('mapController:mapclick - queryUrl: ', queryUrl);

                        var layerNameNoNamespace = (layerName.indexOf(':') > -1) ? layerName.split(':')[1] : layerName;

                        var objectType = AGS3xIoTAdmin.util.util.getObjectTypeFromLayerName(layerName);
                        var geometryTagName = objectType.geometryTagName; // TBD - get from http://hwd-win2016-01:8090/assens/wfs?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName= layerName
                        console.log('mapController:mapclick - geometryTagName: ', geometryTagName);
                        
                        var xmlQuery = '<wfs:GetFeature service=\'WFS\' ' +
                            'version=\'1.1.1\' ' +
                            'outputFormat=\'application/json\' ' + 
                            'xmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\' ' +
                            'xsi:schemaLocation=\'http://www.opengis.net/wfs\' ' + 
                            'xmlns:gml=\'http://www.opengis.net/gml\' ' + 
                            'xmlns:wfs=\'http://www.opengis.net/wfs\' ' + 
                            'xmlns:ogc=\'http://www.opengis.net/ogc\'>' +
                            '<wfs:Query typeName=\'' + layerNameNoNamespace + '\'>' +
                                '<ogc:Filter>' +
                                    '<ogc:Intersects>' +
                                        '<ogc:PropertyName>' + geometryTagName + '</ogc:PropertyName>' +
                                            '<gml:Polygon>' +
                                                '<gml:exterior>' +
                                                    '<gml:LinearRing>' +
							                            '<gml:coordinates>' + linearRingCoordinatesString + '</gml:coordinates>' +
						                            '</gml:LinearRing>' +
					                            '</gml:exterior>' +
				                            '</gml:Polygon>' +
			                            '</ogc:Intersects>' +
		                            '</ogc:Filter>' +
	                            '</wfs:Query>' +
                            '</wfs:GetFeature>';

                        console.log('mapController:mapclick - xmlQuery: ', xmlQuery);

                        // A - get features
                        Ext.Ajax.request({
                            url: queryUrl,
                            method: 'POST',
                            xmlData: xmlQuery,
                            cors: true,
                            scope: this,
                            layerInfo: layerInfo,
                            layer: layer,
                            objectType: objectType,
                            callback: function (options, success, response) {
                                console.log('mapController:mapclick - response: ', response);

                                if (success) {
                                    try {
                                        var result = Ext.decode(response.responseText);
                                        console.log('mapController:mapclick - query map features, result: ', result);

                                        if (result.hasOwnProperty('features')) {
                                            var features = result.features;

                                            var objectType = AGS3xIoTAdmin.util.util.getObjectTypeFromLayerName(layerName);
                                            console.log('mapController.mapclick - objectType: ', objectType);
                                            console.log('mapController.mapclick - layerInfo.itemId: ', layerInfo.itemId);
                                            var idFieldName = objectType.fieldId;
                                            var nameFieldName = objectType.fieldName;
                                            var descriptionFieldName = objectType.fieldDescription;

                                            console.log('mapController:mapclick - features: ', features);

                                            if (features.length > 0) {
                                                for (var f = 0; f < features.length; f++) {
                                                    var feature = features[f];
                                                    console.log('mapController.mapclick - feature: ', feature);

                                                    var keyDescriptions = [
                                                        {
                                                            field: idFieldName,
                                                            type: 'integer',
                                                            value: feature.properties[idFieldName]
                                                        }
                                                    ]

                                                    var selectedObject = {
                                                        objectType: objectType.type,
                                                        objectKeys: keyDescriptions,
                                                        componentType: null,
                                                        componentKey: null,
                                                        name: feature.properties[nameFieldName],
                                                        type: 'ObjectType',
                                                        featureDescription: feature.properties[descriptionFieldName],
                                                        wfs: objectType.wfs,
                                                        wfsLayer: objectType.wfsLayer,
                                                        geometry: feature.geometry,
                                                        geometryName: feature.geometry_name
                                                    }

                                                    selectedObjectsArray.push(selectedObject);
                                                }
                                            }
                                        }

                                        mapLayersToHandleCount++;
                                        console.log('mapController.mapclick - layer ' + mapLayersToHandleCount + ' of ' + mapLayersToHandleLength + ' handled (with data)');

                                        if (mapLayersToHandleCount == mapLayersToHandleLength) {
                                            console.log('mapController.mapclick - all layers handled, proceed to handleClickResultData, selectedObjectsArray: ', selectedObjectsArray);
                                            self.handleClickResultData(evt, selectedObjectsArray);
                                        }
                                    }
                                    catch (exception) {
                                        console.log('mapController.init - Ajax EXCEPTION: ', exception);
                                        mapLayersToHandleCount++;
                                        console.log('mapController.init - layer ' + mapLayersToHandleCount + ' of ' + mapLayersToHandleLength + ' handled (exception)');
                                        if (mapLayersToHandleCount == mapLayersToHandleLength) {
                                            console.log('mapController.init - all layers handled, proceed to handleClickResultData, selectedObjectsArray: ', selectedObjectsArray);
                                            self.handleClickResultData(evt, selectedObjectsArray);
                                        }
                                    }
                                }
                                else {
                                    console.log('mapController.init - Ajax failure');
                                    mapLayersToHandleCount++;
                                    console.log('mapController.init - layer ' + mapLayersToHandleCount + ' of ' + mapLayersToHandleLength + ' handled (failure)');
                                    if (mapLayersToHandleCount == mapLayersToHandleLength) {
                                        console.log('mapController.init - all layers handled, proceed to handleClickResultData, selectedObjectsArray: ', selectedObjectsArray);
                                        self.handleClickResultData(evt, selectedObjectsArray);
                                    }
                                }
                            }
                        }, this);

                    }
                    else {
                        console.log('mapController.init - layer has no url');
                        mapLayersToHandleCount++;
                        console.log('mapController.init - layer ' + mapLayersToHandleCount + ' of ' + mapLayersToHandleLength + ' handled (no URL)');
                        if (mapLayersToHandleCount == mapLayersToHandleLength) {
                            console.log('mapController.init - all layers handled, proceed to handleClickResultData, selectedObjectsArray: ', selectedObjectsArray);
                            self.handleClickResultData(evt, selectedObjectsArray);
                        }
                    }
                }
                else {
                    mapLayersToHandleCount++;
                    console.log('mapController.init - layer ' + mapLayersToHandleCount + ' of ' + mapLayersToHandleLength + ' handled (not proper type)');
                    if (mapLayersToHandleCount == mapLayersToHandleLength) {
                        console.log('mapController.init - all layers handled, proceed to handleClickResultData, selectedObjectsArray: ', selectedObjectsArray);
                        self.handleClickResultData(evt, selectedObjectsArray);
                    }
                }

            }, this); // forEach

        });
    }
});
