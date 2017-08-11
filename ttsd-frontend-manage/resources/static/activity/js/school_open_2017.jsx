require("activityStyle/school_open_2017.scss");
let drawCircleFun = require('activityJsModule/gift_circle_draw');
let commonFun = require('publicJs/commonFun');
let redirect = globalFun.browserRedirect();
let $activityPageFrame = $('#activityPageFrame');
let $myRecordLink = $('.my-record-link',$activityPageFrame),
    $luckDrawBox = $('#luckDrawBox'),
    tipGroupObj = {},
    sourceKind = globalFun.parseURL(location.href);


let $pointerImg = $('.draw-bucket',$luckDrawBox),
    pointAllList='/activity/point-draw/all-list',  //中奖记录接口地址
    pointUserList='/activity/point-draw/user-list',   //我的奖品接口地址
    drawURL='/activity/point-draw/task-draw',   //抽奖的接口链接
    paramData={
        'activityCategory':'MOTHERS_DAY_ACTIVITY'
        };

$activityPageFrame.find('.tip-list-frame .tip-list').each(function (key, option) {
    let kind = $(option).data('return');
    tipGroupObj[kind] = option;
});


//跑马灯效果
(function() {
    let $userRecord= $('.user-record',$activityPageFrame),
        lis = $userRecord.find('li');
    let htmlUl = $userRecord.html();
    let leftW=0;
    $userRecord.append(htmlUl);
    $userRecord.width(lis.length * lis[0].offsetWidth);

    let timer = setInterval(function() {
        leftW-=3;
        if(Math.abs(leftW) > ($userRecord.width()/2)-150) {
            leftW=0;
        }
        $userRecord.css({'left':leftW});
    },50);

})();

//排名翻页效果
(function() {
    let $tableList = $('.table-list',$activityPageFrame),
        $pageNumber = $('.page-number',$activityPageFrame);
    let totalNumber = $tableList.find('li').length,
        pageSize = 5, //每页5条
        pageIndex = 1,
        totalPage = Math.ceil(totalNumber/pageSize);

    $pageNumber.find('i').on('click',function(index) {
        let thisClass = this.className;
        if(thisClass=='icon-left') {
            //上一页
            pageIndex = (pageIndex>1) ? (pageIndex-1) : pageIndex;
        } else if(thisClass == 'icon-right') {
            //下一页
            pageIndex = (pageIndex<totalPage) ? (pageIndex+1) : pageIndex;
        }
        $('.page-index',$pageNumber).text(pageIndex);
        let TopDistance = -235 * (pageIndex-1);
        $tableList.animate({
            left:"0",
            top:TopDistance + 'px'
        });
    });

})();

(function() {
    let $RewardBoxOut= $('.reward-box-list-out',$activityPageFrame);
    if(redirect=='mobile') {
        let [bb,aa,cc] = Array.from($RewardBoxOut.find('.reward-box-list'));
        $RewardBoxOut.empty().html(aa.outerHTML+bb.outerHTML+cc.outerHTML);
    }
})();

//pointAllList:中奖记录接口地址
//pointUserList:我的奖品接口地址
//drawURL:抽奖的接口链接
//oneData:接口参数
//$oneThousandPoints:抽奖模版dom
var drawCircle=new drawCircleFun(pointAllList,pointUserList,drawURL,paramData,$luckDrawBox);

//渲染奖品
drawCircle.MyGift();
drawCircle.GiftRecord();

let $ownRecord = $('.own-record',$activityPageFrame);

drawCircle.scrollList($ownRecord,9);
drawCircle.hoverScrollList($ownRecord,9);


// 写cookies
function setCookie(name,value)
{
    var exp = new Date(),
        year = exp.getFullYear(),
        month = exp.getMonth()+1,
        day = exp.getDate();

    let second = new Date(year,month,day,24,0,0);
    let distanceTime = second.getTime() - exp.getTime();

    exp.setTime(exp.getTime() + distanceTime);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}

if(sourceKind.params.school =='yes') {
    let rank_top = $('#RankingBox').offset().top;
    $('body,html').animate({scrollTop:rank_top},'fast');

}

//**********************开始抽奖**********************//
$pointerImg.on('click',function() {

    //判断是否正在抽奖
    if($pointerImg.hasClass('win-result')) {
        return;//不能重复抽奖
    }
    $pointerImg.addClass('win-result');
    //延迟1.5秒抽奖
    setTimeout(function() {

        drawCircle.beginLuckDraw(function(data) {
            //停止鸡蛋的动画
            $pointerImg.removeClass('win-result');
            if (data.returnCode == 0) {
                var treasureUrl;

                setCookie('drawSignToday','1');
                if(sourceKind.params.source=='app') {
                    treasureUrl='app/tuotian/myfortune-unuse';
                } else {
                    treasureUrl='/my-treasure';
                }

                var prizeType=data.prizeType.toLowerCase();
                $(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);
                $(tipGroupObj[prizeType]).find('.my-treasure').attr('href',treasureUrl);
                var myTimes = parseInt($luckDrawBox.find('.my-number').text());
                // 抽奖次数
                $luckDrawBox.find('.my-number').text(function() {
                    return myTimes>0?(myTimes-1):0;
                });

                drawCircle.noRotateFn(tipGroupObj[prizeType]);

            } else if(data.returnCode == 1) {
                //没有抽奖机会
                $luckDrawBox.find('.my-number').text('0');
                drawCircle.tipWindowPop(tipGroupObj['nochance']);
            }
            else if (data.returnCode == 2) {
                //未登陆
                if(sourceKind.params.source=='app') {
                    location.href="/login";
                } else {
                    $('.no-login-text',$activityPageFrame).trigger('click');  //弹框登录
                }

            } else if(data.returnCode == 3){
                //不在活动时间范围内！
                drawCircle.tipWindowPop(tipGroupObj['expired']);

            } else if(data.returnCode == 4){
                //实名认证
                drawCircle.tipWindowPop(tipGroupObj['authentication']);
            }
        });

    },1500);
});


$myRecordLink.on('click',function() {

    layer.open({
        type: 1,
        title: false,
        area: ['auto', 'auto'],
        skin:'layer-reward-box',
        content: $('.layer-reward-list')
    });

});