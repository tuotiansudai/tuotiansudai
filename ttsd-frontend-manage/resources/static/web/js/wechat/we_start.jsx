require('webStyle/wechat/wechat_login.scss');
require('publicJs/plugins/jQuery.md5');
let ValidatorObj= require('publicJs/validator');
let commonFun=require('publicJs/commonFun');
let formStart=globalFun.$('#formStart');

//用户注册表单校验
let validator = new ValidatorObj.ValidatorForm();
let $errorBox = $('.error-box',$(formStart));

validator.add(formStart.mobile, [{
    strategy: 'isNonEmpty',
    errorMsg: '手机号不能为空',
}, {
    strategy: 'isMobile',
    errorMsg: '手机号格式不正确'
}]);


globalFun.addEventHandler(formStart.mobile,"keyup", "blur", function() {
    let errorMsg=validator.start(this);
    $errorBox.text(errorMsg?errorMsg:'');
});


formStart.onsubmit = function(event) {
    event.preventDefault();
    let mobileVal = formStart.mobile.value;
    let md5Mobile = $.md5(mobileVal); //加密后的手机字符串
    let errorMsg=validator.start(formStart.mobile);
    if(errorMsg) {
        $errorBox.text(errorMsg);
        return;
    } else {
        $errorBox.text('');
    }

    var md5String=commonFun.decrypt.compile(md5Mobile,mobileVal);

    commonFun.useAjax({
        type:'GET',
        async: false,
        url:'/register/user/mobile/'+mobileVal+'/is-exist'
    },function(response) {
        if(response.data.status) {
            // 如果为true说明手机已存在或已注册,走登录流程
            location.href = '/we-chat/entry-point/login?mobile='+md5String;
        }
        else {
            //手机号不存在走注册流程
            location.href = '/we-chat/entry-point/register?mobile='+md5String
        }
    });
};




