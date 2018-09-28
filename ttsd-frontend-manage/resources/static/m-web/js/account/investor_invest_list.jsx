require('mWebStyle/account/investor_invest_list.scss');
let tpl = require('art-template/dist/template');
let commonFun = require('publicJs/commonFun');
require('webJs/plugins/autoNumeric');

let menuClick = require('mWebJsModule/menuClick');
let $myInvest = $('#myInvest'),
    $iconMyInvest = $('#iconMyInvest',$myInvest),
    $repayingBtn = $('#repayingBtn',$myInvest),
    $raisingBtn = $('#raisingBtn',$myInvest),
    $investListBox = $('.invest-list-box',$myInvest),
    $main = $('.main',$myInvest);

menuClick({
    pageDom: $myInvest
});

$iconMyInvest.on('click',function () {
    location.href='/m/account'
});

$(document).on('click', '.invest-item', function (e) {
   let investId = $(this).data('invest-id');
   location.href=`/m/investor/invest/${investId}/detail`;
});

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
var flagScroll=true;
let index = 1;
getList(1,'REPAYING');
$(window).load(function () {
    let myScroll = new IScroll('#wrapperOut', { mouseWheel: true ,click:true});

        myScroll.on('scrollEnd', function () {
            if(flagScroll){

            //如果滑动到底部，则加载更多数据（距离最底部10px高度）
            if ((this.y - this.maxScrollY) <= 10) {
                index++;
                if($repayingBtn.hasClass('current')){
                    getList(index,'REPAYING');
                    myScroll.refresh();
                }else if($raisingBtn.hasClass('current')){
                    getList(index,'RAISING');
                    myScroll.refresh();
                }
            }
            }
        });



    $repayingBtn.on('click',function () {
         index = 1;
        flagScroll = true;
        $('#noData').hide();
        $main.html('');
        getList(1,'REPAYING');
        myScroll.refresh();

    })
    $raisingBtn.on('click',function () {
         index = 1;
        flagScroll = true;
        $('#noData').hide();
        $main.html('');
        getList(1,'RAISING');
        myScroll.refresh();
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
                    console.log(res.data.records)
                    res.data.records.forEach(function (item,index) {
                        if(status == 'RAISING'){
                            item.achievements = [];
                        }else {
                            if(item.achievements.indexOf("MAX_AMOUNT")!=-1){
                                item.achievements = ["MAX_AMOUNT"];
                            }else if(item.achievements.indexOf("FIRST_INVEST")!=-1){
                                item.achievements = ["FIRST_INVEST"];
                            }
                        }

                    })


                    $main.append(tpl('investListTpl', res.data));
                    $('.money').autoNumeric('init');
                }else {
                    flagScroll = false;
                    if(index == 1){
                        let $noListDOM = $('<div class="noList"><div class="img"></div><button>立即出借</button>');
                        $main.append($noListDOM);

                    }else {
                        $('#noData').show();

                    }


                }
            }
        }
    )
}



$(document).on('click','.noList',function () {
    location.href = '/m/loan-list'
})



