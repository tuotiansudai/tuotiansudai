require('webStyle/account/account_common.scss');
require('webStyle/account/change_mobile.scss');
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');

let $originMobile = $('#originMobile');
let $newMobile = $('#newMobile');
let $errorLi = $('.error-li');
let originMobile = $originMobile.text();
let $errorBox = $('.error-box');
let $btnSub = $('.btn-sub');
$newMobile.on('keyup',function () {
    $errorLi.hide();
    $btnSub.attr('disabled',false);
    let elementInput = $newMobile.val();
    if(elementInput==originMobile){
        $errorBox.text('新手机号不能和原手机号一致');
        $errorLi.show();
        $btnSub.attr('disabled',true);
    }else {
        if(!(/^1[3|4|5|8][0-9]\d{8}$/.test(elementInput))){
            $errorBox.text('手机号格式不正确');
            $errorLi.show();
            $btnSub.attr('disabled',true);
        }
    }
})

