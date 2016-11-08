require(['jquery', 'underscore', 'jquery.ajax.extension', 'jquery.validate', 'jquery.form'], function ($, _) {

    (function() {
        var $loginContainer=$('#loginContainer');
        var loginFormElement = $('.form-login',$loginContainer),
            loginSubmitElement = $('.login-submit', loginFormElement),
            captchaElement = $('.captcha', loginFormElement),
            errorElement = $('.error', loginFormElement),
            imageCaptchaElement = $('.image-captcha img', loginFormElement),
            formCheckValid=false; //检查form表单验证是否全部通过

        // 刷新验证码
        var refreshCaptcha = function () {
            captchaElement.val('');
            imageCaptchaElement.attr('src', '/login/captcha?' + new Date().getTime().toString());
        };

        refreshCaptcha();

        imageCaptchaElement.click(function () {
            refreshCaptcha();
        });

     //表单验证
    $.fn.checkLogin=function() {
        return $(this).each(function(key,option) {
            var name=option.name,
                value=$.trim(option.value),
                errorMsg;
            formCheckValid=true;
            switch(name) {
                case 'username':
                    if(_.isEmpty(value)) {
                        errorMsg='用户名不能为空';
                        errorElement.text(errorMsg).css('visibility', 'visible');
                        formCheckValid=false;
                    }
                    break;
                case 'password':
                    if(_.isEmpty(value)) {
                        errorMsg='密码不能为空';
                        errorElement.text(errorMsg).css('visibility', 'visible');
                        formCheckValid=false;
                    }
                    break;
                case 'captcha':
                    if(_.isEmpty(value)) {
                        errorMsg='验证码不能为空';
                        errorElement.text(errorMsg).css('visibility', 'visible');
                        formCheckValid=false;
                    }
                    break;
            }

        });
    };
        //input失去焦点时验证
        loginFormElement.find('input:text,input:password').on('keyup',function() {
            var $this=$(this);
            errorElement.text('').css('visibility', 'hidden');
            $this.checkLogin();
        });

        var submitLoginForm = function () {
            loginFormElement.ajaxSubmit({
                beforeSubmit: function (arr, $form, options) {
                    loginSubmitElement.addClass('loading');
                },
                success: function (data) {
                    if (data.status) {
                        window.location.href = _.difference(data.roles, ['USER']).length > 0 ? loginFormElement.data('redirect-url') : "/register/account";
                    } else {
                        refreshCaptcha();
                        loginSubmitElement.removeClass('loading');
                        errorElement.text(data.message).css('visibility', 'visible');
                        formCheckValid=false;
                    }
                },
                error: function () {
                    loginSubmitElement.removeClass('loading');
                    refreshCaptcha();
                    errorElement.text("用户或密码不正确").css('visibility', 'visible');
                    formCheckValid=false;
                }
            });

            return false;
        };

        loginSubmitElement.click(function () {
            loginFormElement.find('input:text,input:password').checkLogin();
            if(formCheckValid) {
                submitLoginForm();
            }
        });

        $(document).keypress(function (event) {
            var keycode = (event.keyCode ? event.keyCode : event.which)
            if (keycode === 13 && formCheckValid) {
                loginSubmitElement.focus();
                submitLoginForm();
            }
            else {
                loginFormElement.find('input:text,input:password').checkLogin();
            }
        })
    })();

});