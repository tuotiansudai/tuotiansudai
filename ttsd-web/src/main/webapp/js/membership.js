/**
 * created by xuyang
 * 2016-06-08
 */
require(['jquery', 'daterangepicker'], function($) {
	$(function() {
		$('#instructions').find('.item').on('mouseover', function() {
			var $t = $(this);
			$t.addClass('active').siblings().removeClass('active');
		});

		$('#date-time-picker').dateRangePicker({separator: ' ~ '});

	}); // document ready end bracket
}); // require end bracket
