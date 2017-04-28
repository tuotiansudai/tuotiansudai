require(['jquery', 'underscore', 'layerWrapper','placeholder', 'jquery.validate', 'jquery.validate.extension', 'jquery.form', 'jquery.ajax.extension'], function ($, _, layer) {
	$(function() {
		var $registerFrame=$('#registerCommonFrame');
        var $registerForm = $('.register-user-form',$registerFrame),
            $phoneDom = $('#mobile',$registerFrame),
            $fetchCaptcha = $('.fetch-captcha',$registerFrame),
            $changecode = $('.img-change',$registerFrame),
            $appCaptcha = $('#appCaptcha',$registerFrame),
            $registerContainer=$('#registerContainer'),
            $getbagContainer=$('#getbagContainer'),
            $successContainer=$('#successContainer'),
            $getBag=$('#getBag',$getbagContainer),
            $btnExperience=$('#btnExperience',$successContainer);


        var bCategory = globalFun.browserRedirect();

        var mobileValid=false,
            passwordValid=false,
            captchaValid=false;

        $('input[type="text"],input[type="password"]', $registerForm).placeholder();

        $getBag.on('click', function(event) {
        	event.preventDefault();
        	$getbagContainer.hide();
        	$registerContainer.show();
        });
        $btnExperience.on('click', function(event) {
        	event.preventDefault();
        	globalFun.toExperience(event);
        });
        //form validate
        $registerForm.validate({
            focusInvalid: false,
            errorPlacement: function (error, element) {
                error.appendTo($('#' + element.attr('id') + 'Err'));
            },
            rules: {
                mobile: {
                    required: true,
                    digits: true,
                    isPhone: true,
                    minlength: 11,
                    maxlength: 11,
                    isExist: "/register/user/mobile/{0}/is-exist"
                },
                password: {
                    required: true,
                    regex: /^(?=.*[^\d])(.{6,20})$/
                },
                appCaptcha: {
                    required: true
                },
                captcha: {
                    required: true,
                    digits: true,
                    maxlength: 6,
                    minlength: 6,
                    checkCaptcha:true
                },
                agreement: {
                    required: true
                }
            },
            messages: {
                mobile: {
                    required: '请输入手机号',
                    digits: '必须是数字',
                    minlength: '手机格式不正确',
                    isPhone: '请输入正确的手机号码',
                    maxlength: '手机格式不正确',
                    isExist: '手机号已存在'
                },
                password: {
                    required: "请输入密码",
                    regex: '6位至20位，不能全是数字'
                },
                appCaptcha: {
                    required: '请输入验证码'
                },
                captcha: {
                    required: '请输入手机验证码',
                    digits: '验证码格式不正确',
                    maxlength: '验证码格式不正确',
                    minlength: '验证码格式不正确',
                    checkCaptcha:'验证码不正确'
                },
                agreement: {
                    required: "请同意服务协议"
                }
            },
            submitHandler: function (form) {
                form.submit();
            }
        });
        var refreshCapt = function () {
            $('#image-captcha-image').attr('src','/register/user/image-captcha?' + new Date().getTime().toString());
        };
        refreshCapt();

        //change images code
        $changecode.on('touchstart', function (event) {
            event.preventDefault();
            refreshCapt();
        });
        //show protocol info
        $('.show-agreement').on('touchstart', function (event) {
            event.preventDefault();
            var area = ['950px', '600px'];
            if (bCategory == 'mobile') {
                area = ['100%', '100%'];
            }
            layer.open({
                type: 1,
                title: '拓天速贷服务协议',
                area: area,
                shadeClose: true,
                move: false,
                scrollbar: true,
                skin: 'register-skin',
                content: $('#agreementBox')
            });
        });

        $('#agreementBox').find('.close-tip').on('touchstart', function () {
            layer.closeAll();
        })

        //图形验证码输入后高亮显示获取验证码
        $appCaptcha.on('keyup',function(event) {
            if(/^\d{5}$/.test(event.target.value)) {
                $(event.target).addClass('valid').removeClass('error');
                captchaValid=true;
                mobileValid=$phoneDom.hasClass('valid');
                passwordValid=$('.password',$registerFrame).hasClass('valid');

                if(mobileValid && passwordValid && captchaValid) {
                    console.log('ok');
                    $fetchCaptcha.prop('disabled', false);
                    $('#appCaptchaErr').html('');
                }
                else {
                    $fetchCaptcha.prop('disabled', true);
                    console.log('error');
                }

            }
        });

        // 获取手机验证码
        $fetchCaptcha.on('touchstart', function (event) {
            var $this=$(this);
            event.preventDefault();
            if($this.prop('disabled')) {
                return;
            }
            $fetchCaptcha.prop('disabled', true);
            var captchaVal = $appCaptcha.val(),
                mobile = $phoneDom.val();
            $.ajax({
                url: '/register/user/send-register-captcha',
                type: 'POST',
                dataType: 'json',
                data: {imageCaptcha: captchaVal, mobile: mobile}
            })
                .done(function (response) {
                    var data = response.data;
                    var countdown = 60,timer;
                    if (data.status && !data.isRestricted) {
                        timer = setInterval(function () {
                            $fetchCaptcha.prop('disabled', true).text(countdown + '秒后重发');
                            countdown--;
                            if (countdown == 0) {
                                clearInterval(timer);
                                $fetchCaptcha.prop('disabled', false).text('重新发送');
                            }
                        }, 1000);
                        return;
                    }
                    if (!data.status && data.isRestricted) {
                        $('#appCaptchaErr').html('短信发送频繁,请稍后再试');
                    }

                    if (!data.status && !data.isRestricted) {
                        $('#appCaptchaErr').html('图形验证码错误');
                        refreshCapt();
                    }
                })
                .fail(function () {
                    layer.msg('请求失败，请重试！');
                    $fetchCaptcha.prop('disabled', false);
                    refreshCapt();
                });
        });

        // 电话号码是否有效
        jQuery.validator.addMethod("isPhone", function (value, element) {
            var tel = /0?(13|14|15|18)[0-9]{9}/;
            return this.optional(element) || (tel.test(value));
        }, "请正确填写您的手机号码");

        //验证码是否正确
        jQuery.validator.addMethod("checkCaptcha", function(value, element) {
            var mobile=$phoneDom.val();
            var deferred = $.Deferred();
            if(/^\d{6}$/.test(value) && mobile) {
                $.ajax({
                    url:'/register/user/mobile/' + mobile + '/captcha/'+value+'/verify',
                    async:false,
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
            return deferred.state() == "resolved" ? true : false;
        }, "验证码不正确");

	});
});