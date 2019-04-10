Ext.define('AGS3xIoTAdmin.model.configuredDataModel', {
    extend: 'Ext.data.Model',
    alias: 'AGS3xIoTAdmin.model.configuredDataModel',
    fields: [
        { name: 'id', type: 'int' },
        { name: 'sensorObjectName', type: 'string' },
        { name: 'sensorObjectAlias', type: 'string' },
        { name: 'unit', type: 'string' },
        { name: 'measurementType', type: 'int' },
        { name: 'templateType', type: 'int' },
        { name: 'sensorObjectId', type: 'string' },
        { name: 'description', type: 'string' },
        { name: 'sensorObjectDescription', type: 'string' },
        { name: 'dataSourceName', type: 'string' },
        { name: 'dataSourceId', type: 'int' },
        { name: 'sensorObjectNodeId', type: 'string' },
        { name: 'isBatteryStatus', type: 'boolean' }
    ]
});