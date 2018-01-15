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
    window.onload = function() {
        let drew = $newYearIncrease.data('drew');
        if(drew) {
            layer.msg('每个用户只能领取一次哦！');
            $('$toGet',$newYearIncrease).prop('disabled',true);
        }

        let isSuccess = $newYearIncrease.data('isSuccess');
        if(isSuccess) {
            layer.msg('领取成功');
        }
    }
});
