Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.dashboardEditor.dashboardEditorController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xDashboardEditor',

    requires: [
       'AGS3xIoTAdmin.util.events',
       'AGS3xIoTAdmin.util.util'
    ],

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelNewCardWindowTitle: 'Tilføj ny databoks',

    fieldLabelNewCardSource: 'DataRobot',
    fieldLabelNewCardDeviceAlias: 'Målernavn',
    fieldLabelNewCardDeviceName: 'Måler sub-navn',
    fieldLabelNewCardSensorId: 'Måler-ID',
    fieldLabelNewCardId: 'Sensor-ID',
    fieldLabelNewCardSensorType: 'Sensortype',
    fieldLabelNewCardUnit: 'Enhed',

    fieldLabelNewCardTypePlaceholder: 'Vælg databokstype...',
    fieldLabelNewCardCalculationTypePlaceholder: 'Vælg udregningstype...',
    fieldLabelNewCardAggregationTypePlaceholder: 'Vælg aggregeringstype...',
    fieldLabelButtonCreate: 'Opret ny databoks',

    fieldLabelAddNewCard: 'TILFØJ KORT',

    textMessageDashboardSaved: 'Dashboard er gemt',
    errorTextDashboardNotSaved: 'Dashboard blev ikke gemt',
    errorTextDashboardNoTitle: 'Indtast venligst en titel for dashboard\'et',
    textMessageDashboardDeleted: 'Dashboard er slettet',


    hoverTextRemoveCardFromDashboard: 'Fjern databoks fra dashboard',
    hoverTextAssignBatterySensor: 'Tilknyt batterisensor',
    hoverTextAssignSignalSensor: 'Tilknyt signalsensor',
    hoverTextEditSettings: 'Rediger sensornavn og værdienhed',
    hoverTextMoveCardLeft: 'Flyt databoks til venstre',
    hoverTextMoveCardRight: 'Flyt databoks til højre',

    fieldLabelDeleteDashboardTitle: 'Slet dashboard',
    fieldLabelDeleteDashboardYes: 'Ja',
    fieldLabelDeleteDashboardNo: 'Nej',

    fieldLabelDashboardUrlTitle: 'URL link til dashboard',
    fieldLabelParameters: 'Parametre',
    fieldLabelRemoveNavbar: 'Fjern topbjælke',
    fieldLabelDashboardUrlCopyUrl: 'Kopier link',
    fieldLabelDashboardUrlOpenUrl: 'Åbn link',

    fieldLabelSetBatteryTitle: 'Udpeg sensorens batteri',
    fieldLabelButtonSetBattery: 'Vælg batteri',
    fieldLabelButtonUnselectBattery: 'Vælg intet batteri',

    fieldLabelSetSignalTitle: 'Udpeg sensorens signalstyrkemåler',
    fieldLabelButtonSetSignal: 'Vælg signalstyrkemåler',
    fieldLabelButtonUnselectSignal: 'Vælg ingen signalstyrkemåler',
    emptyTextSelectConnectivityType: 'Vælg signaltype...',

    fieldLabelCardSettingsTitle: 'Konfigurer datakortindstillinger',
    fieldLabelCardSettingsName: 'Navn',
    fieldLabelCardSettingsUnit: 'Enhed',
    fieldLabelUseOriginalUnit: 'Brug originale enhed',
    fieldLabelCardSettingsShowLastRun: 'Vis sidste kørsel',
    fieldLabelCardSettingsAccept: 'Accepter',
    fieldLabelCardSettingsCancel: 'Fortryd',
    fieldLabelCardSettingsShowAlarmLimits: 'Vis alarmgrænser',
    fieldLabelCardSettingsMax: 'Max. værdi',
    fieldLabelCardSettingsMin: 'Min. værdi',

    fieldLabelNoLastRunDefined: 'sidste kørsel visning slået fra',
    fieldLabelNoBatterySensorDefined: 'ingen batterisensor valgt',

    // data
    cardTypes: {
        fields: [
            'name', 'id'
        ],
        data: [
            { 'name': 'Ikke-modificeret sensor værdi', 'id': 1 },
            { 'name': 'Graf - sensorværdier over tid', 'id': 2 }
        ]
    },
    batterySelectionTypes: {
        fields: [
            'name', 'id'
        ],
        data: [
            { 'name': 'Vis alle sensorer markeret som batterier', 'id': 1 },
            { 'name': 'Vis alle beslægtede sensorer', 'id': 2 },
            { 'name': 'Vis alle sensorer', 'id': 3 }
        ]
    },
    signalSelectionTypes: {
        fields: [
            'name', 'id'
        ],
        data: [
            { 'name': 'Vis alle sensorer markeret som signalstyrke', 'id': 1 },
            { 'name': 'Vis alle beslægtede konfigurerede sensorer', 'id': 2 }
        ]
    },
    signalConnectivityTypes: {
        fields: [
            'id', 'name'
        ],
        data: [
            { 'id': 1, 'name': 'Sigfox' },
            { 'id': 2, 'name': 'LoRa' },
            { 'id': 3, 'name': 'LoRaWAN' },
            { 'id': 4, 'name': 'NB-IoT' },
            { 'id': 5, 'name': 'LTE-M' }
        ]
    },

    serviceUrl: '',
    basicUrl: '',

    loadDashboardsData: function(selectLast) {
        var self = this;

        var activeDashboardDataStore = self.getViewModel().getStore('activeDashboardDataStore');
        var cardsStore = self.getViewModel().getStore('cardsStore');


        activeDashboardDataStore.setData([]);
        cardsStore.setData([]);

        var dashboardsUrl = self.serviceUrl + 'dashboard'

        Ext.Ajax.request({
            scope: this,
            url: dashboardsUrl,
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dashboardEditorController.loadDashboardsData - result: ', result);

                        var dashboardsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.dashboardsStore');
                        console.log('dashboardEditorController.loadDashboardsData - dashboardsStore, before: ', dashboardsStore);

                        var data = [];

                        var highestDashboardId = 0;
                        var lastRecord;

                        for (var i = 0; i < result.dashboards.length; i++) {
                            var item = result.dashboards[i];
                            var record = {
                                dashboardId: item.id,
                                title: item.title,
                                cards: item.cards,
                                url: item.url
                            };

                            data.push(record);
                            
                            if (highestDashboardId < record.dashboardId) {
                                console.log('LAST RECORD: ', record);
                                highestDashboardId = record.dashboardId;
                                lastRecord = record;
                            }
                        }

                        dashboardsStore.loadData(data);

                        if (selectLast == true) {
                            Ext.ComponentQuery.query('#comboboxLoadDashboard')[0].setValue(lastRecord.dashboardId);
                        }
                        
                    }
                    catch(exception) {
                        console.log('dashboardEditorController.loadDashboardsData - exception: ', exception);
                    }
                }
                else {
                    console.log('dashboardEditorController.loadDashboardsData - failure: ', response);
                }
            }
        });
    },
    loadDashboard: function (dashboardId) {
        var self = this;

        console.log('dashboardEditorController.loadDashboard -  dashboardId: ', dashboardId);

        Ext.ComponentQuery.query('#dashboardEditorStartText')[0].setVisible(false);

        if (dashboardId != null) {
            

            if(Ext.ComponentQuery.query('#dashboard-card-new').length > 0) {
                console.log('dashboardEditorController.loadDashboard - dashboard-card-new exists');
                Ext.ComponentQuery.query('#dashboard-card-new')[0].destroy();
            }

            var contentDiv = document.getElementById('dashboardEditorBoard-innerCt');

            while (contentDiv.firstChild) {
                contentDiv.removeChild(contentDiv.firstChild);
            }

            // temp get dashboard by ID
            var dashboard;
        
            var dashboardsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.dashboardsStore');
            console.log('dashboardEditorController.loadDashboard - dashboardsStore: ', dashboardsStore);

            var length = dashboardsStore.data.items.length;

            for (var d = 0; d < length; d++) {
                var dashboardCandidate = dashboardsStore.data.items[d].data;
                console.log('dashboardEditorController.loadDashboard - dashboardCandidate: ', dashboardCandidate);

                if (dashboardId == dashboardCandidate.dashboardId) {
                    dashboard = dashboardCandidate;
                    console.log('dashboardEditorController.loadDashboard - dashboard ' + dashboardCandidate.title + ', cards: ', dashboard.cards);

                    if (typeof dashboard.cards === 'string') {
                        dashboard.cards = Ext.decode(dashboard.cards); // convert cards value from string to JSON
                    }

                    // set dashboard in activeDashboardDataStore
                    var activeDashboardDataStore = self.getViewModel().getStore('activeDashboardDataStore');
                    activeDashboardDataStore.setData([dashboard]);
                    console.log('dashboardEditorController.loadDashboard - dashboard to select: ', dashboard);
                    console.log('dashboardEditorController.loadDashboard - activeDashboardDataStore: ', activeDashboardDataStore);

                    break;
                }
            }

            console.log('dashboardEditorController.loadDashboard - data as string: ' + JSON.stringify(dashboard));

            Ext.ComponentQuery.query('#textFieldDashboardName')[0].setDisabled(false);
            Ext.ComponentQuery.query('#textFieldDashboardName')[0].setValue(dashboard.title);

            var cardsStore = self.getViewModel().getStore('cardsStore');
            cardsStore.setData([]);

            for (var i = 0; i < dashboard.cards.length; i++) {
                var card = dashboard.cards[i];
                card.tempId = AGS3xIoTAdmin.util.util.generateUUID();
                cardsStore.add(card);
            }

            self.loadCards();

            // enable buttons
            Ext.ComponentQuery.query('#buttonDeleteDashboard')[0].setDisabled(false);
            Ext.ComponentQuery.query('#buttonSaveDashboard')[0].setDisabled(false);
            Ext.ComponentQuery.query('#buttonGetDashboardUrl')[0].setDisabled(false);
            Ext.ComponentQuery.query('#buttonLoadSensorData')[0].setDisabled(false);
        }
    },
    addNewCard: function () {
        var self = this;

        console.log('dashboardEditorController.addNewCard');

        var configuredDataTempStore = AGS3xIoTAdmin.util.util.getConfiguredDataAsStore();
        //var configuredDataTempStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.configuredDataStore');
        console.log('dashboardEditorController.addNewCard - configuredDataTempStore: ', configuredDataTempStore);

        var unconfiguredDataUrl = self.serviceUrl + 'unconfigureddataio';

        self.newCardWindow = new Ext.Window({
            id: 'newCardWindow',
            header: {
                title: self.fieldLabelNewCardWindowTitle,
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important'
                }
            },
            closable: true,
            width: 800,
            height: 700,
            x: ((window.innerWidth - 800) / 2),
            y: 100,
            border: false,
            frame: false,
            modal: true,
            maskClickAction: 'destroy',
            renderTo: document.body,
            style: {
                'text-align': 'center',
                'border': '0 !important',
                'border-radius': '0px !important'
            },
            bodyStyle: {
                'background': '#ffffff !important'
            },
            layout: 'border',
            items: [
                {
                    xtype: 'gridpanel',
                    id: 'sensorsListGridPanel',
                    height: 500,
                    region: 'center',
                    margin: 10,
                    store: configuredDataTempStore,
                    plugins: 'gridfilters',
                    style: {
                        'border': '1px solid #cfcfcf !important'
                    },

                    columns: [
                        { text: self.fieldLabelNewCardSource, dataIndex: 'dataSourceName', width: 175, filter: { type: 'string' } },
                        { text: self.fieldLabelNewCardDeviceAlias, dataIndex: 'sensorObjectAlias', width: 200, filter: { type: 'string' } },
                        { text: self.fieldLabelNewCardDeviceName, dataIndex: 'name', width: 200, filter: { type: 'string' } },
                        { text: self.fieldLabelNewCardSensorId, dataIndex: 'sensorId', flex: 1, filter: { type: 'string' } },
                        { text: self.fieldLabelNewCardId, dataIndex: 'dataId', width: 80, filter: { type: 'string' } },
                        { text: self.fieldLabelNewCardSensorType, dataIndex: 'nodeTypeString', flex: 1, filter: { type: 'string' } },
                    ],
                    listeners: {
                        itemclick: function (component, record, element, index) {
                            console.log('newCard - sensor click, component: ', component);
                            console.log('newCard - sensor click, record: ', record);
                            console.log('newCard - sensor click, element: ', element);
                            console.log('newCard - sensor click, index: ', index);

                            AGS3xIoTAdmin.util.events.fireEvent('sensorSelected', component, record);
                        }
                    }
                },
                {
                    xtype: 'panel',
                    id: 'newCardMiddleBox',
                    height: 100,
                    margin: '0 10px 0 10px',
                    region: 'south',
                    items: [
                        {
                            xtype: 'combobox',
                            id: 'comboboxNewCardType',
                            width: 200,
                            style: {
                                'float': 'left',
                                'margin-right': '10px'
                            },
                            store: self.cardTypes,
                            valueField: 'id',
                            displayField: 'name',
                            editable: false,
                            queryMode: 'local',
                            emptyText: self.fieldLabelNewCardTypePlaceholder,
                            tpl: Ext.create('Ext.XTemplate',
                                '<ul class="x-list-plain"><tpl for=".">',
                                    '<li role="option" class="x-boundlist-item">{name}</li>',
                                '</tpl></ul>'
                            ),
                            disabled: true,
                            listeners: {
                                change: function (a, b, c, d) {
                                    console.log('Header combobox change - a: ', a);
                                    console.log('Header combobox - disabled: ', a.disabled);

                                    if (a.disabled == false) {
                                        AGS3xIoTAdmin.util.events.fireEvent('cardTypeSelected', a);
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'combobox',
                            id: 'comboboxNewCardCalculationType',
                            width: 200,
                            style: {
                                'float': 'left',
                                'margin-right': '10px'
                            },
                            store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.calculationTypesStore'),
                            valueField: 'id',
                            displayField: 'name',
                            editable: false,
                            queryMode: 'local',
                            emptyText: self.fieldLabelNewCardCalculationTypePlaceholder,
                            tpl: Ext.create('Ext.XTemplate',
                                '<ul class="x-list-plain"><tpl for=".">',
                                    '<li role="option" class="x-boundlist-item">{name}</li>',
                                '</tpl></ul>'
                            ),
                            disabled: true,
                            listeners: {
                                change: function (a, b, c, d) {
                                    console.log('dashboardEditorController.cardCalculationType - change - a: ', a);
                                    console.log('dashboardEditorController.cardCalculationType - disabled: ', a.disabled);

                                    if (a.disabled == false) {
                                        AGS3xIoTAdmin.util.events.fireEvent('cardCalculationTypeSelected', a);
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'combobox',
                            id: 'comboboxNewCardAggregationType',
                            width: 200,
                            style: {
                                'float': 'left',
                                'margin-right': '10px'
                            },
                            store: Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.aggregationTypesStore'),
                            valueField: 'id',
                            displayField: 'name',
                            editable: false,
                            queryMode: 'local',
                            emptyText: self.fieldLabelNewCardAggregationTypePlaceholder,
                            tpl: Ext.create('Ext.XTemplate',
                                '<ul class="x-list-plain"><tpl for=".">',
                                    '<li role="option" class="x-boundlist-item">{name} ({minutes} min.)</li>',
                                '</tpl></ul>'
                            ),
                            disabled: true,
                            listeners: {
                                change: function (a, b, c, d) {
                                    console.log('dashboardEditorController.cardAggregationType - change - a: ', a);
                                    console.log('dashboardEditorController.cardAggregationType - disabled: ', a.disabled);

                                    if (a.disabled == false) {
                                        AGS3xIoTAdmin.util.events.fireEvent('cardAggregationTypeSelected', a);
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'newCardCreateButton',
                            disabled: true,
                            text: self.fieldLabelButtonCreate,
                            iconCls: 'fa fa-floppy-o',
                            style: {
                                'float': 'left',
                                'margin-right': '10px'
                            },
                            listeners: {
                                click: function () {
                                    AGS3xIoTAdmin.util.events.fireEvent('buttonCreateNewCardClicked');
                                }
                            }
                        }
                    ]
                }
            ]
        }).show();

    },
    processSensorSelected: function (component, record) {
        var self = this;

        console.log('dashboardEditorController.processSensorSelected - component: ', component);
        console.log('dashboardEditorController.processSensorSelected - record: ', record);

        if( Ext.ComponentQuery.query('#sensorsListGridPanel')[0].getSelection().length > 0 ) {
            console.log('dashboardEditorController.processSensorSelected - selection active');

            Ext.ComponentQuery.query('#comboboxNewCardType')[0].setDisabled(false);
            Ext.ComponentQuery.query('#comboboxNewCardCalculationType')[0].setDisabled(false);
            Ext.ComponentQuery.query('#comboboxNewCardAggregationType')[0].setDisabled(false);
        }
        else {
            Ext.ComponentQuery.query('#comboboxNewCardType')[0].setDisabled(true);
            Ext.ComponentQuery.query('#comboboxNewCardCalculationType')[0].setDisabled(true);
            Ext.ComponentQuery.query('#comboboxNewCardAggregationType')[0].setDisabled(true);
        }
    },
    processCardTypeSelected: function(component) {
        var self = this;

        console.log('dashboardEditorController.processCardTypeSelected - component: ', component);

        if (Ext.ComponentQuery.query('#comboboxNewCardCalculationType')[0].getValue() != null && Ext.ComponentQuery.query('#comboboxNewCardAggregationType')[0].getValue() != null) {
            Ext.ComponentQuery.query('#newCardCreateButton')[0].setDisabled(false);
        }
    },
    processCardCalculationTypeSelected: function (component) {
        var self = this;

        console.log('dashboardEditorController.processCardCalculationTypeSelected - component: ', component);

        if (Ext.ComponentQuery.query('#comboboxNewCardType')[0].getValue() != null && Ext.ComponentQuery.query('#comboboxNewCardAggregationType')[0].getValue() != null) {
            Ext.ComponentQuery.query('#newCardCreateButton')[0].setDisabled(false);
        }
    },
    processCardAggregationTypeSelected: function (component) {
        var self = this;

        console.log('dashboardEditorController.processCardAggregationTypeSelected - component: ', component);

        if (Ext.ComponentQuery.query('#comboboxNewCardCalculationType')[0].getValue() != null && Ext.ComponentQuery.query('#comboboxNewCardType')[0].getValue() != null) {
            Ext.ComponentQuery.query('#newCardCreateButton')[0].setDisabled(false);
        }
    },

    createNewCard: function() {
        var self = this;

        console.log('dashboardEditorController.createNewCard');

        var selectedRecord = Ext.ComponentQuery.query('#sensorsListGridPanel')[0].getSelection()[0].data;
        console.log('dashboardEditorController.createNewCard - selectedRecord: ', selectedRecord);

        var cardType = Ext.ComponentQuery.query('#comboboxNewCardType')[0].getValue();
        var cardCalcuationType = Ext.ComponentQuery.query('#comboboxNewCardCalculationType')[0].getValue();
        var cardAggregationType = Ext.ComponentQuery.query('#comboboxNewCardAggregationType')[0].getValue();

        var newCardsCollection = [];
        var cardsStore = self.getViewModel().getStore('cardsStore');
        console.log('dashboardEditorController.createNewCard - cardsStore before add: ', cardsStore);

        for (var i = 0; i < cardsStore.data.items.length; i++) {
            var existingCard = cardsStore.data.items[i].data;
            existingCard.index = i;
            delete existingCard.id;
            console.log('dashboardEditorController.createNewCard - existingCard: ', existingCard);
            newCardsCollection.push(existingCard);
        }

        var card = {
            'title': selectedRecord.description,
            'index': newCardsCollection.length,
            'sensorId': selectedRecord.dataId,
            'unit': selectedRecord.unit,
            'cardType': cardType,
            'cardCalcuationType': cardCalcuationType,
            'cardAggregationType': cardAggregationType,
            'tempId': AGS3xIoTAdmin.util.util.generateUUID()
        };

        newCardsCollection.push(card);

        console.log('dashboardEditorController.createNewCard - newCardsCollection: ', newCardsCollection);

        cardsStore.clearData();
        cardsStore.add(newCardsCollection);

        console.log('dashboardEditorController.createNewCard - cardsStore after add: ', cardsStore);

        self.loadCards();

        self.newCardWindow.destroy();
    },
    saveDashboard: function(component) {
        var self = this;

        console.log('dashboardEditorController.saveDashboard - component: ', component);

        var activeDashboardDataStore = self.getViewModel().getStore('activeDashboardDataStore');
        console.log('dashboardEditorController.saveDashboard - activeDashboardDataStore: ', activeDashboardDataStore);

        var cardsStore = self.getViewModel().getStore('cardsStore');
        console.log('dashboardEditorController.saveDashboard - cardsStore: ', cardsStore);

        var cardsArray = [];

        for (var i = 0; i < cardsStore.data.items.length; i++ ) {
            var card = cardsStore.data.items[i].data;
            var sensorData = AGS3xIoTAdmin.util.util.getSensorFromStore(card.sensorId, self.getViewModel().getStore('unconfiguredSensorsDataStore'));

            if (card.hasOwnProperty('unit') == false) {
                console.log('dashboardEditorController.saveDashboard - check available unit for card: ', card);           
                console.log('dashboardEditorController.saveDashboard - check available unit for sensor: ', sensorData);
                card.unit = sensorData.unit;
            }

            cardsArray.push(card);
        }
        console.log('dashboardEditorController.saveDashboard - cardsArray: ', cardsArray);

        var title = Ext.ComponentQuery.query('#textFieldDashboardName')[0].getValue();
        console.log('dashboardEditorController.saveDashboard - title: ', title);

        if (title == null || title.length == 0) {
            AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.errorTextDashboardNoTitle, 'ERROR');
        }
        else {
            
            // UPDATE
            if (activeDashboardDataStore.data.items.length > 0 && activeDashboardDataStore.data.items[0].data.dashboardId != null) {
                var params = {
                    cards: JSON.stringify(cardsArray),
                    id: activeDashboardDataStore.data.items[0].data.dashboardId,
                    title: title
                }

                console.log('dashboardEditorController.saveDashboard (update) - params: ', params);

                var saveDashboardUrl = self.serviceUrl + 'dashboard';

                // UPDATE
                Ext.Ajax.request({
                    url: saveDashboardUrl,
                    method: 'PUT',
                    jsonData: Ext.JSON.encode(params),
                    activeDashboardId: activeDashboardDataStore.data.items[0].data.dashboardId,
                    cardsArray: cardsArray,
                    scope: this,
                    callback: function (options, success, response) {
                        if (success) {
                            console.log('dashboardEditorController.saveDashboard (PUT - update) - successful save!');
                            console.log('dashboardEditorController.saveDashboard (PUT - update) - response: ', response);

                            AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.textMessageDashboardSaved, 'INFO');

                            // TBD - update dashboard object in editor
                            var dashboardsStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.dashboardsStore');
                            var dashboardsStoreLength = dashboardsStore.data.items.length;
                            for (var i = 0; i < dashboardsStoreLength; i++) {
                                var dashboard = dashboardsStore.data.items[i].data;

                                if (options.activeDashboardId == dashboard.dashboardId) {
                                    console.log('dashboardEditorController.saveDashboard - dashboard to update: ', dashboard);
                                    dashboard.cards = options.cardsArray;
                                }
                            }
                        }
                        else {
                            AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.errorTextDashboardNotSaved, 'ERROR');
                            console.log('dashboardEditorController.saveDashboard (PUT - update) - error response: ', response);
                        }
                    }
                }, this);
            }
                // CREATE NEW
            else {
                var params = {
                    cards: JSON.stringify(cardsArray),
                    title: title
                }

                // web service creates URL value and returns it
                console.log('dashboardEditorController.saveDashboard (create) - params: ', params);

                var saveDashboardUrl = self.serviceUrl + 'dashboard';

                // CREATE
                Ext.Ajax.request({
                    url: saveDashboardUrl,
                    method: 'POST',
                    jsonData: Ext.JSON.encode(params),
                    scope: this,
                    callback: function (options, success, response) {
                        if (success) {
                            console.log('dashboardEditorController.saveDashboard (POST - create) - successful save!');
                            console.log('dashboardEditorController.saveDashboard (POST - create) - response: ', response);

                            try {
                                var result = Ext.decode(response.responseText);
                                console.log('dashboardEditorController.saveDashboard (POST - create) - result: ', result);

                                AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.textMessageDashboardSaved, 'INFO');

                                // reload dashboards, select newly created
                                self.loadDashboardsData(true);
                            }
                            catch(exception) {
                                console.log('dashboardEditorController.saveDashboard (POST - create) - exception: ', exception);
                            }
                        }
                        else {
                            AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.errorTextDashboardNotSaved, 'ERROR');
                            console.log('dashboardEditorController.saveDashboard (POST - create) - error response: ', response);
                        }
                    }
                }, this);
            }
            
        }

        
    },
    createNewDashboard: function() {
        var self = this;

        console.log('dashboardEditorController.createNewDashboard');

        Ext.ComponentQuery.query('#dashboardEditorStartText')[0].setVisible(false);

        var activeDashboardDataStore = self.getViewModel().getStore('activeDashboardDataStore');
        activeDashboardDataStore.setData([]);

        var cardsStore = self.getViewModel().getStore('cardsStore');
        cardsStore.setData([]);

        if (Ext.ComponentQuery.query('#dashboard-card-new').length > 0) {
            Ext.ComponentQuery.query('#dashboard-card-new')[0].destroy();
        }

        var contentDiv = document.getElementById('dashboardEditorBoard-innerCt');

        while (contentDiv.firstChild) {
            contentDiv.removeChild(contentDiv.firstChild);
        }

        var dashboardNewCard = Ext.create('Ext.panel.Panel', {
            renderTo: Ext.get('dashboardEditorBoard-innerCt'),
            xtype: 'panel',
            id: 'dashboard-card-new',
            cls: 'dashboard-card-new',
            html: '<div class="dashboard-card-new-inner"><div class="dashboard-card-new-text">' + self. fieldLabelAddNewCard + '</div><div class="dashboard-card-new-icon fa fa-plus-square-o"></div></div>',
            listeners: {
                render: function (component) {
                    component.body.on('click', function () {
                        Ext.getCmp('ags3xDashboardEditor').getController('AGS3xIoTAdmin.view.artogis.ags3x.dashboardEditor.dashboardEditorController').addNewCard();
                    });
                },
                scope: this
            }
        });

        

        Ext.ComponentQuery.query('#comboboxLoadDashboard')[0].setValue(null);

        Ext.ComponentQuery.query('#textFieldDashboardName')[0].setDisabled(false);
        Ext.ComponentQuery.query('#textFieldDashboardName')[0].setValue(null);
        Ext.ComponentQuery.query('#textFieldDashboardName')[0].focus();

        Ext.ComponentQuery.query('#buttonSaveDashboard')[0].setDisabled(false);

        console.log('dashboardEditorController.createNewDashboard - end');
    },
    openDeleteDashboardDialog: function (component) {
        var self = this;

        self.dashboardDeletionWindow = Ext.create('Ext.window.Window', {
            id: 'dashboardDeletionWindow',
            header: {
                title: self.fieldLabelDeleteDashboardTitle,
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important',
                    'padding': '12px'
                }
            },
            closable: true,
            layout: 'fit',
            width: 210,
            padding: 10,
            x: ((window.innerWidth - 210) / 2),
            y: ((window.innerHeight - 400) / 2),
            layout: 'fit',
            frame: false,
            modal: true,
            maskClickAction: 'destroy',
            renderTo: document.body,
            items: [
                {
                    xtype: 'panel',
                    items: [
                        {
                            xtype: 'button',
                            text: self.fieldLabelDeleteDashboardYes,
                            style: {
                                'float': 'right'
                            },
                            width: 90,
                            heigh: 25,
                            listeners: {
                                click: function () {
                                    self.dashboardDeletionWindow.destroy();
                                    self.deleteDashboard()
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            text: self.fieldLabelDeleteDashboardNo,
                            style: {
                                'float': 'left'
                            },
                            width: 90,
                            heigh: 25,
                            listeners: {
                                click: function () {
                                    self.dashboardDeletionWindow.destroy();
                                }
                            }

                        }
                    ]
                }
            ]
        }).show();
    },
    deleteDashboard: function () {
        var self = this;

        var activeDashboardDataStore = self.getViewModel().getStore('activeDashboardDataStore');
        
        var dashboardId = activeDashboardDataStore.data.items[0].data.dashboardId;

        var deleteDashboardUrl = self.serviceUrl + 'dashboard/' + dashboardId;

        // CREATE
        Ext.Ajax.request({
            url: deleteDashboardUrl,
            method: 'DELETE',
            scope: this,
            callback: function (options, success, response) {
                if (success) {
                    console.log('dashboardEditorController.deleteDashboard - successful deletion!');
                    console.log('dashboardEditorController.deleteDashboard - response: ', response);

                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dashboardEditorController.deleteDashboard - result: ', result);

                        self.resetDashboardEditor();

                        AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.textMessageDashboardDeleted, 'INFO');

                        Ext.ComponentQuery.query('#dashboardEditorStartText')[0].setVisible(true);
                    }
                    catch (exception) {
                        console.log('dashboardEditorController.saveDashboard (POST - create) - exception: ', exception);
                    }
                }
                else {
                    AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.errorTextDashboardNotSaved, 'ERROR');
                    console.log('dashboardEditorController.saveDashboard (POST - create) - error response: ', response);
                }
            }
        }, this);
    },
    showUrl: function() {
        var self = this;

        // hide all overlays
        var overlaysToHide = document.getElementsByClassName('dashboard-card-inner-overlay');
        for (var i = 0; i < overlaysToHide.length; i++) {
            overlaysToHide[i]['style']['display'] = 'none';
        }

        var activeDashboardDataStore = self.getViewModel().getStore('activeDashboardDataStore');
        var dashboardId = activeDashboardDataStore.data.items[0].data.dashboardId;

        self.dashboardUrlWindow = Ext.create('Ext.window.Window', {
            id: 'dashboardUrlWindow',
            header: {
                title: self.fieldLabelDashboardUrlTitle,
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important',
                    'padding': '12px'
                }
            },
            closable: true,
            layout: 'fit',
            width: 700,
            padding: 10,
            x: ((window.innerWidth - 700) / 2),
            y: 200,
            layout: 'fit',
            frame: false,
            modal: true,
            maskClickAction: 'destroy',
            renderTo: document.body,
            items: [
                {
                    xtype: 'panel',
                    id: 'dashboardUrlWindowLinkBox',
                    width: 700,
                    height: 70,
                   
                    items: [
                        {
                            xtype: 'panel',
                            html: '<div class="dashboard-url-link-box" style="float:left;height:36px;width:100%;border:1px solid rgb(200,200,200);line-height:36px;text-indent:10px;">' + self.dashboardUrl + '?id=' + dashboardId + '</div>',
                            width: '100%'
                        },
                        {
                            xtype: 'panel',
                            region: 'center',
                            items: [
                                {
                                    xtype: 'button',
                                    id: 'buttonCopyUrl',
                                    text: self.fieldLabelDashboardUrlCopyUrl,
                                    style: {
                                        'float': 'right',
                                        'margin-top': '6px',
                                        'margin-left': '10px'
                                    },
                                    handler: function (component) {
                                        AGS3xIoTAdmin.util.events.fireEvent('buttonCopyUrlClicked', component);
                                    }
                                },
                                {
                                    xtype: 'button',
                                    id: 'buttonOpenUrl',
                                    text: self.fieldLabelDashboardUrlOpenUrl,
                                    style: {
                                        'float': 'right',
                                        'margin-top': '6px',
                                        'margin-left': '10px'
                                    },
                                    handler: function (component) {
                                        AGS3xIoTAdmin.util.events.fireEvent('buttonOpenUrlClicked', component);
                                    }
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'dashboardUrlWindowParametresBox',
                    width: 700,
                    height: 100,
                    items: [
                        {
                            
                            xtype: 'fieldcontainer',
                            fieldLabel: self.fieldLabelParameters,
                            defaultType: 'checkboxfield',
                            items: [
                                {
                                    boxLabel  : self.fieldLabelRemoveNavbar,
                                    name: 'fullscreen',
                                    cls: 'dashboard-url-parameter',
                                    inputValue: '1',
                                    id: 'checkboxFullscreen',
                                    handler: function (component) {
                                        console.log('checkboxFullscreen changed');
                                        AGS3xIoTAdmin.util.events.fireEvent('checkboxDashboardUrlParameterChanged', component);
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        }).show();
    },
    resetDashboardEditor: function() {
        var self = this;

        console.log('dashboardEditorController.resetDashboardEditor');

        var activeDashboardDataStore = self.getViewModel().getStore('activeDashboardDataStore');
        var cardsStore = self.getViewModel().getStore('cardsStore');

        activeDashboardDataStore.setData([]);
        cardsStore.setData([]);

        if (Ext.ComponentQuery.query('#dashboard-card-new').length > 0) {
            console.log('dashboardEditorController.loadDashboard - dashboard-card-new exists');
            Ext.ComponentQuery.query('#dashboard-card-new')[0].destroy();
        }

        var contentDiv = document.getElementById('dashboardEditorBoard-innerCt');

        while (contentDiv.firstChild) {
            contentDiv.removeChild(contentDiv.firstChild);
        }

        Ext.ComponentQuery.query('#comboboxLoadDashboard')[0].setValue(null);

        Ext.ComponentQuery.query('#textFieldDashboardName')[0].setValue(null);
        Ext.ComponentQuery.query('#textFieldDashboardName')[0].setDisabled(true);

        Ext.ComponentQuery.query('#buttonLoadSensorData')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonGetDashboardUrl')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonDeleteDashboard')[0].setDisabled(true);
        Ext.ComponentQuery.query('#buttonSaveDashboard')[0].setDisabled(true);
    },
    setDashboardUrlParameters: function (component) {
        var self = this;

        console.log('dashboardEditorController.setDashboardUrlParameters');

        var activeDashboardDataStore = self.getViewModel().getStore('activeDashboardDataStore');
        var dashboardId = activeDashboardDataStore.data.items[0].data.dashboardId;

        var basicDashboardUrl = self.basicUrl + '/dashboard/?id=' + dashboardId;

        var dashboardUrlParametersElements = document.getElementsByClassName('dashboard-url-parameter');
        for (var i = 0; i < dashboardUrlParametersElements.length; i++ ) {
            var checkboxInput = dashboardUrlParametersElements[i].getElementsByTagName('input')[0];
            console.log('dashboardEditorController.setDashboardUrlParameters - input: ', checkboxInput, ', value: ', checkboxInput.checked);

            if( checkboxInput.checked == true ) {
                basicDashboardUrl += '&fullscreen=true'
            }
        }

        document.getElementsByClassName('dashboard-url-link-box')[0].innerHTML = basicDashboardUrl;
    },
    copyDashboardUrl: function(component) {
        var self = this;

        var copyText = document.getElementsByClassName('dashboard-url-link-box')[0].innerHTML;
        copyText = copyText.replace(/&amp;/g, '&');

        console.log('dashboardEditorController.copyDashboardUrl - copyText: ', copyText);

        var dummy = document.createElement("textarea");
        document.body.appendChild(dummy);
        dummy.value = copyText;
        dummy.select();
        document.execCommand("copy");
        document.body.removeChild(dummy);

        document.execCommand("copy");
    },
    openDashboardUrl: function (component) {
        var self = this;

        var url = document.getElementsByClassName('dashboard-url-link-box')[0].innerHTML;
        
        window.open(url, "_blank");
    },
    removeCardFromDashboard: function(component) {
        var self = this;

        // hide all overlays
        var overlaysToHide = document.getElementsByClassName('dashboard-card-inner-overlay');
        for (var i = 0; i < overlaysToHide.length; i++) {
            overlaysToHide[i]['style']['display'] = 'none';
        }

        var tempIdToDelete = component.tempId;

        console.log('dashboardEditorController.removeCardFromDashboard - component: ', component);

        var elementToDelete = document.getElementById(component.id);
        elementToDelete.parentNode.removeChild(elementToDelete);

        var cardsStore = self.getViewModel().getStore('cardsStore');

        for (var i = 0; i < cardsStore.data.items.length; i++) {
            var card = cardsStore.data.items[i].data;
            console.log('dashboardEditorController.removeCardFromDashboard - card: ', card);

            if (tempIdToDelete == card.tempId) {
                console.log('dashboardEditorController.removeCardFromDashboard - DELETE card: ', card);
                cardsStore.removeAt(i, 1);
            }
        }

        console.log('dashboardEditorController.removeCardFromDashboard - cardsStore after removal: ', cardsStore);
    },
    reorderCardOnDashboard: function(component, direction) {
        var self = this;

        console.log('dashboardEditorController.reorderCardOnDashboard - component: ', component);
        console.log('dashboardEditorController.reorderCardOnDashboard - direction: ', direction);

        var tempIdToReorder = component.tempId;

        var cardsStore = self.getViewModel().getStore('cardsStore');

        var newSortedCardsArray = [];
        var cardsStoreSize = cardsStore.data.items.length;
        var oldIndex;
        var newIndex;

        for (var i = 0; i < cardsStore.data.items.length; i++) {
            var card = cardsStore.data.items[i].data;

            if (card.tempId == component.tempId) {
                oldIndex = i;
                if( direction == 'forward' ) {
                    if (oldIndex == (cardsStoreSize - 1)) {
                        newIndex = 0;
                    }
                    else {
                        newIndex = oldIndex + 1;
                    }
                }
                else if (direction == 'backward') {
                    if (oldIndex > 0) {
                        newIndex = oldIndex - 1
                    }
                    else {
                        newIndex = cardsStoreSize - 1;
                    }
                }
            }

            delete card.id;
            newSortedCardsArray.push(card);
        }

        console.log('dashboardEditorController.reorderCardOnDashboard - before splice insert, newSortedCardsArray: ', newSortedCardsArray);

        newSortedCardsArray.splice(newIndex, 0, newSortedCardsArray.splice(oldIndex, 1)[0]);

        // update index values
        for (var i = 0; i < newSortedCardsArray.length; i++) {
            newSortedCardsArray[i].index = i;
        }

        cardsStore.setData(newSortedCardsArray);

        console.log('dashboardEditorController.reorderCardOnDashboard - after splice insert, newSortedCardsArray: ', newSortedCardsArray);

        console.log('dashboardEditorController.reorderCardOnDashboard - oldIndex: ', oldIndex);
        console.log('dashboardEditorController.reorderCardOnDashboard - newIndex: ', newIndex);

        var cardsStore = self.getViewModel().getStore('cardsStore');
        cardsStore.setData(newSortedCardsArray);

        self.loadCards();
    },
    loadCards: function() {
        var self = this;

        var cardsStore = self.getViewModel().getStore('cardsStore');

        console.log('dashboardEditorController.loadCards - cardsStore: ', cardsStore);

        if (Ext.ComponentQuery.query('#dashboard-card-new').length > 0) {
            console.log('dashboardEditorController.loadCards - dashboard-card-new exists');
            Ext.ComponentQuery.query('#dashboard-card-new')[0].destroy();
        }

        var contentDiv = document.getElementById('dashboardEditorBoard-innerCt');

        while (contentDiv.firstChild) {
            contentDiv.removeChild(contentDiv.firstChild);
        }

        for (var i = 0; i < cardsStore.data.items.length; i++) {
            var card = cardsStore.data.items[i].data;
            console.log('dashboardEditorController.loadCards - card to build: ', card);

            var unitValue = (card.hasOwnProperty('unit')) ? card.unit : '';

            var percentageBarWidth = 160;
            var percentageWidth = Math.ceil((percentageBarWidth / 100) * card.batteryLevel);
            var color = '#00b500'; // default, green
            if (card.batteryLevel <= 50 && card.batteryLevel > 20) {
                color = '#e4e61c'; // yellow
            }
            else if (card.batteryLevel <= 20) {
                color = '#cd3739'; // red
            }

            var lastRunDisplayValue = 'block;'
            var noLastRunDisplayValue = 'none';
            if (!card.hasOwnProperty('lastRunVisible') || card.lastRunVisible == false) {
                lastRunDisplayValue = 'none';
                noLastRunDisplayValue = 'block';
            }

            var batteryDisplayValue = 'block'
            var noBatteryDisplayValue = 'none';
            if (!card.hasOwnProperty('batterySensorId')) {
                batteryDisplayValue = 'none';
                noBatteryDisplayValue = 'block';
            }

            var dashboardBatteryButtonDisplayValue = 'none';
            if (self.batteryAvailableCheck(card.sensorId) == true && card.cardType != 2) {
                dashboardBatteryButtonDisplayValue = 'block';
            }

            var signalDisplayValue = 'block'
            var noSignalDisplayValue = 'none';
            if (!card.hasOwnProperty('signalSensorId')) {
                signalDisplayValue = 'none';
                noSignalDisplayValue = 'block';
            }

            var marginLeft = 0;
            if (batteryDisplayValue == 'block' && signalDisplayValue == 'block' ) {
                marginLeft = 25;
            }
            if (batteryDisplayValue == 'block' && signalDisplayValue == 'none') {
                marginLeft = 90;
            }
            if (batteryDisplayValue == 'none' && signalDisplayValue == 'block') {
                marginLeft = 68;
            }

            var cardContent = '<div class="dashboard-card-inner" data-tempid="' + card.tempId + '" data-sensorid="' + card.sensorId + '" data-batterysensorid="' + card.batterySensorId + '" data-signalstrengthsensorid="' + card.signalSensorId + '">' +
            '<div class="dashboard-card-title">' + card.title + '</div>';

            // RAW VALUE
            if (card.cardType == 1) {
                cardContent +=
                '<div class="dashboard-card-value">...</div>' +
                '<div class="dashboard-card-unit">' + unitValue + '</div>';
            }

            // LINE CHART
            if (card.cardType == 2) {
                cardContent += '<div class="dashboard-card-line-chart"></div>';
            }

            // add lower settings
            if (card.cardType == 1) {
                cardContent +=
                // lastrun
                '<div class="dashboard-card-last-run-box" style="display:' + lastRunDisplayValue + ';">' +
                    '<div class="dashboard-last-run-icon fa fa-clock-o"></div>' +
                    '<div class="dashboard-last-run-value">...</div>' +
                '</div>' +
                '<div class="dashboard-card-no-last-run-box" style="display:' + noLastRunDisplayValue + ';">' + self.fieldLabelNoLastRunDefined + '</div>' +

                '<div class="auxiliary-data-holder" style="margin-left:' + marginLeft + 'px;">' +
                    // battery
                    '<div class="dashboard-card-battery-box" style="display:' + batteryDisplayValue + ';">' +

                        '<div class="dashboard-battery-value">XX%</div>' +
                        '<div class="dashboard-battery-icon fa fa-battery-half"></div>' +
                    '</div>' +

                    // signal strength
                    '<div class="dashboard-card-signal-box" style="display:' + signalDisplayValue + ';">' +
                        '<div class="dashboard-signal-icon fa fa-wifi"></div>' +
                        '<div class="dashboard-signal-value">XX dBm</div>' +
                    '</div>' +
                '</div>';
            }

            cardContent += '</div>';

            cardContent += '<div class="dashboard-card-inner-overlay">' +
                '<div class="dashboard-card-inner-overlay-toolbar">' +
                    '<div class="dashboard-button dashboard-delete fa fa-trash" title="' + self.hoverTextRemoveCardFromDashboard + '"></div>' +
                    '<div class="dashboard-button dashboard-battery fa fa-battery-half" title="' + self.hoverTextAssignBatterySensor + '" style="display:' + dashboardBatteryButtonDisplayValue + ';"></div>' +
                    '<div class="dashboard-button dashboard-signal fa fa-wifi" title="' + self.hoverTextAssignSignalSensor + '" style="display:' + dashboardBatteryButtonDisplayValue + ';"></div>' +
                    '<div class="dashboard-button dashboard-settings fa fa-cog" title="' + self.hoverTextEditSettings + '"></div>' +
                '</div>' +
                '<div class="dashboard-button dashboard-move dashboard-move-index-backward fa fa-arrow-left" title="' + self.hoverTextMoveCardLeft + '"></div>' +
                '<div class="dashboard-button dashboard-move dashboard-move-index-forward fa fa-arrow-right" title="' + self.hoverTextMoveCardRight + '"></div>' +
            '</div>';

            var dashboardCard = Ext.create('Ext.panel.Panel', {
                renderTo: Ext.get('dashboardEditorBoard-innerCt'),
                xtype: 'panel',
                tempId: card.tempId,
                cls: 'dashboard-card',
                html: cardContent,
                listeners: {
                    render: function (component) {
                        console.log('Rendered component: ', component);

                        var panelId = component.id;
                        var dashboardCardInner = document.getElementById(panelId).getElementsByClassName('dashboard-card-inner')[0];
                        var dashboardCardInnerOverlay = document.getElementById(panelId).getElementsByClassName('dashboard-card-inner-overlay')[0];
                        var setBatteryButton = dashboardCardInnerOverlay.getElementsByClassName('dashboard-battery')[0];
                        var setSignalButton = dashboardCardInnerOverlay.getElementsByClassName('dashboard-signal')[0];
                        var removeCardButton = dashboardCardInnerOverlay.getElementsByClassName('dashboard-delete')[0];
                        var editSettingsButton = dashboardCardInnerOverlay.getElementsByClassName('dashboard-settings')[0];
                        var moveCardForwardButton = dashboardCardInnerOverlay.getElementsByClassName('dashboard-move-index-forward')[0];
                        var moveCardBackwardButton = dashboardCardInnerOverlay.getElementsByClassName('dashboard-move-index-backward')[0];

                        setBatteryButton.addEventListener('click', function (event) {
                            console.log('card - set battery on click, event: ', event);
                            AGS3xIoTAdmin.util.events.fireEvent('buttonSetBatteryClicked', component, card.sensorId);
                        });

                        setSignalButton.addEventListener('click', function (event) {
                            console.log('card - set signal on click, event: ', event);
                            AGS3xIoTAdmin.util.events.fireEvent('buttonSetSignalClicked', component, card.sensorId);
                        });

                        removeCardButton.addEventListener('click', function (event) {
                            console.log('card - remove on click, event: ', event);
                            AGS3xIoTAdmin.util.events.fireEvent('buttonRemoveCardFromDashboardClicked', component);
                        });

                        editSettingsButton.addEventListener('click', function (event) {
                            console.log('card - edit settings on click, event: ', event);
                            AGS3xIoTAdmin.util.events.fireEvent('buttonEditSettingsClicked', component);
                        });

                        moveCardForwardButton.addEventListener('click', function (event) {
                            console.log('card - dashboard-move-index-forward, on click, event: ', event);
                            AGS3xIoTAdmin.util.events.fireEvent('buttonReorderCardOnDashboardClicked', component, 'forward');
                        });

                        moveCardBackwardButton.addEventListener('click', function (event) {
                            console.log('card - dashboard-move-index-backward, on click, event: ', event);
                            AGS3xIoTAdmin.util.events.fireEvent('buttonReorderCardOnDashboardClicked', component, 'backward');
                        });

                        dashboardCardInner.addEventListener('mouseover', function (event) {
                            //console.log('card - mouseover, event: ', event);
                            dashboardCardInnerOverlay['style']['display'] = 'block';
                        });

                        dashboardCardInnerOverlay.addEventListener('mouseleave', function (event) {
                            // hide overlays
                            dashboardCardInnerOverlay['style']['display'] = 'none';

                            var overlaysToHide = document.getElementsByClassName('dashboard-card-inner-overlay');
                            for (var i = 0; i < overlaysToHide.length; i++) {
                                overlaysToHide[i]['style']['display'] = 'none';
                            }
                            
                        });
                    },
                    scope: this
                }
            });
        }

        var dashboardNewCard = Ext.create('Ext.panel.Panel', {
            renderTo: Ext.get('dashboardEditorBoard-innerCt'),
            xtype: 'panel',
            id: 'dashboard-card-new',
            cls: 'dashboard-card-new',
            html: '<div class="dashboard-card-new-inner"><div class="dashboard-card-new-text">' + self.fieldLabelAddNewCard + '</div><div class="dashboard-card-new-icon fa fa-plus-square-o"></div></div>',
            listeners: {
                render: function (component) {
                    component.body.on('click', function () {
                        Ext.getCmp('ags3xDashboardEditor').getController('AGS3xIoTAdmin.view.artogis.ags3x.dashboardEditor.dashboardEditorController').addNewCard();
                    });
                },
                scope: this
            }
        });
    },
    batteryAvailableCheck: function (sensorId) {
        var self = this;

        console.log('dashboardEditorController.batteryAvailableCheck - sensorId: ', sensorId);

        var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');
        var relatedSensorsList = AGS3xIoTAdmin.util.util.getRelatedSensors(sensorId, unconfiguredDataStore);

        console.log('dashboardEditorController.batteryAvailableCheck - unconfiguredDataStore: ', unconfiguredDataStore);
        console.log('dashboardEditorController.batteryAvailableCheck - relatedSensorsList: ', relatedSensorsList);

        if (relatedSensorsList.length > 0) {
            return true;
        }
        else {
            return false;
        }
    },
    openSetBatteryWindow: function (component) {
        var self = this;

        var sensorId = Number(document.querySelectorAll('[data-tempid="' + component.tempId + '"]')[0].getAttribute('data-sensorid'));

        // hide all overlays
        var overlaysToHide = document.getElementsByClassName('dashboard-card-inner-overlay');
        for (var i = 0; i < overlaysToHide.length; i++) {
            overlaysToHide[i]['style']['display'] = 'none';
        }

        console.log('dashboardEditorController.openSetBatteryWindow - component: ', component);
        console.log('dashboardEditorController.openSetBatteryWindow - sensorId: ', sensorId);

        var unconfiguredDataUrl = self.serviceUrl + 'unconfigureddataio';

        // Load sensors list for viewing
        var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');
        var relatedSensorsList = AGS3xIoTAdmin.util.util.getRelatedSensors(sensorId, unconfiguredDataStore);

        var sensorsDataStore = this.getViewModel().getStore('sensorsDataStore');
        var batterySensorsDataStore = this.getViewModel().getStore('batterySensorsDataStore');

        var data = [];
        var batteryData = [];

        for (var i = 0; i < relatedSensorsList.length; i++) {
            var item = relatedSensorsList[i];

            console.log('dashboardEditorController.openSetBatteryWindow - battery item: ', item);

            var record = {
                dataId: item.dataId,
                dataSourceId: item.dataSourceId,
                sensorObjectId: item.sensorId,
                sensorObjectNodeId: item.sensorObjectNodeId,
                sensorObjectAlias: item.sensorObjectAlias,
                dataSourceName: item.dataSourceName,
                name: item.name,
                description: item.description,
                sensorObjectDescription: item.sensorObjectDescription,
                unit: item.unit,
                configured: item.configured,
                nodeTypeString: item.nodeTypeString
            };

            data.push(record)
            batteryData.push(record);
        }

        sensorsDataStore.setData(data);
        batterySensorsDataStore.setData(batteryData);

        console.log('dashboardEditorController.openSetBatteryWindow -sensorsDataStore: ', sensorsDataStore);
        console.log('dashboardEditorController.openSetBatteryWindow -batterySensorsDataStore: ', batterySensorsDataStore);

        self.setBatteryWindow = new Ext.Window({
            id: 'setBatteryWindow',
            tempId: component.tempId,
            sensorId: sensorId,
            header: {
                title: self.fieldLabelSetBatteryTitle,
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important'
                }
            },
            closable: true,
            width: 500,
            height: 300,
            x: ((window.innerWidth - 500) / 2),
            y: 100,
            border: false,
            frame: false,
            modal: true,
            maskClickAction: 'destroy',
            renderTo: document.body,
            style: {
                'text-align': 'center',
                'border': '0 !important',
                'border-radius': '0px !important'
            },
            bodyStyle: {
                'background': '#ffffff !important'
            },
            layout: 'border',
            items: [
                {
                    xtype: 'gridpanel',
                    id: 'batteriesListGridPanel',
                    height: 300,
                    region: 'center',
                    margin: 10,
                    store: this.getViewModel().getStore('batterySensorsDataStore'),
                    plugins: 'gridfilters',
                    style: {
                        'border': '1px solid #cfcfcf !important'
                    },
                    viewConfig: {
                        stripeRows: false,
                        getRowClass: function (record) {

                            switch (record.get('configured')) {
                                case true:
                                    return 'configured-true';
                                case false:
                                    return 'configured-false';
                                default:
                                    return '  x-grid-row';
                            }
                        }
                    },
                    columns: [
                        {
                            text: 'Status',
                            dataIndex: 'configured',
                            width: 60,
                            renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                                switch (value) {
                                    case true:
                                        return '<div class="fa fa-check configured-status-content" data-statusvalue="' + value + '"></div>';
                                    case false:
                                        return '<div class="fa fa-close configured-status-content" data-statusvalue="' + value + '"></div>';
                        
                                    default:
                                        return '';
                                }
                            }
                        },
                        { text: self.fieldLabelNewCardId, dataIndex: 'dataId', width: 100, filter: { type: 'string' } },
                        { text: self.fieldLabelNewCardSensorType, dataIndex: 'nodeTypeString', flex: 1, filter: { type: 'string' } },
                        { text: self.fieldLabelNewCardUnit, dataIndex: 'unit', width: 60, filter: { type: 'string' } }
                    ],
                    listeners: {
                        itemclick: function (component, record, element, index) {
                            console.log('newCard - sensor click, component: ', component);
                            console.log('newCard - sensor click, record: ', record);
                            console.log('newCard - sensor click, element: ', element);
                            console.log('newCard - sensor click, index: ', index);

                            AGS3xIoTAdmin.util.events.fireEvent('batterySelected', record);
                        }
                    }
                },
                {
                    xtype: 'panel',
                    id: 'setBatteryMiddleBox',
                    height: 50,
                    margin: '0 10px 0 10px',
                    region: 'south',
                    items: [
                        {
                            xtype: 'button',
                            id: 'buttonSelectBattery',
                            disabled: true,
                            text: self.fieldLabelButtonSetBattery,
                            iconCls: 'fa fa-check',
                            style: {
                                'float': 'right'
                            },
                            listeners: {
                                click: function (component) {
                                    AGS3xIoTAdmin.util.events.fireEvent('buttonSelectBatteryClicked', self.tempId);
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'buttonUnselectBattery',
                            disabled: false,
                            text: self.fieldLabelButtonUnselectBattery,
                            iconCls: 'fa fa-trash',
                            style: {
                                'float': 'right',
                                'margin-right': '10px'
                            },
                            listeners: {
                                click: function (component) {
                                    AGS3xIoTAdmin.util.events.fireEvent('buttonUnselectBatteryClicked', self.tempId);
                                }
                            }
                        }
                    ]
                }
            ]
        }).show();

        // hide overlays
        var overlaysToHide = document.getElementsByClassName('dashboard-card-inner-overlay');
        for (var i = 0; i < overlaysToHide.length; i++) {
            overlaysToHide[i]['style']['display'] = 'none';
        }
            
    },
    openSetSignalWindow: function (component) {
        var self = this;

        var sensorId = Number(document.querySelectorAll('[data-tempid="' + component.tempId + '"]')[0].getAttribute('data-sensorid'));
        console.log('dashboardEditorController.openSetSignalWindow - component: ', component);
        console.log('dashboardEditorController.openSetSignalWindow - component.tempId: ', component.tempId);
        console.log('dashboardEditorController.openSetSignalWindow - sensorId: ', sensorId);

        // hide all overlays
        var overlaysToHide = document.getElementsByClassName('dashboard-card-inner-overlay');
        for (var i = 0; i < overlaysToHide.length; i++) {
            overlaysToHide[i]['style']['display'] = 'none';
        }

        var sensorsDataStore = this.getViewModel().getStore('sensorsDataStore');
        var signalSensorsDataStore = this.getViewModel().getStore('signalSensorsDataStore');

        var data = [];
        var signalData = [];

        var unconfiguredDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore');
        var relatedSensorsList = AGS3xIoTAdmin.util.util.getRelatedSensors(sensorId, unconfiguredDataStore);

        console.log('dashboardEditorController.openSetSignalWindow - relatedSensorsList: ', relatedSensorsList);

        for (var i = 0; i < relatedSensorsList.length; i++) {
            var item = relatedSensorsList[i];

            console.log('dashboardEditorController.openSetSignalWindow - signal item: ', item);

            var record = {
                dataId: item.dataId,
                dataSourceId: item.dataSourceId,
                sensorObjectId: item.sensorObjectId,
                sensorObjectNodeId: item.sensorObjectNodeId,
                dataSourceName: item.dataSourceName,
                name: item.name,
                description: item.description,
                sensorObjectDescription: item.sensorObjectDescription,
                unit: item.unit,
                configured: item.configured,
                nodeTypeString: item.nodeTypeString
            };

            data.push(record)
            signalData.push(record);
        }

        sensorsDataStore.setData(data);
        signalSensorsDataStore.setData(signalData);

        console.log('dashboardEditorController.openSetSignalWindow - sensorsDataStore: ', sensorsDataStore);
        console.log('dashboardEditorController.openSetSignalWindow - signalSensorsDataStore: ', signalSensorsDataStore);

        self.setSignalWindow = new Ext.Window({
            id: 'setSignalWindow',
            tempId: component.tempId,
            sensorId: sensorId,
            header: {
                title: self.fieldLabelSetSignalTitle,
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important'
                }
            },
            closable: true,
            width: 500,
            height: 600,
            x: ((window.innerWidth - 500) / 2),
            y: 100,
            border: false,
            frame: false,
            modal: true,
            maskClickAction: 'destroy',
            renderTo: document.body,
            style: {
                'text-align': 'center',
                'border': '0 !important',
                'border-radius': '0px !important'
            },
            bodyStyle: {
                'background': '#ffffff !important'
            },
            layout: 'border',
            items: [
                {
                    xtype: 'gridpanel',
                    id: 'signalsListGridPanel',
                    height: 500,
                    region: 'center',
                    margin: 10,
                    store: this.getViewModel().getStore('signalSensorsDataStore'),
                    plugins: 'gridfilters',
                    style: {
                        'border': '1px solid #cfcfcf !important'
                    },
                    viewConfig: {
                        stripeRows: false,
                        getRowClass: function (record) {

                            switch (record.get('configured')) {
                                case true:
                                    return 'configured-true';
                                case false:
                                    return 'configured-false';
                                default:
                                    return '  x-grid-row';
                            }
                        }
                    },
                    columns: [
                        {
                            text: 'Status',
                            dataIndex: 'configured',
                            width: 60,
                            renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                                switch (value) {
                                    case true:
                                        return '<div class="fa fa-check configured-status-content" data-statusvalue="' + value + '"></div>';
                                    case false:
                                        return '<div class="fa fa-close configured-status-content" data-statusvalue="' + value + '"></div>';

                                    default:
                                        return '';
                                }
                            }
                        },
                        { text: self.fieldLabelNewCardId, dataIndex: 'dataId', width: 100, filter: { type: 'string' } },
                        { text: self.fieldLabelNewCardSensorType, dataIndex: 'nodeTypeString', flex: 1, filter: { type: 'string' } },
                        { text: self.fieldLabelNewCardUnit, dataIndex: 'unit', width: 60, filter: { type: 'string' } }
                    ],
                    listeners: {
                        itemclick: function (component, record, element, index) {
                            console.log('newCard - sensor click, component: ', component);
                            console.log('newCard - sensor click, record: ', record);
                            console.log('newCard - sensor click, element: ', element);
                            console.log('newCard - sensor click, index: ', index);

                            AGS3xIoTAdmin.util.events.fireEvent('signalSelected', record);
                        }
                    }
                },
                {
                    xtype: 'panel',
                    id: 'setSignalMiddleBox',
                    height: 100,
                    margin: '0 10px 0 10px',
                    region: 'south',
                    items: [
                        {
                            xtype: 'button',
                            id: 'buttonSelectSignal',
                            disabled: true,
                            text: self.fieldLabelButtonSetSignal,
                            iconCls: 'fa fa-check',
                            style: {
                                'float': 'right'
                            },
                            listeners: {
                                click: function (component) {
                                    AGS3xIoTAdmin.util.events.fireEvent('buttonSelectSignalClicked', self.tempId);
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'buttonUnselectSignal',
                            disabled: false,
                            text: self.fieldLabelButtonUnselectSignal,
                            iconCls: 'fa fa-trash',
                            style: {
                                'float': 'right',
                                'margin-right': '10px'
                            },
                            listeners: {
                                click: function (component) {
                                    AGS3xIoTAdmin.util.events.fireEvent('buttonUnselectSignalClicked', self.tempId);
                                }
                            }
                        }
                    ]
                }
            ]
        }).show();

        // hide overlays
        var overlaysToHide = document.getElementsByClassName('dashboard-card-inner-overlay');
        for (var i = 0; i < overlaysToHide.length; i++) {
            overlaysToHide[i]['style']['display'] = 'none';
        }
                    
    },
    processBatterySelected: function(record) {
        var self = this;

        console.log('dashboardEditorController.processBatterySelected - record: ', record);

        if(record.data.configured == true) {
            Ext.ComponentQuery.query('#buttonSelectBattery')[0].setDisabled(false);
        }
        else {
            Ext.ComponentQuery.query('#buttonSelectBattery')[0].setDisabled(true);
        }
    },
    processSignalSelected: function (record) {
        var self = this;

        console.log('dashboardEditorController.processSignalSelected');

        if (record.data.configured == true) {
            Ext.ComponentQuery.query('#buttonSelectSignal')[0].setDisabled(false);
        }
        else {
            Ext.ComponentQuery.query('#buttonSelectSignal')[0].setDisabled(true);
        }
    },
    processSignalConnectivityTypeSelected: function () {
        var self = this;

        console.log('dashboardEditorController.processSignalConnectivityTypeSelected');

        Ext.ComponentQuery.query('#buttonSelectSignal')[0].setDisabled(false);
    },
    processSignalUnselect: function () {
        var self = this;

        var windowTempId = self.setSignalWindow.tempId;

        console.log('dashboardEditorController.processSignalUnselect - windowTempId: ', windowTempId);

        var cardsStore = this.getViewModel().getStore('cardsStore');
        var cardsArray = [];

        for (var i = 0; i < cardsStore.data.items.length; i++) {
            var card = cardsStore.data.items[i].data;
            delete card.id;

            if (card.tempId == windowTempId) {
                if (card.hasOwnProperty('signalSensorId')) {
                    delete card.signalSensorId;
                }
            }

            cardsArray.push(card);
        }

        cardsStore.setData(cardsArray);

        self.setSignalWindow.destroy();

        self.loadCards();
    },
    processBatteryConnection: function() {
        var self = this;

        var windowTempId = self.setBatteryWindow.tempId;
        var selectedRecord = Ext.ComponentQuery.query('#batteriesListGridPanel')[0].getSelection()[0].data;
        var batterySensorId = selectedRecord.dataId;

        console.log('dashboardEditorController.processBatteryConnection - windowTempId: ', windowTempId);
        console.log('dashboardEditorController.processBatteryConnection - selectedRecord: ', selectedRecord);
        console.log('dashboardEditorController.processBatteryConnection - batterySensorId: ', batterySensorId);

        var cardsStore = this.getViewModel().getStore('cardsStore');
        var cardsArray = [];

        for (var i = 0; i < cardsStore.data.items.length; i++) {
            var card = cardsStore.data.items[i].data;
            delete card.id;

            if (card.tempId == windowTempId) {
                card.batterySensorId = batterySensorId;
            }

            cardsArray.push(card);
        }

        cardsStore.setData(cardsArray);

        self.setBatteryWindow.destroy();

        self.loadCards();
    },
    processBatteryUnselect: function() {
        var self = this;

        var windowTempId = self.setBatteryWindow.tempId;

        console.log('dashboardEditorController.processBatteryUnselect - windowTempId: ', windowTempId);

        var cardsStore = this.getViewModel().getStore('cardsStore');
        var cardsArray = [];

        for (var i = 0; i < cardsStore.data.items.length; i++) {
            var card = cardsStore.data.items[i].data;
            delete card.id;

            if (card.tempId == windowTempId) {
                if(card.hasOwnProperty('batterySensorId')) {
                    delete card.batterySensorId;
                }
            }

            cardsArray.push(card);
        }

        cardsStore.setData(cardsArray);

        self.setBatteryWindow.destroy();

        self.loadCards();
    },
    loadSensorData: function() {
        var self = this;

        console.log('dashboardEditorController.loadSensorData');

        var cardsStore = this.getViewModel().getStore('cardsStore');
        var sensorIdsArray = [];
        var batteryIdsArray = [];
        var signalStrengthIdsArray = [];

        var fullIdsArray = [];

        for (var i = 0; i < cardsStore.data.items.length; i++) {
            var card = cardsStore.data.items[i].data;

            if (sensorIdsArray.indexOf(card.sensorId) == -1) {
                sensorIdsArray.push(Number(card.sensorId));
            }

            if (fullIdsArray.indexOf(card.sensorId) == -1) {
                fullIdsArray.push(Number(card.sensorId));
            }

            if (card.hasOwnProperty('batterySensorId')) {
                if (batteryIdsArray.indexOf(card.batterySensorId) == -1) {
                    batteryIdsArray.push(Number(card.batterySensorId));
                }

                if (fullIdsArray.indexOf(card.batterySensorId) == -1) {
                    fullIdsArray.push(Number(card.batterySensorId));
                }
            }

            if (card.hasOwnProperty('signalSensorId')) {
                if (signalStrengthIdsArray.indexOf(card.signalSensorId) == -1) {
                    signalStrengthIdsArray.push(Number(card.signalSensorId));
                }

                if (fullIdsArray.indexOf(card.signalSensorId) == -1) {
                    fullIdsArray.push(Number(card.signalSensorId));
                }
            }
        }

        console.log('dashboardEditorController.loadSensorData - sensorIdsArray: ', sensorIdsArray);
        console.log('dashboardEditorController.loadSensorData - batteryIdsArray: ', batteryIdsArray);
        console.log('dashboardEditorController.loadSensorData - signalStengthIdsArray: ', signalStrengthIdsArray);
        console.log('dashboardEditorController.loadSensorData - fullIdsArray: ', fullIdsArray);

        var unconfiguredDataUrl = self.serviceUrl + 'unconfigureddataio?ids=' + fullIdsArray.join(',');

        // Load sensors list for viewing
        Ext.Ajax.request({
            scope: this,
            url: unconfiguredDataUrl,
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dashboardEditorController.loadSensorData - result: ', result);

                        for (var i = 0; i < result.unConfiguredDataIos.length; i++) {
                            var item = result.unConfiguredDataIos[i];
                            console.log('dashboardEditorController.loadSensorData - item: ', item);

                            item.dataId = item.id;

                            // load sensor value
                            if (sensorIdsArray.indexOf(item.dataId) > -1) {
                                console.log('dashboardEditorController.loadSensorData - target item: ', item);

                                var valueTargets = document.querySelectorAll('[data-sensorid="' + item.id + '"]');
                                for (var v = 0; v < valueTargets.length; v++) {
                                    var valueTarget = valueTargets[v];

                                    if (valueTarget.getElementsByClassName('dashboard-card-value').length > 0) {
                                        valueTarget.getElementsByClassName('dashboard-card-value')[0].innerHTML = item.lastValue;
                                    }

                                    // load last run
                                    if (item.hasOwnProperty('lastRun') && item.lastRun != null) {
                                        if (valueTarget.getElementsByClassName('dashboard-last-run-value').length > 0) {
                                            var lastRunTarget = valueTarget.getElementsByClassName('dashboard-last-run-value')[0];
                                            lastRunTarget.innerHTML = AGS3xIoTAdmin.util.util.dateConversionDayTime(Number(item.lastRun));
                                        }
                                    }
                                }
                                
                            }

                            // load battery sensor values
                            if (batteryIdsArray.indexOf(item.id) > -1) {
                                var targets = document.querySelectorAll('[data-batterysensorid="' + item.id + '"]')

                                for(var t = 0; t < targets.length; t++) {
                                    var target = targets[t];
                                    
                                    console.log('dashboardEditorController.loadSensorData - battery ' + item.id + ' for target: ', target);
                                    if (target.getElementsByClassName('dashboard-battery-value').length > 0) {
                                        target.getElementsByClassName('dashboard-battery-value')[0].innerHTML = Math.floor(Number(item.lastValue)) + '%';
                                    }
                                    
                                }
                            }

                            // load signal strength values
                            if (signalStrengthIdsArray.indexOf(item.id) > -1) {
                                var targets = document.querySelectorAll('[data-signalstrengthsensorid="' + item.id + '"]')

                                for (var t = 0; t < targets.length; t++) {
                                    var target = targets[t];

                                    console.log('dashboardEditorController.loadSensorData - signal ' + item.id + ' for target: ', target);
                                    target.getElementsByClassName('dashboard-signal-value')[0].innerHTML = Math.floor(Number(item.lastValue)) + ' dBm';
                                }
                            }
                        }

                    }
                    catch (exception) {
                        console.log('dashboardEditorController.loadSensorData - exception: ', exception);
                    }
                }
            }
        });
    },
    editCardSettings: function (component) {
        var self = this;

        console.log('dashboardEditorController.editCardSettings - component: ', component);

        // hide all overlays
        var overlaysToHide = document.getElementsByClassName('dashboard-card-inner-overlay');
        for (var i = 0; i < overlaysToHide.length; i++) {
            overlaysToHide[i]['style']['display'] = 'none';
        }

        var cardsStore = self.getViewModel().getStore('cardsStore');

        console.log('dashboardEditorController.editCardSettings - cardsStore: ', cardsStore);

        // get active card for data fields
        var activeCard;
        for (var i = 0; i < cardsStore.data.items.length; i++){
            var card = cardsStore.data.items[i].data;
            if(card.tempId == component.tempId) {
                activeCard = card;
                break;
            }
        }

        console.log('dashboardEditorController.editCardSettings - activeCard: ', activeCard);
        

        self.cardSettingsWindow = Ext.create('Ext.window.Window', {
            xtype: 'panel',
            id: 'cardSettingsWindow',
            header: {
                title: self.fieldLabelCardSettingsTitle,
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important',
                    'padding': '12px'
                }
            },
            closable: true,
            width: 400,
            padding: 10,
            x: ((window.innerWidth - 400) / 2),
            y: 200,
            frame: false,
            modal: true,
            maskClickAction: 'destroy',
            renderTo: document.body,
            items: [
                {
                    xtype: 'textfield',
                    id: 'cardSettingsNameBox',
                    name: 'name',
                    width: 380,
                    fieldLabel: self.fieldLabelCardSettingsName,
                    allowBlank: false, 
                    fieldStyle: {
                        'width': '250px'
                    },
                    value: activeCard.title
                },
                {
                    xtype: 'panel',
                    id: 'cardSettingsUnitBoxHolder',
                    margin: '10px 0 0 0',
                    width: 380,
                    items: [
                        {
                            xtype: 'textfield',
                            id: 'cardSettingsUnitBox',
                            name: 'unit',
                            fieldLabel: self.fieldLabelCardSettingsUnit,
                            width: 150,
                            style: {
                                'float': 'left',
                                'width': '100px'
                            },
                            bodyStyle: {
                                'width': '100px'
                            },
                            fieldStyle: {
                                'width': '50px',

                            },
                            value: ((activeCard.hasOwnProperty('unit') && activeCard.unit != null && activeCard.unit.length > 0) ? activeCard.unit : '')
                        },
                        {
                            xtype: 'button',
                            id: 'buttonCardSettingsUseOrigialUnit',
                            text: self.fieldLabelUseOriginalUnit,
                            style: {
                                'float': 'right'
                            },
                            handler: function (component) {
                                AGS3xIoTAdmin.util.events.fireEvent('buttonCardSettingsUseOriginalUnitClicked', activeCard);
                            }
                        }
                    ]
                },
                {
                            
                    xtype: 'fieldcontainer',
                    defaultType: 'checkboxfield',
                    hidden: (activeCard.cardType == 2) ? true : false,
                    margin: '10px 0 0 0',
                    items: [
                        {
                            boxLabel: self.fieldLabelCardSettingsShowLastRun,
                            boxLabelAlign: 'before',
                            boxLabelCls: 'card-settings-checkbox-box-label',
                            name: 'fullscreen',
                            checked: ((activeCard.hasOwnProperty('lastRunVisible') && activeCard.lastRunVisible == true) ? true : false),
                            id: 'cardShowLastRunCheckbox',
                            handler: function (component) {
                                console.log('cardShowLastRunCheckbox changed');
                            }
                        }
                    ]
                },

                // line chart
                {

                    xtype: 'fieldcontainer',
                    defaultType: 'checkboxfield',
                    hidden: (activeCard.cardType == 2) ? false : true,
                    margin: '10px 0 0 0',
                    items: [
                        {
                            id: 'cardShowAlarmLimits',
                            boxLabel: self.fieldLabelCardSettingsShowAlarmLimits,
                            boxLabelAlign: 'before',
                            boxLabelCls: 'card-settings-checkbox-box-label',
                            name: 'showAlarmLimits',
                            checked: ((activeCard.hasOwnProperty('showLimits') && activeCard.showLimits == true) ? true : false)
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'cardSettingsMaxValueBoxHolder',
                    hidden: (activeCard.cardType == 2) ? false : true,
                    margin: '10px 0 0 0',
                    items: [
                        {
                            xtype: 'textfield',
                            id: 'cardSettingsMaxValueBox',
                            name: 'unit',
                            fieldLabel: self.fieldLabelCardSettingsMax,
                            width: 100,
                            style: {
                                'float': 'left'
                            },
                            fieldStyle: {
                                'width': '50px'
                            },
                            value: ((activeCard.hasOwnProperty('maxValue') && activeCard.maxValue != null && activeCard.maxValue.length > 0) ? activeCard.maxValue : '')
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'cardSettingsMinValueBoxHolder',
                    hidden: (activeCard.cardType == 2) ? false : true,
                    margin: '10px 0 0 0',
                    items: [
                        {
                            xtype: 'textfield',
                            id: 'cardSettingsMinValueBox',
                            name: 'unit',
                            fieldLabel: self.fieldLabelCardSettingsMin,
                            width: 100,
                            style: {
                                'float': 'left'
                            },
                            fieldStyle: {
                                'width': '50px'
                            },
                            value: ((activeCard.hasOwnProperty('minValue') && activeCard.minValue != null && activeCard.minValue.length > 0) ? activeCard.minValue : '')
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'cardSettingsButtonsBox',
                    margin: '10px 0 0 0',
                    items: [
                        {
                            xtype: 'button',
                            id: 'buttonCardSettingsAccept',
                            text: self.fieldLabelCardSettingsAccept,
                            style: {
                                'float': 'right',
                                'margin-left': '10px'
                            },
                            handler: function (component) {
                                AGS3xIoTAdmin.util.events.fireEvent('buttonCardSettingsAcceptedClicked', activeCard);
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'buttonCardSettingsCancel',
                            text: self.fieldLabelCardSettingsCancel,
                            style: {
                                'float': 'right',
                                'margin-left': '10px'
                            },
                            handler: function (component) {
                                self.cardSettingsWindow.destroy();
                            }
                        }
                    ]
                }
            ]
        }).show();
    },
    processCardSettingsAccepted: function(activeCard){
        var self = this;

        console.log('dashboardEditorController.processCardSettingsAccepted - activeCard, before: ', activeCard);

        var cardTitle = Ext.ComponentQuery.query('#cardSettingsNameBox')[0].getValue();
        var cardUnit = Ext.ComponentQuery.query('#cardSettingsUnitBox')[0].getValue();
        var cardShowLastRunCheck = Ext.ComponentQuery.query('#cardShowLastRunCheckbox')[0].getValue();

        activeCard.title = cardTitle;
        activeCard.unit = cardUnit;
        activeCard.lastRunVisible = cardShowLastRunCheck;

        // load settings for line chart
        if (activeCard.cardType == 2) {
            var maxValue = Ext.ComponentQuery.query('#cardSettingsMaxValueBox')[0].getValue();
            var minValue = Ext.ComponentQuery.query('#cardSettingsMinValueBox')[0].getValue();
            var showLimitsValue = Ext.ComponentQuery.query('#cardShowAlarmLimits')[0].getValue();

            console.log('dashboardEditorController.processCardSettingsAccepted - line chart, maxValue: ', maxValue);
            console.log('dashboardEditorController.processCardSettingsAccepted - line chart, minValue: ', minValue);
            console.log('dashboardEditorController.processCardSettingsAccepted - line chart, showLimitsValue: ', showLimitsValue);

            activeCard.maxValue = maxValue;
            activeCard.minValue = minValue;
            activeCard.showLimits = showLimitsValue;
        }

        console.log('dashboardEditorController.processCardSettingsAccepted - activeCard, after: ', activeCard);

        var cardsStore = self.getViewModel().getStore('cardsStore');

        // get active card for data fields
        for (var i = 0; i < cardsStore.data.items.length; i++) {
            var card = cardsStore.data.items[i].data;
            if (card.tempId == activeCard.tempId) {
                cardsStore.data.items[i].data = activeCard;
                break;
            }
        }

        console.log('dashboardEditorController.processCardSettingsAccepted - cardsStore, after: ', cardsStore);

        self.cardSettingsWindow.destroy();

        self.loadCards();
    },
    processBatteryTypeSelection: function(component, sensorId) {
        var self = this;

        console.log('dashboardEditorController.processBatteryTypeSelection - component: ', component);
        console.log('dashboardEditorController.processBatteryTypeSelection - sensorId: ', sensorId);

        var sensorsDataStore = this.getViewModel().getStore('sensorsDataStore');

        console.log('dashboardEditorController.processBatteryTypeSelection - sensorsDataStore: ', sensorsDataStore);

        var batterySensorsDataStore = this.getViewModel().getStore('batterySensorsDataStore');
        batterySensorsDataStore.setData([]);

        if (component.value == 1) {
            // Show all sensors defined as batteries
            console.log('dashboardEditorController.processBatteryTypeSelection -  Show all sensors defined as batteries');

            var configuredDataUrl = self.serviceUrl + 'dataconfiguration';

            // Load sensors list for viewing
            Ext.Ajax.request({
                scope: this,
                url: configuredDataUrl,
                method: 'GET',
                contentType: 'application/json',
                callback: function (options, success, response) {
                    if (success) {
                        try {
                            var result = Ext.decode(response.responseText);
                            console.log('dashboardEditorController.processBatteryTypeSelection - result: ', result);

                            var batterySensorsDataStore = self.getViewModel().getStore('batterySensorsDataStore');
                            batterySensorsDataStore.setData([]);

                            var batterySensorsDataArray = [];

                            for (var i = 0; i < result.configurations.length; i++) {
                                var item = result.configurations[i];
                                item.dataId = item.id;

                                if (item.isBatteryStatus == true ) {
                                    batterySensorsDataArray.push(item);
                                }
                            }

                            batterySensorsDataStore.setData(batterySensorsDataArray);
                        }
                        catch(exception) {
                            console.log('dashboardEditorController.processBatteryTypeSelection - EXCEPTION: ', exception);
                        }
                    }
                }
            });

        }
        else if(component.value == 2) {
            // Show related sensors
            console.log('dashboardEditorController.processBatteryTypeSelection - Show related sensors');

            var relatedSensorsArray = [];

            // get sensorObjectId
            var sensorObjectId;
            for(var i = 0; i < sensorsDataStore.data.items.length; i++) {
                var sensor = sensorsDataStore.data.items[i].data;
                

                if( sensor.dataId == sensorId ) {
                    console.log('dashboardEditorController.processBatteryTypeSelection - HIT, sensor: ', sensor);
                    sensorObjectId = sensor.sensorObjectId;
                    break;
                }
            }
            
            console.log('dashboardEditorController.processBatteryTypeSelection - sensorObjectId: ', sensorObjectId);

            for (var i = 0; i < sensorsDataStore.data.items.length; i++) {
                var sensor = sensorsDataStore.data.items[i].data;
                if (sensor.sensorObjectId == sensorObjectId) {
                    console.log('dashboardEditorController.processBatteryTypeSelection - relatedsensor: ', sensor);
                    relatedSensorsArray.push(sensor);
                }
            }

            batterySensorsDataStore.setData(relatedSensorsArray);
        }
        
        else if (component.value == 3) {
            // Show all sensors - default
            console.log('dashboardEditorController.processBatteryTypeSelection - Show all sensors');

            var copiedSensorsArray = [];
            for (var i = 0; i < sensorsDataStore.data.items.length; i++) {
                var sensor = sensorsDataStore.data.items[i].data;
                copiedSensorsArray.push(sensor);
            }

            batterySensorsDataStore.setData(copiedSensorsArray);
        }
        else {
            console.log('dashboardEditorController.processBatteryTypeSelection - ERROR, no value available');
        }
    },
    processSignalTypeSelection: function(component, sensorId) {
        var self = this;

        console.log('dashboardEditorController.processSignalTypeSelection - component: ', component);
        console.log('dashboardEditorController.processSignalTypeSelection - sensorId: ', sensorId);

        var sensorsDataStore = this.getViewModel().getStore('sensorsDataStore');

        console.log('dashboardEditorController.processSignalTypeSelection - sensorsDataStore: ', sensorsDataStore);

        var signalSensorsDataStore = this.getViewModel().getStore('signalSensorsDataStore');
        signalSensorsDataStore.setData([]);

        if (component.value == 1) {
            // Show all sensors defined as batteries
            console.log('dashboardEditorController.processSignalTypeSelection -  Show all sensors defined as signal strength output');

            // 1 - Get measurement type id for signal strength
            var measurementTypeUrl = self.serviceUrl + 'measurementtype';
            
            Ext.Ajax.request({
                scope: this,
                url: measurementTypeUrl,
                method: 'GET',
                contentType: 'application/json',
                callback: function (options, success, response) {
                    if (success) {
                        try {
                            var result = Ext.decode(response.responseText);
                            console.log('dashboardEditorController.processSignalTypeSelection - measurementTypeUrl, result: ', result);

                            var measurementTypes = result.measurementTypes;

                            for (var m = 0; m < measurementTypes.length; m++) {
                                var measurementType = measurementTypes[m];

                                if (measurementType.isSignalStrength == true) {
                                    var measurementTypeId = measurementType.id;

                                    // 2 - Load configured sensors list for viewing
                                    var configuredDataUrl = self.serviceUrl + 'dataconfiguration';

                                    Ext.Ajax.request({
                                        scope: this,
                                        url: configuredDataUrl,
                                        measurementTypeId: measurementTypeId,
                                        method: 'GET',
                                        contentType: 'application/json',
                                        callback: function (options, success, response) {
                                            if (success) {
                                                try {
                                                    var result = Ext.decode(response.responseText);
                                                    console.log('dashboardEditorController.processSignalTypeSelection - result: ', result);

                                                    var signalSensorsDataStore = self.getViewModel().getStore('signalSensorsDataStore');
                                                    signalSensorsDataStore.setData([]);

                                                    var signalSensorsDataArray = [];

                                                    for (var i = 0; i < result.configurations.length; i++) {
                                                        var item = result.configurations[i];
                                                        item.dataId = item.id;

                                                        if (item.measurementType == options.measurementTypeId) {
                                                            signalSensorsDataArray.push(item);
                                                        }
                                                    }

                                                    signalSensorsDataStore.setData(signalSensorsDataArray);
                                                }
                                                catch (exception) {
                                                    console.log('dashboardEditorController.processSignalTypeSelection - EXCEPTION: ', exception);
                                                }
                                            }
                                        }
                                    });

                                    break;
                                }
                            }
                        }
                        catch (exception) {
                            console.log('dashboardEditorController.processSignalTypeSelection - measurementTypeUrl, EXCEPTION: ', exception);
                        }
                    }
                }
            });
        }
        else if (component.value == 2) {
            // Show related sensors
            console.log('dashboardEditorController.processSignalTypeSelection - Show related sensors');

            var relatedSensorsArray = [];

            // get sensorObjectId
            var sensorObjectId;
            for (var i = 0; i < sensorsDataStore.data.items.length; i++) {
                var sensor = sensorsDataStore.data.items[i].data;


                if (sensor.dataId == sensorId) {
                    console.log('dashboardEditorController.processSignalTypeSelection - HIT, sensor: ', sensor);
                    sensorObjectId = sensor.sensorObjectId;
                    break;
                }
            }

            console.log('dashboardEditorController.processSignalTypeSelection - sensorObjectId: ', sensorObjectId);

            for (var i = 0; i < sensorsDataStore.data.items.length; i++) {
                var sensor = sensorsDataStore.data.items[i].data;
                if (sensor.sensorObjectId == sensorObjectId) {
                    console.log('dashboardEditorController.processSignalTypeSelection - relatedsensor: ', sensor);
                    relatedSensorsArray.push(sensor);
                }
            }

            signalSensorsDataStore.setData(relatedSensorsArray);
        }
        else {
            console.log('dashboardEditorController.processSignalTypeSelection - ERROR, no value available');
        }
    },
    processSignalConnection: function() {
        var self = this;

        var windowTempId = self.setSignalWindow.tempId;
        var selectedRecord = Ext.ComponentQuery.query('#signalsListGridPanel')[0].getSelection()[0].data;
        var signalSensorId = selectedRecord.dataId;

        console.log('dashboardEditorController.processSignalConnection - windowTempId: ', windowTempId);
        console.log('dashboardEditorController.processSignalConnection - selectedRecord: ', selectedRecord);
        console.log('dashboardEditorController.processSignalConnection - signalSensorId: ', signalSensorId);

        var cardsStore = this.getViewModel().getStore('cardsStore');
        var cardsArray = [];

        for (var i = 0; i < cardsStore.data.items.length; i++) {
            var card = cardsStore.data.items[i].data;
            delete card.id;

            if (card.tempId == windowTempId) {
                card.signalSensorId = signalSensorId;
            }

            cardsArray.push(card);
        }

        cardsStore.setData(cardsArray);

        console.log('dashboardEditorController.processSignalConnection - cardsArray: ', cardsArray);

        self.setSignalWindow.destroy();

        self.loadCards();
    },
    processSensorTypeSelection: function (component) {
        var self = this;

        console.log('dashboardEditorController.processSensorTypeSelection - component: ', component);

        var optionValue = component.value;

        var unconfiguredDataUrl = self.serviceUrl + 'unconfigureddataio';

        // Load sensors list for viewing
        Ext.Ajax.request({
            scope: this,
            url: unconfiguredDataUrl,
            method: 'GET',
            contentType: 'application/json',
            callback: function (options, success, response) {
                if (success) {
                    try {
                        var result = Ext.decode(response.responseText);
                        console.log('dashboardEditorController.processSensorTypeSelection - result: ', result);

                        var sensorsDataStore = self.getViewModel().getStore('sensorsDataStore');
                        var sensorsDataArray = [];

                        for (var i = 0; i < result.unConfiguredDataIos.length; i++) {
                            var item = result.unConfiguredDataIos[i];
                            item.dataId = item.id;

                            if (optionValue == 2) {
                                sensorsDataArray.push(item);
                            }
                            else {
                                if (item.configured == true) {
                                    sensorsDataArray.push(item);
                                }
                            }
                        }

                        sensorsDataStore.setData(sensorsDataArray);
                    }
                    catch(exception) {
                        console.log('dashboardEditorController.processSensorTypeSelection - EXCEPTION: ', exception);
                    }
                }
            }
        });
    },
    processUseOriginalUnit: function(activeCard) {
        var self = this;

        console.log('dashboardEditorController.processUseOriginalUnit - activeCard: ', activeCard);

        //var sensorsDataStore = self.getViewModel().getStore('sensorsDataStore');
        var sensorsDataStore = Ext.data.StoreManager.lookup('AGS3xIoTAdmin.state.unconfiguredDataStore')
        console.log('dashboardEditorController.processUseOriginalUnit - sensorsDataStore: ', sensorsDataStore);

        var originalUnit;

        for (var i = 0; i < sensorsDataStore.data.items.length; i++) {
            var item = sensorsDataStore.data.items[i].data;

            if (item.dataId == activeCard.sensorId) {
                console.log('dashboardEditorController.processUseOriginalUnit - HIT, item: ', item);
                originalUnit = item.unit;
                break;
            }
        }

        console.log('dashboardEditorController.processUseOriginalUnit - originalUnit: ', originalUnit);

        Ext.ComponentQuery.query('#cardSettingsUnitBox')[0].setValue(originalUnit);
    },
    setDataTextFieldsToEnglish: function() {
        var self = this;

        self.cardTypes = {
            fields: [
                'name', 'id'
            ],
            data: [
                { 'name': 'Unmodified sensor value', 'id': 1 },
                { 'name': 'Graph - sensor values over time', 'id': 2 }
            ]
        }

        self.batterySelectionTypes = {
            fields: [
                'name', 'id'
            ],
            data: [
                { 'name': 'Show all sensors defined as batteries', 'id': 1 },
                { 'name': 'Show related sensors', 'id': 2 },
                { 'name': 'Show all sensors', 'id': 3 }
            ]
        }

        self.signalSelectionTypes = {
            fields: [
                'name', 'id'
            ],
            data: [
                { 'name': 'Show all sensors defined as signal strength', 'id': 1 },
                { 'name': 'Show related configured sensors', 'id': 2 }
            ]
        }
    },
    init: function () {
        var self = this;

        self.serviceUrl = AGS3xIoTAdmin.systemData.serviceUrl;
        self.basicUrl = AGS3xIoTAdmin.systemData.basicUrl;
        self.dashboardUrl = AGS3xIoTAdmin.systemData.dashboardUrl;

        //  TEMP SOLUTION
        var language = AGS3xIoTAdmin.util.util.getUrlParameterByName('locale');
        switch (language) {
            case 'en':
                self.setDataTextFieldsToEnglish();
                break;

            default:
                break;
        }

        // TEMP
        self.loadDashboardsData(false);
        
        AGS3xIoTAdmin.util.events.on('dashboardSelected', self.loadDashboard, self);
        AGS3xIoTAdmin.util.events.on('sensorSelected', self.processSensorSelected, self);
        AGS3xIoTAdmin.util.events.on('cardTypeSelected', self.processCardTypeSelected, self);
        AGS3xIoTAdmin.util.events.on('cardCalculationTypeSelected', self.processCardCalculationTypeSelected, self);
        AGS3xIoTAdmin.util.events.on('cardAggregationTypeSelected', self.processCardAggregationTypeSelected, self);

        AGS3xIoTAdmin.util.events.on('buttonCreateNewCardClicked', self.createNewCard, self);
        AGS3xIoTAdmin.util.events.on('buttonSaveDashboardClicked', self.saveDashboard, self);
        AGS3xIoTAdmin.util.events.on('buttonCreateNewDashboardClicked', self.createNewDashboard, self);
        AGS3xIoTAdmin.util.events.on('buttonDeleteDashboardClicked', self.openDeleteDashboardDialog, self);
        AGS3xIoTAdmin.util.events.on('buttonShowUrlClicked', self.showUrl, self);
        AGS3xIoTAdmin.util.events.on('buttonLoadSensorDataClicked', self.loadSensorData, self);

        AGS3xIoTAdmin.util.events.on('checkboxDashboardUrlParameterChanged', self.setDashboardUrlParameters, self);

        AGS3xIoTAdmin.util.events.on('buttonCopyUrlClicked', self.copyDashboardUrl, self);
        AGS3xIoTAdmin.util.events.on('buttonOpenUrlClicked', self.openDashboardUrl, self);
        
        AGS3xIoTAdmin.util.events.on('buttonSetBatteryClicked', self.openSetBatteryWindow, self);
        AGS3xIoTAdmin.util.events.on('buttonSetSignalClicked', self.openSetSignalWindow, self);
        AGS3xIoTAdmin.util.events.on('buttonRemoveCardFromDashboardClicked', self.removeCardFromDashboard, self);
        AGS3xIoTAdmin.util.events.on('buttonEditSettingsClicked', self.editCardSettings, self);
        AGS3xIoTAdmin.util.events.on('buttonReorderCardOnDashboardClicked', self.reorderCardOnDashboard, self);

        AGS3xIoTAdmin.util.events.on('buttonCardSettingsAcceptedClicked', self.processCardSettingsAccepted, self);

        AGS3xIoTAdmin.util.events.on('batterySelected', self.processBatterySelected, self);
        AGS3xIoTAdmin.util.events.on('buttonSelectBatteryClicked', self.processBatteryConnection, self);
        AGS3xIoTAdmin.util.events.on('buttonUnselectBatteryClicked', self.processBatteryUnselect, self);
        
        AGS3xIoTAdmin.util.events.on('batterySelectionTypeSelected', self.processBatteryTypeSelection, self);

        AGS3xIoTAdmin.util.events.on('signalSelectionTypeSelected', self.processSignalTypeSelection, self);
        AGS3xIoTAdmin.util.events.on('signalSelected', self.processSignalSelected, self);
        AGS3xIoTAdmin.util.events.on('buttonUnselectSignalClicked', self.processSignalUnselect, self);
        
        AGS3xIoTAdmin.util.events.on('signalConnectivityTypeSelected', self.processSignalConnectivityTypeSelected, self);
        AGS3xIoTAdmin.util.events.on('buttonSelectSignalClicked', self.processSignalConnection, self);
        
        AGS3xIoTAdmin.util.events.on('buttonCardSettingsUseOriginalUnitClicked', self.processUseOriginalUnit, self);
    }
});