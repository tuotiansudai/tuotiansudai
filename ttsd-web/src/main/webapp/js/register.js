require(['jquery', 'jquery.validate'], function ($) {

    $.validator.setDefaults({
        submitHandler: function() {
            alert("submitted!");
        }
    });

    $.validator.addMethod(
        "regex",
        function(value, element, regexp) {
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        },
        "请检查您的输入"
    );

    function existFunc (value, element, urlTemplate) {
        var previous = this.previousValue( element ),
            validator, data;

        if ( previous.old === value) {
            return previous.valid;
        }
        if (value === '') {
            return true;
        }

        var url = $.validator.format(urlTemplate);
        previous.old = value;
        validator = this;
        this.startRequest( element );
        data = {};
        data[ element.name ] = value;

        $.ajax({
            url: url(value),
            type: 'GET',
            datatype: 'json',
            success: function(response) {
                var isExist = response.status && response.data.status;
                isExist = !isExist;
                if (isExist) {
                    errors = {};
                    message = validator.defaultMessage( element, "isExist" );
                    errors[ element.name ] = previous.message = $.isFunction( message ) ? message( value ) : message;
                    validator.invalid[ element.name ] = true;
                    validator.showErrors( errors );
                }else {
                    submitted = validator.formSubmitted;
                    validator.prepareElement( element );
                    validator.formSubmitted = submitted;
                    validator.successList.push( element );
                    delete validator.invalid[ element.name ];
                    validator.showErrors();
                }
                previous.valid = !isExist;
                validator.stopRequest( element, previous.valid );
            }
        });
        return "pending";
    }

    $.validator.addMethod("isExist", existFunc, $.validator.format("该记录已被占用"));
    $.validator.addMethod("vcodeExist", function(value, element, mobileNumberClass) {
        var mobileNumber = $(mobileNumberClass).val();
        var vcodeFormat = "/register/mobile/" + mobileNumber + "/captcha/{0}/verify";
        return existFunc.call(this, value, element, vcodeFormat);
    }, $.validator.format("该记录已被占用"));

    $("#registrationForm").validate({
        rules: {
            username: {
                required: true,
                regex: "^[a-zA-Z0-9]+$",
                rangelength: [5, 25],
                isExist: "/register/loginName/{0}/verify"
            },
            "mobile-number": {
                required: true,
                digits: true,
                rangelength: [11, 11],
                isExist: "/register/mobileNumber/{0}/verify"
            },
            password: {
                required: true,
                minlength: 6
            },
            vcode: {
                required: true,
                digits: true,
                vcodeExist: ".phone",
                isExist: "/register/mobile/{0}/captcha/{1}/verify"
            },
            referrer: {
                isExist: "/register/referrer/{0}/verify"
            }
        },
        messages: {
            username: {
                required: "请输入用户名",
                regex: "5至25位以字母、数字组合，请勿使用手机号",
                rangelength: "5至25位以字母、数字组合，请勿使用手机号"
            },
            "mobile-number": "手机号码不正确",
            password: {
                required: "请输入密码",
                minlength: "密码至少6位"
            },
            vcode: "验证码不正确",
            referrer: "用户不存在"
        }
    });

});
