$(function(){
    var iNow = 0;
    $('.step ul li').eq(0).css({opacity:1});
    function tab(){
        $('.step ol li').removeClass('active');
        $('.step ol li').eq(iNow).addClass('active');
        $('.step ul li').stop().animate({opacity: 0},300);;
        $('.step ul li').eq(iNow).stop().animate({opacity: 1},300);;
    }
    $('.step ol li').click(function(){
        iNow = $(this).index();
        tab();
    });
    $('.next').click(next);
    function next(){
        iNow++;
        if(iNow == $('.step ol li').length){
            iNow = 0;
        }
        tab();
    }

    $('.prev').click(function(){
        iNow--;
        if(iNow < 0){
            iNow = $('.step ol li').length - 1;
        }
        tab();
    });
});
