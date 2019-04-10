Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.leftPanel.attributeViewerPanel.attributeViewerPanel', {
    extend: 'Ext.tree.Panel',
    xtype: 'ags3xAttributeViewerTree',

    useArrows: true,
    rootVisible: false,
    multiSelect: false,
    singleExpand: false,
    hideHeaders: true,
    lines: false,

    titleLabel: 'Attribute Viewer',

    initComponent: function () {
        this.title = this.titleLabel;
        this.callParent();
    }

});