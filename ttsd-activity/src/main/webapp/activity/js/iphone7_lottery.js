/**
 * [name]:give iphone7 activity
 * [author]:xuqiang
 * [date]:2016-10-20
 */
require(['jquery', 'layerWrapper', 'template', 'logintip', 'jquery.ajax.extension'], function($, layer, tpl) {
	$(function() {
		//
		$.ajax({
			url: '/path/to/file',
			type: 'POST',
			dataType: 'json',
			data: {
				param1: 'value1'
			}
		})
		.done(function(data) {
			if(data==true){
				$('#moneyTip').find('p').text('活动已结束');
				$('.get-loan-btn').attr('href','javascript:void(0)').addClass('disabled').text('活动已结束');
			}
		})
		.fail(function() {
			layer.msg('请求失败，请刷新页面重试！');
		});
		

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

						console.log(data);
						$('#codeList').html(tpl('codeListTpl',data.data));
					})
					.fail(function(data) {
						layer.msg('请求失败，请重试！');
					});
			}
		}
		getDatelist(1);
	});
});