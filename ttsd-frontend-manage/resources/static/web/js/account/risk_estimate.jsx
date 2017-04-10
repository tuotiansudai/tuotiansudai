require('webStyle/account/account_common.scss');
require('webStyle/account/risk_estimate.scss');
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');
let $investmentBox=$('#investmentBox');
let $toResult=$('#toResult');


$investmentBox.on('click', '.problem-list dd', function(event) {
	event.preventDefault();
	$(this).addClass('active').siblings('dd').removeClass('active');
});


$toResult.on('click', function(event) {
	event.preventDefault();
	let $self=$(this);
		
});




