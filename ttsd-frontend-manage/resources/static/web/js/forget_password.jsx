require('webStyle/forget_password.scss');
let commonFun=require('publicJs/commonFun');
let ValidatorForm= require('publicJs/validator');
let retrieveForm=globalFun.$('#retrieveForm'); //找回密码的form
let inputPasswordForm=globalFun.$('#inputPasswordForm'); //找回密码的form

retrieveForm && forgetPassword();

inputPasswordForm && inputPassword();

function forgetPassword() {
    let errorDom=$(retrieveForm).find('.error-box');
    let $fetchCaptcha=$('#fetchCaptcha');
    //点击获取验证码按钮
    require.ensure(['publicJs/fetch_captcha'], function(require){
        let fetchCaptchaFun=require('publicJs/fetch_captcha');
        let fetchCaptchaRegister=new fetchCaptchaFun('retrieveForm','retrieve');
        fetchCaptchaRegister.init();
    },'fetchCaptchaRetrieve');

    //忘记密码表单校验
    let validator = new ValidatorForm();

    validator.add(retrieveForm.mobile, [{
        strategy: 'isNonEmpty',
        errorMsg: '手机号不能为空',
    }, {
        strategy: 'isMobile',
        errorMsg: '手机号格式不正确'
    },{
        strategy: 'isMobileRetrieveExist',
        errorMsg: '手机号不存在'
    }]);

    validator.add(retrieveForm.captcha, [{
        strategy: 'isNonEmpty',
        errorMsg: '验证码不能为空'
    },{
        strategy: 'isNumber:6',
        errorMsg: '验证码为6位数字'
    },{
        strategy: 'isCaptchaVerify',
        errorMsg: '验证码不正确'
    }]);

    let reInputs=$(retrieveForm).find('input:text,input:password');

    reInputs=Array.from(reInputs);
    for (let el of reInputs) {
        el.addEventListener("blur", function() {
            let errorMsg = validator.start(this);
            if(errorMsg) {
                errorDom.text(errorMsg).css('visibility','visible');
            }
            else {
                errorDom.text('').css('visibility','hidden');
            }
            isDisabledButton();
        })
    }

//用来判断获取验证码是否可点击
    function isDisabledButton() {
        let mobile=retrieveForm.mobile;
        let isMobileValid=!globalFun.hasClass(mobile,'error') && mobile.value;
        //按钮上有样式名count-downing，说明正在倒计时
        if ($fetchCaptcha.hasClass('count-downing')) {
            $fetchCaptcha.prop('disabled',true);
        }
        else {
            $fetchCaptcha.prop('disabled',!isMobileValid);
        }
    }
//获取密码
    retrieveForm.onsubmit = function(event) {
        event.preventDefault();
        let errorMsg;
        for(let i=0,len=reInputs.length;i<len;i++) {
            errorMsg = validator.start(reInputs[i]);
            if(errorMsg) {
                errorDom.text(errorMsg).css('visibility','visible');
                return;
            }
        }
        if (!errorMsg) {
            window.location.href = '/mobile-retrieve-password/mobile/'+retrieveForm.mobile.value+'/captcha/'+retrieveForm.captcha.value+'/new-password-page';
        }
    }
}


function inputPassword() {
    let errorDom=$(inputPasswordForm).find('.error-box');
    //验证2次输入的密码一样
    $('.password-again',$(inputPasswordForm)).on('keyup',function(event) {
        let password=inputPasswordForm.password.value;
        let thisValue=event.target.value;
        if(password!=thisValue) {
            errorDom.text('2次输入的密码不一致');
            $(this).addClass('error');
        }
        else {
            errorDom.text('');
            $(this).removeClass('error');
        }
    });

    //忘记密码表单校验
    let validatorPass = new ValidatorForm();
    validatorPass.add(inputPasswordForm.password, [{
        strategy: 'isNonEmpty',
        errorMsg: '密码不能为空'
    }, {
        strategy: 'checkPassword',
        errorMsg: '密码为6位至20位，不能全是数字'
    }]);

    let passInputs=$(inputPasswordForm).find('input:visible');
    Array.prototype.forEach.call(passInputs,function(el) {
        el.addEventListener("blur", function(event) {
            event.preventDefault();
            let errorMsg = validatorPass.start(this);
            if(errorMsg) {
                errorDom.text(errorMsg).css('visibility','visible');
            }
            else {
                errorDom.text('').css('visibility','hidden');
            }
        })
    });

    inputPasswordForm.onsubmit = function(event) {
        event.preventDefault();
        let errorMsg;
        for(let i=0,len=passInputs.length;i<len;i++) {
            errorMsg = validatorPass.start(passInputs[i]);
            if(errorMsg) {
                errorDom.text(errorMsg).css('visibility','visible');
                break;
            }
        }
        if (!errorMsg) {
            inputPasswordForm.submit();
        }
    }

}



