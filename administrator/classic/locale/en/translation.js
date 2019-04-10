// Translation - English (en)

var language = 'en';
var path = 'AGS3xIoTAdmin.locale.' + language + '.translation';



//////////////////////////////////////////////
// APPLICATION                              //
//////////////////////////////////////////////

Ext.define(path, {
    override: 'AGS3xIoTAdmin.Application',

    textApplicationUpdateAvailableTitle: 'Application Update',
    textApplicationUpdateAvailable: 'This application has an update. Reload?',

    boolTypeYes: 'Yes',
    boolTypeNo: 'No'
});



//////////////////////////////////////////////
// LEFT PANEL MENU                          //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.leftPanel.leftPanel',

    fieldLabelLeftMenuTitle: 'Menu',
    fieldLabelLeftMenuDashboardEditor: 'Dashboards',
    fieldLabelLeftMenuRobot: 'Robots',
    fieldLabelLeftMenuDataIO: 'Data IO',
    fieldLabelLeftMenuObjectTypes: 'Object types',
    fieldLabelLeftMenuComponentTypes: 'Component types',
    fieldLabelLeftMenuFullSensorsList: 'Full sensors list',
    fieldLabelLeftMenuTemplateEditor: 'Templates'
});



//////////////////////////////////////////////
// DASHBOARD CONFIG                         //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.dashboardEditor.dashboardEditor',

    fieldLabelDashboardEditorHeaderTitle: 'Dashboard configuration',
    fieldLabelLoadDashboardPlaceholder: 'Select a dashboard...',
    fieldLabelDashboardName: 'Name',
    fieldLabelFetchSensorData: 'Fetch sensor data',
    fieldLabelShowUrl: 'Show URL',
    fieldLabelDeleteDashboard: 'Delete',
    fieldLabelStartNewDashboard: 'Start new',
    fieldLabelSaveDashboard: 'Save',

    textWelcomeDashboardScreen: 'Select an existing dashboard from the dropdown menu<br>or start a new dashboard by pressing the button "Start new"'

});

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.dashboardEditor.dashboardEditorController',
    
    fieldLabelNewCardWindowTitle: 'Add new data card',

    fieldLabelNewCardSource: 'Data source',
    fieldLabelNewCardSensorId: 'Sensor ID',
    fieldLabelNewCardName: 'Name from data source',
    fieldLabelNewCardDescription: 'Description',
    fieldLabelNewCardSensorObjectDescription: 'Sensor object description',
    fieldLabelNewCardId: 'ID',
    fieldLabelNewCardUnit: 'Unit',

    fieldLabelNewCardTypePlaceholder: 'Select data card type...',
    fieldLabelNewCardCalculationTypePlaceholder: 'Select data card calculation type...',
    fieldLabelNewCardAggregationTypePlaceholder: 'Select data card aggregation type...',
    fieldLabelButtonCreate: 'Create new data card',

    fieldLabelAddNewCard: 'ADD CARD',

    textMessageDashboardSaved: 'Dashboard has been saved',
    errorTextDashboardNotSaved: 'Dashboard has not been saved',
    errorTextDashboardNoTitle: 'Please enter a dashboard title',
    textMessageDashboardDeleted: 'Dashboard has been deleted',


    hoverTextRemoveCardFromDashboard: 'Remove data card from dashboard',
    hoverTextAssignBatterySensor: 'Apply battery sensor',
    hoverTextAssignSignalSensor: 'Apply signal sensor',
    hoverTextEditSettings: 'Edit sensor name and unit',
    hoverTextMoveCardLeft: 'Move data card left',
    hoverTextMoveCardRight: 'Move data card right',

    fieldLabelDeleteDashboardTitle: 'Delete data card',
    fieldLabelDeleteDashboardYes: 'Yes',
    fieldLabelDeleteDashboardNo: 'No',

    fieldLabelDashboardUrlTitle: 'Dashboard URL link',
    fieldLabelParameters: 'Parameters',
    fieldLabelRemoveNavbar: 'Remove navigation bar',
    fieldLabelDashboardUrlCopyUrl: 'Copy link',
    fieldLabelDashboardUrlOpenUrl: 'Open link',

    fieldLabelSetBatteryTitle: 'Designate sensor battery',
    fieldLabelButtonSetBattery: 'Select battery',
    fieldLabelButtonUnselectBattery: 'Set no battery',
    emptyT4extSelectConnectivityType: 'Select connectivity type...',

    fieldLabelSetSignalTitle: 'Designate sensor signal strength source',
    fieldLabelButtonSetSignal: 'Select sensor signal strength source',
    fieldLabelButtonUnselectSignal: 'Set no sensor signal strength source',

    fieldLabelCardSettingsTitle: 'Configure data card settings',
    fieldLabelCardSettingsName: 'Name',
    fieldLabelCardSettingsUnit: 'Unit',
    fieldLabelUseOriginalUnit: 'Use original unit',
    fieldLabelCardSettingsShowLastRun: 'Show last run',
    fieldLabelCardSettingsAccept: 'Accept',
    fieldLabelCardSettingsCancel: 'Cancel',

    fieldLabelNoLastRunDefined: 'show last run disabled',
    fieldLabelNoBatterySensorDefined: 'no battery sensor selected'
});


