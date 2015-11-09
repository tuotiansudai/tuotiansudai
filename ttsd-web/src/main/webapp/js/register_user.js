require(['underscore', 'jquery', 'layer', 'jquery.validate', 'jquery.validate.extension', 'jquery.form', 'csrf'], function (_, $,layer) {

    var registerUserForm = $(".register-user-form"),
        fetchCaptchaElement = $('.fetch-captcha',registerUserForm);
    var $imgCaptchaDialog=$('.image-captcha-dialog');
    var imageCaptchaForm = $('.image-captcha-form',$imgCaptchaDialog),
        imageCaptchaElement = $('.image-captcha',$imgCaptchaDialog),
        imageCaptchaTextElement = $('.image-captcha-text',$imgCaptchaDialog),
        imageCaptchaSubmitElement = $('.image-captcha-confirm',$imgCaptchaDialog);

    /*获取验证码*/
    fetchCaptchaElement.on('click', function () {
        layer.open({
            type: 1,
            title: '手机验证',
            area: ['360px', '190px'],
            shadeClose: true,
            content: $('.image-captcha-dialog'),
            success: function(layero, index){
                $('.image-captcha-form label').remove();
                imageCaptchaTextElement.val('');
                refreshCaptcha();
            }
        });
    });
    // 刷新验证码
    var refreshCaptcha = function () {
        imageCaptchaElement.attr('src', '/register/user/image-captcha?' + new Date().getTime().toString());
    };

    imageCaptchaElement.click(function () {
        refreshCaptcha();
    });

    /*手机验证码*/
    imageCaptchaForm.validate({
        success: 'form-valid',
        focusInvalid: false,
        errorClass: 'form-error',
        onkeyup:true,
       // errorLabelContainer:$('.image-captcha-error'),
        onfocusout: function (element) {
            if (!this.checkable(element) && !this.optional(element)) {
                this.element(element);
            }
        },
        submitHandler: function (form) {
            var self = this;
            $(form).ajaxSubmit({
                data: {mobile: $('.mobile').val()},
                dataType: 'json',
                beforeSubmit: function (arr, $form, options) {
                    imageCaptchaSubmitElement.addClass("loading");
                    imageCaptchaSubmitElement.attr("disabled", true);
                },
                success: function (response) {
                    var data = response.data;
                    if (data.status && !data.isRestricted) {
                        //displayImageCaptchaDialog(false);
                        var seconds = 60;
                        var count = setInterval(function () {
                            fetchCaptchaElement.html(seconds + '秒后重新发送').css({
                                'background': '#666',
                                'pointer-events': 'none'
                            });
                            if (seconds == 0) {
                                clearInterval(count);
                                fetchCaptchaElement.html('重新发送').css({
                                    'background': '#f68e3a',
                                    'pointer-events': 'auto'
                                });
                            }
                            seconds--;
                        }, 1000);
                        return;
                    }

                    if (!data.status && data.isRestricted) {
                        self.showErrors({imageCaptcha: '短信发送频繁，请稍后再试'});
                    }

                    if (!data.status && !data.isRestricted) {
                        self.showErrors({imageCaptcha: '图形验证码不正确'});
                    }
                    self.invalid['imageCaptcha'] = true;
                    refreshCaptcha();
                },
                error: function () {
                    self.invalid['imageCaptcha'] = true;
                    self.showErrors({imageCaptcha: '图形验证码不正确'});
                    refreshCaptcha();
                },
                complete: function () {
                    imageCaptchaSubmitElement.removeClass("loading");
                    imageCaptchaSubmitElement.prop("disabled", false);
                }
            });
        },
        rules: {
            imageCaptcha: {
                required: true,
                regex: "^[a-zA-Z0-9]{5}$",
                imageCaptchaVerify: "/register/user/image-captcha/{0}/verify"
            }
        },
        messages: {
            imageCaptcha: {
                required: "请输入图形验证码",
                regex: "图形验证码不正确",
                imageCaptchaVerify: '图形验证码不正确'
            }
        }
    });

    registerUserForm.validate({
        success: 'form-valid',
        focusInvalid: false,
        errorClass: 'form-error',
        onkeyup:true,
        rules: {
            loginName: {
                required: true,
                checkUser:true,
                isExist: "/register/user/login-name/{0}/is-exist"
            },
            mobile: {
                required: true,
                checkMobile:true,
                isExist: "/register/user/mobile/{0}/is-exist"
            },
            password: {
                required: true,
                checkPass: true
            },
            captcha: {
                required: true,
                digits: true,
                rangelength: [6, 6],
                captchaVerify: {
                    param: function () {
                        var mobile = $('.register .mobile').val();
                        return "/register/user/mobile/" + mobile + "/captcha/{0}/verify"
                    }
                }
            },
            referrer: {
                isNotExist: "/register/user/login-name/{0}/is-exist"
            },
            agreement: {
                required: true
            }
        },
        messages: {
            loginName: {
                required: "请输入用户名",
                isExist: '用户名已存在'
            },
            mobile: {
                required: '请输入手机号',
                isExist: '手机号已存在'
            },
            password: {
                required: "请输入密码"
            },
            captcha: {
                required: '请输入验证码',
                digits: '验证码格式不正确',
                rangelength: '验证码格式不正确',
                captchaVerify: '验证码不正确'
            },
            referrer: {
                isNotExist: "推荐人不存在"
            },
            agreement: {
                required: "请同意服务协议"
            }
        },
        onkeyup: function (element, event) {
            var excludedKeys = [16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];

            if ((event.which !== 9 || this.elementValue(element) !== "") && $.inArray(event.keyCode, excludedKeys) === -1) {
                this.element(element);
            }
        },
        onfocusout: function (element) {
            if (!this.checkable(element) && !this.optional(element)) {
                this.element(element);
            }
        },
        showErrors: function (errorMap, errorList) {

            this.__proto__.defaultShowErrors.call(this);
            if (errorMap['mobile']) {
                $('.fetch-captcha').prop('disabled', true);
            }
        },
        success: function (error, element) {

            error.addClass("valid");
            if (element.name === 'mobile') {
                $('.fetch-captcha').prop('disabled', false);
            }
        }

    });

    //var moveAgree=function() {
    //    var $agreementDom=$('#agreement');
    //    $agreementDom.next('label').prepend($agreementDom.parent('label'));
    //}
    //用户名验证规则
    jQuery.validator.addMethod("checkUser", function(value, element) {
        var checkUser = /(?!^\d+$)^\w{6,20}$/;
        return this.optional(element) || (checkUser.test(value));
    }, "6位至20位数字与字母下划线组合，不能全部数字");

    //手机验证规则
    jQuery.validator.addMethod("checkMobile", function (value, element) {
        var mobile = /^1[3|4|5|7|8]\d{9}$/;
        return this.optional(element) || (mobile.test(value));
    }, "手机格式不对");

    jQuery.validator.addMethod("checkPass", function(value, element) {
        var checkPassword = /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+){6,20}$/;
        return this.optional(element) || (checkPassword.test(value));
    }, "6位至20位数字与字母组合");
    //^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$
});
