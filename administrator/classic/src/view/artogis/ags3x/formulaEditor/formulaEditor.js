var self;

Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.formulaEditor.formulaEditor', {

    extend: 'Ext.panel.Panel',
    xtype: 'ags3xFormulaEditor',
    id: 'ags3xFormulaEditor',

    requires: [
     'AGS3xIoTAdmin.view.artogis.ags3x.formulaEditor.formulaEditorController',
     'AGS3xIoTAdmin.view.artogis.ags3x.formulaEditor.formulaEditorModel'
    ],

    viewModel: {
        type: 'ags3xFormulaEditor'
    },

    controller: 'ags3xFormulaEditor',

    // text content - default is Danish (da), for translations, see translation.js
    fieldLabelFormulaEditorWindowTitle: 'Formular-editor',
    fieldLabelFormulaEditorSavedFormulas: 'Gemte formularer',
    fieldLabelFormulaEditorLoadFormula: 'Indlæs formular',
    fieldLabelFormulaEditorFormula: 'Formel',
    fieldLabelFormulaEditorMathML: 'MathML',
    fieldLabelFormulaEditorJavaScriptString: 'JavaScript-streng',
    fieldLabelFormulaEditorUseFormula: 'Brug formular',
    fieldLabelFormulaEditorResult: 'Resultat &nbsp; = ',
    fieldLabelFormulaEditorInsertPi: 'Indsæt &Pi;',
    fieldLabelFormulaEditorInsertSquared: 'Indsæt &#8730;',
    fieldLabelFormulaEditorInsertPowerTwo: 'Indsæt x&#178;',
    fieldLabelFormulaEditorInsertPowerThree: 'Indsæt x&#179;',
    fieldLabelFormulaEditorInsertSensorVariable: 'Indsæt sensorvariabel',
    fieldLabelFormulaEditorSaveFormula: 'Gem formular',
    fieldLabelFormulaEditorValidateFormula: 'Validér formular',
    fieldLabelFormulaEditorVariablesComponentsObjects: 'Variabler fra komponent eller objekt',
    fieldLabelFormulaEditorInsertVariable: 'Indsæt variabel i formular',
    fieldLabelFormulaEditorNameFormula: 'Navngiv formular',
    fieldLabelFormulaEditorNameFormulaSave: 'Gem',
    fieldLabelFormulaEditorNameFormulaCancel: 'Fortryd',

    fieldLabelFormulaEditorFormulaValidation: 'Indtast sensorværdi',
    fieldLabelFormulaEditorNameFormulaPerformValidate: 'Validér',
    fieldLabelFormulaEditorNameFormulaCancelValidate: 'Fortryd',
    fieldLabelFormulaEditorNameFormulaPerformValidateResult: 'Resultat',

    id: 'formulaEditorPanel',
    objectDataVariablesStore: null,

    initComponent: function () {
        self = this;

        console.log('formularEditor.initcomponent - transferData: ', self.transferData);

        self.objectDataVariablesStore =  {
            fields: ['name'],
            data: self.transferData.objectDataVariables
        };

        self.items = [
        {
            xtype: 'panel',
            id: 'formulaEditorLeft',
            hidden: true,
            height: 400,
            width: 300,
            style: {
                'float': 'left'
            },
            bodyStyle: {

            },
            items: [
                {
                    xtype: 'panel',
                    id: 'formulaEditorExistingFormulasList',
                    height: 360,
                    width: 300,
                    padding: '0 10px 0 10px',
                    style: {

                    },
                    bodyStyle: {

                    },
                    items: [
                        {
                            xtype: 'gridpanel',
                            id: 'formularEditorExistingFormulasListGrid',
                            height: 360,

                            multiSelect: false,
                            frame: true,
                            allowDeselect: true,
                            scrollable: true,
                            store: self.getViewModel().getStore('existingFormulas'),
                            /*bind: {
                                store: '{dataIOVariables}',
                            },*/
                            columns: [
                                {
                                    text: self.fieldLabelFormulaEditorSavedFormulas,
                                    dataIndex: 'name',
                                    flex: 1,
                                    tdCls: 'existing-formula'
                                }
                            ],
                            listeners: {
                                itemclick: function () {
                                    console.log('dataIOController:formular editor - variable click');
                                    if (document.getElementById('formularEditorExistingFormulasListGrid').getElementsByClassName('x-grid-item-selected').length > 0) {
                                        Ext.ComponentQuery.query('#buttonLoadExistingFormula')[0].setDisabled(false);
                                    }
                                    else {
                                        Ext.ComponentQuery.query('#buttonLoadExistingFormula')[0].setDisabled(true);
                                    }

                                }
                            }
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'formulaEditorExistingFormulasListButtonHolder',
                    height: 30,
                    width: 300,
                    margin: '10px 0px 0px 0px',
                    style: {

                    },
                    bodyStyle: {

                    },
                    items: [
                        {
                            xtype: 'button',
                            text: self.fieldLabelFormulaEditorLoadFormula,
                            id: 'buttonLoadExistingFormula',
                            disabled: true,
                            iconCls: 'fa fa-arrow-right',
                            handler: function () {
                                var tableElement = document.getElementById('formularEditorExistingFormulasListGrid').getElementsByClassName('x-grid-item-selected')[0];
                                var recordIndex = tableElement.getAttribute('data-recordindex');
                                var formulaName = tableElement.getElementsByClassName('existing-formula')[0].getElementsByClassName('x-grid-cell-inner ')[0].innerHTML;
                                console.log('recordIndex: ', recordIndex);
                                console.log('formulaName: ', formulaName);

                                AGS3xIoTAdmin.util.events.fireEvent('loadFormula', recordIndex);
                            }
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'panel',
            id: 'formulaEditorCenter',
            height: 400,
            width: 500,
            padding: '0 9px 0 0',
            style: {
                'float': 'left',
                'border-right': '1px solid #e6e0e0'
                /*,
                'border-left': '1px solid #aaa'*/
            },
            bodyStyle: {

            },
            items: [
                {
                    xtype: 'panel',
                    id: 'formulaEditorLeftTop',
                    height: 30,
                    width: 500,
                    style: {

                    },
                    bodyStyle: {

                    },
                    items: [
                        {
                            xtype: 'button',
                            text: self.fieldLabelFormulaEditorFormula,
                            hidden: true,
                            pressed: false,
                            disabled: true,
                            margin: '0 0 0 10px',
                            style: {
                                'float': 'left'
                            }
                        },
                        {
                            xtype: 'button',
                            text: self.fieldLabelFormulaEditorMathML,
                            hidden: true,
                            pressed: false,
                            disabled: true,
                            margin: '0 0 0 10px',
                            style: {
                                'float': 'left'
                            }
                        },
                        {
                            xtype: 'button',
                            text: self.fieldLabelFormulaEditorJavaScriptString,
                            pressed: true,
                            margin: '0 0 0 10px',
                            style: {
                                'float': 'left'
                            }
                        },
                        {
                            xtype: 'button',
                            text: self.fieldLabelFormulaEditorUseFormula,
                            margin: '0 10px 0 0',
                            style: {
                                'float': 'right'
                            },
                            iconCls: 'fa fa-check',
                            handler: function () {
                                self.transferData.formulaString = Ext.ComponentQuery.query('#formulaEditorCalculationTextField')[0].getValue();
                                AGS3xIoTAdmin.util.events.fireEvent('applyFormula', self.transferData);
                            }
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'formulaEditorLeftMiddle',
                    height: 310,
                    width: 500,
                    style: {

                    },
                    bodyStyle: {

                    },
                    items: [
                        {
                            xtype: 'panel',
                            id: 'formulaEditorResultHolder',
                            height: 340,
                            width: 100,
                            style: {
                                'float': 'left'
                            },
                            bodyStyle: {

                            },
                            items: [
                                {
                                    xtype: 'container',
                                    id: 'formulaEditorResultText',
                                    html: self.fieldLabelFormulaEditorResult,
                                    width: 100,
                                    height: 370,
                                    padding: '0 10px 0 0',
                                    style: {
                                        'line-height': '370px',
                                        'text-align': 'right',
                                        'float': 'left'
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            id: 'formulaEditorCalculationHolder',
                            height: 340,
                            width: 400,
                            style: {
                                'float': 'left'
                            },
                            bodyStyle: {

                            },
                            items: [
                                {
                                    xtype: 'textareafield',
                                    id: 'formulaEditorCalculationTextField',
                                    width: 380,
                                    height: 60,
                                    margin: '155px 10px 0 10px',
                                    value: self.transferData.formulaString,
                                    style: {

                                        'float': 'left'
                                    }
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'formulaEditorLeftBottom',
                    height: 60,
                    width: 500,
                    style: {

                    },
                    bodyStyle: {

                    },
                    items: [
                        {
                            xtype: 'panel',
                            id: 'formulaEditorLeftBottomUpper',
                            width: 500,
                            height: 30,
                            items: [
                                {
                                    xtype: 'button',
                                    text: self.fieldLabelFormulaEditorInsertPi,
                                    margin: '0 0 0 10px',
                                    style: {
                                        'float': 'left'
                                    },
                                    handler: function () {
                                        AGS3xIoTAdmin.util.events.fireEvent('addMathChunkToFormula', 'Math.PI');
                                    }
                                },
                                {
                                    xtype: 'button',
                                    text: self.fieldLabelFormulaEditorInsertSquared,
                                    margin: '0 0 0 10px',
                                    style: {
                                        'float': 'left'
                                    },
                                    handler: function () {
                                        AGS3xIoTAdmin.util.events.fireEvent('addMathChunkToFormula', 'Math.sqrt()');
                                    }
                                },
                                {
                                    xtype: 'button',
                                    text: self.fieldLabelFormulaEditorInsertPowerTwo,
                                    margin: '0 0 0 10px',
                                    style: {
                                        'float': 'left'
                                    },
                                    handler: function () {
                                        AGS3xIoTAdmin.util.events.fireEvent('addMathChunkToFormula', 'Math.pow(valueHere, 2)');
                                    }
                                },
                                {
                                    xtype: 'button',
                                    text: self.fieldLabelFormulaEditorInsertPowerThree,
                                    margin: '0 0 0 10px',
                                    style: {
                                        'float': 'left'
                                    },
                                    handler: function () {
                                        AGS3xIoTAdmin.util.events.fireEvent('addMathChunkToFormula', 'Math.pow(valueHere, 3)');
                                    }
                                },
                                {
                                    xtype: 'button',
                                    text: self.fieldLabelFormulaEditorInsertSensorVariable,
                                    margin: '0 0 0 10px',
                                    style: {
                                        'float': 'left'
                                    },
                                    iconCls: 'fa fa-plug',
                                    handler: function () {
                                        AGS3xIoTAdmin.util.events.fireEvent('addVariableToFormula', 'sensor_output_value');
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            id: 'formulaEditorLeftBottomLower',
                            width: 500,
                            height: 30,
                            items: [

                                {
                                    xtype: 'button',
                                    text: self.fieldLabelFormulaEditorSaveFormula,
                                    hidden: true,
                                    margin: '0 0 0 10px',
                                    style: {
                                        'float': 'left'
                                    },
                                    iconCls: 'fa fa-floppy-o',
                                    handler: function () {
                                        AGS3xIoTAdmin.util.events.fireEvent('saveFormula', index, dataSource);
                                    }
                                },
                                {
                                    xtype: 'button',
                                    text: self.fieldLabelFormulaEditorValidateFormula,
                                    disabled: ((self.transferData.objectDataVariables && self.transferData.objectDataVariables.length > 0) ? false : true),
                                    margin: '0 0 0 10px',
                                    style: {
                                        'float': 'left'
                                    },
                                    iconCls: 'fa fa-stethoscope',
                                    handler: function () {
                                        AGS3xIoTAdmin.util.events.fireEvent('validateFormula', self.transferData);
                                    }
                                }
                            ]
                        }

                    ]

                }
            ]
        },
        {
            xtype: 'panel',
            id: 'formulaEditorRight',
            height: 400,
            width: 300,
            style: {
                'float': 'right'
            },
            items: [
                {
                    xtype: 'panel',
                    id: 'formulaEditorList',
                    height: 360,
                    width: 300,
                    padding: '0 10px 0 10px',
                    items: [
                        {
                            xtype: 'gridpanel',
                            id: 'formularEditorListGrid',
                            height: 360,
                            multiSelect: false,
                            frame: true,
                            allowDeselect: true,
                            scrollable: true,
                            store: self.objectDataVariablesStore,
                            columns: [
                                {
                                    text: self.fieldLabelFormulaEditorVariablesComponentsObjects,
                                    dataIndex: 'name',
                                    flex: 1,
                                    tdCls: 'formula-variable'
                                }
                            ],
                            listeners: {
                                itemclick: function () {
                                    console.log('dataIOController:formular editor - variable click');
                                    if (document.getElementById('formularEditorListGrid').getElementsByClassName('x-grid-item-selected').length > 0) {
                                        Ext.ComponentQuery.query('#buttonInsertDataVariableIntoFormula')[0].setDisabled(false);
                                    }
                                    else {
                                        Ext.ComponentQuery.query('#buttonInsertDataVariableIntoFormula')[0].setDisabled(true);
                                    }

                                }
                            }
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'formulaEditorListButtonHolder',
                    height: 30,
                    width: 300,
                    margin: '10px 0px 0px 0px',
                    items: [
                        {
                            xtype: 'button',
                            text: self.fieldLabelFormulaEditorInsertVariable,
                            id: 'buttonInsertDataVariableIntoFormula',
                            disabled: true,
                            iconCls: 'fa fa-arrow-left',
                            handler: function () {
                                var tableElement = document.getElementById('formularEditorListGrid').getElementsByClassName('x-grid-item-selected')[0];
                                var variableName = tableElement.getElementsByClassName('formula-variable')[0].getElementsByClassName('x-grid-cell-inner ')[0].innerHTML;
                                console.log('variableName: ', variableName);
                                AGS3xIoTAdmin.util.events.fireEvent('addVariableToFormula', variableName);
                            }
                        }
                    ]
                }
            ]
        }

        ]

        self.listeners = {
            afterrender: function () {
                console.log('formulaEditor.afterrender');

                if (Ext.ComponentQuery.query('#formulaEditorWindow').length > 0) {
                    Ext.ComponentQuery.query('#formulaEditorWindow')[0].setTitle(self.fieldLabelFormulaEditorWindowTitle);
                }
            }
        }

        self.callParent(arguments);
    }
})