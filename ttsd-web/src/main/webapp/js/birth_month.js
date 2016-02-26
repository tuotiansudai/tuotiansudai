require(['jquery'], function ($) {
    $(function () {
        var	$productBtn=$('.time-btn'),
        	$birthTip=$('.birth-time'),
        	$closeBtn=$('.time-close');

        $productBtn.on('click', function(event) {
            var serverTime = (new Date($(this).attr("data-kick-off-date").replace(/-/g, "/"))).getTime();
            var now=(new Date("2016-03-01 00:00:00".replace(/-/g, "/"))).getTime();
            if (serverTime < now) {
                $birthTip.show();
                return false;
            }else{
                location.href=$(this).attr('data-href');
            }
        });

        $closeBtn.on('click', function(event) {
        	event.preventDefault();
        	$birthTip.hide();
        });

        if($('.birth-month-container').length) {
            if(/app/gi.test(location.search)) {
                $('.header-container,.nav-container,.footer-container,.footer-responsive').remove();
            }
        }


    });
});