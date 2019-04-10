Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.objectEditor.objectEditorModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ags3xObjectEditor',

    stores: {
        objectTypesDataStore: {
            fields: [
                'type',
                'id',
                'name',
                'dataStoreId',
                'editDate',
                'wfs',
                'wfsLayer',
                'fieldName',
                'fieldDescription',
                'fieldId',
                'objectTableName',
                'zOrder'
            ]
        },

        dataStoresDataStore: {
            fields: [
                'id',
                'name'
            ]
        },

        fieldMappingStore: {
            fields: ['name'],
            objectWfs: null,
            data: []
        }
    }
});