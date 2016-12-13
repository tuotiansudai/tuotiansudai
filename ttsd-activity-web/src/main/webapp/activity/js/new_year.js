require(['jquery','drawCircle','commonFun','logintip','register_common'], function ($,drawCircle,commonFun) {
    $(function() {

        var redirect = globalFun.browserRedirect();
        var $newYearDayFrame = $('#newYearDayFrame');
        var $activitySlide=$('#newYearSlide'),
            tipGroupObj={};

        $newYearDayFrame.find('.tip-list').each(function(key,option) {
            var kind=$(option).data('return');
            tipGroupObj[kind]=option;
        });

        //文字连续滚动
        (function() {
            var $slideText=$('.slide-text',$newYearDayFrame);
            var lineHeight = $slideText.find('li').eq(0).height();
            var textTimer;

            $slideText.hover(function() {
                clearInterval(textTimer);
            },function() {
                textTimer = setInterval(function() {
                    var $ulList=$slideText.find('.slide-text-list');
                    $ulList.animate({
                        "margin-top": -lineHeight + "px"
                    }, 2000, function() {
                        $ulList.css({
                            "margin-top": "0px"
                        })
                            .find("li:first")
                            .appendTo($ulList);
                    });
                }, 500);
            }).trigger('mouseout');
        })();

        //为节约手机流量，把pc页面的图片在pc页上显示才增加
        (function() {
            if(redirect=='pc') {
                var HTML='<div class="body-decorate" id="bodyDecorate"> ' +
                    '<div class="bg-left"></div> ' +
                    '<div class="bg-right"></div> ' +
                    '</div>';
                $newYearDayFrame.prepend(HTML);
                $activitySlide.addClass('pc-img');
            }
            else {
                $activitySlide.html('<img src='+staticServer+'/activity/images/christmas-day/app-top.jpg>');

                // 是否加载快速注册的功能
               var urlObj=globalFun.parseURL(location.href);
                if(urlObj.params.tag=='yes') {
                    $newYearDayFrame.find('.reg-tag-current').show();
                 }
            }
        })();

        (function(drawCircle) {
            //抽奖模块
            var $rewardGiftBox=$('.reward-gift-box',$newYearDayFrame);
            var $rewardTaskStatus=$('#rewardTaskStatus'),
                $rewardListFrame=$('.reward-list-frame',$newYearDayFrame);

            var $MobileNumber=$('#MobileNumber'),
                pointAllList='/activity/point-draw/all-list',  //中奖记录接口地址
                pointUserList='/activity/point-draw/user-list',   //我的奖品接口地址
                drawURL='/activity/point-draw/task-draw',    //抽奖的接口链接
                // drawTime='/activity/christmas/drawTime', //抽奖次数
                $pointerImg=$('.gold-egg',$rewardGiftBox),
                myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

            var myTimes=$rewardGiftBox.find('.my-times').data('times'); //初始抽奖次数
            var paramData={
                "mobile":myMobileNumber,
                "activityCategory":"ANNUAL_ACTIVITY"
            };

            //是否已经完成
            $rewardListFrame.find('.reward-box').each(function(key,option) {
                var statusObj=$rewardTaskStatus.val().split(',');
                if(statusObj[key]==1) {
                    $(option).addClass('finished');
                }
            });

            //签到
            drawCircle.prototype.signToday=function(callback,failFun) {
                var $signToday=$('#signToday');
                $signToday.on('click',function() {
                    $.ajax({
                        url:'/point/sign-in',
                        type:'POST',
                        dataType: 'json'
                    })
                        .done(function(response) {
                            callback && callback(response);
                        })
                        .fail(function() {
                            failFun && failFun()
                        })
                })
            }

            var drawCircle=new drawCircle(pointAllList,pointUserList,drawURL,paramData,$rewardGiftBox);

            //签到成功
            drawCircle.signToday(function() {
                drawCircle.tipWindowPop(tipGroupObj['signOk']);
            },function() {
                drawCircle.tipWindowPop(tipGroupObj['signNo']);
            });

            //渲染中奖记录
            drawCircle.GiftRecord();

            //渲染我的奖品
            drawCircle.MyGift();

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

                        // 抽奖次数
                        $rewardGiftBox.find('.my-times').text(--myTimes);

                        if (data.returnCode == 0) {
                            var prizeType=data.prizeType.toLowerCase();
                                $(tipGroupObj[prizeType]).find('.prizeValue').text(prizeType);
                            drawCircle.noRotateFn();
                        } else if(data.returnCode == 1) {
                            //没有抽奖机会
                            drawCircle.tipWindowPop(tipGroupObj['nochance']);
                        }
                        else if (data.returnCode == 2) {
                            //未登录
                            $('.no-login-text',$newYearDayFrame).trigger('click');  //弹框登录

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

            //点击切换按钮
            drawCircle.PrizeSwitch();

        })(drawCircle);
    })
});


