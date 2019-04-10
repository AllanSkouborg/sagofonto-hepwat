Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dataSensors.dataSensorsModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ags3xDataSensors',

    stores: {
        dataSensorsDataStore: {
            storeId: 'dataSensorsDataStore',
            fields: [
                'status',
                'nodeType',
                'interval',
                'lastRun',
                'dataSourceId',
                'dataId',
                'sensorId',
                'description',
                'unit',
                'location',
                'lastValue'
            ]
        }
    }
});