//////////////////////////////////////////////
// DATA ROBOTS                              //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.dataRobots.dataRobots',

    fieldLabelRobotsLeftPanelHeaderTitle: 'Robot monitor',
    fieldLabelRobotsButtonFullscreen: 'Fullscreen mode',
    fieldLabelRobotsButtonClearRobots: 'Turn robot monitor off',
    fieldLabelRobotsRightPanelHeaderTitle: 'Robot data',
    fieldLabelRobotsButtonDevices: 'Devices',
    fieldLabelRobotsButtonSensors: 'Sensors',
    fieldLabelRobotsDevicesGridTitle: 'Devices',
    fieldLabelRobotsDevicesDeviceId: 'Data source',
    fieldLabelRobotsDevicesName: 'Name',
    fieldLabelRobotsAliasName: 'Name/alias',
    fieldLabelRobotsDevicesDescription: 'Description',
    fieldLabelRobotsDevicesLocation: 'Location',
    fieldLabelRobotsDevicesRobotId: 'Robot ID',
    fieldLabelRobotsSensorsGridTitle: 'Sensors',
    fieldLabelRobotsSensorsType: 'Sensor type',
    fieldLabelRobotsSensorsInterval: 'Interval',
    fieldLabelRobotsSensorsLastRun: 'Last run',
    fieldLabelRobotsSensorsDataId: 'Sensor ID',
    fieldLabelRobotsSensorsSensorId: 'Device ID',
    fieldLabelRobotsSensorsName: 'Name',
    fieldLabelRobotsSensorsAliasName: 'Alias',
    fieldLabelRobotsSensorsDescription: 'Description',
    fieldLabelRobotsSensorsUnit: 'Unit',
    fieldLabelRobotsSensorsLocation: 'Location',
    fieldLabelRobotsSensorsLastValue: 'Last value',
    fieldLabelRecordButtonSave: 'Save',
    fieldLabelRecordButtonCancel: 'Cancel'
});

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.dataRobots.dataRobotsController',

    textRobotsSensorsStatusRunning: 'Sensor is running as expected.',
    textRobotsSensorsStatusStopped: 'Sensor has stopped. Check for any errors or scheduled restarts.',
    textRobotsSensorsStatusUnknown: 'Sensor status is unknown.',
    textRobotsSensorsStatusWarning: 'Sensor is running, but the intervals are not met. Check sensor for errors.',

    textRobotsFullscreenInfo: 'Disable fullscreen mode by pressing the<br><br><b>spacebar key</b>'
});



