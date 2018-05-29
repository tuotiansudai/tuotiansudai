require('activityStyle/wechat/share_app.scss');
require('activityStyle/module/app_register_reason.scss');
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');
let $shareAppContainer = $('#shareAppContainer'),
	registerForm = globalFun.$('#registerForm'),
	$fetchCaptcha = $('#getCaptchaBtn');


let validator = new ValidatorObj.ValidatorForm();
let referrerPerson = JSON.parse('{"' + decodeURI(location.search.substring(1)).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}')["referrerMobile"];
$('#recommender').html(referrerPerson);

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
for(let i=0,len=reInputs.length; i<len;i++) {
	globalFun.addEventHandler(reInputs[i],"keyup", function() {
		validator.start(this);
	});
}

let shareAppFun = {

	//获取手机验证码
	getCaptcha:function() {
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
	},
	//验证成功后的调用的函数
	submitForm:function() {
		let surl,
			paramObj={};
		let referrerMobile = JSON.parse('{"' + decodeURI(location.search.substring(1)).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}')["referrerMobile"];
		paramObj.mobile = registerForm.mobile.value;
		paramObj.captcha = registerForm.captcha.value;


		surl = '/register/user/shared';
		paramObj.password = registerForm.password.value;
		paramObj.referrer = referrerMobile;
		paramObj.agreement = $('#agreement').prop('checked');
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
	},
	//判断是否真的注册
	isRegister:function() {
		commonFun.useAjax({
			url: '/register/user/mobile/' + $('#mobile').val() + '/is-register',
			type: 'GET'
		},function(response) {
			let data = response.data;
			if(data.status) {
				let param = JSON.parse('{"' + decodeURI(location.search.substring(1)).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g,'":"') + '"}')
				location.href = '/activity/app-share/success?referrerMobile=' + param["referrerMobile"] +'&mobile='+$('#mobile').val();
			} else {
				shareAppFun.getCaptcha();
			}
		})
	}
}

$fetchCaptcha.on('click',function() {
	//判断手机号 和密码都正确
	let mobileCls = registerForm.mobile.className;
	let passwordCls = registerForm.password.className;
	// let mobileVal = registerForm.mobile.value;
	// let passwordVal = registerForm.password.value;
    let errorMsg;
    for(let i=0,len=2;i<len;i++) {
        errorMsg=validator.start(reInputs[i]);
        if(errorMsg) {
            return;
        }
    }
    if(/valid/.test(mobileCls) && /valid/.test(passwordCls)) {
		//手机号和密码都有效
		shareAppFun.isRegister();
	}

});

//点击立即注册按钮
registerForm.onsubmit = function(event) {
	event.preventDefault();
	let errorMsg;
	for(let i=0,len=reInputs.length;i<len;i++) {
		errorMsg=validator.start(reInputs[i]);
		if(errorMsg) {
			return;
		}
	}
	let ifChecked = registerForm.agreement.checked;
	if (!ifChecked) {
		$('.noAgree').css('display','block');
		return;
	};
	if(!errorMsg) {
		shareAppFun.submitForm();
	}
};

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

$('#agreement').on('click',() => {
	let ifChecked = registerForm.agreement.checked;
	if(ifChecked) $('.noAgree').css('display','none');
});




