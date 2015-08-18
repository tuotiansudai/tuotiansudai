(function($){
    var countDownTriggerThreshold  = 30 * 60 * 1000; // 30 minutes
    // nowTime是服务器当前时间
    var countDown = function(el, loanId, targetTime, nowTime){
        this.el = el;
        this.loanId = loanId;
        this.targetTime = targetTime;
        this.nowTime = nowTime;
        this.clientStartTime = new Date();
        this.remain = this.targetTime - this.nowTime;
        this.windowIntervalHandle = 0;
    };

    countDown.prototype = {
        countdown:function(){
            this.onCountDownBegin();
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
            if (remain < 0) {
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
        onCountDownBegin:function(){
        },
        onCountDownOver:function(){
            var countDownOverMessage = this.el.attr('count-down-message');
            if(!countDownOverMessage){countDownOverMessage='';}
            this.el.html(countDownOverMessage);

            // for index page
            var btn = $('#loan-btn-' + this.loanId);
            btn.html(btn.attr('ori-text'))
                .removeClass('tz-btn-waiting');

            // for detail page
            var btn4detail = $('.sure-btn');
            btn4detail.html(btn4detail.attr('ori-text'))
                .attr('onclick',btn4detail.attr('ori-onclick'))
                .attr('href',btn4detail.attr('ori-href'))
                .removeClass('btn-waiting');
        }
    }

    $(function() {
        $("[rel='invest-time-countdown']").each(function(){
            var cdEl = $(this);
            var loanId = cdEl.attr('loan-id');
            var beginTimeString = cdEl.attr('invest-begin-time');
            var beginTime = new Date(Date.parse(beginTimeString));
            var serverTimeString = cdEl.attr('server-now-time');
            var serverTime = new Date(Date.parse(serverTimeString));
            if(beginTime > serverTime){
                // for index page
                var btn = $('#loan-btn-'+loanId);
                btn.attr('ori-text',btn.html())
                    .html('预热中')
                    .addClass('tz-btn-waiting');

                // for detail page
                var btn4detail = $('.sure-btn');
                btn4detail.attr('ori-text',btn4detail.html())
                    .attr('ori-onclick',btn4detail.attr('onclick'))
                    .attr('ori-href',btn4detail.attr('href'))
                    .html('预热中')
                    .addClass('btn-waiting')
                    .attr('onclick','return false;');

            }
            if(beginTime - serverTime < countDownTriggerThreshold) {
                var cd = new countDown(cdEl, loanId, beginTime, serverTime);
                cd.countdown();
            }
        });
    });
})(jQuery);