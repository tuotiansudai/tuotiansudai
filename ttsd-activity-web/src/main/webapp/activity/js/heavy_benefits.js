require(['jquery','layerWrapper'], function ($, layer) {

    var $topHeader=$('#topHeader'),
        $heavyBenefits = $('#heavyBenefits');

    var browser = globalFun.browserRedirect();

    if(browser=='mobile') {
        var topImage = new Image();
        topImage.src = staticServer + '/activity/images/heavy-benefits/top-wap.jpg';
        topImage.onload=function() {
            $topHeader.append(topImage);
        }
    } else if(browser=='pc') {

        (function() {
            var $totalRewardCalls = $('#totalRewardCalls'),
                totalCalls = $.trim($totalRewardCalls.text());

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
            function investAmount(currentAmount) {
                var initNum = 50000,
                    kind;
                var percent = [0,0.16,0.31,0.47,0.63,0.79,0.94];
                currentAmount = parseInt(currentAmount);
                if(currentAmount <=initNum) {
                    kind = '1';
                } else if(currentAmount>initNum && currentAmount<=initNum*2){
                    kind = '2';
                } else if(currentAmount>initNum*2 && currentAmount<=initNum*4) {
                    kind = '3';
                } else if(currentAmount>initNum*4 && currentAmount<=initNum*6) {
                    kind = '4';
                } else if(currentAmount>initNum*6 && currentAmount<=initNum*10) {
                    kind = '5';
                } else if(currentAmount>initNum*10 && currentAmount<=initNum*16) {
                    kind = '6';
                } else if(currentAmount>initNum*16 && currentAmount<=initNum*20) {
                    kind = '7';
                }

                var Stall = {
                    '1':function() {
                        return '0%';
                    },
                    '2':function() {
                        var nowPercent = percent[1] * (currentAmount - initNum)/initNum ;
                        return nowPercent;
                    },
                    '3':function() {
                        var nowPercent = percent[2] * (currentAmount - initNum*2)/(initNum*2);
                        return nowPercent;
                    },
                    '4':function() {
                        var nowPercent = percent[3] * (currentAmount - initNum*4)/(initNum*2) ;
                        return nowPercent;
                    },
                    '5':function() {
                        var nowPercent = percent[4] * (currentAmount - initNum*6)/(initNum*4) ;
                        return nowPercent;
                    },
                    '6':function() {
                        var nowPercent = percent[5] * (currentAmount - initNum*10)/(initNum*6) ;
                        return nowPercent;
                    },
                    '7':function() {
                        var nowPercent = percent[6] * (currentAmount - initNum*16)/(initNum*4) ;
                        return nowPercent;
                    }
                }
                return Stall[kind]();
            }

            var cardAmount = $.trim($('.jd-card-amount',$heavyBenefits).text());

            var nowPercent = investAmount(cardAmount);
            $('#progressJDCard').css({'width':nowPercent*100+'%'});
        })();


    }

});