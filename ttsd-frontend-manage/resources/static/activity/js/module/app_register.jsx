require("activityStyle/module/app_register.scss");
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');

let registerForm = globalFun.$('#registerForm'),
    $fetchCaptcha = $('#getCaptchaBtn'),
    $captchaText= $(registerForm).find('.image-captcha-text');

let captchaSrc = '/register/user/image-captcha';
// 刷新图形验证码
$('#imageCaptcha').on('click', function () {
    commonFun.refreshCaptcha(this, captchaSrc);
    $captchaText.val('');
}).trigger('click');


//通过图形验证码获取验证码
(function () {
    function sendCaptcha() {
        let imageCaptcha = $captchaText.val(),
            mobileVal = registerForm.mobile.value;
        //判断手机号是否正确
        if(mobileVal=='' || !/(^1[0-9]{10}$)/.test(mobileVal)) {
            return;
        }
        let ajaxOption = {
            url: '/register/user/send-register-captcha',
            type: 'POST',
            data: 'imageCaptcha='+imageCaptcha+'&mobile='+mobileVal
        };
        commonFun.useAjax(ajaxOption, function (responseData) {
            $fetchCaptcha.prop('disabled', false);

            let data = responseData.data;
            if (data.status && !data.isRestricted) {
                //获取手机验证码成功，，并开始倒计时
                commonFun.countDownLoan({
                    btnDom: $fetchCaptcha,
                    isAfterText: '获取验证码',
                    textCounting: 's'
                },function() {
                    //倒计时结束后刷新验证码
                    commonFun.refreshCaptcha(imageCaptcha, captchaSrc);
                });
            } else if (!data.status && data.isRestricted) {
                commonFun.refreshCaptcha(imageCaptcha, captchaSrc);
                registerForm.captcha.value = '';
                layer.msg('短信发送频繁，请稍后再试');

            } else if (!data.status && !data.isRestricted) {
                commonFun.refreshCaptcha(imageCaptcha, captchaSrc);
                registerForm.captcha.value = '';
                layer.msg('图形验证码不正确');
            }
        });
    }

    $fetchCaptcha.on('click', function () {
        $fetchCaptcha.prop('disabled', true);

        let imageCaptcha = $captchaText.val();

        if (/\d{5}/.test(imageCaptcha)) {
            sendCaptcha();
        } else {
            layer.msg('请输入正确的图形验证码');
            $fetchCaptcha.prop('disabled', false);
        }
    });
})();

$('#agreeRule').on('click', function(event) {
    event.preventDefault();
    layer.open({
        type: 1,
        title: '拓天速贷服务协议',
        area: ['100%', '100%'],
        shadeClose: true,
        move: false,
        scrollbar: true,
        skin:'register-skin',
        content: $('#agreementBox')
    });
});

let validator = new ValidatorObj.ValidatorForm();

//验证码是否正确
validator.newStrategy(registerForm.captcha, 'isCaptchaValid', function (errorMsg, showErrorAfter) {
    var getResult = '',
        that = this,
        _arguments = arguments;

    var _phone = registerForm.mobile.value,
        _captcha = registerForm.captcha.value;

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

validator.add(registerForm.mobile, [{
    strategy: 'isNonEmpty',
    errorMsg: '手机号不能为空',
}, {
    strategy: 'isMobile',
    errorMsg: '手机号格式不正确'
},{
    strategy: 'isMobileExist',
    errorMsg: '手机号已经存在'
}],true);

validator.add(registerForm.password, [{
    strategy: 'isNonEmpty',
    errorMsg: '密码不能为空'
}, {
    strategy: 'checkPassword',
    errorMsg: '密码为6位至20位，不能全是数字'
}],true);

validator.add(registerForm.captcha, [{
    strategy: 'isNonEmpty',
    errorMsg: '验证码不能为空'
},{
    strategy: 'isNumber:6',
    errorMsg: '验证码为6位数字'
},{
    strategy: 'isCaptchaValid',
    errorMsg: '验证码不正确'
}],true);

let reInputs=$(registerForm).find('input[validate]');
reInputs=Array.from(reInputs);
for (let el of reInputs) {
    globalFun.addEventHandler(el,"keyup", function() {
        validator.start(this);
    });
}

//点击立即注册按钮
registerForm.onsubmit = function(event) {
    event.preventDefault();
    let errorMsg;
    for(let i=0,len=reInputs.length;i<len;i++) {
        errorMsg=validator.start(reInputs[i]);
        if(errorMsg) {
            break;
        }
    }
    if(!errorMsg) {
        registerForm.submit();
    }
}
