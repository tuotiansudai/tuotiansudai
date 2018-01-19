let commonFun = require('publicJs/commonFun');
let ValidatorObj = require('publicJs/validator');
let isSendingCaptcha = false;
let isVoice = false;
let password = '';
let retrieveForm = globalFun.$('#retrieveForm');  //找回密码的form
let inputPasswordForm=globalFun.$('#inputPasswordForm'); //找回密码的form

retrieveForm && forgetPassword();
inputPasswordForm && inputPassword();

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

function forgetPassword() {

    let formCaptcha = globalFun.$('#formCaptcha');
    let validator = new ValidatorObj.ValidatorForm();
    let $errorBox = $('.error-box', $(retrieveForm));
    let imageCaptcha = globalFun.$('#imageCaptcha');
    let captchaSrc = '/register/user/image-captcha';
    let $getCaptcha = $(retrieveForm).find('.get-captcha');
    let $registerSubmit=$('button[type="submit"]',$(retrieveForm));

    let reInputs = $('#retrieveForm').find('input[validate]');
    for(let i=0,len=reInputs.length; i<len;i++) {
        globalFun.addEventHandler(reInputs[i],"keyup",function () {
            validator.start(this);
            isDisabledButton();
        })
    };
    //刷新验证码
    $(imageCaptcha).on('click', function () {
        commonFun.refreshCaptcha(this, captchaSrc);
        retrieveForm.captcha.value = '';
    });

    //获取手机号
    let telephoneNum = localStorage.getItem('login_telephone') || '';
    $('.show-mobile-register').html(telephoneNum);
    $('.mobile').val(telephoneNum);

    let contentInput = (id,content,length) => {
        $(id).find('input').on('keyup',(e) => {
            if (!e.currentTarget.value.length) {
                $(id).find('.close_btn').hide();
            } else {
                $(id).find('.close_btn').show();
            }
        })
    };

    let clearInputOneVal = (id,className,btnClassName) => {
        $(id).find('.close_btn').on('click',() => {
            $(id).find(className).val('');
            $(id).find('.close_btn').hide();
            if (className != '.captcha') {
                $(btnClassName).prop('disabled',true);
            }
        })
    };

    contentInput('#formCaptcha');
    clearInputOneVal('#formCaptcha','.captcha');

    contentInput('#retrieveForm');
    clearInputOneVal('#retrieveForm','.short-message-captcha','.reset_next_step');

    contentInput('#inputPasswordForm');
    clearInputOneVal('#inputPasswordForm','.password','.confirm_reset');

    let entryEv = () => {
        $('.reset_one').show();
        $('.reset_two').hide();
    };

    let resetEv = () => {
        $('.reset_one').hide();
        $('.reset_two').show();
    };

    let hash_key = location.hash;

    switch (hash_key) {
        case '':
            entryEv();
            break;
        case '#reset_next':
            resetEv();
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

    $('.reset_next_step').on('click',() => {
        pushHistory('#reset_next');
        localStorage.setItem('captcha',retrieveForm.captcha.value);
    });

//获取验证码
    (function () {
        let formCaptcha = globalFun.$('#formCaptcha');
        function sendCaptcha() {
            layer.closeAll();
            isSendingCaptcha = true;
            let ajaxOption = {
                url: `/mobile-retrieve-password/mobile/${localStorage.getItem('login_telephone')}/imageCaptcha/${$('.captcha').val()}/send-mobile-captcha/${isVoice}`,
                type: 'GET',
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
                    retrieveForm.captcha.value = '';
                    layer.msg('短信发送频繁，请稍后再试');
                    isSendingCaptcha = false;
                } else if (!data.status && !data.isRestricted) {
                    commonFun.refreshCaptcha(imageCaptcha, captchaSrc);
                    retrieveForm.captcha.value = '';
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
                    commonFun.CommonLayerTip({
                        btn: ['知道了','不发送'],
                        area:['280px', '160px'],
                        content: $('#freeSuccess'),
                    },() => {
                        sendCaptcha();
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
    validator.newStrategy(retrieveForm.captcha, 'isCaptchaValid', function (errorMsg, showErrorAfter) {
        var getResult = '',
            that = this,
            _arguments = arguments;

        var _captcha = retrieveForm.captcha.value;

        commonFun.useAjax({
            type: 'GET',
            async: false,
            url: `/mobile-retrieve-password/mobile/${localStorage.getItem('login_telephone')}/captcha/${_captcha}/verify`
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
    });

//用来判断获取验证码和立即注册按钮 是否可点击
    function isDisabledButton() {
        let captcha=retrieveForm.captcha;  //短信验证码
        let captchaValid = !$(captcha).hasClass('error') && captcha.value;
        $('.reset_next_step').prop('disabled', !captchaValid);
    }

//重置密码最后一步
    function isDisabledResetBtn(e) {
        let value = e.currentTarget.value;
        return /^(?=.*[^\d])(.{6,20})$/.test(value);
    }

    $("[name='password']").on('keyup',(e) => {
        password = e.currentTarget.value;
        $('.confirm_reset').prop('disabled',!isDisabledResetBtn(e));
    });

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

    $('.confirm_reset').on('click',function(event) {
        event.preventDefault();
        let data = {
            mobile: localStorage.getItem('login_telephone'),
            captcha: localStorage.getItem('captcha'),
            password: password,
            repeatPassword: password
        };

        let errorMsg = validatorPass.start(inputPasswordForm.password);
        if(errorMsg) {
            layer.msg(errorMsg);
            return;
        }
        commonFun.useAjax({
            type: 'POST',
            async: false,
            url: '/mobile-retrieve-password/m',
            data: data,
        }, function (response) {
            if (response.data.status) {
                location.href = '/m';
            }
            else {
                layer.msg(response.data.message);
            }
        });

    })
}

// 密码明文
$('.see_password').on('click',() => {
    let input = $('.see_password').siblings('input');
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

$('.show-agreement').on('click',() => {
    location.href = '/m/'
})