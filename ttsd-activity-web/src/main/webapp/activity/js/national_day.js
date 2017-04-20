require(['jquery', 'underscore', 'template','layerWrapper','drawCircle','commonFun','register_common'], function ($, _,tpl,layer,drawCircle) {

    var $nationalDayFrame=$('#nationalDayFrame'),
        tipGroupObj={},
        $tourSlide=$('#tourSlide'),
        $nationalDayCircle=$('#nationalDayCircle'),
        $allInvestAmount=$nationalDayFrame.find('.invest-percent-box em.total-invest');
    var browser = globalFun.browserRedirect();

    $nationalDayFrame.find('.tip-list-frame .tip-list').each(function(key,option) {
        var kind=$(option).data('return');
        tipGroupObj[kind]=option;
    });

    if (browser == 'mobile') {
        var urlObj=globalFun.parseURL(location.href);
        if(urlObj.params.tag=='yes') {
            $nationalDayFrame.find('.reg-tag-current').show();
        }
    }
    else {

        //取消菜单固定在顶端
        //fixMenuJump();
        //$(window).on('scroll',function() {
        //    fixMenuJump();
        //});
    }

    function fixMenuJump() {
        var scrollTop=$(window).scrollTop();
        if(scrollTop>=580) {
            $tourSlide.addClass('fixed-menu');
            $('.seizeSeat',$nationalDayFrame).show();
        }
        else {
            $tourSlide.removeClass('fixed-menu');
            $('.seizeSeat',$nationalDayFrame).hide();
        }
    }

    var allInvestAmount=$.trim($allInvestAmount.text());

    function addSeparator(str){
        var re = /(?=(?!\b)(\d{3})+$)/g;
        return str.replace(re, ',');
    }
    //给数字加上逗号分隔
    var allAmountInteger=allInvestAmount.split('.')[0];
    if(allInvestAmount.split('.')[1]) {
        var allAmountDecimal='.'+allInvestAmount.split('.')[1];
    }
    else {
        allAmountDecimal='';
    }

    var realInvestAmount=addSeparator(allAmountInteger);
    $allInvestAmount.text(realInvestAmount+allAmountDecimal);

    //以下为抽奖转盘
    var $pointer = $('.pointer-img',$nationalDayFrame);
    var $MobileNumber=$('#MobileNumber'),
        travelAllList='/activity/national/all-list',  //中奖记录接口地址
        travelUserList='/activity/national/user-list',   //我的奖品接口地址
        drawURL='/activity/national/draw',    //抽奖的接口链接
        myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

    var $GiftRecord=$('#GiftRecord'),
        $MyGift=$('#MyGift');

    var drawCircle=new drawCircle(travelAllList,travelUserList,drawURL,myMobileNumber);

    //渲染中奖记录
    drawCircle.GiftRecord();

    //渲染我的奖品
    drawCircle.MyGift(travelUserList,myMobileNumber);

    //开始抽奖
    $pointer.on('click', function(event) {
        drawCircle.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                var angleNum=0;
                switch (data.prize) {
                    case 'MANGO_CARD_100':
                        angleNum=347;
                        break;
                    case 'RED_INVEST_15':
                        angleNum=30;
                        break;
                    case 'RED_INVEST_50':
                        angleNum=120;
                        break;
                    case 'TELEPHONE_FARE_10':
                        angleNum=265;
                        break;
                    case 'IQIYI_MEMBERSHIP':
                        angleNum=80;
                        break;
                    case 'CINEMA_TICKET':
                        angleNum=310;
                        break;
                    case 'FLOWER_CUP':
                        angleNum=170;
                        break;
                    case 'MEMBERSHIP_V5':
                        angleNum=347;
                        break;
                }

                var prizeType=data.prizeType.toLowerCase();
                $(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);
                drawCircle.rotateFn(angleNum,tipGroupObj[prizeType]);

            } else if (data.returnCode == 2) {
                $('.no-login-text',$integralDrawPage).trigger('click');  //弹框登录
            } else if(data.returnCode == 3){
                //不在活动时间范围内！
                drawCircle.tipWindowPop(tipGroupObj['expired']);
            } else {
                //实名认证
                drawCircle.tipWindowPop(tipGroupObj['authentication']);
            }
        });
    });

    //中奖记录,我的奖品超过10条数据滚动

    drawCircle.scrollList($GiftRecord);
    drawCircle.scrollList($MyGift);

    //scroll award record list
    var scrollTimer;

    $GiftRecord.hover(function() {
        clearInterval(scrollTimer);
    }, function() {
        scrollTimer = setInterval(function() {
            drawCircle.scrollList($GiftRecord);
        }, 2000);
    }).trigger("mouseout");

    $MyGift.hover(function() {
        clearInterval(scrollTimer);
    }, function() {
        scrollTimer = setInterval(function() {
            drawCircle.scrollList($MyGift);
        }, 2000);
    }).trigger("mouseout");

});

