require('webStyle/login.scss');
let ValidatorObj= require('publicJs/validator');
let commonFun= require('publicJs/commonFun');

let loginForm=globalFun.$('#formLogin');
let loginSubmit=$(loginForm).find('.login-submit');
let imageCaptcha=globalFun.$('#imageCaptcha');

var func = function(param) {
    console.log(2);
    console.log(param);
};
var funcInit = func.before(function(param) {
    console.log(param);
    param.b = 'b';
    console.log(1);

}).after(function() {

    console.log(3);
});

funcInit({a: 'a'});

commonFun.refreshCaptcha(imageCaptcha,'/login/captcha?');
//刷新验证码
$('#imageCaptcha').on('click',function() {
    commonFun.refreshCaptcha(this,'/login/captcha?');
    loginForm.captcha.value='';
});

//表单校验
let errorDom=$(loginForm).find('.error-box');
let validator = new ValidatorObj.ValidatorForm();
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
    globalFun.addEventHandler(el,'blur',function() {
        let errorMsg = validator.start(this);
        if(errorMsg) {
            errorDom.text(errorMsg).css('visibility','visible');
        }
        else {
            errorDom.text('').css('visibility','hidden');
        }
    })
});

let validateLogin = function() {
    let errorMsg;
    for(let i=0,len=loginInputs.length;i<len;i++) {
        errorMsg = validator.start(loginInputs[i]);
        if(errorMsg) {
            errorDom.text(errorMsg).css('visibility','visible');
            break;
        }
    }
    return errorMsg?true:false;
}

var formSubmit =function() {

    loginSubmit.addClass('loading').prop('disabled',true);
    commonFun.useAjax({
            url:"/login",
            type:'POST',
            data:$(loginForm).serialize()
        },function(data) {
            loginSubmit.removeClass('loading').prop('disabled',false);
            let redirectUrl=$(loginForm).data('redirect-url');
            if (data.status) {
                //用户角色里是否包含USER角色
                let hasUserRole = _.contains(data.roles, 'USER');
                location.href = hasUserRole ? redirectUrl : "/register/account";
            } else {
                let imageCaptcha=globalFun.$('#imageCaptcha');
                commonFun.refreshCaptcha(imageCaptcha,'/login/captcha?');
                errorDom.text(data.message).css('visibility','visible');
            }
        }
    );
}
formSubmit = formSubmit.before(validateLogin);

loginForm.onsubmit = function(event) {
    event.preventDefault();
    formSubmit();

};
