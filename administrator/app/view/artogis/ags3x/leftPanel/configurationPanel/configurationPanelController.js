Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.leftPanel.configurationPanel.configurationPanelController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xConfigurationPanel',

    requires: [
        'AGS3xIoTAdmin.util.events'
    ],

    onTreeContextMenu: function (table, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        var position = [e.getX() - 10, e.getY() - 10];
        e.stopEvent();

        var me = this;

        var menu = new Ext.menu.Menu({
            items: [{
                text: 'Konfigurering',
                handler: function () {
                    AGS3xIoTAdmin.util.events.fireEvent('showContentPanel', null);
                }
            }]
        }).showAt(position);
    },

    /**
     * Called when the view is created
     */
    init: function () {

    }
});