var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.resultList.resultList', {
    extend: 'Ext.tree.Panel',
    xtype: 'ags3xResultList',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.resultList.resultListController',
        'AGS3xIoTAdmin.view.artogis.ags3x.resultList.resultListModel'
    ],

    viewModel: {
        type: 'ags3xResultList'
    },

    controller: 'ags3xResultList',

    // text content - default is Danish (da), for translations, see locale folder
    fieldLabelMapSearch: 'Søgning',

    initComponent: function() {
        self = this;

        self.callParent(arguments);

        self.title = self.fieldLabelMapSearch;    
    }, // initComponent
    displayField: 'text',
    iconCls: 'fa fa-search',
    rootVisible: false,
    hideHeaders: true,
    lines: false,

    listeners: {
        itemclick: 'onResultListItemClick',
        cellcontextmenu: 'onResultListContextMenu'
    },
    bind: {
        store: '{resultListDataStore}'
    }
});