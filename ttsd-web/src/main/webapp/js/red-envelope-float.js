define(['jquery'], function ($) {
    $(window).scrollTop()>0?$('.back-top').fadeIn('fast'):$('.back-top').fadeOut('fast');
    $(window).scroll(function() {
        if($(window).scrollTop()>0){
            $('.back-top').fadeIn('fast');
        }else{
            $('.back-top').fadeOut('fast');
        }
    });
    $('.back-top .nav-text').on('click', function(event) {//back top
        event.preventDefault();
        $('body,html').animate({scrollTop:0},'fast');
    });
});