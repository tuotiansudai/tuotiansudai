require(['jquery','jquery.validate', 'jquery.validate.extension','jquery.ajax.extension'], function ($) {

    var $inputGetPassword=$('#inputGetPassword'),
        $GetPasswordForm=$('form',$inputGetPassword);

    $GetPasswordForm.validate({
        focusInvalid: false,
        rules: {
            password: {
                required: true,
                regex: /^(?=.*[^\d])(.{6,20})$/
            },
            repeatPassword: {
                required: true,
                equalTo: '#newPassword'
            }
        },
        messages:{
            password: {
                required: "请输入密码",
                regex: '6位至20位，不能全是数字'
            },
            repeatPassword: {
                required: "请输入重复密码",
                equalTo: '2次输入的密码不一致'
            }
        },

        submitHandler:function(form) {

            form.submit();

        }
    });


});