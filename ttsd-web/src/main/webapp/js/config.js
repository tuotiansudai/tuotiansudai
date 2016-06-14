var require = {
    'paths': {
        'text': 'js/libs/text-2.0.14',
        'jquery': 'js/libs/jquery-1.11.3.min',
        'csrf': 'js/libs/csrf',
        'jqueryPage': 'js/libs/jquery.page',
        'jquery.validate': 'js/libs/jquery.validate-1.14.0.min',
        'jquery.form': 'js/libs/jquery.form-3.51.0.min',
        'autoNumeric': 'js/libs/autoNumeric',
        'mustache': 'js/libs/mustache-2.1.3.min',
        'moment': 'js/libs/moment-2.10.6.min',
        'underscore': 'js/libs/underscore-1.8.3.min',
        'jquery.ajax.extension': 'js/dest/jquery_ajax_extension.min',
        'daterangepicker': 'js/libs/jquery.daterangepicker-0.0.7',
        'pagination': 'js/dest/pagination.min',
        'lodash': 'js/libs/lodash.min',
        'layer': 'js/libs/layer/layer',
        'layer-extend': 'js/libs/layer/extend/layer.ext',
        'echarts': 'js/libs/echarts/dist/echarts.min',
        'jquery.validate.extension': 'js/dest/jquery_validate_extension.min',
        'commonFun': 'js/dest/common.min',
        'layerWrapper': 'js/dest/wrapper-layer.min',
        'fullPage':'js/libs/jquery.fullPage.min',
        'swiper':'js/libs/swiper-3.2.7.jquery.min',
        'load-swiper':'js/dest/load_swiper.min',
        'coupon-alert': 'js/dest/coupon_alert.min',
        'cnzz-statistics': 'js/dest/cnzz_statistics.min',
        'red-envelope-float': 'js/dest/red-envelope-float.min',
        'drag': 'js/libs/drag',
        'rotate': 'js/libs/jqueryrotate.min',
        'template':'js/libs/template.min',
        'fancybox':'js/libs/jquery.fancybox.min',
        'count_down': 'js/dest/count_down.min',
        'placeholder':'js/libs/jquery.enplaceholder'
    },
    'waitSeconds':0,
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
        'load-swiper':['swiper'],
        'drag':['jquery'],
        'rotate':['jquery'],
        'fancybox':['jquery'],
        'placeholder':['jquery']
    },
    config: {
        text: {
            useXhr: function (url, protocol, hostname, port) {
                return true;
            }
        }
    }
};

if (this.document) {
    require.baseUrl = staticServer;
}
