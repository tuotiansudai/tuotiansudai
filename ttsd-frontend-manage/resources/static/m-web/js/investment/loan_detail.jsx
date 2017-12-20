require('mWebStyle/investment/experience_loan.scss');
require('mWebStyle/investment/loan_detail.scss');
require('mWebStyle/investment/project_detail.scss');
require('mWebStyle/investment/buy_loan.scss');
//require('mWebJsModule/anxin_agreement_pop');
require('webJs/plugins/autoNumeric');
var tpl = require('art-template/dist/template');
let $loanDetail = $('#loanDetail'),
    $iconHelp = $('.icon-help', $loanDetail);


$iconHelp.on('click',function() {
    $('.invest-refer-box',$loanDetail).toggle();
})
//点击立即投资进入购买详情页
$('#toInvest').on('click',function () {
    pushHistory();
    $loanDetail.hide();
    $applyTransfer.show();
    $projectDetail.hide();
})
//借款详情

let commonFun = require('publicJs/commonFun');
let menuClick = require('mWebJsModule/menuClick');

let $projectDetail = $('#projectDetail'),
    $recordTop = $('.record-top',$projectDetail);

menuClick({
    pageDom:$projectDetail
});

$recordTop.find('span').on('click',function() {

    let kind = $(this).data('kind');

    let kindObj = {
        'xianfeng':{
            title:'拓荒先锋',
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

//直投项目购买详情


let $applyTransfer = $('#applyTransfer'),
    $btnWapNormal = $('.btn-wap-normal',$applyTransfer),
    $amountInputElement = $('.input-amount',$applyTransfer);

$applyTransfer.find('.bg-square-box').append(commonFun.repeatBgSquare(33));

$amountInputElement.autoNumeric('init');

$applyTransfer.find('.select-coupon').on('click',  function(event) {
    event.preventDefault();
    location.href=$(this).attr('data-url');
});

function getInvestAmount() {
    var amount = 0;
    if (!isNaN($amountInputElement.autoNumeric("get"))) {
        amount = parseInt(($amountInputElement.autoNumeric("get") * 100).toFixed(0));
    }

    return amount;
}

$amountInputElement
    .on('keyup',function() {

        let value = getInvestAmount();
        if(/^(\d){4,}$/.test(value)) {
            $btnWapNormal.prop('disabled',false);
        } else {
            $btnWapNormal.prop('disabled',true);
        }
    })

if($('#investForm').length>0){
    globalFun.$('#investForm').onsubmit = function() {
        $.when(commonFun.isUserLogin())
            .done(function () {
                //提交表单
            })
            .fail(function () {
                //跳到登录页
            })

    }
}

//点击项目详情去项目详情模块

$('#to_project_detail').on('click',function () {
    $loanDetail.hide();
    $applyTransfer.hide();
    $projectDetail.show();
    pushHistory();


})
$('#apply_materal_btn').click(function () {
    $('#apply_material').show()
})
$('#btn-detail-toggle').click(function () {
    $('#apply_material').hide();
})
//交易记录ajax请求
let $boxContent = $('#box_content');
let ajaxUrl = $boxContent.data('url');
let $scroll = $boxContent.find('#scroll');
let $content = $boxContent.find('#content');
let pageNum = 1;

$('#transaction_record').on('click',function () {
    getMoreRecords();
    //交易记录滚动加载更多
    setTimeout(function () {
        var myScroll = new IScroll('#box_content', {
            probeType: 2,
            mouseWheel: true
        });
        myScroll.on('scrollEnd', function () {

            //如果滑动到底部，则加载更多数据（距离最底部10px高度）
            if ((this.y - this.maxScrollY) <= 10) {
                pageNum++;

                getMoreRecords();
                myScroll.refresh();
            }

        });
    },1000)




})

function getMoreRecords(){
    commonFun.useAjax(
        {
            url:ajaxUrl,
            type:'get',
            data:{
                index:pageNum
            }
        },
        function (res) {
            if(pageNum == 1){
                if(res.data.records.length > 0){
                    var html = tpl('recordsTpl', res.data);
                    $content.append(html)
                }else {
                    $content.html('<div class="no-records">暂无交易记录</div>')
                }
            }else {
                if(res.data.records.length > 0){
                    var html = tpl('recordsTpl', res.data);
                    $content.append(html)
                }else {
                   $('#pullUp').find('.pullUpLabel').html('没有更多数据了');
                }
            }


        }
    )
}


//监控浏览器返回事件
window.addEventListener("popstate", function(e) {
        $loanDetail.show();
        $applyTransfer.hide();
        $projectDetail.hide();
    $('#repay_plan').hide();

}, false);
function pushHistory() {
    var state = {
        title: "title",
        url: "#"
    };
    window.history.pushState(state, "title", "#");
}
//转让购买详情
//承接记录
$('#look_continue_record').click(function () {
    pushHistory();
    $('#loanDetail').hide();
    $('.buy-transfer').hide();
    $('#continue_record').show();
})
//回款计划
$('#look_repay_plan').click(function () {
    pushHistory();
    $('.buy-transfer').hide();
    $('#cotinue_record').hide();
   $('#loanDetail').hide();
   $('#repay_plan').show();
})
//立即投资
$('#to_buy_transfer').click(function () {
    pushHistory();
    $('#cotinue_record').hide();
    $('#loanDetail').hide();
    $('#repay_plan').hide();
    $('.buy-transfer').show();

    }
);
//优惠券
$('#select_coupon').on('click',function () {


})