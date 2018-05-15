require('mWebStyle/investment/experience_loan.scss');
require('webJs/plugins/autoNumeric');
let commonFun= require('publicJs/commonFun');
let $experience_balance = $('#experience_balance');
let isEstimate = $('#experienceAmount').data('estimate');


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
} else {
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
        $close_btn.hide();
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

// $('.go-back-container').on('click',() => {
//     history.go(-1);
// });

$('#investment_btn').on('click',() => {
    $.when(commonFun.isUserLogin())
        .done(function () {
            pushHistory('#applyTransfer');
        })
        .fail(function () {
            location.href = '/m/login'
        })

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
function experSubmit() {
    let getInvestAmount = getAmount();
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
}
$('#submitBtn').on('click',(event) => {
    event.preventDefault();
    $.when(commonFun.isUserLogin())
        .done(function () {
            let getInvestAmount = getAmount();
            if (getInvestAmount >  String(experience_balance).replace(/,/g, "") * 100) {
                $('.shade_mine').show(); // hack ios shade
                let $freeSuccess=$('#freeSuccess');
                commonFun.CommonLayerTip({
                    btn: ['确定'],
                    area:['280px', '160px'],
                    shade: false,
                    content: $freeSuccess,
                },() => {
                    $('.shade_mine').hide(); // hack ios shade
                    layer.closeAll();
                });
                return;
            }
            if(!isEstimate){
                //风险测评
                commonFun.CommonLayerTip({
                    btn: ['确定','取消'],
                    area:['280px', '230px'],
                    content: `<div class="record-tip-box"><b class="pop-title">温馨提示</b> <span>根据监管要求，出借人在出借前需进行投资偏好评估，取消则默认为保守型（可承受风险能力为最低）。是否进行评估？</span></div> `,
                },function() {
                    layer.closeAll();
                    location.href = '/m/risk-estimate'

                },function () {
                    commonFun.useAjax({
                        url: '/risk-estimate',
                        data: {answers: ['-1']},
                        type: 'POST'
                    },function(data) {
                        layer.closeAll();
                        experSubmit();
                    });
                })
                $('.layui-layer-content').css("cssText", "height:180px !important;")
                return false;
            }else {
                experSubmit()
            }


        })
        .fail(function () {
            location.href = '/m/login'
        })
});

$('#goBack_per').click(function () {
    history.go(-1)
})

if($('#closeRisk').length){
    $('#closeRisk').on('click',function () {
        $(this).parent().hide();
        $('.account-summary').css("cssText", "height:156px !important;");
    })

}
