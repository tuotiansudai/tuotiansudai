require(['jquery', 'layerWrapper', 'jquery.ajax.extension'], function ($, layer) {
	$(function() {
		var $pointDetail = $('#pointDetail');
		var $countList=$('.count-list',$pointDetail),
			$numText=$countList.find('.num-text'),
			currentNum=parseInt($numText.val()), //目前已选商品的个数
			$getBtn = $('#getBtn');

		$countList.on('click',function(event) {
			var target = event.target;
			var overplus = parseInt($countList.data('overplus'));  //剩余商品的数量
			var myLimit = parseInt($countList.data('mylimit')); //本月我可以兑换商品的数量
			currentNum=parseInt($numText.val());
			var compareNum = (myLimit==0) ? overplus :Math.min(overplus,myLimit);
			if(overplus<1) {
				return;
			}
			//点击减少－
			if(/low-btn/.test(target.className) && currentNum>1) {
				$numText.val(--currentNum);

			} else if(/add-btn/.test(target.className) && currentNum < compareNum) {
				$numText.val(++currentNum);
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