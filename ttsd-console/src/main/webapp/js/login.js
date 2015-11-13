$(function() {
    var refreshCaptcha = function () {
        var captcha = $('.verification-console-img');
        captcha.attr('src', '/login/captcha?' + new Date().toTimeString());
    };
    $('.verification-console-img').click(function () {
        refreshCaptcha();
    });
    $('.btn-block').click(function () {
        $.ajax({
            url: '/loginHandler',
            type: 'post',
            data: $('.form-login').serialize()
        }).done(function (response) {
            if (response.data.status) {
                window.location.href = '/';
            } else {
                refreshCaptcha();
                $('.captcha').val('');
                if (response.data.isCaptchaNotMatch) {
                    $('.error').text('验证码不正确').show();
                } else {
                    $('.error').text('用户名或密码不正确').show();
                }
            }
        });
    });
});