require(['jquery'],function($){
	$(function() {
		var $countList=$('.count-list'),
			$numText=$countList.find('.num-text'),
			$bigText = $countList.find('.total-num i'),
			$getBtn=$('#getBtn');

		$countList.on('click', '.low-btn', function(event) {//减号
			event.preventDefault();
			if ($bigText.text() > 0) {
				$numText.val() > 1 ? $numText.val(function (index, num) {
					return parseInt(num) - 1
				}) : $numText.val('1');
			}
		}).on('click', '.add-btn', function(event) {//加号
			event.preventDefault();
			if ($bigText.text() > 0) {
				$numText.val() < parseInt($bigText.text()) ? $numText.val(function (index, num) {
					return parseInt(num) + 1
				}) : $numText.val($bigText.text());
			}
		});

		$getBtn.on('click', function(event) {//立即兑换
			event.preventDefault();
			/* Act on the event */
		});
	});
})