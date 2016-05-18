require(['jquery', 'layerWrapper','jquery.validate', 'jquery.validate.extension', 'jquery.form','jquery.ajax.extension','csrf'], function ($,layer) {
        var $InfoBox = $('#personInfoBox'),
            $changeEmailLayer = $('.setEmail', $InfoBox),
            $turnOnNoPasswordInvestLayer = $('.setTurnOnNoPasswordInvest', $InfoBox),
            $turnOffNoPasswordInvestLayer = $('.setTurnOffNoPasswordInvest', $InfoBox),
            $changePasswordLayer = $('.setPass', $InfoBox),
            $resetUmpayPasswordLayer = $('.setUmpayPass', $InfoBox),
            $changeEmailDOM = $('#changeEmailDOM'),
            $noPasswordInvest = $('.setNoPasswordInvest'),
            $turnOnNoPasswordInvestDOM = $('#turnOnNoPasswordInvestDOM'),
            $turnOffNoPasswordInvestDOM = $('#turnOffNoPasswordInvestDOM'),
            $noPasswordInvestDOM = $('#noPasswordInvestDOM'),
            $imageCaptchaElement = $('.image-captcha', $turnOffNoPasswordInvestDOM),
            $imageCaptchaTextElement = $('.image-captcha-text', $turnOffNoPasswordInvestDOM),
            $getCaptchaElement = $('.get-captcha'),
            $btnCancelElement = $('.btn-cancel',$turnOffNoPasswordInvestDOM),
            $btnCloseTurnOnElement = $('.btn-close-turn-on',$turnOnNoPasswordInvestDOM),
            $btnCloseTurnOffElement = $('.btn-close-turn-off', $turnOffNoPasswordInvestDOM),
            $btnTurnOnElement = $('.btn-turn-on',$turnOnNoPasswordInvestDOM),
            $codeNumber = $('.code-number'),


            $changePassDOM = $('#changePassDOM'),
            $resetUmpayPassDOM = $('#resetUmpayPassDOM'),
            $successUmpayPass = $('#successUmpayPass'),
            $EmailForm = $('form', $changeEmailDOM),
            $passwordForm = $('form', $changePassDOM),
            $umpayPasswordForm = $('form', $resetUmpayPassDOM),
            $turnOffNoPasswordInvestForm = $('#turnOffNoPasswordInvestForm', $turnOffNoPasswordInvestDOM),
            $imageCaptchaForm = $('#imageCaptchaForm', $turnOffNoPasswordInvestDOM),
            $updateBankCard = $('#update-bank-card'),
            countTimer;

        $updateBankCard.on('click', function(){
            var url = $(this).attr('data-url');
            $.ajax({
                url: '/bind-card/is-replacing',
                type: 'GET',
                dataType: 'json'
            })
            .done(function(data) {
                if (data) {
                    layer.open({
                        type: 1,
                        title:false,
                        area: ['400px', '120px'],
                        btn:['确定'],
                        shadeClose: true,
                        content: '<p class="tc" style="margin-top:20px;">您已经提交了更换银行卡申请，请耐心等待结果。</p>',
                        btn1:function(){
                            layer.closeAll();
                        }
                    });
                } else {
                    location.href = url;
                }
            })
            .fail(function() {
                layer.msg('请求失败，请重试！');
            });

        });

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
            $.ajax({
                url: "/checkLogin",
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function(response){
                layer.open({
                    type: 1,
                    move: false,
                    offset: "200px",
                    title: '免密投资',
                    area: ['490px', '220px'],
                    shadeClose: false,
                    closeBtn:0,
                    content: $turnOnNoPasswordInvestDOM

                });
            });


        });

        $btnCloseTurnOffElement.on('click',function(){
            cnzzPush.trackClick("个人资料页","关闭免密弹框","我要关闭");
        });

        $turnOffNoPasswordInvestLayer.on('click', function () {
            $.ajax({
                url: "/checkLogin",
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function (response){
                if (response) {
                    refreshTurnOffNoPasswordInvestLayer();

                    layer.open({
                        type: 1,
                        move: false,
                        area:'500px',
                        title: '免密投资',
                        closeBtn:0,
                        shadeClose: false,
                        content: $turnOffNoPasswordInvestDOM
                    });
                }
            });

        });
        var refreshTurnOffNoPasswordInvestLayer = function(){
            clearInterval(countTimer);
            $getCaptchaElement.html('获取验证码').prop('disabled',true);
            refreshCaptcha();
            $('.captcha').val('');
            $('.error-content').html('');
            $codeNumber.addClass('code-number-hidden');

        };
        $getCaptchaElement.on('click',function(){
            $imageCaptchaForm.submit();
        });
        $noPasswordInvest.on('click', function () {
            cnzzPush.trackClick("个人资料页","开启免密投资","直接开启");
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

        $btnCancelElement.on('click',function(){
            layer.closeAll();
        });
        $btnCloseTurnOnElement.on('click',function(){
            cnzzPush.trackClick("个人资料页","开启免密弹框","取消");
            layer.closeAll();
        });
        $btnTurnOnElement.on('click',function(){
            cnzzPush.trackClick("个人资料页","开启免密弹框","去联动优势授权");
            layer.closeAll();
            layer.open({
                type: 1,
                closeBtn:0,
                move: false,
                offset: "200px",
                title: '免密投资',
                area: ['490px', '220px'],
                shadeClose: true,
                content: $noPasswordInvestDOM
            });
        });

        $imageCaptchaForm.validate({

            focusInvalid: false,
            onFocusOut: function (element) {
                if (!this.checkable(element) && !this.optional(element)) {
                    this.element(element);
                }

            },
            success:function(label){
                label.remove();
                $('#turnOffNoPasswordInvestDOM').find('.get-captcha').prop('disabled',false);
            },
            errorPlacement: function(error, element) {
                var errorContent = $('.error-content');
                errorContent.html('');
                error.appendTo(errorContent);
                $('#turnOffNoPasswordInvestDOM').find('.get-captcha').prop('disabled',true);
            },
            submitHandler: function (form) {
                var self = this;
                $(form).ajaxSubmit({
                    data: {mobile: $('.mobile').val()},
                    dataType: 'json',
                    success: function (response) {
                        var data = response.data;
                        if (data.status && !data.isRestricted) {
                            $codeNumber.removeClass('code-number-hidden');
                            var seconds = 60;
                            countTimer = setInterval(function () {
                                $getCaptchaElement.html(seconds + '秒后重新发送').addClass('btn disable-button').removeClass('btn-normal').prop('disabled',true);
                                if (seconds == 0) {
                                    clearInterval(countTimer);
                                    $getCaptchaElement.html('重新发送').removeClass('btn disable-button').addClass('btn-normal').prop('disabled',false);
                                    refreshCaptcha();
                                }
                                seconds--;
                            }, 1000);
                            return;
                        }

                        if (!data.status && data.isRestricted) {
                            $codeNumber.addClass('code-number-hidden');
                            self.showErrors({imageCaptcha: '短信发送频繁，请稍后再试'});
                        }

                        if (!data.status && !data.isRestricted) {
                            $codeNumber.addClass('code-number-hidden');
                            self.showErrors({imageCaptcha: '图形验证码不正确'});
                        }
                        self.invalid['imageCaptcha'] = true;
                        refreshCaptcha();
                    },
                    error: function () {
                        self.invalid['imageCaptcha'] = true;
                        self.showErrors({imageCaptcha: '图形验证码不正确'});
                        refreshCaptcha();
                    }
                });
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

        $turnOffNoPasswordInvestForm.validate({
            focusInvalid: false,
            ignore:".image-captcha-text",
            rules: {
                captcha: {
                    required: true,
                    digits: true,
                    maxlength: 6,
                    minlength: 6,
                    captchaVerify: {
                        param: function () {
                            var mobile = $('input[name="mobile"]').val();
                            return "/no-password-invest/mobile/" + mobile + "/captcha/{0}/verify"
                        }
                    }
                }

            },
            messages: {
                captcha: {
                    required: '请输入验证码',
                    digits: '验证码格式不正确',
                    maxlength: '验证码格式不正确',
                    minlength: '验证码格式不正确',
                    captchaVerify: '验证码不正确'
                }
            },

            errorPlacement: function(error, element) {
                var errorContent = $('.error-content');
                errorContent.html('');
                error.appendTo(errorContent);
                $('#turnOffNoPasswordInvestDOM').find('.get-captcha').prop('disabled',true);
            },
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    success: function (response) {
                        var data = response.data;
                        if (data.status) {
                            location.href = "/personal-info";
                        }
                    }
                });
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
            onFocusOut: function (element,event) {
                var excludedKeys = [16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];
                if ((event.which !== 9 || this.elementValue(element) !== "") && $.inArray(event.keyCode, excludedKeys) === -1) {
                    this.element(element);
                }
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
            $imageCaptchaElement.attr('src', '/no-password-invest/image-captcha?' + new Date().getTime().toString());
            $imageCaptchaTextElement.val('');
        };

        $imageCaptchaElement.click(function () {
            refreshCaptcha();
        });
        $('#readUmpayPass').on('click', function () {
            layer.closeAll();
        });


    });
