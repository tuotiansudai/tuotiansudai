require(['jquery', 'jquery.ajax.extension'], function ($) {

    $("a.autumn-travel-invest-channel").click(function () {
        $.ajax({
            url: '/activity/autumn/travel/invest',
            type: 'POST'
        });
    });

    $("a.autumn-luxury-invest-channel").click(function () {
        $.ajax({
            url: '/activity/autumn/luxury/invest',
            type: 'POST'
        });
    });
});