require('activityStyle/wechat/new_year_increase.scss');

let $newYearIncrease = $('#newYearIncrease'),
    $toGet = $('#toGet'),
    hasEnd = $('#hasEnd');

//领取加息券
$toGet.on('click',function() {
    location.href='/activity/celebration-coupon/draw';
});
window.onload = function() {
    let drew = $newYearIncrease.data('drew');
    if(drew) {
        layer.msg('每个用户只能领取一次哦！');
        $toGet.prop('disabled',true);
    }
}