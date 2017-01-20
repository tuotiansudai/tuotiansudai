
import {popWindow,useAjax,refreshCaptcha,countDownLoan} from "publicJs/common";

import "askStyle/questions.scss";
import "askJs/components/questions";
import "askJs/components/create_question";

//刷新验证码
$('#imageCaptcha').on('click',function() {
    refreshCaptcha(this,'/captcha');
    this.parentElement.children[0].value='';
});














