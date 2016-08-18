require(['jquery'],function($){
	$(function() {
		var $countList=$('.order-number'),
			$numText=$countList.find('.num-text'),
			$bigText = $countList.find('.total-num i');

		$countList.on('click', '.low-btn', function(event) {//减号
			event.preventDefault();
			if ($bigText.text() > 0) {
				$numText.val() > 1 ? $numText.val(function (index, num) {
					return parseInt(num) - 1
				}) && changeCount() : $numText.val('1');
			}
		}).on('click', '.add-btn', function(event) {//加号
			event.preventDefault();
			if ($bigText.text() > 0) {
				$numText.val() < parseInt($bigText.text()) ? $numText.val(function (index, num) {
					return parseInt(num) + 1 
				}) && changeCount(): $numText.val($bigText.text());
			}
		});
		changeCount();
		function changeCount(){
			$('.count-num').each(function(index, el) {
				$(this).text(parseInt($(this).attr('data-num'))*parseInt($numText.val()));
			});
		}
	});
})