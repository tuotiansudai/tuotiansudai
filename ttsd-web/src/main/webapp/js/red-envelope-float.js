define(['jquery'], function ($) {
    $(window).scrollTop()>0?$('.back-top').css('visibility','visible'):$('.back-top').css('visibility','hidden');
    $(window).scroll(function() {
        if($(window).scrollTop()>0){
            $('.back-top').css('visibility','visible');
        }else{
            $('.back-top').css('visibility','hidden');
        }
    });
    $('.back-top .nav-text').on('click', function(event) {//back top
        event.preventDefault();
        $('body,html').animate({scrollTop:0},'fast');
    });
});