require('mWebStyle/account/recharge.scss');
require('mWebStyle/bank_icons.scss');
require('webJs/plugins/autoNumeric');


let $cashMoneyConatiner = $('#cashMoneyConatiner'),
	$goBackIcon=$('#goBackIcon'),
	$cashMoney=$('#cashMoney');


$goBackIcon.on('click', function (event) {
    location.href="/m/account";
});

$cashMoney.autoNumeric("init");
$cashMoney.keyup(function () {
    var amount = parseFloat($cashMoney.autoNumeric("get"));
    if (isNaN(amount) || amount < 1) {
        $('#toCash').prop('disabled',true).text('充值金额最少为1元');
    } else {
        $('form input[name="amount"]').val(amount);
        $('#toCash').prop('disabled',false).text('确定提交');
    }
});
