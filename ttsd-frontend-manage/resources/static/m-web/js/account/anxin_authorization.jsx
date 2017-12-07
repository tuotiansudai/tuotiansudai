require('mWebJsModule/anxin_agreement_pop');
let commonFun= require('publicJs/commonFun');

let $anxinAuthorization = $('#anxinAuthorization'),
    $buttonIdentify = $('.button-identify',$anxinAuthorization);

let $toOpenSMS = $('#toOpenSMS');

//获取验证码
$buttonIdentify.on('click',function (event) {
    let $getSkipBtn = $('#getSkipCode'),
        $microPhone = $('#microPhone');
    $getSkipBtn.prop('disabled',true).width('100%');
    $microPhone.hide();

    let target = event.target;
    let isVoice = $(target).data('voice');
    commonFun.useAjax({
        type:'POST',
        url:'anxinSign/sendCaptcha',
        data:{
            isVoice:isVoice
        }
    },function(data) {
        //请求成功开始倒计时
        if(data.success) {
            commonFun.countDownLoan({
                btnDom:$getSkipBtn,
                time:60,
                isAfterText:'获取验证码'
            },function() {
                $getSkipBtn.prop('disabled',false);
                $microPhone.show();
            });
        }
        else {
            layer.msg('请求失败，请重试或联系客服！');
        }
    });
});


//验证验证码并开通短信服务
$('#skipPhoneCode').on('keyup',function() {

    var $skipPhoneCode=$('#skipPhoneCode'),
        phoneCode=$skipPhoneCode.val();

    if(/^\d{6}$/.test(phoneCode)) {
        $toOpenSMS.prop('disabled',false);
    } else {
        $toOpenSMS.prop('disabled',true);
    }
});

$toOpenSMS.on('click',function() {
    var $this=$(this),
        $skipPhoneCode=$('#skipPhoneCode'),
        phoneCode=$skipPhoneCode.val();

    if(!/^\d{6}$/.test(phoneCode)) {
        layer.msg('验证码错误');
        return;
    }

    $this.prop('disabled',true);

    commonFun.useAjax({
        type:'POST',
        url:'anxinSign/verifyCaptcha',
        data:{
            captcha: phoneCode,
            skipAuth:true
        }
    },function(data) {
        if(data.success) {
            layer.closeAll();
            location.href='';
        }
        else {
            $this.prop('disabled',false);
            layer.msg('验证码错误');
        }
    });


})