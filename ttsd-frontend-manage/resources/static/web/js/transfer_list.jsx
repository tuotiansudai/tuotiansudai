require('webStyle/investment/transfer_list.scss');
require('webJsModule/coupon_alert');

let commonFun= require('publicJs/commonFun');
//投资计算器和意见反馈
require('webJsModule/red_envelope_float');

let $transferListBox=$('#transferListBox');
//我要投资菜单过滤显示
(function() {
	var $wrapperList=$('#wrapperList');
	var $showFilterBox=$wrapperList.find('li').filter(function(key) {
		return key>0;
	});
	$wrapperList.find('.show-more').on('click',function(event) {
		var $this=$(this),
			btnText;
		$this.toggleClass('ok');
		$showFilterBox.toggle();
		var isRetract=$this.hasClass('ok');
		if(isRetract) {
			//有样式名ok，展开状态
			btnText='收起 <i class="fa fa fa-angle-up"></i>';
		}
		else {
			//没有样式名ok，关闭状态
			btnText='更多 <i class="fa fa-angle-down"></i> ';
		}
		$this.html(btnText);
	});

	//判断页面刷新时是否需要开启或者关闭
	var activeLen=$showFilterBox.find('span').next('a').filter('.active').length,
		normalLen=$showFilterBox.length;
	if(activeLen!=normalLen) {
		//展开
		$wrapperList.find('.show-more').trigger('click');
	}

})();


$('.transfer-list-box ul li').on('click', function(event) {
	event.preventDefault();
	var $self = $(this),
		transferApplicationId = $self.attr('data-url-id'),
		transferStatus = $self.attr('data-url-status'),
		urldata = $self.attr('data-url');
	if(transferStatus == "SUCCESS"){
		location.href = urldata;
		return;
	}
	commonFun.useAjax({
		url: '/transfer/'+transferApplicationId+'/purchase-check',
		type: 'GET',
	},function(data) {

		if(data.message == "SUCCESS"){
			layer.open({
				title: '温馨提示',
				btn: ['确定'],
				content: '该项目已被承接，请选择其他项目。',
				btn1: function(index, layero){
					layer.closeAll()
					location.href = "/transfer-list";
				}
			});
		}else if(data.message == "CANCEL"){
			layer.open({
				title: '温馨提示',
				btn: ['确定'],
				content: '该项目已被取消，请选择其他项目。',
				btn1: function(index, layero){
					layer.closeAll();
					location.href = "/transfer-list";
				}
			});
		}else{
			location.href = urldata;
		}
	});
});

//分页跳转
$transferListBox.find('.pagination span[data-url]').on('click',function(event) {
	let target = event.target;
	if(/active/.test(target.className)) {
		location.href = $(target).data('url');
	}
});

//显示隐藏安全性管理告知
$('#noticeBtn').on('mouseover',function () {
    $('.notice-tips').show();
})
$('#noticeBtn').on('mouseout',function () {
    $('.notice-tips').hide();
})


