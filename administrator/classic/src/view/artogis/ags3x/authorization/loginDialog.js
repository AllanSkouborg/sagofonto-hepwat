Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.authorization.loginDialog', {
    extend: 'Ext.window.Window',
    alias: 'widget.loginDialog',
    requires: ['Ext.form.Panel'],

    /**
     * Text labels and headers for localization
     */

    // text content - default is Danish (da), for translations, see translation.js
    windowTitle: 'Log ind', // needs translation
    fieldLabelUsername: 'Brugernavn:', // needs translation
    fieldLabelPassword: 'Password:', // needs translation
    btnLoginText: 'Log ind', // needs translation
    /****/

    autoShow: true,
    height: 210,
    width: 300,
    closable: false,
    resizable: false,
    layout: 'fit',
    modal: true,

    initComponent: function () {
        this.setTitle(this.windowTitle);

        this.items = [
         {
             xtype: 'form',
             bodyPadding: 5,
             defaultButton: 'loginButton',
             defaults: {
                 anchor: '100%'
             },
             items: [
              {
                  xtype: 'textfield',
                  fieldLabel: this.fieldLabelUsername,
                  name: 'username',
                  allowBlank: false,
                  value: AGS3xIoTAdmin.systemData.defaults.login && AGS3xIoTAdmin.systemData.defaults.login.username ? AGS3xIoTAdmin.systemData.defaults.login.username : ''
              },
              {
                  xtype: 'textfield',
                  inputType: 'password',
                  fieldLabel: this.fieldLabelPassword,
                  name: 'password',
                  allowBlank: false,
                  value: AGS3xIoTAdmin.systemData.defaults.login && AGS3xIoTAdmin.systemData.defaults.login.password ? AGS3xIoTAdmin.systemData.defaults.login.password : ''
              }
             ],
             buttons: [
              {
                  reference: 'loginButton',
                  text: this.btnLoginText,
                  formBind: true,
                  disabled: false,
                  handler: function (b, e) {
                      var formDialog = b.up('loginDialog');
                      var form = b.up('form');

                      // fire custom event for the controller to handle
                      formDialog.fireEvent('login', formDialog, form, form.getValues());
                  }
              }
             ]
         }
        ];
        this.callParent();
    }
});
