
let menuClick = require('wapSiteJsModule/menuClick');
let $myInvest = $('#myInvest');

menuClick({
    pageDom:$myInvest
});

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});

$('.box-item',$myInvest).on('click',function() {

    location.href='invest-detail.ftl'
});

