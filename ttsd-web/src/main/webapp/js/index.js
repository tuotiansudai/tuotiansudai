require(['jquery', 'csrf'], function ($) {
    $(function () {
        $('.logout').click(function (event) {
            event.preventDefault();
            $('.logout-form').submit();
        })
    });
});