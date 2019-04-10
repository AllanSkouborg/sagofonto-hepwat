Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.authorization.userProfile', {
    username: '',
    fullname: '',
    company: {},
    email: '',
    roles: [],
    groups: [],
    sessionToken: '',

    constructor: function (config) {
        if (config) {
            if (config.username) this.username = config.username;
            if (config.fullname) this.fullname = config.fullname;
            if (config.company) this.company = config.company;
            if (config.email) this.email = config.email;
            if (config.roles) this.roles = config.roles;
            if (config.groups) this.groups = config.groups;
            if (config.sessionToken) this.sessionToken = config.sessionToken;
        }
        this.callParent(arguments);
    }
});
