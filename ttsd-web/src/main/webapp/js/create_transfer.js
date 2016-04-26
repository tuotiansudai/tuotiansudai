require(['jquery', 'mustache', 'text!/tpl/transferrer-transfer-application-table.mustache', 'pagination', 'layerWrapper', 'jquery.ajax.extension'], function ($, Mustache, transferrerListTemplate, pagination, layer) {
	//$(function() {
	//	//var $filtersList=$('.filters-list li');
	//	//$filtersList.on('click', function(event) {
	//	//	event.preventDefault();
	//	//	var $self=$(this),
	//	//		$recordList=$('.list-container'),
	//	//		index=$self.index();
	//	//	$self.addClass('active').siblings().removeClass('active');
	//	//	$recordList.find('.record-list:eq('+index+')').addClass('active').siblings().removeClass('active');
	//	//});
    //
	//});

	var paginationElement = $('.pagination');
	var loadLoanData = function (currentPage) {
		var status = $('.status-filter .select-item.current').data('status');

		var requestData = {startTime: startTime, endTime: endTime, status: status, index: currentPage || 1};

		paginationElement.loadPagination(requestData, function (data) {
			var html = Mustache.render(investListTemplate, data);
			$('.invest-list').html(html);
			$('.invest-list .show-invest-repay').click(function () {
				$.ajax({
					url: $(this).data('url'),
					type: 'get',
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8'
				}).success(function (response) {
					var data = response.data;
					data.isLoanCompleted = status == 'COMPLETE';
					data.csrfToken = $("meta[name='_csrf']").attr("content");
					if (data.status) {
						_.each(data.records, function (item) {
							data.loanId = item.loanId;
							switch (item.loanRepayStatus) {
								case 'REPAYING':
									item.status = '待还';
									break;
								case 'COMPLETE':
									item.status = '完成';
									break;
								case 'CANCEL':
									item.status = '流标';
									break;
							}
						});
						var html = Mustache.render(investRepayTemplate, data);

						layer.open({
							type: 1,
							title: false,
							offset: '80px',
							area: ['1000px'],
							shadeClose: true,
							content: html
						});

					}
				});
			});
		});
		$('.invest-list').on('mouseenter','.project-name',function() {
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
		$('.invest-list').on('mouseenter','.birth-icon',function() {
			layer.closeAll('tips');
			var num = parseFloat($(this).attr('data-benefit'));
			var benefit = num + 1;
			layer.tips('您已享受生日福利，首月收益翻'+benefit+'倍', $(this), {
				tips: [1, '#efbf5c'],
				time: 2000,
				tipsMore: true,
				area: 'auto',
				maxWidth: '500'
			});
		});
	};
	loadLoanData();
});
