$(function() {
    var refreshCaptcha = function () {
        var captcha = $('.verification-console-img');
        captcha.attr('src', '/login/captcha?' + new Date().toTimeString());
    };
    $('.verification-console-img').click(function () {
        refreshCaptcha();
    });
    $('.btn-block').click(function () {
        var self = $(this);
        var data = $('.form-login').serialize();
        $.ajax({
            url: '/loginHandler',
            type: 'post',
            data: data
        }).done(function (response) {
            if (response.data.status) {
                window.location.href = '/';
                alert('全部正确');
            } else {
                refreshCaptcha();
                $('.captcha').val('');
                alert('用户名或密码错误');
            }
        });
    });

});