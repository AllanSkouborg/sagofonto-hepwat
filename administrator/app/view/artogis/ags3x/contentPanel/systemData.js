Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.configuration.systemData', {

    connection: null,
    currentUser: null,
    currentSite: null,

    textMsgHeader: 'Hent sideoversigt',
    textErrorMsg: 'Fejl: sideoversigt blev ikke hentet',

    requires: [
        'AGS3xIoTAdmin.view.artogis.ags3x.authorization.userProfile',
        'AGS3xIoTAdmin.view.artogis.ags3x.authorization.loginDialog'
    ],

    load: function (userProfile, currentSiteId) {
        var self = this;

        if (userProfile) AGS3xIoTAdmin.systemData.currentUser = userProfile;

        return;

        if (!currentSiteId) currentSiteId = AGS3xIoTAdmin.siteTreeController.getlastSelectedSiteId();

        var msgHeader = self.textMsgHeader; // needs translation
        var errorMsg = self.textErrorMsg; // needs translation
        var headers = AGS3xIoTAdmin.systemData.connection.headers();

        headers.includeMapServiceData = 'true';
        headers.includeProjectionData = 'true';
        headers.includeToolTypeData = 'true';

        Ext.Ajax.request({
            url: AGS3xIoTAdmin.systemData.connection.siteListUrl(),
            headers: headers,
            method: 'GET',
            disableCaching: true,
            success: function (response) {
                var siteResponse = Ext.decode(response.responseText);
                if (siteResponse) {
                    if (siteResponse.success === true) {

                        var siteExists = false;

                        if (currentSiteId && siteResponse.siteList) {
                            for (var s = 0; s < siteResponse.siteList.length; s++) {
                                if (siteResponse.siteList[s].siteId === currentSiteId) {
                                    siteExists = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        var msg = siteResponse.message;
                        var err = siteResponse.error;
                        var inf = errorMsg;
                        if (msg && msg.length > 0) inf += ": " + msg;
                        if (err && err.length > 0) inf += ": " + err;
                        Ext.Msg.alert(msgHeader, inf);
                    }
                }
                else {
                    Ext.Msg.alert(msgHeader, errorMsg);
                }
            },
            failure: function (response) {
                Ext.Msg.alert(msgHeader, errorMsg);
            }
        });
    }
});
