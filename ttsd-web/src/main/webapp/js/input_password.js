require(['jquery', 'csrf'], function ($) {
    $(function () {
        $('.jq-ps-1').blur(function () {
            if ($(this).val() != '') {
                if ($('.jq-ps-2').val() != '') {
                    if ($(this).val() != $('.jq-ps-2').val()) {
                        $('.jq-ps-2').addClass('error');
                        $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
                    } else {
                        $('.jq-ps-2').removeClass('error');
                        $('.btn-send-form').removeClass('grey').removeAttr('disabled');
                    }
                }
                $(this).removeClass('error');
            }else{
                $(this).addClass('error');
                $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
                console.log('333')
            }
        });
        $('.jq-ps-2').blur(function () {
            if ($(this).val() != '') {
                if ($('.jq-ps-1').val() != '') {
                    if ($(this).val() != $('.jq-ps-1').val()) {
                        $('.jq-ps-2').addClass('error');
                        $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
                    } else {
                        $('.jq-ps-2').removeClass('error');
                        $('.btn-send-form').removeClass('grey').removeAttr('disabled');
                    }
                }
                $(this).removeClass('error');
            }else{
                $(this).addClass('error');
                $('.btn-send-form').addClass('grey').attr('disabled', 'disabled');
            }
        })

    });
});