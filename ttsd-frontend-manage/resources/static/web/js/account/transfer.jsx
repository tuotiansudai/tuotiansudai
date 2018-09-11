//债券转让
require('webStyle/account/transfer.scss');
require('webJsModule/coupon_alert');
require('publicJs/pagination');
let commonFun= require('publicJs/commonFun');
let $myTransferCon = $('#myTransferCon');
var activeIndex=$('.filters-list li.active').index(),
	$ruleList = $('#ruleList'),
	$paginationElement = $('.pagination');
let $turnInvestorDOM_TRANSFERABLE = $('#turnInvestorDOM_TRANSFERABLE');//切换成投资人
let $turnInvestorDOM_TRANSFERRING = $('#turnInvestorDOM_TRANSFERRING');//切换成投资人
let isLoaner = $myTransferCon.data('loaner-role');

//template data to page and generate pagenumber
function loadLoanData(currentPage) {
	var status = $('.filters-list li.active').attr('data-status').split(',');
	var requestData = {status: status, index: currentPage || 1};
		$paginationElement.loadPagination(requestData, function (data) {
			let html;
			if(activeIndex==0){
				//获取模版内容
				let $transferableListTemplate=$('#transferableListTemplate'),
					transferableListTpl=$transferableListTemplate.html();

				// 解析模板, 返回解析后的内容
				let render = _.template(transferableListTpl);
				html = render(data);

			}else if(activeIndex==1){

				//获取模版内容
				let $transferrerListTemplate=$('#transferrerListTemplate'),
					transferrerListTpl=$transferrerListTemplate.html();

				// 解析模板, 返回解析后的内容
				let render = _.template(transferrerListTpl);
				html = render(data);

			}else{

				//获取模版内容
				let $RecordTemplate=$('#transferrerRecordTemplate'),
					recordTpl=$RecordTemplate.html();

				// 解析模板, 返回解析后的内容
				let render = _.template(recordTpl);
				html = render(data);
			}

			$('.list-container .record-list.active').html(html);
		});

	$('.list-container').on('mouseenter','.project-name',function() {
		// show tip by mouseenter
		layer.closeAll('tips');
		if($.trim($(this).text()).length>15){
			layer.tips($(this).text(), $(this), {
				tips: [1, '#efbf5c'],
				time: 2000,
				tipsMore: true,
				area: 'auto',
				maxWidth: '500'
			});
		}
	});

}
loadLoanData();

$('body').on('click', '.cancel-btn' ,function(event) {
//click cancel btn
	event.preventDefault();
    if(isLoaner) {
        layer.open({
            type: 1,
            move: false,
            offset: "200px",
            title: '温馨提示',
            area: ['490px', '220px'],
            shadeClose: false,
            closeBtn:0,
            content: $turnInvestorDOM_TRANSFERRING
        });
        return false;
    }
	var $self=$(this),
		transferApplicationId=$self.data('transfer-application-id');

	layer.open({
		title: '温馨提示',
		type:1,
		btn:['再想想','确定'],
		area: ['400px'],
		content: '<p class="tc pad-m">您确定取消该笔债权的转让？</p>',
		btn1:function(){
			layer.closeAll();
		},
		btn2:function(){
			commonFun.useAjax({
				url: '/transfer/application/'+transferApplicationId+'/cancel',
				type: 'POST'
			},function(data) {
				data==true?location.reload():layer.msg('取消失败，请重试！');
			});
		}
	});
})
	.on('click', '.apply-transfer', function(event) {
		//click apply btn
		event.preventDefault();

        if(isLoaner) {
            layer.open({
                type: 1,
                move: false,
                offset: "200px",
                title: '温馨提示',
                area: ['490px', '220px'],
                shadeClose: false,
                closeBtn:0,
                content: $turnInvestorDOM_TRANSFERABLE
            });
            return false;
		}
		var $self=$(this),
			investId=$self.data('invest-id');
		commonFun.useAjax({
			url: '/transfer/invest/' + investId + '/is-transferable',
			type: 'GET'
		},function(response) {
			if(response.data.status==true) {
				location.href='/transfer/invest/' + investId + '/apply';
			} else {
				layer.open({
					title: '温馨提示',
					btn:['确定'],
					area: ['400px'],
					content: '<p class="tc">'+response.data.message+'</p>',
					btn1:function(){
						layer.closeAll();
					}
				});
			}
		});
	})
	//close tip dom
	.on('click', '.close-btn', function(event) {
		event.preventDefault();
		$ruleList.fadeOut('fast');
	})
	//show tip dom
	.on('click', '.rule-show', function(event) {
		event.preventDefault();
		$ruleList.fadeIn('fast');
	});
$('.btn-close').on('click',function () {
    layer.closeAll();
})
