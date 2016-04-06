function is_weixin() {
    var ua = navigator.userAgent.toLowerCase();
    return ua.match(/MicroMessenger/i) == "micromessenger";
}

function jump() {
    var u = navigator.userAgent.toLowerCase();
    if (!is_weixin()) {
        if (u.indexOf('android') > -1) {
            location.href = "/app/tuotiansudai.apk";
        } else if (u.indexOf('iphone') > -1 || u.indexOf('ipad') > -1) {
            location.href = "http://itunes.apple.com/us/app/id1039233966";
        }
    } else {
        document.getElementById("wxPic").style.display = "block";
    }
}
