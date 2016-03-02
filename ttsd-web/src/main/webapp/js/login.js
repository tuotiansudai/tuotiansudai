require(['jquery', 'csrf', 'jquery.validate', 'jquery.form'], function ($) {

        var loginFormElement = $('.form-login'),
            loginSubmitElement = $('.login-submit', loginFormElement),
            loginNameElement = $('.login-name', loginFormElement),
            passwordElement = $('.password', loginFormElement),
            captchaElement = $('.captcha', loginFormElement),
            errorElement = $('.error', loginFormElement),
            imageCaptchaElement = $('.image-captcha img', loginFormElement);

        var refreshCaptcha = function () {
            captchaElement.val('');
            imageCaptchaElement.attr('src', '/login/captcha?' + new Date().getTime().toString());
        };

        imageCaptchaElement.click(function () {
            refreshCaptcha();
        });

        //用户名校验
        loginNameElement.blur(function (event) {
            errorElement.text('').css('visibility', 'hidden');

            if ($.trim(loginNameElement.val()) === '') {
                errorElement.text("用户名不能为空").css('visibility', 'visible');
                return false;
            }
            return false;
        });

        //密码校验
        passwordElement.blur(function (event) {
            errorElement.text('').css('visibility', 'hidden');
            if ($.trim(passwordElement.val()) === '') {
                errorElement.text("密码不能为空").css('visibility', 'visible');
            }
            return false;
        });

        //验证码校验
        captchaElement.blur(function (event) {
            errorElement.text('').css('visibility', 'hidden');
            if ($.trim(captchaElement.val()) === '') {
                errorElement.text("验证码不能为空").css('visibility', 'visible');
            }
            return false;
        });


        var loginSubmitVerify = function () {
            errorElement.text('').css('visibility', 'hidden');

            if ($.trim(loginNameElement.val()) === '') {
                errorElement.text("用户名不能为空").css('visibility', 'visible');
                return false;
            }

            if ($.trim(passwordElement.val()) === '') {
                errorElement.text("密码不能为空").css('visibility', 'visible');
                return false;
            }

            if ($.trim(captchaElement.val()) === '') {
                errorElement.text("验证码不能为空").css('visibility', 'visible');
                return false;
            }

            return true;
        };

        var submitLoginForm = function () {
                loginFormElement.ajaxSubmit({
                    beforeSubmit: function (arr, $form, options) {
                        loginSubmitElement.addClass('loading');
                    },
                    success: function (response) {
                        if (response.data.status) {
                            window.location.href = loginFormElement.data('redirect-url');
                        } else {
                            refreshCaptcha();
                            loginSubmitElement.removeClass('loading');
                            if (response.data.isLocked) {
                                errorElement.text("用户已被锁定").css('visibility', 'visible');
                                return;
                            }
                            if (response.data.isCaptchaNotMatch) {
                                errorElement.text("验证码不正确").css('visibility', 'visible');
                                return;
                            }
                            errorElement.text("用户或密码不正确").css('visibility', 'visible');
                        }
                    },
                    error: function () {
                        loginSubmitElement.removeClass('loading');
                        refreshCaptcha();
                        errorElement.text("用户或密码不正确").css('visibility', 'visible');
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
                loginSubmitElement.focus();
                submitLoginForm();
            }
        })
});