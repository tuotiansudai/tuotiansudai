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
			var exchangeCode = $('#couponByCode').val().trim;
			if (exchangeCode.length == 0) {
				$(this).parent().append('<p class="tip-text error-color">请输入正确的兑换码</p>');
				$('#couponByCode').val('');
			} else {
				$.ajax({
					url: '/my-treasure/'+exchangeCode+'/exchange',
					type: 'POST',
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8'
				}).done(function(data){
					var message = data.message;
					if (data.status) {
						$(this).parent().append('<p class="tip-text success-color">'+message+'</p>');
						setInterval(function(){location.href="/my-treasure"}, 1000);
					} else {
						$(this).parent().append('<p class="tip-text error-color">'+message+'</p>');
						$('#couponByCode').val('');
					}
				}).fail(function() {
					$(this).parent().append('<p class="tip-text error-color">兑换失败，请重试</p>');
					$('#couponByCode').val('');
				});
			}
		});

	});
});