import 'publicJs/underscore';
import {useAjax,refreshCaptcha} from 'publicJs/common';
import {ValidatorForm} from 'publicJs/validator';
import 'webStyle/login.scss';
console.log('popp');

let loginForm=globalFun.$('#formLogin');
let loginSubmit=$(loginForm).find('.login-submit');
let imageCaptcha=globalFun.$('#imageCaptcha');

refreshCaptcha(imageCaptcha,'/login/captcha?');
//刷新验证码
$('#imageCaptcha').on('click',function() {
    refreshCaptcha(this,'/login/captcha?');
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
        useAjax({
            url:"/login",
            type:'POST',
            data:$(loginForm).serialize(),
            beforeSend:function() {
                loginSubmit.prop('disabled',true);
            }
        },function(data) {
            let redirectUrl=$(loginForm).data('redirect-url');
            if (data.status) {
                location.href = _.difference(data.roles, ['USER']).length > 0 ? redirectUrl : "/register/account";
            } else {
                let imageCaptcha=globalFun.$('#imageCaptcha');
                refreshCaptcha(imageCaptcha,'/login/captcha?');
                loginSubmit.removeClass('loading');
                errorDom.text(data.message);
            }

            if(responseData.status){
                location.href = responseData.successRedirectUrl;
            }else{
                errorDom.text(responseData.message);
                $('#imageCaptcha').trigger('click');
                $(loginForm).find('.login-submit').prop('disabled',false);
            }
        }
        );

    }
};

// require(['jquery', 'underscore', 'jquery.ajax.extension', 'jquery.validate', 'jquery.form'], function ($, _) {
//
//     (function () {
//         var $loginContainer = $('#loginContainer');
//         var loginFormElement = $('.form-login', $loginContainer),
//             loginSubmitElement = $('.login-submit', loginFormElement),
//             captchaElement = $('.captcha', loginFormElement),
//             errorElement = $('.error', loginFormElement),
//             imageCaptchaElement = $('.image-captcha img', loginFormElement),
//             formCheckValid = true; //检查form表单验证是否全部通过
//
//         // 刷新验证码
//         function refreshCaptcha() {
//             captchaElement.val('');
//             imageCaptchaElement.attr('src', '/login/captcha?' + new Date().getTime().toString());
//         }
//
//         //表单验证
//         function checkLogin(DomElement) {
//             DomElement.each(function (key, option) {
//                 var name = option.name,
//                     value = $.trim(option.value),
//                     errorMsg;
//                 formCheckValid = true;
//                 switch (name) {
//                     case 'username':
//                         if (_.isEmpty(value)) {
//                             errorMsg = '用户名不能为空';
//                             formCheckValid = false;
//                         }
//                         else if(value.length<5){
//                             errorMsg = '用户名输入太短';
//                             formCheckValid = false;
//                         }
//                         break;
//                     case 'password':
//                         if (_.isEmpty(value)) {
//                             errorMsg = '密码不能为空';
//                             formCheckValid = false;
//                         } else if (!/^(?=.*[^\d])(.{6,20})$/.test(value)) {
//                             errorMsg = '密码为6位至20位，不能全是数字';
//                             formCheckValid = false;
//                         }
//                         break;
//                     case 'captcha':
//                         if (_.isEmpty(value)) {
//                             errorMsg = '验证码不能为空';
//                             formCheckValid = false;
//                         }
//                         break;
//                 }
//                 if (!formCheckValid) {
//                     errorElement.text(errorMsg).css('visibility', 'visible');
//                 }
//             });
//             return formCheckValid;
//         };
//
//         refreshCaptcha();
//         imageCaptchaElement.click(function () {
//             refreshCaptcha();
//         });
//
//         //input失去焦点时验证
//         loginFormElement.find('input:text,input:password').on('blur', function (event) {
//             var $this = $(this);
//             errorElement.text('').css('visibility', 'hidden');
//             checkLogin($this);
//         });
//
//         var submitLoginForm = function () {
//             loginFormElement.ajaxSubmit({
//                 beforeSubmit: function (arr, $form, options) {
//                     loginSubmitElement.addClass('loading');
//                 },
//                 success: function (data) {
//                     if (data.status) {
//                         window.location.href = _.difference(data.roles, ['USER']).length > 0 ? loginFormElement.data('redirect-url') : "/register/account";
//                     } else {
//                         refreshCaptcha();
//                         loginSubmitElement.removeClass('loading');
//                         errorElement.text(data.message).css('visibility', 'visible');
//                     }
//                 },
//                 error: function () {
//                     loginSubmitElement.removeClass('loading');
//                     refreshCaptcha();
//                     errorElement.text("用户或密码不正确").css('visibility', 'visible');
//                 }
//             });
//
//             return false;
//         };
//
//         loginSubmitElement.click(function (event) {
//             event.preventDefault();
//             formCheckValid = checkLogin(loginFormElement.find('input:text,input:password'));
//             if (formCheckValid) {
//                 submitLoginForm();
//             }
//         });
//
//         $(document).keypress(function (event) {
//
//             var keycode = (event.keyCode ? event.keyCode : event.which);
//             if (keycode === 13) {
//                 formCheckValid = checkLogin(loginFormElement.find('input:text,input:password'));
//                 if (formCheckValid) {
//                     loginSubmitElement.focus();
//                     event.preventDefault();
//                     submitLoginForm();
//                 }
//             }
//         })
//     })();
//
// });