require(['jquery', 'layerWrapper', 'jquery.validate', 'jquery.validate.extension', 'csrf', 'jquery.form'], function ($,layer) {
        var $InfoBox = $('#personInfoBox'),
            $changeEmailLayer = $('.setEmail', $InfoBox),
            $turnOnNoPasswordInvestLayer = $('.setTurnOnNoPasswordInvest', $InfoBox),
            $turnOffNoPasswordInvestLayer = $('.setTurnOffNoPasswordInvest', $InfoBox),
            $changePasswordLayer = $('.setPass', $InfoBox),
            $resetUmpayPasswordLayer = $('.setUmpayPass', $InfoBox),
            $changeEmailDOM = $('#changeEmailDOM'),
            $turnOnNoPasswordInvestDOM = $('#turnOnNoPasswordInvestDOM'),
            $turnOffNoPasswordInvestDOM = $('#turnOffNoPasswordInvestDOM'),
            $noPasswordInvestDOM = $('#noPasswordInvestDOM'),
            $imageCaptchaElement = $('.image-captcha', $turnOffNoPasswordInvestDOM),
            $imageCaptchaTextElement = $('.image-captcha-text', $turnOffNoPasswordInvestDOM),
            $getCaptcha = $('.get-captcha'),
            $changePassDOM = $('#changePassDOM'),
            $resetUmpayPassDOM = $('#resetUmpayPassDOM'),
            $successUmpayPass = $('#successUmpayPass'),
            $noPasswordInvest = $('.setNoPasswordInvest'),
            $EmailForm = $('form', $changeEmailDOM),
            $passwordForm = $('form', $changePassDOM),
            $umpayPasswordForm = $('form', $resetUmpayPassDOM),
            $turnOnNoPasswordInvestForm = $('form', $turnOnNoPasswordInvestDOM),
            $turnOffNoPasswordInvestForm = $('form', $turnOffNoPasswordInvestDOM);


        $changeEmailLayer.on('click', function () {
            layer.open({
                type: 1,
                move: false,
                offset: "200px",
                title: '绑定邮箱',
                area: ['490px', '220px'],
                shadeClose: true,
                content: $changeEmailDOM,
                cancel: function () {
                    $EmailForm.validate().resetForm();
                }
            });
        });
        $turnOnNoPasswordInvestLayer.on('click', function () {
            layer.open({
                type: 1,
                move: false,
                offset: "200px",
                title: '免密投资',
                area: ['490px', '220px'],
                shadeClose: false,
                closeBtn:0,
                btn:['取消','去联动优势授权'],
                content: $turnOnNoPasswordInvestDOM,
                btn1:function(){
                },
                btn2: function () {
                    $turnOnNoPasswordInvestForm.submit();
                    layer.open({
                        type: 1,
                        move: false,
                        offset: "200px",
                        title: '免密投资',
                        area: ['490px', '220px'],
                        shadeClose: true,
                        content: $noPasswordInvestDOM
                    });
                }

            });
        });
        $turnOffNoPasswordInvestLayer.on('click', function () {
            layer.open({
                type: 1,
                move: false,
                area:'500px',
                title: '免密投资',
                closeBtn:0,
                btn:['取消','我要关闭'],
                shadeClose: false,
                content: $turnOffNoPasswordInvestDOM,
                btn1:function(){

                },
                btn2: function () {


                }
            });
        });
        $getCaptcha.on('click',function(){
            time($(this));


        });
        $noPasswordInvest.on('click', function () {
            var _this = $(this);
            $.ajax({
                url: _this.data('url'),
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function (response){
                if (response.data.status) {
                    location.href = "/personal-info";
                }
            });
        });
        var wait = 30;

        function time(o) {
            if (wait == 0) {
                o.prop("disabled", false).text("重新发送");
                wait = 30;
            } else {
                o.prop("disabled", true).text("" + wait + "s后重新发送");
                wait--;
                setTimeout(function() {
                    time(o)
                }, 1000)
            }
        }
        //$imageCaptchaTextElement.on('onfocusout',function(){
        //
        //});
        $turnOffNoPasswordInvestForm.validate({
            focusInvalid: false,
            onFocusOut: function (element) {
                if (!this.checkable(element) && !this.optional(element)) {
                    this.element(element);
                }
            },
            errorPlacement: function(error, element) {
                error.appendTo(element.parent());
            },

            rules: {
                imageCaptcha: {
                    required: true,
                    regex: /^[a-zA-Z0-9]{5}$/

                }
            },
            messages: {
                imageCaptcha: {
                    required: "请输入图形验证码",
                    regex: "图形验证码位数不对"
                }
            }
        });

        $EmailForm.validate({
            focusInvalid: false,
            rules: {
                email: {
                    required: true,
                    regex:/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
                    email: true,
                    isExist: "/personal-info/email/{0}/is-exist"
                }
            },
            onkeyup: function (element, event) {
                var excludedKeys = [16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];
                if ((event.which !== 9 || this.elementValue(element) !== "") && $.inArray(event.keyCode, excludedKeys) === -1) {
                    this.element(element);
                }
            },
            onFocusOut: function (element) {
                if (!this.checkable(element) && !this.optional(element)) {
                    this.element(element);
                }
            },
            messages: {
                email: {
                    required: "请输入有效邮箱",
                    regex:'请输入有效邮箱',
                    isExist: "邮箱已存在",
                    email: "请输入有效邮箱"
                }
            },
            success:'valid',
            submitHandler: function (form) {
                var self = this;
                $(form).ajaxSubmit({
                    dataType: 'json',
                    beforeSubmit: function (arr, $form, options) {
                        $('.change-email-success .email').html($('input[name="email"]').val());
                        $form.resetForm();
                        layer.closeAll();
                    },
                    success: function (response) {
                        var data = response.data;
                        if (data.status) {
                            layer.open({
                                type: 1,
                                title: '验证邮箱',
                                area: ['500px', '220px'],
                                shadeClose: true,
                                content: $('#change-email-success'),
                                btn: ['返回'],
                                yes: function (index, layero) {
                                    layer.close(index);
                                }
                            });
                        } else {
                            layer.msg('邮箱绑定失败，请重试！', {type: 1, time: 2000});
                        }
                    },
                    error: function () {
                        layer.msg('邮箱绑定失败，请重试！', {type: 1, time: 2000});
                    },
                    complete: function () {
                    }
                });
                return false;
            }
        });

        $changePasswordLayer.on('click', function () {
            layer.open({
                type: 1,
                move: false,
                offset: "200px",
                title: '修改密码',
                area: ['500px', '300px'],
                shadeClose: false,
                content: $changePassDOM,
                cancel: function () {
                    $passwordForm.validate().resetForm();
                }
            });
        });

        $passwordForm.validate({
            focusCleanup: true,
            focusInvalid: false,
            onkeyup: function (element, event) {
                var excludedKeys = [16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];
                if ((event.which !== 9 || this.elementValue(element) !== "") && $.inArray(event.keyCode, excludedKeys) === -1) {
                    this.element(element);
                }
            },
            onFocusOut: function (element) {
                if (!this.checkable(element) && !this.optional(element)) {
                    this.element(element);
                }
            },
            submitHandler: function (form) {
                var self = this;
                $(form).ajaxSubmit({
                    dataType: 'json',
                    beforeSubmit: function (arr, $form, options) {
                        self.resetForm();
                        layer.closeAll();
                    },
                    success: function (response) {
                        var data = response.data;
                        if (data.status) {
                            layer.msg('密码修改成功，请重新登录！', {type: 1, time: 2000}, function(){
                                $('.header .logout-form').submit();
                            });
                        } else {
                            layer.msg('密码修改失败，请重试！', {type: 1, time: 2000});
                        }
                    },
                    error: function () {
                        layer.msg('密码修改失败，请重试！', {type: 1, time: 2000});
                    },
                    complete: function () {
                    }
                });
                return false;
            },
            rules: {
                originalPassword: {
                    required: true,
                    rangelength: [6, 20],
                    regex: /^(?=.*[^\d])(.{6,20})$/,
                    isNotExist: "/personal-info/password/{0}/is-exist"
                },
                newPassword: {
                    required: true,
                    rangelength: [6, 20],
                    regex: /^(?=.*[^\d])(.{6,20})$/
                },
                newPasswordConfirm: {
                    required: true,
                    rangelength: [6, 20],
                    regex: /^(?=.*[^\d])(.{6,20})$/,
                    equalTo: "input[name='newPassword']"
                }
            },
            messages: {
                originalPassword: {
                    required: "请输入原密码",
                    rangelength: "长度6~20位",
                    regex: "不能全为数字",
                    isNotExist: "原密码不正确"
                },
                newPassword: {
                    required: "请输入新密码",
                    rangelength: "长度6~20位",
                    regex: "不能全为数字"
                },
                newPasswordConfirm: {
                    required: "请输入新密码",
                    rangelength: "长度6~20位",
                    regex: "不能全为数字",
                    equalTo: "密码不一致"
                }
            }
        });

        $resetUmpayPasswordLayer.on('click', function() {
            layer.open({
                type: 1,
                move: false,
                offset: "200px",
                title: '修改支付密码',
                area: ['500px', '300px'],
                shadeClose: false,
                content: $resetUmpayPassDOM,
                cancel: function () {
                    $umpayPasswordForm.validate().resetForm();
                    $('.identityCodeTitle').show();
                    $('.identityCodeError').hide();
                }
            });
        });

        $umpayPasswordForm.validate({
            submitHandler: function (form) {
                var self = this;
                $(form).ajaxSubmit({
                    dataType: 'json',
                    beforeSubmit: function (arr, $form, options) {
                        self.resetForm();
                    },
                    success: function (response) {
                        var data = response.data;
                        if (data.status) {
                            layer.closeAll();
                            layer.open({
                                type: 1,
                                move: false,
                                offset: "200px",
                                title: '修改支付密码',
                                area: ['500px', '300px'],
                                shadeClose: false,
                                content: $successUmpayPass
                            });
                        } else {
                            $('.identityCodeTitle').hide();
                            $('.identityCodeError').show();
                        }
                    },
                    error: function () {
                        $('.identityCodeTitle').hide();
                        $('.identityCodeError').show();
                    },
                    complete: function () {
                    }
                });
                return false;
            },
            rules: {
                identityNumber: {
                    required: true
                }
            },
            messages: {
                identityNumber: {
                    required: "请输入身份证"
                }
            }
        });

        var refreshCaptcha = function () {
            $imageCaptchaElement.attr('src', '/register/user/image-captcha?' + new Date().getTime().toString());
        };

        $imageCaptchaElement.click(function () {
            refreshCaptcha();
        });
        $('#readUmpayPass').on('click', function () {
            layer.closeAll();
        });


    });
