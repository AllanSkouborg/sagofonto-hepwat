Ext.define('AGS3xIoTAdmin.store.artogis.ags3x.resultList.resultListDataSore', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.resultListDataSore',

    root: {
        name: 'Results',
        id: 0,
        expanded: true
    },
    proxy: {
        type: 'memory',
        reader: {
            type: 'json'
        }
    }
});