var require = {
    'baseUrl':commonStaticServer,
    'paths': {
        'text': commonStaticServer+'/activity/js/libs/text-2.0.14',
        'jquery': commonStaticServer+'/activity/js/libs/jquery-1.11.3.min',
        'clipboard': commonStaticServer+'/activity/js/libs/clipboard.min',
        'md5': commonStaticServer+'/activity/js/libs/jQuery.md5',
        'qrcode': commonStaticServer+'/activity/js/libs/jquery.qrcode.min',
        'csrf': commonStaticServer+'/activity/js/libs/csrf',
        'jqueryPage': commonStaticServer+'/activity/js/libs/jquery.page',
        'jquery.validate': commonStaticServer+'/activity/js/libs/jquery.validate-1.14.0.min',
        'jquery.form': commonStaticServer+'/activity/js/libs/jquery.form-3.51.0.min',
        'autoNumeric': commonStaticServer+'/activity/js/libs/autoNumeric',
        'mustache': commonStaticServer+'/activity/js/libs/mustache-2.1.3.min',
        'moment': commonStaticServer+'/activity/js/libs/moment-2.10.6.min',
        'underscore': commonStaticServer+'/activity/js/libs/underscore-1.8.3.min',
        'jquery.ajax.extension': commonStaticServer+'/activity/js/dest/jquery_ajax_extension.min',
        'daterangepicker': commonStaticServer+'/activity/js/libs/jquery.daterangepicker-0.0.7',
        'pagination': commonStaticServer+'/activity/js/dest/pagination.min',
        'lodash': commonStaticServer+'/activity/js/libs/lodash.min',
        'layer1': commonStaticServer+'/activity/js/libs/layer/layer',
        'layer-extend': commonStaticServer+'/activity/js/libs/layer/extend/layer.ext',
        'echarts': commonStaticServer+'/activity/js/libs/echarts/dist',
        'jquery.validate.extension': commonStaticServer+'/activity/js/dest/jquery_validate_extension.min',
        'commonFun': commonStaticServer+'/activity/js/dest/common.min',
        'layerWrapper': commonStaticServer+'/activity/js/dest/wrapper-layer.min',
        'fullPage':commonStaticServer+'/activity/js/libs/jquery.fullPage.min',
        'swiper':commonStaticServer+'/activity/js/libs/swiper-3.2.7.jquery.min',
        'load-swiper':commonStaticServer+'/activity/js/dest/load_swiper.min',
        'coupon-alert': commonStaticServer+'/activity/js/dest/coupon_alert.min',
        'cnzz-statistics': commonStaticServer+'/activity/js/dest/cnzz_statistics.min',
        'red-envelope-float': commonStaticServer+'/activity/js/dest/red-envelope-float.min',
        'drag': commonStaticServer+'/activity/js/libs/drag',
        'rotate': commonStaticServer+'/activity/js/libs/jqueryrotate.min',
        'template':commonStaticServer+'/activity/js/libs/template.min',
        'fancybox':commonStaticServer+'/activity/js/libs/jquery.fancybox.min',
        'count_down': commonStaticServer+'/activity/js/dest/count_down.min',
        'placeholder':commonStaticServer+'/activity/js/libs/jquery.enplaceholder',
        'superslide': commonStaticServer+'/activity/js/libs/jquery.SuperSlide.2.1.1',
        'endTime': commonStaticServer+'/activity/js/dest/actor_time.min',
        'carousel': commonStaticServer+'/activity/js/libs/jquery.carousel',

        /*module*/
        'drawCircle': commonStaticServer+'/activity/js/dest/gift_circle_draw.min',
        'logintip': commonStaticServer+'/activity/js/dest/login_tip.min',
        'register_common': commonStaticServer+'/activity/js/dest/register_common.min',
        'lottery_unit':commonStaticServer+'/activity/js/dest/lottery_unit.min',
        'validator':commonStaticServer+'/activity/js/dest/validator.min'
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
        'clipboard':['jquery'],
        'carousel':['jquery']
    },
    config: {
        text: {
            useXhr: function (url, protocol, hostname, port) {
                return true;
            }
        }
    }
};

