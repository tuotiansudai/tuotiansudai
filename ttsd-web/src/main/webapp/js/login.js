require(['jquery', 'csrf', 'jquery.form'], function ($) {
    $(function () {
        var loginFormElement = $('.form-login'),
         loginSubmitElement = $('.login-submit',loginFormElement),
         loginNameElement = $('.login-name',loginFormElement),
         passwordElement = $('.password',loginFormElement),
         captchaElement = $('.captcha',loginFormElement),
         errorElement = $('.error',loginFormElement),
         imageCaptchaElement = $('.image-captcha img',loginFormElement);

        var loginNameValid = false,
            passwordValid = false,
            captchaValid = false;

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
                return;
            }

            captchaValid = false;

            if (value === '' && $.inArray(event.keyCode, excludedKeys) === -1) {
                errorElement.text("验证码不能为空").css('visibility', 'visible');
                captchaValid = false;
                loginSubmitVerify();
            }

            if (value.length < 5 && $.inArray(event.keyCode, excludedKeys) === -1) {
                captchaValid = false;
            }
            loginSubmitVerify();
        });

        var loginSubmitVerify = function () {
            var isValid = loginNameValid && passwordValid && captchaValid;
            if (!isValid) {
                loginSubmitElement.prop('disabled',!isValid).removeClass('btn-normal');
            } else {
                loginSubmitElement.prop('disabled',!isValid).addClass('btn-normal');
            }
            return isValid;
        };

        var submitLoginForm = function () {
            loginFormElement.ajaxSubmit({
                

                beforeSubmit: function (arr, $form, options) {
                    loginSubmitElement.toggleClass('loading');
                },
                success: function (response) {
                    if (response.data.status) {
                        window.location.href = loginFormElement.data('redirect-url');
                    } else {
                        if (response.data.isLocked) {
                            errorElement.text("用户已被锁定").css('visibility', 'visible');
                        } else {
                            errorElement.text("用户或密码不正确").css('visibility', 'visible');
                        }
                    }
                },
                error: function () {
                    errorElement.text("用户或密码不正确").css('visibility', 'visible');
                },
                complete: function () {
                    refreshCaptcha();
                    loginSubmitElement.toggleClass('loading');
                    loginSubmitVerify();
                }
            });
            return false;
        };

        loginSubmitElement.click(function () {
            if (loginSubmitVerify()) {
                submitLoginForm();
            }
        });

        $(document).keypress(function (event) {
            var keycode = (event.keyCode ? event.keyCode : event.which)
            if (keycode === 13 && loginSubmitVerify()) {
                submitLoginForm();
            }
        })
    });
});