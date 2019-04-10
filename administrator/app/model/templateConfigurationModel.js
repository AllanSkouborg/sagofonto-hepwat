Ext.define('AGS3xIoTAdmin.model.templateConfigurationModel', {
    extend: 'Ext.data.Model',
    alias: 'AGS3xIoTAdmin.model.templateConfigurationModel',
    fields: [
        { name: 'measurementGuid', type: 'string' },
        { name: 'templateType', type: 'int' },
        { name: 'measurementAlias', type: 'string' },
        { name: 'measurementName', type: 'string' },
        { name: 'measurementType', type: 'int' },
        'calculationAndStores'
    ]
});