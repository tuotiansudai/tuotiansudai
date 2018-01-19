let commonFun = require('publicJs/commonFun');
let ValidatorObj = require('publicJs/validator');

let retrieveForm = globalFun.$('#retrieveForm');  //找回密码的form
let inputPasswordForm=globalFun.$('#inputPasswordForm'); //找回密码的form

retrieveForm && forgetPassword();
inputPasswordForm && inputPassword();

function forgetPassword() {

    let formCaptcha = globalFun.$('#formCaptcha');
    let validator = new ValidatorObj.ValidatorForm();
    let $errorBox = $('.error-box', $(retrieveForm));
    let imageCaptcha = globalFun.$('#imageCaptcha');
    let captchaSrc = '/register/user/image-captcha';
    let $getCaptcha = $(retrieveForm).find('.get-captcha');
    let $registerSubmit=$('button[type="submit"]',$(retrieveForm));

    //刷新验证码
    $(imageCaptcha).on('click', function () {
        commonFun.refreshCaptcha(this, captchaSrc);
        retrieveForm.captcha.value = '';
    });

//获取验证码
    (function () {
        function sendCaptcha() {
            let ajaxOption = {
                url: '/register/user/send-register-captcha',
                type: 'POST',
                data: $(formCaptcha).serialize()
            };
            commonFun.useAjax(ajaxOption, function (responseData) {
                $getCaptcha.prop('disabled', false);

                let data = responseData.data;
                if (data.status && !data.isRestricted) {
                    //获取手机验证码成功，，并开始倒计时
                    commonFun.countDownLoan({
                        btnDom: $getCaptcha,
                        isAfterText: '获取验证码',
                        textCounting: 's'
                    },function() {
                        //倒计时结束后刷新验证码
                        commonFun.refreshCaptcha(imageCaptcha, captchaSrc);
                    });
                } else if (!data.status && data.isRestricted) {
                    commonFun.refreshCaptcha(imageCaptcha, captchaSrc);
                    retrieveForm.captcha.value = '';
                    layer.msg('短信发送频繁，请稍后再试');

                } else if (!data.status && !data.isRestricted) {
                    commonFun.refreshCaptcha(imageCaptcha, captchaSrc);
                    retrieveForm.captcha.value = '';
                    layer.msg('图形验证码不正确');
                }
            });
        }

        $getCaptcha.on('click', function () {
            $getCaptcha.prop('disabled', true);
            let imageCaptcha = formCaptcha.imageCaptcha.value;

            if (/\d{5}/.test(imageCaptcha)) {
                sendCaptcha();
            } else {
                layer.msg('请输入正确的图形验证码');
                $getCaptcha.prop('disabled', false);
            }
        });
    })();

//验证码是否正确
    validator.newStrategy(retrieveForm.captcha, 'isCaptchaValid', function (errorMsg, showErrorAfter) {
        var getResult = '',
            that = this,
            _arguments = arguments;

        var _phone = retrieveForm.mobile.value,
            _captcha = retrieveForm.captcha.value;

        //先判断手机号格式是否正确
        if (!/(^1[0-9]{10}$)/.test(_phone)) {
            return;
        }
        commonFun.useAjax({
            type: 'GET',
            async: false,
            url: `/register/user/mobile/${_phone}/captcha/${_captcha}/verify`
        }, function (response) {
            if (response.data.status) {
                // 如果为true说明验证码正确
                getResult = '';
                ValidatorObj.isHaveError.no.apply(that, _arguments);
            }
            else {
                getResult = errorMsg;
                ValidatorObj.isHaveError.yes.apply(that, _arguments);
            }
        });
        return getResult;
    });

    validator.add(retrieveForm.captcha, [{
        strategy: 'isNonEmpty',
        errorMsg: '验证码不能为空'
    }, {
        strategy: 'isNumber:6',
        errorMsg: '验证码为6位数字'
    }, {
        strategy: 'isCaptchaValid',
        errorMsg: '验证码不正确'
    }]);

    globalFun.addEventHandler(retrieveForm.captcha,"keyup",function () {
        isDisabledButton();
    })

//用来判断获取验证码和立即注册按钮 是否可点击
    function isDisabledButton() {
        let imageCaptcha=formCaptcha.imageCaptcha.value,  //图形验证码
            captcha=retrieveForm.captcha.value;  //短信验证码

        let isDisabledSubmit = true;

        if(/\d{5}/.test(imageCaptcha) && /\d{6}/.test(captcha)) {
            isDisabledSubmit = false;
        }
        $registerSubmit.prop('disabled',isDisabledSubmit);
    }

//点击立即注册按钮
    retrieveForm.onsubmit = function (event) {
        event.preventDefault();

        let errorMsg = validator.start(retrieveForm.captcha);
        if (errorMsg) {
            layer.msg(errorMsg);
            return;
        }

        retrieveForm.submit();
    };
}

function inputPassword() {
    let errorDom=$(inputPasswordForm).find('.error-box');
    //忘记密码表单校验
    let validatorPass = new ValidatorObj.ValidatorForm();
    validatorPass.add(inputPasswordForm.password, [{
        strategy: 'isNonEmpty',
        errorMsg: '密码不能为空'
    }, {
        strategy: 'checkPassword',
        errorMsg: '密码为6位至20位，不能全是数字'
    }]);

    inputPasswordForm.onsubmit = function(event) {
        event.preventDefault();

        let errorMsg = validatorPass.start(inputPasswordForm.password);
        if(errorMsg) {
            layer.msg(errorMsg);
            // layer.msg('邮箱绑定失败，请重试,请重试,请重试！', {type: 1, time: 800000});
            return;
        }
        inputPasswordForm.submit();

    }

}
