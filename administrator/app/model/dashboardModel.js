Ext.define('AGS3xIoTAdmin.model.dashboardModel', {
    extend: 'Ext.data.Model',
    alias: 'AGS3xIoTAdmin.model.dashboardModel',
    fields: [
        { name: 'id', type: 'int' },
        { name: 'dashboardId', type: 'int' },
        { name: 'title', type: 'string' },
        { name: 'cards', type: 'string' }
    ]
});