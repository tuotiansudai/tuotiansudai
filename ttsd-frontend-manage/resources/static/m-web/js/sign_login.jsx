let ValidatorObj = require('publicJs/validator');
let commonFun = require('publicJs/commonFun');

let formLogin = globalFun.$('#formLogin');
//用户注册表单校验
let validator = new ValidatorObj.ValidatorForm();
let $errorBox = $('.error-box', $(formLogin));
let imageCaptcha = globalFun.$('#imageCaptcha');
let loginSubmit = $('button[type="submit"]', $(formLogin));

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
    strategy: 'equalLength:5',
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
        let hasValid = loginInputs.filter(function(key,option) {
            return $(option).hasClass('valid');
        });
        let isAllValid = hasValid.length == loginInputs.length;
        $('button',$(formLogin)).prop('disabled',!isAllValid);
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
            loginSubmit.prop('disabled', false);
            if (data.status) {
                location.href = '/bind-success?redirect=' + $('#formLogin').data('redirect');
            } else {
                commonFun.refreshCaptcha(imageCaptcha, '/login/captcha');
                $errorBox.text(data.message);
            }
        }
    );
};

formLogin.onsubmit = function (event) {
    event.preventDefault();

    //提交之前得先执行validateLogin验证表单是否通过验证
    formSubmit.before(validateLogin)();
};

