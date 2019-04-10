Ext.define('AGS3xIoTAdmin.model.measurementTypeModel', {
    extend: 'Ext.data.Model',
    alias: 'AGS3xIoTAdmin.model.measurementTypeModel',
    fields: [
        { name: 'id', type: 'int'},
        { name: 'name', type: 'string'},
        { name: 'isSignalStrength', type: 'boolean'},
        { name: 'isBatteryStatus', type: 'boolean'},
        { name: 'language', type: 'string' }
    ]
});