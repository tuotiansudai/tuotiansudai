require('mWebStyle/account/investor_invest_list.scss');
let menuClick = require('mWebJsModule/menuClick');
let $myInvest = $('#myInvest'),
    $iconMyInvest = $('#iconMyInvest',$myInvest);

menuClick({
    pageDom: $myInvest
});

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});

$iconMyInvest.on('click',function () {
    location.href='/m'
})
