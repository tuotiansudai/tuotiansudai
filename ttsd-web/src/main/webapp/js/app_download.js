window.onload=function() {
    var equipment=globalFun.equipment();
    if(equipment.kind=='android') {
        if(equipment.wechat) {
            globalFun.removeClass(globalFun.$('#wechatAndroid'),'hide');
        }
        else {
            //如果是安卓手机打开页面，不管是浏览器还是支付宝，都直接下载
            location.href = globalFun.categoryUrl[equipment.kind];
        }
    }
    else {
        globalFun.removeClass(globalFun.$('#normalFrame'),'hide');
        globalFun.$('#btnDownload').onclick=function() {
            location.href = globalFun.categoryUrl[equipment.kind];
        }
    }
}