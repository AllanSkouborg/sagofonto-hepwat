Ext.define('AGS3xIoTAdmin.model.unitModel', {
    extend: 'Ext.data.Model',
    alias: 'AGS3xIoTAdmin.model.unitModel',
    fields: [
        { name: 'id', type: 'int' },
        { name: 'name', type: 'string' },
        { name: 'description', type: 'string' },
        { name: 'language', type: 'string' }
    ]
});