require("activityStyle/year_award_2017.scss");
// require("activityStyle/media.scss");
let commonFun= require('publicJs/commonFun');
let sourceKind = globalFun.parseURL(location.href);
require('publicJs/login_tip');
let drawCircle = require('activityJsModule/gift_circle_draw');
let $ownRecord = $('#myRecord');
let $pageNumber = $('#pageNo_container');

if ($(document).width() < 790) {
    (function (doc, win) {
        var docEl = doc.documentElement,
            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
            recalc = function () {
                var clientWidth = docEl.clientWidth;
                if (!clientWidth) return;
                var fSize = 20 * (clientWidth /375);
                fSize > 40 && (fSize = 39.36);
                docEl.style.fontSize = fSize + 'px';
            };

        if (!doc.addEventListener) return;
        win.addEventListener(resizeEvt, recalc, false);
        doc.addEventListener('DOMContentLoaded', recalc, false);
    })(document, window);
}

function showMoreData(num) {
    if (num < 4) return;
    let $height = $('#investRanking-tbody').find('tr').height();
    let $seenArea = $('#tableListWrapper').find('thead').height();
    $('#tableListWrapper').css('height',$seenArea + 3 * $height);
    $('#show_more_listData').show();
    $('#show_more_listData').on('click',() => {
        $('#tableListWrapper').css('height',$seenArea + num * $height);
        $('#tableListWrapper').css('overflow','visible');
        $('#show_more_listData').hide();
        $('#show_less_listData').show();
    });
    $('#show_less_listData').on('click', () => {
        $('#tableListWrapper').css('height',$seenArea + 3 * $height);
        $('#tableListWrapper').css('overflow','hidden');
        $('#show_less_listData').hide();
        $('#show_more_listData').show();
    })
}

let mockData = false; //模拟数据

let $investRankingButton = $('#investRanking-button'),
    $heroNext = $('#heroNext'),
    $heroPre = $('#heroPre');

let $sortBox = $('#rank_list_survey'),
    $date = $('.date_info_No', $sortBox),
    $totalAmount = $('.today_totalAccountNo', $sortBox),
    $rankingOrder = $('.myRank_info_No', $sortBox);

let todayDay = $.trim($date.text());
let startTime = Number($date.data('starttime').substring(0, 10).replace(/-/gi, '')),
    endTime = Number($date.data('endtime').substring(0, 10).replace(/-/gi, ''));

let $nodataInvest = $('.nodata-invest'),
    $contentRanking = $('#investRanking-tbody');

getPrize(drawCircle);



// 设置进度条的比例

$('.percent_wrapper_sub').css('width','30%');

