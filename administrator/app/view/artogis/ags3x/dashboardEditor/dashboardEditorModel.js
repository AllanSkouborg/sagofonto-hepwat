Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dashboardEditor.dashboardEditorModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ags3xDashboardEditor',

    stores: {
        unconfiguredSensorsDataStore: {
            fields: [
                'dataSourceId',
                'sensorId',
                'dataSourceName',
                'description',
                'sensorObjectDescription',
                'dataId',
                'unit'
            ],
            data: []
        },

        sensorsDataStore: {
            fields: [
                'dataSourceId',
                'sensorId',
                'dataSourceName',
                'description',
                'sensorObjectDescription',
                'dataId',
                'unit'
            ],
            data: []
        },

        batterySensorsDataStore: {
            fields: [
                'dataSourceId',
                'sensorId',
                'dataSourceName',
                'description',
                'sensorObjectDescription',
                'dataId',
                'unit'
            ],
            data: []
        },

        signalSensorsDataStore: {
            fields: [
                'dataSourceId',
                'sensorId',
                'dataSourceName',
                'description',
                'sensorObjectDescription',
                'dataId',
                'unit'
            ],
            data: []
        },

        cardsStore: {
            fields: [
                'title',
                'index',
                'sensorId',
                'batterySensorId',
                'cardType',
                'batteryLevel'
            ],
            data: [

            ]
        },
        dashboardsDataStore: {
            fields: [
                'title', 'dashboardId', 'cards'
            ],
            data: []
        },

        activeDashboardDataStore: {
            fields: [
                'title', 'dashboardId', 'cards'
            ],
            data: []
        }

    },

    data: { }
});