require(['jquery', 'layerWrapper', 'template', 'logintip', 'jquery.ajax.extension'], function($, layer, tpl) {
	$(function() {
		$('.progressbar').each(function() {
			var t = $(this),
				dataperc = t.attr('data-perc'),
				number=parseInt(Math.round(dataperc)/5000);
				console.log(number);
			t.find('.bar').animate({
				width: number+'%'
			}, number * 25);
		});
	});
});