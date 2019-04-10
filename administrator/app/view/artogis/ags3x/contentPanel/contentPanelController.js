Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.contentPanel.contentPanelController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xContentPanel',

    requires: [
        'AGS3xIoTAdmin.util.events',
        'AGS3xIoTAdmin.view.artogis.ags3x.dataIO.dataIO',
        'AGS3xIoTAdmin.view.artogis.ags3x.dataRobots.dataRobots',
        'AGS3xIoTAdmin.view.artogis.ags3x.dataSensors.dataSensors',
        'AGS3xIoTAdmin.view.artogis.ags3x.objectEditor.objectEditor',
        'AGS3xIoTAdmin.view.artogis.ags3x.componentEditor.componentEditor',
        'AGS3xIoTAdmin.view.artogis.ags3x.templateEditor.templateEditor',
        'AGS3xIoTAdmin.view.artogis.ags3x.dashboardEditor.dashboardEditor'
    ],

    contentPanel: null,

    // show Robot Dashboard panel
    showDataRobotsPanel: function () {
        var me = this;
        if (document.getElementsByClassName('left-menu-parent-entry active').length > 0) {
            document.getElementsByClassName('left-menu-parent-entry active')[0].classList.remove('active');
        }
        document.getElementById('menuOptionShowRobotMonitor').classList.add('active');
        me.performContentExecution('ags3xDataRobots', 'AGS3xIoTAdmin.view.artogis.ags3x.dataRobots.dataRobots');
    },

    // show Data IO panel
    showContentPanel: function () {
        var me = this;
        if (document.getElementsByClassName('left-menu-parent-entry active').length > 0) {
            document.getElementsByClassName('left-menu-parent-entry active')[0].classList.remove('active');
        }
        document.getElementById('menuOptionShowDataIO').classList.add('active');
        me.performContentExecution('ags3xDataIOConfig', 'AGS3xIoTAdmin.view.artogis.ags3x.dataIO.dataIO');
    },

    // show Object Editing panel
    showObjectEditorPanel: function () {
        var me = this;
        if (document.getElementsByClassName('left-menu-parent-entry active').length > 0) {
            document.getElementsByClassName('left-menu-parent-entry active')[0].classList.remove('active');
        }
        document.getElementById('menuOptionShowObjectTypes').classList.add('active');
        me.performContentExecution('ags3xObjectEditor', 'AGS3xIoTAdmin.view.artogis.ags3x.objectEditor.objectEditor');
    },

    // show Component Editing panel
    showComponentEditorPanel: function () {
        var me = this;
        if (document.getElementsByClassName('left-menu-parent-entry active').length > 0) {
            document.getElementsByClassName('left-menu-parent-entry active')[0].classList.remove('active');
        }
        document.getElementById('menuOptionShowComponentTypes').classList.add('active');
        me.performContentExecution('ags3xComponentEditor', 'AGS3xIoTAdmin.view.artogis.ags3x.componentEditor.componentEditor');
    },
    
    // show Full Sensors List panel
    showFullSensorsListPanel: function () {
        var me = this;
        if (document.getElementsByClassName('left-menu-parent-entry active').length > 0) {
            document.getElementsByClassName('left-menu-parent-entry active')[0].classList.remove('active');
        }
        document.getElementById('menuOptionShowFullSensorsList').classList.add('active');
        me.performContentExecution('ags3xDataSensors', 'AGS3xIoTAdmin.view.artogis.ags3x.dataSensors.dataSensors');
    },

    // show Template Editor panel
    showTemplateEditorPanel: function() {
        var me = this;
        if (document.getElementsByClassName('left-menu-parent-entry active').length > 0) {
            document.getElementsByClassName('left-menu-parent-entry active')[0].classList.remove('active');
        }
        document.getElementById('menuOptionShowTemplateEditor').classList.add('active');
        me.performContentExecution('ags3xTemplateEditor', 'AGS3xIoTAdmin.view.artogis.ags3x.templateEditor.templateEditor');
    },

    // show Dashboard Editor panel
    showDashboardEditorPanel: function () {
        var me = this;
        if (document.getElementsByClassName('left-menu-parent-entry active').length > 0) {
            document.getElementsByClassName('left-menu-parent-entry active')[0].classList.remove('active');
        }
        document.getElementById('menuOptionShowDashboardEditor').classList.add('active');
        me.performContentExecution('ags3xDashboardEditor', 'AGS3xIoTAdmin.view.artogis.ags3x.dashboardEditor.dashboardEditor');
    },

    performContentExecution: function (contentTarget, contentTargetClass) {
        console.log('contentPanelController.performContentExecution - contentTarget: ', contentTarget);
        console.log('contentPanelController.performContentExecution - contentTargetClass: ', contentTargetClass);
        var me = this;

        var contentPanelBoxesList = document.getElementsByClassName('contentPanelBox');
        console.log('contentPanelController.performContentExecution - contentPanelBoxesList: ', contentPanelBoxesList);

        var contentPanelBoxesListLength = contentPanelBoxesList.length;
        console.log('contentPanelController.performContentExecution - contentPanelBoxesListLength: ', contentPanelBoxesListLength);

        for (var i = 0; i < contentPanelBoxesListLength; i++) {
            var contentPanelBox = contentPanelBoxesList[i];
            console.log('contentPanelController.performContentExecution - contentPanelBox: ', contentPanelBox);
            console.log('contentPanelController.performContentExecution - contentPanelBox.id: ', contentPanelBox.id);

            if (contentPanelBox.id !== contentTarget) {
                console.log('contentPanelController.performContentExecution - hiding contentPanelBox');
                contentPanelBox.style.display = 'none';
            }
            else if (contentPanelBox.id === contentTarget) {
                console.log('contentPanelController.performContentExecution - showing contentPanelBox');
                contentPanelBox.style.display = 'block';

                
                var panelToRelayout = Ext.ComponentQuery.query('#' + contentPanelBox.id)[0];
                console.log('contentPanelController.performContentExecution - panelToRelayout: ', panelToRelayout);

                panelToRelayout.update();

                /*
                var panelToRelayoutViewModel = panelToRelayout.getViewModel();
                var panelToRelayoutViewModelType = panelToRelayoutViewModel.type;
                console.log('contentPanelController.performContentExecution - panelToRelayoutViewModel: ', panelToRelayoutViewModel);
                console.log('contentPanelController.performContentExecution - panelToRelayoutViewModelType: ', panelToRelayoutViewModelType);

                var view = AGS3xIoTAdmin.getApplication().getView(panelToRelayoutViewModelType);
                console.log('contentPanelController.performContentExecution - view: ', view);
                view.updateLayout();*/

                //var panelToRelayoutView = panelToRelayout.getView();
                //console.log('contentPanelController.performContentExecution - panelToRelayoutView: ', panelToRelayoutView);

                //panelToRelayoutView.doLayout()
            }
            else {
                console.log('contentPanelController.performContentExecution - ERROR, no id?');
            }
        }

        if (navigator.userAgent.indexOf('MSIE') !== -1 || navigator.appVersion.indexOf('Trident/') > 0) {
            var evt = document.createEvent('UIEvents');
            evt.initUIEvent('resize', true, false, window, 0);
            window.dispatchEvent(evt);
        }
        else {
            window.dispatchEvent(new Event('resize'));
        }

        /*
        var contentPanelBoxesList = Ext.ComponentQuery.query('panel[cls=contentPanelBox]');
        console.log('contentPanelController.performContentExecution - contentPanelBoxesList: ', contentPanelBoxesList);

        var contentPanelBoxesListLength = contentPanelBoxesList.length;
        console.log('contentPanelController.performContentExecution - contentPanelBoxesListLength: ', contentPanelBoxesListLength);

        for (var i = 0; i < contentPanelBoxesListLength; i++) {
            var contentPanelBox = contentPanelBoxesList[i];
            console.log('contentPanelController.performContentExecution - contentPanelBox: ', contentPanelBox);
            console.log('contentPanelController.performContentExecution - contentPanelBox.id: ', contentPanelBox.id);

            if (contentPanelBox.id !== contentTarget) {
                console.log('contentPanelController.performContentExecution - hiding contentPanelBox');
                contentPanelBox.hide();
            }
            else if (contentPanelBox.id === contentTarget) {
                console.log('contentPanelController.performContentExecution - showing contentPanelBox');
                contentPanelBox.show();
            }
            else {
                console.log('contentPanelController.performContentExecution - ERROR, no id?');
            }
        }
        */

        if (Ext.ComponentQuery.query('#' + contentTarget).length == 0) {
            me.contentPanel = null;

            me.contentPanel = Ext.create(contentTargetClass);
            console.log('contentPanelController.performContentExecution - contentPanel after creation');

            me.view.add(this.contentPanel);
            console.log('contentPanelController.performContentExecution - contentPanel added');
        }

        console.log('contentPanelController.performContentExecution - zebra');
    },


    /**
     * Called when the view is created
     */
    init: function () {
        AGS3xIoTAdmin.util.events.on('showContentPanel', this.showContentPanel, this);
        AGS3xIoTAdmin.util.events.on('showDataRobotsPanel', this.showDataRobotsPanel, this); // start load of this is defined in AGS3xIoTAdmin.view.artogis.ags3x.authorization.api
        AGS3xIoTAdmin.util.events.on('showFullSensorsListPanel', this.showFullSensorsListPanel, this);
        AGS3xIoTAdmin.util.events.on('showObjectEditorPanel', this.showObjectEditorPanel, this);
        AGS3xIoTAdmin.util.events.on('showComponentEditorPanel', this.showComponentEditorPanel, this);
        AGS3xIoTAdmin.util.events.on('showTemplateEditorPanel', this.showTemplateEditorPanel, this);
        AGS3xIoTAdmin.util.events.on('showDashboardEditorPanel', this.showDashboardEditorPanel, this);
        
    }
});