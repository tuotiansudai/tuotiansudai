require("activityStyle/double11_2017.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let drawCircle = require('activityJsModule/gift_circle_draw');
let tpl = require('art-template/dist/template');
let sourceKind = globalFun.parseURL(location.href);
//require('activityJsModule/fast_register');
//pointAllList:中奖记录接口地址
//pointUserList:我的奖品接口地址
//drawURL:抽奖的接口链接
//oneData:接口参数
//$oneThousandPoints:抽奖模版dom

let $double11 = $('#double11'),
    tipGroupObj = {};
var $pointerBtn = $('#draw_btn',$double11);
var $oneThousandPoints=$('.gift-circle-frame',$double11);
var $toLogin = $('#to_login_DOM'),
    $leftDrawDOM = $('#left_draw_DOM'),
    $loginBtn = $('.to-login-btn',$double11),
    $prizeLoginDOM = $('#prize_login_DOM'),//京东e卡里去登录
    $rewardCount = $('#reward_count'),//京东e卡获得的金额
    $ownRecord = $('.own-record'),//我的奖品列表ul
    $pageNumber = $('#pageNumber'),//分页
    $myGiftDOM = $('#myGiftDOM'),
    $recodeList  = $('.record-list',$double11),//中奖纪记录大框
    $iconPrize = $('.icon_prize','.tip-list');//我的奖品切换按钮
var pointAllList='/activity/double-eleven/all-list',  //中奖记录接口地址
    pointUserList='/activity/double-eleven/user-list',   //我的奖品接口地址
    drawURL='/activity/double-eleven/task-draw',//抽奖接口
    leftDraw = '/activity/double-eleven/left-times';    //剩余抽奖次数接口

let redWareUrl = require('../images/2017/double11/red_ware.png');
let conpouUrl = require('../images/2017/double11/coupon.png');
let jdeUrl = require('../images/2017/double11/icon_jd.png');
let experienceUrl = require('../images/2017/double11/experience_icon.png');
//京东卡字体居中
let $money = $('#money');
let $rewardCon = $('#rewardCon');
let totalMoney = $money.width() + $rewardCount.width();
$rewardCon.width(totalMoney);
var oneData={
        'activityCategory':'DOUBLE_ELEVEN_ACTIVITY'
    },
    $leftDrawCount=$('#leftDrawCount');

$double11.find('.tip-list-frame .tip-list').each(function (key, option) {
    let kind = $(option).attr('data-return');
    tipGroupObj[kind] = option;
});
//抽奖次数
function drawTimes(){
    commonFun.useAjax({
        dataType: 'json',
        type:'get',
        url:leftDraw
    },function(data) {
        console.log(data);
        $leftDrawCount.text(data);
    });
}

//通过判断是否登录显示隐藏相应的按钮
$.when(commonFun.isUserLogin())
    .done(function () {
       $toLogin.hide();
        $leftDrawDOM.show();
        $prizeLoginDOM.hide();
        drawTimes();
        //渲染我的奖品
        drawCircleOne.MyGift(function(data){
                if(data.length == 0){
                    $recodeList.removeClass('show-pageNumber');
                    $ownRecord.html('<li>没有中奖纪录哦~</li>');
                    $ownRecord.find('li').css({
                        'textAlign':'center',
                        'textIndent':'0px'
                    });
                    $pageNumber.hide();
                } else if(data.length !== 0){
                    $recodeList.addClass('show-pageNumber');
                }


        });
    })
    .fail(function(){
        $leftDrawDOM.hide();
        $toLogin.show();
        $prizeLoginDOM.show();
        //$rewardCount.text('?');
        $ownRecord.html('<li><a href="javascript:;" id="toGiftLogin" class="font-underline my-gift-color">登录</a>后查看获奖记录</li>');
        $('#toGiftLogin').on('click',function(event){
            event.preventDefault();
            //判断是否需要弹框登陆
            toLogin();

        })
        $ownRecord.find('li').css({
            'textAlign':'center',
            'textIndent':0
        });
    });
//去登录
function toLogin() {
    if (sourceKind.params.source == 'app') {
        location.href = "/login";
    }else {
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['auto', 'auto'],
            content: $('#loginTip')
        });
    }
}
//点击登录弹框登录

$('#toLoginBtnDraw,#toLoginBtnPrize').on('click',function(event){
    event.preventDefault();
    //判断是否需要弹框登陆
    toLogin();

})
var drawCircleOne=new drawCircle(pointAllList,pointUserList,drawURL,oneData,$oneThousandPoints);

//渲染中奖记录
let $userRecord = $('#user_record');
drawCircleOne.GiftRecord(function(data){
    if(data== 0) {
        $userRecord.html('<li>没有中奖纪录哦~</li>');
        $userRecord.find('li').css({
            'textAlign':'center',
            'textIndent':'0px'
        })
    }
});

drawCircleOne.scrollUp($double11.find('.user-record'),1000);

