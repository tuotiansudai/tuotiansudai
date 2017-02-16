require(['underscore', 'jquery', 'jquery.validate', 'jquery.validate.extension', 'jquery.form', 'jquery.ajax.extension', 'commonFun'], function (_, $) {

    var registerAccountForm = $('.register-account-form'),
        $buttonLayer = $('.button-layer', registerAccountForm),
        $btnSubmit = $('input[type="submit"]', registerAccountForm);


    registerAccountForm.validate({
        focusCleanup: true,
        focusInvalid: false,
        onfocusout: function (element) {
            this.element(element);
            if($('input.valid',registerAccountForm).length==2) {
                $btnSubmit.prop('disabled',false);
            }
            $buttonLayer.find('.status').removeClass('error').html('');
        },
        submitHandler: function (form) {
            //form.submit();
            statisticsCnzzByRegister();
            $(form).ajaxSubmit({
                dataType: 'json',
                beforeSubmit: function (arr, $form, options) {
                    $buttonLayer.find('.status').removeClass('error').html('认证中...');
                    $btnSubmit.prop('disabled', true);
                },
                success: function (response) {
                    if(response.data.status) {
                        $buttonLayer.find('.status').removeClass('error').html('认证成功');
                        //var redirect = document.referrer;
                        //setTimeout(location.href = redirect, 3000);
                        location.href = '/callback/register_account?ret_code=0000'
                    }
                    else {
                        $buttonLayer.find('.status').addClass('error').html('认证失败，请检查');
                        $btnSubmit.prop('disabled', false);
                    }

                },
                error: function (errorMap) {
                    $buttonLayer.find('.status').addClass('error').html('认证失败，请检查');
                    $btnSubmit.prop('disabled', false);
                },
                complete: function () {

                }
            });

            return false;
        },
        onkeyup: function (element, event) {

            var excludedKeys = [16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];

            if ((event.which !== 9 || this.elementValue(element) !== "") && $.inArray(event.keyCode, excludedKeys) === -1) {
                this.element(element);
            }
        },
        rules: {
            userName: {
                required: true
            },
            identityNumber: {
                required: true,
                identityCheckValid: true,
                identityCardAge: true,
                isExist: "/register/account/identity-number/{0}/is-exist"
            }
        },
        messages: {
            userName: {
                required: "请输入姓名"
            },
            identityNumber: {
                required: '请输入身份证号',
                identityCheckValid: '您的身份证号码不正确',
                identityCardAge: '年龄未满18周岁',
                isExist: "身份证已存在"
            }
        }
    });
    var registerAccountCnzzAddress = [
        {
            'function': '/recharge',
            'category': '75实名认证页',
            'action': '认证',
            'label': '充值'
        },
        {
            'function': '/bind-card',
            'category': '77实名认证页',
            'action': '认证',
            'label': '绑卡'
        },
        {
            'function': '/loan',
            'category': '78实名认证页',
            'action': '认证',
            'label': '马上投资'
        },
        {
            'function': '/account',
            'category': '79实名认证页',
            'action': '认证',
            'label': '签到'
        },
        {
            'function': '/point',
            'category': '79实名认证页',
            'action': '认证',
            'label': '签到'
        },
        {
            'function': '/auto-invest/agreement',
            'category': '80实名认证页',
            'action': '认证',
            'label': '授权自动投标'
        },
        {
            'function': '/personal-info',
            'category': '82实名认证页',
            'action': '认证',
            'label': '开启免密'
        }, {
            'function': '/register/user',
            'category': '84实名认证页',
            'action': '认证',
            'label': '注册后'
        }
    ];

    //获取历史记录的上一页地址
    function getReferrer() {
        var referrer = '';
        try {
            referrer = window.top.document.referrer;
        } catch (e) {
            if (window.parent) {
                try {
                    referrer = window.parent.document.referrer;
                } catch (e2) {
                    referrer = '';
                }
            }
        }
        if (referrer === '') {
            referrer = document.referrer;
        }
        return referrer;
    }

    function statisticsCnzzByRegister() {
        var referrer = getReferrer();
        for (var index in registerAccountCnzzAddress) {
            if (referrer.indexOf(registerAccountCnzzAddress[index].function) != -1) {
                cnzzPush.trackClick(registerAccountCnzzAddress[index].category, registerAccountCnzzAddress[index].action, registerAccountCnzzAddress[index].label);
                break;
            }
        }
    }
});


