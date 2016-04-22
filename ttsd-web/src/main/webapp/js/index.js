require(['jquery', 'underscore', 'jquery.ajax.extension', 'commonFun', 'coupon-alert','red-envelope-float'], function ($, _) {
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

        $dlAmount.find('i').filter(function(index) {
            var value = $(this).text(),
                valueAmount = value.replace(/[^\d|.]*/g, '');
            return valueAmount.length > 5;
        }).css({
            'font-size': '16px'
        });

        screenWid = $(window).width(); //screen width
        picWid = $bannerImg.first().find('img').width();

        leftWid = (picWid - screenWid) / 2;

        $registerBox.css({'right': (screenWid - 1000) / 2 + 'px'});
        $scrollNum.css({'left': (screenWid - $scrollNum.find('li').length * 25) / 2});
        $imgScroll.find('img').css({
            'margin-left': '-' + leftWid + 'px'
        });


        $imgNum.click(function() {
            var num_nav = $imgNum.index(this);
            $(this).addClass("selected").siblings().removeClass("selected");
            $bannerImg.eq(num_nav).fadeIn(1000).siblings().fadeOut(1000);
        });
        $bannerBox.hover(function() {
            clearInterval(adTimer);
        }, function() {
            adTimer = setInterval(function() {
                var index = ++n % $bannerImg.length;
                $imgNum.eq(index).trigger('click');
            }, 6000);
        }).trigger('mouseleave');


        $(".product-box .pad-m").click(function() {

            window.location.href = $(this).data("url");
        });
        var viewport=commonFun.browserRedirect();
        if(viewport=='pc') {
            $imgScroll.find('img.iphone-img').remove();
        } else if(viewport=='mobile') {
            $imgScroll.find('img.pc-img').remove();
            $imgScroll.find('img.iphone-img').css({'margin-left': '0px'});
        }

        var scrollTimer;
        $(".scroll-top").hover(function() {
            clearInterval(scrollTimer);
        }, function() {
            scrollTimer = setInterval(function() {
                scrollNews($(".scroll-top"));
            }, 2000);
        }).trigger("mouseout");

        function scrollNews(obj) {
            var $self = obj.find("ul:first");
            var lineHeight = $self.find("li:first").height();
            $self.animate({
                "margin-top": -lineHeight + "px"
            }, 600, function() {
                $self.css({
                    "margin-top": "0px"
                }).find("li:first").appendTo($self);
            })
        }
        var preheat = $('.preheat');
        function writeTime() {
            if (preheat.length > 0) {
                preheat.each(function () {
                    var $self = $(this);
                    var countdown = $(this).attr("data-time");
                    setInterval(function () {
                        var day = 0,
                            hour = 0,
                            minute = 0,
                            second = 0;
                        if (countdown <= 1800) {
                            if (countdown > 0) {
                                minute = Math.floor(countdown / 60) - (day * 24 * 60) - (hour * 60);
                                second = Math.floor(countdown) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
                            } else {

                                $self.parent().find('.pro').removeClass('display-none').addClass('display-block');
                                //console.log($self.parent().find('.pro').length)
                                $self.addClass('display-none');
                                $self.parent().find('.will').addClass('display-none');
                                $self.parent().find('.now').addClass('display-block');
                            }
                            if (minute <= 9) minute = '0' + minute;
                            if (second <= 9) second = '0' + second;
                            $self.find('.minute_show').html(minute + '分');
                            $self.find('.second_show').html(second + '秒');
                            countdown--;
                        }
                    }, 1000);
                });
            }
        }

        writeTime();
    });
});