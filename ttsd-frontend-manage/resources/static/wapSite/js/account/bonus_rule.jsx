require('wapSiteStyle/account/bonus_rule.scss');
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



// if(recommendRule) {
//
//     let $shareList = $('.share-list',$(recommendRule)),
//         $shareMenu = $('.share-menu',$(recommendRule));
//
//     let $shareSpan = $shareList.find('span'),
//         shareTotal = $shareSpan.length,
//         sharePage = Math.round(shareTotal/4),
//         currentPage = 1;
//
//     touchSlide.options.sliderDom = recommendRule;
//     touchSlide.finish = function() {
//         let direction = touchSlide.options.moveDirection;
//         //如果没有任何滑动迹象，不左处理
//         if(!this.options.moveDirection.horizontal) {
//             return;
//         }
//         if(direction.rtl) {
//             //从右到左,页面页码增加
//             if(currentPage<sharePage) {
//                 currentPage ++;
//             } else {
//                 currentPage = 1;
//             }
//             $shareList.animate({'top': -80 * (currentPage-1) + 'px'});
//
//         } else if(direction.ltr) {
//             //从左到右,页面页码减少
//             if(currentPage>1) {
//                 currentPage --;
//             } else {
//                 currentPage = sharePage;
//             }
//             $shareList.animate({'top': -80 * (currentPage-1) + 'px'});
//         }
//     }
//
//     touchSlide.init();
// }
