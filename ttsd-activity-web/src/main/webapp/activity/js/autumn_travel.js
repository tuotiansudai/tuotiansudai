require(['jquery', 'underscore', 'layerWrapper','drawCircle','template', 'register_common'], function ($, _, layer,drawCircle,tpl) {

var $autumnTravelPage=$('#autumnTravelPage'),
    tipGroupObj={},
    $giftCircleFrame=$('.gift-circle-frame',$autumnTravelPage);

    var browser = globalFun.browserRedirect();
    var locationUrl=location.href;
    var sourceKind=globalFun.parseURL(locationUrl);

    if (browser == 'mobile') {
        if(sourceKind.params.tag=='yes') {
            $autumnTravelPage.find('.reg-tag-current').show();
        }
    }

    var $loginName = $('div.login-name');
    var loginName = $loginName ? $loginName.data('login-name') : '';

    $autumnTravelPage.find('.tip-list-frame .tip-list').each(function(key,option) {
        var kind=$(option).data('return');
        tipGroupObj[kind]=option;
    });

    $("a.autumn-travel-invest-channel").click(function () {
        $.ajax({
            url: '/activity/autumn/travel/invest?loginName=' + loginName,
            type: 'POST'
        });
    });

    //以下为抽奖转盘
    var $pointer = $('.pointer-img',$giftCircleFrame);
    var $MobileNumber=$('#MobileNumber'),
        travelAllList='/activity/autumn/travel-all-list',  //中奖记录接口地址
        travelUserList='/activity/autumn/travel-user-list',   //我的奖品接口地址
        drawURL='/activity/autumn/travel-draw',    //抽奖的接口链接
        myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

    var $GiftRecord=$('.user-record',$giftCircleFrame),
        $MyGift=$('.own-record',$giftCircleFrame);

    var tipList=$('.tip-list',$autumnTravelPage);
    var oneData={
        "mobile":myMobileNumber
    };
    //pointAllList:中奖记录接口地址
    //pointUserList:我的奖品接口地址
    //drawURL:抽奖的接口链接
    //oneData:接口参数
    //tipList:弹框dom
    //$oneThousandPoints:抽奖模版dom
    var drawCircle=new drawCircle(travelAllList,travelUserList,drawURL,oneData,tipList,$giftCircleFrame);

    //渲染中奖记录
    drawCircle.GiftRecord();

    //渲染我的奖品
    drawCircle.MyGift();

    //开始抽奖
    $pointer.on('click', function(event) {
        drawCircle.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                var angleNum=0;
                switch (data.prize) {
                    case 'PORCELAIN_CUP':
                        angleNum = 337;
                        break;
                    case 'INTEREST_COUPON_2':
                        angleNum = 56;
                        break;
                    case 'LUXURY':
                        angleNum = 116;
                        break;
                    case 'RED_ENVELOPE_100':
                        angleNum = 160;
                        break;
                    case 'INTEREST_COUPON_5':
                        angleNum = 230;
                        break;
                    case 'RED_ENVELOPE_50':
                        angleNum = 300;
                        break;
                }

                var prizeType=data.prizeType.toLowerCase();
                $(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);

                drawCircle.rotateFn(angleNum,tipGroupObj[prizeType]);
            } else if (data.returnCode == 2) {

                //未登陆
                if(sourceKind.params.source=='app') {
                    location.href="/login";
                } else {
                    $('.no-login-text',$autumnTravelPage).trigger('click');  //弹框登录
                }

            } else if(data.returnCode == 3){
                //不在活动时间范围内！
                drawCircle.tipWindowPop(tipGroupObj['expired']);
            }
        });
    });

    //点击切换按钮
    drawCircle.PrizeSwitch();

    //中奖记录,我的奖品超过10条数据滚动
    drawCircle.scrollList($GiftRecord);
    drawCircle.scrollList($MyGift);

});