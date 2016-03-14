$(function() {
    var refreshCaptcha = function () {
        var captcha = $('.verification-console-img');
        captcha.attr('src', '/login/captcha?' + new Date().toTimeString());
    };
    $('.verification-console-img').click(function () {
        refreshCaptcha();
        $('.captcha').val('');
    });

    String.prototype.trim = function()
    {
        return this.replace(/(^\s*)|(\s*$)/g, "");
    };

    //var redirectByRoles = function(roles){
    //    var url = null;
    //    if(!!roles && roles instanceof Array) {
    //        for (idx in roles) {
    //            if (roles[idx] === 'ADMIN') {
    //                url = '/';
    //                break;
    //            }
    //            if (roles[idx] === 'CUSTOMER_SERVICE') {
    //                url = '/';
    //            }
    //        }
    //    }
    //    if(!!url){
    //        window.location.href = url;
    //    } else {
    //        window.location.reload();
    //    }
    //};

    var login = function () {
        if ($('input[name="username"]').val().trim() === ''
            || $('input[name="password"]').val().trim() === ''
            || $('input[name="captcha"]').val().trim() === '') {
            return false;
        }

        $.ajax({
            url: '/loginHandler',
            type: 'post',
            data: $('.form-login').serialize()
        }).done(function (response) {
            if (response.data.status) {
                //redirectByRoles(response.data.roles);
                window.location.href = "/";
            } else {
                refreshCaptcha();
                if (response.data.isCaptchaNotMatch) {
                    $('.error').text('验证码不正确').css('visibility', 'visible');
                } else {
                    $('.error').text('用户名或密码不正确').css('visibility', 'visible');
                }
            }
        });
    };

    $('.btn-block').click(login);

    $(document).keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode === 13) {
            login();
        }
    })
});