require('mWebStyle/account/recharge.scss');
require('mWebStyle/bank_icons.scss');


let $cashMoneyConatiner = $('#cashMoneyConatiner'),
	$goBackIcon=$('#goBackIcon'),
	$cashMoney=$('#cashMoney');


$goBackIcon.on('click', function (event) {
    location.href="/m/account";
});

$cashMoney.on('focusout', function(event) {
	event.preventDefault();
	let $self=$(this),
		isMoney=/^\d+(?=\.{0,1}\d+$|$)/;
	if($self.val()!='' && isMoney.test($self.val())){
		$('#toCash').prop('disabled',false);
	}else{
		$('#toCash').prop('disabled',true);
		$self.val('');
	}
});