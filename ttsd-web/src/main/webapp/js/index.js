require(['jquery','imageShowSlide-v1', 'underscore', 'layerWrapper', 'superslide', 'jquery.ajax.extension', 'commonFun', 'coupon-alert', 'red-envelope-float', 'count_down', 'jquery.validate', 'autoNumeric', 'logintip'],
    function ($,imageShowSlide, _, layer) {
        var $homePageContainer = $('#homePageContainer'),
            $imgScroll = $('.banner-img-list', $homePageContainer),
            $registerBox = $('.register-ad-box', $homePageContainer),
            $productFrame = $('#productFrame'),
            $bannerImg = $imgScroll.find('li');

        //首页大图轮播
        //(function(){
            var imgCount=$imgScroll.find('li').length;
            var runimg=new imageShowSlide('bannerBox','30',imgCount);
            runimg.info();

            //var screenWid = $(window).width(); //screen width
            //var leftWid = (1920 - screenWid) / 2;   //1920图片宽度
            //$imgScroll.find('img').css({
            //    'margin-left': '-' + leftWid + 'px'
            //});

        //})();

});