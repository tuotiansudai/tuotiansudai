require(['jquery', 'coupon-alert'], function($) {
	$(function() {
		var $body = $('body'),
			$ruleList = $('#ruleList'),
			$content = $('.model-list');
		//Effects tab
		$body.on('click', '.filters-list li', function(event) {
			event.preventDefault();
			var $self = $(this),
				index = $self.index();
			$self.addClass('active').siblings().removeClass('active');
			$content.find('.coupon-com:eq(' + index + ')').show().siblings().hide();
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

		$('#submitCode').on('click', function() {
			var exchangeCode = $('#couponByCode').val();
			if (exchangeCode.length == 0) {
				$(this).append('<p class="tip-text error-color">请输入正确的兑换码</p>');
			} else {
				$.ajax({
					url: '/my-treasure/'+exchangeCode+'/exchange',
					type: 'POST',
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8'
				}).done(function(data){
					var message = data.message;
					if (data.status) {
						$(this).append('<p class="tip-text success-color">'+message+'</p>');
						setInterval(function(){location.href="/my-treasure"}, 1000);
					} else {
						$(this).append('<p class="tip-text error-color">'+message+'</p>');
					}
				}).fail(function() {
					$(this).append('<p class="tip-text error-color">兑换失败，请重试</p>');
				});
			}
		});

	});
});