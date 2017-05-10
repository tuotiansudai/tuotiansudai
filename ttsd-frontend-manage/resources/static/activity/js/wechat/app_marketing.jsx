require("activityStyle/wechat/app_marketing.scss");

let $shareAppContainer = $('#shareAppContainer'),
    $shareAppSuccess = $('#shareAppSuccess');

//调整头部高度
let $marketingHeader = $('.marketing-header'),
    sw = $(window).width();


if($shareAppContainer.length) {

    require('activityJs/module/app_register');
    require("activityStyle/module/app_register_reason.scss");
    $marketingHeader.height(496 * sw/750);
    $('.red-bag',$shareAppContainer).css({'top': 286 * sw/750 });

    $('input[name="redirectToAfterRegisterSuccess"]',$shareAppContainer).val('/activity/channel/htracking/success');

} else if($shareAppSuccess.length) {

    $marketingHeader.height(526 * sw/750);
    let downloadApp=globalFun.$('#downloadApp');
    globalFun.addEventHandler(downloadApp,'click',globalFun.toExperience.bind(globalFun));

}

