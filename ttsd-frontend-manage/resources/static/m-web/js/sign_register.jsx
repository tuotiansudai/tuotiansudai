let commonFun = require('publicJs/commonFun');
let ValidatorObj = require('publicJs/validator');
let isVoice = false;
let formRegister = globalFun.$('#formRegister');

let validator = new ValidatorObj.ValidatorForm();
let $errorBox = $('.error-box', $(formRegister));
let imageCaptcha = globalFun.$('#imageCaptcha');
let captchaSrc = '/register/user/image-captcha';
let $getCaptcha = $('.get-captcha');
let $registerSubmit=$('button[type="submit"]',$(formRegister));
let referrerValidBool=true;
let isSendingCaptcha = false;
let $referrer=$('input.referrer', $(formRegister));

(function (doc, win) {
    let docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            let clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            let fSize = 20 * (clientWidth /375);
            fSize > 40 && (fSize = 39.36);
            docEl.style.fontSize = fSize + 'px';
        };

    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

$(function () {
    let bool=false;
    setTimeout(function(){ bool=true;},300);
    window.addEventListener("popstate", function(e) {
        if(bool) location.reload();
    },false);
});

let contentInput = (id,content,length) => {
    $(id).find('input').on('keyup',(e) => {
        if (!e.currentTarget.value.length) {
            $(id).find('.close_btn').hide();
        } else {
            $(id).find('.close_btn').show();
        }
    })
};

let clearInputOneVal = (id,className) => {
    $(id).find('.close_btn').on('click',() => {
        $(id).find(className).val('');
        $(id).find('.close_btn').hide();
        if (className === '.short-message-captcha') {
            $('.register_next_step').prop('disabled',true);
        }
    })
};

contentInput('#formCaptcha');
clearInputOneVal('#formCaptcha','.captcha');

contentInput('#formRegister');
clearInputOneVal('#formRegister','.short-message-captcha');
clearInputOneVal('#formRegister','.short-message-captcha1');

let entryEv = () => {
    $('.form-captcha').show();
    $('.step_container').show();
    $('.show-mobile-register').show();
    $('.next_step_container').hide();
};

let registerEv = () => {
    $('.form-captcha').hide();
    $('.show-mobile-register').hide();
    $('.step_container').hide();
    $('.next_step_container').show();
};

let hash_key = location.hash;

switch (hash_key) {
    case '':
        entryEv();
        break;
    case '#register_next':
        registerEv();
        break;
    default:
        entryEv();
        break;
}

let pushHistory = (url) => {
    let state = {title: "title", url: url};
    window.history.pushState(state, "title", url);
    location.reload();
};

//获取手机号
let telephoneNum = localStorage.getItem('login_telephone') || '';
$('.show-mobile-register').html(telephoneNum);
$('.mobile').val(telephoneNum);

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
        shadeClose: false,
        shade: 0,
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
        $('.shade_mine').hide();
        layer.closeAll();
        isSendingCaptcha = true;
        let ajaxOption = {
            url: '/register/user/send-register-captcha',
            type: 'POST',
            data: $(formCaptcha).serialize()+'&voice='+isVoice
        };
        commonFun.useAjax(ajaxOption, function (responseData) {
            $getCaptcha.prop('disabled', false);

            let data = responseData.data;
            if (data.status && !data.isRestricted) {
                if (!isVoice) {
                    $('.get-captcha-icon').addClass('message_send');
                }
                else {
                    $('.get-captcha-icon').removeClass('voice_get');
                    $('.get-captcha-icon').addClass('voice_send');
                }
                localStorage.setItem('imageCaptcha',formCaptcha.imageCaptcha.value);
                //获取手机验证码成功，，并开始倒计时
                commonFun.countDownLoan({
                    btnDom: $('.get-captcha-text'),
                    isAfterText: '获取语音验证码',
                    textCounting: 's后重新获取'
                },function() {
                    //倒计时结束后刷新验证码
                    commonFun.refreshCaptcha(imageCaptcha, captchaSrc);
                    $getCaptcha.prop('disabled', false);
                    isSendingCaptcha = false;
                    if (!isVoice) {
                        $('.get-captcha-icon').removeClass('message_send');
                    }
                    else {
                        $('.get-captcha-icon').removeClass('voice_send');
                    }
                    $('.get-captcha-icon').addClass('voice_get');
                    isVoice = true;
                });
            } else if (!data.status && data.isRestricted) {
                commonFun.refreshCaptcha(imageCaptcha, captchaSrc);
                formRegister.captcha.value = '';
                layer.msg('短信发送频繁，请稍后再试');
                isSendingCaptcha = false;

            } else if (!data.status && !data.isRestricted) {
                commonFun.refreshCaptcha(imageCaptcha, captchaSrc);
                formRegister.captcha.value = '';
                layer.msg('图形验证码不正确');
                isSendingCaptcha = false;
            }
        });
    }

    $getCaptcha.on('click', function () {
        $getCaptcha.prop('disabled', true);
        let imageCaptcha = formCaptcha.imageCaptcha.value;

        if (/\d{5}/.test(imageCaptcha)) {
            if (isSendingCaptcha) return;
            if (isVoice) {
                $('.shade_mine').show(); // hack ios shade
                commonFun.CommonLayerTip({
                    btn: ['知道了','不发送'],
                    area:['280px', '160px'],
                    content: $('#freeSuccess'),
                    shade: false  // hack ios shade
                },() => {
                    sendCaptcha();
                },() => {
                    $('.shade_mine').hide(); // hack ios shade
                });
            } else {
                sendCaptcha();
            }
        } else {
            layer.msg('图形验证码不正确');
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
    let getCaptcha = isMobileValid;

    //按钮上有样式名count-downing，说明正在倒计时
    if ($getCaptcha.hasClass('count-downing')) {
        $getCaptcha.prop('disabled',true);
    }
    else {
        $getCaptcha.prop('disabled',!getCaptcha);
    }
    //给验证码弹框中的mobile隐藏域赋值
    isDisabledCaptcha && (globalFun.$('#formCaptcha').mobile.value = mobile.value);
    //通过获取验证码按钮来判断
    !isDisabledCaptcha && $registerSubmit.prop('disabled',true);

    let captchaValid = !$(captcha).hasClass('error') && captcha.value;
    $('.register_next_step').prop('disabled', !captchaValid);

    if($(referrer).is(':hidden')) {
        referrerValidBool=true;
    }

    let isDisabledSubmit= isMobileValid && isPwdValid  && referrerValidBool;
    $registerSubmit.prop('disabled',!isDisabledSubmit);

}

//点击立即注册按钮
$('#submitBtn').on('click',function (event) {
    event.preventDefault();
    formRegister.captcha.value = localStorage.getItem('captcha');
    for (let i = 0, len = reInputs.length; i < len; i++) {
        let errorMsg = validator.start(reInputs[i]);
        if (errorMsg) {
            layer.msg(errorMsg);
            return;
        }
    };
    let data = {
        mobile: localStorage.getItem('login_telephone'),
        password: formRegister.password.value,
        imageCaptcha: localStorage.getItem('imageCaptcha'),
        captcha: localStorage.getItem('captcha'),
        referrer: formRegister.referrer.value,
        agreement: 'on'
    };
    commonFun.useAjax({
        url:'/register/user/m',
        type:'POST',
        data: data,
        async: false,
    },function(response) {
        if(response.success) {
          location.href = '/m/register/success'
        }else {
            layer.msg(response.referrerMobileError)
        }
    });
});

$('.register_next_step').on('click',() => {
    pushHistory('#register_next');
    localStorage.setItem('captcha',formRegister.captcha.value);
});


// 密码明文
$('.see_password').on('click',() => {
    let input = $('.set-password');
    if (input.attr('type') == 'text') {
        input.attr('type','password');
        $('.see_password').removeClass('open_eye');
    }
    else if (input.attr('type') == 'password') {
        input.attr('type','text');
        $('.see_password').addClass('open_eye');
    }
});

$('.go-back-container').on('click',() => {
    history.go(-1);
});

