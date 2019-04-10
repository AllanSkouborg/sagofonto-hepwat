var self;

var dashboardHeight = 530;
var dashboardWidth = 295;
var dasboardMargin = '20px 19px 0 1px';
var columnMargin = 20;

var windowWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;

var leftPanelWidth;

//var primaryColumnWidth = (window.innerWidth - dashboardWidth - columnMargin - 5) / (window.innerWidth - document.getElementById('ags3xLeftPanel').clientWidth);
// 0.9875930521091811
var primaryColumnWidth = 0.9876;

var panelStyle = {
    //'box-shadow': '0 2px 4px 0 rgba(0,0,0,0.10) !important',
    'box-shadow': '0 1px 2px 0 rgba(0, 0, 0, 0.2)',
    'float': 'left',
    'border': 'none !important'
}
var buttonHeight = 40;
var buttonWidth = 40;
var buttonTop = 9;
var buttonLeft = 245;
var buttonStyle = 'background:#0094CE;cursor: pointer;border:none;z-index:5000;';
var buttonIconSet = 'x-fa fa-television';


Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dataRobots.dataRobots', {

    extend: 'Ext.panel.Panel',
    xtype: 'ags3xDataRobots',
    id: 'ags3xDataRobots',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.dataRobots.dataRobotsController',
        'AGS3xIoTAdmin.view.artogis.ags3x.dataRobots.dataRobotsModel'
    ],

    viewModel: {
        type: 'ags3xDataRobots'
    },

    controller: 'ags3xDataRobots',

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelRobotsLeftPanelHeaderTitle: 'DataRobot-monitor',
    fieldLabelRobotsButtonFullscreen: 'Fuldskærmsvisning',
    fieldLabelRobotsButtonClearRobots: 'Sluk robotmonitor',
    fieldLabelRobotsButtonReloadRobots: 'Tænd robotmonitor',
    fieldLabelRobotsRightPanelHeaderTitle: 'Robotdata',
    fieldLabelRobotsButtonDevices: 'Målere',
    fieldLabelRobotsButtonSensors: 'Sensorer',
    fieldLabelRobotsDevicesGridTitle: 'Målere',
    fieldLabelRobotsDevicesDeviceId: 'Måler-ID',
    fieldLabelRobotsDevicesName: 'Navn',
    fieldLabelRobotsAliasName: 'Navn/alias',
    fieldLabelRobotsDevicesDescription: 'Beskrivelse',
    fieldLabelRobotsDevicesLocation: 'Lokation',
    fieldLabelRobotsDevicesRobotId: 'Robot ID',
    fieldLabelRobotsSensorsGridTitle: 'Sensorer',
    fieldLabelRobotsSensorsType: 'Sensortype',
    fieldLabelRobotsSensorsInterval: 'Interval',
    fieldLabelRobotsSensorsLastRun: 'Seneste kørsel',
    fieldLabelRobotsSensorsDataId: 'Sensor-ID',
    fieldLabelRobotsSensorsSensorId: 'Måler-ID',
    fieldLabelRobotsSensorsName: 'Sensornavn',
    fieldLabelRobotsSensorsAliasName: 'Alias',
    fieldLabelRobotsSensorsDescription: 'Beskrivelse',
    fieldLabelRobotsSensorsUnit: 'Enhed',
    fieldLabelRobotsSensorsLocation: 'Lokation',
    fieldLabelRobotsSensorsLastValue: 'Seneste værdi',

    fieldLabelRecordButtonSave: 'Gem',
    fieldLabelRecordButtonCancel: 'Fortryd',


    id: 'ags3xDataRobots',
    cls: 'contentPanelBox',

    initComponent: function() {
        self = this;
        
        self.layout = 'border';
        self.style = {
            'background': '#f6f6f6 !important'
        };
        self.bodyStyle = {
            'overflow-y': 'auto !important',
            'background': '#f6f6f6 !important'
        };

        self.height = '100%';      

        self.items = [
            {
                xtype: 'panel',
                id: 'robotColumnLeft',
                region: 'center',
                height: '100%',
                margin: '20px 20px 20px 20px',
                style: {
                    'float': 'left',
                    'background': '#f6f6f6 !important'
                },
                bodyStyle: {
                    'background': '#f6f6f6 !important'
                },
                scrollable: 'y',
                header: {
                    //title: self.fieldLabelRobotsLeftPanelHeaderTitle,
                    title: self.fieldLabelRobotsLeftPanelHeaderTitle,
                    height: 44,
                    //width: (window.innerWidth - 300 - 40 - 4),
                    margin: '0 0 2px 2px',
                    items: [
                        {
                            xtype: 'button',
                            id: 'buttonActivateFullscreen',
                            text: self.fieldLabelRobotsButtonFullscreen,
                            iconCls: 'x-fa fa-arrows-alt',
                            style: {
                                'margin-left': '10px'
                            },
                            listeners: {
                                click: 'toggleFullscreen'
                            }
                        }
                    ],
                    listeners: {
                        resize: function () {
                            //console.log('Resizing fullSensorListColumnLeft_header...');
                            //document.getElementById('fullSensorListColumnLeft_header').style.width = (window.innerWidth - 300 - 40 - 4) + 'px';
                        }
                    }
                },
                listeners: {
                    afterrender: function () {
                        var leftColumn = Ext.ComponentQuery.query('#robotColumnLeft')[0];
                    }
                },
                items: [
                    {
                        xtype: 'panel',
                        id: 'robotDashboards',
                        height: 0, // TBD - responsive solution
                        //width: '100%',
                        style: {
                            'background': '#f6f6f6 !important'
                        },
                        bodyStyle: {
                            'background': '#f6f6f6 !important'
                        },
                        items: [

                        ]
                    }
                ]
            },
            {
                xtype: 'panel',
                id: 'robotColumnRight',
                height: 'auto',
                region: 'east',
                //columnWidth: 0.0,
                width: 'auto',
                margin: '20px 20px 20px 0',
                style: {
                    'box-shadow': '0 1px 2px 0 rgba(0, 0, 0, 0.2) !important'
                },
                header: {
                    titlePosition: 0,
                    title: self.fieldLabelRobotsRightPanelHeaderTitle,
                    items: [
                        {
                            xtype: 'fieldcontainer',
                            defaultType: 'checkboxfield',
                            items: [
                                {
                                    boxLabel  : 'Kør dataopdatering', // needs translation
                                    name: 'dataupdate',
                                    //cls: 'dashboard-url-parameter',
                                    inputValue: '1',
                                    id: 'checkboxUpdateData',
                                    handler: function (component) {
                                        console.log('checkboxUpdateData changed');
                                        AGS3xIoTAdmin.util.events.fireEvent('checkboxUpdateDataChanged', component);
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'button',
                            id: 'buttonShowDevicesTable',
                            text: self.fieldLabelRobotsButtonDevices,
                            iconCls: 'x-fa fa-cube',
                            margin: '0 0 0 10px',
                            enableToggle: true,
                            pressed: true,
                            handler: function () {
                                console.log('Show "Målere"');
                                Ext.ComponentQuery.query('#robotDevicesDataTable')[0].show();
                                Ext.ComponentQuery.query('#robotSensorDataTable')[0].hide();

                                Ext.ComponentQuery.query('#buttonShowSensorsTable')[0].setPressed(false);
                                Ext.ComponentQuery.query('#buttonShowDevicesTable')[0].setPressed(true);
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'buttonShowSensorsTable',
                            text: self.fieldLabelRobotsButtonSensors,
                            iconCls: 'x-fa fa-bolt',
                            margin: '0 0 0 10px',
                            enableToggle: true,
                            handler: function (button) {
                                console.log('Show "Sensorer" - button: ', button);
                                Ext.ComponentQuery.query('#robotDevicesDataTable')[0].hide();
                                Ext.ComponentQuery.query('#robotSensorDataTable')[0].show();

                                Ext.ComponentQuery.query('#buttonShowSensorsTable')[0].setPressed(true);
                                Ext.ComponentQuery.query('#buttonShowDevicesTable')[0].setPressed(false);
                            }
                        }
                    ]
                },
                listeners: {
                    beforerender: function () {
                        Ext.ComponentQuery.query('#robotColumnRight')[0].hide();
                    }
                },
                items: [
                    {
                        xtype: 'gridpanel',
                        id: 'robotDevicesDataTable',
                        width: '100%',
                        height: window.innerHeight - 70 - 44 - 40,
                        title: self.fieldLabelRobotsDevicesGridTitle,
                        store: Ext.data.StoreManager.lookup('robotDevicesDataStore'),
                        margin: '0 0 0 0',
                        scrollable: true,
                        plugins: [
                            'gridfilters',
                            {
                                ptype: 'rowediting',
                                clicksToEdit: 2,
                                pluginId: 'roweditingId',

                                saveBtnText: self.fieldLabelRecordButtonSave,
                                cancelBtnText: self.fieldLabelRecordButtonCancel,

                                listeners: {
                                    edit: function (editor, context, eOpts) {

                                        console.log('dataRobots - device editor, edit - editor: ', editor);
                                        console.log('dataRobots - device editor, edit - context: ', context);
                                        console.log('dataRobots - device editor, edit - eOpts: ', eOpts);

                                        AGS3xIoTAdmin.util.events.fireEvent('deviceAliasChanged', editor, context, eOpts);

                                    },
                                    canceledit: function (editor, context, eOpts) {
                                        console.log('dataRobots - device editor, canceledit');
                                    }
                                }
                            }
                        ],
                        columns: [
                            { text: self.fieldLabelRobotsDevicesDeviceId, dataIndex: 'id', flex: 1, filter: { type: 'string' } },
                            //{ text: self.fieldLabelRobotsDevicesName, dataIndex: 'name', flex: 1, filter: { type: 'string' } },
                            {
                                text: self.fieldLabelRobotsAliasName,
                                dataIndex: 'nameAlias',
                                flex: 1,
                                filter: {
                                    type: 'string'
                                },
                                editor: {
                                    xtype: 'textfield'
                                }
                            },
                            { text: self.fieldLabelRobotsDevicesDescription, dataIndex: 'description', flex: 1, filter: { type: 'string' } },
                            { text: self.fieldLabelRobotsDevicesLocation, dataIndex: 'location', flex: 1 }
                            //{ text: self.fieldLabelRobotsDevicesRobotId, dataIndex: 'dataSourceId', filter: { type: 'string' } }
                            //{ text: 'Geometry', dataIndex: 'ogrgeometry' }
                        ],
                        listeners: {
                            itemdblclick: function (component) {
                                console.log('dataRobots - robotDevicesDataTable itemdblclick: ', component);
                            }
                        }
                    },
                    {
                        xtype: 'gridpanel',
                        id: 'robotSensorDataTable',
                        width: '100%',
                        height: window.innerHeight - 70 - 44 - 40,
                        title: self.fieldLabelRobotsSensorsGridTitle,
                        store: Ext.data.StoreManager.lookup('robotSensorsDataStore'),
                        margin: '0 0 0 0',
                        scrollable: true,
                        hidden: true,
                        plugins: [
                            'gridfilters'
                        ],
                        /*plugins: [
                            'gridfilters',
                            {
                                ptype: 'rowediting',
                                clicksToEdit: 2,
                                pluginId: 'roweditingId',

                                saveBtnText: self.fieldLabelRecordButtonSave,
                                cancelBtnText: self.fieldLabelRecordButtonCancel,

                                listeners: {
                                    edit: function (editor, context, eOpts) {

                                        console.log('dataRobots - sensor editor, edit - editor: ', editor);
                                        console.log('dataRobots - sensor editor, edit - context: ', context);
                                        console.log('dataRobots - sensor editor, edit - eOpts: ', eOpts);

                                        self.controller.updateSensorAlias(editor, context, eOpts);

                                    },
                                    canceledit: function (editor, context, eOpts) {
                                        console.log('dataRobots - sensor editor, canceledit');
                                    }
                                }
                            }
                        ],*/
                        viewConfig: {
                            stripeRows: false,
                            getRowClass: function (record) {

                                switch (record.get('status').toLowerCase()) {
                                    case 'operating':
                                        return 'sensor-status sensor-operating';
                                    case 'stopped':
                                        return 'sensor-status sensor-stopped';
                                    case 'unknown':
                                        return 'sensor-status sensor-unknown';
                                    case 'warning':
                                        return 'sensor-status sensor-warning';
                                    default:
                                        return '  x-grid-row';
                                }
                            }
                        },
                        columns: [
                            {
                                text: '',
                                dataIndex: 'status',
                                width: 60,
                                cls: 'header-sensor-status',
                                html: '<div style="height: 28px;width: 60px;position: absolute;top: 0px;left: 0px;" title="Sensorstatus"></div>',
                                renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                                    switch (value.toLowerCase()) {
                                        case 'operating':
                                            return '<div class="fa fa-check sensor-status-content" data-statusvalue="' + value + '" title="' + record.data.statusTooltipText + '"></div>';
                                        case 'stopped':
                                            return '<div class="fa fa-close sensor-status-content" data-statusvalue="' + value + '" title="' + record.data.statusTooltipText + '"></div>';
                                        case 'unknown':
                                            return '<div class="fa fa-question sensor-status-content" data-statusvalue="' + value + '" title="' + record.data.statusTooltipText + '"></div>';
                                        case 'warning':
                                            return '<div class="fa fa-warning sensor-status-content" data-statusvalue="' + value + '" title="' + record.data.statusTooltipText + '"></div>';
                                        default:
                                            return '';
                                    }
                                }
                            },
                            { text: self.fieldLabelRobotsSensorsSensorId, dataIndex: 'sensorId', flex: 1, filter: { type: 'string' } },
                            { text: self.fieldLabelRobotsSensorsDataId, dataIndex: 'dataId', filter: { type: 'string' } },
                            //{ text: self.fieldLabelRobotsSensorsDescription, dataIndex: 'description', flex: 1, filter: { type: 'string' } },
                            //{ text: self.fieldLabelRobotsSensorsName, dataIndex: 'name', flex: 1, filter: { type: 'string' } },
                            //{ text: self.fieldLabelRobotsSensorsAliasName, dataIndex: 'nameAlias', flex: 1, filter: { type: 'string' },  editor: { xtype: 'textfield' } },
                            { text: self.fieldLabelRobotsSensorsType, dataIndex: 'nodeTypeString', width: 150 , filter: { type: 'string' } },
                            { text: self.fieldLabelRobotsSensorsUnit, dataIndex: 'unit', width: 60, filter: { type: 'string' } },
                            //{ text: self.fieldLabelRobotsSensorsLocation, dataIndex: 'location' },
                            { text: self.fieldLabelRobotsSensorsInterval, dataIndex: 'interval' },
                            { text: self.fieldLabelRobotsSensorsLastRun, dataIndex: 'lastRun', width: 150 },
                            { text: self.fieldLabelRobotsSensorsLastValue, dataIndex: 'lastValue', width: 150 }
                        ]
                    }
                ]
            }
        ]

        self.callParent(arguments);

        //AGS3xIoTAdmin.view.artogis.ags3x.dataRobots.dataRobotsController.init;

    } // initComponent    

});


