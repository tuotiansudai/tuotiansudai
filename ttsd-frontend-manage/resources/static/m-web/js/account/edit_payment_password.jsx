require('mWebStyle/account/edit_payment_password.scss');

let editPasswordForm = globalFun.$('#editPasswordForm');
let resetPassword = globalFun.$('#resetPassword');

// 修改支付密码
if(editPasswordForm) {
    let $input = $(editPasswordForm).find('input:visible');

    Array.prototype.forEach.call($input,function(el) {

        globalFun.addEventHandler(el,'keyup',function() {

            checkFormInput();
        });
    });

    function checkFormInput() {
        let $submitBtn = $('.btn-wap-normal',$(editPasswordForm));
        let oldPassword = editPasswordForm.oldPassword.value,
            newPassword = editPasswordForm.newPassword.value;
        if(_.isEmpty(oldPassword) || _.isEmpty(newPassword)) {
            $submitBtn.prop('disabled',true);
        } else {
            $submitBtn.prop('disabled',false);
        }
    }

    editPasswordForm.onsubmit = function(event) {
        event.preventDefault();

        editPasswordForm.submit();
    }
}

//重置支付密码
if(resetPassword) {

    //页面初始化的时候光标移到第一个 ，如果输入一个，光标接着移到下一个输入框
    let $inputBox = $('.input-box',$(resetPassword));
    let $inputEl = $inputBox.find('input');
    let $sendShortMsg = $('#sendShortMsg');
    $inputEl.eq(0).focus();
    $inputEl.on('keyup',function(event) {
        let target = event.target;
        if(target.value.length==1) {
            $(target).next() && $(target).next().focus();
        }
    });

    $sendShortMsg.on('click',function(event) {
        event.preventDefault();

        let paramObj = $(resetPassword).serializeArray();
        let lastCardNum = _.pluck(paramObj, 'value').join('')

        if(lastCardNum.length==4) {
            window.location.href = 'sms:10690569687?body=CSMM#'+lastCardNum
        }
    })

}

