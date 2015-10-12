require(['jquery', 'csrf'], function ($) {
    $(function () {
        $('.jq-ps-1').blur(function () {
            if ($(this).val() != '') {
                if ($('.jq-ps-1').val() != '') {
                    var regex = /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$/;
                    if($(this).val().length < 6 || $(this).val().length > 20){
                        $('.error').html('长度6至20位');
                        return;
                    }
                    if(!regex.test($(this).val())){
                        $('.error').html('只能字母和数字组合');
                        return;
                    }

                    if ($(this).val() != $('.jq-ps-2').val()) {
                        $('.error').html('密码输入不一致');
                        $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
                    } else {
                        $('.jq-ps-2').removeClass('error');
                        $('.btn-send-form').removeClass('grey').removeAttr('disabled');
                        $('.error').html('');
                    }
                }
                $('.error').html('');
            }else{
                $('.error').html('密码输入有误');
                $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
                console.log('333')
            }
        });
        $('.jq-ps-2').blur(function () {
            if ($(this).val() != '') {
                if ($('.jq-ps-1').val() != '') {
                    if ($(this).val() != $('.jq-ps-1').val()) {
                        $('.error').html('密码输入不一致');
                        $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
                    } else {
                        $('.error').html('');
                        $('.btn-send-form').removeClass('grey').removeAttr('disabled');
                    }
                }
            }else{
                $('.error').html('密码输入不一致');
                $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
            }
        })

        function a(sdfsd){

        }

    });
});