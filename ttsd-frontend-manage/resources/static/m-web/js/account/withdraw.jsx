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

// //格式化钱
$amount.autoNumeric('init');
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
        } else if (amount <= serviceCharge) {
            $toCashBtn.prop('disabled', true).text('提现金额需大于手续费');
        } else {
            $toCashBtn.prop('disabled', false).text('确认提交');
        }

    } else {
        $toCashBtn.prop('disabled', true).text('确认提交');
    }
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