require(['jquery', 'csrf'], function ($) {

    $(function () {
        //手机号校验
        var m = /^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$/;
        $('.phone-txt').blur(function () {
            var _this = $(this);
            if ($(this).val().length == 11 && m.test($(this).val())) {
                var _phone = $(this).val();
                $.get('/mobile-retrieve-password/mobile/' + _phone + '/is-exist?random=' + new Date().getTime(), function (data) {
                    console.log(data.data.status);
                    if (data.data.status) {
                        _this.addClass('valid-ok');
                        $('.send-yzm').removeAttr('disabled').removeClass('grey');
                        $('.error').html('');
                        if ($('.yzm').hasClass('valid-ok')) {
                            $('.btn-send-form').removeAttr('disabled').removeClass('grey');
                        } else {
                            $('.btn-send-form').attr('disabled', 'disabled').addClass('grey');
                        }
                    } else {
                        _this.removeClass('valid-ok');
                        $('.error').html('手机号格式不合法或手机不存在');
                        $('.send-yzm').attr('disabled', 'disabled').addClass('grey')
                    }
                });

            } else {
                _this.removeClass('valid-ok');
                $('.error').html('手机号格式不合法或手机不存在');
                $('.send-yzm').attr('disabled', 'disabled').addClass('grey');
            }
        });

        $('.yzm-txt').blur(function () {
            var _this = $(this);
            if ($(this).val().length == 6) {
                var _phone = $('.phone-txt').val();
                var _captcha = $(this).val();
                $.get('/mobile-retrieve-password/mobile/' + _phone + '/captcha/' + _captcha + '/verify?random=' + new Date().getTime(), function (data) {
                    if (data.data.status) {
                        _this.closest('.yzm').addClass('valid-ok');
                        $('.error').html('');
                        if ($('.phone-txt').hasClass('valid-ok')) {
                            $('.btn-send-form').removeAttr('disabled').removeClass('grey');
                        } else {
                            $('.btn-send-form').attr('disabled', 'disabled').addClass('grey');
                        }
                    } else {
                        _this.closest('.yzm').removeClass('valid-ok');
                        $('.error').html('验证码不正确');
                        $('.btn-send-form').attr('disabled', 'disabled').addClass('grey');
                    }
                });

            } else {
                _this.closest('.yzm').removeClass('valid-ok');
                $('.error').html('验证码不正确');
                $('.btn-send-form').attr('disabled', 'disabled').addClass('grey');
            }
        });


        $('.fetch-captcha').on('click', function () {
            $('.verification-code-text').val('');
            $('.layer-box').show();
            refreshCaptcha();
        });
        $('.close').on('click', function () {
            $('.layer-box').hide();
        });
        //$('.phone-txt').blur(function () {
        //    var _this = $(this);
        //    if (_this.val() == '' || _this.hasClass('error')) {
        //        $('.fetch-captcha').addClass('grey').attr('disabled', 'disabled');
        //    } else {
        //        $('.fetch-captcha').removeClass('grey').removeAttr('disabled');
        //    }
        //});

        $('.complete').click(function () {
            var phone = $('.phone-txt').val();
            var captcha = $('.verification-code-text').val();
            var num = 30;
            // 倒计时
            function countdown() {
                $('.fetch-captcha').html(num + '秒后重新发送').addClass('grey');
                if (num == 0) {
                    clearInterval(count);
                    $('.fetch-captcha').html('重新发送').removeClass('grey');
                    $('.complete').addClass('grey').attr('disabled','disabled');
                    $('.verification-code-text').val('');
                }
                num--;
            }

            var count = setInterval(countdown, 1000);
            $('.layer-box').hide();
            $.get('/mobile-retrieve-password/mobile/' + phone + '/captcha/' + captcha + '/send-mobile-captcha');
        });

        // 刷新验证码
        var refreshCaptcha = function () {
            var captcha = $('.verification-code-img');
            captcha.attr('src', '/mobile-retrieve-password/image-captcha?' + new Date().toTimeString());
        };

        $('.verification-code-img').click(function () {
            refreshCaptcha();
        });

        // 弹出框验证码
        $('.verification-code-text').blur(function () {
            var _this = $(this);
            var _value = _this.val();
            if (_value.length < 5) {
                $('.verification-code-main b').css('display', 'inline-block');
                $('.complete').addClass('grey').attr('disabled','disabled');
            } else {
                $.ajax({
                    url: '/register/image-captcha/' + _value + '/verify',
                    type: 'get',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (response) {
                    if (response.data.status) {
                        $('.verification-code-main b').hide();
                        $('.complete').removeClass('grey').removeAttr('disabled');
                    } else {
                        $('.verification-code-main b').css('display', 'inline-block');
                        $('.complete').addClass('grey').attr('disabled','disabled');
                        refreshCaptcha();
                    }
                });
            }
        });

        $('.btn-send-form').click(function () {
            var _mobile = $('.phone-txt').val();
            var _captcha = $('.yzm-txt').val();
            window.location.href = '/mobile-retrieve-password/mobile/'+_mobile+'/captcha/'+_captcha+'/new-password-page';
        });


    })

});