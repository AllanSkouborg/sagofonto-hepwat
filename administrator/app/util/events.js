Ext.define('AGS3xIoTAdmin.util.events', {
    singleton: true,

    mixins: ['Ext.mixin.Observable'],

    constructor: function (config) {
        this.mixins.observable.constructor.call(this, config);
    }
});
