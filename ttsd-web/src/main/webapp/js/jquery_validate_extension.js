require(['jquery', 'jquery.validate','commonFun'], function ($) {
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

        ajaxHelper(url(encodeURIComponent(value)), successHandler, {validator: validator, element: element, errorMessage: errorMessage});

        return "pending";
    };

    var notExistFunc = function (value, element, urlTemplate) {
        var previous = this.previousValue(element);
        var validator = this;

        if ($('.register-user-form input[name="referrer"]').val() === '') {
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

        ajaxHelper(url(encodeURIComponent(value)), successHandler, {validator: validator, element: element, errorMessage: errorMessage});

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

        var isMobileValid = validator.check('input[name="mobile"]');

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

    $.validator.addMethod(
        "regex",
        function (value, element, regexp) {
            return this.optional(element) || regexp.test(value);

        },
        "请检查您的输入"
    );

    $.validator.addMethod('identityCheckValid',function(value, element) {
        var checked=commonFun.IdentityCodeValid(value);
        console.log(checked);
        return this.optional(element) || checked ;

    },'');

    $.validator.addMethod('identityCardAge',function(value, element) {
        var checked=commonFun.IdentityCodeValid(value),
            getAge=value.substring(6,14),
            currentDay=new Date(),
            checkedAge=true;
        if(checked) {
            var y=currentDay.getFullYear(),
                m=currentDay.getMonth()+ 1,
                d=currentDay.getDate();
            var today = y+(m<10?('0'+m):m)+(d<10?('0'+d):d);
            var myAge=Math.floor((today-getAge)/10000);
            if(myAge<18) {
                checkedAge=false;
            }
        }
        return this.optional(element) || checkedAge ;

    },'');

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


});