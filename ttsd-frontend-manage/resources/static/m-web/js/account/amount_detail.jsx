
let menuClick = require('mWebJsModule/menuClick');
let $amountDetail = $('#amountDetail');

menuClick({
    pageDom:$amountDetail
});

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});