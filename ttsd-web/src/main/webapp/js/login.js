require(['jquery', 'csrf', 'jquery.validate', 'jquery.form','commonFun'], function ($) {

        var loginFormElement = $('.form-login'),
            loginSubmitElement = $('.login-submit', loginFormElement),
            loginNameElement = $('.login-name', loginFormElement),
            passwordElement = $('.password', loginFormElement),
            captchaElement = $('.captcha', loginFormElement),
            errorElement = $('.error', loginFormElement),
            imageCaptchaElement = $('.image-captcha img', loginFormElement);
        String.prototype.trim = function()
        {
            return this.replace(/(^\s*)|(\s*$)/g, "");
        }
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

            if (loginNameElement.val().trim() === '') {
                errorElement.text("用户名不能为空").css('visibility', 'visible');
                return false;
            }
            return false;
        });

        //密码校验
        passwordElement.blur(function (event) {
            errorElement.text('').css('visibility', 'hidden');
            if (passwordElement.val().trim() === '') {
                errorElement.text("密码不能为空").css('visibility', 'visible');
            }
            return false;
        });

        //验证码校验
        captchaElement.blur(function (event) {
            errorElement.text('').css('visibility', 'hidden');
            if (captchaElement.val().trim() === '') {
                errorElement.text("验证码不能为空").css('visibility', 'visible');
            }
            return false;
        });


        var loginSubmitVerify = function () {
            errorElement.text('').css('visibility', 'hidden');

            if (loginNameElement.val().trim() === '') {
                errorElement.text("用户名不能为空").css('visibility', 'visible');
                return false;
            }

            if (passwordElement.val().trim() === '') {
                errorElement.text("密码不能为空").css('visibility', 'visible');
                return false;
            }

            if (captchaElement.val().trim() === '') {
                errorElement.text("验证码不能为空").css('visibility', 'visible');
                return false;
            }

            return true;
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
                        refreshCaptcha();
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
                    refreshCaptcha();
                    errorElement.text("用户或密码不正确").css('visibility', 'visible');
                },
                complete: function () {
                    loginSubmitElement.toggleClass('loading');
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