//////////////////////////////////////////////
// DATA IO                                  //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.dataIO.dataIO',

    fieldLabelDataHeaderTitle: 'Data',
    fieldLabelUnconfiguredTitle: 'Sensors',
    fieldLabelUnconfiguredSource: 'Data Robot',
    fieldLabelUnconfiguredSensorId: 'Device ID',
    fieldLabelUnconfiguredName: 'Device name',
    fieldLabelUnconfiguredNodeType: 'Sensor type',
    fieldLabelUnconfiguredDescription: 'Description',
    fieldLabelUnconfiguredSensorObjectDescription: 'Sensor object description',
    fieldLabelUnconfiguredId: 'Sensor ID',
    fieldLabelUnconfiguredUnit: 'Unit',

    fieldLabelCalculationsTitle: 'Calculations',
    fieldLabelCalculationsCalculation: 'Calculation',
    fieldLabelCalculationsFormula: 'Formula',
    fieldLabelCalculationsData: 'Data',
    fieldLabelCalculationsUnit: 'Unit',
    fieldLabelCalculationsStore: 'Store',
    fieldLabelCalculationsAggregationAndStore: 'Aggregation and store',
    fieldLabelCalculationsAverageTwoMinutes: 'Average 2 min.',
    fieldLabelCalculationsAverageSixtyMinutes: 'Average 60 min.',
    fieldLabelCalculationsSumSixtyMinutes: 'Sum 60. min.',
    fieldLabelCalculationsOpenFormulaEditor: 'Open formula editor',
    fieldLabelCalculationsOpenModel: 'Open calculcation model',
    fieldLabelCalculationsPlaceholder: 'Please select a template…',
    fieldLabelRecordButtonSave: 'Save',
    fieldLabelRecordButtonCancel: 'Cancel',

    fieldLabelConfiguredTitle: 'Configured data',
    fieldLabelConfiguredSensorId: 'Sensor ID',
    fieldLabelConfiguredName: 'Name',
    fieldLabelConfiguredDescription: 'Description',
    fieldLabelConfiguredAlias: 'Alias',
    fieldLabelConfiguredMeasurementName: 'Measurement name',

    fieldLabelObjectCompTitle: 'Object or component',
    fieldLabelObjectCompType: 'Type',
    fieldLabelObjectCompName: 'Name',
    fieldLabelObjectCompId: 'ID',

    fieldLabelButtonSave: 'Save',
    fieldLabelButtonClear: 'Clear',
    fieldLabelButtonRefreshData: 'Refresh data',
    fieldLabelButtonGoToTop: 'Go to top',
    fieldLabelButtonResetDataIO: 'Reset sensor',

    fieldLabelButtonCancelComponentMove: 'Cancel component move',

    textMaskCalculations: 'No calculation template has been selected',
    textMaskConfiguredData: 'No unconfigured data has been selected',
    textMaskObjectsAndComponents: 'No component or object has been selected',

    fieldLabelMapHeaderTitle: 'Map',
    fieldLabelMapSearch: 'Search',
    fieldLabelMapLayers: 'Layers',
    //fieldLabelMapInfoWindowOf: 'of',
    fieldLabelMapInfoWindowType: 'Type',
    fieldLabelMapInfoWindowName: 'Name',
    fieldLabelMapInfoWindowId: 'ID',
    fieldLabelMapInfoWindowButtonClose: 'Close',
    fieldLabelMapInfoWindowButtonAccept: 'Accept',

    configuredTypesShowAllSensors: 'Show all sensors',
    configuredTypesShowConfiguredSensors: 'Only show configured sensors',
    configuredTypesShowUnconfiguredSensors: 'Only show unconfigured sensors',

    errorTextConfiguredDataNotSaved: 'Configured data could not be saved.'
});

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.dataIO.dataIOController',

    fieldLabelDataHeaderTitle: 'Data',
    fieldLabelUnconfiguredTitle: 'Unconfigured data',
    fieldLabelUnconfiguredSource: 'Data source',
    fieldLabelUnconfiguredSensorId: 'Sensor ID',
    fieldLabelUnconfiguredName: 'Name from data source',
    fieldLabelUnconfiguredDescription: 'Description',
    fieldLabelUnconfiguredId: 'ID',
    fieldLabelUnconfiguredUnit: 'Unit',

    fieldLabelCalculationsTitle: 'Calculations',
    fieldLabelCalculationsCalculation: 'Calculation',
    fieldLabelCalculationsFormula: 'Formula',
    fieldLabelCalculationsData: 'Data',
    fieldLabelCalculationsUnit: 'Unit',
    fieldLabelCalculationsStore: 'Store',
    fieldLabelCalculationsAggregationAndStore: 'Aggregation and store',
    fieldLabelCalculationsAverageTwoMinutes: 'Average 2 min.',
    fieldLabelCalculationsAverageSixtyMinutes: 'Average 60 min.',
    fieldLabelCalculationsSumSixtyMinutes: 'Sum 60. min.',
    fieldLabelCalculationsPlaceholder: 'Please select a template…',

    fieldLabelCalculationsWindowTitle: 'Calculations',
    fieldLabelCalculationsCalculation: 'Calculation',
    fieldLabelCalculationsFormula: 'Formula',
    fieldLabelCalculationsType: 'Type',
    fieldLabelCalculationsData: 'Data',
    fieldLabelCalculationsUnit: 'Unit',
    fieldLabelCalculationsStore: 'Store',
    fieldLabelCalculationsAverageTwoMinutes: 'Average 2 min.',
    fieldLabelCalculationsAverageSixtyMinutes: 'Average 60 min.',
    fieldLabelCalculationsSumSixtyMinutes: 'Sum 60 min.',

    fieldLabelConfiguredTitle: 'Configured data',
    fieldLabelConfiguredSensorId: 'Sensor ID',
    fieldLabelConfiguredName: 'Name',
    fieldLabelConfiguredDescription: 'Description',
    fieldLabelConfiguredAlias: 'Alias',
    fieldLabelConfiguredMeasurementName: 'Measurement name',

    fieldLabelObjectCompType: 'Type',
    fieldLabelObjectCompName: 'Name',
    fieldLabelObjectCompDescription: 'Description',
    fieldLabelObjectCompId: 'ID',

    textMessageConfiguredDataSaved: 'Configured data has been successfully saved.',

    fieldLabelCalculation: 'Calculation',
    fieldLabelAggregate: 'Aggregate',
    fieldLabelUnit: 'Unit',
    fieldLabelInterval: 'Interval',
    fieldLabelButtonCalculationAccept: 'Accept',
    fieldLabelButtonCalculationCancel: 'Cancel',

    fieldLabelResetDataIOQuestion: 'Do you want to reset the selected sensor data?',
    fieldLabelResetDataIOYes: 'Yes',
    fieldLabelResetDataIOYes: 'No'
});



