require(['jquery', 'csrf'], function ($) {
    $(function () {
        function verifyPassword(field) {
            if (field.val() != '') {

                var regex = /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$/;
                if (field.val().length < 6 || field.val().length > 20) {
                    $('.error').html('长度6至20位');
                    $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
                    return false;
                } else {
                    $('.btn-send-form').removeClass('grey').removeAttr('disabled');
                    $('.error').html('');
                }
                if (!regex.test(field.val())) {
                    $('.error').html('只能字母和数字组合');
                    $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
                    return false;
                } else {
                    $('.btn-send-form').removeClass('grey').removeAttr('disabled');
                    $('.error').html('');
                }
            } else {
                $('.error').html('密码输入有误');
                $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
                return false;
            }
            return true;
        }
        $('.jq-ps-1').blur(function () {
            if ($(this).val() != '') {
                var success = verifyPassword($(this));
                if(success){
                    if ($('.jq-ps-2').val() != '') {

                        if ($(this).val() != $('.jq-ps-2').val()) {
                            $('.error').html('密码输入不一致');
                            $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
                        } else {
                            $('.btn-send-form').removeClass('grey').removeAttr('disabled');
                            $('.error').html('');
                        }
                    }
                }
            }else{
                $('.error').html('密码输入有误');
                $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');

            }
        });
        $('.jq-ps-2').blur(function () {
            if ($(this).val() != '') {
                var success = verifyPassword($(this));
                if(success){
                    if ($('.jq-ps-1').val() != '') {
                        if ($(this).val() != $('.jq-ps-1').val()) {
                            $('.error').html('密码输入不一致');
                            $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
                        } else {
                            $('.error').html('');
                            $('.btn-send-form').removeClass('grey').removeAttr('disabled');
                        }
                    }
                }

            }else{
                $('.error').html('密码输入不一致');
                $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
            }
        });

    });
});