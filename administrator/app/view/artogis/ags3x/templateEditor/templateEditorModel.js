Ext.define('AGS3xIoTAdmin.view.artogis.ags3x.templateEditor.templateEditorModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ags3xTemplateEditor',

    stores: {
        templatesData: {
            fields: [
                'id',
                'templateType',
                'measurementAlias',
                'measurementName',
                'measurementType',
                'calculationAndStores'
            ],
            data: []
        },

        measurementTypesDataStore: {
            fields: [
                'id',
                'name',
                'isSignalStrength',
                'isBatteryStatus'
            ],
            data: []
        },

        selectedTemplateStore: {
            fields: [
                'id',
                'templateType',
                'measurementAlias',
                'measurementName',
                'measurementType',
                'calculationAndStores'
            ],
            data: []
        },

        selectedAggregationAndStores: {
            fields: [
                'aggregationCalculation',
                'aggregate',
                'unit',
                'scaleToUnit',
                'aggregationInterval',

                'status',
                'max',
                'high',
                'low',
                'min'
            ],
            data: []
        },
        
        boolTypes: {
            fields: [
                'type',
                'name'
            ],
            data: [
                { 'type': true, 'name': 'Yes' },
                { 'type': false, 'name': 'No' }
            ]
        },

        measurementsData: {
            fields: [
                'measurementType',
                'measurementAlias'
            ],
            data: []
        },

        unitTypesStore: {
            fields: [
                'id',
                'name',
                'description'
            ],
            data: []
        },
        unitTypesStoreAll: {
            fields: [
                'id',
                'name',
                'description'
            ],
            data: []
        }
    },

    data: {
        calculationGridColumns: []
    },

    emptyTemplate: {
        id: null,
        measurementAlias: null,
        measurementName: null,
        measurementType: null,
        templateType: null,

        // is used for template when creating new template
        calculationAndStores: [
            {
                calculation: 0,
                calculationType: "None",
                formula: null,
                aggregationAndStores: [
                    {
                        aggregate: true,
                        aggregationCalculation: "None",
                        aggregationInterval: 0,
                        aggregationType: 0,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Average",
                        aggregationInterval: 5,
                        aggregationType: 1,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Average",
                        aggregationInterval: 60,
                        aggregationType: 2,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Sum",
                        aggregationInterval: 1440,
                        aggregationType: 3,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Delta Sum",
                        aggregationInterval: 10,
                        aggregationType: 4,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    }
                ]
            },
            {
                calculation: 1,
                calculationType: "Pre",
                formula: null,
                aggregationAndStores: [
                    {
                        aggregate: true,
                        aggregationCalculation: "None",
                        aggregationInterval: 0,
                        aggregationType: 0,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Average",
                        aggregationInterval: 5,
                        aggregationType: 1,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Average",
                        aggregationInterval: 60,
                        aggregationType: 2,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Sum",
                        aggregationInterval: 1440,
                        aggregationType: 3,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Delta Sum",
                        aggregationInterval: 10,
                        aggregationType: 4,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    }
                ]
            },
            {
                calculation: 2,
                calculationType: "Usage",
                formula: null,
                aggregationAndStores: [
                    {
                        aggregate: true,
                        aggregationCalculation: "None",
                        aggregationInterval: 0,
                        aggregationType: 0,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Average",
                        aggregationInterval: 5,
                        aggregationType: 1,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Average",
                        aggregationInterval: 60,
                        aggregationType: 2,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Sum",
                        aggregationInterval: 1440,
                        aggregationType: 3,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Delta Sum",
                        aggregationInterval: 10,
                        aggregationType: 4,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    }
                ]
            },
            {
                calculation: 3,
                calculationType: "Quality",
                formula: null,
                aggregationAndStores: [
                    {
                        aggregate: true,
                        aggregationCalculation: "None",
                        aggregationInterval: 0,
                        aggregationType: 0,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Average",
                        aggregationInterval: 5,
                        aggregationType: 1,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Average",
                        aggregationInterval: 60,
                        aggregationType: 2,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Sum",
                        aggregationInterval: 1440,
                        aggregationType: 3,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    },
                    {
                        aggregate: true,
                        aggregationCalculation: "Delta Sum",
                        aggregationInterval: 10,
                        aggregationType: 4,
                        store: true,
                        unit: null,
                        scaleToUnit: 1
                    }
                ]
            }
        ]
    }
});