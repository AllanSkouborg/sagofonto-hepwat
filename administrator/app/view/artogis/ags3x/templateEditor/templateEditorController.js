Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.templateEditor.templateEditorController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xTemplateEditor',

    requires: [
       'AGS3xIoTAdmin.util.events',
       'AGS3xIoTAdmin.util.util'
    ],

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelCalculationsCalculation: 'Beregning',
    fieldLabelCalculationsFormula: 'Formular',
    fieldLabelCalculationsType: 'Type',
    fieldLabelCalculationsData: 'Data',
    fieldLabelCalculationsUnit: 'Enhed',
    fieldLabelCalculationsStore: 'Gem',
    fieldLabelCalculationsAverageTwoMinutes: 'Gennemsnit 2 min.',
    fieldLabelCalculationsAverageSixtyMinutes: 'Gennemsnit 60 min.',
    fieldLabelCalculationsSumSixtyMinutes: 'Sum 60 min.',
    fieldLabelCalculationsOpenModel: 'Åbn beregningsmodel',
    fieldLabelCalculationsOpenFormulaEditor: 'Åbn formulareditor',
    fieldLabelCalculationsAggregationAndStore: 'Aggregering og lagring',
    fieldLabelCalculationsWindowTitle: 'Beregninger',

    fieldLabelDeleteTemplateQuestion: 'Slet skabelon?',
    fieldLabelDeleteTemplateYes: 'Ja',
    fieldLabelDeleteTemplateNo: 'Nej',

    infoTextTemplateCreationSuccess: 'Ny skabelon er oprettet',
    infoTextTemplateCreationError: 'Oprettelse af skabelon er fejlet',
    infoTextTemplateUpdateSuccess: 'Skabelon er opdateret',
    infoTextTemplateUpdateError: 'Opdatering af skabelon er fejlet',
    infoTextTemplateMissingTemplateName: 'Venligst indtast et navn for skabelonen',
    infoTextTemplateDuplicateNameError: 'En skabelon med det navn eksisterer allerede',

    fieldLabelDataLoading: 'Vent venligst...',

    baseUrl: null,

    loadTemplateData: function () {
        var self = this;

        var templatesDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.templateConfigurationsStore');
        console.log('templateEditorController.loadTemplateData - templatesDataStore: ', templatesDataStore);

        if (Ext.ComponentQuery.query('#templateFullMask').length > 0) {
            Ext.ComponentQuery.query('#templateFullMask')[0].destroy();
        }

        //var templatesDataStore = this.getViewModel().getStore('templatesData');
        //console.log('templateEditorController.loadTemplateData - templatesDataStore: ', templatesDataStore);

        //var templatesDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.templateConfigurationsStore');

        //templatesDataStore.setData(result.configurationTemplates);

        /*
        Ext.Ajax.request({
            scope: this,
            url: self.baseUrl + 'measurementtype',
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('templateEditorController.loadTemplateData, measurementTypes - result: ', result);

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

                        var measurementTypesDataStore = this.getViewModel().getStore('measurementTypesDataStore');
                        measurementTypesDataStore.setData(measurementTypesData);

                        Ext.ComponentQuery.query('#templateFullMask')[0].destroy();
                    }
                    catch (exception) {
                        console.log('templateEditorController.loadTemplateData, measurementTypes - EXCEPTION: ', exception);
                    }
                }
            }
        });*/

        /*
        Ext.Ajax.request({
            scope: this,
            url: self.baseUrl + 'templateconfiguration',
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('templateEditorController.loadTemplateData - result: ', result);
                        
                        var data = result.configurationTemplates;

                        var templatesDataStore = this.getViewModel().getStore('templatesData');
                        console.log('templateEditorController.loadTemplateData - templatesDataStore: ', templatesDataStore);

                        //var templatesDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.templateConfigurationsStore');

                        templatesDataStore.setData(result.configurationTemplates);

                        Ext.Ajax.request({
                            scope: this,
                            url: self.baseUrl + 'measurementtype',
                            method: 'GET',
                            contentType: 'application/json',
                            callback: function (options, success, response) {
                                if (success) {
                                    try {
                                        var result = Ext.decode(response.responseText);
                                        console.log('templateEditorController.loadTemplateData, measurementTypes - result: ', result);
                                        
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

                                        var measurementTypesDataStore = this.getViewModel().getStore('measurementTypesDataStore');
                                        measurementTypesDataStore.setData(measurementTypesData);

                                        Ext.ComponentQuery.query('#templateFullMask')[0].destroy();
                                    }
                                    catch(exception) {
                                        console.log('templateEditorController.loadTemplateData, measurementTypes - EXCEPTION: ', exception);
                                    }
                                }
                            }
                        })
                    }
                    catch(exception) {
                        console.log('templateEditorController.loadTemplateData - exception: ', exception);
                        Ext.ComponentQuery.query('#templateFullMask')[0].destroy();
                    }
                }
            }
        });
        */
    },
    templateSelected: function (record) {
        var self = this;

        console.log('templateEditorController.templateSelected - record: ', record);

        var selectedTemplateStore = self.getViewModel().getStore('selectedTemplateStore');
        selectedTemplateStore.setData(record.data.calculationAndStores);
        selectedTemplateStore.sort('calculation', 'ASC');

        console.log('templateEditorController.templateSelected - selectedTemplateStore: ', selectedTemplateStore);

        var aggregationAndStoresStore = self.getViewModel().getStore('selectedAggregationAndStores');
        aggregationAndStoresStore.setData(selectedTemplateStore.data.items[0].data.aggregationAndStores);
        aggregationAndStoresStore.sort('aggregationType', 'ASC');

        // set measurement combobox
        Ext.ComponentQuery.query('#templateEditorMeasurementTypeCombobox')[0].setValue(record.data.measurementType);

        // reset overall unit combobox
        Ext.ComponentQuery.query('#templateEditorCalculationAllUnitInput')[0].setDisabled(false);
        Ext.ComponentQuery.query('#templateEditorCalculationAllUnitInput')[0].setValue(null);

        // activate first record
        Ext.ComponentQuery.query('#templateEditorCalculationsGridpanel')[0].getSelectionModel().select(0)
        document.getElementById('templateEditorAggregationsAndStoresTarget').innerHTML = '(None)';
        document.getElementById('templateEditorCalculationNameInput-inputEl').value = record.data.measurementName;

        // activate buttons and combo boxes
        Ext.ComponentQuery.query('#buttonDeleteTemplate')[0].setDisabled(false);
        Ext.ComponentQuery.query('#buttonTemplateEditorSaveAsNewTemplate')[0].setDisabled(false);
        Ext.ComponentQuery.query('#templateEditorCalculationNameInput')[0].setDisabled(false);
        Ext.ComponentQuery.query('#templateEditorMeasurementTypeCombobox')[0].setDisabled(false);

        if (record.data.id != null) {
            Ext.ComponentQuery.query('#buttonTemplateEditorUpdateTemplate')[0].setDisabled(false);
        }
    },
    templateCalculationSelected: function(record) {
        var self = this;

        console.log('templateEditorController.templateCalculationSelected - record: ', record);

        var aggregationAndStoresData = record.data.aggregationAndStores;

        console.log('templateEditorController.templateCalculationSelected - aggregationAndStoresData: ', aggregationAndStoresData);

        var aggregationAndStoresStore = self.getViewModel().getStore('selectedAggregationAndStores');
        aggregationAndStoresStore.setData(aggregationAndStoresData);

        document.getElementById('templateEditorAggregationsAndStoresTarget').innerHTML = '(' + record.data.calculationType + ')';
    },
    measurementSelected: function(value) {
        var self = this;

        console.log('templateEditorController.measurementSelected - value: ', value);

        var measurementsData = self.getViewModel().getStore('measurementsData');
        console.log('templateEditorController.measurementSelected - measurementsData: ', measurementsData);

        for (var i = 0; i < measurementsData.data.items.length; i++) {
            var measurement = measurementsData.data.items[i].data;

            if (measurement.measurementType == value) {
                console.log('templateEditorController.measurementSelected - HIT: ', measurement);
                break;
            }
        }

        var selectedTemplateStore = self.getViewModel().getStore('selectedTemplateStore');
        console.log('templateEditorController.measurementSelected - selectedTemplateStore: ', selectedTemplateStore);
    },
    overallUnitSelected: function (component, value) {
        var self = this;

        console.log('templateEditorController.overallUnitSelected - component: ', component);
        console.log('templateEditorController.overallUnitSelected - value: ', value);

        if (component.id == 'templateEditorCalculationAllUnitInput') {

            var selectedTemplateStore = self.getViewModel().getStore('selectedTemplateStore');
            console.log('templateEditorController.overallUnitSelected - selectedTemplateStore, before: ', selectedTemplateStore);

            var calculations = selectedTemplateStore.data.items;

            for (var i = 0; i < calculations.length; i++) {
                var calculation = calculations[i].data;
                var aggregationAndStores = calculation.aggregationAndStores;
                for (var a = 0; a < aggregationAndStores.length; a++) {
                    var aggregationAndStore = aggregationAndStores[a];
                    aggregationAndStore.unit = value;
                }
            }

            console.log('templateEditorController.overallUnitSelected - selectedTemplateStore, after: ', selectedTemplateStore);

            var selectedAggregationAndStores = self.getViewModel().getStore('selectedAggregationAndStores');
            console.log('templateEditorController.overallUnitSelected - selectedAggregationAndStores, before: ', selectedAggregationAndStores);
        
            var newData = [];

            for (var s = 0; s < selectedAggregationAndStores.data.items.length; s++) {
                var selectedAggregationAndStore = selectedAggregationAndStores.data.items[s].data;
                selectedAggregationAndStore.unit = value;
                newData.push(selectedAggregationAndStore);
            }

            selectedAggregationAndStores.setData(newData);
            console.log('templateEditorController.overallUnitSelected - selectedAggregationAndStores, after: ', selectedAggregationAndStores);
        }
        
    },
    deleteTemplate: function () {
        var self = this;

        console.log('templateEditorController.deleteTemplate');

        var record = Ext.ComponentQuery.query('#templateEditorTemplatesList')[0].getSelection()[0].data;
        console.log('templateEditorController.deleteTemplate - record: ', record);

        self.templateDeletionWindow = Ext.create('Ext.window.Window', {
            id: 'templateRemovalWindow',
            header: {
                title: self.fieldLabelDeleteTemplateQuestion,
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important',
                    'padding': '12px'
                }
            },
            closable: true,
            layout: 'fit',
            width: 210,
            padding: 10,
            x: ((window.innerWidth - 400) / 2),
            y: ((window.innerHeight - 400) / 2),
            layout: 'fit',
            frame: false,
            modal: true,
            maskClickAction: 'destroy',
            //shadow: false,
            renderTo: document.body,
            items: [
                {
                    xtype: 'panel',
                    items: [
                        {
                            xtype: 'button',
                            text: self.fieldLabelDeleteTemplateYes,
                            style: {
                                'float': 'right'
                            },
                            width: 90,
                            heigh: 25,
                            listeners: {
                                click: function () {
                                    self.acceptDeleteTemplate(record, self.templateDeletionWindow)
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            text: self.fieldLabelDeleteTemplateNo,
                            style: {
                                'float': 'left'
                            },
                            width: 90,
                            heigh: 25,
                            listeners: {
                                click: function () {
                                    self.cancelDeleteTemplate(self.templateDeletionWindow)
                                }
                            }

                        }
                    ]
                }
            ]
        }).show();
    },
    acceptDeleteTemplate: function(record, promptWindow) {
        var self = this;

        // delete template
        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'templateconfiguration/' + record.id,
            method: 'DELETE',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('templateEditorController.acceptDeleteTemplate - result: ', result);

                        promptWindow.destroy();

                        self.resetTemplateEditor();

                        var templateConfigurationsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.templateConfigurationsStore');

                        templateConfigurationsStore.load(function (records, operation, success) {
                            if (success) {
                                try {
                                    console.log('templateEditorController.loadTemplateConfigurations - records: ', records);
                                    console.log('templateEditorController.loadTemplateConfigurations - operation: ', operation);

                                    if (records.length > 0) {

                                        var records = records[0].data.configurationTemplates;
                                        var length = records.length;

                                        var dataArray = [];

                                        for (var i = 0; i < length; i++) {
                                            var configuredTemplateObject = records[i];

                                            dataArray.push(configuredTemplateObject);
                                        }

                                        templateConfigurationsStore.loadData(dataArray);
                                    }
                                }
                                catch (exception) {
                                    console.log('templateEditorController.loadTemplateConfigurations - EXCEPTION: ', exception);
                                }
                            }
                            else {
                                console.log('templateEditorController.loadTemplateConfigurations - failure, operation: ', operation);
                            }
                        });

                    }
                    catch (exception) {
                        console.log('templateEditorController.acceptDeleteTemplate - exception', exception);
                    }
                }
            }
        });
    },
    cancelDeleteTemplate: function (promptWindow) {
        var self = this;

        promptWindow.destroy();
    },
    startNewTemplate: function () {
        var self = this;

        Ext.ComponentQuery.query('#templateEditorTemplatesList')[0].getSelectionModel().deselectAll();
        Ext.ComponentQuery.query('#templateEditorCalculationNameInput')[0].setDisabled(false);
        Ext.ComponentQuery.query('#templateEditorCalculationNameInput')[0].focus();

        Ext.ComponentQuery.query('#templateEditorMeasurementTypeCombobox')[0].setDisabled(false);
        Ext.ComponentQuery.query('#templateEditorMeasurementTypeCombobox')[0].setValue(null);

        Ext.ComponentQuery.query('#templateEditorCalculationAllUnitInput')[0].setDisabled(false);
        Ext.ComponentQuery.query('#templateEditorCalculationAllUnitInput')[0].setValue(null);

        var emptyTemplate = self.getViewModel().emptyTemplate;
        console.log('templateEditorController.emptyTemplate: ', emptyTemplate);

        var selectedTemplateStore = self.getViewModel().getStore('selectedTemplateStore');
        selectedTemplateStore.setData(emptyTemplate.calculationAndStores);

        var aggregationAndStoresStore = self.getViewModel().getStore('selectedAggregationAndStores');
        aggregationAndStoresStore.setData(emptyTemplate.calculationAndStores[0].aggregationAndStores);

        document.getElementById('templateEditorCalculationNameInput-inputEl').value = '';

        // activate first record
        Ext.ComponentQuery.query('#templateEditorCalculationsGridpanel')[0].getSelectionModel().select(0)
        document.getElementById('templateEditorAggregationsAndStoresTarget').innerHTML = '(None)';

        Ext.ComponentQuery.query('#buttonTemplateEditorSaveAsNewTemplate')[0].setDisabled(false);
        Ext.ComponentQuery.query('#buttonTemplateEditorUpdateTemplate')[0].setDisabled(true);
        
    },
    updateTemplate: function () {
        var self = this;

        var record = Ext.ComponentQuery.query('#templateEditorTemplatesList')[0].getSelection()[0].data;
        record.measurementName = Ext.ComponentQuery.query('#templateEditorCalculationNameInput')[0].getValue();
        record.measurementType = Ext.ComponentQuery.query('#templateEditorMeasurementTypeCombobox')[0].getValue();
        record.measurementAlias = Ext.ComponentQuery.query('#templateEditorMeasurementTypeCombobox')[0].getDisplayValue();

        console.log('templateEditorController.updateTemplate - record: ', record);

        delete record.measurementGuid;

        for (var i = 0; i < record.calculationAndStores.length; i++ ) {
            var calcAndStore = record.calculationAndStores[i];
            delete calcAndStore.id;
            

            for (var x = 0; x < calcAndStore.aggregationAndStores.length; x++ ) {
                var aggAndStore = calcAndStore.aggregationAndStores[x];
                delete aggAndStore.id;
            }
        }

        console.log('templateEditorController.updateTemplate - record update data: ', record);

        var loadingMask = new Ext.LoadMask(
            {
                msg: self.fieldLabelDataLoading,
                id: 'templateUpdateMask',
                target: Ext.ComponentQuery.query('#ags3xTemplateEditor')[0]
            }
        );

        loadingMask.show();

        // update template configuration
        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'templateconfiguration',
            method: 'PUT',
            contentType: 'application/json',
            jsonData: Ext.JSON.encode(record),
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('templateEditorController.updateTemplate - result: ', result);

                        AGS3xIoTAdmin.util.util.errorDlg('Template', self.infoTextTemplateUpdateSuccess, 'INFO');
                        Ext.ComponentQuery.query('#templateUpdateMask')[0].destroy();
                    }
                    catch (exception) {
                        console.log('templateEditorController.updateTemplate - exception: ', exception);
                        AGS3xIoTAdmin.util.util.errorDlg('Template', self.infoTextTemplateUpdateError, 'ERROR');
                        Ext.ComponentQuery.query('#templateUpdateMask')[0].destroy();
                    }
                }
                else {
                    console.log('templateEditorController.updateTemplate - failure: ', response);
                    AGS3xIoTAdmin.util.util.errorDlg('Template', self.infoTextTemplateUpdateError, 'ERROR');
                    Ext.ComponentQuery.query('#templateUpdateMask')[0].destroy();
                }
            }
        });

    },
    saveAsNewTemplate: function () {
        var self = this;

        var record = self.getViewModel().getStore('selectedTemplateStore');

        var nameExists = false;
        var templatesDataStore = self.getViewModel().getStore('templatesData');
        for (var x = 0; x < templatesDataStore.data.items.length; x++) {
            var template = templatesDataStore.data.items[x].data;
            var measurementName = template.measurementName;
            if (Ext.ComponentQuery.query('#templateEditorCalculationNameInput')[0].getValue().toLowerCase() == measurementName.toLowerCase()) {
                console.log('templateEditorController.saveAsNewTemplate - name already exists, template: ', template);
                nameExists = true;
                break;
            }
            
        }

        if (nameExists == false) {
            console.log('templateEditorController.saveAsNewTemplate - record: ', record);

            console.log(document.getElementById('templateEditorCalculationNameInput-inputEl').value.length);

            // check for template name
            if(document.getElementById('templateEditorCalculationNameInput-inputEl').value.length == 0) {
                AGS3xIoTAdmin.util.util.errorDlg('Template', self.infoTextTemplateMissingTemplateName, 'ERROR');
                document.getElementById('templateEditorCalculationNameInput-inputEl').focus();
            }
            else {
                var guid = AGS3xIoTAdmin.util.util.generateUUID();
                var measurementName = Ext.ComponentQuery.query('#templateEditorCalculationNameInput')[0].getValue();
                var measurementType = Ext.ComponentQuery.query('#templateEditorMeasurementTypeCombobox')[0].getValue(); // selection
                var measurementAlias = Ext.ComponentQuery.query('#templateEditorMeasurementTypeCombobox')[0].getDisplayValue(); // selection
                var templateType; // auto-increment

                var calculationsAndStores = [];

                for (var i = 0; i < record.data.items.length; i++ ) {
                    var calculationAndStoreItem = record.data.items[i].data;
                    delete calculationAndStoreItem.id;
                    calculationsAndStores.push(calculationAndStoreItem);

                    var aggregationAndStores = calculationAndStoreItem.aggregationAndStores;
                    for (var a = 0; a < aggregationAndStores.length; a++) {
                        var aggregationAndStoreItem = aggregationAndStores[a];
                        delete aggregationAndStoreItem.id;
                    }
                }

                var params = {
                    measurementName: measurementName,
                    measurementAlias: measurementAlias,
                    measurementType: measurementType,
                    calculationAndStores: calculationsAndStores
                }

                console.log('templateEditorController.saveAsNewTemplate - params: ', params);

                var loadingMask = new Ext.LoadMask(
                    {
                        msg: self.fieldLabelDataLoading,
                        id: 'templateSaveNewMask',
                        target: Ext.ComponentQuery.query('#ags3xTemplateEditor')[0]
                    }
                );

                loadingMask.show();

                Ext.Ajax.request({
                    scope: self,
                    url: self.baseUrl + 'templateconfiguration',
                    method: 'POST',
                    contentType: 'application/json',
                    jsonData: Ext.JSON.encode(params),
                    callback: function (options, success, response) {
                        if (success) {
                            try {
                                var result = Ext.decode(response.responseText);
                                console.log('templateEditorController.saveAsNewTemplate - result: ', result);
                                var newIdObject = Ext.decode(result.JsonObject)
                                console.log('templateEditorController.saveAsNewTemplate - newIdObject: ', newIdObject);

                                var templatesData = self.getViewModel().getStore('templatesData');

                                params.id = newIdObject.configurationId;
                                templatesData.add(params);

                                console.log('templateEditorController.saveAsNewTemplate - templatesData (after): ', templatesData);

                                Ext.ComponentQuery.query('#templateEditorTemplatesList')[0].getSelectionModel().select(templatesData.data.items.length - 1);

                                var templatesList = document.getElementById('templateEditorTemplatesList-body').getElementsByClassName('x-scroller')[0];
                                console.log('templateEditorController.saveAsNewTemplate - templatesList: ', templatesList);
                                templatesList.scrollTop = templatesList.scrollHeight;

                                Ext.ComponentQuery.query('#buttonTemplateEditorUpdateTemplate')[0].setDisabled(false);
                                Ext.ComponentQuery.query('#buttonDeleteTemplate')[0].setDisabled(false);

                                AGS3xIoTAdmin.util.util.errorDlg('Template', self.infoTextTemplateCreationSuccess, 'INFO');

                                Ext.ComponentQuery.query('#templateSaveNewMask')[0].destroy();

                                var measurementTemplatesStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.measurementTemplatesStore');
                                console.log('templateEditorController.saveAsNewTemplate - measurementTemplatesStore, before: ', measurementTemplatesStore);

                                measurementTemplatesStore.load(function (records, operation, success) {
                                    if (success) {
                                        try {
                                            console.log('templateEditorController.saveAsNewTemplate - records: ', records);
                                            console.log('templateEditorController.saveAsNewTemplate - operation: ', operation);

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
                                                measurementTemplatesStore.sort('measurementName', 'ASC');

                                                console.log('templateEditorController.saveAsNewTemplate - measurementTemplatesStore, after: ', measurementTemplatesStore);
                                            }
                                        }
                                        catch (exception) {
                                            console.log('templateEditorController.saveAsNewTemplate - EXCEPTION: ', exception);
                                        }
                                    }
                                    else {
                                        console.log('templateEditorController.saveAsNewTemplate - failure, operation: ', operation);
                                    }
                                });

                                
                            }
                            catch (exception) {
                                console.log('templateEditorController.saveAsNewTemplate - exception: ', exception);
                                AGS3xIoTAdmin.util.util.errorDlg('Template', self.infoTextTemplateCreationError, 'ERROR');
                                Ext.ComponentQuery.query('#templateSaveNewMask')[0].destroy();
                            }
                        }
                    }
                });
            }  
        }
        else {
            AGS3xIoTAdmin.util.util.errorDlg('Template', self.infoTextTemplateDuplicateNameError, 'ERROR');
        }
    },
    resetTemplateEditor: function () {
        var self = this;

        Ext.ComponentQuery.query('#buttonDeleteTemplate')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonTemplateEditorUpdateTemplate')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonTemplateEditorSaveAsNewTemplate')[0].setDisabled(true);
        
        document.getElementById('templateEditorCalculationNameInput-inputEl').value = '';
        Ext.ComponentQuery.query('#templateEditorCalculationNameInput')[0].setDisabled(true);

        Ext.ComponentQuery.query('#templateEditorMeasurementTypeCombobox')[0].setDisabled(true);
        Ext.ComponentQuery.query('#templateEditorMeasurementTypeCombobox')[0].setValue(null);

        Ext.ComponentQuery.query('#templateEditorCalculationAllUnitInput')[0].setDisabled(true);
        Ext.ComponentQuery.query('#templateEditorCalculationAllUnitInput')[0].setValue(null);

        var selectedTemplateStore = self.getViewModel().getStore('selectedTemplateStore');
        selectedTemplateStore.setData([]);

        var selectedAggregationAndStores = self.getViewModel().getStore('selectedAggregationAndStores');
        selectedAggregationAndStores.setData([]);

        document.getElementById('templateEditorAggregationsAndStoresTarget').innerHTML = '';

        //self.loadTemplateData();
    },
    openFormulaEditor: function (index, dataSource, domElement, objectDataVariables) {
        var self = this;

        console.log('templateEditorController.openFormulaEditor - index: ', index);
        console.log('templateEditorController.openFormulaEditor - dataSource: ', dataSource);
        console.log('templateEditorController.openFormulaEditor - domElement: ', domElement);
        console.log('templateEditorController.openFormulaEditor - objectDataVariables: ', objectDataVariables);

        var transferData = {
            formulaString: dataSource.data.items[index].data.formula,
            dataTarget: {
                object: dataSource.data.items[index].data,
                key: 'formula'
            },
            domTarget: domElement,
            objectDataVariables: objectDataVariables
        }

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
    loadUnitTypes: function() {
        var self = this;

        console.log('templateEditorController.loadUnitTypes - url: ', self.baseUrl + 'unit');

        // get unit types
        Ext.Ajax.request({
            scope: this,
            url: self.baseUrl + 'unit',
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('templateEditorController.loadUnitTypes - result: ', result);

                        var language = (AGS3xIoTAdmin.util.util.getUrlParameterByName('locale')) ? AGS3xIoTAdmin.util.util.getUrlParameterByName('locale') : 'da';
                        console.log('templateEditorController.loadUnitTypes - language: ', language);

                        var data = [];

                        for (var i = 0; i < result.units.length; i++) {
                            var unit = result.units[i];

                            if( unit.language == language ) {
                                data.push(unit);
                            }
                        }

                        var unitTypesStore = self.getViewModel().getStore('unitTypesStore');
                        unitTypesStore.setData(data);

                        var unitTypesStoreAll = self.getViewModel().getStore('unitTypesStoreAll');
                        unitTypesStoreAll.setData(data);
                    }
                    catch (exception) {
                        console.log('templateEditorController.loadUnitTypes - exception: ', exception);
                    }
                }
            }
        });
    },
    toggleStatusMonitoring: function (editor, context, options) {
        var self = this;

        var index = context.rowIdx;

        console.log('templateEditorController.toggleStatusMonitoring - editor: ', editor);
        console.log('templateEditorController.toggleStatusMonitoring - context: ', context);
        console.log('templateEditorController.toggleStatusMonitoring - options: ', options);

        var storeData = context.store.data.items[index].data;
        console.log('templateEditorController.toggleStatusMonitoring - storeData: ', storeData);

        var statusIconElement = document.getElementById('templateEditorStatusGridPanel').getElementsByClassName('open-status-button')[index].getElementsByClassName('fa')[0];
        console.log('templateEditorController.toggleStatusMonitoring - statusIconElement: ', statusIconElement);

        if (
            (storeData.max != null && storeData.max.length > 0) ||
            (storeData.high != null && storeData.high.length > 0) ||
            (storeData.low != null && storeData.low.length > 0) ||
            (storeData.min != null && storeData.min.length > 0)
            ) {
            console.log('templateEditorController.toggleStatusMonitoring - turn status on');

            storeData.statusOn = true;

            statusIconElement.className = 'fa fa-bell';
        }
        else {
            console.log('templateEditorController.toggleStatusMonitoring - turn status off');

            storeData.statusOn = false;

            statusIconElement.className = 'fa fa-bell-slash-o';
        }
    },
    init: function () {
        var self = this;
        
        self.baseUrl = AGS3xIoTAdmin.systemData.serviceUrl;

        //self.loadTemplateData();
        //self.loadUnitTypes();

        // set calculation grid columns
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
                        var domElement = document.getElementById(component.id).getElementsByClassName('x-grid-item')[index].getElementsByTagName('td')[1].getElementsByTagName('div')[0];
                        self.openFormulaEditor(index, component.dataSource, domElement, null);
                    }
                }
            }
        ]

        self.getViewModel().set('calculationGridColumns', calculationGridColumns);

        // Event listeners
        AGS3xIoTAdmin.util.events.on('templateEditorMeasurementSelected', self.measurementSelected, self);
        AGS3xIoTAdmin.util.events.on('templateEditorOverallUnitSelected', self.overallUnitSelected, self);
        AGS3xIoTAdmin.util.events.on('templateEditorToggleStatusMonitoring', self.toggleStatusMonitoring, self);
        AGS3xIoTAdmin.util.events.on('templateEditorTemplateSelected', self.templateSelected, self);
        AGS3xIoTAdmin.util.events.on('templateEditorTemplateCalculationSelected', self.templateCalculationSelected, self);

    }
});