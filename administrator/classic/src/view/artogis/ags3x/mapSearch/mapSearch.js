var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.mapSearch.mapSearch', {
    extend: 'Ext.container.Container',
    xtype: 'ags3xMapSearch',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.mapSearch.mapSearchController',
        'AGS3xIoTAdmin.view.artogis.ags3x.mapSearch.mapSearchModel'
    ],

    viewModel: {
        type: 'ags3xMapSearch'
    },

    controller: 'ags3xMapSearch',

    // text content - default is Danish (da), for translations, see locale folder
    fieldLabelMapSearch: 'Søg',

    /*
    width: 200,
    initComponent: function () {
        self = this;
        self.callParent(arguments);
    },
    items: [
        {
            xtype: 'textfield',
            emptyText: self.fieldLabelMapSearch,
            
            enableKeyEvents: true,
            width: '100%',
            listeners: {
                keyup: 'onKeyUp'
            },
            bind: {
                value: '{mapSearchValue}',
                disabled: '{searchFieldDisabled}'
            },
            afterrender: function (componenet) {
                component.setEmptyText(self.fieldLabelMapSearch);
            }
        }
    ]*/

    initComponent: function () {
        self = this;
        self.width = 200;
        self.items = [
            {
                xtype: 'textfield',
                emptyText: self.fieldLabelMapSearch,
            
                enableKeyEvents: true,
                width: '100%',
                listeners: {
                    keyup: 'onKeyUp'
                },
                bind: {
                    value: '{mapSearchValue}',
                    disabled: '{searchFieldDisabled}'
                }
            }
        ]

        self.callParent(arguments);
    }
});