var require = {
    'baseUrl': staticServer,
    'paths': {
        'text': staticServer + '/point/js/libs/text-2.0.14',
        'jquery': staticServer + '/point/js/libs/jquery-1.11.3.min',
        'csrf': staticServer + '/point/js/libs/csrf',
        'jqueryPage': staticServer + '/point/js/libs/jquery.page',
        'jquery.validate': staticServer + '/point/js/libs/jquery.validate-1.14.0.min',
        'jquery.form': staticServer + '/point/js/libs/jquery.form-3.51.0.min',
        'autoNumeric': staticServer + '/point/js/libs/autoNumeric',
        'mustache': staticServer + '/point/js/libs/mustache-2.1.3.min',
        'moment': staticServer + '/point/js/libs/moment-2.10.6.min',
        'underscore': staticServer + '/point/js/libs/underscore-1.8.3.min',
        'jquery.ajax.extension': staticServer + '/point/js/dest/jquery_ajax_extension.min',
        'daterangepicker': staticServer + '/point/js/libs/jquery.daterangepicker-0.0.7',
        'pagination': staticServer + '/point/js/dest/pagination.min',
        'lodash': staticServer + '/point/js/libs/lodash.min',
        'layer': staticServer + '/point/js/libs/layer/layer',
        'layer-extend':staticServer+'/point/js/libs/layer/extend/layer.ext',
        'echarts': staticServer + '/point/js/libs/echarts/dist/echarts.min',
        'jquery.validate.extension': staticServer + '/point/js/dest/jquery_validate_extension.min',
        'commonFun': staticServer + '/point/js/dest/common.min',
        'layerWrapper': staticServer + '/point/js/dest/wrapper-layer.min',
        'fullPage':staticServer+'/point/js/libs/jquery.fullPage.min',
        'swiper':staticServer+'/point/js/libs/swiper-3.2.7.jquery.min',
        'load-swiper':staticServer+'/point/js/dest/load_swiper.min',
        'coupon-alert': staticServer+'/point/js/dest/coupon_alert.min',
        'cnzz-statistics': staticServer+'/point/js/dest/cnzz_statistics.min',
        'red-envelope-float': staticServer+'/point/js/dest/red-envelope-float.min',
        'drag': staticServer+'/point/js/libs/drag',
        'rotate': staticServer+'/point/js/libs/jqueryrotate.min',
        'template':staticServer+'/point/js/libs/template.min',
        'fancybox':staticServer+'/point/js/libs/jquery.fancybox.min',
        'count_down': staticServer+'/point/js/dest/count_down.min',
        'placeholder': staticServer + '/point/js/libs/jquery.enplaceholder',
        'superslide': staticServer + '/point/js/libs/jquery.SuperSlide.2.1.1',
        'drawCircle': staticServer+'/point/js/dest/gift_circle_draw.min',
        'lottery_unit':staticServer+'/point/js/dest/lottery_unit.min'
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

