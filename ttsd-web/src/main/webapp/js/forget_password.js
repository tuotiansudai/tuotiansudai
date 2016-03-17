require(['jquery', 'layerWrapper','jquery.validate', 'jquery.validate.extension', 'jquery.form','jquery.ajax.extension'], function ($, layer) {

    var $retrievePasswordBox=$('#retrievePasswordBox'),
        $retrieveForm=$('.retrieve-form',$retrievePasswordBox),
        $btnSend=$('.btn-send-form',$retrievePasswordBox),
        $getCaptcha = $('.fetch-captcha', $retrievePasswordBox);

    var $verificationForm=$('.verification-code-main'),
        $imageCaptchaSubmit = $('.image-captcha-confirm',$verificationForm);

    $('.phone-txt',$retrievePasswordBox).on('focusout',function(option) {
        $getCaptcha.addClass('btn').removeClass('btn-normal').prop('disabled',true);
    });

    /* form blank validate */
    $retrieveForm.validate({
        focusInvalid: false,
        onkeyup:false,
        rules: {
            mobile: {
                required: true,
                digits: true,
                minlength: 11,
                maxlength: 11,
                isNotExist: '/mobile-retrieve-password/mobile/{0}/is-exist?random=' + new Date().getTime()
            },
            captcha: {
                required: true,
                digits: true,
                minlength: 6,
                maxlength:6,
                captchaVerify: {
                    param: function () {
                        var _phone = $('input[name="mobile"]').val(),
                            _captcha=$('input[name="captcha"]').val();
                        if(_captcha.length==6) {
                            $btnSend.prop('disabled',false);
                            return '/mobile-retrieve-password/mobile/' + _phone + '/captcha/{0}/verify?random=' + new Date().getTime();
                        }

                    }
                }
            }
        },
        messages: {
            mobile: {
                required: '请输入手机号',
                digits: '必须是数字',
                minlength: '手机格式不正确',
                maxlength: '手机格式不正确',
                isNotExist: '手机号不存在'
            },
            captcha: {
                required: '请输入验证码',
                digits: '验证码格式不正确',
                minlength: '验证码格式不正确',
                maxlength: '验证码格式不正确',
                captchaVerify: '验证码不正确'
            }
        },
        success: function (error, element) {
            var disableButton=$('.disable-button',$retrievePasswordBox);
            if(!disableButton.length && element.name === 'mobile') {
                $getCaptcha.addClass('btn-normal').removeClass('btn').prop('disabled',false);
            }
        },
        submitHandler:function(form) {
            var _mobile = $('.phone-txt').val(),
                _captcha = $('.yzm-txt').val();
            window.location.href = '/mobile-retrieve-password/mobile/'+_mobile+'/captcha/'+_captcha+'/new-password-page';
        }
    });


        $getCaptcha.on('click', function () {
            $('.verification-code-main b').hide();
            $('.verification-code-text').val('');

            layer.open({
                type: 1,
                title: '输入图形验证码',
                area: ['380px', '200px'],
                shadeClose: true,
                offset: '146px',
                content: $('.verification-code-main'),
                success: function (layero, index) {
                    refreshCaptcha();
                }
            });
            return false;
        });

        $imageCaptchaSubmit.click(function () {
            var $this=$(this),
             phone = $('.phone-txt').val(),
             imageCaptcha = $('.verification-code-text').val();
            $this.addClass('loading');
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
                    $this.removeClass('loading');
                    if (response.data.status) {
                        layer.closeAll();
                        var seconds = 30;
                        var count = setInterval(function () {
                            $getCaptcha.html(seconds + '秒后重新发送').addClass('btn disable-button').removeClass('btn-normal').prop('disabled',true);
                            if (seconds == 0) {
                                clearInterval(count);
                                $getCaptcha.html('重新发送').removeClass('btn disable-button').addClass('btn-normal').prop('disabled',false);
                                $('.verification-code-text').val('');
                            }
                            seconds--;
                        }, 1000);
                        return;

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

    })
