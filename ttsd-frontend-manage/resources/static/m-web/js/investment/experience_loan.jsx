require('mWebStyle/investment/experience_loan.scss');
require('webJs/plugins/autoNumeric');
let commonFun= require('publicJs/commonFun');
let $experience_balance = $('#experience_balance');

// 关闭按钮
let $close_btn = $('#close_btn');

// 体验金余额
let experience_balance = $experience_balance.data('experience_balance') || 0;

// 起投金额
let start_investment = $experience_balance.data('start_investment') || 0;

let getInvestAmount = localStorage.getItem('getInvestAmount') || 0;
$('#investment_suc_amount').html(getInvestAmount + '元体验金');

$experience_balance.autoNumeric("init");

// 判断体验金余额为0的情况
if (experience_balance==0) {
    $experience_balance.val('');
    buttonDisabled();
    $('#submitBtn').html('请输入正确的金额');
}

// 输入投资金额按钮底部button状态变化
$experience_balance.on('keyup',() => {
    let value =  $experience_balance.val();
    // 计算预期收益
    let getInvestAmount = getAmount();
    commonFun.useAjax({
        url: '/calculate-expected-coupon-interest/loan/1/amount/' + getInvestAmount,
        type: 'GET',
        data: {"loanId":'1',"amount":getInvestAmount,"couponIds":''}
    },(amount) => {
        $("#expect-amount").html(amount);

    });
    if (value == "") {
        buttonDisabled();
        $('#submitBtn').html('请输入正确的金额');
    } else if (value < start_investment) {
        $close_btn.show();
        buttonDisabled();
        $('#submitBtn').html('输入金额应大于起投金额');
   } else if (value >= start_investment) {
        $close_btn.show();
        buttonNormal();
        $('#submitBtn').html('确认购买');
    }
});

$close_btn.on('click',() => {
    $experience_balance.val("");
    buttonDisabled();
    $('#submitBtn').html('请输入正确的金额');
});

let hash_key = location.hash;

switch (hash_key) {
    case '':
        _partOneShow();
        break;
    case '#applyTransfer':
        _partTwoShow();
        break;
    case '#investmentSuc':
        _partThreeShow();
        break;
    default:
        _partOneShow();
        break;
}

$('#goBack_experienceAmount').on('click',() => {
    history.go(-1);
});

$('#goBack_applyTransfer').on('click',() => {
    history.go(-1);
});

$('#investment_btn').on('click',() => {
    pushHistory('#applyTransfer');
});

function _partOneShow() {
    $('#experienceAmount').show();
    $('#applyTransfer').hide();
    $('#investmentSuc').hide();
};

function _partTwoShow() {
    $('#experienceAmount').hide();
    $('#applyTransfer').show();
    $('#investmentSuc').hide();
}

function _partThreeShow() {
    $('#experienceAmount').hide();
    $('#applyTransfer').hide();
    $('#investmentSuc').show();
}


function pushHistory(url) {
    let state = {title: "title", url: url};
    window.history.pushState(state, "title", url);
    location.reload();
}
$(function () {
    let bool=false;
    setTimeout(function(){ bool=true;},300);
    window.addEventListener("popstate", function(e) {
        if(bool) location.reload();
    },false);
});
function buttonDisabled() {
    $('#submitBtn').attr('disabled',true);
}
function buttonNormal() {
    $('#submitBtn').attr('disabled',false);
}

//获取输入的金额（分）
function getAmount() {
    let amount = 0;
    if (!isNaN($experience_balance.autoNumeric("get"))) {
        amount = parseInt(($experience_balance.autoNumeric("get") * 100).toFixed(0));
    }
    return amount;
}

$('#submitBtn').on('click',(event) => {
    event.preventDefault();
    let getInvestAmount = getAmount();
    if (getInvestAmount >  experience_balance) {
        let $freeSuccess=$('#freeSuccess');
        commonFun.CommonLayerTip({
            btn: ['确定'],
            area:['280px', '160px'],
            content: $freeSuccess,
        },() => {
            layer.closeAll();
        });
        return;
    }
    commonFun.useAjax({
        type:'POST',
        url: '/experience-invest',
        data:"loanId=1&amount="+getInvestAmount
    },function(response) {
        let data = response.data;
        if (data.status) {
            pushHistory('#investmentSuc');
            localStorage.setItem('getInvestAmount',$experience_balance.val());
        }
    });
});


