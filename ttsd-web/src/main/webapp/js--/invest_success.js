require(['jquery'], function($) {
	var $countTime = $('.count-time'),
		i=3;
	var intervalid=setInterval(function(){
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
});