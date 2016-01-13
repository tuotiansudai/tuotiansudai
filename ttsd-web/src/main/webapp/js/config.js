var require = {
    'paths': {
        'text': staticServer + '/js/libs/text-2.0.14',
        'jquery': staticServer + '/js/libs/jquery-1.11.3.min',
        'jqueryPage': staticServer + '/js/libs/jquery.page',
        'jquery.validate': staticServer + '/js/libs/jquery.validate-1.14.0.min',
        'jquery.form': staticServer + '/js/libs/jquery.form-3.51.0.min',
        'autoNumeric': staticServer + '/js/libs/autoNumeric',
        'mustache': staticServer + '/js/libs/mustache-2.1.3.min',
        'moment': staticServer + '/js/libs/moment-2.10.6.min',
        'underscore': staticServer + '/js/libs/underscore-1.8.3.min',
        'csrf': staticServer + '/js/dest/csrf.min',
        'daterangepicker': staticServer + '/js/libs/jquery.daterangepicker-0.0.7',
        'pagination': staticServer + '/js/dest/pagination.min',
        'lodash': staticServer + '/js/libs/lodash.min',
        'layer': staticServer + '/js/libs/layer/layer',
        'layer-extend':staticServer+'/js/libs/layer/extend/layer.ext',
        'echarts': staticServer + '/js/libs/echarts/dist/echarts.min',
        'jquery.validate.extension': staticServer + '/js/dest/jquery_validate_extension.min',
        'commonFun': staticServer + '/js/dest/common.min',
        'layerWrapper': staticServer + '/js/dest/wrapper-layer.min',
        'fullPage':staticServer+'/js/libs/jquery.fullPage.min',
        'swiper':staticServer+'/js/libs/swiper-3.2.7.jquery.min',
        'load-swiper':staticServer+'/js/dest/load_swiper.min'
    },

    'shim': {
        'jquery.validate': ['jquery'],
        'jquery.form': ['jquery'],
        'jqueryPage': ['jquery'],
        'autoNumeric': ['jquery'],
        'pagination': ['jquery'],
        'layer': ['jquery'],
        'layer-extend': ['jquery','layer'],
        'layerWrapper':['layer','layer-extend'],
        'commonFun': ['jquery.validate'],
        'jquery.validate.extension': ['jquery', 'jquery.validate'],
        'fullPage': ['jquery'],
        'swiper':['jquery'],
        'load-swiper':['swiper']
    }
};

