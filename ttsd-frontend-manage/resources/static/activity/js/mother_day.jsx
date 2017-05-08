require('publicJs/login_tip');
let drawCircle = require('activityJsModule/gift_circle_draw');
require('activityStyle/mother_day.scss');
let tpl = require('art-template/dist/template');
let commonFun = require('publicJs/commonFun');


let $motherDayContainer = $('#motherDayContainer'),
    tipGroupObj = {},
    $isLogin=$('#isLogin'),
    $pointerBtn = $('.pointer-img',$motherDayContainer),
	$oneThousandPoints=$('.gift-item',$motherDayContainer),
	pointAllList='/activity/point-draw/all-list',  //中奖记录接口地址
    pointUserList='/activity/point-draw/user-list',   //我的奖品接口地址
    drawURL='/activity/point-draw/task-draw',   //抽奖的接口链接
	oneData={
		'activityCategory':'MOTHERS_DAY_ACTIVITY'
	};

$motherDayContainer.find('.tip-list-frame .tip-list').each(function (key, option) {
    let kind = $(option).attr('data-return');
    tipGroupObj[kind] = option;
});



//pointAllList:中奖记录接口地址
//pointUserList:我的奖品接口地址
//drawURL:抽奖的接口链接
//oneData:接口参数
//$oneThousandPoints:抽奖模版dom
var drawCircleOne=new drawCircle(pointAllList,pointUserList,drawURL,oneData,$oneThousandPoints);

//渲染中奖记录
drawCircleOne.GiftRecord(10);

//渲染我的奖品
drawCircleOne.MyGift(10);

//点击切换按钮
drawCircleOne.PrizeSwitch();

//开始抽奖
$pointerBtn.on('click', function(event) {
    // drawCircleOne.rotateFn(45*4-20,tipGroupObj['concrete']);
    drawCircleOne.beginLuckDraw(function(data) {
        //抽奖接口成功后奖品指向位置
        console.log(data);
        if (data.returnCode == 0) {
            var angleNum=0;
            switch (data.wechatLotteryPrize) {
                case 'MOTHERS_DAY_ACTIVITY_BEDDING': //纯棉四件套 
                    angleNum=45*1-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('纯棉四件套');
                    break;
                case 'MOTHERS_DAY_ACTIVITY_EXPERIENCE_GOLD_888': //888元体验金
                    angleNum=72*4-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('888元体验金');
                    break;
                case 'MOTHERS_DAY_ACTIVITY_ENVELOP_20':  //20元红包
                    angleNum=72*3-20;
                     $(tipGroupObj['concrete']).find('.prizeValue').text('20元红包');
                    break;
                case 'MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_1':  //1%加息券
                    angleNum=72*2-20;
                     $(tipGroupObj['concrete']).find('.prizeValue').text('1%加息券');
                    break;
                case 'MOTHERS_DAY_ACTIVITY_ENVELOP_10': //10元红包
                    angleNum=72*1-20;
                     $(tipGroupObj['concrete']).find('.prizeValue').text('10元红包');
                    break;
                case 'MOTHERS_DAY_ACTIVITY_EXPERIENCE_GOLD_8888': //8888元体验金
                    angleNum=72*1-20;
                     $(tipGroupObj['concrete']).find('.prizeValue').text('8888元体验金');
                    break;
                case 'MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5': //0.5%加息券
                    angleNum=72*1-20;
                     $(tipGroupObj['concrete']).find('.prizeValue').text('0.5%加息券');
                    break;
                case 'MOTHERS_DAY_ACTIVITY_ENVELOP_5': //5元红包
                    angleNum=72*1-20;
                     $(tipGroupObj['concrete']).find('.prizeValue').text('5元红包');
                    break;
            }
            drawCircleOne.rotateFn(angleNum,tipGroupObj['concrete']);

        } else if(data.returnCode == 1) {
            //抽奖次数不足
            drawCircleOne.tipWindowPop(tipGroupObj['nochance']);
        }
        else if (data.returnCode == 2) {
            //未登录
            layer.open({
				type: 1,
				title: false,
				closeBtn: 0,
				area: ['auto', 'auto'],
				content: $('#loginTip')
			});

        } else if(data.returnCode == 3){
            //不在活动时间范围内！
            drawCircleOne.tipWindowPop(tipGroupObj['expired']);

        } else if(data.returnCode == 4){
            //实名认证
            drawCircleOne.tipWindowPop(tipGroupObj['authentication']);
        }
    });
});

$isLogin.on('click', function(event) {
	event.preventDefault();
	layer.open({
		type: 1,
		title: false,
		closeBtn: 0,
		area: ['auto', 'auto'],
		content: $('#loginTip')
	});
});


    