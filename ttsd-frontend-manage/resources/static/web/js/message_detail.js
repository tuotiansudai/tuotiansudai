require(['jquery', 'jquery.ajax.extension'], function ($) {
    $('.go-back a').click(function (event) {
        event.preventDefault();
        var index = window.location.hash;
        window.location = "/message/user-messages" + index;
        return false;
    });
});
