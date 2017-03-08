let commonFun= require('publicJs/commonFun');
//登陆领红包
if($('#logout-link').length > 0){
    commonFun.useAjax({
        url: '/assign-coupon',
        type: 'POST',
        contentType: 'application/json; charset=UTF-8'
    });
}


