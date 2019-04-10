Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.componentEditor.componentEditorModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ags3xComponentEditor',

    stores: {
        componentTypesDataStore: {
            fields: [
                'id',
                'name',
                'type',
                'datastoreId',
                'wfs',
                'wfsLayer',
                'fieldDescription',
                'fieldId',
                'editDate'
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
            componentWfs: null,
            data: []
        }
    }
});