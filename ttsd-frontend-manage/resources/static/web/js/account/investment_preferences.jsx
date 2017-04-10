require('webStyle/account/account_common.scss');
require('webStyle/account/investment_preferences.scss');
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');
let $investmentBox=$('#investmentBox');


$investmentBox.on('click', '.problem-list dd', function(event) {
	event.preventDefault();
	$(this).addClass('active').siblings('dd').removeClass('active');
});





