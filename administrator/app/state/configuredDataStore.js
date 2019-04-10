Ext.define('AGS3xIoTAdmin.state.configuredDataStore', {
    extend: 'Ext.data.Store',
    singleton: true,
    autoLoad: false,
    storeId: 'AGS3xIoTAdmin.state.configuredDataStore',

    data: [],

    proxy: null

})