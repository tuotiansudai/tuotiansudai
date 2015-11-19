function is_weixin() {
    var ua = navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == "micromessenger") {
        return true;
    } else {
        return false;
    }
}

function jump() {
    var u = navigator.userAgent;
    if (!is_weixin()) {
        if (u.indexOf('Android') > -1) {
            location.href = "/app/tuotiansudai.apk";
        } else if (u.indexOf('iPhone') > -1 || u.indexOf('iPad') > -1) {
            location.href = "http://itunes.apple.com/us/app/id1039233966";
        }
    } else {
        document.getElementById("wxPic").style.display = "block";
    }
}
