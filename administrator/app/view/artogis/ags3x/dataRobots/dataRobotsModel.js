Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dataRobots.dataRobotsModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ags3xDataRobots',

    stores: {
        robotSensorsDataStore: {
            storeId: 'robotSensorsDataStore',
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
                'status',
                'lastValue',
                'nameAlias'
            ]
        },
        robotDevicesDataStore: {
            storeId: 'robotDevicesDataStore',
            fields: [
                'id',
                'name',
                'description',
                'location',
                'ogrgeometry',
                'dataSourceId',
                'nameAlias'
            ]
        }
    }
});