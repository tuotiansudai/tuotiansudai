var require = {
    'paths': {
        'text': '/js/libs/text-2.0.14',
        'jquery': '/js/libs/jquery-1.11.3.min',
        'jquery.validate': '/js/libs/jquery.validate-1.14.0.min',
        'mustache': '/js/libs/mustache-2.1.3.min',
        'moment': '/js/libs/moment-with-locales.min',
        'bootstrap': '/js/libs/bootstrap.min',
        'bootstrapDatetimepicker': '/js/libs/bootstrap-datetimepicker',
        'moment-with-locales': '/js/libs/moment-with-locales',
        'bootstrapSelect': '/js/libs/bootstrap-select',
        'fileinput': '/js/libs/fileinput',
        'fileinput_locale_zh': '/js/libs/fileinput_locale_zh',
        'jquery-ui': '/js/libs/jquery-ui-1.11.4.min',
        'template': '/js/libs/template',
        'csrf': '/js/libs/csrf',
        'ueditor': '/js/libs/ueditor/ueditor',
        'ueditor.all': '/js/libs/ueditor/ueditor.all',
        'ueditor.config': '/js/libs/ueditor/ueditor.config',
        'ueditor.parse': '/js/libs/ueditor/ueditor.parse.min',
        'Validform': '/js/libs/Validform_v5.3.2_min',
        'Validform_Datatype':'/js/libs/Validform_Datatype',
        'autoNumeric': '/js/libs/autoNumeric-2.0-BETA',
        'echarts': '/js/libs/echarts/dist/echarts.min',
        'loadEcharts':'/js/loadEcharts',
        'underscore': '/js/libs/underscore-1.8.3.min'
    },
    'waitSeconds':0,
    'shim': {
        'bootstrap': ['jquery'],
        'bootstrapSelect': ['jquery', 'bootstrap'],
        'fileinput': ['jquery'],
        'fileinput_locale_zh': ['jquery', 'fileinput'],
        'jquery-ui': ['jquery'],
        'ueditor': ['ueditor.all', 'ueditor.config', 'ueditor.parse'],
        'jquery.validate': ['jquery'],
        'Validform': ['jquery'],
        'Validform_Datatype':['jquery'],
        'autoNumeric': ['jquery']
    }
};

