require(['jquery', 'layerWrapper', 'template', 'logintip', 'jquery.ajax.extension'], function($, layer, tpl) {
	$(function() {
		$('.progressbar').each(function() {
			var t = $(this),
				dataperc = t.attr('data-perc'),
				number=parseInt(100-Math.round(dataperc)/5000);
				console.log(number);
			t.find('.bar').animate({
				width: number+'%'
			}, number * 25);
		});

		function getDatelist(page){
			$.ajax({
				url: '/path/to/file',
				type: 'POST',
				dataType: 'json',
				data: {
					param1: page,
					user:loginname
				}
			})
			.done(function(data) {
				console.log(data);
				// $('#codeList').html(tpl('codeListTpl',data));
			})
			.fail(function(data) {
				layer.msg('请求失败，请重试！');
			});
			
		}
	});
});