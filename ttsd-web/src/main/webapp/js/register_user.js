require(['underscore', 'jquery', 'jquery.validate', 'csrf'], function (_, $) {

    var registerUserForm = $(".register-step-one .register-user-form");
    var imageCaptchaForm = $('.image-captcha-dialog .image-captcha-form');

    var fetchCaptchaElement = $('.register-user-form .fetch-captcha');
    var imageCaptchaElement = $('.image-captcha-dialog .image-captcha');
    var imageCaptchaTextElement = $('.image-captcha-dialog .image-captcha-text');

    fetchCaptchaElement.on('click', function () {
        displayImageCaptchaDialog(true);
    });

    $('.image-captcha-dialog .close').on('click', function () {
        displayImageCaptchaDialog(false);
    });

    var displayImageCaptchaDialog = function (isShow) {
        var imageCaptchaDialogMask = $('.image-captcha-dialog-mask');
        var imageCaptchaDialog = $('.image-captcha-dialog');
        if (isShow) {
            var clientH = $(window).height();
            $('.image-captcha-form label').remove();
            imageCaptchaDialogMask.css({'height': clientH, 'display': 'block'});
            imageCaptchaTextElement.val('');
            imageCaptchaDialog.show();
            refreshCaptcha();
        } else {
            imageCaptchaDialog.hide();
            imageCaptchaDialogMask.hide();
        }
    };

    // 刷新验证码
    var refreshCaptcha = function () {
        imageCaptchaElement.attr('src', '/register/user/image-captcha?' + new Date().getTime().toString());
    };

    imageCaptchaElement.click(function () {
        refreshCaptcha();
    });

    $.validator.addMethod(
        "regex",
        function (value, element, regexp) {
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        },
        "请检查您的输入"
    );

    var ajaxHelper = function (url, successHandler, options) {
        var isSuccess = false;
        var validator = options.validator;
        var element = options.element;
        var previous = validator.previousValue(element);
        var errorMessage = options.errorMessage;
        $.ajax({
            url: url,
            type: 'GET',
            dataType: 'json',
            error: function (response) {
                previous.valid = isSuccess;
                validator.stopRequest(element, isSuccess);
            },
            success: function (response) {
                isSuccess = successHandler(response);
                if (isSuccess) {
                    var submitted = validator.formSubmitted;
                    validator.prepareElement(element);
                    validator.formSubmitted = submitted;
                    validator.successList.push(element);
                    delete validator.invalid[element.name];
                    validator.showErrors();
                } else {
                    var errors = {};
                    errors[element.name] = previous.message = $.isFunction(errorMessage) ? errorMessage(value) : errorMessage;
                    validator.invalid[element.name] = true;
                    validator.showErrors(errors);
                }
                previous.valid = isSuccess;
                validator.stopRequest(element, isSuccess);
            }
        });
    };

    var existFunc = function (value, element, urlTemplate) {
        var previous = this.previousValue(element);
        var validator = this;

        if (previous.old === value) {
            return previous.valid;
        } else {
            previous.old = value;
        }

        this.startRequest(element);

        var errorMessage = validator.defaultMessage(element, "isExist");

        var successHandler = function (response) {
            return response.success && !response.data.status;
        };

        var url = $.validator.format(urlTemplate);

        ajaxHelper(url(value), successHandler, {validator: validator, element: element, errorMessage: errorMessage});

        return "pending";
    };

    var notExistFunc = function (value, element, urlTemplate) {
        var previous = this.previousValue(element);
        var validator = this;

        if ($('.register .referrer').val() === '') {
            validator.showErrors();
            return 'pending';
        }

        if (previous.old === value) {
            return previous.valid;
        } else {
            previous.old = value;
        }

        this.startRequest(element);

        var errorMessage = validator.defaultMessage(element, "isNotExist");

        var successHandler = function (response) {
            return response.success && response.data.status;
        };

        var url = $.validator.format(urlTemplate);

        ajaxHelper(url(value), successHandler, {validator: validator, element: element, errorMessage: errorMessage});

        return "pending";
    };

    var imageCaptchaVerifyFun = function (value, element, urlTemplate) {
        var previous = this.previousValue(element);
        var validator = this;

        if (previous.old === value) {
            return previous.valid;
        } else {
            previous.old = value;
        }

        this.startRequest(element);

        var errorMessage = validator.defaultMessage(element, "imageCaptchaVerify");

        var successHandler = function (response) {
            return response.success && response.data.status;
        };

        var url = $.validator.format(urlTemplate);

        ajaxHelper(url(value), successHandler, {validator: validator, element: element, errorMessage: errorMessage});

        return "pending";
    };

    var captchaVerifyFunc = function (value, element, urlTemplate) {
        var previous = this.previousValue(element);
        var validator = this;

        var isMobileValid = validator.check('.register .mobile');

        if (!isMobileValid) {
            return false;
        }

        if (previous.old === value) {
            return previous.valid;
        } else {
            previous.old = value;
        }

        this.startRequest(element);

        var errorMessage = validator.defaultMessage(element, "captchaVerify");

        var successHandler = function (response) {
            return response.success && response.data.status;
        };

        var url = $.validator.format(urlTemplate);

        ajaxHelper(url(value), successHandler, {validator: validator, element: element, errorMessage: errorMessage});

        return "pending";
    };

    $.validator.addMethod("isExist", function (value, element, urlTemplate) {
        return existFunc.call(this, value, element, urlTemplate);
    }, $.validator.format("该记录已存在"));

    $.validator.addMethod("isNotExist", function (value, element, urlTemplate) {
        return notExistFunc.call(this, value, element, urlTemplate);
    }, $.validator.format("该记录已存在"));

    $.validator.addMethod("captchaVerify", function (value, element, urlTemplate) {
        return captchaVerifyFunc.call(this, value, element, urlTemplate);
    }, $.validator.format("手机验证码不正确"));

    $.validator.addMethod("imageCaptchaVerify", function (value, element, urlTemplate) {
        return imageCaptchaVerifyFun.call(this, value, element, urlTemplate);
    }, $.validator.format("图形验证码不正确"));

    var sendRegisterCaptcha = function (data) {
        $.ajax({
            url: imageCaptchaForm.attr("action"),
            type: 'post',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            success: function (response) {
                if (response.data.status) {
                    displayImageCaptchaDialog(false);
                    var seconds = 60;
                    var count = setInterval(function () {
                        fetchCaptchaElement.html(seconds + '秒后重新发送').css({
                            'background': '#666',
                            'pointer-events': 'none'
                        });
                        if (seconds == 0) {
                            clearInterval(count);
                            fetchCaptchaElement.html('重新发送').css({'background': '#f68e3a', 'pointer-events': 'auto'});
                        }
                        seconds--;
                    }, 1000);
                } else {
                    var validate = imageCaptchaForm.validate();
                    validate.resetForm();
                    validate.checkForm();
                }
            }
        });
    };

    imageCaptchaForm.validate({
        success: 'valid',
        focusCleanup: true,
        focusInvalid: false,
        onkeyup: false,
        onfocusout: function (element) {
            this.element(element);
        },
        submitHandler: function (validator) {
            var mobile = $('.mobile').val();
            var names = ['imageCaptcha'];
            var elements = validator.elements;
            var postData = {"mobile": mobile};
            _.each(elements, function (element) {
                if (names.indexOf(element.name) !== -1) {
                    postData[element.name] = element.value;
                }
            });
            sendRegisterCaptcha(postData);
            return false;
        },
        showErrors: function (errorMap, errorList) {
            this.__proto__.defaultShowErrors.call(this);
            if (errorMap['imageCaptcha']) {
                refreshCaptcha();
            }
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
        focusCleanup: true,
        focusInvalid: false,
        onkeyup: false,
        onfocusout: function (element) {
            this.element(element);
        },
        showErrors: function (errorMap, errorList) {
            this.__proto__.defaultShowErrors.call(this);
            if (errorMap['mobile']) {
                $('.fetch-captcha').addClass('grey').attr('disabled', 'disabled');
            }
        },
        success: function (error, element) {
            error.addClass("valid");
            if (element.name === 'mobile') {
                $('.fetch-captcha').removeClass('grey').removeAttr('disabled');
            }
        },
        rules: {
            loginName: {
                required: true,
                rangelength: [5, 25],
                regex: "(?!^\\d+$)^([a-zA-Z0-9]+)$",
                isExist: "/register/user/login-name/{0}/is-exist"
            },
            mobile: {
                required: true,
                digits: true,
                rangelength: [11, 11],
                isExist: "/register/user/mobile/{0}/is-exist"
            },
            password: {
                required: true,
                rangelength: [6, 20],
                regex: '^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$'
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
                regex: "字母和数字组合",
                rangelength: "长度5至25位",
                isExist: '用户名已存在'
            },
            mobile: {
                required: '请输入手机号',
                digits: '手机号格式不正确',
                rangelength: '手机号格式不正确',
                isExist: '手机号已存在'
            },
            password: {
                required: "请输入密码",
                regex: "只能字母和数字组合",
                rangelength: "长度6至20位"
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
        }
    });
});
