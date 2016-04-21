require(['jquery', 'jquery.ajax.extension', 'coupon-alert', 'red-envelope-float'], function ($) {
    var $loan = $('.loan-list-box').find('li');
    var preheat = $('.preheat');

    $loan.click(function () {
        window.location.href=$(this).data('url');
    });

    $('.pagination .prev').click(function () {
        var self = $(this);
        if (self.hasClass('active')) {
            window.location.href = self.data('url');
        }
        return false;
    });

    $('.pagination .next').click(function () {
        var self = $(this);
        if (self.hasClass('active')) {
            window.location.href = self.data('url');
        }
        return false;
    });

    function writeTime() {
        if(preheat.length > 0){
            preheat.each(function(index){
                var stringTime = $(this).attr("data-time");
                //console.log(stringTime);
                var timestamp2 = Date.parse(new Date(stringTime));
                //console.log(stringTime);
                var startInterval = timestamp2;//开始销售时间，历史毫秒数
                var nowInterval = (new Date()).getTime();//当前时间，历史毫秒数
                //用开始时间 - 当前时间，格式化这个时间差；
                flagInterval = getLastDays(startInterval - nowInterval, true);
                //这里控制样式比如 xx.innerHtml = flagInterval
                $(this).html(flagInterval);
            });
        }

    }
    writeTime();
    setInterval(writeTime, 1000);
    
    function getLastDays(input,isShort) {
        var minSecondsPerDay = 86400 * 1000;
        var minSecondsPerHour = 3600 * 1000;
        var minSecondsPerMin = 60 * 1000;
        var days = Math.floor( input / minSecondsPerDay );
        var hours = Math.floor( (input % minSecondsPerDay) / minSecondsPerHour );
        var hoursT = Math.floor( input / minSecondsPerHour);
        var minutes = Math.floor( ( input % minSecondsPerDay % minSecondsPerHour ) / minSecondsPerMin );
        var seconds = Math.floor( input % minSecondsPerDay % minSecondsPerHour % minSecondsPerMin / 1000 );

        if(minutes < 10){
            minutes = '0' + minutes;
        }
        if(seconds < 10){
            seconds = '0' + seconds;
        }
        //加上short 不显示天数，只显示小时
        if(isShort){
            return  hoursT + ':' + minutes + ':' + seconds ;//24小时之内倒计时
        }

        return  days + '天' + hours + '时' + minutes + '分' + seconds + '秒';
    }
});
