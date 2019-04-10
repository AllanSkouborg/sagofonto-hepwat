Ext.define('AGS3xIoTAdmin.state.robotsStore', {
    extend: 'Ext.data.Store',
    singleton: true,

    autoLoad: true,
    storeId: 'AGS3xIoTAdmin.state.robotsStore',

    data: [],

    proxy: null
})