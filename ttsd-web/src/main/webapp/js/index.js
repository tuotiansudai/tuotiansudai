require(['jquery', 'csrf'], function($) {
    $(function() {
        var $bannerBox = $('.banner-box'),
            $imgScroll = $('.banner-img-list', $bannerBox),
            $registerBox = $('.register-ad-box', $bannerBox),
            $scrollNum = $('.scroll-num', $bannerBox),
            $productFrame = $('#productFrame'),
            $dlAmount = $('.dl-amount', $productFrame),
            $imgNum = $('li', $scrollNum),
            $bannerImg = $imgScroll.find('a'),
            $couponClose = $('.coupon-close'),
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

        $registerBox.css({
            'right': (screenWid - 1000) / 2 + 'px'
        });
        $scrollNum.css({
            'left': (screenWid - $scrollNum.width()) / 2
        });
        $imgScroll.find("a:not(:first)").hide();
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
                n = n >= ($bannerImg.length - 1) ? 0 : (n + 1);
                $imgNum.eq(n).trigger('click');
            }, 3000);
        }).trigger('mouseleave');


        $(".product-box .pad-m").click(function() {
            window.location.href = $(this).data("url");
        });

        if (screenWid < 700) {
            $imgScroll.find('img').eq(0).attr('src', staticServer + '/images/banner/ph-banner01.jpg');
            $imgScroll.find('img').eq(1).attr('src', staticServer + '/images/banner/ph-banner02.jpg');
            $imgScroll.find('img').eq(2).attr('src', staticServer + '/images/banner/ph-banner03.jpg');

            $imgScroll.find('img').css({
                'margin-left': '0px'
            });
        }

        var adjustBanner = function() {
            var screenWidNow = $(window).width();
            if (screenWidNow < 700) {
                $imgScroll.find('img').eq(0).attr('src', staticServer + '/images/banner/ph-banner01.jpg');
                $imgScroll.find('img').eq(1).attr('src', staticServer + '/images/banner/ph-banner02.jpg');
                $imgScroll.find('img').eq(2).attr('src', staticServer + '/images/banner/ph-banner03.jpg');

                $imgScroll.find('img').css({
                    'margin-left': '0px'
                });

            } else {
                $imgScroll.find('img').eq(0).attr('src', staticServer + '/images/sign/activities/ranking/qph.jpg');
                $imgScroll.find('img').eq(1).attr('src', staticServer + '/images/sign/activities/grand/ad2.jpg');
                $imgScroll.find('img').eq(2).attr('src', staticServer + '/images/banner/banner-home03.png');

                $imgScroll.find('img').css({
                    'margin-left': '-' + leftWid + 'px'
                });
                $registerBox.css({
                    'right': (screenWidNow - 1000) / 2 + 'px'
                });
            }
        }



        $(window).resize(function() {
            adjustBanner();
        });

        $couponClose.on('click',function(e) {
            e.preventDefault();
            var $self=$(this),
                $couponModel=$self.parents('#couponModel');
            $couponModel.fadeOut('fast');
        });
    });
});