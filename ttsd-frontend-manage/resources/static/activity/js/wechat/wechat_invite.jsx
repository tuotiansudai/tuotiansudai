require("activityStyle/wechat/wechat_invite.scss");
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');


let $wechatInvite = $('#wechatInvite'),
	registerForm = globalFun.$('#registerForm'),
	$fetchCaptcha = $('#getCaptchaBtn');
let validator = new ValidatorObj.ValidatorForm();

let redBag=require('../../images/dragon-boat/red-bag.png'),
	$redBag=$('#redBag');

$redBag.attr('src',redBag);

//验证码是否正确
// validator.newStrategy(registerForm.captcha,'isCaptchaValid',function(errorMsg,showErrorAfter) {
// 	var getResult='',
// 		that=this,
// 		_arguments=arguments;

// 	var _phone = registerForm.mobile.value,
// 		_captcha=registerForm.captcha.value;

// 	//先判断手机号格式是否正确
// 	if(!/(^1[0-9]{10}$)/.test(_phone)) {
// 		return;
// 	}
// 	commonFun.useAjax({
// 		type:'GET',
// 		async: false,
// 		url:`/register/user/mobile/${_phone}/captcha/${_captcha}/verify`
// 	},function(response) {
// 		if(response.data.status) {
// 			// 如果为true说明验证码正确
// 			getResult='';
// 			ValidatorObj.isHaveError.no.apply(that,_arguments);
// 		}
// 		else {
// 			getResult=errorMsg;
// 			ValidatorObj.isHaveError.yes.apply(that,_arguments);
// 		}
// 	});
// 	return getResult;
// });

// validator.add(registerForm.mobile, [{
// 	strategy: 'isNonEmpty',
// 	errorMsg: '手机号不能为空',
// }, {
// 	strategy: 'isMobile',
// 	errorMsg: '手机号格式不正确'
// },{
// 	strategy: 'isMobileExist',
// 	errorMsg: '手机号已经存在'
// }],true);

// validator.add(registerForm.captcha, [{
// 	strategy: 'isNonEmpty',
// 	errorMsg: '验证码不能为空'
// },{
// 	strategy: 'isNumber:6',
// 	errorMsg: '验证码为6位数字'
// },{
// 	strategy: 'isCaptchaValid',
// 	errorMsg: '验证码不正确'
// }],true);


// let reInputs=$(registerForm).find('input[validate]');
// reInputs=Array.from(reInputs);
// for (let el of reInputs) {
// 	globalFun.addEventHandler(el,"keyup", function() {
// 		validator.start(this);
// 	});
// }

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






