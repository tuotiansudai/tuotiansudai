require("activityStyle/wechat/share_app.scss");
let commonFun = require('publicJs/commonFun');
require('publicJs/placeholder');
require('swiper/dist/css/swiper.css')
let Swiper = require('swiper/dist/js/swiper.jquery.min');
let ValidatorObj = require('publicJs/validator');
let $registerContainer = $('#registerContainer');
let $agreementLi = $('#agreementLable');
let $btnCoupon = $('.coupon-btn'),
    browser = globalFun.browserRedirect();
let urlObj = globalFun.parseURL(location.href);
let $btnChangeImgCode = $('.image-captcha',$registerContainer );//换一张
let registerForm = globalFun.$('#registerUserForm');
let $fetchCaptcha = $('#getCaptchaBtn');//获取短信验证码btn
let $inputImgCaptcha = $('#input_img_captcha');//图形验证码输入框
let $inputSmsCaptcha = $('#smsCaptcha');//图形验证码输入框
let isSmsSended = false;
let $registerSubmit=$('input[type="submit"]',$(registerForm));
let $voiceCaptcha = $('#voice_captcha');
let $voiceBtn = $('#voice_btn',$voiceCaptcha);
let referrerValidBool=true;

$('#fuliList').find('.swiper-slide').each(function (index,item) {
    let  _self = $(this);
    let imgUrl = require('../../images/landingpage/fuli'+(index+1)+'.png');
    let img = new Image();
    img.src = imgUrl;
    _self.append(img);
})

var mySwiper = new Swiper ('#fuliList', {
    direction: 'horizontal',
    loop: true,
    autoplay:5000,
    autoplayDisableOnInteraction:false,
    slidesPerView: 'auto',
    centeredSlides:true,
    spaceBetween: -20,
    loopAdditionalSlides:1,
    nextButton: '.nextBtn',
    prevButton: '.prevBtn'

});


let isVoice = false;
$inputImgCaptcha.on('keyup', function (event) {
    event.preventDefault();
    disableCaptchaBtn();
});
disableCaptchaBtn();
function disableCaptchaBtn() {
    if ($('#mobile').val().length == 11 && $inputImgCaptcha.val().length == 5&&!$('#mobile').hasClass('error')) {
        if(!isSmsSended){
            $fetchCaptcha.prop('disabled', false);
        }
    } else {
        $fetchCaptcha.prop('disabled', true);
    }
}
$('#agreementInput').prop('checked',true);

$agreementLi.on('click', function (e) {
    $('.icon-yesOrNo-checked').toggleClass('checked');
    let mobile=registerForm.mobile,
        password=registerForm.password,
        captcha=registerForm.captcha;

    //获取验证码点亮
    let isMobileValid=!globalFun.hasClass(mobile,'error') && mobile.value;
    let isPwdValid = !globalFun.hasClass(password,'error') && password.value;

    let isDisabledCaptcha = isMobileValid && isPwdValid;

    //通过获取验证码按钮来判断


    let captchaValid = !$(captcha).hasClass('error') && captcha.value;
    let isDisabledSubmit= isMobileValid && isPwdValid && captchaValid  && !$('#agreementInput').prop('checked');
    $registerSubmit.prop('disabled',!isDisabledSubmit);

})
$('#recommendLabel').on('click', function () {
    $('.icon-arrow-bottom').toggleClass('active');
    $('.recomender-iphone').toggleClass('show');
})
//刷新验证码
$btnChangeImgCode.on('click', function (event) {
    event.preventDefault();
    refreshImgCaptcha();
});


function refreshImgCaptcha() {
    $('.image-captcha img').each(function (index, el) {
        commonFun.refreshCaptcha(this, '/register/user/image-captcha');
    });
}

refreshImgCaptcha();

