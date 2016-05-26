require(['jquery', 'underscore', 'jquery.ajax.extension', 'commonFun', 'count_down'], function ($, _) {
    $(function () {
        var $bannerBox = $('.banner-box'),
            $imgScroll = $('.media-img-list', $bannerBox),
            $scrollNum = $('.scroll-num', $bannerBox),
            $imgNum = $('li', $scrollNum),
            $mediaPraise = $('.media-praise'),
            $bannerImg = $imgScroll.find('a'),
            screenWid, picWid, leftWid, adTimer = null,
            n = 0;


        screenWid = $(window).width(); //screen width
        picWid = $bannerImg.first().find('img').width();

        leftWid = (picWid - screenWid) / 2;

        $scrollNum.css({'left': (screenWid - $scrollNum.find('li').length * 25) / 2});
        $imgScroll.find('img').css({
            'margin-left': '-' + leftWid + 'px'
        });


        $imgNum.click(function () {
            var num_nav = $imgNum.index(this);
            $(this).addClass("selected").siblings().removeClass("selected");
            $bannerImg.eq(num_nav).fadeIn(1000).siblings().fadeOut(1000);
        });
        $bannerBox.hover(function () {
            clearInterval(adTimer);
        }, function () {
            adTimer = setInterval(function () {
                var index = ++n % $bannerImg.length;
                $imgNum.eq(index).trigger('click');
            }, 6000);
        }).trigger('mouseleave');

        $mediaPraise.click(function(){

        });


    });
});