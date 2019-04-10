var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.toc.toc', {
    extend: 'Ext.tree.Panel',
    xtype: 'ags3xTOC',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.toc.tocController',
        'AGS3xIoTAdmin.view.artogis.ags3x.toc.tocModel'
    ],

    viewModel: {
        type: 'ags3xTOC'
    },

    controller: 'ags3xTOC',

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelMapLayers: 'Lag',
    fieldLabelMapInfoWindowOf: 'af',
    fieldLabelMapInfoWindowType: 'Type',
    fieldLabelMapInfoWindowName: 'Navn',
    fieldLabelMapInfoWindowId: 'ID',
    fieldLabelMapInfoWindowButtonClose: 'Luk',
    fieldLabelMapInfoWindowButtonAccept: 'Acceptér',

    id: 'tocLayers',
    title: this.fieldLabelMapLayers,
    displayField: 'name',
    iconCls: 'fa fa-object-ungroup',
    rootVisible: false,
    hideHeaders: true,
    lines: false,
    bind: {
        store: '{layerTree}'
    },
    listeners: {
        afterrender: function() {
            console.log('toc afterrender -  this.fieldLabelMapLayers: ', this.fieldLabelMapLayers);
            this.setTitle(this.fieldLabelMapLayers);
        },
        beforecheckchange: 'onBeforeCheckChange'
    }
});