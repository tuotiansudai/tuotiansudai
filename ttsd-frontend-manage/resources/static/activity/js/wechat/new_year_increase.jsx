require('activityStyle/wechat/new_year_increase.scss');
let commonFun = require('publicJs/commonFun');

let $newYearIncrease = $('#newYearIncrease'),
    $toGet = $('#toGet'),
    hasEnd = $('#hasEnd');

//领取加息券
$toGet.on('click',function() {
    location.href='/activity/new-year/draw';
});

window.onload = function() {
    let drew = $newYearIncrease.data('drew');
    if(drew) {
        layer.msg('每个用户只能领取一次哦！');
        $('.btn-receive',$newYearIncrease).prop('disabled',true);
    }
    let isSuccess = $newYearIncrease.data('success');
    if(isSuccess) {
        layer.msg('领取成功');
    }
};
