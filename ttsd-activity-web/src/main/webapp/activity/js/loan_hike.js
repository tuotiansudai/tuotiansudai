require(['jquery'], function ($) {
    $(function () {
        $('.example-show').on('click', function (event) {
            event.preventDefault();
            $('.example-text').slideToggle('fast');
        });
    });
});