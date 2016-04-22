require(['jquery', 'pagination', 'layerWrapper'], function ($, pagination, layer) {
	$(function() {
		var $filtersList=$('.filters-list li');
		$filtersList.on('click', function(event) {
			event.preventDefault();
			var $self=$(this),
				$recordList=$('.list-container'),
				index=$self.index();
			$self.addClass('active').siblings().removeClass('active');
			$recordList.find('.record-list:eq('+index+')').addClass('active').siblings().removeClass('active');
		});
		//Agreement Read button
		var n=true;
		$(".btn-icon").on("click",function(){
			$(this).hasClass('active')?$(this).removeClass('active'):$(this).addClass('active');
		})

		$(".submit-btn").on("click",function(){
			var price=$("#price").val()
			if(price<9950||price>10000||price==""||isNaN(price)){
				$(".price-range").css("color","#ee1b1b")
			}else{
				$(".price-range").css("color","#a1a1a1")
			}
		})


	});
});
