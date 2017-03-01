require('webStyle/investment/loan_detail.scss');
let commonFun= require('publicJs/commonFun');
require('webJs/plugins/autoNumeric');
//新手体验项目
let $experienceLoan=$('#experienceLoanDetailContent');
let amountInputElement = $(".text-input-amount", $experienceLoan);
let investForm=globalFun.$('#investForm');

amountInputElement.autoNumeric("init");

//获取输入的金额
function getAmount() {
    var amount = 0;
    if (!isNaN(amountInputElement.autoNumeric("get"))) {
        amount = parseInt((amountInputElement.autoNumeric("get") * 100).toFixed(0));
    }
    return amount;
}

amountInputElement.on('blur',function() {
    //计算体验金余额是否不足
    let experienceAmount = $(investForm).find('.account-amount').data("user-balance") || 0,   //体验金余额
        getInvestAmount = getAmount();  //输入的投资金额
    if(parseFloat(getInvestAmount) > experienceAmount) {
        $('.error-box',$experienceLoan).show();
    }
    else {
        $('.error-box',$experienceLoan).hide();
    }

    commonFun.useAjax({
        url: '/calculate-expected-coupon-interest/loan/1/amount/' + getInvestAmount,
        type: 'GET',
        data: {"loanId":'1',"amount":getInvestAmount,"couponIds":''}
    },function(amount) {
        $(".principal-income",$experienceLoan).text(amount);

    });
}).trigger('blur');

investForm.onsubmit=function(event) {
    event.preventDefault();
    commonFun.useAjax({
        type:'POST',
        url: '/experience-invest',
        data:$(investForm).serialize()
    },function(response) {
        let $freeSuccess=$('#freeSuccess');
        let data = response.data,
            htmlCon = $freeSuccess.find('.detail-word').eq(0); //默认获取没有实名认证的内容
        //是否实名认证
        let account=$freeSuccess.data('account');
        if(account) {
            htmlCon = $freeSuccess.find('.detail-word').eq(1);
        }
        if (data.status) {
            layer.open({
                type: 1,
                title: '&nbsp',
                area: ['400px'],
                content: htmlCon
            });
        }
    });
}
$('.close-free',$experienceLoan).on('click', function (event) {
    event.preventDefault();
    layer.closeAll();
    location.reload();
});
