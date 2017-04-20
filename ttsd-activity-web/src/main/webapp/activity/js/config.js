var require = {
    'baseUrl':staticServer,
    'paths': {
        'text': staticServer+'/activity/js/libs/text-2.0.14',
        'jquery': staticServer+'/activity/js/libs/jquery-1.11.3.min',
        'clipboard': staticServer+'/activity/js/libs/clipboard.min',
        'md5': staticServer+'/activity/js/libs/jQuery.md5',
        'qrcode': staticServer+'/activity/js/libs/jquery.qrcode.min',
        'csrf': staticServer+'/activity/js/libs/csrf',
        'jqueryPage': staticServer+'/activity/js/libs/jquery.page',
        'jquery.validate': staticServer+'/activity/js/libs/jquery.validate-1.14.0.min',
        'jquery.form': staticServer+'/activity/js/libs/jquery.form-3.51.0.min',
        'autoNumeric': staticServer+'/activity/js/libs/autoNumeric',
        'mustache': staticServer+'/activity/js/libs/mustache-2.1.3.min',
        'moment': staticServer+'/activity/js/libs/moment-2.10.6.min',
        'underscore': staticServer+'/activity/js/libs/underscore-1.8.3.min',
        'jquery.ajax.extension': staticServer+'/activity/js/dest/jquery_ajax_extension.min',
        'daterangepicker': staticServer+'/activity/js/libs/jquery.daterangepicker-0.0.7',
        'pagination': staticServer+'/activity/js/dest/pagination.min',
        'lodash': staticServer+'/activity/js/libs/lodash.min',
        'layer1': staticServer+'/activity/js/libs/layer/layer',
        'layer-extend': staticServer+'/activity/js/libs/layer/extend/layer.ext',
        'echarts': staticServer+'/activity/js/libs/echarts/dist',
        'jquery.validate.extension': staticServer+'/activity/js/dest/jquery_validate_extension.min',
        'commonFun': staticServer+'/activity/js/dest/common.min',
        'layerWrapper': staticServer+'/activity/js/dest/wrapper-layer.min',
        'fullPage':staticServer+'/activity/js/libs/jquery.fullPage.min',
        'swiper':staticServer+'/activity/js/libs/swiper-3.2.7.jquery.min',
        'load-swiper':staticServer+'/activity/js/dest/load_swiper.min',
        'coupon-alert': staticServer+'/activity/js/dest/coupon_alert.min',
        'cnzz-statistics': staticServer+'/activity/js/dest/cnzz_statistics.min',
        'red-envelope-float': staticServer+'/activity/js/dest/red-envelope-float.min',
        'drag': staticServer+'/activity/js/libs/drag',
        'rotate': staticServer+'/activity/js/libs/jqueryrotate.min',
        'template':staticServer+'/activity/js/libs/template.min',
        'fancybox':staticServer+'/activity/js/libs/jquery.fancybox.min',
        'count_down': staticServer+'/activity/js/dest/count_down.min',
        'placeholder':staticServer+'/activity/js/libs/jquery.enplaceholder',
        'superslide': staticServer+'/activity/js/libs/jquery.SuperSlide.2.1.1',
        'endTime': staticServer+'/activity/js/dest/actor_time.min',

        /*module*/
        'drawCircle': staticServer+'/activity/js/dest/gift_circle_draw.min',
        'logintip': staticServer+'/activity/js/dest/login_tip.min',
        'register_common': staticServer+'/activity/js/dest/register_common.min',
        'lottery_unit':staticServer+'/activity/js/dest/lottery_unit.min',
        'validator':staticServer+'/activity/js/dest/validator.min'
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
        'superslide': ['jquery'],
        'md5':['jquery'],
        'qrcode':['jquery'],
        'clipboard':['jquery']
    },
    config: {
        text: {
            useXhr: function (url, protocol, hostname, port) {
                return true;
            }
        }
    }
};

