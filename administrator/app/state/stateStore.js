Ext.define('AGS3xIoTAdmin.state.stateStore', {
    extend: 'Ext.data.Store',
    singleton: true,

    autoLoad: true,
    storeId: 'AGS3xIoTAdmin.state.stateStore',

    data: [
        { name: 'Socrates', id: 1 },
        { name: 'Plato', id: 2 },
        { name: 'Aristotles', id: 3 }
    ]

    /*proxy: {
        type: 'ajax',
        url: 'data1.json'
    },

    sorters: ['text']*/
})