require(['jquery', 'layerWrapper', 'jquery.ajax.extension'], function ($, layer) {
	$(function() {
		var $pointDetail = $('#pointDetail');
		var $exchangeTip = $('#exchangeTip');
		var $countList=$('.count-list',$pointDetail),
			$numText=$countList.find('.num-text'),
			currentNum=parseInt($numText.val()), //目前已选商品的个数
			$getBtn = $('#getBtn');

		//该用户总共可以兑换商品的数量
		var myLimit = parseInt($countList.data('mylimit'));
		//该用户本月已兑换的数量
		var buyCount = parseInt($countList.data('buycount'));
		//本月该用户剩下能兑换的数量
		var myRest = myLimit-buyCount;
		$exchangeTip.find('i').text(myRest);
		if(myRest==0) {
			$getBtn.addClass('disabled');
		}

		$countList.on('click',function(event) {
			var target = event.target;
			var overplus = parseInt($countList.data('overplus'));  //剩余商品的数量

			currentNum=parseInt($numText.val());
			var compareNum = (myRest==0) ? overplus :Math.min(overplus,myRest);
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

		//立即兑换
		$getBtn.on('click', function (event) {
			event.preventDefault();
			var $self = $(this),
				idString = $self.attr('data-id'),
				typeString = $self.attr('data-type');
			if($self.hasClass('disabled')) {
				return;
			}
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