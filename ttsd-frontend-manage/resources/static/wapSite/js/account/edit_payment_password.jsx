require('wapSiteStyle/account/edit_payment_password.scss');

let editPasswordForm = globalFun.$('#editPasswordForm');

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
