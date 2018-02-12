require('mWebStyle/account/anxin_sign.scss');
require('mWebJsModule/anxin_agreement_pop');
let commonFun = require('publicJs/commonFun');
require('publicJs/honeySwitch');
require('publicStyle/honeySwitch.scss');
let $anxinElectronicsSign = $('.anxinElectronicsSign');
let $signature = $('#signature');
let $openMessage = $('#openMessage');
let isAnxinUser = $('.bind-data').data('is-anxin-user');
let switchStatus = $('#switchStatus').data('skip-auth');
let fromPage = getQueryString('fromPage');

commonFun.calculationFun(document, window);

if (!isAnxinUser) {
    $signature.show();
}
else {
    $openMessage.show();
}

/*
 * 安心签电子签章服务
 */
$('#authentication_identity').on('click', () => {  // 未实名认证则进行跳转
    location.href = './register/account';
});

$('.init-checkbox-style',$anxinElectronicsSign).initCheckbox(function(element) {
    var $parentBox = $(element).parents('.safety-status-box');
    //点击我已阅读并同意是否disable按钮
    $(element).hasClass('on');
});

// 开启安心签服务
$('#openSafetySigned').on('click', function () {
    var $this = $(this);
    $this.prop('disabled', true);
    commonFun.useAjax({
        type: 'POST',
        url: '/anxinSign/createAccount'
    }, function (response) {
        $this.prop('disabled', false);

        if (!response.success) {
            layer.msg('开启失败');
        } else {
            $signature.hide();
            $openMessage.show();
        }
    });
});

// 点击返回btn
$('.go-back-container').on('click', () => {
    location.href=document.referrer;
});

$('.init-checkbox-style').initCheckbox(function (element) {
    //点击我已阅读并同意是否disable按钮
    var isCheck = $(element).hasClass('on');
    var $btnNormal;
    if ($('#signature').css('display') == 'block') {
        $btnNormal = $('#openSafetySigned');
    }
    else {
        $btnNormal = $('#toOpenSMS');
    }

    if (isCheck) {
        $btnNormal.prop('disabled', false);
    }
    else {
        $btnNormal.prop('disabled', true);
    }
});

$('#openAuthorization').on('click', function () {
    $('#openMessage').hide();
    $('#authorization_message').show();
});

/*
 * 短信授权服务
 */

let $anxinAuthorization = $('#anxinAuthorization'),
    $buttonIdentify = $('.button-identify', $anxinAuthorization);

let $toOpenSMS = $('#toOpenSMS');

//获取验证码
$buttonIdentify.on('click', function (event) {
    let target = event.target;
    let isVoice = $(target).data('voice');
    commonFun.useAjax({
        type: 'POST',
        url: '/anxinSign/sendCaptcha',
        data: {
            isVoice: isVoice
        }
    }, function (data) {
        //请求成功开始倒计时
        if (data.success) {
            countDownTime();
        }
        else {
            layer.msg('请求失败，请重试或联系客服！');
        }
    });
});

function countDownTime() {
    let seconds = 60;
    $('.seconds').html(seconds);
    $('.button-identify').hide();
    $('.countDownTime').show();
    let countDown = setInterval(() => {
        seconds--;
        $('.seconds').html(seconds);
        if (seconds == 0) {
            clearInterval(countDown);
            $('.button-identify').show();
            $('.countDownTime').hide();
        }
    }, 1000)
}
//验证验证码并开通短信服务
$('#skipPhoneCode').on('keyup', function () {

    var $skipPhoneCode = $('#skipPhoneCode'),
        phoneCode = $skipPhoneCode.val();

    if (/^\d{6}$/.test(phoneCode) && $('#readOk1').is(':checked')) {
        $toOpenSMS.prop('disabled', false);
    } else {
        $toOpenSMS.prop('disabled', true);
    }
});

$toOpenSMS.on('click', function () {
    var $this = $(this),
        $skipPhoneCode = $('#skipPhoneCode'),
        phoneCode = $skipPhoneCode.val();

    if (!/^\d{6}$/.test(phoneCode)) {
        $('.error').show();
        return;
    }

    $this.prop('disabled', true);
    $this.html('授权中...');
    commonFun.useAjax({
        type: 'POST',
        url: '/anxinSign/verifyCaptcha',
        data: {
            captcha: phoneCode,
            skipAuth: true
        }
    }, function (data) {
        if (data.success) {
            layer.closeAll();
            location.href = '';
        }
        else {
            $this.prop('disabled', false);
            $this.html('立即授权');
            $('.error').show();
        }
    });
});

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
};
if (switchStatus) {  // 开关为开启状态
    $('#switch').addClass('switch-on');
}
else {
    $('#switch').addClass('switch-off'); // 开关为关闭状态
}

$('#goPage_1').on('click', () => {
    history.go(-1);
});

$('#goPage_2').on('click', () => {
    location.href = `./${fromPage}`;
});
$('#goPage_3').on('click', () => {
    $('#openMessage').show();
    $('#authorization_message').hide();
    $('#signature').hide();
});
$('#lastPage').on('click', () => {
    location.href = `./${fromPage}`;
});
$('#skipPhoneCode').on('keyup', (e) => {
    if (!e.currentTarget.value.length) {
        $('.close_btn').hide();
        return;
    }
    $('.close_btn').show();
});
$('.close_btn').on('click', () => {
    $('#skipPhoneCode').val('');
    $('#toOpenSMS').attr('disabled', true);
    $('.close_btn').hide();
});
