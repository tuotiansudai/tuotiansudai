$(function () {
    var refreshCaptcha = function () {
        var captcha = $('.verification-console-img');
        captcha.attr('src', '/login/captcha?' + new Date().toTimeString());
    };
    $('.verification-console-img').click(function () {
        refreshCaptcha();
        $('.captcha').val('');
    });

    String.prototype.trim = function () {
        return this.replace(/(^\s*)|(\s*$)/g, "");
    };

    var login = function () {
        if ($('input[name="username"]').val().trim() === ''
            || $('input[name="password"]').val().trim() === ''
            || $('input[name="captcha"]').val().trim() === '') {
            return false;
        }

        $(this).addClass("disabled");

        $.ajax({
            url: '/login',
            type: 'post',
            data: $('.form-login').serialize()
        }).done(function (response) {
            if (response.status) {
                //redirectByRoles(response.data.roles);
                window.location.href = "/";
            } else {
                refreshCaptcha();
                $('.error').text(response.message).css('visibility', 'visible');
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