let commonFun = require('publicJs/commonFun');
let ValidatorObj = require('publicJs/validator');

let formRegister = globalFun.$('#formRegister');

let validator = new ValidatorObj.ValidatorForm();
let $errorBox = $('.error-box', $(formRegister));
let imageCaptcha = globalFun.$('#imageCaptcha');
let captchaSrc = '/register/user/image-captcha';
let $getCaptcha = $(formRegister).find('.get-captcha');
let $registerSubmit=$('button[type="submit"]',$(formRegister));
let referrerValidBool=true;
let $referrer=$('input.referrer', $(formRegister));

//刷新验证码
$('#imageCaptcha').on('click', function () {
    commonFun.refreshCaptcha(this, captchaSrc);
    formRegister.captcha.value = '';
});

//填写邀请人
$(formRegister).find('.invite-mobile').on('click',function() {
    let $thisTitle = $(this),
        $inputInvite = $thisTitle.next('.referrer');
    $thisTitle.toggleClass('closed');
    $inputInvite.toggleClass('closed');

    if($referrer.is(':hidden')) {
        referrerValidBool=true;
    }
    else if(!$referrer.is(':hidden') && $referrer.hasClass('error')) {
        referrerValidBool=false;
    }
    isDisabledButton();

});

//弹出拓天速贷服务协议
$(formRegister).find('.show-agreement').on('touchstart', function (event) {
    event.preventDefault();
    layer.open({
        type: 1,
        title: '拓天速贷服务协议',
        area: $(window).width()<700?['100%', '100%']:['950px', '600px'],
        shadeClose: true,
        move: false,
        scrollbar: true,
        skin: 'register-skin',
        content: $('#agreementBox')
    });
});


//获取验证码
(function () {
    let formCaptcha = globalFun.$('#formCaptcha');
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
                formRegister.captcha.value = '';
                layer.msg('短信发送频繁，请稍后再试');

            } else if (!data.status && !data.isRestricted) {
                commonFun.refreshCaptcha(imageCaptcha, captchaSrc);
                formRegister.captcha.value = '';
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
for(let i=0,len=reInputs.length; i<len;i++) {
    globalFun.addEventHandler(reInputs[i],"keyup",function () {
        let errorMsg = validator.start(this);

        referrerValidBool = !(this.name == 'referrer' && errorMsg);
        isDisabledButton();
    })
}

//用来判断获取验证码和立即注册按钮 是否可点击
//表单验证通过会
function isDisabledButton() {
    let mobile=formRegister.mobile,
        password=formRegister.password,
        captcha=formRegister.captcha,
        referrer=formRegister.referrer;

    //获取验证码点亮
    let isMobileValid=!globalFun.hasClass(mobile,'error') && mobile.value;
    let isPwdValid = !globalFun.hasClass(password,'error') && password.value;

    let isDisabledCaptcha = isMobileValid && isPwdValid;

    //按钮上有样式名count-downing，说明正在倒计时
    if ($getCaptcha.hasClass('count-downing')) {
        $getCaptcha.prop('disabled',true);
    }
    else {
        $getCaptcha.prop('disabled',!isDisabledCaptcha);
    }
    //给验证码弹框中的mobile隐藏域赋值
    isDisabledCaptcha && (globalFun.$('#formCaptcha').mobile.value = mobile.value);
    //通过获取验证码按钮来判断
    !isDisabledCaptcha && $registerSubmit.prop('disabled',true);

    let captchaValid = !$(captcha).hasClass('error') && captcha.value;

    if($(referrer).is(':hidden')) {
        referrerValidBool=true;
    }

    let isDisabledSubmit= isMobileValid && isPwdValid && captchaValid  && referrerValidBool;
    $registerSubmit.prop('disabled',!isDisabledSubmit);

}

//点击立即注册按钮
formRegister.onsubmit = function (event) {
    event.preventDefault();

    for (let i = 0, len = reInputs.length; i < len; i++) {
        let errorMsg = validator.start(reInputs[i]);
        if (errorMsg) {
            layer.msg(errorMsg);
            return;
        }
    }

    formRegister.submit();
};