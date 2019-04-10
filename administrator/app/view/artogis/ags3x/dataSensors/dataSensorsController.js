Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dataSensors.dataSensorsController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xDataSensors',

    requires: [
        'AGS3xIoTAdmin.util.events',
        'AGS3xIoTAdmin.util.util'
    ],

    // text content - default is Danish (da), for translations, see locale folder
    textRobotsSensorsStatusRunning: 'Sensor kører som forventet.',
    textRobotsSensorsStatusStopped: 'Sensor er stoppet. Tjek for eventuelle fejl eller ventende genstart.',
    textRobotsSensorsStatusUnknown: 'Sensor status er ukendt.',
    textRobotsSensorsStatusWarning: 'Sensor kører, men intervallerne bliver ikke overholdt. Tjek sensor for eventuelle fejl.',

    state: {
        baseUrl: ''
    },

    fetchSensorsData: function () {
        var self = this;

        var measurementTypesUrl = self.state.baseUrl + 'measurementtype';

        Ext.Ajax.request({
            scope: this,
            url: measurementTypesUrl,
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dataIOController.fetchSensorsData, measurement types - result: ', result);

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

                        console.log('dataIOController.fetchSensorsData, measurement types - measurementTypesData: ', measurementTypesData);

                        Ext.Ajax.request({
                            scope: self,
                            url: self.state.baseUrl + 'unconfigureddataio',
                            measurementTypesData: measurementTypesData,
                            method: 'GET',
                            contentType: 'application/json',
                            callback: function (options, success, response) {
                                if (success) {
                                    try {
                                        var result = Ext.decode(response.responseText);
                                        console.log('dataSensorsController.fetchSensorsData - result: ', result);
                                        var store = self.getViewModel().getStore('dataSensorsDataStore');
                                        var data = [];
                                        for (var i = 0; i < result.unConfiguredDataIos.length; i++) {
                                            var item = result.unConfiguredDataIos[i];
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
                                                lastValue: item.lastValue
                                            };
                                            data.push(record);

                                        }
                                        store.setData(data);

                                        var gridPanel = Ext.ComponentQuery.query('#fullSensorListDataTable')[0];
                                        gridPanel.reconfigure(store);

                                        if (Ext.ComponentQuery.query('#sensorsListFullMask').length > 0) {
                                            Ext.ComponentQuery.query('#sensorsListFullMask')[0].destroy();
                                        }
                                    }
                                    catch (ex) {
                                        console.log('dataSensorsController.fetchSensorsData - EXCEPTION: ', ex);
                                        Ext.ComponentQuery.query('#sensorsListFullMask')[0].destroy();
                                    }
                                }
                                else {
                                    console.log('dataSensorsController.fetchSensorsData - FAILURE: ', response);
                                    Ext.ComponentQuery.query('#sensorsListFullMask')[0].destroy();
                                }
                            }

                        });
                    }
                    catch (exception) {
                        console.log('dataIOController.fetchSensorsData, measurement types - EXCEPTION: ', exception);
                    }
                }
            }
        });
    },
    refreshdata: function () {
        var self = this;

        console.log('dataSensorsController.refreshData');

        self.fetchSensorsData();
    },
    getTooltipText: function (status) {
        status = (status != null) ? status.toLowerCase() : 'unknown';

        switch (status) {
            case 'operating':
                return this.textRobotsSensorsStatusRunning;
            case 'stopped':
                return this.textRobotsSensorsStatusStopped;
            case 'unknown':
                return this.textRobotsSensorsStatusUnknown;
            case 'warning':
                return this.textRobotsSensorsStatusWarning
            default:
                return this.textRobotsSensorsStatusUnknown;
        }
    },
    timeConversion: function timeConversion(millisec) {

        var seconds = (millisec / 1000).toFixed(1);

        var minutes = (millisec / (1000 * 60)).toFixed(1);

        var hours = (millisec / (1000 * 60 * 60)).toFixed(1);

        var days = (millisec / (1000 * 60 * 60 * 24)).toFixed(1);

        if (seconds < 60) {
            return seconds + " Sec";
        } else if (minutes < 60) {
            return minutes + " Min";
        } else if (hours < 24) {
            return hours + " Hrs";
        } else {
            return days + " Days"
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
    init: function () {
        var self = this;

        self.state.baseUrl = AGS3xIoTAdmin.systemData.serviceUrl;

        console.log('dataSensorsController.init...');
        console.log('dataSensorsController.init - AGS3xIoTAdmin.systemData.serviceUrl: ', AGS3xIoTAdmin.systemData.serviceUrl);
    }
});