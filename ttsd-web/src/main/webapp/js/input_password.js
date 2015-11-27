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

        //function verifyPassword(field) {
        //    if (field.val() != '') {
        //
        //        var regex = /^(?=.*[^\d])(.{6,20})$/;
        //        if (field.val().length < 6 || field.val().length > 20) {
        //            $('.error').html('长度6至20位');
        //            $('.btn-send-form-second').addClass('grey').attr('disabled', 'disabled');
        //            return false;
        //        } else {
        //            $('.btn-send-form-second').removeClass('grey').removeAttr('disabled');
        //            $('.error').html('');
        //        }
        //        if (!regex.test(field.val())) {
        //            $('.error').html('6位至20位，不能全是数字');
        //            $('.btn-send-form-second').addClass('grey').attr('disabled', 'disabled');
        //            return false;
        //        } else {
        //            $('.btn-send-form-second').removeClass('grey').removeAttr('disabled');
        //            $('.error').html('');
        //        }
        //    } else {
        //        $('.error').html('密码输入有误');
        //        $('.btn-send-form-second').addClass('grey').attr('disabled', 'disabled');
        //        return false;
        //    }
        //    return true;
        //}
        //$('.jq-ps-1').blur(function () {
        //    if ($(this).val() != '') {
        //        var success = verifyPassword($(this));
        //        if(success){
        //            if ($('.jq-ps-2').val() != '') {
        //
        //                if ($(this).val() != $('.jq-ps-2').val()) {
        //                    $('.error').html('密码输入不一致');
        //                    $('.btn-send-form-second').addClass('grey').attr('disabled', 'disabled');
        //                } else {
        //                    $('.btn-send-form-second').removeClass('grey').removeAttr('disabled');
        //                    $('.error').html('');
        //                }
        //            }
        //        }
        //    }else{
        //        $('.error').html('密码输入有误');
        //        $('.btn-send-form-second').addClass('grey').attr('disabled', 'disabled');
        //
        //    }
        //});
        //$('.jq-ps-2').blur(function () {
        //    if ($(this).val() != '') {
        //        var success = verifyPassword($(this));
        //        if(success){
        //            if ($('.jq-ps-1').val() != '') {
        //                if ($(this).val() != $('.jq-ps-1').val()) {
        //                    $('.error').html('密码输入不一致');
        //                    $('.btn-send-form-second').addClass('grey').attr('disabled', 'disabled');
        //                } else {
        //                    $('.error').html('');
        //                    $('.btn-send-form-second').removeClass('grey').removeAttr('disabled');
        //                }
        //            }
        //        }
        //
        //    }else{
        //        $('.error').html('密码输入不一致');
        //        $('.btn-send-form-second').addClass('grey').attr('disabled', 'disabled');
        //    }
        //});


});