//////////////////////////////////////////////
// MAP                                      //
//////////////////////////////////////////////

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.map.mapController',

    fieldLabelMapInfoWindowOf: 'of',
    fieldLabelMapInfoWindowType: 'Type',
    fieldLabelMapInfoWindowName: 'Name',
    fieldLabelMapInfoWindowDescription: 'Description',
    fieldLabelMapInfoWindowId: 'ID',
    fieldLabelMapInfoWindowButtonClose: 'Close',
    fieldLabelMapInfoWindowButtonAccept: 'Accept',

    fieldLabelMapInfoWindowTitle: 'Objects and components',
    fieldLabelMapInfoWindowTitleObject: 'Map object',
    fieldLabelMapInfoWindowTitleObjects: 'Map objects',
    fieldLabelMapInfoWindowShowObjectComponents: 'Show components of object',
    fieldLabelMapInfoWindowAcceptMoveComponent: 'Execute move',
    fieldLabelMapInfoWindowCancelMoveComponent: 'Abort move',
    fieldLabelMapInfoWindowComponentsList: 'Components list',
    fieldLabelMapInfoWindowDetachComponent: 'Detach component',
    fieldLabelMapInfoWindowMoveComponent: 'Move component',
    fieldLabelMapInfoWindowNewComponent: 'Create component',
    fieldLabelMapInfoWindowNewCancel: 'Cancel',
    fieldLabelMapInfoWindowNewSelectIO: 'Select component of the Data IO',
    fieldLabelMapInfoWindowNewRobotName: 'Data Robot',
    fieldLabelMapInfoWindowNewDeviceName: 'Device name',
    fieldLabelMapInfoWindowNewDeviceID: 'Device ID',
    fieldLabelMapInfoWindowApply: 'Apply',
    fieldLabelMapInfoWindowCancel: 'Cancel',
    fieldLabelMapInfoWindowComponentType: 'Component type',
    fieldLabelMapInfoWindowChooseComponentType: 'Choose a component type...',
    fieldLabelMapInfoWindowNewComponentName: 'Name',
    fieldLabelMapInfoWindowNewComponentDescription: 'Description',
    fieldLabelMapInfoWindowNewComponentCreate: 'Create',

    textErrorMessageNoComponentName: 'Please enter a descriptive name for the component.',
    textErrorMessageNoComponentNameTitle: 'Error - New component',

    textMessageLoadingBaseMapLayer: 'mapController.createWMTSLayer - Loading base map layer. Please wait.',
    textMessageNoComponentsAttached: 'No components connected to object',
    textMessageComponentDetachedSuccess: 'Component has been detached from object',

    fieldLabelNewComponentWindowTitle: 'Create new component from device'
});

