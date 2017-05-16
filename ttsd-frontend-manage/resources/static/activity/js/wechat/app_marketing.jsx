require("activityStyle/wechat/app_marketing.scss");

let $shareAppContainer = $('#shareAppContainer'),
    $shareAppSuccess = $('#shareAppSuccess');

//调整头部高度
let $marketingHeader = $('.marketing-header'),
    sw = $(window).width();


function toExperienceNow() {

    globalFun.categoryCodeUrl['android'] = window.commonStaticServer+'/images/apk/tuotiansudai_htracking.apk';
    let equipment=globalFun.equipment();
    if(equipment.wechat && equipment.kind=='android') {
        // 微信,并且是安卓，跳到页面
        window.location.href = "/app/download?app=htracking";
        return;
    } else {
        window.location.href =globalFun.categoryCodeUrl[equipment.kind];
    }

}

if($shareAppContainer.length) {

    let getConfig = require('activityJs/module/app_register');
    getConfig.redirectToAfterRegisterSuccess = '/activity/channel/htracking/success';
    getConfig.setRedirectUrl();

    require("activityStyle/module/app_register_reason.scss");
    $marketingHeader.height(496 * sw/750);
    $('.red-bag',$shareAppContainer).css({'top': 286 * sw/750 });

} else if($shareAppSuccess.length) {

    $marketingHeader.height(526 * sw/750);

}
globalFun.addEventHandler($('#downloadApp')[0],'click',toExperienceNow);
globalFun.addEventHandler($('#btnExperienceNow')[0],'click',toExperienceNow);

