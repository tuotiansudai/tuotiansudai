require(['jquery', 'csrf'], function ($) {
    var $loan = $('.loan-list-box').find('li'),
        $couponClose = $('.coupon-close');

    $couponClose.on('click',function(e) {
        e.preventDefault();
        var $self=$(this),
            $couponModel=$self.parents('#couponModel');
        $couponModel.fadeOut('fast');
    });

    $loan.click(function () {
        window.location.href=$(this).data('url');
    });

    $('.pagination .prev').click(function () {
        var self = $(this);
        if (self.hasClass('active')) {
            window.location.href = self.data('url');
        }
        return false;
    });

    $('.pagination .next').click(function () {
        var self = $(this);
        if (self.hasClass('active')) {
            window.location.href = self.data('url');
        }
        return false;
    });
});
