require('webStyle/investment/loan_application.scss');
require('publicJs/login_tip');
let ValidatorObj= require('publicJs/validator');
let commonFun= require('publicJs/commonFun');
let pcBanner = require('webImages/wantloan/top-images.png'),
	mobileBanner = require('webImages/wantloan/top-images-phone.jpg');

let $loanApplicationFrame=$('#loanApplicationFrame'),
	$topBanner=$('#topBanner');

let pageTitle = '';

var $loanTip = $('.loan-tip',$loanApplicationFrame);
$topBanner.find('.top-images').attr('src',pcBanner);
$topBanner.find('.top-images-phone').attr('src',mobileBanner);

//show tip
$loanTip.on('click', function(event) {
	event.preventDefault();
	let $this = $(this);
    if ($('.header-login').data('wechat-login-name')) {
        location.href = '/login?redirect=' + location.href;
        return;
    }
    pageTitle = $(this).data('type');
	$.when(commonFun.isUserLogin())
		.done(function() {
			if ($this.hasClass('consume') && $('#isAuthenticationRequired').val()==='false') {
                layer.open({
                    type: 1,
                    btn: 0,
                    area: ['auto', 'auto'],
                    title: '温馨提示',
                    content: $('#toAnXinSign')
                });
			}
			else {
                if ($('#userName').val() != '') {
                    var ifChecked = $('.risk-checkbox').prop('checked');
                    if (ifChecked) {
                        $('#risk-confirm-btn').removeClass('disabled');
                    }
                    else {
                        $('#risk-confirm-btn').addClass('disabled');
                    }
                    layer.open({
                        type: 1,
                        btn: 0,
                        area: ['auto', 'auto'],
                        title: '温馨提示',
                        content: $('#riskTip')
                    });
                } else {
                    layer.open({
                        type: 1,
                        btn: 0,
                        area: ['auto', 'auto'],
                        title: '温馨提示',
                        content: $('#isUser')
                    });
                }
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
	layer.closeAll();
	$('#loanForm').find('.input-box').val('');
});

$('.risk-checkbox').on("click",function(){
    if($(this).prop("checked")){
        $('#risk-confirm-btn').removeClass('disabled');
    }else{
        $('#risk-confirm-btn').addClass('disabled');
    }
});

$('.risk-confirm-btn').on('click',function () {
    if ($(this).hasClass('disabled')) return;
    location.href = '/loan-application/borrow-apply?type=' + pageTitle;
});
