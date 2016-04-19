require(['jquery', 'layerWrapper','coupon-alert','red-envelope-float'], function ($, layer) {

    $('.pagination .page-list').click(function () {
        var self = $(this);
        if (self.hasClass('active')) {
            window.location.href = self.data('url');
        }
        return false;
    });
});