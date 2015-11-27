require(['jquery','jquery.validate', 'jquery.validate.extension', 'jquery.form','csrf'], function ($) {

    var $inputGetPassword=$('#inputGetPassword'),
        $newPassword=$('input[name="password"]',$inputGetPassword),
        $repeatnewPassword=$('input[name="repeatPassword"]',$inputGetPassword),
        $GetPasswordForm=$('form',$inputGetPassword),
        $btnSendData=$('.btn-send-form-second',$inputGetPassword);

    $GetPasswordForm.validate({
        focusInvalid: false,
        rules: {
            password: {
                required: true,
                regex: /^(?=.*[^\d])(.{6,20})$/
            },
            repeatPassword: {
                required: true,
                equalTo: 'input[name="password"]'
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
            $(form).submit();

        }
    });


});