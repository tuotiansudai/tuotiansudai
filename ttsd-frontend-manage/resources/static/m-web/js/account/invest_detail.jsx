require('mWebStyle/account/invest_detail.scss');
let commonFun = require('publicJs/commonFun');
let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});

let $applyTransfer = $('#applyTransfer');
$applyTransfer.on('click','a',function() {
    let $self=$(this);
    let status = $self.data('status');

    if(status =='cancel') {
        commonFun.CommonLayerTip({
            btn: ['确定','取消'],
            area:['250px', '180px'],
            content: $('#closeTransfer')
        },function() {

            //走取消转让流程
            layer.closeAll();

            let transferApplicationId=$self.data('transfer-application-id');

            commonFun.useAjax({
                url: '/transfer/application/'+transferApplicationId+'/cancel',
                type: 'POST'
            },function(data) {
                data?location.reload():layer.msg('取消失败，请重试！');
            });

        });
    }
})

