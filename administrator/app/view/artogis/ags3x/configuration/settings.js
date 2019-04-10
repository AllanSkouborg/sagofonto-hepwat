Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.configuration.settings', {

    // TEST - slet senere

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.authorization.api',
        'AGS3xIoTAdmin.view.artogis.ags3x.configuration.systemData'
    ],

    // text content - default is Danish (da), for translations, see translation.js
    textGeneralInitializeError: 'Indstillingerne for denne applikation er ikke gyldige.',
    textBaseUrlMissing: 'Variablen "baseUrl" mangler',
    
    textNoMapConfigurationError: 'Konfigurationen for kortet mangler i filen "setting.json".',
    textNoMapSearchConfigurationError: 'Konfigurationen for "MapSearch" mangler i filen "settings.json".',
    textNoServiceUrlConfigurationError: 'Konfigurationen for "serviceUrl" mangler i filen "settings.json".',
    textNoBasicUrlConfigurationError: 'Konfigurationen "basicUrl" mangler i filen "settings.json".',
    textInitApplication: 'Start applikation',
    textSettingsNotReadError: 'Indstillingerne for denne applikation kunne ikke indlæses.',
    textNoVersionConfigurationError: 'Der er ikke defineret et versionsnummer i filen "settings.json"',

    load: function () {
        var self = this;

        Ext.Ajax.request({
            url: "settings.json",
            method: 'GET',
            success: function (response) {
                var settings = Ext.decode(response.responseText);

                var initializeError = null;
                var generalInitializeError = self.textGeneralInitializeError;

                if (!settings) initializeError = generalInitializeError;

                if (!initializeError) {

                    AGS3xIoTAdmin.systemData = Ext.create('AGS3xIoTAdmin.view.artogis.ags3x.configuration.systemData');

                    if (settings.applicationId) AGS3xIoTAdmin.systemData.applicationId = settings.applicationId; else AGS3xIoTAdmin.systemData.applicationId = "dk.artogis.ags3x.administrator";
                    if (settings.showDebug && settings.showDebug === true) AGS3xIoTAdmin.systemData.showDebug = true; else AGS3xIoTAdmin.systemData.showDebug = false;

                    if (settings.mapConfiguration) {
                        AGS3xIoTAdmin.systemData.mapConfiguration = settings.mapConfiguration;
                    }
                    else {
                        AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.textNoMapConfigurationError, 'ERROR');
                    }

                    if (settings.mapSearchConfig) {
                        AGS3xIoTAdmin.systemData.mapSearchConfig = settings.mapSearchConfig;
                    }
                    else {
                        AGS3xIoTAdmin.util.util.errorDlg('dataIO', self.textNoMapSearchConfigurationError, 'ERROR');
                    }


                    if (settings.mapConfiguration.hasOwnProperty('showLayersOnStart')) {
                        AGS3xIoTAdmin.systemData.showLayersOnStart = settings.mapConfiguration.showLayersOnStart;
                    }
                    else {
                        AGS3xIoTAdmin.systemData.showLayersOnStart = true; // default
                    }

                    // VERSION
                    var version;
                    if (settings.version) {
                        version = settings.version;
                        AGS3xIoTAdmin.systemData.version = settings.version;
                    }

                    var locationOrigin = window.location.origin;
                    var location = window.location;
                    console.log('Settings.load - locationOrigin: ', locationOrigin);
                    console.log('Settings.load - location: ', location);

                    if (locationOrigin.indexOf('localhost') == -1) {
                        
                        Ext.Ajax.request({
                            url: 'buildinfo.json',
                            method: 'GET',
                            scope: this,
                            callback: function (options, success, response) {
                                if (success) {
                                    var buildSettings = Ext.decode(response.responseText);
                                    console.log('Settings.load - buildSettings: ', buildSettings);

                                    var buildDate = buildSettings.buildDate;
                                    document.getElementById('menuVersionFooterContent').innerHTML = 'Version ' + settings.version + ' - Build date: ' + buildDate;

                                    AGS3xIoTAdmin.systemData.environmentIsProduction = buildSettings.production;

                                    var settingsFileUrlBase = window.location.href.split('?')[0];
                                    var lastPathValue = settingsFileUrlBase.substring(settingsFileUrlBase.lastIndexOf('/'));

                                    if (lastPathValue == '/') {
                                        settingsFileUrlBase = settingsFileUrlBase.substring(0, settingsFileUrlBase.length - 1);
                                    }
                                    else if (lastPathValue == '/#') {
                                        settingsFileUrlBase = settingsFileUrlBase.substring(0, settingsFileUrlBase.length - 2);
                                    }

                                    var settingsFileUrl = settingsFileUrlBase.substring(0, settingsFileUrlBase.lastIndexOf('/'));

                                    Ext.Ajax.request({
                                        url: settingsFileUrl + '/settings.json',
                                        method: 'GET',
                                        scope: this,
                                        callback: function (options, success, response) {
                                            if (success) {
                                                var settingsJson = Ext.decode(response.responseText);
                                                console.log('Settings.load - settings from webapp root folder: ', settingsJson);

                                                AGS3xIoTAdmin.systemData.serviceUrl = settingsJson.restServiceUrl;
                                                AGS3xIoTAdmin.systemData.basicUrl = settingsJson.baseUrl;
                                                AGS3xIoTAdmin.systemData.webSocketUrl = settingsJson.webSocketUrl;
                                                AGS3xIoTAdmin.systemData.dashboardUrl = settingsJson.dashboardUrl;

                                                console.log('Settings.load - AGS3xIoTAdmin.systemData.serviceUrl: ', AGS3xIoTAdmin.systemData.serviceUrl);
                                                console.log('Settings.load - AGS3xIoTAdmin.systemData.basicUrl: ', AGS3xIoTAdmin.systemData.basicUrl);
                                                console.log('Settings.load - AGS3xIoTAdmin.systemData.webSocketUrl: ', AGS3xIoTAdmin.systemData.webSocketUrl);
                                                console.log('Settings.load - AGS3xIoTAdmin.systemData.dashboardUrl: ', AGS3xIoTAdmin.systemData.dashboardUrl);

                                                // load start data
                                                AGS3xIoTAdmin.util.events.fireEvent('applicationLoadStartData', settings.startPageCommand);
                                            }
                                            else {
                                                console.log('Settings.load - reading settings.json failed');
                                            }
                                        }
                                    }, this);
                                }
                                else {
                                    console.log('Settings.load - reading buildinfo.json failed');
                                }
                            }
                        }, this);
                    }
                    else {
                        console.log('Settings.load - does not run on web server, settings: ', settings);

                        if (settings.basicUrl) {
                            AGS3xIoTAdmin.systemData.basicUrl = settings.basicUrl;
                            AGS3xIoTAdmin.systemData.dashboardUrl = settings.basicUrl + '/dashboard';
                            console.log('Settings.load - localhost, AGS3xIoTAdmin.systemData.basicUrl: ', AGS3xIoTAdmin.systemData.basicUrl);
                            console.log('Settings.load - localhost, AGS3xIoTAdmin.systemData.dashboardUrl: ', AGS3xIoTAdmin.systemData.dashboardUrl);
                        }
                        else {
                            AGS3xIoTAdmin.util.util.errorDlg('Basic URL', self.textNoBasicUrlConfigurationError, 'ERROR');
                        }

                        if (settings.serviceUrl) {
                            AGS3xIoTAdmin.systemData.serviceUrl = settings.serviceUrl;
                            console.log('Settings.load - localhost, AGS3xIoTAdmin.systemData.serviceUrl: ', AGS3xIoTAdmin.systemData.serviceUrl);
                        }
                        else {
                            AGS3xIoTAdmin.util.util.errorDlg('Service URL', self.textNoServiceUrlConfigurationError, 'ERROR');
                        }

                        if (settings.mapConfiguration.hasOwnProperty('showLayersOnStart')) {
                            AGS3xIoTAdmin.systemData.showLayersOnStart = settings.mapConfiguration.showLayersOnStart;
                        }
                        else {
                            AGS3xIoTAdmin.systemData.showLayersOnStart = true; // default
                            AGS3xIoTAdmin.util.util.errorDlg('Service URL', 'Variable showLayersOnStart not available', 'ERROR');
                        }
                        console.log('Settings.load - localhost, AGS3xIoTAdmin.systemData.showLayersOnStart (2): ', AGS3xIoTAdmin.systemData.showLayersOnStart);

                        // load start data
                        AGS3xIoTAdmin.util.events.fireEvent('applicationLoadStartData', settings.startPageCommand);
                    }

                }

                if (initializeError) {
                    Ext.Msg.alert(self.textInitApplication, initializeError);
                }
            },
            failure: function (response) {
                Ext.Msg.alert(self.textInitApplication, self.textSettingsNotReadError);
            }
        });
    }
});
