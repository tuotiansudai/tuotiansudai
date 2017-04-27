require(['jquery', 'underscore', 'layerWrapper', 'commonFun', 'superslide', 'placeholder', 'jquery.validate', 'jquery.validate.extension', 'jquery.form', 'jquery.ajax.extension'], function ($, _, layer, commonFun) {
    (function () {
        var $landingContainerBox = $('.landingContainerBox');

        //注册
        var $registerForm = $('.register-user-form', $landingContainerBox);
        var $tfPhoneNum = $('#mobile');
        var $btnFetchCaptcha = $('.fetch-captcha');
        var $appCaptcha = $('#appCaptcha');
        var $btnChangeImgCode = $('.img-change');

        //
        var $landingContainer = $('.landing-container');
        var $btnCoupon = $('#btn-get-coupon', $landingContainer);

        //
        setupViews();
        setupListeners();

        function setupViews() {
            showReferrerInfoIfNeeded();
            refreshImgCaptcha();

            // phone validate
            jQuery.validator.addMethod("isPhone", function (value, element) {
                var tel = /0?(13|14|15|18)[0-9]{9}/;
                return this.optional(element) || (tel.test(value));
            }, "请正确填写您的手机号码");
        }

        function setupListeners() {
            $btnCoupon.on('click', function (event) {
                event.preventDefault();

                if (isFromApp()) {
                    window.location.href = "/register/user";
                } else {
                    if (isFromMobile()) {
                        $('body,html').animate({scrollTop: $('.landingContainerBox').height()}, 'fast');
                    } else {
                        $('body,html').animate({scrollTop: 0}, 'fast');
                    }
                }
            });

            //
            $appCaptcha.on('focus', function (event) {
                $('#appCaptchaErr').html('');
            });

            //refresh images captcha
            $btnChangeImgCode.on('click', function (event) {
                event.preventDefault();
                refreshImgCaptcha();
            });

            //show protocol info
            $('.show-agreement').on('click', function (event) {
                event.preventDefault();
                showAgreementProtocol();
            });

            //phone focusout
            $('#appCaptcha').on('focusout', function (event) {
                event.preventDefault();
                if ($tfPhoneNum.val() != '' && /0?(13|14|15|18)[0-9]{9}/.test($tfPhoneNum.val()) && $('#appCaptcha').val() != '') {
                    $btnFetchCaptcha.prop('disabled', false);
                } else {
                    $btnFetchCaptcha.prop('disabled', true);
                }
            });

            $('#agreementBox').find('.close-tip').on('click', function () {
                layer.closeAll();
            })

            $btnFetchCaptcha.on('click', function (event) {
                event.preventDefault();
                getSmsCaptcha();
            });

            //form validate
            $registerForm.validate({
                focusInvalid: false,
                errorPlacement: function (error, element) {
                    error.appendTo($('#' + element.attr('id') + 'Err'));
                },
                rules: {
                    mobile: {
                        required: true,
                        digits: true,
                        isPhone: true,
                        minlength: 11,
                        maxlength: 11,
                        isExist: "/register/user/mobile/{0}/is-exist"
                    },
                    password: {
                        required: true,
                        regex: /^(?=.*[^\d])(.{6,20})$/
                    },
                    appCaptcha: {
                        required: true
                    },
                    captcha: {
                        required: true,
                        digits: true,
                        maxlength: 6,
                        minlength: 6,
                        captchaVerify: {
                            param: function () {
                                var mobile = $('#mobile').val();
                                return "/register/user/mobile/" + mobile + "/captcha/{0}/verify"
                            }
                        }
                    },
                    agreement: {
                        required: true
                    }
                },
                messages: {
                    mobile: {
                        required: '请输入手机号',
                        digits: '必须是数字',
                        minlength: '手机格式不正确',
                        isPhone: '请输入正确的手机号码',
                        maxlength: '手机格式不正确',
                        isExist: '手机号已存在'
                    },
                    password: {
                        required: "请输入密码",
                        regex: '6位至20位，不能全是数字'
                    },
                    appCaptcha: {
                        required: '请输入验证码'
                    },
                    captcha: {
                        required: '请输入手机验证码',
                        digits: '验证码格式不正确',
                        maxlength: '验证码格式不正确',
                        minlength: '验证码格式不正确',
                        captchaVerify: '验证码不正确'
                    },
                    agreement: {
                        required: "请同意服务协议"
                    }
                },
                submitHandler: function (form) {
                    form.submit();
                }
            });
        }

        function showReferrerInfoIfNeeded() {
            var urlObj = globalFun.parseURL(location.href);
            var referNum = urlObj.params.referrer;

            if (referNum) {
                //有推荐人
                var mobileNum = commonFun.uncompile(referNum);
                $('input[name="referrer"]', $landingContainerBox).val(mobileNum);
                //通过手机号得到用户名
                $.ajax({
                    url: "/activity/get-realRealName?mobile=" + mobileNum,
                    type: 'GET',
                    dataType: 'json'
                }).done(function (data) {
                    //姓名的第一个字母用*替换
                    $('.refer-name', $landingContainerBox).text(data);
                });
            }
            else {
                //无推荐人
                $('.refer-person-info', $landingContainerBox).hide();
            }
        }

        function showAgreementProtocol() {
            var area = ['950px', '600px'];

            if (isFromMobile()) {
                area = ['100%', '100%'];
            }
            layer.open({
                type: 1,
                title: '拓天速贷服务协议',
                area: area,
                shadeClose: true,
                move: false,
                scrollbar: true,
                skin: 'register-skin',
                content: $('#agreementBox')
            });
        }

        function isFromMobile() {
            var agentType = globalFun.browserRedirect();
            return agentType == 'mobile';
        }

        function isFromApp() {
            var urlObj = globalFun.parseURL(location.href);
            return urlObj.params.source == 'app';
        }

        function refreshImgCaptcha() {
            $('.image-captcha img').each(function (index, el) {
                $(this).attr('src', '/register/user/image-captcha?' + new Date().getTime().toString());
            });
        };

        function getSmsCaptcha() {
            var captchaVal = $('#appCaptcha').val();
            var mobileNum = $tfPhoneNum.val();

            $.ajax({
                url: '/register/user/send-register-captcha',
                type: 'POST',
                dataType: 'json',
                data: {imageCaptcha: captchaVal, mobile: mobileNum}
            })
                .done(function (data) {
                    var countdown = 60;
                    if (data.data.status && !data.data.isRestricted) {
                        timer = setInterval(function () {
                            $btnFetchCaptcha.prop('disabled', true).text(countdown + '秒后重发');
                            countdown--;
                            if (countdown == 0) {
                                clearInterval(timer);
                                countdown = 60;
                                $btnFetchCaptcha.prop('disabled', false).text('重新发送');
                            }
                        }, 1000);
                        return;
                    }
                    if (!data.data.status && data.data.isRestricted) {
                        $('#appCaptchaErr').html('短信发送频繁,请稍后再试');
                    }

                    if (!data.data.status && !data.data.isRestricted) {
                        $('#appCaptchaErr').html('图形验证码错误');
                    }
                    refreshImgCaptcha();
                })
                .fail(function () {
                    refreshImgCaptcha();
                    layer.msg('请求失败，请重试！');
                });
        }
    })();
});
