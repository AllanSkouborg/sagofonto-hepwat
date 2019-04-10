Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.componentEditor.componentEditorController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xComponentEditor',

    requires: [
       'AGS3xIoTAdmin.util.events',
       'AGS3xIoTAdmin.util.util'
    ],

    baseUrl: null,
    newComponent: false, // flag user to determine update or create action
    
    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelRemoveComponentQuestion: 'Fjern komponent?',
    fieldLabelRemoveComponentYes: 'Ja',
    fieldLabelRemoveComponentNo: 'Nej',

    fieldLabelDataLoading: 'Vent venligts...',

    fetchComponentTypesData: function () {
        var self = this;

        console.log('componenttEditorController.fetchComponentTypesData...');

        var dataStoreData = [];

        // load datastore, for population of combobox
        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'datastore',
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        var dataStoresDataStore = self.getViewModel().getStore('dataStoresDataStore');

                        console.log('componenetEditorController.fetchComponentTypesData - fetchComponentTypesData: ', dataStoresDataStore);

                        for (var i = 0; i < result.dataStores.length; i++) {
                            var item = result.dataStores[i];
                            var record = {
                                id: item.id,
                                name: item.name
                            };
                            dataStoreData.push(record);
                        }

                        dataStoresDataStore.setData(dataStoreData);

                        var comboBox = Ext.ComponentQuery.query('#componentDataStoreComboBox')[0];

                        // load components to populate existing components list
                        Ext.Ajax.request({
                            scope: self,
                            url: self.baseUrl + 'componenttype',
                            method: 'GET',
                            contentType: 'application/json',
                            callback: function (options, success, response) {
                                if (success) {
                                    try {
                                        var result = Ext.decode(response.responseText);
                                        console.log('componenetEditorController.fetchComponentTypesData - load componenttype data, result: ', result);

                                        var componentTypesDataStore = self.getViewModel().getStore('componentTypesDataStore');

                                        var dataToSave = [];
                                        for (var i = 0; i < result.componentTypes.length; i++) {
                                            var item = result.componentTypes[i];
                                            var record = {
                                                id: item.id,
                                                name: item.name,
                                                type: item.type,
                                                datastoreId: item.datastoreId,
                                                dataStoreName: AGS3xIoTAdmin.util.util.getDataStoreName(item.datastoreId, self.getViewModel()),
                                                componentTableName: item.componentTableName,
                                                wfs: item.wfs,
                                                wfsLayer: item.wfsLayer,
                                                fieldId: item.fieldId,
                                                fieldDescription: item.fieldDescription,
                                                editDate: item.editDate
                                            };
                                            dataToSave.push(record);

                                            // perform sorting according to type ID integer
                                            dataToSave.sort(function (a, b) {
                                                var keyA = new Date(a.type),
                                                    keyB = new Date(b.type);
                                                // Compare the 2 dates
                                                if (keyA < keyB) return -1;
                                                if (keyA > keyB) return 1;
                                                return 0;
                                            });
                                        }
                                        console.log('componenetEditorController.fetchComponentTypesData - load componenttype, data to save: ', dataToSave);
                                        componentTypesDataStore.setData(dataToSave);

                                        var gridPanel = Ext.ComponentQuery.query('#componentEditorDataTable')[0];
                                        gridPanel.reconfigure(componentTypesDataStore);

                                        // hide loading mask
                                        if (Ext.ComponentQuery.query('#componentEditorFullMask').length > 0) {
                                            Ext.ComponentQuery.query('#componentEditorFullMask')[0].destroy();
                                        }
                                    }
                                    catch (ex) {
                                        console.log('componenetEditorController.fetchComponentTypesData - load componenttype data, EXCEPTION: ', ex);

                                        // hide loading mask
                                        if (Ext.ComponentQuery.query('#componentEditorFullMask').length > 0) {
                                            Ext.ComponentQuery.query('#componentEditorFullMask')[0].destroy();
                                        }
                                    }
                                }
                            }
                        });
                    }
                    catch (ex) {
                        console.log('componenetEditorController.fetchComponentTypesData - load datastore data - EXCEPTION: ', ex);

                        // hide loading mask
                        Ext.ComponentQuery.query('#componentEditorFullMask')[0].destroy();
                    }
                }
            }
        });

        
    },
    fetchDataStoresData: function () {
        var self = this;

        console.log('componentEditorController.fetchDataStoresData...');

        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'datastore',
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('componenetEditorController.fetchDataStoresData - load datastore data, result: ', result);

                        var dataStoresDataStore = self.getViewModel().getStore('dataStoresDataStore');

                        console.log('componenetEditorController.fetchDataStoresData - dataStoresDataStore: ', dataStoresDataStore);

                        var dataToSave = [];

                        for (var i = 0; i < result.dataStores.length; i++) {
                            var item = result.dataStores[i];
                            var record = {
                                id: item.id,
                                name: item.name
                            };
                            dataToSave.push(record);
                        }

                        dataStoresDataStore.setData(dataToSave);

                        var comboBox = Ext.ComponentQuery.query('#componentDataStoreComboBox')[0];
                    }
                    catch (ex) {
                        console.log('componenetEditorController.fetchDataStoresData - load datastore data, EXCEPTION: ', ex);
                    }
                }
            }
        });
    },
    saveComponent: function (editor, context, eOpts) {
        console.log('componentEditorController.saveComponent - editor: ', editor);
        console.log('componentEditorController.saveComponent - context: ', context);
        console.log('componentEditorController.saveComponent - eOpts: ', eOpts);

        var self = this;

        if (self.newComponent == true) {
            self.createComponent(editor, context, eOpts);
        }
        else {
            self.updateComponent(editor, context, eOpts);
        }
    },
    createComponent: function (editor, context, eOpts) {
        var self = this;

        var componentTypesDataStore = self.getViewModel().getStore('componentTypesDataStore');
        console.log('componentEditorController.createComponent - componentTypesDataStore: ', componentTypesDataStore);

        var newValues = {
            name: context.newValues.name,
            componentTableName: context.newValues.componentTableName,
            editDate: context.newValues.editDate,
            type: context.newValues.type,
            wfs: context.newValues.wfs,
            wfsLayer: context.newValues.wfsLayer,
            datastoreId: context.record.data.datastoreId,
            id: context.record.data.id
        }

        console.log('componentEditorController.createComponent - newValues: ', newValues);
        console.log('componentEditorController.createComponent - url: ', self.baseUrl + 'componenttype');

        // Add loading mask
        var loadingMask = new Ext.LoadMask(
            {
                msg: self.fieldLabelDataLoading,
                id: 'saveComponentEditorMask',
                target: Ext.ComponentQuery.query('#ags3xComponentEditor')[0]
            }
        );
        loadingMask.show();

        Ext.Ajax.request({
            url: self.baseUrl + 'componenttype',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            jsonData: Ext.JSON.encode(newValues),
            success: function (response) {
                console.log('componenetEditorController.createComponent - load componenttype data, response: ', response);

                self.fetchComponentTypesData();

                Ext.ComponentQuery.query('#buttonRemoveComponent')[0].setDisabled(false);

                self.newComponent = false;

                self.enableToolbar();

                Ext.ComponentQuery.query('#saveComponentEditorMask')[0].destroy();
            },
            failure: function (response) {
                console.log('componenetEditorController.createComponent - load componenttype data, failure: ', response);
                Ext.ComponentQuery.query('#saveComponentEditorMask')[0].destroy();
            }
        });
    },
    updateComponent: function (editor, context, eOpts) {
        var self = this;

        var componentTypesDataStore = self.getViewModel().getStore('componentTypesDataStore');
        console.log('componentEditorController.updateComponent - componentTypesDataStore: ', componentTypesDataStore);

        var newValues = {
            name: context.newValues.name,
            componentTableName: context.newValues.componentTableName,
            editDate: context.newValues.editDate,
            type: context.newValues.type,
            wfs: context.newValues.wfs,
            wfsLayer: context.newValues.wfsLayer,
            fieldId: context.newValues.fieldId,
            fieldDescription: context.newValues.fieldDescription,
            datastoreId: context.record.data.datastoreId,
            id: context.record.data.id
        }

        console.log('componentEditorController.updateComponent - newValues: ', newValues);
        console.log('componentEditorController.updateComponent - url: ', self.baseUrl + 'componenttype');

        // Add loading mask
        var loadingMask = new Ext.LoadMask(
            {
                msg: self.fieldLabelDataLoading,
                id: 'saveComponentEditorMask',
                target: Ext.ComponentQuery.query('#ags3xComponentEditor')[0]
            }
        );
        loadingMask.show();

        Ext.Ajax.request({
            url: self.baseUrl + 'componenttype',
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            jsonData: Ext.JSON.encode(newValues),
            success: function (response) {
                console.log('componentEditorController.updateComponent - load componenttype data, response: ', response);

                self.fetchComponentTypesData();

                Ext.ComponentQuery.query('#buttonRemoveComponent')[0].setDisabled(false);

                self.newComponent = false;

                self.enableToolbar();

                Ext.ComponentQuery.query('#saveComponentEditorMask')[0].destroy();
            },
            failure: function (response) {
                console.log('componentEditorController.updateComponent - load componenttype data, failure: ', response);
                Ext.ComponentQuery.query('#saveComponentEditorMask')[0].destroy();
            }
        });
    },
    removeComponent: function () {
        var self = this;

        self.componentRemovalWindow = Ext.create('Ext.window.Window', {
            id: 'componentRemovalWindow',
            header: {
                title: self.fieldLabelRemoveComponentQuestion,
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
            renderTo: document.body,
            items: [
                {
                    xtype: 'panel',
                    items: [
                        {
                            xtype: 'button',
                            text: self.fieldLabelRemoveComponentYes,
                            style: {
                                'float': 'right'
                            },
                            width: 90,
                            heigh: 25,
                            listeners: {
                                click: function () {
                                    self.acceptRemoveComponent(self.componentRemovalWindow)
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            text: self.fieldLabelRemoveComponentNo,
                            style: {
                                'float': 'left'
                            },
                            width: 90,
                            heigh: 25,
                            listeners: {
                                click: function () {
                                    self.cancelRemoveComponent(self.componentRemovalWindow)
                                }
                            }

                        }
                    ]
                }
            ]
        }).show();
    },
    acceptRemoveComponent: function (componentRemovalWindow) {
        var self = this;
        console.log('componentEditorController.acceptRemoveComponent - start');

        var selectionData = Ext.ComponentQuery.query('#componentEditorDataTable')[0].selection.data;
        console.log('componentEditorController.acceptRemoveComponent - selectionData: ', selectionData);
        var componentTypeId = selectionData.type;
        var id;

        var componentTypesDataStore = self.getViewModel().getStore('componentTypesDataStore');

        for (var i = 0; i < componentTypesDataStore.data.items.length; i++) {
            var component = componentTypesDataStore.data.items[i].data;
            if (componentTypeId == component.type) {
                id = component.id;
                break;
            }
        }

        // Add loading mask
        var loadingMask = new Ext.LoadMask(
            {
                msg: self.fieldLabelDataLoading,
                id: 'saveComponentEditorMask',
                target: Ext.ComponentQuery.query('#ags3xComponentEditor')[0]
            }
        );
        loadingMask.show();

        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'componenttype/' + id,
            method: 'DELETE',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('componentEditorController.acceptRemoveComponent - result: ', result);

                        componentRemovalWindow.destroy();
                        Ext.ComponentQuery.query('#buttonRemoveComponent')[0].setDisabled(true);

                        self.fetchComponentTypesData();

                        Ext.ComponentQuery.query('#saveComponentEditorMask')[0].destroy();
                    }
                    catch (ex) {
                        console.log('componentEditorController.acceptRemoveComponent - delete, EXCEPTION: ', ex);
                        Ext.ComponentQuery.query('#saveComponentEditorMask')[0].destroy();
                    }
                }
            }
        });
    },
    cancelRemoveComponent: function (componentRemovalWindow) {
        console.log('componentEditorController.cancelRemoveComponent');
        componentRemovalWindow.destroy();
    },
    disableToolbar: function() {
        Ext.ComponentQuery.query('#componentDataStoreComboBox')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonCreateNewComponent')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonRemoveComponent')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonComponentEditorRefreshData')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonComponentEditorGoToTop')[0].setDisabled(true);
    },
    enableToolbar: function() {
        Ext.ComponentQuery.query('#componentDataStoreComboBox')[0].setDisabled(false);

        console.log('componentEditorController.enableToolbar - combobox value: ', Ext.ComponentQuery.query('#componentDataStoreComboBox')[0].getValue());

        if(Ext.ComponentQuery.query('#componentDataStoreComboBox')[0].getValue()) {
            Ext.ComponentQuery.query('#buttonCreateNewComponent')[0].setDisabled(false);
        }

        if (document.getElementById('componentEditorDataTable').getElementsByClassName('x-grid-item-selected').length > 0) {
            Ext.ComponentQuery.query('#buttonRemoveComponent')[0].setDisabled(false);
        }

        Ext.ComponentQuery.query('#buttonComponentEditorRefreshData')[0].setDisabled(false);
        Ext.ComponentQuery.query('#buttonComponentEditorGoToTop')[0].setDisabled(false);

        var selection = Ext.ComponentQuery.query('#componentEditorDataTable')[0].getSelection();
        console.log('componentEditorController.enableToolbar - selection length: ', selection.length);

        if (Ext.ComponentQuery.query('#componentEditorDataTable')[0].getSelection().length > 0) {
            Ext.ComponentQuery.query('#buttonRemoveComponent')[0].setDisabled(false);
        }
    },
    handleEditorWfsFieldBlur: function (event) {
        var self = this;

        console.log('componentEditorController.handleEditorWfsFieldBlur - event: ', event);

        var featureDescriptUrl = AGS3xIoTAdmin.util.util.getFeatureTypeDescriptionUrl(event.value);
        console.log('componentEditorController.handleEditorWfsFieldBlur - featureDescriptUrl: ', featureDescriptUrl);

        var fieldMappingStore = self.getViewModel().getStore('fieldMappingStore');

        // Get object info from WFS
        Ext.Ajax.request({
            url: featureDescriptUrl,
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
                        console.log('componentEditorController.handleEditorWfsFieldBlur - component description WFS GET result: ', xmlResult);

                        // Define namespace prefix
                        var targetNamespace;
                        if (navigator.userAgent.indexOf('Edge') >= 0) {
                            targetNamespace = xmlResult.getElementsByTagName('schema')[0].getAttribute('targetNamespace');
                        }
                        else {
                            targetNamespace = xmlResult.getElementsByTagName('xsd:schema')[0].getAttribute('targetNamespace');
                        }
                       
                        console.log('componentEditorController.handleEditorWfsFieldBlur - targetNamespace: ', targetNamespace);

                        var tagElements;
                        if (navigator.userAgent.indexOf('Edge') >= 0) {
                            tagElements = xmlResult.getElementsByTagName('element');
                        }
                        else {
                            tagElements = xmlResult.getElementsByTagName('xsd:element');
                        }

                        console.log('componentEditorController.handleEditorWfsFieldBlur - tagElements: ', tagElements);

                        var data = [];

                        for (var i = 0; i < tagElements.length; i++) {
                            var tagElement = tagElements[i];
                            console.log('componentEditorController.handleEditorWfsFieldBlur - tagElement: ', tagElement.getAttribute('name'));
                            data.push({ 'name': tagElement.getAttribute('name') });
                        }


                        fieldMappingStore.objectWfs = featureDescriptUrl;
                        fieldMappingStore.setData(data);

                        console.log('componentEditorController.handleEditorWfsFieldBlur - fieldMappingStore: ', fieldMappingStore);

                        // extract layer name and add to editor
                        var wfsLayerName = AGS3xIoTAdmin.util.util.getUrlParameterByName('typeNames', event.value);
                        console.log('componentEditorController.handleEditorWfsFieldBlur - wfsLayerName: ', wfsLayerName);
                        if (wfsLayerName) {
                            document.getElementsByClassName('x-grid-row-editor-wrap')[0].querySelectorAll('[name="wfsLayer"]')[0].value = wfsLayerName;
                        }
                    }
                    catch (exception) {
                        console.log('componentEditorController.handleEditorWfsFieldBlur - exception: ', exception);
                    }
                }
            }
        });
    },
    processWfsCheck: function (record, element) {
        var self = this;

        console.log('componentEditorController.processWfsCheck - record: ', record);
        console.log('componentEditorController.processWfsCheck - element: ', element);

        var elementId = element.id;
        console.log('componentEditorController.processWfsCheck - elementId: ', elementId);

        var parentNode = document.getElementById(elementId).querySelector("div").closest(".x-box-target");
        var wfsUrl = parentNode.querySelectorAll('[name="wfs"]')[0].value;
        var wfsLayerName = parentNode.querySelectorAll('[name="wfsLayer"]')[0].value;
        console.log('componentEditorController.processWfsCheck - parentNode: ', parentNode);
        console.log('componentEditorController.processWfsCheck - wfsUrl: ', wfsUrl);
        console.log('componentEditorController.processWfsCheck - wfsLayerValue: ', wfsLayerName);

        if ((wfsUrl != null && wfsUrl.length > 0) && (wfsLayerName != null && wfsLayerName.length > 0)) {
            var featureDescriptUrl = wfsUrl + '?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName=' + wfsLayerName;
            console.log('componentEditorController.processWfsCheck - featureDescriptUrl: ', featureDescriptUrl);

            var fieldMappingStore = self.getViewModel().getStore('fieldMappingStore');

            // Get object info from WFS layer name
            Ext.Ajax.request({
                url: featureDescriptUrl,
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
                            console.log('componentEditorController.processWfsCheck - object description WFS GET result: ', xmlResult);

                            // Define namespace prefix
                            var targetNamespace;
                            if (navigator.userAgent.indexOf('Edge') >= 0) {
                                targetNamespace = xmlResult.getElementsByTagName('schema')[0].getAttribute('targetNamespace');
                            }
                            else {
                                targetNamespace = xmlResult.getElementsByTagName('xsd:schema')[0].getAttribute('targetNamespace');
                            }
                            console.log('componentEditorController.processWfsCheck - targetNamespace: ', targetNamespace);

                            var tagElements;
                            if (navigator.userAgent.indexOf('Edge') >= 0) {
                                tagElements = xmlResult.getElementsByTagName('element');
                            }
                            else {
                                tagElements = xmlResult.getElementsByTagName('xsd:element');
                            }
                            console.log('componentEditorController.processWfsCheck - tagElements: ', tagElements);

                            var data = [];

                            for (var i = 0; i < tagElements.length; i++) {
                                var tagElement = tagElements[i];
                                //console.log('objectEditorController.handleEditorWfsLayerFieldBlur - tagElement: ', tagElement.getAttribute('name'));
                                data.push({ 'name': tagElement.getAttribute('name') });
                            }


                            fieldMappingStore.objectWfs = featureDescriptUrl;
                            fieldMappingStore.setData(data);

                            console.log('componentEditorController.processWfsCheck - fieldMappingStore: ', fieldMappingStore);

                        }
                        catch (exception) {
                            console.log('componentEditorController.processWfsCheck - exception: ', exception);
                        }
                    }
                }
            });
        }

    },
    setNewComponentValue: function (value) {
        var self = this;
        self.newComponent = value;
    },
    init: function () {
        var self = this;

        self.baseUrl = AGS3xIoTAdmin.systemData.serviceUrl;

        self.fetchComponentTypesData();
        self.fetchDataStoresData();

        AGS3xIoTAdmin.util.events.on('componentWfsBlurred', self.processWfsCheck, self);
        AGS3xIoTAdmin.util.events.on('disableToolbar', self.disableToolbar, self);
        AGS3xIoTAdmin.util.events.on('saveComponent', self.saveComponent, self);
        AGS3xIoTAdmin.util.events.on('enableToolbar', self.enableToolbar, self);
        AGS3xIoTAdmin.util.events.on('setNewComponentValue', self.setNewComponentValue, self);
    }
});