require('mWebStyle/investment/experience_amount.scss');

let $experienceAmount = $('#experienceAmount');
let menuClick = require('mWebJsModule/menuClick');

menuClick({
    pageDom:$experienceAmount
});

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});


myScroll.on("scrollEnd",function(){
    console.log('ppp');
});

// myScroll.destroy();

// setTimeout(function () {
//     myScroll.refresh();
// }, 0);

//是否可以投资拓天体验金项目

let experienceVal = $.trim($('.summary-box b',$experienceAmount).text()),
    $investButton = $('.to-invest-project',$experienceAmount);
if(experienceVal<50) {
    //不可投资
    $investButton.prop('disabled',true).text('满50元即可投资拓天体验金项目');
} else {
    $investButton.prop('disabled',false).text('投资拓天体验金项目');
}





