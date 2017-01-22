require(['jquery','drawCircle','commonFun','logintip','register_common'], function ($,drawCircle,commonFun) {
    $(function() {

        var redirect = globalFun.browserRedirect();
        var $newYearDayFrame = $('#newYearDayFrame');
        var $activitySlide=$('#newYearSlide');
        var $loginInBtn=$('#loginIn');
        var locationUrl=location.href;
        var sourceKind=globalFun.parseURL(locationUrl);
        var tipGroupObj={};

        $newYearDayFrame.find('.tip-list').each(function(key,option) {
            var kind=$(option).data('return');
            tipGroupObj[kind]=option;
        });

        (function() {
            $loginInBtn.on('click',function() {
                if(sourceKind.params.source=='app') {
                    location.href="/login";
                } else {
                    $('.no-login-text',$newYearDayFrame).trigger('click');  //弹框登录
                }
            });

            //邀请好友在PC和APP端链接不同
            $('.to-referrer',$newYearDayFrame).on('click',function() {
                if(sourceKind.params.source=='app') {
                    location.href='/app/tuotian/refer-reward';
                }
                else {
                    location.href='/referrer/refer-list';
                }
            });

        })();

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
                $activitySlide.html('<img src='+staticServer+'/activity/images/new-year/app-top.jpg>');

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
                $pointerImg=$('.gold-egg',$rewardGiftBox),
                myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号
            var $signToday=$('#signToday');
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

             $.when(commonFun.isUserLogin())
                 .done(function(){
                     $('.signedIn-status .normal-button',$rewardGiftBox).show();
                 })
                 .fail(function(){
                     console.log('未登陆');
                     $loginInBtn.show();
                 });

            //签到
            drawCircle.prototype.signToday=function(callback,failFun) {

                $signToday.on('click',function(event) {
                    if(event.target.id!='signToday') {
                        return;
                    }
                    $(this).prop('disabled',true);
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
                drawCircle.tipWindowPop(tipGroupObj['signOk'],function() {
                    $signToday.text('已签到');
                    var thisTime = Number($rewardGiftBox.find('.my-times').text());
                    if($("#inActivityDate").val() == "true"){
                          $rewardGiftBox.find('.my-times').text(thisTime+1);
                        }

                    $signToday.removeAttr('id');
                });

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
                        if (data.returnCode == 0) {
                            var treasureUrl;
                            if(sourceKind.params.source=='app') {
                                treasureUrl='app/tuotian/myfortune-unuse';
                            } else {
                                treasureUrl='/my-treasure';
                            }

                            var prizeType=data.prizeType.toLowerCase();
                                $(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);
                            $(tipGroupObj[prizeType]).find('.my-treasure').attr('href',treasureUrl);
                            var myTimes = parseInt($rewardGiftBox.find('.my-times').text());
                            // 抽奖次数
                            $rewardGiftBox.find('.my-times').text(function() {
                                return myTimes>0?(myTimes-1):0;
                            });

                            drawCircle.noRotateFn(tipGroupObj[prizeType]);

                        } else if(data.returnCode == 1) {
                            //没有抽奖机会
                            $rewardGiftBox.find('.my-times').text('0');
                            drawCircle.tipWindowPop(tipGroupObj['nochance']);
                        }
                        else if (data.returnCode == 2) {
                            //未登陆
                            if(sourceKind.params.source=='app') {
                                location.href="/login";
                            } else {
                                $('.no-login-text',$newYearDayFrame).trigger('click');  //弹框登录
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

            //点击切换按钮
            drawCircle.PrizeSwitch();

        })(drawCircle);
    })
});


