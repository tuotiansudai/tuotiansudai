require("activityStyle/wechat/dragon_register.scss");
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');


let $wechatInvite = $('#wechatInvite'),
	registerForm = globalFun.$('#registerForm'),
	$registerSubmit=$('#registerSubmit'),
	validator = new ValidatorObj.ValidatorForm();



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




// 获取手机验证码
$('#getCaptchaBtn').on('touchstart', function (event) {
    var $this=$(this);
    event.preventDefault();
    if($this.prop('disabled')) {
        return;
    }
    $('#getCaptchaBtn').prop('disabled', true);

    commonFun.useAjax({
        url: '/register/user/send-register-captcha',
        type: 'POST',
        dataType: 'json',
        data: {mobile: registerForm.mobile.value}
    },function(response) {
        var data = response.data;
        var countdown = 60,timer;
        if (data.status && !data.isRestricted) {
            timer = setInterval(function () {
                $('#getCaptchaBtn').prop('disabled', true).text(countdown + '秒后重发');
                countdown--;
                if (countdown == 0) {
                    clearInterval(timer);
                    $('#getCaptchaBtn').prop('disabled', false).text('重新发送');
                }
            }, 1000);
            return;
        }
        if (!data.status && data.isRestricted) {
            layer.msg('短信发送频繁,请稍后再试');
        }

        if (!data.status && !data.isRestricted) {
            layer.msg('图形验证码错误');
        }
    });
});

//注册领取校验
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
},{
	strategy: 'isCaptchaValid',
	errorMsg: '验证码不正确'
}],true);

let reInputs=$(registerForm).find('input[validate]');
	
reInputs=Array.from(reInputs);
for (let el of reInputs) {
	globalFun.addEventHandler(el,"keyup", function() {
		validator.start(this);
		isDisabledRegister();
	});
}

//表单验证通过
function isDisabledRegister() {
    let mobile=registerForm.mobile,
        password=registerForm.password,
        captcha=registerForm.captcha;

    
    let isMobileValid=!globalFun.hasClass(mobile,'error') && mobile.value;
    let isPwdValid = !globalFun.hasClass(password,'error') && password.value;
    let captchaValid = !$(captcha).hasClass('error') && captcha.value;
    let isDisabledCaptcha = isMobileValid && isPwdValid;
    //获取验证码点亮
    isDisabledCaptcha && $('#getCaptchaBtn').prop('disabled',false);
    let isDisabledSubmit= isMobileValid && isPwdValid && captchaValid  && $('#agreementRegister').val()=='true';
    $registerSubmit.prop('disabled',!isDisabledSubmit);
}


isDisabledRegister();
//点击注册领取按钮
registerForm.onsubmit = function(event) {
    event.preventDefault();
    registerForm.submit();
}

//显示服务协议
$('.agree-text',$wechatInvite).on('click', function(event) {
	event.preventDefault();
	layer.open({
		type: 1,
		title: '拓天速贷服务协议',
		area: ['100%', '100%'],
		shadeClose: true,
		move: false,
		scrollbar: true,
		content: $('#agreementBox')
	});
});

//勾选同意协议
$('.icon-check',$wechatInvite).on('click', function(event) {
	event.preventDefault();
	let $self=$(this);
	if($self.hasClass('active')){
		$self.removeClass('active');
		$self.siblings('.agree-check').val(false);
	}else{
		$self.addClass('active');
		$self.siblings('.agree-check').val(true);
	}
	isDisabledRegister();
	isDisabledLogin();
});







