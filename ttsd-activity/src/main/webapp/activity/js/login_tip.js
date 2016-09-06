define(['jquery', 'layerWrapper', 'jquery.ajax.extension', 'jquery.validate', 'jquery.form'], function($, layer) {

    var $loginFormElement = $('.form-login'),
        $loginSubmitElement = $('.login-submit'),
        $captchaElement = $('.captcha'),
        $errorElement = $('i.error'),
        $imageCaptchaElement = $('.image-captcha img');

    var refreshCaptcha = function() {
        $captchaElement.val('');
        $imageCaptchaElement.attr('src', '/login/captcha?' + new Date().getTime().toString());
    };

    $imageCaptchaElement.click(function() {
        refreshCaptcha();
    });


    var submitLoginForm = function() {
        $loginFormElement.ajaxSubmit({
            beforeSubmit: function(arr, $form, options) {
                $loginSubmitElement.addClass('loading');
            },
            success: function(data) {
                if (data.status) {
                    window.location.reload();
                } else {
                    refreshCaptcha();
                    $loginSubmitElement.removeClass('loading');
                    if (data.isLocked) {
                        layer.msg('用户已被锁定');
                    }else if (data.isCaptchaNotMatch) {
                        layer.msg('验证码不正确');
                    }else{
                        layer.msg('用户或密码不正确');
                    }
                }
            },
            error: function() {
                $loginSubmitElement.removeClass('loading');
                refreshCaptcha();
                layer.msg('用户或密码不正确');
            }
        });
        return false;
    };



    $loginFormElement.validate({
        debug: true,
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            },
            captcha: {
                required: true,
                minlength: 5
            }
        },
        messages: {
            username: {
                required: '用户名不能为空'
            },
            password: {
                required: '密码不能为空'
            },
            captcha: {
                required: '验证码不能为空',
                minlength: '验证码不正确'
            }
        },
        errorPlacement: function(error, element) {  
            error.appendTo(element.parent());  
        },
        submitHandler: function(form) {
            submitLoginForm();
        }
    });

    $('body').on('click', '.close-btn', function(event) {
        event.preventDefault();
        layer.closeAll();
    })
    .on('click', '.show-login', function(event) {
        event.preventDefault();
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['auto', 'auto'],
            content: $('#loginTip')
        });
    });
});