require('webStyle/investment/invest_success.scss');

let $countTime = $('.count-time'),
	i=3;
let intervalid=setInterval(function(){
	if (i == 0) {
		window.location.href = '/account';
		clearInterval(intervalid);
	}else{
		$countTime.each(function(index, el) {
			$(this).text(i);
		});
		i--;
	}
}, 1000);