require('mWebStyle/account/investor_invest_list.scss');
let tpl = require('art-template/dist/template');
let commonFun = require('publicJs/commonFun');

let menuClick = require('mWebJsModule/menuClick');
let $myInvest = $('#myInvest'),
    $iconMyInvest = $('#iconMyInvest',$myInvest),
    $repayingBtn = $('#repayingBtn',$myInvest),
    $rasingBtn = $('#rasingBtn',$myInvest),
    $investListBox = $('.invest-list-box',$myInvest);

menuClick({
    pageDom: $myInvest
});

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});
//$('#investList').html(tpl('investListTpl', data));
$iconMyInvest.on('click',function () {
    location.href='/m'
})
function getList(index,status) {
    commonFun.useAjax({
            url:'/m/investor/invest-list-data',
            type:'get',
            data:{
                index:index,
                status:status
            }
        },
        function (res) {
            console.log(res)
            if(res.success == true){
                if(res.data.records.length){
                  //  $investListBox.html(tpl('investListTpl', res.data));
                }else {
                    if(index == 1){
                        let $noListDOM = $('<div class="noList"><div class="img"></div><button>立即投资</button>');
                        $investListBox.append($noListDOM);
                    }else {
                        console.log('没有数据了')
                    }


                }
            }
        }
    )
}
let index = 1;
getList(1,'REPAYING');
$repayingBtn.on('click',function () {
    getList(1,'REPAYING')
})

$(document).on('click','#toInvest',function () {
    location.href = '/m/loan-list'
})

