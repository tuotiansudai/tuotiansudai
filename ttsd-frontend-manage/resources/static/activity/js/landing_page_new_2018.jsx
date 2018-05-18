require("activityStyle/landing_page_new_2018.scss");
let commonFun = require('publicJs/commonFun');
require('publicJs/placeholder');
let ValidatorObj = require('publicJs/validator');
let $registerContainer = $('#registerContainer');
let $agreementLi = $('#agreementLable');
let $btnCoupon = $('#btn-get-coupon', $registerContainer),
    browser = globalFun.browserRedirect();
let urlObj = globalFun.parseURL(location.href);
let $btnChangeImgCode = $('.img-change',$registerContainer );//换一张
let registerForm = globalFun.$('#registerUserForm');
let $fetchCaptcha = $('#getCaptchaBtn');//获取短信验证码btn
let $inputImgCaptcha = $('#input_img_captcha');//图形验证码输入框
let $inputSmsCaptcha = $('#smsCaptcha');//图形验证码输入框
let isSmsSended = false;
let $registerSubmit=$('input[type="submit"]',$(registerForm));

// let $registerSubmit = $('input[type="submit"]', $(registerForm));
// let $voiceCaptcha = $('#voice_captcha');
// let $voiceBtn = $('#voice_btn', $voiceCaptcha);


$inputImgCaptcha.on('keyup', function (event) {
    event.preventDefault();
    if ($('#mobile').val() != '' && /0?(13|14|15|18)[0-9]{9}/.test($('#mobile').val()) && $inputImgCaptcha.val().length == 5) {
        if(!isSmsSended){
            $fetchCaptcha.prop('disabled', false);
        }
    } else {
        $fetchCaptcha.prop('disabled', true);
    }
});

$agreementLi.on('click', function (e) {
    e.preventDefault();
    $('.icon-yesOrNo-checked').toggleClass('checked');
})
$('#recommendLabel').on('click', function () {
    $('.icon-arrow-bottom').toggleClass('active');
    $('.recomender-iphone').toggleClass('show');
})
//刷新验证码
$btnChangeImgCode.on('click', function (event) {
    event.preventDefault();
    refreshImgCaptcha();
});

function refreshImgCaptcha() {
    $('.image-captcha img').each(function (index, el) {
        commonFun.refreshCaptcha(this, '/register/user/image-captcha');
    });
}

refreshImgCaptcha();

$inputImgCaptcha.on('click',function () {

})
$fetchCaptcha.on('click',function () {
    getSmsCaptcha();
})
require.ensure(['publicJs/placeholder'], function (require) {
    require('publicJs/placeholder');
    $('input[type="text"],input[type="password"]', $(registerForm)).placeholder();
}, 'placeholder');
//显示服务协议
$('.show-agreement').on('click', function (event) {
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
//获取短信验证码
function getSmsCaptcha() {
    var captchaVal = $inputImgCaptcha.val();
    var mobileNum = $('#mobile').val();
    if(captchaVal.length !==5){
        layer.msg('图形验证码错误');
        return;
    }

    commonFun.useAjax({
        url: '/register/user/send-register-captcha',
        type: 'POST',
        dataType: 'json',
        data: {imageCaptcha: captchaVal, mobile: mobileNum,isVoice:false}
    },function(data) {
        console.log(data)
        if (data.data.status && !data.data.isRestricted) {

        commonFun.countDownLoan({
            btnDom:$fetchCaptcha
        })
            return;
        }
        if (!data.data.status && data.data.isRestricted) {
            layer.msg('短信发送频繁,请稍后再试');
        }

        if (!data.data.status && !data.data.isRestricted) {
            layer.msg('图形验证码错误');
        }
        refreshImgCaptcha();
    });
}

//用户注册表单校验
let validator = new ValidatorObj.ValidatorForm();
//验证码是否正确
validator.newStrategy(registerForm.captcha,'isCaptchaValid',function(errorMsg,showErrorAfter) {
    var getResult='',
        that=this,
        _arguments=arguments;

    var _phone = registerForm.mobile.value,
        _captcha=registerForm.captcha.value;


    //先判断手机号格式是否正确
    if(!/(^1[0-9]{10}$)/.test(_phone)) {
        return;
    }
    commonFun.useAjax({
        type:'GET',
        async: false,
        url:`/register/user/mobile/${_phone}/captcha/${_captcha}/verify`
    },function(response) {
        if(response.data.status) {
            // 如果为true说明验证码正确
            getResult='';
            ValidatorObj.isHaveError.no.apply(that,_arguments);
        }
        else {
            getResult=errorMsg;
            ValidatorObj.isHaveError.yes.apply(that,_arguments);
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
validator.add(registerForm.appCaptcha, [{
    strategy: 'isNonEmpty',
    errorMsg: '验证码不能为空'
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

for(let i=0,len=reInputs.length; i<len;i++) {
    globalFun.addEventHandler(reInputs[i],"keyup", "focusout", function() {
        let errorMsg=validator.start(this);
        isDisabledButton();
    })
}

function isDisabledButton() {
    let mobile=registerForm.mobile,
        password=registerForm.password,
        captcha=registerForm.captcha;

    //获取验证码点亮
    let isMobileValid=!globalFun.hasClass(mobile,'error') && mobile.value;
    let isPwdValid = !globalFun.hasClass(password,'error') && password.value;

    let isDisabledCaptcha = isMobileValid && isPwdValid;

    //通过获取验证码按钮来判断
    !isDisabledCaptcha && $registerSubmit.prop('disabled',true);

    let captchaValid = !$(captcha).hasClass('error') && captcha.value;

    let isDisabledSubmit= isMobileValid && isPwdValid && captchaValid  && $('#agreementInput').prop('checked');
    $registerSubmit.prop('disabled',!isDisabledSubmit);

}

//点击立即注册按钮
registerForm.onsubmit = function(event) {
    event.preventDefault();
    registerForm.submit();
}

