require(['jquery','imageShowSlide-v1', 'underscore', 'layerWrapper', 'superslide', 'jquery.ajax.extension', 'commonFun', 'coupon-alert', 'red-envelope-float', 'count_down', 'jquery.validate', 'autoNumeric', 'logintip'],
    function ($,imageShowSlide, _, layer) {
        var $homePageContainer = $('#homePageContainer'),
            $imgScroll = $('.banner-img-list', $homePageContainer),
            $registerBox = $('.register-ad-box', $homePageContainer),
            $productFrame = $('#productFrame'),
            $bannerImg = $imgScroll.find('li');

        //首页大图轮播
        (function(){
            var imgCount=$imgScroll.find('li').length;
            if(imgCount>0) {
                var runimg=new imageShowSlide('bannerBox','30',imgCount);
                runimg.info();
            }
        })();

        //最新公告
        (function(){
        var box=document.getElementById("noticeList"),
            can=true,
            scrollTop;
        box.innerHTML+=box.innerHTML;
        box.onmouseover=function(){can=false};
        box.onmouseout=function(){can=true};
        new function (){
            var stop=box.scrollTop%40==0&&!can;
            if(!stop){

                box.scrollTop++;
            }
            setTimeout(arguments.callee,box.scrollTop%40?10:1500);
        };
        })();

});