require('wapSiteStyle/investment/invest_detail.scss');

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});

let $applyTransfer = $('#applyTransfer');
$applyTransfer.on('click',function() {

    let status = $(this).data('status');

    if(status =='cancel') {
        commonFun.CommonLayerTip({
            btn: ['确定','取消'],
            content: $('#turnOnNoPassword')
        },function() {

        });
    }
})

