require(['jquery'], function ($) {
    $(function() {
        var $banner=$('#activity-banner');
        $(window).width()<700?$banner.attr('src',staticServer+'/images/banner/ph-banner01.jpg'):$banner.attr('src',staticServer+'/images/sign/activities/daili/ad.jpg');
    });
});