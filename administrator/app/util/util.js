Ext.define('AGS3xIoTAdmin.util.util', {
    singleton: true,
    generateUUID: function () {
        var d = new Date().getTime();
        if (window.performance && typeof window.performance.now === "function") {
            d += performance.now();;
        }
        var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
        return uuid;
    },
    errorDlg: function (dlgTitle, errMessage, errType) {
        switch (errType) {
            case 'WARNING': iconBoxType = Ext.MessageBox.WARNING; break;
            case 'ERROR': iconBoxType = Ext.MessageBox.ERROR; break;
            case 'QUESTION': iconBoxType = Ext.MessageBox.QUESTION; break;
            case 'INFO': iconBoxType = Ext.MessageBox.INFO; break;
        };

        Ext.MessageBox.show({
            title: dlgTitle,
            msg: errMessage,
            icon: iconBoxType,
            buttons: Ext.Msg.OK
        });
    },
    getMeasurementTypeFromTemplateType: function (templateType, viewModel) {
        var self = this;
        var measurementTemplateTypeStore = viewModel.getStore('measurementTemplateType');

        console.log('util.getMeasurementTypeFromTemplateType - templateType: ', templateType);
        console.log('util.getMeasurementTypeFromTemplateType - measurementTemplateTypeStore: ', measurementTemplateTypeStore);

        for (var i = 0; i < measurementTemplateTypeStore.data.items.length; i++) {
            var item = measurementTemplateTypeStore.data.items[i].data;
            if (templateType == item.templateType) {
                measurementType = item.measurementType;
                console.log('util.getMeasurementTypeFromTemplateType - result: ', measurementType);
                return measurementType;
            }
        }

        return null;
    },
    getMeasurementAliasFromTemplateType: function (templateType, viewModel) {
        var self = this;
        var measurementTemplateTypeStore = viewModel.getStore('measurementTemplateType');

        console.log('util.getMeasurementAliasFromTemplateType - templateType: ', templateType);
        console.log('util.getMeasurementAliasFromTemplateType - measurementTemplateTypeStore: ', measurementTemplateTypeStore);

        for (var i = 0; i < measurementTemplateTypeStore.data.items.length; i++) {
            var item = measurementTemplateTypeStore.data.items[i].data;
            if (templateType == item.templateType) {
                measurementAlias = item.measurementAlias;
                console.log('util.getMeasurementAliasFromTemplateType - result: ', measurementAlias);
                return measurementAlias;
            }
        }

        return null;
    },
    getMeasurementNameFromTemplateType: function (templateType, viewModel) {
        var self = this;
        var measurementTemplateTypeStore = viewModel.getStore('measurementTemplateType');

        console.log('util.getMeasurementNameFromTemplateType - templateType: ', templateType);
        console.log('util.getMeasurementNameFromTemplateType - measurementTemplateTypeStore: ', measurementTemplateTypeStore);

        for (var i = 0; i < measurementTemplateTypeStore.data.items.length; i++) {
            var item = measurementTemplateTypeStore.data.items[i].data;
            if (templateType == item.templateType) {
                measurementName = item.measurementName;
                console.log('util.getMeasurementNameFromTemplateType - result: ', measurementName);
                return measurementName;
            }
        }

        return null;
    },
    getMeasurementTypeNameFromId: function (id, measurementTypesArray) {
        var self = this;
        var result = null;

        for (var i = 0; i < measurementTypesArray.length; i++) {
            if (measurementTypesArray[i].id == id) {
                return measurementTypesArray[i].name;
            }
        }

        return result;
    },
    setConfiguredDataRelations: function (viewModel) {
        var self = this;
        var configuredDataStore = viewModel.getStore('configuredDataStore');
        console.log('util.setConfiguredDataRelations - configuredDataStore (before): ', configuredDataStore);
    },
    setConfiguredDataCalculationAndStores: function (calculationAndStores, viewModel) {
        var self = this;
        var configuredDataStore = viewModel.getStore('configuredDataStore');

        console.log('util.setConfiguredDataCalculationAndStores - configuredDataStore (before): ', configuredDataStore);
        console.log('util.setConfiguredDataCalculationAndStores - data to store: ', calculationAndStores);

        configuredDataStore.data.items[0].data.calculationAndStores = calculationAndStores;

        console.log('util.setConfiguredDataCalculationAndStores - configuredDataStore (after): ', configuredDataStore);
    },
    getComponentObject: function (componentType, viewModel) {
        var self = this;
        var componentTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.componentTypesStore');
        var wfsUrl;
        var wfsBaseUrl;

        console.log('util.getComponentObject - componentTypesStore: ', componentTypesStore);

        for (var i = 0; i < componentTypesStore.data.items.length; i++) {
            var componentTypeObject = componentTypesStore.data.items[i].data;
            console.log('util.getComponentObject - componentTypeObject: ', componentTypeObject);

            if (componentTypeObject.type == componentType) {
                if (!componentTypeObject.wfsBaseUrl) {
                    if (componentTypeObject.wfs.indexOf('?') > -1) {
                        componentTypeObject.wfsBaseUrl = componentTypeObject.wfs.split('?')[0];
                    }
                }
                return componentTypeObject;
            }
        }

        return null;
    },
    getDataStoreName: function (datastoreId, viewModel) {
        var dataStoresStore = viewModel.getStore('dataStoresDataStore');

        for (var i = 0; i < dataStoresStore.data.items.length; i++) {
            var dataStore = dataStoresStore.data.items[i].data;

            if (dataStore.id == datastoreId) {
                return dataStore.name;
            }
        }

        return null;
    },
    getTemplateConfigurationFromMeasurementType: function(measurementTypeId, viewModel) {
        var templateConfigurationsStore = viewModel.getStore('templatesConfiguration');

        for (var i = 0; i < templateConfigurationsStore.data.items.length; i++) {
            var templateConfig = templateConfigurationsStore.data.items[i].data;

            if (templateConfig.measurementType == measurementTypeId) {
                return templateConfig;
            }
        }

        return null;
    },
    getUrlParameterByName: function (name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    },
    getFeatureTypeDescriptionUrl: function (url) {
        console.log('util.getFeatureTypeDescriptionUrl - url: ', url);
        var self = this;
        var baseUrl = url.split('?')[0];
        var service = self.getUrlParameterByName('service', url);
        var version = self.getUrlParameterByName('version', url);
        var typeName = self.getUrlParameterByName('typeNames', url);

        return baseUrl + '?service=' + service + '&version=' + version + '&request=DescribeFeatureType&typeName=' + typeName;
    },
    checkTagIsNumber: function (nodeName, featureDescriptionTags) {
        nodeName = nodeName.split(':')[1];

        for( var i = 0; i < featureDescriptionTags.length; i++ ) {
            var featureDescription = featureDescriptionTags[i];
            var featureDescriptionName = featureDescription.getAttribute('name');
            var featureDescriptionType = featureDescription.getAttribute('type'); 

            if (featureDescriptionName == nodeName && featureDescriptionType == 'xsd:decimal') {
                return true
            }
        }

        return false;
    },
    getCursorPosition: function (input) {
        if ("selectionStart" in input && document.activeElement == input) {
            return {
                start: input.selectionStart,
                end: input.selectionEnd
            };
        }
        else if (input.createTextRange) {
            var sel = document.selection.createRange();
            if (sel.parentElement() === input) {
                var rng = input.createTextRange();
                rng.moveToBookmark(sel.getBookmark());
                for (var len = 0;
                         rng.compareEndPoints("EndToStart", rng) > 0;
                         rng.moveEnd("character", -1)) {
                    len++;
                }
                rng.setEndPoint("StartToStart", input.createTextRange());
                for (var pos = { start: 0, end: len };
                         rng.compareEndPoints("EndToStart", rng) > 0;
                         rng.moveEnd("character", -1)) {
                    pos.start++;
                    pos.end++;
                }
                return pos;
            }
        }
        return -1;
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
    dateConversionDayTime: function (epochTime) {
        var self = this;
        var date = new Date(epochTime);
        var timeString = self.zeroFill(date.getHours() + '') + ':' + self.zeroFill(date.getMinutes() + '') + ':' + self.zeroFill(date.getSeconds() + '');

        var dateString = self.zeroFill(date.getDate() + '') + '/' + self.zeroFill((date.getMonth() + 1) + '') + '/' + date.getFullYear()
        return dateString + ', ' + timeString;
    },
    zeroFill: function (number) {
        if (number.length == 1) {
            number = '0' + number;
        }
        return number;
    },
    getSensorFromStore: function(sensorId, store) {
        var sensorObject;

        for (var i = 0; i < store.data.items.length; i++) {
            var sensorCandidate = store.data.items[i].data;
            if (sensorId == sensorCandidate.id) {
                return sensorCandidate;
            }
        }

        return sensorObject;
    },
    getDeviceAlias: function (deviceId, deviceName) {
        var deviceAlias = deviceName;
        var devicesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.devicesStore');

        var length = devicesStore.data.items.length;

        for (var i = 0; i < length; i++) {
            var deviceCandidate = devicesStore.data.items[i].data;

            if (deviceId == deviceCandidate.deviceId) {
                return deviceCandidate.deviceAlias;
            }
        }

        return deviceAlias;
    },
    getConfiguredDataAsStore: function () {
        var self = this;
        var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');
        var unconfiguredDataStoreLength = unconfiguredDataStore.data.items.length;
        var configuredDataArray = [];
        for (var i = 0; i < unconfiguredDataStoreLength; i++) {
            var item = unconfiguredDataStore.data.items[i].data;
            if(item.configured == true) {
                configuredDataArray.push(item)
            }
        }

        var store = Ext.create('Ext.data.Store', {});
        store.setData(configuredDataArray);
        return store;
    },
    getDataSourceNameFromId: function(dataSourceId) {
        var result = null;

        var robotsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.robotsStore');

        for (var i = 0; i < robotsStore.data.items.length; i++) {
            var robot = robotsStore.data.items[i].data;
            if (robot.id == dataSourceId) {

                return robot.name;
            }
        }

        return result;
    },
    getRelatedSensors: function (sensorId, unconfiguredDataStore) {
        var relatedSensorsList = [];
        var sensorObjectId;

        var length = unconfiguredDataStore.data.items.length;

        for (var i = 0; i < length; i++) {
            var item = unconfiguredDataStore.data.items[i].data;

            if(item.dataId == sensorId) {
                sensorObjectId = item.sensorId;
                break;
            }
        }

        for (var i = 0; i < length; i++) {
            var item = unconfiguredDataStore.data.items[i].data;

            if (item.sensorId == sensorObjectId && item.dataId != sensorId) {
                relatedSensorsList.push(item);
            }
        }

        return relatedSensorsList;
    },
    getMeasurementTypeFromTemplate: function(templateType) {
        var templateConfigurationsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.templateConfigurationsStore');
        var templateConfigurationsStoreLength = templateConfigurationsStore.data.items.length;

        for (var i = 0; i < templateConfigurationsStoreLength; i++ ) {
            var templateConfiguration = templateConfigurationsStore.data.items[i].data;
            if (templateConfiguration.templateType == templateType) {
                return templateConfiguration.measurementType;
            }
        }

        return null;
    },
    getObjectTypeFromLayerName: function(wfsLayer) {

        var objectTypesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.objectTypesStore');
        var objectTypesStoreLength = objectTypesStore.data.items.length;

        for(var i = 0; i < objectTypesStoreLength; i++) {
            var objectType = objectTypesStore.data.items[i].data;

            if(objectType.wfsLayer == wfsLayer) {
                return objectType;
            }
        }

        return null;
    },
    performSpecialAction: function () {
        console.log('util.performSpecialAction - TEST');
    }
});