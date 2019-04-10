Ext.define('AGS3xIoTAdmin.state.devicesStore', {
    extend: 'Ext.data.Store',
    //model: 'AGS3xIoTAdmin.model.deviceModel',
    singleton: true,
    autoLoad: false,
    storeId: 'AGS3xIoTAdmin.state.devicesStore',

    data: [],

    proxy: null

})