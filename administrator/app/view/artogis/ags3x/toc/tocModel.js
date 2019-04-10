Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.toc.tocModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ags3xTOC',

    requires: [
        'AGS3xIoTAdmin.store.artogis.ags3x.toc.layerTree'
    ],

    stores: {
        layerTree: {
            type: 'layerTree'
        }
    }
});