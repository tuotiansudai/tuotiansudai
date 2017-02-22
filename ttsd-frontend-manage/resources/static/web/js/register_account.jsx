require('webStyle/register.scss');
let ValidatorForm= require('publicJs/validator');
let commonFun= require('publicJs/commonFun');
var registerAccountForm = globalFun.$('#registerAccountForm'),
    $buttonLayer = $(registerAccountForm).find('.button-layer'),
    $btnSubmit = $(registerAccountForm).find('input[type="submit"]');

let validator = new ValidatorForm();

validator.add(registerAccountForm.userName, [{
    strategy: 'isNonEmpty',
    errorMsg: '请输入姓名',
}],true);

validator.add(registerAccountForm.identityNumber, [{
    strategy: 'isNonEmpty',
    errorMsg: '请输入身份证号',
}, {
    strategy: 'identityValid',
    errorMsg: '您的身份证号码不正确'
},{
    strategy: 'ageValid',
    errorMsg: '年龄未满18周岁'
},{
    strategy: 'isCardExist',
    errorMsg: '身份证已存在'
}],true);

let reInputs=$(registerAccountForm).find('input:text');

reInputs=Array.from(reInputs);
for (var el of reInputs) {
    el.addEventListener("blur", function() {
       validator.start(this);
        isDisabledButton();
    })
}

//用来判断获取验证码和立即注册按钮 是否可点击
function isDisabledButton() {
    let userName = registerAccountForm.userName,
        identityNumber = registerAccountForm.identityNumber;
    let isuserNameValid=!globalFun.hasClass(userName,'error') && userName.value;
    let isCardValid = !globalFun.hasClass(identityNumber,'error') && identityNumber.value;
    if(isuserNameValid && isCardValid) {
        $btnSubmit.prop('disabled',false);
    }
    else {
        $btnSubmit.prop('disabled',true);
    }
}

//点击立即注册按钮
registerAccountForm.onsubmit = function(event) {
    event.preventDefault();
    $buttonLayer.find('.status').removeClass('error').html('认证中...');
    $btnSubmit.prop('disabled', true);
    var redirect = document.referrer;
    commonFun.useAjax({
        url:"/register/account",
        type:'POST',
        data:$(registerAccountForm).serialize()
    },function(response) {
        if(response.data.status) {
            $buttonLayer.find('.status').removeClass('error').html('认证成功');
            setTimeout(location.href = redirect, 3000);
        }
        else {
            $buttonLayer.find('.status').addClass('error').html('认证失败，请检查');
            $btnSubmit.prop('disabled', false);
        }
    });
}




