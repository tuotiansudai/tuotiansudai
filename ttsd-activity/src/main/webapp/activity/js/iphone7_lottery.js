require(['jquery', 'layerWrapper', 'template', 'logintip', 'jquery.ajax.extension'], function($, layer, tpl) {
	$(function() {
		$('.progressbar').each(function() {
			var t = $(this),
				dataperc = t.attr('data-perc'),
				number = parseInt(100 - Math.round(dataperc) / 5000);
			t.find('.bar').animate({
				width: number + '%'
			}, number * 25);
		});

		$('body').on('click', '.code-btn', function(event) {
			event.preventDefault();
			var $self=$(this),
				_page=$self.attr('data-page');
			getDatelist(_page);
		});

		function getDatelist(page) {
			if($('#codeList').length>0){
				$.ajax({
						url: '/activity/iphone7-lottery/myInvestDetail',
						type: 'POST',
						dataType: 'json',
						data: {
							index: page,
							loginName: $('#loginName').val()
						}
					})
					.done(function(data) {
						data = {
							"status": true,
							"message": null,
							"index": 1,
							"pageSize": 10,
							"count": 11,
							"maxPage": 2,
							"hasPreviousPage": false,
							"hasNextPage": true,
							"records": [{
								"lotteryNumber": "653342",
								"status": "待开奖"
							}, {
								"lotteryNumber": "653343",
								"status": "待开奖"
							}, {
								"lotteryNumber": "653344",
								"status": "待开奖"
							}, {
								"lotteryNumber": "653342",
								"status": "待开奖"
							}, {
								"lotteryNumber": "653342",
								"status": "待开奖"
							}, {
								"lotteryNumber": "653342",
								"status": "待开奖"
							}, {
								"lotteryNumber": "653342",
								"status": "待开奖"
							}, {
								"lotteryNumber": "653342",
								"status": "待开奖"
							}, {
								"lotteryNumber": "653342",
								"status": "待开奖"
							}, {
								"lotteryNumber": "653342",
								"status": "待开奖"
							}]
						};
						console.log(data);
						$('#codeList').html(tpl('codeListTpl',data));
					})
					.fail(function(data) {
						layer.msg('请求失败，请重试！');
					});
			}
		}
		getDatelist(1);
	});
});