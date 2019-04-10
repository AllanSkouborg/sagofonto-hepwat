Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dataIO.dataIOModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ags3xDataIOConfig',

    stores: {
        devicesStore: {
            fields: [
                'deviceName',
                'deviceId',
                'dataSourceId',
                'description',
                'location',
                'geometry'
            ],
            data: []
        },

        unconfiguredDataStore: {
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

        configuredDataStore: {
            fields: [
                'dataSourceId',
                'sensorObjectDescription',
                'unit',
                'name',
                'alias',
                'templateType',
                'measurementType',
                'nodeType',
                'id',
                'description',
                'dataSourceName',
                'sensorObjectId',
                'sensorObjectNodeId'
            ],
            data: []
        },

        measurementTemplateType: {
            fields: [
                'measurementType',
                'measurementGuid',
                'measurementName',
                'templateType'
            ],
            data: []
        },

        measurementTypesDataStore: {
            fields: [
                'id',
                'name',
                'isSignalStrength',
                'isBatteryStatus'
            ],
            data: []
        },

        calculationDataStore: {
            fields: [
                'calculationType',
                'formula',
                'aggregationAndStores',
                'dataIoId',
                'templateType'
            ],
            data: []
            
        },

        componentOrObjectStore: {
            fields: [
                'type',
                'temp_id',
                'name',
                'description'
            ],
            data: []
        },

        selectedObjectStore: {
            fields: [
                'type',
                'temp_id',
                'name',
                'description'
            ],
            data: []
        },

        componentOrObjectMultipleStore: {
            fields: [
                'type',
                'temp_id',
                'name'
            ],
            data: []
        },

        componentTypesStore: {
            fields: [
                'id',
                'name',
                'datastoreId',
                'type',
                'editDate',
                'dataStore',
                'wfs',
                'fieldId',
                'fieldDescription',
                'componentTableName'
            ],
            data: []
        },

        objectTypesStore: {
            fields: [
                'id',
                'name',
                'datastoreId',
                'type',
                'editDate',
                'dataStore',
                'wfs',
                'objectTableName'
            ],
            data: []
        },

        unitTypesStore: {
            fields: [
                'id',
                'name',
                'description'
            ],
            data: []
        },

        layerTree: {
            type: 'layerTree'
        },

        boolTypes: {
            fields: [
                'type',
                'name'
            ],
            data: [
                { 'type': true, 'name': 'Yes' },
                { 'type': false, 'name': 'No' }
            ]
        },

        dataIOVariables: {
            fields: ['name'],
            data: []
        },

        existingFormulas: {
            fields: [
                'id',
                'name',
                'string'
            ],
            data: [
                {
                    'id': 1,
                    'name': 'Overløb',
                    'string': 'C x B x h x Math.sqrt(2 x g x h)'
                }
            ]
        },

        templatesConfiguration: {
            fields: [
                'id',
                'templateType',
                'measurementAlias',
                'measurementName',
                'measurementType',
                'calculationAndStores'
            ],
            data: []
        }
    },

    data: {
        unconfiguredGridColumns: [],
        configuredGridColumns: [],
        calculationGridColumns: [],
        componentOrObjectColumns: [],

        unconfiguredGridSelection: null,
        selectedTemplateValue: null,
        connectionObjectValue: null,

        activeTab: 0,
        saveButtonDisabled: true
    }
});