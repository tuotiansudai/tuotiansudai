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
        'jquery.ajax.extension': 'js/jquery_ajax_extension',
        'daterangepicker': 'js/libs/jquery.daterangepicker-0.0.7',
        'pagination': 'js/pagination',
        'lodash': 'js/libs/lodash.min',
        'layer1': 'js/libs/layer/layer',
        'layer-extend': 'js/libs/layer/extend/layer.ext',
        'echarts': 'js/libs/echarts/dist',
        'jquery.validate.extension': 'js/jquery_validate_extension',
        'commonFun': 'js/common',
        'layerWrapper': 'js/wrapper-layer',
        'fullPage':'js/libs/jquery.fullPage.min',
        'swiper':'js/libs/swiper-3.2.7.jquery.min',
        'load-swiper':'js/load_swiper',
        'coupon-alert': 'js/coupon_alert',
        'cnzz-statistics': 'js/cnzz_statistics',
        'red-envelope-float': 'js/red-envelope-float',
        'drag': 'js/libs/drag',
        'rotate': 'js/libs/jqueryrotate.min',
        'template':'js/libs/template.min',
        'fancybox':'js/libs/jquery.fancybox.min',
        'count_down': 'js/count_down',
        'placeholder':'js/libs/jquery.enplaceholder',
        'superslide': 'js/libs/jquery.SuperSlide.2.1.1'
    },
    'waitSeconds':0,
    'shim': {
        'jquery.validate': ['jquery'],
        'jquery.form': ['jquery'],
        'jqueryPage': ['jquery'],
        'autoNumeric': ['jquery'],
        'pagination': ['jquery'],
        'layer1': ['jquery'],
        'layer-extend': ['jquery','layer1'],
        'layerWrapper':['layer1','layer-extend'],
        'commonFun': ['jquery.validate', 'echarts/echarts.min'],
        'jquery.validate.extension': ['jquery', 'jquery.validate'],
        'fullPage': ['jquery'],
        'swiper':['jquery'],
        'load-swiper':['swiper'],
        'drag':['jquery'],
        'rotate':['jquery'],
        'fancybox':['jquery'],
        'placeholder': ['jquery'],
        'superslide': ['jquery']
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
