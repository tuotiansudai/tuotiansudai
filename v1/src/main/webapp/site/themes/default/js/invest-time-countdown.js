/**
 * onCountDownBegin(String countDownId);
 * onCountDownOver(String countDownId, jqueryElement countdownElement);
 */
(function($){
    var _noop = function(s){};
    var logger = (typeof console != 'undefined') ? {log:_noop,warn:_noop,error:_noop} : console;
    var countDownTriggerThreshold  = 30 * 60 * 1000; // 30 minutes
    // nowTime是服务器当前时间
    var countDown = function(el, countDownId, targetTime, nowTime, overHandle){
        this.el = el;
        this.countDownId = countDownId;
        this.targetTime = targetTime;
        this.nowTime = nowTime;
        this.overHandle = overHandle;
        this.clientStartTime = new Date();
        this.remain = this.targetTime - this.nowTime;
        this.windowIntervalHandle = 0;
    };

    countDown.prototype = {
        countdown:function(){
            this.cancel();
            this.windowIntervalHandle = window.setInterval(this.tickDelegate(),1000);
        },
        cancel:function(){
            if(this.windowIntervalHandle != 0){
                window.clearInterval(this.windowIntervalHandle);
                this.windowIntervalHandle = 0;
            }
        },
        tickDelegate:function(){
            var cd = this;
            return function(){
                cd.tick.call(cd);
            }
        },
        tick:function() {
            var n = new Date();
            var passed = n - this.clientStartTime;
            var remain = parseInt((this.remain - passed) / 1000);
            if (remain <= 0) {
                this.cancel();
                this.onCountDownOver();
            } else {
                var remainSecond = remain % 60;
                var remainSecondString = ('0'+remainSecond).substr(-2);
                var remainMinute = parseInt(remain / 60);
                var remainMinuteString = ('0'+remainMinute).substr(-2);
                var cdString = remainMinuteString + ":" + remainSecondString + ' 后可以投资';
                this.el.html(cdString);
            }
        },
        onCountDownOver:function(){
            var onOverHandle = window[this.overHandle];
            if(typeof onOverHandle === 'function'){
                try {
                    onOverHandle(this.countDownId, this.el);
                }catch(e){logger.error(e);}
            }
        }
    }

    $(function() {
        $("[rel='invest-time-countdown']").each(function(){
            var cdEl = $(this);
            var countDownId = cdEl.attr('count-down-id');
            var beginTimeString = cdEl.attr('invest-begin-time');
            var beginTime = new Date(Date.parse(beginTimeString));
            var serverTimeString = cdEl.attr('server-now-time');
            var serverTime = new Date(Date.parse(serverTimeString));
            var beginHandle = cdEl.attr('on-begin');
            var overHandle = cdEl.attr('on-over');
            if(beginTime > serverTime){
                var onBeginHandle = window[beginHandle];
                if(typeof onBeginHandle === 'function'){
                    try {
                        onBeginHandle(countDownId);
                    }catch(e){logger.error(e);}
                }
            }
            if(beginTime - serverTime < countDownTriggerThreshold) {
                var cd = new countDown(cdEl, countDownId, beginTime, serverTime, overHandle);
                cd.countdown();
            }
        });
    });
})(jQuery);