function getPrize(obj) {
    let tipGroupObj = {};
    let ifShowPageBtn = false;
    let $christmasDayFrame = $('#doubleElevenContainer'); //整个页面
    //抽奖模块
    let $rewardGiftBox=$('.nine-lottery-group',$christmasDayFrame);
    let $MobileNumber=$('#MobileNumber'),
        pointAllList='/activity/year-end-awards/all-list',  //中奖记录接口地址
        pointUserList='/activity/year-end-awards/user-list',   //我的奖品接口地址
        drawURL='/activity/year-end-awards/draw',    //抽奖的接口链接
        $pointerImg=$('.lottery-btn',$rewardGiftBox), //抽奖按钮
        myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

    $christmasDayFrame.find('.tip-list-frame .tip-list').each(function(key,option) {  //
        let kind=$(option).data('return');
        tipGroupObj[kind]=option;
    });

    let paramData={  // 拉取中奖记录及我的奖品列表接口所需数据
        "mobile":myMobileNumber,
        "activityCategory":"YEAR_END_AWARDS_ACTIVITY"
    };

    let drawCircle = new obj(pointAllList,pointUserList,drawURL,paramData,$rewardGiftBox);

    drawCircle.GiftRecord();  //渲染中奖记录

    //通过判断是否登录显示隐藏相应的按钮
    $.when(commonFun.isUserLogin())
        .done(function () {
            //渲染我的奖品
            drawCircle.MyGift(function(data){
                if(data.length == 0){
                    drawCircle.removeClass('show-pageNumber');
                    drawCircle.html('<li class="noGiftTip">没有中奖纪录哦~</li>');
                    drawCircle.find('li').css({
                        'textAlign':'center',
                        'textIndent':'0px'
                    });
                    $pageNumber.hide();
                } else if(data.length !== 0){
                    ifShowPageBtn = true;
                }
            });
        })
        .fail(function(){
            $ownRecord.html('<li class="loginToSee"><span id="toGiftLogin" class="toGiftLogin">登录</span>后查看获奖记录</li>');
            $('#toGiftLogin').on('click',function(event){
                event.preventDefault();
                //判断是否需要弹框登陆
                toLogin();

            });
            $ownRecord.find('li').css({
                'textAlign':'center',
                'textIndent':0
            });
        });

    $pointerImg.on('click',function() {
        drawCircle.beginLuckDraw(function(data) {
            let prizeKind;
            if (mockData) {
                data = {
                    drawLotterySuccess: false,
                    message: null,
                    myPoint: null,
                    prize: 'INTEREST_COUPON_5_POINT_DRAW_REF_CARNIVAL', // 100元红包
                    prizeType: null,
                    prizeValue: null,
                    returnCode: 3,
                    status: false
                }
            };

            if (data.returnCode == 0) {

                switch (data.prize) {
                    case 'YEAR_END_AWARDS_ACTIVITY_JD_E_CARD_200':  //200元京东E卡
                        prizeKind=0;
                        break;
                    case 'YEAR_END_AWARDS_ACTIVITY_ENVELOP_120': //120元红包
                        prizeKind=1;
                        break;
                    case 'YEAR_END_AWARDS_ACTIVITY_ENVELOP_50':  //50元红包
                        prizeKind=2;
                        break;
                    case 'YEAR_END_AWARDS_ACTIVITY_ENVELOP_10':  //10元红包
                        prizeKind=7;
                        break;
                    case 'YEAR_END_AWARDS_ACTIVITY_EXPERIENCE_GOLD_500':  //500元体验金
                        prizeKind=3;
                        break;
                    case 'YEAR_END_AWARDS_ACTIVITY_ENVELOP_100':  //100元红包
                        prizeKind=6;
                        break;
                    case 'YEAR_END_AWARDS_ACTIVITY_ENVELOP_20': //20元红包
                        prizeKind=5;
                        break;
                    case 'YEAR_END_AWARDS_ACTIVITY_ENVELOP_200':  //200元红包
                        prizeKind=4;
                        break;
                }

                // let prizeType=data.prizeType.toLowerCase();
                // $(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);

                drawCircle.lotteryRoll({
                    elementId:'drawLotteryAreaSub',
                    speed:100,
                    prize:prizeKind
                },null); // 参数1：抽奖参数； 参数2：提示信息

            } else if(data.returnCode == 1) {
                //没有抽奖机会
                drawCircle.tipWindowPop(tipGroupObj['nochance']);
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
    let menuCls=$('.lottery-right-group').find('h3 span');
    menuCls.on('click',function() {
        let $this=$(this),
            index=$this.index(),
            contentCls=$('#stateContainerTip_m').find('.record-item');
        $this.addClass('active').siblings().removeClass('active');
        contentCls.eq(index).show().siblings().hide();
        if((ifShowPageBtn) && index === 1) {
            if ($(document).width() >= 790) {
                $pageNumber.show();
            }
            else {
                $('.myRecordWrapper').css('overflow','scroll');
            }
        }else {
            $pageNumber.hide();
        }
    });
};

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
};

//点击登录弹框登录

$('.toGiftLogin').on('click',function(event){
    event.preventDefault();
    //判断是否需要弹框登陆
     toLogin();

});

//我的奖品翻页效果
function pageTurn() {
    let $ownList = $('.own-record'),
        $pageNumber = $('.page-number');
    let totalNumber = $ownList.find('li').length,
        pageSize = 8, //每页8条
        pageIndex = 1,
        totalPage = Math.ceil(totalNumber/pageSize),
        heightContent = $('.myRecordWrapper').height();
    $pageNumber.on('click',function() {
        let index = $(this).index();
        if(index === 0) {
            //上一页
            if(pageIndex==1) {
                return;
            }
            pageIndex = (pageIndex>1) ? (pageIndex-1) : pageIndex;
        } else if(index === 1) {
            //下一页
            if(pageIndex ==totalPage) {
                return;
            }
            pageIndex = (pageIndex<totalPage) ? (pageIndex+1) : pageIndex;
        }
        let TopDistance = -heightContent * (pageIndex-1);
        let currentPosTop = $ownList.position().top;
        if (TopDistance === currentPosTop) return;
        $ownList.animate({
            top:TopDistance + 'px'
        });
    });
}
pageTurn();



//英雄榜排名,今日投资排行
function heroRank(date) {
    commonFun.useAjax({
        type: 'GET',
        url: '/activity/year-end-awards/ranking/' + date
    }, function (data) {
        if (data.status) {
            if (_.isNull(data.records) || data.records.length == 0) {
                $('#investRanking-tbody').html('');
                return;
            }
            //获取模版内容
            let ListTpl = $('#tplTable').html();
            // 解析模板, 返回解析后的内容
            let render = _.template(ListTpl);
            let html = render(data);
            $('#investRanking-tbody').html(html);
            showMoreData(data.records.length);
        }
    });

    commonFun.useAjax({
        type: 'GET',
        url: '/activity/year-end-awards/my-ranking/' + date
    }, function (data) {
        //今日投资总额 和 排名
        let investRanking = data.investRanking;
        $totalAmount.text(data.investAmount / 100 + '元');
        if(investRanking==0) {
            $rankingOrder.text('未上榜');
        } else {
            $rankingOrder.text(data.investRanking);
        }

    })
}

$('#toInvest').on('click',function() {
    window.location.href = '/loan-list';
});


function activityStatus(nowDay) {
    let nowDayStr = Number(nowDay.replace(/-/gi, '')),
        todayDayStr = Number(todayDay.replace(/-/gi, '')),
        isToday = nowDayStr==todayDayStr;
    endTime = (todayDayStr < endTime) ? todayDayStr : endTime;
    $nodataInvest.hide();

    if (nowDayStr < startTime) {
        //活动未开始
        $heroPre.css({'visibility':'hidden'});
        $heroNext.css({'visibility':'hidden'});
        $contentRanking.hide();
    }
    else if (nowDayStr > endTime) {
        //活动已经结束
        $heroNext.css({'visibility':'hidden'});
        $heroPre.css({'visibility':'visible'});
        $contentRanking.hide();
        $nodataInvest.show().html('活动已经结束');

    }  else if(nowDayStr>=startTime && nowDayStr<=endTime){
        //活动中
        $heroNext.css({'visibility':'visible'});
        $heroPre.css({'visibility':'visible'});
        $contentRanking.show();
        if(nowDayStr==startTime) {
            //活动第一天
            $heroPre.css({'visibility':'hidden'});
        } else if(nowDayStr==endTime) {
            //活动最后一天
            $heroNext.css({'visibility':'hidden'});
        }
        heroRank(nowDay);
    }

    $('.is-today').text(function() {
        return isToday ? '今日' : '当日'
    });
}

//页面初始
activityStatus(todayDay);









