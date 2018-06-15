require('webStyle/register.scss');
let ValidatorObj = require('publicJs/validator');
let commonFun = require('publicJs/commonFun');
let registerAccountForm = globalFun.$('#registerAccountForm'),
    $buttonLayer = $(registerAccountForm).find('.button-layer'),
    $btnSubmit = $(registerAccountForm).find('input[type="submit"]');

let validator = new ValidatorObj.ValidatorForm();

validator.add(registerAccountForm.userName, [{
    strategy: 'isNonEmpty',
    errorMsg: '请输入姓名',
},
    {
        strategy: 'minLength:2',
        errorMsg: '至少输入两位汉字',
    },
    {
        strategy: 'maxLength:16',
        errorMsg: '最多输入16位汉字',
    }
], true);

validator.add(registerAccountForm.identityNumber, [{
    strategy: 'isNonEmpty',
    errorMsg: '请输入身份证号',
}, {
    strategy: 'identityValid',
    errorMsg: '您的身份证号码不正确'
}, {
    strategy: 'maxLength:18',
    errorMsg: '身份证位数不正确'
}, {
    strategy: 'ageValid',
    errorMsg: '年龄未满18周岁'
}, {
    strategy: 'isCardExist',
    errorMsg: '身份证已存在'
}], true);

let reInputs = $(registerAccountForm).find('input:text');
for (let i = 0, len = reInputs.length; i < len; i++) {
    globalFun.addEventHandler(reInputs[i], "keyup", "blur", function () {
        validator.start(this);
        $buttonLayer.find('.status').removeClass('error').html('');
        isDisabledButton();
    })
}
//用来判断获取验证码和立即注册按钮 是否可点击
function isDisabledButton() {
    let userName = registerAccountForm.userName,
        identityNumber = registerAccountForm.identityNumber;
    let isuserNameValid = !globalFun.hasClass(userName, 'error') && userName.value;
    let isCardValid = !globalFun.hasClass(identityNumber, 'error') && identityNumber.value;
    if (isuserNameValid && isCardValid) {
        $btnSubmit.prop('disabled', false);
    }
    else {
        $btnSubmit.prop('disabled', true);
    }
}

// registerAccountForm.onsubmit = function (event) {
//     event.preventDefault();
//     $buttonLayer.find('.status').removeClass('error').html('请耐心等待');
//     $btnSubmit.prop('disabled', true).val('认证中');
//     $('.loading-effect', $buttonLayer).show();
//     let params = $(registerAccountForm).serialize();
//     if (location.hash == '#loan') {
//         params += '&referrer=loan';
//     }
//
//     commonFun.useAjax({
//         url: "/register/account",
//         type: 'POST',
//         data: params
//     }, function (response) {
//         if (response.data.status) {
//             $buttonLayer.find('.status').removeClass('error').html('认证成功');
//             if (response.data.extraValues && response.data.extraValues.referrer) {
//                 location.href = '/register/account/success?referrer=' + response.data.extraValues.referrer;
//             } else {
//                 location.href = '/register/account/success';
//             }
//         } else {
//             let errorMsg = (response.data.code === '1002') ? '实名认证超时，请重试' : '认证失败，请检查您的信息';
//             $buttonLayer.find('.status').addClass('error').html(errorMsg);
//             $btnSubmit.prop('disabled', false).val('认证');
//             $('.loading-effect', $buttonLayer).hide();
//
//         }
//     });
// };





