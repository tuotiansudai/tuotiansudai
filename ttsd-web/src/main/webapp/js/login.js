require(['jquery'], function ($) {
    $(function () {
        // 异步请求
        var ajaxPost = function (url, arg, ele) {
            var _this = ele;
            var attr = attr;
            var arg = arg;
            $.ajax({
                url: url,
                type: 'POST',
                dataType: 'json',
                data: arg,

            })
                .done(function (data) {
                    if (data.status) {
                        _this.addClass('lock').removeClass('unlock');
                        _this.closest('label').next().addClass('right').removeClass('wrong').text('');
                    } else {
                        _this.closest('label').next().addClass('wrong').removeClass('right').text("输入有误");
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
            var _this = $(this);
            var _value = _this.val();
            var arg = {user: _value};  //传递数据
            if (_this.val() == '') {
                _this.removeClass('lock').addClass('unlock');
                _this.closest('label').next().addClass('wrong').removeClass('right').text('请输入用户名');
            } else {
                ajaxPost(_API_USER, arg, _this, "user");
            }
            validSuccess();
        });

        //密码校验
        $('.password').blur(function () {
            var _this = $(this);
            var _value = _this.val();
            if (_this.val() == '' || _value.length < 6) {
                _this.removeClass('lock').addClass('unlock');
                _this.closest('label').next().addClass('wrong').removeClass('right').text('请输入密码,密码至少6位');
            } else {
                _this.addClass('lock').removeClass('unlock');
                _this.closest('label').next().addClass('right').removeClass('wrong').text('');
            }
            validSuccess();
        });

        //验证码校验
        $('.captcha').blur(function () {
            var _this = $(this);
            var _value = _this.val();
            var arg = {yzm: _value};
            if (_this.val() == '') {
                _this.addClass('lock');
                _this.closest('label').next().addClass('wrong').removeClass('right').text('请输入验证码');
            } else {
                ajaxPost(_API_YZM, arg, _this);
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
                        $('.userPass').addClass('lock').removeClass('unlock');
                        $('.userPass').closest('label').next().text(data.msg);
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