require('webStyle/account/account_common.scss');
require('webStyle/account/risk_estimate.scss');
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');
let $investmentBox=$('#investmentBox');
let $toResult=$('#toResult');

//选择评估选项
$investmentBox.on('click', '.problem-list dd span', function(event) {
	event.preventDefault();
	$(this).parent().addClass('active').siblings('dd').removeClass('active');
	$(this).parent().siblings('dt').hasClass('on')?$(this).parent().siblings('dt').removeClass('on'):false;
});

//查询评估结果
$toResult.on('click', function(event) {
	event.preventDefault();
	let $self=$(this),
		$problemList=$('.problem-list',$investmentBox),
		scoreArry=[];
	$problemList.find('dl').each(function(index, el) {
		if(!$(this).find('dd').hasClass('active')){
			$(this).find('dt').addClass('on');
		}else{
			scoreArry.push($(this).find('dd.active').attr('data-score'));
		}
	});
	if($('.problem-list dt.on').length>0){
		$('body,html').animate({scrollTop:$('.problem-list dt.on').eq(0).offset().top},'fast')
	}else{
		commonFun.useAjax({
		    url: '/risk-estimate',
		    data: {answers: scoreArry},
		    type: 'POST'
		},function(data) {
		    if(data.data.status){
		        location.href='/risk-estimate';
		    }
		});
	}
});

let metaViewPort = $('meta[name=viewport]');//
metaViewPort.remove()
$('head').prepend($('<meta name="viewport" content="width=1024,user-scalable=yes" />'));



