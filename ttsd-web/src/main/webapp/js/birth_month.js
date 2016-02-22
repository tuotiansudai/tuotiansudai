require(['jquery'], function ($) {
    $(function () {
        var	$productBtn=$('.time-btn'),
        	$birthTip=$('.birth-time'),
        	$closeBtn=$('.time-close');

        $productBtn.on('click', function(event) {
            var serverTime = new Date($(event.currentTarget).data("kick-off-date")).getTime();
            if (serverTime < new Date("2016-03-01 00:00:00").getTime()) {
                $birthTip.show();
                return false;
            }
        });

        $closeBtn.on('click', function(event) {
        	event.preventDefault();
        	$birthTip.hide();
        });
    });
});