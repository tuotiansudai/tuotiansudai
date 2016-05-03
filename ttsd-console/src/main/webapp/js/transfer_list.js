require(['jquery'], function ($) {
    $(function() {
        $('.transfer-list-box ul li').on('click', function(event) {
            event.preventDefault();
            var $self=$(this),
                urldata=$self.attr('data-url');
            console.log('1');
            location.href=urldata;
        });
    });
});
