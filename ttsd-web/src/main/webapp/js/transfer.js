require(['jquery', 'mustache', 'text!/tpl/transfer-transferable-table.mustache','text!/tpl/transferrer-transfer-application-table.mustache','text!/tpl/transferrer-transfer-record-table.mustache', 'pagination', 'layerWrapper', 'jquery.ajax.extension'], function ($, Mustache, transferableListTemplate, transferrerListTemplate,transferrerRecordTemplate,pagination, layer) {
	$(function() {
		var activeIndex=$('.filters-list li.active').index(),
			$paginationElement = $('.pagination');
		
		function loadLoanData(currentPage) { //template data to page and generate pagenumber
			var status = $('.filters-list li.active').attr('data-status').split(',');
			var requestData = {status: status, index: currentPage || 1};
			$paginationElement.loadPagination(requestData, function (data) {
				if(activeIndex==0){
					var html = Mustache.render(transferableListTemplate, data);
				}else if(activeIndex==1){
					var html = Mustache.render(transferrerListTemplate, data);
				}else{
					var html = Mustache.render(transferrerRecordTemplate, data);
				}

				$('.list-container .record-list.active').html(html);
			});
			$('.list-container').on('mouseenter','.project-name',function() {// show tip by mouseenter
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

		};
		loadLoanData();
		
		$('body').on('click', '.cancel-btn' ,function(event) {//click cancle btn
			event.preventDefault();
			var $self=$(this),
				urlData=$self.attr('data-link');

			layer.open({
			  title: '温馨提示',
			  btn:['再想想','确定'],
			  skin: 'demo-class',
			  area: ['400px', '180px'],
			  content: '<p class="tc">您确定取消该笔债权的转让？</p>',
			  btn1:function(){
			  	layer.closeAll();
			  },
			  btn2:function(){
			  	$.ajax({
					url: '/transfer/application/'+urlData+'/cancel',
					type: 'POST',
					dataType: 'json'
				})
				.done(function(data) {
					data==true?location.reload():layer.msg('取消失败，请重试！');
				})
				.fail(function() {
					layer.msg('请求失败，请重试！');
				});
			  }
			});
		})
		.on('click', '.apply-transfer', function(event) {//click apply btn
			event.preventDefault();
			var $self=$(this),
				applyId=$self.attr('data-applyId'),
				urlData=$self.attr('data-link');
			$.ajax({
				url: '/transfer/application/'+urlData+'/isAllowTransfer',
				type: 'POST',
				dataType: 'json'
			})
			.done(function(data) {
				if(data.status==true){
					location.href='/transfer/application/'+applyId+'/apply';
				}else{
					layer.open({
					  title: '温馨提示',
					  btn:['确定'],
					  area: ['400px', '180px'],
					  content: '<p class="tc">'+data.message+'</p>',
					  btn1:function(){
					  	layer.closeAll();
					  }
					});
				}
			})
			.fail(function() {
				layer.msg('请求失败，请重试！');
			});
		});
	});
});
