Ext.define('AGS3xIoTAdmin.state.dashboardsStore', {
    extend: 'Ext.data.Store',
    singleton: true,
    autoLoad: false,
    storeId: 'AGS3xIoTAdmin.state.dashboardsStore',

    data: [],

    proxy: null

})