// import {useAjax,refreshCaptcha} from 'publicJs/common';
// import {ValidatorForm} from 'publicJs/validator';

require('webStyle/login.scss');
let ValidatorForm= require('publicJs/validator');
let commonFun= require('publicJs/commonFun');

let loginForm=globalFun.$('#formLogin');
let loginSubmit=$(loginForm).find('.login-submit');
let imageCaptcha=globalFun.$('#imageCaptcha');

commonFun.refreshCaptcha(imageCaptcha,'/login/captcha?');
//刷新验证码
$('#imageCaptcha').on('click',function() {
    commonFun.refreshCaptcha(this,'/login/captcha?');
    loginForm.captcha.value='';
});

//表单校验
let errorDom=$(loginForm).find('.error-box');
let validator = new ValidatorForm();
validator.add(loginForm.username, [{
    strategy: 'isNonEmpty',
    errorMsg: '用户名不能为空'
}]);
validator.add(loginForm.password, [{
    strategy: 'isNonEmpty',
    errorMsg: '密码不能为空'
}, {
    strategy: 'checkPassword',
    errorMsg: '密码为6位至20位，不能全是数字'
}]);
validator.add(loginForm.captcha, [{
    strategy: 'isNonEmpty',
    errorMsg: '验证码不能为空'
}]);

let loginInputs=$(loginForm).find('input:visible');
Array.prototype.forEach.call(loginInputs,function(el) {
    el.addEventListener("blur", function(event) {
        event.preventDefault();
        let errorMsg = validator.start(this);
        if(errorMsg) {
            errorDom.text(errorMsg);
        }
        else {
            errorDom.text('');
        }
    })
});

loginForm.onsubmit = function(event) {
    event.preventDefault();
    let errorMsg;

    for(let i=0,len=loginInputs.length;i<len;i++) {
        errorMsg = validator.start(loginInputs[i]);
        if(errorMsg) {
            errorDom.text(errorMsg);
            break;
        }
    }
    if (!errorMsg) {
        commonFun.useAjax({
            url:"/login",
            type:'POST',
            data:$(loginForm).serialize(),
            beforeSend:function() {
                loginSubmit.prop('disabled',true);
            }
        },function(data) {
            let redirectUrl=$(loginForm).data('redirect-url');
            if (data.status) {
                 //用户角色里是否包含USER角色
                let hasUserRole=data.roles.includes('USER');
                location.href = hasUserRole ? redirectUrl : "/register/account";
            } else {
                let imageCaptcha=globalFun.$('#imageCaptcha');
                commonFun.refreshCaptcha(imageCaptcha,'/login/captcha?');
                loginSubmit.removeClass('loading');
                errorDom.text(data.message);
            }
        }
        );
    }
};