$inputImgCaptcha.on('click',function () {

})
$fetchCaptcha.on('click',function () {
    getSmsCaptcha();
})
require.ensure(['publicJs/placeholder'], function (require) {
    require('publicJs/placeholder');
    $('input[type="text"],input[type="password"]', $(registerForm)).placeholder();
}, 'placeholder');
//显示服务协议
$('.show-agreement').on('click', function (event) {
    event.preventDefault();
    layer.open({
        type: 1,
        title: '拓天速贷服务协议',
        area: $(window).width() < 700 ? ['100%', '100%'] : ['950px', '600px'],
        shadeClose: true,
        move: false,
        scrollbar: true,
        skin: 'register-skin',
        content: $('#agreementBox')
    });
});
//获取短信验证码
function getSmsCaptcha() {
    var captchaVal = $inputImgCaptcha.val();
    var mobileNum = $('#mobile').val();
    if(captchaVal.length !==5){
        layer.msg('图形验证码错误');
        return;
    }

    commonFun.useAjax({
        url: '/register/user/send-register-captcha',
        type: 'POST',
        dataType: 'json',
        data: {imageCaptcha: captchaVal, mobile: mobileNum,isVoice:isVoice}
    },function(data) {
        console.log(data)
        if (data.data.status && !data.data.isRestricted) {
            $voiceCaptcha.hide();
            commonFun.countDownLoan({
                btnDom:$fetchCaptcha
            },function () {
                $voiceCaptcha.show();
            })
            return;
        }
        if (!data.data.status && data.data.isRestricted) {
            layer.msg('短信发送频繁,请稍后再试');
        }

        if (!data.data.status && !data.data.isRestricted) {
            layer.msg('图形验证码错误');
        }
        refreshImgCaptcha();
    });
}

