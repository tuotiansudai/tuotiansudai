require('webStyle/investment/loan_detail.scss');
let commonFun= require('publicJs/commonFun');
require('webJs/plugins/autoNumeric');
//投资计算器和意见反馈
require('webJsModule/red_envelope_float');
//新手体验项目
let $experienceLoan=$('#experienceLoanDetailContent');
let amountInputElement = $(".text-input-amount", $experienceLoan);
let investForm=globalFun.$('#investForm'),
    $investSubmit = $('#investSubmit'),
    minInvestVal=5000; //最小投资金额为50元，金额都是以分为单位
//体验金余额
let experienceAmount = $(investForm).find('.account-amount').data("user-balance") || 0;

amountInputElement.autoNumeric("init");

//获取输入的金额（分）
function getAmount() {
    var amount = 0;
    if (!isNaN(amountInputElement.autoNumeric("get"))) {
        amount = parseInt((amountInputElement.autoNumeric("get") * 100).toFixed(0));
    }
    return amount;
}

function checkError(amount) {
    //输入的投资金额
    let errorInfo ={
        min:'最小投资金额为50元！',
        balance:'体验金余额不足'
    };
    let error='';
    $('.error-box',$experienceLoan).hide();
    //错误检测,输入的金额必须大于50元并且小于体验金余额
    if(amount<minInvestVal || amount>experienceAmount) {
        error = (amount>experienceAmount)? errorInfo['balance'] :errorInfo['min'];
        $('.error-box',$experienceLoan).html(error).show();
    }

    return error;
}

amountInputElement.on('blur',function() {

    //计算体验金余额是否不足
    let getInvestAmount = getAmount();
    let error = checkError(getInvestAmount);
    if(error) {
        return;
    }
    commonFun.useAjax({
        url: '/calculate-expected-coupon-interest/loan/1/amount/' + getInvestAmount,
        type: 'GET',
        data: {"loanId":'1',"amount":getInvestAmount,"couponIds":''}
    },function(amount) {
        //amount单位（元）
        $(".principal-income",$experienceLoan).text(amount);

    });
}).trigger('blur');

function experInvest() {
    commonFun.useAjax({
        type:'POST',
        url: '/experience-invest',
        data:"loanId=1&amount="+getInvestAmount
    },function(response) {
        let $freeSuccess=$('#freeSuccess'),
            $detailWord=$('.detail-word',$freeSuccess);
        let data = response.data;
        $detailWord.eq(0).show().siblings('.detail-word').hide(); //默认获取没有实名认证的内容
        $('.finish-amount',$freeSuccess).html(getInvestAmount/100);
        //是否实名认证
        let accountBool=$freeSuccess.data('account');
        if(accountBool) {
            $detailWord.eq(1).show().siblings('.detail-word').hide();
        }
        if (data.status) {
            layer.open({
                type: 1,
                title: '&nbsp',
                area: ['400px'],
                content: $freeSuccess
            });
        }
    });
}
let isEstimate = $experienceLoan.data('estimate');

investForm.onsubmit=function(event) {
    event.preventDefault();
    let getInvestAmount = getAmount();
    let error = checkError(getInvestAmount);
    if(error) {
        return;
    }
    if(!isEstimate){
        //风险测评
        layer.open({
            type: 1,
            title:false,
            closeBtn: 0,
            area: ['400px', '250px'],
            shadeClose: true,
            content: $('#riskAssessment')

        });
        return false;
    }else {
        experInvest();
    }


}
$('.close-free',$experienceLoan).on('click', function (event) {
    event.preventDefault();
    layer.closeAll();
    location.reload();
});

var $cancelAssessment = $('#cancelAssessment'),
    $confirmAssessment = $('#confirmAssessment'),
    $riskTips = $('#riskTips');

$cancelAssessment.on('click', function(event) {
    event.preventDefault();
    commonFun.useAjax({
        url: '/risk-estimate',
        data: {answers: ['-1']},
        type: 'POST'
    },function(data) {
        layer.closeAll();
        experInvest();
    });

});


$confirmAssessment.on('click', function(event) {
    event.preventDefault();
    layer.closeAll();
    location.href = '/risk-estimate'
});
$riskTips.on('mouseover', function(event) {
    event.preventDefault();
    $('.risk-tip-content').show();
});
$riskTips.on('mouseout', function(event) {
    event.preventDefault();
    $('.risk-tip-content').hide();
});

