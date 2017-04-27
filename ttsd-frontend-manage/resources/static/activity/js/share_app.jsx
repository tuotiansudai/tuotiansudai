require('activityStyle/share_app.scss');
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');

let $shareAppContainer = $('#shareAppContainer'),
	registerForm = globalFun.$('#registerForm'),
	$fetchCaptcha = $('#getCaptchaBtn');

//判断终端是ios 还是 android
let equipment = globalFun.equipment(),
	isIos = equipment.ios,
	isAndroid = equipment.android;

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

//Android注册的时候有密码
if(isAndroid) {
	validator.add(registerForm.password, [{
		strategy: 'isNonEmpty',
		errorMsg: '密码不能为空'
	}, {
		strategy: 'checkPassword',
		errorMsg: '密码为6位至20位，不能全是数字'
	}],true);
}

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
	globalFun.addEventHandler(el,"keyup", "blur", function() {
		validator.start(this);
	});
}

//获取手机验证码
function getCaptcha() {
	let  mobile = registerForm.mobile,
		 mobileVal = mobile.value;
	if(!mobileVal || /error/.test(mobile.className)) {
		return;
	}
	commonFun.useAjax({
		url: '/register/user/' + mobileVal + '/send-register-captcha',
		type:'GET'
	},function(responseData) {
		$fetchCaptcha.prop('disabled',false);

		let data = responseData.data;
		if (data.status && !data.isRestricted) {
			//获取手机验证码成功，关闭弹框，并开始倒计时
			layer.closeAll();
			commonFun.countDownLoan({
				btnDom:$fetchCaptcha,
				textCounting:'s'
			});

		} else if (!data.status && data.isRestricted) {
			layer.msg('短信发送频繁,请稍后再试');
		}
	});
}

let formSubmit = {
	android:function() {
		commonFun.useAjax({
			url: '/register/user/shared',
			type: 'POST',
			dataType: 'json',
			data: {
				mobile: $('#mobile').val(),
				password: $('#password').val(),
				captcha: $('#captcha').val(),
				referrer: JSON.parse('{"' + decodeURI(location.search.substring(1)).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}')["referrerMobile"],
				agreement: $('#agreement').attr("checked") == "checked"
			}
		}, function (data) {
			if (data.data.status == true) {
				location.href = '/activity/app-share/success?referrerMobile=' + location.href.split('referrerMobile=')[1];
			} else {
				layer.msg('请求失败，请重试！');
			}
		})
	},
	ios:function() {
		commonFun.useAjax({
			url: '/register/user/shared-prepare',
			type: 'POST',
			dataType: 'json',
			data: {
				mobile: $('#mobile').val(),
				captcha: $('#captcha').val(),
				referrerMobile: JSON.parse('{"' + decodeURI(location.search.substring(1)).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}')["referrerMobile"]
			}
		},function(data) {
			if (data.data.status == true) {
				location.href = '/activity/app-share/success?referrerMobile=' + location.href.split('referrerMobile=')[1];
			} else {
				layer.msg('请求失败，请重试！');
			}
		});
	}
}

$fetchCaptcha.on('click',function() {
	getCaptcha();
})

//点击立即注册按钮
registerForm.onsubmit = function(event) {
	event.preventDefault();
	let errorMsg;
	for(let i=0,len=reInputs.length;i<len;i++) {
		errorMsg=validator.start(reInputs[i]);
		if(errorMsg) {
			break;
		}
	}
	if(!errorMsg) {
		// registerForm.submit();
	}
}


$('#agreeRule').on('click', function(event) {
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

var $androidForm = $('#androidForm'),
	$iosForm = $('#iosForm'),
	$androidBtn = $('#androidBtn'),
	$iosBtn = $('#iosBtn'),
	countdown = 60,
	sendSms = {
		androidCount: function() { //安卓倒计时
			timer = setInterval(function() {
				$androidBtn.prop('disabled', true).val(countdown + 's');
				countdown--;
				if (countdown == 0) {
					clearInterval(timer);
					countdown = 60;
					$androidBtn.prop('disabled', false).val('重新发送');
				}
			}, 1000);
		},
		anCaptcha: function(state) { //安卓-获取手机验证码
			if (state == false) {
				commonFun.useAjax({
					url: '/register/user/' + $('#mobile').val() + '/send-register-captcha',
					type: 'get',
					dataType: 'json'

				})
					.done(function(data) {
						if (data.data.status && !data.data.isRestricted) {
							sendSms.androidCount();
						} else if (!data.data.status && data.data.isRestricted) {
							layer.msg('短信发送频繁,请稍后再试');
						}
					})
					.fail(function() {
						layer.msg('请求失败，请重试！');
					});
			}else {
				var param = JSON.parse('{"' + decodeURI(location.search.substring(1)).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g,'":"') + '"}')
				location.href = '/activity/app-share/success?referrerMobile=' + param["referrerMobile"]+'&mobile='+$('#mobile').val();
			}
		},
		iosCount: function() { //ios倒计时
			timer = setInterval(function() {
				$iosBtn.prop('disabled', true).val(countdown + 's');
				countdown--;
				if (countdown == 0) {
					clearInterval(timer);
					countdown = 60;
					$iosBtn.prop('disabled', false).val('重新发送');
				}
			}, 1000);
		},
		iosCaptcha: function(state) { //ios-获取手机验证码
			if (state == false) {
				commonFun.useAjax({
					url: '/register/user/' + $('#mobile').val() + '/send-register-captcha',
					type: 'get',
					dataType: 'json'
				})
					.done(function(data) {
						if (data.data.status && !data.data.isRestricted) {
							sendSms.iosCount();
						} else if (!data.data.status && data.data.isRestricted) {
							layer.msg('短信发送频繁,请稍后再试');
						}
					})
					.fail(function() {
						layer.msg('请求失败，请重试！');
					});
			}else {
				var param = JSON.parse('{"' + decodeURI(location.search.substring(1)).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g,'":"') + '"}')
				location.href = '/activity/app-share/success?referrerMobile=' + param["referrerMobile"] +'&mobile='+$('#mobile').val();
			}
		}
	};


