Ext.define('AGS3xIoTAdmin.model.objectTypeModel', {
    extend: 'Ext.data.Model',
    alias: 'AGS3xIoTAdmin.model.objectTypeModel',
    fields: [
        { name: 'id', type: 'string' },
        { name: 'name', type: 'string' },
        { name: 'datastoreId', type: 'string' },
        { name: 'type', type: 'int' },
        { name: 'editDate', type: 'string' },
        { name: 'dataStore', type: 'string' },
        { name: 'wfs', type: 'string' },
        { name: 'wfsLayer', type: 'string' },
        { name: 'fieldId', type: 'string' },
        { name: 'fieldName', type: 'string' },
        { name: 'fieldDescription', type: 'string' },
        { name: 'objectTableName', type: 'string' },
        { name: 'zOrder', type: 'int' },
        { name: 'keyDesciption', type: 'string' }
    ]
});