define(['jquery'], function ($) {
    var $couponAlert = $('.coupon-alert');
    var $couponClose = $('.coupon-alert .coupon-close');

    $couponClose.on('click',function() {
        $couponAlert.fadeOut('fast');
        return false;
    });
});