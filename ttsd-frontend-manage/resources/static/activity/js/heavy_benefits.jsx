require("activityStyle/heavy_benefits.scss");
var $topHeader=$('#topHeader'),
    $heavyBenefits = $('#heavyBenefits');

var browser = globalFun.browserRedirect();

var $totalRewardCalls = $('#totalRewardCalls'),
    totalCalls = Number($.trim($totalRewardCalls.text()));

var cardAmount = $.trim($('.jd-card-amount',$heavyBenefits).text()).replace(/,/g,'');

function investAmount(currentAmount) {
    var initNum = 50000,
        kind;
    var percent = [0,0.16,0.31,0.47,0.63,0.79,0.94];
    currentAmount = parseInt(currentAmount);

    if(currentAmount <initNum) {
        kind = '1';
    } else if(currentAmount>=initNum && currentAmount<initNum*2){
        kind = '2';
    } else if(currentAmount>=initNum*2 && currentAmount<initNum*4) {
        kind = '3';
    } else if(currentAmount>=initNum*4 && currentAmount<initNum*6) {
        kind = '4';
    } else if(currentAmount>=initNum*6 && currentAmount<initNum*10) {
        kind = '5';
    } else if(currentAmount>=initNum*10 && currentAmount<initNum*16) {
        kind = '6';
    } else if(currentAmount>=initNum*16 && currentAmount<initNum*20) {
        kind = '7';
    } else {
        kind = '8';
    }

    if(browser=='pc') {
        var Stall = {
            '1':function() {
                return '0';
            },
            '2':function() {
                var nowPercent = percent[1] * (currentAmount - initNum)/initNum ;
                return nowPercent;
            },
            '3':function() {
                var nowPercent = (percent[2]-percent[1]) * (currentAmount - initNum*2)/(initNum*2) + percent[1];
                return nowPercent;
            },
            '4':function() {
                var nowPercent = (percent[3]-percent[2]) * (currentAmount - initNum*4)/(initNum*2) +percent[2];
                return nowPercent;
            },
            '5':function() {
                var nowPercent = (percent[4]-percent[3]) * (currentAmount - initNum*6)/(initNum*4)+percent[3] ;
                return nowPercent;
            },
            '6':function() {
                var nowPercent = (percent[5]-percent[4]) * (currentAmount - initNum*10)/(initNum*6)+percent[4] ;
                return nowPercent;
            },
            '7':function() {
                var nowPercent = (percent[6]-percent[5]) * (currentAmount - initNum*16)/(initNum*4)+percent[5] ;
                return nowPercent;
            }
        }
        return Stall[kind]();

    } else if(browser=='mobile') {
        return kind;
    }
}

if(browser=='mobile') {
    var topImage = new Image();
    var topWapUrl = require('../images/heavy-benefits/top-wap.jpg');
    topImage.src = topWapUrl;
    topImage.onload=function() {
        $topHeader.append(topImage);
    };

    //赚话费
    (function() {
        var $alignBox =$('.bill-column .align-box',$heavyBenefits);
        if(totalCalls>=4 && totalCalls<6) {
            $alignBox.eq(0).find('.inner-progress-active').css({'width':'50%'});
        } else if(totalCalls>=6 && totalCalls<9) {
            $alignBox.eq(0).find('.inner-progress-active').css({'width':'89%'});
            $alignBox.eq(1).find('.inner-progress-active').css({'height':'50%'});
        } else if(totalCalls>=9 && totalCalls<11) {
            $alignBox.eq(0).find('.inner-progress-active').css({'width':'89%'});
            $alignBox.eq(1).find('.inner-progress-active').css({'height':'100%'});
            $alignBox.eq(2).find('.inner-progress-active').css({'width':'39%'});

        } else if(totalCalls>=11) {
            $alignBox.eq(0).find('.inner-progress-active').css({'width':'89%'});
            $alignBox.eq(1).find('.inner-progress-active').css({'height':'100%'});
            $alignBox.eq(2).find('.inner-progress-active').css({'width':'88%'});
        }

    })();

    //京东e卡
    (function() {
        var $alignBoxC =$('.JD-CARD .align-box',$heavyBenefits);
        var kind = investAmount(cardAmount);
        if(kind =='3' ) {
            $alignBoxC.eq(0).find('.inner-progress-active').css({'width':'50%'});
        } else if(kind =='4') {
            $alignBoxC.eq(0).find('.inner-progress-active').css({'width':'88%'});
            $alignBoxC.eq(1).find('.inner-progress-active').css({'height':'50%'});
        } else if(kind == '5') {
            $alignBoxC.eq(0).find('.inner-progress-active').css({'width':'88%'});
            $alignBoxC.eq(1).find('.inner-progress-active').css({'height':'100%'});
            $alignBoxC.eq(2).find('.inner-progress-active').css({'width':'40%'});
        } else if(kind == '6') {
            $alignBoxC.eq(0).find('.inner-progress-active').css({'width':'88%'});
            $alignBoxC.eq(1).find('.inner-progress-active').css({'height':'100%'});
            $alignBoxC.eq(2).find('.inner-progress-active').css({'width':'88%'});
            $alignBoxC.eq(3).find('.inner-progress-active').css({'height':'50%'});
        } else if(kind == '7') {
            $alignBoxC.eq(0).find('.inner-progress-active').css({'width':'88%'});
            $alignBoxC.eq(1).find('.inner-progress-active').css({'height':'100%'});
            $alignBoxC.eq(2).find('.inner-progress-active').css({'width':'88%'});
            $alignBoxC.eq(3).find('.inner-progress-active').css({'height':'100%'});
            $alignBoxC.eq(4).find('.inner-progress-active').css({'width':'42%'});
        } else if(kind=='8') {
            $alignBoxC.eq(0).find('.inner-progress-active').css({'width':'88%'});
            $alignBoxC.eq(1).find('.inner-progress-active').css({'height':'100%'});
            $alignBoxC.eq(2).find('.inner-progress-active').css({'width':'88%'});
            $alignBoxC.eq(3).find('.inner-progress-active').css({'height':'100%'});
            $alignBoxC.eq(4).find('.inner-progress-active').css({'width':'88%'});
        }
    })();
} else if(browser=='pc') {

    (function() {
        function calPercent(num) {
            var statusOpt = {
                '0-2':'0%',
                3:'11%',
                4:'25%',
                5:'37%',
                6:'50%',
                7:'57%',
                8:'64%',
                9:'73%',
                10:'84%',
                11:'95%'
            };
            if(num<3) {
                return statusOpt['0-2'];
            }
            if(num>11) {
                return statusOpt['11'];
            }
            return statusOpt[num];
        }

        var percentNum = calPercent(totalCalls);
        $('#progressReward').css({"width":percentNum});
    })();

    //领取京东卡
    (function() {
        var nowPercent = investAmount(cardAmount);
        $('#progressJDCard').css({'width':nowPercent*100+'%'});
    })();
}