//用户注册表单校验
let validator = new ValidatorObj.ValidatorForm();
//验证码是否正确
validator.newStrategy(registerForm.captcha,'isCaptchaValid',function(errorMsg,showErrorAfter) {
    var getResult='',
        that=this,
        _arguments=arguments;

    var _phone = registerForm.mobile.value,
        _captcha=registerForm.captcha.value;


    //先判断手机号格式是否正确
    if(!/(^1[0-9]{10}$)/.test(_phone)) {
        return;
    }
    commonFun.useAjax({
        type:'GET',
        async: false,
        url:`/register/user/mobile/${_phone}/captcha/${_captcha}/verify`
    },function(response) {
        if(response.data.status) {
            // 如果为true说明验证码正确
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
//推荐人是否存在
validator.newStrategy(registerForm.referrer,'isReferrerExist',function(errorMsg,showErrorAfter) {
    var getResult='',
        that=this,
        _arguments=arguments;
    //只验证推荐人是否存在，不验证是否为空
    if(this.value=='') {
        referrerValidBool=true;
        getResult='';
        ValidatorObj.isHaveError.no.apply(that,_arguments);
        return '';
    }
    commonFun.useAjax({
        type:'GET',
        async: false,
        url:'/register/user/referrer/'+this.value+'/is-exist'
    },function(response) {
        if(response.data.status) {
            // 如果为true说明推荐人存在
            referrerValidBool=true;
            getResult='';
            ValidatorObj.isHaveError.no.apply(that,_arguments);
        }
        else {
            referrerValidBool=false;
            getResult=errorMsg;
            ValidatorObj.isHaveError.yes.apply(that,_arguments);
        }
    });
    return getResult;
});
validator.add(registerForm.referrer, [{
    strategy: 'isReferrerExist',
    errorMsg: '推荐人不存在'
}],true);
validator.add(registerForm.mobile, [{
    strategy: 'isNonEmpty',
    errorMsg: '手机号不能为空',
}, {
    strategy: 'isMobile',
    errorMsg: '手机号格式不正确'
},{
    strategy: 'isMobileExist',
    errorMsg: '手机号已经存在'
}],true);
validator.add(registerForm.password, [{
    strategy: 'isNonEmpty',
    errorMsg: '密码不能为空'
}, {
    strategy: 'checkPassword',
    errorMsg: '密码为6位至20位，不能全是数字'
}],true);
validator.add(registerForm.appCaptcha, [{
    strategy: 'isNonEmpty',
    errorMsg: '验证码不能为空'
}],true);
validator.add(registerForm.captcha, [{
    strategy: 'isNonEmpty',
    errorMsg: '验证码不能为空'
},{
    strategy: 'isNumber:6',
    errorMsg: '验证码为6位数字'
},{
    strategy: 'isCaptchaValid',
    errorMsg: '验证码不正确'
}],true);

let reInputs=$(registerForm).find('input[validate]');

for(let i=0,len=reInputs.length; i<len;i++) {
    globalFun.addEventHandler(reInputs[i],"keyup", "focusout", function() {
        let errorMsg=validator.start(this);
        isDisabledButton();
    })
}

function isDisabledButton() {
    let mobile=registerForm.mobile,
        password=registerForm.password,
        referrer=registerForm.referrer,
        captcha=registerForm.captcha;
    if($(referrer).is(':hidden')) {
        referrerValidBool=true;
    }

    //获取验证码点亮
    let isMobileValid=!globalFun.hasClass(mobile,'error') && mobile.value;
    let isPwdValid = !globalFun.hasClass(password,'error') && password.value;

    let isDisabledCaptcha = isMobileValid && isPwdValid;

    //通过获取验证码按钮来判断
    !isDisabledCaptcha && $registerSubmit.prop('disabled',true);

    let captchaValid = !$(captcha).hasClass('error') && captcha.value;
    let isDisabledSubmit= isMobileValid && isPwdValid&&referrerValidBool && captchaValid  && $('#agreementInput').prop('checked');
    $registerSubmit.prop('disabled',!isDisabledSubmit);

}

//点击立即注册按钮
registerForm.onsubmit = function(event) {
    event.preventDefault();
     //registerForm.submit();
    let surl,
        paramObj={};
    let referrerMobile = JSON.parse('{"' + decodeURI(location.search.substring(1)).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}')["referrerMobile"];
    paramObj.mobile = registerForm.mobile.value;
    paramObj.captcha = registerForm.captcha.value;


    surl = '/register/user/shared';
    paramObj.password = registerForm.password.value;
    paramObj.referrer = referrerMobile;
    paramObj.agreement = $('#agreementInput').prop('checked');
    paramObj.activityReferrer = $('#activityReferrer').val();


    commonFun.useAjax({
        url: surl,
        type: 'POST',
        dataType: 'json',
        data: paramObj
    },function(data) {
        if (data.data.status) {
            location.href = '/activity/app-share/success?referrerMobile=' + location.href.split('referrerMobile=')[1];
        } else {
            layer.msg('请求失败，请重试！');
        }
    });
}




//点击立即注册领取
$('.coupon-btn1').on('click',function (event) {
    event.preventDefault();
    if (urlObj.params.source == 'app') {
        window.location.href = "/register/user";
    } else {
        $('body,html').animate({scrollTop: 0}, 'fast');
    }
});
$('.coupon-btn2').on('click',function (event) {
    event.preventDefault();
    if (urlObj.params.source == 'app') {
        window.location.href = "/register/user";
    } else {
        $('body,html').animate({scrollTop: 0}, 'fast');
    }
});
$('.coupon-btn3').on('click',function (event) {
    event.preventDefault();
    if (urlObj.params.source == 'app') {
        window.location.href = "/register/user";
    } else {
        $('body,html').animate({scrollTop: 0}, 'fast');
    }
});

//判断有无推荐人
function showReferrerInfoIfNeeded() {
    var referNum = urlObj.params.referrerMobile;

    if (referNum) {
        //有推荐人
        var mobileNum = referNum;
        $('#recommendLabel').hide();
        $('#recommendLabelExist').show();
        $('.refer-info-other').show();
        $('#referMobile').text(mobileNum);
        //通过手机号得到用户名
        commonFun.useAjax({
            type:'GET',
            dataType: 'json',
            url:"/activity/get-realRealName?mobile=" + mobileNum
        },function(data) {
            //姓名的第一个字母用*替换
            $('.refer-name', $registerContainer).text(data);
        });
    }
    else {
        //无推荐人
        $('.refer-person-info', $registerContainer).hide();
        $('#recommendLabel').show();
        $('#recommendLabelExist').hide();

    }
    isDisabledButton();
}
showReferrerInfoIfNeeded();

$voiceBtn.on('click', function(event) {
    isVoice = true;
    event.preventDefault();
    let mobile=registerForm.mobile,
        password=registerForm.password,
        captcha=registerForm.captcha;

    //获取验证码点亮
    let isMobileValid=!globalFun.hasClass(mobile,'error') && mobile.value;
    let isPwdValid = !globalFun.hasClass(password,'error') && password.value;

    let isDisabledCaptcha = isMobileValid && isPwdValid;

    //通过获取验证码按钮来判断


    let captchaValid = !$(captcha).hasClass('error') && captcha.value;
    let isDisabledSubmit= isMobileValid && isPwdValid && captchaValid  && !$('#agreementInput').prop('checked');
    if(isDisabledSubmit){
        getSmsCaptcha();
    }

});

// **************** 投资计算器开始****************
(function() {
    let countForm=globalFun.$('#countForm');
    let $countFormOut=$(countForm).parents('.count-form');
    let errorCountDom=$(countForm).find('.error-box');

    //验证表单
    let countValidator = new ValidatorObj.ValidatorForm();

    countValidator.add(countForm.money, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入投资金额！'
    }, {
        strategy: 'isNumber',
        errorMsg: '请输入有效的数字！'
    }]);

    countValidator.add(countForm.day, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入投资期限！'
    }, {
        strategy: 'isNumber',
        errorMsg: '请输入有效的数字！'
    }]);

    countValidator.add(countForm.rate, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入年化利率！'
    }, {
        strategy: 'isNumber',
        errorMsg: '请输入有效的数字！'
    }]);

    let reInputs=$(countForm).find('input:text');
    for(let i=0,len=reInputs.length; i<len;i++) {
        reInputs[i].addEventListener("blur", function() {
            let errorMsg = countValidator.start(this);
            if(errorMsg) {
                errorCountDom.text(errorMsg);
            }
            else {
                errorCountDom.text('');
            }
        })
    }
    countForm.onsubmit = function(event) {
        event.preventDefault();
        let errorMsg;
        for(let i=0,len=reInputs.length;i<len;i++) {
            errorMsg = countValidator.start(reInputs[i]);
            if(errorMsg) {
                errorCountDom.text(errorMsg);
                break;
            }
        }
        if (!errorMsg) {
            //计算本息
            var moneyNum = Math.floor(countForm.money.value * 100),
                dayNum = Math.floor(countForm.day.value),
                rateNum = Math.floor(countForm.rate.value * 10),
                $resultNum = $('#resultNum');

            var period = dayNum % 30 == 0 ? 30 : dayNum % 30,
                resultNum = parseFloat((moneyNum / 100).toFixed(2)),
                interest, fee;

            while (dayNum > 0) {
                interest = parseFloat((Math.floor(moneyNum * rateNum * period / 365000) / 100).toFixed(2));
                fee = parseFloat((Math.floor(interest * 10) / 100).toFixed(2));
                resultNum += (interest - fee);
                dayNum -= period;
                period = 30;
            }
            $resultNum.text(resultNum.toFixed(2));
        }
    }

})();
$('.open-app').click(function (e) {
    commonFun.toDownloadApp();
})
$('.close-app').click(function (e) {
    e.stopPropagation();
    $('.app-container-landing').hide();
})













