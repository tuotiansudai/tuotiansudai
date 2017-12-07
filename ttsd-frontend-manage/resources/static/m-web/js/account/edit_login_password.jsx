require('mWebStyle/account/edit_login_password.scss');

let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');
//修改密码
let changePasswordForm=globalFun.$('#changePasswordForm'),
    $submitForm = $(changePasswordForm).find('button:submit');
let $errorBox = $('.error-box',$(changePasswordForm));

//修改密码表单验证
let passValidator = new ValidatorObj.ValidatorForm();
//验证原密码是否存在
passValidator.newStrategy(changePasswordForm.originalPassword,'isNotExistPassword',function(errorMsg,showErrorAfter) {
    var getResult='',
        that=this,
        _arguments=arguments;
    commonFun.useAjax({
        type:'GET',
        async: false,
        url:'/personal-info/password/'+this.value+'/is-exist'
    },function(response) {
        if(response.data.status) {
            // 如果为true说明密码存在有效
            getResult='';
            ValidatorObj.isHaveError.no.apply(that,_arguments);

        }
        else {
            getResult=errorMsg;
            ValidatorObj.isHaveError.yes.apply(that,_arguments);
        }
    });
    return getResult;
});
passValidator.add(changePasswordForm.originalPassword, [{
    strategy: 'isNonEmpty',
    errorMsg: '请输入原密码'
}, {
    strategy: 'checkPassword',
    errorMsg: '密码为6位至20位，不能全是数字'
},{
    strategy: 'isNotExistPassword',
    errorMsg: '原密码不正确'
}]);

passValidator.add(changePasswordForm.newPassword, [{
    strategy: 'isNonEmpty',
    errorMsg: '请输入新密码'
}, {
    strategy: 'checkPassword',
    errorMsg: '密码为6位至20位，不能全是数字'
}]);

passValidator.add(changePasswordForm.newPasswordConfirm, [{
    strategy: 'equalTo:#newPassword',
    errorMsg: '与新密码不一致'
}]);

let reInputs=$(changePasswordForm).find('input[validate]');
for(let i=0,len=reInputs.length; i<len;i++) {
    reInputs[i].addEventListener("keyup", function() {
        let errorMsg = passValidator.start(this);
        if(errorMsg) {
            $errorBox.text(errorMsg);
        } else {
            $errorBox.text('');
        }
        isDisabledButton();
    })
}

function isDisabledButton() {
    let validateNum = 0,
        inputLen = reInputs.length;
    for(let i=0; i<inputLen;i++) {
        if(/valid/.test(reInputs[i].className)) {
            validateNum++;
        }
    }
    if(validateNum == inputLen ) {
        $submitForm.prop('disabled',false);
    } else {
        $submitForm.prop('disabled',true);
    }

}
changePasswordForm.onsubmit = function(event) {
    event.preventDefault();
    commonFun.useAjax({
        url:"/personal-info/change-password",
        type:'POST',
        data:$(changePasswordForm).serialize()
    },function(response) {
        var data = response.data;
        if (data.status) {
            layer.msg('密码修改成功，请重新登录！', {type: 1, time: 2000}, function(){
                globalFun.$('#logout-form').submit();
            });
        } else {
            layer.msg('密码修改失败，请重试！', {type: 1, time: 2000});
            changePasswordForm.reset();
        }
    });

}


