let commonFun= require('publicJs/commonFun');
require("askStyle/questions.scss");
//刷新验证码
$('#imageCaptcha').on('click',function() {
    commonFun.refreshCaptcha(this, applicationContext + '/captcha');
    this.parentElement.children[0].value='';

});

require.ensure(['askJs/components/questions'],function() {
    require("askJs/components/questions");
},'questions');

require.ensure(['askJs/components/create_question'],function() {
    require("askJs/components/create_question");
},'create_question');














