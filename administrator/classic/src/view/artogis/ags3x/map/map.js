Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.map.map', {
    extend: 'GeoExt.component.Map',
    xtype: 'ags3xMap',
    id: 'ags3xMap',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.map.mapController'
    ],

    controller: 'ags3xMap',
    layout: 'fit',
    map: new ol.Map({
        logo: false
    }),

    listeners: {
        afterrender: 'onAfterRenderer'
    }
});