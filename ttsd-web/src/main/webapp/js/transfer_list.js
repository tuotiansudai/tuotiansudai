require(['jquery', 'layerWrapper','coupon-alert','red-envelope-float'], function ($, layer) {

    $(function() {
        $('.transfer-list-box ul li').on('click', function(event) {
            event.preventDefault();
            var $self = $(this),
                urldata = $self.attr('data-url');
                location.href = urldata;
        });
    });
});