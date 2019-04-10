Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.resultList.resultListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xResultList',

    requires: [
        'AGS3xIoTAdmin.util.events',
        'AGS3xIoTAdmin.util.util'
    ],

    mapSearchLayer: null,
    mapSearchLayerSource: null,

    deleteResultLbl: 'Slet resultat',
    deleteAllResultsLbl: 'Slet alle resultater',
    clearMapSelectionLbl: 'Ryd kortselektion',

    setMapSearchResult: function (resultData) {
        var resultStore = this.getViewModel().getStore('resultListDataStore');
        var root = resultStore.getRootNode();
        root.insertChild(0, resultData);
        AGS3xIoTAdmin.util.events.fireEvent('openResultListPanel', null);

        if(Ext.ComponentQuery.query('#mapTabPanel').length > 0) {
            if (Ext.ComponentQuery.query('#mapTabPanel')[0].getCollapsed() != false) {
                Ext.ComponentQuery.query('#mapTabPanel')[0].setCollapsed(false);
            }
        }
    },

    onResultListItemClick: function (control, record) {
        if (!record.data.leaf) return;
        this.locateInMap(control.selectionModel.selected.items);
    },

    onResultListContextMenu: function (table, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        var me = this;
        var position = [e.getX() - 10, e.getY() - 10];
        e.stopEvent();
        if (!record.data.leaf) {
            var menu = new Ext.menu.Menu({
                items: [{
                    text: this.deleteResultLbl,
                    handler: function () {
                        record.remove();
                        me.clearMapResults();
                    }
                },
                {
                    text: this.deleteAllResultsLbl,
                    handler: function () {
                        table.store.getRootNode().removeAll();
                        me.clearMapResults();
                    }
                },
                {
                    text: this.clearMapSelectionLbl,
                    handler: function () {
                        me.clearMapResults();
                    }
                }]
            }).showAt(position);
        }
    },

    clearMapResults: function () {
        if (this.mapSearchLayerSource !== null) {
            this.mapSearchLayerSource.clear();
        }
    },

    locateInMap: function (items) {
        this.addNodeToMap(items);
        this.panToNode(items);
        this.zoomToNode();
    },

    addNodeToMap: function (items) {
        var node, style;

        for (i = 0; i < items.length; i++) {
            var node = items[i].data;

            var feature = new ol.Feature({
                geometry: node.geom,
                name: node.text,
                type: node.type
            });

            var style = this.getFeatureStyle(node.geomType);

            switch (node.geomType) {
                case 'point':
                case 'multipoint':
                    feature.setStyle(style);
                    break;
                case 'polygon':
                case 'multipolygon':
                    feature.setStyle(style);
                    feature.set('strokeDashStyle', AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.style);
                    break;
                case 'linestring':
                case 'multilinestring':
                    feature.setStyle(style);
                    feature.set('strokeDashStyle', AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.style);
                    break;
            }

            this.mapSearchLayerSource.clear();
            this.mapSearchLayerSource.addFeature(feature);
        }
    },

    getFeatureStyle: function (geom) {
        var style = null;

        switch (geom) {
            case 'point':
            case 'multipoint':
                switch (AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.type) {
                    case "Icon":
                        style = new ol.style.Style({
                            image: new ol.style.Icon({
                                opacity: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.opacity,
                                src: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.iconSource,
                                scale: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.scale
                            })
                        });
                        break;
                    case "Circle":
                        style = new ol.style.Style({
                            image: new ol.style.Circle({
                                fill: new ol.style.Fill({
                                    color: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.fillColor
                                }),
                                stroke: new ol.style.Stroke({
                                    color: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.strokeColor,
                                    width: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.strokeWidth
                                }),
                                radius: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.radius
                            })
                        });
                        break;
                }
                break;
            case 'polygon':
            case 'multipolygon':
                style = new ol.style.Style({
                    fill: new ol.style.Fill({
                        color: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.fillColor
                    }),
                    stroke: new ol.style.Stroke({
                        color: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.strokeColor,
                        width: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.strokeWidth,
                        lineDash: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.style
                    })
                });
                break;
            case 'line':
            case 'multilinestring':
                style = new ol.style.Style({
                    stroke: new ol.style.Stroke({
                        color: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.strokeColor,
                        width: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.strokeWidth,
                        lineDash: AGS3xIoTAdmin.systemData.mapSearchConfig.pointStyle.style
                    })
                });
        }

        return style;
    },

    panToNode: function (items) {
        var pan = ol.animation.pan({
            duration: 500,
            source: (AGS3xIoTAdmin.map.getView().getCenter())
        });
        AGS3xIoTAdmin.map.beforeRender(pan);
        AGS3xIoTAdmin.map.getView().setCenter(ol.proj.transform([items[0].data.x, items[0].data.y], 'EPSG:4326', AGS3xIoTAdmin.map.getView().getProjection()));
    },

    zoomToNode: function () {
        var extent = this.mapSearchLayerSource.getExtent();
        var map = AGS3xIoTAdmin.map;
        map.getView().fit(extent, map.getSize());
        var currentScale = Math.round(this.getCurrentScale(map));
        var minScale = AGS3xIoTAdmin.systemData.mapSearchConfig.singleSearchScale;
        if (currentScale < minScale) {
            map.getView().setResolution(this.getResolutionFromScale(map, minScale));
        }
    },

    getCurrentScale: function (map) {
        var view = map.getView();
        var resolution = view.getResolution();
        var units = map.getView().getProjection().getUnits();
        var dpi = 25.4 / 0.28;
        var mpu = ol.proj.METERS_PER_UNIT[units];
        var scale = resolution * mpu * 39.37 * dpi;
        return scale;
    },

    getResolutionFromScale: function (map, scale) {
        var units = map.getView().getProjection().getUnits();
        var dpi = 25.4 / 0.28;
        var mpu = ol.proj.METERS_PER_UNIT[units];
        var resolution = scale / mpu / 39.37 / dpi;
        return parseFloat(resolution);
    },

    createMapSearchLayer: function () {
        if (this.mapSearchLayer !== null) {
            if (this.mapSearchLayerSource !== null) {
                this.mapSearchLayerSource.clear();
            }
            if (!this.layerExistsInMap(this.mapSearchLayer)) {
                AGS3xIoTAdmin.map.addLayer(this.mapSearchLayer);
            }
            return;
        }

        this.mapSearchLayerSource = new ol.source.Vector();
        this.mapSearchLayer = new ol.layer.Vector({
            source: this.mapSearchLayerSource
        });
        this.mapSearchLayer.set('name', 'mapSearch-layer');
        AGS3xIoTAdmin.map.addLayer(this.mapSearchLayer);
    },

    init: function () {
        AGS3xIoTAdmin.util.events.on('setMapSearchResult', this.setMapSearchResult, this);

        this.createMapSearchLayer();
    }
});