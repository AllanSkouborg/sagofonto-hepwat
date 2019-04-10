Ext.define('AGS3xIoTAdmin.model.measurementTemplateModel', {
    extend: 'Ext.data.Model',
    alias: 'AGS3xIoTAdmin.model.measurementTemplateModel',
    fields: [
        { name: 'measurementGuid', type: 'string' },
        { name: 'templateType', type: 'int' },
        { name: 'measurementAlias', type: 'string' },
        { name: 'measurementName', type: 'string' },
        { name: 'measurementType', type: 'int' }
    ]
});