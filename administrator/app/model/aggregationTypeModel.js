Ext.define('AGS3xIoTAdmin.model.aggregationTypeModel', {
    extend: 'Ext.data.Model',
    alias: 'AGS3xIoTAdmin.model.aggregationTypeModel',
    fields: [
        { name: 'id', type: 'int' },
        { name: 'name', type: 'string' },
        { name: 'aggregationCalculationType', type: 'int' },
        { name: 'minutes', type: 'int' }
    ]
});
