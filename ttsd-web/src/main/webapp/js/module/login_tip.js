define(['jquery', 'layerWrapper', 'jquery.ajax.extension', 'jquery.validate', 'jquery.form'], function($, layer) {

    (function() {
        var $loginTipBox=$('#loginTip');
        var $loginFormElement = $('.form-login',$loginTipBox),
            $loginSubmitElement = $('.login-submit',$loginTipBox),
            $captchaElement = $('.captcha',$loginTipBox),
            $errorElement = $('i.error',$loginTipBox),
            $imageCaptchaElement = $('.image-captcha img',$loginTipBox);

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
                        layer.msg(data.message);
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
            errorPlacement: function (error, element) {
                error.appendTo(element.parent());
            },
            submitHandler: function(form) {
                submitLoginForm();
            }
        });

        $('.close-btn',$loginTipBox).on('click',function(event) {
            event.preventDefault();
            layer.closeAll();
        })
        //判断是否登陆，如果没有登陆弹出登录框
        $('body').on('click', '.show-login', function(event) {
                event.preventDefault();
                refreshCaptcha();
                $.ajax({
                    url: '/isLogin',
                    //data:data,
                    type: 'GET',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                })
                    .fail(function (response) {
                            if (response.responseText != "") {
                                var $head=$('head');
                                $head.find("meta[name='_csrf']").remove()
                                    .append($(response.responseText));
                                var token = $head.find("meta[name='_csrf']").attr("content");
                                var header = $head.find("meta[name='_csrf_header']").attr("content");

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

    })();

});