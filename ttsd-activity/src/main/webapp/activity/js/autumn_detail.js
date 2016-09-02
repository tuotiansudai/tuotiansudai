require(['jquery', 'jquery.ajax.extension'], function ($) {

    var $loginName = $('div.login-name');
    var loginName = $loginName ? $loginName.data('login-name') : '';

    $("a.autumn-travel-invest-channel").click(function () {
        $.ajax({
            url: '/activity/autumn/travel/invest?loginName=' + loginName,
            type: 'POST'
        });
    });

    $("a.autumn-luxury-invest-channel").click(function () {
        $.ajax({
            url: '/activity/autumn/luxury/invest?loginName=' + loginName,
            type: 'POST'
        });
    });
});