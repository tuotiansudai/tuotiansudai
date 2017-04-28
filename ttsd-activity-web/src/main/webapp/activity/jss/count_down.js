define(['jquery'], function ($) {
    function writeTime() {
        if ($('.preheat').length > 0) {
            $('.preheat').each(function () {
                var $self = $(this);
                var countdown = $(this).attr("data-time");
                setInterval(function () {
                    var day = 0,
                        hour = 0,
                        minute = 0,
                        second = 0;
                    if (countdown <= 1800) {
                        if (countdown > 0) {
                            minute = Math.floor(countdown / 60) - (day * 24 * 60) - (hour * 60);
                            second = Math.floor(countdown) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
                        } else {

                            $self.parent().addClass('active-after');

                        }
                        if (minute <= 9) minute = '0' + minute;
                        if (second <= 9) second = '0' + second;
                        $self.find('.minute_show').html(minute + '分');
                        $self.find('.second_show').html(second + '秒');
                        countdown--;
                    }
                }, 1000);
            });
        }
    }
    writeTime();
});