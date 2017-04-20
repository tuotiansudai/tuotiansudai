require('publicJs/plugins/jQuery.md5');
let ValidatorObj= require('publicJs/validator');
let commonFun=require('publicJs/commonFun');

let formLogin=globalFun.$('#formLogin');
//用户注册表单校验
let validator = new ValidatorObj.ValidatorForm();
let $errorBox = $('.error-box',$(formLogin));


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
}]);

let urlObj = globalFun.parseURL(location.href);
let mobileVal = urlObj.params.mobile;
formLogin.username.value = mobileVal;
//显示手机号
String.prototype.hideForMobile=function() {

}



