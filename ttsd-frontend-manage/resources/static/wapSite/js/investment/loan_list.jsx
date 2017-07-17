require('wapSiteStyle/investment/loan_list.scss');

let menuClick = require('wapSiteJsModule/menuClick');

let $loanList = $('#loanList');
menuClick({
    pageDom:$loanList
});

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});