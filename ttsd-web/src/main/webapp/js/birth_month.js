require(['jquery'], function ($) {
    $(function () {
        var	$productBtn=$('.time-btn'),
        	$birthTip=$('.birth-time'),
        	$closeBtn=$('.time-close');

        $productBtn.on('click', function(event) {
        	event.preventDefault();
        	$birthTip.show();
        });

        $closeBtn.on('click', function(event) {
        	event.preventDefault();
        	$birthTip.hide();
        });
    });
});