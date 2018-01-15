require('activityStyle/wechat/new_year_increase.scss');
let commonFun = require('publicJs/commonFun');

let $newYearIncrease = $('#newYearIncrease'),
    $toGet = $('#toGet'),
    hasEnd = $('#hasEnd');

//领取加息券
$toGet.on('click',function() {
    let drew = $newYearIncrease.data('drew');
    if(drew) {
        layer.msg('每个用户只能领取一次哦！');
        $toGet.prop('disabled',true);
    }
    commonFun.useAjax({
        url:'/activity/celebration-coupon/draw',


    },
        function (draw) {
            if(draw) {
                layer.msg('领取成功');
            }
        }
        )
});
