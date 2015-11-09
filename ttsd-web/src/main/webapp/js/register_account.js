require(['underscore', 'jquery', 'jquery.validate', 'jquery.validate.extension', 'csrf'], function (_, $) {

    var registerAccountForm = $('.register-step-two .register-account-form');

    registerAccountForm.validate({
        success: 'valid',
        focusCleanup: true,
        focusInvalid: false,
        onkeyup: false,
        onfocusout: function (element) {
            this.element(element);
        },
        submitHandler: function (form) {
            $('.register-account').toggleClass('loading');
            form.submit();
        },
        rules: {
            userName: {
                required: true
            },
            identityNumber: {
                required: true,
                regex: "^[1-9]\\d{13,16}[a-zA-Z0-9]{1}$",
                isExist: "/register/account/identity-number/{0}/is-exist"
            }
        },
        messages: {
            userName: {
                required: "请输入姓名"
            },
            identityNumber: {
                required: '请输入身份证',
                regex: '身份证格式不正确',
                isExist: "身份证已存在"
            }
        }
    });
});
