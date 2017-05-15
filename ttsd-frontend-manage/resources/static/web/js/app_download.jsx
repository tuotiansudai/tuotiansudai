require('webStyle/app_download.scss');

window.onload=function() {

    var equipment=globalFun.equipment();

    //安卓
    if(equipment.android) {

        //这里用来做app推广用
        let location = location.href;
        let params = globalFun.parseURL(location);
        if(params.app=='htracking') {
            globalFun.categoryCodeUrl['android'] = window.commonStaticServer+'/images/apk/tuotiansudai_htracking.apk';
        }

        //安卓机 在支付宝和微信 端都需要指示在浏览器端打开下载，其他的直接下载
        if (equipment.wechat || equipment.alipay) {
            document.getElementById('wechatAndroid').style.display='block';
        }
        else {
            // 在浏览器直接打开页面下载地址
            window.location.href = globalFun.categoryCodeUrl[equipment.kind];
        }
    }
    else {
        //苹果
        document.getElementById('normalFrame').style.display='block';
        globalFun.$('#btnDownload').onclick=function() {
            window.location.href = globalFun.categoryCodeUrl[equipment.kind];
        }
    }

}