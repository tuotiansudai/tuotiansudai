
window.onload=function() {
    function equipment() {
        var ua = navigator.userAgent.toLowerCase(),
            which={};
        which.wechat=false;
        var is_weixin=ua.match(/MicroMessenger/i) == "micromessenger";
        if(is_weixin) {
            which.wechat=true;
        }
        if(ua.match('android')) {
            which.kind='android';
        }
        else if(ua.match('iphone') || ua.match('ipad')) {
            which.kind='ios';
        }
        return which;
    }

    var equipment=equipment();
    globalFun.$('#btnDownload').onclick=function() {
        if(equipment.kind=='android') {
            //安卓
            window.location.href = "https://tuotiansudai.com/app/tuotiansudai.apk";
        }
        else if(equipment.kind=='ios') {
            window.location.href ="https://itunes.apple.com/cn/app/ta-tian-su-dai/id1039233966?mt=8";
        }
    }
}
