Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.formulaEditor.formulaEditorController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ags3xFormulaEditor',

    requires: [
       'AGS3xIoTAdmin.util.events',
       'AGS3xIoTAdmin.util.util'
    ],

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelCalculationsCalculation: 'Beregning',
    fieldLabelCalculationsFormula: 'Formular',
    fieldLabelCalculationsType: 'Type',
    fieldLabelCalculationsData: 'Data',
    fieldLabelCalculationsUnit: 'Enhed',
    fieldLabelCalculationsStore: 'Gem',
    fieldLabelCalculationsAverageTwoMinutes: 'Gennemsnit 2 min.',
    fieldLabelCalculationsAverageSixtyMinutes: 'Gennemsnit 60 min.',
    fieldLabelCalculationsSumSixtyMinutes: 'Sum 60 min.',
    fieldLabelCalculationsOpenModel: 'Åbn beregningsmodel',
    fieldLabelCalculationsOpenFormulaEditor: 'Åbn formulareditor',
    fieldLabelCalculationsAggregationAndStore: 'Aggregering og lagring',
    fieldLabelCalculationsWindowTitle: 'Beregninger',

    fieldLabelFormulaEditorFormulaValidation: 'Indtast sensorværdi',
    fieldLabelFormulaEditorNameFormulaPerformValidate: 'Validér',
    fieldLabelFormulaEditorNameFormulaCancelValidate: 'Fortryd',
    fieldLabelFormulaEditorNameFormulaPerformValidateResult: 'Resultat',

    baseUrl: null,

    loadFormula: function (recordIndex) {
        var self = this;

        var existingFormulas = self.getViewModel().getStore('existingFormulas');
        console.log('dataIOController.loadFormula - existingFormulas: ', existingFormulas);

        for (var i = 0; i < existingFormulas.data.items.length; i++) {
            var formula = existingFormulas.data.items[i].data;
            if (recordIndex == i) {
                console.log('HIT - formula: ', formula);
                var inputElement = document.getElementById('formulaEditorCalculationTextField-inputEl');
                inputElement.value = formula.string;
                break;
            }
        }
    },
    applyFormula: function (transferData) {
        var self = this;

        console.log('dataIOController.applyFormula - transferData: ', transferData);

        // apply record data
        if (transferData.dataTarget) {
            transferData.dataTarget.object[transferData.dataTarget.key] = transferData.formulaString;
        }

        // apply dom element data
        if (transferData.domTarget) {
            if (transferData.domTarget.tagName == 'input') {
                transferData.domTarget.value = transferData.formulaString;
            }
            else {
                transferData.domTarget.innerHTML = transferData.formulaString;
            }
            
        }

        if (Ext.ComponentQuery.query('#formulaEditorWindow').length > 0) {
            Ext.ComponentQuery.query('#formulaEditorWindow')[0].destroy();
        }
    },
    saveFormula: function (index, dataSource) {
        var self = this;

        self.formulaEditorSaveWindow = new Ext.Window({
            id: 'formulaEditorSaveWindow',
            header: {
                title: self.fieldLabelFormulaEditorNameFormula,
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important',
                    'text-align': 'center !important'
                },
                bodyStyle: {
                    'text-align': 'center !important'
                }
            },
            closable: true,
            width: 300,
            height: 102,
            x: ((window.innerWidth - 300) / 2),
            y: ((window.innerHeight - 100) / 2),
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
            items: [
                {
                    xtype: 'panel',
                    id: 'formulaEditorSaveWindowInputHolder',
                    items: [
                        {
                            xtype: 'textfield',
                            id: 'formulaEditorSaveWindowTextField',
                            width: 280,
                            height: 20,
                            margin: '0 10px 10px 10px',
                            style: {

                            }
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'formulaEditorSaveWindowButtonsHolder',
                    items: [
                        {
                            xtype: 'button',
                            text: self.fieldLabelFormulaEditorNameFormulaSave,
                            style: {
                                'float': 'right',
                                'margin-right': '10px'
                            },
                            handler: function () {
                                console.log('Perform save formula');
                                self.performSaveFormula(document.getElementById('formulaEditorSaveWindowTextField-inputEl').value);
                            }
                        },
                        {
                            xtype: 'button',
                            text: self.fieldLabelFormulaEditorNameFormulaCancel,
                            style: {
                                'float': 'right',
                                'margin-right': '10px'
                            },
                            handler: function () {
                                console.log('Cancel save formula');
                                self.formulaEditorSaveWindow.destroy();
                            }
                        }
                    ]
                }
            ]
        }).show();

        document.getElementById('formulaEditorSaveWindowTextField-inputEl').focus();
    },

    // NOT IMPLEMENTED IN CURRENT VERSION
    performSaveFormula: function (name) {
        var self = this;

        console.log('dataIOController.performSaveFormula - name: ', name);

        // perform save
        alert('TBD - save formula here: ' + name);

        self.formulaEditorSaveWindow.destroy();
    },

    addVariableToFormula: function (variableName) {
        var self = this;

        variableName = '{{' + variableName + '}}';

        var inputElement = document.getElementById('formulaEditorCalculationTextField-inputEl');

        var existingValue = inputElement.value;
        console.log('dataIOController.addVariableToFormula - exsiting value: ', existingValue);

        //IE support
        if (document.selection) {
            inputElement.focus();
            sel = document.selection.createRange();
            sel.text = variableName;
        }
            // Microsoft Edge
        else if (window.navigator.userAgent.indexOf("Edge") > -1) {
            var startPos = inputElement.selectionStart;
            var endPos = inputElement.selectionEnd;

            inputElement.value = inputElement.value.substring(0, startPos) + variableName + inputElement.value.substring(endPos, inputElement.value.length);

            var pos = startPos + variableName.length;
            inputElement.focus();
            inputElement.setSelectionRange(pos, pos);
        }
            //MOZILLA and others
        else if (inputElement.selectionStart || inputElement.selectionStart == '0') {
            var startPos = inputElement.selectionStart;
            var endPos = inputElement.selectionEnd;
            inputElement.value = inputElement.value.substring(0, startPos) + variableName + inputElement.value.substring(endPos, inputElement.value.length);
        } else {
            inputElement.value += variableName;
        }
    },
    addMathChunkToFormula: function (mathChunk) {
        var self = this;

        var inputElement = document.getElementById('formulaEditorCalculationTextField-inputEl');

        mathChunk = ' ' + mathChunk + ' ';

        var existingValue = inputElement.value;
        console.log('dataIOController.addVariableToFormula - exsiting value: ', existingValue);

        //IE support
        if (document.selection) {
            inputElement.focus();
            sel = document.selection.createRange();
            sel.text = mathChunk;
        }
            // Microsoft Edge
        else if (window.navigator.userAgent.indexOf("Edge") > -1) {
            var startPos = inputElement.selectionStart;
            var endPos = inputElement.selectionEnd;

            inputElement.value = inputElement.value.substring(0, startPos) + mathChunk + inputElement.value.substring(endPos, inputElement.value.length);

            var pos = startPos + mathChunk.length;
            inputElement.focus();
            inputElement.setSelectionRange(pos, pos);
        }
            //MOZILLA and others
        else if (inputElement.selectionStart || inputElement.selectionStart == '0') {
            var startPos = inputElement.selectionStart;
            var endPos = inputElement.selectionEnd;
            inputElement.value = inputElement.value.substring(0, startPos) + mathChunk + inputElement.value.substring(endPos, inputElement.value.length);
        } else {
            inputElement.value += mathChunk;
        }
    },
    validateFormula: function (transferData) {
        var self = this;

        console.log('dataIOController.validateFormula - transferData: ', transferData);

        var params = {
            id: transferData.dataId,
            formula: document.getElementById('formulaEditorCalculationTextField-inputEl').value
        }

        self.formulaEditorValidationWindow = new Ext.Window({
            id: 'formulaEditorValidationWindow',
            header: {
                title: self.fieldLabelFormulaEditorFormulaValidation,
                style: {
                    'text-align': 'center',
                    'border': '0 !important',
                    'border-radius': '0px !important',
                    'background': '#ffffff !important',
                    'text-align': 'center !important'
                },
                bodyStyle: {
                    'text-align': 'center !important'
                }
            },
            closable: true,
            width: 300,
            height: 132,
            x: ((window.innerWidth - 300) / 2),
            y: ((window.innerHeight - 132) / 2),
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
            items: [
                {
                    xtype: 'panel',
                    id: 'formulaEditorValidationWindowInputHolder',
                    items: [
                        {
                            xtype: 'textfield',
                            id: 'formulaEditorValidationWindowTextField',
                            width: 280,
                            height: 20,
                            margin: '0 10px 10px 10px'
                        },
                        {
                            xtype: 'panel',
                            id: 'formulaEditorValidationWindowResult',
                            width: 280,
                            height: 20,
                            margin: '0 10px 10px 10px',
                            html: '<span id="validationResultSpan" style="float:left;"></span>'
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'formulaEditorValidationWindowButtonsHolder',
                    items: [
                        {
                            xtype: 'button',
                            text: self.fieldLabelFormulaEditorNameFormulaPerformValidate,
                            style: {
                                'float': 'right',
                                'margin-right': '10px'
                            },
                            handler: function () {
                                console.log('Perform formula validation');

                                var validationValue = document.getElementById('formulaEditorValidationWindowTextField-inputEl').value;

                                Ext.Ajax.request({
                                    scope: self,
                                    url: self.baseUrl + 'configurationformula?measurementvalue=' + validationValue,
                                    method: 'PUT',
                                    contentType: 'application/json',
                                    jsonData: Ext.JSON.encode(params),
                                    callback: function (options, success, response) {
                                        if (success) {
                                            try {
                                                var result = Ext.decode(response.responseText);
                                                console.log('dataIOController.validateFormula - result: ', result);
                                                var jsonObjectResult = Ext.decode(result.JsonObject);

                                                document.getElementById('validationResultSpan').innerHTML = self.fieldLabelFormulaEditorNameFormulaPerformValidateResult + ': ' + jsonObjectResult.result;
                                            }
                                            catch (exception) {
                                                console.log('dataIOController.validateFormula - exception: ', exception);
                                            }
                                        }
                                    }
                                });
                            }
                        },
                        {
                            xtype: 'button',
                            text: self.fieldLabelFormulaEditorNameFormulaCancelValidate,
                            style: {
                                'float': 'right',
                                'margin-right': '10px'
                            },
                            handler: function () {
                                console.log('Cancel formula validation');
                                self.formulaEditorValidationWindow.destroy();
                            }
                        }
                    ]
                }
            ]
        }).show();

        document.getElementById('formulaEditorValidationWindowTextField-inputEl').focus();

    },

    init: function () {
        var self = this;

        self.baseUrl = AGS3xIoTAdmin.systemData.serviceUrl;

        AGS3xIoTAdmin.util.events.on('loadFormula', self.loadFormula, self);
        AGS3xIoTAdmin.util.events.on('applyFormula', self.applyFormula, self);
        AGS3xIoTAdmin.util.events.on('addMathChunkToFormula', self.addMathChunkToFormula, self);
        AGS3xIoTAdmin.util.events.on('addVariableToFormula', self.addVariableToFormula, self);
        AGS3xIoTAdmin.util.events.on('saveFormula', self.saveFormula, self);
        AGS3xIoTAdmin.util.events.on('validateFormula', self.validateFormula, self);
    }
});