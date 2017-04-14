require(['jquery','layerWrapper', 'template', 'logintip', 'jquery.ajax.extension'], function($, layer, tpl) {
	$(function() {
		var $newmanTyrant=$('#newmanTyrant'),
			$boardTab=$('.board-tab span',$newmanTyrant);


		$boardTab.on('click', function(event) {
			event.preventDefault();
			var $self=$(this),
				type=$self.attr('data-type');
			$self.addClass('active').siblings().removeClass('active');
			// getBoard(type);
		});

		
		//获取排行榜数据
		function getBoard(type,data){
			$.ajax({
				url: '/activity/newman-tyrant/'+type+'/' + date,
				type: 'GET',
				dataType: 'json'
			})
			.done(function(data) {
				if(data.status) {
					$('#rankTable').html(tpl('rankTableTpl',data));
				}
			})
			.fail(function() {
				layer.msg('请求失败，请重试！');
			});
		}
	});
});