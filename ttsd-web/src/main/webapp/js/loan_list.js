require(['jquery', 'csrf'], function ($) {
    var $loan = $('.loan-list-box').find('li');
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
