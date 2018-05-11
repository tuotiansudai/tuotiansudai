let commonFun= require('publicJs/commonFun');
require("askStyle/questions.scss");
var $questionDetailTag=$('#questionDetailTag');
var $formAnswer=$('.formAnswer',$questionDetailTag),
    $formAnswerSubmit=$('.formSubmit',$formAnswer);

var $createQuestion=$('#createQuestion');
var $formQuestion=$('.form-question',$createQuestion);
var $formSubmit=$('.formSubmit',$formQuestion);
//刷新验证码
$('#imageCaptcha').on('click',function() {
    commonFun.refreshCaptcha(this, applicationContext + '/captcha');
    this.parentElement.children[0].value='';
    if($formSubmit.length){
        $formSubmit.prop('disabled',true);
    }
   if($formAnswerSubmit.length){
       $formAnswerSubmit.prop('disabled',true);
   }


});

require.ensure(['askJs/components/questions'],function() {
    require("askJs/components/questions");
},'questions');

require.ensure(['askJs/components/create_question'],function() {
    require("askJs/components/create_question");
},'create_question');














