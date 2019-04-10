var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dataIO.dataIO', {
    extend: 'Ext.panel.Panel',
    xtype: 'ags3xDataIOConfig',

    requires: [
        'AGS3xIoTAdmin.util.events',

        'AGS3xIoTAdmin.view.artogis.ags3x.dataIO.dataIOController',
        'AGS3xIoTAdmin.view.artogis.ags3x.dataIO.dataIOModel',

        'AGS3xIoTAdmin.view.artogis.ags3x.resultList.resultList',

        'AGS3xIoTAdmin.view.artogis.ags3x.map.map',
        'AGS3xIoTAdmin.view.artogis.ags3x.mapSearch.mapSearch',

        'AGS3xIoTAdmin.view.artogis.ags3x.toc.tocController',
        'AGS3xIoTAdmin.view.artogis.ags3x.toc.tocModel',

        'AGS3xIoTAdmin.view.artogis.ags3x.toc.toc'
    ],

    viewModel: {
        type: 'ags3xDataIOConfig'
    },

    controller: 'ags3xDataIOConfig',

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelDataHeaderTitle: 'Data',
    fieldLabelUnconfiguredTitle: 'Sensorer',
    fieldLabelUnconfiguredSource: 'Datakilde',
    fieldLabelUnconfiguredSensorId: 'Sensor ID',
    fieldLabelUnconfiguredName: 'Navn fra datakilde',
    fieldLabelUnconfiguredDescription: 'Beskrivelse',
    fieldLabelUnconfiguredId: 'ID',
    fieldLabelUnconfiguredUnit: 'Enhed',

    fieldLabelCalculationsTitle: 'Beregninger',
    fieldLabelCalculationsCalculation: 'Beregning',
    fieldLabelCalculationsFormula: 'Formular',
    fieldLabelCalculationsData: 'Data',
    fieldLabelCalculationsUnit: 'Enhed',
    fieldLabelCalculationsStore: 'Gem',
    fieldLabelCalculationsAverageTwoMinutes: 'Gennemsnit 2 min.',
    fieldLabelCalculationsAverageSixtyMinutes: 'Gennemsnit 60 min.',
    fieldLabelCalculationsSumSixtyMinutes: 'Sum 60 min.',
    fieldLabelCalculationsPlaceholder: 'Vælg venligst en skabelon…',
    
    fieldLabelConfiguredTitle: 'Konfigureret data',
    fieldLabelConfiguredSensorId: 'Sensor ID',
    fieldLabelConfiguredName: 'Navn',
    fieldLabelConfiguredDescription: 'Beskrivelse',
    fieldLabelConfiguredAlias: 'Alias',
    fieldLabelConfiguredMeasurementName: 'Målingsbeskrivelse',
    fieldLabelRecordButtonSave: 'Gem',
    fieldLabelRecordButtonCancel: 'Fortryd',

    fieldLabelObjectCompTitle: 'Objekt eller komponent',
    fieldLabelObjectCompType: 'Type',
    fieldLabelObjectCompName: 'Navn',
    fieldLabelObjectCompId: 'ID',

    fieldLabelButtonSave: 'Gem',
    fieldLabelButtonClear: 'Ryd',
    fieldLabelButtonRefreshData: 'Genopfrisk data',
    fieldLabelButtonGoToTop: 'Gå til toppen',
    fieldLabelButtonResetDataIO: 'Nulstil sensor',

    fieldLabelButtonCancelComponentMove: 'Afbryd komponentflytning',

    textMaskCalculations: 'Der er ikke valgt en beregningsskabelon',
    textMaskConfiguredData: 'Der er ikke valgt nogen ukonfigureret data',
    textMaskObjectsAndComponents: 'Der er ikke valgt en komponent eller et objekt',

    fieldLabelMapHeaderTitle: 'Kort',

    configuredTypesShowAllSensors: 'Vis alle sensorer',
    configuredTypesShowConfiguredSensors: 'Vis kun konfigurerede sensorer',
    configuredTypesShowUnconfiguredSensors: 'Vis kun ikke-konfigurerede sensorer',

    doubleClickIntervalStarted: null,

    configuredTypes: {
        fields: ['value', 'name'],
        data: [
            { 'value': 1, 'name': null }, 
            { 'value': 2, 'name': null }, 
            { 'value': 3, 'name': null } 
        ]
    },

    id: 'ags3xDataIOConfig',
    cls: 'contentPanelBox',
    layout: 'fit',

    oldDataIOComponentWidth: null,

    initComponent: function () {
        self = this;

        self.configuredTypes.data[0].name = self.configuredTypesShowAllSensors;
        self.configuredTypes.data[1].name = self.configuredTypesShowConfiguredSensors;
        self.configuredTypes.data[2].name = self.configuredTypesShowUnconfiguredSensors;

        console.log('dataIO - initComponent');
        
        self.items = [
            {
                xtype: 'panel',
                layout: 'border',
                items: [
                    // data grids
                    {
                        xtype: 'panel',
                        id: 'leftPanelContent',
                        region: 'west',
                        title: self.fieldLabelDataHeaderTitle,
                        collapsible: true,
                        frame: true,
                        resizable: true,
                        width: '65%',
                        margin: '20px 0 20px 20px',
                        style: {
                            'box-shadow': '0 1px 2px 0 rgba(0, 0, 0, 0.2)',
                            'border-radius': '0px'
                        },
                        layout: {
                            type: 'vbox',
                            align: 'stretch',
                            pack: 'start'
                        },
                        listeners: {
                            afterrender: function () {
                                document.getElementById('leftPanelContent-innerCt').style.overflowY = 'auto';
                            }
                        },
                        items: [

                            //////////////////////////////////////////////
                            // UNCONFIGURED DATA                        //
                            //////////////////////////////////////////////
                            {
                                xtype: 'panel',
                                title: self.fieldLabelUnconfiguredTitle,
                                id: 'panelDataGridUnconfigured',
                                flex: 1,
                                minHeight: 100,
                                layout: 'fit',
                                tools: [
                                     {
                                         xtype: 'combobox',
                                         id: 'comboxConfiguredFilter',
                                         width: 250,
                                         valueField: 'value',
                                         displayField: 'name',
                                         bind: {
                                             store: self.configuredTypes,
                                             value: '{value}'
                                         },
                                         value: 1,
                                         editable: false,
                                         queryMode: 'local',
                                         tpl: Ext.create('Ext.XTemplate',
                                             '<ul class="x-list-plain"><tpl for=".">',
                                                 '<li role="option" class="x-boundlist-item">{name}</li>',
                                             '</tpl></ul>'
                                         ),
                                         listeners: {
                                             change: function (a, b, c, d) {
                                                 console.log('Configured combobox change - a: ', a);
                                                 AGS3xIoTAdmin.util.events.fireEvent('configurationSelected', a.value);
                                             }
                                         }
                                     },
                                    {
                                        xtype: 'button',
                                        id: 'buttonRefreshUnconfiguredData',
                                        text: self.fieldLabelButtonRefreshData,
                                        iconCls: 'fa fa-refresh',
                                        style: {
                                            'float': 'right',
                                            'margin-left': '10px'
                                        },
                                        handler: 'refreshUnconfiguredData'
                                    },
                                    {
                                        xtype: 'button',
                                        id: 'buttonUnconfiguredDataGoToTop',
                                        text: self.fieldLabelButtonGoToTop,
                                        iconCls: 'fa fa-long-arrow-up',
                                        style: {
                                            'float': 'right',
                                            'margin-left': '10px'
                                        },
                                        handler: function () {
                                            console.log('Button - go to top');
                                            var scrollElement = document.getElementById('dataGridUnconfigured').getElementsByClassName('x-grid-view')[0].scrollTop = 0;
                                        }
                                    },
                                    {
                                        xtype: 'button',
                                        id: 'buttonResetDataIO',
                                        text: self.fieldLabelButtonResetDataIO,
                                        iconCls: 'fa fa-eraser',
                                        disabled: true,
                                        style: {
                                            'float': 'right',
                                            'margin-left': '10px'
                                        },
                                        handler: function (a,b,c,d) {
                                            console.log('Button - reser Data IO - a: ', a);
                                            console.log('Button - reser Data IO - b: ', b);
                                            console.log('Button - reser Data IO - c: ', c);
                                            console.log('Button - reser Data IO - d: ', d);

                                            AGS3xIoTAdmin.util.events.fireEvent('resetDataIO');
                                        }
                                    }
                                ],
                                items: [
                                    {
                                        xtype: 'gridpanel',
                                        id: 'dataGridUnconfigured',
                                        scrollable: true,
                                        multiSelect: false,
                                        allowDeselect: true,
                                        frame: true,
                                        margin: '0 0 10 0',
                                        plugins: [
                                            {
                                                ptype: 'gridfilters',
                                                id: 'filterplugin',
                                                stateId: 'filtertest'
                                            },
                                            {
                                                ptype: 'rowediting',
                                                clicksToEdit: 2,
                                                pluginId: 'roweditingId',

                                                saveBtnText: self.fieldLabelRecordButtonSave,
                                                cancelBtnText: self.fieldLabelRecordButtonCancel,

                                                listeners: {
                                                    edit: function (editor, context, eOpts) {

                                                        console.log('dataIO - device editor, edit - editor: ', editor);
                                                        console.log('dataIO - device editor, edit - context: ', context);
                                                        console.log('dataIO - device editor, edit - eOpts: ', eOpts);

                                                        AGS3xIoTAdmin.util.events.fireEvent('dataIODeviceAliasChanged', editor, context, eOpts);

                                                    },
                                                    canceledit: function (editor, context, eOpts) {
                                                        console.log('dataIO - device editor, canceledit');
                                                    }
                                                }
                                            }
                                        ],
                                        store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore'),
                                        bind: {
                                            columns: '{unconfiguredGridColumns}',
                                            selection: '{unconfiguredGridSelection}'
                                        },
                                        viewConfig: {
                                            stripeRows: false,
                                            getRowClass: function (record) {

                                                switch (record.get('configured')) {
                                                    case true:
                                                        return 'configured-true';
                                                    case false:
                                                        return 'configured-false';
                                                    default:
                                                        return '  x-grid-row';
                                                }
                                            }
                                        },
                                        listeners: {
                                            cellclick: function (view, cellEl, colIdx, store, rowEl, rowIdx, event) {
                                                var selected = event.item.classList.contains('x-grid-item-selected');
                                                console.log('dataIO.cellclick - store: ', store);
                                                if (selected == true) {
                                                    AGS3xIoTAdmin.util.events.fireEvent('dataEntrySelected', store);
                                                }
                                                else {
                                                    AGS3xIoTAdmin.util.events.fireEvent('dataEntryUnselected');
                                                }
                                            },
                                            afterrender: function (comp) {
                                                // update title with proper count
                                                console.log('Comp: ', comp);
                                                var titleComponent = Ext.ComponentQuery.query('#panelDataGridUnconfigured')[0];
                                                var count = comp.store.data.length;
                                                console.log('Count: ', count);
                                                var newTitle = titleComponent.getTitle() + ' <span id="dataIOCount" style="font-size:0.8em;color:#a2a2a2;">(' + count + ')</span>';
                                                titleComponent.setTitle(newTitle);
                                                console.log('Title - afterrender: ', titleComponent.getTitle());

                                            },
                                            filterchange: function (store, filters, eOpts) {
                                                console.log('dataIO.filterchange - store: ', store);
                                                console.log('dataIO.filterchange - filters: ', filters);
                                                console.log('dataIO.filterchange - eOpts: ', eOpts);

                                                AGS3xIoTAdmin.util.events.fireEvent('processFilterChange', filters);
                                            }
                                        }
                                    }
                                ]
                            },

                            {
                                xtype: 'panel',
                                height: 12
                            },

                            //////////////////////////////////////////////
                            // CALCULATIONS                             //
                            //////////////////////////////////////////////
                            {
                                xtype: 'panel',
                                title: self.fieldLabelCalculationsTitle,
                                maskText: self.textMaskCalculations,
                                placeholderText: self.fieldLabelCalculationsPlaceholder,
                                id: 'panelCalculations',
                                height: 190,
                                tools: [
                                    {
                                        xtype: 'combobox',
                                        id: 'comboxTemplateOptionsHeader',
                                        width: 200,
                                        store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.measurementTemplatesStore'),
                                        bind: {
                                            //store: '{measurementTemplateType}',
                                            value: '{templateType}'
                                        },
                                        valueField: 'templateType',
                                        displayField: 'measurementName',
                                        editable: false,
                                        queryMode: 'local',
                                        emptyText: self.fieldLabelCalculationsPlaceholder,
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{measurementName}</li>',
                                            '</tpl></ul>'
                                        ),
                                        disabled: true,
                                        listeners: {
                                            change: function (a, b, c, d) {
                                                console.log('Header combobox change - a: ', a);
                                                console.log('Header combobox - disabled: ', a.disabled);
                                                //console.log('combobox change - b: ', b);
                                                //console.log('combobox change - c: ', c);
                                                //console.log('combobox change - d: ', d);

                                                AGS3xIoTAdmin.util.events.fireEvent('dataIOTemplateSelected', a);
                                            }
                                        }
                                    }
                                ],
                                items: [
                                    {
                                        xtype: 'gridpanel',
                                        id: 'gridpanelCalculation',
                                        scrollable: true,
                                        multiSelect: false,
                                        frame: true,
                                        allowDeselect: true,
                                        plugins: [
                                            Ext.create('Ext.grid.plugin.RowEditing', {
                                                clicksToEdit: 2,
                                                saveBtnText: self.fieldLabelRecordButtonSave,
                                                cancelBtnText: self.fieldLabelRecordButtonCancel
                                            })
                                        ],
                                        bind: {
                                            store: '{calculationDataStore}',
                                            columns: '{calculationGridColumns}'
                                        }
                                    }
                                ],
                                listeners: {
                                    afterrender: function (component) {
                                        var maskSelf = this;

                                        var myMask = new Ext.LoadMask(
                                            {
                                                msg: maskSelf.maskText,
                                                id: 'maskCalculations',
                                                target: component,
                                                //cls: 'io-data-mask',
                                                style: {
                                                    /*'opacity': '0.5',*/
                                                    'width': '100%',
                                                    'height': '100%'
                                                }
                                            }
                                        );
                                        myMask.show();

                                        Ext.create('Ext.form.Panel', {
                                            id: 'templateSelector',
                                            renderTo: Ext.get('maskCalculations'),
                                            height: 'auto',
                                            width: 'auto',
                                            top: 50,
                                            margin: '38px auto 0 auto',
                                            style: {
                                                'background': 'transparent !important'
                                            },
                                            bodyStyle: {
                                                'background': 'transparent !important',
                                                'width': '100%'
                                            },
                                            items: [
                                                {
                                                    xtype: 'combobox',
                                                    id: 'comboxTemplateOptions',
                                                    emptyText: this.placeholderText,
                                                    //store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.measurementTemplatesStore'),
                                                    store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.templateConfigurationsStore'),
                                                    bind: {
                                                        //store: '{measurementTemplateType}',
                                                        value: '{templateType}'
                                                    },
                                                    valueField: 'templateType',
                                                    displayField: 'measurementName',
                                                    disabled: true,
                                                    editable: false,
                                                    queryMode: 'local',
                                                    width: 250,
                                                    height: 36,
                                                    style: {
                                                        'text-indent': '5px'
                                                    },
                                                    bodyStyle: {
                                                        'text-indent': '5px'
                                                    },
                                                
                                                    tpl: Ext.create('Ext.XTemplate',
                                                        '<ul class="x-list-plain"><tpl for=".">',
                                                            '<li role="option" class="x-boundlist-item">{measurementName}</li>',
                                                        '</tpl></ul>'
                                                    ),
                                                    listeners: {
                                                    
                                                        change: function (a, b, c, d) {
                                                            console.log('Mask combobox change - a: ', a);
                                                            if (a.disabled == false) {
                                                                AGS3xIoTAdmin.util.events.fireEvent('templateSelected', a);
                                                            }
                                                        }
                                                    }

                                                }
                                            ]
                                        });

                                    }
                                }
                            },
                            {
                                xtype: 'panel',
                                hidden: true,
                                height: 36,
                                html: '<div class="data-grid-arrow-down-spacer"></div>'
                            },


                            //////////////////////////////////////////////
                            // OBJECT OR COMPONENT                      //
                            //////////////////////////////////////////////
                            {
                                xtype: 'panel',
                                title: self.fieldLabelObjectCompTitle,
                                maskText: self.textMaskObjectsAndComponents,
                                id: 'panelObjectComponent',
                                height: 93,
                                listeners: {
                                    afterrender: function (component) {
                                        var maskSelf = this;
                                        var myMask = new Ext.LoadMask(
                                            {
                                                msg: maskSelf.maskText,
                                                id: 'maskObjectsAndComponents',
                                                target: component,
                                                //cls: 'io-data-mask',
                                                style: {
                                                    /*'opacity': '0.5',*/
                                                    'width': '100%',
                                                    'height': '100%'
                                                }
                                            }
                                        );
                                        myMask.show();
                                    }
                                },
                                items: [
                                    {
                                        xtype: 'gridpanel',
                                        scrollable: true,
                                        multiSelect: false,
                                        allowDeselect: true,
                                        disableSelection: true,
                                        frame: true,
                                        margin: '0 0 10 0',
                                        /*plugins: [
                                            Ext.create('Ext.grid.plugin.RowEditing', {
                                                clicksToEdit: 1
                                            })
                                        ],*/
                                        viewConfig: {
                                            getRowClass: function (record) {
                                                return 'component-object-row-class'
                                            }
                                        },
                                        bind: {
                                            store: '{componentOrObjectStore}',
                                            columns: '{componentOrObjectColumns}'
                                        },
                                        listeners: {
                                            itemclick: function (component, record, element, index) {
                                                console.log('dataIO - component or object item click, component: ', component);
                                                console.log('dataIO - component or object item click, record: ', record);
                                                console.log('dataIO - component or object item click, element: ', element);
                                                console.log('dataIO - component or object item click, index: ', index);

                                                var clickTime = new Date().getTime();
                                                console.log('dataIO - clickTime: ', clickTime);

                                                // FIX - DOUBLE CLICK
                                                if(!self.doubleClickIntervalStarted) {
                                                    self.doubleClickIntervalStarted = clickTime
                                                }
                                                else {
                                                    if (clickTime < (self.doubleClickIntervalStarted + 300) ) {
                                                        console.log('dataIO - DOUBLE CLICK!');
                                                        AGS3xIoTAdmin.util.events.fireEvent('activateMapLocalization', record);
                                                    }
                                                    self.doubleClickIntervalStarted = clickTime
                                                }

                                            }
                                        }
                                    }
                                ]

                            },
                            {
                                //////////////////////////////////////////////
                                // BUTTONS                                  //
                                //////////////////////////////////////////////

                                xtype: 'panel',
                                height: 36,
                                margin: '10px 0 0 0',

                                items: [
                                    {
                                        xtype: 'button',
                                        id: 'dataIOSaveButton',
                                        disabled: true,
                                        text: self.fieldLabelButtonSave,
                                        iconCls: 'fa fa-floppy-o',
                                        style: {
                                            'float': 'right',
                                            'margin-right': '10px'
                                        },
                                        listeners: {
                                            click: 'performEditorSave'
                                        }
                                    },
                                    {
                                        xtype: 'button',
                                        id: 'dataIOClearButton',
                                        disabled: false,
                                        text: self.fieldLabelButtonClear,
                                        iconCls: 'fa fa-eraser',
                                        style: {
                                            'float': 'right',
                                            'margin-right': '10px'
                                        },
                                        listeners: {
                                            click: 'resetConfigurationEditor'
                                        }
                                    },
                                    {
                                        xtype: 'button',
                                        id: 'dataIOSpecialButton',
                                        hidden: true,
                                        disabled: false,
                                        text: 'Special action',
                                        iconCls: 'fa fa-bolt',
                                        style: {
                                            'float': 'right',
                                            'margin-right': '10px'
                                        },
                                        listeners: {
                                            click: 'performSpecialAction'
                                        }
                                    }
                                ]
                            }
                        ]
                    },

                    //////////////////////////////////////////////
                    // MAP                                      //
                    //////////////////////////////////////////////
                    {
                        xtype: 'panel',
                        region: 'center',
                        title: self.fieldLabelMapHeaderTitle,
                        style: {
                            'box-shadow': '0 1px 2px 0 rgba(0, 0, 0, 0.2)',
                            'border-radius': '0px'
                        },
                        margin: '20px 20px 20px 0',
                        collapsible: true,
                        collapsed: false,
                        collapseDirection: 'left',
                        frame: true,
                        resizable: false,
                        width: '35%',
                        //minWidth: '50%',
                        layout: 'border',
                        items: [
                            {
                                xtype: 'toolbar',
                                region: 'north',
                                items: [
                                    {
                                        xtype: 'button',
                                        id: 'buttonToolbarCancelComponentMove',
                                        text: self.fieldLabelButtonCancelComponentMove,
                                        cls: 'button-component-movement-cancel',
                                        iconCls: 'fa fa-close',
                                        hidden: true,
                                        style: {
                                            'background': '#3892d4',
                                            'border': '1px solid #157fcc'
                                            
                                        },
                                        bodyStyle: {
                                            'color': 'white'
                                        },
                                        handler: 'cancelComponentMove'
                                    },
                                    '->',
                                    {
                                        xtype: 'ags3xMapSearch'
                                    }
                                ]
                            },
                            {
                                xtype: 'ags3xMap',
                                id: 'mapId',
                                region: 'center',
                                layout: 'fit'
                            },
                            {
                                xtype: 'tabpanel',
                                id: 'mapTabPanel',
                                region: 'east',
                                resizable: true,
                                collapsible: true,
                                collapsed: true,
                                collapseDirection: 'left',
                                title: 'Søgning',
                                width: 200,
                                bind: {
                                    activeTab: '{activeTab}'
                                },
                                items: [
                                    {
                                        xtype: 'ags3xTOC'
                                    },
                                    {
                                        xtype: 'ags3xResultList'
                                    }
                                ]
                            }
                        ],
                        listeners: {
                            collapse: function(component) {
                                console.log('dataIO - map collapsed');

                                console.log('dataIO - map collapsed, component:', component);
                                console.log('dataIO - map collapsed, is collapsed:', component.getCollapsed());

                                var dataIOComponent = Ext.ComponentQuery.query('#leftPanelContent')[0];
                                self.oldDataIOComponentWidth = dataIOComponent.getWidth();
                                console.log('dataIO - map collapsed, oldDataIOComponentWidth:', self.oldDataIOComponentWidth);
                                dataIOComponent.setWidth(window.innerWidth - 300 - 40 - 36);
                            },
                            expand: function (component) {
                                console.log('dataIO - map expand');

                                console.log('dataIO - map expand, component:', component);
                                console.log('dataIO - map expand, is collapsed:', component.getCollapsed());

                                var dataIOComponent = Ext.ComponentQuery.query('#leftPanelContent')[0];
                                console.log('dataIO - map expand, oldDataIOComponentWidth:', self.oldDataIOComponentWidth);
                                dataIOComponent.setWidth(self.oldDataIOComponentWidth);
                            },
                            resize: function (component) {
                                console.log('dataIO - map resized');
                            }
                        }
                    }
                ]
            }
        ]

        self.callParent(arguments);

    } // initComponent

});