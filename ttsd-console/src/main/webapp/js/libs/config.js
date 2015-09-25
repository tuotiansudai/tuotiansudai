var require = {
    'paths': {
<<<<<<< HEAD

        'jquery': '/js/libs/jquery-1.10.1.min',
        'mustache': '/js/libs/mustache-2.1.3.min',
        'moment': '/js/libs/moment-2.10.6.min',
        'bootstrap': '/js/libs/bootstrap.min',
        'bootstrapDatetimepicker': '/js/libs/bootstrap-datetimepicker',
        'moment-with-locales': '/js/libs/moment-with-locales',
        'bootstrapSelect': '/js/libs/bootstrap-select',
        'fileinput': '/js/libs/fileinput',
        'fileinput_locale_zh': '/js/libs/fileinput_locale_zh',
        'jquery-ui': '/js/libs/jquery-ui-1.9.2.custom.min',
        'template': '/js/libs/template',
        'Validform_Datatype': '/js/libs/Validform_Datatype',
        'Validform_v5.3.2': '/js/libs/Validform_v5.3.2',
        'csrf': '/js/libs/csrf'
=======
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
>>>>>>> new_version_master
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

