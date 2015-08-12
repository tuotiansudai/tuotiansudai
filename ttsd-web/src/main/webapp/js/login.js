require(['jquery'], function ($) {
    $(function () {
        var errorElement = $('.error');

        var refreshCaptcha = function() {
            var captcha = $('.login .img-captcha img');
            captcha.attr('src', '/login/captcha?' + new Date().toTimeString());
        };


        $('.login .img-captcha img').click(refreshCaptcha);

        //及时校验 用户名
        $('.login-name').blur(function () {
            var msg = "用户名不能为空";
            var _this = $(this);
            if (_this.val().trim() == '') {
                _this.removeClass('lock').addClass('unlock');
                errorElement.addClass('wrong').text(msg);
            } else {
                _this.removeClass('unlock').addClass('lock');
                errorElement.removeClass('wrong').text('');
            }
            validSuccess();
        });

        //密码校验
        $('.password').blur(function () {
            var msg = "密码不能为空";
            var _this = $(this);
            if (_this.val() == '') {
                _this.removeClass('lock').addClass('unlock');
                errorElement.addClass('wrong').text(msg);
            } else {
                _this.addClass('lock').removeClass('unlock');
                errorElement.removeClass('wrong').text('');
            }
            validSuccess();
        });

        //验证码校验
        $('.captcha').blur(function () {
            var _this = $(this);
            var msg = "验证码错误";
            var _value = _this.val();
            if (_value.length < 5) {
                _this.removeClass('lock').addClass('unlock');
                errorElement.addClass('wrong').text(msg);
                validSuccess();
            } else {
                $.ajax({
                    url: '/login/captcha/' + _value + '/verify',
                    type: 'get',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (response) {
                    if (response.data.status) {
                        _this.addClass('lock').removeClass('unlock');
                        $('.error').removeClass('wrong').text('');
                    } else {
                        $('.error').addClass('wrong').text(msg);
                        _this.removeClass('lock').addClass('unlock');
                        refreshCaptcha();
                    }
                    validSuccess();
                });
            }
        });

        var validSuccess = function () {
            var _form = $('.form-login');
            for (var i = 0; i < 3; i++) {
                if (_form.find('label').eq(i).find('input').hasClass('unlock')) {
                    $('.login-now').addClass('grey').attr('disabled', 'disabled');
                    return false;
                } else {
                    $('.login-now').removeClass('grey').removeAttr('disabled');
                }
            }
        };

        $('.login-now').click(function () {
            var self = $(this);
            if (self.hasClass('grey')) {
                return;
            }
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
                    $('.captcha').addClass('unlock').removeClass('lock').val('');
                    $('.error').addClass('wrong').text('用户名或密码错误');
                }
            });
        })
    });
});