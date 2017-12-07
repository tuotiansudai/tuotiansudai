require('mWebStyle/account/certification.scss');
let ValidatorObj= require('publicJs/validator');
let commonFun= require('publicJs/commonFun');
var registerAccountForm = globalFun.$('#registerAccountForm'),
    $buttonLayer = $(registerAccountForm).find('.button-layer'),
    $btnSubmit = $(registerAccountForm).find('input[type="submit"]');

let validator = new ValidatorObj.ValidatorForm();

validator.add(registerAccountForm.userName, [{
    strategy: 'isNonEmpty',
    errorMsg: '请输入姓名',
}]);

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
}]);

let reInputs=$(registerAccountForm).find('input:text'),
    $errorBox = $('.error-box',$(registerAccountForm));
for(let i=0,len=reInputs.length; i<len;i++) {
    globalFun.addEventHandler(reInputs[i],"keyup", function() {
        let errorMsg = validator.start(this);
        if(errorMsg) {
            $errorBox.text(errorMsg);
        } else {
            $errorBox.text('');
        }
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
    commonFun.useAjax({
        url:"/register/account",
        type:'POST',
        data:$(registerAccountForm).serialize()
    },function(response) {
        if(response.data.status) {
            $buttonLayer.find('.status').removeClass('error').html('认证成功');
            location.href = '/account/success';
        } else {
            $buttonLayer.find('.status').addClass('error').html('认证失败，请检查');
            $btnSubmit.prop('disabled', false);
        }
    });
};




