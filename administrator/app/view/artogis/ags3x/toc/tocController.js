Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.toc.tocController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xTOC',

    onBeforeCheckChange: function (record, checkedState, e) {
        console.log('tocController.onBeforeCheckChange - record: ', record);
        console.log('tocController.onBeforeCheckChange - checkedState: ', checkedState);
        console.log('tocController.onBeforeCheckChange - e: ', e);
        AGS3xIoTAdmin.util.events.fireEvent('showHideLayer', { layerId: record.data.data.layerId, visible: !checkedState });
    },

    addTOCItem: function (item) {
        console.log('tocController.addTOCItem, item: ', item);

        // Uncheck all items if showLayersOnStart is false
        if (AGS3xIoTAdmin.systemData.hasOwnProperty('showLayersOnStart') && AGS3xIoTAdmin.systemData.showLayersOnStart == false) {
            for (var i = 0; i < item.children.length; i++) {
                var child = item.children[i];
                child.checked = false;
            }
        }

        var layerTreeStore = this.getViewModel().getStore('layerTree');
        var rootNode = layerTreeStore.getRootNode();
        rootNode.appendChild(item);
    },
    test: function() {
        console.log('tocController.test...');
    },
    init: function () {
        console.log('tocController.init...');
        AGS3xIoTAdmin.util.events.on('addTOCItem', this.addTOCItem, this);
    }

});