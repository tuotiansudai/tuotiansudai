require(['jquery'], function ($) {
    $(function () {
        var	$productBtn=$('.time-btn'),
        	$birthTip=$('.birth-time'),
        	$closeBtn=$('.time-close');

        $productBtn.on('click', function(event) {
            var serverTime = (new Date($(this).attr("data-kick-off-date").replace(/-/g, "/"))).getTime();
            var now=(new Date("2016-03-01 00:00:00".replace(/-/g, "/"))).getTime();
            serverTime < now?$birthTip.show():location.href=$(this).attr('data-href');
        });

        $closeBtn.on('click', function(event) {
        	event.preventDefault();
        	$birthTip.hide();
        });
    });
});