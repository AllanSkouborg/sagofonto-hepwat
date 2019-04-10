Ext.define('AGS3xIoTAdmin.state.measurementTemplatesStore', {
    extend: 'Ext.data.Store',
    singleton: true,
    autoLoad: false,
    storeId: 'AGS3xIoTAdmin.state.measurementTemplatesStore',

    data: [],

    proxy: null

})