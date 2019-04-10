Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.contentPanel.contentPanel', {
    extend: 'Ext.container.Container',
    xtype: 'ags3xContentPanel',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.dataIO.dataIO',
        'AGS3xIoTAdmin.view.artogis.ags3x.contentPanel.contentPanelController'
    ],

    controller: 'ags3xContentPanel',
    id: 'ags3xContentPanel'
});