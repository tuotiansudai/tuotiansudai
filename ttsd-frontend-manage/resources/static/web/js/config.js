var require = {
    'baseUrl': staticServer,
    'paths': {
        'text': staticServer + '/js/libs/text-2.0.14',
        'jquery': staticServer + '/js/libs/jquery-1.11.3.min',
        'clipboard': staticServer+'/js/libs/clipboard.min',
        'md5': staticServer+'/js/libs/jQuery.md5',
        'qrcode': staticServer+'/js/libs/jquery.qrcode.min',
        'csrf': staticServer + '/js/libs/csrf',
        'jqueryPage': staticServer + '/js/libs/jquery.page',
        'jquery.validate': staticServer + '/js/libs/jquery.validate-1.14.0.min',
        'jquery.form': staticServer + '/js/libs/jquery.form-3.51.0.min',
        'autoNumeric': staticServer + '/js/libs/autoNumeric',
        'mustache': staticServer + '/js/libs/mustache-2.1.3.min',
        'moment': staticServer + '/js/libs/moment-2.10.6.min',
        'underscore': staticServer + '/js/libs/underscore-1.8.3.min',
        'jquery.ajax.extension': staticServer + '/js/dest/jquery_ajax_extension.min',
        'daterangepicker': staticServer + '/js/libs/jquery.daterangepicker-0.0.7',
        'layer': staticServer + '/js/libs/layer/layer',
        'layer-extend':staticServer+'/js/libs/layer/extend/layer.ext',
        'echarts': staticServer + '/js/libs/echarts/dist/echarts.min',
        'jquery.validate.extension': staticServer + '/js/dest/jquery_validate_extension.min',
        'fullPage':staticServer+'/js/libs/jquery.fullPage.min',
        'swiper':staticServer+'/js/libs/swiper-3.2.7.jquery.min',
        'drag': staticServer+'/js/libs/drag',
        'rotate': staticServer+'/js/libs/jqueryrotate.min',
        'template':staticServer+'/js/libs/template.min',
        'fancybox':staticServer+'/js/libs/jquery.fancybox.min',
        'placeholder': staticServer + '/js/libs/jquery.enplaceholder',
        'superslide': staticServer + '/js/libs/jquery.SuperSlide.2.1.1',

        /*module*/
        'cnzz-statistics': staticServer+'/js/dest/cnzz_statistics.min',
        'load-swiper':staticServer+'/js/dest/load_swiper.min',
        'pagination': staticServer + '/js/dest/pagination.min',
        'logintip': staticServer+'/js/dest/login_tip.min',
        'commonFun': staticServer + '/js/dest/common.min',
        'layerWrapper': staticServer + '/js/dest/wrapper-layer.min',
        'count_down': staticServer+'/js/dest/count_down.min',
        'coupon-alert': staticServer+'/js/dest/coupon_alert.min',
        'global_page': staticServer+'/js/dest/global_page.min',
        'imageShowSlide-v1': staticServer+'/js/dest/image-show-slider.min',
        'red-envelope-float': staticServer+'/js/dest/red-envelope-float.min',
        'assign_coupon':staticServer+'/js/dest/assign_coupon.min',
        'anxin_qian':staticServer+'/js/dest/anxin_qian.min',
        'load_echarts':staticServer+'/js/dest/load_echarts.min'

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

