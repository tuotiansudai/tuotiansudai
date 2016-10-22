define(['jquery'], function ($) {
    var $couponAlert = $('.coupon-alert');
    var $couponClose = $('.coupon-alert .coupon-close');

    $couponClose.on('click',function() {
        $couponAlert.fadeOut('fast');
        return false;
    });
    $('#getFree').on('click', function (event) {
        event.preventDefault();
        $couponClose.trigger('click');
        if ($(window).width() < 700) {
            $('body,html').animate({scrollTop: '176px'}, "slow", function () {
                $('.product-box-inner').addClass('active');
            });
        } else {
            $('body,html').animate({scrollTop: '550px'}, "slow", function () {
                $('.new-user-free').addClass('active');
            });
        }
    });
});