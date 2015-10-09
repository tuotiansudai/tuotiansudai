require(['jquery', 'csrf', 'jquery.form'], function ($) {
    $(function () {
        var loginFormElement = $('.form-login');
        var loginSubmitElement = $('.form-login .login-submit');
        var loginNameElement = $('.form-login .login-name');
        var passwordElement = $('.form-login .password');
        var captchaElement = $('.form-login .captcha');
        var errorElement = $('.form-login .error');
        var imageCaptchaElement = $('.form-login .image-captcha img');

        var loginNameValid = false;
        var passwordValid = false;
        var captchaValid = false;

        var refreshCaptcha = function () {
            captchaValid = false;
            captchaElement.val('');
            imageCaptchaElement.attr('src', '/login/captcha?' + new Date().getTime().toString());
            loginSubmitVerify();
        };

        imageCaptchaElement.click(function () {
            refreshCaptcha();
        });

        //用户名校验
        loginNameElement.keyup(function (event) {
            var errorMessage = "用户名不能为空";
            var value = loginNameElement.val().trim();
            var excludedKeys = [9, 13, 16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];

            errorElement.text('').css('visibility', 'hidden');

            if (value !== '' && $.inArray(event.keyCode, excludedKeys) === -1) {
                loginNameValid = true;
            }

            if (value === '' && $.inArray(event.keyCode, excludedKeys) === -1) {
                errorElement.text(errorMessage).css('visibility', 'visible');
                loginNameValid = false;
            }
            loginSubmitVerify();

            return true;
        });

        //密码校验
        passwordElement.keyup(function (event) {
            var errorMessage = "密码不能为空";
            var value = passwordElement.val().trim();
            var excludedKeys = [9, 13, 16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];

            errorElement.text('').css('visibility', 'hidden');

            if (value !== '' && $.inArray(event.keyCode, excludedKeys) === -1) {
                passwordValid = true;
            }

            if (value === '' && $.inArray(event.keyCode, excludedKeys) === -1) {
                errorElement.text(errorMessage).css('visibility', 'visible');
                passwordValid = false;
            }
            loginSubmitVerify();
        });

        //验证码校验
        captchaElement.keyup(function (event) {
            var value = captchaElement.val().trim();
            var excludedKeys = [9, 13, 16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];

            errorElement.text('').css('visibility', 'hidden');

            if (value.length === 5 && $.inArray(event.keyCode, excludedKeys) === -1) {
                $.ajax({
                    url: '/login/captcha/' + value + '/verify',
                    type: 'get',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).success(function (response) {
                    if (response.data.status) {
                        captchaValid = true;
                    } else {
                        errorElement.text("验证码不正确").css('visibility', 'visible');
                        captchaValid = false;
                    }
                }).error(function () {
                    captchaValid = false;
                }).complete(function () {
                    loginSubmitVerify();
                });
            }

            if (value === '' && $.inArray(event.keyCode, excludedKeys) === -1) {
                errorElement.text("验证码不能为空").css('visibility', 'visible');
                captchaValid = false;
                loginSubmitVerify();
            }
        });


        var loginSubmitVerify = function () {
            if (loginNameValid && passwordValid && captchaValid) {
                loginSubmitElement.removeClass('grey').attr('disabled', false);
            } else {
                loginSubmitElement.addClass('grey').attr('disabled', true);
            }
        };

        var submitLoginForm = function () {
            loginFormElement.ajaxSubmit({
                beforeSubmit: function (arr, $form, options) {
                    loginSubmitElement.toggleClass('loading');
                },
                success: function (response) {
                    if (response.data.status) {
                        window.location.href = '/';
                    } else {
                        errorElement.text("用户或密码不正确").css('visibility', 'visible');
                    }
                },
                error: function () {
                    errorElement.text("用户或密码不正确").css('visibility', 'visible');
                },
                complete: function () {
                    refreshCaptcha();
                    loginNameValid = passwordValid = captchaValid = false;
                    loginSubmitElement.toggleClass('loading');
                    loginSubmitVerify();
                }
            });
        };

        loginSubmitElement.click(function () {
            submitLoginForm();
        });

        $(document).keypress(function (event) {
            var keycode = (event.keyCode ? event.keyCode : event.which)
            if (keycode === 13) {
                submitLoginForm();
            }
        })
    });
});