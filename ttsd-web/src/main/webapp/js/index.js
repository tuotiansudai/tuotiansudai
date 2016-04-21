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
            if(preheat.length > 0){
                preheat.each(function(index){
                    var stringTime = $(this).attr("data-time");
                    //console.log(stringTime);
                    var timestamp2 = Date.parse(new Date(stringTime));
                    //console.log(stringTime);
                    var startInterval = timestamp2;//开始销售时间，历史毫秒数
                    var nowInterval = (new Date()).getTime();//当前时间，历史毫秒数
                    //用开始时间 - 当前时间，格式化这个时间差；
                    flagInterval = getLastDays(startInterval - nowInterval, true);
                    //这里控制样式比如 xx.innerHtml = flagInterval
                    $(this).html(flagInterval);
                });
            }

        }
        writeTime();
        setInterval(writeTime, 1000);


        function getLastDays(input,isShort) {
            var minSecondsPerDay = 86400 * 1000;
            var minSecondsPerHour = 3600 * 1000;
            var minSecondsPerMin = 60 * 1000;
            var days = Math.floor( input / minSecondsPerDay );
            var hours = Math.floor( (input % minSecondsPerDay) / minSecondsPerHour );
            var hoursT = Math.floor( input / minSecondsPerHour);
            var minutes = Math.floor( ( input % minSecondsPerDay % minSecondsPerHour ) / minSecondsPerMin );
            var seconds = Math.floor( input % minSecondsPerDay % minSecondsPerHour % minSecondsPerMin / 1000 );

            if(minutes < 10){
                minutes = '0' + minutes;
            }
            if(seconds < 10){
                seconds = '0' + seconds;
            }
            //加上short 不显示天数，只显示小时
            if(isShort){
                return  hoursT + ':' + minutes + ':' + seconds ;//24小时之内倒计时
            }

            return  days + '天' + hours + '时' + minutes + '分' + seconds + '秒';
        }
    });
});