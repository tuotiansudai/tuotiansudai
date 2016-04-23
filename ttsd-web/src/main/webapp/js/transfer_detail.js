require(['jquery', 'pagination', 'layerWrapper', 'coupon-alert','red-envelope-float'], function ($, pagination, Mustache, layer) {
    $(function() {
        $('').on('click', function(event) {
            event.preventDefault();
            var $self=$(this),
                urldata=$self.attr('data-url');
            location.href=urldata;
        });
    });
});