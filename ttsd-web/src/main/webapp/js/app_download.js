window.onload=function() {

    function equipment() {
        var ua = navigator.userAgent.toLowerCase(),
            which={};
        var is_weixin=ua.match(/MicroMessenger/i) == "micromessenger";
        if(is_weixin) {
            which.wechat=true;
            if(ua.match('android')) {
                which.kind='android';
            }
            else if(ua.match('iphone') || ua.match('ipad')) {
                which.kind='weIos';
            }
        }
        else {
            which.wechat=false;

            if(ua.match('android')) {
                which.kind='android';
            }
            else if(ua.match('iphone') || ua.match('ipad')) {
                which.kind='ios';
            }
        }
        return which;
    }

    var equipment=equipment();
    if(equipment.wechat && equipment.kind=='android') {
        document.getElementById('wechatAndroid').style.display='block';
    }
    else {
        document.getElementById('normalFrame').style.display='block';
    }

    document.getElementById('btnDownload').onclick=function() {
        var categoryUrl={
            'android':'https://tuotiansudai.com/app/tuotiansudai.apk',
            'ios':'http://itunes.apple.com/us/app/id1039233966',
            'weIos':'http://a.app.qq.com/o/simple.jsp?pkgname=com.tuotiansudai.tuotianclient'
        };
        location.href = categoryUrl[equipment.kind];
    }
}