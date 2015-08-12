require(['jquery'], function ($) {
    $(function () {
        // 异步请求
        var ajaxPost = function (url, arg, ele) {
            var msg = "验证码错误";
            var _this = ele;
            var arg = arg;
            $.ajax({
                url: url,
                type: 'GET',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            })
                .done(function (data) {
                    if (data.data.status) {
                        _this.addClass('lock').removeClass('unlock');
                        $('.error').removeClass('wrong').text('');
                    } else {
                        $('.error').addClass('wrong').text(msg);
                        _this.removeClass('lock').addClass('unlock');
                    }
                    validSuccess();
                    console.log("success");
                })
                .fail(function () {
                    console.log("error");
                })
                .always(function () {
                    console.log("complete");
                });
        };


        //及时校验 用户名
        $('.login-name').blur(function () {
            var msg = "用户名不能为空";
            var _this = $(this);
            var _value = _this.val();
            if (_this.val() == '') {
                _this.removeClass('lock').addClass('unlock');
                $('.error').addClass('wrong').text(msg);
            } else {
                $('.error').removeClass('wrong').text('');
            }
            validSuccess();
        });

        //密码校验
        $('.password').blur(function () {
            var msg = "密码不能为空";
            var _this = $(this);
            if (_this.val() == '') {
                _this.removeClass('lock').addClass('unlock');
                $('.error').addClass('wrong').text(msg);
            } else {
                _this.addClass('lock').removeClass('unlock');
                $('.error').removeClass('wrong').text('');
            }
            validSuccess();
        });

        //验证码校验
        $('.captcha').blur(function () {
            var _this = $(this);
            var msg = "验证码错误";
            var _value = _this.val();
            var arg = {yzm: _value};
            if (_this.val() == '') {
                _this.addClass('lock').removeClass('unlock');
                $('.error').addClass('wrong').text(msg);
            } else {
                ajaxPost('/login/captcha/'+ _value + '/verify', arg, _this);
            }
            validSuccess();
        });

        // 校验是否全部通过 按钮变亮
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
        }

        $('.login-now').click(function () {
            var data = $('.form-login').serialize();
            $.ajax({
                url: url,    //post 提交地址
                type: 'POST',
                dataType: 'json',
                data: data,
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