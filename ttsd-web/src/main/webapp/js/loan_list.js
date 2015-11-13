require(['jquery', 'csrf'], function ($) {
    var $loan = $('.loan-list-box').find('li');
    $loan.click(function () {
        var thisUrl = $(this).data('url');
        window.open(thisUrl);
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
