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
            } else {
                refreshCaptcha();
                $('.captcha').val('');
                $('.error').text('用户名或密码错误').show();
                $('.btn-block').attr('disabled', 'disabled');
            }
        });
    });

    //验证码校验
    $('.captcha').blur(function () {
        var _this = $(this);
        var msg = "验证码错误";
        var _value = _this.val();
        if (_value.length < 5) {
            $('.error').text(msg).show();
            $('.btn-block').attr('disabled', 'disabled');
        } else {
            $.ajax({
                url: '/login/captcha/' + _value + '/verify',
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function (response) {
                if (response.data.status) {
                    $('.btn-block').removeAttr('disabled');
                    $('.error').hide();
                } else {
                    $('.error').text(msg).show();
                    $('.btn-block').attr('disabled', 'disabled');
                    var captcha = $('.captcha-img img');
                    captcha.attr('src', '/login/captcha?' + new Date().toTimeString());
                }
            });
        }
    });

});