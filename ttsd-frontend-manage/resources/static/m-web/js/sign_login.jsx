let ValidatorObj = require('publicJs/validator');
let commonFun = require('publicJs/commonFun');
let isVoice = false;
let formLogin = globalFun.$('#formLogin');
let EntryPointFormPageOne = globalFun.$('#EntryPointFormPageOne');
//用户注册表单校验
let validator = new ValidatorObj.ValidatorForm();
let $errorBox = $('.error-box', $(formLogin));
let imageCaptcha = globalFun.$('#imageCaptcha');
let loginSubmit = $('button[type="submit"]', $(formLogin));
let telephoneNum = '';

let imageCaptchaPageOne = globalFun.$('#imageCaptchaPageOne');

(function (doc, win) {
    let docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            let clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            let fSize = 20 * (clientWidth / 375);
            fSize > 40 && (fSize = 39.36);
            docEl.style.fontSize = fSize + 'px';
        };

    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

$(function () {
    let bool = false;
    setTimeout(function () {
        bool = true;
    }, 300);
    window.addEventListener("popstate", function (e) {
        if (bool) hashFun();
    }, false);
});

let backStatus = () => {  // 返回此页面判断手机号长度，确定是否需要点亮按钮
    if ($('.telephoneInput').val().length == 11) {
        $('.show-mobile-entry').html($('.telephoneInput').val());
        $('.show-mobile-entry').show();
        telephoneNum = $('.telephoneInput').val();
        $('.step_one').prop('disabled', false);
    }
};
backStatus();
let entryEv = () => {
    $('.entry_container').show();
    $('.login_container').hide();
};

commonFun.refreshCaptcha(imageCaptchaPageOne, '/login/captcha');
//刷新验证码
$('#imageCaptchaPageOne').on('click', function () {
    commonFun.refreshCaptcha(this, '/login/captcha');
    $('.captchaPageOne').val('');
});
let loginEv = () => {
    $('.entry_container').hide();
    $('.login_container').show();
    let telephoneNum = localStorage.getItem('login_telephone') || '';
    $('.show-mobile-login').html(telephoneNum);
    $('#username').val(telephoneNum);
};

let registerEv = () => {
    $('.entry_container').hide();
    $('.login_container').hide();
    let telephoneNum = localStorage.getItem('login_telephone') || '';
    $('.show-mobile-register').html(telephoneNum);
};

function hashFun() {
    let hash_key = location.hash;

    switch (hash_key) {
        case '':
            entryEv();
            break;
        case '#login':
            loginEv();
            break;
        case '#register-one':
            registerEv();
            break;
        default:
            entryEv();
            break;
    }
};
hashFun();

let pushHistory = (url) => {
    let state = {title: "title", url: url};
    window.history.pushState(state, "title", url);
    // location.reload();
};

let contentInput = (id, content, length) => {
    $(id).find('input').on('keyup', (e) => {
        if (!e.currentTarget.value) {
            $(id).find('.close_btn').hide();
            $(id).find(content).html('');
            $(id).find(content).hide();
        } else {
            $(id).find('.close_btn').show();
            $(id).find(content).html(e.currentTarget.value);
            $(id).find(content).show();
            if (!!length) {
                if (e.currentTarget.value.length === length) {
                    telephoneNum = e.currentTarget.value;
                    $('.step_one').attr("disabled", false);
                } else {
                    $('.step_one').attr("disabled", true);
                }
            }
        }
    })
};

let clearInputOneVal = (id, content, submitBtn) => {
    $(id).find('.close_btn').on('click', () => {
        $(id).find('input').val('');
        $(id).find(content).html('');
        $(id).find(content).hide();
        $(id).find('.close_btn').hide();
        $(submitBtn).attr("disabled", true);
    })
};

let clearInputTwoVal = (id) => {
    $(id).find('.close_btn').on('click', () => {
        $(id).find('input').val('');
        $(id).find('.close_btn').hide();
        $('.step_two').attr("disabled", true);
    })
};

let stepOneEv = () => {
    $('.step_one').on('click', (e) => {
        e.preventDefault();
        if (!/(^1[0-9]{10}$)/.test(telephoneNum)) { // 入口手机号码校验
            layer.msg('手机号格式不正确');
            return;
        }
        if($('.captchaPageOne').val()!==''){
            layer.msg('验证码不能为空');
            return;
        }
        if($('.captchaPageOne').val().length <5){
            layer.msg('验证码为5位数');
            return;
        }
        var captcha = $('#captchaPageOneInput').val();

        localStorage.setItem('login_telephone', telephoneNum);
        commonFun.useAjax({
            type: 'POST',
            async: false,
            url: '/register/user/mobile/' + telephoneNum + '/is-exist?captcha='+captcha
        }, function (response) {
            if (response.data.status) {
                pushHistory('#login'); // 登录
                hashFun();
            }
            else {
                location.href = '/m/register/user'; // 注册
            }
        });
    })
};

let seePassword = () => {
    $('.see_password').on('click', () => {
        let input = $('.see_password').siblings('input');
        if (input.attr('type') == 'text') {
            input.attr('type', 'password');
            $('.see_password').removeClass('open_eye');
        }
        else if (input.attr('type') == 'password') {
            input.attr('type', 'text');
            $('.see_password').addClass('open_eye');
        }
    })
};

// 登录注册第一步入口
contentInput('#EntryPointForm', '.show-mobile-entry', 11);
clearInputOneVal('#EntryPointForm', '.show-mobile-entry', '.step_one');
stepOneEv();

// 登录
contentInput('.captcha_container', '.show-mobile-login');
contentInput('.password_container', '.show-mobile-login');
clearInputTwoVal('.captcha_container');
clearInputTwoVal('.password_container');

// $('.go-back-container').on('click',() => {
//     history.go(-1);
// });

seePassword();

commonFun.refreshCaptcha(imageCaptcha, '/login/captcha');
//刷新验证码
$('#imageCaptcha').on('click', function () {
    commonFun.refreshCaptcha(this, '/login/captcha');
    formLogin.captcha.value = '';
});




validator.add(formLogin.password, [{
    strategy: 'isNonEmpty',
    errorMsg: '密码不能为空'
}, {
    strategy: 'checkPassword',
    errorMsg: '密码为6位至20位，不能全是数字'
}]);

validator.add(formLogin.captcha, [{
    strategy: 'isNonEmpty',
    errorMsg: '验证码不能为空'
}, {
    strategy: 'isNumber:5',
    errorMsg: '验证码必须为5位数字'
}]);

validator.add(EntryPointFormPageOne.captchaPageOne, [{
    strategy: 'isNonEmpty',
    errorMsg: '验证码不能为空'
}, {
    strategy: 'isNumber:5',
    errorMsg: '验证码必须为5位数字'
}]);

let loginInputs = $(formLogin).find('input[validate]');
Array.prototype.forEach.call(loginInputs, function (el) {
    globalFun.addEventHandler(el, 'keyup', function () {
        $errorBox.text('');
        let errorMsg = validator.start(this);
        if (errorMsg) {
            $errorBox.text(errorMsg);
        }
        let hasValid = loginInputs.filter(function (key, option) {
            return $(option).hasClass('valid');
        });
        let isAllValid = hasValid.length == loginInputs.length;
        $('button', $(formLogin)).prop('disabled', !isAllValid);
    })
});

let loginInputsPageOne = $(EntryPointFormPageOne).find('input[validate]');
Array.prototype.forEach.call(loginInputsPageOne, function (el) {
    globalFun.addEventHandler(el, 'keyup', function () {
        $errorBox.text('');
        let errorMsg = validator.start(this);
        if (errorMsg) {
            $errorBox.text(errorMsg);
        }
        let hasValid = loginInputsPageOne.filter(function (key, option) {
            return $(option).hasClass('valid');
        });
        let isAllValid = hasValid.length == loginInputsPageOne.length;
        $('button', $(loginInputsPageOne)).prop('disabled', !isAllValid);
    })
});

//提交表单前验证表单函数
let validateLogin = function () {
    let errorMsg;
    for (let i = 0, len = loginInputs.length; i < len; i++) {
        errorMsg = validator.start(loginInputs[i]);
        if (errorMsg) {
            $errorBox.text(errorMsg);
            break;
        }
    }
    return !errorMsg;
    //返回false代表表单验证没有通过
};

//login表单提交函数
let formSubmit = function () {
    loginSubmit.prop('disabled', true);
    commonFun.useAjax({
            url: "/login",
            type: 'POST',
            data: $(formLogin).serialize()
        }, function (data) {
            if (data.status) {
                if ($('#redirectBox').val()!=='/') {
                    location.href = $('#redirectBox').val();
                }else {
                    location.href = '/m/';
                }

            } else {
                loginSubmit.prop('disabled', false);
                commonFun.refreshCaptcha(imageCaptcha, '/login/captcha');
                $errorBox.text(data.message);
            }
        }, function () {
            loginSubmit.prop('disabled', false);
        }
    );
};

$('.step_two').on('click', function (event) {
    event.preventDefault();
    //提交之前得先执行validateLogin验证表单是否通过验证
    formSubmit.before(validateLogin)();
});

$('#goBack_login').click(function () {
    history.back();
})



