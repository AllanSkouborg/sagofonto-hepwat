Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.leftPanel.dataRobotsPanel.dataRobotsPanel', {
    extend: 'Ext.tree.Panel',
    xtype: 'ags3xDataRobotsTree',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.leftPanel.dataRobotsPanel.dataRobotsPanelController'
    ],

    controller: 'ags3xDataRobotsPanel',

    useArrows: true,
    rootVisible: false,
    multiSelect: false,
    singleExpand: false,
    hideHeaders: true,
    lines: false,

    titleLabel: 'Datarobotter',

    initComponent: function () {
        this.title = this.titleLabel;
        this.callParent();
    },

    listeners: {
        afterrender: function (c) {
            var store = Ext.create('Ext.data.TreeStore', {
                root: {
                    expanded: true,
                    children: [
                        {
                            text: 'Datarobotter - monitor',
                            leaf: true,
                            id: 'dataRobotsMonitor'
                        },
                        {
                            text: 'Komplet sensorliste',
                            leaf: true,
                            id: 'dataFullSensorsList'
                        }
                    ]
                }
            });

            c.setStore(store);

            Ext.ComponentQuery.query('ags3xContentPanel')[0].setStyle('overflow-y', 'auto');
            //Ext.getCmp('ags3xContentPanel-1023').setStyle('overflow-y', 'auto');
        },
        itemclick: function (node, rec) {
            console.log('Robot tree option click - node', node);
            console.log('Robot tree option click - rec', rec);

            if (rec.id === 'dataRobotsMonitor') {
                console.log('Fire event: showDataRobotsPanel');
                AGS3xIoTAdmin.util.events.fireEvent('showDataRobotsPanel', null);
            }

            if (rec.id === 'dataFullSensorsList') {
                console.log('Fire event: showFullSensorsListPanel');
                AGS3xIoTAdmin.util.events.fireEvent('showFullSensorsListPanel', null);
            }
        }
    }


});
