require(['jquery'], function ($) {
    $(function() {
        var $banner=$('#activity-banner');
        $(window).width()<700?$banner.attr('src','/images/banner/ph-banner01.jpg'):$banner.attr('src','/images/sign/activities/daili/ad.jpg');
    });
});