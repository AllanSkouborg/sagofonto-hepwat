Ext.define('AGS3xIoTAdmin.model.unconfiguredDataModel', {
    extend: 'Ext.data.Model',
    alias: 'AGS3xIoTAdmin.model.unconfiguredDataModel',
    fields: [
        { name: 'dataSourceId', type: 'int' },
        { name: 'dataSourceName', type: 'string' },

        { name: 'sensorId', type: 'string' },
        { name: 'dataId', type: 'string' },
        { name: 'name', type: 'string' },
        { name: 'description', type: 'string' },
        { name: 'unit', type: 'string' },

        { name: 'sensorObjectName', type: 'string' },
        { name: 'sensorObjectAlias', type: 'string' },
        { name: 'sensorObjectNodeId', type: 'string' },
        { name: 'sensorObjectDescription', type: 'string' },

        { name: 'nodeType', type: 'int' },
        { name: 'nodeTypeString', type: 'string' },

        { name: 'configured', type: 'boolean' }

    ]
});