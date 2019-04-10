var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.leftPanel.leftPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'ags3xLeftPanel',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.leftPanel.leftPanelController',
        'AGS3xIoTAdmin.util.events'
    ],

    controller: 'ags3xLeftPanel',

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelLeftMenuTitle: 'Menu',
    fieldLabelLeftMenuDashboardEditor: 'Dashboards',
    fieldLabelLeftMenuRobot: 'DataRobotter',
    fieldLabelLeftMenuDataConfig: 'Datakonfigurering',
    fieldLabelLeftMenuDataIO: 'Data IO',
    fieldLabelLeftMenuObjectTypes: 'Objekttyper',
    fieldLabelLeftMenuComponentTypes: 'Komponenttyper',
    fieldLabelLeftMenuFullSensorsList: 'Komplet sensorliste',
    fieldLabelLeftMenuTemplateEditor: 'Udregningsskabeloner',

    
    style : {
        'margin-top': '2px !important',
        'top': '2px !important'
    },

    initComponent: function () {
        self = this;

        console.log('leftPanel.initComponent - version: ', self.version);

        //self.version = AGS3xIoTAdmin.systemData.version;
        self.id = 'ags3xLeftPanel',
        self.layout = 'vbox',
        self.collapsible = false,
        
        self.header = {
            title: self.fieldLabelLeftMenuTitle,
            id: 'leftPanelMenuHeader'
        }
        self.items = [
            {
                xtype: 'panel',
                id: 'menuOptionShowDashboardEditor',
                title: self.fieldLabelLeftMenuDashboardEditor,
                width: 300,
                height: 'auto',
                padding: '0 !important',
                cls: 'left-menu-parent-entry',
                iconCls: 'fa fa-th',
                header: {
                    listeners: {
                        click: function (node, rec) {
                            console.log('Left menu click - node: ', node);
                            console.log('Left menu click - rec: ', rec);
                            var ownerComponentId = node.ownerCt.id;
                            if (!document.getElementById(ownerComponentId).classList.contains('active')) {
                                document.getElementsByClassName('left-menu-parent-entry active')[0].classList.toggle('active');
                                document.getElementById(ownerComponentId).classList.toggle('active');
                                AGS3xIoTAdmin.util.events.fireEvent('showDashboardEditorPanel', null);
                            }

                        }
                    }
                }
            },
            {
                xtype: 'panel',
                id: 'menuOptionShowRobotMonitor',
                title: self.fieldLabelLeftMenuRobot,
                width: 300,
                height: 'auto',
                padding: '0 !important',
                cls: 'left-menu-parent-entry',
                iconCls: 'fa fa-television',
                header: {
                    listeners: {
                        click: function (node, rec) {
                            console.log('Left menu click - node: ', node);
                            console.log('Left menu click - rec: ', rec);
                            var ownerComponentId = node.ownerCt.id;
                            if (!document.getElementById(ownerComponentId).classList.contains('active')) {
                                document.getElementsByClassName('left-menu-parent-entry active')[0].classList.toggle('active');
                                document.getElementById(ownerComponentId).classList.toggle('active');
                                AGS3xIoTAdmin.util.events.fireEvent('showDataRobotsPanel', null);
                            }

                        }
                    }
                }
            },
            /*{
                xtype: 'panel',
                id: 'menuOptionShowDataOptions',
                title: self.fieldLabelLeftMenuDataConfig,
                width: 300,
                height: 'auto',
                padding: 0,
                cls: 'left-menu-parent-entry collapsible',
                iconCls: 'fa fa-calculator',
                //html: '<div>World!</div>',
                collapsible: true,
                collapsed: false,
                header: {
                    listeners: {
                        click: function (node, rec) {
                            console.log('Left menu click - node: ', node);
                            console.log('Left menu click - event: ', event);
                            console.log('Collapse / expand...');
                            var ownerComponent = node.ownerCt;
                            var isCollapsed = ownerComponent.getCollapsed();
                            console.log(ownerComponent.id + 'is collapsed: ', isCollapsed);
                            if (isCollapsed == false) {
                                ownerComponent.setCollapsed(true);
                            }
                            else {
                                ownerComponent.setCollapsed(false);
                            }
                        }
                    }
                }
                ,
                items: [
                    {
                        xtype: 'panel',
                        id: 'menuOptionShowDataIO',
                        title: self.fieldLabelLeftMenuDataIO,
                        cls: 'left-menu-parent-entry left-menu-child-entry',
                        iconCls: 'fa fa-database',
                        width: 300,
                        height: 'auto',
                        header: {
                            listeners: {
                                click: function (node, rec) {
                                    console.log('Left menu click (menuOptionShowDataIO) - node: ', node);
                                    //console.log('Left menu click (menuOptionShowDataIO) - event: ', event);
                                    var ownerComponentId = node.ownerCt.id;
                                    if (!document.getElementById(ownerComponentId).classList.contains('active')) {
                                        document.getElementsByClassName('left-menu-parent-entry active')[0].classList.toggle('active');
                                        document.getElementById(ownerComponentId).classList.toggle('active');
                                        AGS3xIoTAdmin.util.events.fireEvent('showContentPanel', null);
                                    }
                                }
                            }
                        }
                    },
                     {
                         xtype: 'panel',
                         id: 'menuOptionShowObjectTypes',
                         title: self.fieldLabelLeftMenuObjectTypes,
                         cls: 'left-menu-parent-entry left-menu-child-entry',
                         iconCls: 'fa fa-cubes',
                         width: 300,
                         height: 'auto',
                         header: {
                             listeners: {
                                 click: function (node, rec) {
                                     console.log('Left menu click (menuOptionShowObjectTypes) - node: ', node);
                                     console.log('Left menu click (menuOptionShowObjectTypes) - rec: ', rec);
                                     var ownerComponentId = node.ownerCt.id;
                                     if (!document.getElementById(ownerComponentId).classList.contains('active')) {
                                         document.getElementsByClassName('left-menu-parent-entry active')[0].classList.toggle('active');
                                         document.getElementById(ownerComponentId).classList.toggle('active');
                                         AGS3xIoTAdmin.util.events.fireEvent('showObjectEditorPanel', null);
                                     }
                                 }
                             }
                         }
                     },
                     {
                         xtype: 'panel',
                         id: 'menuOptionShowComponentTypes',
                         title: self.fieldLabelLeftMenuComponentTypes,
                         cls: 'left-menu-parent-entry left-menu-child-entry',
                         iconCls: 'fa fa-cube',
                         width: 300,
                         height: 'auto',
                         header: {
                             listeners: {
                                 click: function (node, rec) {
                                     console.log('Left menu click (menuOptionShowComponentTypes) - node: ', node);
                                     console.log('Left menu click (menuOptionShowComponentTypes) - rec: ', rec);
                                     var ownerComponentId = node.ownerCt.id;
                                     if (!document.getElementById(ownerComponentId).classList.contains('active')) {
                                         document.getElementsByClassName('left-menu-parent-entry active')[0].classList.toggle('active');
                                         document.getElementById(ownerComponentId).classList.toggle('active');
                                         AGS3xIoTAdmin.util.events.fireEvent('showComponentEditorPanel', null);
                                     }
                                 }
                             }
                         }
                     }
                ]
                
            },*/
            {
                xtype: 'panel',
                id: 'menuOptionShowDataIO',
                title: self.fieldLabelLeftMenuDataIO,
                cls: 'left-menu-parent-entry',
                iconCls: 'fa fa-database',
                width: 300,
                height: 'auto',
                header: {
                    listeners: {
                        click: function (node, rec) {
                            console.log('Left menu click (menuOptionShowDataIO) - node: ', node);
                            //console.log('Left menu click (menuOptionShowDataIO) - event: ', event);
                            var ownerComponentId = node.ownerCt.id;
                            if (!document.getElementById(ownerComponentId).classList.contains('active')) {
                                document.getElementsByClassName('left-menu-parent-entry active')[0].classList.toggle('active');
                                document.getElementById(ownerComponentId).classList.toggle('active');
                                AGS3xIoTAdmin.util.events.fireEvent('showContentPanel', null);
                            }
                        }
                    }
                }
            },
            {
                xtype: 'panel',
                id: 'menuOptionShowObjectTypes',
                title: self.fieldLabelLeftMenuObjectTypes,
                cls: 'left-menu-parent-entry',
                iconCls: 'fa fa-cubes',
                width: 300,
                height: 'auto',
                header: {
                    listeners: {
                        click: function (node, rec) {
                            console.log('Left menu click (menuOptionShowObjectTypes) - node: ', node);
                            console.log('Left menu click (menuOptionShowObjectTypes) - rec: ', rec);
                            var ownerComponentId = node.ownerCt.id;
                            if (!document.getElementById(ownerComponentId).classList.contains('active')) {
                                document.getElementsByClassName('left-menu-parent-entry active')[0].classList.toggle('active');
                                document.getElementById(ownerComponentId).classList.toggle('active');
                                AGS3xIoTAdmin.util.events.fireEvent('showObjectEditorPanel', null);
                            }
                        }
                    }
                }
            },
            {
                xtype: 'panel',
                id: 'menuOptionShowComponentTypes',
                title: self.fieldLabelLeftMenuComponentTypes,
                cls: 'left-menu-parent-entry',
                iconCls: 'fa fa-cube',
                width: 300,
                height: 'auto',
                header: {
                    listeners: {
                        click: function (node, rec) {
                            console.log('Left menu click (menuOptionShowComponentTypes) - node: ', node);
                            console.log('Left menu click (menuOptionShowComponentTypes) - rec: ', rec);
                            var ownerComponentId = node.ownerCt.id;
                            if (!document.getElementById(ownerComponentId).classList.contains('active')) {
                                document.getElementsByClassName('left-menu-parent-entry active')[0].classList.toggle('active');
                                document.getElementById(ownerComponentId).classList.toggle('active');
                                AGS3xIoTAdmin.util.events.fireEvent('showComponentEditorPanel', null);
                            }
                        }
                    }
                }
            },
            {
                xtype: 'panel',
                id: 'menuOptionShowFullSensorsList',
                title: self.fieldLabelLeftMenuFullSensorsList,
                width: 300,
                height: 'auto',
                padding: 0,
                cls: 'left-menu-parent-entry',
                iconCls: 'fa fa-plug',
                header: {
                    listeners: {
                        click: function (node, rec) {
                            console.log('Left menu click - node: ', node);
                            console.log('Left menu click - rec: ', rec);
                            var ownerComponentId = node.ownerCt.id;
                            if (!document.getElementById(ownerComponentId).classList.contains('active')) {
                                document.getElementsByClassName('left-menu-parent-entry active')[0].classList.toggle('active');
                                document.getElementById(ownerComponentId).classList.toggle('active');
                                AGS3xIoTAdmin.util.events.fireEvent('showFullSensorsListPanel', null);
                            }
                        }
                    }
                }
            },
            {
                xtype: 'panel',
                id: 'menuOptionShowTemplateEditor',
                title: self.fieldLabelLeftMenuTemplateEditor,
                width: 300,
                height: 'auto',
                padding: 0,
                cls: 'left-menu-parent-entry',
                iconCls: 'fa fa-pencil',
                header: {
                    listeners: {
                        click: function (node, rec) {
                            console.log('Left menu click - node: ', node);
                            console.log('Left menu click - rec: ', rec);
                            var ownerComponentId = node.ownerCt.id;
                            if (!document.getElementById(ownerComponentId).classList.contains('active')) {
                                document.getElementsByClassName('left-menu-parent-entry active')[0].classList.toggle('active');
                                document.getElementById(ownerComponentId).classList.toggle('active');
                                AGS3xIoTAdmin.util.events.fireEvent('showTemplateEditorPanel', null);
                            }
                        }
                    }
                }
            },
            {
                xtype: 'panel',
                id: 'menuVersionFooter',
                width: 300,
                height: 20,
                margin: 0,
                html: '<div id="menuVersionFooterContent"></div>',
                cls: 'left-menu-footer',
                style: {
                    'text-align': 'center'
                },
                bodyStyle: {
                    'font-size': '10px !important'
                },
                listeners: {
                    afterrender: function (component) {
                        var leftPanel = document.getElementById('ags3xLeftPanel');
                        console.log('menuVersionFooter - leftPanel: ', leftPanel);
                        console.log('menuVersionFooter - component: ', component);
                        console.log('menuVersionFooter - calculation: ', window.innerHeight);

                        //var version = '1.0.0';
                        //document.getElementById('menuVersionFooterContent').innerHTML = 'Version ' + version;

                        //console.log('menuVersionFooter - version, afterrender: ', version);

                        var leftMenuButtonCount = Ext.ComponentQuery.query('#left-menu-parent-entry').length;

                        var newFooterValue = window.innerHeight - (leftMenuButtonCount * 36) - 36 + 16;

                        component.setMargin(newFooterValue + 'px 0 0 0');
                    }
                }
            }
            
        ];

        self.callParent(arguments);

    } // initComponent
});