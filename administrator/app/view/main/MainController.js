/**
 * This class is the controller for the main view for the application. It is specified as
 * the "controller" of the Main view class.
 *
 * TODO - Replace this content of this view to suite the needs of your application.
 */
Ext.define('AGS3xIoTAdmin.view.main.MainController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.ags3xMain',

    performResize: function () {
        console.log('Resizing is in progress...');

        var windowInnerWidth = window.innerWidth;

        // refit all sensors content
        if (Ext.ComponentQuery.query('#fullSensorListColumnLeft').length > 0) {

            document.getElementById('fullSensorListColumnLeft_header').style.margin = '0 0 2px 2px';
            document.getElementById('fullSensorListColumnLeft_header').style.width = (window.innerWidth - 300 - 40 - 4) + 'px';

            document.getElementById('fullSensorListDataTable').style.margin = '20px 0 2px 2px';
            document.getElementById('fullSensorListDataTable').style.width = (window.innerWidth - 300 - 40 - 4) + 'px';
            Ext.ComponentQuery.query('#fullSensorListDataTable')[0].setHeight(window.innerHeight - 180);
        }

        // refit object editor
        if (Ext.ComponentQuery.query('#objectEditorDataTable').length > 0) {
            Ext.ComponentQuery.query('#objectEditorDataTable')[0].setHeight(window.innerHeight - 180 - 65 - 20);
        }

        // refit component editor
        if (Ext.ComponentQuery.query('#componentEditorDataTable').length > 0) {
            Ext.ComponentQuery.query('#componentEditorDataTable')[0].setHeight(window.innerHeight - 180 - 65 - 20);
        }

        var footerComponent = Ext.ComponentQuery.query('#menuVersionFooter')[0];

        // refit left menu footer
        var leftMenuButtonCount = Ext.ComponentQuery.query('#left-menu-parent-entry').length;
        var newFooterValue = window.innerHeight - (leftMenuButtonCount * 36) - 36 + 16;
        footerComponent.setY(newFooterValue);

        // refit dashboard editor
        if (Ext.ComponentQuery.query('#dashboardEditorBoard').length > 0) {
            // height
            var dashboardEditorBoard = Ext.ComponentQuery.query('#dashboardEditorBoard')[0];
            var newDashboardEditorBoardHeight = window.innerHeight - 174;
            dashboardEditorBoard.setHeight(newDashboardEditorBoardHeight);

            // width
            document.getElementById('dashboardEditorBoard-body').style.width = (window.innerWidth - 300 - 40 - 4) + 'px';

            // conditionals
            if (window.innerWidth < 1430) {
                document.getElementById('fullDashboardEditorPanel_header-title').style.display = 'none';
            }
            else {
                document.getElementById('fullDashboardEditorPanel_header-title').style.display = 'block';
                document.getElementById('fullDashboardEditorPanel_header-title').style.top = '4px';
            }
        }

        // refit template editor
        var templateEditorAggregationsAndStoresGridPanel = Ext.ComponentQuery.query('#templateEditorAggregationsAndStoresGridPanel')[0];
        var templateEditorStatusGridPanel = Ext.ComponentQuery.query('#templateEditorStatusGridPanel')[0];

        if(Ext.ComponentQuery.query('#fullTemplateEditorPanelCenterHolder').length > 0) {
            if (windowInnerWidth <= 1280) {
                console.log('MainController.performResize - template editor refit - narrow: ', windowInnerWidth);

                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenter')[0].setWidth(windowInnerWidth - 670);

                Ext.ComponentQuery.query('#fullTemplateEditorPanelRight')[0].setWidth(windowInnerWidth - 670);

                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenterHolder')[0].setHeight(588);

                document.getElementById('fullTemplateEditorPanelRight-outerCt').style.width = '800px';

                document.getElementById('fullTemplateEditorPanelRight-body').style.overflowX = 'scroll';
                
            }
            else if (windowInnerWidth > 1280 && windowInnerWidth <= 1470) {
                console.log('MainController.performResize - template editor refit - medium: ', windowInnerWidth);

                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenter')[0].setWidth(windowInnerWidth - 670);

                Ext.ComponentQuery.query('#fullTemplateEditorPanelRight')[0].setWidth(windowInnerWidth - 670);

                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenterHolder')[0].setHeight(588);

                document.getElementById('fullTemplateEditorPanelRight-outerCt').style.width = '800px';

                document.getElementById('fullTemplateEditorPanelRight-body').style.overflowX = 'scroll';
            }
            else if (windowInnerWidth > 1470 && windowInnerWidth <= 1992) {
                console.log('MainController.performResize - template editor refit - wide: ', windowInnerWidth);               

                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenter')[0].setWidth(800);
                Ext.ComponentQuery.query('#fullTemplateEditorPanelRight')[0].setWidth(800);

                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenterHolder')[0].setHeight(588);

                document.getElementById('fullTemplateEditorPanelRight-body').style.overflowX = 'hidden';
            }
            else if (windowInnerWidth > 1992) {
                console.log('MainController.performResize - template editor refit - full: ', windowInnerWidth);

                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenter')[0].setWidth(500);
                Ext.ComponentQuery.query('#fullTemplateEditorPanelRight')[0].setWidth(800);

                Ext.ComponentQuery.query('#fullTemplateEditorPanelCenterHolder')[0].setHeight(304);
            }
        }

        if (window.devicePixelRatio) {
            console.log('MainController.performResize - window.devicePixelRatio: ', window.devicePixelRatio);
            console.log('MainController.performResize - window.innerWidth: ', window.innerWidth);
        }
    },
    init: function () {
        console.log('MainController.init...');
    }
});
