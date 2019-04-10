/**
 * This class is the main view for the application. It is specified in app.js as the
 * "mainView" property. That setting automatically applies the "viewport"
 * plugin causing this view to become the body element (i.e., the viewport).
 *
 * TODO - Replace this content of this view to suite the needs of your application.
 */
Ext.define('AGS3xIoTAdmin.view.main.Main', {
    extend: 'Ext.panel.Panel',
    xtype: 'ags3xMain',

    requires: [
        'Ext.plugin.Viewport',
        'Ext.window.MessageBox',
        'Ext.layout.*',
        'Ext.grid.*',
        'Ext.form.*',
        'Ext.tab.*',
        'Ext.data.*',
        //'Ext.field.*',

        'AGS3xIoTAdmin.view.main.MainController',
        'AGS3xIoTAdmin.view.main.MainModel',

        'AGS3xIoTAdmin.view.artogis.ags3x.leftPanel.leftPanel',
        'AGS3xIoTAdmin.view.artogis.ags3x.contentPanel.contentPanel'
    ],

    controller: 'ags3xMain',
    id: 'ags3xMain',
    viewModel: 'ags3xMain',


    layout: 'border',
    margin: '1 1 1 1',
    header: {
        //title: 'sago&#8226;fonto&#8482; IoTAdmin',
        height: 70,
        html: '<div class="logo-sagofonto"><img height="44" src="resources/artogis/ags3x/images/logo/sagofonto RGB.svg"></img></div>' +
              '<a href="http://artogis.dk" target="_blank"><div class="logo-artogis" title="artogis.dk"></div></a>'
    },
    items: [
        {
            region: 'west',
            xtype: 'ags3xLeftPanel',
            floatable: false,
            hideCollapseTool: false,
            collapsible: true,
            collapsed: false,
            width: 300
        },
        {
            region: 'center',
            xtype: 'ags3xContentPanel',
            layout: 'fit',
            header: false
        }
    ],
    listeners: {
        resize: 'performResize',

        afterrender: function (component) {
            console.log('Main.afterrender - component: ', component);
            var dataLoadingMask = new Ext.LoadMask(
                {
                    msg: 'Henter startdata - vent venligst...',
                    id: 'maskInitData',
                    target: component,
                    //cls: 'io-data-mask',
                    style: {
                        /*'opacity': '0.5',*/
                        'width': '100%',
                        'height': '100%'
                    }
                }
            );
            dataLoadingMask.show();
        }
    }
});