// isphone validate
jQuery.validator.addMethod("isPhone", function(value, element) {
	var tel = /0?(13|14|15|18|17)[0-9]{9}/;
	return this.optional(element) || (tel.test(value));
}, "请正确填写您的手机号码");

//android validate && submit data
$androidForm.validate({
	debug: true,
	focusInvalid: false,
	ignore: '.ignore',
	rules: {
		mobile: {
			required: true,
			digits: true,
			isPhone: true,
			minlength: 11,
			maxlength: 11,
			isExist: '/register/user/mobile/{0}/is-exist'
		},
		password: {
			required: true,
			regex: /^(?=.*[^\d])(.{6,20})$/
		},
		captcha: {
			required: true,
			digits: true,
			maxlength: 6,
			minlength: 6,
			captchaVerify: {
				param: function() {
					var mobile = $('#mobile').val();
					return "/register/user/mobile/" + mobile + "/captcha/{0}/verify"
				}
			}
		},
		agreement: {
			required: true
		}
	},
	messages: {
		mobile: {
			required: '请输入手机号',
			digits: '必须是数字',
			minlength: '手机格式不正确',
			isPhone: '请输入正确的手机号码',
			maxlength: '手机格式不正确',
			isExist: '手机号已存在'
		},
		password: {
			required: "请输入密码",
			regex: '6位至20位，不能全是数字'
		},
		captcha: {
			required: '请输入手机验证码',
			digits: '验证码格式不正确',
			maxlength: '验证码格式不正确',
			minlength: '验证码格式不正确',
			captchaVerify: '验证码不正确'
		},
		agreement: '请同意服务协议'
	},
	errorPlacement: function(error, element) {
		error.appendTo(element.parent());
	},
	submitHandler: function(form) {



	}
});

//ios validate && submit data
$iosForm.validate({
	debug: true,
	focusInvalid: false,
	ignore: '.ignore',
	rules: {
		mobile: {
			required: true,
			digits: true,
			isPhone: true,
			minlength: 11,
			maxlength: 11,
			isExist: '/register/user/mobile/{0}/is-exist'
		},
		captcha: {
			required: true,
			digits: true,
			maxlength: 6,
			minlength: 6,
			captchaVerify: {
				param: function() {
					var mobile = $('#mobile').val();
					return "/register/user/mobile/" + mobile + "/captcha/{0}/verify"
				}
			}
		},
		agreement: {
			required: true
		}
	},
	messages: {
		mobile: {
			required: '请输入手机号',
			digits: '必须是数字',
			minlength: '手机格式不正确',
			isPhone: '请输入正确的手机号码',
			maxlength: '手机格式不正确',
			isExist: '手机号已存在'
		},
		captcha: {
			required: '请输入手机验证码',
			digits: '验证码格式不正确',
			maxlength: '验证码格式不正确',
			minlength: '验证码格式不正确',
			captchaVerify: '验证码不正确'
		},
		agreement: '请同意服务协议'
	},
	errorPlacement: function(error, element) {
		error.appendTo(element.parent());
	},
	submitHandler: function(form) {

	}
});

//get code event
$androidBtn.on('click', function(event) {
	event.preventDefault();
	if($('#mobile').val()!='' && /0?(13|14|15|18|17)[0-9]{9}/.test($('#mobile').val())){
		commonFun.useAjax({
			url: '/register/user/mobile/' + $('#mobile').val() + '/is-register', //获取手机验证码接口
			type: 'get',
			dataType: 'json'
		})
			.done(function(data) {
				sendSms.anCaptcha(data.data.status);
			})
			.fail(function(data) {
				layer.msg('请求失败，请重试');
			});
	}
});

//get code event
$iosBtn.on('click', function(event) {
	event.preventDefault();
	if($('#mobile').val()!='' && /0?(13|14|15|18|17)[0-9]{9}/.test($('#mobile').val())){
		commonFun.useAjax({
			url: '/register/user/mobile/' + $('#mobile').val() + '/is-register', //判断手机号是否存在
			type: 'get',
			dataType: 'json'
		})
			.done(function(data) {
				sendSms.iosCaptcha(data.data.status);
			})
			.fail(function(data) {
				layer.msg('请求失败，请重试');
			});
	}
});


