Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.authorization.api', {
    baseUrl: null,

    onLoginSuccess: null,
    onLoginFailure: null,

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.authorization.userProfile',
        'AGS3xIoTAdmin.view.artogis.ags3x.authorization.loginDialog'
    ],

    siteListUrl: function () {
        return this.baseUrl + '/Site.svc/Sites/';
    },

    siteUrl: function (siteId) {
        if (siteId)
            return this.baseUrl + '/Site.svc/Site/' + siteId;
        else
            return this.baseUrl + '/Site.svc/Site/';
    },

    siteUpdateUrl: function (elementType) {
        return elementType ? this.baseUrl + '/Site.svc/SiteU/' + elementType : null;
    },

    siteDeleteUrl: function (elementType, elementId) {
        return elementId && elementType ? this.baseUrl + '/Site.svc/SiteD/' + elementType + '/' + elementId : null;
    },

    updateDefaultsUrl: function () {
        return this.baseUrl + '/Site.svc/UpdateDefaults/';
    },

    mapServiceProxyUrl: function (mapServiceId) {
        return this.baseUrl + '/MapServiceProxy?mapServiceId=' + mapServiceId;
    },

    uploadImageUrl: function (elementType, elementId) {
        return this.baseUrl + '/UploadImages?elementType=' + elementType + '&elementId=' + elementId;
    },

    headers: function () {
        var up = AGS3xIoTAdmin.systemData.currentUser;
        if (up) {
            return {
                //'Content-type': 'application/json',
                'Accept': 'application/json',
                'cache-control': 'no-cache',
                'SessionToken': up.sessionToken,
                'applicationId': AGS3xIoTAdmin.systemData.applicationId
            };
        }
    },

    constructor: function (options) {

        if (options) {
            if (options.baseUrl) this.baseUrl = options.baseUrl;
            if (options.onLoginSuccess) this.onLoginSuccess = options.onLoginSuccess;
            if (options.onLoginFailure) this.onLoginFailure = options.onLoginFailure;
        }
        if (AGS3xIoTAdmin.app) {
            AGS3xIoTAdmin.app.control({
                "loginDialog": {
                    login: this.onLogin
                }
            });
        }
    },

    login: function () {
        console.log('api.login - start...');
        var st = Ext.util.Cookies.get(AGS3xIoTAdmin.systemData.applicationId + '.sessionToken');
        if (st) {
            st = Ext.decode(st);
            this.refreshSession(st.sessionToken, this.onLoginSuccess, this.login);
        }
        else {
            var loginDialog = Ext.create('AGS3xIoTAdmin.view.artogis.ags3x.authorization.loginDialog');

            if (AGS3xIoTAdmin.systemData && AGS3xIoTAdmin.systemData.connection)
                loginDialog.api = AGS3xIoTAdmin.systemData.connection;
            else
                loginDialog.api = this;
        }
    },

    logout: function () {
        if (AGS3xIoTAdmin.systemData && AGS3xIoTAdmin.systemData.currentUser && AGS3xIoTAdmin.systemData.currentUser.sessionToken) {
            var clearSession = function () {
                AGS3xIoTAdmin.systemData.connection = null;
                AGS3xIoTAdmin.systemData.currentUser = null;
                AGS3xIoTAdmin.siteTreeController.setCurrentSite(null);
                AGS3xIoTAdmin.systemData = Ext.create('AGS3xIoTAdmin.view.artogis.ags3x.configuration.systemData');
                AGS3xIoTAdmin.systemData.prepare();
            };
            Ext.Ajax.request({
                url: this.baseUrl + '/Authorization.svc/DeAuthorize/',
                //method: 'POST',
                method: 'PUT',
                headers: {
                    //'Content-type': 'application/json',
                    'Accept': 'application/json',
                    'cache-control': 'no-cache',
                    'SessionToken': AGS3xIoTAdmin.systemData.currentUser.sessionToken
                },
                success: function (response) {
                    clearSession();
                },
                failure: function (response) {
                    clearSession();
                }
            });
        }
    },

    onLogin: function (formDialog, loginForm, params) {

        AGS3xIoTAdmin.startLogin = Date.now();

        // authenticate
        var loginApi = formDialog.api;
        loginApi.authenticate(params,
            function (userProfile) { /* Success */
                //TODO Verifica daca e necesar
                if (loginApi.onLoginSuccess) {

                    var loginOk = false;
                    if (!AGS3xIoTAdmin.systemData.adminRoles) AGS3xIoTAdmin.systemData.adminRoles = ['AdminGroup', 'SystemAdminGroup'];

                    var ar = 0;
                    for (ar = 0; ar < AGS3xIoTAdmin.systemData.adminRoles.length; ar++) {
                        var adminRole = AGS3xIoTAdmin.systemData.adminRoles[ar];
                        if (adminRole) {
                            adminRole = adminRole.toLowerCase();

                            var ur = 0;
                            for (ur = 0; ur < userProfile.roles.length; ur++) {
                                var urn = userProfile.roles[ur].name;
                                if (urn) {
                                    urn = urn.toLowerCase();
                                    if (urn === adminRole) {
                                        loginOk = true;
                                        break;
                                    }
                                }
                            }

                            if (!loginOk) {
                                var ug = 0;
                                for (ug = 0; ug < userProfile.groups.length; ug++) {
                                    var ugn = userProfile.groups[ug].name;
                                    if (ugn) {
                                        ugn = ugn.toLowerCase();
                                        if (ugn === adminRole) {
                                            loginOk = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!loginOk) {
                        Ext.Msg.alert("Invalid credentials", "The credentials used are not authorized for admin or systemadmin access.", // needs translation
                            function () {
                                loginForm.getForm().reset();
                            });
                        if (loginApi.onFailure) loginApi.onLoginFailure(response);
                    }
                    else {
                        // destroy login dialog
                        formDialog.destroy();
                        loginApi.onLoginSuccess(userProfile);


                    }
                }
            },
            function (response) { /* Failure */
                Ext.Msg.alert("Invalid credentials", "The credentials used could not be validated. Please try different username or password.", // needs translation
                    function () {
                        loginForm.getForm().reset();
                    }
                );

                if (loginApi.onFailure) loginApi.onLoginFailure(response);
            }
         );
    },

    authenticate: function (params, callbackSuccess, callbackFailure) {

        Ext.Ajax.request({
            url: this.baseUrl + '/Authorization.svc/Authorize/',
            //method: 'POST',
            method: 'PUT',
            headers: {
                //'Content-type': 'application/json',
                'Accept': 'application/json',
                'cache-control': 'no-cache',
                'Authorization': 'Basic: ' + Ext.util.Base64.encode(params.username + ':' + params.password)
            },
            success: function (response) {
                var authorizationResponse = Ext.decode(response.responseText);
                if (authorizationResponse && authorizationResponse.success === true) {
                    if (callbackSuccess) {
                        var up = Ext.create('AGS3xIoTAdmin.view.artogis.ags3x.authorization.userProfile', authorizationResponse.userProfile);
                        var st = { sessionToken: authorizationResponse.userProfile.sessionToken };
                        Ext.util.Cookies.set(AGS3xIoTAdmin.systemData.applicationId + '.sessionToken', Ext.encode(st));
                        callbackSuccess(up);

                        // Load first panel
                        console.log('authenticate - callbackSuccess');
                        AGS3xIoTAdmin.util.events.fireEvent('showDataRobotsPanel', null);
                    }
                }
                else if (callbackFailure) {
                    callbackFailure(authorizationResponse);
                }
            },
            failure: function (response) {
                if (callbackFailure) {
                    callbackFailure(response);
                }
            }
        });
    },

    refreshSession: function (sessionToken, callbackSuccess, callbackFailure) {
        Ext.util.Cookies.clear(AGS3xIoTAdmin.systemData.applicationId + '.sessionToken');
        Ext.Ajax.request({
            url: this.baseUrl + '/Authorization.svc/Validate/',
            //method: 'POST',
            method: 'PUT',
            headers: {
                //'Content-type': 'application/json',
                'Accept': 'application/json',
                'cache-control': 'no-cache',
                'SessionToken': sessionToken
            },
            success: function (response) {
                var authorizationResponse = Ext.decode(response.responseText);
                if (authorizationResponse && authorizationResponse.success === true) {
                    if (callbackSuccess) {
                        var up = Ext.create('AGS3xIoTAdmin.view.artogis.ags3x.authorization.userProfile', authorizationResponse.userProfile);
                        var st = { sessionToken: authorizationResponse.userProfile.sessionToken };
                        Ext.util.Cookies.set(AGS3xIoTAdmin.systemData.applicationId + '.sessionToken', Ext.encode(st));
                        callbackSuccess(up);

                        // Load first panel
                        console.log('refreshSession - callbackSuccess');
                        AGS3xIoTAdmin.util.events.fireEvent('showDataRobotsPanel', null);
                    }
                }
                else if (callbackFailure) {
                    callbackFailure(authorizationResponse);
                }
            },
            failure: function (response) {
                var o = Ext.decode(response.responseText);
                if (callbackFailure) {
                    callbackFailure(o);
                }
            }
        });
    }
});
