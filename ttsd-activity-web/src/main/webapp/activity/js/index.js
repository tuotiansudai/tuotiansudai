require(['jquery', 'underscore', 'jquery.ajax.extension','coupon-alert', 'red-envelope-float', 'count_down'], function ($, _) {
    $(function () {
        var $bannerBox = $('.banner-box'),
            $imgScroll = $('.banner-img-list', $bannerBox),
            $registerBox = $('.register-ad-box', $bannerBox),
            $scrollNum = $('.scroll-num', $bannerBox),
            $productFrame = $('#productFrame'),
            $dlAmount = $('.dl-amount', $productFrame),
            $imgNum = $('li', $scrollNum),
            $bannerImg = $imgScroll.find('a'),
            screenWid, picWid, leftWid, adTimer = null,
            n = 0;

        $dlAmount.find('i').filter(function (index) {
            var value = $(this).text(),
                valueAmount = value.replace(/[^\d|.]*/g, '');
            return valueAmount.length > 5;
        }).css({
            'font-size': '16px'
        });

        screenWid = $(window).width(); //screen width
        picWid = $bannerImg.first().find('img').width();

        leftWid = (picWid - screenWid) / 2;

        $scrollNum.css({'left': (screenWid - $scrollNum.find('li').length * 25) / 2, 'visibility': 'visible'});
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


        $(".product-box .pad-m").click(function () {
            window.location.href = $(this).data("url");
        });

        var viewport = globalFun.browserRedirect();

        if (viewport == 'pc') {
            $imgScroll.find('img.iphone-img').remove();
        } else if (viewport == 'mobile') {
            $imgScroll.find('img.pc-img').remove();
            $imgScroll.find('img.iphone-img').css({'margin-left': '0px'});

            // 移动端滑动切换
            (function() {
                var startX, startY;
                $bannerBox.on('touchstart', function(event) {
                    startX = event.originalEvent.changedTouches[0].pageX;
                    startY = event.originalEvent.changedTouches[0].pageY;
                });
                $bannerBox.on('touchmove', function(event) {
                    event.preventDefault();
                })
                $bannerBox.on('touchend', function(event) {
                    var endX, endY;
                     endX = event.originalEvent.changedTouches[0].pageX;
                     endY = event.originalEvent.changedTouches[0].pageY;
                     var direction = GetSlideDirection(startX, startY, endX, endY);
                     switch(direction) {
                         case 3:
                            var index = ++n % $bannerImg.length;
                            $imgNum.eq(index).trigger('click');
                             break;
                         case 4:
                            var index = --n % $bannerImg.length;
                            $imgNum.eq(index).trigger('click');
                             break;
                     }
                });
            })();
        }

        var scrollTimer;
        $(".scroll-top").hover(function () {
            clearInterval(scrollTimer);
        }, function () {
            scrollTimer = setInterval(function () {
                scrollNews($(".scroll-top"));
            }, 2000);
        }).trigger("mouseout");

        function scrollNews(obj) {
            var $self = obj.find("ul:first");
            var lineHeight = $self.find("li:first").height();
            $self.animate({
                "margin-top": -lineHeight + "px"
            }, 600, function () {
                $self.css({
                    "margin-top": "0px"
                }).find("li:first").appendTo($self);
            })
        }
        $('.loan-btn li').on('click', function(event) {
            event.preventDefault();
            window.location.href=$(this).attr('data-url');
        });
    });


    function GetSlideAngle(dx, dy) {
        return Math.atan2(dy, dx) * 180 / Math.PI;
    }
    //根据起点和终点返回方向 1：向上，2：向下，3：向左，4：向右,0：未滑动
    function GetSlideDirection(startX, startY, endX, endY) {
         var dy = startY - endY;
         var dx = endX - startX;
         var result = 0;
         if(Math.abs(dx) < 2 && Math.abs(dy) < 2) {
             return result;
         }
         var angle = GetSlideAngle(dx, dy);
         if(angle >= -45 && angle < 45) {
             result = 4;
         }else if (angle >= 45 && angle < 135) {
             result = 1;
         }else if (angle >= -135 && angle < -45) {
             result = 2;
         }
         else if ((angle >= 135 && angle <= 180) || (angle >= -180 && angle < -135)) {
             result = 3;
         }
         return result;
    }
});