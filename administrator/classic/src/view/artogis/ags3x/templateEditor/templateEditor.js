var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.templateEditor.templateEditor', {

    extend: 'Ext.panel.Panel',
    xtype: 'ags3xTemplateEditor',
    id: 'ags3xTemplateEditor',

    requires: [
     'AGS3xIoTAdmin.view.artogis.ags3x.templateEditor.templateEditorController',
     'AGS3xIoTAdmin.view.artogis.ags3x.templateEditor.templateEditorModel',

     'AGS3xIoTAdmin.view.artogis.ags3x.formulaEditor.formulaEditor',
     'AGS3xIoTAdmin.view.artogis.ags3x.formulaEditor.formulaEditorController'
    ],

    viewModel: {
        type: 'ags3xTemplateEditor'
    },

    controller: 'ags3xTemplateEditor',

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelTemplateEditorHeaderTitle: 'Skabelonkonfigurator for udregninger',
    fieldLabelTemplateEditorTitleExistingTemplates: 'Eksisterende skabeloner',
    fieldLabelTemplateEditorTitleConfiguration: 'Konfiguration',
    fieldLabelTemplateEditorTitleTemplate: 'Skabelon',
    fieldLabelTemplateEditorTemplateName: 'Skabelonnavn',
    fieldLabelTemplateEditorUnitInput: 'Enhed for alle',
    fieldLabelTemplateEditorUnitInputEmptyText: 'Vælg en enhedstype...',
    fieldLabelTemplateEditorTemplateAlias: 'Skabelonalias',
    fieldLabelTemplateEditorMeasurementType: 'Målingstype',
    fieldLabelTemplateEditorMeasurementTypeEmptyText: 'Vælg en målingstype...',

    templateEditorMeasurementName: 'Navn',

    fieldLabelTemplateEditorEditSave: 'Gem',
    fieldLabelTemplateEditorEditCancel: 'Fortryd',

    fieldLabelCalculations: 'Beregninger',

    fieldLabelCalculation: 'Udregning',
    fieldLabelAggregate: 'Aggregering',
    fieldLabelUnit: 'Enhed',
    fieldLabelScaleToUnit: 'Enhedsskalering',
    fieldLabelInterval: 'Interval',

    fieldLabelTemplateEditorDeleteTemplate: 'Slet skabelon',
    fieldLabelTemplateEditorSaveAsNewTemplate: 'Gem som ny skabelon',
    fieldLabelTemplateEditorUpdateTemplate: 'Opdater skabelon',
    fieldLabelTemplateEditorStartNewTemplate: 'Start ny skabelon',

    optionYes: 'Ja',
    optionNo: 'Nej',

    fieldLabelDataLoading: 'Vent venligst...',

    id: 'ags3xTemplateEditor',
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
                /*var loadingMask = new Ext.LoadMask(
                    {
                        msg: self.fieldLabelDataLoading,
                        id: 'templateFullMask',
                        target: self
                    }
                );

                loadingMask.show();*/
            }
        }
        
        self.items = [
            {
                xtype: 'panel',
                id: 'fullTemplateEditorPanel',
                height: '100%',
                margin: '20px 20px 0 20px',
                layout: 'border',
                bodyStyle: {
                    'background': '#f6f6f6'
                },
                header: {
                    title: 'TEST',
                    height: 44,
                    width: (window.innerWidth - 300 - 40 - 4),
                    margin: '0 0 2px 2px',
                    listeners: {
                        resize: function () {
                            //console.log('Resizing fullSensorListColumnLeft_header...');
                            //document.getElementById('fullSensorListColumnLeft_header').style.width = (window.innerWidth - 300 - 40 - 4) + 'px';
                        },
                        afterrender: function (component) {
                            component.setTitle(self.fieldLabelTemplateEditorHeaderTitle);

                            //Ext.ComponentQuery.query('#fullSensorListColumnLeft_header')[0].setHeight(window.innerHeight - 180);
                        }
                    }
                },
                items: [
                    {
                        xtype: 'panel',
                        id: 'fullTemplateEditorPanelLeft',
                        region: 'west',
                        layout: 'border',
                        height: window.innerHeight - 180,
                        width: 300,
                        margin: '20px 0 22px 2px',
                        bodyStyle: {
                            'float': 'left'
                        },
                        style: {
                            'box-shadow': '0 1px 2px rgba(0, 0, 0, 0.2) !important',
                            'float': 'left'
                        },
                        header: {
                            title: self.fieldLabelTemplateEditorTitleExistingTemplates,
                            style: {
                                'box-shadow': 'none !important',
                                'border-bottom': '1px solid #f5f5f5 !important'
                            }
                        },
                        items: [
                            {
                                xtype: 'gridpanel',
                                id: 'templateEditorTemplatesList',
                                region: 'center',
                                store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.templateConfigurationsStore'),
                                height: window.innerHeight - 180 - 65 - 20,
                                scrollable: true,
                                columns: [
                                    { id: 'colMeasurementType', text: self.templateEditorMeasurementName, dataIndex: 'measurementName', width: 300 }
                                ],
                                listeners: {
                                    itemclick: function (component, record, target, index, event) {
                                        console.log('templateEditor.itemclick (template) - component: ', component);
                                        console.log('templateEditor.itemclick (template) - record: ', record);
                                        console.log('templateEditor.itemclick (template) - target: ', target);
                                        console.log('templateEditor.itemclick (template) - index: ', index);
                                        console.log('templateEditor.itemclick (template) - event: ', event);

                                        AGS3xIoTAdmin.util.events.fireEvent('templateEditorTemplateSelected', record);
                                    },
                                    afterrender: function(component) {
                                        document.getElementById('templateEditorTemplatesList').getElementsByClassName('x-grid-view')[0].style.overflowX = 'hidden !important';
                                    }
                                }
                            },
                            {
                                xtype: 'panel',
                                id: 'templateEditorTemplatesListButtonsHolder',
                                region: 'south',
                                padding: 10,
                                style: {
                                    'background': 'white !important'
                                },
                                items: [
                                    {
                                        xtype: 'button',
                                        id: 'buttonDeleteTemplate',
                                        text: self.fieldLabelTemplateEditorDeleteTemplate,
                                        disabled: true,
                                        iconCls: 'fa fa-trash',
                                        style: {
                                            'float': 'right'
                                        },
                                        handler: 'deleteTemplate'
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        xtype: 'panel',
                        id: 'fullTemplateEditorPanelMain',
                        region: 'center',
                        height: 500,
                        //width: window.innerWidth - 300 - 322 - 40 - 4,
                        //width: 'auto',
                        margin: '20px 2px 2px 22px',
                        bodyStyle: {
                            'float': 'left',
                            'background': 'none !important'
                        },
                        style: {
                            //'box-shadow': '0 1px 2px rgba(0, 0, 0, 0.2) !important',
                            'float': 'left',
                            'background': 'none !important'
                        },
                        header: {
                            title: self.fieldLabelTemplateEditorTitleConfiguration,
                            style: {
                                'box-shadow': '0 1px 2px rgba(0, 0, 0, 0.2) !important',
                                'border-bottom': '1px solid #f5f5f5 !important',
                                'margin': '0 0 0 2px'
                            }
                        },
                        items: [
                            {
                                xtype: 'panel',
                                id: 'fullTemplateEditorPanelCenterHolder',
                                //layout: 'border',
                                height: 600,
                                style: {
                                    'background': 'none !important'
                                },
                                bodyStyle: {
                                    'background': 'none !important',
                                    'padding': '0 2px 0 0'
                                },
                                items: [
                                    {
                                        xtype: 'panel',
                                        id: 'fullTemplateEditorPanelCenter',
                                        //region: 'center',
                                        height: 270,
                                        //width: window.innerWidth - 300 - 40 - 300 - 340 - 13,
                                        width: 500,
                                        margin: '20px 20px 2px 2px',
                                        bodyStyle: {
                                            'float': 'left'
                                        },
                                        style: {
                                            'box-shadow': '0 1px 2px rgba(0, 0, 0, 0.2) !important',
                                            'float': 'left'
                                        },
                                        header: {
                                            title: self.fieldLabelTemplateEditorTitleTemplate,
                                            style: {
                                                'box-shadow': 'none !important',
                                                'border-bottom': '1px solid #f5f5f5 !important'
                                            }
                                        },
                                        items: [
                                            {
                                                xtype: 'panel',
                                                id: 'templateEditorCalculationsToolsPanel',
                                                items: [
                                                    {
                                                        xtype: 'panel',
                                                        id: 'templateEditorCalculationNameHolder',
                                                        margin: 10,
                                                        height: 80,
                                                        layout: 'border',
                                                        items: [
                                                            {
                                                                xtype: 'panel',
                                                                region: 'north',
                                                                items: [
                                                                    {
                                                                        xtype: 'textfield',
                                                                        id: 'templateEditorCalculationNameInput',
                                                                        width: 400,
                                                                        fieldLabel: self.fieldLabelTemplateEditorTemplateName,
                                                                        labelWidth: 130,
                                                                        disabled: true
                                                                    }

                                                                    /*,
                                                                    {
                                                                        xtype: 'textfield',
                                                                        id: 'templateEditorCalculationAliasInput',
                                                                        width: 300,
                                                                        fieldLabel: self.fieldLabelTemplateEditorTemplateAlias
                                                                    }*/
                                                                ]
                                                            },
                                                            {
                                                                xtype: 'panel',
                                                                region: 'center',
                                                                items: [
                                                                    {
                                                                        xtype: 'combobox',
                                                                        id: 'templateEditorMeasurementTypeCombobox',
                                                                        width: 400,
                                                                        margin: '0 2px 0 0',
                                                                        fieldLabel: self.fieldLabelTemplateEditorMeasurementType,
                                                                        labelWidth: 130,
                                                                        valueField: 'id',
                                                                        displayField: 'name',
                                                                        store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.measurementTypesStore'),
                                                                        bind: {
                                                                            value: '{id}'
                                                                        },
                                                                        editable: false,
                                                                        disabled: true,
                                                                        queryMode: 'local',
                                                                        emptyText: self.fieldLabelTemplateEditorMeasurementTypeEmptyText,
                                                                        tpl: Ext.create('Ext.XTemplate',
                                                                            '<ul class="x-list-plain"><tpl for=".">',
                                                                                '<li role="option" class="x-boundlist-item signal-{isSignalStrength} battery-{isBatteryStatus}">{name}</li>',
                                                                            '</tpl></ul>'
                                                                        ),
                                                                        listeners: {
                                                                            afterrender: function (component) {
                                                                                //component.setEmptyText('Vælg en målingstype...');
                                                                            },
                                                                            change: function (component, value, c, d) {
                                                                                console.log('Header combobox change - component: ', component);
                                                                                console.log('Header combobox change - value: ', value);
                                                                                console.log('Header combobox change - c: ', c);
                                                                                console.log('Header combobox change - d: ', d);

                                                                                AGS3xIoTAdmin.util.events.fireEvent('templateEditorMeasurementSelected', value);
                                                                            }
                                                                        }
                                                                    }
                                                                ]
                                                            },
                                                            {
                                                                xtype: 'panel',
                                                                region: 'south',
                                                                items: [
                                                                    {
                                                                        xtype: 'combobox',
                                                                        id: 'templateEditorCalculationAllUnitInput',
                                                                        width: 400,
                                                                        margin: '0 2px 0 0',
                                                                        fieldLabel: self.fieldLabelTemplateEditorUnitInput,
                                                                        labelWidth: 130,
                                                                        store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unitsStore'),
                                                                        value: null,
                                                                        /*bind: {
                                                                            store: '{unitTypesStoreAll}',
                                                                            value: '{id}'
                                                                        },*/
                                                                        valueField: 'name',
                                                                        displayField: 'name',
                                                                        editable: false,
                                                                        queryMode: 'local',
                                                                        tpl: Ext.create('Ext.XTemplate',
                                                                            '<ul class="x-list-plain"><tpl for=".">',
                                                                                '<li role="option" class="x-boundlist-item" title="{description}">{name}</li>',
                                                                            '</tpl></ul>'
                                                                        ),
                                                                        emptyText: self.fieldLabelTemplateEditorUnitInputEmptyText,
                                                                        disabled: true,
                                                                        queryMode: 'local',
                                                                        listeners: {
                                                                            afterrender: function (component) {
                                                                                //component.setEmptyText('Vælg en målingstype...');
                                                                            },
                                                                            change: function (component, value, c, d) {
                                                                                console.log('Header combobox change - component: ', component);
                                                                                console.log('Header combobox change - value: ', value);
                                                                                console.log('Header combobox change - c: ', c);
                                                                                console.log('Header combobox change - d: ', d);

                                                                                AGS3xIoTAdmin.util.events.fireEvent('templateEditorOverallUnitSelected', component, value);
                                                                            }
                                                                        }
                                                                    }

                                                                    /*,
                                                                    {
                                                                        xtype: 'textfield',
                                                                        id: 'templateEditorCalculationAliasInput',
                                                                        width: 300,
                                                                        fieldLabel: self.fieldLabelTemplateEditorTemplateAlias
                                                                    }*/
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                ]
                                            },
                                            {
                                                xtype: 'gridpanel',
                                                id: 'templateEditorCalculationsGridpanel',
                                                scrollable: true,
                                                style: {
                                                    'border-top': '1px solid #f5f5f5 !important'
                                                },
                                                /*plugins: [
                                                    Ext.create('Ext.grid.plugin.RowEditing', {
                                                        clicksToEdit: 2,
                                                        saveBtnText: self.fieldLabelRecordButtonSave,
                                                        cancelBtnText: self.fieldLabelRecordButtonCancel,
                                                    })
                                                ],*/
                                                plugins: {
                                                    ptype: 'rowediting',
                                                    clicksToEdit: 2,
                                                    pluginId: 'roweditingId',

                                                    saveBtnText: self.fieldLabelTemplateEditorEditSave,
                                                    cancelBtnText: self.fieldLabelTemplateEditorEditCancel,

                                                    listeners: {
                                                        beforeedit: function (editor, context, eOpts) {
                                                            console.log('componentEditor.beforeedit - editor: ', editor);
                                                            console.log('componentEditor.beforeedit - context: ', context);
                                                        },
                                                        edit: function (editor, context, eOpts) {
                                                            console.log('componentEditor.edit - editor: ', editor);
                                                            console.log('componentEditor.edit - context: ', context);
                                                            console.log('componentEditor.edit - eOpts: ', eOpts);
                                                            
                                                            //context.record.commit();

                                                            for(key in context.newValues) {
                                                                console.log(key + ' : ' + context.newValues[key]);
                                                                
                                                                if (key != null && key != 'null') {
                                                                    console.log(key + ' : ' + context.newValues[key]);

                                                                    context.record.data[key] = context.newValues[key];
                                                                }
                                                            }

                                                            context.record.commit();
                                                        },
                                                        canceledit: function () {
                                                            console.log('componentEditor.cancelEdit')
                                                            console.log('componentEditor.cancelEdit - self.controller.newComponent: ', self.controller.newComponent);
                                                        }
                                                    }
                                                },
                                                bind: {
                                                    store: '{selectedTemplateStore}',
                                                    columns: '{calculationGridColumns}'
                                                },
                                                listeners: {
                                                    itemclick: function (component, record, target, index, event) {
                                                        console.log('templateEditor.itemclick (template) - component: ', component);
                                                        console.log('templateEditor.itemclick (template) - record: ', record);
                                                        console.log('templateEditor.itemclick (template) - target: ', target);
                                                        console.log('templateEditor.itemclick (template) - index: ', index);
                                                        console.log('templateEditor.itemclick (template) - event: ', event);

                                                        AGS3xIoTAdmin.util.events.fireEvent('templateEditorTemplateCalculationSelected', record);
                                                    }
                                                }
                                            }
                                        ]
                                    },
                                    // here
                                    {
                                        xtype: 'panel',
                                        id: 'fullTemplateEditorPanelRight',
                                        //region: 'east',
                                        height: 270,
                                        width: 800,
                                        margin: '20px 0 2px 2px',
                                        bodyStyle: {
                                            'float': 'left'
                                        },
                                        style: {
                                            'box-shadow': '0 1px 2px rgba(0, 0, 0, 0.2) !important',
                                            'float': 'left'
                                        },
                                        header: {
                                            title: self.fieldLabelCalculations + ' <span id="templateEditorAggregationsAndStoresTarget"></span>',
                                            style: {
                                                'box-shadow': 'none !important',
                                                'border-bottom': '1px solid #f5f5f5 !important'
                                            }
                                        },
                                        items: [
                                            {
                                                xtype: 'gridpanel',
                                                id: 'templateEditorAggregationsAndStoresGridPanel',
                                                width: 530,
                                                //width: '100%',
                                                height: 153,
                                                style: {
                                                    'float': 'left'
                                                },
                                                bodyStyle: {
                                                    'float': 'left'
                                                },
                                                multiSelect: false,
                                                frame: true,
                                                allowDeselect: true,
                                                bind: {
                                                    store: '{selectedAggregationAndStores}'
                                                },
                                                plugins: {
                                                    ptype: 'rowediting',
                                                    clicksToEdit: 1,
                                                    saveBtnText: self.fieldLabelTemplateEditorEditSave,
                                                    cancelBtnText: self.fieldLabelTemplateEditorEditCancel,
                                                    listeners: {
                                                        beforeedit: function (a, view) {
                                                            console.log('templateEditor.beforeedit - a: ', a);
                                                            console.log('templateEditor.beforeedit - view.record: ', view.record);
                                                            console.log('templateEditor.beforeedit - view.record.data: ', view.record.data);
                                                        },
                                                        edit: function (a, view) {
                                                            console.log('templateEditor.edit - a: ', a);
                                                            console.log('templateEditor.edit - view.record: ', view.record);
                                                            console.log('templateEditor.edit - view.record.data: ', view.record.data);
                                                        }
                                                    }
                                                },
                                                columns: [
                                                    {
                                                        text: self.fieldLabelCalculation,
                                                        dataIndex: 'aggregationCalculation',
                                                        flex: 2
                                                    },
                                                    {
                                                        text: self.fieldLabelAggregate,
                                                        dataIndex: 'aggregate',
                                                        flex: 1,
                                                        renderer: function (value) {
                                                            console.log('Rendering: ', value);
                                                            switch (value) {
                                                                case true:
                                                                    return self.optionYes;
                                                                case false:
                                                                    return self.optionNo;
                                                            }
                                                        },
                                                        editor: {
                                                            xtype: 'combobox',
                                                            store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.boolTypesStore'),
                                                            displayField: 'name',
                                                            valueField: 'type',
                                                            queryMode: 'local',
                                                            tpl: Ext.create('Ext.XTemplate',
                                                                '<ul class="x-list-plain"><tpl for=".">',
                                                                    '<li role="option" class="x-boundlist-item">{name}</li>',
                                                                '</tpl></ul>'
                                                            )
                                                        }
                                                    },
                                                    {
                                                        text: self.fieldLabelUnit,
                                                        dataIndex: 'unit',
                                                        flex: 1,
                                                        editor: {
                                                            xtype: 'combobox',
                                                            store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unitsStore'),
                                                            value: 'id',
                                                            valueField: 'name',
                                                            displayField: 'name',
                                                            editable: false,
                                                            queryMode: 'local',
                                                            tpl: Ext.create('Ext.XTemplate',
                                                                '<ul class="x-list-plain"><tpl for=".">',
                                                                    '<li role="option" class="x-boundlist-item" title="{description}">{name}</li>',
                                                                '</tpl></ul>'
                                                            ),
                                                            listeners: {
                                                                change: function () {
                                                                    console.log('templateEditor - single unit dropdown changed');
                                                                }
                                                            }
                                                        }
                                                    },
                                                    {
                                                        text: self.fieldLabelScaleToUnit,
                                                        dataIndex: 'scaleToUnit',
                                                        width: 120,
                                                        editor: {
                                                            xtype: 'textfield',
                                                            maskRe: /[0-9.-]/
                                                        }
                                                    },
                                                    {
                                                        text: self.fieldLabelInterval,
                                                        dataIndex: 'aggregationInterval',
                                                        width: 75
                                                    }
                                                ]
                                            },

                                            {
                                                xtype: 'gridpanel',
                                                id: 'templateEditorStatusGridPanel',
                                                region: 'east',
                                                width: 265,
                                                height: 153,
                                                style: {
                                                    'float': 'right'
                                                },
                                                bodyStyle: {
                                                    'overflow': 'hidden !important',
                                                    'float': 'right'
                                                },
                                                multiSelect: false,
                                                frame: true,
                                                allowDeselect: true,
                                                bind: {
                                                    store: '{selectedAggregationAndStores}'
                                                },
                                                plugins: {
                                                    ptype: 'rowediting',
                                                    clicksToEdit: 1,
                                                    saveBtnText: self.fieldLabelTemplateEditorEditSave,
                                                    cancelBtnText: self.fieldLabelTemplateEditorEditCancel,
                                                    listeners: {
                                                        edit: function (editor, context, options) {
                                                            console.log('dataIOController.edit - editor: ', editor);
                                                            console.log('dataIOController.edit - context: ', context);
                                                            console.log('dataIOController.edit - options: ', options);

                                                            // convert strings with commas to strings with punctuations for number conversion

                                                            AGS3xIoTAdmin.util.events.fireEvent('templateEditorToggleStatusMonitoring', editor, context, options);
                                                        },
                                                        beforeedit: function (editor, context, options) {
                                                            console.log('dataIOController.beforeedit - editor: ', editor);
                                                            console.log('dataIOController.beforeedit - context: ', context);
                                                            console.log('dataIOController.beforeedit - options: ', options);


                                                        }
                                                    }
                                                },
                                                columns: [
                                                    {
                                                        text: 'Status',
                                                        dataIndex: 'statusOn',
                                                        width: 60,
                                                        sortable: false,
                                                        cls: 'formula-header-extra',
                                                        tdCls: 'open-status-button',
                                                        renderer: function (value, b, c, d, e) {
                                                            console.log('Value: ', value);
                                                            if (value == true) {
                                                                return '<div class="fa fa-bell" title="Statusmonitorering er aktiveret"></div>'
                                                            }
                                                            else {
                                                                return '<div class="fa fa-bell-slash-o" title="Statusmonitorering er ikke aktiveret"></div>'
                                                            }
                                                        },
                                                        listeners: {
                                                            click: function (component, b, c, d, e) {
                                                                console.log('click - open formular editor: ', component);
                                                                console.log('Row: ', component.eventPosition.rowIdx);
                                                            }
                                                        }
                                                    },
                                                    {
                                                        text: 'Max',
                                                        dataIndex: 'max',
                                                        width: 50,
                                                        editor: {
                                                            xtype: 'textfield',
                                                            maskRe: /[0-9.,-]/
                                                        }
                                                    },
                                                    {
                                                        text: 'High',
                                                        dataIndex: 'high',
                                                        width: 50,
                                                        editor: {
                                                            xtype: 'textfield',
                                                            maskRe: /[0-9.,-]/
                                                        }
                                                    },
                                                    {
                                                        text: 'Low',
                                                        dataIndex: 'low',
                                                        width: 50,
                                                        editor: {
                                                            xtype: 'textfield',
                                                            maskRe: /[0-9.,-]/
                                                        }
                                                    },
                                                    {
                                                        text: 'Min',
                                                        dataIndex: 'min',
                                                        width: 50,
                                                        editor: {
                                                            xtype: 'textfield',
                                                            maskRe: /[0-9.,-]/
                                                        }
                                                    }
                                                ],
                                                listeners: {
                                                    afterrender: function () {
                                                        // refit template editor
                                                        var windowInnerWidth = window.innerWidth;
                                                        var templateEditorAggregationsAndStoresGridPanel = Ext.ComponentQuery.query('#templateEditorAggregationsAndStoresGridPanel')[0];
                                                        var templateEditorStatusGridPanel = Ext.ComponentQuery.query('#templateEditorStatusGridPanel')[0];

                                                        if (Ext.ComponentQuery.query('#fullTemplateEditorPanelCenterHolder').length > 0) {
                                                            if (windowInnerWidth <= 1280) {
                                                                console.log('MainController.performResize - template editor refit - narrow: ', windowInnerWidth);

                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenter')[0].setWidth(windowInnerWidth - 670);

                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelRight')[0].setWidth(windowInnerWidth - 670);

                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenterHolder')[0].setHeight(588);

                                                                document.getElementById('fullTemplateEditorPanelRight-outerCt').style.width = '800px';

                                                                document.getElementById('fullTemplateEditorPanelRight-body').style.overflowX = 'scroll';

                                                            }
                                                            else if (windowInnerWidth > 1280 && windowInnerWidth <= 1470) {
                                                                console.log('MainController.performResize - template editor refit - medium: ', windowInnerWidth);

                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenter')[0].setWidth(windowInnerWidth - 670);

                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelRight')[0].setWidth(windowInnerWidth - 670);

                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenterHolder')[0].setHeight(588);

                                                                document.getElementById('fullTemplateEditorPanelRight-outerCt').style.width = '800px';

                                                                document.getElementById('fullTemplateEditorPanelRight-body').style.overflowX = 'scroll';
                                                            }
                                                            else if (windowInnerWidth > 1470 && windowInnerWidth <= 1992) {
                                                                console.log('MainController.performResize - template editor refit - wide: ', windowInnerWidth);

                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenter')[0].setWidth(800);
                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelRight')[0].setWidth(800);

                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenterHolder')[0].setHeight(588);

                                                                document.getElementById('fullTemplateEditorPanelRight-body').style.overflowX = 'hidden';
                                                            }
                                                            else if (windowInnerWidth > 1992) {
                                                                console.log('MainController.performResize - template editor refit - full: ', windowInnerWidth);

                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenter')[0].setWidth(500);
                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelRight')[0].setWidth(800);

                                                                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenterHolder')[0].setHeight(304);

                                                                //document.getElementById('fullTemplateEditorPanelRight-body').style.overflowX = 'hidden';

                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                xtype: 'panel',
                                id: 'fullTemplateEditorPanelButtonsHolder',
                                margin: '20px 0 0 2px',
                                style: {
                                    'box-shadow': '0 1px 2px rgba(0, 0, 0, 0.2) !important'
                                },
                                bodyStyle: {
                                    'padding': '10px 12px 10px 10px'
                                },
                                items: [
                                    {
                                        xtype: 'button',
                                        id: 'buttonTemplateEditorSaveAsNewTemplate',
                                        text: self.fieldLabelTemplateEditorSaveAsNewTemplate,
                                        disabled: true,
                                        iconCls: 'fa fa-floppy-o',
                                        style: {
                                            float: 'right',
                                            'margin-left': '10px'
                                        },
                                        handler: 'saveAsNewTemplate'
                                    },
                                    {
                                        xtype: 'button',
                                        id: 'buttonTemplateEditorUpdateTemplate',
                                        text: self.fieldLabelTemplateEditorUpdateTemplate,
                                        disabled: true,
                                        iconCls: 'fa fa-refresh',
                                        style: {
                                            float: 'right',
                                            'margin-left': '10px'
                                        },
                                        handler: 'updateTemplate'
                                    },
                                    {
                                        xtype: 'button',
                                        id: 'buttonTemplateEditorStartNewTemplate',
                                        text: self.fieldLabelTemplateEditorStartNewTemplate,
                                        iconCls: 'fa fa-plus',
                                        style: {
                                            'float': 'right',
                                            'margin-left': '10px'
                                        },
                                        handler: 'startNewTemplate'
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ]

        self.callParent(arguments);
    }

});