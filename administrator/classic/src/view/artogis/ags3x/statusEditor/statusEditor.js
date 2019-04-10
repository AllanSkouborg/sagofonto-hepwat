var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.statusEditor.statusEditor', {

    extend: 'Ext.panel.Panel',
    xtype: 'ags3xStatusEditor',
    id: 'ags3xStatusEditor',

    requires: [
     'AGS3xIoTAdmin.view.artogis.ags3x.statusEditor.statusEditorController',
     'AGS3xIoTAdmin.view.artogis.ags3x.statusEditor.statusEditorModel'
    ],

    viewModel: {
        type: 'ags3xStatusEditor'
    },

    controller: 'ags3xStatusEditor',

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelStatusEditorWindowTitle: 'Status-editor',

    id: 'statusEditorPanel',
    objectDataVariablesStore: null,

    initComponent: function () {
        self = this;

        console.log('statusEditor.initcomponent - transferData: ', self.transferData);

        self.objectDataVariablesStore = {
            fields: ['name'],
            data: self.transferData.objectDataVariables
        };

        self.items = [
            {
                xtype: 'panel',
                id: 'statusEditorLeft',
                hidden: true,
                height: 400,
                width: 300,
                style: {
                    'float': 'left'
                },
                bodyStyle: {

                },
                items: [

                ]
            },
            {
                xtype: 'panel',
                id: 'statusEditorRight',
                height: 400,
                width: 300,
                style: {
                    'float': 'right'
                },
                bodyStyle: {

                },
                items: [

                ]
            }
        ]


        self.listeners = {
            afterrender: function () {
                console.log('formulaEditor.afterrender');

                if (Ext.ComponentQuery.query('#statusEditorWindow').length > 0) {
                    Ext.ComponentQuery.query('#statusEditorWindow')[0].setTitle(self.fieldLabelStatusEditorWindowTitle);
                }
            }
        }

        self.callParent(arguments);
    }
})