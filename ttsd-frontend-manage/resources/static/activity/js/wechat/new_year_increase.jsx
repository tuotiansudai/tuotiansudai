require('activityStyle/wechat/new_year_increase.scss');
let commonFun = require('publicJs/commonFun');

let $newYearIncrease = $('#newYearIncrease'),
    $toGet = $('#toGet'),
    hasEnd = $('#hasEnd');

//领取加息券
$toGet.on('click',function() {
    let drew = $newYearIncrease.data('activitystatus');
    if (drew === 'NOT_START'){
        layer.msg('活动未开始');
    }else{
        location.href='/activity/new-year/draw';
    }
});

window.onload = function() {
    let drew = $newYearIncrease.data('drew');
    if(drew) {
        $('.my-layer').show();
        setTimeout(function () {
            $('.my-layer').hide();
        },3000)
        $('.btn-receive',$newYearIncrease).prop('disabled',true);
    }
    let isSuccess = $newYearIncrease.data('success');
    if(isSuccess) {
        layer.msg('领取成功');
    }
};
