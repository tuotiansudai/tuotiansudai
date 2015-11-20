require(['jquery', 'layerWrapper','csrf'], function ($,layer) {

    $(function () {
        //手机号校验
        var m = /^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$/;
        $('.phone-txt').blur(function () {
            var _this = $(this);
            if ($(this).val().length == 11 && m.test($(this).val())) {
                var _phone = $(this).val();
                $.get('/mobile-retrieve-password/mobile/' + _phone + '/is-exist?random=' + new Date().getTime(), function (data) {
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
            $('.verification-code-main b').hide();
            $('.verification-code-text').val('');

            layer.open({
                type: 1,
                title: '输入图形验证码',
                area: ['542px', '244px'],
                shadeClose: true,
                offset: '146px',
                scrollbar: false,
                content: $('.layer-box'),
                success: function (layero, index) {
                    refreshCaptcha();
                }
            });
            return false;
        });

        $('.complete').click(function () {
            var phone = $('.phone-txt').val();
            var imageCaptcha = $('.verification-code-text').val();
            if(imageCaptcha.length < 5){
                $('.verification-code-main b').html('验证码不正确').show();
                refreshCaptcha();
            }else{
                $.ajax({
                    url: '/mobile-retrieve-password/mobile/' + phone + '/imageCaptcha/' + imageCaptcha + '/send-mobile-captcha',
                    type: 'get',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (response) {
                    if (response.data.status) {
                        var num = 30;
                        // 倒计时
                        function countdown() {
                            $('.fetch-captcha').html(num + '秒后重新发送').addClass('grey');
                            $('.fetch-captcha').attr('disabled','disabled');
                            if (num == 0) {
                                clearInterval(count);
                                $('.fetch-captcha').html('重新发送').removeClass('grey');
                                $('.fetch-captcha').removeAttr('disabled','disabled');
                                $('.verification-code-text').val('');
                            }
                            num--;
                        }
                        var count = setInterval(countdown, 1000);
                        layer.closeAll();
                    }else{
                        if (response.data.isRestricted) {
                            $('.verification-code-main b').html('短信发送频繁，请稍后再试').show();
                            refreshCaptcha();
                        }else{
                            $('.verification-code-main b').html('验证码不正确').show();
                            refreshCaptcha();
                        }
                    }

                });

            }
        });

        // 刷新验证码
        var refreshCaptcha = function () {
            var captcha = $('.verification-code-img');
            captcha.attr('src', '/mobile-retrieve-password/image-captcha?' + new Date().toTimeString());
        };

        $('.verification-code-img').click(function () {
            refreshCaptcha();
        });

        $('.btn-send-form').click(function () {
            var _mobile = $('.phone-txt').val();
            var _captcha = $('.yzm-txt').val();
            window.location.href = '/mobile-retrieve-password/mobile/'+_mobile+'/captcha/'+_captcha+'/new-password-page';
        });


    })

});