//////////////////////////////////////////////
// MAP SEARCH                               //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.mapSearch.mapSearch',

    fieldLabelMapSearch: 'Search'

});



//////////////////////////////////////////////
// MAP SEARCH RESULT                        //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.resultList.resultList',

    fieldLabelMapSearch: 'Search'

});



//////////////////////////////////////////////
// OBJECT EDITOR                         //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.objectEditor.objectEditor',
    fieldLabelObjectEditorTitle: 'Object types',
    fieldLabelObjectSelectDataStore: 'Select Data Store...',
    fieldLabelObjectAddObject: 'Add object',
    fieldLabelObjectRemoveObject: 'Remove object',
    fieldLabelObjectObjectListTitle: 'Object list',
    fieldLabelObjectRefreshData: 'Refresh data',
    fieldLabelObjectGoToTop: 'Go to top',

    columnHeaderObjectType: 'Type',
    columnHeaderObjectId: 'ID',
    columnHeaderObjectName: 'Name',
    columnHeaderObjectDataStoreId: 'Data Store',
    columnHeaderObjectZOrder: 'Layer index',
    columnHeaderObjectWfs: 'WFS URL',
    columnHeaderObjectWfsLayer: 'WFS layer name',
    columnHeaderObjectFieldId: 'Field ID',
    columnHeaderObjectFieldName: 'Field name',
    columnHeaderObjectFieldDescription: 'Field description',
    columnHeaderObjectObjectTableName: 'Object table name',
    columnHeaderObjectEditDate: 'Edit date',

    fieldLabelRecordButtonSave: 'Save',
    fieldLabelRecordButtonCancel: 'Cancel',

    fieldLabelDataLoading: 'Please wait...'
});

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.objectEditor.objectEditorController',
    fieldLabelRemoveObjectQuestion: 'Fjern objekt?',
    fieldLabelRemoveObjectYes: 'Yes',
    fieldLabelRemoveObjectNo: 'No',
    fieldLabelDataLoading: 'Please wait...'
});

//////////////////////////////////////////////
// COMPONENT EDITOR                         //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.componentEditor.componentEditor',
    fieldLabelComponentEditorTitle: 'Component types',
    fieldLabelComponentSelectDataStore: 'Select Data Store...',
    fieldLabelComponentAddComponent: 'Add component',
    fieldLabelComponentRemoveComponent: 'Remove component',
    fieldLabelComponentComponentListTitle: 'Component list',
    fieldLabelComponentRefreshData: 'Refresh data',
    fieldLabelComponentGoToTop: 'Go to top',

    columnHeaderComponentType: 'Type',
    columnHeaderComponentId: 'ID',
    columnHeaderComponentName: 'Name',
    columnHeaderComponentDataStoreId: 'Data Store',
    columnHeaderComponentWfs: 'WFS URL',
    columnHeaderComponentWfsLayer: 'WFS layer name',
    columnHeaderComponentFieldId: 'Field ID',
    columnHeaderComponentFieldDescription: 'Field description',
    columnHeaderComponentComponentTableName: 'Component table name',
    columnHeaderComponentEditDate: 'Edit date',

    fieldLabelRecordButtonSave: 'Save',
    fieldLabelRecordButtonCancel: 'Cancel',

    fieldLabelComponentComboboxName: 'Data Store',

    fieldLabelDataLoading: 'Please wait...'
});

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.componentEditor.componentEditorController',
    fieldLabelRemoveComponentQuestion: 'Remove component?',
    fieldLabelRemoveComponentYes: 'Yes',
    fieldLabelRemoveComponentNo: 'No',

    fieldLabelDataLoading: 'Please wait...'
});

//////////////////////////////////////////////
// TOC                                      //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.toc.toc',

    fieldLabelMapLayers: 'Layers',
    fieldLabelMapInfoWindowOf: 'of',
    fieldLabelMapInfoWindowType: 'Type',
    fieldLabelMapInfoWindowName: 'Name',
    fieldLabelMapInfoWindowId: 'ID',
    fieldLabelMapInfoWindowButtonClose: 'Close',
    fieldLabelMapInfoWindowButtonAccept: 'Accept'

});

// controller



