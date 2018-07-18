require('mWebStyle/account/recharge.scss');
require('mWebStyle/bank_icons.scss');
require('webJs/plugins/autoNumeric');


let $cashMoneyConatiner = $('#cashMoneyConatiner'),
	$goBackIcon=$('#goBackIcon'),
	$cashMoney=$('#cashMoney');
let $limitMoney = $('#limitMoney');
let limitMoney = parseFloat(currencyToNumber($limitMoney.val()));



$goBackIcon.on('click', function (event) {
    history.go(-1);
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
$('#toCash').on('click',function (e) {
    e.preventDefault();
    var amount = parseFloat($cashMoney.autoNumeric("get"));
    if(isNaN(amount) || amount < 1){
        return;
    }
    else if(amount>limitMoney){
        layer.msg('今日充值总额已超限')
        return;
    }else {
        $('#rechargeForm').submit()
    }

})
function currencyToNumber(s){
    return s.replace(/,/g,"");
}