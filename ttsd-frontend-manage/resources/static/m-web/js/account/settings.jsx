let ValidatorObj= require('publicJs/validator');
let commonFun = require('publicJs/commonFun');
require('mWebStyle/account/settings.scss');
commonFun.calculationFun(document, window);

let $settingBox = $('#settingBox');
let $turnOnSendCaptcha = $('#turnOnSendCaptcha'),
    $getCaptchaElement = $turnOnSendCaptcha.find('.get-captcha');

let imageCaptchaForm = globalFun.$('#imageCaptchaForm'),
    turnOffNoPasswordInvestForm = globalFun.$('#turnOffNoPasswordInvestForm');

let UrlOption = {
    //关闭免密投资
    disabled:'no-password-invest/disabled',
    //打开免密投资
    enabled:'no-password-invest/enabled',
    //发送手机短信验证码
    sendCaptcha:'no-password-invest/send-captcha',
    //刷新图形验证码
    imageCaptcha:'no-password-invest/image-captcha',

    //去联动优势授权
    agreement:'/agreement'
};

//开启免密投资
let $btnOpenNopwd = $('.update-payment-nopwd',$settingBox);

$btnOpenNopwd.on('click',function() {
    let isOpen = $btnOpenNopwd.hasClass('opened'); //
    let firstopen = $btnOpenNopwd.data('firstopen'); //用来判断是否第一次开启
    if(isOpen) {
        //之前是开启的状态，现在做的是要去关闭
        $btnOpenNopwd.removeClass('opened');
        turnOffNoPassword();

    } else {
        //之前是开闭的状态，现在做的是要去开启
        $btnOpenNopwd.addClass('opened');
        OpenNoPasswordInvest(firstopen);

    }
});

//确认,第一步获取手机验证码
$getCaptchaElement.on('click',function(event){
    sendMsgCaptcha();
});

//去开启免密投资业务，第一次开启免密投资，需要去联动优势授权，之后不需要
function OpenNoPasswordInvest(firstopen) {

    if(firstopen) {
        commonFun.CommonLayerTip({
            btn: ['确定','取消'],
            content: $('#turnOnNoPassword')
        },function() {
            //确定打开，去联动优势授权
            commonFun.useAjax({
                type: 'POST',
                url:UrlOption['agreement']
            },function() {
                //授权成功
                commonFun.CommonLayerTip({
                    btn: ['我知道了'],
                    area:['300px', '260px'],
                    content: $('#noPasswordInvestDOM')
                },function() {
                    layer.closeAll();
                });
            });
        });
    } else {

        //如果是第二次打开 ，无需走联动优势，直接打开
        commonFun.useAjax({
            type: 'POST',
            url:UrlOption['enabled']
        },function() {
            commonFun.CommonLayerTip({
                btn: ['我知道了'],
                content: '<div class="tip-result-success"> <em class="icon-success"></em><span>免密支付已开启</span></div>',
            },function() {
                location.reload();
                layer.closeAll();
            });

        });
    }
}

//去关闭免密投资业务
function turnOffNoPassword() {
    commonFun.CommonLayerTip({
        btn: ['确定', '取消'],
        content: $('#turnOffNoPassword')
    },function() {

        // 第二步正式关闭免密投资
        closeNoPasswordCheck();

        commonFun.CommonLayerTip({
            btn: ['确定', '取消'],
            area:['300px', '300px'],
            content: $('#turnOnSendCaptcha')
        },function() {
            let captachElClass  =  turnOffNoPasswordInvestForm.captcha.className;
            if(/error/.test(captachElClass)) {
                return;
            }
            commonFun.useAjax({
                url: UrlOption['disabled'],
                type: 'POST',
                data: $(turnOffNoPasswordInvestForm).serialize()
            }, function (response) {
                var data = response.data;
                if (data.status) {
                    commonFun.CommonLayerTip({
                        btn: ['我知道了'],
                        content: '<div class="tip-result-success"> <em class="icon-success"></em><span>免密支付已关闭</span></div>',
                    },function() {
                        location.reload();
                        layer.closeAll();
                    });
                }

            });

        });

    });
}

//发送短信验证码
function sendMsgCaptcha() {

    let imageCaptchaForm = globalFun.$('#imageCaptchaForm'),
        turnOffNoPasswordInvestForm = globalFun.$('#turnOffNoPasswordInvestForm'),
        captchaFormData = $(imageCaptchaForm).serialize();

   let $codeNumber = $('.code-number-hidden',$turnOnSendCaptcha);

    $getCaptchaElement.prop('disabled',true);

        commonFun.useAjax({
            url:UrlOption['sendCaptcha'],
            type:'POST',
            data:captchaFormData
        },function(response) {
            $getCaptchaElement.prop('disabled',false);
            var data =response.data;
            if (data.status && !data.isRestricted) {
                $codeNumber.css({'visibility':'hidden'});
                commonFun.countDownLoan({
                    btnDom:$getCaptchaElement,
                    textCounting:'秒',
                });
            }

            if (!data.status && data.isRestricted) {
                $codeNumber.css({'visibility':'visible'});
                layer.msg('短信发送频繁，请稍后再试');
            }

            if (!data.status && !data.isRestricted) {
                $codeNumber.css({'visibility':'visible'});
                layer.msg('图形验证码不正确');
            }
            commonFun.refreshCaptcha(globalFun.$('#imageCaptcha'),UrlOption['imageCaptcha']);

        });

}

//发起关闭免密投资流程
function closeNoPasswordCheck() {

    let turnOffPassValidator = new ValidatorObj.ValidatorForm();
    //免密投资验证图形码
    turnOffPassValidator.newStrategy(turnOffNoPasswordInvestForm.captcha,'isNoPasswordCaptchaVerify',function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this,
            _arguments=arguments;
        var _phone = turnOffNoPasswordInvestForm.mobile.value,
            _captcha=turnOffNoPasswordInvestForm.captcha.value;
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:`/no-password-invest/mobile/${_phone}/captcha/${_captcha}/verify`
        },function(response) {
            if(response.data.status) {
                // 如果为true说明手机已存在
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

    turnOffPassValidator.add(turnOffNoPasswordInvestForm.captcha, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入验证码'
    },{
        strategy: 'equalLength:6',
        errorMsg: '验证码格式不正确'
    },{
        strategy: 'isNoPasswordCaptchaVerify',
        errorMsg: '验证码不正确'
    }]);

    $(turnOffNoPasswordInvestForm.captcha).on('blur',function(event) {
        let errorMsg = turnOffPassValidator.start(this);
        layer.msg(errorMsg);

    });

}

$('#logout').on('click',() => {
    commonFun.useAjax('',{
        url:"/logout",
        type:'POST',
    },function(response) {
        location.href = '/m/'
    },function () {
        location.href = '/m/'
    });
});

$('#reset-password').on('click',() => {
   location.href = './personal-info/reset-umpay-password';
});

$('#anxinSign').on('click',() => {
   location.href = './anxinSign?fromPage=settings';
});