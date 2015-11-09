var require = {
    'paths': {
        'jquery': '/js/libs/jquery-1.10.1.min',
        'mustache': '/js/libs/mustache-2.1.3.min',
        'bootstrap': '/js/libs/bootstrap.min',
        'bootstrapDatetimepicker': '/js/libs/bootstrap-datetimepicker',
        'moment': '/js/libs/moment-with-locales.min',
        'bootstrapSelect': '/js/libs/bootstrap-select',
        'fileinput': '/js/libs/fileinput',
        'fileinput_locale_zh': '/js/libs/fileinput_locale_zh',
        'jquery-ui': '/js/libs/jquery-ui-1.9.2.custom.min',
        'template': '/js/libs/template',
        'Validform_Datatype': '/js/libs/Validform_Datatype',
        'Validform_v5.3.2': '/js/libs/Validform_v5.3.2',
        'csrf': '/js/libs/csrf'
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

