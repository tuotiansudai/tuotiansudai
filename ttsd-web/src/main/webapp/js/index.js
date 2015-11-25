require(['jquery','csrf'], function ($) {
    var $bannerBox=$('.banner-box'),
        $imgScroll=$('.banner-img-list',$bannerBox),
        $registerBox=$('.register-ad-box',$bannerBox),
        $scrollNum=$('.scroll-num',$bannerBox),
        $imgNum=$('li',$scrollNum),
        $bannerImg=$imgScroll.find('a'),
        screenWid,picWid,leftWid,adTimer=null,n=0;

    screenWid=$(window).width(); //screen width
    picWid=$bannerImg.first().find('img').width();

    leftWid=(picWid-screenWid)/2;

    $registerBox.css({'right':(screenWid-1000)/2+'px'});
    $scrollNum.css({'left':(screenWid-$scrollNum.width())/2});
    $imgScroll.find("a:not(:first)").hide();
    $imgScroll.find('img').css({'margin-left':'-'+leftWid+'px'});


    $imgNum.click(function(){
        var num_nav = $imgNum.index(this);
        $(this).addClass("selected").siblings().removeClass("selected");
        $bannerImg.eq(num_nav).fadeIn(1000).siblings().fadeOut(1000);
    });
    $bannerBox.hover(function(){
        clearInterval(adTimer);
    }, function(){
        adTimer = setInterval(function() {
            n = n >= ($bannerImg.length-1) ? 0 : (n + 1);
            $imgNum.eq(n).trigger('click');
        }, 3000);
    }).trigger('mouseleave');


    $(".product-box .pad-m").click(function() {
        window.location.href = $(this).data("url");
    });
});