require(['underscore', 'jquery', 'jquery.validate', 'csrf'], function (_, $) {

    var registerAccountForm = $('.register-step-two .register-account-form');

    $.validator.addMethod(
        "regex",
        function (value, element, regexp) {
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        },
        "请检查您的输入"
    );

    registerAccountForm.validate({
        success: 'valid',
        focusCleanup: true,
        focusInvalid: false,
        onkeyup: false,
        onfocusout: function (element) {
            this.element(element);
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
});
