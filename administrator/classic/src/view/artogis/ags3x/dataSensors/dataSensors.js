var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dataSensors.dataSensors', {

    extend: 'Ext.panel.Panel',
    xtype: 'ags3xDataSensors',
    id: 'ags3xDataSensors',

    requires: [
     'AGS3xIoTAdmin.view.artogis.ags3x.dataSensors.dataSensorsController',
     'AGS3xIoTAdmin.view.artogis.ags3x.dataSensors.dataSensorsModel'
    ],

    viewModel: {
        type: 'ags3xDataSensors'
    },

    controller: 'ags3xDataSensors',

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelSensorsListHeaderTitle: 'Komplet sensorliste',
    fieldLabelSensorsListGridTitle: 'Sensordata',
    fieldLabelSensorsListStatus: 'Sensorstatus',
    fieldLabelSensorsListType: 'Sensortype',
    fieldLabelSensorsListInterval: 'Interval',
    fieldLabelSensorsListLastRun: 'Seneste kørsel',
    fieldLabelSensorsListRobotId: 'DataRobot-ID',
    fieldLabelSensorsListDataId: 'Sensor-ID',
    fieldLabelSensorsListSensorId: 'Måler-ID',
    fieldLabelSensorsListName: 'Sensornavn',
    fieldLabelSensorsListDescription: 'Sensorbeskrivelse',
    fieldLabelSensorsListUnit: 'Enhed',
    fieldLabelSensorsListLocation: 'Lokation',
    fieldLabelSensorsListLastValue: 'Seneste værdi',
    fieldLabelButtonRefreshData: 'Genopfrisk data',
    fieldLabelButtonToTop: 'Gå til toppen',


    fieldLabelDataLoading: 'Vent venligst...',

    id: 'ags3xDataSensors',
    cls: 'contentPanelBox',
    layout: 'fit',
    style: {
        'background': '#f6f6f6'
    },
    bodyStyle: {
        'background': '#f6f6f6'
    },

    initComponent: function() {
        self = this;

        self.listeners = {
            afterrender: function () {
                var loadingMask = new Ext.LoadMask(
                    {
                        msg: self.fieldLabelDataLoading,
                        id: 'sensorsListFullMask',
                        target: self
                    }
                );

                loadingMask.show();
            }
        }

        self.items = [
        {
            xtype: 'panel',
            id: 'fullSensorListColumnLeft',
            height: '100%',
            margin: '20px 20px 0 20px',
            bodyStyle: {
                'background': '#f6f6f6'
            },
            //columnWidth: (window.innerWidth - 300 - 20) / (window.innerWidth - 300),
            listeners: {
                afterrender: 'fetchSensorsData'
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
                        component.setTitle(self.fieldLabelSensorsListHeaderTitle);

                        Ext.ComponentQuery.query('#fullSensorListDataTable')[0].setHeight(window.innerHeight - 180);
                    }
                }
            },
            items: [
                {
                    xtype: 'gridpanel',
                    id: 'fullSensorListDataTable',
                    header: {
                        title: this.fieldLabelSensorsListGridTitle,
                        tools: [
                            {
                                xtype: 'button',
                                id: 'buttonSensorsRefreshData',
                                text: null,
                                iconCls: 'fa fa-refresh',
                                style: {
                                    'float': 'right',
                                    'margin-left': '10px'
                                },
                                handler: 'refreshdata',
                                listeners: {
                                    afterrender: function (component) {
                                        component.setText(self.fieldLabelButtonRefreshData);
                                    }
                                }
                            },
                            {
                                xtype: 'button',
                                text: null,
                                iconCls: 'fa fa-long-arrow-up',
                                style: {
                                    'float': 'right',
                                    'margin-left': '10px'
                                },
                                handler: function () {
                                    console.log('Button - go to top');
                                    var scrollElement = document.getElementById('fullSensorListDataTable').getElementsByClassName('x-grid-view')[0].scrollTop = 0;
                                },
                                listeners: {
                                    afterrender: function (component) {
                                        component.setText(self.fieldLabelButtonToTop);
                                    }
                                }
                            }
                        ]
                    },
                    
                    store: Ext.data.StoreManager.lookup('dataSensorsDataStore'),
                    height: window.innerHeight - 180,
                    //width: (window.innerWidth - 300 - 40 - 4),
                    margin: '20px 0 2px 2px',
                    style: {
                        'box-shadow': '0 1px 2px rgba(0, 0, 0, 0.2) !important'
                    },
                    scrollable: true,
                    plugins: 'gridfilters',
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
                            id: 'colFullListStatus',
                            width: 60,
                            cls: 'header-sensor-status',
                            html: '<div style="height: 28px;width: 60px;position: absolute;top: 0px;left: 0px;" title="' + this.fieldLabelSensorsListStatus + '"></div>',
                            renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                                //var status = (value != null) ? value.toLowerCase() : 'unknown';
                                //console.log('value: ', value);
                                //console.log('metaData: ', metaData);
                                //console.log('record: ', record);
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
                        
                        { id: 'colFullListRobotId', text: 'D', dataIndex: 'dataSourceId', width: 120, filter: { type: 'string' } },
                        { id: 'colFullListSensorId', text: 'F', dataIndex: 'sensorId', width: 150, filter: { type: 'string' } },
                        { id: 'colFullListDataId', text: 'E', dataIndex: 'dataId', filter: { type: 'string' } },
                        { id: 'colFullListName', text: 'K', dataIndex: 'name', flex: 1, filter: { type: 'string' } },
                        { id: 'colFullListDescription', text: 'G', dataIndex: 'description', flex: 1, filter: { type: 'string' } },
                        //{ id: 'colFullListType', text: 'A', dataIndex: 'nodeType', filter: { type: 'string' } },
                        { id: 'colFullListType', text: 'A', dataIndex: 'nodeTypeString', filter: { type: 'string' } },
                        { id: 'colFullListUnit', text: 'H', dataIndex: 'unit', filter: { type: 'string' } },
                        //{ id: 'colFullListLocation', text: 'I', dataIndex: 'location' },
                        { id: 'colFullListInterval', text: 'B', dataIndex: 'interval' },
                        { id: 'colFullListLastRun', text: 'C', dataIndex: 'lastRun', width: 150 },
                        { id: 'colFullListLastValue', text: 'J', dataIndex: 'lastValue', width: 120 }

                    ],
                    listeners: {
                        afterrender: function () {
                            Ext.ComponentQuery.query('#fullSensorListDataTable')[0].setTitle(self.fieldLabelSensorsListGridTitle);

                            console.log('Column: ', Ext.ComponentQuery.query('#colFullListType')[0]);
                            Ext.ComponentQuery.query('#colFullListType')[0].setText(self.fieldLabelSensorsListType);
                            Ext.ComponentQuery.query('#colFullListInterval')[0].setText(self.fieldLabelSensorsListInterval);
                            Ext.ComponentQuery.query('#colFullListLastRun')[0].setText(self.fieldLabelSensorsListLastRun);
                            Ext.ComponentQuery.query('#colFullListRobotId')[0].setText(self.fieldLabelSensorsListRobotId);
                            Ext.ComponentQuery.query('#colFullListDataId')[0].setText(self.fieldLabelSensorsListDataId);
                            Ext.ComponentQuery.query('#colFullListSensorId')[0].setText(self.fieldLabelSensorsListSensorId);
                            Ext.ComponentQuery.query('#colFullListDescription')[0].setText(self.fieldLabelSensorsListDescription);
                            Ext.ComponentQuery.query('#colFullListUnit')[0].setText(self.fieldLabelSensorsListUnit);
                            //Ext.ComponentQuery.query('#colFullListLocation')[0].setText(self.fieldLabelSensorsListLocation);
                            Ext.ComponentQuery.query('#colFullListLastValue')[0].setText(self.fieldLabelSensorsListLastValue);

                            Ext.ComponentQuery.query('#colFullListName')[0].setText(self.fieldLabelSensorsListName);
                        }
                    }
                }
            ]
        }
        ]

        self.callParent(arguments);

    }
});