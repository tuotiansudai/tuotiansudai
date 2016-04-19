require(['jquery'], function($) {
	$("#downloadBtn").on('click', function(event) {
		event.preventDefault();
		$('body,html').animate({scrollTop:0},1000);
	});
});