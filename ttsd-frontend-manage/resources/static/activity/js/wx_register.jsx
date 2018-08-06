require("activityStyle/wx_register.scss");
let commonFun = require('publicJs/commonFun');
require('publicJs/placeholder');
let ValidatorObj = require('publicJs/validator');
let referrerValidBool=true;
let registerForm = globalFun.$('#registerUserForm');
let $registerSubmit = $('.register-user.btn-normal-invest');
var $registerFrame = $('#registerCommonFrame');
var $registerForm = $('.register-user-form', $registerFrame),
    $phoneDom = $('#mobile', $registerFrame),
    $fetchCaptcha = $('.fetch-captcha', $registerFrame),
    $changecode = $('.img-change', $registerFrame),
    $appCaptcha = $('#appCaptcha', $registerFrame),
    $registerContainer = $('#registerContainer'),
    $getbagContainer = $('#getbagContainer'),
    $successContainer = $('#successContainer'),
    $getBag = $('#getBag', $getbagContainer),
    $btnExperience = $('#btnExperience', $successContainer);


require.ensure(['publicJs/placeholder'], function (require) {
    require('publicJs/placeholder');
    $('input[type="text"],input[type="password"]', $(registerForm)).placeholder();
}, 'placeholder');
$(':input','.register-user-form').not(':button,:submit,:reset,:hidden').val('');
$getBag.on('click', function (event) {
    event.preventDefault();
    $getbagContainer.hide();
    $registerContainer.show();
});
$btnExperience.on('click', function (event) {
    event.preventDefault();
    globalFun.toExperience(event);
});

let refreshCapt = function (flush) {
    commonFun.refreshCaptcha(globalFun.$('#image-captcha-image'), '/register/user/image-captcha', flush);
};
refreshCapt(false);

//change images code
$changecode.on('touchstart', function (event) {
    event.preventDefault();
    refreshCapt(true);
});
//show protocol info
$('.show-agreement').on('touchstart', function (event) {
    event.preventDefault();
    layer.open({
        type: 1,
        title: '拓天速贷服务协议',
        area: $(window).width() < 700 ? ['100%', '100%'] : ['950px', '600px'],
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
$appCaptcha.on('keyup', function (event) {
    if ($('#mobile').val() != '' && /0?(13|14|15|18)[0-9]{9}/.test($('#mobile').val()) && $('#appCaptcha').val() != '') {
        $fetchCaptcha.prop('disabled', false);
    } else {
        $fetchCaptcha.prop('disabled', true);
    }
});
let captchaValid = false;
// 获取手机验证码
$fetchCaptcha.on('touchstart', function (event) {
    var $this = $(this);
    event.preventDefault();
    if ($this.prop('disabled')) {
        return;
    }
    var captchaVal = $appCaptcha.val(),
        mobile = $phoneDom.val();
    if (captchaVal.length < 5) {
        layer.msg('请输入5位验证码');
        return;
    };
    $fetchCaptcha.prop('disabled', true);

    commonFun.useAjax({
        url: '/register/user/send-register-captcha',
        type: 'POST',
        dataType: 'json',
        data: {imageCaptcha: captchaVal, mobile: mobile}
    }, function (response) {
        var data = response.data;
        var countdown = 60, timer;
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
            layer.msg('短信发送频繁,请稍后再试');
        }

        if (!data.status && !data.isRestricted) {
            layer.msg('图形验证码错误');
        }
        refreshCapt(true);
    });
});


//用户注册表单校验
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
            captchaValid = true;
            ValidatorObj.isHaveError.no.apply(that, _arguments);
        }
        else {
            getResult = errorMsg;
            captchaValid = false;
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
},], true);
validator.add(registerForm.password, [{
    strategy: 'isNonEmpty',
    errorMsg: '密码不能为空'
}, {
    strategy: 'checkPassword',
    errorMsg: '密码为6位至20位，不能全是数字'
}], true);
validator.add(registerForm.appCaptcha, [{
    strategy: 'isNonEmpty',
    errorMsg: '验证码不能为空'
}], true);
validator.add(registerForm.captcha, [{
    strategy: 'isNonEmpty',
    errorMsg: '验证码不能为空'
}, {
    strategy: 'isNumber:6',
    errorMsg: '验证码为6位数字'
}, {
    strategy: 'isCaptchaValid',
    errorMsg: '验证码不正确'
}], true);



let reInputs = $(registerForm).find('input[validate]');

for (let i = 0, len = reInputs.length; i < len; i++) {
    globalFun.addEventHandler(reInputs[i], "keyup", "focusout", function () {
        let errorMsg = validator.start(this);
        isDisabledButton();
    })
}

//用来判断获取验证码和立即注册按钮 是否可点击
//表单验证通过会
function isDisabledButton() {
    let mobile = registerForm.mobile,
        password = registerForm.password,
        captcha = registerForm.captcha,
        imgcaptcha = registerForm.appCaptcha;

    //获取验证码点亮
    let isMobileValid = !globalFun.hasClass(mobile, 'error') && mobile.value;
    let isPwdValid = !globalFun.hasClass(password, 'error') && password.value;
    let isAppCaptchaValid = !globalFun.hasClass(imgcaptcha, 'error') && imgcaptcha.value;

    let isDisabledCaptcha = isMobileValid && isPwdValid && isAppCaptchaValid;

    //通过获取验证码按钮来判断
    $('#getCaptchaBtn').prop('disabled', !isDisabledCaptcha);

    let captchaValid = !$(captcha).hasClass('error') && captcha.value;

    let isDisabledSubmit = captchaValid && referrerValidBool && $('#agreementInput').val() == 'true';
    $registerSubmit.prop('disabled', !isDisabledSubmit);

}
isDisabledButton();
//点击立即注册按钮
registerForm.onsubmit = function (event) {
    event.preventDefault();
    registerForm.submit();
}

