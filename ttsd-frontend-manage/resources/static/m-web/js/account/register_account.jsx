require('mWebStyle/account/register_account.scss');
let ValidatorObj= require('publicJs/validator');
let commonFun= require('publicJs/commonFun');
let registerAccountForm = globalFun.$('#registerAccountForm'),
    $buttonLayer = $(registerAccountForm).find('.button-layer'),
    $btnSubmit = $(registerAccountForm).find('input[type="submit"]');

let validator = new ValidatorObj.ValidatorForm();

validator.add(registerAccountForm.userName, [{
    strategy: 'isNonEmpty',
    errorMsg: '请输入姓名',
},
    {
        strategy: 'isChinese',
        errorMsg: '请输入汉字',
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
    globalFun.addEventHandler(reInputs[i],"input", function(e) {
        let errorMsg = validator.start(this);
        if(errorMsg) {
            $errorBox.text(errorMsg);
        } else {
            $errorBox.text('');
        }
        isDisabledButton();
    })
}
$('#perNum').on('keyup',(e) => {
    if (e.keyCode != 8) {
        if ($('#perNum').val().length === 6 || $('#perNum').val().length === 15) {
            let text = $('#perNum').val() + ' ';
            $('#perNum').val(text);
        }
    }
    else {
        if ($('#perNum').val().length === 7) {
            let text = $('#perNum').val().substring(0,6);
            $('#perNum').val(text);
        }
        else if ($('#perNum').val().length === 16) {
            let text = $('#perNum').val().substring(0,15);
            $('#perNum').val(text);
        }
    }
});
$('#perNum').on("paste",(e) => {
    var pastedText = undefined;
    if (window.clipboardData && window.clipboardData.getData) { // IE
        pastedText = window.clipboardData.getData('Text');
    } else {
        pastedText = e.originalEvent.clipboardData.getData('Text');//e.clipboardData.getData('text/plain');
    }
    let inputVal = pastedText.replace(/\s+/g, "");
    let text = inputVal.substring(0,6) + ' ' +  inputVal.substring(6,14) + ' ' + inputVal.substring(14);
    $('#perNum').val(text);
});
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
    $btnSubmit.prop('disabled', true).val('认证中');
    location.href = '/m/register/account/success';
    commonFun.useAjax({
        url:"/register/account",
        type:'POST',
        data:$(registerAccountForm).serialize()
    },function(response) {
        if(response.data.status) {
            $btnSubmit.prop('disabled', true).val('认证成功');
            location.href = '/m/register/account/success';
        } else {
            let errorMsg = (response.data.code === '1002') ? '实名认证超时，请重试' : '认证失败，请检查您的信息';
            $errorBox.text(errorMsg);
            $btnSubmit.prop('disabled', false).val('认证');
        }
    });
};

$('#iconRegister').on('click',function () {
    history.go(-1);
})
