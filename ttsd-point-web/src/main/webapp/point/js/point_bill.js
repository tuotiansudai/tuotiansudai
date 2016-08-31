/**
 * [name]:membership bill page
 * [user]:xuqiang
 * [date]:2016-08-08
 */
require(['jquery', 'template', 'moment','pagination', 'daterangepicker', 'jquery.ajax.extension'], function($, tpl, moment) {
	$(function() {
		var $dataList = $('#dataList');


		var today = moment().format('YYYY-MM-DD'), // 今天
			week = moment().subtract(1, 'week').format('YYYY-MM-DD'),
			month = moment().subtract(1, 'month').format('YYYY-MM-DD'),
			sixMonths = moment().subtract(6, 'month').format('YYYY-MM-DD'),
			$dateFilter = $('.date-filter'),
			$statusFilter = $('.status-filter'),
			dataPickerElement = $('#date-picker');

		dataPickerElement.dateRangePicker({
			separator: ' ~ '
		});
		var changeDatePicker = function() {
			var duration = $(".date-filter .select-item.current").data('day');
			switch (duration) {
				case 1:
					dataPickerElement.val(today + '~' + today);
					break;
				case 7:
					dataPickerElement.val(week + '~' + today);
					break;
				case 30:
					dataPickerElement.val(month + '~' + today);
					break;
				case 180:
					dataPickerElement.val(sixMonths + '~' + today);
					break;
				default:
					dataPickerElement.val('');
			}
		};
		var loadLoanData = function(currentPage) {
			var dates = dataPickerElement.val().split('~'),
				startTime = $.trim(dates[0]) || '',
				endTime = $.trim(dates[1]) || '',
				status = $('.status-filter .select-item.current').attr('data-status'),
				requestData = {
					startTime: startTime,
					endTime: endTime,
					businessType: status,
					index: currentPage || 1
				};

			$('#pageList').loadPagination(requestData, function(data) {
				if (data.records) {
					$.each(data.records, function(index,item) {
						switch (item.businessType) {
							case 'SIGN_IN':
								item.businessType = '签到奖励';
								break;
							case 'TASK':
								item.businessType = '任务奖励';
								break;
							case 'INVEST':
								item.businessType = '投资奖励';
								break;
							case 'EXCHANGE':
								item.businessType = '积分兑换';
								break;
							case 'LOTTERY':
								item.businessType = '抽奖';
								break;
						}
					});
				}
				$dataList.html(tpl('dataListTpl', data));
			});
		};
		changeDatePicker();
		loadLoanData();
		$dateFilter.find(".select-item").click(function() {
			$(this).addClass("current").siblings(".select-item").removeClass("current");
			changeDatePicker();
			loadLoanData();
		});
		$statusFilter.find(".select-item").click(function() {
			$(this).addClass("current").siblings(".select-item").removeClass("current");
			loadLoanData();
		});

		$('.apply-btn').click(function() {
			loadLoanData();
			$(".date-filter .select-item").removeClass("current");
		});
	});
})