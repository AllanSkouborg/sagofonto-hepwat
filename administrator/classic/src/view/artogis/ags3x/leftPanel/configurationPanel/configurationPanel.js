Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.leftPanel.configurationPanel.configurationPanel', {
    extend: 'Ext.tree.Panel',
    xtype: 'ags3xConfigurationTree',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.leftPanel.configurationPanel.configurationPanelController'
    ],

    useArrows: true,
    rootVisible: false,
    multiSelect: false,
    singleExpand: false,
    hideHeaders: true,
    lines: false,

    controller: 'ags3xConfigurationPanel',

    titleLabel: 'Configuration',

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
                            text: 'Data IO',
                            leaf: true,
                            id: 'configuration-data-io'
                        },
                        {
                            text: 'Object Types',
                            leaf: true,
                            id: 'configuration-object-types'
                        },
                        {
                            text: 'Component Types',
                            leaf: true,
                            id: 'configuration-component-types'
                        }
                    ]
                }
            });

            /*c.getEl().on('click', function(e) {
                console.log('CHECK - e: ', e);
            });*/

            c.setStore(store);

        },
        itemclick: function (node, rec) {
            console.log('config tree option click - node', node);
            console.log('config tree option click - rec', rec);

            var selectedGridItems = Ext.select('.x-grid-item-selected').elements;
            console.log('Selected grid items: ', selectedGridItems);

            if (rec.id === 'configuration-data-io' || rec.id === 'configuration-object-types' || rec.id === 'configuration-component-types') {
                console.log('Fire event: showContentPanel');
                AGS3xIoTAdmin.util.events.fireEvent('showContentPanel', null);
            }

        }
    }
});