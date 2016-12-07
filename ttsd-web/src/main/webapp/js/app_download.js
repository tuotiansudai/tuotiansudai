window.onload=function() {
    var equipment=globalFun.equipment();
    alert(equipment.kind);
    if(equipment.kind=='android') {
        alert(equipment.wechat);
        if(equipment.wechat) {
            globalFun.removeClass(globalFun.$('#wechatAndroid'),'hide');
        }
        else {
            alert('ok'+globalFun.categoryCodeUrl[equipment.kind]);
            //如果是安卓手机打开页面，不管是浏览器还是支付宝，都直接下载
            window.location.href = globalFun.categoryCodeUrl[equipment.kind];
        }
    }
    else {
        globalFun.removeClass(globalFun.$('#normalFrame'),'hide');
        globalFun.$('#btnDownload').onclick=function() {
            window.location.href = globalFun.categoryCodeUrl[equipment.kind];
        }
    }
}