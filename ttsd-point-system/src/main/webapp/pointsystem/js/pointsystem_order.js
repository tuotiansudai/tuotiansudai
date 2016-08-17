require(['jquery', 'layerWrapper', 'jquery.ajax.extension'], function ($, layer) {
	$(function() {
		var $countList=$('.order-number'),
			$numText=$countList.find('.num-text'),
			$bigText = $countList.find('.total-num i'),
			$orderBtn = $('#orderBtn');

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
		changeCount()
		function changeCount(){
			$('.count-num').each(function(index, el) {
				$(this).text(parseInt($(this).attr('data-num'))*parseInt($numText.val()));
			});
		}

		$orderBtn.on('click', function (event) {//立即兑换
			event.preventDefault();
			var $self = $(this),
				idString = $self.attr('data-id'),
				typeString = $self.attr('data-type');
			$.ajax({
				url: '/pointsystem/order',
				type: 'POST',
				dataType: 'json',
				data: {
					id: idString,
					itemType: typeString,
					number: $numText.val()
				}
			})
				.done(function (data) {
					console.log(data);
					if (data.data.status) {
						location.href = '/pointsystem/record';
					} else {
						layer.msg(data.data.message);
					}
				})
				.fail(function (data) {
					layer.msg('请求失败，请重试！');
				});
		});
	});
})