/**
 * created by xuyang
 * 2016-06-08
 */
require(['jquery', 'underscore', 'moment', 'daterangepicker'], function($, _, moment) {
	$(function() {
		if (!$('#instructions').length) {
			return false;
		}

		$('#instructions').find('.item').on('mouseover', function() {
			var $t = $(this);
			$t.addClass('active').siblings().removeClass('active');
		});

		$('#date-time-picker').dateRangePicker({
			separator: ' ~ '
		}).on('datepicker-change', function(event, obj) {
			renderTable({
				startTime: moment(obj.date1).format('YYYY-MM-DD'),
				endTime: moment(obj.date2).format('YYYY-MM-DD')
			})
		})

		var template = $('#tpl').html();
		var $tbody = $('#tbody');
		var renderTable = function(sendData, fn) {
			$.ajax({
				url: '/membership/structure-list-data',
				data: sendData || ''
			}).done(function(response) {
				response = [{
					"id": 0,
					"loginName": "gaoyinglong",
					"experience": 5000000,
					"totalExperience": 5025000,
					"createdTime": "2016-06-01 23:22:22",
					"description": "投资5000，增加5000成长值"
				}, {
					"id": 0,
					"loginName": "gaoyinglong",
					"experience": 5000,
					"totalExperience": 25000,
					"createdTime": "2016-06-01 22:22:22",
					"description": "投资5000，增加5000成长值"
				}, {
					"id": 0,
					"loginName": "gaoyinglong",
					"experience": 5000,
					"totalExperience": 20000,
					"createdTime": "2016-06-01 21:22:22",
					"description": "投资5000，增加5000成长值"
				}, {
					"id": 0,
					"loginName": "gaoyinglong",
					"experience": 5000,
					"totalExperience": 15000,
					"createdTime": "2016-06-01 20:22:22",
					"description": "投资5000，增加5000成长值"
				}, {
					"id": 0,
					"loginName": "gaoyinglong",
					"experience": 5000,
					"totalExperience": 10000,
					"createdTime": "2016-06-01 19:22:22",
					"description": "投资5000，增加5000成长值"
				}, {
					"id": 0,
					"loginName": "gaoyinglong",
					"experience": 5000,
					"totalExperience": 5000,
					"createdTime": "2016-06-01 18:22:22",
					"description": "投资5000，增加5000成长值"
				}];

				$tbody.html(_.template(template)({
					data: response
				}));
				fn && fn();

			}).fail(function(xhr) {
				console.error(xhr, '获取数据失败!');
			});
		};

		renderTable();
		$('#filter-btns').find('span').on('click', function() {
			var $t = $(this);
			var type = $t.data('type');
			if (type == 'all') {
				renderTable(null, function() {
					$t.addClass('active').siblings().removeClass('active');
				});
			} else {
				var time = moment();
				renderTable({
					startTime: time.format('YYYY-MM-DD'),
					endTime: time.subtract(parseInt(type), 'days').format('YYYY-MM-DD')
				}, function() {
					$t.addClass('active').siblings().removeClass('active');
				});
			}
		})


	}); // document ready end bracket
}); // require end bracket