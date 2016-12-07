window.onload=function() {
    var equipment=globalFun.equipment();
    if(equipment.kind=='android') {
        if(equipment.wechat) {
            globalFun.removeClass(globalFun.$('#wechatAndroid'),'hide');
        }
        else {
            //如果是安卓手机打开页面，不管是浏览器还是支付宝，都直接下载
            location.href = globalFun.categoryCodeUrl[equipment.kind];
        }
    }
    else {
        alert(globalFun.$('#normalFrame'));
        globalFun.removeClass(globalFun.$('#normalFrame'),'hide');
        alert(equipment.kind);
        globalFun.$('#btnDownload').onclick=function() {
            location.href = globalFun.categoryCodeUrl[equipment.kind];
        }
    }
}