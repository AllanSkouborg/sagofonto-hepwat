Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.resultList.resultListModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ags3xResultList',

    requires: [
        'AGS3xIoTAdmin.store.artogis.ags3x.resultList.resultListDataSore'
    ],

    stores: {
        resultListDataStore: {
            type: 'resultListDataSore'
        }
    }
});