require(['jquery','underscore', 'layerWrapper', 'jquery.validate', 'jquery.validate.extension', 'jquery.form', 'jquery.ajax.extension'], function ($, _, layer) {
    (function(){
        var $registerForm=$('.register-user-form'),
            $phoneDom=$('#mobile'),
            $fetchCaptcha=$('.fetch-captcha'),
            $changecode=$('.img-change'),
            $registerBtn=$(".registered"),
            $loginName = $('#login-name'),
            $password = $('#password'),
            $registerUser = $('#register-user'),
            countdown=60;

        //form validate
        $registerForm.validate({
            focusInvalid: false,
            errorPlacement: function(error, element) {
                error.appendTo($('#'+ element.attr('id') + 'Err'));
            },
            rules: {
                loginName: {
                    required: true,
                    regex: /(?!^\d+$)^\w{5,25}$/,
                    isExist: "/register/user/login-name/{0}/is-exist"
                },
                mobile: {
                    required: true,
                    digits: true,
                    isPhone:true,
                    minlength: 11,
                    maxlength: 11,
                    isExist: "/register/user/mobile/{0}/is-exist"
                },
                password: {
                    required: true,
                    regex: /^(?=.*[^\d])(.{6,20})$/
                },
                appCaptcha : {
                    required: true
                },
                captcha : {
                    required: true,
                    digits: true,
                    maxlength: 6,
                    minlength: 6,
                    captchaVerify: {
                        param: function () {
                            var mobile = $('input[name="mobile"]').val();
                            return "/register/user/mobile/" + mobile + "/captcha/{0}/verify"
                        }
                    }
                },
                agreement: {
                    required: true
                }
            },
            messages: {
                loginName: {
                    required: "请输入用户名",
                    regex: '5位至25位数字与字母下划线组合，不能全部数字',
                    isExist: '用户名已存在'
                },
                mobile: {
                    required: '请输入手机号',
                    digits: '必须是数字',
                    minlength: '手机格式不正确',
                    isPhone:'请输入正确的手机号码',
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
                    captchaVerify: '验证码不正确'
                },
                agreement: {
                    required: "请同意服务协议"
                }
            }


        });


        var refreshCaptcha = function () {
            $('.image-captcha img').attr('src', '/register/user/image-captcha?' + new Date().getTime().toString());
        };
        refreshCaptcha();


        //phone focusout
        $phoneDom.on('focusout', function(event) {
            event.preventDefault();
            $(this).val()!='' && /0?(13|14|15|18)[0-9]{9}/.test($(this).val())?$fetchCaptcha.prop('disabled', false):$fetchCaptcha.prop('disabled', true);
        });

        //change images code
        $changecode.on('click', function(event) {
            event.preventDefault();
            refreshCaptcha();
        });
        //show protocol info
        $('.show-agreement').on('click', function(event) {
            event.preventDefault();
            layer.open({
                type: 1,
                title: '拓天速贷服务协议',
                area: ['950px', '600px'],
                shadeClose: true,
                move: false,
                scrollbar: true,
                skin:'register-skin',
                content: $('#agreementBox')
            });
        });
        
        $fetchCaptcha.on('click', function(event) {
            event.preventDefault();

            var captchaVal=$('#appCaptcha').val(),
                mobile=$phoneDom.val();
             $.ajax({
                 url: '/register/user/send-register-captcha',
                 type: 'POST',
                 dataType: 'json',
                 data: {imageCaptcha: captchaVal,mobile:mobile},
             })
             .done(function() {
                timer=window.setInterval(getCode, 1000);
             })
             .fail(function() {
                 //console.log("error");
             });
            
        });
        //timer 
        function getCode() { 
            if (countdown == 0) {
                window.clearInterval(timer); 
                $fetchCaptcha.prop('disabled',false).text('获取验证码');    
                countdown = 60; 
            } else { 
                $fetchCaptcha.prop('disabled', true).text(countdown+'秒后重发');
                countdown--; 
            } 
        }

        // phone validate   
        jQuery.validator.addMethod("isPhone", function(value, element) {   
            var tel = /0?(13|14|15|18)[0-9]{9}/;
            return this.optional(element) || (tel.test(value));
        }, "请正确填写您的手机号码");

        //register button
        $registerBtn.on('click', function(event) {
            event.preventDefault();
            $('body,html').animate({scrollTop:0},'fast');
        });
        
    })();
});
