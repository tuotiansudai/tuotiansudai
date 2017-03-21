require(['jquery', 'layerWrapper', 'jquery.ajax.extension'], function ($, layer) {
	$(function() {
		var $countList=$('.count-list'),
			$numText=$countList.find('.num-text'),
			$bigText = $countList.find('.total-num i'),
			$getBtn = $('#getBtn');

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

		$getBtn.on('click', function (event) {//立即兑换
			event.preventDefault();
			var $self = $(this),
				idString = $self.attr('data-id'),
				typeString = $self.attr('data-type');
			$.ajax({
				url: '/point-shop/hasEnoughGoods',
				type: 'POST',
				dataType: 'json',
				data: {
					id: idString,
					goodsType: typeString,
					amount: $numText.val()
				}
			})
				.done(function (data) {
					console.log(data);
					if (data.data.status) {
						location.href = '/point-shop/order/' + idString + '/' + typeString + '/' + $numText.val();
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