//////////////////////////////////////////////
// COMPLETE SENSOR DATA LIST                //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.dataSensors.dataSensors',

    fieldLabelSensorsListHeaderTitle: 'Complete sensors list',
    fieldLabelSensorsListGridTitle: 'Sensor data',
    fieldLabelSensorsListStatus: 'Sensor status',
    fieldLabelSensorsListType: 'Sensor type',
    fieldLabelSensorsListInterval: 'Interval',
    fieldLabelSensorsListLastRun: 'Last run',
    fieldLabelSensorsListRobotId: 'Data Robot ID',
    fieldLabelSensorsListDataId: 'Sensor ID',
    fieldLabelSensorsListSensorId: 'Device ID',
    fieldLabelSensorsListName: 'Sensor name',
    fieldLabelSensorsListDescription: 'Sensor description',
    fieldLabelSensorsListUnit: 'Unit',
    fieldLabelSensorsListLocation: 'Location',
    fieldLabelSensorsListLastValue: 'Last value',
    fieldLabelButtonRefreshData: 'Refresh data',
    fieldLabelButtonToTop: 'Go to top',

    fieldLabelDataLoading: 'Vent venligst'
});

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.dataSensors.dataSensorsController',

    textRobotsSensorsStatusRunning: 'Sensor is running as expected.',
    textRobotsSensorsStatusStopped: 'Sensor has stopped. Check for any errors or scheduled restarts.',
    textRobotsSensorsStatusUnknown: 'Sensor status is unknown.',
    textRobotsSensorsStatusWarning: 'Sensor is running, but the intervals are not met. Check sensor for errors'

});




//////////////////////////////////////////////
// TEMPLATE EDITOR                          //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.templateEditor.templateEditor',

    fieldLabelTemplateEditorHeaderTitle: 'Template editor for calculations',
    fieldLabelTemplateEditorTitleExistingTemplates: 'Existing templates',
    fieldLabelTemplateEditorTitleConfiguration: 'Configuration',
    fieldLabelTemplateEditorTitleTemplate: 'Template',
    fieldLabelTemplateEditorTemplateName: 'Template name',
    fieldLabelTemplateEditorUnitInput: 'Unit for all',
    fieldLabelTemplateEditorUnitInputEmptyText: 'Select a unit type...',
    fieldLabelTemplateEditorMeasurementType: 'Measurement type',
    fieldLabelTemplateEditorMeasurementTypeEmptyText: 'Select a measurement type...',

    templateEditorMeasurementName: 'Name',

    fieldLabelTemplateEditorEditSave: 'Save',
    fieldLabelTemplateEditorEditCancel: 'Cancel',

    fieldLabelCalculations: 'Calculations',

    fieldLabelCalculation: 'Calculation',
    fieldLabelAggregate: 'Aggregation',
    fieldLabelUnit: 'Unit',
    fieldLabelScaleToUnit: 'Scale to unit',
    fieldLabelInterval: 'Interval',

    fieldLabelTemplateEditorDeleteTemplate: 'Delete template',
    fieldLabelTemplateEditorSaveAsNewTemplate: 'Save as new template',
    fieldLabelTemplateEditorUpdateTemplate: 'Update template',
    fieldLabelTemplateEditorStartNewTemplate: 'Start new template',

    fieldLabelDataLoading: 'Please wait...',

    optionYes: 'Yes',
    optionNo: 'No'
});

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.templateEditor.templateEditorController',

    fieldLabelCalculationsCalculation: 'Calculation',
    fieldLabelCalculationsFormula: 'Formula',
    fieldLabelCalculationsType: 'Type',
    fieldLabelCalculationsData: 'Data',
    fieldLabelCalculationsUnit: 'Unit',
    fieldLabelCalculationsStore: 'Save',
    fieldLabelCalculationsAverageTwoMinutes: 'Average 2 min.',
    fieldLabelCalculationsAverageSixtyMinutes: 'Average 60 min.',
    fieldLabelCalculationsSumSixtyMinutes: 'Sum 60 min.',
    fieldLabelScaleToUnit: 'Scale to unit',
    fieldLabelCalculationsOpenModel: 'Open calculation model',
    fieldLabelCalculationsOpenFormulaEditor: 'Open formula editor',
    fieldLabelCalculationsAggregationAndStore: 'Aggregation and storage',
    fieldLabelCalculationsWindowTitle: 'Calculations',

    fieldLabelDeleteTemplateQuestion: 'Delete template?',
    fieldLabelDeleteTemplateYes: 'Yes',
    fieldLabelDeleteTemplateNo: 'No',


    infoTextTemplateCreationSuccess: 'New template has been created',
    infoTextTemplateCreationError: 'Creation of new template failed',
    infoTextTemplateUpdateSuccess: 'Template has been updated',
    infoTextTemplateUpdateError: 'Update of template failed',
    infoTextTemplateMissingTemplateName: 'Please enter a template name',
    infoTextTemplateDuplicateNameError: 'A template with that name already exists',

    fieldLabelDataLoading: 'Please wait...'
});




