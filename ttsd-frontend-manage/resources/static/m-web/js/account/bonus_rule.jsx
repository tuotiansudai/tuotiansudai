require('mWebStyle/account/bonus_rule.scss');
// let touchSlide = require('publicJs/touch_slide');

let $recommendRule = $('#recommendRule');

//推荐送奖金
if($recommendRule.length) {
    let $shareList = $('.share-list',$(recommendRule));

    //判断是是否微信端
    let equipment = globalFun.equipment(),
        $layerDom;

    let isWechat = equipment.wechat;
    if(equipment.wechat) {
        // 微信端
        $shareList.find('span').css({
            'width': '33%'
        });
        $layerDom = $('#wechatAndroid')

    } else {
        $layerDom = $('.layer-share',$recommendRule);
    }

    $shareList.find('span:not(:first)').on('click',function ()   {
        $layerDom.show();
    })
    $('.layer-share',$recommendRule).on('touchend',function() {
        $layerDom.hide();
    })
}
