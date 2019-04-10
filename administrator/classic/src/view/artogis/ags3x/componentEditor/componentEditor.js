var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.componentEditor.componentEditor', {
    extend: 'Ext.panel.Panel',
    xtype: 'ags3xComponentEditor',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.componentEditor.componentEditorController',
        'AGS3xIoTAdmin.view.artogis.ags3x.componentEditor.componentEditorModel'
    ],

    viewModel: {
        type: 'ags3xComponentEditor'
    },

    controller: 'ags3xComponentEditor',

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelComponentEditorTitle: 'Komponenttyper',
    fieldLabelComponentSelectDataStore: 'Vælg Data Store...',
    fieldLabelComponentAddComponent: 'Tilføj komponent',
    fieldLabelComponentRemoveComponent: 'Fjern komponent',
    fieldLabelComponentComponentListTitle: 'Komponentliste',
    fieldLabelComponentRefreshData: 'Genopfrisk data',
    fieldLabelComponentGoToTop: 'Gå til toppen',

    columnHeaderComponentType: 'Type',
    columnHeaderComponentId: 'ID',
    columnHeaderComponentName: 'Navn',
    columnHeaderComponentDataStoreId: 'Data Store',
    columnHeaderComponentWfs: 'WFS URL',
    columnHeaderComponentWfsLayer: 'WFS lagnavn',
    columnHeaderComponentFieldId: 'Felt ID',
    columnHeaderComponentFieldDescription: 'Feltbeskrivelse',
    columnHeaderComponentComponentTableName: 'Komponenttabelnavn',
    columnHeaderComponentEditDate: 'Redigeringsdato',

    fieldLabelRecordButtonSave: 'Gem',
    fieldLabelRecordButtonCancel: 'Fortryd',

    fieldLabelComponentComboboxName: 'Data Store',

    fieldLabelDataLoading: 'Vent venligst...',

    id: 'ags3xComponentEditor',
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

        self.listeners = {
            afterrender: function () {
                var loadingMask = new Ext.LoadMask(
                    {
                        msg: self.fieldLabelDataLoading,
                        id: 'componentEditorFullMask',
                        target: self
                    }
                );

                loadingMask.show();
            }
        }

        self.items = [
            {
                xtype: 'panel',
                id: 'componentEditorContent',
                layout: 'border',
                height: '100%',
                margin: '20px 20px 20px 20px',
                bodyStyle: {
                    'background': '#f6f6f6'
                },
                header: {
                    title: self.fieldLabelComponentEditorTitle, // needs translation
                    height: 44,
                    width: (window.innerWidth - 300 - 40 - 4),
                    margin: '0 0 2px 2px'
                },
                listeners: {
                    afterrender: function () {
                        console.log('componentEditor.componentEditorContent - afterrender');
                    }
                },
                items: [
                    {
                        xtype: 'panel',
                        id: 'componentEditorToolBar',
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
                                id: 'componentDataStoreComboBox',
                                fieldLabel: self.fieldLabelComponentComboboxName,
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
                                emptyText: self.fieldLabelComponentSelectDataStore,
                                tpl: Ext.create('Ext.XTemplate',
                                    '<ul class="x-list-plain"><tpl for=".">',
                                        '<li role="option" class="x-boundlist-item">{name}</li>',
                                    '</tpl></ul>'
                                ),
                                listeners: {
                                    afterrender: function () {
                                        var fieldLabel = document.getElementById('componentDataStoreComboBox-labelTextEl');
                                        fieldLabel.style.color = '#0f4875';
                                        fieldLabel.style.fontSize = '15px';
                                    },
                                    change: function (a, b, c, d) {
                                        console.log('Header combobox change - a: ', a);
                                        console.log('Header combobox - disabled: ', a.disabled);
                                        if (a.disabled == false) {
                                            var value = a.value;
                                            Ext.ComponentQuery.query('#buttonCreateNewComponent')[0].setDisabled(false);
                                        }
                                    }
                                }

                            },
                            {
                                xtype: 'button',
                                id: 'buttonCreateNewComponent',
                                text: self.fieldLabelComponentAddComponent,
                                iconCls: 'fa fa-plus',
                                style: {
                                    'margin-left': '10px'
                                },
                                disabled: true,
                                handler: function () {
                                    console.log('componentEditor - Add component...');

                                    self.controller.newComponent = true;  // convert to fire event method

                                    var grid = Ext.ComponentQuery.query('#componentEditorDataTable')[0];
                                    console.log('componentEditor - grid: ', grid);
                                    Ext.ComponentQuery.query('#buttonRemoveComponent')[0].setDisabled(true);
                                    var store = self.getViewModel().getStore('componentTypesDataStore');
                                    console.log('componentEditor - store: ', store);
                                    var rowediting = grid.getPlugin('roweditingId');
                                    rowediting.cancelEdit();

                                    var guid = AGS3xIoTAdmin.util.util.generateUUID();

                                    var dataStoreId = Ext.ComponentQuery.query('#componentDataStoreComboBox')[0].getValue();
                                    var dataStoreName = AGS3xIoTAdmin.util.util.getDataStoreName(dataStoreId, self.getViewModel());
                                    console.log('componentEditor - datastoreId: ', dataStoreId);
                                    console.log('componentEditor - dataStoreName: ', dataStoreName);


                                    var now = new Date();
                                    var date = Ext.Date.format(now, 'd-m-Y H:i:s');

                                    var record = Ext.create('Ext.data.Model', {
                                        "id": guid,
                                        "name": null,
                                        "datastoreId": dataStoreId,
                                        "dataStoreName": dataStoreName,
                                        "componentTableName": null,
                                        "wfs": null,
                                        "fieldId": null,
                                        "fieldDescription": null,
                                        "editDate": date,
                                        "dataStore": {
                                            "id": dataStoreId
                                        },
                                        "type": null
                                    });

                                    // Insert row at table top
                                    var index = 0;
                                    console.log('componentEditor - Elephant, store: ', store);
                                    store.insert(index, record);
                                    console.log('componentEditor - Zebra');
                                    record = store.getAt(index ? index - 1 : 0);
                                    console.log('componentEditor - Start editing of row - record: ', record);
                                    rowediting.startEdit(record, 0);
                                }
                            },
                            {
                                xtype: 'button',
                                id: 'buttonRemoveComponent',
                                text: self.fieldLabelComponentRemoveComponent,
                                iconCls: 'fa fa-remove',
                                style: {
                                    'margin-left': '10px'
                                },
                                disabled: true,
                                handler: 'removeComponent'
                            }
                        ]
                    },
                    {
                        xtype: 'gridpanel',
                        id: 'componentEditorDataTable',
                        margin: '0 2px 0 0',
                        region: 'center',
                        header: {
                            title: self.fieldLabelComponentComponentListTitle,
                            tools: [
                                {
                                    xtype: 'button',
                                    id: 'buttonComponentEditorRefreshData',
                                    text: self.fieldLabelComponentRefreshData, // needs translation
                                    iconCls: 'fa fa-refresh',
                                    style: {
                                        'float': 'right',
                                        'margin-left': '10px'
                                    },
                                    handler: 'fetchComponentTypesData'
                                },
                                {
                                    xtype: 'button',
                                    id: 'buttonComponentEditorGoToTop',
                                    text: self.fieldLabelComponentGoToTop, // needs translation
                                    iconCls: 'fa fa-long-arrow-up',
                                    style: {
                                        'float': 'right',
                                        'margin-left': '10px'
                                    },
                                    handler: function () {
                                        console.log('Button - go to top');
                                        var scrollElement = document.getElementById('componentEditorDataTable').getElementsByClassName('x-grid-view')[0].scrollTop = 0;
                                    }
                                }
                            ]
                        },

                        store: Ext.data.StoreManager.lookup('componentTypesDataStore'),
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
                                    console.log('componentEditor.beforeedit - a: ', a);
                                    console.log('componentEditor.beforeedit - view.record: ', view.record);
                                    console.log('componentEditor.beforeedit - view.record.data: ', view.record.data);

                                    var fieldMappingStore = self.getViewModel().getStore('fieldMappingStore');
                                    fieldMappingStore.setData([]);
                                    fieldMappingStore.objectWfs = null;

                                    var componentTypesDataStore = self.getViewModel().getStore('componentTypesDataStore');

                                    console.log('componentEditor.beforeedit - componentTypesDataStore: ', componentTypesDataStore);

                                    var largestId = 0;

                                    /*for (var i = 0; i < componentTypesDataStore.data.items.length; i++) {
                                        var componentItem = componentTypesDataStore.data.items[i].data;
                                        console.log('componentEditor.beforeedit - componentItem: ', componentItem);
                                        if (componentItem.type >= largestId) {
                                            largestId = componentItem.type;
                                        }
                                    }
                                    largestId++;*/

                                    var newValues = view.record.data;
                                    newValues.editDate = Ext.Date.format(new Date(), 'd-m-Y H:i:s');
                                    //newValues.type = largestId;
                                    newValues.fieldId = newValues.fieldId;
                                    newValues.fieldDescription = newValues.fieldDescription;
                                    var dataStoreComponent = { id: newValues.datastoreId };
                                    newValues.dataStore = dataStoreComponent;

                                    // REVISIT - start
                                    // if record already exists, don't find largest type ID
                                    /*if (view.record.data.type == null) {
                                        var largestId = 0;
                                        for (var i = 0; i < componentTypesDataStore.data.items.length; i++) {
                                            var componentItem = componentTypesDataStore.data.items[i].data;
                                            console.log('componentEditor.beforeedit - componentItem: ', componentItem);
                                            if (componentItem.type >= largestId) {
                                                largestId = componentItem.type;
                                            }
                                        }
                                        largestId++;

                                        //newValues.type = largestId;
                                    }
                                    */

                                    // REVISIT - end

                                    console.log('componentEditor.beforeedit - newValues: ', newValues);

                                    AGS3xIoTAdmin.util.events.fireEvent('disableToolbar');

                                    if (view.record.data.wfs) {
                                        var featureDescriptUrl = AGS3xIoTAdmin.util.util.getFeatureTypeDescriptionUrl(view.record.data.wfs);
                                        console.log('componentEditor.beforeedit - featureDescriptUrl: ', featureDescriptUrl);


                                        if (fieldMappingStore.componentWfs != view.record.data.wfs) {
                                            // Get component info from WFS
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
                                                            console.log('componentEditor.beforeedit - component description WFS GET result: ', xmlResult);

                                                            Ext.ComponentQuery.query('#buttonRemoveComponent')[0].setDisabled(true);

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
                                                                console.log('componentEditor.beforeedit - targetNamespace: ', targetNamespace);

                                                                var tagElements;
                                                                if (navigator.userAgent.indexOf('Edge') >= 0) {
                                                                    tagElements = xmlResult.getElementsByTagName('element');
                                                                }
                                                                else {
                                                                    tagElements = xmlResult.getElementsByTagName('xsd:element');
                                                                }
                                                                
                                                                console.log('componentEditor.beforeedit - tagElements: ', tagElements);

                                                                var data = [];

                                                                for (var i = 0; i < tagElements.length; i++) {
                                                                    var tagElement = tagElements[i];
                                                                    console.log('componentEditor.beforeedit - tagElement: ', tagElement.getAttribute('name'));
                                                                    data.push({ 'name': tagElement.getAttribute('name') });
                                                                }


                                                                fieldMappingStore.componentWfs = view.record.data.wfs;
                                                                fieldMappingStore.setData(data);
                                                                console.log('componentEditor.beforeedit - fieldMappingStore: ', fieldMappingStore);
                                                            }
                                                            
                                                        }
                                                        catch (exception) {
                                                            console.log('componentEditor.beforeedit - get component info, EXCEPTION: ', exception);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    // disable top buttons and drowpdown
                                    //self.controller.disableToolbar();
                                },
                                edit: function (editor, context, eOpts) {
                                    console.log('componentEditor.edit');
                                    AGS3xIoTAdmin.util.events.fireEvent('saveComponent', editor, context, eOpts);
                                },
                                canceledit: function () {
                                    console.log('componentEditor.cancelEdit')
                                    console.log('componentEditor.cancelEdit - self.controller.newComponent: ', self.controller.newComponent);

                                    if (self.controller.newComponent == true) {
                                        var componentTypesDataStore = self.getViewModel().getStore('componentTypesDataStore');
                                        console.log('componentEditor.cancelEdit - componentTypesDataStore (before): ', componentTypesDataStore);
                                        componentTypesDataStore.removeAt(0, 1);
                                        console.log('componentEditor.cancelEdit - componentTypesDataStore (after): ', componentTypesDataStore);

                                        AGS3xIoTAdmin.util.events.fireEvent('setNewComponentValue', false);
                                    }

                                    AGS3xIoTAdmin.util.events.fireEvent('enableToolbar');
                                }
                            }
                        },
                        viewConfig: {
                            stripeRows: false
                        },
                        columns: [
                            { id: 'colComponentType', text: self.columnHeaderComponentType, dataIndex: 'type', width: 50 },
                            { id: 'colComponentName', text: self.columnHeaderComponentName, dataIndex: 'name', editor: 'textfield', width: 300 },
                            { id: 'colComponentDataStoreID', text: self.columnHeaderComponentDataStoreId, dataIndex: 'dataStoreName', width: 200 },
                            {
                                id: 'colComponentWfs',
                                text: self.columnHeaderComponentWfs,
                                dataIndex: 'wfs',
                                editor: {
                                    xtype: 'textfield',
                                    listeners: {
                                        blur: function (element) {
                                            AGS3xIoTAdmin.util.events.fireEvent('componentWfsBlurred', self.activeRecord, element);
                                        }
                                    }
                                },
                                flex: 1
                            },
                            {
                                id: 'colComponentWfsLayer',
                                text: self.columnHeaderComponentWfsLayer,
                                dataIndex: 'wfsLayer',
                                editor: {
                                    xtype: 'textfield',
                                    listeners: {
                                        blur: function (element) {
                                            AGS3xIoTAdmin.util.events.fireEvent('componentWfsBlurred', self.activeRecord, element);
                                        }
                                    }
                                },
                                flex: 1
                            },
                            {
                                id: 'colComponentFieldId',
                                text: self.columnHeaderComponentFieldId,
                                dataIndex: 'fieldId',
                                flex: 1,
                                editor: {
                                    xtype: 'combobox',
                                    bind: {
                                        store: '{fieldMappingStore}'
                                    },
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
                                id: 'colComponentFieldDescription',
                                text: self.columnHeaderComponentFieldDescription,
                                dataIndex: 'fieldDescription',
                                flex: 1,
                                editor: {
                                    xtype: 'combobox',
                                    bind: {
                                        store: '{fieldMappingStore}'
                                    },
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
                            { id: 'colComponentTableName', text: self.columnHeaderComponentComponentTableName, dataIndex: 'componentTableName', editor: 'textfield', width: 200 },
                            { id: 'colComponentEditDate', text: self.columnHeaderComponentEditDate, dataIndex: 'editDate', width: 150 }
                        ],
                        listeners: {
                            resize: function (component) {
                                console.log('Component Editor resizing');
                            },
                            itemclick: function (component, record, node, index, event, options) {
                                console.log('componentEditor - itemclick, component: ', component);
                                console.log('componentEditor - itemclick, record: ', record);
                                console.log('componentEditor - itemclick, node: ', node);
                                console.log('componentEditor - itemclick, index: ', index);
                                console.log('componentEditor - itemclick, event: ', event);
                                console.log('componentEditor - itemclick, options: ', options);

                                Ext.ComponentQuery.query('#buttonRemoveComponent')[0].setDisabled(false);

                                var rowEditorPlugin = Ext.ComponentQuery.query('#componentEditorDataTable')[0].getPlugin('roweditingId');
                                console.log('componentEditor - itemclick, rowEditorPlugin: ', rowEditorPlugin);

                                if (document.getElementById('componentEditorDataTable').getElementsByClassName('x-row-editor-cancel-button').length > 0) {
                                    document.getElementById('componentEditorDataTable').getElementsByClassName('x-row-editor-cancel-button')[0].click();
                                }
                            },
                            itemdblclick: function (component, record, node, index, event, options) {
                                console.log('componentEditor - itemdblclick, component: ', component);
                                console.log('componentEditor - itemdblclick, record: ', record);
                                console.log('componentEditor - itemdblclick, node: ', node);
                                console.log('componentEditor - itemdblclick, index: ', index);
                                console.log('componentEditor - itemdblclick, event: ', event);
                                console.log('componentEditor - itemdblclick, options: ', options);

                                Ext.ComponentQuery.query('#buttonRemoveComponent')[0].setDisabled(false);
                            },
                            sortchange: function (grid, sortingInfo) {
                                console.log('componentEditor - sortchange, grid :', grid);
                                console.log('componentEditor - sortchange, sortingInfo :', sortingInfo);

                                if (document.getElementById('componentEditorDataTable').getElementsByClassName('x-row-editor-cancel-button').length > 0) {
                                    document.getElementById('componentEditorDataTable').getElementsByClassName('x-row-editor-cancel-button')[0].click();
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
