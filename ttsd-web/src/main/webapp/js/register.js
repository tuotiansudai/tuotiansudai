require(['underscore', 'jquery', 'jquery.validate'], function (_, $) {

    var registerUserForm = $("#register-user-form");
    var registerAccountForm = $("#register-account-form");

    $('.register .fetch-captcha').click(function () {
        var validate = registerUserForm.validate();
        var isMobileValid = validate.check('.register .mobile');
        if (isMobileValid) {
            var mobile = $('.register .mobile').val();
            $.ajax({
                url: '/register/mobile/' + mobile + '/sendregistercaptcha',
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8',
                success: function (response) {
                    console.log(response);
                }
            });
        } else {
            validate.showErrors();
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
        success: 'valid',
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
            if (element.name === 'mobile') {
                $('.register .captcha').val('');
            }
            this.element(element);
        },
        rules: {
            loginName: {
                required: true,
                regex: "^[a-zA-Z0-9]+$",
                rangelength: [5, 25],
                isExist: "/register/loginName/{0}/isexist"
            },
            mobile: {
                required: true,
                digits: true,
                rangelength: [11, 11],
                isExist: "/register/mobile/{0}/isexist"
            },
            password: {
                required: true,
                minlength: 6
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
                isNotExist: "/register/loginName/{0}/isExist"
            }
        },
        messages: {
            loginName: {
                required: "请输入用户名",
                regex: "5至25位以字母、数字组合，请勿使用手机号",
                rangelength: "5至25位以字母、数字组合，请勿使用手机号",
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
                minlength: "密码至少6位"
            },
            captcha: {
                required: '请输入手机验证码',
                digits: '手机验证码格式不正确',
                rangelength: '手机验证码格式不正确',
                captchaVerify: '手机验证码不正确'
            },
            referrer: {
                isNotExist: "推荐人不存在"
            }
        }
    });
});
