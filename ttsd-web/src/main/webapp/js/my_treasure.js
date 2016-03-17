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
	});
});
