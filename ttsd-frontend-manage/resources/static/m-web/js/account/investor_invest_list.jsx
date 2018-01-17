require('mWebStyle/account/investor_invest_list.scss');
let menuClick = require('mWebJsModule/menuClick');
let $myInvest = $('#myInvest');

menuClick({
    pageDom: $myInvest
});

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});

$('.box-item', $myInvest).on('click', function () {

    location.href = 'invest-detail.ftl'
});