//开始抽奖
$('#draw_btn,.pointer-img').on('click', function(event) {
    drawCircleOne.beginLuckDraw(function(data) {

        //抽奖接口成功后奖品指向位置
        if (data.returnCode == 0) {
            var angleNum=0;
            drawTimes();
            switch (data.prize) {
                case 'DOUBLE_ELEVEN_ACTIVITY_INTEREST_COUPON_5': //0.5%加息券
                    angleNum=45*0;
                    $(tipGroupObj['virtual']).find('.prizeValue').text('0.5%加息券');
                    $iconPrize.css({
                        'backgroundImage':'url('+conpouUrl+')'
                    });

                    break;
                case 'DOUBLE_ELEVEN_ACTIVITY_JD_E_CARD_200': //200元京东E卡
                    angleNum=45*1;
                    $(tipGroupObj['virtual']).find('.prizeValue').text('200元京东E卡');
                    $iconPrize.css({
                        'backgroundImage':'url('+jdeUrl+')'
                    })
                    break;
                case 'DOUBLE_ELEVEN_ACTIVITY_ENVELOP_200': //200元红包
                    angleNum=45*2;
                    $(tipGroupObj['virtual']).find('.prizeValue').text('200元红包');
                    $iconPrize.css({
                        'backgroundImage':'url('+redWareUrl+')'
                    })
                    break;
                case 'DOUBLE_ELEVEN_ACTIVITY_INTEREST_COUPON_2': //0.2%加息券
                    angleNum=45*3;
                    $(tipGroupObj['virtual']).find('.prizeValue').text('0.2%加息券');
                    $iconPrize.css({
                        'backgroundImage':'url('+conpouUrl+')'
                    })
                    break;
                case 'DOUBLE_ELEVEN_ACTIVITY_ENVELOP_100': //100元红包
                    angleNum=45*4;
                    $(tipGroupObj['virtual']).find('.prizeValue').text('100元红包');
                    $iconPrize.css({
                        'backgroundImage':'url('+redWareUrl+')'
                    })
                    break;
                case 'DOUBLE_ELEVEN_ACTIVITY_ENVELOP_20':  //20元红包
                    angleNum=45*5;
                    $(tipGroupObj['virtual']).find('.prizeValue').text('20元红包');
                    $iconPrize.css({
                        'backgroundImage':'url('+redWareUrl+')'
                    })
                    break;
                case 'DOUBLE_ELEVEN_ACTIVITY_EXPERIENCE_GOLD_1000':  //1000元体验金
                    angleNum=45*6;
                    $(tipGroupObj['virtual']).find('.prizeValue').text('1000元体验金');
                    $iconPrize.css({
                        'backgroundImage':'url('+experienceUrl+')'
                    })
                    break;
                case 'DOUBLE_ELEVEN_ACTIVITY_ENVELOP_50': //50元红包
                    angleNum=45*7;
                    $(tipGroupObj['virtual']).find('.prizeValue').text('50元红包');
                    $iconPrize.css({
                        'backgroundImage':'url('+redWareUrl+')'
                    })
                    break;
            }
            drawCircleOne.rotateFn(angleNum,tipGroupObj['virtual']);

        } else if(data.returnCode == 1) {
            //抽奖次数不足
            drawCircleOne.tipWindowPop(tipGroupObj['nochance']);
        }
        else if (data.returnCode == 2) {
            //未登录
            toLogin();

        } else if(data.returnCode == 3){
            //不在活动时间范围内！
            layer.msg('不在活动时间范围内~');

        } else if(data.returnCode == 4){
            //今日没有抽奖机会了
            layer.msg('今天没有抽奖机会了哦~，明天再来吧',{
                time:3000
            });

        }
    });
});

//点击切换按钮
function switchPrize(){
    var menuCls = $('.gift-record').find('li'),
        contentCls = $double11.find('.record-list ul');
    menuCls.on('click', function (index) {
        var $this = $(this),
            index = $this.index();
        $this.addClass('active').siblings().removeClass('active');
        contentCls.eq(index).show().siblings().hide();
        if($recodeList.hasClass('show-pageNumber')&&$myGiftDOM.hasClass('active')) {
            $pageNumber.show();
            pageTurn();
        }else {
            $pageNumber.hide();
        }
    });

}
switchPrize();
//我的奖品翻页效果
function pageTurn() {
    let $ownList = $('.own-record',$double11),
        $pageNumber = $('.page-number',$double11);
    let totalNumber = $ownList.find('li').length,
        pageSize = 8, //每页5条
        pageIndex = 1,
        totalPage = Math.ceil(totalNumber/pageSize),
        heightContent = $recodeList.height();
    $pageNumber.find('i').on('click',function(index) {
        console.log(totalNumber)
        let thisClass = this.className;
        if(thisClass=='icon-left') {
            //上一页
            if(pageIndex==1) {
                return;
            }
            pageIndex = (pageIndex>1) ? (pageIndex-1) : pageIndex;
        } else if(thisClass == 'icon-right') {
            //下一页
            if(pageIndex ==totalPage) {
                return;
            }
            pageIndex = (pageIndex<totalPage) ? (pageIndex+1) : pageIndex;
        }
        $('.page-index',$pageNumber).text(pageIndex);
        let TopDistance = -heightContent * (pageIndex-1);
        $ownList.animate({
            top:TopDistance + 'px'
        });
    });
}



