require('webStyle/wechat/wechat_entry_point.scss');
let ValidatorObj = require('publicJs/validator');
let commonFun = require('publicJs/commonFun');
let weChatEntryPointForm = globalFun.$('#weChatEntryPointForm');

//用户注册表单校验
let validator = new ValidatorObj.ValidatorForm();
let $errorBox = $('.error-box', $(weChatEntryPointForm));

validator.add(weChatEntryPointForm.mobile, [{
    strategy: 'isNonEmpty',
    errorMsg: '手机号不能为空',
}, {
    strategy: 'isMobile',
    errorMsg: '手机号格式不正确'
}]);


globalFun.addEventHandler(weChatEntryPointForm.mobile, "keyup", "blur", function () {
    let errorMsg = validator.start(this);
    $errorBox.text(errorMsg ? errorMsg : '');
});


weChatEntryPointForm.onsubmit = function (event) {
    $errorBox.text('');
    let errorMsg = validator.start(weChatEntryPointForm.mobile);
    if (errorMsg) {
        $errorBox.text(errorMsg);
        event.preventDefault();
    }
};




