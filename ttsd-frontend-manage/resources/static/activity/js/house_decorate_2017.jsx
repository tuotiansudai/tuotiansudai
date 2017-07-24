require("activityStyle/house_decorate_2017.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let tpl = require('art-template/dist/template');



let $houseDecorateContainer = $('#houseDecorateContainer'),
    sourceKind = globalFun.parseURL(location.href);
let topimg=require('../images/2017/house-decorate/top-img.png'),
    topimgPhone=require('../images/2017/house-decorate/top-img-phone.png');

$houseDecorateContainer.find('.top-img .media-pc').attr('src',topimg).siblings('.media-phone').attr('src',topimgPhone);

let drawBtn=require('../images/2017/sport-play/draw-btn.png');
$houseDecorateContainer.find('.draw-model img').attr('src',drawBtn);


let $pointerImg = $('.draw-btn', $houseDecorateContainer);
$pointerImg.on('click', function () {
    //未登录
    $.when(commonFun.isUserLogin())
    .done(function () {
        getGift();
    })
    .fail(function () {
        if (sourceKind.params.source == 'app') {
            location.href = "/login";
        } else {
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: ['auto', 'auto'],
                content: $('#loginTip')
            });
        }
    });
});

function getGift() {
    //判断是否正在抽奖
    if ($pointerImg.hasClass('lottering')) {
        return;//不能重复抽奖
    }
    $pointerImg.addClass('lottering');
    //延迟1秒抽奖
    setTimeout(function () {

        commonFun.useAjax({
            dataType: 'json',
            url:'/activity/exercise-work/exercise-work-draw',
            data: {
                'activityCategory': 'EXERCISE_WORK_ACTIVITY'
            }
        },function(data) {
            $pointerImg.removeClass('lottering');
            if (data.returnCode == 2) {
                //判断是否需要弹框登陆
                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: ['auto', 'auto'],
                    content: $('#loginTip')
                });
            } else {
                commonFun.useAjax({
                    dataType: 'json',
                    url:'/activity/exercise-work/draw-time'
                },function(data) {
                    $houseDecorateContainer.find('.draw-time').text(data);
                });

                $('#lotteryTip').html(tpl('lotteryTipTpl', data));
                layer.open({
                  type: 1,
                  title: false,
                  closeBtn: 0,
                  area: $(window).width()>700?['450px', '230px']:['300px','155px'],
                  content: $('#lotteryTip') 
                });
            }
        });
    }, 1000);
}

$('body').on('click', '.close-tip', function(event) {
    event.preventDefault();
    layer.closeAll();
});
