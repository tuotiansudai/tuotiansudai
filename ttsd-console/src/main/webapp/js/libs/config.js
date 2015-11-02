var require = {
    'paths': {
        'jquery': '/js/libs/jquery-1.10.1.min',
        'mustache': '/js/libs/mustache-2.1.3.min',
        'moment': '/js/libs/moment-2.10.6.min',
        'bootstrap': '/js/libs/bootstrap.min',
        'bootstrapDatetimepicker': '/js/libs/bootstrap-datetimepicker',
        'bootstrapDatePicker': '/js/libs/bootstrap-datepicker',
        'moment-with-locales': '/js/libs/moment-with-locales',
        'bootstrapSelect': '/js/libs/bootstrap-select',
        'fileinput': '/js/libs/fileinput',
        'fileinput_locale_zh': '/js/libs/fileinput_locale_zh',
        'jquery-ui': '/js/libs/jquery-ui-1.9.2.custom.min',
        'Validform_Datatype': '/js/libs/Validform_Datatype',
        'Validform_v5.3.2': '/js/libs/Validform_v5.3.2',
        //'ueditor-all': '/js/libs/ueditor/ueditor.all.min',
        'ueditor-all': '/js/libs/ueditor/ueditor.all',
        'ueditor-config': '/js/libs/ueditor/ueditor.config',
        'ueditor': '/js/libs/ueditor/ueditor',
        'ueditor-lang': '/js/libs/ueditor/lang/zh-cn/zh-cn',
        'csrf': '/js/libs/csrf'
    },

    'shim': {
        'bootstrap': {
            deps: ['jquery']
        },
        'bootstrapSelect': {
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
        },
        'ueditor-all':{
            deps: ['ueditor-config']
        },
        'ueditor':{
            deps: ['ueditor-all']
        },
        'ueditor-lang':{
            deps: ['ueditor-all']
        }
    }
};

