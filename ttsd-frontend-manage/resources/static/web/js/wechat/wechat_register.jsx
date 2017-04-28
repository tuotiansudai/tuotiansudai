require('publicJs/plugins/jQuery.md5');
let commonFun = require('publicJs/commonFun');
let ValidatorObj = require('publicJs/validator');

let weChatRegister = globalFun.$('#weChatRegister');
let formRegister = globalFun.$('#formRegister');

let validator = new ValidatorObj.ValidatorForm();
let $errorBox = $('.error-box', $(formRegister));
let imageCaptcha = globalFun.$('#imageCaptcha');
let captchaSrc = '/register/user/image-captcha';

commonFun.refreshCaptcha(imageCaptcha, captchaSrc);

//获取验证码
(function () {
    let formCaptcha = globalFun.$('#formCaptcha');
    let $getCaptcha = $(formRegister).find('.get-captcha');

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
                layer.msg('短信发送频繁，请稍后再试');

            } else if (!data.status && !data.isRestricted) {
                layer.msg('图形验证码不正确');
            }
        });
    }

    $getCaptcha.on('click', function () {
        $getCaptcha.prop('disabled', true);
        let imageCaptcha = formCaptcha.imageCaptcha.value;

        if (/\d{5}/.test(imageCaptcha)) {
            sendCaptcha();
        }
        else {
            layer.msg('请输入正确的图形验证码');
            $getCaptcha.prop('disabled', false);
        }
    });
})();

//验证码是否正确
validator.newStrategy(formRegister.captcha, 'isCaptchaValid', function (errorMsg, showErrorAfter) {
    var getResult = '',
        that = this,
        _arguments = arguments;

    var _phone = formRegister.mobile.value,
        _captcha = formRegister.captcha.value;

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

validator.add(formRegister.password, [{
    strategy: 'isNonEmpty',
    errorMsg: '密码不能为空'
}, {
    strategy: 'checkPassword',
    errorMsg: '密码为6位至20位，不能全是数字'
}]);

validator.add(formRegister.captcha, [{
    strategy: 'isNonEmpty',
    errorMsg: '验证码不能为空'
}, {
    strategy: 'isNumber:6',
    errorMsg: '验证码为6位数字'
}, {
    strategy: 'isCaptchaValid',
    errorMsg: '验证码不正确'
}]);

let reInputs = $(formRegister).find('input[validate]');

reInputs = Array.from(reInputs);
for (var el of reInputs) {
    globalFun.addEventHandler(el, "keyup", "blur", function () {
        let errorMsg = validator.start(this);
        if (errorMsg) {
            $errorBox.text(errorMsg);
        }
        else {
            $errorBox.text('');
        }
    })
}

//点击立即注册按钮
formRegister.onsubmit = function (event) {
    event.preventDefault();

    for (let i = 0, len = reInputs.length; i < len; i++) {
        let errorMsg = validator.start(reInputs[i]);
        if (errorMsg) {
            $errorBox.text(errorMsg);
            return;
            // break;
        }
    }

    formRegister.submit();
};