define([], function () {
    let commonFun= require('publicJs/commonFun');
    if($('#logout-link').length > 0){
        commonFun.useAjax({
            url: '/assign-coupon',
            type: 'POST'
        });
    }
});

