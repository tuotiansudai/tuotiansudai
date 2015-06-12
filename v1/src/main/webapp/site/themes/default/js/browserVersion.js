var clientH = $(window).height();

if (IEVersion() < 9.0) {
    $(function(){
        $('.warning_mask').css('display', 'block').css('height', clientH);
        $('.Warning').css('display', 'block');
    });
}

function IEVersion() {
    var agent = navigator.userAgent.toLowerCase() ;
    alert(agent);
    var regStr_ie = /msie [\d.]+;/gi ;
    var browser = '';
    var versionInfo = '';
    if(agent.indexOf("msie") > 0 && agent.indexOf("firefox") == -1 && agent.indexOf("safari") == -1 && agent.indexOf("chrome") == -1) {
        browser = agent.match(regStr_ie) ;
        alert(browser);
        versionInfo = (browser+"").replace(/[^0-9.]/ig,"");
        alert(versionInfo);
        return parseFloat(versionInfo);
    } else {
        return 9.0;
    }
}

