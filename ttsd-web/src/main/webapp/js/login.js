require(['jquery'], function ($) {
    $(function () {
        var errorElement = $('.error');

        $('.login .img-captcha img').click(function (event) {
            var target = $(event.target);
            target.attr('src', '/login/captcha?' + new Date().toTimeString());
        });

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
                _this.addClass('lock').removeClass('unlock');
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
                        var captcha = $('.login .img-captcha img');
                        captcha.attr('src', '/login/captcha?' + new Date().toTimeString());
                    }
                    validSuccess();
                });
            }
        });

        var validSuccess = function () {
            console.log("in");
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
            var data = $('.form-login').serialize();
            $.ajax({
                url: url,    //post 提交地址
                type: 'POST',
                dataType: 'json',
                data: data
            })
                .done(function (data) {
                    if (data.status) {
                        //go to
                    } else {
                        $('.captcha').addClass('unlock').removeClass('lock').val('');
                        $('.error').addClass('wrong').text('用户名或密码错误');
                    }
                    console.log("success");
                })
                .fail(function () {
                    console.log("error");
                })
                .always(function () {
                    console.log("complete");
                });
        })
    });
});