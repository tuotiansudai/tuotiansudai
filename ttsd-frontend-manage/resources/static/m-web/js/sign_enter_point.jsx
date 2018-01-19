require('mWebStyle/sign_enter_point.scss');

let ValidatorObj = require('publicJs/validator');
let commonFun = require('publicJs/commonFun');
let EntryPointForm = globalFun.$('#EntryPointForm'),
    $showMobile = $('.show-mobile',$(EntryPointForm)),
    $logoNote = $('#logoNote');

//用户注册表单校验
let validator = new ValidatorObj.ValidatorForm();
let $errorBox = $('.error-box', $(EntryPointForm));

validator.add(EntryPointForm.mobile, [{
    strategy: 'isNonEmpty',
    errorMsg: '手机号不能为空',
}, {
    strategy: 'isMobile',
    errorMsg: '手机号格式不正确'
}]);


globalFun.addEventHandler(EntryPointForm.mobile, "keyup","focus",function (event) {
    if(event.type == 'keyup') {
        let isRealLen = event.target.value.length == 11;
        $showMobile.text(event.target.value);
        $('button',$(EntryPointForm)).prop('disabled',!isRealLen);
    } else if(event.type == 'focus') {
        $logoNote.slideUp();
    }
});

$showMobile.on('click',function (event) {
    $logoNote.slideDown();
});

EntryPointForm.onsubmit = function (event) {
    let errorMsg = validator.start(EntryPointForm.mobile);
    if (errorMsg) {
        layer.msg(errorMsg);
        event.preventDefault();
    }
};




