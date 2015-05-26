function IEVersion() {
    var rv = -1;
    if (navigator.appName == 'Microsoft Internet Explorer') {//IE浏览器
        var ua = navigator.userAgent;
        var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
        if (re.exec(ua) != null)
            rv = parseFloat(RegExp.$1);
        return rv;
    }
};

var clientH = $(window).height();

if (IEVersion() < 9) {
    $(function(){
        $('.warning_mask').css('display', 'block').css('height', clientH);
        $('.Warning').css('display', 'block');
    });
}