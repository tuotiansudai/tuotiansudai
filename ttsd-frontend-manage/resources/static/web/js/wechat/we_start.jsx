require('webStyle/wechat/wechat_login.scss');
let ValidatorObj= require('publicJs/validator');
let formStart=globalFun.$('#formStart');


//用户注册表单校验
let validator = new ValidatorObj.ValidatorForm();
let $errorBox = $('.error-box',$(formStart));
//验证手机号是否存在

validator.add(formStart.mobile, [{
    strategy: 'isNonEmpty',
    errorMsg: '手机号不能为空',
}, {
    strategy: 'isMobile',
    errorMsg: '手机号格式不正确'
},{
    strategy: 'isMobileExist',
    errorMsg: '手机号已经存在'
}]);


globalFun.addEventHandler(formStart.mobile,"keyup", "blur", function() {
    let errorMsg=validator.start(this);
    $errorBox.text(errorMsg?errorMsg:'');
});


formStart.onsubmit = function(event) {
    event.preventDefault();
    //提交之前得先执行validateLogin验证表单是否通过验证
    // formStartFun.before(validateLogin)();

};




