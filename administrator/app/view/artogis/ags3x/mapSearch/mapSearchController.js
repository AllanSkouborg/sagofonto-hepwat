Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.mapSearch.mapSearchController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xMapSearch',

    requires: [
        'AGS3xIoTAdmin.util.events',
        'AGS3xIoTAdmin.util.util'
    ],

    task: null,

    // text content - default is Danish (da), for translations, see translation.js
    connErrMsg: 'Søgning er ikke mulig, kontakt veligst support. \n Grund: Server er ikke tilgængelig.',
    fieldLabelSearch: 'Søgning',

    onKeyUp: function (ct, e) {
        this.task.cancel();

        if (e.keyCode === 13) {
            var searchValue = this.getViewModel().get('mapSearchValue');
            if (searchValue !== '') {
                this.getViewModel().set('searchFieldDisabled', true);
                this.startSearch(searchValue);
            }
        }
        else {
            this.task.delay(2000);
        }
    },

    startSearch: function (searchValue) {
        var self = this;

        try {
            var url = '';
            var mapSearchConfig = AGS3xIoTAdmin.systemData.mapSearchConfig;

            var store = Ext.create('Ext.data.TreeStore', {
                root: {
                    text: 'Results',
                    expanded: true
                },
                proxy: {
                    type: 'memory',
                    reader: {
                        type: 'json'
                    }
                }
            });

            if (~searchValue.indexOf('*')) {
                var encodedSearchValue = encodeURI(searchValue);
                url = mapSearchConfig.serverUrl + '/' + mapSearchConfig.indexNames + '/_search?size=' + mapSearchConfig.searchSize + '&sort=_score&q=' + mapSearchConfig.searchFieldName + ':' + encodedSearchValue;

                console.log('mapSearchController.startSearch - A, url: ', url);
                Ext.Ajax.request({
                    url: url,
                    method: 'GET',
                    disableCaching: true,
                    scope: this,
                    callback: function (options, success, response) {

                        if (success !== true) {
                            if (response.responseText == "") {
                                AGS3xIoTAdmin.util.util.errorDlg('mapSearch', this.connErrMsg, 'ERROR');
                            } else {
                                AGS3xIoTAdmin.util.util.errorDlg('mapSearch', response.responseText, 'ERROR');
                            }
                            this.getViewModel().set('searchFieldDisabled', false);
                            return;
                        }
                        else {
                            if (response.responseText == "") {
                                AGS3xIoTAdmin.util.util.errorDlg('mapSearch', this.connErrMsg, 'ERROR');
                                this.getViewModel().set('searchFieldDisabled', false);
                                return;
                            }
                        }

                        var result = Ext.JSON.decode(response.responseText);
                        if (result.hits.hits) {
                            var data = {
                                text: self.fieldLabelSearch + ' (' + searchValue + ') (' + mapSearchConfig.searchSize + '/' + result.hits.total + ')',
                                type: "MapSearch",
                                expanded: true,
                                leaf: false,
                                uniqueId: AGS3xIoTAdmin.util.util.generateUUID(),
                                children: []
                            }

                            var items = result.hits.hits;
                            var type = '';
                            var x, y;

                            for (var i = 0; i < items.length; i++) {
                                var item = items[i];
                                var t = item._index + '/' + item._type;
                                if (type !== t) {
                                    type = t;
                                    var typeNode = {
                                        type: "MapSearch",
                                        text: t,
                                        expanded: true,
                                        leaf: false,
                                        uniqueId: AGS3xIoTAdmin.util.util.generateUUID(),
                                        children: []
                                    }

                                    data.children.push(typeNode);
                                }

                                console.log('mapSearchController.startSearch - A, perform getGeometryInfo');
                                var geometryInfo = this.getGeometryInfo(item);

                                var node = {
                                    type: "MapSearch",
                                    nodeType: item._type,
                                    text: item._source.searchfield,
                                    geom: geometryInfo.geometry,
                                    x: geometryInfo.x,
                                    y: geometryInfo.y,
                                    geomType: geometryInfo.geomType,
                                    expanded: false,
                                    leaf: true,
                                    uniqueId: AGS3xIoTAdmin.util.util.generateUUID()
                                };

                                typeNode.children.push(node);
                            }

                            this.getViewModel().set('searchFieldDisabled', false);
                            AGS3xIoTAdmin.util.events.fireEvent('setMapSearchResult', data);
                        }
                    }
                }, this);
            }
            else {
                url = mapSearchConfig.serverUrl + '/' + mapSearchConfig.indexNames + '/_search';
                

                var query = {
                    "query": {
                        "match_phrase": {
                            "searchfield": searchValue
                        }
                    },
                    "size": mapSearchConfig.searchSize,
                    "sort": "_score",
                    "aggs": {
                        "byIndex": {
                            "terms": {
                                "field": "_index"
                            },
                            "aggs": {
                                "byType": {
                                    "terms": {
                                        "field": "_type"
                                    }
                                }
                            }
                        }
                    }
                };

                console.log('mapSearchController.startSearch - B, url: ', url);

                // perform search
                Ext.Ajax.request({
                    url: url,
                    method: 'POST',
                    params: Ext.JSON.encode(query),
                    disableCaching: true,
                    scope: this,
                    callback: function (options, success, response) {
                        console.log('Response: ', Ext.JSON.decode(response.responseText));
                        if (success !== true) {
                            if (response.responseText == "") {
                                AGS3xIoTAdmin.util.util.errorDlg('mapSearch', this.connErrMsg, 'ERROR');
                            } else {
                                AGS3xIoTAdmin.util.util.errorDlg('mapSearch', response.responseText, 'ERROR');
                            }
                            this.getViewModel().set('searchFieldDisabled', false);
                            return;
                        }
                        else {
                            if (response.responseText == "") {
                                AGS3xIoTAdmin.util.util.errorDlg('mapSearch', this.connErrMsg, 'ERROR');
                                this.getViewModel().set('searchFieldDisabled', false);
                                return;
                            }
                        }

                        var result = Ext.JSON.decode(response.responseText);

                        if (result.hits.hits) {
                            var agg = result.aggregations.byIndex.buckets;

                            var data = {
                                text: 'Søgning (' + searchValue + ') (' + mapSearchConfig.searchSize + '/' + result.hits.total + ')',
                                type: "MapSearch",
                                expanded: true,
                                leaf: false,
                                uniqueId: AGS3xIoTAdmin.util.util.generateUUID(),
                                children: []
                            }

                            var items = result.hits.hits;
                            var type = '';
                            var x, y;

                            for (var i = 0; i < items.length; i++) {
                                var item = items[i];
                                var t = item._index + '/' + item._type;
                                if (type !== t) {
                                    type = t;

                                    var typeNode = {
                                        text: t + this.getAggCount(agg, item._index, item._type),
                                        type: "MapSearch",
                                        expanded: true,
                                        leaf: false,
                                        uniqueId: AGS3xIoTAdmin.util.util.generateUUID(),
                                        children: []
                                    }

                                    data.children.push(typeNode);
                                }

                                console.log('mapSearchController.startSearch - B, perform getGeometryInfo');
                                var geometryInfo = this.getGeometryInfo(item);

                                var node = {
                                    type: "MapSearch",
                                    nodeType: item._type,
                                    text: item._source.searchfield,
                                    geom: geometryInfo.geometry,
                                    x: geometryInfo.x,
                                    y: geometryInfo.y,
                                    geomType: geometryInfo.geomType,
                                    expanded: false,
                                    leaf: true,
                                    uniqueId: AGS3xIoTAdmin.util.util.generateUUID()
                                };

                                typeNode.children.push(node);
                            }

                            this.getViewModel().set('searchFieldDisabled', false);
                            AGS3xIoTAdmin.util.events.fireEvent('setMapSearchResult', data);
                        }
                    }
                }, this);
            }
        }
        catch (ex) {
            AGS3xIoTAdmin.util.util.errorDlg('mapSearch', 'Search error.', 'ERROR');
            this.getViewModel().set('searchFieldDisabled', false);
        }
    },

    getGeometryInfo: function (item) {
        console.log('mapSearchController.getGeometryInfo - item: ', item);
        var x, y, geom, geomType;

        if (item._source.location.hasOwnProperty('type')) {
            geomType = item._source.location['type'];
        }

        var record = {};
        record.data = item;

        if (geomType) {
            switch (geomType) {
                case undefined:
                    geomType = "point";
                    geom = new ol.geom.Point(ol.proj.transform([record.data._source.location['lon'], record.data._source.location['lat']], 'EPSG:4326', AGS3xIoTAdmin.map.getView().getProjection()));
                    x = geom.x;
                    y = geom.y;
                    break;
                case 'multipoint':
                    var res = record.data._source.location['coordinates'];
                    geom = new ol.geom.MultiPoint();
                    for (var i = 0; i < res.length; i++) {
                        geom.appendPoint(new ol.geom.Point(ol.proj.transform([Number(res[i][0]), Number(res[i][1])], 'EPSG:4326', AGS3xIoTAdmin.map.getView().getProjection())));
                    }
                    x = ol.extent.getCenter(geom.getExtent())[0];
                    y = ol.extent.getCenter(geom.getExtent())[1];
                    break;
                case 'polygon':
                    var res = record.data._source.location['coordinates'][0];
                    var points = [];
                    for (var i = 0; i < res.length; i++) {
                        points[i] = ol.proj.transform([Number(res[i][0]), Number(res[i][1])], 'EPSG:4326', AGS3xIoTAdmin.map.getView().getProjection());
                    }
                    geom = new ol.geom.Polygon([points]);
                    x = ol.extent.getCenter(geom.getExtent())[0];
                    y = ol.extent.getCenter(geom.getExtent())[1];
                    break;
                case 'multipolygon':
                    var mres = record.data._source.location['coordinates'];
                    geom = new ol.geom.MultiPolygon();
                    for (var j = 0; j < mres.length; j++) {
                        var points = [];
                        for (var i = 0; i < mres[j][0].length; i++) {
                            points[i] = ol.proj.transform([Number(mres[j][0][i][0]), Number(mres[j][0][i][1])], 'EPSG:4326', AGS3xIoTAdmin.map.getView().getProjection());
                        }
                        mgeom = new ol.geom.Polygon([points]);
                        geom.appendPolygon(mgeom);
                    }
                    x = ol.extent.getCenter(geom.getExtent())[0];
                    y = ol.extent.getCenter(geom.getExtent())[1];
                    break;
                case 'linestring':
                    var res = record.data._source.location['coordinates'];
                    geom = new ol.geom.LineString();
                    for (var i = 0; i < res.length; i++) {
                        geom.appendCoordinate(ol.proj.transform([Number(res[i][0]), Number(res[i][1])], 'EPSG:4326', AGS3xIoTAdmin.map.getView().getProjection()));
                    }
                    x = ol.extent.getCenter(geom.getExtent())[0];
                    y = ol.extent.getCenter(geom.getExtent())[1];
                    break;
                case 'multilinestring':
                    var mres = record.data._source.location['coordinates'];
                    geom = new ol.geom.MultiLineString();
                    for (var j = 0; j < mres.length; j++) {
                        var lgeom = new ol.geom.LineString();
                        for (var i = 0; i < mres[j].length; i++) {
                            lgeom.appendCoordinate(ol.proj.transform([Number(mres[j][i][0]), Number(mres[j][i][1])], 'EPSG:4326', AGS3xIoTAdmin.map.getView().getProjection()));
                        }
                        geom.appendLineString(lgeom);
                    }
                    x = ol.extent.getCenter(geom.getExtent())[0];
                    y = ol.extent.getCenter(geom.getExtent())[1];
                    break;
                default:
                    break;
            }
        }
        else {
            x = item._source.location[0];
            y = item._source.location[1];
            geom = new ol.geom.Point(ol.proj.transform([x, y], 'EPSG:4326', AGS3xIoTAdmin.map.getView().getProjection()))
            geomType = "point";
        }

        return { x: x, y: y, geometry: geom, geomType: geomType }
    },

    getAggCount: function (agg, index, type) {
        for (var i = 0; i < agg.length; i++) {
            if (agg[i].key == index) {
                for (var j = 0; j < agg[i].byType.buckets.length; j++) {
                    if (agg[i].byType.buckets[j].key == type) {
                        return ' (' + agg[i].byType.buckets[j].doc_count + ')';
                    }
                }
            }
        }

        return '';
    },

    init: function () {
        this.task = new Ext.util.DelayedTask(function () {
            var searchValue = this.getViewModel().get('mapSearchValue');
            if (searchValue !== '') {
                this.getViewModel().set('searchFieldDisabled', true);
                this.startSearch(searchValue);
            }
        }, this);
    }
});