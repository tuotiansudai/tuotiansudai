require(['jquery', 'mustache', 'text!/tpl/transfer-transferable-table.mustache','text!/tpl/transferrer-transfer-application-table.mustache','text!/tpl/transferrer-transfer-record-table.mustache', 'pagination', 'layerWrapper', 'jquery.ajax.extension'], function ($, Mustache, transferableListTemplate, transferrerListTemplate,transferrerRecordTemplate,pagination, layer) {
	$(function() {
		var activeIndex=$('.filters-list li.active').index(),
			$paginationElement = $('.pagination');
		
		function loadLoanData(currentPage) {
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
			$('.list-container').on('mouseenter','.project-name',function() {
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

		$('body').on('click', '.cancel-btn' ,function(event) {
			event.preventDefault();
			var $self=$(this),
				urlData=$self.attr('data-link');
			$.ajax({
				url: '/transfer/application/'+urlData+'/cancel',
				type: 'POST',
				dataType: 'json'
			})
			.done(function(data) {
				console.log(data);
			})
			.fail(function() {
				layer.msg('请求失败，请重试！');
			});
			
		});

	});

});
