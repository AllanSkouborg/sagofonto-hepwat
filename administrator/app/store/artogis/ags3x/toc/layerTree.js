Ext.define('AGS3xIoTAdmin.store.artogis.ags3x.toc.layerTree', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.layerTree',

    fields: [
     'name',
     'description',
     'envelope',
     'mapService',
     'editable',
     'geometryEditable',
     'transparent',
     'isBackgroundLayer',
     'mapId',
     'transparent',
     'tiled',
     'isBackgroundLayer',
     'legend_url',
     'visibleInMap'
    ],
    root: {
        text: 'mapLayers',
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