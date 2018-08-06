require('webStyle/register.scss');
let commonFun=require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');

let registerForm=globalFun.$('#registerUserForm'); //注册的form
let imageCaptchaForm=globalFun.$('#imageCaptchaForm'); //获取验证码的form
let $fetchCaptcha=$('#fetchCaptcha');
let $registerSubmit=$('input[type="submit"]',$(registerForm));
let  $referrerOpen=$('.referrer-open',$(registerForm));
let $referrer=$('input.referrer', $(registerForm));
let agreementValid=true,
    referrerValidBool=true,
    testPicCode = false,  // 图形验证码验证默认值
    testMobile = false,    // 手机号验证默认值
    testCaptcha = false, // 手机验证码
    receiveCodeBtn = false; //获取验证码按钮
let $captchaSubmit=$('.image-captcha-confirm'),
    $voiceCaptcha = $('#voice_captcha_code');

require.ensure(['publicJs/placeholder'], function(require){
    require('publicJs/placeholder');
    $('input[type="text"],input[type="password"]',$(registerForm)).placeholder();
},'placeholder');

//点击获取验证码按钮
require.ensure(['publicJs/my_fetch_captcha'], function(require){
    let fetchCaptchaFun=require('publicJs/my_fetch_captcha');
    let fetchCaptchaRegister=new fetchCaptchaFun('registerUserForm','register');

    fetchCaptchaRegister.init();

},'fetchCaptcha');

//推荐人显示隐藏
(function() {
    $referrerOpen.on('click',function() {
        let $this=$(this),
            checkOption = false,
            iconArrow=$this.find('i');
        $this.next('li').toggleClass('hide');
        checkOption=$this.next('li').hasClass('hide');
        iconArrow[0].className=checkOption?'icon-arrow-right':'icon-arrow-bottom';
        if($referrer.is(':hidden')) {
            referrerValidBool=true;
        }
        else if(!$referrer.is(':hidden') && $referrer.hasClass('error')) {
            referrerValidBool=false;
        }
        isDisabledButton();
    });
})();

//同意服务协议
(function() {
    let $checkbox=$('label.check-label',$(registerForm));
    let $agreement = $('#agreementInput');
    let $showAgreement = $('.show-agreement', $(registerForm))
    $checkbox.on('click', function (event) {
        if (event.target.tagName.toUpperCase() == 'A') {
            return;
        }
        var $this=$(this),
            $agreeLast=$this.parents('.agree-last'),
            $cIcon=$agreeLast.find('i');
        if($this.hasClass('checked')) {
            $this.removeClass('checked');
            $agreement.prop('checked',false);
            $cIcon[0].className='icon-no-checked';
            agreementValid=false;
        }
        else {
            $this.addClass('checked');
            $agreement.prop('checked',true);
            $cIcon[0].className='icon-yes-checked';
            agreementValid=true;
        }
        isDisabledButton();
    });

    $showAgreement.click(function () {
        layer.open({
            type: 1,
            title: '拓天速贷服务协议',
            area: $(window).width()<1000?['100%', '100%']:['950px', '600px'],
            shadeClose: true,
            move: false,
            scrollbar: true,
            skin:'register-skin',
            content: $('#agreementBox'),
            success: function (layero, index) {
            }
        });
    });

})();

//用户注册表单校验
let validator = new ValidatorObj.ValidatorForm();



//验证图形验证码

validator.newStrategy(registerForm.imageCaptcha,'imageCaptcha',function(errorMsg,showErrorAfter) {
    let getResult='',
        that=this,
        _arguments=arguments;

    if ( $("input[name='imageCaptcha']").val().length != 5) {
        receiveCodeBtn = false;
        return;
    }
    let ajaxOption = {
        url: '/register/user/send-register-captcha',
        type:'POST',
        data:$(imageCaptchaForm).serialize()+'&voice='+isVoice
    };
    commonFun.useAjax(ajaxOption,function(responseData) {
        $captchaSubmit.prop('disabled',false);
        let data = responseData.data;
        if (data.status && !data.isRestricted) {
            $('.sendOften').hide();
            testPicCode = true;
            getResult='';
            ValidatorObj.isHaveError.no.apply(that,_arguments);
            receiveCodeBtn = false;
            commonFun.countDownLoan({
                btnDom:$fetchCaptcha,
                isAfterText:'重新发送'
            },function(){
                // getResult='';
                // ValidatorObj.isHaveError.no.apply(that,_arguments);
                $voiceCaptcha.show();
            });

        }
        else if (!data.status && data.isRestricted) {
            $('.sendOften').show();
            refreshCode();
        }
        else if (!data.status && !data.isRestricted) {
            $('.sendOften').hide();
            testPicCode = false;
            getResult=errorMsg;
            ValidatorObj.isHaveError.yes.apply(that,_arguments);
            refreshCode();
            $("input[name='imageCaptcha']").val('')
        }
    });
});

