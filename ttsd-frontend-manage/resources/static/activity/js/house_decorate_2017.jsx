require("activityStyle/house_decorate_2017.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let tpl = require('art-template/dist/template');



let $houseDecorateContainer = $('#houseDecorateContainer'),
    sourceKind = globalFun.parseURL(location.href);
let topimg=require('../images/2017/house-decorate/top-img.png'),
    topimgPhone=require('../images/2017/house-decorate/top-img-phone.png');

let mediaPCHtml = '<img src="' + topimg + '" width="100%" class="media-pc">';
let mediaPhoneHtml = '<img src="' + topimgPhone + '" width="100%" class="media-phone">';

$houseDecorateContainer.find('.top-img').append(mediaPCHtml + mediaPhoneHtml);



let $pointerImg = $('.bag-item strong', $houseDecorateContainer);
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
            url:'/activity/house-decorate/house-decorate-draw',
            data: {
                'activityCategory': 'HOUSE_DECORATE_ACTIVITY'
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
                $('#lotteryTip').html(tpl('lotteryTipTpl', data));
                layer.open({
                  type: 1,
                  title: false,
                  closeBtn: 0,
                  area: $(window).width()>700?['405px', '350px']:['300px','255px'],
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


// 登录弹框
$houseDecorateContainer.find('.to-login').on('click', function(event) {
    event.preventDefault();
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

