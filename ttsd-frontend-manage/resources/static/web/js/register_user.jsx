require('publicStyle/module/register_png.scss');
require('webStyle/register.scss');
let commonFun=require('publicJs/commonFun');
let ValidatorForm= require('publicJs/validator');

let registerForm=globalFun.$('#registerUserForm'); //注册的form
let imageCaptchaForm=globalFun.$('#imageCaptchaForm'); //获取验证码的form
let $fetchCaptcha=$('#fetchCaptcha');
let $registerSubmit=$('input[type="submit"]',$(registerForm));
let  $referrerOpen=$('.referrer-open',$(registerForm));
let $referrer=$('input.referrer', $(registerForm));
let agreementValid=true,
    referrerValidBool=true;

require.ensure(['publicJs/placeholder'], function(require){
    require('publicJs/placeholder');
    $('input[type="text"],input[type="password"]',$(registerForm)).placeholder();
},'placeholder');

//点击获取验证码按钮
require.ensure(['publicJs/fetch_captcha'], function(require){
    let fetchCaptchaFun=require('publicJs/fetch_captcha');
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
        iconArrow[0].className=checkOption?'sprite-register-arrow-bottom':'sprite-register-arrow-right';
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
            $cIcon[0].className='sprite-register-no-checked';
            agreementValid=false;
        }
        else {
            $this.addClass('checked');
            $agreement.prop('checked',true);
            $cIcon[0].className='sprite-register-yes-checked';
            agreementValid=true;
        }
        isDisabledButton();
    });

    $showAgreement.click(function () {
        layer.open({
            type: 1,
            title: '拓天速贷服务协议',
            area: ['950px', '600px'],
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
let validator = new ValidatorForm();
//推荐人是非存在
validator.newStrategy(registerForm.referrer,'isReferrerExist',function(errorMsg,showErrorAfter) {
    var getResult='',
        that=this,
        _arguments=arguments;
    //只验证推荐人是否存在，不验证是否为空
    if(this.value=='') {
        getResult='';
        commonFun.isHaveError.no.apply(that,_arguments);
        return '';
    }
    commonFun.useAjax({
        type:'GET',
        async: false,
        url:'/register/user/referrer/'+this.value+'/is-exist'
    },function(response) {
        if(response.data.status) {
            // 如果为true说明推荐人存在
            getResult='';
            commonFun.isHaveError.no.apply(that,_arguments);
        }
        else {
            getResult=errorMsg;
            commonFun.isHaveError.yes.apply(that,_arguments);
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
validator.add(registerForm.captcha, [{
    strategy: 'isNonEmpty',
    errorMsg: '验证码不能为空'
},{
    strategy: 'isNumber:6',
    errorMsg: '验证码为6位数字'
}],true);

validator.add(registerForm.referrer, [{
    strategy: 'isReferrerExist',
    errorMsg: '推荐人不存在'
}],true)


let reInputs=$(registerForm).find('input:text,input:password');

reInputs=Array.from(reInputs);
for (var el of reInputs) {
    globalFun.addEventHandler(el,"keyup", function() {
        let errorMsg=validator.start(this);
        if(this.name=='referrer' && errorMsg) {
            referrerValidBool=false;
        }
        else {
            referrerValidBool=true;
        }
        isDisabledButton();
    })
}

//用来判断获取验证码和立即注册按钮 是否可点击
function isDisabledButton() {
    let mobile=registerForm.mobile,
        password=registerForm.password,
        captcha=registerForm.captcha,
        referrer=registerForm.referrer;

    //获取验证码点亮
    let isMobileValid=!globalFun.hasClass(mobile,'error') && mobile.value;
    let isPwdValid = !globalFun.hasClass(password,'error') && password.value;

    let isDisabledCaptcha = isMobileValid && isPwdValid;

    //按钮上有样式名count-downing，说明正在倒计时
    if ($fetchCaptcha.hasClass('count-downing')) {
        $fetchCaptcha.prop('disabled',true);
    }
    else {
        $fetchCaptcha.prop('disabled',!isDisabledCaptcha);
    }
    //给验证码弹框中的mobile隐藏域赋值
    isDisabledCaptcha && (globalFun.$('#imageCaptchaForm').mobile.value = mobile.value);
    //通过获取验证码按钮来判断
    !isDisabledCaptcha && $registerSubmit.prop('disabled',true);

    let captchaValid = !$(captcha).hasClass('error') && captcha.value;

    if($(referrer).is(':hidden')) {
        referrerValidBool=true;
    }

    let isDisabledSubmit= isMobileValid && isPwdValid && captchaValid  && referrerValidBool && agreementValid;
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
}

