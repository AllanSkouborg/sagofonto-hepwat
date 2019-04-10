Ext.define('AGS3xIoTAdmin.model.deviceModel', {
    extend: 'Ext.data.Model',
    alias: 'AGS3xIoTAdmin.model.deviceModel',
    fields: [
        {name: 'dataSourceId', type: 'int'},
        {name: 'deviceName',  type: 'string'},
        {name: 'deviceAlias',  type: 'string'},
        {name: 'deviceId',  type: 'string'},
        {name: 'description',  type: 'string'},
        {name: 'location',  type: 'string'},
        {name: 'geometry',  type: 'string'}
    ]
});
