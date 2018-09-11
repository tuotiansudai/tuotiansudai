require('mWebStyle/account/withdraw.scss');
require('webJs/plugins/autoNumeric');

let $withdrawContainer = $('#withdrawContainer'),
    $amount = $('#amount');
let $cashMoney = $('#cash_money'),
    $serviceCharge = $('#service_charge'),
    $toCashBtn = $('#toCash'),
    $cashForm = $('#cashForm');
let amount = $amount.val(),
    cashMoney = $cashMoney.text(),
    serviceCharge = $serviceCharge.text();


$amount.autoNumeric('init');
let isFudianBank = $serviceCharge.data('fudianbank');
moneyCheck(isFudianBank);
// //格式化钱

$cashMoney.autoNumeric('init');
$serviceCharge.autoNumeric('init');
function getAmount(name) {
    var amount = parseFloat(name.autoNumeric("get"));
    return amount;
}
function testAmount() {
    event.preventDefault();
    $amount.autoNumeric('init');
    let $self = $(this);
    var amount = getAmount($amount);
    var cashMoney = getAmount($cashMoney);
    var serviceCharge = getAmount($serviceCharge);

    if (!isNaN(amount) && amount != '') {

        if (amount > cashMoney) {
            $toCashBtn.prop('disabled', true).text('可提现余额不足');
        } else if (amount <= 1.5) {
            $toCashBtn.prop('disabled', true).text('提现金额需大于1.5元');
        } else {
            $toCashBtn.prop('disabled', false).text('确认提交');
        }

    } else {
        $toCashBtn.prop('disabled', true).text('确认提交');
    }
    moneyCheck(isFudianBank);
}
$amount.on('keyup', function (event) {
    testAmount();
});

$('#iconBack').on('click', function () {
    history.back();
})

$toCashBtn.on('click', function (e) {
    e.preventDefault();
    var amount = getAmount($amount);
    $amount.val(amount);
    $cashForm.submit();

})

function moneyCheck(isFudianBank) {
    let amount = getAmount($amount);

    if(isFudianBank){
        $serviceCharge.html('1.00');
    }else{
        if(isNaN(amount) || amount<=50000){
            $serviceCharge.html('1.50');
        }else {
            $serviceCharge.html('5.00');
        }  
    }
}
