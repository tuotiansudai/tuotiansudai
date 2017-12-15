require('mWebStyle/investment/project_detail.scss');
let commonFun = require('publicJs/commonFun');
let menuClick = require('mWebJsModule/menuClick');

let $projectDetail = $('#projectDetail'),
    $recordTop = $('.record-top',$projectDetail);

menuClick({
    pageDom:$projectDetail
});
// let myScroll = new IScroll('#wrapperOut', {
//     probeType: 2,
//     mouseWheel: true
// });

$recordTop.find('span').on('click',function() {

    let kind = $(this).data('kind');

    let kindObj = {
        'xianfeng':{
            title:'拓天先锋',
            content:'第一位投资的用户，<br/>获得<i>0.2%加息券，50元红包。</i>'
        },
        'biaowang':{
            title:'拓天标王',
            content:'单标累计投资最高的用户，<br/>获得<i>0.5%加息券，100元红包。</i>'
        },
        'dingying':{
            title:'一锤定音',
            content:'最后一位投资的用户，<br/>获得<i>0.2%加息券，50元红包。</i>'
        }
    };
    commonFun.CommonLayerTip({
        btn: ['我知道了'],
        area:['280px', '190px'],
        content: `<div class="record-tip-box"><span class="kaola"></span> <b class="pop-title">${kindObj[kind].title}</b> <span>${kindObj[kind].content}</span></div> `,
    },function() {
        layer.closeAll();
    })

});

