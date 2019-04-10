Ext.define('AGS3xIoTAdmin.model.robotModel', {
    extend: 'Ext.data.Model',
    alias: 'AGS3xIoTAdmin.model.robotModel',
    fields: [
        { name: 'id', type: 'int' },
        { name: 'name', type: 'string' },
        { name: 'url', type: 'string' },
        { name: 'authenticationType', type: 'string' },
        { name: 'description', type: 'string' },
        { name: 'dashboardUrl', type: 'string' },
        { name: 'robotStarted', type: 'boolean' },
        { name: 'notificationOn', type: 'boolean' },
        { name: 'availability', type: 'int' }
    ]
});