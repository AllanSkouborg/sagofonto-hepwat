var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dashboardEditor.dashboardEditor', {

    extend: 'Ext.panel.Panel',
    xtype: 'ags3xDashboardEditor',
    id: 'ags3xDashboardEditor',

    requires: [
     'AGS3xIoTAdmin.view.artogis.ags3x.dashboardEditor.dashboardEditorController',
     'AGS3xIoTAdmin.view.artogis.ags3x.dashboardEditor.dashboardEditorModel'
    ],

    viewModel: {
        type: 'ags3xDashboardEditor'
    },

    controller: 'ags3xDashboardEditor',

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelDashboardEditorHeaderTitle: 'Dashboardkonfigurering',
    fieldLabelLoadDashboardPlaceholder: 'Vælg et dashboard...',
    fieldLabelDashboardName: 'Navn',
    fieldLabelFetchSensorData: 'Hent sensordata',
    fieldLabelShowUrl: 'Vis URL',
    fieldLabelDeleteDashboard: 'Slet',
    fieldLabelStartNewDashboard: 'Start ny',
    fieldLabelSaveDashboard: 'Gem',

    textWelcomeDashboardScreen: 'Vælg eksisterende dashboard fra dropdown\'en <br>eller start på en ny ved at trykke på knappen "Start ny"',


    id: 'ags3xDashboardEditor',
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

        self.items = [
            {
                xtype: 'panel',
                id: 'fullDashboardEditorPanel',
                height: '100%',
                margin: '20px 20px 0 20px',
                layout: 'border',
                bodyStyle: {
                    'background': '#f6f6f6'
                },
                header: {
                    title: self.fieldLabelDashboardEditorHeaderTitle,
                    height: 44,
                    width: (window.innerWidth - 300 - 40 - 4),
                    margin: '0 0 2px 2px',
                    tools: [
                        {
                            xtype: 'combobox',
                            id: 'comboboxLoadDashboard',
                            width: 200,
                            style: {
                                'float': 'left',
                                'margin-right': '30px'
                            },
                            //store: this.getViewModel().getStore('dashboardsDataStore'),
                            store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.dashboardsStore'),
                            valueField: 'dashboardId',
                            displayField: 'title',
                            editable: false,
                            queryMode: 'local',
                            emptyText: self.fieldLabelLoadDashboardPlaceholder,
                            tpl: Ext.create('Ext.XTemplate',
                                '<ul class="x-list-plain"><tpl for=".">',
                                    '<li role="option" class="x-boundlist-item">{title}</li>',
                                '</tpl></ul>'
                            ),
                            listeners: {
                                change: function (a, b, c, d) {
                                    console.log('Header combobox change - a: ', a);
                                    console.log('Header combobox - disabled: ', a.disabled);

                                    if (a.disabled == false) {
                                        AGS3xIoTAdmin.util.events.fireEvent('dashboardSelected', a.value);
                                    }
                                }
                            }
                        },
                        {                         
                            xtype: 'textfield',
                            id: 'textFieldDashboardName',
                            name: 'name',
                            fieldLabel: self.fieldLabelDashboardName,
                            width: 300,
                            labelWidth: 50,
                            //labelStyle: 'width:60px',
                            disabled: true,
                            allowBlank: false  // requires a non-empty value
                        },
                        {
                            xtype: 'button',
                            id: 'buttonLoadSensorData',
                            html: self.fieldLabelFetchSensorData,
                            disabled: true,
                            style: {
                                'margin-left': '10px'
                            },
                            listeners: {
                                click: function (component) {
                                    AGS3xIoTAdmin.util.events.fireEvent('buttonLoadSensorDataClicked');
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'buttonGetDashboardUrl',
                            html: self.fieldLabelShowUrl,
                            disabled: true,
                            style: {
                                'margin-left': '10px'
                            },
                            listeners: {
                                click: function (component) {
                                    AGS3xIoTAdmin.util.events.fireEvent('buttonShowUrlClicked', component);
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'buttonDeleteDashboard',
                            html: self.fieldLabelDeleteDashboard,
                            disabled: true,
                            style: {
                                'margin-left': '10px'
                            },
                            listeners: {
                                click: function (component) {
                                    AGS3xIoTAdmin.util.events.fireEvent('buttonDeleteDashboardClicked', component);
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'buttonCreateNewDashboard',
                            html: self.fieldLabelStartNewDashboard,
                            style: {
                                'margin-left': '10px'
                            },
                            listeners: {
                                click: function (component) {
                                    AGS3xIoTAdmin.util.events.fireEvent('buttonCreateNewDashboardClicked', component);
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'buttonSaveDashboard',
                            html: self.fieldLabelSaveDashboard,
                            disabled: true,
                            style: {
                                'margin-left': '10px'
                            },
                            listeners: {
                                click: function (component) {
                                    AGS3xIoTAdmin.util.events.fireEvent('buttonSaveDashboardClicked', component);
                                }
                            }
                        }
                    ]
                },
                items: [
                    {
                        xtype: 'panel',
                        id: 'dashboardEditorBoard',
                        //layout: 'table',
                        height: (window.innerHeight - 175 - 4),
                        width: (window.innerWidth - 300 - 40 - 4),
                        margin: '20px 0 0 2px',
                        style: {
                            'background': 'rgb(246,246,246)'
                        },
                        bodyStyle: {
                            'background': 'rgb(246,246,246)',
                            'overflow-x': 'hidden',
                            'overflow-y': 'auto'
                        },
                       
                        items: [
                            {
                                xtype: 'panel',
                                id: 'dashboardEditorStartText',
                                html: '<span>' + self.textWelcomeDashboardScreen + '</span>',
                                height: 500,
                                style: {
                                    'text-align': 'center',
                                    'line-height': '70px'
                                    
                                },
                                bodyStyle: {
                                    'font-size': '2.0em',
                                    'background': 'transparent',
                                    'color': '#969696',
                                    'margin-top': '100px'
                                }
                            }
                        ],
                        listeners: {

                        }
                    }
                ]
            }
        ]

        self.listeners = {
            afterrender: function () {
                console.log('formulaEditor.afterrender');

                if (Ext.ComponentQuery.query('#formulaEditorWindow').length > 0) {
                    Ext.ComponentQuery.query('#formulaEditorWindow')[0].setTitle(self.fieldLabelFormulaEditorWindowTitle);
                }
            }
        }

        self.callParent(arguments);
    }
});