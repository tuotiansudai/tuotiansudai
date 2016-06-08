require(['jquery'], function($) {
	$(function() {
		$('#instructions').find('.item').on('mouseover', function() {
			var $t = $(this);
			$t.addClass('active').siblings().removeClass('active');
		});
	});
});
