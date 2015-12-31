require(['jquery', 'underscore', 'csrf'], function ($, _) {
    $(function () {
        var $bannerBox = $('.banner-box'),
            $imgScroll = $('.banner-img-list', $bannerBox),
            $registerBox = $('.register-ad-box', $bannerBox),
            $scrollNum = $('.scroll-num', $bannerBox),
            $productFrame = $('#productFrame'),
            $dlAmount = $('.dl-amount', $productFrame),
            $imgNum = $('li', $scrollNum),
            $userCount=$('#userCount'),
            $bannerImg = $imgScroll.find('a'),
            screenWid, picWid, leftWid, adTimer = null, n = 0;

        $dlAmount.find('i').filter(function (index) {
            var value = $(this).text(),
                valueAmount = value.replace(/[^\d|.]*/g, '');
            return valueAmount.length > 5;
        }).css({'font-size': '16px'});

        screenWid = $(window).width(); //screen width
        picWid = $bannerImg.first().find('img').width();

        leftWid = (picWid - screenWid) / 2;

        //Traversing the data and formatting output
        var userCountArray=_.filter($userCount.attr('data-count')),html='';
        for (var i=0,len=userCountArray.length;i<len; i++) {
            html+='<i>'+userCountArray[i]+'</i>';
        };
        $userCount.html(html);

        $registerBox.css({'right': (screenWid - 1000) / 2 + 'px'});
        $scrollNum.css({'left': (screenWid - $scrollNum.width()) / 2});
        $imgScroll.find("a:not(:first)").hide();
        $imgScroll.find('img').css({'margin-left': '-' + leftWid + 'px'});


        $imgNum.click(function () {
            var num_nav = $imgNum.index(this);
            $(this).addClass("selected").siblings().removeClass("selected");
            $bannerImg.eq(num_nav).fadeIn(1000).siblings().fadeOut(1000);
        });
        $bannerBox.hover(function () {
            clearInterval(adTimer);
        }, function () {
            adTimer = setInterval(function () {
                n = n >= ($bannerImg.length - 1) ? 0 : (n + 1);
                $imgNum.eq(n).trigger('click');
            }, 3000);
        }).trigger('mouseleave');


        $(".product-box .pad-m").click(function () {
            window.location.href = $(this).data("url");
        });


        var adjustBanner = function () {
            var screenWidNow = $(window).width();
            if (screenWidNow < 700) {
                $imgScroll.find('img').eq(0).attr('src', staticServer + '/images/ttimg/ph-a01.jpg');
                $imgScroll.find('img').eq(1).attr('src', staticServer + '/images/ttimg/ph-a02.jpg');
                $imgScroll.find('img').eq(2).attr('src', staticServer + '/images/ttimg/ph-a03.jpg');

                $imgScroll.find('img').css({'margin-left': '0px'});

            }
            else {
                $imgScroll.find('img').eq(0).attr('src', staticServer + '/images/sign/actor/ranking/qph.jpg');
                $imgScroll.find('img').eq(1).attr('src', staticServer + '/images/sign/actor/grand/ba-grand.jpg');
                $imgScroll.find('img').eq(2).attr('src', staticServer + '/images/ttimg/ttimg-home03.png');

                $imgScroll.find('img').css({'margin-left': '-' + leftWid + 'px'});
                $registerBox.css({'right': (screenWidNow - 1000) / 2 + 'px'});
            }
            $userCount.html($userCount.attr('data-count')).css({'color':'#ff752a'});
        };

        $(window).resize(function () {
            adjustBanner();
        });


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

    });
});