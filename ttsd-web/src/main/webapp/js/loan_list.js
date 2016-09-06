require(['jquery', 'layerWrapper', 'jquery.ajax.extension', 'coupon-alert', 'red-envelope-float','count_down'], function ($,layer) {
    var $loan = $('.loan-list-box').find('li');
    //var preheat = $('.preheat');

    $loan.click(function () {
        window.location.href = $(this).data('url');
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

    $('.loan-info-dl .fa-mobile').on('mouseover', function(event) {
        event.preventDefault();
        var $self=$(this);
        layer.tips('APP投资该项目享受最高0.8%年化收益奖励', $self, {
            tips: 1
        });
    });

});
