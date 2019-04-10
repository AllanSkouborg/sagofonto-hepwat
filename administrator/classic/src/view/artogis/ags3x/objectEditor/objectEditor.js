var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.objectEditor.objectEditor', {
    extend: 'Ext.panel.Panel',
    xtype: 'ags3xObjectEditor',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.objectEditor.objectEditorController',
        'AGS3xIoTAdmin.view.artogis.ags3x.objectEditor.objectEditorModel'
    ],

    viewModel: {
        type: 'ags3xObjectEditor'
    },

    controller: 'ags3xObjectEditor',

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelObjectEditorTitle: 'Objekttyper',
    fieldLabelObjectSelectDataStore: 'Vælg Data Store...',
    fieldLabelObjectAddObject: 'Tilføj objekt',
    fieldLabelObjectRemoveObject: 'Fjern objekt',
    fieldLabelObjectObjectListTitle: 'Objektliste',
    fieldLabelObjectRefreshData: 'Genopfrisk data',
    fieldLabelObjectGoToTop: 'Gå til toppen',

    columnHeaderObjectType: 'Type', 
    columnHeaderObjectId: 'ID', 
    columnHeaderObjectName: 'Navn',
    columnHeaderObjectDataStoreId: 'Data Store',
    columnHeaderObjectZOrder: 'Lagindeks',
    columnHeaderObjectWfs: 'WFS URL',
    columnHeaderObjectWfsLayer: 'WFS lagnavn',
    columnHeaderObjectFieldId: 'Felt ID',
    columnHeaderObjectFieldName: 'Feltnavn',
    columnHeaderObjectFieldDescription: 'Feltbeskrivelse',
    columnHeaderObjectObjectTableName: 'Objekttabelnavn',
    columnHeaderObjectEditDate: 'Redigeringsdato',

    fieldLabelRecordButtonSave: 'Gem',
    fieldLabelRecordButtonCancel: 'Fortryd',

    fieldLabelDataLoading: 'Vent venligst...',

    id: 'ags3xObjectEditor',
    cls: 'contentPanelBox',
    layout: 'fit',
    style: {
        'background': '#f6f6f6'
    },
    bodyStyle: {
        'background': '#f6f6f6'
    },
    

    initComponent: function () {
        self = this;

        self.activeRecord;

        self.listeners = {
            afterrender: function () {
                var loadingMask = new Ext.LoadMask(
                    {
                        msg: self.fieldLabelDataLoading,
                        id: 'objectEditorFullMask',
                        target: self
                    }
                );

                loadingMask.show();
            }
        }

        self.items = [
            {
                xtype: 'panel',
                id: 'objectEditorContent',
                layout: 'border',
                height: '100%',
                margin: '20px 20px 20px 20px',
                bodyStyle: {
                    'background': '#f6f6f6'
                },
                header: {
                    title: self.fieldLabelObjectEditorTitle,
                    height: 44,
                    width: (window.innerWidth - 300 - 40 - 4),
                    margin: '0 0 2px 2px'
                },
                items: [
                    {
                        xtype: 'panel',
                        id: 'objectEditorToolBar',
                        region: 'north',
                        height: 65,
                        width: (window.innerWidth - 300 - 40 - 4),
                        margin: '20px 2px 2px 2px',
                        padding: '20px 10px 20px 10px',
                        style: {
                            'box-shadow': '0 1px 2px rgba(0, 0, 0, 0.2) !important',
                            'background': '#ffffff'
                        },
                        bodyStyle: {
                            'background': '#ffffff'
                        },
                        items: [
                            {
                                xtype: 'combobox',
                                id: 'objectDataStoreComboBox',
                                fieldLabel: 'Data Store', // needs translation
                                style: {
                                    'float': 'left'
                                },
                                valueField: 'id',
                                displayField: 'name',
                                bind: {
                                    store: '{dataStoresDataStore}',
                                    value: '{id}'
                                },
                                editable: false,
                                queryMode: 'local',
                                emptyText: self.fieldLabelObjectSelectDataStore,
                                //emptyText: 'Vælg Data Store...',
                                tpl: Ext.create('Ext.XTemplate',
                                    '<ul class="x-list-plain"><tpl for=".">',
                                        '<li role="option" class="x-boundlist-item">{name}</li>',
                                    '</tpl></ul>'
                                ),
                                listeners: {
                                    afterrender: function () {
                                        var fieldLabel = document.getElementById('objectDataStoreComboBox-labelTextEl');
                                        fieldLabel.style.color = '#0f4875';
                                        fieldLabel.style.fontSize = '15px';
                                    },
                                    change: function (a, b, c, d) {
                                        console.log('Header combobox change - a: ', a);
                                        console.log('Header combobox - disabled: ', a.disabled);
                                        //console.log('combobox change - b: ', b);
                                        //console.log('combobox change - c: ', c);
                                        //console.log('combobox change - d: ', d);

                                        if (a.disabled == false) {
                                            //Ext.getCmp('ags3xDataIOConfig').getController('AGS3xIoTAdmin.view.artogis.ags3x.dataIO.dataIOController').templateSelected(a);
                                            var value = a.value;

                                            Ext.ComponentQuery.query('#buttonCreateNewObject')[0].setDisabled(false);
                                        }
                                    }
                                }

                            },
                            {
                                xtype: 'button',
                                id: 'buttonCreateNewObject',
                                text: self.fieldLabelObjectAddObject, // needs translation
                                iconCls: 'fa fa-plus',
                                style: {
                                    'margin-left': '10px'
                                },
                                disabled: true,
                                handler: function () {
                                    console.log('Add object...');

                                    // reset buttons and inputs
                                    Ext.ComponentQuery.query('#objectDataStoreComboBox')[0].setDisabled(true);
                                    Ext.ComponentQuery.query('#buttonCreateNewObject')[0].setDisabled(true);

                                    var fieldMappingStore = self.getViewModel().getStore('fieldMappingStore');
                                    fieldMappingStore.setData([]);

                                    self.controller.newObject = true;

                                    var grid = Ext.ComponentQuery.query('#objectEditorDataTable')[0];
                                    console.log('grid: ', grid);
                                    Ext.ComponentQuery.query('#buttonRemoveObject')[0].setDisabled(true);
                                    var store = self.getViewModel().getStore('objectTypesDataStore');
                                    console.log('store: ', store);
                                    var rowediting = grid.getPlugin('roweditingId');
                                    rowediting.cancelEdit();

                                    var keyDescription = { 'field': 'ogc_fid', 'type': 'integer', 'value': null };

                                    var guid = AGS3xIoTAdmin.util.util.generateUUID();

                                    var dataStoreId = Ext.ComponentQuery.query('#objectDataStoreComboBox')[0].getValue();
                                    var dataStoreName = AGS3xIoTAdmin.util.util.getDataStoreName(dataStoreId, self.getViewModel());
                                    console.log('datastoreId: ', dataStoreId);

                                    var now = new Date();
                                    var date = Ext.Date.format(now, 'd-m-Y H:i:s');

                                    var record = Ext.create('Ext.data.Model', {
                                        "id": guid,
                                        "name": null,
                                        "datastoreId": dataStoreId,
                                        "dataStoreName": dataStoreName,
                                        "objectTableName": null,
                                        "wfs": null,
                                        "fieldId": null,
                                        "fieldName": null,
                                        "fieldDescription": null,
                                        "keyDescriptions": [
                                          {
                                              "field": "ogc_fid",
                                              "type": "integer",
                                              "value": null
                                          }
                                        ],
                                        "editDate": date,
                                        "dataStore": {
                                            "id": dataStoreId
                                        },
                                        "type": null,
                                        "zOrder": null
                                    });

                                    // Insert row at table top
                                    var index = 0;
                                    store.insert(index, record);
                                    record = store.getAt(index ? index - 1 : 0);
                                    console.log('Start editing of row - record: ', record);
                                    rowediting.startEdit(record, 0);
                                }
                            },
                            {
                                xtype: 'button',
                                id: 'buttonRemoveObject',
                                text: self.fieldLabelObjectRemoveObject, // needs translation
                                iconCls: 'fa fa-remove',
                                style: {
                                    'margin-left': '10px'
                                },
                                disabled: true,
                                handler: 'removeObject'
                            }
                        ]
                    },
                    {
                        xtype: 'gridpanel',
                        id: 'objectEditorDataTable',
                        region: 'center',
                        header: {
                            title: self.fieldLabelObjectObjectListTitle,
                            tools: [
                                {
                                    xtype: 'button',
                                    id: 'buttonObjectEditorRefreshData',
                                    text: self.fieldLabelObjectRefreshData,
                                    iconCls: 'fa fa-refresh',
                                    style: {
                                        'float': 'right',
                                        'margin-left': '10px'
                                    },
                                    handler: 'fetchObjectTypesData'
                                },
                                {
                                    xtype: 'button',
                                    id: 'buttonObjectEditorGoToTop',
                                    text: self.fieldLabelObjectGoToTop, // needs translation
                                    iconCls: 'fa fa-long-arrow-up',
                                    style: {
                                        'float': 'right',
                                        'margin-left': '10px'
                                    },
                                    handler: function () {
                                        console.log('Button - go to top');
                                        var scrollElement = document.getElementById('objectEditorDataTable').getElementsByClassName('x-grid-view')[0].scrollTop = 0;
                                    }
                                }
                            ]
                        },

                        store: Ext.data.StoreManager.lookup('objectTypesDataStore'),
                        height: window.innerHeight - 180 - 65 - 20,
                        width: (window.innerWidth - 300 - 40 - 4),
                        margin: '20px 2px 2px 2px',
                        style: {
                            'box-shadow': '0 1px 2px rgba(0, 0, 0, 0.2) !important'
                        },
                        scrollable: true,

                        plugins: {
                            ptype: 'rowediting',
                            clicksToEdit: 2,
                            pluginId: 'roweditingId',

                            saveBtnText: self.fieldLabelRecordButtonSave,
                            cancelBtnText: self.fieldLabelRecordButtonCancel,

                            listeners: {
                                beforeedit: function (a, view) {
                                    console.log('objectEditor.beforeedit - a: ', a);
                                    console.log('objectEditor.beforeedit - view.record: ', view.record);
                                    console.log('objectEditor.beforeedit - view.record.data: ', view.record.data);

                                    self.activeRecord = view.record.data;

                                    var fieldMappingStore = self.getViewModel().getStore('fieldMappingStore');
                                    fieldMappingStore.setData([]);
                                    fieldMappingStore.objectWfs = null;

                                    var objectTypesDataStore = self.getViewModel().getStore('objectTypesDataStore');

                                    console.log('objectEditor.beforeedit - objectTypesDataStore: ', objectTypesDataStore);

                                    var newValues = view.record.data;

                                    newValues.editDate = Ext.Date.format(new Date(), 'd-m-Y H:i:s');
                                    newValues.keyDescriptions = [{ field: 'id', type: 'integer', value: 100 }];
                                    //newValues.type = largestId;
                                    newValues.fieldId = newValues.fieldId;
                                    newValues.fieldName = newValues.fieldName;
                                    newValues.fieldDescription = newValues.fieldDescription;
                                    var dataStoreObject = { id: newValues.datastoreId };
                                    newValues.dataStore = dataStoreObject;
                                    newValues.zOrder = newValues.zOrder;

                                    // REVISIT - start
                                    // if record already exists, don't find largest type ID
                                    /*if (view.record.data.type == null) {
                                        var largestId = 0;
                                        for (var i = 0; i < objectTypesDataStore.data.items.length; i++) {
                                            var objectItem = objectTypesDataStore.data.items[i].data;
                                            console.log('objectItem: ', objectItem);
                                            if (objectItem.type >= largestId) {
                                                largestId = objectItem.type;
                                            }
                                        }
                                        largestId++;

                                        newValues.type = largestId;
                                    }
                                    */

                                    // REVISIT - end

                                    console.log('objectEditor.beforeedit - newValues: ', newValues);

                                    AGS3xIoTAdmin.util.events.fireEvent('disableToolbar');

                                    if (view.record.data.wfs) {
                                        //var featureDescriptUrl = AGS3xIoTAdmin.util.util.getFeatureTypeDescriptionUrl(view.record.data.wfs);
                                        var featureDescriptUrl = view.record.data.wfs + '?service=wfs&version=1.1.0&request=DescribeFeatureType&typeName=' + view.record.data.wfsLayer;
                                        console.log('objectEditor.beforeedit - featureDescriptUrl: ', featureDescriptUrl);


                                        if (fieldMappingStore.objectWfs != view.record.data.wfs) {
                                            // Get object info from WFS
                                            Ext.Ajax.request({
                                                url: featureDescriptUrl,
                                                method: 'GET',
                                                callback: function (options, success, response) {
                                                    if (success) {
                                                        try {
                                                            //var result = Ext.JSON.decode(response.responseText);
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
                                                            console.log('objectEditor.beforeedit - object description WFS GET result: ', xmlResult);

                                                            Ext.ComponentQuery.query('#buttonRemoveObject')[0].setDisabled(true);

                                                            // Define namespace prefix
                                                            var schemaElements;
                                                            if (navigator.userAgent.indexOf('Edge') >= 0) {
                                                                schemaElements = xmlResult.getElementsByTagName('schema');
                                                            }
                                                            else {
                                                                schemaElements = xmlResult.getElementsByTagName('xsd:schema');
                                                            }

                                                            if (schemaElements.length > 0) {
                                                                var targetNamespace = schemaElements[0].getAttribute('targetNamespace');
                                                                console.log('objectEditor.beforeedit - targetNamespace: ', targetNamespace);

                                                                var tagElements;
                                                                if (navigator.userAgent.indexOf('Edge') >= 0) {
                                                                    tagElements = xmlResult.getElementsByTagName('element');
                                                                }
                                                                else {
                                                                    tagElements = xmlResult.getElementsByTagName('xsd:element');
                                                                }
                                                                console.log('objectEditor.beforeedit - tagElements: ', tagElements);

                                                                var data = [];

                                                                for (var i = 0; i < tagElements.length; i++) {
                                                                    var tagElement = tagElements[i];
                                                                    console.log('objectEditor.beforeedit - tagElement: ', tagElement.getAttribute('name'));
                                                                    data.push({ 'name': tagElement.getAttribute('name') });
                                                                }


                                                                fieldMappingStore.objectWfs = view.record.data.wfs;
                                                                fieldMappingStore.setData(data);
                                                                console.log('objectEditor.beforeedit - fieldMappingStore: ', fieldMappingStore);  
                                                            }
                                                        }
                                                        catch (exception) {
                                                            console.log('objectEditor.beforeedit - exception: ', exception);
                                                        }
                                                    }
                                                }
                                            });

                                            
                                        }
                                        else {
                                            console.log('objectEditor.beforeedit - same store still applies');
                                        }
                                    }
                                },
                                edit: function (editor, context, eOpts) {

                                    console.log('objectEditor.edit');

                                    AGS3xIoTAdmin.util.events.fireEvent('saveObject', editor, context, eOpts);

                                },
                                canceledit: function () {
                                    console.log('objectEditor.cancelEdit');
                                    console.log('self.controller.newObject: ', self.controller.newObject);

                                    if (self.controller.newObject == true) {
                                        var objectTypesDataStore = self.getViewModel().getStore('objectTypesDataStore');
                                        console.log('objectEditor.cancelEdit - objectTypesDataStore (before): ', objectTypesDataStore);
                                        objectTypesDataStore.removeAt(0, 1);
                                        console.log('objectEditor.cancelEdit - objectTypesDataStore (after): ', objectTypesDataStore);

                                        AGS3xIoTAdmin.util.events.fireEvent('setNewObjectValue', false);
                                    }

                                    AGS3xIoTAdmin.util.events.fireEvent('enableToolbar');
                                }
                            }
                        },
                        viewConfig: {
                            stripeRows: false
                        },
                        columns: [
                            { id: 'colObjectType', text: self.columnHeaderObjectType, dataIndex: 'type', width: 50 },
                            //{ id: 'colObjectId', text: self.columnHeaderObjectId, dataIndex: 'id', width: 200 },
                            { id: 'colObjectName', text: self.columnHeaderObjectName, dataIndex: 'name', editor: 'textfield', width: 300 },
                            //{ id: 'colObjectDataStoreID', text: self.columnHeaderObjectDataStoreId, dataIndex: 'datastoreId', flex: 1 },
                            { id: 'colObjectDataStoreID', text: self.columnHeaderObjectDataStoreId, dataIndex: 'dataStoreName', width: 200 },
                            {
                                id: 'colObjectZOrder',
                                text: self.columnHeaderObjectZOrder,
                                dataIndex: 'zOrder',
                                width: 100,
                                editor: {
                                    xtype: 'textfield',
                                    maskRe: /[0-9]/
                                }
                            },
                            {
                                id: 'colObjectWfs',
                                text: self.columnHeaderObjectWfs,
                                dataIndex: 'wfs',
                                editor: {
                                    xtype: 'textfield',
                                    listeners: {
                                        //blur: 'handleEditorWfsLayerFieldBlur'
                                        blur: function (element) {
                                            AGS3xIoTAdmin.util.events.fireEvent('objectWfsBlurred', self.activeRecord, element);
                                        }
                                    }
                                },
                                flex: 1
                            },
                            {
                                id: 'colObjectWfsLayer',
                                text: self.columnHeaderObjectWfsLayer,
                                dataIndex: 'wfsLayer',
                                editor: {
                                    xtype: 'textfield',
                                    listeners: {
                                        //blur: 'handleEditorWfsLayerFieldBlur'
                                        blur: function (element) {
                                            AGS3xIoTAdmin.util.events.fireEvent('objectWfsBlurred', self.activeRecord, element);
                                        }
                                    }
                                },
                                flex: 1
                            },
                            {
                                id: 'colObjectFieldId',
                                text: self.columnHeaderObjectFieldId,
                                dataIndex: 'fieldId',
                                flex: 1,
                                editor: {
                                    xtype: 'combobox',
                                    bind: {
                                        store: '{fieldMappingStore}'
                                        //store: self.getViewModel().getStore('fieldMappingStore')
                                    },
                                    //store: Ext.data.StoreManager.lookup('fieldMappingStore'),
                                    displayField: 'name',
                                    valueField: 'name',
                                    queryMode: 'local',
                                    editable: false,
                                    tpl: Ext.create('Ext.XTemplate',
                                        '<ul class="x-list-plain"><tpl for=".">',
                                            '<li role="option" class="x-boundlist-item" title="{name}">{name}</li>',
                                        '</tpl></ul>'
                                    )
                                }
                            },
                            {
                                id: 'colObjectFieldName',
                                text: self.columnHeaderObjectFieldName,
                                dataIndex: 'fieldName',
                                flex: 1,
                                editor: {
                                    xtype: 'combobox',
                                    bind: {
                                        store: '{fieldMappingStore}'
                                        //store: self.getViewModel().getStore('fieldMappingStore')
                                    },
                                    //store: Ext.data.StoreManager.lookup('fieldMappingStore'),
                                    displayField: 'name',
                                    valueField: 'name',
                                    queryMode: 'local',
                                    editable: false,
                                    tpl: Ext.create('Ext.XTemplate',
                                        '<ul class="x-list-plain"><tpl for=".">',
                                            '<li role="option" class="x-boundlist-item" title="{name}">{name}</li>',
                                        '</tpl></ul>'
                                    )
                                }
                            },
                            {
                                id: 'colObjectFieldDescription',
                                text: self.columnHeaderObjectFieldDescription,
                                dataIndex: 'fieldDescription',
                                flex: 1,
                                editor: {
                                    xtype: 'combobox',
                                    bind: {
                                        store: '{fieldMappingStore}'
                                        //store: self.getViewModel().getStore('fieldMappingStore')
                                    },
                                    //store: Ext.data.StoreManager.lookup('fieldMappingStore'),
                                    displayField: 'name',
                                    valueField: 'name',
                                    queryMode: 'local',
                                    editable: false,
                                    tpl: Ext.create('Ext.XTemplate',
                                        '<ul class="x-list-plain"><tpl for=".">',
                                            '<li role="option" class="x-boundlist-item" title="{name}">{name}</li>',
                                        '</tpl></ul>'
                                    )
                                }
                            },
                            { id: 'colObjectTableName', text: self.columnHeaderObjectObjectTableName, dataIndex: 'objectTableName', editor: 'textfield', width: 200 },
                            { id: 'colObjectEditDate', text: self.columnHeaderObjectEditDate, dataIndex: 'editDate', width: 150 }
                        ],
                        listeners: {
                            resize: function (component) {
                                console.log('objectEditor - Object Editor resizing');
                                //height: window.innerHeight - 180 - 65 - 20,
                            },
                            afterrender: function (a, b, c, d) {
                                console.log('objectEditor - Object columns - afterrender a: ', a);
                                console.log('objectEditor - Object columns - afterrender b: ', b);
                                console.log('objectEditor - Object columns - afterrender c: ', c);
                                console.log('objectEditor - Object columns - afterrender d: ', d);
                            },
                            itemclick: function (component, record, node, index, event, options) {
                                console.log('objectEditor - itemclick, component: ', component);
                                console.log('objectEditor - itemclick, record: ', record);
                                console.log('objectEditor - itemclick, node: ', node);
                                console.log('objectEditor - itemclick, index: ', index);
                                console.log('objectEditor - itemclick, event: ', event);
                                console.log('objectEditor - itemclick, options: ', options);

                                Ext.ComponentQuery.query('#buttonRemoveObject')[0].setDisabled(false);

                                var rowEditorPlugin = Ext.ComponentQuery.query('#objectEditorDataTable')[0].getPlugin('roweditingId');
                                console.log('objectEditor - itemclick, rowEditorPlugin: ', rowEditorPlugin);

                                if (document.getElementById('objectEditorDataTable').getElementsByClassName('x-row-editor-cancel-button').length > 0) {
                                    document.getElementById('objectEditorDataTable').getElementsByClassName('x-row-editor-cancel-button')[0].click();
                                }
                            },
                            itemdblclick: function (component, record, node, index, event, options) {
                                console.log('objectEditor - itemdblclick, component: ', component);
                                console.log('objectEditor - itemdblclick, record: ', record);
                                console.log('objectEditor - itemdblclick, node: ', node);
                                console.log('objectEditor - itemdblclick, index: ', index);
                                console.log('objectEditor - itemdblclick, event: ', event);
                                console.log('objectEditor - itemdblclick, options: ', options);

                                Ext.ComponentQuery.query('#buttonRemoveObject')[0].setDisabled(false);


                            },
                            change: function (component, b) {
                                console.log('objectEditor - Change - component: ', component);
                                console.log('objectEditor - Change - b: ', b);
                            },
                            sortchange: function (grid, sortingInfo) {
                                console.log('objectEditor - sortchange, grid :', grid);
                                console.log('objectEditor - sortchange, sortingInfo :', sortingInfo);

                                if (document.getElementById('objectEditorDataTable').getElementsByClassName('x-row-editor-cancel-button').length > 0) {
                                    document.getElementById('objectEditorDataTable').getElementsByClassName('x-row-editor-cancel-button')[0].click();
                                }
                            }
                        }
                    }
                ]
            }
        ]

        self.callParent(arguments);

    } // initComponent

    
            

});
