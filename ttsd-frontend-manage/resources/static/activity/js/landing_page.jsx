require("activityStyle/landing_page.scss");

let commonFun= require('publicJs/commonFun');
require('publicJs/placeholder');
let ValidatorObj= require('publicJs/validator');

let $landingContainerBox = $('.landingContainerBox'),
    $landingContainer = $('.landing-container'),
    $btnCoupon = $('#btn-get-coupon', $landingContainer),
    browser = globalFun.browserRedirect();
let urlObj = globalFun.parseURL(location.href);
let $btnChangeImgCode=$('.img-change',$landingContainer);
let registerForm=globalFun.$('#registerUserForm');
let $fetchCaptcha=$('#fetchCaptcha');
let $registerSubmit=$('input[type="submit"]',$(registerForm));
let $voiceCaptcha = $('#voice_captcha');
let $voiceBtn = $('#voice_btn',$voiceCaptcha);


require.ensure(['publicJs/placeholder'], function(require){
    require('publicJs/placeholder');
    $('input[type="text"],input[type="password"]',$(registerForm)).placeholder();
},'placeholder');


//点击立即注册领取
$btnCoupon.on('click', function (event) {
    event.preventDefault();
    if (urlObj.params.source == 'app') {
        window.location.href = "/register/user";
    } else {
        if (browser=='mobile') {
            $('body,html').animate({scrollTop: $('.landingContainerBox').height()}, 'fast');
        } else {
            $('body,html').animate({scrollTop: 0}, 'fast');
        }
    }
});

//显示服务协议
$('.show-agreement').on('click', function (event) {
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
//判断有无推荐人
function showReferrerInfoIfNeeded() {
    var referNum = urlObj.params.referrer;

    if (referNum) {
        //有推荐人
        var mobileNum = commonFun.decrypt.uncompile(referNum);
        $('input[name="referrer"]', $landingContainerBox).val(mobileNum);
        //通过手机号得到用户名
        commonFun.useAjax({
            type:'GET',
            dataType: 'json',
            url:"/activity/get-realRealName?mobile=" + mobileNum
        },function(data) {
            //姓名的第一个字母用*替换
            $('.refer-name', $landingContainerBox).text(data);
        });
    }
    else {
        //无推荐人
        $('.refer-person-info', $landingContainerBox).hide();
    }
    isDisabledButton();
}
showReferrerInfoIfNeeded();

//刷新验证码
$btnChangeImgCode.on('click', function (event) {
    event.preventDefault();
    refreshImgCaptcha();
});

function refreshImgCaptcha() {
    $('.image-captcha img').each(function (index, el) {
        commonFun.refreshCaptcha(this,'/register/user/image-captcha');
    });
}
refreshImgCaptcha();

//phone focusout
$('#appCaptcha').on('focusout', function (event) {
    event.preventDefault();
    if ($('#mobile').val() != '' && /0?(13|14|15|18)[0-9]{9}/.test($('#mobile').val()) && $('#appCaptcha').val() != '') {
        $fetchCaptcha.prop('disabled', false);
    } else {
        $fetchCaptcha.prop('disabled', true);
    }
});
let isVoice = false;
//获取短信验证码
$fetchCaptcha.on('click', function(event) {
    isVoice = false;
    event.preventDefault();
    getSmsCaptcha();
});
$voiceBtn.on('click', function(event) {
    isVoice = true;
    event.preventDefault();
    getSmsCaptcha();
});
function getSmsCaptcha() {
    var captchaVal = $('#appCaptcha').val();
    var mobileNum = $('#mobile').val();

    commonFun.useAjax({
        url: '/register/user/send-register-captcha',
        type: 'POST',
        dataType: 'json',
        data: {imageCaptcha: captchaVal, mobile: mobileNum,isVoice:isVoice}
    },function(data) {
        var countdown = 60;
        console.log(isVoice)
        if (data.data.status && !data.data.isRestricted) {
            var timer = setInterval(function () {
                $fetchCaptcha.prop('disabled', true).text(countdown + '秒后重发');
                $voiceCaptcha.hide();
                countdown--;
                if (countdown == 0) {
                    clearInterval(timer);
                    countdown = 60;
                    $fetchCaptcha.prop('disabled', false).text('重新发送');
                    $voiceCaptcha.show();
                }
            }, 1000);
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

//用来判断获取验证码和立即注册按钮 是否可点击
//表单验证通过会
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













