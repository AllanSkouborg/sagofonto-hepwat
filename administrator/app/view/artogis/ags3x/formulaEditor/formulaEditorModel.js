Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.formulaEditor.formulaEditorModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ags3xFormulaEditor',

    stores: {
        existingFormulas: {
            fields: [
                'id',
                'name',
                'string'
            ],
            data: [
                { 'id': 1, 'name': 'Overløb', 'string': 'C x B x h x Math.sqrt(2 x g x h)' }
            ]
        }
    },

    data: {
    
    }
});