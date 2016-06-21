require(['underscore', 'jquery', 'layerWrapper','placeholder', 'jquery.validate', 'jquery.validate.extension', 'jquery.form', 'jquery.ajax.extension','commonFun'], function (_, $, layer) {
    var registerUserForm = $(".register-user-form"),
        fetchCaptchaElement = $('.fetch-captcha', registerUserForm),
        showAgreement = $('.show-agreement', registerUserForm),
        $agreement = $('#agreementInput');
    var $imgCaptchaDialog = $('.image-captcha-dialog'),
        imageCaptchaForm = $('.image-captcha-form', $imgCaptchaDialog),
        imageCaptchaElement = $('.image-captcha', $imgCaptchaDialog),
        imageCaptchaTextElement = $('.image-captcha-text', $imgCaptchaDialog),
        imageCaptchaSubmitElement = $('.image-captcha-confirm', $imgCaptchaDialog),

        $referrerOpen=$('.referrer-open',registerUserForm),
        $loginName=$('input.login-name',registerUserForm),
        $referrer=$('input.referrer', registerUserForm),
        $captchaInput=$('input.captcha',registerUserForm),
        $mobileInput=$('input.mobile',registerUserForm),
        $passwordInput=$('input.password',registerUserForm),
        $checkbox=$('label.check-label',registerUserForm),
        $registerSubmit=$('input[type="submit"]',registerUserForm),
        referrerError=$('#referrerError'),
        countTimer;

    var loginNameValid=false,
        mobileValid=false,
        passwordValid=false,
        captchaValid=false,
        referrerValidBool=true,
        agreementValid=true;

    $('input[type="text"],input[type="password"]',registerUserForm).placeholder();

    $checkbox.on('click', function (event) {
        if (event.target.tagName.toUpperCase() == 'A') {
            return;
        }
        var $this=$(this),
            $agreeLast=$this.parents('.agree-last'),
            $cIcon=$agreeLast.find('i');
        if($this.hasClass('checked')) {
            $this.removeClass('checked');
            $agreement.prop('checked',false);
            $cIcon[0].className='sprite-register-no-checked';
            agreementValid=false;
        }
        else {
            $this.addClass('checked');
            $agreement.prop('checked',true);
            $cIcon[0].className='sprite-register-yes-checked';
            agreementValid=true;
        }
        checkInputValid();

    });
    $referrerOpen.on('click',function() {
        var $this=$(this),
            checkOption=false,
            iconArrow=$this.find('i');
        $this.next('li').toggleClass('hide');
        checkOption=$this.next('li').hasClass('hide');
        iconArrow[0].className=checkOption?'sprite-register-arrow-bottom':'sprite-register-arrow-right';
        if($referrer.is(':hidden')) {
            $referrer.val('');
            $referrer.removeClass('error').addClass('valid');
            referrerError.html('').hide();
            referrerValidBool=true;
            checkInputValid();
        }
    });

    showAgreement.click(function () {
        layer.open({
            type: 1,
            title: '拓天速贷服务协议',
            area: ['950px', '600px'],
            shadeClose: true,
            move: false,
            scrollbar: true,
            skin:'register-skin',
            content: $('#agreementBox'),
            success: function (layero, index) {
            }
        });
    });
    fetchCaptchaElement.on('click', function () {
        layer.open({
            type: 1,
            title: '手机验证',
            area: ['300px', '190px'],
            shadeClose: true,
            content: $('.image-captcha-dialog'),
            success: function (layero, index) {
                $('.image-captcha-form label').remove();
                imageCaptchaTextElement.val('');
                refreshCaptcha();
            }
        });
        return false;
    });

    var refreshCaptcha = function () {
        imageCaptchaElement.attr('src', '/register/user/image-captcha?' + new Date().getTime().toString());
    };

    imageCaptchaElement.click(function () {
        refreshCaptcha();
    });

    imageCaptchaForm.validate({
        focusInvalid: false,
        onfocusout: function (element) {
            if (!this.checkable(element) && !this.optional(element)) {
                this.element(element);
            }
        },
        submitHandler: function (form) {
            var self = this;
            $(form).ajaxSubmit({
                data: {mobile: $('.mobile').val()},
                dataType: 'json',
                beforeSubmit: function (arr, $form, options) {
                    imageCaptchaSubmitElement.addClass("loading");
                },
                success: function (response) {
                    var data = response.data;
                    if (data.status && !data.isRestricted) {
                        layer.closeAll();
                        var seconds = 60
                        countTimer = setInterval(function () {
                            fetchCaptchaElement.html(seconds + '秒后重新发送').addClass('disabledButton').prop('disabled',true);
                            if (seconds == 0) {
                                clearInterval(countTimer);
                                fetchCaptchaElement.html('重新发送').removeClass('disabledButton').prop('disabled',false);
                            }
                            seconds--;
                        }, 1000);
                        return;
                    }

                    if (!data.status && data.isRestricted) {
                        self.showErrors({imageCaptcha: '短信发送频繁，请稍后再试'});
                    }

                    if (!data.status && !data.isRestricted) {
                        self.showErrors({imageCaptcha: '图形验证码不正确'});
                    }
                    self.invalid['imageCaptcha'] = true;
                    refreshCaptcha();
                },
                error: function () {
                    self.invalid['imageCaptcha'] = true;
                    self.showErrors({imageCaptcha: '图形验证码不正确'});
                    refreshCaptcha();
                },
                complete: function () {
                    imageCaptchaSubmitElement.removeClass("loading");

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

    registerUserForm.validate({
        ignore:'.referrer,.agreement',
        rules: {
            loginName: {
                required: true,
                regex: /(?!^\d+$)^\w{5,25}$/,
                checkLoginName:true
            },
            mobile: {
                required: true,
                digits: true,
                minlength: 11,
                maxlength: 11,
                isExist: "/register/user/mobile/{0}/is-exist"
            },
            password: {
                required: true,
                regex: /^(?=.*[^\d])(.{6,20})$/
            },
            captcha: {
                required: true,
                digits: true,
                maxlength: 6,
                minlength: 6,
                checkCaptcha:true
            }
        },
        messages: {
            loginName: {
                required: "请输入用户名",
                regex: '5位至25位数字与字母下划线组合，不能全部数字'
            },
            mobile: {
                required: '请输入手机号',
                digits: '必须是数字',
                minlength: '手机格式不正确',
                maxlength: '手机格式不正确',
                isExist: '手机号已存在'
            },
            password: {
                required: "请输入密码",
                regex: '6位至20位，不能全是数字'
            },
            captcha: {
                required: '请输入验证码',
                digits: '验证码格式不正确',
                maxlength: '验证码格式不正确',
                minlength: '验证码格式不正确',
                checkCaptcha:'验证码不正确'
            }
        },
        success: function (error, element) {
            checkInputValid();
            if(!fetchCaptchaElement.hasClass('disabledButton')) {
                if (element.name === 'mobile' && $loginName.hasClass('valid')) {
                    fetchCaptchaElement.prop('disabled', false);
                    $mobileInput.attr('preValue',$mobileInput.val());
                }
                if (element.name === 'loginName' && $mobileInput.hasClass('valid')) {
                    fetchCaptchaElement.prop('disabled', false);
                }
            }
        }
    });

    function checkInputValid(event) {
        loginNameValid=$loginName.hasClass('valid');
        mobileValid=$mobileInput.hasClass('valid');
        passwordValid=$passwordInput.hasClass('valid');

        if(loginNameValid && mobileValid && passwordValid && captchaValid && referrerValidBool && agreementValid) {
            $registerSubmit.prop('disabled',false);
        }
        else {
            $registerSubmit.prop('disabled',true);
        }
    }
    $captchaInput.on('keyup',function(event) {
        if(!/^\d{6}$/.test(event.target.value)) {
            captchaValid=false;
            $(event.target).addClass('error').removeClass('valid');
            $registerSubmit.prop('disabled',true);
        }
    });
    $loginName.on('keyup',function(event) {
        if(!/(?!^\d+$)^\w{5,25}$/.test(event.target.value)) {
            loginNameValid=false;
            $(event.target).addClass('error').removeClass('valid');
            $(event.target).next().html('请输入用户名');
            $registerSubmit.prop('disabled',true);
        }
    });
    $passwordInput.on('keyup',function(event) {
        if(!/^(?=.*[^\d])(.{6,20})$/.test(event.target.value)) {
            passwordValid=false;
            $(event.target).addClass('error').removeClass('valid');
            $(event.target).next().html('请输入密码');
            $registerSubmit.prop('disabled',true);
        }
    });
    $mobileInput.on('keyup',function(event) {
        if(countTimer) {
            clearInterval(countTimer);
            $('input.captcha', registerUserForm).removeClass('valid').val('')
                .next('label')
                .html('请输入验证码');
            fetchCaptchaElement.html('重新发送').removeClass('disabledButton').prop('disabled',false);
        }
        else {
            $('input.captcha', registerUserForm).removeClass('valid').val('');
        }
        if(_.isEmpty(event.target.value)) {
            $(event.target).addClass('error').removeClass('valid');
            $(event.target).next().html('请输入手机号');
            fetchCaptchaElement.prop('disabled', true);
        }
        else if(event.target.value.length<11) {
            fetchCaptchaElement.prop('disabled', true);
        }
        $registerSubmit.prop('disabled',true);
        captchaValid=false;
    });
    jQuery.validator.addMethod("checkLoginName", function(value, element) {
        var deferred = $.Deferred();
        if(/(?!^\d+$)^\w{5,25}$/.test(value)) {
            $.ajax({
                url:'/register/user/login-name/'+value+'/is-exist',
                async:false,
                dataType:"json",
                success:function(response) {
                    var status = response.data.status;
                    if (status) {
                        deferred.reject();
                        loginNameValid=false;
                    } else {
                        deferred.resolve();
                        loginNameValid=true;
                    }
                }
            });
        }
        else {
            deferred.reject();
            loginNameValid=false;
        }
        checkInputValid();

        return deferred.state() == "resolved" ? true : false;
    },'用户名已存在');

    jQuery.validator.addMethod("checkCaptcha", function(value, element) {
        var mobile=$('input.mobile',registerUserForm).val();
        var deferred = $.Deferred();
        if(/^\d{6}$/.test(value) && mobile) {
            $.ajax({
                url:'/register/user/mobile/' + mobile + '/captcha/'+value+'/verify',
                async:false, //要指定不能异步,必须等待后台服务校验完成再执行后续代码
                dataType:"json",
                success:function(response) {
                    var status = response.data.status;
                    if (status) {
                        deferred.resolve();
                        captchaValid=true;
                    } else {
                        deferred.reject();
                        captchaValid=false;
                    }
                }
            });
        }
        else {
            deferred.reject();
            captchaValid=false;
        }
        checkInputValid();

        return deferred.state() == "resolved" ? true : false;
    }, "验证码不正确");

    $referrer.on('keyup',function(event) {
        var $target=$(event.target),
        value=event.target.value;

        var deferred = $.Deferred();
        if(value) {
            $.ajax({
                url:'/register/user/referrer/'+value+'/is-exist',
                type: 'GET',
                dataType: 'json',
                async:false,
                contentType: 'application/json; charset=UTF-8',
            })
                .done(function (res) {
                   var  checkValid=res.data.status;
                    if(checkValid) {
                        referrerValidBool=true;
                        deferred.resolve();
                    }
                    else {
                        referrerValidBool=false;
                        deferred.reject();
                    }
                });

            if(deferred.state() == "resolved")  {
                $target.removeClass('error').addClass('valid');
                referrerError.html('').hide().show();

            }
            else {
                $target.removeClass('valid').addClass('error');
                referrerError.html('推荐人不存在').show();
            }
        }
        else {
            $target.removeClass('error').addClass('valid');
            referrerError.html('').hide();
            referrerValidBool=true;
        }
        checkInputValid();
    });
});
