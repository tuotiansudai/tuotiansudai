require(['underscore', 'jquery', 'jquery.validate', 'jquery.validate.extension', 'jquery.ajax.extension'], function (_, $) {

    var registerAccountForm = $('.register-step-two .register-account-form');

    registerAccountForm.validate({
        focusCleanup: true,
        focusInvalid: false,
        onfocusout: function (element) {
            this.element(element);
        },
        submitHandler: function (form) {
            $('.register-account').toggleClass('loading');
            form.submit();
        },
        onkeyup: function (element, event) {
            var excludedKeys = [16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];

            if ((event.which !== 9 || this.elementValue(element) !== "") && $.inArray(event.keyCode, excludedKeys) === -1) {
                this.element(element);
            }
        },
        rules: {
            userName: {
                required: true
            },
            identityNumber: {
                required: true,
                regex: /^[1-9]\d{13,16}[a-zA-Z0-9]$/,
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
