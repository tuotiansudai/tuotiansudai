require("activityStyle/module/app_register.scss");
require("activityStyle/wechat/app_marketing.scss");

let $shareAppContainer = $('#shareAppContainer');
//调整头部高度

(function() {
    let $marketingHeader = $('.marketing-header',$shareAppContainer),
        sw = $(window).width();

    $marketingHeader.height(496 * sw/750);

    $('.red-bag',$shareAppContainer).css({'top': 286 * sw/750 });
})();