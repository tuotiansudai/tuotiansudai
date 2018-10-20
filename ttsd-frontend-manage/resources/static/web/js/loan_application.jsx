require('webStyle/investment/loan_application.scss');
require('publicJs/login_tip');
let ValidatorObj= require('publicJs/validator');
let commonFun= require('publicJs/commonFun');
let pcBanner = require('webImages/wantloan/top-images.png'),
	mobileBanner = require('webImages/wantloan/top-images-phone.jpg');

let $loanApplicationFrame=$('#loanApplicationFrame'),
	$topBanner=$('#topBanner');

var $loanTip = $('.loan-tip',$loanApplicationFrame);
$topBanner.find('.top-images').attr('src',pcBanner);
$topBanner.find('.top-images-phone').attr('src',mobileBanner);

//show tip
$loanTip.on('click', function(event) {
	event.preventDefault();
    if ($('.header-login').data('wechat-login-name')) {
        location.href = '/login?redirect=' + location.href;
        return;
    }
	var _title = $(this).attr('data-title'),
		_holder = $(this).attr('data-holder'),
		_type = $(this).attr('data-type');
	$.when(commonFun.isUserLogin())
		.done(function() {
			if ($('#userName').val() != '') {
                layer.open({
                    type: 1,
                    btn: 0,
                    area: ['auto', 'auto'],
                    title: '温馨提示',
                    content: $('#riskTip')
                });
				// layerTip(_title, _holder, _type);
			} else {
				layer.open({
					type: 1,
					btn: 0,
					area: ['auto', 'auto'],
					title: '温馨提示',
					content: $('#isUser')
				});
			}
		})
		.fail(function() {
			//判断是否需要弹框登陆
			layer.open({
				type: 1,
				title: false,
				closeBtn: 0,
				area: ['auto', 'auto'],
				content: $('#loginTip')
			});
		});
});

//我要借款表单验证
let loanForm=globalFun.$('#loanForm'),
	errorDom=$('.error-box',$(loanForm));
let validator = new ValidatorObj.ValidatorForm();
validator.add(loanForm.moneyText, [{
	strategy: 'isNonEmpty',
	errorMsg: '请填写借款金额'
},{
	strategy: 'isNumber',
	errorMsg: '请输入正确的借款金额'
},{
	strategy: 'minValue:1',
	errorMsg: '请输入不小于1的整数'
},{
	strategy: 'maxValue:100000000',
	errorMsg: '请输入不大于1亿的整数'
}]);

validator.add(loanForm.monthText, [{
	strategy: 'isNonEmpty',
	errorMsg: '请填写借款周期'
},{
	strategy: 'isNumber',
	errorMsg: '请输入不小于1的整数'
},{
	strategy: 'minValue:1',
	errorMsg: '请输入不小于1的整数'
},{
	strategy: 'maxValue:1000',
	errorMsg: '请输入不大于1千的整数'
}]);
validator.add(loanForm.infoText, [{
	strategy: 'isNonEmpty',
	errorMsg: '请填写信息'
},{
	strategy: 'maxLength:200',
	errorMsg: '字数限制200字以内'
}]);

let reloanInputs=$(loanForm).find('input:text');

Array.prototype.forEach.call(reloanInputs,function(el) {
	globalFun.addEventHandler(el,'blur',function() {
		let errorMsg = validator.start(this);
		event.preventDefault();
		if(errorMsg) {
			errorDom.text(errorMsg).css('visibility','visible');
		}
		else {
			errorDom.text('').css('visibility','hidden');
		}
	});
});

loanForm.onsubmit = function(event) {
	event.preventDefault();
	let errorMsg;
	for(let i=0,len=reloanInputs.length;i<len;i++) {
		errorMsg = validator.start(reloanInputs[i]);
		if(errorMsg) {
			errorDom.text(errorMsg).css('visibility','visible');
			break;
		}
	}
	if (!errorMsg) {
		var _data = {
			loginName: null,
			region: $('#placeText').attr("data-value"),
			amount: $('#moneyText').val(),
			period: $('#monthText').val(),
			pledgeInfo: $('#infoText').val(),
			pledgeType: $('#pledgeType').val()
		};
		commonFun.useAjax({
			url: '/loan-application/create',
			type: 'POST',
			dataType: 'json',
			data: JSON.stringify(_data),
			contentType: 'application/json; charset=UTF-8'
		},function(data) {
			if (data.data.status) {
				layer.closeAll();
				layer.open({
					type: 1,
					btn: 0,
					area: ['auto', 'auto'],
					title: '温馨提示',
					content: $('#successTip'),
					cancel: function() {
						loanForm.reset();
					}
				});
			}
		});
	}
}

$('body').on('click', '.area-bg', function(event) {
	event.preventDefault();
	var $self = $(this),
		$areaList = $self.siblings('.area-list-group');
	$areaList.slideToggle('fast');
}).on('click', '.area-list-group li', function(event) {
	event.preventDefault();
	var $self = $(this),
		text = $self.text(),
		value = $self.attr('data-value'),
		$parent = $self.parent(),
		$areaInt = $('#placeText');
	$('#placeText').val(text).attr('data-value', value);
	$parent.slideUp('fast');
}).on('click', '.close-btn', function(event) {
	event.preventDefault();
	if ($(this).hasClass('disabled')) return;
	layer.closeAll();
	$('#loanForm').find('.input-box').val('');
});

//tip code
function layerTip(title, holder, type) {
	$('#pledgeType').val(type);
	$('#infoText').attr('placeholder', holder);
	layer.open({
		type: 1,
		btn: 0,
		area: ['auto', 'auto'],
		title: [title, 'padding:0;text-align:center'],
		content: $('#homeTip'),
		cancel: function () {
			$('#loanForm').find('label.error').hide();
			$('#placeText').val('北京');
		}
	});
}

$('.risk-checkbox').on("click",function(){
    if($(this).prop("checked")){
        $('#risk-confirm-btn').removeClass('disabled');
    }else{
        $('#risk-confirm-btn').addClass('disabled');
    }
});
