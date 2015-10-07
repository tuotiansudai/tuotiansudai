define(['jquery'], function ($) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $(function () {
        $('.header .logout').click(function (event) {
            event.preventDefault();
            $('.header .logout-form').submit();
        })
    });
});