//////////////////////////////////////////////
// FORMULA EDITOR                           //
//////////////////////////////////////////////

// view
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.formulaEditor.formulaEditor',

    fieldLabelFormulaEditorWindowTitle: 'Formula editor',
    fieldLabelFormulaEditorSavedFormulas: 'Saved formulas',
    fieldLabelFormulaEditorLoadFormula: 'Load formula',
    fieldLabelFormulaEditorFormula: 'Formula',
    fieldLabelFormulaEditorMathML: 'MathML',
    fieldLabelFormulaEditorJavaScriptString: 'JavaScript string',
    fieldLabelFormulaEditorUseFormula: 'Apply formula',
    fieldLabelFormulaEditorResult: 'Result &nbsp; = ',
    fieldLabelFormulaEditorInsertPi: 'Insert &Pi;',
    fieldLabelFormulaEditorInsertSquared: 'Insert &#8730;',
    fieldLabelFormulaEditorInsertPowerTwo: 'Insert x&#178;',
    fieldLabelFormulaEditorInsertPowerThree: 'Insert x&#179;',
    fieldLabelFormulaEditorInsertSensorVariable: 'Insert sensor variable',
    fieldLabelFormulaEditorSaveFormula: 'Save formula',
    fieldLabelFormulaEditorValidateFormula: 'Validate formula',
    fieldLabelFormulaEditorVariablesComponentsObjects: 'Variables from component or object',
    fieldLabelFormulaEditorInsertVariable: 'Insert variable into formula',
    fieldLabelFormulaEditorNameFormula: 'Name formula'

})

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.formulaEditor.formulaEditorController',

    fieldLabelFormulaEditorFormulaValidation: 'Enter sensor value',
    fieldLabelFormulaEditorNameFormulaPerformValidate: 'Validate',
    fieldLabelFormulaEditorNameFormulaCancelValidate: 'Cancel',
    fieldLabelFormulaEditorNameFormulaPerformValidateResult: 'Result'
});




//////////////////////////////////////////////
// RESULT LIST CONTROLLER                   //
//////////////////////////////////////////////

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.resultList.resultListController',

    deleteResultLbl: 'Delete result',
    deleteAllResultsLbl: 'Delete all results',
    clearMapSelectionLbl: 'Clear map selection'
});




//////////////////////////////////////////////
// MAP SEARCH CONTROLLER                    //
//////////////////////////////////////////////

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.mapSearch.mapSearchController',

    connErrMsg: 'Search not possible, please contact support. \n Reason: Server not available.',
    fieldLabelSearch: 'Search'
});



//////////////////////////////////////////////
// SYSTEM DATA                              //
//////////////////////////////////////////////

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.configuration.systemData',

    textMsgHeader: 'Load List of Sites',
    textErrorMsg: 'Error: List of Sites did not load'
});




//////////////////////////////////////////////
// SETTINGS                                 //
//////////////////////////////////////////////

// controller
Ext.define(path, {
    override: 'AGS3xIoTAdmin.view.artogis.ags3x.configuration.settings',

    textGeneralInitializeError: 'Settings for this application is not valid.',
    textBaseUrlMissing: 'baseUrl is missing',
    textNoMapConfigurationError: 'No map configuration has been provided in settings.json file.',
    textNoMapSearchConfigurationError: 'No MapSearch configuration has been provided in settings.json file.',
    textNoServiceUrlConfigurationError: 'No serviceUrl configuration has been provided in settings.json file.',
    textNoBasicUrlConfigurationError: 'No basicUrl configuration has been provided in settings.json.',
    textInitApplication: 'Initialize Application',
    textSettingsNotReadError: 'Settings for this application could not be read.',
    textNoVersionConfigurationError: 'No version number has been provided in settings.json file'
});