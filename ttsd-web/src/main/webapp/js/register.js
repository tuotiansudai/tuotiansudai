require(['underscore', 'jquery', 'jquery.validate', 'csrf'], function (_, $) {

    var registerUserForm = $("#register-user-form");
    var registerAccountForm = $("#register-account-form");
    var fetchCaptchaElement = $('.fetch-captcha');
    var imageCaptchaElement = $('.verification-code-img');

    fetchCaptchaElement.on('click', function () {
        var clientH = $(window).height();
        $('.verification-code').css({'height': clientH, 'display': 'block'});
        $('.verification-code-text').val('');
        $('.verification-code-main b').hide();
        $('.complete').addClass('grey').attr('disabled');
        $('.verification-code-main').show();
        refreshCaptcha();
    });

    $('.close').on('click', function () {
        $('.verification-code, .verification-code-main').hide();
    });

    $('.complete').click(function () {
        var phone = $('.mobile').val();
        var captcha = $('.verification-code-text').val();
        var num = 30;
        // 倒计时
        function countdown() {
            fetchCaptchaElement.html(num + '秒后重新发送').css({'background': '#666', 'pointer-events': 'none'});
            if (num == 0) {
                clearInterval(count);
                fetchCaptchaElement.html('重新发送').css({'background': '#f68e3a', 'pointer-events': 'auto'});
            }
            num--;
        }

        var count = setInterval(countdown, 1000);
        $('.verification-code,.verification-code-main').hide();
        $.get('/register/mobile/' + phone + '/image-captcha/' + captcha + '/send-register-captcha');
    });

    // 刷新验证码
    var refreshCaptcha = function () {
        imageCaptchaElement.attr('src', '/register/image-captcha?' + new Date().getTime());
    };

    imageCaptchaElement.click(function () {
        refreshCaptcha();
    });

    // 弹出框验证码
    $('.verification-code-text').blur(function () {
        var _this = $(this);
        var _value = _this.val();
        if (_value.length < 5) {
            $('.verification-code-main b').css('display', 'inline-block');
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
                    $('.complete').addClass('grey').attr('disabled');
                    refreshCaptcha();
                }
            });
        }
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
                console.log(response);
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

    var registerUser = function (data) {
        $.ajax({
            type: 'post',
            url: '/register/user',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            success: function (response) {
                var registerAccountForm = $('#register-account-form');
                var registerUserForm = $('#register-user-form');
                if (response.data.status) {
                    registerAccountForm.find('.login-name').val(registerUserForm.find('.login-name').val());
                    registerAccountForm.find('.mobile').val(registerUserForm.find('.mobile').val());
                    $('.register .register-step-one-title').removeClass("active");
                    $('.register .register-step-one').removeClass("active");
                    $('.register .register-step-two-title').addClass("active");
                    $('.register .register-step-two').addClass("active");
                } else {
                    var validate = registerUserForm.validate();
                    validate.resetForm();
                    validate.checkForm();
                }
            },
            error: function (response) {
            }
        });
    };

    var registerAccount = function (data) {
        $.ajax({
            type: 'post',
            url: '/register/account',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            success: function (response) {
                if (response.data.status) {
                    window.location.href = "/";
                } else {
                    var validate = registerAccountForm.validate();
                    validate.resetForm();
                    validate.checkForm();
                }
            },
            error: function (response) {
            }
        });
    };

    registerAccountForm.validate({
        success: 'valid',
        focusCleanup: true,
        focusInvalid: false,
        onkeyup: false,
        onfocusout: function (element) {
            this.element(element);
        },
        submitHandler: function (validator) {
            var names = ['loginName', 'mobile', 'userName', 'identityNumber'];
            var elements = validator.elements;
            var postData = {};
            _.each(elements, function (element) {
                if (names.indexOf(element.name) !== -1) {
                    postData[element.name] = element.value;
                }
            });
            registerAccount(postData);
            return false;
        },
        rules: {
            userName: {
                required: true
            },
            identityNumber: {
                required: true,
                regex: "^[1-9]\\d{13,16}[a-zA-Z0-9]{1}$"
            }
        },
        messages: {
            userName: {
                required: "请输入姓名"
            },
            identityNumber: {
                required: '请输入身份证',
                regex: '身份证格式不正确'
            }
        }
    });

    registerUserForm.validate({
        focusCleanup: true,
        focusInvalid: false,
        onkeyup: false,
        submitHandler: function (validator) {
            var names = ['loginName', 'mobile', 'captcha', 'password', 'referrer', 'agreement'];
            var elements = validator.elements;
            var postData = {};
            _.each(elements, function (element) {
                if (names.indexOf(element.name) !== -1) {
                    if (element.name === 'agreement') {
                        postData[element.name] = element.checked;
                    } else {
                        postData[element.name] = element.value;
                    }
                }
            });
            registerUser(postData);
            return false;
        },
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
                isExist: "/register/login-name/{0}/is-exist"
            },
            mobile: {
                required: true,
                digits: true,
                rangelength: [11, 11],
                isExist: "/register/mobile/{0}/is-exist"
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
                        return "/register/mobile/" + mobile + "/captcha/{0}/verify"
                    }
                }
            },
            referrer: {
                isNotExist: "/register/login-name/{0}/is-exist"
            }
        },
        messages: {
            loginName: {
                required: "请输入用户名",
                regex: "只能字母和数字，至少包含一个字母",
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
            }
        }
    });
});
