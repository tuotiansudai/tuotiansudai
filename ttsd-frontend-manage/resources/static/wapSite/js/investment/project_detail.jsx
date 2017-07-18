require('wapSiteStyle/investment/project_detail.scss');

let menuClick = require('wapSiteJsModule/menuClick');

let $projectDetail = $('#projectDetail');

menuClick({
    pageDom:$projectDetail
});
let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});