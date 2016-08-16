require(['jquery'],function($){
	$(function() {
		var $countList=$('.count-list'),
			$lowBtn=$countList.find('.low-btn'),
			$addBtn=$countList.find('.add-btn'),
			$numText=$countList.find('.num-text'),
			$getBtn=$('#getBtn');

		$countList.on('click', '.low-btn', function(event) {//减号
			event.preventDefault();
			$numText.val()>0?$numText.val(function(index,num){return parseInt(num)-1}):$numText.val('1');
		}).on('click', '.add-btn', function(event) {//加号
			event.preventDefault();
			$numText.val(function(index,num){return parseInt(num)-1});
		});

		$getBtn.on('click', function(event) {//立即兑换
			event.preventDefault();
			/* Act on the event */
		});
	});
})