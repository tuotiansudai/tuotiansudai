require("activityStyle/wechat/dragon_login.scss");
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');


let $wechatInvite = $('#wechatInvite'),
	loginForm = globalFun.$('#loginForm'),
	$loginSubmit=$('#loginSubmit'),
	$changecode=$('.captcha-img',$wechatInvite),
	validator = new ValidatorObj.ValidatorForm();


function refreshCapt() {
    $('#captchaImg').attr('src','/register/user/image-captcha?' + new Date().getTime().toString());
};
refreshCapt();

//换一张图形验证码
$changecode.on('touchstart', function (event) {
    event.preventDefault();
    refreshCapt();
});




//登录领取校验
validator.add(loginForm.mobile, [{
	strategy: 'isNonEmpty',
	errorMsg: '手机号不能为空',
}, {
	strategy: 'isMobile',
	errorMsg: '手机号格式不正确'
},{
	strategy: 'isMobileExist',
	errorMsg: '手机号已经存在'
}],true);
validator.add(loginForm.password, [{
    strategy: 'isNonEmpty',
    errorMsg: '密码不能为空'
}, {
    strategy: 'checkPassword',
    errorMsg: '密码为6位至20位，不能全是数字'
}],true);
validator.add(loginForm.appCaptcha, [{
	strategy: 'isNonEmpty',
	errorMsg: '验证码不能为空'
}],true);


let reInputs='';
reInputs=$(loginForm).find('input[validate]');
reInputs=Array.from(reInputs);
for (let el of reInputs) {
	globalFun.addEventHandler(el,"keyup", function() {
		validator.start(this);
		isDisabledLogin();
	});
}

//表单验证通过
function isDisabledLogin() {
    let mobile=loginForm.mobile,
        password=loginForm.password,
        appCaptcha=loginForm.appCaptcha;

    //获取验证码点亮
    let isMobileValid=!globalFun.hasClass(mobile,'error') && mobile.value;
    let isPwdValid = !globalFun.hasClass(password,'error') && password.value;
    let captchaValid = !$(appCaptcha).hasClass('error') && appCaptcha.value;

    let isDisabledSubmit= isMobileValid && isPwdValid && captchaValid  && $('#agreementLogin').val()=='true';
    $loginSubmit.prop('disabled',!isDisabledSubmit);
}
isDisabledLogin();

//点击登录领取按钮
loginForm.onsubmit = function(event) {
    event.preventDefault();
    loginForm.submit();
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






