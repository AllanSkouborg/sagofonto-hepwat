Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.objectEditor.objectEditorController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xObjectEditor',

    requires: [
       'AGS3xIoTAdmin.util.events',
       'AGS3xIoTAdmin.util.util'
    ],

    baseUrl: null,
    newObject: false,

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelRemoveObjectQuestion: 'Fjern objekt?',
    fieldLabelRemoveObjectYes: 'Ja',
    fieldLabelRemoveObjectNo: 'Nej',
    fieldLabelDataLoading: 'Vent venligst...',

    fetchObjectTypesData: function() {
        var self = this;

        console.log('objectEditorController.fetchObjectTypesData');

        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'datastore',
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        var dataStoresStore = self.getViewModel().getStore('dataStoresDataStore');

                        console.log('objectEditorController.fetchObjectTypesData - data store result: ', result);

                        var dataStoreData = [];

                        for (var i = 0; i < result.dataStores.length; i++) {
                            var item = result.dataStores[i];
                            var record = {
                                id: item.id,
                                name: item.name
                            };
                            dataStoreData.push(record);
                        }
                        console.log('objectDataStoresController - dataStoreData: ', dataStoreData);
                        dataStoresStore.setData(dataStoreData);

                        // Fetch object types data
                        Ext.Ajax.request({
                            scope: self,
                            url: self.baseUrl + 'objecttype',
                            method: 'GET',
                            contentType: 'application/json',
                            callback: function (options, success, response) {
                                if (success) {
                                    try {
                                        var result = Ext.decode(response.responseText);
                                        console.log('objectEditorController.fetchObjectTypesData - result: ', result);
                                        var store = self.getViewModel().getStore('objectTypesDataStore');
                                        var data = [];
                                        for (var i = 0; i < result.objectTypes.length; i++) {
                                            var item = result.objectTypes[i];

                                            var record = {
                                                id: item.id,
                                                name: item.name,
                                                type: item.type,
                                                datastoreId: item.datastoreId,
                                                dataStoreName: AGS3xIoTAdmin.util.util.getDataStoreName(item.datastoreId, self.getViewModel()),
                                                keyDescriptions: JSON.stringify(item.keyDescriptions),
                                                objectTableName: item.objectTableName,
                                                wfs: item.wfs,
                                                wfsLayer: item.wfsLayer,
                                                fieldId: item.fieldId,
                                                fieldName: item.fieldName,
                                                fieldDescription: item.fieldDescription,
                                                editDate: item.editDate,
                                                zOrder: item.zOrder
                                            };
                                            data.push(record);

                                            // perform sorting according to type ID integer
                                            data.sort(function (a, b) {
                                                var keyA = new Date(a.type),
                                                    keyB = new Date(b.type);
                                                // Compare the 2 dates
                                                if (keyA < keyB) return -1;
                                                if (keyA > keyB) return 1;
                                                return 0;
                                            });
                                        }

                                        console.log('objectEditorController.fetchObjectTypesData - data: ', data);

                                        store.setData(data);

                                        var gridPanel = Ext.ComponentQuery.query('#objectEditorDataTable')[0];

                                        console.log('objectEditorController.fetchObjectTypesData - grid panel to update data in: ', gridPanel);

                                        gridPanel.reconfigure(store);

                                        if (Ext.ComponentQuery.query('#objectEditorFullMask').length > 0) {
                                            Ext.ComponentQuery.query('#objectEditorFullMask')[0].destroy();
                                        }
                                    }
                                    catch (ex) {
                                        console.log('objectEditorController.fetchObjectTypesData - EXCEPTION: ', ex);
                                        if (Ext.ComponentQuery.query('#objectEditorFullMask').length > 0) {
                                            Ext.ComponentQuery.query('#objectEditorFullMask')[0].destroy();
                                        }
                                    }
                                }
                            }
                        });
                    }
                    catch (ex) {
                        console.log('objectEditorController.fetchObjectTypesData - EXCEPTION: ', ex);
                        Ext.ComponentQuery.query('#objectEditorFullMask')[0].destroy();
                    }
                }
            }
        });

        
    },
    fetchDataStoresData: function () {
        var self = this;

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

                        console.log("objectEditorController.fetchDataStoresData - dataStoresDataStore: ", dataStoresDataStore);

                        var data = [];

                        for (var i = 0; i < result.dataStores.length; i++) {
                            var item = result.dataStores[i];
                            var record = {
                                id: item.id,
                                name: item.name
                            };
                            data.push(record);
                        }
                        console.log('objectEditorController.fetchDataStoresData - objectDataStoresController - data: ', data);
                        dataStoresDataStore.setData(data);
                        var comboBox = Ext.ComponentQuery.query('#objectDataStoreComboBox')[0];
                        dataStoresDataStore.setData(data);
                        console.log('objectEditorController.fetchDataStoresData - combo box to update data in: ', comboBox);
                    }
                    catch (ex) {
                        console.log('objectEditorController.fetchDataStoresData - EXCEPTION: ', ex);
                    }
                }
            }
        });
    },
    saveObject: function (editor, context, eOpts) {
        var self = this;

        console.log('objectEditorController.saveObject - editor: ', editor);
        console.log('objectEditorController.saveObject - context: ', context);
        console.log('objectEditorController.saveObject - eOpts: ', eOpts);

        if( self.newObject == true ) {
            self.createObject(editor, context, eOpts);
        }
        else {
            self.updateObject(editor, context, eOpts);
        }
    },
    createObject: function (editor, context, eOpts) {
        var self = this;

        console.log('objectEditorController.createObject - context: ', context);

        var objectTypesDataStore = self.getViewModel().getStore('objectTypesDataStore');

        console.log('objectEditorController.createObject - objectTypesDataStore: ', objectTypesDataStore);

        var newValues = {
            name: context.newValues.name,
            objectTableName: context.newValues.objectTableName,
            editDate: context.newValues.editDate,
            type: context.newValues.type,
            wfs: context.newValues.wfs,
            wfsLayer: context.newValues.wfsLayer,
            fieldId: context.newValues.fieldId,
            fieldName: context.newValues.fieldName,
            fieldDescription: context.newValues.fieldDescription,
            datastoreId: context.record.data.datastoreId,
            id: context.record.data.id,
            zOrder: context.newValues.zOrder
        }

        newValues.keyDescriptions = [{ field: context.newValues.fieldId, type: 'integer', value: null }];

        console.log('objectEditorController.createObject - newValues: ', newValues);
        console.log('objectEditorController.createObject - url: ', self.baseUrl + 'objecttype');

        // Add loading mask
        var loadingMask = new Ext.LoadMask(
            {
                msg: self.fieldLabelDataLoading,
                id: 'saveObjectEditorMask',
                target: Ext.ComponentQuery.query('#ags3xObjectEditor')[0]
            }
        );
        loadingMask.show();

        // create the object type
        Ext.Ajax.request({
            url: self.baseUrl + 'objecttype',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            jsonData: Ext.JSON.encode(newValues),
            success: function (response) {
                console.log('success - response: ', response);

                self.fetchObjectTypesData();

                Ext.ComponentQuery.query('#buttonRemoveObject')[0].setDisabled(false);

                self.newObject = false;

                self.enableToolbar();

                Ext.ComponentQuery.query('#saveObjectEditorMask')[0].destroy();
            },
            failure: function (response) {
                console.log('objectEditorController.createObject - FAILURE: ', response);
                Ext.ComponentQuery.query('#saveObjectEditorMask')[0].destroy();
            }
        });
    },
    updateObject: function (editor, context, eOpts) {
        var self = this;

        console.log('objectEditorController.updateObject - context: ', context);

        var objectTypesDataStore = self.getViewModel().getStore('objectTypesDataStore');
        console.log('objectEditorController.updateObject - objectTypesDataStore: ', objectTypesDataStore);

        var newValues = {
            name: context.newValues.name,
            objectTableName: context.newValues.objectTableName,
            editDate: context.newValues.editDate,
            type: context.newValues.type,
            wfs: context.newValues.wfs,
            wfsLayer: context.newValues.wfsLayer,
            fieldId: context.newValues.fieldId,
            fieldName: context.newValues.fieldName,
            fieldDescription: context.newValues.fieldDescription,
            datastoreId: context.record.data.datastoreId,
            id: context.record.data.id,
            zOrder: context.record.data.zOrder
        }
        newValues.keyDescriptions = [{ field: context.newValues.fieldId, type: 'integer', value: null }];

        console.log('objectEditorController.updateObject - newValues: ', newValues);
        console.log('objectEditorController.updateObject - url: ', self.baseUrl + 'objecttype');

        // Add loading mask
        var loadingMask = new Ext.LoadMask(
            {
                msg: self.fieldLabelDataLoading,
                id: 'saveObjectEditorMask',
                target: Ext.ComponentQuery.query('#ags3xObjectEditor')[0]
            }
        );
        loadingMask.show();

        // update object type
        Ext.Ajax.request({
            url: self.baseUrl + 'objecttype',
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            jsonData: Ext.JSON.encode(newValues),
            success: function (response) {
                console.log('objectEditorController.updateObject - success,response: ', response);

                self.fetchObjectTypesData();

                Ext.ComponentQuery.query('#buttonRemoveObject')[0].setDisabled(false);

                self.newObject = false;

                self.enableToolbar();

                Ext.ComponentQuery.query('#saveObjectEditorMask')[0].destroy();
            },
            failure: function (response) {
                console.log('objectEditorController.updateObject - FAILURE: ', response);
                Ext.ComponentQuery.query('#saveObjectEditorMask')[0].destroy();
            }
        });
    },
    removeObject: function () {
        var self = this;

        self.objectRemovalWindow = Ext.create('Ext.window.Window', {
            id: 'objectRemovalWindow',
            header: {
                title: self.fieldLabelRemoveObjectQuestion,
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
                            text: self.fieldLabelRemoveObjectYes,
                            style: {
                                'float': 'right'
                            },
                            width: 90,
                            heigh: 25,
                            listeners: {
                                click: function () {
                                    self.acceptRemoveObject(self.objectRemovalWindow)
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            text: self.fieldLabelRemoveObjectNo,
                            style: {
                                'float': 'left'
                            },
                            width: 90,
                            heigh: 25,
                            listeners: {
                                click: function () {
                                    self.cancelRemoveObject(self.objectRemovalWindow)
                                }
                            }

                        }
                    ]
                }
            ]
        }).show();
    },
    acceptRemoveObject: function (objectRemovalWindow) {
        var self = this;
        console.log('objectEditorController.acceptRemoveObject');

        var selectionData = Ext.ComponentQuery.query('#objectEditorDataTable')[0].selection.data;
        console.log('objectEditorController.acceptRemoveObject - selectionData: ', selectionData);
        var objectTypeId = selectionData.type;
        var id;

        var store = self.getViewModel().getStore('objectTypesDataStore');
        for (var i = 0; i < store.data.items.length; i++ ) {
            var object = store.data.items[i].data;
            if (objectTypeId == object.type ) {
                id = object.id;
                break;
            }
        }

        // Add loading mask
        var loadingMask = new Ext.LoadMask(
            {
                msg: self.fieldLabelDataLoading,
                id: 'saveObjectEditorMask',
                target: Ext.ComponentQuery.query('#ags3xObjectEditor')[0]
            }
        );
        loadingMask.show();
        
        Ext.Ajax.request({
            scope: self,
            url: self.baseUrl + 'objecttype/' + id,
            method: 'DELETE',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('objectEditorController.acceptRemoveObject - result: ', result);

                        objectRemovalWindow.destroy();
                        Ext.ComponentQuery.query('#buttonRemoveObject')[0].setDisabled(true);

                        self.fetchObjectTypesData();

                        if (Ext.ComponentQuery.query('#saveObjectEditorMask').length > 0) {
                            Ext.ComponentQuery.query('#saveObjectEditorMask')[0].destroy();
                        }
                    }
                    catch (ex) {
                        console.log('EXCEPTION: ', ex);
                        Ext.ComponentQuery.query('#saveObjectEditorMask')[0].destroy();
                    }
                }
            }
        });
    },
    cancelRemoveObject: function (objectRemovalWindow) {
        console.log('objectEditorController.cancelRemoveObject');
        objectRemovalWindow.destroy();
    },
    disableToolbar: function () {
        Ext.ComponentQuery.query('#objectDataStoreComboBox')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonCreateNewObject')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonRemoveObject')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonObjectEditorRefreshData')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonObjectEditorGoToTop')[0].setDisabled(true);
        
    },
    enableToolbar: function () {
        Ext.ComponentQuery.query('#objectDataStoreComboBox')[0].setDisabled(false);

        console.log('objectEditorController.enableToolbar - combobox value: ', Ext.ComponentQuery.query('#objectDataStoreComboBox')[0].getValue());

        if (Ext.ComponentQuery.query('#objectDataStoreComboBox')[0].getValue()) {
            Ext.ComponentQuery.query('#buttonCreateNewObject')[0].setDisabled(false);
        }

        if (document.getElementById('objectEditorDataTable').getElementsByClassName('x-grid-item-selected').length > 0) {
            Ext.ComponentQuery.query('#buttonRemoveObject')[0].setDisabled(false);
        }


        Ext.ComponentQuery.query('#buttonObjectEditorRefreshData')[0].setDisabled(false);
        Ext.ComponentQuery.query('#buttonObjectEditorGoToTop')[0].setDisabled(false);

        var selection = Ext.ComponentQuery.query('#objectEditorDataTable')[0].getSelection();
        console.log('objectEditorController.enableToolbar - selection length: ', selection.length);

        if (Ext.ComponentQuery.query('#objectEditorDataTable')[0].getSelection().length > 0) {
            Ext.ComponentQuery.query('#buttonRemoveObject')[0].setDisabled(false);
        }
        
    },
    
    processWfsCheck: function (record, element) {
        var self = this;

        console.log('objectEditorController.processWfsCheck - record: ', record);
        console.log('objectEditorController.processWfsCheck - element: ', element);

        var elementId = element.id;
        console.log('objectEditorController.processWfsCheck - elementId: ', elementId);

        var parentNode = document.getElementById(elementId).querySelector("div").closest(".x-box-target");
        var wfsUrl = parentNode.querySelectorAll('[name="wfs"]')[0].value;
        var wfsLayerName = parentNode.querySelectorAll('[name="wfsLayer"]')[0].value;
        console.log('objectEditorController.processWfsCheck - parentNode: ', parentNode);
        console.log('objectEditorController.processWfsCheck - wfsUrl: ', wfsUrl);
        console.log('objectEditorController.processWfsCheck - wfsLayerValue: ', wfsLayerName);

        if ((wfsUrl != null && wfsUrl.length > 0) && (wfsLayerName != null && wfsLayerName.length > 0)) {
            var featureDescriptUrl = wfsUrl + '?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName=' + wfsLayerName;
            console.log('objectEditorController.processWfsCheck - featureDescriptUrl: ', featureDescriptUrl);

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
                            console.log('objectEditorController.processWfsCheck - object description WFS GET result: ', xmlResult);

                            // Define namespace prefix
                            var targetNamespace;
                            if (navigator.userAgent.indexOf('Edge') >= 0) {
                                targetNamespace = xmlResult.getElementsByTagName('schema')[0].getAttribute('targetNamespace');
                            }
                            else {
                                targetNamespace = xmlResult.getElementsByTagName('xsd:schema')[0].getAttribute('targetNamespace');
                            }

                            console.log('objectEditorController.handleEditorWfsFieldBlur - targetNamespace: ', targetNamespace);

                            var tagElements;
                            if (navigator.userAgent.indexOf('Edge') >= 0) {
                                tagElements = xmlResult.getElementsByTagName('element');
                            }
                            else {
                                tagElements = xmlResult.getElementsByTagName('xsd:element');
                            }

                            var data = [];

                            for (var i = 0; i < tagElements.length; i++) {
                                var tagElement = tagElements[i];
                                data.push({ 'name': tagElement.getAttribute('name') });
                            }


                            fieldMappingStore.objectWfs = featureDescriptUrl;
                            fieldMappingStore.setData(data);

                            console.log('objectEditorController.processWfsCheck - fieldMappingStore: ', fieldMappingStore);

                        }
                        catch (exception) {
                            console.log('objectEditorController.processWfsCheck - exception: ', exception);
                        }
                    }
                }
            });
        }
        
    },
    setNewObjectValue: function(value) {
        var self = this;

        self.newObject = value;
    }, 
    init: function () {
        var self = this;

        self.baseUrl = AGS3xIoTAdmin.systemData.serviceUrl;

        self.fetchObjectTypesData();
        self.fetchDataStoresData();

        AGS3xIoTAdmin.util.events.on('objectWfsBlurred', self.processWfsCheck, self);
        AGS3xIoTAdmin.util.events.on('disableToolbar', self.disableToolbar, self);
        AGS3xIoTAdmin.util.events.on('saveObject', self.saveObject, self);
        AGS3xIoTAdmin.util.events.on('enableToolbar', self.enableToolbar, self);
        AGS3xIoTAdmin.util.events.on('setNewObjectValue', self.setNewObjectValue, self);
    }
});