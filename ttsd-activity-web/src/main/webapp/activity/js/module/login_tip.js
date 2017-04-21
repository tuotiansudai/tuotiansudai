define(['jquery', 'layerWrapper', 'jquery.ajax.extension', 'jquery.validate', 'jquery.form'], function ($, layer) {

    var $loginFormElement = $('.form-login'),
        $loginSubmitElement = $('.login-submit'),
        $captchaElement = $('.captcha'),
        $errorElement = $('i.error'),
        $imageCaptchaElement = $('.image-captcha img');

    var refreshCaptcha = function () {
        $captchaElement.val('');
        $imageCaptchaElement.attr('src', '/login/captcha?' + new Date().getTime().toString());
    };

    $imageCaptchaElement.click(function () {
        refreshCaptcha();
    });

    var submitLoginForm = function () {
        $loginFormElement.ajaxSubmit({
            beforeSubmit: function (arr, $form, options) {
                $loginSubmitElement.addClass('loading');
            },
            success: function (data) {
                if (data.status) {
                    window.location.reload();

                } else {
                    refreshCaptcha();
                    $loginSubmitElement.removeClass('loading');
                    layer.msg(data.message);
                }
            },
            error: function () {
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
                required: true,
                minlength:5
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
                required: '用户名不能为空',
                minlength:'用户名输入太短'
            },
            password: {
                required: '密码不能为空'
            },
            captcha: {
                required: '验证码不能为空',
                minlength: '验证码不正确'
            }
        },
        errorPlacement: function (error, element) {
            error.appendTo(element.parent());
        },
        submitHandler: function (form) {
            submitLoginForm();
        }
    });

    $('body').on('click', '.close-btn', function (event) {
        event.preventDefault();
        layer.closeAll();
    })
        .on('click', '.show-login', function (event) {
            event.preventDefault();
            refreshCaptcha();
            $.ajax({
                url: '/activity/isLogin',
                type: 'GET',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            })
                .fail(function (response) {
                        if (response.responseText != "") {
                            $("meta[name='_csrf']").remove();
                            $('head').append($(response.responseText));
                            var token = $("meta[name='_csrf']").attr("content");
                            var header = $("meta[name='_csrf_header']").attr("content");
                            $(document).ajaxSend(function (e, xhr, options) {
                                xhr.setRequestHeader(header, token);
                            });
                            layer.open({
                                type: 1,
                                title: false,
                                closeBtn: 0,
                                area: ['auto', 'auto'],
                                content: $('#loginTip')
                            });
                        }

                    }
                );
        });
});