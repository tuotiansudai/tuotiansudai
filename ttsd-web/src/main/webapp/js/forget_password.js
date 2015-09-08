require(['jquery', 'csrf'], function ($) {

    $(function(){


    //手机号校验
        var m = /^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$/;
    $('.phone-txt').keyup(function () {
        if($(this).val().length==11 && m.test($(this).val())){
            var _phone = $(this).val();

        }else{
            $(this).addClass('error').removeClass('valid-ok');
        }
    });









    $('.fetch-captcha').on('click', function () {
        $('.layer-box').show();
        refreshCaptcha();
    });
    $('.close').on('click', function () {
        $('.verification-code,.verification-code-main').hide();
    });
    $('.mobile').blur(function () {
        var _this = $(this);
        if(_this.val()=='' || _this.hasClass('error')){
            $('.fetch-captcha').addClass('grey').attr('disabled','disabled');
        }else{
            $('.fetch-captcha').removeClass('grey').removeAttr('disabled');
        }
    });

    $('.complete').click(function () {
        var phone = $('.mobile').val();
        var captcha = $('.verification-code-text').val();
        var num = 30;
        // 倒计时
        function countdown() {
            $('.fetch-captcha').html(num + '秒后重新发送').css({'background': '#666', 'pointer-events': 'none'});
            if (num == 0) {
                clearInterval(count);
                $('.fetch-captcha').html('重新发送').css({'background': '#f68e3a', 'pointer-events': 'auto'});
            }
            num--;
        }
        var count = setInterval(countdown, 1000);
        $('.layer-box').hide();
        $.get('/register/mobile/' + phone + '/image-captcha/' + captcha + '/send-register-captcha');
    });

    // 刷新验证码
    var refreshCaptcha = function () {
        var captcha = $('.verification-code-img');
        captcha.attr('src', '/register/image-captcha?' + new Date().toTimeString());
    };

    $('.verification-code-img').click(function () {
        refreshCaptcha();
    });

    // 弹出框验证码
    $('.verification-code-text').blur(function () {
        var _this = $(this);
        var _value = _this.val();
        if (_value.length < 5) {
            $('.verification-code-main b').css('display', 'inline-block');
        } else {
            $.ajax({
                url: '/register/image-captcha/' + _value + '/verify',
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function (response) {
                if (response.data.status) {
                    $('.verification-code-main b').hide();
                    $('.complete').removeClass('grey').removeAttr('disabled');
                } else {
                    $('.verification-code-main b').css('display', 'inline-block');
                    $('.complete').addClass('grey').attr('disabled');
                    refreshCaptcha();
                }
            });
        }
    });
    })

});