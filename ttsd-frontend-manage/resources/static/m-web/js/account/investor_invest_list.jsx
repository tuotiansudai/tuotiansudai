require('mWebStyle/account/investor_invest_list.scss');
let tpl = require('art-template/dist/template');
let commonFun = require('publicJs/commonFun');

let menuClick = require('mWebJsModule/menuClick');
let $myInvest = $('#myInvest'),
    $iconMyInvest = $('#iconMyInvest',$myInvest),
    $repayingBtn = $('#repayingBtn',$myInvest),
    $raisingBtn = $('#raisingBtn',$myInvest),
    $investListBox = $('.invest-list-box',$myInvest);

menuClick({
    pageDom: $myInvest
});

$iconMyInvest.on('click',function () {
    location.href='/m'
})

function isPassive() {
    var supportsPassiveOption = false;
    try {
        addEventListener("test", null, Object.defineProperty({}, 'passive', {
            get: function () {
                supportsPassiveOption = true;
            }
        }));
    } catch(e) {}
    return supportsPassiveOption;
}
var myScroll;

$(window).load(function () {
    let myScroll = new IScroll('#wrapperOut', { mouseWheel: true });
    myScroll.on('scrollEnd', function () {
        index++;
        //如果滑动到底部，则加载更多数据（距离最底部10px高度）
        if ((this.y - this.maxScrollY) <= 10) {
            if($repayingBtn.hasClass('current')){
                getList(index,'REPAYING');
                myScroll.refresh();
            }else if($raisingBtn.hasClass('current')){
                getList(index,'RAISING');
                myScroll.refresh();
            }
        }
    });
    myScroll.on('touchEnd',function () {
        alert(9)
    })
})

document.addEventListener('touchmove', function (e) { e.preventDefault(); }, isPassive() ? {
    capture: false,
    passive: false
} : false);

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

            if(res.success == true){
                if(res.data.records.length){
                   $investListBox.append(tpl('investListTpl', res.data));
                }else {
                    if(index == 1){
                        let $noListDOM = $('<div class="noList"><div class="img"></div><button>立即投资</button>');
                        $investListBox.append($noListDOM);
                    }else {
                        $('#noData').show();
                    }


                }
            }
        }
    )
}
let index = 1;
getList(1,'REPAYING');

$repayingBtn.on('click',function () {
    index = 1;
    $investListBox.html('');
    getList(1,'REPAYING');

})
$raisingBtn.on('click',function () {
    index = 1;
    $investListBox.html('');
    getList(1,'RAISING');

})

$(document).on('click','#toInvest',function () {
    location.href = '/m/loan-list'
})

