var require = {
    'paths': {

        'jquery': 'libs/jquery-1.10.1.min',
        'mustache': 'libs/mustache-2.1.3.min',
        'moment': 'libs/moment-2.10.6.min',
        'bootstrap': 'libs/bootstrap.min',
        'bootstrapDatetimepicker': 'libs/bootstrap-datetimepicker',
        'moment-with-locales': 'libs/moment-with-locales',
        'bootstrapSelect': 'libs/bootstrap-select',
        'fileinput': 'libs/fileinput',
        'fileinput_locale_zh': 'libs/fileinput_locale_zh',
        'jquery-ui': 'libs/jquery-ui-1.9.2.custom.min',
        'template': 'libs/template',
        'Validform_Datatype': 'libs/Validform_Datatype',
        'Validform_v5.3.2': 'libs/Validform_v5.3.2',
        'csrf': 'libs/csrf'
    },

    'shim': {
        'bootstrap': {
            deps: ['jquery']
        },
        'fileinput': {
            deps: ['jquery']
        },
        'fileinput_locale_zh': {
            deps: ['jquery','fileinput']
        },
        'Validform_Datatype': {
            deps: ['jquery']
        },
        'Validform_v5.3.2': {
            deps: ['jquery']
        },
        'jquery-ui': {
            deps: ['jquery']
        }
    }
};

