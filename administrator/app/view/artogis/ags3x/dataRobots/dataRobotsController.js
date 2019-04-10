Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dataRobots.dataRobotsController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xDataRobots',

    requires: [
        'AGS3xIoTAdmin.util.events',
       'AGS3xIoTAdmin.util.util'
    ],

    robotId: null,
    drillActive: false,
    fullscreenActive: false,

    originalDashboardColumnWidth: 0,

    fullscreenInfoWindow: null,
    columnMargin: 20,
    dashboardWidth: 29,
    dashboardEntryWidth: 315, // for calculating number of dashboards per row
    singleDashboardEntryWidth: 295 + 20, // for calculating columnWidth of single open robot dashboard
    baseUrl: '',

    updateDataCycleOn: false,

    // text content - default is Danish (da), for translations, see translation.js
    textRobotsSensorsStatusRunning: 'Sensor kører som forventet.',
    textRobotsSensorsStatusStopped: 'Sensor er stoppet. Tjek for eventuelle fejl eller ventende genstart.',
    textRobotsSensorsStatusUnknown: 'Sensor status er ukendt.',
    textRobotsSensorsStatusWarning: 'Sensor kører, men intervallerne bliver ikke overholdt. Tjek sensor for eventuelle fejl.',
    textRobotsFullscreenInfo: 'Afslut fuldskærmsvisning ved at trykke på<br><br><b>mellemrums-tasten</b>',

    loadRobots: function () {
        var self = this;

        var loadDataRobotsStatus = true;

        console.log('dataRobotsController.loadRobots...');

        var robotsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.robotsStore');
        console.log('dataRobotsController.loadRobots - robotsStore: ', robotsStore);

        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'datasource',
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {

                        var robotsResult = Ext.decode(response.responseText);
                        var dataSources = robotsResult.dataSources;
                        
                        // load sensors
                        Ext.Ajax.request({
                            scope: self,
                            url: self.baseUrl + 'unconfigureddataio',
                            method: 'GET',
                            contentType: 'application/json',
                            callback: function (options, success, response) {
                                if (success) {
                                    try {

                                        var dashboardHeight = 260;
                                        var dashboardWidth = 295;

                                        var sensorsResult = Ext.decode(response.responseText);
                                        var sensors = sensorsResult.unConfiguredDataIos;

                                        var robotStatus = {};
                                            
                                        // Add metadata to robotStatus
                                        for (var d = 0; d < dataSources.length; d++) {
                                            var dataSource = dataSources[d];
                                            var dataSourceId = dataSource.id;
                                            var dataSourceName = dataSource.name;
                                            var dashboardUrl = dataSource.dashboardUrl;

                                            robotStatus[dataSourceId] = {};
                                            robotStatus[dataSourceId]['totalCount'] = 0;
                                            robotStatus[dataSourceId]['activeCount'] = 0;
                                            robotStatus[dataSourceId]['name'] = dataSourceName;
                                            robotStatus[dataSourceId]['id'] = dataSourceId;
                                            robotStatus[dataSourceId]['dashboardUrl'] = dashboardUrl;
                                        }

                                        // Add status data to robotStatus
                                        for (var s = 0; s < sensors.length; s++) {
                                            var sensor = sensors[s];
                                            var dataSourceId = sensor.dataSourceId;

                                            // create object, if it doesn't already exist
                                            if (robotStatus.hasOwnProperty(dataSourceId)) {
                                                robotStatus[dataSourceId]['totalCount'] = robotStatus[dataSourceId]['totalCount'] + 1;

                                                if (sensor.hasOwnProperty('status') && sensor.status != null && sensor.status.toLowerCase() === 'operating') {
                                                    robotStatus[dataSourceId]['activeCount'] = robotStatus[dataSourceId]['activeCount'] + 1;
                                                }
                                            }
                                        }

                                        // set height of robotDashboards
                                        var robotDashboardsPerRowCount = Math.floor((window.innerWidth - 340) / (dashboardWidth + 20));
                                        var robotDashboardsTotalRowsCount = Math.ceil(dataSources.length / robotDashboardsPerRowCount);
                                        var robotDashboards = Ext.ComponentQuery.query('#robotDashboards')[0];
                                        robotDashboards.setHeight((dashboardHeight * robotDashboardsTotalRowsCount) + 60);

                                        // populate robotDashboards-innerCt
                                        for (var robotIdKey in robotStatus) {
                                            if (robotStatus.hasOwnProperty(robotIdKey)) {
                                                    
                                                var dataSource = robotStatus[robotIdKey];

                                                var color = '';
                                                var percentage = (dataSource.activeCount / dataSource.totalCount ) * 100;
                                                if(percentage <= 50 && percentage > 20) {
                                                    color = '#e4e61c';
                                                }
                                                else if(percentage <= 20) {
                                                    color = '#cd3739';
                                                }
                                                else {
                                                    color = '#00b500';
                                                }

                                                var robotDashboard = Ext.create('Ext.panel.Panel', {
                                                    renderTo: Ext.get('robotDashboards-innerCt'),
                                                    robotId: dataSource.id,
                                                    cls: 'robot-dashboard-panel',
                                                    height: 250,
                                                    width: 295,
                                                    margin: '20px 18px 0 2px',
                                                    style: {
                                                        'box-shadow': '0 1px 2px 0 rgba(0, 0, 0, 0.2)',
                                                        'float': 'left',
                                                        'border': 'none !important'
                                                    },
                                                    items: [
                                                        {
                                                            xtype: 'panel',
                                                            width: 295,
                                                            height: 60,
                                                            html: dataSource.name,
                                                            style: {
                                                                'text-indent': '-20px',
                                                                'line-height': '60px',
                                                                'font-size': '16px'
                                                            },
                                                            bodyStyle: {
                                                                'font-size': '16px'
                                                            },
                                                            items: [
                                                                {
                                                                    xtype: 'button',
                                                                    height: 40,
                                                                    width: 40,
                                                                    style: {
                                                                        'background': '#ffffff',
                                                                        'color': '#000000 !important',
                                                                        'cursor': 'pointer',
                                                                        'z-index': '5000',
                                                                        'text-indent': '0px'
                                                                    },
                                                                    bodyStyle: {
                                                                        'background': '#ffffff',
                                                                        'color': '#000000 !important'
                                                                    },
                                                                    cls: 'robot-monitor-icon-button',
                                                                    iconCls: 'x-fa fa-television',
                                                                    y: 9,
                                                                    x: 265,
                                                                    robotId: dataSource.id,
                                                                    listeners: {
                                                                        click: function (buttonCmp) {
                                                                            Ext.getCmp('ags3xDataRobots').getController('AGS3xIoTAdmin.view.artogis.ags3x.dataRobots.dataRobotsController').loadRobotDetails(buttonCmp.robotId);
                                                                        }
                                                                    }
                                                                }
                                                            ]
                                                        },
                                                        {
                                                            xtype: 'panel',
                                                            width: 295,
                                                            height: 200,
                                                            layout: 'fit',
                                                            items: {
                                                                xtype: 'gauge',
                                                                id: 'robot-gauge-' + dataSource.id,
                                                                padding: 25,
                                                                value: dataSource.activeCount,
                                                                minValue: 0,
                                                                maxValue: dataSource.totalCount,
                                                                trackStart: 180,
                                                                trackLength: 180,
                                                                valueStyle: {
                                                                    outerRadius: '100%',
                                                                    innerRadius: '100% - 48',
                                                                    round: false,
                                                                    fill: color,
                                                                    strokeOpacity: 0
                                                                },
                                                                trackStyle: {
                                                                    outerRadius: '100%',
                                                                    innerRadius: '100% - 48',
                                                                    round: false,
                                                                    fill: '#b8b8b8',
                                                                    strokeOpacity: 0
                                                                },
                                                                textTpl: [
                                                                    '<div id="robot-sensors-counter-' + dataSource.id + '" style="font-size: 20px;margin-top:-35px;"><tpl><div style="font-size:12px;margin-bottom:5px;">Aktive sensorer</div><div id="robot-sensors-counter-' + dataSource.id + '-text">' + dataSource.activeCount + ':' + dataSource.totalCount + '</div></tpl></div>'
                                                                ]
                                                            }
                                                        }
                                                    ],
                                                    listeners: {
                                                        render: function (c) {
                                                            document.getElementById(c.el.id + '-body').style.border = 'none';
                                                        }
                                                    }
                                                });
                                            }
                                        }

                                        // start monitoring of robot status
                                        window.setInterval(function () {
                                            self.updateRobotDetails();
                                        }, 10000);
                                            
                                    }
                                    catch(exception) {
                                        console.log('dataRobotsController.loadRobots, get sensors - EXCEPTION: ', exception);
                                    }
                                }
                            }
                        });
                           
                    }
                    catch (exception) {
                        console.log('dataRobotsController.loadRobots - EXCEPTION: ', exception);
                    }
                }
                else {
                    console.log('dataRobotsController.loadRobots - FAILURE: ', response);
                }
            }
        });
    },
    updateRobotDetails: function () {
        var self = this;

        console.log('dataRobotsController.updateRobotDetails');

        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'datasource',
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {

                        var robotsResult = Ext.decode(response.responseText);

                        var dataSources = robotsResult.dataSources;

                        // load sensors
                        Ext.Ajax.request({
                            scope: self,
                            url: self.baseUrl + 'unconfigureddataio',
                            method: 'GET',
                            contentType: 'application/json',
                            callback: function (options, success, response) {
                                if (success) {
                                    try {

                                        var dashboardHeight = 530;
                                        var dashboardWidth = 295;

                                        var sensorsResult = Ext.decode(response.responseText);
                                        var sensors = sensorsResult.unConfiguredDataIos;

                                        var robotStatus = {};

                                        // Add metadata to robotStatus
                                        for (var d = 0; d < dataSources.length; d++) {
                                            var dataSource = dataSources[d];
                                            var dataSourceId = dataSource.id;
                                            var dataSourceName = dataSource.name;
                                            var dashboardUrl = dataSource.dashboardUrl;

                                            robotStatus[dataSourceId] = {};
                                            robotStatus[dataSourceId]['totalCount'] = 0;
                                            robotStatus[dataSourceId]['activeCount'] = 0;
                                            robotStatus[dataSourceId]['name'] = dataSourceName;
                                            robotStatus[dataSourceId]['id'] = dataSourceId;
                                            robotStatus[dataSourceId]['dashboardUrl'] = dashboardUrl;
                                        }

                                        // Add status data to robotStatus
                                        for (var s = 0; s < sensors.length; s++) {
                                            var sensor = sensors[s];
                                            var dataSourceId = sensor.dataSourceId;

                                            // create object, if it doesn't already exist
                                            if (robotStatus.hasOwnProperty(dataSourceId)) {
                                                robotStatus[dataSourceId]['totalCount'] = robotStatus[dataSourceId]['totalCount'] + 1;

                                                if (sensor.hasOwnProperty('status') && sensor.status != null && sensor.status.toLowerCase() === 'operating') {
                                                    robotStatus[dataSourceId]['activeCount'] = robotStatus[dataSourceId]['activeCount'] + 1;
                                                }
                                            }
                                        }


                                        // populate robotDashboards-innerCt
                                        for (var robotIdKey in robotStatus) {
                                            if (robotStatus.hasOwnProperty(robotIdKey)) {
                                                var dataSource = robotStatus[robotIdKey];

                                                var color = '';
                                                var percentage = (dataSource.activeCount / dataSource.totalCount) * 100;
                                                if (percentage <= 50 && percentage > 20) {
                                                    color = '#e4e61c';
                                                }
                                                else if (percentage <= 20) {
                                                    color = '#cd3739';
                                                }
                                                else {
                                                    color = '#00b500';
                                                }

                                                var gauge = Ext.ComponentQuery.query('#robot-gauge-' + dataSource.id)[0];
                                                gauge.setMaxValue(dataSource.totalCount);
                                                gauge.setValue(dataSource.activeCount);
                                                gauge.setValueStyle({
                                                    outerRadius: '100%',
                                                    innerRadius: '100% - 48',
                                                    round: false,
                                                    fill: color,
                                                    strokeOpacity: 0
                                                });

                                                document.getElementById('robot-sensors-counter-' + dataSource.id + '-text').innerHTML = dataSource.activeCount + ':' + dataSource.totalCount;

                                            }
                                        }

                                        if (self.updateDataCycleOn == true) {
                                            self.updateRobotDetailsSensorData(sensorsResult);
                                        }
                                    }
                                    catch (exception) {
                                        console.log('dataRobotsController.updateRobotDetails, get sensors - EXCEPTION: ', exception);
                                    }
                                }
                            }
                        });
                    }
                    catch (exception) {
                        console.log('dataRobotsController.updateRobotDetails - EXCEPTION: ', exception);
                    }
                }
                else {
                    console.log('dataRobotsController.updateRobotDetails - FAILURE: ', response);
                }
            }
        });
    },
    loadRobotDetails: function (robotId) {
        var self = this;

        console.log('dataRobotsController.loadRobotDetails');

        document.getElementById('robotColumnLeft-body').scrollTop = 0;        

        self.robotId = robotId;

        if (this.drillActive === false) {
            self.drillActive = true;
            var dashboardsList = Ext.ComponentQuery.query('panel[cls=robot-dashboard-panel]');

            for (var i = 0; i < dashboardsList.length; i++) {
                if (dashboardsList[i].robotId !== robotId) {
                    dashboardsList[i].hide();
                }
            }

            var ags3xDataRobots = Ext.ComponentQuery.query('#ags3xDataRobots')[0];

            var robotColumnLeft = Ext.ComponentQuery.query('#robotColumnLeft')[0];
            var robotColumnRight = Ext.ComponentQuery.query('#robotColumnRight')[0];

            robotColumnLeft.setScrollable(false);
            robotColumnRight.setWidth(window.innerWidth - 300 -20 - 20 - 295 - 20 - 4);
            robotColumnRight.show();

            // hide fullscreen button
            Ext.ComponentQuery.query('#buttonActivateFullscreen')[0].hide();

            // fetch device data for robot
            Ext.Ajax.request({
                scope: self,
                url: self.baseUrl + 'sensorobject',
                method: 'GET',
                contentType: 'application/json',
                callback: function (options, success, response) {
                    if (success) {
                        try {
                            var result = Ext.decode(response.responseText);

                            console.log('dataRobotsController.loadRobotDetails - devices result: ', result);

                            var robotDevicesDataStore = self.getViewModel().getStore('robotDevicesDataStore');
                            var data = [];
                            for (var i = 0; i < result.sensorObjects.length; i++) {
                                var item = result.sensorObjects[i];
                                if (Number(item.dataSourceId) == Number(self.robotId)) {
                                    var record = {
                                        id: item.id,
                                        name: item.name,
                                        description: item.description,
                                        location: item.location,
                                        ogrgeometry: item.ogrgeometry,
                                        dataSourceId: item.dataSourceId,
                                        nameAlias: (item.nameAlias != null && item.nameAlias.length > 0) ? item.nameAlias : item.name
                                    };
                                    data.push(record);
                                }
                            }
                            robotDevicesDataStore.setData(data);

                            var gridPanel = Ext.ComponentQuery.query('#robotDevicesDataTable')[0];
                            gridPanel.reconfigure(robotDevicesDataStore);

                            document.getElementById('robotColumnLeft-body').scrollTop = 0;
                        }
                        catch (exception) {
                            console.log('dataRobotsController.loadRobotDetails - load devices info, EXCEPTION: ', exception);
                        }
                    }
                }
            });
            

            // fetch sensor data for robot
            var measurementTypesUrl = self.baseUrl + 'measurementtype';

            Ext.Ajax.request({
                scope: this,
                url: measurementTypesUrl,
                method: 'GET',
                contentType: 'application/json',
                callback: function (options, success, response) {
                    if (success) {
                        try {
                            var result = Ext.decode(response.responseText);
                            console.log('dataRobotsController.loadRobotDetails, load sensors data, measurement types - result: ', result);

                            var language = (AGS3xIoTAdmin.util.util.getUrlParameterByName('locale')) ? AGS3xIoTAdmin.util.util.getUrlParameterByName('locale') : 'da';

                            var measurementTypesData = [];

                            for (var i = 0; i < result.measurementTypes.length; i++) {
                                var measurementType = result.measurementTypes[i];

                                if (measurementType.language == language) {
                                    if (measurementType.isSignalStrength == true || measurementType.isBatteryStatus == true) {
                                        measurementTypesData.unshift(measurementType);
                                    }
                                    else {
                                        measurementTypesData.push(measurementType);
                                    }
                                }
                            }

                            Ext.Ajax.request({
                                scope: self,
                                url: self.baseUrl + 'unconfigureddataio',
                                measurementTypesData: measurementTypesData,
                                method: 'GET',
                                contentType: 'application/json',
                                callback: function (options, success, response) {
                                    if (success) {
                                        try {
                                            var result = Ext.decode(response.responseText);

                                            console.log('dataRobotsController.loadRobotDetails - sensors result: ', result);

                                            var store = self.getViewModel().getStore('robotSensorsDataStore');
                                            var data = [];
                                            for (var i = 0; i < result.unConfiguredDataIos.length; i++) {
                                                var item = result.unConfiguredDataIos[i];
                                                if (Number(item.dataSourceId) == Number(self.robotId)) {
                                                    var record = {
                                                        status: (item.status != null) ? item.status.toLowerCase() : 'unknown',
                                                        statusTooltipText: self.getTooltipText(item.status),
                                                        nodeType: item.nodeType,
                                                        nodeTypeString: AGS3xIoTAdmin.util.util.getMeasurementTypeNameFromId(item.nodeType, options.measurementTypesData),
                                                        interval: self.timeConversion(item.interval),
                                                        lastRun: self.dateConversion(item.lastRun),
                                                        dataSourceId: item.dataSourceId,
                                                        sensorId: item.sensorObjectId,
                                                        name: item.name,
                                                        description: item.description,
                                                        dataId: item.id,
                                                        unit: item.unit,
                                                        lastValue: item.lastValue,
                                                        nameAlias: item.nameAlias
                                                    };
                                                    data.push(record);
                                                }
                                            }
                                            store.setData(data);

                                            var gridPanel = Ext.ComponentQuery.query('#robotSensorDataTable')[0];
                                            gridPanel.reconfigure(store);

                                            document.getElementById('robotColumnLeft-body').scrollTop = 0;
                                        }
                                        catch (ex) {
                                            console.log('dataRobotsController.loadRobotDetails - EXCEPTION: ', ex);
                                        }
                                    }
                                }
                            });
                        }
                        catch (exception) {
                            console.log('dataRobotsController.loadRobotDetails, load sensors data, EXCEPTIONt: ', exception);
                        }
                    }
                }
            });

        }
        else {
            this.drillActive = false;
            var dashboardsList = Ext.ComponentQuery.query('panel[cls=robot-dashboard-panel]');
            var dashboardsListLength = dashboardsList.length;
            var i = 0;
            for (i = 0; i < dashboardsListLength; i++) {
                dashboardsList[i].show();
            }

            var ags3xDataRobots = Ext.ComponentQuery.query('#ags3xDataRobots')[0];

            var robotColumnLeft = Ext.ComponentQuery.query('#robotColumnLeft')[0];
            var robotColumnRight = Ext.ComponentQuery.query('#robotColumnRight')[0];

            robotColumnLeft.setScrollable('y');
            robotColumnRight.hide();

            Ext.ComponentQuery.query('#buttonActivateFullscreen')[0].show();

            document.getElementById('robotColumnLeft-body').scrollTop = 0;
        }
        
    },
    getTooltipText: function (status) {
        var self = this;

        status = (status != null) ? status.toLowerCase() : 'unknown';

        switch (status) {
            case 'operating':
                return self.textRobotsSensorsStatusRunning;
            case 'stopped':
                return self.textRobotsSensorsStatusStopped;
            case 'unknown':
                return self.textRobotsSensorsStatusUnknown;
            case 'warning':
                return self.textRobotsSensorsStatusWarning;
            default:
                return self.textRobotsSensorsStatusUnknown;
        }
    },
    timeConversion: function timeConversion(millisec) {

        var seconds = (millisec / 1000).toFixed(0);

        var minutes = (millisec / (1000 * 60)).toFixed(0);

        var hours = (millisec / (1000 * 60 * 60)).toFixed(0);

        var days = (millisec / (1000 * 60 * 60 * 24)).toFixed(0);

        if (seconds <= 60) {
            return seconds + " second" + ((seconds > 1) ? 's' : '');
        } else if (minutes <= 60) {
            return minutes + " minute" + ((minutes > 1) ? 's' : '');
        } else if (hours <= 24) {
            return hours + " hour" + ((hours > 1) ? 's' : '');
        } else {
            return days + " days"
        }
    },
    dateConversion: function (epochTime) {
        var self = this;
        var date = new Date(epochTime);
        var timeString = self.zeroFill(date.getHours() + '') + ':' + self.zeroFill(date.getMinutes() + '') + ':' + self.zeroFill(date.getSeconds() + '');

        var dateString = self.zeroFill(date.getDate() + '') + '/' + self.zeroFill((date.getMonth() + 1) + '') + '/' + date.getFullYear()
        return timeString + ', ' + dateString;
    },
    zeroFill: function (number) {
        if (number.length == 1) {
            number = '0' + number;
        }
        return number;
    },
    toggleFullscreen: function () {
        var self = this;

        if (self.fullscreenActive === false) {

            self.logOldPositions();

            document.addEventListener('keyup', function (event) {
                if (self.fullscreenActive == true) {
                    if ((event.keyCode || event.which) == 32) {
                        self.cancelFullscreen();
                    }
                }
            }, true);

            self.performFullscreen();

            self.fullscreenInfoWindow = new Ext.Window({
                id: 'fullscreenInfoWindow',
                title: 'TEST',
                layout: 'fit',
                width: 300,
                height: 100,
                x: ((window.innerWidth - 300) / 2),
                y: ((window.innerHeight - 100) / 2),
                border: false,
                frame: false,
                renderTo: document.body,
                header: false,
                style: 'text-align: center;border: 0 !important;',
                items: [
                    {
                        xtype: 'panel',
                        id: 'infoPanel',
                        layout: 'fit',
                        fullscreen: true,
                        border: false,
                        padding: 20,
                        style: 'text-align: center;border: 0 !important;',
                        listeners: {
                            afterrender: function () {
                                document.getElementById('infoPanel-body').style.border = '0';
                                setTimeout(function () {
                                    closeFullscreenInfoWindow(self.fullscreenInfoWindow);
                                }, 5000);
                            }
                        },
                        items: [
                            {
                                xtype: 'component',
                                html: '<div style="text-align:center;">' + this.textRobotsFullscreenInfo + '</div>',
                                border: false
                            }
                        ]
                    }
                ]

            }).show();
        }
    },
    clearRobots: function() {
        document.getElementById('robotDashboards-innerCt').innerHTML = '';
        Ext.ComponentQuery.query('#buttonReloadRobots')[0].setHidden(false);
    },
    reloadRobots: function() {
        var self = this;
        self.loadRobots();
        Ext.ComponentQuery.query('#buttonReloadRobots')[0].setHidden(true);
    },
    logOldPositions: function () {
        var self = this;
        self.originalDashboardColumnWidth = document.getElementById('robotColumnLeft').clientWidth;
    },
    performFullscreen: function () {
        var self = this;

        Ext.getCmp('ags3xLeftPanel').hide();
        Ext.getCmp('ags3xMain_header').hide();
        Ext.getCmp('robotColumnLeft_header').hide();

        var maxDashboardsPerRow = Math.floor(window.outerWidth / self.dashboardEntryWidth);
        var robotColumnLeftMargin = (window.outerWidth - (self.dashboardEntryWidth * maxDashboardsPerRow)) / 2;

        document.getElementById('robotColumnLeft').clientWidth = window.outerWidth;
        document.getElementById('robotColumnLeft').style.marginLeft = robotColumnLeftMargin + 'px';

        self.fullscreenActive = true;
    },
    cancelFullscreen: function () {
        var self = this;

        Ext.getCmp('ags3xLeftPanel').show();
        Ext.getCmp('ags3xMain_header').show();
        Ext.getCmp('robotColumnLeft_header').show();

        document.getElementById('robotColumnLeft').clientWidth = self.originalDashboardColumnWidth;
        document.getElementById('robotColumnLeft').style.marginLeft = '0px';

        self.fullscreenActive = false;
    },
    processCheckboxUpdateDataChanged: function(component) {
        var self = this;

        var checkedValue = component.value;

        console.log('dataRobotsController.processCheckboxUpdateDataChanged - component: ', component);
        console.log('dataRobotsController.processCheckboxUpdateDataChanged - checkedValue: ', checkedValue);

        if(checkedValue == true) {
            self.updateDataCycleOn = true;
        }
        else if(checkedValue == false) {
            self.updateDataCycleOn = false;
        }
    },
    updateRobotDetailsSensorData: function (sensorsData) {
        var self = this;

        console.log('dataRobotsController.updateRobotDetailsSensorData - sensorsData: ', sensorsData);

        var robotSensorsDataStore = self.getViewModel().getStore('robotSensorsDataStore');

        var dataSourceId = robotSensorsDataStore.data.items[0].data.dataSourceId;

        var updatedSensorsArray = [];

        console.log('dataRobotsController.updateRobotDetailsSensorData - robotSensorsDataStore: ', robotSensorsDataStore);

        var measurementTypesUrl = self.baseUrl + 'measurementtype';

        Ext.Ajax.request({
            scope: this,
            url: measurementTypesUrl,
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dataRobotsController.updateRobotDetailsSensorData, load sensors data, measurement types - result: ', result);

                        var language = (AGS3xIoTAdmin.util.util.getUrlParameterByName('locale')) ? AGS3xIoTAdmin.util.util.getUrlParameterByName('locale') : 'da';

                        var measurementTypesData = [];

                        for (var i = 0; i < result.measurementTypes.length; i++) {
                            var measurementType = result.measurementTypes[i];

                            if (measurementType.language == language) {
                                if (measurementType.isSignalStrength == true || measurementType.isBatteryStatus == true) {
                                    measurementTypesData.unshift(measurementType);
                                }
                                else {
                                    measurementTypesData.push(measurementType);
                                }
                            }
                        }

                        for (var i = 0; i < sensorsData.unConfiguredDataIos.length; i++) {
                            var item = sensorsData.unConfiguredDataIos[i];
                            if (item.dataSourceId == dataSourceId) {
                                var record = {
                                    status: (item.status != null) ? item.status.toLowerCase() : 'unknown',
                                    statusTooltipText: self.getTooltipText(item.status),
                                    nodeType: item.nodeType,
                                    nodeTypeString: AGS3xIoTAdmin.util.util.getMeasurementTypeNameFromId(item.nodeType, measurementTypesData),
                                    interval: self.timeConversion(item.interval),
                                    lastRun: self.dateConversion(item.lastRun),
                                    dataSourceId: item.dataSourceId,
                                    sensorId: item.sensorObjectId,
                                    dataSourceName: item.name,
                                    description: item.description,
                                    dataId: item.id,
                                    unit: item.unit,
                                    lastValue: item.lastValue,
                                    nameAlias: item.nameAlias
                                };

                                updatedSensorsArray.push(record);

                            }
                        }

                        robotSensorsDataStore.setData(updatedSensorsArray);

                        console.log('dataRobotsController.updateRobotDetailsSensorData - updatedSensorsArray: ', updatedSensorsArray);
                    }
                    catch (exception) {
                        console.log('dataRobotsController.updateRobotDetailsSensorData - EXCEPTION: ', exception);
                    }
                }
            }
        });

        
    },
    updateDeviceAlias: function(editor, context, eOpts) {
        var self = this;

        console.log('dataRobotsController.updateDeviceAlias - editor: ', editor);
        console.log('dataRobotsController.updateDeviceAlias - context: ', context);
        console.log('dataRobotsController.updateDeviceAlias - eOpts: ', eOpts);

        var dataSourceId = context.record.data.dataSourceId;
        var sensorObjectId = context.record.data.id;
        var originalName = context.newValues.name;
        var newNameAlias = context.newValues.nameAlias;
        console.log('dataRobotsController.updateDeviceAlias - newNameAlias: ', newNameAlias);

        var params = {

            nameAlias: newNameAlias
        }

        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'sensorobject/' + dataSourceId + '/alias?sensorobjectid=' + sensorObjectId,
            method: 'PUT',
            jsonData: Ext.JSON.encode(params),
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dataRobotsController.updateDeviceAlias - update result: ', result);


                        self.updateConfiguredDataIOAliases(sensorObjectId, newNameAlias, originalName);
                    }
                    catch (exception) {
                        console.log('dataRobotsController.updateDeviceAlias - EXCEPTION: ', exception);
                    }
                }
                else {
                    console.log('dataRobotsController.updateDeviceAlias - failure, response: ', response);
                }
            }
        });
    },
    updateSensorAlias: function (editor, context, eOpts) {
        var self = this;

        console.log('dataRobotsController.updateSensorAlias - editor: ', editor);
        console.log('dataRobotsController.updateSensorAlias - context: ', context);
        console.log('dataRobotsController.updateSensorAlias - eOpts: ', eOpts);

        var sensorId = context.record.data.dataId;
        var newNameAlias = context.newValues.nameAlias;
        console.log('dataRobotsController.updateSensorAlias - newNameAlias: ', newNameAlias);

        var params = {
            nameAlias: newNameAlias
        }

        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'unconfigureddataio/' + sensorId  + '/alias',
            method: 'PUT',
            jsonData: Ext.JSON.encode(params),
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dataRobotsController.updateSensorAlias - update result: ', result);

                    }
                    catch (exception) {
                        console.log('dataRobotsController.updateSensorAlias - EXCEPTION: ', exception);
                    }
                }
                else {
                    console.log('dataRobotsController.updateSensorAlias - failure, response: ', response);
                }
            }
        });
    },
    updateConfiguredDataIOAliases: function(sensorObjectId, newNameAlias, originalName) {
        var self = this;

        console.log('dataRobotsController.updateConfiguredDataIOAliases - sensorObjectId: ', sensorObjectId);
        console.log('dataRobotsController.updateConfiguredDataIOAliases - newNameAlias: ', newNameAlias);

        var devicesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.devicesStore');
        console.log('dataRobotsController.updateConfiguredDataIOAliases - devicesStore, before: ', devicesStore);

        var devicesStoreLength = devicesStore.data.items.length;

        for (var i = 0; i < devicesStoreLength; i++) {
            var deviceItem = devicesStore.data.items[i].data;
            if (deviceItem.deviceId == sensorObjectId) {
                console.log('dataRobotsController.updateConfiguredDataIOAliases - deviceItem (index ' + i + '): ', deviceItem);
                deviceItem.deviceAlias = newNameAlias;
                break;
            }
        }

        console.log('dataRobotsController.updateConfiguredDataIOAliases - devicesStore, after: ', devicesStore);

        var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');
        var unconfiguredDataStoreLength = unconfiguredDataStore.data.items.length;

        console.log('dataRobotsController.updateConfiguredDataIOAliases - unconfiguredDataStore: ', unconfiguredDataStore);

        for (var i = 0; i < unconfiguredDataStoreLength; i++) {
            var unconfiguredDataItem = unconfiguredDataStore.data.items[i].data;
            if (unconfiguredDataItem.sensorId == sensorObjectId) {
                console.log('dataRobotsController.updateConfiguredDataIOAliases - unconfiguredDataItem (index ' + i + '): ', unconfiguredDataItem);
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
                        console.log('dataRobotsController.updateConfiguredDataIOAliases - get result: ', result);

                        var configurations = result.configurations;

                        for (var i = 0; i < configurations.length; i++) {
                            var configuration = configurations[i];

                            if (configuration.sensorObjectId == sensorObjectId) {
                                console.log('dataRobotsController.updateConfiguredDataIOAliases - HIT: ', configuration);

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
                                                console.log('dataRobotsController.updateConfiguredDataIOAliases - update result: ', result);
                                            }
                                            catch (exception) {
                                                console.log('dataRobotsController.updateConfiguredDataIOAliases - update, EXCEPTION: ', exception);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                    catch (exception) {
                        console.log('dataRobotsController.updateConfiguredDataIOAliases - EXCEPTION: ', exception);
                    }
                }
                else {
                    console.log('dataRobotsController.updateConfiguredDataIOAliases - failure, response: ', response);
                }
            }
        });
    },
    init: function () {
        var self = this;

        self.baseUrl = AGS3xIoTAdmin.systemData.serviceUrl;

        console.log('dataRobotsController.init...');
        console.log('dataRobotsController.init - AGS3xIoTAdmin.systemData.serviceUrl: ', AGS3xIoTAdmin.systemData.serviceUrl);

        self.loadRobots();

        AGS3xIoTAdmin.util.events.on('checkboxUpdateDataChanged', self.processCheckboxUpdateDataChanged, self);
        AGS3xIoTAdmin.util.events.on('deviceAliasChanged', self.updateDeviceAlias, self);
        
    }
});

function closeFullscreenInfoWindow(infoWindow) {
    infoWindow.destroy();
}