//验证码是否正确
validator.newStrategy(registerForm.captcha,'isCaptchaValid',function(errorMsg,showErrorAfter) {
    var getResult='',
        that=this,
        _arguments=arguments;

    var _phone = registerForm.mobile.value,
        _password = registerForm.password.value,
        _captcha=registerForm.captcha.value;

    //先判断手机号格式是否正确
    if(!/(^1[0-9]{10}$)/.test(_phone)) {
        validator.start(registerForm.mobile);
        return;
    }

    if (!testPicCode && $('#fetchCaptcha').html() != '获取验证码') {
        testCaptcha = true;
        validator.start(registerForm.captcha);
        return;
    }

    commonFun.useAjax({
        type:'GET',
        async: false,
        url:`/register/user/mobile/${_phone}/captcha/${_captcha}/verify`
    },function(response) {
        if(response.data.status) {
            // 如果为true说明验证码正确
            testCaptcha = true;
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

validator.add(registerForm.mobile, [{
    strategy: 'isNonEmpty',
    errorMsg: '手机号不能为空',
}, {
    strategy: 'isMobile',
    errorMsg: '手机号格式不正确'
}],true);
validator.add(registerForm.password, [{
    strategy: 'isNonEmpty',
    errorMsg: '密码不能为空'
}, {
    strategy: 'checkPassword',
    errorMsg: '请输入6-20位密码，不能为纯数字'
}],true);
validator.add(registerForm.imageCaptcha, [{
    strategy: 'isNonEmpty',
    errorMsg: '图形验证码不能为空'
}, {
    strategy: 'imageCaptcha',
    errorMsg: '图形验证码不正确'
},{
    strategy: 'isNumber:5',
    errorMsg: '图形验证码不正确'
}],true);
validator.add(registerForm.captcha, [{
    strategy: 'isNumber:6',
    errorMsg: '验证码不正确'
},{
    strategy: 'isCaptchaValid',
    errorMsg: '验证码不正确'
}],true);



let reInputs=$(registerForm).find('input[validate]');

for(let i=0,len=reInputs.length; i<len;i++) {
    globalFun.addEventHandler(reInputs[i], "blur", function() {
        let tipName = '.' + $(this).attr('name');
        let tipText = '.' + $(this).attr('name') + 'InputText';
        $(tipText).hide();
        if (reInputs[i].value) {
            $(tipName).siblings('.error').show();
            if ($(this).attr('name') != 'imageCaptcha') {
                validator.start(this);
            }
            else if ($(this).attr('name') == 'imageCaptcha' && reInputs[i].value.length < 5) {
                validator.start(this);
            }
        }
    })
}

for(let i=0,len=reInputs.length; i<len;i++) {
    globalFun.addEventHandler(reInputs[i], "keyup", function(e) {
       if(e.currentTarget.id =='mobileInput'){
           $('.errorMessage').hide();
       }

        let tipName = '.' + $(this).attr('name');
        let tipText = '.' + $(this).attr('name') + 'InputText';
        if (tipName === '.mobile') {
            $('#hiddenPhone').val(reInputs[i].value);
            if (reInputs[i].value.length == 11) {
                $(tipName).siblings('.error').show();
                $(tipText).hide();
                let errorMsg=validator.start(this);
                if (!errorMsg) testMobile = true;
            } else {
                $(tipName).siblings('.error').remove();
                $(tipText).show();
            }
            isDisabledButton();
        }
        else if (tipName === '.password') {
            if (reInputs[i].value.length > 5) {
                let isPwdValid = validate(reInputs[i].value);
                if (!isPwdValid) {
                    $(tipName).siblings('.error').remove();
                    $(this).removeClass('error');
                    $(tipText).hide();
                } else {
                    $(tipName).siblings('.error').remove();
                    $(tipText).show();
                }
            }
            isDisabledButton();
        }
        else if (tipName === '.imageCaptcha') {
             $('#hiddenCode').val(reInputs[i].value);
             if (reInputs[i].value.length == 5) {
                 $(tipText).hide();
                 isDisabledButton();
             }
        }
        else if (tipName === '.captcha') {
            if (reInputs[i].value.length == 6) {
                validator.start(this);
            }
            isDisabledButton();
        }
        // else if (tipName === '.referrer') {
        //     validator.start(this);
        //     isDisabledButton();
        // }
    })
}

for(let i=0,len=reInputs.length; i<len;i++) {
    let mobile=registerForm.mobile;
    globalFun.addEventHandler(reInputs[i], "focus", function() {
        let isMobileValid=!globalFun.hasClass(mobile,'error') && mobile.value;
        let isPwdValid = validate(reInputs[i].value);
        let tipName = '.' + $(this).attr('name');
        let tipText = '.' + $(this).attr('name') + 'InputText';
        $(tipName).siblings('.error').remove();
        if (tipName === '.mobile' && isMobileValid) {
            $(tipText).hide();
            return;
        }
        else if (tipName === '.password' && !isPwdValid) {
            $(tipText).hide();
            return;
        }
        else if (tipName === '.imageCaptcha') {
            receiveCodeBtn = false;
            $(tipText).show();
            return;
        }
        else if (tipName === '.captcha') {
            return;
        }
        $(tipText).show();
    })
}



//用来判断获取验证码和立即注册按钮 是否可点击
//表单验证通过会
function isDisabledButton() {
    let mobile=registerForm.mobile.value && registerForm.mobile.value.length == 11,
        password=!validate(registerForm.password.value) && registerForm.password.value.length > 5,
        referrer=registerForm.referrer,
        hasError=!$('.error').length,
        captchaLength=registerForm.captcha.value.length == 6;

    //按钮上有样式名count-downing，说明正在倒计时
    if ($fetchCaptcha.hasClass('count-downing')) {
        $fetchCaptcha.prop('disabled',true);
    }

    if($(referrer).is(':hidden')) {
        referrerValidBool=true;
    }

    let isDisabledSubmit= mobile && password && testCaptcha  && referrerValidBool && agreementValid && hasError && captchaLength;
    $registerSubmit.prop('disabled',!isDisabledSubmit);

}

//点击立即注册按钮
registerForm.onsubmit = function(event) {
    event.preventDefault();

    if($referrer.hasClass('error')) {
        $referrerOpen.trigger('click');
        return false;
    }
    else {
        registerForm.submit();
    }
};

//  图形验证码刷新
let isVoice = false;
let $imageCaptcha = $('.image-captcha');
refreshCode();
$imageCaptcha .on('click',function() {
    isVoice = false;
    refreshCode();
});

function refreshCode() {
    commonFun.refreshCaptcha($imageCaptcha[0],'/register/user/image-captcha');
};

//验证是否为纯数字
function validate(obj){
    var reg = /^[0-9]*$/;
    return reg.test(obj);

};

//图形验证码
$('#fetchCaptcha').on('click',function() {
    isVoice = false;
    getValidateCode();
});

//语音验证码
$('#voice_validate').on('click',function(){
    isVoice = true;
    getValidateCode();
});

function getValidateCode() {
    if ($("input[name='imageCaptcha']").val().length < 5) receiveCodeBtn = false;
    if (!receiveCodeBtn) {  // 防止重复点击
        receiveCodeBtn = true;
        let dom_phone = $("input[name='mobile']")[0];
        let dom_picCode = $("input[name='imageCaptcha']")[0];
        validator.start(dom_phone);
        if (testMobile) {
            $('.imageCaptchaInputText').hide();
            validator.start(dom_picCode);
        } else {
            receiveCodeBtn = false;
        }
    }
}






