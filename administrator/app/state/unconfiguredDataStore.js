Ext.define('AGS3xIoTAdmin.state.unconfiguredDataStore', {
    extend: 'Ext.data.Store',
    singleton: true,

    autoLoad: true,
    storeId: 'AGS3xIoTAdmin.state.unconfiguredDataStore',

    data: [],

    proxy: null
})