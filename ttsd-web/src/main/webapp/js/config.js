var require = {
    'paths': {
        'text': '/js/libs/text-2.0.14',
        'jquery': '/js/libs/jquery-1.11.3.min',
        'jqueryPage': '/js/libs/jquery.page',
        'jquery.validate': '/js/libs/jquery.validate-1.14.0.min',
        'jquery.form': '/js/libs/jquery.form-3.51.0.min',
        'autoNumeric': '/js/libs/autoNumeric-2.0-BETA',
        'mustache': '/js/libs/mustache-2.1.3.min',
        'moment': '/js/libs/moment-2.10.6.min',
        'underscore': '/js/libs/underscore-1.8.3.min',
        'csrf': '/js/dest/csrf.min',
        'daterangepicker': '/js/libs/jquery.daterangepicker-0.0.7',
        'pagination': '/js/dest/pagination.min',
        'lodash':'/js/libs/lodash.min',
        'layer':'/js/libs/layer/layer',
        'echarts':'/js/libs/echarts/dist/echarts.min',
        'jquery.validate.extension': '/js/dest/jquery_validate_extension.min',
        'commonFun': '/js/dest/common.min'
    },

    'shim': {
        'jquery.validate': ['jquery'],
        'jquery.form': ['jquery'],
        'jqueryPage': ['jquery'],
        'autoNumeric': ['jquery'],
        'pagination': ['jquery'],
        'layer':['jquery'],
        'commonFun':['jquery.validate'],
        'jquery.validate.extension':['jquery', 'jquery.validate']
    }
};

